package com.giggalpeople.backoffice.api.record.service;

import java.util.List;
import java.util.Map;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogSearchDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorLogSaveRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorLogListResponseDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorLogTotalDetailResponseDto;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;

/**
 * <h2><b>Log 관리 business Logic Interface</b></h2>
 */

public interface LogService {

	/**
	 * <b>Log 저장</b>
	 * @param totalErrorLogSaveRequestDto log 정보를 담은 DTO
	 * @return Log 저장 뒤 생성된 Log ID
	 */
	DefaultResponse<Map<String, Long>> save(TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDto);

	/**
	 * <b>Discord를 통한 Error Log 목록 조회 비즈니스 로직</b>
	 * @param criteria 페이징 처리를 위한 정보
	 * @param errorLogSearchDto 검색어(검색 조건) Request 객체
	 * @return 조회된 Error Log 목록 정보
	 */

	DefaultListResponse<List<ErrorLogListResponseDto>> toDiscordAllErrorInfoFind(Criteria criteria,
		ErrorLogSearchDto errorLogSearchDto);

	/**
	 * <b>Discord를 통한 Error Log 상세 조회를 위한 비즈니스 로직</b>
	 * @param errorLogDetailSearchRequestDto Discord에서 입력 받은 조회할 Log ID와 Crew 등급을 담은 DTO 객체
	 * @return Log 상세 정보를 담은 응답 DTO 객체
	 */

	DefaultResponse<ErrorLogTotalDetailResponseDto> toDiscordDetailErrorInfoFind(
		ErrorLogDetailSearchRequestDto errorLogDetailSearchRequestDto);
}
