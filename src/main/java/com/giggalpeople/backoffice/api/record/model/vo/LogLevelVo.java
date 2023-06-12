package com.giggalpeople.backoffice.api.record.model.vo;

import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogLevelSaveRequestDto;

import lombok.Getter;

/**
 * <h2><b>Data Base Log Level 저장을 위한 Value Object</b></h2>
 */

@Getter
public class LogLevelVo {
	private Long logLevelId;
	private String level;

	public static LogLevelVo toVO(ErrorLogLevelSaveRequestDto errorLogLevelSaveRequestDTO) {
		LogLevelVo logVO = new LogLevelVo();

		logVO.logLevelId = errorLogLevelSaveRequestDTO.getLogLevelID();
		logVO.level = errorLogLevelSaveRequestDTO.getLevel();

		return logVO;
	}
}
