package com.giggalpeople.backoffice.api.server.model.dto.response;

import lombok.Data;

/**
 * <h2><b>기깔나는 사람들 WAS 상세 정보 조회 관련 응답 DTO</b></h2>
 */

@Data
public class ServerInfoDetailResponseDTO {
    private Long internalServerID;
    private String serverName;
    private String serverVmInfo;
    private String serverOsInfo;
    private String serverIP;
    private String serverEnvironment;
}
