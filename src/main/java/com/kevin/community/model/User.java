package com.kevin.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long GmtCreate;
    private Long GmtModified;
    private String bio;
    private String avatarUrl;


}
