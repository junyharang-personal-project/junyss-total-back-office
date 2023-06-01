package com.giggalpeople.backoffice.api.crew.model.dto.request;

import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.AgreeType;
import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.DrinkingInfo;
import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.Participation;
import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.WhetherType;
import com.giggalpeople.backoffice.common.util.DataTypeChangerUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * <h2><b>지원자 정보 담은 DTO</b></h2>
 */
@Slf4j
@Schema(description = "지원자 정보 담은 DTO")
@Data
public class SuggestRequestDTO {

    @Schema(description = "지원 순서", nullable = false, example = "0032")
    @NotBlank
    @Size(min = 1, max = 4)
    private String suggestId;

    @Schema(description = "지원서 등록 일시", nullable = false, example = "2023-04-13T10:05:36.698+00:00")
    @NotBlank
    @Size(min = 1, max = 20)
    private LocalDate suggestDate;

    @Schema(description = "기깔나는 사람들을 알게 된 경로 정보", nullable = false, example = "인프런")
    @Size(max = 10)
    private String howKnowInfo;

    @Schema(description = "크루 번호", nullable = false, example = "2022-000-0000")
    @NotBlank
    @Size(min = 1, max = 15)
    private String crewNumber;


    @Schema(description = "지원자 이름", nullable = false, example = "홍길동")
    @NotBlank
    @Size(min = 1, max = 10)
    @Pattern(regexp = "^[가-힣]+$")
    private String name;

    @Schema(description = "지원자 성별", nullable = false, example = "남자 or 여")
    @NotBlank
    @Size(min = 1, max = 5)
    @Pattern(regexp = "^[가-힣]+$")
    private String sex;

    @Schema(description = "지원자 Email 주소", nullable = false, example = "abc3939@gmail.com")
    @NotBlank
    @Size(min = 1, max = 255)
    @Email
    private String email;

    @Schema(description = "지원자 생년월일", nullable = false, example = "1990. 1. 14")
    @NotBlank
    @Size(min = 1, max = 255)
    private LocalDate birthDate;

    @Schema(description = "지원자 나이", nullable = false, example = "34")
    @NotBlank
    @Size(min = 1, max = 5)
    private String ageInfo;

    @Schema(description = "지원자 MBTI", nullable = false, example = "ISTP")
    @NotBlank
    @Size(min = 1, max = 5)
    @Pattern(regexp = "^[A-Z]+$")
    private String mbti;

    @Schema(description = "지원자 성격 상세", nullable = false, example = "안녕하세요? 저의 성격은...")
    private String personality;

    @Schema(description = "지원자 현재 직업", nullable = false, example = "BackEnd 개발자")
    @NotBlank
    @Size(min = 1, max = 20)
    private String jobInfo;

    @Schema(description = "최종 학력", nullable = false, example = "4년제")
    @NotBlank
    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[가-힣]+$")
    private String lastEducational;

    @Schema(description = "지원자 최종 학력 학교 이름", nullable = false, example = "서울대학교")
    @Size(max = 20)
    @Pattern(regexp = "^[a-zA-Z가-힣]+$")
    private String schoolName;

    @Schema(description = "지원자 거주지 주변 역 이름", nullable = false, example = "서울역")
    @NotBlank
    @Size(min = 1, max = 30)
    @Pattern(regexp = "^[가-힣]+$")
    private String stationName;

    @Schema(description = "지원자 재직 회사명", nullable = false, example = "삼성전자")
    @Size(max = 40)
    @Pattern(regexp = "^[가-힣]+$")
    private String companyName;

    @Schema(description = "지원자 핸드폰 번호", nullable = false, example = "010-1234-1234")
    @NotBlank
    @Size(min = 1, max = 15)
    @Pattern(regexp = "^01(?:0|1|[6-9])?(\\\\d{3}|\\\\d{4})?(\\\\d{4})$")
    private String phoneNumber;

    @Schema(description = "티스토리 계정", nullable = false, example = "abc3939@gmail.com")
    @NotBlank
    @Size(min = 1, max = 255)
    private String tistory;

    @Schema(description = "피그마 계정", nullable = false, example = "abc3939@gmail.com")
    @NotBlank
    @Size(min = 1, max = 255)
    private String figma;

    @Schema(description = "노션 계정", nullable = false, example = "abc3939@gmail.com")
    @NotBlank
    @Size(min = 1, max = 255)
    private String notion;

    @Schema(description = "개인 블로그 주소", nullable = false, example = "https://giggal-people.tistory.com/")
    @NotBlank
    @Size(min = 1, max = 255)
    private String blogUrl;

