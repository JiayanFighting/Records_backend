package com.demo.records.constant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PhotoStore implements InitializingBean {
    //local, change this to your local storage
//    @Value("${com.demo.records.photoStoreUrl}")
    private String photoStoreUrl;

    public static String PHOTO_STORE_URL;
    //deployment url
//    public String photoStoreUrl = "/home/omsz/photos/";

    @Override
    public void afterPropertiesSet() throws Exception {
        PHOTO_STORE_URL = photoStoreUrl;
    }
}
