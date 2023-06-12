package com.giggalpeople.backoffice.api.record.model.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType;
import com.giggalpeople.backoffice.common.util.StringUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2><b>Log 검색을 위한 검색 DTO</b></h2>
 */

@Schema(description = "Log 검색을 위한 검색 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRecordSearchDto {

	@Schema(description = "검색 조건(종류) - LOG_ID: Log ID(생성 순번), " +
		"LOG_CREATE_DATE: Log 생성일, " +
		"LOG_LEVEL: Log Level, " +
		"SERVER_NAME: Server Name, " +
		"SERVER_IP: Server IP, " +
		"USER_IP: 이용자 IP 주소, " +
		"EXCEPTION_BRIEF: Exception 간략 내용",
		nullable = true, example = "SERVER_NAME")
	private ErrorRecordSearchType inputSearchType;

	/**
	 * <b>MyBatis에 Mapping하기 위한 검색 Type(검색 조건)</b>
	 */

	@JsonIgnore
	private String searchType;

	@Schema(description = "검색어 - 검색 종류가 내부 서버일 경우 내부 서버 종류로 검색 가능 : " +
		"1. 통합 관리 서버 = 통합관리서버, "
		, nullable = true, example = "192.168.0.3")
	private String searchWord;

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

	@Schema(description = "날짜 검색을 위한 끝 날짜", nullable = true, example = "2023-03-01")
	@Pattern(regexp = ".*(\\d{4})-(\\d{2})-(\\d{2})")
	private String date;

	@JsonIgnore
	private LocalDate inDBDate;

	/**
	 * <b>Client에서 날짜 범위 검색을 위해 시작 날짜 문자열 'yyyy-MM-dd' 형식으로 보냈을 때, 이를 날짜 형식으로 변경하기 위한 Setter</b>
	 * @param startDate Client에서 날짜 검색을 위해 문자열 'yyyy-MM-dd' 형식
	 */

	public void setStartDate(String startDate) {
		this.inDBStartDate = LocalDate.parse(startDate);
		this.startDate = startDate;
	}

	/**
	 * <b>Client에서 날짜 범위 검색을 위해 끝 날짜 문자열 'yyyy-MM-dd' 형식으로 보냈을 때, 이를 날짜 형식으로 변경하기 위한 Setter</b>
	 * @param endDate Client에서 날짜 검색을 위해 문자열 'yyyy-MM-dd' 형식
	 */

	public void setEndDate(String endDate) {
		this.inDBEndDate = LocalDate.parse(endDate);
		this.endDate = endDate;
	}

	/**
	 * <b>Client에서 날짜 하나 검색을 위해 날짜 문자열 'yyyy-MM-dd' 형식으로 보냈을 때, 이를 날짜 형식으로 변경하기 위한 Setter</b>
	 * @param date Client에서 날짜 검색을 위해 문자열 'yyyy-MM-dd' 형식
	 */

	public void setDate(String date) {
		this.inDBDate = LocalDate.parse(date);
		this.date = date;
	}

	/**
	 * <b>Client가 검색을 위해 한글로 검색어를 입력하면 Data Base 검색 조건에 맞게 변환하기 위한 Getter</b>
	 * @return Data Base 검색 조건에 맞게 변환한 검색어
	 */

	public String getSearchType() {

		if (inputSearchType != null && searchWord != null) {
			String serverNameSearchWord = StringUtil.checkSearchCommandForInternalServerName(
				inputSearchType.getDescription(), searchWord);

			if (!serverNameSearchWord.equals("")) {
				setSearchWord(serverNameSearchWord);
			}

			return putSearchTypeValue(inputSearchType);

		} else if (searchWord == null && inputSearchType != null) {
			return putSearchTypeValue(inputSearchType);
		}
		return "";
	}

	/**
	 * <b>getSearchType()에서 Server 조회가 아니라면 이용자가 요청한 Search Type 값을 넣어주기 위한 Method</b>
	 * @param inputSearchType 이용자가 요청한 검색 타입
	 * @return 이용자가 요청한 검색 타입을 Data Base에서 조회할 수 있는 값으로 바꾼 문자열
	 */
	private String putSearchTypeValue(ErrorRecordSearchType inputSearchType) {
		setSearchType(inputSearchType.getDescription());
		return this.inputSearchType.getDescription();
	}
}
