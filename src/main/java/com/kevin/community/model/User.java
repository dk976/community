package com.kevin.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private Long accountId;
    private String token;
    private int GmtCreate;
    private int GmtModified;
    private String bio;
    private String avatarUrl;


}
