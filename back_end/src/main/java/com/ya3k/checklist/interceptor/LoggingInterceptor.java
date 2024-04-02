package com.ya3k.checklist.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.MDC;
import java.util.UUID;
@Component

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Generate UUID for the request
        String uuid = UUID.randomUUID().toString().substring(0,6);
//            String id=uuid.substring(0,6);
//            request.setAttribute("requestId", uuid);
        MDC.put(REQUEST_ID, uuid); // Set request ID in MDC

        // Get HTTP method
        String httpMethod = request.getMethod();
        // Log the request information
        if (handler instanceof HandlerMethod handlerMethod) {
            String methodName = handlerMethod.getMethod().getName();
            logger.info("HTTP Method: {}, URI: {}", httpMethod, request.getRequestURI());
            // logger.info("Request ID: {}, HTTP Method: {}, URI: {}", uuid, httpMethod, request.getRequestURI());
        } else {
            logger.info("HTTP Method: {}, URI: {}", "Unknown", request.getRequestURI());
        }
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(REQUEST_ID); // Remove request ID from MDC after completion
    }
}

