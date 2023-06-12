package com.giggalpeople.backoffice.api.common.database.dao.impl;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

import org.springframework.stereotype.Repository;

import com.giggalpeople.backoffice.api.common.database.dao.OccurrenceDataDateTimeManagementDao;
import com.giggalpeople.backoffice.api.common.database.mapper.OccurrenceDataDateTimeManagementMapper;
import com.giggalpeople.backoffice.api.common.exception.OccurrenceDateTimeException;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;

import lombok.RequiredArgsConstructor;

/**
 * <h2><b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보를 각 Table에 저장할 때, 동일한 날짜와 시간이 중복 저장 되어 날짜와 시간 관련 Table 분리하기 위한 Class</b></h2>
 */
@RequiredArgsConstructor
@Repository
public class OccurrenceDataDateTimeManagementDaoImpl implements OccurrenceDataDateTimeManagementDao {

	private final OccurrenceDataDateTimeManagementMapper occurrenceDataDateTimeManagementMapper;

	/**
	 * <b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보를 각 Table에 저장할 때, 동일한 날짜와 시간이 중복 저장 되어 날짜와 시간 관련 Table 분리하기 위한 Method</b>
	 * @param dataCreatedDateTimeVO Error Log 발생 혹은 이용자 요청 시 날짜와 시각 정보를 담은 VO
	 * @return Data Base 저장 뒤 해당 정보 순서 번호
	 */

	@Override
	public Long save(DataCreatedDateTimeVo dataCreatedDateTimeVO) {
		Long saveCount = occurrenceDataDateTimeManagementMapper.save(dataCreatedDateTimeVO);
		if (saveCount != null && saveCount != 0) {
			return dataCreatedDateTimeVO.getDataCreatedDateTimeID();
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
		return occurrenceDataDateTimeManagementMapper.findByOccurrenceInfoDateTime(createdDate, createdDateTime);
	}
}
