package com.giggalpeople.backoffice.api.user.database.dao.impl;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.user.database.dao.UserInfoDao;
import com.giggalpeople.backoffice.api.user.database.mapper.UserManagementMapper;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDto;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVo;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;

import lombok.RequiredArgsConstructor;

/**
 * <h2><b>기깔나는 사람들 서비스 접속 이용자 정보 관리 DAO 구현체</b></h2>
 */

@RequiredArgsConstructor
@Repository
public class UserInfoDaoImpl implements UserInfoDao {

	private final UserManagementMapper userManagementMapper;

	/**
	 * <b>암호화 된 이용자 접속 정보를 Data Base에 저장하기 위한 Method</b>
	 * @param errorLogUserInfoVO 암호화된 이용자 정보
	 * @return Data Base에 저장된 이용자 접속 순서 번호
	 */

	@Transactional
	@Override
	public Long connectedUserSave(ErrorLogUserInfoVo errorLogUserInfoVO) {
		return userManagementMapper.connectedUserSave(errorLogUserInfoVO);
	}

	/**
	 * <b>동일 정보로 접속한 이용자가 있을 때, 해당 이용자의 정보를 Daba Base에 중복 저장하는 것이 아닌 접속 횟수를 올리기 위한 Method</b>
	 * @param updateUserInfo 이용자 접속 순서 번호와 접속일 순서 번호
	 * @return 접속 횟수 증가 이후 반환 된 PK 값
	 */
	@Transactional
	@Override
	public Long updateCount(UpdateUserInfo updateUserInfo) {
		Long updateCountId = userManagementMapper.updateCount(updateUserInfo);
		if (updateCountId == null) {
			throw new ConnectedUserException(CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE,
				CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE.getMessage());
		}
		return updateCountId;
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
	 * <b>이용자가 보낸 요청 정보를 Data Base에 저장하기 위한 Method</b>
	 * @param userRequestInfoVO 이용자가 보낸 요청 정보를 담은 VO
	 * @return Data Base 저장 뒤 저장된 요청 순서 번호
	 */
	@Transactional
	@Override
	public Long findByRequestInfoSave(UserRequestInfoVo userRequestInfoVO) {
		return userManagementMapper.findByRequestInfoSave(userRequestInfoVO);
	}

	/**
	 * <b>이용자 접속 및 요청 정보 목록 조회 시 일치하는 Data가 몇 개 있는지 알기 위한 Method</b>
	 * @param userInfoSearchDto 이용자 접속 및 요청 정보 검색을 위한 검색 Type과 검색어가 들어 있는 요청 DTO
	 * @return Data Base에서 조회된 Data의 ID 개수
	 */
	@Transactional(readOnly = true)
	@Override
	public int totalUserInfoSearchCount(UserInfoSearchDto userInfoSearchDto) {
		return userManagementMapper.totalUserInfoSearchCount(userInfoSearchDto);
	}

	/**
	 * <b>이용자 접속 및 요청 정보 목록 조회 시 1개만 검색 결과가 있을 경우 해당 Data Limit 절 타지 않고, Optional로 감싸 반환하기 위한 Method</b>
	 * @param userInfoSearchDTO 이용자 접속 및 요청 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */
	@Transactional(readOnly = true)
	@Override
	public Optional<ConnectedUserInfoVo> findByUserInfoSearchOneThing(UserInfoSearchDto userInfoSearchDTO) {
		return userManagementMapper.findByUserInfoSearchOneThing(userInfoSearchDTO);
	}

	/**
	 * <b>이용자 접속 및 요청 정보 목록 조회 시 여러 개 검색 결과가 있을 경우 해당 Data Limit 절 태우면서, Paging 처리 위한 Method</b>
	 * @param userInfoSearchDTO 이용자 접속 및 요청 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */
	@Transactional(readOnly = true)
	@Override
	public List<ConnectedUserInfoVo> findByUserInfoList(Criteria criteria, UserInfoSearchDto userInfoSearchDTO) {
		return userManagementMapper.findByUserInfoList(criteria, userInfoSearchDTO);
	}

	/**
	 * <b>이용자 접속 및 요청 정보 상세 조회를 위한 Method</b>
	 * @param connectedUserRequestInfoID Discord에서 입력 받은 조회할 이용자 요청 정보 ID
	 * @return 이용자 접속 및 요청 정보를 담은 VO 객체
	 */
	@Transactional(readOnly = true)
	@Override
	public Optional<ConnectedUserInfoVo> detailUserInfoFind(String connectedUserRequestInfoID) {
		return userManagementMapper.detailUserInfoFind(connectedUserRequestInfoID);
	}
}
