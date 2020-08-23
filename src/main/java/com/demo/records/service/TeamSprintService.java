package com.demo.records.service;

import com.demo.records.domain.TeamSprintDO;

import java.util.List;
import java.util.Map;

public interface TeamSprintService {

    int create(TeamSprintDO teamSprintDO);

    int delete(int id);

    int update(TeamSprintDO teamSprintDO);

    List<TeamSprintDO> list(Map<String, Object> map);

    List<TeamSprintDO> getCurrentSprints(Map<String, Object> map);

    TeamSprintDO getSprintUnique(int teamId, int sprint, String type);
}
