package com.demo.records.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamSprintDO {
    private int id;
    private int teamId;
    private String type;
    private int sprint;
    private String beginTime;
    private String endTime;
    private int statusCode;
}
