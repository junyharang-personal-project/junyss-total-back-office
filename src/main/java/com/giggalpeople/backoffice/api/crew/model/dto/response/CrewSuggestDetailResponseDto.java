package com.giggalpeople.backoffice.api.crew.model.dto.response;

import java.time.LocalDate;

import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.WhetherType;
import com.giggalpeople.backoffice.api.crew.model.vo.SuggestInfoVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>크루 지원자 상세 조회를 위한 정보 담은 DTO</b></h2>
 */
@Schema(description = "크루 지원자 상세 조회를 위한 정보 담은 DTO")
@Data
public class CrewSuggestDetailResponseDto {
	@Schema(description = "크루 번호", nullable = false, example = "20220000000")
	private String crewNumber;

	@Schema(description = "지원서 등록 일시", nullable = false, example = "2022. 8. 7 오후 11:48:20")
	private LocalDate suggestDate;

	@Schema(description = "기깔나는 사람들을 알게 된 경로 정보", nullable = false, example = "인프런")
	private String howKnowInfo;

	@Schema(description = "지원자 이름", nullable = false, example = "홍길동")
	private String name;

	@Schema(description = "지원자 성별", nullable = false, example = "남자 or 여")
	private String sex;

	@Schema(description = "지원자 Email 주소", nullable = false, example = "abc3939@gmail.com")
	private String email;

	@Schema(description = "크루 생년월일", nullable = false, example = "1990. 1. 14")
	private LocalDate birthDate;

	@Schema(description = "지원자 나이", nullable = false, example = "34")
	private String ageInfo;

	@Schema(description = "지원자 MBTI", nullable = false, example = "ISTP")
	private String mbti;

	@Schema(description = "지원 성격 상세", nullable = false, example = "안녕하세요? 저의 성격은...")
	private String personality;

	@Schema(description = "지원자 현재 직업", nullable = false, example = "BackEnd 개발자")
	private String jobInfo;

	@Schema(description = "지원자 최종 학력", nullable = false, example = "4년대 졸업")
	private String lastEducational;

	@Schema(description = "지원자 최종 학력 학교 이름", nullable = false, example = "서울대학교")
	private String schoolName;

	@Schema(description = "지원자 거주지 주변 역 이름", nullable = false, example = "서울역")
	private String stationName;

	@Schema(description = "지원자 회사명", nullable = false, example = "삼성전자")
	private String companyName;

	@Schema(description = "지원자 핸드폰 번호", nullable = false, example = "010-000-0000")
	private String phoneNumber;

	@Schema(description = "티스토리 계정", nullable = false, example = "abc3939@gmail.com")
	private String tistory;

	@Schema(description = "피그마 계정", nullable = false, example = "abc3939@gmail.com")
	private String figma;

	@Schema(description = "지원자 노션 계정", nullable = false, example = "abc3939@gmail.com")
	private String notion;

	@Schema(description = "개인 블로그 주소", nullable = false, example = "https://giggal-people.tistory.com/")
	private String blogUrl;

	@Schema(description = "지원자 합류하고자 하는 Part", nullable = false, example = "WEB / APP 디자이너")
	private String suggestPart;

	@Schema(description = "기술력 상세", nullable = false)
	private String techStack;

	@Schema(description = "지원자 경력 상세", example = "안녕하세요? 저는...")
	private String career;

	@Schema(description = "지원자 Git Hub 주소", example = "https://github.com/abc")
	private String githubAddress;

	@Schema(description = "지원자 포트폴리오", example = "https://github.com/abc")
	private String portfolio;

	@Schema(description = "개인정보 수집 동의 여부", nullable = false, example = "동의 합니다. 혹은 동의하지 않습니다.")
	private boolean privacyInfoAgree;

	@Schema(description = "추가 사항 동의 여부", nullable = false, example = "동의 합니다. 혹은 동의하지 않습니다.")
	private boolean addendumInfoAgree;

	@Schema(description = "지원자 추가로 하고 싶은 말", example = "제가 합류하게 된다면...")
	private String etc;

	@Schema(description = "지원자 참여 여부 상태", example = "대표 합류 거부")
	private String participation;

	@Schema(description = "지원자 대면 면담 일자", example = "2022년 09월 25일(일) 20시 00분")
	private LocalDate meetDate;

	@Schema(description = "지원자 음주 여부", example = "소주 기준 1병 미만")
	private String drinking;

	@Schema(description = "지원자 취미", example = "게임")
	private String hobby;

	@Schema(description = "지원자 개인 활동 상세", example = "저의 일주일 활동은 ...")
	private String personalClassInfo;

	@Schema(description = "지원자 연애 여부", example = "NO")
	private WhetherType loveWhether;

