package com.demo.records.dao;

import com.demo.records.domain.TemplateDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface TemplateMapper {

    @Select("SELECT `id`,`type`,`title` ,`content`, `creator`, `create_time`,`update_time`,`status` FROM templates WHERE status = 1")
    @Results(id = "templateMap", value = {
            @Result(column = "team_id", property = "teamId"),
            @Result(column = "type", property = "type"),
            @Result(column = "creator", property = "creator"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "status", property = "status"),
    })
    List<TemplateDO> getAllTemplates();

    @Select("SELECT `id`,`team_id`,`report_type`,`title` ,`content`, `creator`, `create_time`,`update_time`,`status`"
            + " FROM templates "
            + " WHERE team_id = #{teamId} AND status = 1")
    @ResultMap("templateMap")
    List<TemplateDO> getTemplatesByTeamId(@Param("teamId") int teamId);


    @Insert("INSERT IGNORE INTO templates (`type`, `title`, `content`) values (#{type}, #{title}, #{content})")
    int save(TemplateDO templateDO);

    @Select("SELECT `id`,`team_id`,`type`,`title` ,`content`, `creator`, `create_time`,`update_time`,`status` FROM templates WHERE id = #{id}")
    @ResultMap("templateMap")
    TemplateDO findById(@Param("id") int id);

    @Select("SELECT `id`,`team_id`,`type`,`title` ,`content`, `creator`, `create_time`,`update_time`,`status` "
            + "FROM templates "
            + "WHERE team_id = #{teamId} and type = #{type} and status = 1")
    @ResultMap("templateMap")
    List<TemplateDO> findByType(@Param("teamId") int teamId, @Param("type") String type);

    @Update("<script>" +
            "update templates" +
            "<set>" +
            "<if test=\"type != null\">`type` = #{type}, </if>" +
            "<if test=\"title != null\">`title` = #{title}, </if>" +
            "<if test=\"content != null\">`content` = #{content}, </if>" +
            "<if test=\"creator != null\">`creator` = #{creator}, </if>" +
            "</set>" +
            "where id = #{id}" +
            "</script>")
    int update(TemplateDO templateDO);

    @Update("update templates set status = 0 where id = #{id}")
    int delete(int id);


}
