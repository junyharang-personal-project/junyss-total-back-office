package com.giggalpeople.backoffice.api.user.model;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserInfo {
    private Long connectedUserID;
    private Long dataCreatedDateTimeID;

    @Builder
    public UpdateUserInfo(Long connectedUserID, Long dataCreatedDateTimeID) {
        this.connectedUserID = connectedUserID;
        this.dataCreatedDateTimeID = dataCreatedDateTimeID;
    }
}
