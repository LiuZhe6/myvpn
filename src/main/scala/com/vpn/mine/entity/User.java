package com.vpn.mine.entity;

import java.io.Serializable;

/**
 * Created by coder on 17-7-2.
 */
public class User implements Serializable {

    private String uid;

    private String password;

    private String token;

    private String username;

    private String email;

    private String connections;

    //到期时间
    private String expiration;

    //免费用户或者VIP
    private String level;


    public User() {
    }

    public User(String uid, String token) {
        this.uid = uid;
        this.token = token;
    }

    public User(String uid, String password, String token) {
        this.uid = uid;
        this.password = password;
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConnections() {
        return connections;
    }

    public void setConnections(String connections) {
        this.connections = connections;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
