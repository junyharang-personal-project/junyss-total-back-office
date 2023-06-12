package com.giggalpeople.backoffice.api.user.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConnectedUserInfoVo {
	private Long connectedUserRequestInfoID;
	private String dataCreatedDate;
	private String dataCreatedTime;
	private String serverName;
	private String serverVmInfo;
	private String serverOsInfo;
	private String serverIp;
	private String serverEnvironment;
	private String userIp;
	private String userEnvironment;
	private String userLocation;
	private String requestHeader;
	private String userCookies;
	private String requestParameter;
	private String requestBody;

	public ConnectedUserInfoVo(Long connectedUserRequestInfoID,
		String dataCreatedDate,
		String dataCreatedTime,
		String serverName,
		String serverVmInfo,
		String serverOsInfo,
		String serverIp,
		String serverEnvironment,
		String userIp,
		String userEnvironment,
		String userLocation,
		String requestHeader,
		String userCookies,
		String requestParameter,
		String requestBody) {

		this.connectedUserRequestInfoID = connectedUserRequestInfoID;
		this.dataCreatedDate = dataCreatedDate;
		this.dataCreatedTime = dataCreatedTime;
		this.serverName = serverName;
		this.serverVmInfo = serverVmInfo;
		this.serverOsInfo = serverOsInfo;
		this.serverIp = serverIp;
		this.serverEnvironment = serverEnvironment;
		this.userIp = userIp;
		this.userEnvironment = userEnvironment;
		this.userLocation = userLocation;
		this.requestHeader = requestHeader;
		this.userCookies = userCookies;
		this.requestParameter = requestParameter;
		this.requestBody = requestBody;
	}
}
