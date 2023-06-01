package com.giggalpeople.backoffice.api.user.service.impl;

import com.giggalpeople.backoffice.api.common.database.dao.OccurrenceDataDateTimeManagementDAO;
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.Pagination;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDTO;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeRequestVO;
import com.giggalpeople.backoffice.api.server.database.dao.ServerInfoDAO;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVO;
import com.giggalpeople.backoffice.api.user.database.dao.UserInfoDAO;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoDetailSearchRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoDetailResponseDTO;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoListResponseDTO;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVO;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVO;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVO;
import com.giggalpeople.backoffice.api.user.service.ConnectedUserInfoService;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.env.exception.ServerInfoException;
import com.giggalpeople.backoffice.common.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;
import static com.giggalpeople.backoffice.common.enumtype.SuccessCode.CREATE;
import static com.giggalpeople.backoffice.common.enumtype.SuccessCode.SUCCESS;

/**
 * <h2><b>접속 이용자 정보 관리 business Logic 구현체</b></h2>
 */

@RequiredArgsConstructor
@Service
public class ConnectedUserInfoServiceImpl implements ConnectedUserInfoService {

    private final ServerInfoDAO serverInfoDAO;
    private final OccurrenceDataDateTimeManagementDAO occurrenceDataDateTimeManagementDAO;
    private final UserInfoDAO userInfoDAO;

    @Override
    public DefaultResponse<Map<String, Long>> save(UserRequestTotalInfoSaveRequestDTO userRequestTotalInfoSaveRequestDTO) {
        if (userRequestTotalInfoSaveRequestDTO == null) {
            throw new ConnectedUserException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long internalServerSaveID = processServerInfoSave(userRequestTotalInfoSaveRequestDTO.getServerInfo());
        Long occurrenceInfoDateTimeSaveID = processOccurrenceInfoDateTimeSave(userRequestTotalInfoSaveRequestDTO.getDataCreatedDateTimeRequestDTO());
        Long connectedUserSaveID = processUserInfoSave(userRequestTotalInfoSaveRequestDTO.getConnectedUserInfoSaveRequestDTO(), internalServerSaveID, occurrenceInfoDateTimeSaveID);
        Long connectedUserRequestInfoSaveID = processUserRequestInfoSave(userRequestTotalInfoSaveRequestDTO.getConnectedUserRequestInfoSaveRequestDTO(), internalServerSaveID, occurrenceInfoDateTimeSaveID, connectedUserSaveID);

        if (errorLogProcessNullCheck(internalServerSaveID, occurrenceInfoDateTimeSaveID, connectedUserSaveID, connectedUserRequestInfoSaveID)) {
            resultMap.put("내부 서버 정보 저장 순서 번호", internalServerSaveID);
            resultMap.put("Data 공통 저장 날짜, 시각 순서 번호", occurrenceInfoDateTimeSaveID);
            resultMap.put("이용자 정보 저장 순서 번호", connectedUserSaveID);
            resultMap.put("이용자 요청 정보 저장 순서 번호", connectedUserRequestInfoSaveID);

        } else {
            throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE, CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
        }
        return DefaultResponse.response(CREATE.getStatusCode(), CREATE.getMessage(), resultMap);
    }

    /**
     * <b>Application 이용자 접속 및 요청 정보 목록 조회를 위한 Method</b>
     *
     * @param criteria          페이징 처리를 위한 정보
     * @param userInfoSearchDTO 검색어(검색 조건) Request 객체
     * @return 조회된 이용자 접속 및 요청 목록 정보
     */
    @Override
    public DefaultListResponse<List<UserInfoListResponseDTO>> toDiscordAllUserInfoFind(Criteria criteria, UserInfoSearchDTO userInfoSearchDTO) {
        int searchListCount = userInfoDAO.totalUserInfoSearchCount(CryptoUtil.UserInfoSearchEncrypt(userInfoSearchDTO));

        if (searchListCount <= 0) {
            throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER, NOT_EXIST_CONNECTED_USER.getMessage());

        } else if (searchListCount == 1) {
            List<UserInfoListResponseDTO> responseDTOList = new ArrayList<>();

            Optional<ConnectedUserInfoVO> byUserInfoSearchOneThing = userInfoDAO.findByUserInfoSearchOneThing(userInfoSearchDTO);

            byUserInfoSearchOneThing.ifPresent(connectedUserInfoVO ->
                    responseDTOList.add(UserInfoListResponseDTO.toDTO(connectedUserInfoVO)));

            return DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(), new Pagination(criteria, searchListCount), responseDTOList);
        }

