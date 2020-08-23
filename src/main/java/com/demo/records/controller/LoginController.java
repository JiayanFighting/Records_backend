package com.demo.records.controller;

import com.demo.records.aad.AuthHelper;
import com.demo.records.domain.UserDO;
import com.demo.records.service.LoginService;
import com.demo.records.service.UserService;
import com.demo.records.utils.Graph;
import com.demo.records.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    /**
     * login with AAD
     * @return Success html and post a access token to the front end
     */
    @RequestMapping("/login")
    public Result login(String email,String password) throws Throwable {
        try{
            UserDO userDO = loginService.login(email,password);
            if (userDO == null || userDO.getType() <= 0 || userDO.getStatusCode() == UserDO.USER_STATUS_DELETED) {
                throw new Exception("Your account has no permissions, please contact the administrator");
            }else {
                Result res = new Result();
                res.put("userId",userDO.getId());
                res.put("username",userDO.getUsername());
                res.put("email",userDO.getEmail());
                res.put("type",userDO.getType());
                res.put("avatar",userDO.getAvatar());
                res.put("access_token","accessToken");
                res.put("token","token");
                return res;
            }
        }catch (Exception e){
            this.logout();
            ModelAndView mv = new ModelAndView("error");
            System.out.println("/login/secure/aad Exception");
            mv.addObject("error",e.getMessage());
            return Result.error("no user");
        }
    }

    /**
     * log out
     * @return status code
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/logout2")
    public Result logout() throws IOException {
        System.out.println("/logout");
        Graph.logoutGraphClient();
        httpServletRequest.getSession().invalidate();
//        String endSessionEndpoint = "https://login.microsoftonline.com/common/oauth2/v2.0/logout";
//        String redirectUrl = "http://localhost:3000";
//        httpServletResponse.sendRedirect(endSessionEndpoint + "?post_logout_redirect_uri=" +
//                URLEncoder.encode(redirectUrl, "UTF-8"));
        return Result.ok();
    }


}
