package com.demo.records.vo;

import com.demo.records.domain.NoteDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteVO {

    private int id;
    private int userId;
    private int directory;
    private String type;
    private String[] tags;
    private String title;
    private String content;
    private String cover;
    private int thumbUp;
    private int star;
    private String createTime;
    private String updateTime;
    private int status;

    public NoteVO(NoteDO note) {
        this.id = note.getId();
        this.userId = note.getUserId();
        this.directory = note.getDirectory();
        this.type = note.getType();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.cover = note.getCover();
        this.thumbUp = note.getThumbUp();
        this.star = note.getStar();
        this.updateTime = note.getUpdateTime();
        this.createTime = note.getCreateTime();
        this.status = note.getStatus();
        this.tags = note.getTags().split(",");
    }
}
