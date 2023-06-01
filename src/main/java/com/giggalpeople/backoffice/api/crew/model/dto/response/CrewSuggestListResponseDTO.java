package com.giggalpeople.backoffice.api.crew.model.dto.response;

import com.giggalpeople.backoffice.api.crew.model.vo.SuggestInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <h2><b>크루 지원자 목록 조회를 위한 정보 담은 DTO</b></h2>
 */

@Schema(description = "크루 지원자 목록 조회를 위한 정보 담은 DTO")
@Data
@NoArgsConstructor
public class CrewSuggestListResponseDTO {
    @Schema(description = "크루 지원 순서 번호", nullable = false, example = "0032")
    private String suggestId;

    @Schema(description = "크루 번호", nullable = false, example = "20220000000")
    private String crewNumber;

    @Schema(description = "지원서 등록 일시", nullable = false, example ="2022. 8. 7 오후 11:48:20")
    private LocalDate suggestDate;

    @Schema(description = "기깔나는 사람들을 알게 된 경로 정보", nullable = false, example ="인프런")
    private String howKnowInfo;

    @Schema(description = "지원자 이름", nullable = false, example ="홍길동")
    private String name;

    @Schema(description = "지원자 성별", nullable = false, example ="남자 or 여")
    private String sex;

    @Schema(description = "지원자 Email 주소", nullable = false, example ="abc3939@gmail.com")
    private String email;

    @Schema(description = "지원자 생년월일/나이", nullable = false, example ="19900101/34세")
    private String ageInfo;

    @Schema(description = "지원자 MBTI", nullable = false, example ="ISTP")
    private String mbti;

    @Schema(description = "지원자 현재 직업", nullable = false, example = "BackEnd 개발자")
    private String jobInfo;

    @Schema(description = "지원자 최종 학력", nullable = false, example = "4년대 졸업")
    private String lastEducational;

    @Schema(description = "지원자 거주지 주변 역 이름", nullable = false, example = "서울역")
    private String stationName;

    @Schema(description = "지원자 합류하고자 하는 Part", nullable = false, example = "WEB / APP 디자이너")
    private String suggestPart;

    @Schema(description = "지원자 참여 여부 상태", example = "대표 합류 거부")
    private String participation;

    @Schema(description = "지원자 대면 면담 일자", example = "2022년 09월 25일(일) 20시 00분")
    private LocalDate meetDate;

    @Builder
    public CrewSuggestListResponseDTO(String suggestId,
                                      String crewNumber,
                                      LocalDate suggestDate,
                                      String howKnowInfo,
                                      String name,
                                      String sex,
                                      String email,
                                      String ageInfo,
                                      String mbti,
                                      String jobInfo,
                                      String lastEducational,
                                      String stationName,
                                      String suggestPart,
                                      String participation,
                                      LocalDate meetDate) {
        this.suggestId = suggestId;
        this.crewNumber = crewNumber;
        this.suggestDate = suggestDate;
        this.howKnowInfo = howKnowInfo;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.ageInfo = ageInfo;
        this.mbti = mbti;
        this.jobInfo = jobInfo;
        this.lastEducational = lastEducational;
        this.stationName = stationName;
        this.suggestPart = suggestPart;
        this.participation = participation;
        this.meetDate = meetDate;
    }

    /**
     * <b>크루 합류를 위한 지원자 Data Base 지원 정보 VO를 Client에게 반환하기 위해 DTO로 변환하는 Method</b>
     * @param suggestInfoVO Data Base 작성된 지원자 정보
     * @return CrewSuggestListResponseDTO - Client 목록 조회 요청의 반환하기 위한 지원자 정보
     */

    public static CrewSuggestListResponseDTO toDTO(SuggestInfoVO suggestInfoVO) {
        CrewSuggestListResponseDTO crewSuggestResponseDTO = new CrewSuggestListResponseDTO();

        crewSuggestResponseDTO.suggestId = suggestInfoVO.getSuggestId();
        crewSuggestResponseDTO.crewNumber = suggestInfoVO.getCrewNumber();
        crewSuggestResponseDTO.suggestDate = suggestInfoVO.getSuggestDate();
        crewSuggestResponseDTO.howKnowInfo = suggestInfoVO.getHowKnowInfo();
        crewSuggestResponseDTO.name = suggestInfoVO.getName();
        crewSuggestResponseDTO.sex = suggestInfoVO.getSex();
        crewSuggestResponseDTO.email = suggestInfoVO.getEmail();
        crewSuggestResponseDTO.ageInfo = suggestInfoVO.getAgeInfo();
        crewSuggestResponseDTO.mbti = suggestInfoVO.getMbti();
        crewSuggestResponseDTO.jobInfo = suggestInfoVO.getJobInfo();
        crewSuggestResponseDTO.lastEducational = suggestInfoVO.getLastEducational();
        crewSuggestResponseDTO.stationName = suggestInfoVO.getStationName();
        crewSuggestResponseDTO.suggestPart = suggestInfoVO.getSuggestPart();
        crewSuggestResponseDTO.participation = suggestInfoVO.getParticipation();
        crewSuggestResponseDTO.meetDate = suggestInfoVO.getMeetDate();

        return crewSuggestResponseDTO;
    }
}
