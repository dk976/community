package com.kevin.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private int id;
    private String bio;
    private String avatarUrl;
}
