package com.giggalpeople.backoffice.chatops.logback.appender.util;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giggalpeople.backoffice.chatops.logback.appender.exception.ErrorLogAppenderException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MDCUtil {
	public static final String SERVER_APPLICATION_NAME_MDC = "Server Application 이름";
	public static final String SERVER_VM_INFO_MDC = "Server VM 정보";
	public static final String SERVER_OS_INFO_MDC = "Server 운영체제 정보";
	public static final String SERVER_IP_MDC = "SERVER IP 주소";
	public static final String SERVER_ENVIRONMNET_MDC = "SERVER 구동 환경 정보";
	public static final String REQUEST_URI_MDC = "이용자 요청 URI 정보";
	public static final String USER_IP_MDC = "이용자 IP 정보";
	public static final String USER_AGENT_DETAIL_MDC = "이용자 환경 정보";
	public static final String USER_LOCATION_MDC = "이용자 위치 정보";
	public static final String USER_REQUEST_COOKIES = "이용자 Cookie 정보";
	public static final String HEADER_MAP_MDC = "HTTP 헤더 정보";
	public static final String PARAMETER_MAP_MDC = "Parameter 정보";
	public static final String BODY_MDC = "HTTP Body 정보";
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final MDCAdapter mdc = MDC.getMDCAdapter();

	public static void set(String key, String value) {
		mdc.put(key, value);
	}

	public static void setJsonValue(String key, Object value) {
		try {
			if (value != null) {
				String json = objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(value);
				mdc.put(key, json);

			} else {
				mdc.put(key, "내용 없음");
			}

		} catch (JsonProcessingException jsonProcessingException) {
			throw new ErrorLogAppenderException(JSON_PARSER_ERROR,
				JSON_PARSER_ERROR.getMessage(String.valueOf(jsonProcessingException)));
		}
	}

	public static void clear() {
		MDC.clear();
	}
}
