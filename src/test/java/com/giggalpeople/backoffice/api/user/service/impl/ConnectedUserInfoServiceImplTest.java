package com.giggalpeople.backoffice.api.user.service.impl;

import static com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType.CONNECTED_USER_REQUEST_ID;
import static com.giggalpeople.backoffice.common.enumtype.CrewGrade.GENERAL_CREW;
import static com.giggalpeople.backoffice.common.enumtype.CrewGrade.TEAM_LEADER;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_EXIST_CONNECTED_USER;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NO_AUTHORIZATION;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.common.database.dao.OccurrenceDataDateTimeManagementDao;
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;
import com.giggalpeople.backoffice.api.server.database.dao.ServerInfoDao;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.api.user.database.dao.UserInfoDao;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoDetailResponseDto;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoListResponseDto;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;
import com.giggalpeople.backoffice.api.user.service.ConnectedUserInfoService;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.database.DataBaseManagerMapper;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.enumtype.GiggalPeopleServerNames;
import com.giggalpeople.backoffice.common.env.exception.ServerInfoException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/data.sql")
class ConnectedUserInfoServiceImplTest {

	@Autowired
	ConnectedUserInfoService connectedUserInfoService;

	@Autowired
	UserInfoDao userInfoDAO;

	@Autowired
	ServerInfoDao serverInfoDAO;

	@Autowired
	OccurrenceDataDateTimeManagementDao occurrenceDataDateTimeManagementDAO;

	@Autowired
	DataBaseManagerMapper dataBaseManagerMapper;

	DataCreatedDateTimeVo dataCreatedDateTimeVo;

	ServerInfo serverInfo;
	ServerInfoVo serverInfoVo;

	ConnectedUserInfoVo userInfoVO;

	UserInfoSearchDto searchForUserId;

	UserInfoSearchDto searchForUserConnectedDateRange;

	UserInfoSearchDto searchForUserConnectedDate;

	UserInfoSearchDto searchForServerName;

	UserInfoSearchDto searchForServerIp;

	UserInfoSearchDto searchForUserIp;

	DataCreatedDateTimeRequestDto dataCreatedDateTimeRequestDto;

	ServerInfoSaveRequestDto serverInfoSaveRequestDto;

	ConnectedUserInfoSaveRequestDto connectedUserInfoSaveRequestDto;

	ConnectedUserRequestInfoSaveRequestDto connectedUserRequestInfoSaveRequestDto;

	UserRequestInfoVo userRequestInfoVo;

	UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestDto;

	UserInfoDetailSearchRequestDto userInfoDetailSearchRequestDto;

	Criteria criteria;

	List<UserInfoListResponseDto> userInfoList;

	@BeforeEach
	void beforeTestSetup() {
		this.userRequestTotalInfoSaveRequestDto = initializedMockModels();

		initializedMockSearchRequestDto();
	}

