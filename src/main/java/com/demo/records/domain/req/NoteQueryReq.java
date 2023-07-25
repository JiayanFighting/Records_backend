package com.demo.records.domain.req;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hiterhuang
 * @date 2023-07-25 18:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteQueryReq {

    private Integer id;
    private Integer directory;
    private List<String> types;
    private List<String> tags;
    private String title;
    private String content;
    private Integer status;
}
