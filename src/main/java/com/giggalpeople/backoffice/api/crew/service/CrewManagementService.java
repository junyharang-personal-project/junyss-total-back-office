package com.giggalpeople.backoffice.api.crew.service;

import java.util.List;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewJoinRequestDto;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewPeopleManagementSearchDto;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewSuggestPeopleManagementSearchDto;
import com.giggalpeople.backoffice.api.crew.model.dto.request.SuggestRequestDto;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewDetailResponseDto;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewListResponseDto;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewSuggestDetailResponseDto;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewSuggestListResponseDto;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;

/**
 * <h2><b>크루 합류 지원서 관리 business Logic Interface</b></h2>
 */

public interface CrewManagementService {

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 내용을 저장.</b>
	 * @param suggestRequestDTO 크루 합류 지원자 신청 정보
	 * @return Long - 등록된 지원자 번호
	 */

	DefaultResponse<List<Long>> allSuggestInfoSave(List<SuggestRequestDto> suggestRequestDTO);

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류가 결정된 크루 정보를 해당 Method를 통해 Data Base에 내용을 저장.</b>
	 * @param crewJoinRequestDTO 크루 합류 지원자 내부 사용정보
	 * @return Long - 크루 합류 순서 번호
	 */

	DefaultResponse<List<Long>> join(List<CrewJoinRequestDto> crewJoinRequestDTO);

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 지원자 정보 목록 조회</b>
	 * @param criteria 페이징 처리를 위한 정보
	 * @param crewSuggestSearchDTO 검색어(검색 조건) Request 객체
	 * @return CrewSuggestListResponseDTO - 지원자 목록 조회 결과
	 */

	DefaultListResponse<List<CrewSuggestListResponseDto>> allSuggestInfoFind(Criteria criteria,
		CrewSuggestPeopleManagementSearchDto crewSuggestSearchDTO);

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 지원자 정보 한 건 조회</b>
	 * @param suggestId Data Base 등록 시 만들어진 Index 번호
	 * @return CrewSuggestDetailResponseDTO - 지원자 상세 정보
	 */

	DefaultResponse<CrewSuggestDetailResponseDto> detailSuggestInfoFind(String suggestId);

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 크루 정보 목록 조회</b>
	 * @param criteria 페이징 처리를 위한 정보
	 * @param crewSearchDTO 검색어(검색 조건) Request 객체
	 * @return CrewListResponseDTO - 등록된 지원자 정보
	 */

	DefaultListResponse<List<CrewListResponseDto>> allCrewInfoFind(Criteria criteria,
		CrewPeopleManagementSearchDto crewSearchDTO);

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 크루 정보 한 건 조회</b>
	 * @param crewNumber Google App Script에서 지원자 지원 시 생성하는 크루 번호
	 * @return CrewDetailResponseDTO - 크루 상세 정보
	 */

	DefaultResponse<CrewDetailResponseDto> detailCrewInfoFind(String crewNumber);

	/**
	 * <b>크루 중 중도 포기, 강제 퇴출 등으로 인해 탈회 처리가 되면 크루 정보 삭제</b>
	 * @param crewNumber Google App Script에서 지원자 지원 시 생성하는 크루 번호
	 * @return Long - 삭제된 crew_join_id
	 */

	DefaultResponse<Long> deleteCrewInfo(String crewNumber);
}
