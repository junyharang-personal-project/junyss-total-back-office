package com.giggalpeople.backoffice.api.record.model.dto.response;

import com.giggalpeople.backoffice.api.record.model.vo.ErrorRecordTotalInfoVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * <h2><b>Log 목록 조회 시 이용될 Response DTO</b></h2>
 */

@Data
public class ErrorRecordListResponseDto {
	@Schema(description = "Log 번호", nullable = false, example = "1")
	private Long logId;

	@Schema(description = "Log 생성 일시", nullable = false, example = "2023-04-06 17:10:49")
	private String createdDateTime;

	@Schema(description = "Log Level", nullable = false, example = "WARN")
	private String level;

	@Schema(description = "Error 발생 Server 이름", nullable = false, example = "Giggal-...")
	private String serverName;

	@Schema(description = "Error 발생 Server 구동 환경", nullable = false, example = "prod1")
	private String serverEnvironment;

	@Schema(description = "Error 발생 Server IP", nullable = false, example = "192.168.0.3")
	private String serverIP;

	@Schema(description = "Exeption 간략 내용", nullable = false, example = "java.lang.NullPointerException: null")
	private String exceptionBrief;

	@Builder
	public ErrorRecordListResponseDto(Long logId, String createdDateTime, String level, String serverName,
		String serverEnvironment, String serverIP, String exceptionBrief) {
		this.logId = logId;
		this.createdDateTime = createdDateTime;
		this.level = level;
		this.serverName = serverName;
		this.serverEnvironment = serverEnvironment;
		this.serverIP = serverIP;
		this.exceptionBrief = exceptionBrief;
	}

	public static ErrorRecordListResponseDto toDTO(ErrorRecordTotalInfoVo errorRecordTotalInfoVO) {
		return ErrorRecordListResponseDto.builder()
			.logId(errorRecordTotalInfoVO.getLogId())
			.createdDateTime(
				errorRecordTotalInfoVO.getDataCreatedDate() + " " + errorRecordTotalInfoVO.getDataCreatedTime())
			.level(errorRecordTotalInfoVO.getLevel())
			.serverName(errorRecordTotalInfoVO.getServerName())
			.serverIP(errorRecordTotalInfoVO.getServerip())
			.serverEnvironment(errorRecordTotalInfoVO.getServerEnvironment())
			.exceptionBrief(errorRecordTotalInfoVO.getExceptionBrief())
			.build();
	}
}
