package com.iuoip.chat;

import java.io.Serializable;

/**
 * 用户类
 */
public class User implements Serializable {
    private static final long serialVersionUID = -1950805966237583105L;
    private String username; //用户名
    private String password; //密码
    private String email; //邮箱

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