    @Schema(description = "지원하고자 하는 파트", nullable = false, example = "WEB / APP 디자이너")
    @NotBlank
    @Size(min = 1, max = 20)
    private String suggestPart;

    @Schema(description = "기술력 상세", nullable = false)
    @NotBlank
    @Size(min = 1)
    @Pattern(regexp = "^[가-힣]+$")
    private String techStack;

    @Schema(description = "지원자 경력 상세", example = "안녕하세요? 저는...")
    @Pattern(regexp = "^[가-힣]+$")
    private String career;

    @Schema(description = "크루 Git Hub 주소", example = "https://github.com/abc")
    @NotBlank
    @Size(min = 1)
    private String githubAddress;

    @Schema(description = "지원자 포트폴리오", example = "https://drive.google.com/open?id=abc")
    private String portfolio;

    @Schema(description = "개인정보 수집 동의 여부", nullable = false, example = "동의 합니다. 혹은 동의하지 않습니다.")
    @NotBlank
    private String privacyInfoAgree;

    @Schema(description = "추가 사항 동의 여부", nullable = false, example = "동의 합니다. 혹은 동의하지 않습니다.")
    @NotBlank
    private String addendumInfoAgree;

    @Schema(description = "지원자 추가로 하고 싶은 말", example = "제가 합류하게 된다면...")
    private String etc;

    @Schema(description = "지원자 음주 여부", example = "소주 기준 1 ~ 3병 미만")
    private DrinkingInfo drinking;

    @Schema(description = "지원자 취미", example = "게임")
    private String hobby;

    @Schema(description = "지원자 개인 활동 상세", example = "저의 일주일 활동은 ...")
    private String personalClassInfo;

    @Schema(description = "지원자 연애 여부 - YES - 1, NO - 0, 미 작성 - 3", example = "NO")
    private String loveWhether;

    @Schema(description = "지원자 참여 여부 상태", example = "대표 합류 거부")
    private String participation;

    @Schema(description = "지원자 대면 면담 일자", example = "2022년 09월 25일(일) 20시 00분")
    private String inputMeetDate;

    @JsonIgnore
    @Schema(description = "지원자 대면 면담 일자", example = "2022-09-25")
    private LocalDate meetDate;

    @Schema(description = "비고", example = "대면 면담일에 약속 시간 늦어 탈락!")
    private String note;

    /**
     * <b>구글 스프레드 시트에 기존에는 4년제 졸업과 같이 최종 학력을 작성했고, 최근 에는 공백 졸업을 빼고, 작성하고 있는데, 공백과 졸업을 없애고, DB에 저장하기 위한 Setter</b>
     *
     * @param lastEducational 구글 스프레드 시트에 저장된 최종 학력 정보
     */

    public void setLastEducational(String lastEducational) {
        this.lastEducational = DataTypeChangerUtil.processLastEducational(lastEducational);
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = DataTypeChangerUtil.processSchoolName(schoolName);
    }

    public boolean getPrivacyInfoAgree() {
        if (privacyInfoAgree.equals("동의 합니다.")) {
            return AgreeType.AGREE.isCheckAgree();
        } else {
            return AgreeType.DISAGREE.isCheckAgree();
        }
    }

    public boolean getAddendumInfoAgree() {
        if (addendumInfoAgree.equals("동의 합니다.")) {
            return AgreeType.AGREE.isCheckAgree();
        } else {
            return AgreeType.DISAGREE.isCheckAgree();
        }
    }

    public void setDrinking(String drinking) {
        if (drinking.equals(DrinkingInfo.ONE_BOTTLE.getIsKOREANCheck())) {
            this.drinking = DrinkingInfo.ONE_BOTTLE;
        } else if (drinking.equals(DrinkingInfo.ONE_AND_THREE_BOTTLES.getIsKOREANCheck())) {
            this.drinking = DrinkingInfo.ONE_AND_THREE_BOTTLES;
        } else if (drinking.equals(DrinkingInfo.THREE_OR_MORE_BOTTLES.getIsKOREANCheck())) {
            this.drinking = DrinkingInfo.THREE_OR_MORE_BOTTLES;
        } else if (drinking.equals(DrinkingInfo.NOPE.getIsKOREANCheck())) {
            this.drinking = DrinkingInfo.NOPE;
        } else if (drinking.equals(DrinkingInfo.ETC.getIsKOREANCheck())) {
            this.drinking = DrinkingInfo.ETC;
        } else {
            this.drinking = DrinkingInfo.NONE;
        }
    }

