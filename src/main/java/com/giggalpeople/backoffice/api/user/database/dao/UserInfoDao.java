package com.giggalpeople.backoffice.api.user.database.dao;

import java.util.List;
import java.util.Optional;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDto;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVo;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserRequestInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;

/**
 * <h2><b>기깔나는 사람들 서비스 접속 이용자 정보 관리 DAO</b></h2>
 */
public interface UserInfoDao {

	/**
	 * <b>기존에 이용자 접속한 정보가 있는지 확인하기 위한 Method</b>
	 * @param userIp 이용자 사용 IP
	 * @return Data Base에 저장된 접속 이용자 접속 순서 번호
	 */

	Long findByUserIP(String userIp);

	/**
	 * <b>암호화 된 이용자 접속 정보를 Data Base에 저장하기 위한 Method</b>
	 * @param connectedUserInfoVO 암호화된 이용자 정보
	 * @return Data Base에 저장된 이용자 접속 순서 번호
	 */
	Long connectedUserSave(ConnectedUserInfoVo connectedUserInfoVO);

	Long updateCount(UpdateUserInfo updateUserInfo);

	/**
	 * <b>이용자가 보낸 요청 정보를 Data Base에 저장하기 위한 Method</b>
	 * @param userRequestInfoVO 이용자가 보낸 요청 정보를 담은 VO
	 * @return Data Base 저장 뒤 저장된 요청 순서 번호
	 */
	Long findByRequestInfoSave(UserRequestInfoVo userRequestInfoVO);

	/**
	 * <b>이용자 접속 및 요청 정보 목록 조회 시 일치하는 Data가 몇 개 있는지 알기 위한 Method</b>
	 * @param userInfoSearchDTO 이용자 접속 및 요청 정보 검색을 위한 검색 Type과 검색어가 들어 있는 요청 DTO
	 * @return Data Base에서 조회된 Data의 ID 개수
	 */
	int totalUserInfoSearchCount(UserInfoSearchDto userInfoSearchDTO);

	/**
	 * <b>이용자 접속 및 요청 정보 목록 조회 시 1개만 검색 결과가 있을 경우 해당 Data Limit 절 타지 않고, Optional로 감싸 반환하기 위한 Method</b>
	 * @param userInfoSearchDTO 이용자 접속 및 요청 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */
	Optional<ConnectedUserRequestInfoVo> findByUserInfoSearchOneThing(UserInfoSearchDto userInfoSearchDTO);

	/**
	 * <b>이용자 접속 및 요청 정보 목록 조회 시 여러 개 검색 결과가 있을 경우 해당 Data Limit 절 태우면서, Paging 처리 위한 Method</b>
	 * @param userInfoSearchDTO 이용자 접속 및 요청 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */

	List<ConnectedUserRequestInfoVo> findByUserInfoList(Criteria criteria, UserInfoSearchDto userInfoSearchDTO);

	/**
	 * <b>이용자 접속 및 요청 정보 상세 조회를 위한 Method</b>
	 * @param connectedUserRequestInfoID Discord에서 입력 받은 조회할 이용자 요청 정보 ID
	 * @return 이용자 접속 및 요청 정보를 담은 VO 객체
	 */
	Optional<ConnectedUserRequestInfoVo> detailUserInfoFind(String connectedUserRequestInfoID);
}
