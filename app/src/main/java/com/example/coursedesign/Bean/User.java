package com.example.coursedesign.Bean;

import java.io.Serializable;

public class User implements Serializable {
    private int uid;
    private String account;
    private String name;
    private String password;
    int wid;

    public User(){}
    public User(int uid, String account, String name, String password, int wid) {
        this.uid = uid;
        this.account = account;
        this.name = name;
        this.password = password;
        this.wid = wid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }
}
