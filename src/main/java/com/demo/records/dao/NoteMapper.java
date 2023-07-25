package com.demo.records.dao;

import com.demo.records.domain.NoteDO;
import com.demo.records.domain.req.NoteQueryReq;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface NoteMapper {

    String allColumn = " `id`,`user_id`,`directory`,`type`,`tags`,`title`,`content`,`cover`,`thumbUp`,`star`,`create_time`,`update_time`,`status` ";
    String tableName = " notes ";
    int statusNormal = 1;
    int statusDeleted = 0;

    @Select("<script>" +
            "select " + allColumn +
            " from " + tableName +
            "<where>" +
            "<if test=\"userId != null and userId != ''\">" + "and user_id = #{userId} " + "</if>" +
            "<if test=\"directory != null and directory != ''\">" + "and directory = #{directory} " + "</if>" +
            "<if test=\"type != null and type != ''\">" + "and type = #{type} " + "</if>" +
            "<if test=\"tags != null and tags != ''\">" + "and tags = #{tags} " + "</if>" +
            "<if test=\"beginTime != null and beginTime != ''\">" + "and update_time &gt;= #{beginTime} " + "</if>" +
            "<if test=\"endTime != null and endTime != ''\">" + "and update_time &lt;= #{endTime}" + "</if>" +
            " and status = " + statusNormal + " " +
            "</where>" +
            " order by create_time desc " +
            "</script>")
    @Results(id = "noteMap", value = {
            @Result(column = "user_id", property = "userId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<NoteDO> list(Map<String, Object> map);

    @Select("select `id`,`directory`,`title`" +
            " from " + tableName +
            " where `user_id` = #{userId} and `directory` = #{directory} and status = " + statusNormal + " " +
            " order by create_time desc ")
    @ResultMap("noteMap")
    List<NoteDO> getListByDirectory(@Param("userId") int userId, @Param("directory") int directory);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO notes (`user_id`,`directory`, `type`, `tags`,`title`, `content`,`cover`,`thumbUp`,`star`)"
            + "values (#{userId},#{directory}, #{type}, #{tags}, #{title},#{content},#{cover},#{thumbUp},#{star})")
    int save(NoteDO note);

    @Update("update " + tableName + "set `status` = " + statusDeleted + " where id=#{id} ")
    int delete(@Param("id") int id);

    @Update("<script>" +
            "update " + tableName +
            "<set>" +
            "<if test=\"type != null\">`type` = #{type}, </if>" +
            "<if test=\"tags != null\">`tags` = #{tags}, </if>" +
            "<if test=\"title != null\">`title` = #{title}, </if>" +
            "<if test=\"content != null\">`content` = #{content}, </if>" +
            "</set>" +
            "where `id` = #{id} and `status` = " + statusNormal + " " +
            "</script>")
    int update(NoteDO note);

    @Select("select " + allColumn +
            " from " + tableName +
            " where id = #{id} ")
    NoteDO getNoteById(int id);


    @Select("<script>" +
            "select tags " +
            " from " + tableName +
            "<where>" +
            "<if test=\"userId != null and userId != ''\">" + "and user_id = #{userId} " + "</if>" +
            "<if test=\"directory != null and directory != ''\">" + "and directory = #{directory} " + "</if>" +
            "<if test=\"type != null and type != ''\">" + "and type = #{type} " + "</if>" +
            "<if test=\"tags != null and tags != ''\">" + "and tags = #{tags} " + "</if>" +
            "<if test=\"beginTime != null and beginTime != ''\">" + "and update_time &gt;= #{beginTime} " + "</if>" +
            "<if test=\"endTime != null and endTime != ''\">" + "and update_time &lt;= #{endTime}" + "</if>" +
            " and status = " + statusNormal + " " +
            "</where>" +
            "</script>")
    List<String> getTags(Map<String, Object> map);

    @Update("<script>" +
            "update " + tableName +
            "<set>" +
            "<if test=\"type != null\">`type` = #{type}, </if>" +
            "<if test=\"tags != null\">`tags` = #{tags}, </if>" +
            "<if test=\"title != null\">`title` = #{title}, </if>" +
            "<if test=\"content != null\">`content` = #{content}, </if>" +
            "</set>" +
            "where `id` = #{id} and `status` = " + statusNormal + " " +
            "</script>")
    int starNote(NoteDO note);


    @Select("<script>"
            + "select " + allColumn
            + " from " + tableName
            + " <where>"
            + " status = " + statusNormal
            + "<if test=\"types != null and types.size()>0\"> "
            + "    AND `type` IN "
            + "    <foreach item='item' collection='types' open='(' separator=',' close=')'> "
            + "        #{item} "
            + "    </foreach>  "
            + "</if>"
            + "<if test=\"tags != null and tags.size()>0\"> "
            + "    AND  "
            + "    <foreach item='item' collection='tags' open='(' separator=' OR ' close=')'> "
            + "        FIND_IN_SET(#{item}, tags)  "
            + "    </foreach>  "
            + "</if>"
            + "<if test=\"title != null\"> "
            + "    and `title` like concat('%',#{title},'%')  "
            + "</if>"
            + "<if test=\"content != null\"> "
            + "    and `content` like concat('%',#{content},'%') "
            + "</if>"
            + "</where>"
            + " order by create_time desc "
            + "</script>")
    @ResultMap("noteMap")
    List<NoteDO> query(NoteQueryReq req);

    @Select("select distinct `type` from " + tableName + "where `status` = " + statusNormal)
    List<String> getAllTypes();

    @Select("select distinct `tags` from " + tableName+ "where `status` = " + statusNormal)
    List<String> getAllTags();

}
