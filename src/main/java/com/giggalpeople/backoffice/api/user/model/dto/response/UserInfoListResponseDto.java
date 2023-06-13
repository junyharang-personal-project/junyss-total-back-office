package com.giggalpeople.backoffice.api.user.model.dto.response;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserRequestInfoVo;
import com.giggalpeople.backoffice.common.util.CryptoUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * <h2><b>Application 이용자 접속 및 요청 정보 목록 조 시 이용될 Response DTO</b></h2>
 */

@Data
public class UserInfoListResponseDto {
	@Schema(description = "이용자 요청 정보 ID(순번)", nullable = false, example = "1")
	private Long connectedUserRequestInfoID;

	@Schema(description = "이용자 접속 일시", nullable = false, example = "2023-04-06 17:10:49")
	private String connectedDateTime;

	@Schema(description = "이용자 접속 Server 이름", nullable = false, example = "Giggal-...")
	private String serverName;

	@Schema(description = "이용자 IP", nullable = false, example = "192.168.0.3")
	private String userIP;

	/**
	 * <b>생성자</b>
	 * @param connectedUserRequestInfoID Application 이용자 요청 정보 PK
	 * @param connectedDateTime Application 이용자 요청 일시
	 * @param serverName 내부 Server 이름
	 * @param userIP Application 이용자 IP 주소
	 */
	@Builder
	public UserInfoListResponseDto(Long connectedUserRequestInfoID,
		String connectedDateTime,
		String serverName,
		String userIP) {

		ObjectMapper objectMapper = new ObjectMapper();

		this.connectedUserRequestInfoID = connectedUserRequestInfoID;
		this.connectedDateTime = connectedDateTime;
		this.serverName = serverName;

		try {
			this.userIP = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userIP);

		} catch (JsonProcessingException jsonProcessingException) {
			throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR,
				NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR.getMessage(String.valueOf(jsonProcessingException)));
		}
	}

	/**
	 * <b>Data Base에서 조회된 결과를 담은 VO를 DTO로 변환하기 위한 Method</b>
	 * @param connectedUserRequestInfoVO Data Base에서 조회된 결과를 담은 VO
	 * @return DTO로 변환된 UserInfoListResponseDTO
	 */

	public static UserInfoListResponseDto toDTO(ConnectedUserRequestInfoVo connectedUserRequestInfoVO) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			return UserInfoListResponseDto.builder()
				.connectedUserRequestInfoID(connectedUserRequestInfoVO.getConnectedUserRequestInfoID())
				.connectedDateTime(
					connectedUserRequestInfoVO.getDataCreatedDate() + " "
						+ connectedUserRequestInfoVO.getDataCreatedTime())
				.serverName(connectedUserRequestInfoVO.getServerName())
				.userIP(objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(CryptoUtil.userInfoIPDecrypt(connectedUserRequestInfoVO.getUserIp())))
				.build();

		} catch (JsonProcessingException jsonProcessingException) {
			throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR,
				NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR.getMessage(String.valueOf(jsonProcessingException)));
		}
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 userIP Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 IP 주소
	 */
	public String getUserIP() {
		return userIP.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}
}
