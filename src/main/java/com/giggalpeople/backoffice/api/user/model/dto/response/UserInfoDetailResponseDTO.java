package com.giggalpeople.backoffice.api.user.model.dto.response;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>이용자 접속 및 요청 정보 상세 조회를 위한 정보 담은 DTO</b></h2>
 */

@Schema(description = "이용자 접속 및 요청 정보 상세 조회를 위한 정보 담은 DTO")
@Data
public class UserInfoDetailResponseDTO {
	private Long connectedUserRequestInfoID;
	private String dataCreatedDateTime;
	private String serverName;
	private String serverVmInfo;
	private String serverOSInfo;
	private String serverIP;
	private String serverEnvironment;
	private String userIP;
	private String userEnvironment;
	private String userLocation;
	private String requestHeader;
	private String userCookies;
	private String requestParameter;
	private String requestBody;

	public UserInfoDetailResponseDTO toDTO(ConnectedUserInfoVO connectedUserInfoVO) {
		ObjectMapper objectMapper = new ObjectMapper();

		UserInfoDetailResponseDTO userInfoDetailResponseDTO = new UserInfoDetailResponseDTO();

		userInfoDetailResponseDTO.connectedUserRequestInfoID = connectedUserInfoVO.getConnectedUserRequestInfoID();
		userInfoDetailResponseDTO.dataCreatedDateTime =
			connectedUserInfoVO.getDataCreatedDate() + " " + connectedUserInfoVO.getDataCreatedTime();
		userInfoDetailResponseDTO.serverName = connectedUserInfoVO.getServerName();
		userInfoDetailResponseDTO.serverVmInfo = connectedUserInfoVO.getServerVmInfo();
		userInfoDetailResponseDTO.serverOSInfo = connectedUserInfoVO.getServerOSInfo();
		userInfoDetailResponseDTO.serverIP = connectedUserInfoVO.getServerIP();
		userInfoDetailResponseDTO.serverEnvironment = connectedUserInfoVO.getServerEnvironment();

		try {
			userInfoDetailResponseDTO.userIP = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(connectedUserInfoVO.getUserIP());
			userInfoDetailResponseDTO.userEnvironment = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(connectedUserInfoVO.getUserEnvironment());
			userInfoDetailResponseDTO.userLocation = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(connectedUserInfoVO.getUserLocation());
			userInfoDetailResponseDTO.requestHeader = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(connectedUserInfoVO.getRequestHeader());
			userInfoDetailResponseDTO.userCookies = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(connectedUserInfoVO.getUserCookies());
			userInfoDetailResponseDTO.requestParameter = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(connectedUserInfoVO.getRequestParameter());
			userInfoDetailResponseDTO.requestBody = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(connectedUserInfoVO.getRequestBody());

		} catch (JsonProcessingException jsonProcessingException) {
			throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR,
				NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR.getMessage(String.valueOf(jsonProcessingException)));
		}

		return userInfoDetailResponseDTO;
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 userIP Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 IP 주소
	 */

	public String getUserIP() {
		return userIP.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 userEnvironment Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 환경 정보
	 */

	public String getUserEnvironment() {
		return userEnvironment.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 userLocation Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 접속 위치 정보
	 */

	public String getUserLocation() {
		return userLocation.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 requestHeader Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 요청 헤더 정보
	 */

	public String getRequestHeader() {
		return requestHeader.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 userCookies Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 쿠키 정보
	 */

	public String getUserCookies() {
		return userCookies.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 requestParameter Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 요청 파라미터 정보
	 */

	public String getRequestParameter() {
		return requestParameter.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}

	/**
	 * <b>지저분한 특수문자 지우기 위한 requestBody Getter</b>
	 * @return 지저분한 특수문자 지워진 이용자 요청 바디 정보
	 */

	public String getRequestBody() {
		return requestBody.replace("\"", "").replace("\\", "").replace("n", "").replaceAll("[\\{\\{\\}]", "");
	}
}
