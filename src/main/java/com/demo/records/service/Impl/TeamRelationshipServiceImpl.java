package com.demo.records.service.Impl;

import com.demo.records.dao.TeamRelationshipMapper;
import com.demo.records.domain.UserDO;
import com.demo.records.service.TeamRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamRelationshipServiceImpl implements TeamRelationshipService {
    @Autowired
    TeamRelationshipMapper teamRelationshipMapper;

    @Override
    public List<UserDO> getTeamMembers(int teamId) {
        return teamRelationshipMapper.getTeamMembers(teamId);
    }

    @Override
    public int deleteAll(int teamId) {
        return teamRelationshipMapper.deleteAll(teamId);
    }

    @Override
    public int remove(int teamId, String userEmail) {
        return teamRelationshipMapper.remove(teamId,userEmail);
    }

    @Override
    public int join(int teamId, String userEmail) {
        return teamRelationshipMapper.join(teamId,userEmail);
    }
}
