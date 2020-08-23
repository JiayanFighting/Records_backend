package com.demo.records.controller;

import com.demo.records.domain.TemplateDO;
import com.demo.records.service.TemplateService;
import com.demo.records.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    //get all templates in the database
    @ResponseBody
    @RequestMapping("/getAllTemplates")
    public Result getTemplates() {
        Result res = new Result();
        List<TemplateDO> list = templateService.getAllTemplates();
        res.put("templates",list);
        return res;
    }

    //get all templates name with a specific team
    @ResponseBody
    @RequestMapping("/getTemplatesInTeam")
    public Result getTemplates(int teamId){
//        log.info("/template/getTemplatesInTeam:teamId="+teamId);
        Result res = new Result();
        List<TemplateDO> list = templateService.getTemplatesInTeam(teamId);
        res.put("templates",list);
        return res;
    }

    //create a new template for a team
    @ResponseBody
    @PostMapping(path="/create")
    public Result createTemplate(@RequestBody TemplateDO templateDO) {
//        log.info("/template/create:templateDO="+templateDO.toString());
        if (templateService.createTemplate(templateDO) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    //update an existing template
    @ResponseBody
    @PostMapping(path="/update")
    public Result updateTemplate(@RequestBody TemplateDO templateDO) {
//        log.info("/template/update:templateDO="+templateDO.toString());
        int updated = templateService.updateTemplate(templateDO);
        if (updated > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    //delete an existing template
    @ResponseBody
    @PostMapping("/delete")
    public Result deleteTemplate(@RequestBody Map<String,String> params) {
        int id = Integer.parseInt(params.get("id"));
//        log.info("/template/delete:id="+id);
        int deleted = templateService.deleteTemplate(id);
        if (deleted > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    //return detail info of a specific template
    @ResponseBody
    @RequestMapping("/selectTemplate")
    public Result selectTemplate(@RequestBody int id){
//        log.info("/template/selectTemplate:id="+id);
        Result res = new Result();
        TemplateDO temp = templateService.findById(id);
        res.put("template",temp);
        return res;
    }

    // search template by teamId and type
    @ResponseBody
    @RequestMapping("/search")
    public Result searchTemplates(int teamId,String type){
//        log.info("/template/search:teamId="+teamId+",type="+type);
        Result res = new Result();
        List<TemplateDO> list = templateService.findByType(teamId,type);
        res.put("templates",list);
        return res;
    }
}
