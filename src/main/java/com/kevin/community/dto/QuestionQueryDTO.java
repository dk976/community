package com.kevin.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private Integer page;
    private Integer size;
    private String search;
}
