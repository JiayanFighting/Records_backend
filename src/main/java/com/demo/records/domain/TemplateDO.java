package com.demo.records.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDO {
    private int id;
    private int teamId;
    private String type;
    private String theme;
    private String content;
    private String creatorEmail;
    private String createTime;
    private String updateTime;
    private int statusCode;
}
