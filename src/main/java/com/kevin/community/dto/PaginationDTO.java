package com.kevin.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private Boolean showPrevious;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();

    public void setPagintion(Integer totalCount, Integer page, Integer size) {
        Integer totalPage = 0;
        if (totalCount % size == 0 ) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        pages.add(page);
        for (int i = 0; i < 3; i++) {
            if (page-i > 0){
                pages.add(page-i,0);
            }
            if (page + i <= totalCount ) {
                pages.add(page+1);
            }
        }
        //是否展示上一页
        if (page == 1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }
        //是否展示下一页
        if (page == totalPage){
            showNext = false;
        }else {
            showNext = true;
        }
        //是否展示第一页
        if (pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
        //是否展示最后一页
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }

    }
}
