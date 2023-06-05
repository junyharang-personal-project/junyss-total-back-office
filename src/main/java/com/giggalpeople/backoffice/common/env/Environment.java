package com.giggalpeople.backoffice.common.env;

import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.*;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.env.exception.ServerInfoException;

import ch.qos.logback.core.util.SystemInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2><b>Discord Bot을 통해 API 호출 시 각 환경을 확인하기 위한 Class</b</h2>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Environment {

	/**
	 * <b>Server 구동 환경 (Local, Dev, Prod)를 판별하기 위한 Method</b>
	 * @return 현재 구동 환경 정보
	 */
	public static String checkServerEnvironment() {
		return System.getProperty("spring.profiles.active");
	}

	/**
	 * <b>Spring Boot 작동 환경을 파악하여 환경에 맞는 API URL 반환</b>
	 * @return 환경에 맞는 API URL
	 */
	public static StringBuilder initPrefixAPIURL() {
		String springProfile = checkServerEnvironment();

		StringBuilder url = new StringBuilder();
		if (springProfile.equals("local")) {
			url.append(LOCAL_ENV_DEFAULT_URL);
		} else if (springProfile.equals("dev")) {
			url.append(DEV_ENV_DEFAULT_URL);
		} else {
			url.append(PROD_ENV_DEFAULT_URL);
		}
		return initCommonAPIURL(url);
	}

	/**
	 * <b>URL에 공통적으로 들어가야 하는 내용을 첨가하기 위한 Method</b>
	 * @param url 환경별로 호출해야 하는 API Port 정보가 입력된 URL 정보
	 */
	private static StringBuilder initCommonAPIURL(StringBuilder url) {
		return url.append(API_PREFIX_URN + API_VERSION);
	}

	/**
	 * <b>Exception 발생으로 Discord에 Log를 보낼 때, 구동 서버 정보를 담아 보내기 위한 Method</b>
	 * @return 구동 서버 정보를 담은 객체
	 */

	public static ServerInfo getServerInfo() {
		SpringApplication applicationInfo = new SpringApplication(SystemInfo.class);
		applicationInfo.setWebApplicationType(WebApplicationType.NONE);
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

		try {       /* InetAddress 객체에서 getLocalHost로 Server IP 정보를 가져 올 때, Exception 처리를 위한 Try Catch */
			return ServerInfo.builder()
				.vmInfo(runtimeMXBean.getVmName() + " " + runtimeMXBean.getVmVersion())
				.osInfo(
					System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty(
						"os.arch"))
				.serverIP(InetAddress.getLocalHost().getHostAddress())
				.serverEnvironment(checkServerEnvironment())
				.build();

		} catch (UnknownHostException unknownHostException) {
			throw new ServerInfoException(unknownHostException.getMessage(), unknownHostException);
		}
	}
}
