package com.demo.records.service.Impl;

import com.demo.records.dao.TeamMapper;
import com.demo.records.dao.TeamRelationshipMapper;
import com.demo.records.dao.UserMapper;
import com.demo.records.domain.TeamDO;
import com.demo.records.domain.UserDO;
import com.demo.records.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    TeamRelationshipMapper teamRelationshipMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public TeamDO getTeamInfo(int id) {
        return teamMapper.getTeamInfo(id);
    }

    @Override
    public int create(TeamDO team) {
        return teamMapper.create(team);
    }

    @Override
    public int update(TeamDO team) {
        return teamMapper.update(team);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int delete(int id) {
        if (teamMapper.delete(id) > 0) {
            // delete team_relationship
            teamRelationshipMapper.deleteAll(id);
            // delete sprint
            // delete template
            return 1;
        }
        return 0;
    }

    @Override
    public List<TeamDO> getCreatedTeamList(String leadEmail) {
        return teamMapper.getCreatedTeamList(leadEmail);
    }

    @Override
    public  List<TeamDO> getJoinedTeamList(String userEmail) {
        return teamMapper.getJoinedTeamList(userEmail);
    }

    @Override
    public List<TeamDO> search(String content) {
        return teamMapper.search(content);
    }

    @Override
    public List<UserDO> getTeamMembers(int teamId) {
        return teamRelationshipMapper.getTeamMembers(teamId);
    }

    @Override
    public List<TeamDO> searchNotJoined(String content, String userEmail) {
        return teamMapper.searchNotJoined(content,userEmail);
    }

}
