package com.giggalpeople.backoffice.api.crew.model.dto.request;

import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSearchType;
import com.giggalpeople.backoffice.api.crew.model.dto.request.common.PeopleManagementSearchDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h2><b>합류 크루 검색을 위한 검색어 DTO</b></h2>
 */

@EqualsAndHashCode(callSuper = true)
@Schema(description = "합류 크루 검색을 위한 검색어 DTO")
@Data
public class CrewPeopleManagementSearchDTO extends PeopleManagementSearchDTO {

	@Schema(description = "검색 조건(종류) - CREW_NUMBER: 크루번호, HOW_KNOW_INFO: 우리를 알게된 경로, NAME: 이름, SEX: 성별, MBTI: MBTI, LAST_EDUCATIONAL: 최종 학력, SCHOOL_NAME: 최종 학력 학교 이름, STATION_NAME: 거주지 주변 역, SUGGEST_PART: 지원 파트, MEET_DATE: 대면 만남일, SUGGEST_DATE: 지원 날짜, BIRTH_DATE: 생년월일, AGE_INFO: 나이",
		nullable = true, example = "NAME")
	private CrewSearchType inputSearchType;

	/**
	 * <b>Client에서 입력된 값을 Enum으로 처리한 뒤 해당 값을 알맞는 문자열로 바꿔 Data Base 처리 시 사용되게 하기 위한 Method</b>
	 * @return String Enum Type의 실제 Data Base 처리 시 사용될 문자열
	 */
	@Override
	public String getSearchType() {
		if (inputSearchType != null) {
			super.setSearchType(inputSearchType.getDescription());
			return this.inputSearchType.getDescription();
		} else if (super.getSearchType() != null) {
			return super.getSearchType();
		}
		return "";
	}
}