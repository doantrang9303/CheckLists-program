package com.ya3k.checklist.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;
@Component

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Generate UUID for the request
        String uuid = UUID.randomUUID().toString();
        request.setAttribute("requestId", uuid);

        // Get HTTP method
        String httpMethod = request.getMethod();
        // Log the request information
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String methodName = handlerMethod.getMethod().getName();
            logger.info("Request ID: {}, HTTP Method: {}, URI: {}", uuid, httpMethod, request.getRequestURI());
        } else {
            logger.info("Request ID: {}, HTTP Method: {}, URI: {}", uuid, "Unknown", request.getRequestURI());
        }
        return true;
    }
}
