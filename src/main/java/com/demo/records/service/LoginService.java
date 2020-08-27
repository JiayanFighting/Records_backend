package com.demo.records.service;

import com.demo.records.domain.UserDO;

public interface LoginService {

//    UserDO login(String email,String username);

    UserDO login(String email, String password);

    int register(UserDO user);

    void saveUser(UserDO user);

    boolean inWhitelist(String email);
}
