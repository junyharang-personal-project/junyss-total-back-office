package com.giggalpeople.backoffice.api.user.service;

import java.util.List;
import java.util.Map;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoDetailSearchRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoDetailResponseDTO;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoListResponseDTO;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;

/**
 * <h2><b>접속 이용자 정보 관리 business Logic Interface</b></h2>
 */

public interface ConnectedUserInfoService {

	/**
	 * <b>접속 이용자 정보 Data Base 저장을 위한 Method</b>
	 * @param userRequestTotalInfoSaveRequestDTO 접속 일시, 이용자 정보, 이용자 요청 정보를 담은 DTO
	 * @return 접속 일시 저장 PK, 이용자 정보 PK, 이용자 요청 정보 PK를 묶은 Map
	 */
	DefaultResponse<Map<String, Long>> save(UserRequestTotalInfoSaveRequestDTO userRequestTotalInfoSaveRequestDTO);

	/**
	 * <b>이용자 접속 및 요청 정보 목록 조회를 위한 Method</b>
	 * @param criteria 페이징 처리를 위한 정보
	 * @param userInfoSearchDTO 검색어(검색 조건) Request 객체
	 * @return 조회된 이용자 접속 및 요청 목록 정보
	 */
	DefaultListResponse<List<UserInfoListResponseDTO>> toDiscordAllUserInfoFind(Criteria criteria,
		UserInfoSearchDTO userInfoSearchDTO);

	/**
	 * <b>이용자 접속 및 요청 정보 상세 조회를 위한 Method</b>
	 * @param userInfoDetailSearchRequestDTO Discord에서 입력 받은 조회할 이용자 요청 정보 ID와 Crew 등급을 담은 DTO 객체
	 * @return 이용자 접속 및 요청 정보를 담은 응답 DTO 객체
	 */

	DefaultResponse<UserInfoDetailResponseDTO> toDiscordDetailConnectedUserInfoFind(
		UserInfoDetailSearchRequestDTO userInfoDetailSearchRequestDTO);
}
