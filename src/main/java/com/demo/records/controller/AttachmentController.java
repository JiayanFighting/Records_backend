package com.demo.records.controller;

import com.microsoft.aad.msal4j.IAccount;
import com.demo.records.aad.SessionManagementHelper;
import com.demo.records.constant.PhotoStore;
import com.demo.records.service.PhotoService;
import com.demo.records.service.UserService;
import com.demo.records.utils.Result;
import com.demo.records.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@CrossOrigin
@RestController
public class AttachmentController {

    public static final String PHOTO_STORE_URL = "/root/ProjectFiles/Records/photos/";

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    UserService userService;

    @Autowired
    private PhotoService photoService;

    //upload photo
    //form data
    @ResponseBody
    @PostMapping("/photo/upload")
    public Result upload(@RequestParam("photo") MultipartFile photo, @RequestParam("userId") int userId) throws IOException {
        System.out.println("save a photo");
//        String root = PhotoStore.PHOTO_STORE_URL;
        String root = PHOTO_STORE_URL;
//        System.out.println("current root: "+root);
        String originalFilename = photo.getOriginalFilename();
        assert originalFilename != null;
        String uuidFilename = UploadUtils.getUUIDName(originalFilename);
//        String userEmail = getLoginUser();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File userDir = new File(root+userId+"/"+date);
        if (!userDir.exists()) {
            if (!userDir.mkdirs()){
                return Result.error();
            }
        }

        File savedFile = new File(root+userId+"/"+date,uuidFilename);
        photo.transferTo(savedFile);
        String savePath = root+userId+"/"+date + "/" + uuidFilename;

        photoService.save(savePath,date,userId);
        Result res = new Result();
        res.put("url","/"+userId+"/"+date + "/" + uuidFilename);
        return res;
    }

    @ResponseBody
    @PostMapping("/photo/upload/avatar")
    public Result uploadAvatar(@RequestParam("photo") MultipartFile photo) throws IOException {
        String root = PHOTO_STORE_URL;
//        String root = PhotoStore.PHOTO_STORE_URL;
        String avatarName = String.valueOf(System.currentTimeMillis())+new Random().nextInt(9)+".jpg";
        String userEmail = getLoginUser();
        String fileDic = root+"avatars/"+userEmail;
        File userDir = new File(fileDic);
        if (!userDir.exists() && !userDir.mkdirs()) {
            return Result.error();
        }
        try {
            File savedFile = new File(fileDic,avatarName);
            photo.transferTo(savedFile);
            String savePath = "/avatars/"+userEmail + "/" + avatarName;
            if (userService.updateAvatar(userEmail,savePath) > 0) {
                Result res = new Result();
                res.put("url",savePath);
                return res;
            }
            throw new Exception("Save failed!");
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //get all photos by user email || teamId
    @ResponseBody
    @RequestMapping("/photo/getPhotos")
    public Result getPhotos(int teamId){
        String email = getLoginUser();
        Result res = new Result();
        List<String> photoURLs = photoService.getPhotos(email,teamId);
        res.put("photos",photoURLs);
        System.out.println("urls"+photoURLs.toString());
        return res;
    }

    //delete a photo
    @ResponseBody
    @PostMapping("/photo/delete")
    public Result deletePhoto(@RequestBody Map<String,String> params) {
        String url = params.get("url");
        System.out.println(url);
        photoService.deletePhoto(url);
        File f = new File(url);
        if (!f.exists()) {
            System.out.println("delete failed, file not exists");
            return Result.error("fail not exists");
        }
        if (!f.delete()) {
            System.out.println("delete failed");
            return Result.error("delete failed");
        }
        System.out.println("delete");
        return Result.ok();
    }

    //get a photo by url
    @ResponseBody
    @RequestMapping(value = "/photo/get",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPhoto(String url) throws IOException {
        System.out.println("display photo");
        File f = new File(url);
        FileInputStream inputStream = new FileInputStream(f);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }

    public String getLoginUser() {
        IAccount account =  SessionManagementHelper.getAuthSessionObject(httpServletRequest).account();
        return account.username();
    }
}
