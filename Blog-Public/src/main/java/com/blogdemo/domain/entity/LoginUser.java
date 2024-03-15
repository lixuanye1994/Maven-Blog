package com.blogdemo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 *  登录用户容器
 * @author VMlee
 */
@JsonIgnoreProperties(ignoreUnknown = true) //这个注解忽略了 没有password字段仍然setPassword getPassword 不能序列化的问题
public class LoginUser implements UserDetails {
    /**
     * 把User类变成容器的成员变量
     */
    private User user;

    public LoginUser(User user) {
        this.user = user;
    }
    public LoginUser(){
    }
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
