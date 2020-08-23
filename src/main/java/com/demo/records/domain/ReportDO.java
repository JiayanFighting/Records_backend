package com.demo.records.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDO {
    private int id;
    private String fromEmail;
    private String toEmail;
    private int teamId;
    private int sprint;
    private String type; // weekly
    private String theme; // 个人标题
    private String content;
    private String createTime;
    private String updateTime;
    private int statusCode;

    public ReportDO(String  fromEmail, int teamId,int sprint,String type,String theme,String content){
        this.fromEmail = fromEmail;
        this.teamId = teamId;
        this.sprint = sprint;
        this.type = type;
        this.theme = theme;
        this.content = content;
    }

}
