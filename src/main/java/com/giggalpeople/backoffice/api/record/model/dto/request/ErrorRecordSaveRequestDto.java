package com.giggalpeople.backoffice.api.record.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class ErrorRecordSaveRequestDto {
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserID;
	private Long connectedUserRequestInfoID;
	private Long logLevelID;
	private String exceptionBrief;
	private String exceptionDetail;

	@Builder
	public ErrorRecordSaveRequestDto(Long internalServerID, Long dataCreatedDateTimeID, Long connectedUserID,
		Long connectedUserRequestInfoID, Long logLevelID, String exceptionBrief, String exceptionDetail) {
		this.internalServerID = internalServerID;
		this.dataCreatedDateTimeID = dataCreatedDateTimeID;
		this.connectedUserID = connectedUserID;
		this.connectedUserRequestInfoID = connectedUserRequestInfoID;
		this.logLevelID = logLevelID;
		this.exceptionBrief = exceptionBrief;
		this.exceptionDetail = exceptionDetail;
	}
}
