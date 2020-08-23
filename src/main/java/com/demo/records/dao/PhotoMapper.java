package com.demo.records.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper

public interface PhotoMapper {
    @Insert("Insert INTO photos (email,path,team_id) VALUES (#{email},#{path}, #{teamId})")
    void save(@Param("path") String path, @Param("email") String email, @Param("teamId") int teamId);

    @Select("SELECT path from photos WHERE email = #{email} AND team_id = #{teamId}")
    List<String> getPhotos(@Param("email") String email, @Param("teamId") int teamId);

    @Delete("DELETE from photos WHERE path = #{path}")
    void delete(@Param("path") String path);
}
