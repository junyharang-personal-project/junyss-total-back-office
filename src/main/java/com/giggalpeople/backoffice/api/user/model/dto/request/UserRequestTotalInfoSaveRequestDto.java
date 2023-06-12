package com.giggalpeople.backoffice.api.user.model.dto.request;

import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.common.entity.ServerInfo;

import lombok.Builder;
import lombok.Data;

@Data
public class UserRequestTotalInfoSaveRequestDto {

	private ServerInfo serverInfo;
	private DataCreatedDateTimeRequestDto dataCreatedDateTimeRequestDTO;
	private ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDTO;
	private ConnectedUserRequestInfoSaveRequestDto connectedUserRequestInfoSaveRequestDTO;

	@Builder
	public UserRequestTotalInfoSaveRequestDto(ServerInfo serverInfo,
		DataCreatedDateTimeRequestDto dataCreatedDateTimeRequestDTO,
		ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDTO,
		ConnectedUserRequestInfoSaveRequestDto connectedUserRequestInfoSaveRequestDTO) {

		this.serverInfo = serverInfo;
		this.dataCreatedDateTimeRequestDTO = dataCreatedDateTimeRequestDTO;
		this.connectedUserInfoSaveRequestDTO = connectedUserInfoSaveRequestDTO;
		this.connectedUserRequestInfoSaveRequestDTO = connectedUserRequestInfoSaveRequestDTO;
	}
}
