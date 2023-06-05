package com.giggalpeople.backoffice.api.crew.model.dto.response;

import java.time.LocalDate;

import com.giggalpeople.backoffice.api.crew.model.vo.JoinInfoVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * <h2><b>크루 상세 조회를 위한 정보 담은 DTO</b></h2>
 */
@Schema(description = "크루 상세 조회를 위한 정보 담은 DTO")
@Data
public class CrewDetailResponseDTO {
	@Schema(description = "지원 순서", nullable = false, example = "0032")
	private String suggestId;

	@Schema(description = "합류 순서", nullable = false, example = "5")
	private Long crewJoinId;

	@Schema(description = "기깔나는 사람들을 알게 된 경로 정보", nullable = false, example = "인프런")
	private String howKnowInfo;

	@Schema(description = "크루 번호", nullable = false, example = "2022-000-0000")
	private String crewNumber;

	@Schema(description = "지원서 등록 일시", nullable = false, example = "2022. 8. 7 오후 11:48:20")
	private LocalDate suggestDate;

	@Schema(description = "합류 일자", nullable = false, example = "2022년 09월 25일(일) 합류")
	private String joinDate;

	@Schema(description = "크루 이름", nullable = false, example = "홍길동")
	private String name;

	@Schema(description = "크루 핸드폰 번호", nullable = false, example = "010-000-0000")
	private String phoneNumber;

	@Schema(description = "크루 성별", nullable = false, example = "남자 or 여")
	private String sex;

	@Schema(description = "크루 Email 주소", nullable = false, example = "abc3939@gmail.com")
	private String email;

	@Schema(description = "크루 생년월일", nullable = false, example = "1990. 1. 14")
	private LocalDate birthDate;

	@Schema(description = "지원자 나이", nullable = false, example = "34")
	private String ageInfo;

	@Schema(description = "크루 MBTI", nullable = false, example = "ISTP")
	private String mbti;

	@Schema(description = "크루 내부 시스템 계정 (ID)", nullable = false, example = "u20220000000")
	private String userId;

	@Schema(description = "크루 직책(역할)", nullable = false, example = "해당 사항 없음")
	private String crewRole;

	@Schema(description = "크루 성격 상세", nullable = false, example = "안녕하세요? 저의 성격은...")
	private String personality;

	@Schema(description = "크루 현재 직업", nullable = false, example = "BackEnd 개발자")
	private String jobInfo;

	@Schema(description = "크루 최종 학력", nullable = false, example = "4년대 졸업")
	private String lastEducational;

	@Schema(description = "크루 최종 학력 학교 이름", nullable = false, example = "서울대학교")
	private String schoolName;

	@Schema(description = "크루 회사명", nullable = false, example = "삼성전자")
	private String companyName;

	@Schema(description = "티스토리 계정", nullable = false, example = "abc3939@gmail.com")
	private String tistory;

	@Schema(description = "피그마 계정", nullable = false, example = "abc3939@gmail.com")
	private String figma;

	@Schema(description = "크루 노션 계정", nullable = false, example = "abc3939@gmail.com")
	private String notion;

	@Schema(description = "개인 블로그 주소", nullable = false, example = "https://giggal-people.tistory.com/")
	private String blogUrl;

	@Schema(description = "크루 거주지 주변 역 이름", nullable = false, example = "서울역")
	private String stationName;

	@Schema(description = "크루 소속 Team", nullable = false, example = "WEB / APP 디자이너")
	private String suggestPart;

	@Schema(description = "기술력 상세", nullable = false)
	private String techStack;

	@Schema(description = "크루 경력 상세", example = "안녕하세요? 저는...")
	private String career;

	@Schema(description = "크루 Git Hub 주소", example = "https://github.com/abc")
	private String githubAddress;

	@Schema(description = "개인정보 수집 동의 여부", nullable = false, example = "동의 합니다. 혹은 동의하지 않습니다.")
	private boolean privacyInfoAgree;

	@Schema(description = "추가 사항 동의 여부", nullable = false, example = "동의 합니다. 혹은 동의하지 않습니다.")
	private boolean addendumInfoAgree;

	@Schema(description = "크루 포트폴리오", example = "https://drive.google.com/open?id=abc")
	private String portfolio;

	@Schema(description = "크루 추가로 하고 싶은 말", example = "제가 합류하게 된다면...")
	private String etc;

	@Schema(description = "지원자 참여 여부 상태", example = "대표 합류 거부")
	private String participation;

