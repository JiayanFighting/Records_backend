// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.demo.records.aad;

import com.microsoft.aad.msal4j.IAuthenticationResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Helpers for managing session
 */
public class SessionManagementHelper {

    static final String FAILED_TO_VALIDATE_MESSAGE = "Failed to validate data received from Authorization service - ";

    static void storeTokenCacheInSession(HttpServletRequest httpServletRequest, String tokenCache){
        httpServletRequest.getSession().setAttribute(AuthHelper.TOKEN_CACHE_SESSION_ATTRIBUTE, tokenCache);
    }

    static void setSessionPrincipal(HttpServletRequest httpRequest, IAuthenticationResult result) {
        httpRequest.getSession().setAttribute(AuthHelper.PRINCIPAL_SESSION_NAME, result);
    }

    static void removePrincipalFromSession(HttpServletRequest httpRequest) {
        httpRequest.getSession().removeAttribute(AuthHelper.PRINCIPAL_SESSION_NAME);
    }

    public static IAuthenticationResult getAuthSessionObject(HttpServletRequest request) {
        Object principalSession = request.getSession().getAttribute(AuthHelper.PRINCIPAL_SESSION_NAME);
        if(principalSession instanceof IAuthenticationResult){
            return (IAuthenticationResult) principalSession;
        } else {
            throw new IllegalStateException("Session does not contain principal session name");
        }
    }
}
