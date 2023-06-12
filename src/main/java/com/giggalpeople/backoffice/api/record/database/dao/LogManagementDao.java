package com.giggalpeople.backoffice.api.record.database.dao;

import java.util.List;
import java.util.Optional;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogSearchDto;
import com.giggalpeople.backoffice.api.record.model.vo.LogTotalInfoVo;
import com.giggalpeople.backoffice.api.record.model.vo.LogVo;

/**
 * <h2><b>Log 관리 Mybatis를 이용한 Data Access Object</b></h2>
 */

public interface LogManagementDao {

	/**
	 * <b>Data Base에 등록된 Error Log Level을 찾기 위한 Method</b>
	 * @param level Application에서 발생된 Error Level
	 * @return 해당 Error Level PK
	 */
	Long findByErrorLogLevel(String level);

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

	int totalErrorLogSearchCount(ErrorLogSearchDto errorLogSearchDto);

	/**
	 * <b>Error Log 목록 조회 시 1개만 검색 결과가 있을 경우 해당 Data Limit 절 타지 않고, Optional로 감싸 반환하기 위한 Method</b>
	 * @param errorLogSearchDto Error Log 검색을 위한 DTO
	 * @return Data Base에서 조회된 Optional로 감싼 Data
	 */

	Optional<LogTotalInfoVo> findByErrorLogTotalInfoSearchOneThing(ErrorLogSearchDto errorLogSearchDto);

	/**
	 * <b>Error Log 목록 조회 시 1개 이상 검색 결과가 있을 경우 Limit 절을 태워 Paging 처리를 하기 위한 Method</b>
	 * @param criteria Paging 처리를 위한 객체
	 * @param errorLogSearchDto Error Log 검색을 위한 DTO
	 * @return Data Base에서 조회된 Data 목록
	 */
	List<LogTotalInfoVo> findByErrorLogTotalInfoList(Criteria criteria, ErrorLogSearchDto errorLogSearchDto);

	/**
	 * <b>Discord Bot을 통해 팀장 이상 크루가 Error Log 상세 조회 시 이용자 정보 포함한 정보 반환 Method</b>
	 * @param logId Error Log 순서 번호
	 * @return Data Base에서 Error Log 순서 번호에 해당하는 조회된 Error Log 상세 정보
	 */

	Optional<LogTotalInfoVo> detailErrorTotalInfoFind(String logId);

	/**
	 * <b>Discord Bot을 통해 팀장 이하 크루가 Error Log 상세 조회 시 이용자 정보 제외한 정보 반환 Method</b>
	 * @param logId Error Log 순서 번호
	 * @return Data Base에서 Error Log 순서 번호에 해당하는 조회된 Error Log 상세 정보
	 */

	Optional<LogTotalInfoVo> forGeneralCrewDetailErrorTotalInfoFind(String logId);
}
