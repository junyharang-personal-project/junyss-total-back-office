package com.giggalpeople.backoffice.api.crew.model.dto.request.common;

import java.time.LocalDate;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.giggalpeople.backoffice.common.util.DataTypeChangerUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>검색을 위한 검색 관련 공통 사항 분리 DTO</b></h2>
 */

@Schema(description = "합류 크루 검색을 위한 검색어 DTO")
@Data
public class PeopleManagementSearchDTO {

	@Schema(description = "검색어", nullable = true, example = "홍길동")
	private String searchWord;

	@JsonIgnore
	private boolean inDBPrivacyInfoAgree;

	@JsonIgnore
	private boolean inDBAddendumInfoAgree;

	@Schema(description = "날짜 범위 검색을 위한 시작 날짜", nullable = true, example = "2023-03-01")
	@Pattern(regexp = ".*(\\d{4})-(\\d{2})-(\\d{2})")
	private String startDate;

	@JsonIgnore
	private LocalDate inDBStartDate;

	@Schema(description = "날짜 범위 검색을 위한 끝 날짜", nullable = true, example = "2023-03-01")
	@Pattern(regexp = ".*(\\d{4})-(\\d{2})-(\\d{2})")
	private String endDate;

	@JsonIgnore
	private LocalDate inDBEndDate;

	@Schema(description = "날짜 하나 검색 위한 날짜", nullable = true, example = "2023-03-01")
	@Pattern(regexp = ".*(\\d{4})-(\\d{2})-(\\d{2})")
	private String date;

	@JsonIgnore
	private LocalDate inDBDate;

	@Schema(description = "나이 범위 검색 위한 시작 나이", nullable = true, example = "20")
	@Pattern(regexp = ".*(\\d{2})")
	private String startAge;

	@JsonIgnore
	private int inDBStartAge;

	@Schema(description = "나이 범위 검색 위한 끝 나이", nullable = true, example = "30")
	@Pattern(regexp = ".*(\\d{2})")
	private String endAge;

	@JsonIgnore
	private int inDBEndAge;

	@Schema(description = "나이 하나 검색을 위한 검색어", nullable = true, example = "20")
	@Pattern(regexp = ".*(\\d{2})")
	private String age;

	@JsonIgnore
	private int inDBAge;

	/**
	 * <b>MyBatis에 Mapping하기 위한 검색 Type(검색 조건)</b>
	 */
	@JsonIgnore
	private String searchType;

	/**
	 * <b>Client가 파트 조회 시 여러 검색어를 DB에서 검색할 검색어로 바꿔주기 위한 Getter</b>
	 *
	 * @return DB 검색어로 변경된 파트 명
	 */
	public String getSearchWord() {
		if (this.searchType != null && this.searchType.equals("SUGGEST_PART")) {
			return DataTypeChangerUtil.changeSearchWordForPart(searchWord);
		} else {
			return searchWord;
		}
	}

	/**
	 * <b>Client에서 날짜 범위 검색을 위해 시작 날짜 문자열 'yyyy-MM-dd' 형식으로 보냈을 때, 이를 날짜 형식으로 변경하기 위한 Setter</b>
	 *
	 * @param startDate Client에서 날짜 검색을 위해 문자열 'yyyy-MM-dd' 형식
	 */

	public void setStartDate(String startDate) {
		this.inDBStartDate = LocalDate.parse(startDate);
		this.startDate = startDate;
	}

	/**
	 * <b>Client에서 날짜 범위 검색을 위해 끝 날짜 문자열 'yyyy-MM-dd' 형식으로 보냈을 때, 이를 날짜 형식으로 변경하기 위한 Setter</b>
	 *
	 * @param endDate Client에서 날짜 검색을 위해 문자열 'yyyy-MM-dd' 형식
	 */

	public void setEndDate(String endDate) {
		this.inDBEndDate = LocalDate.parse(endDate);
		this.endDate = endDate;
	}

	/**
	 * <b>Client에서 하나의 날짜 검색을 위해 끝 날짜 문자열 'yyyy-MM-dd' 형식으로 보냈을 때, 이를 날짜 형식으로 변경하기 위한 Setter</b>
	 *
	 * @param date Client에서 하나의 날짜 검색을 위해 문자열 'yyyy-MM-dd' 형식
	 */

	public void setDate(String date) {
		this.inDBDate = LocalDate.parse(date);
		this.date = date;
	}

	/**
	 * <b>Client에서 나이 범위 검색을 위해 시작 나이 문자열 'dd' 형식으로 보냈을 때, 이를 정수로 변경하기 위한 Setter</b>
	 *
	 * @param startAge Client에서 나이 검색을 위해 문자열 'dd' 형식
	 */

	public void setStartAge(String startAge) {
		this.inDBStartAge = Integer.parseInt(startAge);
		this.startAge = startAge;
	}

	/**
	 * <b>Client에서 나이 범위 검색을 위해 끝 나이 문자열 'dd' 형식으로 보냈을 때, 이를 정수로 변경하기 위한 Setter</b>
	 *
	 * @param endAge Client에서 나이 검색을 위해 문자열 'dd' 형식
	 */

	public void setEndAge(String endAge) {
		this.inDBEndAge = Integer.parseInt(endAge);
		this.endAge = endAge;
	}

	/**
	 * <b>Client에서 나이 하나 검색을 위해 나이 문자열 'dd' 형식으로 보냈을 때, 이를 정수로 변경하기 위한 Setter</b>
	 *
	 * @param age Client에서 나이 검색을 위해 문자열 'dd' 형식
	 */

	public void setAge(String age) {
		this.inDBAge = Integer.parseInt(age);
		this.age = age;
	}
}
