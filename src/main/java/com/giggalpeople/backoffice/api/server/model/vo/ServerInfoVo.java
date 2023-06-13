package com.giggalpeople.backoffice.api.server.model.vo;

import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDto;

import lombok.Getter;

@Getter
public class ServerInfoVo {
	private Long internalServerID;
	private String serverName;
	private String serverVmInfo;
	private String serverOsInfo;
	private String serverIP;
	private String serverEnvironment;

	public static ServerInfoVo toVo(ServerInfoSaveRequestDto serverInfoSaveRequestDto) {
		ServerInfoVo serverInfoVo = new ServerInfoVo();

		serverInfoVo.serverName = serverInfoSaveRequestDto.getServerName();
		serverInfoVo.serverVmInfo = serverInfoSaveRequestDto.getServerVmInfo();
		serverInfoVo.serverOsInfo = serverInfoSaveRequestDto.getServerOsInfo();
		serverInfoVo.serverIP = serverInfoSaveRequestDto.getServerIP();
		serverInfoVo.serverEnvironment = serverInfoSaveRequestDto.getServerEnvironment();

		return serverInfoVo;
	}
}
