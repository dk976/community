package com.kevin.community.controller;

import com.kevin.community.dto.AccessTokenDTO;
import com.kevin.community.dto.GithubUser;
import com.kevin.community.mapper.UserMapper;
import com.kevin.community.model.User;
import com.kevin.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String clientRedirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(clientRedirectUri);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setName(githubUser.getName());
            user.setAccountId(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarUrl());
            System.out.println(System.currentTimeMillis());
            System.out.println(user);
            userMapper.insert(user);
            response.addCookie(new Cookie( "token", token));
            request.getSession().setAttribute("user",user);
            return "redirect:/";

        }else {
            //登录失败
            return "redirect:/";
        }
    }
}
