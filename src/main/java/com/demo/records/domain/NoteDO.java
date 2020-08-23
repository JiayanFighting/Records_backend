package com.demo.records.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDO {
    private int id;
    private int userId;
    private int directory;
    private String type;
    private String tags;
    private String title;
    private String content;
    private String cover;
    private int thumbUp;
    private int star;
    private String createTime;
    private String updateTime;
    private int status;
}
