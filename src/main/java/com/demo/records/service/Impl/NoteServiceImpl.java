package com.demo.records.service.Impl;

import com.demo.records.dao.NoteMapper;
import com.demo.records.domain.*;
import com.demo.records.domain.req.NoteQueryReq;
import com.demo.records.service.NoteService;
import com.demo.records.utils.RedisUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NoteServiceImpl implements NoteService {

    public static final String REDIS_DRAFT_NOTES_KEY_PREFIX = "draft:note";
    public static final int REDIS_EXPIRED_TIME = 86400;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<NoteDO> list(Map<String, Object> map) {
        return noteMapper.list(map);
    }

    @Override
    public List<NoteDO> query(NoteQueryReq req) {
        return noteMapper.query(req);
    }

    @Override
    public List<NoteDO> getListByDirectory(int userId, int directory) {
        return noteMapper.getListByDirectory(userId, directory);
    }

    @Override
    public List<String> getTags(Map<String, Object> map) {
        return noteMapper.getTags(map);
    }

    @Override
    public int save(NoteDO note) {
        return noteMapper.save(note);
    }

    @Override
    public int delete(int id) {
        return noteMapper.delete(id);
    }

    @Override
    public int update(NoteDO note) {
        return noteMapper.update(note);
    }

    @Override
    public NoteDO getNoteById(int id) {
        return noteMapper.getNoteById(id);
    }

    @Override
    public int saveDraft(NoteDO note) {
        String key = REDIS_DRAFT_NOTES_KEY_PREFIX + ":" + note.getUserId();
        redisUtil.hset(key, "title", note.getTitle());
        redisUtil.hset(key, "content", note.getContent());
        return 0;
    }

    @Override
    public ReportDO getDraft(Map<String, Object> params) {
        return null;
    }

    @Override
    public int deleteDraft(Map<String, Object> params) {
        return 0;
    }

    @Override
    public List<String> getAllTypes() {
        return noteMapper.getAllTypes();
    }

    @Override
    public List<String> getAllTags() {
        List<String> tagStrs = noteMapper.getAllTags();
        List<String> tags = new ArrayList<>();
        for (String tag : tagStrs) {
            tags.addAll(Arrays.stream(tag.split(",")).collect(Collectors.toList()));
        }
        return tags.stream().filter(x -> !x.equals("")).distinct().collect(Collectors.toList());
    }


}
