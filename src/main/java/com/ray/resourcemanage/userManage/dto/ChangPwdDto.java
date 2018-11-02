package com.ray.resourcemanage.userManage.dto;

public class ChangPwdDto {
    private String username;
    private String password;
    private String newPwd;
    private String newPwdSec;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getNewPwdSec() {
        return newPwdSec;
    }

    public void setNewPwdSec(String newPwdSec) {
        this.newPwdSec = newPwdSec;
    }
}