package com.demo.records.dao;

import com.demo.records.domain.TeamSprintDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface TeamSprintMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert ignore into team_sprint (`team_id`, `type`, `sprint`, `begin_time`, `end_time`)"
            + "values (#{teamId}, #{type}, #{sprint}, #{beginTime}, #{endTime})")
    int create(TeamSprintDO teamSprintDO);

    @Update("update team_sprint set status_code = 1 where id = #{id}")
    int delete(int id);

    @Update("<script>"+
            "update team_sprint " +
            "<set>" +
            "<if test=\"sprint != null and sprint > 0 \">`sprint` = #{sprint}, </if>" +
            "<if test=\"beginTime != null\">`begin_time` = #{beginTime}, </if>" +
            "<if test=\"endTime != null\">`end_time` = #{endTime}, </if>" +
            "<if test=\"statusCode != null\">`status_code` = #{statusCode}, </if>" +
            "</set>" +
            "where `id` = #{id}" +
            "</script>")
    int update(TeamSprintDO teamSprintDO);

    @Select("<script>" +
            "select `id`,`team_id`, `type`, `sprint`, `begin_time`, `end_time`,`status_code` " +
            " from team_sprint "+
            "<where>" +
            "<if test=\"type != null and type != ''\">"+ "and type = #{type} " + "</if>" +
            "<if test=\"sprint != null and sprint != ''\">"+ "and sprint = #{sprint} " + "</if>" +
            "<if test=\"beginTime != null and beginTime != ''\">"+ "and begin_time &gt;= #{beginTime} " + "</if>" +
            "<if test=\"endTime != null and endTime != ''\">"+ "and endTime &lt;= #{endTime} " + "</if>" +
            " and team_id = #{teamId} "+
            " and status_code = 0  " +
            "</where>"+
            "</script>")
    @Results(id="teamSprintMap", value={
            @Result(column="team_id", property="teamId"),
            @Result(column="begin_time", property="beginTime"),
            @Result(column="end_time", property="endTime"),
            @Result(column="status_code", property="statusCode")
    })
    List<TeamSprintDO> list(Map<String, Object> map);


    @Select("<script>" +
            "select `id`,`team_id`, `type`, `sprint`, `begin_time`, `end_time`,`status_code` " +
            " from team_sprint "+
            "<where>" +
            "<if test=\"type != null and type != ''\">"+ "and type = #{type} " + "</if>" +
            "<if test=\"sprint != null and sprint != ''\">"+ "and sprint = #{sprint} " + "</if>" +
            " and team_id = #{teamId} "+
            " and status_code = 0  " +
            " and begin_time &lt;= #{curTime} " +
            " and end_time &gt;= #{curTime} " +
            "</where>"+
            "</script>")
    @ResultMap("teamSprintMap")
    List<TeamSprintDO> getCurrentSprints(Map<String, Object> map);

    @Select("select `id`,`team_id`, `type`, `sprint`, `begin_time`, `end_time`,`status_code` " +
            " from team_sprint " +
            " where team_id = #{teamId} and sprint = #{sprint} and type = #{type} ")
    @ResultMap("teamSprintMap")
    TeamSprintDO getSprintUnique(@Param("teamId") int teamId, @Param("sprint") int sprint, @Param("type") String type);
}


