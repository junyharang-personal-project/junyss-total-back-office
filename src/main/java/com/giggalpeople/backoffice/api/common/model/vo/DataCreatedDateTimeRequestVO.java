package com.giggalpeople.backoffice.api.common.model.vo;

import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDTO;
import lombok.Getter;

@Getter
public class DataCreatedDateTimeRequestVO {
    private Long dataCreatedDateTimeID;
    private String dataCreatedDate;
    private String dataCreatedTime;

    public static DataCreatedDateTimeRequestVO toVO (DataCreatedDateTimeRequestDTO dataCreatedDateTimeRequestDTO) {
        DataCreatedDateTimeRequestVO dataCreatedDateTimeRequestVO = new DataCreatedDateTimeRequestVO();

        dataCreatedDateTimeRequestVO.dataCreatedDate = dataCreatedDateTimeRequestDTO.getDataCreatedDate();
        dataCreatedDateTimeRequestVO.dataCreatedTime = dataCreatedDateTimeRequestDTO.getDataCreatedTime();

        return dataCreatedDateTimeRequestVO;
    }
}