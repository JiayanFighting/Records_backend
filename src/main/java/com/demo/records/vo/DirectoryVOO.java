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
public class DirectoryVOO {
    private String title;
    private String key;
    private boolean isLeaf;
    private int noteId;
    private List<DirectoryVOO> children;

    public void setIsLeaf(boolean isLeaf){
        this.isLeaf =  isLeaf;
    }

    public boolean getIsLeaf(){
        return this.isLeaf;
    }
}
