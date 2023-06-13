package com.giggalpeople.backoffice.api.user.service.impl;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CONNECTED_INTERNAL_SERVER_SAVE_FAILURE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CONNECTED_USER_SAVE_FAILURE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_EXIST_CONNECTED_USER;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NO_AUTHORIZATION;
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
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.Pagination;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;
import com.giggalpeople.backoffice.api.server.database.dao.ServerInfoDao;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.api.user.database.dao.UserInfoDao;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoDetailResponseDto;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoListResponseDto;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVo;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserRequestInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;
import com.giggalpeople.backoffice.api.user.service.ConnectedUserInfoService;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.env.exception.ServerInfoException;
import com.giggalpeople.backoffice.common.util.CryptoUtil;

import lombok.RequiredArgsConstructor;

/**
 * <h2><b>접속 이용자 정보 관리 business Logic 구현체</b></h2>
 */

@RequiredArgsConstructor
@Service
public class ConnectedUserInfoServiceImpl implements ConnectedUserInfoService {

	private final ServerInfoDao serverInfoDAO;
	private final OccurrenceDataDateTimeManagementDao occurrenceDataDateTimeManagementDAO;
	private final UserInfoDao userInfoDAO;

	/**
	 * <b>접속 이용자 정보 Data Base 저장을 위한 Method</b>
	 * @param userRequestTotalInfoSaveRequestDto 접속 일시, 이용자 정보, 이용자 요청 정보를 담은 DTO
	 * @return 접속 일시 저장 PK, 이용자 정보 PK, 이용자 요청 정보 PK를 묶은 Map
	 */

	@Override
	public DefaultResponse<Map<String, Long>> save(
		UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestDto) {
		if (userRequestTotalInfoSaveRequestDto == null) {
			throw new ConnectedUserException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
		}

		Map<String, Long> resultMap = new HashMap<>();

		Long internalServerSaveID = processServerInfoSave(userRequestTotalInfoSaveRequestDto.getServerInfo());
		Long occurrenceInfoDateTimeSaveID = processOccurrenceInfoDateTimeSave(
			userRequestTotalInfoSaveRequestDto.getDataCreatedDateTimeRequestDTO());
		Long connectedUserSaveID = processUserInfoSave(
			userRequestTotalInfoSaveRequestDto.getConnectedUserInfoSaveRequestDTO(), internalServerSaveID,
			occurrenceInfoDateTimeSaveID);
		Long connectedUserRequestInfoSaveID = processUserRequestInfoSave(
			userRequestTotalInfoSaveRequestDto.getConnectedUserRequestInfoSaveRequestDTO(), internalServerSaveID,
			occurrenceInfoDateTimeSaveID, connectedUserSaveID);

		resultMap.put("내부 서버 정보 저장 순서 번호", internalServerSaveID);
		resultMap.put("Data 공통 저장 날짜, 시각 순서 번호", occurrenceInfoDateTimeSaveID);
		resultMap.put("이용자 정보 저장 순서 번호", connectedUserSaveID);
		resultMap.put("이용자 요청 정보 저장 순서 번호", connectedUserRequestInfoSaveID);

		return DefaultResponse.response(CREATE.getStatusCode(), CREATE.getMessage(), resultMap);
	}

