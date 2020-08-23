// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.demo.records.aad;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.MsalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Processes incoming requests based on auth status
 */
@Slf4j
@CrossOrigin
@Component
public class AuthFilter implements Filter {

    private List<String> excludedUrls = Arrays.asList("/", "/logout","/login/url","/updateCode");

    @Autowired
    AuthHelper authHelper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            try {
                String currentUri = httpRequest.getRequestURL().toString();
                String path = httpRequest.getServletPath();
                String queryStr = httpRequest.getQueryString();
                String fullUrl = currentUri + (queryStr != null ? "?" + queryStr : "");
                if (true){
                    chain.doFilter(request,response);
                    return;
                }
                // exclude home page
                if(excludedUrls.contains(path)){
                    chain.doFilter(request, response);
                    return;
                }

                if(containsAuthenticationCode(httpRequest)){
                    log.info("containsAuthenticationCode : true");
                    // response should have authentication code, which will be used to acquire access token
                    authHelper.processAuthenticationCodeRedirect(httpRequest, currentUri, fullUrl);
                    CookieHelper.removeStateNonceCookies(httpResponse);

                    chain.doFilter(request, response);
                    return;
                }
                log.info("containsAuthenticationCode : false");
                // check if user has a AuthData in the session
                if (!isAuthenticated(httpRequest)) {
                    log.info("isAuthenticated : false ");
                    if (path.equals("/login/secure/aad")){
                        log.info("path = /login/secure/aad");
                        // not authenticated, redirecting to login.microsoft.com so user can authenticate
                        authHelper.sendAuthRedirect(
                                httpRequest,
                                httpResponse,
                                AuthHelper.SCOPES,
                                authHelper.getRedirectUriSignIn());
                        return;
                    }else{
                        log.info("Other situations requiring login ！");
                        httpResponse.setStatus(302);
                        return;
                    }

                }
                log.info("isAuthenticated : true ");
                if (isAccessTokenExpired(httpRequest)) {
                    updateAuthDataUsingSilentFlow(httpRequest, httpResponse);
                }
            } catch (MsalException authException) {
                // something went wrong (like expiration or revocation of token)
                // we should invalidate AuthData stored in session and redirect to Authorization server
                SessionManagementHelper.removePrincipalFromSession(httpRequest);
                authHelper.sendAuthRedirect(
                        httpRequest,
                        httpResponse,
                        AuthHelper.SCOPES,
                        authHelper.getRedirectUriSignIn());
                return;
            } catch (Throwable exc) {
                log.info("Throwable exc :"+exc.getMessage());
                httpResponse.setStatus(500);
                request.setAttribute("error", exc.getMessage());
                request.getRequestDispatcher("/error").forward(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean containsAuthenticationCode(HttpServletRequest httpRequest) {
        Map<String, String[]> httpParameters = httpRequest.getParameterMap();

        boolean isPostRequest = httpRequest.getMethod().equalsIgnoreCase("POST");
        boolean containsErrorData = httpParameters.containsKey("error");
        boolean containIdToken = httpParameters.containsKey("id_token");
        boolean containsCode = httpParameters.containsKey("code");

        return isPostRequest && containsErrorData || containsCode || containIdToken;
    }

    private boolean isAccessTokenExpired(HttpServletRequest httpRequest) {
        IAuthenticationResult result = SessionManagementHelper.getAuthSessionObject(httpRequest);
        return result.expiresOnDate().before(new Date());
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        return request.getSession().getAttribute(AuthHelper.PRINCIPAL_SESSION_NAME) != null;
    }

    private void updateAuthDataUsingSilentFlow(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Throwable {
        IAuthenticationResult authResult = authHelper.getAuthResultBySilentFlow(httpRequest, httpResponse);
        SessionManagementHelper.setSessionPrincipal(httpRequest, authResult);
    }
}
