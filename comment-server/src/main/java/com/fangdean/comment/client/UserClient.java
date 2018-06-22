package com.fangdean.comment.client;

import com.fangdean.comment.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-server")
public interface UserClient {

    @RequestMapping("/user/getById")
    User getUserDetail(Long userId);

}
