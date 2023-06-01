package com.giggalpeople.backoffice.common.filter;

import com.giggalpeople.backoffice.chatops.logback.appender.util.MDCUtil;
import com.giggalpeople.backoffice.common.util.HttpRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * <h2><b>모든 Servlet에 일관된 요청을 처리하기 위한 OncePerRequestFilter 상속한 Filter Class</b></h2>
 * <b>HTTP Request 정보를 MDC(Mapped Diagnostic Context)에 저장하는 Filter</b>
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpReq = WebUtils.getNativeRequest(request, HttpServletRequest.class);

        MDCUtil.setJsonValue(MDCUtil.REQUEST_URI_MDC, HttpRequestUtil.getRequestUri(Objects.requireNonNull(httpReq)));
        MDCUtil.setJsonValue(MDCUtil.USER_IP_MDC, HttpRequestUtil.getUserIP(Objects.requireNonNull(httpReq)));
        MDCUtil.setJsonValue(MDCUtil.USER_LOCATION_MDC, HttpRequestUtil.getUserLocation(httpReq));
        MDCUtil.setJsonValue(MDCUtil.HEADER_MAP_MDC, HttpRequestUtil.getHeaderMap(httpReq));
        MDCUtil.setJsonValue(MDCUtil.USER_REQUEST_COOKIES, HttpRequestUtil.getUserCookies(httpReq));
        MDCUtil.setJsonValue(MDCUtil.PARAMETER_MAP_MDC, HttpRequestUtil.getParamMap(httpReq));
        MDCUtil.setJsonValue(MDCUtil.USER_AGENT_DETAIL_MDC, HttpRequestUtil.getAgentDetail(httpReq));
        MDCUtil.setJsonValue(MDCUtil.BODY_MDC, HttpRequestUtil.getBody(httpReq));

        filterChain.doFilter(request, response);
    }
}

