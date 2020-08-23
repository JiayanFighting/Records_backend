package com.demo.records.service;

import com.demo.records.domain.DirectoryDO;

import java.util.List;

public interface DirectoryService {

    List<DirectoryDO> getAll(int userId);

    List<DirectoryDO> findDirectory(DirectoryDO directory);

    DirectoryDO getFather(int id);

    List<DirectoryDO> getChildren(int id);

    int create(DirectoryDO directory);

    int update(DirectoryDO directory);

    int delete(int id);

}
