package com.demo.records.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDO {
    private int id;
    private String from_name;
    private String operation;
    private String from_email;
    private String to_email;
    private String content;
    private String data;
    private String create_time;
    private int status_code;
}
