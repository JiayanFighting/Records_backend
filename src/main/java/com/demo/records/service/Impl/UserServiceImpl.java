package com.demo.records.service.Impl;

import com.demo.records.dao.UserMapper;
import com.demo.records.domain.UserDO;
import com.demo.records.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public List<UserDO> search(String content) {
        return userMapper.search(content);
    }

    @Override
    public UserDO findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public int updateAvatar(String email, String avatar) {
        return userMapper.updateAvatar(email,avatar);
    }

    @Override
    public int updateName(String email, String username) {
        return userMapper.updateName(email,username);
    }

    @Override
    public int addWhitelist(String email) {
        return userMapper.addWhitelist(email);
    }

    @Override
    public int addWhitelist(UserDO user) {
        return userMapper.saveUser(user);
    }

    @Override
    public int deleteWhitelist(String email) {
        return userMapper.deleteUser(email);
    }

    @Override
    public List<UserDO> getAllWhitelist() {
        return userMapper.getAllWhitelist();
    }

    @Override
    public List<UserDO> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public int setAdmin(String email) {
        return userMapper.updateUserType(email,UserDO.USER_TYPE_ADMIN);
    }

    @Override
    public int cancelAdmin(String email) {
        return userMapper.updateUserType(email,UserDO.USER_TYPE_NORMAL);
    }

    @Override
    public boolean isSuperAdmin(String email) {
        return findByEmail(email).getType() == UserDO.USER_TYPE_SUPER_ADMIN;
//        for (String admin:SUPER_ADMIN){
//            if (admin.equals(email)){
//                return true;
//            }
//        }
//        return false;
    }

    @Override
    public String getUsernameFromEmail(String email) {
        String username = email.split("@")[0];
        username = username.replace('.',' ');
        return username;
    }
}
