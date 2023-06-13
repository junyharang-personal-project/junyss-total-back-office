package com.giggalpeople.backoffice.api.error_record.service;

import static com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType.EXCEPTION_BRIEF;
import static com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType.LOG_CREATE_DATE;
import static com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType.LOG_ID;
import static com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType.LOG_LEVEL;
import static com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType.SERVER_IP;
import static com.giggalpeople.backoffice.common.enumtype.CrewGrade.GENERAL_CREW;
import static com.giggalpeople.backoffice.common.enumtype.CrewGrade.TEAM_LEADER;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_EXIST_ERROR_LOG;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.PARAMETER_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.common.database.dao.OccurrenceDataDateTimeManagementDao;
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;
import com.giggalpeople.backoffice.api.record.exception.ErrorRecordException;
import com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorRecordDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.ErrorRecordSearchDto;
import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorRecordSaveRequestDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorRecordListResponseDto;
import com.giggalpeople.backoffice.api.record.model.dto.response.ErrorRecordTotalDetailResponseDto;
import com.giggalpeople.backoffice.api.record.service.ErrorRecordService;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.api.user.database.dao.UserInfoDao;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.database.DataBaseManagerMapper;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.enumtype.GiggalPeopleServerNames;
import com.giggalpeople.backoffice.common.env.exception.ServerInfoException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ErrorRecordServiceImplTest {

	@Autowired
	ErrorRecordService errorRecordService;

	@Autowired
	OccurrenceDataDateTimeManagementDao occurrenceDataDateTimeManagementDao;

	@Autowired
	UserInfoDao userInfoDao;

	@Autowired
	DataBaseManagerMapper dataBaseManagerMapper;

	DataCreatedDateTimeVo dataCreatedDateTimeVo;

	ServerInfo serverInfo;
	ServerInfoVo serverInfoVo;

	ErrorLogUserInfoVo userInfoVo;

	ErrorRecordSearchDto searchForErrorLogId;

	ErrorRecordSearchDto searchForCreatedErrorLogDateRange;

	ErrorRecordSearchDto errorLogSearchCreatedErrorLogDateDto;

	ErrorRecordSearchDto searchForErrorExceptionBrief;

	ErrorRecordSearchDto searchForServerName;

	ErrorRecordSearchDto searchForServerIp;

	ErrorRecordSearchDto searchForUserIp;

	ErrorRecordSearchDto searchForErrorLogLevel;

	DataCreatedDateTimeRequestDto dataCreatedDateTimeRequestDto;

	ServerInfoSaveRequestDto serverInfoSaveRequestDto;

	ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDto;

	ConnectedUserRequestInfoSaveRequestDto connectedUserRequestInfoSaveRequestDto;

	UserRequestInfoVo userRequestInfoVo;

	UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestDto;

	TotalErrorRecordSaveRequestDto totalErrorRecordSaveRequestDto;

	Criteria criteria;

	List<ErrorRecordListResponseDto> errorLogList;

	@BeforeEach
	void beforeTestSetup() {
		totalErrorRecordSaveRequestDto = initializedMockModels();

		initializedMockSearchRequestDto();
	}

	/**
	 * <b>가짜 검색 요청 DTO를 만들기 위한 Method</b>
	 */
	private void initializedMockSearchRequestDto() {
		String[] initializedCreateDateTime = initializedCreateDateTime();

		initializedMockSearchErrorLogIdRequestDto();
		initializedMockSearchUserConnectedDateRangeRequestDto(initializedCreateDateTime[0]);
		initializedMockSearchCreatedErrorLogDateRequestDto(initializedCreateDateTime[0]);
		initializedMockSearchServerNameRequestDto();
		initializedMockSearchServerIpRequestDto();
		initializedMockSearchUserIpRequestDto();
		initializedMockCriteria();
		initializedMocErrorLogInfoList();
		initializedMockSearchErrorLogLevelRequestDto();
		initializedMockSearchErrorRecordExceptionBriefRequestDto();
	}

	@Test
	@Order(0)
	@DisplayName("ErrorLog 기본 목록 조회 시 검색 결과 없을 때, 문제 사항 테스트")
	@Transactional
	void toDiscordAllErrorInfoFindNotFound() {
		//given
		ErrorRecordSearchDto errorRecordSearchDto = new ErrorRecordSearchDto();
		errorRecordSearchDto.setInputSearchType(null);
		errorRecordSearchDto.setSearchWord(null);

		//when
		ErrorRecordException errorLogException = Assertions.assertThrows(ErrorRecordException.class,
			() -> errorRecordService.toDiscordAllErrorInfoFind(criteria, errorRecordSearchDto));

		//then
		assertEquals(NOT_EXIST_ERROR_LOG.getMessage(), errorLogException.getMessage());
	}

	@Test
	@Order(1)
	@DisplayName("ErrorLog 저장 테스트")
	void save() {
		//given
		//when
		DefaultResponse<Map<String, Long>> responseInfo = errorRecordService.save(totalErrorRecordSaveRequestDto);

		//then
		System.out.println("응답 값 : " + responseInfo);

		assertThat(responseInfo.getStatusCode()).isEqualTo(201);
		assertThat(responseInfo.getMessage()).isEqualTo("생성 성공");
	}

	@Test
	@Order(2)
	@DisplayName("ErrorLog 저장 시 내부 서버 정보 Null 값 입력으로 인한 문제 테스트")
	void causedByServerInfoSaveFailure() {
		//given
		TotalErrorRecordSaveRequestDto isInputNulluserRequestTotalErrorRecordSaveRequestDto = createErrorStatusDto(
			"serverInfo");
		//when
		ServerInfoException errorLogException = assertThrows(ServerInfoException.class,
			() -> errorRecordService.save(isInputNulluserRequestTotalErrorRecordSaveRequestDto));

		// then
		assertEquals(PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)),
			errorLogException.getMessage());
	}

	@Test
	@Order(3)
	@DisplayName("ErrorLog 저장 시 Error Log 생성 일시 Null 값 입력으로 인한 문제 테스트")
	void causedByOccurrenceDateTimeSaveFailure() {
		//given
		TotalErrorRecordSaveRequestDto isInputNulluserRequestTotalErrorRecordSaveRequestDto = createErrorStatusDto(
			"occurrenceDateTime");
		//when
		ErrorRecordException errorLogException = assertThrows(ErrorRecordException.class,
			() -> errorRecordService.save(isInputNulluserRequestTotalErrorRecordSaveRequestDto));

		// then
		assertEquals(PARAMETER_NULL.getMessage(), errorLogException.getMessage());
	}

	@Test
	@Order(4)
	@DisplayName("ErrorLog 저장 시 이용자 정보 Null 값 입력으로 인한 문제 테스트")
	void causedByUserInfoSaveFailure() {
		//given
		TotalErrorRecordSaveRequestDto isInputNulluserRequestTotalErrorRecordSaveRequestDto = createErrorStatusDto(
			"userInfo");
		//when
		ErrorRecordException errorLogException = assertThrows(ErrorRecordException.class,
			() -> errorRecordService.save(isInputNulluserRequestTotalErrorRecordSaveRequestDto));

		// then
		assertEquals(PARAMETER_NULL.getMessage(), errorLogException.getMessage());
	}

	@Test
	@Order(5)
	@DisplayName("ErrorLog 저장 시 이용자 요청 정보 Null 값 입력으로 인한 문제 테스트")
	void causedByUserRequestInfoSaveFailure() {
		//given
		TotalErrorRecordSaveRequestDto isInputNulluserRequestTotalErrorRecordSaveRequestDto = createErrorStatusDto(
			"userRequest");
		//when
		ErrorRecordException errorLogException = assertThrows(ErrorRecordException.class,
			() -> errorRecordService.save(isInputNulluserRequestTotalErrorRecordSaveRequestDto));

		// then
		assertEquals(PARAMETER_NULL.getMessage(), errorLogException.getMessage());
	}

	@Test
	@Order(6)
	@DisplayName("ErrorLog 저장 시 Error Log 정보 Null 값 입력으로 인한 문제 테스트")
	void causedByErrorLogInfoSaveFailure() {
		//given
		TotalErrorRecordSaveRequestDto isInputNulluserRequestTotalErrorRecordSaveRequestDto = createErrorStatusDto("");
		//when
		ErrorRecordException errorLogException = assertThrows(ErrorRecordException.class,
			() -> errorRecordService.save(isInputNulluserRequestTotalErrorRecordSaveRequestDto));

		// then
		assertEquals(PARAMETER_NULL.getMessage(), errorLogException.getMessage());
	}

	@Test
	@Order(7)
	@DisplayName("ErrorLog 기본 목록 조회 테스트")
	@Transactional
	void toDiscordAllErrorInfoFind() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		ErrorRecordSearchDto errorRecordSearchDto = new ErrorRecordSearchDto();
		errorRecordSearchDto.setInputSearchType(null);
		errorRecordSearchDto.setSearchWord(null);

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, errorRecordSearchDto);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(8)
	@DisplayName("ErrorLog 기본 목록 조회 시 결과 하나 테스트")
	@Transactional
	void toDiscordAllErrorInfoFindOneThing() {
		//given
		dataBaseManagerMapper.initializeAutoIncrement("log");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user_request_info");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user");
		dataBaseManagerMapper.initializeAutoIncrement("server_info");
		dataBaseManagerMapper.initializeAutoIncrement("data_created_date_time");

		System.out.println("Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));

		ErrorRecordSearchDto errorLogSearchDto = new ErrorRecordSearchDto();
		errorLogSearchDto.setInputSearchType(null);
		errorLogSearchDto.setSearchWord(null);

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, errorLogSearchDto);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(9)
	@DisplayName("ErrorLog 기본 목록 조회 Paging 처리 테스트")
	@Transactional
	void toDiscordAllErrorInfoFindPaging() {
		//given
		dataBaseManagerMapper.initializeAutoIncrement("log");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user_request_info");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user");
		dataBaseManagerMapper.initializeAutoIncrement("server_info");
		dataBaseManagerMapper.initializeAutoIncrement("data_created_date_time");

		IntStream.rangeClosed(1, 10).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		Criteria requestPaging = new Criteria();
		requestPaging.setPage(3);
		requestPaging.setPerPageNum(5);

		ErrorRecordSearchDto errorLogSearchDto = new ErrorRecordSearchDto();
		errorLogSearchDto.setInputSearchType(null);
		errorLogSearchDto.setSearchWord(null);

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			requestPaging, errorLogSearchDto);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
		assertThat(responseInfo.getPagination().getCriteria().getPage()).isEqualTo(3);
		assertThat(responseInfo.getPagination().getCriteria().getPerPageNum()).isEqualTo(5);
		assertThat(responseInfo.getPagination().getTotalCount()).isEqualTo(11);
	}

	@Test
	@Order(10)
	@DisplayName("ErrorLog 목록 Log 생성 순서 번호 검색 조회 테스트")
	@Transactional
	void toErrorLogIdSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, searchForErrorLogId);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(11)
	@DisplayName("ErrorLog 생성일 검색 조회 테스트")
	@Transactional
	void toErrorLogCreatedDateSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, errorLogSearchCreatedErrorLogDateDto);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(12)
	@DisplayName("ErrorLog 생성일 범위 검색 조회 테스트")
	@Transactional
	void toErrorLogCreatedDateRangeSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, searchForCreatedErrorLogDateRange);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(13)
	@DisplayName("ErrorLog Log Level 검색 조회 테스트")
	@Transactional
	void toErrorLogLevelSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, searchForErrorLogLevel);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(14)
	@DisplayName("ErrorLog 내부 서버 이름 검색 조회 테스트")
	@Transactional
	void toErrorLogServerNameSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, searchForServerName);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(15)
	@DisplayName("ErrorLog 내부 서버 IP 검색 조회 테스트")
	@Transactional
	void toErrorLogServerIpSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, searchForServerIp);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(15)
	@DisplayName("ErrorLog 이용자 IP 검색 조회 테스트")
	@Transactional
	void toErrorLogUserIpSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, searchForUserIp);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(16)
	@DisplayName("ErrorLog Exception 간략 내용 검색 조회 테스트")
	@Transactional
	void toErrorRecordExceptionBriefSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));
		});

		//when
		DefaultListResponse<List<ErrorRecordListResponseDto>> responseInfo = errorRecordService.toDiscordAllErrorInfoFind(
			criteria, searchForErrorExceptionBrief);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(17)
	@DisplayName("ErrorLog 모든 정보 조회 가능 크루가 상세 조회 시 테스트")
	@Transactional
	void toDiscordDetailErrorInfoFind() {
		//given
		System.out.println(
			"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));

		ErrorRecordDetailSearchRequestDto errorRecordDetailSearchRequestDto = new ErrorRecordDetailSearchRequestDto();
		errorRecordDetailSearchRequestDto.setLogId("1");
		errorRecordDetailSearchRequestDto.setCrewGrade(TEAM_LEADER);

		//when
		DefaultResponse<ErrorRecordTotalDetailResponseDto> responseInfo = errorRecordService.toDiscordDetailErrorInfoFind(
			errorRecordDetailSearchRequestDto);

		//then
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(18)
	@DisplayName("ErrorLog 상세 조회 시 모든 정보 조회 불 가능 크루 조회 요청 시 주요 정보를 제외한 정보 조회 테스트")
	@Transactional
	void toDiscordDetailErrorInfoUnAuthorizedFind() {
		//given
		System.out.println(
			"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));

		ErrorRecordDetailSearchRequestDto errorLogDetailSearchRequestDto = new ErrorRecordDetailSearchRequestDto();
		errorLogDetailSearchRequestDto.setLogId("1");
		errorLogDetailSearchRequestDto.setCrewGrade(GENERAL_CREW);

		//when
		DefaultResponse<ErrorRecordTotalDetailResponseDto> responseInfo = errorRecordService.toDiscordDetailErrorInfoFind(
			errorLogDetailSearchRequestDto);

		//then
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(19)
	@DisplayName("ErrorLog 모든 정보 조회 가능 크루가 상세 조회 시 결과 값 없을 때, 문제 테스트")
	@Transactional
	void toDiscordDetailErrorInfoNotFind() {
		//given
		System.out.println(
			"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));

		ErrorRecordDetailSearchRequestDto errorLogDetailSearchRequestDto = new ErrorRecordDetailSearchRequestDto();
		errorLogDetailSearchRequestDto.setLogId("1");
		errorLogDetailSearchRequestDto.setCrewGrade(TEAM_LEADER);

		//when
		DefaultResponse<ErrorRecordTotalDetailResponseDto> responseInfo = errorRecordService.toDiscordDetailErrorInfoFind(
			errorLogDetailSearchRequestDto);

		//then
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(20)
	@DisplayName("ErrorLog 상세 조회 시 모든 정보 조회 가능 크루가 조회 요청 시 결과 값 없을 때, 문제 테스트")
	@Transactional
	void toDiscordDetailErrorInfoUnAuthorizedNotFind() {
		//given
		System.out.println(
			"Service Logic 저장 상태 여부 확인 : " + errorRecordService.save(totalErrorRecordSaveRequestDto));

		ErrorRecordDetailSearchRequestDto errorLogDetailSearchRequestDto = new ErrorRecordDetailSearchRequestDto();
		errorLogDetailSearchRequestDto.setLogId("99999999");
		errorLogDetailSearchRequestDto.setCrewGrade(GENERAL_CREW);

		//when
		ErrorRecordException errorLogException = assertThrows(ErrorRecordException.class,
			() -> errorRecordService.toDiscordDetailErrorInfoFind(errorLogDetailSearchRequestDto));

		//then
		assertEquals(NOT_EXIST_ERROR_LOG.getMessage(), errorLogException.getMessage());
	}

	/**
	 * <b>Error Log 저장 간 관계 테이블 정보 저장 시 문제를 발생 시킬 요인을 만들 Method</b>
	 * @param errorCause 각 Test Case에서 상황별로 동작할 수 있게 이용할 Keyword
	 * @return 이용자 정보 저장을 위한 문제 있는 요청 DTO
	 */
	private TotalErrorRecordSaveRequestDto createErrorStatusDto(String errorCause) {
		String[] initializedCreateDateTime = initializedCreateDateTime();
		TotalErrorRecordSaveRequestDto inputNullTotalErrorRecordSaveRequestDto = new TotalErrorRecordSaveRequestDto();
		switch (errorCause) {
			case "serverInfo":
				inputNullTotalErrorRecordSaveRequestDto.setCreatedAt(
					initializedCreateDateTime[0] + " " + initializedCreateDateTime[1]);
				inputNullTotalErrorRecordSaveRequestDto.setLevel("ERROR");
				inputNullTotalErrorRecordSaveRequestDto.setServerName(null);
				inputNullTotalErrorRecordSaveRequestDto.setVmInfo(null);
				inputNullTotalErrorRecordSaveRequestDto.setOsInfo(null);
				inputNullTotalErrorRecordSaveRequestDto.setServerIP(null);
				inputNullTotalErrorRecordSaveRequestDto.setServerEnvironment(null);
				inputNullTotalErrorRecordSaveRequestDto.setUserIp(userInfoVo.getUserIP());
				inputNullTotalErrorRecordSaveRequestDto.setUserLocation(userInfoVo.getUserLocation());
				inputNullTotalErrorRecordSaveRequestDto.setUserEnvironment(userInfoVo.getUserEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setRequestHeader(userRequestInfoVo.getRequestHeader());
				inputNullTotalErrorRecordSaveRequestDto.setUserCookies(null);
				inputNullTotalErrorRecordSaveRequestDto.setRequestParameter(userRequestInfoVo.getRequestParameter());
				inputNullTotalErrorRecordSaveRequestDto.setRequestBody(userRequestInfoVo.getRequestBody());
				inputNullTotalErrorRecordSaveRequestDto.setExceptionBrief(
					"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
				inputNullTotalErrorRecordSaveRequestDto.setExceptionDetail(
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

				break;

			case "userInfo":
				inputNullTotalErrorRecordSaveRequestDto.setCreatedAt(
					initializedCreateDateTime[0] + " " + initializedCreateDateTime[1]);
				inputNullTotalErrorRecordSaveRequestDto.setLevel("ERROR");
				inputNullTotalErrorRecordSaveRequestDto.setServerName(serverInfo.getServerName());
				inputNullTotalErrorRecordSaveRequestDto.setVmInfo(serverInfo.getVmInfo());
				inputNullTotalErrorRecordSaveRequestDto.setOsInfo(serverInfo.getOsInfo());
				inputNullTotalErrorRecordSaveRequestDto.setServerIP(serverInfo.getServerIP());
				inputNullTotalErrorRecordSaveRequestDto.setServerEnvironment(serverInfo.getServerEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setUserIp(null);
				inputNullTotalErrorRecordSaveRequestDto.setUserLocation(null);
				inputNullTotalErrorRecordSaveRequestDto.setUserEnvironment(null);
				inputNullTotalErrorRecordSaveRequestDto.setRequestHeader(userRequestInfoVo.getRequestHeader());
				inputNullTotalErrorRecordSaveRequestDto.setUserCookies(null);
				inputNullTotalErrorRecordSaveRequestDto.setRequestParameter(userRequestInfoVo.getRequestParameter());
				inputNullTotalErrorRecordSaveRequestDto.setRequestBody(userRequestInfoVo.getRequestBody());
				inputNullTotalErrorRecordSaveRequestDto.setExceptionBrief(
					"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
				inputNullTotalErrorRecordSaveRequestDto.setExceptionDetail(
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

				break;

			case "userRequest":
				inputNullTotalErrorRecordSaveRequestDto.setCreatedAt(
					initializedCreateDateTime[0] + " " + initializedCreateDateTime[1]);
				inputNullTotalErrorRecordSaveRequestDto.setLevel("ERROR");
				inputNullTotalErrorRecordSaveRequestDto.setServerName(serverInfo.getServerName());
				inputNullTotalErrorRecordSaveRequestDto.setVmInfo(serverInfo.getVmInfo());
				inputNullTotalErrorRecordSaveRequestDto.setOsInfo(serverInfo.getOsInfo());
				inputNullTotalErrorRecordSaveRequestDto.setServerIP(serverInfo.getServerIP());
				inputNullTotalErrorRecordSaveRequestDto.setServerEnvironment(serverInfo.getServerEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setUserIp(userInfoVo.getUserIP());
				inputNullTotalErrorRecordSaveRequestDto.setUserLocation(userInfoVo.getUserLocation());
				inputNullTotalErrorRecordSaveRequestDto.setUserEnvironment(userInfoVo.getUserEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setRequestHeader(null);
				inputNullTotalErrorRecordSaveRequestDto.setUserCookies(null);
				inputNullTotalErrorRecordSaveRequestDto.setRequestParameter(null);
				inputNullTotalErrorRecordSaveRequestDto.setRequestBody(null);
				inputNullTotalErrorRecordSaveRequestDto.setExceptionBrief(
					"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
				inputNullTotalErrorRecordSaveRequestDto.setExceptionDetail(
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
				break;

			case "occurrenceDateTime":
				inputNullTotalErrorRecordSaveRequestDto.setCreatedAt(null);
				inputNullTotalErrorRecordSaveRequestDto.setLevel("ERROR");
				inputNullTotalErrorRecordSaveRequestDto.setServerName(serverInfo.getServerName());
				inputNullTotalErrorRecordSaveRequestDto.setVmInfo(serverInfo.getVmInfo());
				inputNullTotalErrorRecordSaveRequestDto.setOsInfo(serverInfo.getOsInfo());
				inputNullTotalErrorRecordSaveRequestDto.setServerIP(serverInfo.getServerIP());
				inputNullTotalErrorRecordSaveRequestDto.setServerEnvironment(serverInfo.getServerEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setUserIp(userInfoVo.getUserIP());
				inputNullTotalErrorRecordSaveRequestDto.setUserLocation(userInfoVo.getUserLocation());
				inputNullTotalErrorRecordSaveRequestDto.setUserEnvironment(userInfoVo.getUserEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setRequestHeader(userRequestInfoVo.getRequestHeader());
				inputNullTotalErrorRecordSaveRequestDto.setUserCookies(null);
				inputNullTotalErrorRecordSaveRequestDto.setRequestParameter(userRequestInfoVo.getRequestParameter());
				inputNullTotalErrorRecordSaveRequestDto.setRequestBody(userRequestInfoVo.getRequestBody());
				inputNullTotalErrorRecordSaveRequestDto.setExceptionBrief(
					"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
				inputNullTotalErrorRecordSaveRequestDto.setExceptionDetail(
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
				break;

			default:
				inputNullTotalErrorRecordSaveRequestDto.setCreatedAt(
					initializedCreateDateTime[0] + " " + initializedCreateDateTime[1]);
				inputNullTotalErrorRecordSaveRequestDto.setLevel("ERROR");
				inputNullTotalErrorRecordSaveRequestDto.setServerName(serverInfo.getServerName());
				inputNullTotalErrorRecordSaveRequestDto.setVmInfo(serverInfo.getVmInfo());
				inputNullTotalErrorRecordSaveRequestDto.setOsInfo(serverInfo.getOsInfo());
				inputNullTotalErrorRecordSaveRequestDto.setServerIP(serverInfo.getServerIP());
				inputNullTotalErrorRecordSaveRequestDto.setServerEnvironment(serverInfo.getServerEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setUserIp(userInfoVo.getUserIP());
				inputNullTotalErrorRecordSaveRequestDto.setUserLocation(userInfoVo.getUserLocation());
				inputNullTotalErrorRecordSaveRequestDto.setUserEnvironment(userInfoVo.getUserEnvironment());
				inputNullTotalErrorRecordSaveRequestDto.setRequestHeader(userRequestInfoVo.getRequestHeader());
				inputNullTotalErrorRecordSaveRequestDto.setUserCookies(null);
				inputNullTotalErrorRecordSaveRequestDto.setRequestParameter(userRequestInfoVo.getRequestParameter());
				inputNullTotalErrorRecordSaveRequestDto.setRequestBody(userRequestInfoVo.getRequestBody());
				inputNullTotalErrorRecordSaveRequestDto.setExceptionBrief(null);
				inputNullTotalErrorRecordSaveRequestDto.setExceptionDetail(null);
		}
		return inputNullTotalErrorRecordSaveRequestDto;
	}

	/**
	 * <b>가짜 ErrorLog 정보를 만들기 위한 Method</b>
	 */
	private void initializedMocErrorLogInfoList() {
		this.errorLogList = new ArrayList<>();

		IntStream.rangeClosed(1, 11).forEach(count -> {
			errorLogList.add(ErrorRecordListResponseDto.builder()
				.logId((long)count)
				.createdDateTime(this.totalErrorRecordSaveRequestDto.getCreatedAt())
				.level(this.totalErrorRecordSaveRequestDto.getLevel())
				.serverName(this.totalErrorRecordSaveRequestDto.getServerName())
				.serverIP("192.168.20." + count)
				.exceptionBrief(this.totalErrorRecordSaveRequestDto.getExceptionBrief())
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
		this.searchForUserIp = new ErrorRecordSearchDto();
		this.searchForUserIp.setInputSearchType(ErrorRecordSearchType.USER_IP);
		this.searchForUserIp.setSearchWord("127.0.0.1");
	}

	/**
	 * <b>내부 서버 IP 주소를 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchServerIpRequestDto() {
		this.searchForServerIp = new ErrorRecordSearchDto();
		this.searchForServerIp.setInputSearchType(SERVER_IP);
		this.searchForServerIp.setSearchWord("192.168.20.2");
	}

	/**
	 * <b>내부 서버 이름을 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchServerNameRequestDto() {
		this.searchForServerName = new ErrorRecordSearchDto();
		this.searchForServerName.setInputSearchType(ErrorRecordSearchType.SERVER_NAME);
		this.searchForServerName.setSearchWord("통합관리서버");
	}

	/**
	 * <b>Error Log 생성일 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchCreatedErrorLogDateRequestDto(String initializedCreatedDate) {
		this.errorLogSearchCreatedErrorLogDateDto = new ErrorRecordSearchDto();
		this.errorLogSearchCreatedErrorLogDateDto.setInputSearchType(LOG_CREATE_DATE);
		this.errorLogSearchCreatedErrorLogDateDto.setDate(initializedCreatedDate);
	}

	/**
	 * <b>Error Log 생성일 범위 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */

	private void initializedMockSearchUserConnectedDateRangeRequestDto(String nowDate) {
		this.searchForCreatedErrorLogDateRange = new ErrorRecordSearchDto();
		this.searchForCreatedErrorLogDateRange.setInputSearchType(LOG_CREATE_DATE);
		this.searchForCreatedErrorLogDateRange.setStartDate("2023-01-01");
		this.searchForCreatedErrorLogDateRange.setEndDate(nowDate);
	}

	/**
	 * <b>Error Log 생성 순서 번호 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchErrorLogIdRequestDto() {
		this.searchForErrorLogId = new ErrorRecordSearchDto();
		this.searchForErrorLogId.setInputSearchType(LOG_ID);
		this.searchForErrorLogId.setSearchWord("1");
	}

	/**
	 * <b>Error Log 생성 순서 번호 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchErrorLogLevelRequestDto() {
		this.searchForErrorLogLevel = new ErrorRecordSearchDto();
		this.searchForErrorLogLevel.setInputSearchType(LOG_LEVEL);
		this.searchForErrorLogLevel.setSearchWord("ERROR");
	}

	/**
	 * <b>Error Log Exception 간략 내용으로 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchErrorRecordExceptionBriefRequestDto() {
		this.searchForErrorExceptionBrief = new ErrorRecordSearchDto();
		this.searchForErrorExceptionBrief.setInputSearchType(EXCEPTION_BRIEF);
		this.searchForErrorExceptionBrief.setSearchWord(
			"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
	}

	/**
	 * <b>가짜 Error Log 발생 DTO를 만든기 위한 Method</b>
	 * @return Error Log 발생 정보를 담은 요청 DTO
	 */
	private TotalErrorRecordSaveRequestDto initializedMockModels() {
		this.dataCreatedDateTimeVo = initializedCreateDateTimeVo();
		this.serverInfoVo = initializedServerInfo();
		this.userInfoVo = initializedConnectedUserInfo();
		this.userRequestInfoVo = initializedConnectedUserRequestInfo();
		this.userRequestTotalInfoSaveRequestDto = initializedConnectedUserRequestTotalInfo();

		TotalErrorRecordSaveRequestDto totalErrorRecordSaveRequestDto = new TotalErrorRecordSaveRequestDto();
		totalErrorRecordSaveRequestDto.setCreatedAt(
			dataCreatedDateTimeVo.getDataCreatedDate() + " " + dataCreatedDateTimeVo.getDataCreatedTime());
		totalErrorRecordSaveRequestDto.setLevel("ERROR");
		totalErrorRecordSaveRequestDto.setServerName(serverInfo.getServerName());
		totalErrorRecordSaveRequestDto.setVmInfo(serverInfo.getVmInfo());
		totalErrorRecordSaveRequestDto.setOsInfo(serverInfo.getOsInfo());
		totalErrorRecordSaveRequestDto.setServerIP(serverInfo.getServerIP());
		totalErrorRecordSaveRequestDto.setServerEnvironment(serverInfo.getServerEnvironment());
		totalErrorRecordSaveRequestDto.setUserIp(userInfoVo.getUserIP());
		totalErrorRecordSaveRequestDto.setUserLocation(userInfoVo.getUserLocation());
		totalErrorRecordSaveRequestDto.setUserEnvironment(userInfoVo.getUserEnvironment());
		totalErrorRecordSaveRequestDto.setRequestHeader(userRequestInfoVo.getRequestHeader());
		totalErrorRecordSaveRequestDto.setUserCookies(null);
		totalErrorRecordSaveRequestDto.setRequestParameter(userRequestInfoVo.getRequestParameter());
		totalErrorRecordSaveRequestDto.setRequestBody(userRequestInfoVo.getRequestBody());
		totalErrorRecordSaveRequestDto.setExceptionBrief(
			"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
		totalErrorRecordSaveRequestDto.setExceptionDetail(
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

		return totalErrorRecordSaveRequestDto;
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