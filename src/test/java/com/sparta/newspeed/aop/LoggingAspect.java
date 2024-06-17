package com.sparta.newspeed.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private HttpServletRequest request;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
        // Pointcut for all methods in classes annotated with @RestController
    }

    @Before("controllerMethods()")
    public void logRequestMethodAndURL(JoinPoint joinPoint) {
        String httpMethod = request.getMethod();
        String requestURL = request.getRequestURL().toString();
        logger.info("HTTP Method: {}, Request URL: {}", httpMethod, requestURL);
    }
}