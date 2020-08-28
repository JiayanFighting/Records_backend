package com.demo.records.controller;

import com.demo.records.domain.DirectoryDO;
import com.demo.records.domain.NoteDO;
import com.demo.records.service.*;
import com.demo.records.utils.Result;
import com.demo.records.vo.DirectoryVO;
import com.demo.records.vo.DirectoryVOO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/directory")
public class DirectoryController {
    @Autowired
    DirectoryService directoryService;
    @Autowired
    NoteService noteService;

    @Autowired
    TeamService teamService;
    @Autowired
    UserController userController;
    @Autowired
    TeamRelationshipService teamRelationshipService;
    @Autowired
    TeamSprintService teamSprintService;

    @RequestMapping("/all")
    public Result getAllDirectory(){
        int userId = 1;
//        List<DirectoryDO> list= directoryService.getAll(userId);
        int rootId = 0;
        DirectoryVOO voo = new DirectoryVOO();
        // 当前目录的基本信息
        voo.setKey("0");
        voo.setTitle("顶级目录");
        voo.setIsLeaf(false);
        // 子目录
        List<DirectoryVOO> ldvoo = getDirectoryVOO(userId,rootId);
        // 当前目录下的博客
        List<NoteDO> notes = noteService.getListByDirectory(1,rootId);
        // 变成子目录，但是 isLeaf为true
        for (NoteDO note:notes){
            DirectoryVOO vooo = new DirectoryVOO();
            vooo.setTitle(note.getTitle());
            vooo.setKey(note.getId()+note.getTitle().substring(0,Math.min(note.getTitle().length(),5)));
            vooo.setIsLeaf(true);
            vooo.setNoteId(note.getId());
            ldvoo.add(vooo);
        }
        voo.setChildren(new ArrayList<>(ldvoo));
        Result res = new Result();
        res.put("all",voo);
        return res;
    }

    @RequestMapping("/allOnly")
    public Result getAllDirectoryWithoutNote(){
        int rootId = 0;
        DirectoryVOO voo = new DirectoryVOO();
        // 当前目录的基本信息
        voo.setKey("0");
        voo.setTitle("顶级目录");
        voo.setIsLeaf(false);
        // 子目录
        List<DirectoryVOO> ldvoo = getDirectoryVOOWithOutNote(rootId);
        voo.setChildren(new ArrayList<>(ldvoo));
        Result res = new Result();
        res.put("all",voo);
        return res;
    }

    public List<DirectoryVOO> getDirectoryVOOWithOutNote(int fatherId){
        List<DirectoryVOO> vooList = new ArrayList<>();
        List<DirectoryDO> children =  directoryService.getChildren(fatherId);
        Map<String, Object> map = new HashMap<>();
        for (DirectoryDO ddo:children){
            DirectoryVOO voo = new DirectoryVOO();
            // 当前目录的基本信息
            voo.setKey(String.valueOf(ddo.getId()));
            voo.setTitle(ddo.getName());
            voo.setIsLeaf(false);
            // 子目录
            List<DirectoryVOO> ldvoo = getDirectoryVOOWithOutNote(ddo.getId());
            voo.setChildren(new ArrayList<>(ldvoo));
            vooList.add(voo);
        }
        return vooList;
    }

    public List<DirectoryVOO> getDirectoryVOO(int userId,int fatherId){
        List<DirectoryVOO> vooList = new ArrayList<>();
        List<DirectoryDO> children =  directoryService.getChildren(fatherId);
        Map<String, Object> map = new HashMap<>();
        for (DirectoryDO ddo:children){
            DirectoryVOO voo = new DirectoryVOO();
            // 当前目录的基本信息
            voo.setKey(String.valueOf(ddo.getId()));
            voo.setTitle(ddo.getName());
            voo.setIsLeaf(false);
            // 子目录
            List<DirectoryVOO> ldvoo = getDirectoryVOO(userId,ddo.getId());
            // 当前目录下的博客
            map.put("directory",ddo.getId());
            List<NoteDO> notes = noteService.getListByDirectory(userId,ddo.getId());
//            List<NoteDO> notes = noteService.list(map);
            // 变成子目录，但是 isLeaf为true
            for (NoteDO note:notes){
                DirectoryVOO vooo = new DirectoryVOO();
                vooo.setTitle(note.getTitle());
                vooo.setKey(note.getId()+note.getTitle().substring(0,Math.min(note.getTitle().length(),5)));
                vooo.setIsLeaf(true);
                vooo.setNoteId(note.getId());
                ldvoo.add(vooo);
            }
            voo.setChildren(new ArrayList<>(ldvoo));
            vooList.add(voo);
        }
        return vooList;
    }

    public List<DirectoryVO> getDirectoryVO(int fatherId){
        List<DirectoryVO> voList = new ArrayList<>();
        List<DirectoryDO> children =  directoryService.getChildren(fatherId);
        Map<String, Object> map = new HashMap<>();
        for (DirectoryDO ddo:children){
            DirectoryVO vo = new DirectoryVO();
            List<DirectoryVO> clvo = getDirectoryVO(ddo.getId());
            map.put("directory",ddo.getId());
            List<NoteDO> notes = noteService.list(map);
            vo.setChildren(new ArrayList<>(clvo));
            vo.setDirectoryDO(ddo);
            vo.setNotes(new ArrayList<>(notes));
            vo.setExtral();
            if (notes.size()==0&& clvo.size()==0){
                vo.setIsLeaf(true);
            }else{
                vo.setIsLeaf(false);
            }
            voList.add(vo);
        }
        return voList;
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
