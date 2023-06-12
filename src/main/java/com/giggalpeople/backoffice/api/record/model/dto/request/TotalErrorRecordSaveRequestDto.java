package com.giggalpeople.backoffice.api.record.model.dto.request;

import javax.servlet.http.Cookie;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * <h2><b>Log 저장 관련 요청 DTO</b></h2>
 */

@Data
public class TotalErrorRecordSaveRequestDto {
	private String createdAt;
	@JsonIgnore
	private String createdDate;
	@JsonIgnore
	private String createdTime;
	private String level;
	private String serverName;
	private String vmInfo;
	private String osInfo;
	private String serverIP;
	private String serverEnvironment;
	private String userIp;
	private String userLocation;
	private String userEnvironment;
	private String requestHeader;
	private Cookie[] userCookies;
	private String requestParameter;
	private String requestBody;
	private String exceptionBrief;
	private String exceptionDetail;
}
