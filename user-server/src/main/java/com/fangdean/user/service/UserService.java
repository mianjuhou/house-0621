package com.fangdean.user.service;

import com.alibaba.fastjson.JSON;
import com.fangdean.user.common.BeanHelper;
import com.fangdean.user.common.HashUtils;
import com.fangdean.user.common.JwtHelper;
import com.fangdean.user.exception.UserException;
import com.fangdean.user.mapper.UserMapper;
import com.fangdean.user.model.User;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Value("${file.prefix}")
    private String imgPrefix;

    /**
     * 带缓存的获取用户信息
     */
    public User getUserById(Long id) {
        String key = "user:" + id;
        String json = redisTemplate.opsForValue().get(key);
        User user = null;
        if (Strings.isNullOrEmpty(json)) {
            user = userMapper.selectById(id);
            user.setAvatar(imgPrefix + user.getAvatar());
            String string = JSON.toJSONString(user);
            redisTemplate.opsForValue().set(key, string);
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        } else {
            user = JSON.parseObject(json, User.class);
        }
        return user;
    }

    /**
     * 根据条件查询用户列表
     */
    public List<User> getUserByQuery(User user) {
        List<User> users = userMapper.select(user);
        users.forEach(u -> {
            u.setAvatar(imgPrefix + u.getAvatar());
        });
        return users;
    }

    /**
     * 注册
     */
    public boolean addAccount(User user, String enableUrl) {
        user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        BeanHelper.onInsert(user);
        userMapper.insert(user);
        registerNotify(user.getEmail(), enableUrl);
        return true;
    }

    //发送注册激活邮件
    private void registerNotify(String email, String enableUrl) {
        String randomKey = HashUtils.hashString(email) + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey, email);
        redisTemplate.expire(randomKey, 1, TimeUnit.HOURS);
        String content = enableUrl + "?key=" + randomKey;
        mailService.sendSimpleMail("房产平台激活邮件", content, email);
    }

    /**
     * 设置账户为激活状态
     */
    public boolean enable(String key) {
        String email = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(email)) {
            throw new UserException(UserException.Type.USER_NOT_FOUND, "无效的key");
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        userMapper.update(updateUser);
        return true;
    }

    /**
     * 校验用户名密码、生成token并返回用户对象
     */
    public User auth(String email, String passwd) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(passwd)) {
            throw new UserException(UserException.Type.USER_AUTH_FAIL, "User Auth Fail");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswd(HashUtils.encryPassword(passwd));
        user.setEnable(1);
        List<User> users = getUserByQuery(user);
        if (!users.isEmpty()) {
            User retUser = users.get(0);
            onLogin(retUser);
            return retUser;
        }
        throw new UserException(UserException.Type.USER_AUTH_FAIL, "User Auth Fail");
    }

    //生成Token
    private void onLogin(User user) {
        String token = JwtHelper.genToken(ImmutableMap.of("email", user.getEmail(), "name", user.getName(), "ts", Instant.now().getEpochSecond() + ""));
        renewToken(token, user.getEmail());
        user.setToken(token);
    }

    //刷新Token缓存时间
    private String renewToken(String token, String email) {
        redisTemplate.opsForValue().set(email, token);
        redisTemplate.expire(email, 30, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 根据Token获取用户信息
     */
    public User getLoginedUserByToken(String token) {
        Map<String, String> map = JwtHelper.verifyToken(token);
        String email = map.get("email");
        Long expire = redisTemplate.getExpire(email);
        if (expire > 0L) {
            renewToken(token, email);
            User user = getUserByEmail(email);
            user.setToken(token);
            return user;
        }
        throw new UserException(UserException.Type.USER_NOT_LOGIN, "user not login");
    }

    //根据邮箱获取用户信息
    private User getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        List<User> list = getUserByQuery(user);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        throw new UserException(UserException.Type.USER_NOT_FOUND, "User not found for " + email);
    }

    /**
     * 删除缓存用户信息，实现登出
     */
    public void invalidate(String token) {
        Map<String, String> map = JwtHelper.verifyToken(token);
        redisTemplate.delete(map.get("email"));
    }

    /**
     * 更新个人信息
     */
    public User updateUser(User user) {
        if (user.getEmail() == null) {
            return null;
        }
        if (!Strings.isNullOrEmpty(user.getPasswd())) {
            user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        }
        userMapper.update(user);
        return userMapper.selectByEmail(user.getEmail());
    }

    /**
     * 发起重置密码请求
     */
    public void resetNotify(String email, String url) {
        String randomKey = "reset_" + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey, email);
        redisTemplate.expire(randomKey, 1, TimeUnit.HOURS);
        String content = url + "?key=" + randomKey;
        mailService.sendSimpleMail("房产平台重置密码邮件", content, email);
    }

    /**
     * 进行重置密码操作
     */
    public User reset(String key, String passwd) {
        String email = getResetKeyEmail(key);
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setPasswd(HashUtils.encryPassword(passwd));
        userMapper.update(updateUser);
        return getUserByEmail(key);
    }

    //获取缓存中的邮箱
    public String getResetKeyEmail(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
