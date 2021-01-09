package com.kuro.common.entity;

import lombok.ToString;

/**
 * JWT密钥的载荷对象
 */
@ToString
public class UserInfo {

    private Long id;

    private String phone;

    private String nickname;

    private String avatar;

    public UserInfo() {
    }

    public UserInfo(Long id, String phone, String nickname, String avatar) {
        this.id = id;
        this.phone = phone;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
