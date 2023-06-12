package com.giggalpeople.backoffice.api.log.database.mapper;

import static com.giggalpeople.backoffice.api.log.model.dto.enumtype.ErrorLogSearchType.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.common.database.mapper.OccurrenceDataDateTimeManagementMapper;
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;
import com.giggalpeople.backoffice.api.log.model.dto.enumtype.ErrorLogSearchType;
import com.giggalpeople.backoffice.api.log.model.dto.request.ErrorLogSaveRequestDto;
import com.giggalpeople.backoffice.api.log.model.dto.request.ErrorLogSearchDto;
import com.giggalpeople.backoffice.api.log.model.dto.request.TotalErrorLogSaveRequestDto;
import com.giggalpeople.backoffice.api.log.model.dto.response.ErrorLogListResponseDto;
import com.giggalpeople.backoffice.api.log.model.vo.LogTotalInfoVo;
import com.giggalpeople.backoffice.api.log.model.vo.LogVo;
import com.giggalpeople.backoffice.api.server.database.mapper.ServerManagementMapper;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.api.user.database.mapper.UserManagementMapper;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserRequestTotalInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;
import com.giggalpeople.backoffice.common.database.DataBaseManagerMapper;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.enumtype.GiggalPeopleServerNames;
import com.giggalpeople.backoffice.common.util.CryptoUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LogManagementMapperTest {

	@Autowired
	LogManagementMapper logManagementMapper;

	@Autowired
	OccurrenceDataDateTimeManagementMapper occurrenceDataDateTimeManagementMapper;

	@Autowired
	ServerManagementMapper serverManagementMapper;

	@Autowired
	UserManagementMapper userManagementMapper;

	@Autowired
	DataBaseManagerMapper dataBaseManagerMapper;

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

	@Transactional
	@BeforeEach
	void beforeTestSetup() {
		this.totalErrorLogSaveRequestDto = initializedMockModels();

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
		initializedMockSearchErrorLogExceptionBriefRequestDto();
	}

	@Test
	@DisplayName("Error Log 정보 한 건 저장 테스트")
	@Order(0)
	void save() {
		//given
		//when
		Long saveId = logManagementMapper.save(initializedLogVo());

		//then
		assertThat(saveId).isEqualTo(1L);
	}

	@Test
	@DisplayName("Data Base에 저장 되어 있는 Error Level 정보 가져오기 테스트")
	@Order(1)
	@Transactional
	void findByErrorLogLevel() {
		//given
		//when
		Long byErrorLogLevel = logManagementMapper.findByErrorLogLevel(totalErrorLogSaveRequestDto.getLevel());

		//then
		assertThat(byErrorLogLevel).isEqualTo(5L);
	}

	@Test
	@DisplayName("Error Log 목록 조회 시 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	@Order(2)
	@Transactional
	void totalErrorLogSearchCount() {
		//given
		IntStream.rangeClosed(0, 10).forEach(count -> {
			System.out.println(count + "번째 테스트를 위한 정보 저장 상황 : " + logManagementMapper.save(initializedLogVo()));
		});

		ErrorLogSearchDto errorLogSearchDto = new ErrorLogSearchDto();
		errorLogSearchDto.setInputSearchType(null);
		errorLogSearchDto.setSearchWord(null);
		//when
		int searchCount = logManagementMapper.totalErrorLogSearchCount(errorLogSearchDto);

		//then
		assertTrue(String.valueOf(searchCount), searchCount > 10);
	}

	@Test
	@DisplayName("Error Log 목록 조회 시 1개만 검색 결과가 있을 경우 해당 Data Limit 절 타지 않고, Optional로 감싸 반환 테스트")
	@Order(3)
	@Transactional
	void findByErrorLogTotalInfoSearchOneThing() {
		//given
		//when
		logManagementMapper.findByErrorLogInfoSearchOneThing(searchForErrorLogId)
			.ifPresent(totalInfoVo -> {
				//then
				assertThat(totalInfoVo.getLogId()).isEqualTo(1L);
				assertThat(totalInfoVo.getLevel()).isEqualTo("ERROR");
				assertThat(totalInfoVo.getExceptionBrief()).isEqualTo(
					"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
			});
	}

	@Test
	@DisplayName("Error Log 목록 조회 시 1개 이상 검색 결과가 있을 경우 Limit 절을 태워 Paging 처리 테스트")
	@Transactional
	@Order(4)
	void findByErrorLogTotalInfoList() {
		//given
		IntStream.rangeClosed(0, 10).forEach(count -> {
			System.out.println(count + "번째 테스트를 위한 정보 저장 상황 : " + logManagementMapper.save(initializedLogVo()));
		});

		ErrorLogSearchDto errorLogSearchDto = new ErrorLogSearchDto();
		errorLogSearchDto.setInputSearchType(null);
		errorLogSearchDto.setSearchWord(null);

		List<LogTotalInfoVo> byErrorLogTotalInfoList = logManagementMapper.findByErrorLogInfoList(criteria,
			errorLogSearchDto);

		for (LogTotalInfoVo logTotalInfoVo : byErrorLogTotalInfoList) {
			assertThat(logTotalInfoVo.getLevel()).isEqualTo("ERROR");
			assertThat(logTotalInfoVo.getExceptionBrief()).isEqualTo(
				"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
		}
	}

	@Test
	@DisplayName("Discord Bot을 통해 팀장 이상 크루가 Error Log 상세 조회 시 이용자 정보 포함한 정보 반환 테스트")
	@Order(5)
	@Transactional
	void detailErrorTotalInfoFind() {
		//given
		System.out.println("테스트를 위한 정보 저장 상황 : " + logManagementMapper.save(initializedLogVo()));

		//when
		logManagementMapper.detailErrorInfoFind("1")
			.ifPresent(logTotalInfoVo -> {
				//then
				assertThat(logTotalInfoVo.getLogId()).isEqualTo(1L);
				assertThat(logTotalInfoVo.getLevel()).isEqualTo("ERROR");
				assertThat(logTotalInfoVo.getExceptionBrief()).isEqualTo(
					"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
			});
	}

	@Test
	@DisplayName("Discord Bot을 통해 팀장 이하 크루가 Error Log 상세 조회 시 이용자 정보 제외한 정보 반환 테스트")
	@Order(6)
	@Transactional
	void forGeneralCrewDetailErrorTotalInfoFind() {
		//given
		System.out.println("테스트를 위한 정보 저장 상황 : " + logManagementMapper.save(initializedLogVo()));

		//when
		logManagementMapper.forGeneralCrewDetailErrorInfoFind("1")
			.ifPresent(logTotalInfoVo -> {
				//then
				assertThat(logTotalInfoVo.getLogId()).isEqualTo(1L);
				assertThat(logTotalInfoVo.getLevel()).isEqualTo("ERROR");
				assertThat(logTotalInfoVo.getExceptionBrief()).isEqualTo(
					"com.giggalpeople.backoffice.api.user.exception.ConnectedUserException: Bad Request");
				assertThat(logTotalInfoVo.getUserIP()).isEqualTo(null);
			});
	}

	/**
	 * <b>Data Base 저장 테스트를 위한 Error Log Vo를 만드는 Method</b>
	 * @return Data Base에 저장될 Error Log Vo
	 */
	private LogVo initializedLogVo() {
		return LogVo.toVO(ErrorLogSaveRequestDto.builder()
			.internalServerID(saveMockServerInfo())
			.dataCreatedDateTimeID(saveMockCreatedLogDateTime())
			.connectedUserID(saveMockConnectedUserId())
			.connectedUserRequestInfoID(saveMockConnectedUserRequestInfo())
			.logLevelID(getLogId())
			.exceptionBrief(totalErrorLogSaveRequestDto.getExceptionBrief())
			.exceptionDetail(totalErrorLogSaveRequestDto.getExceptionDetail())
			.build());
	}

	/**
	 * <b>Data Base에 등록된 Error Log Level을 찾기 위한 Method</b>
	 * @return 해당 Error Level PK
	 */

	private Long getLogId() {
		return logManagementMapper.findByErrorLogLevel(totalErrorLogSaveRequestDto.getLevel());
	}

	/**
	 * <b>Error 발생 이용자 요청 정보 Data Base 저장</b>
	 * @return Error 발생 이용자 요청 정보 PK 값
	 */
	private Long saveMockConnectedUserRequestInfo() {
		return userManagementMapper.findByRequestInfoSave(UserRequestInfoVo.toVo(CryptoUtil.userRequestInfoEncrypt(
			ConnectedUserRequestInfoSaveRequestDto.builder()
				.internalServerID(saveMockServerInfo())
				.dataCreatedDateTimeID(saveMockCreatedLogDateTime())
				.connectedUserID(saveMockConnectedUserId())
				.requestHeader(
					"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n" + "\"ip\" : \"127.0.0.1\",\n"
						+ "\"error\" : \"true\",\n" + "\"version\" : \"IPv4\\n\"")
				.userCookiesArray(new Cookie[0])
				.userCookies("내용 없음")
				.requestParameter("")
				.requestBody("\"\"")
				.build())));
	}

	/**
	 * <b>Error 발생 이용자 정보 Data Base 저장</b>
	 * @return Error 발생 이용자 정보 PK 값
	 */

	private Long saveMockConnectedUserId() {
		return userManagementMapper.connectedUserSave(ErrorLogUserInfoVo.toVO(CryptoUtil.userInfoEncrypt(
			ConnectedUserInfoSaveRequestDto.builder()
				.internalServerID(saveMockServerInfo())
				.dataCreatedDateTimeID(saveMockCreatedLogDateTime())
				.userIP("127.0.0.1")
				.userLocation(
					"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n" + "\"ip\" : \"127.0.0.1\",\n"
						+ "\"error\" : \"true\",\n" + "\"version\" : \"IPv4\"\n")
				.userEnvironment("\"Mozilla/5.0\"")
				.build())));
	}

	/**
	 * <b>Test 전용 생성 일시 Data Base 저장</b>
	 * @return 생성 일시 정보 PK 값
	 */
	private Long saveMockCreatedLogDateTime() {
		String[] initializedCreateDateTime = initializedCreateDateTime();

		return occurrenceDataDateTimeManagementMapper.save(DataCreatedDateTimeVo.toVO(
			DataCreatedDateTimeRequestDto.builder()
				.createdDate(initializedCreateDateTime[0])
				.createdTime(initializedCreateDateTime[1])
				.build()));
	}

	/**
	 * <b>Test 전용 Server 정보 Data Base 저장</b>
	 * @return Server 정보 PK 값
	 */
	private Long saveMockServerInfo() {
		return serverManagementMapper.save(ServerInfoVo.toVo(ServerInfoSaveRequestDto.builder()
			.serverName(serverInfo.getServerName())
			.serverVmInfo(serverInfo.getVmInfo())
			.serverOsInfo(serverInfo.getOsInfo())
			.serverIP(serverInfo.getServerIP())
			.serverEnvironment(serverInfo.getServerEnvironment())
			.build()));
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
	private void initializedMockSearchCreatedErrorLogDateRequestDto(String initializedCreatedDate) {
		this.errorLogSearchCreatedErrorLogDateDto = new ErrorLogSearchDto();
		this.errorLogSearchCreatedErrorLogDateDto.setInputSearchType(LOG_CREATE_DATE);
		this.errorLogSearchCreatedErrorLogDateDto.setDate(initializedCreatedDate);
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

		TotalErrorLogSaveRequestDto totalErrorLogSaveRequestDto = new TotalErrorLogSaveRequestDto();
		totalErrorLogSaveRequestDto.setCreatedAt(
			dataCreatedDateTimeVo.getDataCreatedDate() + " " + dataCreatedDateTimeVo.getDataCreatedTime());
		totalErrorLogSaveRequestDto.setLevel("ERROR");
		totalErrorLogSaveRequestDto.setServerName(serverInfo.getServerName());
		totalErrorLogSaveRequestDto.setVmInfo(serverInfo.getVmInfo());
		totalErrorLogSaveRequestDto.setOsInfo(serverInfo.getOsInfo());
		totalErrorLogSaveRequestDto.setServerIP(serverInfo.getServerIP());
		totalErrorLogSaveRequestDto.setServerEnvironment(serverInfo.getServerEnvironment());
		totalErrorLogSaveRequestDto.setUserIp(userInfoVo.getUserIP());
		totalErrorLogSaveRequestDto.setUserLocation(userInfoVo.getUserLocation());
		totalErrorLogSaveRequestDto.setUserEnvironment(userInfoVo.getUserEnvironment());
		totalErrorLogSaveRequestDto.setRequestHeader(userRequestInfoVo.getRequestHeader());
		totalErrorLogSaveRequestDto.setUserCookies(null);
		totalErrorLogSaveRequestDto.setRequestParameter(userRequestInfoVo.getRequestParameter());
		totalErrorLogSaveRequestDto.setRequestBody(userRequestInfoVo.getRequestBody());
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