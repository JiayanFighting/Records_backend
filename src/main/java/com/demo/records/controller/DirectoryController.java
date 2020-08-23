package com.demo.records.controller;

import com.demo.records.domain.DirectoryDO;
import com.demo.records.service.DirectoryService;
import com.demo.records.service.TeamRelationshipService;
import com.demo.records.service.TeamService;
import com.demo.records.service.TeamSprintService;
import com.demo.records.utils.Result;
import com.demo.records.vo.DirectoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Slf4j
@RestController
@RequestMapping("/directory")
public class DirectoryController {
    @Autowired
    DirectoryService directoryService;

    @Autowired
    TeamService teamService;
    @Autowired
    UserController userController;
    @Autowired
    TeamRelationshipService teamRelationshipService;
    @Autowired
    TeamSprintService teamSprintService;

    @RequestMapping("/all")
    public Result getAllDirectory(int userId){
        List<DirectoryDO> list= directoryService.getAll(userId);
        DirectoryVO vo = new DirectoryVO();
        int rootId = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        while (!queue.isEmpty()){
            int fatherId  =  queue.poll();
            List<DirectoryDO> children =  directoryService.getChildren(fatherId);
        }

        Result res = new Result();
        res.put("all",list);
        return res;
    }

    @RequestMapping("/find")
    public Result findDirectory(DirectoryDO directory){
        List<DirectoryDO> list= directoryService.findDirectory(directory);
        Result res = new Result();
        res.put("list",list);
        return res;
    }

    @ResponseBody
    @RequestMapping("/info")
    public Result getInfo(int id){
        DirectoryDO directory=new DirectoryDO(id);
        List<DirectoryDO> list= directoryService.findDirectory(directory);
        if (list.size()>0){
            Result res = new Result();
            res.put("info",list.get(0));
            return res;
        }
        return Result.error("nonexistent");
    }

    @RequestMapping("/father")
    public Result getFather(int id){
        DirectoryDO directory= directoryService.getFather(id);
        Result res = new Result();
        res.put("directory",directory);
        return res;
    }

    @RequestMapping("/children")
    public Result getChildren(int id){
        List<DirectoryDO> list= directoryService.getChildren(id);
        Result res = new Result();
        res.put("list",list);
        return res;
    }

    @ResponseBody
    @PostMapping("/create")
    public Result create(@RequestBody DirectoryDO directory){
        if(directoryService.create(directory)>0){
            log.info("create directory success!");
            Result res = new Result();
            res.put("id",directory.getId());
            return res;
        }
        log.info("create directory fail!");
        return Result.error();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result update(@RequestBody DirectoryDO directory){
        if (directoryService.update(directory) > 0) {
            return Result.ok();
        }else {
            return Result.error("error!");
        }
    }


    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String,String> params) {
        int id = Integer.parseInt(params.get("id"));
//        log.info("/team/delete:id="+id);
        try {
            directoryService.delete(id);
            return Result.ok();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }
}
