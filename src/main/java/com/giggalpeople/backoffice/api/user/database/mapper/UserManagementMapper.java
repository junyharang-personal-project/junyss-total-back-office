package com.giggalpeople.backoffice.api.user.database.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDto;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVo;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserRequestInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.UpdateUserRequestInfo;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;

/**
 * <h2><b>기깔나는 사람들 서비스 요청 이용자 정보 관리 Mapper</b></h2>
 */

@Mapper
@Repository
public interface UserManagementMapper {

	/**
	 * <b>기존에 요청한 정보가 있는지 확인하기 위한 Method로 동일 IP가 여러개 조회 될 수 있기 때문에 마지막 결과 값을 가져오기 위해 MAX() 사용</b>
	 * @param userIp 이용자 사용 IP
	 * @return Data Base에 저장된 접속 이용자 접속 순서 번호
	 */

	@Select("select MAX(connected_user_id) from connected_user where user_ip = #{userIp} ")
	Long findByUserIP(@Param("userIp") String userIp);

	/**
	 * <b>암호화 된 이용자 접속 정보를 Data Base에 저장하기 위한 Method</b>
	 * @param connectedUserInfoVo 암호화된 이용자 정보
	 * @return Data Base에 저장된 이용자 접속 순서 번호
	 */

	Long connectedUserSave(ConnectedUserInfoVo connectedUserInfoVo);

	/**
	 * <b>동일 정보로 요청한 이용자가 있을 때, 해당 이용자의 정보를 Daba Base에 중복 저장하는 것이 아닌 접속 횟수를 올리기 위한 Method</b>
	 * @param updateUserInfo 이용자 접속 순서 번호와 접속일 순서 번호
	 * @return 접속 횟수 증가 이후 반환 된 PK 값
	 */

	Long updateCount(@Param("updateUserInfo") UpdateUserInfo updateUserInfo);

	@Select("SELECT MAX(connected_user_id) from connected_user")
	Long getLastPrimaryKey();

	Long updateConnectedSameRequestCount(@Param("updateUserRequestInfo") UpdateUserRequestInfo updateUserRequestInfo);

	/**
	 * <b>이용자가 보낸 요청 정보를 Data Base에 저장하기 위한 Method</b>
	 * @param userRequestInfoVO 이용자가 보낸 요청 정보를 담은 VO
	 * @return Data Base 저장 뒤 저장된 요청 순서 번호
	 */

	Long findByRequestInfoSave(UserRequestInfoVo userRequestInfoVO);

	/**
	 * <b>요청 이용자 정보 및 요청 정보 목록 조회 시 일치하는 Data가 몇 개 있는지 알기 위한 Method</b>
	 * @param userInfoSearchDto 이용자 접속 및 요청 정보 검색을 위한 검색 Type과 검색어가 들어 있는 요청 DTO
	 * @return Data Base에서 조회된 Data의 ID 개수
	 */

	int totalUserInfoSearchCount(@Param("userInfoSearchDto") UserInfoSearchDto userInfoSearchDto);

	/**
	 * <b>요청 이용자 정보 및 요청 정보 목록 조회 시 1개만 검색 결과가 있을 경우 해당 Data Limit 절 타지 않고, Optional로 감싸 반환하기 위한 Method</b>
	 * @param userInfoSearchDto 이용자 접속 및 요청 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */

	Optional<ConnectedUserRequestInfoVo> findByUserInfoSearchOneThing(
		@Param("userInfoSearchDto") UserInfoSearchDto userInfoSearchDto);

	/**
	 * <b>요청 이용자 정보 및 요청 정보 목록 조회 시 여러 개 검색 결과가 있을 경우 해당 Data Limit 절 태우면서, Paging 처리 위한 Method</b>
	 * @param userInfoSearchDto 이용자 접속 및 요청 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */

	List<ConnectedUserRequestInfoVo> findByUserInfoList(@Param("criteria") Criteria criteria,
		@Param("userInfoSearchDto") UserInfoSearchDto userInfoSearchDto);

	/**
	 * <b>요청 이용자 정보 및 요청 정보 상세 조회를 위한 Method</b>
	 * @param connectedUserRequestInfoID Discord에서 입력 받은 조회할 이용자 요청 정보 ID
	 * @return 이용자 접속 및 요청 정보를 담은 VO 객체
	 */
	@Select("select dcdt.data_created_date, dcdt.data_created_time, si.*, cu.*, curi.* " +
		"from connected_user_request_info as curi " +
		"inner join connected_user as cu " +
		"on curi.connected_user_id = cu.connected_user_id " +
		"inner join data_created_date_time as dcdt " +
		"on curi.data_created_date_time_id = dcdt.data_created_date_time_id " +
		"inner join server_info as si " +
		"on curi.internal_server_id = si.internal_server_id " +
		"where curi.connected_user_request_info_id = #{connectedUserRequestInfoID}")
	Optional<ConnectedUserRequestInfoVo> detailUserInfoFind(String connectedUserRequestInfoID);
}
