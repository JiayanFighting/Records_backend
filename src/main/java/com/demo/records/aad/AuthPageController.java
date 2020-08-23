// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.demo.records.aad;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.MsalInteractionRequiredException;
import com.nimbusds.jwt.JWTParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@Controller
public class AuthPageController {

    @Autowired
    AuthHelper authHelper;

    @RequestMapping("/login2")
    public String homepage(){
        System.out.println("entered auth page");
        return "index";
    }

    @RequestMapping("/login/secure")
    public ModelAndView securePage(HttpServletRequest httpRequest) throws ParseException {
        System.out.println("entered auth page");
        ModelAndView mav = new ModelAndView("auth_page");

        setAccountInfo(mav, httpRequest);

        return mav;
    }

    @RequestMapping("/login/sign_out")
    public void signOut(HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {

        httpRequest.getSession().invalidate();

        String endSessionEndpoint = "https://login.microsoftonline.com/common/oauth2/v2.0/logout";

        String redirectUrl = "http://localhost:8080/login/";
        response.sendRedirect(endSessionEndpoint + "?post_logout_redirect_uri=" +
                URLEncoder.encode(redirectUrl, "UTF-8"));
    }

    @RequestMapping("/login/graph/me")
    public ModelAndView getUserFromGraph(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Throwable {

        IAuthenticationResult result;
        ModelAndView mav;
        try {
            result = authHelper.getAuthResultBySilentFlow(httpRequest, httpResponse);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof MsalInteractionRequiredException) {

                // If silent call returns MsalInteractionRequired, then redirect to Authorization endpoint
                // so user can consent to new scopes
                String state = UUID.randomUUID().toString();
                String nonce = UUID.randomUUID().toString();

                CookieHelper.setStateNonceCookies(httpRequest, httpResponse, state, nonce);

                String authorizationCodeUrl = authHelper.getAuthorizationCodeUrl(
                        httpRequest.getParameter("claims"),
                        "",
                        authHelper.getRedirectUriGraph(),
                        state,
                        nonce);//User.Read

                return new ModelAndView("redirect:" + authorizationCodeUrl);
            } else {

                mav = new ModelAndView("error");
                mav.addObject("error", e);
                return mav;
            }
        }

        if (result == null) {
            mav = new ModelAndView("error");
            mav.addObject("error", new Exception("AuthenticationResult not found in session."));
        } else {
            mav = new ModelAndView("auth_page");
            setAccountInfo(mav, httpRequest);

            try {
                mav.addObject("userInfo", getUserInfoFromGraph(result.accessToken()));

                return mav;
            } catch (Exception e) {
                mav = new ModelAndView("error");
                mav.addObject("error", e);
            }
        }
        return mav;
    }

    private String getUserInfoFromGraph(String accessToken) throws Exception {
        // Microsoft Graph user endpoint
        URL url = new URL(authHelper.getMsGraphEndpointHost() + "v1.0/me");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the appropriate header fields in the request header.
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        String response = HttpClientHelper.getResponseStringFromConn(conn);

        int responseCode = conn.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException(response);
        }

        JSONObject responseObject = HttpClientHelper.processResponse(responseCode, response);
        return responseObject.toString();
    }

    private void setAccountInfo(ModelAndView model, HttpServletRequest httpRequest) throws ParseException {
        IAuthenticationResult auth = SessionManagementHelper.getAuthSessionObject(httpRequest);

        String tenantId = JWTParser.parse(auth.idToken()).getJWTClaimsSet().getStringClaim("tid");

        model.addObject("tenantId", tenantId);
        model.addObject("account", SessionManagementHelper.getAuthSessionObject(httpRequest).account());
    }
}
