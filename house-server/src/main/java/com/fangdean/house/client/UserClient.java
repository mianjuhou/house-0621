package com.fangdean.house.client;

import com.fangdean.house.common.result.RestResponse;
import com.fangdean.house.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-server")
public interface UserClient {

    @RequestMapping("/agency/agentDetail")
    RestResponse<User> agentDetail(Long id);

}
