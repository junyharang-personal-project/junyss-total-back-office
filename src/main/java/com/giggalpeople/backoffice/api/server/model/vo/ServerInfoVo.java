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

	public static ServerInfoVo toVo(ServerInfoSaveRequestDto serverInfoSaveRequestDTO) {
		ServerInfoVo serverInfoVO = new ServerInfoVo();

		serverInfoVO.serverName = serverInfoSaveRequestDTO.getServerName();
		serverInfoVO.serverVmInfo = serverInfoSaveRequestDTO.getServerVmInfo();
		serverInfoVO.serverOsInfo = serverInfoSaveRequestDTO.getServerOsInfo();
		serverInfoVO.serverIP = serverInfoSaveRequestDTO.getServerIP();
		serverInfoVO.serverEnvironment = serverInfoSaveRequestDTO.getServerEnvironment();

		return serverInfoVO;
	}
}
