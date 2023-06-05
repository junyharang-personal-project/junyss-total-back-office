package com.giggalpeople.backoffice.api.user.model.dto.enumtype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UserInfoSearchType {

	@Schema(description = "이용자 요청 ID(순번)", nullable = true, example = "1")
	CONNECTED_USER_REQUEST_ID("CONNECTED_USER_REQUEST_ID", "순서번호"),

	@Schema(description = "이용자 서비스 접속일", nullable = true, example = "2023-04-06")
	USER_CONNECTED_DATE("USER_CONNECTED_DATE", "요청일"),

	@Schema(description = "Server Name (서버 종류: 1. 통합관리서버)", nullable = true, example = "통합관리서버")
	SERVER_NAME("SERVER_NAME", "서버이름"),

	@Schema(description = "Server IP", nullable = true, example = "192.168.0.3")
	SERVER_IP("SERVER_IP", "서버주소"),

	@Schema(description = "이용자 IP 주소", nullable = true, example = "168.126.63.1")
	USER_IP("USER_IP", "이용자주소");

	private String description;
	private String searchCommand;
}
