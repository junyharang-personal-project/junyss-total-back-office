package com.giggalpeople.backoffice.api.record.database.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogSearchDto;
import com.giggalpeople.backoffice.api.record.model.vo.LogLevelVo;
import com.giggalpeople.backoffice.api.record.model.vo.LogTotalInfoVo;
import com.giggalpeople.backoffice.api.record.model.vo.LogVo;

/**
 * <h2><b>Log 관리 Mybatis를 이용한 Data Base Handler</b></h2>
 */

@Mapper
@Repository
public interface LogManagementMapper {

	/**
	 * <b>Data Base에 등록된 Error Log Level을 찾기 위한 Method</b>
	 * @param level Application에서 발생된 Error Level
	 * @return 해당 Error Level PK
	 */

	@Select("select log_level_id from log_level where level = #{level}")
	Long findByErrorLogLevel(String level);

	/**
	 * <b>Error Log Level Table에 Error Log Level 저장</b>
	 * @param logLevelVo Application에서 발생된 Error Level
	 * @return 저장 뒤 해당 Error Level PK
	 */

	Long errorLogLevelSave(LogLevelVo logLevelVo);

	/**
	 * <b>Log 저장</b>
	 * @param logVO log 정보를 Data Base에 저장하기 위한 Value Object
	 * @return Log 저장 뒤 생성된 Log ID
	 */

	Long save(LogVo logVO);

	/**
	 * <b>Error Log 목록 조회 시 일치하는 Data가 몇 개 있는지 알기 위한 Method</b>
	 * @param errorLogSearchDto Error Log 검색을 위한 검색 Type과 검색어가 들어 있는 요청 DTO
	 * @return Data Base에서 조회된 Data의 ID 개수
	 */

	int totalErrorLogSearchCount(@Param("errorLogSearchDto") ErrorLogSearchDto errorLogSearchDto);

	/**
	 * <b>Error Log 목록 조회 시 1개만 검색 결과가 있을 경우 해당 Data Limit 절 타지 않고, Optional로 감싸 반환하기 위한 Method</b>
	 * @param errorLogSearchDto Error Log 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */

	Optional<LogTotalInfoVo> findByErrorLogInfoSearchOneThing(
		@Param("errorLogSearchDto") ErrorLogSearchDto errorLogSearchDto);

	/**
	 * <b>Error Log 목록 조회 시 1개 이상 검색 결과가 있을 경우 Limit 절을 태워 Paging 처리를 하기 위한 Method</b>
	 * @param criteria Paging 처리를 위한 객체
	 * @param errorLogSearchDto Error Log 검색을 위한 DTO
	 * @return Data Base에서 조회된 Data 목록
	 */

	List<LogTotalInfoVo> findByErrorLogInfoList(@Param("criteria") Criteria criteria,
		@Param("errorLogSearchDto") ErrorLogSearchDto errorLogSearchDto);

	/**
	 * <b>Discord Bot을 통해 팀장 이상 크루가 Error Log 상세 조회 시 이용자 정보 포함한 정보 반환 Method</b>
	 * @param logId Error Log 순서 번호
	 * @return Data Base에서 Error Log 순서 번호에 해당하는 조회된 Error Log 상세 정보
	 */

	@Select("select dcdt.data_created_date, dcdt.data_created_time, si.*, cu.*, curi.*, ll.level, l.* " +
		"from log as l " +
		"inner join connected_user_request_info as curi " +
		"on l.connected_user_request_info_id = curi.connected_user_request_info_id " +
		"inner join connected_user as cu " +
		"on l.connected_user_id = cu.connected_user_id " +
		"inner join data_created_date_time as dcdt " +
		"on l.data_created_date_time_id = dcdt.data_created_date_time_id " +
		"inner join log_level as ll " +
		"on l.log_level_id = ll.log_level_id " +
		"inner join server_info as si " +
		"on l.internal_server_id = si.internal_server_id " +
		"where l.log_id = #{logId}")
	Optional<LogTotalInfoVo> detailErrorInfoFind(String logId);

	/**
	 * <b>Discord Bot을 통해 팀장 이하 크루가 Error Log 상세 조회 시 이용자 정보 제외한 정보 반환 Method</b>
	 * @param logId Error Log 순서 번호
	 * @return Data Base에서 Error Log 순서 번호에 해당하는 조회된 Error Log 상세 정보
	 */
	@Select("select dcdt.data_created_date, dcdt.data_created_time, si.*, ll.level, l.* " +
		"from log as l " +
		"inner join data_created_date_time as dcdt " +
		"on l.data_created_date_time_id = dcdt.data_created_date_time_id " +
		"inner join log_level as ll " +
		"on l.log_level_id = ll.log_level_id " +
		"inner join server_info as si " +
		"on l.internal_server_id = si.internal_server_id " +
		"where l.log_id = #{logId}")
	Optional<LogTotalInfoVo> forGeneralCrewDetailErrorInfoFind(String logId);
}
