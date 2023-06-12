package com.giggalpeople.backoffice.api.common.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;

@Mapper
@Repository
public interface OccurrenceDataDateTimeManagementMapper {

	/**
	 * <b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보를 각 Table에 저장할 때, 동일한 날짜와 시간이 중복 저장 되어 날짜와 시간 관련 Table 분리하기 위한 Method</b>
	 * @param dataCreatedDateTimeVO Error Log 발생 혹은 이용자 요청 시 날짜와 시각 정보를 담은 VO
	 * @return Data Base 저장 뒤 해당 정보 순서 번호
	 */

	Long save(DataCreatedDateTimeVo dataCreatedDateTimeVO);

	/**
	 * <b>Error Log 발생 혹은 이용자 요청 시 이용자 정보, 이용자 요청 정보, Error Log 정보를 각 Table에 저장할 때, 동일한 날짜와 시간이 중복 저장 되어 날짜와 시간 관련 Table 분리하기 위한 Method</b>
	 * @param createdDate Error Log 발생 혹은 이용자 요청 시 날짜
	 * @return Data Base 저장 뒤 해당 정보 순서 번호
	 */

	@Select("select max(data_created_date_time_id) from data_created_date_time where data_created_date = #{createdDate} and data_created_time = #{createdTime}")
	Long findByOccurrenceInfoDateTime(@Param("createdDate") String createdDate,
		@Param("createdTime") String createdTime);
}