        return DefaultListResponse.response(
                SUCCESS.getStatusCode(),
                SUCCESS.getMessage(),
                new Pagination(criteria, searchListCount),
                userInfoDAO.findByUserInfoList(criteria, userInfoSearchDTO)
                        .stream()
                        .filter(Objects::nonNull)
                        .map(connectedUserInfoVO ->
                                UserInfoListResponseDTO.builder()
                                        .connectedUserRequestInfoID(connectedUserInfoVO.getConnectedUserRequestInfoID())
                                        .connectedDateTime(connectedUserInfoVO.getDataCreatedDate() + " " + connectedUserInfoVO.getDataCreatedTime())
                                        .serverName(connectedUserInfoVO.getServerName())
                                        .userIP(CryptoUtil.userInfoIPDecrypt(connectedUserInfoVO.getUserIP()))
                                        .build())
                        .collect(Collectors.toList()));
    }

    /**
     * <b>이용자 접속 및 요청 정보 상세 조회를 위한 Method</b>
     *
     * @param userInfoDetailSearchRequestDTO Discord에서 입력 받은 조회할 이용자 요청 정보 ID와 Crew 등급을 담은 DTO 객체
     * @return 이용자 접속 및 요청 정보를 담은 응답 DTO 객체
     */

    @Override
    public DefaultResponse<UserInfoDetailResponseDTO> toDiscordDetailConnectedUserInfoFind(UserInfoDetailSearchRequestDTO userInfoDetailSearchRequestDTO) {
        ConnectedUserInfoVO connectedUserInfoVO;

        if (userInfoDetailSearchRequestDTO.getCrewGrade().getGradeNum() > 3) {
            throw new ConnectedUserException(NO_AUTHORIZATION, NO_AUTHORIZATION.getMessage());
        }

        Optional<ConnectedUserInfoVO> byUserInfoVO = userInfoDAO.detailUserInfoFind(userInfoDetailSearchRequestDTO.getConnectedUserRequestInfoID());

        if (byUserInfoVO.isPresent()) {
            connectedUserInfoVO = CryptoUtil.userInfoDecrypt(byUserInfoVO.get());
        } else {
            throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER, NOT_EXIST_CONNECTED_USER.getMessage());
        }

        return DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(), new UserInfoDetailResponseDTO().toDTO(connectedUserInfoVO));
    }

    /**
     * <b>MDCFilter 통해 접속 이용자 정보를 저장하기 위한 API가 호출되면 userRequestTotalInfoSaveRequestDTO 안에 WAS 정보를 저장하기 위한 Method</b>
     *
     * @param serverInfo Server 정보를 담은 객체
     * @return Data Base에 저장 뒤 저장 순서 번호(ID)
     */

    private Long processServerInfoSave(ServerInfo serverInfo) {
        if (serverInfo == null) {
            throw new ServerInfoException(PARAMETER_NULL, PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
        }

        Long byServerIP = serverInfoDAO.findByServerIP(serverInfo.getServerIP());

        if (byServerIP == null) {
            return serverInfoDAO.save(
                    ServerInfoVO.toVO(
                            ServerInfoSaveRequestDTO.builder()
                                    .serverName(serverInfo.getServerName())
                                    .serverVmInfo(serverInfo.getVmInfo())
                                    .serverOsInfo(serverInfo.getOsInfo())
                                    .serverIP(serverInfo.getServerIP())
                                    .serverEnvironment(serverInfo.getServerEnvironment())
                                    .build()));
        }
        return byServerIP;
    }

    /**
     * <b>MDCFilter 통해 접속 이용자 정보를 저장하기 위한 API가 호출되면 userRequestTotalInfoSaveRequestDTO 안에 날짜와 시각 정보 저장하기 위한 Method</b>
     *
     * @param dataCreatedDateTimeRequestDTO MDCUtil이 호출된 일시를 담은 DTO 객체
     * @return Data Base에 저장 뒤 저장 순서 번호(ID)
     */

    private Long processOccurrenceInfoDateTimeSave(DataCreatedDateTimeRequestDTO dataCreatedDateTimeRequestDTO) {
        if (dataCreatedDateTimeRequestDTO == null) {
            throw new ConnectedUserException(PARAMETER_NULL, PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
        }

        Long byOccurrenceInfoDateTimeID = occurrenceDataDateTimeManagementDAO.findByOccurrenceInfoDateTime(
                dataCreatedDateTimeRequestDTO.getDataCreatedDate(),
                dataCreatedDateTimeRequestDTO.getDataCreatedTime());

        if (byOccurrenceInfoDateTimeID == null) {

            byOccurrenceInfoDateTimeID = occurrenceDataDateTimeManagementDAO.save(
                    DataCreatedDateTimeRequestVO.toVO(
                            DataCreatedDateTimeRequestDTO.builder()
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
     * @param connectedUserInfoSaveRequestDTO 이용자 정보가 담긴 요청 DTO 객체
     * @param internalServerSaveID            Server 정보 PK
     * @param occurrenceInfoDateTimeSaveID    MDCUtil 호출 일시 정보 PK
     * @return Data Base에 저장 뒤 저장 순서 번호(ID)
     */

    private Long processUserInfoSave(ConnectedUserInfoSaveRequestDTO connectedUserInfoSaveRequestDTO, Long internalServerSaveID, Long occurrenceInfoDateTimeSaveID) {
        if (connectedUserInfoSaveRequestDTO == null) {
            throw new ConnectedUserException(PARAMETER_NULL, PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
        } else if (internalServerSaveID == null) {
            throw new ServerInfoException(CONNECTED_INTERNAL_SERVER_SAVE_FAILURE, CONNECTED_INTERNAL_SERVER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
        } else if (occurrenceInfoDateTimeSaveID == null) {
            throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE, CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
        } else {
            Long isIDbyUserIP =
                    userInfoDAO.findByUserIP(
                            connectedUserInfoSaveRequestDTO.getUserIP().replace("\"", ""));

            if (isIDbyUserIP == null) {
                return userInfoDAO.connectedUserSave(
                        ErrorLogUserInfoVO.toVO(
                                ConnectedUserInfoSaveRequestDTO.builder()
                                        .internalServerID(internalServerSaveID)
                                        .dataCreatedDateTimeID(occurrenceInfoDateTimeSaveID)
                                        .userIP(connectedUserInfoSaveRequestDTO.getUserIP())
                                        .userLocation(connectedUserInfoSaveRequestDTO.getUserLocation())
                                        .userEnvironment(connectedUserInfoSaveRequestDTO.getUserEnvironment())
                                        .build()));

            } else {
                UpdateUserInfo updateUserInfo = new UpdateUserInfo(isIDbyUserIP, occurrenceInfoDateTimeSaveID);

                userInfoDAO.updateCount(
                        UpdateUserInfo.builder()
                                .connectedUserID(updateUserInfo.getConnectedUserID())
                                .dataCreatedDateTimeID(updateUserInfo.getDataCreatedDateTimeID())
                                .build());

                return userInfoDAO.getLastPrimaryKey();
            }
        }
    }

    /**
     * <b>MDCFilter를 통해 접속 이용자 정보를 저장하기 위한 API가 호출되면 userRequestTotalInfoSaveRequestDTO 안에 요청 이용자가 보낸 요청 정보를 저장하기 위한 Method</b>
     *
     * @param connectedUserRequestInfoSaveRequestDTO 이용자 요청 정보가 담긴 요청 DTO 객체
     * @return Data Base에 저장 뒤 저장 순서 번호(ID)
     */

    private Long processUserRequestInfoSave(ConnectedUserRequestInfoSaveRequestDTO connectedUserRequestInfoSaveRequestDTO, Long internalServerSaveID, Long occurrenceInfoDateTimeSaveID, Long connectedUserSaveID) {
        if (connectedUserRequestInfoSaveRequestDTO == null) {
            throw new ConnectedUserException(PARAMETER_NULL, PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)));
        } else if (internalServerSaveID == null) {
            throw new ServerInfoException(CONNECTED_INTERNAL_SERVER_SAVE_FAILURE, CONNECTED_INTERNAL_SERVER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
        } else if (occurrenceInfoDateTimeSaveID == null) {
            throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE, CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
        } else if (connectedUserSaveID == null) {
            throw new ConnectedUserException(CONNECTED_USER_SAVE_FAILURE, CONNECTED_USER_SAVE_FAILURE.getMessage(String.valueOf(NullPointerException.class)));
        } else {

            return userInfoDAO.findByRequestInfoSave(
                    UserRequestInfoVO.toVO(
                            ConnectedUserRequestInfoSaveRequestDTO.builder()
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

    /**
     * <b>서버 정보, 생성일시, 이용자 정보, 이용자 요청 정보가 Data Base에 정상적으로 저장 되었는지 확인하기 위한 Method</b>
     *
     * @param internalServerSaveID           서버 정보 PK
     * @param occurrenceInfoDateTimeSaveID   생성 일시 정보 PK
     * @param connectedUserSaveID            이용자 정보 PK
     * @param connectedUserRequestInfoSaveID 이용자 요청 정보 PK
     * @return 모두 Null이 아니면 True 하나라도 Null이면 False
     */
    private boolean errorLogProcessNullCheck(Long internalServerSaveID, Long occurrenceInfoDateTimeSaveID, Long connectedUserSaveID, Long connectedUserRequestInfoSaveID) {
        return internalServerSaveID != null && occurrenceInfoDateTimeSaveID != null && connectedUserSaveID != null && connectedUserRequestInfoSaveID != null;
    }
}
