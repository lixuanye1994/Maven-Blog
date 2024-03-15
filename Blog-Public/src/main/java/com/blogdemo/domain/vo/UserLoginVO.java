package com.blogdemo.domain.vo;

public class UserLoginVO {
    private String token;
    private UserinfoVO userInfo;

    public UserLoginVO(String token, UserinfoVO userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserinfoVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserinfoVO userInfo) {
        this.userInfo = userInfo;
    }
}
