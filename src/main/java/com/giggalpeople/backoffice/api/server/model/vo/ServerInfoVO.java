package com.giggalpeople.backoffice.api.server.model.vo;

import com.giggalpeople.backoffice.api.server.model.dto.request.ServerInfoSaveRequestDTO;
import lombok.Getter;

@Getter
public class ServerInfoVO {
    private Long internalServerID;
    private String serverName;
    private String serverVmInfo;
    private String serverOsInfo;
    private String serverIP;
    private String serverEnvironment;

    public static ServerInfoVO toVO (ServerInfoSaveRequestDTO serverInfoSaveRequestDTO) {
        ServerInfoVO serverInfoVO = new ServerInfoVO();

        serverInfoVO.serverName = serverInfoSaveRequestDTO.getServerName();
        serverInfoVO.serverVmInfo = serverInfoSaveRequestDTO.getServerVmInfo();
        serverInfoVO.serverOsInfo = serverInfoSaveRequestDTO.getServerOsInfo();
        serverInfoVO.serverIP = serverInfoSaveRequestDTO.getServerIP();
        serverInfoVO.serverEnvironment = serverInfoSaveRequestDTO.getServerEnvironment();

        return serverInfoVO;
    }
}
