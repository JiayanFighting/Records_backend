package com.demo.records.utils;

import com.microsoft.aad.msal4j.IAccount;
import com.demo.records.aad.SessionManagementHelper;
import com.demo.records.controller.WhitelistController;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    WhitelistController whitelistController;
    @Pointcut("execution(* com.demo.records.controller.*.*(..)) && !execution(* com.demo.records.controller.UserController.getLoginUser*(..))")
    public void cutMethod(){
    }

    @Before("cutMethod()")
    public void beforeClass(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String path = request.getServletPath();
        // get params
        Object[] args = joinPoint.getArgs();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = pnd.getParameterNames(method);
        StringBuilder params = new StringBuilder();
        params.append("{");
        if (parameterNames!=null && parameterNames.length>0){
            for (int i = 0; i < parameterNames.length; i++) {
                params.append(parameterNames[i]).append("=").append(args[i]);
                if (i<parameterNames.length-1){
                    params.append(",");
                }
            }
        }
        params.append("}");
        String user = "";
        try {
            IAccount account =  SessionManagementHelper.getAuthSessionObject(request).account();
            user = account.username();
        }catch (Exception e){

        }
        log.info("operator="+user+"|url="+path+"|method="+request.getMethod()+"|classMethod="+joinPoint.getSignature().getName()+"|params="+params.toString());
    }
}
