package com.kevin.community.model;

public class User {
    private String name;
    private Long accountId;
    private String token;
    private int GmtCreate;
    private int GmtModified;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getGmtCreate() {
        return GmtCreate;
    }

    public void setGmtCreate(int gmtCreate) {
        GmtCreate = gmtCreate;
    }

    public int getGmtModified() {
        return GmtModified;
    }

    public void setGmtModified(int gmtModified) {
        GmtModified = gmtModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
