package com.fangdean.house.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserMsg {
    private Long id;
    private String msg;
    private Long   userId;
    private Date createTime;
    private Long   agentId;
    private Long   houseId;
    private String email;

    private String userName;
}
