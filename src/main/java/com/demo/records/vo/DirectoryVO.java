package com.demo.records.vo;

import com.demo.records.domain.DirectoryDO;
import com.demo.records.domain.NoteDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryVO {
    private DirectoryDO directoryDO;
    private List<DirectoryVO> children;
    private List<NoteDO> notes;
}
