package com.demo.records.service;

import com.demo.records.domain.NoteDO;
import com.demo.records.domain.ReportDO;

import java.util.List;
import java.util.Map;

public interface NoteService {

    List<NoteDO> list(Map<String, Object> map);

    int save(NoteDO note);

    int delete(int id);

    int update(NoteDO note);

    NoteDO getNoteById(int id);

    int saveDraft(NoteDO note);

    ReportDO getDraft(Map<String, Object> params);

    int deleteDraft(Map<String, Object> params);
}
