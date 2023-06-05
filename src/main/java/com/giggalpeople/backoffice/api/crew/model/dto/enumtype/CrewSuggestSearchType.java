package com.giggalpeople.backoffice.api.crew.model.dto.enumtype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CrewSuggestSearchType {

	@Schema(description = "지원서 접수 일시", nullable = true, example = "2023. 1. 15 오후 8:28:07")
	SUGGEST_DATE("SUGGEST_DATE", "지원날짜"),
	@Schema(description = "우리를 알게 된 경로", nullable = true, example = "인프런")
	HOW_KNOW_INFO("HOW_KNOW_INFO", "검색경로"),
	@Schema(description = "이름", nullable = true, example = "홍길동")
	NAME("NAME", "이름"),
	@Schema(description = "성별", nullable = true, example = "남자")
	SEX("SEX", "성별"),
	@Schema(description = "생년월일", nullable = true, example = "1990.03.27")
	BIRTH_DATE("BIRTH_DATE", "생년월일"),
	@Schema(description = "나이", nullable = true, example = "34")
	AGE_INFO("AGE_INFO", "나이"),
	@Schema(description = "MBTI", nullable = true, example = "ISTP")
	MBTI("MBTI", "mbti"),
	@Schema(description = "학력", nullable = true, example = "4년제 졸업")
	LAST_EDUCATIONAL("LAST_EDUCATIONAL", "학력"),
	@Schema(description = "최종 학력 학교 이름", nullable = true, example = "서울 대학교")
	SCHOOL_NAME("SCHOOL_NAME", "최종학력학교"),
	@Schema(description = "거주지 주변 역", nullable = true, example = "서울역")
	STATION_NAME("STATION_NAME", "거주지주변역"),
	@Schema(description = "지원 파트", nullable = true, example = "BackEnd 개발")
	SUGGEST_PART("SUGGEST_PART", "지원파트"),
	@Schema(description = "개인 정보 수집 동의(필수)", nullable = true, example = "동의 or 미동의")
	PRIVACY_INFO_AGREE("PRIVACY_INFO_AGREE", "개인정보수집동의여부"),
	@Schema(description = "추가 사항 동의(필수)", nullable = true, example = "동의 or 미동의")
	ADDENDUM_INFO_AGREE("ADDENDUM_INFO_AGREE", "추가사항동의여부"),
	@Schema(description = "합류 상태", nullable = true, example = "검토 중")
	PARTICIPATION("PARTICIPATION", "합류상태"),
	@Schema(description = "대표 대면 만남일", nullable = true, example = "2023년 01월 05일 (금) 19시 00분")
	MEET_DATE("MEET_DATE", "대면만남일");

	private String description;
	private String searchCommand;
}
