package com.giggalpeople.backoffice.api.record.model.dto.response;

import com.giggalpeople.backoffice.api.record.model.vo.LogTotalInfoVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>Log 상세 조회를 위한 정보 담은 DTO</b></h2>
 */
@Schema(description = "Log 상세 조회를 위한 정보 담은 DTO")
@Data
public class ErrorLogTotalDetailResponseDto {
	private Long logId;
	private String createdDateTime;
	private String level;
	private String serverName;
	private String serverVMInfo;
	private String serverOSInfo;
	private String serverIP;
	private String serverEnvironment;
	private String userIp;
	private String userEnvironment;
	private String userLocation;
	private String requestHeader;
	private String userCookies;
	private String requestParameter;
	private String requestBody;
	private String exceptionBrief;
	private String exceptionDetail;

	/**
	 * <b>Data Base에서 조회된 정보를 담은 Value Object를 Client에게 반환하기 위한 Response DTO로 바꾸기 위한 Method</b>
	 * @param logTotalInfoVO Data Base에서 조회된 정보를 담은 Value Object
	 * @return Client에게 반환하기 위한 Response DTO
	 */
	public ErrorLogTotalDetailResponseDto toDTO(LogTotalInfoVo logTotalInfoVO) {
		ErrorLogTotalDetailResponseDto errorLogTotalDetailResponseDTO = new ErrorLogTotalDetailResponseDto();
		String gradeWarning = "해당 정보를 확인할 권한이 없습니다. 자세한 사항은 모임 대표에게 문의해 주세요 😀";

		errorLogTotalDetailResponseDTO.logId = logTotalInfoVO.getLogId();
		errorLogTotalDetailResponseDTO.createdDateTime =
			logTotalInfoVO.getDataCreatedDate() + " " + logTotalInfoVO.getDataCreatedTime();
		errorLogTotalDetailResponseDTO.level = logTotalInfoVO.getLevel();
		errorLogTotalDetailResponseDTO.serverName = logTotalInfoVO.getServerName();
		errorLogTotalDetailResponseDTO.serverVMInfo = logTotalInfoVO.getServerVmInfo();
		errorLogTotalDetailResponseDTO.serverOSInfo = logTotalInfoVO.getServerOsInfo();
		errorLogTotalDetailResponseDTO.serverIP = logTotalInfoVO.getServerip();
		errorLogTotalDetailResponseDTO.serverEnvironment = logTotalInfoVO.getServerEnvironment();

		if (logTotalInfoVO.getUserIP() != null &&
			logTotalInfoVO.getUserEnvironment() != null &&
			logTotalInfoVO.getUserLocation()
				!= null) {     /* 정보 보안을 위한 이용자 정보 확인 인원을 최소화 하고, 해당 정보 확인 권한이 없으면 Null 값이 전달됨. */

			errorLogTotalDetailResponseDTO.userIp = logTotalInfoVO.getUserIP();
			errorLogTotalDetailResponseDTO.userEnvironment = logTotalInfoVO.getUserEnvironment();
			errorLogTotalDetailResponseDTO.userLocation = logTotalInfoVO.getUserLocation();

		} else {
			errorLogTotalDetailResponseDTO.userIp = gradeWarning;
			errorLogTotalDetailResponseDTO.userEnvironment = gradeWarning;
			errorLogTotalDetailResponseDTO.userLocation = gradeWarning;
		}

		errorLogTotalDetailResponseDTO.requestHeader = logTotalInfoVO.getRequestHeader();
		errorLogTotalDetailResponseDTO.userCookies = logTotalInfoVO.getUserCookies();
		errorLogTotalDetailResponseDTO.requestParameter = logTotalInfoVO.getRequestParameter();

		if (logTotalInfoVO.getRequestBody() != null) {
			errorLogTotalDetailResponseDTO.requestBody = logTotalInfoVO.getRequestBody();
		} else {
			errorLogTotalDetailResponseDTO.requestBody = "요청 Body 값 없음";
		}

		errorLogTotalDetailResponseDTO.exceptionBrief = logTotalInfoVO.getExceptionBrief();
		errorLogTotalDetailResponseDTO.exceptionDetail = logTotalInfoVO.getExceptionDetail();

		return errorLogTotalDetailResponseDTO;
	}
}
