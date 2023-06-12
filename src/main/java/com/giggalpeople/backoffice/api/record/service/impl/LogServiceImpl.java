package com.giggalpeople.backoffice.api.record.service.impl;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CONNECTED_INTERNAL_SERVER_SAVE_FAILURE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CONNECTED_USER_REQUEST_SAVE_FAILURE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CONNECTED_USER_SAVE_FAILURE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.ERROR_SAVE_FAILURE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_EXIST_ERROR_LOG;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_FOUND_ERROR_LOG_LEVEL;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.PARAMETER_NULL;
import static com.giggalpeople.backoffice.common.enumtype.SuccessCode.CREATE;
import static com.giggalpeople.backoffice.common.enumtype.SuccessCode.SUCCESS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.giggalpeople.backoffice.api.common.database.dao.OccurrenceDataDateTimeManagementDao;
import com.giggalpeople.backoffice.api.common.exception.OccurrenceDateTimeException;
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.Pagination;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;
import com.giggalpeople.backoffice.api.record.database.dao.LogManagementDao;
import com.giggalpeople.backoffice.api.record.exception.ErrorLogException;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogSaveRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogSearchDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorLogSaveRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorLogListResponseDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorLogTotalDetailResponseDto;
import com.giggalpeople.backoffice.api.record.model.vo.LogTotalInfoVo;
import com.giggalpeople.backoffice.api.record.model.vo.LogVo;
import com.giggalpeople.backoffice.api.record.service.LogService;
import com.giggalpeople.backoffice.api.server.database.dao.ServerInfoDao;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.api.user.database.dao.UserInfoDao;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.env.exception.ServerInfoException;
import com.giggalpeople.backoffice.common.util.CryptoUtil;
import com.giggalpeople.backoffice.common.util.StringUtil;

import lombok.RequiredArgsConstructor;

/**
 * <h2><b>Log 관리 business Logic 구현체</b></h2>
 */

@RequiredArgsConstructor
@Service
public class LogServiceImpl implements LogService {

	private final ServerInfoDao serverInfoDAO;
	private final OccurrenceDataDateTimeManagementDao occurrenceDataDateTimeManagementDAO;
	private final UserInfoDao userInfoDAO;
	private final LogManagementDao logManagementDao;

	/**
	 * <b>Log 저장</b>
	 *
	 * @param totalErrorLogSaveRequestDto log 정보를 담은 DTO
	 * @return Log 저장 뒤 생성된 Log ID
	 */

