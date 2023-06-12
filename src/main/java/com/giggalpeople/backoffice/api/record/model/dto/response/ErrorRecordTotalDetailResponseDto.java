package com.giggalpeople.backoffice.api.record.model.dto.response;

import com.giggalpeople.backoffice.api.record.model.vo.ErrorRecordTotalInfoVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>Log 상세 조회를 위한 정보 담은 DTO</b></h2>
 */
@Schema(description = "Log 상세 조회를 위한 정보 담은 DTO")
@Data
public class ErrorRecordTotalDetailResponseDto {
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
	 * @param errorRecordTotalInfoVO Data Base에서 조회된 정보를 담은 Value Object
	 * @return Client에게 반환하기 위한 Response DTO
	 */
	public ErrorRecordTotalDetailResponseDto toDTO(ErrorRecordTotalInfoVo errorRecordTotalInfoVO) {
		ErrorRecordTotalDetailResponseDto errorRecordTotalDetailResponseDTO = new ErrorRecordTotalDetailResponseDto();
		String gradeWarning = "해당 정보를 확인할 권한이 없습니다. 자세한 사항은 모임 대표에게 문의해 주세요 😀";

		errorRecordTotalDetailResponseDTO.logId = errorRecordTotalInfoVO.getLogId();
		errorRecordTotalDetailResponseDTO.createdDateTime =
			errorRecordTotalInfoVO.getDataCreatedDate() + " " + errorRecordTotalInfoVO.getDataCreatedTime();
		errorRecordTotalDetailResponseDTO.level = errorRecordTotalInfoVO.getLevel();
		errorRecordTotalDetailResponseDTO.serverName = errorRecordTotalInfoVO.getServerName();
		errorRecordTotalDetailResponseDTO.serverVMInfo = errorRecordTotalInfoVO.getServerVmInfo();
		errorRecordTotalDetailResponseDTO.serverOSInfo = errorRecordTotalInfoVO.getServerOsInfo();
		errorRecordTotalDetailResponseDTO.serverIP = errorRecordTotalInfoVO.getServerip();
		errorRecordTotalDetailResponseDTO.serverEnvironment = errorRecordTotalInfoVO.getServerEnvironment();

		if (errorRecordTotalInfoVO.getUserIP() != null &&
			errorRecordTotalInfoVO.getUserEnvironment() != null &&
			errorRecordTotalInfoVO.getUserLocation()
				!= null) {     /* 정보 보안을 위한 이용자 정보 확인 인원을 최소화 하고, 해당 정보 확인 권한이 없으면 Null 값이 전달됨. */

			errorRecordTotalDetailResponseDTO.userIp = errorRecordTotalInfoVO.getUserIP();
			errorRecordTotalDetailResponseDTO.userEnvironment = errorRecordTotalInfoVO.getUserEnvironment();
			errorRecordTotalDetailResponseDTO.userLocation = errorRecordTotalInfoVO.getUserLocation();

		} else {
			errorRecordTotalDetailResponseDTO.userIp = gradeWarning;
			errorRecordTotalDetailResponseDTO.userEnvironment = gradeWarning;
			errorRecordTotalDetailResponseDTO.userLocation = gradeWarning;
		}

		errorRecordTotalDetailResponseDTO.requestHeader = errorRecordTotalInfoVO.getRequestHeader();
		errorRecordTotalDetailResponseDTO.userCookies = errorRecordTotalInfoVO.getUserCookies();
		errorRecordTotalDetailResponseDTO.requestParameter = errorRecordTotalInfoVO.getRequestParameter();

		if (errorRecordTotalInfoVO.getRequestBody() != null) {
			errorRecordTotalDetailResponseDTO.requestBody = errorRecordTotalInfoVO.getRequestBody();
		} else {
			errorRecordTotalDetailResponseDTO.requestBody = "요청 Body 값 없음";
		}

		errorRecordTotalDetailResponseDTO.exceptionBrief = errorRecordTotalInfoVO.getExceptionBrief();
		errorRecordTotalDetailResponseDTO.exceptionDetail = errorRecordTotalInfoVO.getExceptionDetail();

		return errorRecordTotalDetailResponseDTO;
	}
}
