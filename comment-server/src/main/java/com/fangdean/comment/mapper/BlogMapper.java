package com.fangdean.comment.mapper;

import com.fangdean.comment.common.request.LimitOffset;
import com.fangdean.comment.model.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {
    List<Blog> selectBlog(@Param("blog") Blog blog, @Param("pageParams") LimitOffset limitOffset);

    Long selectBlogCount(Blog query);
}
