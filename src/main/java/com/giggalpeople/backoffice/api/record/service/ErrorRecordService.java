package com.giggalpeople.backoffice.api.record.service;

import java.util.List;
import java.util.Map;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorRecordDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorRecordSearchDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorRecordSaveRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorRecordListResponseDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorRecordTotalDetailResponseDto;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;

/**
 * <h2><b>Log 관리 business Logic Interface</b></h2>
 */

public interface ErrorRecordService {

	/**
	 * <b>Log 저장</b>
	 * @param totalErrorRecordSaveRequestDto log 정보를 담은 DTO
	 * @return Log 저장 뒤 생성된 Log ID
	 */
	DefaultResponse<Map<String, Long>> save(TotalErrorRecordSaveRequestDto totalErrorRecordSaveRequestDto);

	/**
	 * <b>Discord를 통한 Error Log 목록 조회 비즈니스 로직</b>
	 * @param criteria 페이징 처리를 위한 정보
	 * @param errorRecordSearchDto 검색어(검색 조건) Request 객체
	 * @return 조회된 Error Log 목록 정보
	 */

	DefaultListResponse<List<ErrorRecordListResponseDto>> toDiscordAllErrorInfoFind(Criteria criteria,
		ErrorRecordSearchDto errorRecordSearchDto);

	/**
	 * <b>Discord를 통한 Error Log 상세 조회를 위한 비즈니스 로직</b>
	 * @param errorRecordDetailSearchRequestDto Discord에서 입력 받은 조회할 Log ID와 Crew 등급을 담은 DTO 객체
	 * @return Log 상세 정보를 담은 응답 DTO 객체
	 */

	DefaultResponse<ErrorRecordTotalDetailResponseDto> toDiscordDetailErrorInfoFind(
		ErrorRecordDetailSearchRequestDto errorRecordDetailSearchRequestDto);
}
