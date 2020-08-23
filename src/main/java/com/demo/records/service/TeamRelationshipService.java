package com.demo.records.service;

import com.demo.records.domain.UserDO;

import java.util.List;

public interface TeamRelationshipService {

    List<UserDO> getTeamMembers(int teamId);

    int deleteAll(int teamId);

    int remove(int teamId, String userEmail);

    int join(int teamId, String userEmail);
}
