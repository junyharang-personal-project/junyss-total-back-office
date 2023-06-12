package com.giggalpeople.backoffice.api.record.model.vo;

import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogSaveRequestDto;

import lombok.Getter;

/**
 * <h2><b>Data Base Log 저장을 위한 Value Object</b></h2>
 */

@Getter
public class LogVo {
	private Long logId;
	private Long internalServerID;
	private Long dataCreatedDateTimeID;
	private Long connectedUserID;
	private Long connectedUserRequestInfoID;
	private Long logLevelID;
	private String exceptionBrief;
	private String exceptionDetail;

	public static LogVo toVO(ErrorLogSaveRequestDto errorLogSaveRequestDto) {
		LogVo logVO = new LogVo();

		logVO.internalServerID = errorLogSaveRequestDto.getInternalServerID();
		logVO.dataCreatedDateTimeID = errorLogSaveRequestDto.getDataCreatedDateTimeID();
		logVO.connectedUserID = errorLogSaveRequestDto.getConnectedUserID();
		logVO.connectedUserRequestInfoID = errorLogSaveRequestDto.getConnectedUserRequestInfoID();
		logVO.logLevelID = errorLogSaveRequestDto.getLogLevelID();
		logVO.exceptionBrief = errorLogSaveRequestDto.getExceptionBrief();
		logVO.exceptionDetail = errorLogSaveRequestDto.getExceptionDetail();

		return logVO;
	}
}
