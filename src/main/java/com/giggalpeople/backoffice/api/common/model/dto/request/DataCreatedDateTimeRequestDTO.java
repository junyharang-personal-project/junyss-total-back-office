package com.giggalpeople.backoffice.api.common.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class DataCreatedDateTimeRequestDTO {
    private String dataCreatedDate;
    private String dataCreatedTime;

    @Builder
    public DataCreatedDateTimeRequestDTO(String createdDate, String createdTime) {
        this.dataCreatedDate = createdDate;
        this.dataCreatedTime = createdTime;
    }
}
