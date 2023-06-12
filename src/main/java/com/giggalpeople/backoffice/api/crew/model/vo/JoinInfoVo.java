package com.giggalpeople.backoffice.api.crew.model.vo;

import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewJoinRequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2><b>기깔나는 사람들 크루 합류 인원 내부 정보 VO</b></h2>
 */

@Schema(description = "크루 합류 인원 내부 정보를 담은 Value Object")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinInfoVo extends SuggestInfoVo {

	@Schema(description = "지원 순서", nullable = false, example = "0032")
	private String suggestId;

	@Schema(description = "크루 합류 순서", nullable = false, example = "1")
	private Long crewJoinId;

	@Schema(description = "합류 일자", nullable = false, example = "2022년 09월 25일(일) 합류")
	private String joinDate;

	@Schema(description = "크루 내부 시스템 계정 (ID)", nullable = false, example = "u20220000000")
	private String userId;

	@Schema(description = "크루 내부 시스템 계정 (Password)", nullable = false, example = "Giggal123")
	private String password;

	@Schema(description = "크루 직책(역할)", nullable = false, example = "해당 사항 없음")
	private String crewRole;

	@Schema(description = "크루 별칭", nullable = false, example = "[대표&BackEnd]준이")
	private String crewAlias;

	/**
	 * <b>크루 합류 지원서가 작성되면 해당 인원 정보를 담은 DTO를 VO로 변환하기 위한 Method</b>
	 * @param crewJoinRequestDTO Client에서 보낸 합류 지원자의 내부 정보
	 * @param suggestId Client에서 보낸 지원자의 지원 순서 번호
	 * @return CrewJoinInfoVO - Data Base에 Mapping할 Value Object
	 */
	public static JoinInfoVo toVO(CrewJoinRequestDto crewJoinRequestDTO, String suggestId) {
		JoinInfoVo crewJoinInfoVo = new JoinInfoVo();

		crewJoinInfoVo.suggestId = suggestId;
		crewJoinInfoVo.joinDate = crewJoinRequestDTO.getJoinDate();
		crewJoinInfoVo.userId = crewJoinRequestDTO.getUserId();
		crewJoinInfoVo.crewRole = crewJoinRequestDTO.getCrewRole();
		crewJoinInfoVo.crewAlias = crewJoinRequestDTO.getCrewAlias();
		crewJoinInfoVo.password = crewJoinRequestDTO.getPassword();

		return crewJoinInfoVo;
	}
}