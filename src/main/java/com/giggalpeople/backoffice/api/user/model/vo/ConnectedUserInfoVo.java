package com.giggalpeople.backoffice.api.user.model.vo;

import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;

import lombok.Getter;

@Getter
public class ConnectedUserInfoVo {
	private Long connectedUserID;
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserCount;
	private String userIP;
	private String userLocation;
	private String userEnvironment;

	public static ConnectedUserInfoVo toVO(ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDto) {
		ConnectedUserInfoVo connectedUserInfoVO = new ConnectedUserInfoVo();

		connectedUserInfoVO.internalServerID = connectedUserInfoSaveRequestDto.getInternalServerID();
		connectedUserInfoVO.dataCreatedDateTimeID = connectedUserInfoSaveRequestDto.getDataCreatedDateTimeID();
		connectedUserInfoVO.userIP = connectedUserInfoSaveRequestDto.getUserIP();
		connectedUserInfoVO.userLocation = connectedUserInfoSaveRequestDto.getUserLocation();
		connectedUserInfoVO.userEnvironment = connectedUserInfoSaveRequestDto.getUserEnvironment();

		return connectedUserInfoVO;
	}
}