	@Schema(description = "대면 면담 일자", example = "2022년 09월 25일(일) 20시 00분")
	private LocalDate meetDate;

	@Schema(description = "지원자 연애 여부", example = "false")
	private boolean datingInfo;

	@Schema(description = "지원자 취미", example = "게임")
	private String hobby;

	@Schema(description = "지원자 개인 활동 상세", example = "저의 일주일 활동은 ...")
	private String personalClassInfo;

	@Schema(description = "지원자 음주 여부", example = "소주 기준 1병 이상 3병 이하")
	private String drinkInfo;

	@Schema(description = "비고", example = "대면 면담일에 약속 시간 늦어 탈락!")
	private String note;

	@Schema(description = "크루 별칭", nullable = false, example = "[대표&BackEnd]준이")
	private String crewAlias;

	/**
	 * <b>Data Base에 저장된 크루 정보를 해당 DTO로 변환하는 Method</b>
	 * @param crewJoinInfoVO 크루 합류가 결정된 인원에 대한 Data Base 저장 크루 정보
	 * @return CrewDetailResponseDTO - Client에게 반환하기 위한 크루 정보
	 */

	@Builder
	public static CrewDetailResponseDTO toDTO(JoinInfoVO crewJoinInfoVO) {
		CrewDetailResponseDTO crewDetailResponseDTO = new CrewDetailResponseDTO();

		crewDetailResponseDTO.suggestId = crewJoinInfoVO.getSuggestId();
		crewDetailResponseDTO.crewJoinId = crewJoinInfoVO.getCrewJoinId();
		crewDetailResponseDTO.crewNumber = crewJoinInfoVO.getCrewNumber();
		crewDetailResponseDTO.suggestDate = crewJoinInfoVO.getSuggestDate();
		crewDetailResponseDTO.joinDate = crewJoinInfoVO.getJoinDate();
		crewDetailResponseDTO.howKnowInfo = crewJoinInfoVO.getHowKnowInfo();
		crewDetailResponseDTO.name = crewJoinInfoVO.getName();
		crewDetailResponseDTO.sex = crewJoinInfoVO.getSex();
		crewDetailResponseDTO.email = crewJoinInfoVO.getEmail();
		crewDetailResponseDTO.birthDate = crewJoinInfoVO.getBirthDate();
		crewDetailResponseDTO.ageInfo = crewJoinInfoVO.getAgeInfo();
		crewDetailResponseDTO.mbti = crewJoinInfoVO.getMbti();
		crewDetailResponseDTO.userId = crewJoinInfoVO.getUserId();
		crewDetailResponseDTO.crewRole = crewJoinInfoVO.getCrewRole();
		crewDetailResponseDTO.privacyInfoAgree = crewJoinInfoVO.isPrivacyInfoAgree();
		crewDetailResponseDTO.personality = crewJoinInfoVO.getPersonality();
		crewDetailResponseDTO.addendumInfoAgree = crewJoinInfoVO.isAddendumInfoAgree();
		crewDetailResponseDTO.jobInfo = crewJoinInfoVO.getJobInfo();
		crewDetailResponseDTO.lastEducational = crewJoinInfoVO.getLastEducational();
		crewDetailResponseDTO.schoolName = crewJoinInfoVO.getSchoolName();
		crewDetailResponseDTO.stationName = crewJoinInfoVO.getStationName();
		crewDetailResponseDTO.phoneNumber = crewJoinInfoVO.getPhoneNumber();
		crewDetailResponseDTO.tistory = crewJoinInfoVO.getTistory();
		crewDetailResponseDTO.figma = crewJoinInfoVO.getFigma();
		crewDetailResponseDTO.notion = crewJoinInfoVO.getNotion();
		crewDetailResponseDTO.blogUrl = crewJoinInfoVO.getBlogUrl();
		crewDetailResponseDTO.portfolio = crewJoinInfoVO.getPortfolio();
		crewDetailResponseDTO.techStack = crewJoinInfoVO.getTechStack();
		crewDetailResponseDTO.githubAddress = crewJoinInfoVO.getGithubAddress();
		crewDetailResponseDTO.suggestPart = crewJoinInfoVO.getSuggestPart();
		crewDetailResponseDTO.crewAlias = crewJoinInfoVO.getCrewAlias();
		crewDetailResponseDTO.etc = crewJoinInfoVO.getEtc();

		return crewDetailResponseDTO;
	}
}
