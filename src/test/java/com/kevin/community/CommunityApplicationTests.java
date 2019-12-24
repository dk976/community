package com.kevin.community;

import com.kevin.community.dto.QuestionQueryDTO;
import com.kevin.community.mapper.QuestionExtMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommunityApplication.class})
public class CommunityApplicationTests {
    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Test
    public void contextLoads() {
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch("");
        questionQueryDTO.setPage(1);
        questionQueryDTO.setSize(2);
        Integer integer = questionExtMapper.countBySearch(questionQueryDTO);
        System.out.println(integer);
    }

}
