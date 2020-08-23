package com.demo.records.service;

import java.util.List;

public interface PhotoService {
    void save(String path, String email, int teamId);

    List<String> getPhotos(String email, int teamId);

    void deletePhoto(String url);
}
