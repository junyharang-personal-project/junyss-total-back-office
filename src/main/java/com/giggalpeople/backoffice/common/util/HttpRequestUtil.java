package com.giggalpeople.backoffice.common.util;

import com.giggalpeople.backoffice.common.filter.CachedBodyRequestWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <h2><b>HttpServletRequest에서 들어온 이용자 정보를 파싱하여 전달하기 위한 Util Class</b></h2>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtil {

    public static String getRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(name -> paramMap.put(name, request.getParameter(name)));
        return paramMap;
    }

    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> {
                    if (!name.equals("user-agent")) {
                        headerMap.put(name, request.getHeader(name));
                    }
                });
        return headerMap;
    }

    public static String getAgentDetail(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    public static String getBody(HttpServletRequest httpReq) {
        CachedBodyRequestWrapper nativeRequest = WebUtils.getNativeRequest(httpReq, CachedBodyRequestWrapper.class);

        if (nativeRequest != null) {
            return nativeRequest.getBody();
        }
        return "requestBody 정보 없음";
    }

    public static String getUserIP(HttpServletRequest httpReq) {
        String clientIp = httpReq.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            // Proxy 서버인 경우
            clientIp = httpReq.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            // WebLogic 서버인 경우
            clientIp = httpReq.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = httpReq.getHeader("HTTP_CLIENT_IP");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = httpReq.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = httpReq.getRemoteAddr();
        }
        String[] clientIpList = clientIp.split(",");
        return clientIpList[0];
    }

    /**
     * <b>User IP를 이용하여 https://ipapi.co/를 통해 이용자 접속 위치 정보를 얻기 위한 Method</b>
     * @param request 이용자 요청 정보를 담은 객체
     * @return 이용자 위치 정보를 담은 Map
     */
    public static Map<String, String> getUserLocation(HttpServletRequest request) {
        Map<String, String> locationMap = new HashMap<>();
        String userIP = getUserIP(request);

        String locationFindAPIUrl = "https://ipapi.co/" + userIP + "/json/";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(locationFindAPIUrl, String.class);

        String[] locationEntity = Objects.requireNonNull(response).split(",");

        for (String entity : locationEntity) {
            String[] element = entity.split(":");
            if (element.length == 2) {
                locationMap.put(element[0].replace(" ", "").replace("\n", "").replace("{", "").replace("}", "").replace("\"", ""), element[1].replace(" ", "").replace("\"", ""));
            } else {
                locationMap.put("languages", entity);
            }
        }

        return locationMap;
    }

    /**
     * <b>이용자 쿠키 정보 가져오기 위한 Method</b>
     * @param httpReq 이용자 요청 정보를 담은 객체
     * @return 이용자 쿠키 정보 배열
     */

    public static Cookie[] getUserCookies(HttpServletRequest httpReq) {
        return httpReq.getCookies();
    }
}