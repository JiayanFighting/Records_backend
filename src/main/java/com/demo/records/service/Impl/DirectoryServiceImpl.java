package com.demo.records.service.Impl;

import com.demo.records.dao.DirectoryMapper;
import com.demo.records.domain.DirectoryDO;
import com.demo.records.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    @Autowired
    DirectoryMapper directoryMapper;

    @Override
    public List<DirectoryDO> getAll(int userId) {
        return directoryMapper.getAll(userId);
    }

    @Override
    public List<DirectoryDO> findDirectory(DirectoryDO directory) {
        return directoryMapper.findDirectory(directory);
    }

    @Override
    public DirectoryDO getFather(int id) {
        return directoryMapper.getFather(id);
    }

    @Override
    public List<DirectoryDO> getChildren(int id) {
        return directoryMapper.getChildren(id);
    }

    @Override
    public int create(DirectoryDO directory) {
        return directoryMapper.create(directory);
    }

    @Override
    public int update(DirectoryDO directory) {
        return directoryMapper.update(directory);
    }

    @Override
    public int delete(int id) {
        return directoryMapper.delete(id);
    }
}
