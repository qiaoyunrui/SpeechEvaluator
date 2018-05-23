package com.nuc.speechevaluator.db.bean;

import java.util.Date;
import java.util.UUID;

public class User {

    public static final int USER_TYPE_NONE = 0x00;  
    public static final int USER_TYPE_NORMAL = 0x01;    //普通用户
    public static final int USER_TYPE_ADMIN = 0x02;     //管理员【可以提交题目】


    private String id;
    private int type;   //判断是否为 管理员或者普通类型
    private String username;
    private String password;

    public static User createUser(int type) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setType(type);
        return user;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public int getType() {
        return type;
    }

    public User setType(int type) {
        this.type = type;
        if (this.type != USER_TYPE_ADMIN &&
                this.type != USER_TYPE_NORMAL) {
            this.type = USER_TYPE_NORMAL;
        }
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}