	@Schema(description = "비고", example = "대면 면담일에 약속 시간 늦어 탈락!")
	private String note;

	/**
	 * <b>크루 합류를 위한 지원자 Data Base 지원 정보 VO를 Client에게 반환하기 위해 DTO로 변환하는 Method</b>
	 * @param suggestInfoVO Data Base 작성된 지원자 정보
	 * @return CrewSuggestDetailResponseDTO - Client 상세 조회 요청의 반환하기 위한 지원자 정보
	 */

	public static CrewSuggestDetailResponseDto toDTO(SuggestInfoVo suggestInfoVO) {
		CrewSuggestDetailResponseDto crewSuggestDetailResponseDTO = new CrewSuggestDetailResponseDto();

		crewSuggestDetailResponseDTO.crewNumber = suggestInfoVO.getCrewNumber();
		crewSuggestDetailResponseDTO.suggestDate = suggestInfoVO.getSuggestDate();
		crewSuggestDetailResponseDTO.howKnowInfo = suggestInfoVO.getHowKnowInfo();
		crewSuggestDetailResponseDTO.name = suggestInfoVO.getName();
		crewSuggestDetailResponseDTO.sex = suggestInfoVO.getSex();
		crewSuggestDetailResponseDTO.email = suggestInfoVO.getEmail();
		crewSuggestDetailResponseDTO.birthDate = suggestInfoVO.getBirthDate();
		crewSuggestDetailResponseDTO.ageInfo = suggestInfoVO.getAgeInfo();
		crewSuggestDetailResponseDTO.mbti = suggestInfoVO.getMbti();
		crewSuggestDetailResponseDTO.personality = suggestInfoVO.getPersonality();
		crewSuggestDetailResponseDTO.jobInfo = suggestInfoVO.getJobInfo();
		crewSuggestDetailResponseDTO.lastEducational = suggestInfoVO.getLastEducational();
		crewSuggestDetailResponseDTO.schoolName = suggestInfoVO.getSchoolName();
		crewSuggestDetailResponseDTO.stationName = suggestInfoVO.getStationName();
		crewSuggestDetailResponseDTO.companyName = suggestInfoVO.getCompanyName();
		crewSuggestDetailResponseDTO.phoneNumber = suggestInfoVO.getPhoneNumber();
		crewSuggestDetailResponseDTO.tistory = suggestInfoVO.getTistory();
		crewSuggestDetailResponseDTO.figma = suggestInfoVO.getFigma();
		crewSuggestDetailResponseDTO.notion = suggestInfoVO.getNotion();
		crewSuggestDetailResponseDTO.blogUrl = suggestInfoVO.getBlogUrl();
		crewSuggestDetailResponseDTO.suggestPart = suggestInfoVO.getSuggestPart();
		crewSuggestDetailResponseDTO.techStack = suggestInfoVO.getTechStack();
		crewSuggestDetailResponseDTO.career = suggestInfoVO.getCareer();
		crewSuggestDetailResponseDTO.githubAddress = suggestInfoVO.getGithubAddress();

		if (suggestInfoVO.getPortfolio() != null) {
			crewSuggestDetailResponseDTO.portfolio = suggestInfoVO.getPortfolio();
		}

		crewSuggestDetailResponseDTO.privacyInfoAgree = suggestInfoVO.isPrivacyInfoAgree();
		crewSuggestDetailResponseDTO.addendumInfoAgree = suggestInfoVO.isAddendumInfoAgree();

		if (suggestInfoVO.getEtc() != null) {
			crewSuggestDetailResponseDTO.etc = suggestInfoVO.getEtc();
		}

		crewSuggestDetailResponseDTO.participation = suggestInfoVO.getParticipation();
		crewSuggestDetailResponseDTO.meetDate = suggestInfoVO.getMeetDate();
		crewSuggestDetailResponseDTO.drinking = suggestInfoVO.getDrinkingInfo();
		crewSuggestDetailResponseDTO.hobby = suggestInfoVO.getHobby();
		crewSuggestDetailResponseDTO.personalClassInfo = suggestInfoVO.getPersonalClassInfo();

		if (suggestInfoVO.getLoveWhether() == 0) {
			crewSuggestDetailResponseDTO.loveWhether = WhetherType.NO;
		} else if (suggestInfoVO.getLoveWhether() == 1) {
			crewSuggestDetailResponseDTO.loveWhether = WhetherType.YES;
		} else {
			crewSuggestDetailResponseDTO.loveWhether = WhetherType.UNKNOWN;
		}

		crewSuggestDetailResponseDTO.note = suggestInfoVO.getNote();

		return crewSuggestDetailResponseDTO;
	}
}
