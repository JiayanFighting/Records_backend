package com.demo.records.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDO {
    public static final int USER_TYPE_NORMAL = 1;
    public static final int USER_TYPE_ADMIN = 2;
    public static final int USER_TYPE_SUPER_ADMIN = 3;

    public static final int USER_STATUS_NORMAL = 0;
    public static final int USER_STATUS_DELETED = 1;

    //primary key
    private String id;
    private String email;
    private String password;
    private String username;
    private String avatar;
    private int type; //1-admin,2-normal
    private int statusCode;

    public UserDO(String email,String username){
        this.email = email;
        this.username = username;
    }
}
