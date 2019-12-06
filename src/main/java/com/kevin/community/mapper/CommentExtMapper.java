package com.kevin.community.mapper;

import com.kevin.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}