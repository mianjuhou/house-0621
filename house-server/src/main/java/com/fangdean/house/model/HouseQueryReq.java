package com.fangdean.house.model;

import lombok.Data;

@Data
public class HouseQueryReq {
    private House query;

    private Integer limit;

    private Integer offset;
}
