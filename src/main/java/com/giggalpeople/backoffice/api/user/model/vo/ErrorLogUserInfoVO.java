package com.giggalpeople.backoffice.api.user.model.vo;

import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorLogUserInfoVO {
	private Long connectedUserID;
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserCount;
	private String userIP;
	private String userLocation;
	private String userEnvironment;

	public static ErrorLogUserInfoVO toVO(ConnectedUserInfoSaveRequestDTO connectedUserInfoSaveRequestDTO) {
		ErrorLogUserInfoVO errorLogUserInfoVO = new ErrorLogUserInfoVO();

		errorLogUserInfoVO.internalServerID = connectedUserInfoSaveRequestDTO.getInternalServerID();
		errorLogUserInfoVO.dataCreatedDateTimeID = connectedUserInfoSaveRequestDTO.getDataCreatedDateTimeID();
		errorLogUserInfoVO.userIP = connectedUserInfoSaveRequestDTO.getUserIP();
		errorLogUserInfoVO.userLocation = connectedUserInfoSaveRequestDTO.getUserLocation();
		errorLogUserInfoVO.userEnvironment = connectedUserInfoSaveRequestDTO.getUserEnvironment();

		return errorLogUserInfoVO;
	}

	@Builder
	public void decryptionUserInfoVO(String userIP, String userLocation, String userEnvironment) {
		this.userIP = userIP;
		this.userLocation = userLocation;
		this.userEnvironment = userEnvironment;
	}
}
