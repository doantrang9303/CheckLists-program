package com.ya3k.checklist.interceptor;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.UUID;

@Component
@Order(1)
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_ID = "requestId";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Generate UUID
        String uuid = UUID.randomUUID().toString().substring(0,6);

        // Store UUID in MDCxv
        MDC.put(REQUEST_ID, uuid);

        // Wrap request and response to read multiple times
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            // Pass wrapped request and response through the filter chain
            filterChain.doFilter(wrappedRequest, wrappedResponse);

            // Log request
            logRequest(wrappedRequest);

            // Log response
            logResponse(wrappedResponse);

            // Copy the content of the response back to the original response
            wrappedResponse.copyBodyToResponse();
        } finally {
            // Remove UUID from MDC after request processing is complete
            MDC.remove("uuid");
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        logger.info("REQUEST:  HTTP method: {} , URL:{}, BODY:{}",request.getMethod(),request.getRequestURI(),getContentAsString(request.getContentAsByteArray()));
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        logger.info("RESPONSE:  Status: {}, BODY:{}",response.getStatus(),getContentAsString(response.getContentAsByteArray()));
    }

    private String getContentAsString(byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }
        return new String(content, StandardCharsets.UTF_8);
    }
}
