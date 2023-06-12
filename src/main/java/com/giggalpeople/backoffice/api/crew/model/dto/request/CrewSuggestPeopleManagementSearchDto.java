package com.giggalpeople.backoffice.api.crew.model.dto.request;

import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSuggestSearchType;
import com.giggalpeople.backoffice.api.crew.model.dto.request.common.PeopleManagementSearchDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h2><b>크루 지원자 검색을 위한 검색어 DTO</b></h2>
 */

@EqualsAndHashCode(callSuper = true)
@Schema(description = "크루 지원자 검색을 위한 검색어 DTO")
@Data
public class CrewSuggestPeopleManagementSearchDto extends PeopleManagementSearchDto {

	private CrewSuggestSearchType inputSearchType;

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