	@Override
	public DefaultResponse<Map<String, Long>> save(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDto) {

		if (!checkNotNull(totalErrorLogSaveRequestDto)) {
			throw new ErrorLogException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
		}

		Map<String, Long> resultMap = new HashMap<>();

		StringUtil.dateTimeSplit(totalErrorLogSaveRequestDto);

		Long internalServerSaveID = processServerInfoSave(totalErrorLogSaveRequestDto);
		Long occurrenceInfoDateTimeSaveID = processOccurrenceInfoDateTimeSave(totalErrorLogSaveRequestDto);
		Long connectedUserSaveID = processUserInfoSave(totalErrorLogSaveRequestDto, internalServerSaveID,
			occurrenceInfoDateTimeSaveID);
		Long connectedUserRequestInfoSaveID = processUserRequestInfoSave(totalErrorLogSaveRequestDto,
			internalServerSaveID, occurrenceInfoDateTimeSaveID, connectedUserSaveID);
		Long errorLogSaveID = processErrorLogInfoSave(totalErrorLogSaveRequestDto, internalServerSaveID,
			occurrenceInfoDateTimeSaveID, connectedUserSaveID, connectedUserRequestInfoSaveID);

		if (errorLogProcessNullCheck(internalServerSaveID, occurrenceInfoDateTimeSaveID, connectedUserSaveID,
			connectedUserRequestInfoSaveID, errorLogSaveID)) {
			resultMap.put("내부 서버 정보 저장 순서 번호", internalServerSaveID);
			resultMap.put("Data 공통 저장 날짜, 시각 순서 번호", occurrenceInfoDateTimeSaveID);
			resultMap.put("이용자 정보 저장 순서 번호", connectedUserSaveID);
			resultMap.put("이용자 요청 정보 저장 순서 번호", connectedUserRequestInfoSaveID);
			resultMap.put("Error Log 저장 순서 번호", errorLogSaveID);

		} else {
			throw new ErrorLogException(ERROR_SAVE_FAILURE,
				ERROR_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		}
		return DefaultResponse.response(CREATE.getStatusCode(), CREATE.getMessage(), resultMap);
	}

	/**
	 * <b>Discord를 통한 Error Log 목록 조회 비즈니스 로직</b>
	 *
	 * @param criteria          페이징 처리를 위한 정보
	 * @param errorLogSearchDto 검색어(검색 조건) Request 객체
	 * @return 조회된 Error Log 목록 정보
	 */
	@Override
	public DefaultListResponse<List<ErrorLogListResponseDto>> toDiscordAllErrorInfoFind(Criteria criteria,
		ErrorLogSearchDto errorLogSearchDto) {

		int searchCount = logManagementDao.totalErrorLogSearchCount(
			CryptoUtil.errorLogSearchEncrypt(errorLogSearchDto));

		if (searchCount <= 0) {
			throw new ErrorLogException(NOT_EXIST_ERROR_LOG, NOT_EXIST_ERROR_LOG.getMessage());

		} else if (searchCount == 1) {
			List<ErrorLogListResponseDto> responseDTOList = new ArrayList<>();
			Optional<LogTotalInfoVo> byErrorLogTotalInfoSearchOneThing = logManagementDao.findByErrorLogTotalInfoSearchOneThing(
				errorLogSearchDto);

			byErrorLogTotalInfoSearchOneThing.ifPresent(
				errorLogTotalInfoVO -> responseDTOList.add(ErrorLogListResponseDto.toDTO(errorLogTotalInfoVO)));

			return DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, searchCount), responseDTOList);
		}

