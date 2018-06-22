package com.fangdean.house.model;

import lombok.Data;

import java.util.Date;

@Data
public class HouseUser {
    private Long id;
    private Long houseId;
    private Long userId;
    private Date createTime;
    private Integer type;
}
