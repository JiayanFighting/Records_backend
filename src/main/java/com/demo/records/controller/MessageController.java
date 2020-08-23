package com.demo.records.controller;

import com.demo.records.domain.MessageDO;
import com.demo.records.service.MessageService;
import com.demo.records.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    UserController userController;

    @ResponseBody
    @RequestMapping("/getMessageList")
    public Result getMessageList(){
//        log.info("/message/getMessageList");
        String email = userController.getLoginUser();
//        log.info(email);
        List<MessageDO> createdList = messageService.getMessageList(email);
        Result res = Result.ok();
        res.put("inboxMessage",createdList);
        return res;
    }

    @ResponseBody
    @PostMapping("/create")
    public Result createTeam(@RequestBody MessageDO messageDO){
//        log.info("/message/create: body="+messageDO.toString());
        String userEmail = userController.getLoginUser();
        messageDO.setFrom_email(userEmail);
        if(messageService.createMessage(messageDO)>0){
            log.info("create message success!");
            return Result.ok();
        }
        log.info("create message fail!");
        return Result.error();
    }

    @ResponseBody
    @PostMapping("/delete")
    public Result deleteMessage(@RequestBody Map<String,String> params) throws JSONException {
        int id = Integer.parseInt(params.get("id"));
//        log.info("/message/delete:id="+id);
        if (messageService.deleteMessage(id) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    @ResponseBody
    @PostMapping("/read")
    public Result readMessage(@RequestBody Map<String,String> params) throws JSONException {
        int id = Integer.parseInt(params.get("id"));
//        log.info("/message/read:id="+id);
        if (messageService.readMessage(id) > 0) {
            return Result.ok();
        }
        return Result.error();
    }
}
