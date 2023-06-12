package com.giggalpeople.backoffice.api.record.model.dto.request;

import com.giggalpeople.backoffice.common.enumtype.CrewGrade;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>Log 상세 조회를 위한 DTO</b></h2>
 */

@Schema(description = "Log 상세 조회를 위한 DTO")
@Data
public class ErrorRecordDetailSearchRequestDto {

	@Schema(description = "조회 대상 Log ID", nullable = false, example = "1")
	private String logId;

	@Schema(description = "Discord 사용자 등급", nullable = false, example = "TeamLeader(TL)")
	private CrewGrade crewGrade;
}
