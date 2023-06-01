package com.giggalpeople.backoffice.common.aop;

import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.service.ConnectedUserInfoService;
import com.giggalpeople.backoffice.common.env.Environment;
import com.giggalpeople.backoffice.common.util.CryptoUtil;
import com.giggalpeople.backoffice.common.util.HttpRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * <h2><b>이용자 접속 정보 확인하기 위한 AOP</b></h2>
 */

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserAccessInfoAspect {

    private final HttpServletRequest httpServletRequest;
    private final ConnectedUserInfoService connectedUserInfoService;

    @Around("@annotation(com.giggalpeople.backoffice.common.annotaion.UserAccessInfoCheck)")
    public Object checkAccessInfo(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest httpRequest = WebUtils.getNativeRequest(httpServletRequest, HttpServletRequest.class);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

        log.info("접속 이용자 정보 및 요청 정보 저장 시작 합니다.");

        try {
            return joinPoint.proceed();

        } finally {
            connectedUserInfoService.save(UserRequestTotalInfoSaveRequestDTO.builder()
                    .serverInfo(Environment.getServerInfo())
                    .dataCreatedDateTimeRequestDTO(
                            DataCreatedDateTimeRequestDTO.builder()
                                    .createdDate(splitNowDateTime[0])
                                    .createdTime(splitNowDateTime[1])
                                    .build())

                    .connectedUserInfoSaveRequestDTO(
                            CryptoUtil.userInfoEncrypt(
                                    ConnectedUserInfoSaveRequestDTO.builder()
                                            .userIP(HttpRequestUtil.getUserIP(Objects.requireNonNull(httpRequest)))
                                            .userLocation(HttpRequestUtil.getUserLocation(httpRequest).toString())
                                            .userEnvironment(HttpRequestUtil.getAgentDetail(httpRequest))
                                            .build()))

                    .connectedUserRequestInfoSaveRequestDTO(
                            CryptoUtil.userRequestInfoEncrypt(
                            ConnectedUserRequestInfoSaveRequestDTO.builder()
                                    .userCookiesArray(HttpRequestUtil.getUserCookies(httpRequest))
                                    .requestHeader(HttpRequestUtil.getHeaderMap(httpRequest).toString())
                                    .requestParameter(HttpRequestUtil.getParamMap(httpRequest).toString())
                                    .requestBody(HttpRequestUtil.getBody(httpRequest))
                                    .build()))
                    .build());

            log.info("접속 이용자 정보 및 요청 정보 저장 완료 하였습니다.");
        }


//        connectedUserInfoDataBaseAppender.append(dataCreatedDateTimeRequestDTO, errorLogUserInfoSaveRequestDTO, userRequestInfoSaveRequestDTO);
    }
}
