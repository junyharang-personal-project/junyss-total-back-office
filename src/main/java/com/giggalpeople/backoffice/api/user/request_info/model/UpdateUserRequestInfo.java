package com.giggalpeople.backoffice.api.user.request_info.model;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserRequestInfo {
	private Long connectedUserID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserRequestInfoID;

	@Builder
	public UpdateUserRequestInfo(Long connectedUserID, Long dataCreatedDateTimeID, Long connectedUserRequestInfoID) {
		this.connectedUserID = connectedUserID;
		this.dataCreatedDateTimeID = dataCreatedDateTimeID;
		this.connectedUserRequestInfoID = connectedUserRequestInfoID;
	}
}
