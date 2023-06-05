package com.giggalpeople.backoffice.api.user.controller;

import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.*;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoDetailSearchRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoDetailResponseDTO;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoListResponseDTO;
import com.giggalpeople.backoffice.api.user.service.ConnectedUserInfoService;
import com.giggalpeople.backoffice.common.annotaion.ExecutionTimeCheck;
import com.giggalpeople.backoffice.common.annotaion.UserAccessInfoCheck;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>이용자 접속 및 요청 정보 관리 API</b></h2>
 */

@Tag(name = "이용자 접속 및 요청 정보 관리 API", description = "이용자 접속 및 요청 정보 관리를 위한 API 입니다.")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(API_PREFIX_URN + API_VERSION)
@RestController
public class ConnectedUserInfoController {

	private final ConnectedUserInfoService connectedUserInfoService;

	@Operation(summary = "접속 이용자 정보 저장", description = "접속 이용자 정보 저장하기 위한 API로써 해당 API는 자동으로 동작하는 API로 Client 개발 시 이용하지 않습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "등록 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@ExecutionTimeCheck
	@PostMapping(CONNECTED_USER)
	public DefaultResponse<Map<String, Long>> save(
		@Valid @RequestBody UserRequestTotalInfoSaveRequestDTO userRequestTotalInfoSaveRequestDTO) {
		return connectedUserInfoService.save(userRequestTotalInfoSaveRequestDTO);
	}

	@Operation(summary = "Discord Bot을 이용한 이용자 접속 및 요청 정보 목록 조회", description = "이용자 접속 및 요청 정보 목록 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "작업 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
	})
	@UserAccessInfoCheck
	@ExecutionTimeCheck
	@GetMapping(API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
	public DefaultListResponse<List<UserInfoListResponseDTO>> toDiscordAllConnectedUserInfoFind(
		@ModelAttribute("criteria") Criteria criteria,
		@Valid @ModelAttribute("errorLogSearchDTO") UserInfoSearchDTO userInfoSearchDTO) {
		return connectedUserInfoService.toDiscordAllUserInfoFind(criteria, userInfoSearchDTO);
	}

	@Operation(summary = "Discord Bot을 이용한 이용자 접속 및 요청 정보 상세 조회", description = "이용자 접속 및 요청 정보 상세 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "작업 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
	})
	@UserAccessInfoCheck
	@ExecutionTimeCheck
	@GetMapping(API_CALLER_DISCORD_BOT + CONNECTED_USER + "/details")
	public DefaultResponse<UserInfoDetailResponseDTO> toDiscordDetailConnectedUserInfoFind(
		@Valid @ModelAttribute("userInfoDetailSearchRequestDTO") UserInfoDetailSearchRequestDTO userInfoDetailSearchRequestDTO) {
		return connectedUserInfoService.toDiscordDetailConnectedUserInfoFind(userInfoDetailSearchRequestDTO);
	}
}
