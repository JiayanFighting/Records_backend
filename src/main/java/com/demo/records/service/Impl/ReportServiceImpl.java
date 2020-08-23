package com.demo.records.service.Impl;

import com.demo.records.dao.ReportMapper;
import com.demo.records.domain.ReportDO;
import com.demo.records.domain.TeamDO;
import com.demo.records.domain.TeamSprintDO;
import com.demo.records.domain.UserDO;
import com.demo.records.service.ReportService;
import com.demo.records.service.TeamService;
import com.demo.records.service.TeamSprintService;
import com.demo.records.utils.RedisUtil;
import com.demo.records.vo.RemindVO;
import com.demo.records.vo.ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    public static final String REDIS_DRAFT_REPORTS_KEY_PREFIX = "draft";
    public static final int REDIS_EXPIRED_TIME = 86400;

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamSprintService teamSprintService;

    @Autowired
    private RedisUtil redisUtil;
    @Override
    public void submit(ReportDO report) {

    }

    @Override
    public List<ReportVO> list(Map<String, Object> map) {
        return reportMapper.list(map);
    }

    @Override
    public int save(ReportDO report) {
        return reportMapper.save(report);
    }

    @Override
    public List<UserDO> peopleToRemind(Map<String, Object> params, List<TeamDO> createdList) {
        List<UserDO> res = new ArrayList<>();

        List<UserDO> users = new ArrayList<>();

        List<ReportVO> reportDOS = list(params);
        Set<String> reportSet = new HashSet<>();
        for (ReportVO report : reportDOS) {
            reportSet.add(report.getFromEmail());
        }

        for (TeamDO team : createdList) {
            users.addAll(teamService.getTeamMembers(team.getId()));
        }

        Set<String> resDeDup = new HashSet<>();
        for (UserDO cur : users) {
            String curEmail = cur.getEmail();
            if (!reportSet.contains(curEmail)) {
                if (resDeDup.add(curEmail)) {
                    res.add(cur);
                }
            }
        }

        return res;
    }

    @Override
    public List<RemindVO> getRemindList(Map<String, Object> params, List<TeamDO> teamList) {
        HashMap<String,RemindVO> list = new HashMap<>();
        Date dd=new Date();
        String curTime=new SimpleDateFormat("yyyy-MM-dd").format(dd);
        Map<String, Object> sprintParams = new HashMap<>();
        sprintParams.put("curTime",curTime);
        sprintParams.put("type",params.get("type"));
        sprintParams.put("sprint",params.get("sprint"));
//        System.out.println("-=================================");
        for (TeamDO team:teamList) {
//            System.out.println("teamiD = "+team.getId()+" "+team.getTeamName());
            sprintParams.put("teamId",team.getId());
            List<TeamSprintDO> sprints = teamSprintService.getCurrentSprints(sprintParams);
            for (TeamSprintDO sprint:sprints){
                Map<String, Object> unParams = new HashMap<>();
                unParams.put("teamId",team.getId());
                unParams.put("type",sprint.getType());
                unParams.put("sprint",sprint.getSprint());
                List<UserDO> users = reportMapper.getUnReportUsers(unParams);
//                System.out.println("sprint = "+sprint.getSprint()+" "+sprint.getType());
                for (UserDO user:users){
//                    System.out.println("user = "+user.getEmail());
                    if (list.containsKey(user)){
                        ArrayList<Map<String,Object>> alist = list.get(user).getList();
                        alist.add(unParams);
                        list.put(user.getEmail(),new RemindVO(user,alist));
                    }else{
                        ArrayList<Map<String,Object>> alist = new ArrayList<>();
                        alist.add(unParams);
                        list.put(user.getEmail(),new RemindVO(user,alist));
                    }
                }
            }
        }
        List<RemindVO> res= new ArrayList<>();
//        System.out.println("-=========================");
        for (String key:list.keySet()){
//            System.out.println("user ="+key);
            ArrayList<Map<String,Object>> list22 = list.get(key).getList();
            for (Map<String,Object> mm :list22){
//                System.out.println(mm.get("teamId")+" "+mm.get("type")+" "+mm.get("sprint"));
            }
            res.add(list.get(key));
        }
//        System.out.println("-=========================");
        return res;
    }

    @Override
    public ReportVO getReportById(int id) {
        return reportMapper.getReportById(id);
    }

    @Override
    public int update(ReportDO report) {
        return reportMapper.update(report);
    }

    @Override
    public ReportDO getReportDetail(Map<String, Object> map) {
        return reportMapper.getReportDetail(map);
    }

    @Override
    public int saveDraft(ReportDO reportDO) {
        try {
            String key = getDraftKey(reportDO.getFromEmail(),reportDO.getTeamId(),reportDO.getType(),reportDO.getSprint());
            redisUtil.hset(key,"theme",reportDO.getTheme());
            redisUtil.hset(key,"content",reportDO.getContent());
            redisUtil.expire(key,REDIS_EXPIRED_TIME);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public ReportDO getDraft(Map<String, Object> params) {
        String user = (String) params.get("fromEmail");
        int teamId = (int) params.get("teamId");
        String type = (String) params.get("type");
        int sprint = params.get("sprint") == null ?-1:(int) params.get("sprint");
        String key = getDraftKey(user,teamId,type,sprint);
        if (redisUtil.hasKey(key)) {
            String theme = (String) redisUtil.hget(key,"theme");
            String content = (String) redisUtil.hget(key,"content");
            return new ReportDO(user,teamId,sprint,type,theme,content);
        }
        System.out.println("no key");
        return null;
    }

    @Override
    public int deleteDraft(Map<String, Object> params) {
        String user = (String) params.get("user");
        int teamId = (int) params.get("teamId");
        String type = (String) params.get("type");
        int sprint = (int) params.get("sprint");
        String key = getDraftKey(user,teamId,type,sprint);
        redisUtil.del(key);
        return 1;
    }

    public String getDraftKey(String user,int teamId,String type,int sprint) {
        return REDIS_DRAFT_REPORTS_KEY_PREFIX+":"+user
                +":"+teamId+":"+type+":"+sprint;
    }


}
