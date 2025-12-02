package com.example.holidaykeeper.support.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@WebFilter(urlPatterns = "/api/*")
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // HTTP 요청/응답 객체로 캐스팅
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

        try {
            // 요청 처리 전
            logRequest(requestWrapper);

            // 컨트롤러 및 로직 실행
            filterChain.doFilter(requestWrapper, responseWrapper);

            // 요청 처리 후
            logResponse(requestWrapper, responseWrapper);

        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }

    /**
     * 요청 정보(URI, Method, Body) 로깅
     */
    private void logRequest(ContentCachingRequestWrapper request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";

        log.info(">>>> [REQUEST] {} {}{}", method, url, queryString);

        logContent(request.getContentAsByteArray(), request.getCharacterEncoding(), "Request Body");
    }

    /**
     * 응답 정보(Status, Body) 로깅
     */
    private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        int status = response.getStatus();

        log.info("<<<< [RESPONSE] {} {} | Status: {}", request.getMethod(), request.getRequestURI(), status);

        logContent(response.getContentAsByteArray(), response.getCharacterEncoding(), "Response Body");
    }

    /**
     * 바이트 배열로 된 내용을 문자열로 변환하여 로깅
     */
    private void logContent(byte[] content, String encoding, String logType) {
        if (content.length > 0) {
            String contentString = null;
            try {

                String finalEncoding = StringUtils.hasText(encoding) ? encoding : "UTF-8";
                contentString = new String(content, finalEncoding);

                log.info("  └ {} ({} bytes): \n{}", logType, content.length, contentString.trim());
            } catch (UnsupportedEncodingException e) {
                log.error("Error decoding content for {}: {}", logType, e.getMessage());
            }
        }
    }
}
