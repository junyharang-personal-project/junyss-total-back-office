package com.giggalpeople.backoffice.api.log.controller;

import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.*;
import static com.giggalpeople.backoffice.api.log.model.dto.enumtype.ErrorLogSearchType.*;
import static com.giggalpeople.backoffice.common.enumtype.CrewGrade.*;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;
import static com.giggalpeople.backoffice.common.enumtype.SuccessCode.*;
import static com.giggalpeople.backoffice.common.env.ServerType.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.Pagination;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;
import com.giggalpeople.backoffice.api.log.model.dto.enumtype.ErrorLogSearchType;
import com.giggalpeople.backoffice.api.log.model.dto.request.ErrorLogDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.log.model.dto.request.ErrorLogSearchDto;
import com.giggalpeople.backoffice.api.log.model.dto.request.TotalErrorLogSaveRequestDto;
import com.giggalpeople.backoffice.api.log.model.dto.response.ErrorLogListResponseDto;
import com.giggalpeople.backoffice.api.log.model.dto.response.ErrorLogTotalDetailResponseDto;
import com.giggalpeople.backoffice.api.log.model.vo.LogTotalInfoVo;
import com.giggalpeople.backoffice.api.log.service.LogService;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.enumtype.GiggalPeopleServerNames;
import com.giggalpeople.backoffice.common.util.CryptoUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(LogManagementController.class)
class LogManagementControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	LogService logService;

	DataCreatedDateTimeVo dataCreatedDateTimeVo;

	ServerInfo serverInfo;
	ServerInfoVo serverInfoVo;

	ErrorLogUserInfoVo userInfoVo;

	ErrorLogSearchDto searchForErrorLogId;

	ErrorLogSearchDto searchForCreatedErrorLogDateRange;

	ErrorLogSearchDto errorLogSearchCreatedErrorLogDateDto;

	ErrorLogSearchDto searchForErrorExceptionBrief;

	ErrorLogSearchDto searchForServerName;

	ErrorLogSearchDto searchForServerIp;

	ErrorLogSearchDto searchForUserIp;

	ErrorLogSearchDto searchForErrorLogLevel;

	DataCreatedDateTimeRequestDto dataCreatedDateTimeRequestDto;

	ServerInfoSaveRequestDto serverInfoSaveRequestDto;

	ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDto;

	ConnectedUserRequestInfoSaveRequestDto connectedUserRequestInfoSaveRequestDto;

	UserRequestInfoVo userRequestInfoVo;

	UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestDto;

	TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDto;

	Criteria criteria;

	List<ErrorLogListResponseDto> errorLogList;

	@BeforeEach
	void beforeTestSetup() {
		this.totalErrorLogSaveRequestDto = initializedMockModels();

		initializedMockSearchRequestDto();
	}

	/**
	 * <b>가짜 검색 요청 DTO를 만들기 위한 Method</b>
	 */
	private void initializedMockSearchRequestDto() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		initializedMockSearchErrorLogIdRequestDto();
		initializedMockSearchUserConnectedDateRangeRequestDto(splitNowDateTime[0]);
		initializedMockSearchCreatedErrorLogDateRequestDto(splitNowDateTime[0]);
		initializedMockSearchServerNameRequestDto();
		initializedMockSearchServerIpRequestDto();
		initializedMockSearchUserIpRequestDto();
		initializedMockDetailRequestDto();
		initializedMockCriteria();
		initializedMocErrorLogInfoList();
		initializedMockSearchErrorLogLevelRequestDto();
		initializedMockSearchErrorLogExceptionBriefRequestDto();
	}

	@Test
	@Order(0)
	@DisplayName("Error Log 정보 Data Base 저장 테스트")
	void logSave() throws Exception {
		//given
		Map<String, Long> resultMap = new HashMap<>();
		resultMap.put("내부 서버 정보 저장 순서 번호", 1L);
		resultMap.put("Data 공통 저장 날짜, 시각 순서 번호", 1L);
		resultMap.put("이용자 정보 저장 순서 번호", 1L);
		resultMap.put("이용자 요청 정보 저장 순서 번호", 1L);
		resultMap.put("Error Log 저장 순서 번호", 1L);

		given(logService.save(any())).willReturn(
			DefaultResponse.response(CREATE.getStatusCode(), CREATE.getMessage(), resultMap));

		//when
		mockMvc.perform(post(API_PREFIX_URN + API_VERSION + LOG).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(initializedMockModels())))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(CREATE.getStatusCode()))
			.andExpect(jsonPath("$.message").value(CREATE.getMessage()));
	}

	@Test
	@Order(1)
	@DisplayName("Discord Bot을 이용한 Log 목록 조회 테스트")
	void toDiscordAllErrorInfoFind() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(2)
	@DisplayName("Error Log 정보 Paging 처리 목록 조회 테스트")
	void toDiscordAllErrorInfoFindPaging() throws Exception {
		//given
		Criteria mockCriteria = new Criteria();
		mockCriteria.setPage(3);
		mockCriteria.setPerPageNum(5);

		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(mockCriteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(3))
				.param("perPageNum", String.valueOf(5)))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(jsonPath("$.pagination.criteria.page").value(3))
			.andExpect(jsonPath("$.pagination.criteria.perPageNum").value(5))
			.andExpect(jsonPath("$.pagination.startPage").value(1))
			.andExpect(jsonPath("$.pagination.endPage").value(3))
			.andExpect(jsonPath("$.pagination.totalCount").value(11));
	}

	@Test
	@Order(3)
	@DisplayName("Error Log 정보 생성 순서 번호를 통한 검색 테스트")
	void toDiscordAllErrorInfoSearchId() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForErrorLogId.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForErrorLogId.getSearchWord())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForErrorLogId.getSearchWord())));
	}

	@Test
	@Order(4)
	@DisplayName("Error Log 정보 생성일 검색 테스트")
	void toDiscordAllErrorInfoSearchCreatedLogDate() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(errorLogSearchCreatedErrorLogDateDto.getInputSearchType()))
				.param("date", String.valueOf(errorLogSearchCreatedErrorLogDateDto.getDate())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(5)
	@DisplayName("Error Log 정보 생성일 범위 검색 테스트")
	void toDiscordAllErrorInfoSearchCreatedLogDateRange() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForCreatedErrorLogDateRange.getInputSearchType()))
				.param("startDate", String.valueOf(searchForCreatedErrorLogDateRange.getStartDate()))
				.param("endDate", String.valueOf(searchForCreatedErrorLogDateRange.getEndDate())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(6)
	@DisplayName("Error Log Level 검색 테스트")
	void toDiscordAllErrorInfoSearchLogLevel() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForErrorLogLevel.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForErrorLogLevel.getSearchWord())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForErrorLogLevel.getSearchWord())));
	}

	@Test
	@Order(7)
	@DisplayName("내부 서버 이름 검색 테스트")
	void toDiscordAllErrorInfoSearchServerName() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForServerName.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForServerName.getSearchWord())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(TOTAL_BACK_OFFICE.getDescription())));
	}

	@Test
	@Order(8)
	@DisplayName("내부 서버 IP 주소 검색 테스트")
	void toDiscordAllErrorInfoSearchServerIp() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForServerIp.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForServerIp.getSearchWord())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForServerIp.getSearchWord())));
	}

	@Test
	@Order(9)
	@DisplayName("이용자 IP 주소 검색 테스트")
	void toDiscordAllErrorInfoSearchUserIp() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForUserIp.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForUserIp.getSearchWord())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(10)
	@DisplayName("Exception 간략 내용 검색 테스트")
	void toDiscordAllErrorInfoSearchExceptionBrief() throws Exception {
		//given
		given(logService.toDiscordAllErrorInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, errorLogList.size()), errorLogList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForErrorExceptionBrief.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForErrorExceptionBrief.getSearchWord())))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForErrorExceptionBrief.getSearchWord())));
	}

	@Test
	@Order(11)
	@DisplayName("Error Log 상세 조회 테스트")
	void toDiscordDetailErrorInfoFind() throws Exception {
		//given
		String[] initializedCreateDateTime = initializedCreateDateTime();

		given(logService.toDiscordDetailErrorInfoFind(initializedMockDetailRequestDto())).willReturn(
			DefaultResponse.response(
				SUCCESS.getStatusCode(),
				SUCCESS.getMessage(),
				new ErrorLogTotalDetailResponseDto().toDTO(
					CryptoUtil.forGeneralCrewErrorLogDetailRequestInfoDecrypt(initializedLogTotalInfoVo()))));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/details")
				.contentType(MediaType.APPLICATION_JSON)
				.param("logId", "1")
				.param("crewGrade", String.valueOf(TEAM_LEADER)))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForErrorLogId.getSearchWord())))
			.andExpect(content().string(containsString(initializedCreateDateTime[0])))
			.andExpect(content().string(containsString(searchForErrorLogLevel.getSearchWord())))
			.andExpect(content().string(containsString(TOTAL_BACK_OFFICE.getDescription())))
			.andExpect(content().string(containsString(searchForServerIp.getSearchWord())))
			.andExpect(content().string(containsString(searchForErrorExceptionBrief.getSearchWord())));
	}

	@Test
	@Order(12)
	@DisplayName("Error Log 상세 조회 시 권한 없음 테스트")
	void toDiscordDetailErrorInfoFindUnAuthorized() throws Exception {
		//given
		given(logService.toDiscordDetailErrorInfoFind(any())).willReturn(
			DefaultResponse.response(NO_AUTHORIZATION.getStatusCode(), NO_AUTHORIZATION.getMessage(), null));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + LOG + "/details")
				.contentType(MediaType.APPLICATION_JSON)
				.param("logId", "1")
				.param("crewGrade", String.valueOf(GENERAL_CREW)))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(NO_AUTHORIZATION.getStatusCode()))
			.andExpect(jsonPath("$.message").value(NO_AUTHORIZATION.getMessage()));
	}

	/**
	 * <b>상세 조회 요청 DTO 만들기 위한 Method</b>
	 */
	private ErrorLogDetailSearchRequestDto initializedMockDetailRequestDto() {
		ErrorLogDetailSearchRequestDto errorLogDetailSearchRequestDto = new ErrorLogDetailSearchRequestDto();
		errorLogDetailSearchRequestDto.setLogId("1");
		errorLogDetailSearchRequestDto.setCrewGrade(TEAM_LEADER);

		return errorLogDetailSearchRequestDto;
	}

	/**
	 * <b>가짜 ErrorLog 정보를 만들기 위한 Method</b>
	 */
	private void initializedMocErrorLogInfoList() {
		this.errorLogList = new ArrayList<>();

		IntStream.rangeClosed(1, 11).forEach(count -> {
			errorLogList.add(ErrorLogListResponseDto.builder()
				.logId((long)count)
				.createdDateTime(this.totalErrorLogSaveRequestDto.getCreatedAt())
				.level(this.totalErrorLogSaveRequestDto.getLevel())
				.serverName(this.totalErrorLogSaveRequestDto.getServerName())
				.serverIP("192.168.20." + count)
				.exceptionBrief(this.totalErrorLogSaveRequestDto.getExceptionBrief())
				.build());
		});
	}

	/**
	 * <b>Paging 처리를 위한 Criteria 객체를 만들기 위한 Method</b>
	 */
	private void initializedMockCriteria() {
		this.criteria = new Criteria();
		this.criteria.setPage(1);
		this.criteria.setPerPageNum(10);
		this.criteria.setPageMoveButtonNum(10);
	}

	/**
	 * <b>이용자 IP 주소를 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchUserIpRequestDto() {
		this.searchForUserIp = new ErrorLogSearchDto();
		this.searchForUserIp.setInputSearchType(ErrorLogSearchType.USER_IP);
		this.searchForUserIp.setSearchWord("127.0.0.1");
	}

	/**
	 * <b>내부 서버 IP 주소를 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchServerIpRequestDto() {
		this.searchForServerIp = new ErrorLogSearchDto();
		this.searchForServerIp.setInputSearchType(ErrorLogSearchType.SERVER_IP);
		this.searchForServerIp.setSearchWord("192.168.20.2");
	}

	/**
	 * <b>내부 서버 이름을 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchServerNameRequestDto() {
		this.searchForServerName = new ErrorLogSearchDto();
		this.searchForServerName.setInputSearchType(ErrorLogSearchType.SERVER_NAME);
		this.searchForServerName.setSearchWord("통합관리서버");
	}

	/**
	 * <b>Error Log 생성일 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchCreatedErrorLogDateRequestDto(String searchDate) {
		this.errorLogSearchCreatedErrorLogDateDto = new ErrorLogSearchDto();
		this.errorLogSearchCreatedErrorLogDateDto.setInputSearchType(LOG_CREATE_DATE);
		this.errorLogSearchCreatedErrorLogDateDto.setDate(searchDate);
	}

	/**
	 * <b>Error Log 생성일 범위 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */

	private void initializedMockSearchUserConnectedDateRangeRequestDto(String nowDate) {
		this.searchForCreatedErrorLogDateRange = new ErrorLogSearchDto();
		this.searchForCreatedErrorLogDateRange.setInputSearchType(LOG_CREATE_DATE);
		this.searchForCreatedErrorLogDateRange.setStartDate("2023-01-01");
		this.searchForCreatedErrorLogDateRange.setEndDate(nowDate);
	}

	/**
	 * <b>Error Log 생성 순서 번호 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchErrorLogIdRequestDto() {
		this.searchForErrorLogId = new ErrorLogSearchDto();
		this.searchForErrorLogId.setInputSearchType(LOG_ID);
		this.searchForErrorLogId.setSearchWord("1");
	}

	/**
	 * <b>Error Log 생성 순서 번호 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchErrorLogLevelRequestDto() {
		this.searchForErrorLogLevel = new ErrorLogSearchDto();
		this.searchForErrorLogLevel.setInputSearchType(LOG_LEVEL);
		this.searchForErrorLogLevel.setSearchWord("ERROR");
	}

	/**
	 * <b>Error Log Exception 간략 내용으로 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchErrorLogExceptionBriefRequestDto() {
		this.searchForErrorExceptionBrief = new ErrorLogSearchDto();
		this.searchForErrorExceptionBrief.setInputSearchType(EXCEPTION_BRIEF);
		this.searchForErrorExceptionBrief.setSearchWord(
			"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
	}

	/**
	 * <b>가짜 Error Log 발생 DTO를 만든기 위한 Method</b>
	 * @return Error Log 발생 정보를 담은 요청 DTO
	 */
	private TotalErrorLogSaveRequestDto initializedMockModels() {
		this.dataCreatedDateTimeVo = initializedCreateDateTimeVo();
		this.serverInfoVo = initializedServerInfo();
		this.userInfoVo = initializedConnectedUserInfo();
		this.userRequestInfoVo = initializedConnectedUserRequestInfo();
		this.userRequestTotalInfoSaveRequestDto = initializedConnectedUserRequestTotalInfo();

		String[] createDateTimeArray = initializedCreateDateTime();

		TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDto = new TotalErrorLogSaveRequestDto();
		totalErrorLogSaveRequestDto.setCreatedAt(createDateTimeArray[0] + " " + createDateTimeArray[1]);
		totalErrorLogSaveRequestDto.setLevel("ERROR");
		totalErrorLogSaveRequestDto.setServerName(this.serverInfo.getServerName());
		totalErrorLogSaveRequestDto.setVmInfo(this.serverInfo.getVmInfo());
		totalErrorLogSaveRequestDto.setOsInfo(this.serverInfo.getOsInfo());
		totalErrorLogSaveRequestDto.setServerIP(this.serverInfo.getServerIP());
		totalErrorLogSaveRequestDto.setServerEnvironment(this.serverInfo.getServerEnvironment());
		totalErrorLogSaveRequestDto.setUserIp(this.userInfoVo.getUserIP());
		totalErrorLogSaveRequestDto.setUserLocation(this.userInfoVo.getUserLocation());
		totalErrorLogSaveRequestDto.setRequestHeader(this.userInfoVo.getUserEnvironment());
		totalErrorLogSaveRequestDto.setUserCookies(null);
		totalErrorLogSaveRequestDto.setRequestParameter(this.userRequestInfoVo.getRequestParameter());
		totalErrorLogSaveRequestDto.setRequestBody(this.userRequestInfoVo.getRequestBody());
		totalErrorLogSaveRequestDto.setExceptionBrief(
			"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
		totalErrorLogSaveRequestDto.setExceptionDetail(
			"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request\n"
				+ "at com.giggalpeople.backoffice.common.util.StringUtil.checkSearchCommandForInternalServerName(StringUtil.java:116)\n"
				+ "at com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDTO.getSearchType(UserInfoSearchDTO.java:105)\n"
				+ "at com.giggalpeople.backoffice.common.util.CryptoUtil.userInfoSearchEncrypt(CryptoUtil.java:272)\n"
				+ "at com.giggalpeople.backoffice.api.user.service.impl.ConnectedUserInfoServiceImpl.toDiscordAllUserInfoFind(ConnectedUserInfoServiceImpl.java:101)\n"
				+ "at com.giggalpeople.backoffice.api.user.controller.ConnectedUserInfoController.toDiscordAllConnectedUserInfoFind(ConnectedUserInfoController.java:74)\n"
				+ "at com.giggalpeople.backoffice.api.user.controller.ConnectedUserInfoController$$FastClassBySpringCGLIB$$842df735.invoke(<generated>)\n"
				+ "at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\n"
				+ "at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\n"
				+ "at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n"
				+ "at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\n"
				+ "at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:89)");

		return totalErrorLogSaveRequestDto;
	}

	private UserRequestTotalInfoSaveRequestDto initializedConnectedUserRequestTotalInfo() {
		return UserRequestTotalInfoSaveRequestDto.builder()
			.serverInfo(serverInfo)
			.dataCreatedDateTimeRequestDTO(dataCreatedDateTimeRequestDto)
			.connectedUserInfoSaveRequestDTO(connectedUserInfoSaveRequestDto)
			.connectedUserRequestInfoSaveRequestDTO(connectedUserRequestInfoSaveRequestDto)
			.build();
	}

	/**
	 * <b>이용자 요청 정보를 만들기 위한 Method</b>
	 * @return 이용자 요청 정보 Value Object
	 */
	private UserRequestInfoVo initializedConnectedUserRequestInfo() {
		this.connectedUserRequestInfoSaveRequestDto = ConnectedUserRequestInfoSaveRequestDto.builder()
			.internalServerID(1L)
			.dataCreatedDateTimeID(1L)
			.connectedUserID(1L)
			.userCookiesArray(null)
			.userCookies("내용 없음")
			.requestHeader("\"sec-fetch-mode\" : \"cors\",\n")
			.requestParameter("\"perPageNum\" : \"10\",\n" + "\"displayPageNum\" : \"10\",\n"
				+ "\"inputSearchType\" : \"USER_CONNECTED_DATE\",\n" + "\"endDate\" : \"2023-06-01\",\n"
				+ "\"page\" : \"1\",\n" + "\"startDate\" : \"2023-01-01\"")
			.requestBody("")
			.build();

		return UserRequestInfoVo.toVo(connectedUserRequestInfoSaveRequestDto);
	}

	/**
	 * <b>이용자 접속 정보를 만들기 위한 Method</b>
	 * @return 이용자 요청 정보를 담은 Value Object
	 */
	private ErrorLogUserInfoVo initializedConnectedUserInfo() {
		this.connectedUserInfoSaveRequestDto = ConnectedUserInfoSaveRequestDto.builder()
			.internalServerID(1L)
			.dataCreatedDateTimeID(1L)
			.userIP("127.0.0.1")
			.userLocation(
				"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n" + "\"ip\" : \"127.0.0.1\",\n"
					+ "\"error\" : \"true\",\n" + "\"version\" : \"IPv4\"\n")
			.userEnvironment("\"Mozilla/5.0\"")
			.build();

		return ErrorLogUserInfoVo.toVO(connectedUserInfoSaveRequestDto);
	}

	/**
	 * <b>내부 서버 정보 만들기 위한 Method</b>
	 * @return 내부 서버 정보 담은 Value Object
	 */
	private ServerInfoVo initializedServerInfo() {
		this.serverInfo = ServerInfo.builder()
			.serverName(GiggalPeopleServerNames.GIGGAL_TOTAL_BACK_OFFICE.getDescription())
			.vmInfo("OpenJDK 64-Bit")
			.osInfo("Mac OS")
			.serverIP("192.168.20.254")
			.serverEnvironment("dev")
			.build();

		this.serverInfoSaveRequestDto = ServerInfoSaveRequestDto.builder()
			.serverName(serverInfo.getServerName())
			.serverVmInfo(serverInfo.getVmInfo())
			.serverOsInfo(serverInfo.getOsInfo())
			.serverIP(serverInfo.getServerIP())
			.serverEnvironment(serverInfo.getServerEnvironment())
			.build();

		return ServerInfoVo.toVo(serverInfoSaveRequestDto);
	}

	private LogTotalInfoVo initializedLogTotalInfoVo() {
		String[] initializedCreateDateTime = initializedCreateDateTime();

		return new LogTotalInfoVo(
			1L,
			initializedCreateDateTime[0],
			initializedCreateDateTime[1],
			totalErrorLogSaveRequestDto.getLevel(),
			totalErrorLogSaveRequestDto.getServerName(),
			totalErrorLogSaveRequestDto.getVmInfo(),
			totalErrorLogSaveRequestDto.getOsInfo(),
			totalErrorLogSaveRequestDto.getServerIP(),
			totalErrorLogSaveRequestDto.getServerEnvironment(),
			totalErrorLogSaveRequestDto.getUserIp(),
			totalErrorLogSaveRequestDto.getUserEnvironment(),
			totalErrorLogSaveRequestDto.getUserLocation(),
			totalErrorLogSaveRequestDto.getRequestHeader(),
			null,
			totalErrorLogSaveRequestDto.getRequestParameter(),
			totalErrorLogSaveRequestDto.getRequestBody(),
			totalErrorLogSaveRequestDto.getExceptionBrief(),
			totalErrorLogSaveRequestDto.getExceptionDetail());
	}

	/**
	 * <b>이용자 접속 및 요청일시를 만들기 위한 Method</b>
	 * @return 접속 및 요청 일시를 담은 Value Object
	 */
	private DataCreatedDateTimeVo initializedCreateDateTimeVo() {
		String[] splitNowDateTime = initializedCreateDateTime();

		this.dataCreatedDateTimeRequestDto = DataCreatedDateTimeRequestDto.builder()
			.createdDate(splitNowDateTime[0])
			.createdTime(splitNowDateTime[1])
			.build();

		return DataCreatedDateTimeVo.toVO(dataCreatedDateTimeRequestDto);
	}

	/**
	 * <b>생성 일시를 처리하기 위한 Method</b>
	 * @return 날짜(yyyy - MM - dd)와 시간(HH:mm:ss)가 분리된 두 개의 배열
	 */
	private String[] initializedCreateDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");
	}
}