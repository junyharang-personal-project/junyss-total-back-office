package com.giggalpeople.backoffice.api.record.controller;

import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_CALLER_DISCORD_BOT;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_PREFIX_URN;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_VERSION;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.LOG;

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
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorLogSearchDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorLogSaveRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorLogListResponseDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorLogTotalDetailResponseDto;
import com.giggalpeople.backoffice.api.record.service.LogService;
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
 * <h2><b>Log 관리 API</b></h2>
 */

@Tag(name = "Log 관리 API", description = "Log 관리를 위한 API 입니다.")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(API_PREFIX_URN + API_VERSION)
@RestController
public class LogManagementController {

	private final LogService logService;

	@Operation(summary = "Log 정보 저장", description = "Log 정보 저장하기 위한 API로써 해당 API는 자동으로 동작하는 API로 Client 개발 시 이용하지 않습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "등록 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@ExecutionTimeCheck
	@PostMapping(LOG)
	public DefaultResponse<Map<String, Long>> logSave(
		@Valid @RequestBody TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDTO) {
		return logService.save(totalErrorLogSaveRequestDTO);
	}

	@Operation(summary = "Discord Bot을 이용한 Log 목록 조회", description = "Log 목록 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "작업 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
	})
	@UserAccessInfoCheck
	@ExecutionTimeCheck
	@GetMapping(API_CALLER_DISCORD_BOT + LOG + "/lists")
	public DefaultListResponse<List<ErrorLogListResponseDto>> toDiscordAllErrorInfoFind(
		@ModelAttribute("criteria") Criteria criteria,
		@Valid @ModelAttribute("errorLogSearchDTO") ErrorLogSearchDto errorLogSearchDTO) {
		return logService.toDiscordAllErrorInfoFind(criteria, errorLogSearchDTO);
	}

	@Operation(summary = "Discord Bot을 이용한 Log 상세 조회", description = "Log 상세 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "작업 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
	})
	@UserAccessInfoCheck
	@ExecutionTimeCheck
	@GetMapping(API_CALLER_DISCORD_BOT + LOG + "/details")
	public DefaultResponse<ErrorLogTotalDetailResponseDto> toDiscordDetailErrorInfoFind(
		@Valid @ModelAttribute("errorLogDetailSearchRequestDTO") ErrorLogDetailSearchRequestDto errorLogDetailSearchRequestDTO) {
		return logService.toDiscordDetailErrorInfoFind(errorLogDetailSearchRequestDTO);
	}
}