	/**
	 * <b>Application 이용자 접속 및 요청 정보 목록 조회를 위한 Method</b>
	 *
	 * @param criteria          페이징 처리를 위한 정보
	 * @param userInfoSearchDto 검색어(검색 조건) Request 객체
	 * @return 조회된 이용자 접속 및 요청 목록 정보
	 */
	@Override
	public DefaultListResponse<List<UserInfoListResponseDto>> toDiscordAllUserInfoFind(Criteria criteria,
		UserInfoSearchDto userInfoSearchDto) {
		int searchListCount = userInfoDAO.totalUserInfoSearchCount(CryptoUtil.userInfoSearchEncrypt(userInfoSearchDto));

		if (searchListCount <= 0) {
			throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER, NOT_EXIST_CONNECTED_USER.getMessage());

		} else if (searchListCount == 1) {
			List<UserInfoListResponseDto> responseDtoList = new ArrayList<>();

			Optional<ConnectedUserRequestInfoVo> byUserInfoSearchOneThing = userInfoDAO.findByUserInfoSearchOneThing(
				userInfoSearchDto);

			byUserInfoSearchOneThing.ifPresent(connectedUserInfoVO ->
				responseDtoList.add(UserInfoListResponseDto.toDTO(connectedUserInfoVO)));

			return DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, searchListCount), responseDtoList);
		}

		return DefaultListResponse.response(
			SUCCESS.getStatusCode(),
			SUCCESS.getMessage(),
			new Pagination(criteria, searchListCount),
			userInfoDAO.findByUserInfoList(criteria, userInfoSearchDto)
				.stream()
				.filter(Objects::nonNull)
				.map(connectedUserInfoVO ->
					UserInfoListResponseDto.builder()
						.connectedUserRequestInfoID(connectedUserInfoVO.getConnectedUserRequestInfoID())
						.connectedDateTime(
							connectedUserInfoVO.getDataCreatedDate() + " " + connectedUserInfoVO.getDataCreatedTime())
						.serverName(connectedUserInfoVO.getServerName())
						.userIP(CryptoUtil.userInfoIPDecrypt(connectedUserInfoVO.getUserIp()))
						.build())
				.collect(Collectors.toList()));
	}

	/**
	 * <b>이용자 접속 및 요청 정보 상세 조회를 위한 Method</b>
	 *
	 * @param userInfoDetailSearchRequestDto Discord에서 입력 받은 조회할 이용자 요청 정보 ID와 Crew 등급을 담은 DTO 객체
	 * @return 이용자 접속 및 요청 정보를 담은 응답 DTO 객체
	 */

	@Override
	public DefaultResponse<UserInfoDetailResponseDto> toDiscordDetailConnectedUserInfoFind(
		UserInfoDetailSearchRequestDto userInfoDetailSearchRequestDto) {

		if (userInfoDetailSearchRequestDto.getCrewGrade().getGradeNum() > 3) {
			throw new ConnectedUserException(NO_AUTHORIZATION, NO_AUTHORIZATION.getMessage());
		}

		Optional<ConnectedUserRequestInfoVo> byUserInfoVO = userInfoDAO.detailUserInfoFind(
			userInfoDetailSearchRequestDto.getConnectedUserRequestInfoID());

		if (byUserInfoVO.isPresent()) {
			return DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				CryptoUtil.userInfoDecrypt(byUserInfoVO.get()));

		} else {
			throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER, NOT_EXIST_CONNECTED_USER.getMessage());
		}

	}

	/**
	 * <b>MDCFilter 통해 접속 이용자 정보를 저장하기 위한 API가 호출되면 userRequestTotalInfoSaveRequestDTO 안에 WAS 정보를 저장하기 위한 Method</b>
	 *
	 * @param serverInfo Server 정보를 담은 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processServerInfoSave(ServerInfo serverInfo) {
		if (serverInfo == null) {
			throw new ServerInfoException(PARAMETER_NULL,
				PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
		}

		Long findByServerID = serverInfoDAO.findByServerID(serverInfo.getServerIP());

		if (findByServerID == null || findByServerID <= 0) {
			return serverInfoDAO.save(
				ServerInfoVo.toVo(
					ServerInfoSaveRequestDto.builder()
						.serverName(serverInfo.getServerName())
						.serverVmInfo(serverInfo.getVmInfo())
						.serverOsInfo(serverInfo.getOsInfo())
						.serverIP(serverInfo.getServerIP())
						.serverEnvironment(serverInfo.getServerEnvironment())
						.build()));
		}
		return findByServerID;
	}

	/**
	 * <b>MDCFilter 통해 접속 이용자 정보를 저장하기 위한 API가 호출되면 userRequestTotalInfoSaveRequestDTO 안에 날짜와 시각 정보 저장하기 위한 Method</b>
	 *
	 * @param dataCreatedDateTimeRequestDTO MDCUtil이 호출된 일시를 담은 DTO 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processOccurrenceInfoDateTimeSave(DataCreatedDateTimeRequestDto dataCreatedDateTimeRequestDTO) {
		if (dataCreatedDateTimeRequestDTO == null) {
			throw new ConnectedUserException(PARAMETER_NULL,
				PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
		}

		Long byOccurrenceInfoDateTimeID = occurrenceDataDateTimeManagementDAO.findByOccurrenceInfoDateTime(
			dataCreatedDateTimeRequestDTO.getDataCreatedDate(),
			dataCreatedDateTimeRequestDTO.getDataCreatedTime());

		if (byOccurrenceInfoDateTimeID == null || byOccurrenceInfoDateTimeID <= 0) {

			byOccurrenceInfoDateTimeID = occurrenceDataDateTimeManagementDAO.save(
				DataCreatedDateTimeVo.toVO(
					DataCreatedDateTimeRequestDto.builder()
						.createdDate(dataCreatedDateTimeRequestDTO.getDataCreatedDate())
						.createdTime(dataCreatedDateTimeRequestDTO.getDataCreatedTime())
						.build()));

			return byOccurrenceInfoDateTimeID;
		} else {
			return byOccurrenceInfoDateTimeID;
		}
	}

	/**
	 * <b>MDCFilter 통해 접속 이용자 정보를 저장하기 위한 API가 호출되면 userRequestTotalInfoSaveRequestDTO 안에 접속 및 요청을 보낸 이용자 정보를 저장하기 위한 Method</b>
	 *
	 * @param connectedUserInfoSaveRequestDto 이용자 정보가 담긴 요청 DTO 객체
	 * @param internalServerSaveID            Server 정보 PK
	 * @param occurrenceInfoDateTimeSaveID    MDCUtil 호출 일시 정보 PK
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processUserInfoSave(ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDto,
		Long internalServerSaveID, Long occurrenceInfoDateTimeSaveID) {
		if (connectedUserInfoSaveRequestDto == null) {
			throw new ConnectedUserException(PARAMETER_NULL,
				PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
		} else if (internalServerSaveID == null) {
			throw new ServerInfoException(CONNECTED_INTERNAL_SERVER_SAVE_FAILURE,
				CONNECTED_INTERNAL_SERVER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (occurrenceInfoDateTimeSaveID == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE,
				CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else {
			Long isIDbyUserIP =
				userInfoDAO.findByUserIP(
					connectedUserInfoSaveRequestDto.getUserIP().replace("\"", ""));

			if (isIDbyUserIP == null || isIDbyUserIP <= 0) {
				return userInfoDAO.connectedUserSave(
					ConnectedUserInfoVo.toVO(
						CryptoUtil.userInfoEncrypt(
							ConnectedUserInfoSaveRequestDto.builder()
								.internalServerID(internalServerSaveID)
								.dataCreatedDateTimeID(occurrenceInfoDateTimeSaveID)
								.userIP(connectedUserInfoSaveRequestDto.getUserIP())
								.userLocation(connectedUserInfoSaveRequestDto.getUserLocation())
								.userEnvironment(connectedUserInfoSaveRequestDto.getUserEnvironment())
								.build())));

			} else {
				UpdateUserInfo updateUserInfo = new UpdateUserInfo(isIDbyUserIP, occurrenceInfoDateTimeSaveID);

				return userInfoDAO.updateCount(
					UpdateUserInfo.builder()
						.connectedUserID(updateUserInfo.getConnectedUserID())
						.dataCreatedDateTimeID(updateUserInfo.getDataCreatedDateTimeID())
						.build());
			}
		}
	}

	/**
	 * <b>MDCFilter를 통해 접속 이용자 정보를 저장하기 위한 API가 호출되면 userRequestTotalInfoSaveRequestDTO 안에 요청 이용자가 보낸 요청 정보를 저장하기 위한 Method</b>
	 *
	 * @param connectedUserRequestInfoSaveRequestDTO 이용자 요청 정보가 담긴 요청 DTO 객체
	 * @return Data Base에 저장 뒤 저장 순서 번호(ID)
	 */

	private Long processUserRequestInfoSave(
		ConnectedUserRequestInfoSaveRequestDto connectedUserRequestInfoSaveRequestDTO, Long internalServerSaveID,
		Long occurrenceInfoDateTimeSaveID, Long connectedUserSaveID) {
		if (connectedUserRequestInfoSaveRequestDTO == null) {
			throw new ConnectedUserException(PARAMETER_NULL,
				PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
		} else if (internalServerSaveID == null) {
			throw new ServerInfoException(CONNECTED_INTERNAL_SERVER_SAVE_FAILURE,
				CONNECTED_INTERNAL_SERVER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (occurrenceInfoDateTimeSaveID == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE,
				CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else if (connectedUserSaveID == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE,
				CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
		} else {

			return userInfoDAO.findByRequestInfoSave(
				UserRequestInfoVo.toVo(
					ConnectedUserRequestInfoSaveRequestDto.builder()
						.internalServerID(internalServerSaveID)
						.dataCreatedDateTimeID(occurrenceInfoDateTimeSaveID)
						.connectedUserID(connectedUserSaveID)
						.requestHeader(connectedUserRequestInfoSaveRequestDTO.getRequestHeader())
						.userCookies(connectedUserRequestInfoSaveRequestDTO.getUserCookies())
						.requestParameter(connectedUserRequestInfoSaveRequestDTO.getRequestParameter())
						.requestBody(connectedUserRequestInfoSaveRequestDTO.getRequestBody())
						.build()));
		}
	}
}
