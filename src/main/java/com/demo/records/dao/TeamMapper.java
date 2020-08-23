package com.demo.records.dao;

import com.demo.records.domain.TeamDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface TeamMapper {

    @Select("select `id`,`team_name`,`team_email`,`team_desc`,`lead_email`,`cc_list`,`create_time`,`update_time`,`status_code` from teams where id = #{id} and status_code = 0")
    @Results(id="teamMap", value={
            @Result(column="team_name", property="teamName"),
            @Result(column="team_email", property="teamEmail"),
            @Result(column="team_desc", property="teamDesc"),
            @Result(column="lead_email", property="leadEmail"),
            @Result(column="cc_list", property="ccList"),
            @Result(column="create_time", property="createTime"),
            @Result(column="update_time", property="updateTime"),
            @Result(column="status_code", property="statusCode"),
    })
    TeamDO getTeamInfo(@Param("id") int id);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert ignore into teams (`team_name`, `team_email`, `team_desc`, `lead_email`,`cc_list`)"
            + "values (#{teamName}, #{teamEmail}, #{teamDesc}, #{leadEmail}, #{ccList})")
    int create(TeamDO team);


    @Update("<script>"+
            "update teams " +
            "<set>" +
            "<if test=\"teamName != null\">`team_name` = #{teamName}, </if>" +
            "<if test=\"teamEmail != null\">`team_email` = #{teamEmail}, </if>" +
            "<if test=\"teamDesc != null\">`team_desc` = #{teamDesc}, </if>" +
            "<if test=\"leadEmail != null\">`lead_email` = #{leadEmail}, </if>" +
            "<if test=\"ccList != null\">`cc_list` = #{ccList}, </if>" +
            "</set>" +
            "where `id` = #{id}" +
            " and `lead_email` = #{leadEmail}"+
            "</script>")
    int update(TeamDO team);

    @Update("update teams set status_code = 1 where id = #{id}")
    int delete(int id);


    @Select("select `id`,`team_name`,`team_email`,`team_desc`,`lead_email`,`cc_list`,`create_time`,`update_time`,`status_code` from teams " +
            "where lead_email = #{leadEmail} and status_code = 0 ")
    @ResultMap("teamMap")
    List<TeamDO> getCreatedTeamList(@Param("leadEmail") String leadEmail);

    @Select("select `id`,`team_name`,`team_email`,`team_desc`,`lead_email`,`cc_list`,`create_time`,`update_time`,t1.`status_code`" +
            " from teams t1, team_relationships t2" +
            " where id = team_id and t2.user_email = #{userEmail} " +
            " and t1.status_code = 0" +
            " and t2.status_code = 0")
    @ResultMap("teamMap")
    List<TeamDO> getJoinedTeamList(@Param("userEmail") String userEmail);


    @Select("<script>" +
            " select `id`,`team_name`,`team_email`,`team_desc`,`lead_email`,`cc_list`,`create_time`,`update_time`,`status_code` " +
            " from teams "+
            "<where>" +
            "<if test=\"content != null and content != ''\">"+ "and ((team_name LIKE CONCAT(CONCAT('%', #{content}),'%')  ) or (lead_email LIKE CONCAT(CONCAT('%', #{content}),'%')  ))" + "</if>" +
            " and status_code = 0 "+
            "</where>"+
            " order by create_time desc " +
            "</script>")
    @ResultMap("teamMap")
    List<TeamDO> search(@Param("content") String content);

    @Select("<script>" +
            " select `id`,`team_name`,`team_email`,`team_desc`,`lead_email`,`cc_list`,`create_time`,`update_time`,`status_code` " +
            " from teams "+
            "<where>" +
            "<if test=\"content != null and content != ''\">"+ "and ((team_name LIKE CONCAT(CONCAT('%', #{content}),'%')  ) or (lead_email LIKE CONCAT(CONCAT('%', #{content}),'%')  ))" + "</if>" +
            " and status_code = 0 "+
            " and id not in (select team_id as id from team_relationships where user_email = #{userEmail} and status_code = 0 ) "+
            "</where>"+
            " order by create_time desc " +
            "</script>")
    @ResultMap("teamMap")
    List<TeamDO> searchNotJoined(@Param("content") String content, @Param("userEmail") String userEmail);
}


