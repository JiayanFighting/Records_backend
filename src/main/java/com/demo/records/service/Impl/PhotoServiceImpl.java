package com.demo.records.service.Impl;

import com.demo.records.dao.PhotoMapper;
import com.demo.records.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoMapper photoMapper;

    @Override
    public void save(String path, String email, int teamId) {
        photoMapper.save(path,email,teamId);
    }

    @Override
    public List<String> getPhotos(String email,int teamId) {
        return photoMapper.getPhotos(email,teamId);
    }

    @Override
    public void deletePhoto(String url) {
        photoMapper.delete(url);
    }

}
