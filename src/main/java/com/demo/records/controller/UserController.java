package com.demo.records.controller;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonObject;
import com.microsoft.aad.msal4j.IAccount;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.graph.models.extensions.User;
import com.demo.records.aad.AuthHelper;
import com.demo.records.aad.CookieHelper;
import com.demo.records.aad.SessionManagementHelper;
import com.demo.records.domain.UserDO;
import com.demo.records.service.LoginService;
import com.demo.records.service.UserService;
import com.demo.records.utils.Graph;
import com.demo.records.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping("/visitInfo")
    public Result getUserInfoForVisit(String email) {
        UserDO userDO = userService.findByEmail(email);
        if (userDO == null){
            Result res = new Result();
            res.put("code",404);
            res.put("msg","用户不存在");
            return res;
        }
        if (false){
            return Result.error("用户没有开放主页！");
        }
        Result res =new Result();
        System.out.println(userDO.toString());
        res.put("user",userDO);
        return res;
    }
//    /**
//     * handle login, but should use securePage instead
//     */
//    @ResponseBody
//    @RequestMapping("/login/url")
//    public Result login() throws IOException, JSONException {
//        System.out.println("进入 /login/url");
//        String state = UUID.randomUUID().toString();
//        String nonce = UUID.randomUUID().toString();
//
//        CookieHelper.setStateNonceCookies(httpServletRequest, httpServletResponse, state, nonce);
//
//        httpServletResponse.setStatus(200);
//        String redirectURL = authHelper.getRedirectUriSignIn();
//        String authorizationCodeUrl = authHelper.getAuthorizationCodeUrl(httpServletRequest.getParameter("claims"), null, redirectURL, state, nonce);
//        System.out.println("authorizationCodeUrl = "+authorizationCodeUrl);
//
//        Result res = new Result();
//        res.put("url",authorizationCodeUrl);
//        return res;
//    }
//
//
//    /**
//     * login with AAD
//     * @return Success html and post a access token to the front end
//     */
//    @RequestMapping("/login/secure/aad")
//    public ModelAndView securePage() throws Throwable {
//        log.info("/login/secure/aad");
//        IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(httpServletRequest, httpServletResponse);
//        String accessToken=result.accessToken();
//        try{
//            String email = getLoginUser();
//            String username = userService.getUsernameFromEmail(email);
////            User user = Graph.getUser(accessToken);
////            Graph.getPhoto(accessToken);
////            String email = (user.mail == null || user.mail.equals("")) ? user.userPrincipalName : user.mail;
////            if(user.displayName == null || user.displayName.length() == 0 || email == null || email.length() == 0
////                    || !email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
////                throw new Exception("The email address is invalid. Please try again with another account!");
////            }
////            System.out.println("username="+user.displayName+",email="+email);
//            // save user info into our database at the first time login
////            UserDO userDO = loginService.login(email,user.displayName);
////            UserDO userDO = loginService.login(email,username);
//            // Microsoft employee
//            if (email.split("@")[1] != null && email.split("@")[1].equals("microsoft.com")){
//                userService.addWhitelist(new UserDO(email,username));
//            }
//            UserDO userDO = userService.findByEmail(email);
//            if (userDO == null || userDO.getType() <= 0 || userDO.getStatusCode() == UserDO.USER_STATUS_DELETED) {
//                throw new Exception("Your account has no permissions, please contact the administrator");
//            }
//
//            Result res = new Result();
////            JsonObject json = user.getRawObject();
//            //set header to allow cross origin
//            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
//            res.put("Access-Control-Allow-Origin", "*");
//            res.put("access_token",accessToken);
//            res.put("user",userDO.toString());
//            res.put("username",username);
//            res.put("email",email);
//            res.put("token",accessToken);
//            res.put("type",userDO.getType());
//            res.put("avatar",userDO.getAvatar());
//            ModelAndView mv = new ModelAndView("success");
//            mv.addObject("res",res.toJSONString());
//            return mv;
//        }catch (Exception e){
//            this.logout();
//            ModelAndView mv = new ModelAndView("error");
//            System.out.println("/login/secure/aad Exception");
//            mv.addObject("error",e.getMessage());
//            return mv;
//        }
//    }
//
//    /**
//     * log out
//     * @return status code
//     * @throws IOException
//     */
//    @ResponseBody
//    @RequestMapping("/logout")
//    public Result logout() throws IOException {
//        System.out.println("/logout");
//        Graph.logoutGraphClient();
//        httpServletRequest.getSession().invalidate();
////        String endSessionEndpoint = "https://login.microsoftonline.com/common/oauth2/v2.0/logout";
////        String redirectUrl = "http://localhost:3000";
////        httpServletResponse.sendRedirect(endSessionEndpoint + "?post_logout_redirect_uri=" +
////                URLEncoder.encode(redirectUrl, "UTF-8"));
//        return Result.ok();
//    }
//
//    /**
//     *
//     * @return user's information in json format
//     * But haven't got used now
//     */
//    @RequestMapping("/user/info")
//    public String getUserInfo()
//            throws Throwable {
//
//        IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(httpServletRequest, httpServletResponse);
//        String accessToken=result.accessToken();
//
//        User user = Graph.getUser(accessToken);
//        System.out.println("/user/info : " + user.displayName);
//
//        JsonObject json = user.getRawObject();
//        return json.toString();
//    }
//
//
//    /**
//     *
//     * @return whether is log in
//     */
//    @ResponseBody
//    @RequestMapping("/login/isLogin")
//    public Result isLogin() {
//        System.out.println("进入 /login/isLogin");
//        boolean flag = httpServletRequest.getSession().getAttribute(AuthHelper.PRINCIPAL_SESSION_NAME) != null;
//        return Result.ok(String.valueOf(flag));
//    }
//
//
//    @RequestMapping("/user/search")
//    public Result searchUsers(String content){
//        System.out.println("/user/search"+content);
//        List<UserDO> list = userService.search(content);
//        Result res = new Result();
//        res.put("list",list);
//        return res;
//    }
//
//    @PostMapping("/user/updateName")
//    public Result updateUserName(@RequestBody Map<String,String> params) {
//        String email = getLoginUser();
//        String username = params.get("username");
//        if (userService.updateName(email,username) > 0) {
//            return Result.ok();
//        }
//        return Result.error();
//    }
//
//    /**
//     * get Recommended Users for the logged user.
//     * @return Result
//     */
//    @RequestMapping("/user/recommend")
//    public Result getRecommendedUsers(){
//        String email = getLoginUser();
//        System.out.println("/user/recommend:email="+email);
//        List<UserDO> list = userService.search("");
//        Result res = new Result();
//        res.put("list",list);
//        return res;
//    }
//
//    @RequestMapping("/me/people")
//    public Result searchUsersInTeams(String search) throws Throwable {
//        IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(httpServletRequest, httpServletResponse);
//        String accessToken=result.accessToken();
//
//        Result res = new Result();
//        List<UserDO> ulist = Graph.searchUsers(accessToken,search);
//        res.put("list",ulist);
//        return res;
//    }
//
//    /**
//     *
//     * @return email of current log-in user
//     */
    public String getLoginUser() {
        IAccount account =  SessionManagementHelper.getAuthSessionObject(httpServletRequest).account();
        return account.username();
    }
//
//
//    /**
//     * this method is for update code on the server
//     */
//    @RequestMapping("/updateCode")
//    public void testAuto() throws IOException, InterruptedException {
//        String bashCommand = "/home/omsz/Statusly/deployed.sh";
//        Runtime runtime = Runtime.getRuntime();
//        Process pro = runtime.exec(bashCommand);
//        int status = pro.waitFor();
//        if (status != 0){
//            log.info("restart go server error");
//            return;
//        }
//        log.info("restart go server success");
//    }
//
//    @RequestMapping("/group/members")
//    public Result getGroupMembers() throws Throwable {
//        IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(httpServletRequest, httpServletResponse);
//        String accessToken=result.accessToken();
//
//        Graph.getGroupMembers(accessToken,"10ba6316-0bd3-4ecc-b9b1-7cda6326fb93");
//        return Result.ok();
//    }
//

}
