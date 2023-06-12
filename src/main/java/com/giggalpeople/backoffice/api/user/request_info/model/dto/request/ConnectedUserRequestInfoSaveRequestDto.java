package com.giggalpeople.backoffice.api.user.request_info.model.dto.request;

import javax.servlet.http.Cookie;

import lombok.Builder;
import lombok.Data;

@Data
public class ConnectedUserRequestInfoSaveRequestDto {
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserID;
	private String requestHeader;
	private Cookie[] userCookiesArray;
	private String userCookies;
	private String requestParameter;
	private String requestBody;

	@Builder
	public ConnectedUserRequestInfoSaveRequestDto(Long internalServerID,
		Long dataCreatedDateTimeID,
		Long connectedUserID,
		String requestHeader,
		Cookie[] userCookiesArray,
		String userCookies,
		String requestParameter,
		String requestBody) {

		this.internalServerID = internalServerID;
		this.dataCreatedDateTimeID = dataCreatedDateTimeID;
		this.connectedUserID = connectedUserID;
		this.requestHeader = requestHeader;
		this.userCookiesArray = userCookiesArray;
		this.userCookies = userCookies;
		this.requestParameter = requestParameter;
		this.requestBody = requestBody;
	}

	public void setUserCookies(String encryptUserCookies) {
		this.userCookies = encryptUserCookies;
	}
}