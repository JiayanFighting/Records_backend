package com.demo.records.controller;

import com.demo.records.domain.TeamDO;
import com.demo.records.domain.TeamSprintDO;
import com.demo.records.domain.UserDO;
import com.demo.records.service.TeamRelationshipService;
import com.demo.records.service.TeamService;
import com.demo.records.service.TeamSprintService;
import com.demo.records.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    TeamService teamService;
    @Autowired
    UserController userController;
    @Autowired
    TeamRelationshipService teamRelationshipService;
    @Autowired
    TeamSprintService teamSprintService;

    /**
     * get the teams the logged user joined
     * @return Result
     */
    @ResponseBody
    @RequestMapping("/joinedTeamList")
    public Result joinedTeamList(){
        // 测试是否好使了
        String email = userController.getLoginUser();
//        log.info("/team/joinedTeamList:user="+email);
        Result res = new Result();
        List<TeamDO> joinedList = teamService.getJoinedTeamList(email);
        res.put("joinedList",joinedList);
        return res;
    }

    /**
     * get the teams the logged user created
     * @return Result
     */
    @ResponseBody
    @RequestMapping("/createdTeamList")
    public Result createdTeamList(){
//        log.info("/team/createdTeamList");
        String email = userController.getLoginUser();
//        log.info("The logged in user is "+email);
        List<TeamDO> createdList = teamService.getCreatedTeamList(email);
        Result res = Result.ok();
        res.put("createdList",createdList);
        return res;
    }

    /**
     * get the team basic information (and members
     * @param id teamId
     * @return Result
     */
    @ResponseBody
    @RequestMapping("/info")
    public Result getTeamInfo(int id){
//        log.info("/team/info: id="+id);
        TeamDO team = teamService.getTeamInfo(id);
        if (team == null || team.getStatusCode() == 1) {
            log.info("/team/info: Not Found!");
            return Result.error("Not Found!");
        }else {
            Result res = Result.ok();
            res.put("info",team);
            List<UserDO> members = teamRelationshipService.getTeamMembers(id);
            res.put("members",members);
            return res;
        }
    }

    /**
     * create a new team, the default leader email is the logged user
     * @param team creating team's information
     * @return Result , ok or error
     */
    @ResponseBody
    @PostMapping("/create")
    public Result createTeam(@RequestBody TeamDO team){
//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        team.setCreateTime(dateFormat.format(now));
        team.setLeadEmail(userController.getLoginUser());
//        log.info("/team/create: team="+team.toString());
        if(teamService.create(team)>0){
            log.info("create team success!");
            Result res = new Result();
            res.put("id",team.getId());
            res.put("leadEmail",team.getLeadEmail());
            return res;
        }
        log.info("create team fail!");
        return Result.error();
    }

    /**
     * update the team's info, the team id is a must
     * @param team team's information will update
     * @return Result , ok or error
     */
    @ResponseBody
    @PostMapping("/update")
    public Result updateTeam(@RequestBody TeamDO team){
//        log.info("/team/update:team="+team.toString());
        team.setLeadEmail(userController.getLoginUser());
//        log.info("The logged in user is "+team.getLeadEmail());
        if (teamService.update(team) > 0) {
            return Result.ok();
        }else {
            return Result.error("Only team leader can update information!");
        }
    }


    /**
     * delete a specific team
     * @param params : id-team's id
     * @return Result
     */
    @ResponseBody
    @PostMapping("/delete")
    public Result deleteTeam(@RequestBody Map<String,String> params) {
        int id = Integer.parseInt(params.get("id"));
//        log.info("/team/delete:id="+id);
        try {
            teamService.delete(id);
            return Result.ok();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    /**
     * remove the given user from a team
     * @param params including teamId and userEmail of the user who will leave,
     *               if userEmail is not given, the default is the logged user
     * @return Result
     */
    @ResponseBody
    @PostMapping("/removeMember")
    public Result removeFromTeam(@RequestBody Map<String,String> params) {
        int teamId = Integer.parseInt(params.get("teamId"));
        String userEmail = (params.get("userEmail")!=null && !params.get("userEmail").equals(""))? params.get("userEmail"):userController.getLoginUser();
//        log.info("/team/removeMember:teamId="+teamId+",userEmail="+userEmail);
        if (teamRelationshipService.remove(teamId,userEmail) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * join a team
     * @param params including teamId and userEmail of the user who will join,
     *               if userEmail is not given, the default is the logged user
     * @return Result
     */
    @ResponseBody
    @PostMapping("/join")
    public Result joinTeam(@RequestBody Map<String,String> params){
        int teamId = Integer.parseInt(params.get("teamId"));
        String userEmail = params.get("userEmail")!=null? params.get("userEmail"):userController.getLoginUser();
//        log.info("/team/join:teamId="+teamId+",userEmail="+userEmail);
        TeamDO team = teamService.getTeamInfo(teamId);
        if (team.getLeadEmail().equals(userController.getLoginUser()) && userEmail.equals(team.getLeadEmail())){
            return Result.error("You can't join a team you've created !");
        }

        // if in not insert
        if (teamRelationshipService.join(teamId,userEmail) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * search the team by fuzzy query by leader's email and team's name
     * except for the given user already joined
     * @param content means that the search content
     * @param userEmail user's email
     * @return Result
     */
    @RequestMapping("/search")
    public Result searchTeams(String content,String userEmail){
//        log.info("/team/search:content="+content+",userEmail="+userEmail);
        // mark : need improve
        List<TeamDO> list = teamService.searchNotJoined(content,userEmail);
        Result res = new Result();
        res.put("list",list);
        return res;
    }

    /**
     * recommend teams for user
     * @return Result
     */
    @RequestMapping("/recommend")
    public Result getRecommendedTeams(){
        String email = userController.getLoginUser();
        System.out.println("/team/recommend:email="+email);
        List<TeamDO> list = teamService.searchNotJoined("",email);
        List<TeamDO> fList = new LinkedList<>();
        for (TeamDO team:list){
            if (!team.getLeadEmail().equals(email)){
                fList.add(team);
            }
        }
        Result res = new Result();
        res.put("list",fList);
        return res;
    }

    /**
     * get team's members
     * @param teamId team id
     * @return Result
     */
    @RequestMapping("/members")
    public Result getTeamMembers(int teamId){
//        log.info("/team/members:teamId="+teamId);
        List<UserDO> members = teamRelationshipService.getTeamMembers(teamId);
        Result res = new Result();
        res.put("members",members);
        return res;
    }

    /**
     * get the sprint list of the given team
     * @param teamId team's id
     * @return Result
     */
    @RequestMapping("/sprint/list")
    public Result getSprintList(int teamId) {
//        log.info("/team/sprint/list:teamId="+teamId);
        Map<String, Object> map = new HashMap<>();
        map.put("teamId",teamId);
        List<TeamSprintDO> list = teamSprintService.list(map);
        Result res = new Result();
        res.put("list",list);
        return res;
    }

    /**
     * create a sprint for a team
     * @param teamSprintDO sprint information
     * @return Result
     */
    @PostMapping("/sprint/create")
    public Result createTeamSprint(@RequestBody TeamSprintDO teamSprintDO) {
//        log.info("/team/sprint/create:teamSprintDO="+teamSprintDO.toString());
        int id = teamSprintService.create(teamSprintDO);
        if (id > 0){
            Result res = new Result();
            res.put("id",id);
            return res;
        }
        return Result.error();
    }

    /**
     * update the sprint's info, the id is a must
     * @param teamSprintDO sprint's information will update
     * @return Result , ok or error
     */
    @PostMapping("/sprint/update")
    public Result updateTeamSprint(@RequestBody TeamSprintDO teamSprintDO) {
//        log.info("/team/sprint/update:teamSprintDO="+teamSprintDO.toString());
        try {
            if (teamSprintService.update(teamSprintDO) > 0){
                return Result.ok();
            }
            return Result.error();
        }catch (Exception e){
            return Result.error(-1,"conflict");
        }
    }

    /**
     * delete a specific sprint
     * @param params id is a must
     * @return Result
     */
    @PostMapping("/sprint/delete")
    public Result deleteTeamSprint(@RequestBody Map<String,String> params) {
        int id = Integer.parseInt(params.get("id"));
//        log.info("/team/sprint/delete:id="+id);
        if (teamSprintService.delete(id) > 0){
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * get the current sprints of a team
     * @param teamId team's id
     * @return Result
     */
    @RequestMapping("/sprint/current")
    public Result getCurrentSprints(int teamId) {
//        log.info("/team/sprint/current:teamId="+teamId);
        Date dd=new Date();
        String curTime=new SimpleDateFormat("yyyy-MM-dd").format(dd);
        Map<String, Object> map = new HashMap<>();
        map.put("teamId",teamId);
        map.put("curTime",curTime);
        Result res = new Result();
        List<TeamSprintDO> curList = teamSprintService.getCurrentSprints(map);
        res.put("curList",curList);
        return res;
    }

    /**
     * get the current sprints of a team
     * @param params type, team id, and sprint
     * @return Result
     */
    @RequestMapping("/sprint/get")
    public Result getSprint(@RequestBody Map<String,String> params) {
        int id = Integer.parseInt(params.get("id"));
        int sprint = Integer.parseInt(params.get("sprint"));
        String type = params.get("type");
        Result res = new Result();
        TeamSprintDO sprintRes = teamSprintService.getSprintUnique(id, sprint, type);
        res.put("res",sprintRes);
        return res;
    }

}
