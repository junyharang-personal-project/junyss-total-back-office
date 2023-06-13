package com.giggalpeople.backoffice.api.user.controller;

import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_CALLER_DISCORD_BOT;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_PREFIX_URN;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_VERSION;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.CONNECTED_USER;
import static com.giggalpeople.backoffice.common.enumtype.CrewGrade.GENERAL_CREW;
import static com.giggalpeople.backoffice.common.enumtype.CrewGrade.TEAM_LEADER;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NOT_EXIST_CONNECTED_USER;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.NO_AUTHORIZATION;
import static com.giggalpeople.backoffice.common.enumtype.SuccessCode.SUCCESS;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.Pagination;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoDetailSearchRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoDetailResponseDto;
import com.giggalpeople.backoffice.api.user.model.dto.response.UserInfoListResponseDto;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVo;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.service.ConnectedUserInfoService;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.enumtype.CrewGrade;
import com.giggalpeople.backoffice.common.enumtype.GiggalPeopleServerNames;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// JUnit 5 사용 시 사용, MyBatisTest 2.0.1 Version 이상에서 생략 가능
@ExtendWith(SpringExtension.class)
@WebMvcTest(ConnectedUserInfoController.class)
class ConnectedUserInfoControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ConnectedUserInfoService connectedUserInfoService;

	DataCreatedDateTimeVo dataCreatedDateTimeVO;

	ServerInfo serverInfo;
	ServerInfoVo serverInfoVO;

	ErrorLogUserInfoVo userInfoVO;

	Long dataCreatedDateTimeSaveId;
	Long serverInfoSaveId;

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

	UserRequestTotalInfoSaveRequestDto userRequestTotalInfoSaveRequestDto;

	UserInfoDetailSearchRequestDto userInfoDetailSearchRequestDto;

	Criteria criteria;

	List<UserInfoListResponseDto> userInfoList;

	@BeforeEach
	void beforeTestSetup() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		dataCreatedDateTimeRequestDto = DataCreatedDateTimeRequestDto.builder()
			.createdDate(splitNowDateTime[0])
			.createdTime(splitNowDateTime[1])
			.build();

		dataCreatedDateTimeVO = DataCreatedDateTimeVo.toVO(dataCreatedDateTimeRequestDto);

		serverInfo = ServerInfo.builder()
			.vmInfo("OpenJDK 64-Bit")
			.osInfo("Mac OS")
			.serverIP("192.168.20.254")
			.serverEnvironment("dev")
			.build();

		serverInfoSaveRequestDto = ServerInfoSaveRequestDto.builder()
			.serverName(serverInfo.getServerName())
			.serverVmInfo(serverInfo.getVmInfo())
			.serverOsInfo(serverInfo.getOsInfo())
			.serverIP(serverInfo.getServerIP())
			.serverEnvironment(serverInfo.getServerEnvironment())
			.build();

		serverInfoVO = ServerInfoVo.toVo(serverInfoSaveRequestDto);

		connectedUserInfoSaveRequestDto = ConnectedUserInfoSaveRequestDto.builder()
			.internalServerID(serverInfoSaveId)
			.dataCreatedDateTimeID(dataCreatedDateTimeSaveId)
			.userIP("127.0.0.1")
			.userLocation(
				"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n" + "\"ip\" : \"127.0.0.1\",\n"
					+ "\"error\" : \"true\",\n" + "\"version\" : \"IPv4\"\n")
			.userEnvironment("\"Mozilla/5.0\"")
			.build();

		userInfoVO = ErrorLogUserInfoVo.toVO(connectedUserInfoSaveRequestDto);

		connectedUserRequestInfoSaveRequestDto = ConnectedUserRequestInfoSaveRequestDto.builder()
			.internalServerID(serverInfoSaveId)
			.dataCreatedDateTimeID(dataCreatedDateTimeSaveId)
			.connectedUserID(1L)
			.userCookiesArray(null)
			.userCookies("내용 없음")
			.requestParameter("\"perPageNum\" : \"10\",\n" + "\"displayPageNum\" : \"10\",\n"
				+ "\"inputSearchType\" : \"USER_CONNECTED_DATE\",\n" + "\"endDate\" : \"2023-06-01\",\n"
				+ "\"page\" : \"1\",\n" + "\"startDate\" : \"2023-01-01\"")
			.requestBody("")
			.build();

		userRequestTotalInfoSaveRequestDto = UserRequestTotalInfoSaveRequestDto.builder()
			.serverInfo(serverInfo)
			.dataCreatedDateTimeRequestDTO(dataCreatedDateTimeRequestDto)
			.connectedUserInfoSaveRequestDTO(connectedUserInfoSaveRequestDto)
			.connectedUserRequestInfoSaveRequestDTO(connectedUserRequestInfoSaveRequestDto)
			.build();

		searchForUserId = new UserInfoSearchDto();
		searchForUserId.setInputSearchType(UserInfoSearchType.CONNECTED_USER_REQUEST_ID);
		searchForUserId.setSearchWord("1");

		searchForUserConnectedDateRange = new UserInfoSearchDto();
		searchForUserConnectedDateRange.setInputSearchType(UserInfoSearchType.USER_CONNECTED_DATE);
		searchForUserConnectedDateRange.setStartDate("2023-01-01");
		searchForUserConnectedDateRange.setEndDate(splitNowDateTime[0]);

		searchForUserConnectedDate = new UserInfoSearchDto();
		searchForUserConnectedDate.setInputSearchType(UserInfoSearchType.USER_CONNECTED_DATE);
		searchForUserConnectedDate.setDate(splitNowDateTime[0]);

		searchForServerName = new UserInfoSearchDto();
		searchForServerName.setInputSearchType(UserInfoSearchType.SERVER_NAME);
		searchForServerName.setSearchWord("통합관리서버");

		searchForServerIp = new UserInfoSearchDto();
		searchForServerIp.setInputSearchType(UserInfoSearchType.SERVER_IP);
		searchForServerIp.setSearchWord("192.168.20.254");

		searchForUserIp = new UserInfoSearchDto();
		searchForUserIp.setInputSearchType(UserInfoSearchType.USER_IP);
		searchForUserIp.setSearchWord("127.0.0.1");

		criteria = new Criteria();
		criteria.setPage(1);
		criteria.setPerPageNum(10);
		criteria.setPageMoveButtonNum(10);

		userInfoList = new ArrayList<>();

		IntStream.rangeClosed(1, 11).forEach(count -> {
			userInfoList.add(UserInfoListResponseDto.builder()
				.connectedUserRequestInfoID((long)count)
				.connectedDateTime(splitNowDateTime[0] + " " + splitNowDateTime[1])
				.serverName(GiggalPeopleServerNames.GIGGAL_TOTAL_BACK_OFFICE.getDescription())
				.userIP("127.0.0." + count)
				.build());
		});

		userInfoDetailSearchRequestDto = new UserInfoDetailSearchRequestDto();
		userInfoDetailSearchRequestDto.setConnectedUserRequestInfoID("1");
		userInfoDetailSearchRequestDto.setCrewGrade(CrewGrade.TEAM_LEADER);
	}

	@Test
	@Order(0)
	@DisplayName("접속 이용자 정보 및 요청 정보 목록 조회 테스트")
	void toDiscordAllConnectedUserInfoFind() throws Exception {
		//given
		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, userInfoList.size()), userInfoList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(1)
	@DisplayName("접속 이용자 정보 및 요청 정보 Paging 처리 목록 조회 테스트")
	void toDiscordAllConnectedUserInfoFindPaging() throws Exception {
		//given
		Criteria mockCriteria = new Criteria();
		mockCriteria.setPage(3);
		mockCriteria.setPerPageNum(5);

		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(mockCriteria, userInfoList.size()), userInfoList));

		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
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
	@Order(2)
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 이용자 순서 번호를 통해 검색 테스트")
	void totalUserInfoIdSearch() throws Exception {
		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, userInfoList.size()), userInfoList));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForUserId.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForUserId.getSearchWord())))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForUserId.getSearchWord())));
	}

	@Test
	@Order(3)
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 접속일 범위를 통해 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoConnectedDateRangeSearchCount() throws Exception {
		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, userInfoList.size()), userInfoList));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForUserConnectedDateRange.getInputSearchType()))
				.param("startDate", String.valueOf(searchForUserConnectedDateRange.getStartDate()))
				.param("endDate", String.valueOf(searchForUserConnectedDateRange.getEndDate())))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(4)
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 접속일 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoConnectedDateSearchCount() throws Exception {
		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, userInfoList.size()), userInfoList));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForUserConnectedDate.getInputSearchType()))
				.param("date", String.valueOf(searchForUserConnectedDate.getDate())))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(5)
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 내부 서버 이름 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoServerNameSearchCount() throws Exception {
		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, userInfoList.size()), userInfoList));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForServerName.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForServerName.getSearchWord())))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(
				content().string(containsString(GiggalPeopleServerNames.GIGGAL_TOTAL_BACK_OFFICE.getDescription())));
	}

	@Test
	@Order(6)
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 내부 서버 IP 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoServerIpSearchCount() throws Exception {
		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, userInfoList.size()), userInfoList));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForServerIp.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForServerIp.getSearchWord())))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()));
	}

	@Test
	@Order(7)
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 이용자 IP 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	void totalUserInfoUserIpSearchCount() throws Exception {
		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new Pagination(criteria, userInfoList.size()), userInfoList));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForUserIp.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForUserIp.getSearchWord())))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForUserIp.getSearchWord())));
	}

	@Test
	@Order(8)
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 검색 결과가 없을 때, NOT_EXIST_CONNECTED_USER Exception 발생 테스트")
	void isNotFoundSearchValueCustomException() throws Exception {
		UserInfoSearchDto userInfoSearchDto = new UserInfoSearchDto();
		userInfoSearchDto.setInputSearchType(UserInfoSearchType.USER_IP);
		userInfoSearchDto.setSearchWord("255.255.255.255");

		given(connectedUserInfoService.toDiscordAllUserInfoFind(any(), any())).willReturn(
			DefaultListResponse.response(NOT_EXIST_CONNECTED_USER.getStatusCode(),
				NOT_EXIST_CONNECTED_USER.getMessage(),
				new Pagination(criteria, userInfoList.size()), null));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", String.valueOf(criteria.getPage()))
				.param("perPageNum", String.valueOf(criteria.getPerPageNum()))
				.param("displayPageNum", String.valueOf(criteria.getPageMoveButtonNum()))
				.param("inputSearchType", String.valueOf(searchForUserIp.getInputSearchType()))
				.param("searchWord", String.valueOf(searchForUserIp.getSearchWord())))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(NOT_EXIST_CONNECTED_USER.getStatusCode()))
			.andExpect(jsonPath("$.message").value(NOT_EXIST_CONNECTED_USER.getMessage()));
	}

	@Test
	@Order(9)
	@DisplayName("이용자 접속 및 요청 정보 상세 조회 테스트")
	void toDiscordDetailConnectedUserInfoFind() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		ConnectedUserInfoVo connectedUserInfoVo = new ConnectedUserInfoVo(
			1L,
			splitNowDateTime[0],
			splitNowDateTime[1],
			serverInfo.getServerName(),
			serverInfo.getVmInfo(),
			serverInfo.getOsInfo(),
			serverInfo.getServerIP(),
			serverInfo.getServerEnvironment(),
			userInfoVO.getUserIP(),
			userInfoVO.getUserEnvironment(),
			userInfoVO.getUserLocation(),
			connectedUserRequestInfoSaveRequestDto.getRequestHeader(),
			connectedUserRequestInfoSaveRequestDto.getUserCookies(),
			connectedUserRequestInfoSaveRequestDto.getRequestParameter(),
			connectedUserRequestInfoSaveRequestDto.getRequestBody());

		given(connectedUserInfoService.toDiscordDetailConnectedUserInfoFind(userInfoDetailSearchRequestDto)).willReturn(
			DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				new UserInfoDetailResponseDto().toDto(connectedUserInfoVo)));

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/details")
				.contentType(MediaType.APPLICATION_JSON)
				.param("connectedUserRequestInfoID", "1")
				.param("crewGrade", String.valueOf(TEAM_LEADER)))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(SUCCESS.getStatusCode()))
			.andExpect(jsonPath("$.message").value(SUCCESS.getMessage()))
			.andExpect(content().string(containsString(searchForUserIp.getSearchWord())));
	}

	@Test
	@Order(10)
	@DisplayName("이용자 접속 및 요청 정보 상세 조회 시 권한 없는 크루가 조회 요청 시 문제 테스트")
	void toDiscordDetailConnectedUserInfoFindUnAutorized() throws Exception {
		//given
		given(connectedUserInfoService.toDiscordDetailConnectedUserInfoFind(any())).willReturn(
			DefaultResponse.response(NO_AUTHORIZATION.getStatusCode(), NO_AUTHORIZATION.getMessage(),
				null));

		//when

		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/details")
				.contentType(MediaType.APPLICATION_JSON)
				.param("connectedUserRequestInfoID", "1")
				.param("crewGrade", String.valueOf(GENERAL_CREW)))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(NO_AUTHORIZATION.getStatusCode()))
			.andExpect(jsonPath("$.message").value(NO_AUTHORIZATION.getMessage()));
	}

	@Test
	@Order(11)
	@DisplayName("이용자 접속 및 요청 정보 상세 조회 실패(값 못 찾음) 테스트")
	void toDiscordDetailConnectedUserInfoNotFind() throws Exception {
		//given
		given(connectedUserInfoService.toDiscordDetailConnectedUserInfoFind(userInfoDetailSearchRequestDto)).willReturn(
			DefaultResponse.response(NOT_EXIST_CONNECTED_USER.getStatusCode(), NOT_EXIST_CONNECTED_USER.getMessage(),
				null));
		//when
		mockMvc.perform(get(API_PREFIX_URN + API_VERSION + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/details")
				.contentType(MediaType.APPLICATION_JSON)
				.param("connectedUserRequestInfoID", "1")
				.param("crewGrade", String.valueOf(CrewGrade.TEAM_LEADER)))

			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value(NOT_EXIST_CONNECTED_USER.getStatusCode()))
			.andExpect(jsonPath("$.message").value(NOT_EXIST_CONNECTED_USER.getMessage()));
	}
}