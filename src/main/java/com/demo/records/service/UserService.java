package com.demo.records.service;

import com.demo.records.domain.UserDO;

import java.util.List;

public interface UserService {

    public static final String[] SUPER_ADMIN = {"jiayan.huang@dchdc.net"};

    int updateUserAvatar(int userId, String avatar);


    List<UserDO> search(String content);

    UserDO findByEmail(String email);

    int updateAvatar(String email, String avatar);

    int updateName(String email, String username);

    int addWhitelist(String email);

    int addWhitelist(UserDO user);

    int deleteWhitelist(String email);

    List<UserDO> getAllWhitelist();

    List<UserDO> getAllUsers();

    int setAdmin(String email);

    int cancelAdmin(String email);

    boolean isSuperAdmin(String email);

    String getUsernameFromEmail(String email);
}
