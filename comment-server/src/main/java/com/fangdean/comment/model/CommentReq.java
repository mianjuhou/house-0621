package com.fangdean.comment.model;

import lombok.Data;

@Data
public class CommentReq {
    private Long userId;
    private Long houseId;
    private Integer blogId;
    private String content;
    private Integer type; //1-房产，2-博客百科
    private Integer size; //获取多少评论
}
