package com.demo.records.dao;

import com.demo.records.domain.TemplateDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface TemplateMapper {

    @Select("SELECT `id`,`team_id`,`report_type`,`theme` ,`content`, `creator_email`, `create_time`,`update_time`,`status_code` FROM templates WHERE status_code = 0")
    @Results(id="templateMap", value={
            @Result(column="team_id",property = "teamId"),
            @Result(column="report_type",property = "type"),
            @Result(column="creator_email",property = "creatorEmail"),
            @Result(column="create_time",property = "createTime"),
            @Result(column="update_time",property = "updateTime"),
            @Result(column="status_code",property = "statusCode"),
    })
    List<TemplateDO> getAllTemplates();

    @Select("SELECT `id`,`team_id`,`report_type`,`theme` ,`content`, `creator_email`, `create_time`,`update_time`,`status_code` FROM templates WHERE team_id = #{teamId} AND status_code = 0")
    @ResultMap("templateMap")
    List<TemplateDO> getTemplatesByTeamId(@Param("teamId") int teamId);


    @Insert("INSERT IGNORE INTO templates (`team_id`, `report_type`, `theme`, `content`, `creator_email`)"
                             +       "values (#{teamId}, #{type}, #{theme}, #{content}, #{creatorEmail})")
    int save(TemplateDO templateDO);

    @Select("SELECT `id`,`team_id`,`report_type`,`theme` ,`content`, `creator_email`, `create_time`,`update_time`,`status_code` FROM templates WHERE id = #{id}")
    @ResultMap("templateMap")
    TemplateDO findById(@Param("id") int id);

    @Select("SELECT `id`,`team_id`,`report_type`,`theme` ,`content`, `creator_email`, `create_time`,`update_time`,`status_code` FROM templates WHERE team_id = #{teamId} and report_type = #{type} and status_code = 0")
    @ResultMap("templateMap")
    List<TemplateDO> findByType(@Param("teamId") int teamId, @Param("type") String type);

    @Update("<script>"+
            "update templates" +
            "<set>" +
            "<if test=\"type != null\">`report_type` = #{type}, </if>" +
            "<if test=\"creatorEmail != null\">`creator_email` = #{creatorEmail}, </if>" +
            "<if test=\"content != null\">`content` = #{content}, </if>" +
            "<if test=\"theme != null\">`theme` = #{theme}, </if>" +
            "</set>" +
            "where id = #{id}"+
            "</script>")
    int update(TemplateDO templateDO);

    @Update("update templates set status_code = 1 where id = #{id}")
    int delete(int id);



}
