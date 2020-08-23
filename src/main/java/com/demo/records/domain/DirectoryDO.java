package com.demo.records.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryDO {
    private int id;
    private int userId;
    private String name;
    private int fatherId;
    private int rank;
    private String description;
    private String createTime;
    private int status;

    public DirectoryDO(int id) {
        this.id=id;
    }
}
