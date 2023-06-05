package com.giggalpeople.backoffice.api.server.model.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * <h2><b>기깔나는 사람들 WAS 정보 저장 관련 요청 DTO</b></h2>
 */

@Data
public class ServerInfoSaveRequestDTO {
	private String serverName;
	private String serverVmInfo;
	private String serverOsInfo;
	private String serverIP;
	private String serverEnvironment;

	@Builder
	public ServerInfoSaveRequestDTO(String serverName, String serverVmInfo, String serverOsInfo, String serverIP,
		String serverEnvironment) {
		this.serverName = serverName;
		this.serverVmInfo = serverVmInfo;
		this.serverOsInfo = serverOsInfo;
		this.serverIP = serverIP;
		this.serverEnvironment = serverEnvironment;
	}
}
