package com.giggalpeople.backoffice.api.record.model.vo;

import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorRecordLevelSaveRequestDto;

import lombok.Getter;

/**
 * <h2><b>Data Base Log Level 저장을 위한 Value Object</b></h2>
 */

@Getter
public class ErrorRecordLevelVo {
	private Long logLevelId;
	private String level;

	public static ErrorRecordLevelVo toVO(ErrorRecordLevelSaveRequestDto errorRecordLevelSaveRequestDTO) {
		ErrorRecordLevelVo logVO = new ErrorRecordLevelVo();

		logVO.logLevelId = errorRecordLevelSaveRequestDTO.getLogLevelID();
		logVO.level = errorRecordLevelSaveRequestDTO.getLevel();

		return logVO;
	}
}
