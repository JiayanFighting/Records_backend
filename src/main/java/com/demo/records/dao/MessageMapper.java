package com.demo.records.dao;

import com.demo.records.domain.MessageDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MessageMapper {
    @Select("select `id`, `operation`, `from_email`, `from_name`, `to_email`, `content`, `data`, `create_time`, `status_code` from messages where id = #{id}")
    @Results(id="messageMap", value={
            @Result(column="operation", property="operation"),
            @Result(column="from_name", property="from_name"),
            @Result(column="from_email", property="from_email"),
            @Result(column="to_email", property="to_email"),
            @Result(column="content", property="content"),
            @Result(column="data", property="data"),
            @Result(column="create_time", property="create_time"),
            @Result(column="status_code", property="status_code")
    })
    MessageDO getMessage(int id);

    @Update("update messages set status_code = 1 where id=#{id}")
    int deleteMessage(int id);

    @Update("update messages set status_code=2 where id = #{id}")
    int readMessage(int id);

    @Select("select `id`, `operation`, `from_name`, `from_email`, `to_email`, `content`, `data`, `create_time`, `status_code` from messages where to_email = #{userEmail} and status_code != 1")
    @ResultMap("messageMap")
    List<MessageDO> getMessageList(String userEmail);


    @Insert("insert ignore into messages (`operation`, `from_name`, `from_email`, `to_email`, `content`, `data`, `status_code`)"
            + "values (#{operation}, #{from_name}, #{from_email}, #{to_email}, #{content}, #{data}, #{status_code})")
    int createMessage(MessageDO messageDO);

}
