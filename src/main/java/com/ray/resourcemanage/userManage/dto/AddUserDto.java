package com.ray.resourcemanage.userManage.dto;

public class AddUserDto {
    private String username;
    private boolean adminAuth;
    private boolean userAuth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getAdminAuth() {
        return adminAuth;
    }

    public void setAdminAuth(boolean adminAuth) {
        this.adminAuth = adminAuth;
    }

    public boolean getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(boolean userAuth) {
        this.userAuth = userAuth;
    }
}