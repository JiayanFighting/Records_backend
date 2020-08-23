package com.demo.records.dao;

import com.demo.records.domain.ReportDO;
import com.demo.records.domain.UserDO;
import com.demo.records.vo.ReportVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface ReportMapper {

    @Select("<script>" +
            "select `id`,`from_email`,u1.username as from_name,`to_email`,u2.username as to_name,`team_id`,reports.sprint,`report_type`,`theme`,`content`,`create_time`,`update_time`,reports.`status_code` " +
            " from reports , users u1, users u2 "+
            "<where>" +
            "<if test=\"teamId != null and teamId != ''\">"+ "and team_id = #{teamId} " + "</if>" +
            "<if test=\"fromEmail != null and fromEmail != ''\">"+ "and from_email = #{fromEmail} " + "</if>" +
            "<if test=\"toEmail != null and toEmail != ''\">"+ "and to_email = #{toEmail} " + "</if>" +
            "<if test=\"type != null and type != ''\">"+ "and report_type = #{type} " + "</if>" +
            "<if test=\"sprint != null and sprint != ''\">"+ "and sprint = #{sprint} " + "</if>" +
            "<if test=\"userEmail != null and userEmail != ''\">"+ "and from_email = #{userEmail} " + "</if>" +
            "<if test=\"beginTime != null and beginTime != ''\">"+ "and update_time &gt;= #{beginTime} " + "</if>" +
            "<if test=\"endTime != null and endTime != ''\">"+ "and update_time &lt;= #{endTime}" + "</if>" +
            " and reports.status_code = 0 "+
            " and u1.email = reports.from_email " +
            " and u2.email = reports.to_email "+
            "</where>"+
            " order by create_time desc " +
            "<if test=\"offset != null and limit != null\">"+
            " limit #{offset}, #{limit}" +
            "</if>"+
            "</script>")
    @Results(id="reportMap", value={
            @Result(column="from_email", property="fromEmail"),
            @Result(column="from_name", property="fromName"),
            @Result(column="to_email", property="toEmail"),
            @Result(column="to_name", property="toName"),
            @Result(column="team_id", property="teamId"),
            @Result(column="report_type", property="type"),
            @Result(column="create_time", property="createTime"),
            @Result(column="update_time", property="updateTime"),
            @Result(column="status_code",property = "statusCode")
    })
    List<ReportVO> list(Map<String, Object> map);

//    @Insert("INSERT INTO indicators(symbol,indicator_date,indicator_name,param_key,param_value)
//    VALUES(#{symbol},#{indicatorDate},#{indicatorName},#{paramKey},#{paramValue})
//    ON DUPLICATE KEY UPDATE param_value=VALUES(param_value);")
    @Options(useGeneratedKeys=true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO reports (`from_email`, `to_email`, `team_id`,`sprint`, `report_type`, `theme`,`content`)"
                                + "values (#{fromEmail}, #{toEmail}, #{teamId}, #{sprint}, #{type}, #{theme},#{content})" +
            "ON DUPLICATE KEY UPDATE content=VALUES(content), theme=VALUES(theme)")
    int save(ReportDO report);

    @Select("select `id`,`from_email`,`to_email`,`team_id`,`sprint`,`report_type`,`theme`,`content`,`create_time`,`update_time`,`status_code` " +
            " from reports " +
            " where id = #{id} ")
    ReportVO getReportById(int id);

    @Update("<script>"+
            "update reports " +
            "<set>" +
            "<if test=\"theme != null\">`theme` = #{theme}, </if>" +
            "<if test=\"content != null\">`content` = #{content}, </if>" +
            "</set>" +
            "where `id` = #{id}" +
            " and `from_email` = #{fromEmail}"+
            "</script>")
    int update(ReportDO report);

    @Select("select id,`theme`,`content` " +
            " from reports " +
            " where team_id = #{teamId} and sprint = #{sprint} and report_type = #{type} " +
            " and from_email = #{fromEmail} and status_code = 0 limit 1 ")
    ReportDO getReportDetail(Map<String, Object> map);


    @Select("select `email`, `username`, `avatar` " +
            "from users u inner join ("+
            "select `user_email` " +
            " from team_relationships " +
            " where team_id = #{teamId} and status_code = 0" +
            " and `user_email` NOT IN "+
            " (select from_email " +
            " from reports " +
            " where team_id = #{teamId} and report_type = #{type} and sprint = #{sprint} and status_code = 0 )" +
            ") as t " +
            " on u.email = t.user_email")
    List<UserDO> getUnReportUsers(Map<String, Object> map);
    /*
    private int id;
    private UserDO from;
    private UserDO to;
    private int teamId;
    private String type;
    private String theme;
    private String content;
    private String createTime;
    private String updateTime;
    private int status;
     */
}
