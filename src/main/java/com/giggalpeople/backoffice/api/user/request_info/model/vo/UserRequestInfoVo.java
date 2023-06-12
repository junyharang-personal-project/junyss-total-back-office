package com.giggalpeople.backoffice.api.user.request_info.model.vo;

import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;

import lombok.Getter;

@Getter
public class UserRequestInfoVo {
	private Long connectedUserRequestInfoID;
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserID;
	private Long connectedUserSameRequestCount;
	private String requestHeader;
	private String userCookies;
	private String requestParameter;
	private String requestBody;

	public static UserRequestInfoVo toVo(
		ConnectedUserRequestInfoSaveRequestDto connectedUserRequestInfoSaveRequestDto) {
		UserRequestInfoVo userRequestInfoVo = new UserRequestInfoVo();

		userRequestInfoVo.internalServerID = connectedUserRequestInfoSaveRequestDto.getInternalServerID();
		userRequestInfoVo.dataCreatedDateTimeID = connectedUserRequestInfoSaveRequestDto.getDataCreatedDateTimeID();
		userRequestInfoVo.connectedUserID = connectedUserRequestInfoSaveRequestDto.getConnectedUserID();
		userRequestInfoVo.requestHeader = connectedUserRequestInfoSaveRequestDto.getRequestHeader();
		userRequestInfoVo.userCookies = connectedUserRequestInfoSaveRequestDto.getUserCookies();
		userRequestInfoVo.requestParameter = connectedUserRequestInfoSaveRequestDto.getRequestParameter();
		userRequestInfoVo.requestBody = connectedUserRequestInfoSaveRequestDto.getRequestBody();

		return userRequestInfoVo;
	}
}
