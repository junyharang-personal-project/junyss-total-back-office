package com.giggalpeople.backoffice.api.server.database.dao.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.server.database.dao.ServerInfoDao;
import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;
import com.giggalpeople.backoffice.common.entity.ServerInfo;

// JUnit 5 사용 시 사용, MyBatisTest 2.0.1 Version 이상에서 생략 가능
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class ServerInfoDaoImplTest {

	@Autowired
	ServerInfoDao serverInfoDAO;

	@Test
	@DisplayName("기깔나는 사람들 보유 WAS 정보 저장 - 정상")
	@Order(0)
	void saveSuccess() {
		//given
		String serverIP = "192.168.20.254";
		assertThat(serverInfoDAO.findByServerID(serverIP)).isNull();

		ServerInfoVo serverInfoVO = ServerInfoVo.toVo(
			ServerInfoSaveRequestDto.builder()
				.serverName("total-back-office")
				.serverVmInfo("OpenJDK 64-Bit Server VM 11.0.16+8-LTS")
				.serverOsInfo("Mac OS X 13.3.1 aarch64")
				.serverIP("192.168.20.254")
				.serverEnvironment("dev")
				.build());

		//when
		Long saveID = serverInfoDAO.save(serverInfoVO);

		//then
		assertThat(saveID).isEqualTo(1L);
	}

	@Test
	@DisplayName("기깔나는 사람들 보유 WAS 정보 저장 - DTO Null로 인한 실패")
	@Order(1)
	void saveFailure() {
		//given
		ServerInfo serverInfo = ServerInfo.builder()
			.vmInfo(null)
			.osInfo(null)
			.serverIP(null)
			.serverEnvironment(null)
			.build();

		ServerInfoVo serverInfoVO = ServerInfoVo.toVo(
			ServerInfoSaveRequestDto.builder()
				.serverName(serverInfo.getServerName())
				.serverVmInfo(serverInfo.getVmInfo())
				.serverOsInfo(serverInfo.getOsInfo())
				.serverIP(serverInfo.getServerIP())
				.serverEnvironment(serverInfo.getServerEnvironment())
				.build());

		//when
		//then
		assertThrows(DataIntegrityViolationException.class, () -> serverInfoDAO.save(serverInfoVO));
		// assertThat(saveID).isEqualTo(1L);
	}

	@Test
	@DisplayName("기깔나는 사람들 보유 WAS 정보 저장 - 이미 정보가 저장 되어있을 때")
	@Order(2)
	void findByServerIdFailure() {
		//given
		String serverIP = "192.168.20.254";

		//when
		if (serverInfoDAO.findByServerID(serverIP) != null) {
			assertThat(serverInfoDAO.findByServerID(serverIP)).isEqualTo(1L);

		} else {
			ServerInfo serverInfo = ServerInfo.builder()
				.vmInfo("OpenJDK 64-Bit Server VM 11.0.16+8-LTS")
				.osInfo("Mac OS X 13.3.1 aarch64")
				.serverIP("192.168.20.254")
				.serverEnvironment("dev")
				.build();

			ServerInfoVo serverInfoVO = ServerInfoVo.toVo(
				ServerInfoSaveRequestDto.builder()
					.serverName(serverInfo.getServerName())
					.serverVmInfo(serverInfo.getVmInfo())
					.serverOsInfo(serverInfo.getOsInfo())
					.serverIP(serverInfo.getServerIP())
					.serverEnvironment(serverInfo.getServerEnvironment())
					.build());

			//then
			assertThat(serverInfoDAO.save(serverInfoVO)).isEqualTo(1L);
		}
	}
}