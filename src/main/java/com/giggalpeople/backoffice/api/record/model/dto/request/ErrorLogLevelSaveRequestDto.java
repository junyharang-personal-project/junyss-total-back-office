package com.giggalpeople.backoffice.api.record.model.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * <h2><b>Data Base Log Level 저장을 위한 DTO</b></h2>
 */

@Data
public class ErrorLogLevelSaveRequestDto {
	private Long logLevelID;
	private String level;

	@Builder
	public ErrorLogLevelSaveRequestDto(Long logLevelID, String level) {
		this.logLevelID = logLevelID;
		this.level = level;
	}
}