		return DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
			new Pagination(criteria, searchCount),
			logManagementDao.findByErrorLogTotalInfoList(criteria, errorLogSearchDto)
				.stream()
				.filter(Objects::nonNull)
				.map(logTotalInfoVO -> ErrorLogListResponseDto.builder()
					.logId(logTotalInfoVO.getLogId())
					.createdDateTime(logTotalInfoVO.getDataCreatedDate() + " " + logTotalInfoVO.getDataCreatedTime())
					.level(logTotalInfoVO.getLevel())
					.serverName(logTotalInfoVO.getServerName())
					.serverEnvironment(logTotalInfoVO.getServerEnvironment())
					.serverIP(logTotalInfoVO.getServerip())
					.exceptionBrief(logTotalInfoVO.getExceptionBrief())
					.build())
				.collect(Collectors.toList()));
	}

	/**
	 * <b>Discord를 통한 Error Log 상세 조회를 위한 비즈니스 로직</b>
	 *
	 * @param errorLogDetailSearchRequestDto Discord에서 입력 받은 조회할 Log ID와 Crew 등급을 담은 DTO 객체
	 * @return Log 상세 정보를 담은 응답 DTO 객체
	 */
	@Override
	public DefaultResponse<ErrorLogTotalDetailResponseDto> toDiscordDetailErrorInfoFind(
		ErrorLogDetailSearchRequestDto errorLogDetailSearchRequestDto) {
		if (errorLogDetailSearchRequestDto.getCrewGrade().getGradeNum() > 3) {
			Optional<LogTotalInfoVo> forGeneralCrewLogTotalInfoVO = logManagementDao.forGeneralCrewDetailErrorTotalInfoFind(
				errorLogDetailSearchRequestDto.getLogId());

			if (forGeneralCrewLogTotalInfoVO.isPresent()) {
				return DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
					new ErrorLogTotalDetailResponseDto().toDTO(
						CryptoUtil.forGeneralCrewErrorLogDetailRequestInfoDecrypt(forGeneralCrewLogTotalInfoVO.get())));
			} else {
				throw new ErrorLogException(NOT_EXIST_ERROR_LOG, NOT_EXIST_ERROR_LOG.getMessage());
			}

		} else {
			Optional<LogTotalInfoVo> forLeaderCrewLogVO = logManagementDao.detailErrorTotalInfoFind(
				errorLogDetailSearchRequestDto.getLogId());

			if (forLeaderCrewLogVO.isPresent()) {
				return DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
					new ErrorLogTotalDetailResponseDto().toDTO(
						CryptoUtil.errorLogDetailUserTotalInfoDecrypt(forLeaderCrewLogVO.get())));

			} else {
				throw new ErrorLogException(NOT_EXIST_ERROR_LOG, NOT_EXIST_ERROR_LOG.getMessage());
			}
		}

	}

	/**
	 * <b>Log Back을 통해 Error Log를 저장하기 위한 API가 호출되면 totalErrorLogSaveRequestDTO 안에 WAS 정보를 저장하기 위한 Method</b>
	 *
	 * @param totalErrorLogSaveRequestDTO Server 정보, 이용자 정보, Error Log 정보가 담긴 요청 DTO 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processServerInfoSave(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDTO) {
		if (totalErrorLogSaveRequestDTO.getServerIP() != null) {
			Long byServerIP = serverInfoDAO.findByServerID(totalErrorLogSaveRequestDTO.getServerIP());

			if (byServerIP == null || byServerIP <= 0) {
				return serverInfoDAO.save(ServerInfoVo.toVo(ServerInfoSaveRequestDto.builder()
					.serverName(totalErrorLogSaveRequestDTO.getServerName())
					.serverVmInfo(totalErrorLogSaveRequestDTO.getVmInfo())
					.serverOsInfo(totalErrorLogSaveRequestDTO.getOsInfo())
					.serverIP(totalErrorLogSaveRequestDTO.getServerIP())
					.serverEnvironment(totalErrorLogSaveRequestDTO.getServerEnvironment())
					.build()));
			}
			return byServerIP;
		}
		throw new ServerInfoException(PARAMETER_NULL,
			PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
	}

	/**
	 * <b>Log Back을 통해 Error Log를 저장하기 위한 API가 호출되면 totalErrorLogSaveRequestDTO 안에 날짜와 시각 정보 저장하기 위한 Method</b>
	 *
	 * @param totalErrorLogSaveRequestDTO Server 정보, 이용자 정보, Error Log 정보가 담긴 요청 DTO 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processOccurrenceInfoDateTimeSave(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDTO) {
		if (totalErrorLogSaveRequestDTO.getCreatedDate() != null
			&& totalErrorLogSaveRequestDTO.getCreatedTime() != null) {
			Long byOccurrenceInfoDateTimeID = occurrenceDataDateTimeManagementDAO.findByOccurrenceInfoDateTime(
				totalErrorLogSaveRequestDTO.getCreatedDate(), totalErrorLogSaveRequestDTO.getCreatedTime());

			if (byOccurrenceInfoDateTimeID == null || byOccurrenceInfoDateTimeID <= 0) {
				byOccurrenceInfoDateTimeID = occurrenceDataDateTimeManagementDAO.save(DataCreatedDateTimeVo.toVO(
					DataCreatedDateTimeRequestDto.builder()
						.createdDate(totalErrorLogSaveRequestDTO.getCreatedDate())
						.createdTime(totalErrorLogSaveRequestDTO.getCreatedTime())
						.build()));

				return byOccurrenceInfoDateTimeID;
			} else {
				return byOccurrenceInfoDateTimeID;
			}
		}
		throw new OccurrenceDateTimeException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
	}

	/**
	 * <b>Log Back을 통해 Error Log를 저장하기 위한 API가 호출되면 totalErrorLogSaveRequestDto 안에 Error를 발생 시킨 요청 이용자 정보를 저장하기 위한 Method</b>
	 *
	 * @param totalErrorLogSaveRequestDto Server 정보, 이용자 정보, Error Log 정보가 담긴 요청 DTO 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processUserInfoSave(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDto, Long internalServerSaveID,
		Long dataCreatedDateTimeID) {
		if (totalErrorLogSaveRequestDto == null) {
			throw new ConnectedUserException(PARAMETER_NULL,
				PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
		} else if (internalServerSaveID == null) {
			throw new ServerInfoException(CONNECTED_INTERNAL_SERVER_SAVE_FAILURE,
				CONNECTED_INTERNAL_SERVER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (dataCreatedDateTimeID == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE,
				CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else {

			Long isIdByUserIp = userInfoDAO.findByUserIP(
				CryptoUtil.encryptUserIP(totalErrorLogSaveRequestDto.getUserIp()));

			if (isIdByUserIp == null || isIdByUserIp <= 0) {
				return userInfoDAO.connectedUserSave(ErrorLogUserInfoVo.toVO(CryptoUtil.userInfoEncrypt(
					ConnectedUserInfoSaveRequestDto.builder()
						.internalServerID(internalServerSaveID)
						.dataCreatedDateTimeID(dataCreatedDateTimeID)
						.userIP(totalErrorLogSaveRequestDto.getUserIp())
						.userLocation(totalErrorLogSaveRequestDto.getUserLocation())
						.userEnvironment(totalErrorLogSaveRequestDto.getUserEnvironment())
						.build())));

			} else {
				UpdateUserInfo updateUserInfo = new UpdateUserInfo(isIdByUserIp, dataCreatedDateTimeID);
				return userInfoDAO.updateCount(UpdateUserInfo.builder()
					.connectedUserID(updateUserInfo.getConnectedUserID())
					.dataCreatedDateTimeID(updateUserInfo.getDataCreatedDateTimeID())
					.build());
			}
		}
	}

	/**
	 * <b>Log Back을 통해 Error Log를 저장하기 위한 API가 호출되면 totalErrorLogSaveRequestDTO 안에 Error를 발생 시킨 요청 이용자가 보낸 요청 정보를 저장하기 위한 Method</b>
	 *
	 * @param totalErrorLogSaveRequestDTO Server 정보, 이용자 정보, Error Log 정보가 담긴 요청 DTO 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processUserRequestInfoSave(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDTO,
		Long internalServerSaveID, Long dataCreatedDateTimeID, Long connectedUserID) {
		if (totalErrorLogSaveRequestDTO == null) {
			throw new ConnectedUserException(PARAMETER_NULL,
				PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
		} else if (internalServerSaveID == null) {
			throw new ServerInfoException(CONNECTED_INTERNAL_SERVER_SAVE_FAILURE,
				CONNECTED_INTERNAL_SERVER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (dataCreatedDateTimeID == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE,
				CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (connectedUserID == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE,
				CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else {

			return userInfoDAO.findByRequestInfoSave(UserRequestInfoVo.toVo(CryptoUtil.userRequestInfoEncrypt(
				ConnectedUserRequestInfoSaveRequestDto.builder()
					.internalServerID(internalServerSaveID)
					.dataCreatedDateTimeID(dataCreatedDateTimeID)
					.connectedUserID(connectedUserID)
					.requestHeader(totalErrorLogSaveRequestDTO.getRequestHeader())
					.userCookiesArray(totalErrorLogSaveRequestDTO.getUserCookies())
					.requestParameter(totalErrorLogSaveRequestDTO.getRequestParameter())
					.requestBody(totalErrorLogSaveRequestDTO.getRequestBody())
					.build())));
		}
	}

	/**
	 * <b>Log Back을 통해 Error Log를 저장하기 위한 API가 호출되면 totalErrorLogSaveRequestDTO 안에 Error Log를 저장하기 위한 Method</b>
	 *
	 * @param totalErrorLogSaveRequestDTO Server 정보, 이용자 정보, Error Log 정보가 담긴 요청 DTO 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processErrorLogInfoSave(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDTO,
		Long internalServerSaveId, Long occurrenceInfoDateTimeSaveId, Long connectedUserID,
		Long connectedUserRequestInfoSaveId) {

		if (totalErrorLogSaveRequestDTO == null) {
			throw new ErrorLogException(PARAMETER_NULL,
				PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
		} else if (internalServerSaveId == null) {
			throw new ServerInfoException(CONNECTED_INTERNAL_SERVER_SAVE_FAILURE,
				CONNECTED_INTERNAL_SERVER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (occurrenceInfoDateTimeSaveId == null) {
			throw new ConnectedUserException(ERROR_SAVE_FAILURE,
				ERROR_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (connectedUserID == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE,
				CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (connectedUserRequestInfoSaveId == null) {
			throw new ConnectedUserException(CONNECTED_USER_REQUEST_SAVE_FAILURE,
				CONNECTED_USER_REQUEST_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else {

			Long isIDbyErrorLogLevelIp = logManagementDao.findByErrorLogLevel(totalErrorLogSaveRequestDTO.getLevel());

			if (isIDbyErrorLogLevelIp == null) {
				throw new ErrorLogException(NOT_FOUND_ERROR_LOG_LEVEL, NOT_FOUND_ERROR_LOG_LEVEL.getMessage());
			}

			return logManagementDao.save(LogVo.toVO(ErrorLogSaveRequestDto.builder()
				.internalServerID(internalServerSaveId)
				.dataCreatedDateTimeID(occurrenceInfoDateTimeSaveId)
				.connectedUserID(connectedUserID)
				.connectedUserRequestInfoID(connectedUserRequestInfoSaveId)
				.logLevelID(isIDbyErrorLogLevelIp)
				.exceptionBrief(totalErrorLogSaveRequestDTO.getExceptionBrief())
				.exceptionDetail(totalErrorLogSaveRequestDTO.getExceptionDetail())
				.build()));
		}
	}

	/**
	 * <b>LogRequestDTO에 필수로 들어가야 하는 내용들이 있는지 확인하기 위한 Method</b>
	 *
	 * @param totalErrorLogSaveRequestDTO Log 정보를 담은 요청 객체
	 * @return Null이 있으면 false, 없으면 True 반환
	 */

	private boolean checkNotNull(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDTO) {
		return totalErrorLogSaveRequestDTO.getCreatedAt() != null && totalErrorLogSaveRequestDTO.getLevel() != null
			&& totalErrorLogSaveRequestDTO.getUserIp() != null && totalErrorLogSaveRequestDTO.getUserLocation() != null
			&& totalErrorLogSaveRequestDTO.getRequestHeader() != null
			&& totalErrorLogSaveRequestDTO.getRequestParameter() != null
			&& totalErrorLogSaveRequestDTO.getExceptionDetail() != null;
	}

	/**
	 * <b>Error Log Data Base 저장 시 Server 정보, 이용자 정보, 이용자 요청 정보, Error Log가 모두 잘 저장 되었는지 확인하기 위한 Method</b>
	 *
	 * @param internalServerSaveID   Server 정보 Data Base에 저장 뒤 저장 순서 번호(ID)
	 * @param connectedUserID        이용자 정보 Data Base에 저장 뒤 저장 순서 번호(ID)
	 * @param connectedUserRequestID 이용 요청 정보 Data Base에 저장 뒤 저장 순서 번호(ID)
	 * @param errorLogID             Error Log Data Base에 저장 뒤 저장 순서 번호(ID)
	 * @return Parameter로 전달된 순서 번호가 모두 Null이 아니면 True 하나라도 Null이면 False 반환
	 */
	private boolean errorLogProcessNullCheck(Long internalServerSaveID, Long occurrenceInfoDateTimeSaveID,
		Long connectedUserID, Long connectedUserRequestID, Long errorLogID) {
		return internalServerSaveID != null && occurrenceInfoDateTimeSaveID != null && connectedUserID != null
			&& connectedUserRequestID != null && errorLogID != null;
	}
}