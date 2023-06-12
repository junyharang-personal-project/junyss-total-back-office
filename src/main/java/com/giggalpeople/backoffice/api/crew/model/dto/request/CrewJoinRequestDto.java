package com.giggalpeople.backoffice.api.crew.model.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.giggalpeople.backoffice.common.util.DataTypeChangerUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>크루 합류자 내부 정보 담은 DTO</b></h2>
 */
@Schema(description = "크루 합류자 내부 정보 담은 DTO")
@Data
public class CrewJoinRequestDto {

	@Schema(description = "크루 번호", nullable = false, example = "2022-000-0000")
	@NotBlank
	@Size(min = 1, max = 15)
	private String crewNumber;

	@Schema(description = "크루 합류 날짜", nullable = false, example = "2023년 01월 26일(목) 합류")
	@Size(max = 20)
	private String joinDate;

	@Schema(description = "크루 내부 시스템 계정(ID)", nullable = true, example = "u20221231234")
	@Size(max = 15)
	@Pattern(regexp = "^u+\\d{11}+$")
	private String userId;

	@Schema(description = "크루 내부 시스템 계정(Password)", nullable = true, example = "Giggal20220001234!#%")
	@Size(max = 30)
	private String password;

	@Schema(description = "크루 직책(역할)", nullable = false, example = "Team Leader(TL)")
	@Size(max = 20)
	private String crewRole;

	@Schema(description = "크루 별칭", nullable = false, example = "[대표&BackEnd]준")
	@Size(max = 20)
	private String crewAlias;

	/**
	 * <b>스프레드 시트에서 보기 편하게 작성한 합류 일자 정보를 조회하기 편하게 가공하기 위한 Setter</b>
	 * @param joinDate 스프레드 시트에서 보기 편하게 작성한 합류 일자
	 */

	public void setJoinDate(String joinDate) {
		this.joinDate = DataTypeChangerUtil.changeFormatDate(joinDate);
	}
}
