package com.giggalpeople.backoffice.api.record.model.dto.enumtype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorRecordSearchType {

	@Schema(description = "Log ID(생성 순번)", nullable = true, example = "1")
	LOG_ID("LOG_ID", "순서번호"),

	@Schema(description = "Log 생성일", nullable = true, example = "2023-04-06")
	LOG_CREATE_DATE("LOG_CREATE_DATE", "생성일"),

	@Schema(description = "Log Level", nullable = true, example = "WARN")
	LOG_LEVEL("LOG_LEVEL", "레벨"),

	@Schema(description = "Server Name", nullable = true, example = "통합관리서버")
	SERVER_NAME("SERVER_NAME", "서버이름"),

	@Schema(description = "Server IP", nullable = true, example = "192.168.0.3")
	SERVER_IP("SERVER_IP", "서버주소"),

	@Schema(description = "User IP", nullable = true, example = "168.126.63.1")
	USER_IP("USER_IP", "이용자주소"),

	@Schema(description = "Exception 간략 내용", nullable = true, example = "java.lang.NullPointerException: null")
	EXCEPTION_BRIEF("EXCEPTION_BRIEF", "간략정보");

	private String description;
	private String searchCommand;
}
