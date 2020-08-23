package com.demo.records.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportVO {
    private int id;
    private String fromEmail;
    private String fromName;
    private String toEmail;
    private String toName;
    private int teamId;
    private int sprint;
    private String teamName;
    private String type;
    private String theme;
    private String content;
    private String createTime;
    private String updateTime;
    private int statusCode;
}
