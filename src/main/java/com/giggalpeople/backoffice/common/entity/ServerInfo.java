package com.giggalpeople.backoffice.common.entity;

import lombok.Builder;
import lombok.Data;

@Data
public class ServerInfo {
	private String serverName;
	private String vmInfo;
	private String osInfo;
	private String serverIP;
	private String serverEnvironment;

	public String getServerName() {
		return "Giggal-Total-Back-Office";
	}

	@Builder
	public ServerInfo(String vmInfo, String osInfo, String serverIP, String serverEnvironment) {
		this.vmInfo = vmInfo;
		this.osInfo = osInfo;
		this.serverIP = serverIP;
		this.serverEnvironment = serverEnvironment;
	}
}