	/**
	 * <b>가짜 검색 요청 DTO를 만들기 위한 Method</b>
	 */
	private void initializedMockSearchRequestDto() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		initializedMockSearchUserIdRequestDto();
		initializedMockSearchUserConnectedDateRangeRequestDto(splitNowDateTime[0]);
		initializedMockSearchUserConnectedDateRequestDto(splitNowDateTime[0]);
		initializedMockSearchServerNameRequestDto();
		initializedMockSearchServerIpRequestDto();
		initializedMockSearchUserIpRequestDto();
		initializedMockDetailRequestDto();
		initializedMockCriteria();
		initializedMockUserInfo(splitNowDateTime);
	}

	@Test
	@Order(0)
	@DisplayName("요청 이용자 정보 Data Base 저장 테스트")
	void save() {
		//given
		DefaultResponse<Map<String, Long>> responseInfo = connectedUserInfoService.save(
			userRequestTotalInfoSaveRequestDto);

		//when
		System.out.println("응답 값 : " + responseInfo);

		//then
		assertThat(responseInfo.getStatusCode()).isEqualTo(201);
		assertThat(responseInfo.getMessage()).isEqualTo("생성 성공");
	}

	@Test
	@Order(1)
	@DisplayName("요청 이용자 정보 Data Base 저장 시 내부 서버 정보 Null 값 입력으로 인한 문제 테스트")
	void causedByServerInfoSaveFailure() {
		//given
		UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestErrorStatusDto = createErrorStatusDto(
			"serverInfo");
		//when
		ServerInfoException serverInfoException = assertThrows(ServerInfoException.class, () -> {
			connectedUserInfoService.save(
				userRequestTotalInfoSaveRequestErrorStatusDto);
		});

		// then
		assertEquals(PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)),
			serverInfoException.getMessage());
	}

	@Test
	@Order(2)
	@DisplayName("요청 이용자 정보 Data Base 저장 시 Data 발생 일시 Null 값 입력으로 인한 문제 테스트")
	void causedByOccurrenceDateTimeSaveFailure() {
		//given
		UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestErrorStatusDto = createErrorStatusDto(
			"occurrenceDateTime");
		//when
		ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
			connectedUserInfoService.save(
				userRequestTotalInfoSaveRequestErrorStatusDto);
		});

		// then
		assertEquals(PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)),
			connectedUserException.getMessage());
	}

	@Test
	@Order(3)
	@DisplayName("요청 이용자 정보 Data Base 저장 시 요청 이용자 정보 Null로 인한 문제 테스트")
	void causedByUserInfoSaveFailure() {
		//given
		UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestErrorStatusDto = createErrorStatusDto(
			"userInfo");
		//when
		ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
			connectedUserInfoService.save(
				userRequestTotalInfoSaveRequestErrorStatusDto);
		});

		// then
		assertEquals(PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)),
			connectedUserException.getMessage());
	}

	@Test
	@Order(4)
	@DisplayName("요청 이용자 정보 Data Base 저장 시 이용자 요청 정보 Null로 인한 문제 테스트")
	void causedByUserRequestInfoSaveFailure() {
		//given
		UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestErrorStatusDto = createErrorStatusDto(
			"userInfo");
		//when
		ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
			connectedUserInfoService.save(
				userRequestTotalInfoSaveRequestErrorStatusDto);
		});

		// then
		assertEquals(PARAMETER_NULL.getMessage(String.valueOf(NullPointerException.class)),
			connectedUserException.getMessage());
	}

	@Test
	@Order(5)
	@DisplayName("요청 이용자 정보 Data Base 저장 시 저장 DTO 값 Null 일 경우 문제 테스트")
	void saveNotNull() {
		//given
		//when
		ConnectedUserException connectedUserException = Assertions.assertThrows(ConnectedUserException.class,
			() -> connectedUserInfoService.save(null));

		//then
		assertEquals(PARAMETER_NULL.getMessage(), connectedUserException.getMessage());
	}

	@Test
	@Order(6)
	@DisplayName("Application 요청 이용자 및 요청 정보 기본 목록 조회 테스트")
	@Transactional
	void toDiscordAllUserInfoFind() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));
		});

		UserInfoSearchDto userInfoSearchDto = new UserInfoSearchDto();
		userInfoSearchDto.setInputSearchType(null);
		userInfoSearchDto.setSearchWord(null);

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, userInfoSearchDto);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(7)
	@DisplayName("Application 요청 이용자 정보 및 요청 정보 기본 목록 조회 시 결과 하나 테스트")
	@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/schema.sql")
	void toDiscordAllUserInfoFindOneThing() {
		//given
		dataBaseManagerMapper.initializeAutoIncrement("log");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user_request_info");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user");
		dataBaseManagerMapper.initializeAutoIncrement("server_info");
		dataBaseManagerMapper.initializeAutoIncrement("data_created_date_time");

		System.out.println(
			"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));

		UserInfoSearchDto userInfoSearchDto = new UserInfoSearchDto();
		userInfoSearchDto.setInputSearchType(null);
		userInfoSearchDto.setSearchWord(null);

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, userInfoSearchDto);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(8)
	@DisplayName("Application 요청 이용자 정보 및 요청 정보 Paging 처리 목록 조회 테스트")
	@Transactional
	void toDiscordAllUserInfoFindPaging() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));
		});

		Criteria requestPaging = new Criteria();
		requestPaging.setPage(3);
		requestPaging.setPerPageNum(5);

		UserInfoSearchDto userInfoSearchDto = new UserInfoSearchDto();
		userInfoSearchDto.setInputSearchType(null);
		userInfoSearchDto.setSearchWord(null);

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			requestPaging, userInfoSearchDto);

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
	@Order(9)
	@DisplayName("요청 이용자 정보 및 요청 정보 목록 조회 시 검색 결과 없을 때, 문제 사항 테스트")
	@Transactional
	@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/schema.sql")
	void totalSearchNotFound() {
		//given
		dataBaseManagerMapper.initializeAutoIncrement("log");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user_request_info");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user");
		dataBaseManagerMapper.initializeAutoIncrement("server_info");
		dataBaseManagerMapper.initializeAutoIncrement("data_created_date_time");

		UserInfoSearchDto userInfoSearchDto = new UserInfoSearchDto();
		userInfoSearchDto.setInputSearchType(null);
		userInfoSearchDto.setSearchWord(null);

		//when
		ConnectedUserException connectedUserException = Assertions.assertThrows(ConnectedUserException.class,
			() -> connectedUserInfoService.toDiscordAllUserInfoFind(criteria, userInfoSearchDto));

		//then
		assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
	}

	@Test
	@Order(10)
	@DisplayName("요청 이용자 정보 및 요청 정보 목록 조회 시 이용자 순서 번호를 통해 검색 테스트")
	void totalUserInfoIdSearch() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));
		});

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, searchForUserId);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(11)
	@DisplayName("요청 이용자 정보 및 요청 정보 목록 조회 시 접속일 범위를 통해 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoConnectedDateRangeSearchCount() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));
		});

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, searchForUserConnectedDateRange);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(12)
	@DisplayName("요청 이용자 정보 및 요청 정보 목록 조회 시 접속일 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoConnectedDateSearchCount() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));
		});

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, searchForUserConnectedDate);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(13)
	@DisplayName("요청 이용자 정보 및 요청 정보 목록 조회 시 내부 서버 이름 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoServerNameSearchCount() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));
		});

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, searchForServerName);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(14)
	@DisplayName("요청 이용자 정보 및 요청 정보 목록 조회 시 내부 서버 IP 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoServerIpSearchCount() {
		//given
		IntStream.rangeClosed(1, 11).forEach(count -> {
			System.out.println(
				"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));
		});

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, searchForServerIp);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(9)
	@DisplayName("요청 이용자 정보 및 요청 정보 목록 조회 시 이용자 IP 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoUserIpSearchCount() {
		//given
		System.out.println(
			"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(
				userRequestTotalInfoSaveRequestDto));

		//when
		DefaultListResponse<List<UserInfoListResponseDto>> responseInfo = connectedUserInfoService.toDiscordAllUserInfoFind(
			criteria, this.searchForUserIp);

		//then
		System.out.println("Paging 처리 결과 : " + responseInfo.getPagination());
		System.out.println("Data Base에서 찾은 Data 내역 : " + responseInfo.getData());
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(15)
	@DisplayName("요청 이용자 정보 및 요청 정보 검색 목록 조회 시 검색 결과 없을 때, 문제 사항 테스트")
	@Transactional
	@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/schema.sql")
	void totalUserInfoSearchNotFound() {
		//given
		dataBaseManagerMapper.initializeAutoIncrement("log");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user_request_info");
		dataBaseManagerMapper.initializeAutoIncrement("connected_user");
		dataBaseManagerMapper.initializeAutoIncrement("server_info");
		dataBaseManagerMapper.initializeAutoIncrement("data_created_date_time");

		//when
		ConnectedUserException connectedUserException = Assertions.assertThrows(ConnectedUserException.class,
			() -> connectedUserInfoService.toDiscordAllUserInfoFind(criteria, searchForUserIp));

		//then
		assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
	}

	@Test
	@Order(16)
	@DisplayName("요청 이용자 정보 및 요청 정보 상세 조회 테스트")
	void toDiscordDetailConnectedUserInfoFind() {
		//given
		System.out.println(
			"Service Logic 저장 상태 여부 확인 : " + connectedUserInfoService.save(userRequestTotalInfoSaveRequestDto));

		UserInfoDetailSearchRequestDto userInfoDetailSearchRequestDto = new UserInfoDetailSearchRequestDto();
		userInfoDetailSearchRequestDto.setConnectedUserRequestInfoID("1");
		userInfoDetailSearchRequestDto.setCrewGrade(TEAM_LEADER);

		//when
		DefaultResponse<UserInfoDetailResponseDto> responseInfo = connectedUserInfoService.toDiscordDetailConnectedUserInfoFind(
			userInfoDetailSearchRequestDto);

		//then
		assertThat(responseInfo.getStatusCode()).isEqualTo(200);
		assertThat(responseInfo.getMessage()).isEqualTo("성공");
	}

	@Test
	@Order(17)
	@DisplayName("요청 이용자 정보 및 요청 정보 상세 조회 시 권한 없는 크루가 조회 요청 시 문제 테스트")
	void toDiscordDetailConnectedUserInfoUnAuthorizedFind() {
		//given
		UserInfoDetailSearchRequestDto userInfoDetailSearchRequestDto = new UserInfoDetailSearchRequestDto();
		userInfoDetailSearchRequestDto.setConnectedUserRequestInfoID("1");
		userInfoDetailSearchRequestDto.setCrewGrade(GENERAL_CREW);

		//when
		ConnectedUserException connectedUserException = Assertions.assertThrows(ConnectedUserException.class,
			() -> connectedUserInfoService.toDiscordDetailConnectedUserInfoFind(userInfoDetailSearchRequestDto));

		//then
		assertEquals(NO_AUTHORIZATION.getMessage(), connectedUserException.getMessage());
	}

	@Test
	@Order(18)
	@DisplayName("요청 이용자 정보 및 요청 정보 상세 조회 시 결과 값 없을 때, 문제 테스트")
	void toDiscordDetailConnectedUserInfoNotFind() {
		//given
		UserInfoDetailSearchRequestDto userInfoDetailSearchRequestDto = new UserInfoDetailSearchRequestDto();
		userInfoDetailSearchRequestDto.setConnectedUserRequestInfoID("99999999");
		userInfoDetailSearchRequestDto.setCrewGrade(TEAM_LEADER);

		//when
		ConnectedUserException connectedUserException = Assertions.assertThrows(ConnectedUserException.class,
			() -> connectedUserInfoService.toDiscordDetailConnectedUserInfoFind(userInfoDetailSearchRequestDto));

		//then
		assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
	}

	/**
	 * <b>이용자 정보 저장 간 관계 테이블 정보 저장 시 문제를 발생 시킬 요인을 만들 Method</b>
	 * @param errorCause 각 Test Case에서 상황별로 동작할 수 있게 이용할 Keyword
	 * @return 이용자 정보 저장을 위한 문제 있는 요청 DTO
	 */
	private UserRequestTotalInfoSaveRequestDto createErrorStatusDto(String errorCause) {
		switch (errorCause) {
			case "serverInfo":
				return UserRequestTotalInfoSaveRequestDto.builder()
					.serverInfo(null)
					.dataCreatedDateTimeRequestDTO(dataCreatedDateTimeRequestDto)
					.connectedUserInfoSaveRequestDTO(connectedUserInfoSaveRequestDto)
					.connectedUserRequestInfoSaveRequestDTO(connectedUserRequestInfoSaveRequestDto)
					.build();
			case "occurrenceDateTime":
				return UserRequestTotalInfoSaveRequestDto.builder()
					.serverInfo(serverInfo)
					.dataCreatedDateTimeRequestDTO(null)
					.connectedUserInfoSaveRequestDTO(connectedUserInfoSaveRequestDto)
					.connectedUserRequestInfoSaveRequestDTO(connectedUserRequestInfoSaveRequestDto)
					.build();
			case "userInfo":
				return UserRequestTotalInfoSaveRequestDto.builder()
					.serverInfo(serverInfo)
					.dataCreatedDateTimeRequestDTO(dataCreatedDateTimeRequestDto)
					.connectedUserInfoSaveRequestDTO(null)
					.connectedUserRequestInfoSaveRequestDTO(connectedUserRequestInfoSaveRequestDto)
					.build();
			default:
				return UserRequestTotalInfoSaveRequestDto.builder()
					.serverInfo(serverInfo)
					.dataCreatedDateTimeRequestDTO(dataCreatedDateTimeRequestDto)
					.connectedUserInfoSaveRequestDTO(connectedUserInfoSaveRequestDto)
					.connectedUserRequestInfoSaveRequestDTO(null)
					.build();
		}
	}

	/**
	 * <b>Test에 사용될 DTO, VO를 만들기 위한 Method</b>
	 * @return 이용자 요청 정보와 서버 정보 등을 포함한 요청 DTO 객체
	 */
	private UserRequestTotalInfoSaveRequestDto initializedMockModels() {
		this.dataCreatedDateTimeVo = initializedCreateDateTime();
		this.serverInfoVo = initializedServerInfo();
		this.userInfoVO = initializedConnectedUserInfo();
		this.userRequestInfoVo = initializedConnectedUserRequestInfo();

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
	private ConnectedUserInfoVo initializedConnectedUserInfo() {
		this.connectedUserInfoSaveRequestDto = ConnectedUserInfoSaveRequestDto.builder()
			.internalServerID(1L)
			.dataCreatedDateTimeID(1L)
			.userIP("127.0.0.1")
			.userLocation(
				"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n"
					+ "\"ip\" : \"127.0.0.1\",\n"
					+ "\"error\" : \"true\",\n" + "\"version\" : \"IPv4\"\n")
			.userEnvironment("\"Mozilla/5.0\"")
			.build();

		return ConnectedUserInfoVo.toVO(connectedUserInfoSaveRequestDto);
	}

	/**
	 * <b>이용자 접속 및 요청일시를 만들기 위한 Method</b>
	 * @return 접속 및 요청 일시를 담은 Value Object
	 */
	private DataCreatedDateTimeVo initializedCreateDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		this.dataCreatedDateTimeRequestDto = DataCreatedDateTimeRequestDto.builder()
			.createdDate(splitNowDateTime[0])
			.createdTime(splitNowDateTime[1])
			.build();

		return DataCreatedDateTimeVo.toVO(dataCreatedDateTimeRequestDto);
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
	 * <b>상세 조회 요청 DTO 만들기 위한 Method</b>
	 */
	private void initializedMockDetailRequestDto() {
		this.userInfoDetailSearchRequestDto = new UserInfoDetailSearchRequestDto();
		this.userInfoDetailSearchRequestDto.setConnectedUserRequestInfoID("1");
		this.userInfoDetailSearchRequestDto.setCrewGrade(TEAM_LEADER);
	}

	/**
	 * <b>가짜 이용자 정보를 만들기 위한 Method</b>
	 * @param splitNowDateTime 접속 일시를 공백 기준으로 나누어 담긴 배열
	 */
	private void initializedMockUserInfo(String[] splitNowDateTime) {
		this.userInfoList = new ArrayList<>();
		IntStream.rangeClosed(1, 11).forEach(count -> {
			this.userInfoList.add(UserInfoListResponseDto.builder()
				.connectedUserRequestInfoID((long)count)
				.connectedDateTime(splitNowDateTime[0] + " " + splitNowDateTime[1])
				.serverName(GiggalPeopleServerNames.GIGGAL_TOTAL_BACK_OFFICE.getDescription())
				.userIP("127.0.0." + count)
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
		this.searchForUserIp = new UserInfoSearchDto();
		this.searchForUserIp.setInputSearchType(UserInfoSearchType.USER_IP);
		this.searchForUserIp.setSearchWord("127.0.0.1");
	}

	/**
	 * <b>내부 서버 IP 주소를 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchServerIpRequestDto() {
		this.searchForServerIp = new UserInfoSearchDto();
		this.searchForServerIp.setInputSearchType(UserInfoSearchType.SERVER_IP);
		this.searchForServerIp.setSearchWord("192.168.20.254");
	}

	/**
	 * <b>내부 서버 이름을 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchServerNameRequestDto() {
		this.searchForServerName = new UserInfoSearchDto();
		this.searchForServerName.setInputSearchType(UserInfoSearchType.SERVER_NAME);
		this.searchForServerName.setSearchWord("통합관리서버");
	}

	/**
	 * <b>이용자 접속일 통해 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchUserConnectedDateRequestDto(String searchDate) {
		this.searchForUserConnectedDate = new UserInfoSearchDto();
		this.searchForUserConnectedDate.setInputSearchType(UserInfoSearchType.USER_CONNECTED_DATE);
		this.searchForUserConnectedDate.setDate(searchDate);
	}

	/**
	 * <b>이용자 접속일 범위 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchUserConnectedDateRangeRequestDto(String nowDate) {
		this.searchForUserConnectedDateRange = new UserInfoSearchDto();
		this.searchForUserConnectedDateRange.setInputSearchType(UserInfoSearchType.USER_CONNECTED_DATE);
		this.searchForUserConnectedDateRange.setStartDate("2023-01-01");
		this.searchForUserConnectedDateRange.setEndDate(nowDate);
	}

	/**
	 * <b>이용자 접속 순서 번호 검색하고자 할 때, 사용하기 위한 요청 객체 DTO</b>
	 */
	private void initializedMockSearchUserIdRequestDto() {
		this.searchForUserId = new UserInfoSearchDto();
		this.searchForUserId.setInputSearchType(CONNECTED_USER_REQUEST_ID);
		this.searchForUserId.setSearchWord("1");
	}
}