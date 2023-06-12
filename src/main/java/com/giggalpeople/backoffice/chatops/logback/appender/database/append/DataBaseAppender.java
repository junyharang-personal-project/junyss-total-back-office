package com.giggalpeople.backoffice.chatops.logback.appender.database.append;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorRecordSaveRequestDto;
import com.giggalpeople.backoffice.chatops.discord.chatbot.common.BackOfficeAPICaller;
import com.giggalpeople.backoffice.chatops.logback.appender.util.MDCUtil;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.env.Environment;

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
	public void inDBInsert(String level, String exceptionBrief, String exceptionDetail, ServerInfo serverInfo,
		Map<String, String> mdcPropertyMap) throws IOException {
		TotalErrorRecordSaveRequestDto totalErrorRecordSaveRequestDTO = new TotalErrorRecordSaveRequestDto();
		totalErrorRecordSaveRequestDTO.setServerEnvironment(Environment.checkServerEnvironment());
		totalErrorRecordSaveRequestDTO.setLevel(level);
		totalErrorRecordSaveRequestDTO.setCreatedAt(
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		totalErrorRecordSaveRequestDTO.setServerName(serverInfo.getServerName());
		totalErrorRecordSaveRequestDTO.setVmInfo(serverInfo.getVmInfo());
		totalErrorRecordSaveRequestDTO.setOsInfo(serverInfo.getOsInfo());
		totalErrorRecordSaveRequestDTO.setServerIP(serverInfo.getServerIP());
		totalErrorRecordSaveRequestDTO.setServerEnvironment(serverInfo.getServerEnvironment());

		for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			if (key.equals(MDCUtil.USER_IP_MDC)) {
				totalErrorRecordSaveRequestDTO.setUserIp(value);
			} else if (key.equals(MDCUtil.USER_LOCATION_MDC)) {
				totalErrorRecordSaveRequestDTO.setUserLocation(value);
			} else if (key.equals(MDCUtil.HEADER_MAP_MDC)) {
				totalErrorRecordSaveRequestDTO.setRequestHeader(value);
			} else if (key.equals(MDCUtil.PARAMETER_MAP_MDC)) {
				totalErrorRecordSaveRequestDTO.setRequestParameter(value);
			} else if (key.equals(MDCUtil.USER_AGENT_DETAIL_MDC)) {
				totalErrorRecordSaveRequestDTO.setUserEnvironment(value);
			} else if (key.equals(MDCUtil.BODY_MDC)) {
				totalErrorRecordSaveRequestDTO.setRequestBody(value);
			}

			if (exceptionBrief != null) {
				totalErrorRecordSaveRequestDTO.setExceptionBrief(exceptionBrief);
			}

			if (exceptionDetail != null) {
				totalErrorRecordSaveRequestDTO.setExceptionDetail(exceptionDetail);
			}
		}

		BackOfficeAPICaller.logSaveAPICall(totalErrorRecordSaveRequestDTO);
	}
}
