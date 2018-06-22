package com.fangdean.comment.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fangdean.comment.client.UserClient;
import com.fangdean.comment.common.BeanHelper;
import com.fangdean.comment.mapper.CommentMapper;
import com.fangdean.comment.model.Comment;
import com.fangdean.comment.model.User;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 添加评论
     */
    @Transactional(rollbackFor = Exception.class)
    void addComment(Long houseId, Integer blogId, String content, Long userId, int type) {
        String key = null;
        Comment comment = new Comment();
        if (type == 1) {
            comment.setHouseId(houseId);
            key = "house_comments_" + houseId;
        } else if (type == 2) {
            comment.setBlogId(blogId);
            key = "blog_comments_" + blogId;
        }
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setType(type);
        BeanHelper.onInsert(comment);
        BeanHelper.setDefaultProp(comment, Comment.class);
        commentMapper.insert(comment);
        redisTemplate.delete(redisTemplate.keys(key + "*"));
    }

    /**
     * 添加房屋评论
     */
    public void addHouseComment(Long houseId, String content, Long userId) {
        addComment(houseId, null, content, userId, 1);
    }


    /**
     * 获取博客评论列表
     */
    public List<Comment> getBlogComments(long blogId, int size) {
        String key = "blog_comments_" + blogId + "_" + size;
        String json = redisTemplate.opsForValue().get(key);
        List<Comment> comments = JSON.parseObject(json, new TypeReference<List<Comment>>() {
        });
        if (Strings.isNullOrEmpty(json)) {
            comments = commentMapper.selectBlogComments(blogId, size);
            redisTemplate.opsForValue().set(key, JSON.toJSONString(comments));
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        }
        comments.forEach(comment -> {
            User user = userClient.getUserDetail(comment.getUserId());
            comment.setAvatar(user.getAvatar());
            comment.setUserName(user.getName());
        });
        return comments;
    }

    /**
     * 增加博客评论
     */
    public void addBlogComment(int blogId, String content, Long userId) {
        addComment(null, blogId, content, userId, 2);
    }

    /**
     * 获取房屋评论列表
     */
    public List<Comment> getHouseComments(Long houseId, Integer size) {
        String key = "house_comments" + "_" + houseId + "_" + size;
        String json = redisTemplate.opsForValue().get(key);
        List<Comment> lists = null;
        if (Strings.isNullOrEmpty(json)) {
            lists = doGetHouseComments(houseId, size);
            redisTemplate.opsForValue().set(key, JSON.toJSONString(lists));
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        } else {
            lists = JSON.parseObject(json, new TypeReference<List<Comment>>() {
            });
        }
        return lists;
    }

    /**
     * 获取数据库房屋评论
     */
    public List<Comment> doGetHouseComments(Long houseId, Integer size) {
        List<Comment> comments = commentMapper.selectComments(houseId, size);
        comments.forEach(comment -> {
            User user = userClient.getUserDetail(comment.getUserId());
            comment.setAvatar(user.getAvatar());
            comment.setUserName(user.getName());
        });
        return comments;
    }

    /**
     * 获取博客评论列表
     */
    public List<Comment> getBlogComments(Integer blogId, Integer size) {
        String key = "blog_comments" + "_" + blogId + "_" + size;
        String json = redisTemplate.opsForValue().get(key);
        List<Comment> lists = null;
        if (Strings.isNullOrEmpty(json)) {
            lists = doGetBlogComments(blogId, size);
            redisTemplate.opsForValue().set(key, JSON.toJSONString(lists));
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        } else {
            lists = JSON.parseObject(json, new TypeReference<List<Comment>>() {
            });
        }
        return lists;
    }

    /**
     * 获取数据库博客评论
     */
    private List<Comment> doGetBlogComments(Integer blogId, Integer size) {
        List<Comment> comments = commentMapper.selectBlogComments(blogId, size);
        comments.forEach(comment -> {
            User user = userClient.getUserDetail(comment.getUserId());
            comment.setAvatar(user.getAvatar());
            comment.setUserName(user.getName());
        });
        return comments;
    }

}
