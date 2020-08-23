package com.demo.records.dao;

import com.demo.records.domain.UserDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface TeamRelationshipMapper {

    @Update("update team_relationships set status_code = 1 where team_id = #{teamId}")
    int deleteAll(@Param("teamId") int teamId);

    @Insert("insert into team_relationships (`team_id`, `user_email`)"
            + "values (#{teamId}, #{userEmail}) ON DUPLICATE KEY UPDATE status_code=0 ")
    int join(@Param("teamId") int teamId, @Param("userEmail") String userEmail);

    @Update("update team_relationships set status_code = 1 where team_id = #{teamId} and user_email = #{userEmail}")
    int remove(@Param("teamId") int teamId, @Param("userEmail") String userEmail);

    @Select("select `email`, `username`, `avatar` " +
            " from team_relationships t, users u " +
            "where u.email = t.user_email and team_id = #{teamId} and t.status_code = 0")
    List<UserDO> getTeamMembers(@Param("teamId") int teamId);


}


