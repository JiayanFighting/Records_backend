package com.demo.records.dao;

import com.demo.records.domain.DirectoryDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface DirectoryMapper {
    String allColumn = " `id`,`user_id`,`name`,`father_id`,`rank`,`description`,`create_time`,`status` ";
    String tableName = " directory ";
    String primaryKey = " id ";
    int statusNormal = 1;
    int statusDeleted = 0;

    @Select("select " + allColumn + " from " + tableName +
            " where user_id = #{userId} and status = " + statusNormal)
    List<DirectoryDO> getAll(int userId);


    @Select("select " + allColumn + " from " + tableName +
            " where father_id in " +
            "( select father_id from " + tableName +
            " where " + primaryKey + "= #{id} )" +
            "and status = " + statusNormal)
    @Results(id = "directoryMap", value = {
            @Result(column = "user_id", property = "userId"),
            @Result(column = "father_id", property = "fatherId"),
            @Result(column = "create_time", property = "createTime"),
    })
    DirectoryDO getFather(@Param("id") int id);

    @Select("select " + allColumn + " from " + tableName +
            " where father_id = #{id} and status = " + statusNormal)
    List<DirectoryDO> getChildren(int id);


    @Select("<script>" +
            " select " + allColumn +
            " from " + tableName +
            "<where>" +
            "<if test=\"id != null\">`id` = #{id}, </if>" +
            "<if test=\"user_id != null\">`user_id` = #{userId}, </if>" +
            "<if test=\"name != null\">`name` = #{name}, </if>" +
            "<if test=\"father_id != null\">`father_id` = #{fatherId}, </if>" +
            "<if test=\"description != null\">`description` = #{name}, </if>" +
            "<if test=\"create_time != null\">`create_time` = #{createTime}, </if>" +
//            "<if test=\"content != null and content != ''\">"+ "and ((team_name LIKE CONCAT(CONCAT('%', #{content}),'%')  ) or (lead_email LIKE CONCAT(CONCAT('%', #{content}),'%')  ))" + "</if>" +
            " and status = 0 " +
            "</where>" +
            " order by create_time desc " +
            "</script>")
    @ResultMap("directoryMap")
    List<DirectoryDO> findDirectory(DirectoryDO directory);


    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert ignore into directory (`user_id`,`name`,`father_id`,`rank`,`description` )"
            + "values (#{userId}, #{name}, #{fatherId}, #{rank}, #{description})")
    int create(DirectoryDO directory);

    @Update("update " + tableName + "set status = " + statusDeleted + " where id = #{id}")
    int delete(int id);

    @Update("<script>" +
            "update " + tableName +
            "<set>" +
            "<if test=\"name != null\">`name` = #{name}, </if>" +
            "<if test=\"father_id != null\">`father_id` = #{fatherId}, </if>" +
            "<if test=\"rank != null\">`rank` = #{rank}, </if>" +
            "<if test=\"description != null\">`description` = #{description}, </if>" +
            "</set>" +
            "where `id` = #{id}" +
            "</script>")
    int update(DirectoryDO directory);


}


