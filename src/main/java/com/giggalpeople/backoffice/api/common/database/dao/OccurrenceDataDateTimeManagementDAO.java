package com.giggalpeople.backoffice.api.common.database.dao;

import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeRequestVO;

public interface OccurrenceDataDateTimeManagementDAO {

    /**
     * <b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보를 각 Table에 저장할 때, 동일한 날짜와 시간이 중복 저장 되어 날짜와 시간 관련 Table 분리하기 위한 Method</b>
     * @param dataCreatedDateTimeRequestVO Error Log 발생 혹은 이용자 요청 시 날짜와 시각 정보를 담은 VO
     * @return Data Base 저장 뒤 해당 정보 순서 번호
     */

    Long save(DataCreatedDateTimeRequestVO dataCreatedDateTimeRequestVO);


    /**
     * <b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보 등의 동일 날짜, 시각이 저장 되어 있는지 확인하기 위한 Method</b>
     * @param createdDate Error Log 발생 혹은 이용자 요청 시 날짜
     * @return Data Base 저장 뒤 해당 정보 순서 번호
     */

    Long findByOccurrenceInfoDateTime(String createdDate, String createdDateTime);
}
