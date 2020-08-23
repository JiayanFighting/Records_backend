package com.demo.records.dao;

import com.demo.records.domain.UserDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserMapper{
    String allColumn = " `id`,`username`,`email`,`avatar`,`type`,`status` ";
    String tableName = " user_info ";
    int statusNormal = 1;
    int statusDeleted = 0;

    @Select("SELECT * FROM "+tableName+" Where email = #{email} and password = #{password}")
    UserDO login(@Param("email") String email, @Param("password") String password);

    @Insert("INSERT INTO users (email,username) VALUES (#{email},#{username}) ON DUPLICATE KEY UPDATE status_code= "+UserDO.USER_STATUS_NORMAL +"  and type = "+UserDO.USER_TYPE_NORMAL)
    int saveUser(UserDO user);

    @Select("SELECT  `email`,`username`  FROM users Where name = #{username}")
    UserDO findByName(@Param("username") String name);

    @Select("SELECT `email`,`username`,`avatar`, `type`,`status_code` FROM users WHERE email = #{email}")
    @Results(id="userMap", value={
            @Result(column="status_code", property="statusCode")
    })
    UserDO findByEmail(@Param("email") String email);

    @Select("<script>" +
            " select `username`, `email`,`avatar` from users "+
            "<where>" +
            "<if test=\"content != null and content != ''\">"+ "and ((username LIKE CONCAT(CONCAT('%', #{content}),'%')  ) or (email LIKE CONCAT(CONCAT('%', #{content}),'%')  ))" + "</if>" +
            " and status_code = "+UserDO.USER_STATUS_NORMAL +
            "</where>"+
            "</script>")
    List<UserDO> search(@Param("content") String content);

    @Update("update users set `avatar` = #{avatar}  where `email`=#{email}")
    int updateAvatar(@Param("email") String email, @Param("avatar") String avatar);

    @Update("update users set `username` = #{username}  where `email`=#{email}")
    int updateName(@Param("email") String email, @Param("username") String username);

    @Update("update users set `type` = "+UserDO.USER_TYPE_NORMAL+" , `status_code` = "+UserDO.USER_STATUS_DELETED+"  where `email`=#{email}")
    int deleteUser(@Param("email") String email);

    @Select("SELECT `type` FROM users WHERE email = #{email}")
    int getUserType(@Param("email") String email);

    @Update("update users set `type` = #{type} where `email` = #{email}")
    int updateUserType(@Param("email") String email, @Param("type") int type);

    @Insert("INSERT ignore INTO users_whitelist (email) VALUES (#{email})")
    int addWhitelist(@Param("email") String email);

    @Select("SELECT count(1) FROM users_whitelist WHERE email = #{email}")
    int inWhitelist(@Param("email") String email);

    @Select("SELECT uw.`email`, `username`,`avatar`, `type`,`status_code` FROM users_whitelist uw left join users on uw.email = users.email")
    @ResultMap("userMap")
    List<UserDO> getAllWhitelist();

    @Select("SELECT `email`, `username`,`avatar`, `type`,`status_code` FROM users where status_code = "+UserDO.USER_STATUS_NORMAL)
    @ResultMap("userMap")
    List<UserDO> getAllUsers();

    @Update("delete from users_whitelist where `email`=#{email}")
    int deleteWhitelist(@Param("email") String email);

}
