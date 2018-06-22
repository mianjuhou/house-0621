package com.fangdean.comment.controller;

import com.fangdean.comment.common.result.ListResponse;
import com.fangdean.comment.common.result.RestResponse;
import com.fangdean.comment.model.Blog;
import com.fangdean.comment.model.BlogQueryReq;
import com.fangdean.comment.service.BlogService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @RequestMapping("list")
    public RestResponse<ListResponse<Blog>> list(@RequestBody BlogQueryReq req) {
        Pair<List<Blog>, Long> pair = blogService.queryBlog(req.getBlog(), req.getLimit(), req.getOffset());
        return RestResponse.success(ListResponse.build(pair.getKey(), pair.getValue()));
    }

    @RequestMapping("one")
    public RestResponse<Blog> one(Integer id) {
        Blog blog = blogService.queryOneBlog(id);
        return RestResponse.success(blog);
    }

}
