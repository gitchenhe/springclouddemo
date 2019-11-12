package com.chenhe.oauthserver.access;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenhe
 * @date 2019-11-07 16:36
 * @desc
 */
@Data
public class UserInfo implements Serializable {
    private String realName;
    private int id;
    private String address;
    private boolean enable;
    private String password;
    private String account;
    private int age;
}
