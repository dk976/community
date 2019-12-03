package com.kevin.community.controller;

import com.kevin.community.dto.CommentCreateDTO;
import com.kevin.community.dto.ResultDTO;
import com.kevin.community.exception.CustomizeErrorCode;
import com.kevin.community.model.Comment;
import com.kevin.community.model.User;
import com.kevin.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ResponseBody //通过这个注解可以接受到前端传来的json格式的数据
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
            HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>(); //用这个就可以返回给前端json的数据
//        objectObjectHashMap.put("message","成功");
        return ResultDTO.okOf();

    }
}
