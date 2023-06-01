package com.giggalpeople.backoffice.api.user.database.dao.impl;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoDetailSearchRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDTO;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVO;
import com.giggalpeople.backoffice.api.user.request_info.model.UpdateUserRequestInfo;
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVO;
import com.giggalpeople.backoffice.api.user.database.dao.UserInfoDAO;
import com.giggalpeople.backoffice.api.user.database.mapper.UserManagementMapper;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CONNECTED_USER_REQUEST_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE;

/**
 * <h2><b>기깔나는 사람들 서비스 접속 이용자 정보 관리 DAO 구현체</b></h2>
 */

@RequiredArgsConstructor
@Repository
public class UserInfoDAOImpl implements UserInfoDAO {

    private final UserManagementMapper userManagementMapper;

    /**
     * <b>암호화 된 이용자 접속 정보를 Data Base에 저장하기 위한 Method</b>
     * @param errorLogUserInfoVO 암호화된 이용자 정보
     * @return Data Base에 저장된 이용자 접속 순서 번호
     */

    @Transactional
    @Override
    public Long connectedUserSave(ErrorLogUserInfoVO errorLogUserInfoVO) {
        return userManagementMapper.connectedUserSave(errorLogUserInfoVO);
    }

    @Transactional
    @Override
    public void updateCount(UpdateUserInfo updateUserInfo) {
        Long updateCount = userManagementMapper.updateCount(updateUserInfo);
        if (updateCount == null) {
            throw new ConnectedUserException(CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE, CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Long getLastPrimaryKey() {
        return userManagementMapper.getLastPrimaryKey();
    }

    @Override
    public void updateConnectedSameRequestCount(UpdateUserRequestInfo updateUserRequestInfo) {
        Long updateCount = userManagementMapper.updateConnectedSameRequestCount(updateUserRequestInfo);
        if (updateCount == null) {
            throw new ConnectedUserException(CONNECTED_USER_REQUEST_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE, CONNECTED_USER_REQUEST_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE.getMessage());
        }
    }

    @Override
    public Long getLastConnectedRequestPrimaryKey() {
        return userManagementMapper.getLastPrimaryKeyConnectedUserRequest();
    }

    /**
     * <b>기존에 이용자 접속한 정보가 있는지 확인하기 위한 Method</b>
     * @param userIp 이용자 사용 IP
     * @return Data Base에 저장된 접속 이용자 접속 순서 번호
     */

    @Transactional(readOnly = true)
    @Override
    public Long findByUserIP(String userIp) {
        return userManagementMapper.findByUserIP(userIp);
    }

    /**
     * <b>Exception 발생으로 인한 이용자 정보 Table에 동일 Server 정보가 Mapping 되어 있는지 확인하기 위한 Method</b>
     * @param internalServerSaveID Exception 발생 Server ID
     * @return Data Base에 Server 순서 번호
     */

    @Transactional(readOnly = true)
    @Override
    public Long findByServerID(Long internalServerSaveID) {
        return userManagementMapper.findByServerID(internalServerSaveID);
    }

    /**
     * <b>이용자가 요청한 요청 정보 중 기존에 접속한 정보가 있는지 확인하기 위한 Method</b>
     * @param requestHeader 이용자 요청 Header 정보
     * @param requestParameter 이용자 요청 Parameter 정보
     * @param requestBody 이용자 요청 Body 정보
     * @return Data Base에 저장된 이용자 접속 요청 순서 번호
     */

    @Transactional(readOnly = true)
    @Override
    public Long findByRequestInfo(String requestHeader, String requestParameter, String requestBody) {
        return userManagementMapper.findByRequestInfo(requestHeader, requestParameter, requestBody);
    }

    /**
     * <b>이용자가 보낸 요청 정보를 Data Base에 저장하기 위한 Method</b>
     * @param userRequestInfoVO 이용자가 보낸 요청 정보를 담은 VO
     * @return Data Base 저장 뒤 저장된 요청 순서 번호
     */
    @Transactional
    @Override
    public Long findByRequestInfoSave(UserRequestInfoVO userRequestInfoVO) {
        return userManagementMapper.findByRequestInfoSave(userRequestInfoVO);
    }

    /**
     * <b>이용자 접속 및 요청 정보 목록 조회 시 일치하는 Data가 몇 개 있는지 알기 위한 Method</b>
     * @param userInfoSearchDTO 이용자 접속 및 요청 정보 검색을 위한 검색 Type과 검색어가 들어 있는 요청 DTO
     * @return Data Base에서 조회된 Data의 ID 개수
     */
    @Transactional(readOnly = true)
    @Override
    public int totalUserInfoSearchCount(UserInfoSearchDTO userInfoSearchDTO) {
        return userManagementMapper.totalUserInfoSearchCount(userInfoSearchDTO);
    }

    /**
     * <b>이용자 접속 및 요청 정보 목록 조회 시 1개만 검색 결과가 있을 경우 해당 Data Limit 절 타지 않고, Optional로 감싸 반환하기 위한 Method</b>
     * @param userInfoSearchDTO 이용자 접속 및 요청 검색을 위한 DTO
     * @return Data Base에서 조회된 Optional로 감싼 Data
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<ConnectedUserInfoVO> findByUserInfoSearchOneThing(UserInfoSearchDTO userInfoSearchDTO) {
        return userManagementMapper.findByUserInfoSearchOneThing(userInfoSearchDTO);
    }

    /**
     * <b>이용자 접속 및 요청 정보 목록 조회 시 여러 개 검색 결과가 있을 경우 해당 Data Limit 절 태우면서, Paging 처리 위한 Method</b>
     * @param userInfoSearchDTO 이용자 접속 및 요청 검색을 위한 DTO
     * @return Data Base에서 조회된 Optional로 감싼 Data
     */
    @Transactional(readOnly = true)
    @Override
    public List<ConnectedUserInfoVO> findByUserInfoList(Criteria criteria, UserInfoSearchDTO userInfoSearchDTO) {
        return userManagementMapper.findByUserInfoList(criteria, userInfoSearchDTO);
    }

    /**
     * <b>이용자 접속 및 요청 정보 목록 조회 시 Data Base 저장 Data 개수를 가져오기 위한 Method</b>
     * @return Data Base에서 조회된 Data 개수
     */
    @Transactional(readOnly = true)
    @Override
    public int listTotalCount() {
        return userManagementMapper.listTotalCount();
    }

    /**
     * <b>이용자 접속 및 요청 정보 상세 조회를 위한 Method</b>
     * @param connectedUserRequestInfoID Discord에서 입력 받은 조회할 이용자 요청 정보 ID
     * @return 이용자 접속 및 요청 정보를 담은 VO 객체
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<ConnectedUserInfoVO> detailUserInfoFind(String connectedUserRequestInfoID) {
        return userManagementMapper.detailUserInfoFind(connectedUserRequestInfoID);
    }
}
