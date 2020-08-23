package com.demo.records.controller;

import com.demo.records.domain.UserDO;
import com.demo.records.service.UserService;
import com.demo.records.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/whitelist")
public class WhitelistController {

    @Autowired
    UserService userService;

    @Autowired
    UserController userController;

    @RequestMapping("/list")
    public Result getWhitelist(){
        String email = userController.getLoginUser();
//        log.info("/whitelist/list:email="+email);
        if (isAdmin(email)){
            Result res = new Result();
            List<UserDO> list = userService.getAllUsers();
            //sort list, admin first
            list.sort((a, b) -> b.getType()-a.getType());
            res.put("list",list);
            return res;
        }else {
            return Result.error("No permission");
        }
    }

    @PostMapping("/add")
    public Result addWhitelistUser(@RequestBody Map<String,String> params) {
        String email =  params.get("email");
//        log.info("/whitelist/add:email="+email);
        if (email== null || email.length() == 0){
            return Result.error("Parameter error");
        }
        if (isAdmin(userController.getLoginUser())) {
            UserDO user = new UserDO(email,userService.getUsernameFromEmail(email));
            if (userService.addWhitelist(user) > 0){
                return Result.ok();
            }
            return Result.error("Do not add again!");
        }else {
            return Result.error("No permission");
        }
    }

    @PostMapping("/delete")
    public Result deleteWhitelistUser(@RequestBody Map<String,String> params) {
        String email =  params.get("email");
//        log.info("/whitelist/delete:email="+email);
        String loggedUser =userController.getLoginUser();
        // only the admin can delete users from the whitelist and the super admin can delete the normal admin
        System.out.println(isAdmin(loggedUser));
        if (isAdmin(loggedUser)){
            UserDO deleteUser = userService.findByEmail(email);
            System.out.println(deleteUser.toString());
            System.out.println(userService.isSuperAdmin(loggedUser));
            System.out.println(deleteUser == null);
            System.out.println(deleteUser.getType() == UserDO.USER_TYPE_NORMAL);
            if (userService.isSuperAdmin(loggedUser) || deleteUser == null || deleteUser.getType() == UserDO.USER_TYPE_NORMAL){
                userService.deleteWhitelist(email);
                return Result.ok();
            }
        }
        return Result.error("No permission");
    }

    @PostMapping("/setAdmin")
    public Result setAdmin(@RequestBody Map<String,String> params) {
        String email =  params.get("email");
//        log.info("/whitelist/setAdmin:email="+email);
        if (userService.isSuperAdmin(userController.getLoginUser())) {
            if (userService.setAdmin(email) > 0){
                return Result.ok();
            }
            return Result.error("Has been set or the user has not logged in");
        }else {
            return Result.error("No permission");
        }
    }

    @PostMapping("/cancelAdmin")
    public Result cancelAdmin(@RequestBody Map<String,String> params) {
        String email =  params.get("email");
//        log.info("/whitelist/cancelAdmin:email="+email);
        if (userService.isSuperAdmin(userController.getLoginUser())) {
            if (userService.cancelAdmin(email) > 0){
                return Result.ok();
            }
            return Result.error("Has been set or the user has not logged in");
        }else {
            return Result.error("No permission");
        }
    }

    public boolean isAdmin(String email){
        int type = userService.findByEmail(email).getType();
        return type == UserDO.USER_TYPE_ADMIN || type == UserDO.USER_TYPE_SUPER_ADMIN;
    }
}
