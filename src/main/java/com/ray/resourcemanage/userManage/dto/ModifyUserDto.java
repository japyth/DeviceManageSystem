package com.ray.resourcemanage.userManage.dto;

public class ModifyUserDto {
    private Integer userId;
    private String username;
    private boolean adminAuth;
    private boolean userAuth;

    public boolean isAdminAuth() {
        return adminAuth;
    }

    public boolean isUserAuth() {
        return userAuth;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}