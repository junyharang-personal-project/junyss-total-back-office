package com.giggalpeople.backoffice.api.user.model.dto.request;

import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDTO;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDTO;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import lombok.Builder;
import lombok.Data;

@Data
public class UserRequestTotalInfoSaveRequestDTO {

    private ServerInfo serverInfo;
    private DataCreatedDateTimeRequestDTO dataCreatedDateTimeRequestDTO;
    private ConnectedUserInfoSaveRequestDTO connectedUserInfoSaveRequestDTO;
    private ConnectedUserRequestInfoSaveRequestDTO connectedUserRequestInfoSaveRequestDTO;

    @Builder
    public UserRequestTotalInfoSaveRequestDTO(ServerInfo serverInfo,
                                              DataCreatedDateTimeRequestDTO dataCreatedDateTimeRequestDTO,
                                              ConnectedUserInfoSaveRequestDTO connectedUserInfoSaveRequestDTO,
                                              ConnectedUserRequestInfoSaveRequestDTO connectedUserRequestInfoSaveRequestDTO) {

        this.serverInfo = serverInfo;
        this.dataCreatedDateTimeRequestDTO = dataCreatedDateTimeRequestDTO;
        this.connectedUserInfoSaveRequestDTO = connectedUserInfoSaveRequestDTO;
        this.connectedUserRequestInfoSaveRequestDTO = connectedUserRequestInfoSaveRequestDTO;
    }
}
