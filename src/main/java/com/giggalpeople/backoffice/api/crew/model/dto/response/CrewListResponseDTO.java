package com.giggalpeople.backoffice.api.crew.model.dto.response;

import com.giggalpeople.backoffice.api.crew.model.vo.JoinInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <h2><b>크루 전체 조회를 위한 정보 담은 DTO</b></h2>
 */

@Schema(description = "크루 전체 조회를 위한 정보 담은 DTO")
@Data
@NoArgsConstructor
public class CrewListResponseDTO {
    @Schema(description = "지원 순서", nullable = false, example = "1")
    private String suggestId;

    @Schema(description = "크루 합류 순서 번호", nullable = false, example = "1")
    private Long crewJoinId;

    @Schema(description = "크루 번호", nullable = false, example = "20220000000")
    private String crewNumber;

    @Schema(description = "지원서 등록 일시", nullable = false, example ="2022. 8. 7 오후 11:48:20")
    private LocalDate suggestDate;

    @Schema(description = "크루 합류 날짜", nullable = false, example ="2023년 01월 26일(목) 합류")
    private String joinDate;

    @Schema(description = "크루 이름", nullable = false, example ="홍길동")
    private String name;

    @Schema(description = "크루 성별", nullable = false, example ="남자 or 여")
    private String sex;

    @Schema(description = "크루 Email 주소", nullable = false, example ="abc3939@gmail.com")
    private String email;

    @Schema(description = "크루 생년월일", nullable = false, example ="1990. 1. 14")
    private LocalDate birthDate;

    @Schema(description = "지원자 나이", nullable = false, example ="34")
    private String ageInfo;

    @Schema(description = "크루 MBTI", nullable = false, example ="ISTP")
    private String mbti;

    @Schema(description = "크루 거주지 주변 역 이름", nullable = false, example = "서울역")
    private String stationName;

    @Schema(description = "크루 핸드폰 번호", nullable = false, example = "010-000-0000")
    private String phoneNumber;

    @Schema(description = "크루 소속 Team", nullable = false, example = "WEB / APP 디자이너")
    private String suggestPart;

    @Schema(description = "크루 별칭", nullable = false, example = "[FrontEnd]홍길동")
    private String crewAlias;

    @Builder
    public CrewListResponseDTO(String suggestId,
                               Long crewJoinId,
                               String crewNumber,
                               LocalDate suggestDate,
                               String joinDate,
                               String name,
                               String sex,
                               String email,
                               LocalDate birthDate,
                               String ageInfo,
                               String mbti,
                               String stationName,
                               String phoneNumber,
                               String suggestPart,
                               String crewAlias) {
        this.suggestId = suggestId;
        this.crewJoinId = crewJoinId;
        this.crewNumber = crewNumber;
        this.suggestDate = suggestDate;
        this.joinDate = joinDate;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.birthDate = birthDate;
        this.ageInfo = ageInfo;
        this.mbti = mbti;
        this.stationName = stationName;
        this.phoneNumber = phoneNumber;
        this.suggestPart = suggestPart;
        this.crewAlias = crewAlias;
    }

    /**
     * <b>Data Base에 저장된 크루 정보를 해당 DTO로 변환하는 Method</b>
     * @param crewJoinInfoVO 크루 합류가 결정된 인원에 대한 Data Base 저장 크루 정보
     * @return CrewListResponseDTO - Client에게 목록 조회를 위해 반환하기 위한 크루 정보
     */

    public static CrewListResponseDTO toDTO(JoinInfoVO crewJoinInfoVO) {
        CrewListResponseDTO crewListResponseDTO = new CrewListResponseDTO();

        crewListResponseDTO.suggestId = crewJoinInfoVO.getSuggestId();
        crewListResponseDTO.crewJoinId = crewJoinInfoVO.getCrewJoinId();
        crewListResponseDTO.crewNumber = crewJoinInfoVO.getCrewNumber();
        crewListResponseDTO.suggestDate = crewJoinInfoVO.getSuggestDate();
        crewListResponseDTO.joinDate = crewJoinInfoVO.getJoinDate();
        crewListResponseDTO.name = crewJoinInfoVO.getName();
        crewListResponseDTO.sex = crewJoinInfoVO.getSex();
        crewListResponseDTO.email = crewJoinInfoVO.getEmail();
        crewListResponseDTO.birthDate = crewJoinInfoVO.getBirthDate();
        crewListResponseDTO.ageInfo = crewJoinInfoVO.getAgeInfo();
        crewListResponseDTO.mbti = crewJoinInfoVO.getMbti();
        crewListResponseDTO.stationName = crewJoinInfoVO.getStationName();
        crewListResponseDTO.phoneNumber = crewJoinInfoVO.getPhoneNumber();
        crewListResponseDTO.suggestPart = crewJoinInfoVO.getSuggestPart();
        crewListResponseDTO.crewAlias = crewJoinInfoVO.getCrewAlias();

        return crewListResponseDTO;
    }
}
