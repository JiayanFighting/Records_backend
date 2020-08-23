package com.demo.records.service;

import com.demo.records.domain.TeamDO;
import com.demo.records.domain.UserDO;

import java.util.List;

public interface TeamService {
    TeamDO getTeamInfo(int id);

    int create(TeamDO team);

    int update(TeamDO team);

    int delete(int id);

    List<TeamDO> getCreatedTeamList(String leadEmail);

    List<TeamDO> getJoinedTeamList(String userEmail);

    List<TeamDO> search(String content);

    List<UserDO> getTeamMembers(int teamId);

    List<TeamDO> searchNotJoined(String content, String userEmail);
}
