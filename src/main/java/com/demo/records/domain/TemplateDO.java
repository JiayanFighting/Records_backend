package com.demo.records.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDO {

    private Integer id;
    private String type;
    private String title;
    private String content;
    private Integer teamId;
    private String creator;
    private Integer status;
    private String createTime;
    private String updateTime;

}
