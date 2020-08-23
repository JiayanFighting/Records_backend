package com.demo.records.vo;

import com.demo.records.domain.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemindVO {
    private UserDO user;
    private ArrayList<Map<String,Object>> list;
}
