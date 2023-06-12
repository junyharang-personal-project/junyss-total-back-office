package com.giggalpeople.backoffice.api.user.model.vo;

import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;

import lombok.Getter;

@Getter
public class ErrorLogUserInfoVo {
	private Long connectedUserID;
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserCount;
	private String userIP;
	private String userLocation;
	private String userEnvironment;

	public static ErrorLogUserInfoVo toVO(ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDto) {
		ErrorLogUserInfoVo errorLogUserInfoVO = new ErrorLogUserInfoVo();

		errorLogUserInfoVO.internalServerID = connectedUserInfoSaveRequestDto.getInternalServerID();
		errorLogUserInfoVO.dataCreatedDateTimeID = connectedUserInfoSaveRequestDto.getDataCreatedDateTimeID();
		errorLogUserInfoVO.userIP = connectedUserInfoSaveRequestDto.getUserIP();
		errorLogUserInfoVO.userLocation = connectedUserInfoSaveRequestDto.getUserLocation();
		errorLogUserInfoVO.userEnvironment = connectedUserInfoSaveRequestDto.getUserEnvironment();

		return errorLogUserInfoVO;
	}
}
