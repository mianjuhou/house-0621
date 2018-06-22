package com.fangdean.user.controller;

import com.fangdean.user.common.result.RestResponse;
import com.fangdean.user.model.User;
import com.fangdean.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("getById")
    public RestResponse<User> getUserById(Long id){
        User user = userService.getUserById(id);
        return RestResponse.success(user);
    }

    @RequestMapping("getList")
    public RestResponse<List<User>> getUserList(@RequestBody User user){
        List<User> users = userService.getUserByQuery(user);
        return RestResponse.success(users);
    }

    @RequestMapping("add")
    public RestResponse<User> add(@RequestBody User user){
        userService.addAccount(user,user.getEnableUrl());
        return RestResponse.success();
    }

    @RequestMapping("enable")
    public RestResponse<Object> enable(String key){
        userService.enable(key);
        return RestResponse.success();
    }

    @RequestMapping("auth")
    public RestResponse<User> auth(@RequestBody User user){
        User finalUser = userService.auth(user.getEmail(),user.getPasswd());
        return RestResponse.success(finalUser);
    }

    @RequestMapping("get")
    public RestResponse<User> getUser(String token){
        User finalUser = userService.getLoginedUserByToken(token);
        return RestResponse.success(finalUser);
    }

    @RequestMapping("logout")
    public RestResponse<Object> logout(String token){
        userService.invalidate(token);
        return RestResponse.success();
    }

    @RequestMapping("update")
    public RestResponse<User> update(@RequestBody User user){
        User updateUser = userService.updateUser(user);
        return RestResponse.success(updateUser);
    }

    @RequestMapping("reset")
    public RestResponse<User> reset(String key ,String password){
        User updateUser = userService.reset(key,password);
        return RestResponse.success(updateUser);
    }

    @RequestMapping("getKeyEmail")
    public RestResponse<String> getKeyEmail(String key){
        String  email = userService.getResetKeyEmail(key);
        return RestResponse.success(email);
    }

    @RequestMapping("resetNotify")
    public RestResponse<User> resetNotify(String email,String url){
        userService.resetNotify(email,url);
        return RestResponse.success();
    }

}
