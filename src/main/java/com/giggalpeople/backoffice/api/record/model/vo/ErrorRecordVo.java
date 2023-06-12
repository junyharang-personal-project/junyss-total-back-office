package com.giggalpeople.backoffice.api.record.model.vo;

import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorRecordSaveRequestDto;

import lombok.Getter;

/**
 * <h2><b>Data Base Log 저장을 위한 Value Object</b></h2>
 */

@Getter
public class ErrorRecordVo {
	private Long logId;
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserID;
	private Long connectedUserRequestInfoID;
	private Long logLevelID;
	private String exceptionBrief;
	private String exceptionDetail;

	public static ErrorRecordVo toVO(ErrorRecordSaveRequestDto errorRecordSaveRequestDto) {
		ErrorRecordVo errorRecordVO = new ErrorRecordVo();

		errorRecordVO.internalServerID = errorRecordSaveRequestDto.getInternalServerID();
		errorRecordVO.dataCreatedDateTimeID = errorRecordSaveRequestDto.getDataCreatedDateTimeID();
		errorRecordVO.connectedUserID = errorRecordSaveRequestDto.getConnectedUserID();
		errorRecordVO.connectedUserRequestInfoID = errorRecordSaveRequestDto.getConnectedUserRequestInfoID();
		errorRecordVO.logLevelID = errorRecordSaveRequestDto.getLogLevelID();
		errorRecordVO.exceptionBrief = errorRecordSaveRequestDto.getExceptionBrief();
		errorRecordVO.exceptionDetail = errorRecordSaveRequestDto.getExceptionDetail();

		return errorRecordVO;
	}
}
