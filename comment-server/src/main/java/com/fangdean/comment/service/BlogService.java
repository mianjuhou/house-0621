package com.fangdean.comment.service;

import com.fangdean.comment.common.request.LimitOffset;
import com.fangdean.comment.mapper.BlogMapper;
import com.fangdean.comment.model.Blog;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    public Pair<List<Blog>, Long> queryBlog(Blog blog, Integer limit, Integer offset) {
        List<Blog> blogs = blogMapper.selectBlog(blog, LimitOffset.build(limit, offset));
        Long count = blogMapper.selectBlogCount(blog);
        populate(blogs);
        return ImmutablePair.of(blogs, count);
    }

    public Blog queryOneBlog(Integer id) {
        Blog query = new Blog();
        query.setId(id);
        List<Blog> blogs = blogMapper.selectBlog(query, LimitOffset.build(1, 0));
        if (!blogs.isEmpty()) {
            Blog blog = blogs.get(0);
            return blog;
        }
        return null;
    }


    private void populate(List<Blog> blogs) {
        if (!blogs.isEmpty()) {
            blogs.stream().forEach(item -> {
                String stripped = Jsoup.parse(item.getContent()).text();
                item.setDigest(stripped.substring(0, Math.min(stripped.length(), 40)));
                String tags = item.getTags();
                item.getTagList().addAll(Lists.newArrayList(Splitter.on(",").split(tags)));
            });
        }
    }

}
