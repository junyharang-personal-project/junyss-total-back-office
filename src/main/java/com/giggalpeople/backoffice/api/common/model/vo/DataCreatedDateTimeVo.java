package com.giggalpeople.backoffice.api.common.model.vo;

import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataCreatedDateTimeVo {
	private Long dataCreatedDateTimeID;
	private String dataCreatedDate;
	private String dataCreatedTime;

	@Builder
	public DataCreatedDateTimeVo(Long dataCreatedDateTimeID, String dataCreatedDate, String dataCreatedTime) {
		this.dataCreatedDateTimeID = dataCreatedDateTimeID;
		this.dataCreatedDate = dataCreatedDate;
		this.dataCreatedTime = dataCreatedTime;
	}

	public static DataCreatedDateTimeVo toVO(DataCreatedDateTimeRequestDto dataCreatedDateTimeRequestDTO) {
		DataCreatedDateTimeVo dataCreatedDateTimeVO = new DataCreatedDateTimeVo();

		dataCreatedDateTimeVO.dataCreatedDate = dataCreatedDateTimeRequestDTO.getDataCreatedDate();
		dataCreatedDateTimeVO.dataCreatedTime = dataCreatedDateTimeRequestDTO.getDataCreatedTime();

		return dataCreatedDateTimeVO;
	}
}