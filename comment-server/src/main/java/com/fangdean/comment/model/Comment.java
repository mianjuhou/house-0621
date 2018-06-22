package com.fangdean.comment.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Long id;
    private String content;
    private Long houseId;
    private Date createTime;
    private Integer blogId;
    private Integer type;
    private Long userId;

    private String userName;
    private String avatar;
}
