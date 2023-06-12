package com.giggalpeople.backoffice.api.user.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class ConnectedUserInfoSaveRequestDto {
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private String userIP;
	private String userLocation;
	private String userEnvironment;

	@Builder
	public ConnectedUserInfoSaveRequestDto(Long internalServerID, Long dataCreatedDateTimeID, String userIP,
		String userLocation, String userEnvironment) {
		this.internalServerID = internalServerID;
		this.dataCreatedDateTimeID = dataCreatedDateTimeID;
		this.userIP = userIP.replace("\"", "");
		this.userLocation = userLocation;
		this.userEnvironment = userEnvironment;
	}
}
