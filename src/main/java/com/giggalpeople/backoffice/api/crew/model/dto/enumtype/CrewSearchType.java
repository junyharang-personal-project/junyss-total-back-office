package com.giggalpeople.backoffice.api.crew.model.dto.enumtype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CrewSearchType {
	@Schema(description = "지원서 접수 일시", nullable = true, example = "yyyy년MM월dd일 HH시MM분")
	SUGGEST_DATE("SUGGEST_DATE", "접수날짜"),
	@Schema(description = "지원 순서", nullable = true, example = "1")
	CREW_SUGGET_ID("CREW_SUGGET_ID", "지원순서"),
	@Schema(description = "합류 순서", nullable = true, example = "1")
	CREW_JOIN_ID("CREW_JOIN_ID", "합류순서"),
	@Schema(description = "합류 일자", nullable = true, example = "2022년 09월 29일(목)  합류")
	JOIN_DATE("JOIN_DATE", "합류날짜"),
	@Schema(description = "우리를 알게 된 경로", nullable = true, example = "인프런")
	HOW_KNOW_INFO("HOW_KNOW_INFO", "검색경로"),
	@Schema(description = "크루 번호", nullable = true, example = "2023-000-0001")
	CREW_NUMBER("CREW_NUMBER", "크루번호"),
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
	@Schema(description = "대표 대면 만남일", nullable = true, example = "2023년 01월 05일 (금) 19시 00분")
	MEET_DATE("MEET_DATE", "대면만남일");

	private String description;
	private String searchCommand;
}
