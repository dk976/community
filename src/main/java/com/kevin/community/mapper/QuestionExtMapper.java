package com.kevin.community.mapper;

import com.kevin.community.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}
