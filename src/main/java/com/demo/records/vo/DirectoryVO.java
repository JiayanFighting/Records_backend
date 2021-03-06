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
    private String title;
    private String key;
    private boolean isLeaf;
    private DirectoryDO directoryDO;
    private List<DirectoryVO> children;
    private List<NoteDO> notes;

    public void setIsLeaf(boolean isLeaf){
        this.isLeaf =  isLeaf;
    }

    public boolean getIsLeaf(){
        return this.isLeaf;
    }
    public void setExtral(){
        this.title = directoryDO.getName();
        this.key = String.valueOf(directoryDO.getId());
    }
}
