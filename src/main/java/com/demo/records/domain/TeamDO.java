package com.demo.records.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDO {
    private int id;
    private String teamName;
    private String teamEmail;
    private String teamDesc;
    private String leadEmail;
    private String ccList;
    private String createTime;
    private String updateTime;
    private int statusCode;

    public TeamDO(int teamId) {
        this.id = teamId;
    }
}
