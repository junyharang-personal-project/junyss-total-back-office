package com.giggalpeople.backoffice.api.user.request_info.model.vo;

import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDTO;
import lombok.Getter;

@Getter
public class UserRequestInfoVO {
    private Long connectedUserRequestInfoID;
    private Long internalServerID;
    private Long dataCreatedDateTimeID;
    private Long connectedUserID;
    private Long connectedUserSameRequestCount;
    private String requestHeader;
    private String userCookies;
    private String requestParameter;
    private String requestBody;

    public static UserRequestInfoVO toVO (ConnectedUserRequestInfoSaveRequestDTO connectedUserRequestInfoSaveRequestDTO) {
        UserRequestInfoVO userRequestInfoVO = new UserRequestInfoVO();

        userRequestInfoVO.internalServerID = connectedUserRequestInfoSaveRequestDTO.getInternalServerID();
        userRequestInfoVO.dataCreatedDateTimeID = connectedUserRequestInfoSaveRequestDTO.getDataCreatedDateTimeID();
        userRequestInfoVO.connectedUserID = connectedUserRequestInfoSaveRequestDTO.getConnectedUserID();
        userRequestInfoVO.requestHeader = connectedUserRequestInfoSaveRequestDTO.getRequestHeader();
        userRequestInfoVO.userCookies = connectedUserRequestInfoSaveRequestDTO.getUserCookies();
        userRequestInfoVO.requestParameter = connectedUserRequestInfoSaveRequestDTO.getRequestParameter();
        userRequestInfoVO.requestBody = connectedUserRequestInfoSaveRequestDTO.getRequestBody();

        return userRequestInfoVO;
    }
}
