package com.fangdean.comment.mapper;

import com.fangdean.comment.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    int insert(Comment comment);

    List<Comment> selectComments(@Param("houseId") long houseId, @Param("size") int size);

    List<Comment> selectBlogComments(@Param("blogId") long blogId, @Param("size") int size);

}
