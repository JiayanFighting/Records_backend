package com.demo.records.service;

import com.demo.records.domain.ReportDO;
import com.demo.records.domain.TeamDO;
import com.demo.records.domain.UserDO;
import com.demo.records.vo.RemindVO;
import com.demo.records.vo.ReportVO;

import java.util.List;
import java.util.Map;

public interface ReportService {
    void submit(ReportDO report);

    List<ReportVO> list(Map<String, Object> map);

    int save(ReportDO report);

    List<UserDO> peopleToRemind(Map<String, Object> params, List<TeamDO> createdList);

    List<RemindVO> getRemindList(Map<String, Object> params, List<TeamDO> teamList);

    ReportVO getReportById(int id);

    int update(ReportDO report);

    ReportDO getReportDetail(Map<String, Object> map);

    int saveDraft(ReportDO reportDO);

    ReportDO getDraft(Map<String, Object> params);

    int deleteDraft(Map<String, Object> params);
}
