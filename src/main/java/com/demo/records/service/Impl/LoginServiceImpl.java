package com.demo.records.service.Impl;

import com.demo.records.dao.UserMapper;
import com.demo.records.domain.UserDO;
import com.demo.records.service.LoginService;
import com.demo.records.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserService userService;

//    @Override
//    public UserDO login(String email,String username) {
//        if (inWhitelist(email) || userService.isSuperAdmin(email)) {
//            UserDO user = userMapper.findByEmail(email);
//            if (user != null && user.getEmail().equals(email) && user.getStatusCode() ==  UserDO.USER_STATUS_NORMAL){
//                return user;
//            }else {
//                saveUser(new UserDO(email,username));
//                if (userService.isSuperAdmin(email)){
//                    userMapper.updateUserType(email,UserDO.USER_TYPE_ADMIN);
//                }
//                return new UserDO(email,username,"",UserDO.USER_TYPE_NORMAL,0);
//            }
//        }
//        return new UserDO(email,username,"",-1,0);
//    }

    @Override
    public UserDO login(String email,String password) {
        return userMapper.login(email,password);
    }

    @Override
    public int register(UserDO user) {
        return userMapper.register(user);
    }

    @Override
    public void saveUser(UserDO user) {
        userMapper.saveUser(user);
    }

    @Override
    public boolean inWhitelist(String email) {
        return userMapper.inWhitelist(email) > 0;
    }
}