    public String getDrinking() {
        if (drinking.equals(DrinkingInfo.ONE_BOTTLE)) {
            return DrinkingInfo.ONE_BOTTLE.getIsKOREANCheck();
        } else if (drinking.equals(DrinkingInfo.ONE_AND_THREE_BOTTLES)) {
            return DrinkingInfo.ONE_AND_THREE_BOTTLES.getIsKOREANCheck();
        } else if (drinking.equals(DrinkingInfo.THREE_OR_MORE_BOTTLES)) {
            return DrinkingInfo.THREE_OR_MORE_BOTTLES.getIsKOREANCheck();
        } else if (drinking.equals(DrinkingInfo.NOPE)) {
            return DrinkingInfo.NOPE.getIsKOREANCheck();
        } else if (drinking.equals(DrinkingInfo.ETC)) {
            return DrinkingInfo.ETC.getIsKOREANCheck();
        } else {
            return DrinkingInfo.NONE.getIsKOREANCheck();
        }
    }

    public void setLoveWhether(String loveWhether) {
        if (loveWhether.equals(WhetherType.NO.getInput())) {
            this.loveWhether = WhetherType.NO.getInput();
        } else if (loveWhether.equals(WhetherType.YES.getInput())) {
            this.loveWhether = WhetherType.YES.getInput();
        } else {
            this.loveWhether = WhetherType.UNKNOWN.getInput();
        }
    }

    public int getLoveWhether() {
        if (loveWhether.equals(WhetherType.NO.getInput())) {
            return WhetherType.NO.getValue();
        } else if (loveWhether.equals(WhetherType.YES.getInput())) {
            return WhetherType.YES.getValue();
        } else {
            return WhetherType.UNKNOWN.getValue();
        }
    }

    public void setInputMeetDate(String inputMeetDate) {
        this.inputMeetDate = inputMeetDate;

        if (!this.inputMeetDate.equals("")) {
            String[] splitDateString = inputMeetDate.split(" ");
            StringBuilder resultDate = new StringBuilder();
            StringBuilder garbageDate = new StringBuilder();

            for (String dateElement : splitDateString) {
                if (dateElement.contains("년")) {
                    resultDate.append(dateElement.replace("년", "-"));
                } else if (dateElement.contains("월") && !dateElement.contains("(월)")) {
                    resultDate.append(dateElement.replace("월", "-"));
                } else if (dateElement.contains("일") && !dateElement.contains("(일)")) {
                    resultDate.append(dateElement.replace("일", ""));
                } else {
                    garbageDate.append(dateElement);
                    garbageDate.delete(0, garbageDate.length());
                }
            }
            this.meetDate = LocalDate.parse(resultDate.toString());
        }
    }

    public String getParticipation() {
        if (participation.equals(Participation.JOIN.getInParticipationInfo()) || participation.equals("참가(신입)")) {
            return Participation.JOIN.getSaveParticipationInfo();
        } else if (participation.equals(Participation.TEST.getInParticipationInfo())) {
            return Participation.TEST.getSaveParticipationInfo();
        } else if (participation.equals(Participation.MEETING_EXPECTED.getInParticipationInfo())) {
            return Participation.MEETING_EXPECTED.getSaveParticipationInfo();
        } else if (participation.equals(Participation.JOIN_GIVE_UP.getInParticipationInfo())) {
            return Participation.JOIN_GIVE_UP.getSaveParticipationInfo();
        } else if (participation.equals(Participation.MID_WAY_GIVE_UP.getInParticipationInfo())) {
            return Participation.MID_WAY_GIVE_UP.getSaveParticipationInfo();
        } else if (participation.equals(Participation.FORCED_EXIT.getInParticipationInfo())) {
            return Participation.FORCED_EXIT.getSaveParticipationInfo();
        } else if (participation.equals(Participation.TEAM_LEADER_JOIN_REFUSAL.getInParticipationInfo())) {
            return Participation.TEAM_LEADER_JOIN_REFUSAL.getSaveParticipationInfo();
        } else if (participation.equals(Participation.PROJECT_LEADER_JOIN_REFUSAL.getInParticipationInfo())) {
            return Participation.PROJECT_LEADER_JOIN_REFUSAL.getSaveParticipationInfo();
        } else if (participation.equals(Participation.BOSS_JOIN_REFUSAL.getInParticipationInfo())) {
            return Participation.BOSS_JOIN_REFUSAL.getSaveParticipationInfo();
        } else {
            return Participation.CHECK.getSaveParticipationInfo();
        }
    }
}
