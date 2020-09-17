package com.demo.records.controller;

import com.demo.records.vo.NoteVO;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.graph.models.extensions.EmailAddress;
import com.microsoft.graph.models.extensions.ItemBody;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.models.extensions.Recipient;
import com.microsoft.graph.models.generated.BodyType;
import com.demo.records.aad.AuthHelper;
import com.demo.records.domain.NoteDO;
import com.demo.records.domain.ReportDO;
import com.demo.records.domain.TeamDO;
import com.demo.records.service.LoginService;
import com.demo.records.service.NoteService;
import com.demo.records.service.ReportService;
import com.demo.records.service.TeamService;
import com.demo.records.utils.Graph;
import com.demo.records.utils.Query;
import com.demo.records.utils.RedisUtil;
import com.demo.records.utils.Result;
import com.demo.records.vo.RemindVO;
import com.demo.records.vo.ReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    NoteService noteService;

    @Autowired
    ReportService reportService;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    UserController userController;

    @Autowired
    TeamService teamService;

    @Autowired
    LoginService loginService;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private HttpServletResponse httpResponse;

    @Autowired
    private RedisUtil redisUtil;

    @ResponseBody
    @RequestMapping("/list")
    public Result getNotesList(String userId){
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        System.out.println("/note/list:userId="+userId);
        List<NoteDO> list = noteService.list(params);
        List<NoteVO> flist = new ArrayList<>();
//        for (NoteDO note:list){
//            NoteVO nv =  new NoteVO(note);
//            flist.add(nv);
//        }
        Result res = new Result();
        res.put("list",list);
        return res;
    }

    @ResponseBody
    @RequestMapping("/id")
    public Result getNoteById(int id){
//        new HashMap<Integer,Integer>(2).put();
        NoteDO note = noteService.getNoteById(id);
        if (note == null) {
            return Result.error("笔记不存在");
        }
        Result res = new Result();
        res.put("note",note);
        return res;
    }

    @ResponseBody
    @PostMapping(path="/submit")
    public Result submitNote(@RequestBody NoteDO note){
        System.out.println("/note/submit:note="+note.toString());
        Result res = new Result();
        if (noteService.save(note) > 0) {
//            noteService.saveDraft(note);
            int id = note.getId();
            res.put("id", id);
            return res;
        }
        return Result.error();
    }

    @ResponseBody
    @PostMapping(path="/saveDraft")
    public Result saveDraft(@RequestBody NoteDO note){
//        noteService.saveDraft(note);
        return Result.ok();
    }


    @ResponseBody
    @PostMapping("/update")
    public Result updateNote(@RequestBody NoteDO note){
        if (noteService.update(note) > 0){
//            noteService.saveDraft(note);
            return Result.ok();
        }
        return Result.error();
    }

    @ResponseBody
    @PostMapping("/delete")
    public Result deleteNote(@RequestBody Map<String,Object> params){
        System.out.println(params.get("id"));
        int id = (Integer) params.get("id");
        System.out.println("id="+id);
        if (noteService.delete(id) > 0){
            return Result.ok();
        }
        return Result.error();
    }

    @ResponseBody
    @RequestMapping("/tags")
    public Result getTagList(String userId){
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        System.out.println("/note/tags:userId="+userId);
        List<String> list = noteService.getTags(params);
        List<String> tags = new ArrayList<>();
        for (String tt:list){
            String[] tts = tt.split(";");
            tags.addAll(Arrays.asList(tts));
        }
        Result res = new Result();
        res.put("tags",tags);
        return res;
    }

    @ResponseBody
    @PostMapping("/star")
    public Result starNote(@RequestBody Map<String,Object> params){
        System.out.println(params.get("id"));
        int id = (Integer) params.get("id");
        System.out.println("id="+id);
        if (noteService.delete(id) > 0){
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("/content")
    public Result getReportContent(@RequestBody Map<String,Object> params){
        ReportDO report = reportService.getDraft(params);
        if (report == null) {
            report = reportService.getReportDetail(params);
        }
        else {
            System.out.println("redis content:"+report.toString());
        }
        if (report == null) {
            return Result.error(-1,"no found");
        }
        Result res = new Result();
        res.put("id",report.getId());
        res.put("theme",report.getTheme());
        res.put("content",report.getContent());
        res.put("code",1);
        return res;
    }

    @ResponseBody
    @RequestMapping("/aggregate")
    public Result getReportList(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("fromEmail") && !params.containsKey("toEmail")){
            return Result.error("Please give the username!");
        }
        Query query = new Query(params);
//        log.info("/report/aggregate: query="+query.toString());
        List<ReportVO> list = reportService.list(query);
        List<ReportVO> resList = new ArrayList<>();
        for (ReportVO reportVO : list){
            TeamDO curTeam = teamService.getTeamInfo(reportVO.getTeamId());
            if (curTeam == null) {
                continue;
            }
            reportVO.setTeamName(curTeam.getTeamName());
            resList.add(reportVO);
        }
        Result res = new Result();
        res.put("list",resList);
        return res;
    }

    @ResponseBody
    @RequestMapping("/receivedList")
    public Result getReceivedReportList(@RequestBody Map<String, Object> params){
        Query query = new Query(params);
        String toEmail = userController.getLoginUser();
        query.put("toEmail",toEmail);
//        log.info("/report/receivedList: query="+query.toString());
        List<ReportVO> list = reportService.list(query);
        for (ReportVO reportVO : list){
            reportVO.setTeamName(teamService.getTeamInfo(reportVO.getTeamId()).getTeamName());
        }
        Result res = new Result();
        res.put("list",list);
        return res;
    }

    @ResponseBody
    @RequestMapping("/sentList")
    public Result getSentReportList(@RequestBody Map<String, Object> params){
        Query query = new Query(params);
        String fromEmail = userController.getLoginUser();
        query.put("fromEmail",fromEmail);
//        log.info("/report/sentList: query="+query.toString());
        List<ReportVO> list = reportService.list(query);
        for (ReportVO reportVO : list){
            reportVO.setTeamName(teamService.getTeamInfo(reportVO.getTeamId()).getTeamName());
        }
        Result res = new Result();
        res.put("list",list);
        return res;
    }

    /*
        convert linked image to inline image
        from : <img src="https://weekly.omsz.io:3000/7/jiayan.huang@dchdc.net/A05B71F8968B455EA07817D28EB9EA61.png" alt=“upload”  width="60%" height="60%"/>
        to : <img />
     */
    private String parseInline(String s) throws IOException, URISyntaxException {
        int idx = 0;
        StringBuilder sb = new StringBuilder(s);
        while (true) {
            int i = sb.indexOf("<img",idx);
            if (i < 0 || i >= sb.length()) {
                break;
            }
            int start = sb.indexOf("\"",i)+1;
            int end = sb.indexOf("\"",start);
//            System.out.println("before:"+sb.substring(start,end)+"\nbefore end");
            URL url = new URL(sb.substring(start,end));
            BufferedImage img = ImageIO.read(url);
            File f = new File("downloaded.png");
            ImageIO.write(img,"png",f);
            byte[] encode = Base64.encodeBase64(FileUtils.readFileToByteArray(f));
            String replaced = "data:image/png;base64,"+new String(encode, StandardCharsets.US_ASCII);
//            System.out.println("replaced:" + replaced);
            sb.replace(start,end,replaced);
            f.delete();
            idx = i + 1;
        }
        return sb.toString();
    }

    @ResponseBody
    @PostMapping("/sendEmail")
    public Result sendEmail(@RequestBody Map<String, Object> params) throws Throwable {
//        log.info("/report/sendEmail: params="+params.toString());
        Message message = new Message();
        message.subject = params.get("subject").toString();
        ItemBody body = new ItemBody();
        //html way to send
        body.contentType = BodyType.HTML;
        //parse linked images to inline images
        String content = parseInline(params.get("content").toString());
        if (content.getBytes().length > 4e+6) {
            System.out.println("oversize 4mb");
            return Result.error("email oversize");
        }
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        body.content = renderer.render(document);
        String ccList = params.containsKey("cc") && params.get("cc") != null ? params.get("cc").toString() : "";
        //add cc recipients
        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
        String[] ccs = ccList.split(";");
        for (String cc : ccs) {
            if (cc == null || cc.length() == 0) continue;
            Recipient ccRecipient = new Recipient();
            EmailAddress ccEmailAddress = new EmailAddress();
            ccEmailAddress.address = cc.trim();
            ccRecipient.emailAddress = ccEmailAddress;
            ccRecipientsList.add(ccRecipient);
        }
        //
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = params.get("to").toString();
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        message.ccRecipients = ccRecipientsList;
        IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(httpRequest, httpResponse);
        String accessToken=result.accessToken();
        Graph.sendEmail(accessToken,message);
        return Result.ok();
    }

    @ResponseBody
    @RequestMapping("/getRemindAll")
    public Result getRemindList(@RequestBody Map<String, Object> params){
//        log.info("/report/getRemindAll: params="+params.toString());
        String email = userController.getLoginUser();
        List<TeamDO> teamList = new ArrayList<>();
        if (params.get("teamId")!=null && (int)params.get("teamId")>0){
            int teamId = (int)params.get("teamId");
            if (!teamService.getTeamInfo(teamId).getLeadEmail().equals(email)){
                return Result.error("No permission!");
            }
            teamList.add(new TeamDO(teamId));
        }else { // all created team
            teamList = teamService.getCreatedTeamList(email);
        }
        Result res = new Result();
//        List<UserDO> peopleToRemind = reportService.peopleToRemind(params, teamList);
        List<RemindVO> remindList = reportService.getRemindList(params, teamList);
        res.put("peopleToRemind",remindList);
//        res.put("peopleToRemind",peopleToRemind);
        return res;
    }

    @RequestMapping("/download/md")
    public void downloadMD(int id) {
//        log.info("/report/download/md: id="+id);
        ReportVO report = reportService.getReportById(id);
        String fileName = report.getTheme()+".md";
        String content = report.getContent();
        httpResponse.setContentType("text/plain");

        try {
            httpResponse.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ServletOutputStream outputStream = null;
        BufferedOutputStream buffer = null;

        try {
            outputStream = httpResponse.getOutputStream();
            buffer = new BufferedOutputStream(outputStream);
            buffer.write(content.getBytes("UTF-8"));
            buffer.flush();
            buffer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}