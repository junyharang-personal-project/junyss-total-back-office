package com.giggalpeople.backoffice.api.record.model.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * <h2><b>Data Base Log Level 저장을 위한 DTO</b></h2>
 */

@Data
public class ErrorRecordLevelSaveRequestDto {
	private Long logLevelID;
	private String level;

	@Builder
	public ErrorRecordLevelSaveRequestDto(Long logLevelID, String level) {
		this.logLevelID = logLevelID;
		this.level = level;
	}
}
