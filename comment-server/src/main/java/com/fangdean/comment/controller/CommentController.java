package com.fangdean.comment.controller;

import com.fangdean.comment.common.result.RestResponse;
import com.fangdean.comment.model.Comment;
import com.fangdean.comment.model.CommentReq;
import com.fangdean.comment.service.CommentService;
import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "add")
    public RestResponse<Object> leaveComment(@RequestBody CommentReq commentReq) {
        Integer type = commentReq.getType();
        if (Objects.equal(1, type)) {
            commentService.addHouseComment(commentReq.getHouseId(), commentReq.getContent(), commentReq.getUserId());
        } else {
            commentService.addBlogComment(commentReq.getBlogId(), commentReq.getContent(), commentReq.getUserId());
        }
        return RestResponse.success();
    }


    @RequestMapping("list")
    public RestResponse<List<Comment>> list(@RequestBody CommentReq commentReq) {
        Integer type = commentReq.getType();
        List<Comment> comments = null;
        if (Objects.equal(1, type)) {
            comments = commentService.getHouseComments(commentReq.getHouseId(), commentReq.getSize());
        } else {
            comments = commentService.getBlogComments(commentReq.getBlogId(), commentReq.getSize());
        }
        return RestResponse.success(comments);
    }
}
