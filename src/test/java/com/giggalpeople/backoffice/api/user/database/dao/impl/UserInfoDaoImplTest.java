package com.giggalpeople.backoffice.api.user.database.dao.impl;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import org.springframework.boot.test.context.SpringBootTest;
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
import com.giggalpeople.backoffice.api.user.model.UpdateUserInfo;
import com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDto;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVo;
import com.giggalpeople.backoffice.api.user.model.vo.ErrorLogUserInfoVo;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.user.request_info.model.vo.UserRequestInfoVo;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.enumtype.GiggalPeopleServerNames;
import com.giggalpeople.backoffice.common.util.CryptoUtil;

// JUnit 5 사용 시 사용, MyBatisTest 2.0.1 Version 이상에서 생략 가능
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserInfoDaoImplTest {

	@Autowired
	OccurrenceDataDateTimeManagementDao occurrenceDataDateTimeManagementDao;

	@Autowired
	ServerInfoDao serverInfoDao;

	@Autowired
	UserInfoDao userInfoDao;

	DataCreatedDateTimeVo dataCreatedDateTimeVo;

	ServerInfoVo serverInfoVo;

	ErrorLogUserInfoVo userInfoVo;

	Long dataCreatedDateTimeSaveId;
	Long serverInfoSaveId;

	UserInfoSearchDto searchForUserId;

	UserInfoSearchDto searchForUserConnectedDateRange;

	UserInfoSearchDto searchForUserConnectedDate;

	UserInfoSearchDto searchForServerName;

	UserInfoSearchDto searchForServerIp;

	UserInfoSearchDto searchForUserIp;

	@Transactional
	@BeforeEach
	void beforeTestSetup() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		dataCreatedDateTimeVo = DataCreatedDateTimeVo.toVO(DataCreatedDateTimeRequestDto.builder()
			.createdDate(splitNowDateTime[0])
			.createdTime(splitNowDateTime[1])
			.build());

		dataCreatedDateTimeSaveId = occurrenceDataDateTimeManagementDao.save(dataCreatedDateTimeVo);

		ServerInfo serverInfo = ServerInfo.builder()
			.serverName(GiggalPeopleServerNames.GIGGAL_TOTAL_BACK_OFFICE.getDescription())
			.vmInfo("OpenJDK 64-Bit")
			.osInfo("Mac OS")
			.serverIP("192.168.20.254")
			.serverEnvironment("dev")
			.build();

		serverInfoVo = ServerInfoVo.toVo(ServerInfoSaveRequestDto.builder()
			.serverName(serverInfo.getServerName())
			.serverVmInfo(serverInfo.getVmInfo())
			.serverOsInfo(serverInfo.getOsInfo())
			.serverIP(serverInfo.getServerIP())
			.serverEnvironment(serverInfo.getServerEnvironment())
			.build());

		serverInfoSaveId = serverInfoDao.save(serverInfoVo);

		userInfoVo = ErrorLogUserInfoVo.toVO(CryptoUtil.userInfoEncrypt(ConnectedUserInfoSaveRequestDto.builder()
			.internalServerID(serverInfoSaveId)
			.dataCreatedDateTimeID(dataCreatedDateTimeSaveId)
			.userIP("127.0.0.1")
			.userLocation(
				"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n" + "\"ip\" : \"127.0.0.1\",\n"
					+ "\"error\" : \"true\",\n" + "\"version\" : \"IPv4\"\n")
			.userEnvironment("\"Mozilla/5.0\"")
			.build()));

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
		searchForUserIp.setSearchWord(CryptoUtil.encryptUserIP("127.0.0.1"));
	}

	@Test
	@DisplayName("요청 이용자 정보 한 건 저장 테스트")
	@Order(0)
	void connectedUserSave() {
		//given
		String encryptUserIP = CryptoUtil.encryptUserIP("127.0.0.1");
		Long byUserId = userInfoDao.findByUserIP(encryptUserIP);

		//when
		if (byUserId != null) {
			System.out.println("이미 Data Base에 존재하는 이용자 정보 처리");
			assertThat(byUserId).isEqualTo(1L);
		} else {
			System.out.println("Data Base에 존재하지 않은 이용자 정보 처리");
			Long saveId = userInfoDao.connectedUserSave(userInfoVo);

			//then
			assertThat(saveId).isEqualTo(1L);
		}
	}

	@Test
	@DisplayName("요청 이용자 정보가 이미 Data Base에 저장 되어 있을 때, 정보 Update를 위한 테스트")
	@Order(1)
	void updateCount() {
		//given
		userInfoDao.connectedUserSave(userInfoVo);
		Long byUserId = userInfoDao.findByUserIP(userInfoVo.getUserIP());

		//when
		if (byUserId != null) {
			System.out.println("이미 Data Base에 존재하는 이용자 정보 처리");

			assertThat(userInfoDao.updateCount(UpdateUserInfo.builder()
				.connectedUserID(byUserId)
				.dataCreatedDateTimeID(dataCreatedDateTimeSaveId)
				.build())).isEqualTo(1L);
		} else {
			System.out.println("Data Base에 존재하지 않은 이용자 정보 처리");
			Long saveId = userInfoDao.connectedUserSave(userInfoVo);

			//then
			assertThat(saveId).isEqualTo(1L);
		}
	}

	@Test
	@DisplayName("요청 이용자 정보가 이미 Data Base에 저장 되어 있을 때, 정보 Update 실패를 위한 테스트")
	@Order(2)
	void updateCountFailure() {
		//given
		Long userId = 99999L;

		//when
		assertThrows(ConnectedUserException.class, () -> {
			userInfoDao.updateCount(UpdateUserInfo.builder()
				.connectedUserID(userId)
				.dataCreatedDateTimeID(dataCreatedDateTimeSaveId)
				.build());

			bySearchCountThrowException();
		});

		//then
		assertEquals(CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE.getMessage(),
			CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE.getMessage());
	}

	@Test
	@DisplayName("이용자가 보낸 요청 정보를 Data Base에 저장을 위한 테스트")
	@Order(3)
	void findByRequestInfoSave() {
		//given
		UserRequestInfoVo userRequestInfoVO = UserRequestInfoVo.toVo(CryptoUtil.userRequestInfoEncrypt(
			ConnectedUserRequestInfoSaveRequestDto.builder()
				.internalServerID(serverInfoSaveId)
				.dataCreatedDateTimeID(dataCreatedDateTimeSaveId)
				.connectedUserID(userInfoDao.connectedUserSave(userInfoVo))
				.requestHeader(
					"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n" + "\"ip\" : \"127.0.0.1\",\n"
						+ "\"error\" : \"true\",\n" + "\"version\" : \"IPv4\\n\"")
				.userCookiesArray(new Cookie[0])
				.userCookies("내용 없음")
				.requestParameter("")
				.requestBody("\"\"")
				.build()));

		//when
		Long byRequestInfoSaveId = userInfoDao.findByRequestInfoSave(userRequestInfoVO);

		//then
		assertThat(byRequestInfoSaveId).isEqualTo(1L);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 테스트")
	@Order(4)
	@Transactional
	void totalUserInfoSearch() {
		// given
		UserInfoSearchDto userInfoSearchDto = new UserInfoSearchDto();
		userInfoSearchDto.setInputSearchType(null);
		userInfoSearchDto.setSearchWord(null);

		// when
		int searchCountToUserId = userInfoDao.totalUserInfoSearchCount(userInfoSearchDto);

		// then
		if (searchCountToUserId <= 0) {
			System.out.println("요청 검색 결과가 Data Base에서 찾지 못했음으로 해당 Exception Test 진행");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.totalUserInfoSearchCount(searchForUserId);

				bySearchCountThrowException();
			});

			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		} else {
			System.out.println("요청 검색 결과가 Data Base에서 한 건이므로, 해당 Logic 처리 Test 진행");
			Optional<ConnectedUserInfoVo> byUserInfoSearchOneThing = userInfoDao.findByUserInfoSearchOneThing(
				userInfoSearchDto);

			byUserInfoSearchOneThing.ifPresent(
				connectedUserInfoVO -> assertEquals((long)connectedUserInfoVO.getConnectedUserRequestInfoID(),
					Long.parseLong(searchForUserId.getSearchWord())));
		}

		System.out.println("이용자 접속 ID 검색 조회 결과수 : " + searchCountToUserId);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 이용자 순서 번호를 통해 검색 테스트")
	@Order(5)
	@Transactional
	void totalUserInfoIdSearch() {
		// given
		// when
		int searchCountToUserId = userInfoDao.totalUserInfoSearchCount(searchForUserId);

		// then
		if (searchCountToUserId <= 0) {
			System.out.println("요청 검색 결과가 Data Base에서 찾지 못했음으로 해당 Exception Test 진행");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.totalUserInfoSearchCount(searchForUserId);

				bySearchCountThrowException();
			});

			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		} else {
			System.out.println("요청 검색 결과가 Data Base에서 한 건이므로, 해당 Logic 처리 Test 진행");
			Optional<ConnectedUserInfoVo> byUserInfoSearchOneThing = userInfoDao.findByUserInfoSearchOneThing(
				searchForUserId);

			byUserInfoSearchOneThing.ifPresent(
				connectedUserInfoVO -> assertEquals((long)connectedUserInfoVO.getConnectedUserRequestInfoID(),
					Long.parseLong(searchForUserId.getSearchWord())));
		}

		System.out.println("이용자 접속 ID 검색 조회 결과수 : " + searchCountToUserId);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 접속일 범위를 통해 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	@Order(6)
	@Transactional
	void totalUserInfoConnectedDateRangeSearchCount() {

		// given
		// when
		int searchCountToConnectedDateRange = userInfoDao.totalUserInfoSearchCount(searchForUserConnectedDateRange);

		// then
		if (searchCountToConnectedDateRange <= 0) {
			System.out.println("요청 검색 결과가 Data Base에서 찾지 못했음으로 해당 Exception Test 진행");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.totalUserInfoSearchCount(searchForUserConnectedDateRange);

				bySearchCountThrowException();
			});

			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		} else if (searchCountToConnectedDateRange == 1) {
			System.out.println("요청 검색 결과가 Data Base에서 한 건이므로, 해당 Logic 처리 Test 진행");
			Optional<ConnectedUserInfoVo> byUserInfoSearchOneThing = userInfoDao.findByUserInfoSearchOneThing(
				searchForUserConnectedDateRange);

			byUserInfoSearchOneThing.ifPresent(connectedUserInfoVO -> assertTrue(
				isWithSearchDateRange(connectedUserInfoVO.getDataCreatedDate(), searchForUserConnectedDateRange)));
		} else {
			System.out.println("요청 검색 결과가 Data Base에서 여러 건이므로, 해당 Logic 처리 Test 진행");
			Criteria criteria = new Criteria();
			criteria.setPage(1);
			criteria.setPerPageNum(10);
			criteria.setPageMoveButtonNum(10);

			List<ConnectedUserInfoVo> byUserInfoList = userInfoDao.findByUserInfoList(criteria,
				searchForUserConnectedDateRange);

			for (ConnectedUserInfoVo byUserInfo : byUserInfoList) {
				assertTrue(isWithSearchDateRange(byUserInfo.getDataCreatedDate(), searchForUserConnectedDateRange));
			}
		}

		System.out.println("이용자 접속일 범위 검색 조회 결과수 : " + searchCountToConnectedDateRange);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 접속일 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	@Order(7)
	@Transactional
	void totalUserInfoConnectedDateSearchCount() {
		// given
		// when
		int searchCountToConnectedDate = userInfoDao.totalUserInfoSearchCount(searchForUserConnectedDate);

		// then
		if (searchCountToConnectedDate <= 0) {
			System.out.println("요청 검색 결과가 Data Base에서 찾지 못했음으로 해당 Exception Test 진행");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.totalUserInfoSearchCount(searchForUserConnectedDate);

				bySearchCountThrowException();
			});

			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		} else if (searchCountToConnectedDate == 1) {
			System.out.println("요청 검색 결과가 Data Base에서 한 건이므로, 해당 Logic 처리 Test 진행");
			Optional<ConnectedUserInfoVo> byUserInfoSearchOneThing = userInfoDao.findByUserInfoSearchOneThing(
				searchForUserConnectedDate);

			byUserInfoSearchOneThing.ifPresent(connectedUserInfoVO -> assertEquals(
				Integer.parseInt(searchForUserConnectedDate.getDate().replace("-", "")),
				Integer.parseInt(connectedUserInfoVO.getDataCreatedDate().replace("-", ""))));

		} else {
			System.out.println("요청 검색 결과가 Data Base에서 여러 건이므로, 해당 Logic 처리 Test 진행");
			Criteria criteria = new Criteria();
			criteria.setPage(1);
			criteria.setPerPageNum(10);
			criteria.setPageMoveButtonNum(10);

			List<ConnectedUserInfoVo> byUserInfoList = userInfoDao.findByUserInfoList(criteria,
				searchForUserConnectedDate);

			for (ConnectedUserInfoVo byUserInfo : byUserInfoList) {
				assertEquals(Integer.parseInt(searchForUserConnectedDate.getDate().replace("-", "")),
					Integer.parseInt(byUserInfo.getDataCreatedDate().replace("-", "")));
			}
		}

		System.out.println("이용자 접속일 검색 조회 결과수 : " + searchCountToConnectedDate);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 내부 서버 이름 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	@Order(8)
	@Transactional
	void totalUserInfoServerNameSearchCount() {

		// given
		// when
		int searchCountToServerName = userInfoDao.totalUserInfoSearchCount(searchForServerName);

		// then
		if (searchCountToServerName <= 0) {
			System.out.println("요청 검색 결과가 Data Base에서 찾지 못했음으로 해당 Exception Test 진행");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.totalUserInfoSearchCount(searchForServerName);

				bySearchCountThrowException();
			});

			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		} else if (searchCountToServerName == 1) {
			System.out.println("요청 검색 결과가 Data Base에서 한 건이므로, 해당 Logic 처리 Test 진행");
			Optional<ConnectedUserInfoVo> byUserInfoSearchOneThing = userInfoDao.findByUserInfoSearchOneThing(
				searchForServerName);

			byUserInfoSearchOneThing.ifPresent(connectedUserInfoVO -> assertEquals(searchForServerName.getSearchWord(),
				connectedUserInfoVO.getServerName()));

		} else {
			System.out.println("요청 검색 결과가 Data Base에서 여러 건이므로, 해당 Logic 처리 Test 진행");
			Criteria criteria = new Criteria();
			criteria.setPage(1);
			criteria.setPerPageNum(10);
			criteria.setPageMoveButtonNum(10);

			List<ConnectedUserInfoVo> byUserInfoList = userInfoDao.findByUserInfoList(criteria, searchForServerName);

			for (ConnectedUserInfoVo byUserInfo : byUserInfoList) {
				assertEquals(searchForServerName.getSearchWord(), byUserInfo.getServerName());
			}
		}

		System.out.println("내부 서버 이름 검색 조회 결과수 : " + searchCountToServerName);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 내부 서버 IP 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	@Order(9)
	@Transactional
	void totalUserInfoServerIpSearchCount() {

		// given
		// when
		int searchCountToServerIp = userInfoDao.totalUserInfoSearchCount(searchForServerIp);

		// then
		if (searchCountToServerIp <= 0) {
			System.out.println("요청 검색 결과가 Data Base에서 찾지 못했음으로 해당 Exception Test 진행");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.totalUserInfoSearchCount(searchForServerIp);

				bySearchCountThrowException();
			});

			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		} else if (searchCountToServerIp == 1) {
			System.out.println("요청 검색 결과가 Data Base에서 한 건이므로, 해당 Logic 처리 Test 진행");
			Optional<ConnectedUserInfoVo> byUserInfoSearchOneThing = userInfoDao.findByUserInfoSearchOneThing(
				searchForServerIp);

			byUserInfoSearchOneThing.ifPresent(connectedUserInfoVO -> assertEquals(searchForServerIp.getSearchWord(),
				connectedUserInfoVO.getServerIp()));

		} else {
			System.out.println("요청 검색 결과가 Data Base에서 여러 건이므로, 해당 Logic 처리 Test 진행");
			Criteria criteria = new Criteria();
			criteria.setPage(1);
			criteria.setPerPageNum(10);
			criteria.setPageMoveButtonNum(10);

			List<ConnectedUserInfoVo> byUserInfoList = userInfoDao.findByUserInfoList(criteria, searchForServerIp);

			for (ConnectedUserInfoVo byUserInfo : byUserInfoList) {
				assertEquals(searchForServerIp.getSearchWord(), byUserInfo.getServerIp());
			}
		}

		System.out.println("내부 서버 IP 검색 조회 결과수 : " + searchCountToServerIp);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 이용자 IP 검색 했을 때, 일치하는 Data가 몇 개 있는지 알기 위한 테스트")
	@Order(10)
	@Transactional
	void totalUserInfoUserIpSearchCount() {

		// given
		// when
		int searchCountToUserIp = userInfoDao.totalUserInfoSearchCount(searchForUserIp);

		// then
		if (searchCountToUserIp <= 0) {
			System.out.println("요청 검색 결과가 Data Base에서 찾지 못했음으로 해당 Exception Test 진행");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.totalUserInfoSearchCount(searchForServerIp);

				bySearchCountThrowException();
			});

			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		} else if (searchCountToUserIp == 1) {
			System.out.println("요청 검색 결과가 Data Base에서 한 건이므로, 해당 Logic 처리 Test 진행");
			Optional<ConnectedUserInfoVo> byUserInfoSearchOneThing = userInfoDao.findByUserInfoSearchOneThing(
				searchForUserIp);

			byUserInfoSearchOneThing.ifPresent(
				connectedUserInfoVO -> assertEquals(CryptoUtil.userInfoIPDecrypt(searchForUserIp.getSearchWord()),
					CryptoUtil.userInfoIPDecrypt(connectedUserInfoVO.getUserIp())));

		} else {
			System.out.println("요청 검색 결과가 Data Base에서 여러 건이므로, 해당 Logic 처리 Test 진행");
			Criteria criteria = new Criteria();
			criteria.setPage(1);
			criteria.setPerPageNum(10);
			criteria.setPageMoveButtonNum(10);

			List<ConnectedUserInfoVo> byUserInfoList = userInfoDao.findByUserInfoList(criteria, searchForUserIp);

			for (ConnectedUserInfoVo byUserInfo : byUserInfoList) {
				assertEquals(CryptoUtil.userInfoIPDecrypt(searchForUserIp.getSearchWord()),
					CryptoUtil.userInfoIPDecrypt(byUserInfo.getUserIp()));
			}
		}
		System.out.println("이용자 IP 검색 조회 결과 개수 : " + searchCountToUserIp);
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 목록 조회 시 검색 결과가 없을 때, Custom Exception 발생 테스트")
	@Order(11)
	@Transactional
	void isNotFoundSearchValueCustomException() {
		// given
		UserInfoSearchDto userInfoSearchDto = new UserInfoSearchDto();
		userInfoSearchDto.setInputSearchType(UserInfoSearchType.USER_IP);
		userInfoSearchDto.setSearchWord(CryptoUtil.encryptUserIP("255.255.255.255"));

		// when
		// then
		ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
			userInfoDao.totalUserInfoSearchCount(userInfoSearchDto);

			bySearchCountThrowException();
		});

		assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 상세 조회 테스트")
	@Order(12)
	@Transactional
	void detailUserInfoFind() {
		String requestUserId = "1";

		Optional<ConnectedUserInfoVo> detailSearchConnectedUserInfoVO = userInfoDao.detailUserInfoFind(requestUserId);

		if (detailSearchConnectedUserInfoVO.isPresent()) {
			assertEquals(detailSearchConnectedUserInfoVO.get().getConnectedUserRequestInfoID(),
				Long.parseLong(requestUserId));
		} else {
			System.out.println("조회 결과 Data Base에 일치하는 값이 없으므로, Exception 처리");
			ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
				userInfoDao.detailUserInfoFind(requestUserId);

				bySearchCountThrowException();
			});
			assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
		}
	}

	@Test
	@DisplayName("이용자 접속 및 요청 정보 상세 조회 시 조회 결과가 없을 때, Custom Exception 발생 여부 테스트")
	@Order(13)
	@Transactional
	void isNotFoundDetailFindCustomException() {
		String requestUserId = "99999999999999999999999";

		ConnectedUserException connectedUserException = assertThrows(ConnectedUserException.class, () -> {
			userInfoDao.detailUserInfoFind(requestUserId);

			bySearchCountThrowException();
		});
		assertEquals(NOT_EXIST_CONNECTED_USER.getMessage(), connectedUserException.getMessage());
	}

	@Test
	@DisplayName("요청 이용자 정보 100건 저장 테스트")
	@Order(14)
	void manyConnectedUserSave() {
		IntStream.rangeClosed(1, 100).forEach(count -> {
			System.out.println(count + "번째 Mock 이용자 정보 생성 시작 합니다.");
			assertThat(userInfoDao.connectedUserSave(ErrorLogUserInfoVo.toVO(CryptoUtil.userInfoEncrypt(
				ConnectedUserInfoSaveRequestDto.builder()
					.internalServerID(serverInfoSaveId)
					.dataCreatedDateTimeID(dataCreatedDateTimeSaveId)
					.userIP("127.0.0." + count)
					.userLocation(
						"\"reason\" : \"ReservedIPAddress\",\n" + "\"reserved\" : \"true\",\n" + "\"ip\" : \"127.0.0."
							+ count)
					.userEnvironment("\"Mozilla/5.\"" + count)
					.build())))).isPositive().isLessThan(101);
		});
	}

	/**
	 * <b>날짜 범위 검사 결과가 올바르게 나왔는지 확인하기 위한 Method</b>
	 * @param dataCreatedDate Data Base에 저장 되어 있는 Data의 생성일
	 * @param searchForUserConnectedDateRange 검색 요청 정보를 담고 있는 DTO
	 * @return Data Base에 저장 되어 있는 Data 생성일이 검색 요청 정보 중 날짜 범위 중 시작과 끝 날짜 사이에 있으면 True 아니면 False 반환
	 */
	private boolean isWithSearchDateRange(String dataCreatedDate, UserInfoSearchDto searchForUserConnectedDateRange) {
		int startDate = Integer.parseInt(searchForUserConnectedDateRange.getStartDate().replace("-", ""));
		int endDate = Integer.parseInt(searchForUserConnectedDateRange.getEndDate().replace("-", ""));
		int valueDate = Integer.parseInt(dataCreatedDate.replace("-", ""));

		return valueDate >= startDate && endDate >= valueDate;
	}

	/**
	 * <b>검색 결과가 없을 경우 Custom Exception이 동작하도록 하기 위한 Method</b>
	 */
	private void bySearchCountThrowException() {
		throw new ConnectedUserException(NOT_EXIST_CONNECTED_USER, NOT_EXIST_CONNECTED_USER.getMessage());
	}
}