package com.giggalpeople.backoffice.api.common.database.dao.impl;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

import org.springframework.stereotype.Repository;

import com.giggalpeople.backoffice.api.common.database.dao.OccurrenceDataDateTimeManagementDAO;
import com.giggalpeople.backoffice.api.common.database.mapper.OccurrenceDataDateTimeManagementMapper;
import com.giggalpeople.backoffice.api.common.exception.OccurrenceDateTimeException;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeRequestVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class OccurrenceDataDateTimeManagementDAOImpl implements OccurrenceDataDateTimeManagementDAO {

	private final OccurrenceDataDateTimeManagementMapper occurrenceDataDateTimeManagementMapper;

	/**
	 * <b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보를 각 Table에 저장할 때, 동일한 날짜와 시간이 중복 저장 되어 날짜와 시간 관련 Table 분리하기 위한 Method</b>
	 * @param dataCreatedDateTimeRequestVO Error Log 발생 혹은 이용자 요청 시 날짜와 시각 정보를 담은 VO
	 * @return Data Base 저장 뒤 해당 정보 순서 번호
	 */

	@Override
	public Long save(DataCreatedDateTimeRequestVO dataCreatedDateTimeRequestVO) {
		Long saveCount = occurrenceDataDateTimeManagementMapper.save(dataCreatedDateTimeRequestVO);
		if (saveCount != null && saveCount != 0) {
			return dataCreatedDateTimeRequestVO.getDataCreatedDateTimeID();
		}
		throw new OccurrenceDateTimeException(COMMON_DATA_DATE_TIME_SAVE_FAILURE,
			COMMON_DATA_DATE_TIME_SAVE_FAILURE.getMessage());
	}

	/**
	 * <b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보 등의 동일 날짜, 시각이 저장 되어 있는지 확인하기 위한 Method</b>
	 * @param createdDate Error Log 발생 혹은 이용자 요청 시 날짜
	 * @return Data Base 저장 뒤 해당 정보 순서 번호
	 */

	@Override
	public Long findByOccurrenceInfoDateTime(String createdDate, String createdDateTime) {
		return occurrenceDataDateTimeManagementMapper.findByOccurrenceInfoDate(createdDate, createdDateTime);
	}
}
