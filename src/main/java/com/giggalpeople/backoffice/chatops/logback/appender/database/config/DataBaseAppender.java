package com.giggalpeople.backoffice.chatops.logback.appender.database.config;

import com.giggalpeople.backoffice.api.log.model.dto.request.TotalErrorLogSaveRequestDTO;
import com.giggalpeople.backoffice.chatops.discord.chatbot.common.BackOfficeAPICaller;
import com.giggalpeople.backoffice.chatops.logback.appender.util.MDCUtil;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.env.Environment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * <h2><b>Data Base에 Log 저장을 위한 Appender</b></h2>
 */

public class DataBaseAppender {

    /**
     * <b>Data Base에 Log 저장을 위한 Method로 Discord Appender를 통해 축척된 Log 정보를 LogRequestDTO에 넣고, API 호출 Method 호출</b>
     *
     * @param level           log Level
     * @param exceptionDetail Exception 상세 정보
     * @param serverInfo Exception 발생 Server Application 정보
     * @param mdcPropertyMap  Exception 발생 시 문제 내용 및 이용자 정보를 담은 Map
     * @throws IOException logSaveAPICall의 JSONObject 처리에 문제 발생 시 발생하는 Exception
     */
    public void inDBInsert(String level, String exceptionBrief, String exceptionDetail, ServerInfo serverInfo, Map<String, String> mdcPropertyMap) throws IOException {
        TotalErrorLogSaveRequestDTO totalErrorLogSaveRequestDTO = new TotalErrorLogSaveRequestDTO();
        totalErrorLogSaveRequestDTO.setServerEnvironment(Environment.checkServerEnvironment());
        totalErrorLogSaveRequestDTO.setLevel(level);
        totalErrorLogSaveRequestDTO.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        totalErrorLogSaveRequestDTO.setServerName(serverInfo.getServerName());
        totalErrorLogSaveRequestDTO.setVmInfo(serverInfo.getVmInfo());
        totalErrorLogSaveRequestDTO.setOsInfo(serverInfo.getOsInfo());
        totalErrorLogSaveRequestDTO.setServerIP(serverInfo.getServerIP());
        totalErrorLogSaveRequestDTO.setServerEnvironment(serverInfo.getServerEnvironment());

        for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.equals(MDCUtil.USER_IP_MDC)) {
                totalErrorLogSaveRequestDTO.setUserIp(value);
            } else if (key.equals(MDCUtil.USER_LOCATION_MDC)) {
                totalErrorLogSaveRequestDTO.setUserLocation(value);
            } else if (key.equals(MDCUtil.HEADER_MAP_MDC)) {
                totalErrorLogSaveRequestDTO.setRequestHeader(value);
            } else if (key.equals(MDCUtil.PARAMETER_MAP_MDC)) {
                totalErrorLogSaveRequestDTO.setRequestParameter(value);
            } else if (key.equals(MDCUtil.USER_AGENT_DETAIL_MDC)) {
                totalErrorLogSaveRequestDTO.setUserEnvironment(value);
            } else if (key.equals(MDCUtil.BODY_MDC)) {
                totalErrorLogSaveRequestDTO.setRequestBody(value);
            }

            if (exceptionBrief != null) {
                totalErrorLogSaveRequestDTO.setExceptionBrief(exceptionBrief);
            }

            if (exceptionDetail != null) {
                totalErrorLogSaveRequestDTO.setExceptionDetail(exceptionDetail);
            }
        }

        BackOfficeAPICaller.logSaveAPICall(totalErrorLogSaveRequestDTO);
    }
}
