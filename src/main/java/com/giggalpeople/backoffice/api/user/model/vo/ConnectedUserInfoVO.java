package com.giggalpeople.backoffice.api.user.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConnectedUserInfoVO {
    private Long connectedUserRequestInfoID;
    private String dataCreatedDate;
    private String dataCreatedTime;
    private String serverName;
    private String serverVmInfo;
    private String serverOSInfo;
    private String serverIP;
    private String serverEnvironment;
    private String userIP;
    private String userEnvironment;
    private String userLocation;
    private String requestHeader;
    private String userCookies;
    private String requestParameter;
    private String requestBody;

    public ConnectedUserInfoVO(Long connectedUserRequestInfoID,
                               String dataCreatedDate,
                               String dataCreatedTime,
                               String serverName,
                               String serverVmInfo,
                               String serverOSInfo,
                               String serverIP,
                               String serverEnvironment,
                               String userIP,
                               String userEnvironment,
                               String userLocation,
                               String requestHeader,
                               String userCookies,
                               String requestParameter,
                               String requestBody) {

        this.connectedUserRequestInfoID = connectedUserRequestInfoID;
        this.dataCreatedDate = dataCreatedDate;
        this.dataCreatedTime = dataCreatedTime;
        this.serverName = serverName;
        this.serverVmInfo = serverVmInfo;
        this.serverOSInfo = serverOSInfo;
        this.serverIP = serverIP;
        this.serverEnvironment = serverEnvironment;
        this.userIP = userIP;
        this.userEnvironment = userEnvironment;
        this.userLocation = userLocation;
        this.requestHeader = requestHeader;
        this.userCookies = userCookies;
        this.requestParameter = requestParameter;
        this.requestBody = requestBody;
    }
}
