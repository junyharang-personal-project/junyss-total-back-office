package com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util;

import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.DiscordBotException;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.EmbedObject;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.env.resource.ServerResourceCheck;
import com.giggalpeople.backoffice.common.env.resource.impl.ServerResourceCheckImpl;
import com.giggalpeople.backoffice.common.util.DataTypeChangerUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.webjars.NotFoundException;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

/**
 * <h2><b>Discord Bot을 통해 API 조회 시 응답 메시지를 가공하기 위한 Class</b></h2>
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscordBotResponseMessageUtil {

    /**
     * <b>크루 상세 조회 시 API를 통해 얻어온 응답 Data를 JAVA 문자열로 가공하기 위한 Method</b>
     * @param data API를 통해 얻어온 응답 Data
     * @return JAVA 문자열로 가공된 응답 Data
     */
    public static String createDetailCrewSearchResponseMessage(JSONObject data) {
        StringBuilder resultMessage = new StringBuilder();

        resultMessage.append("지원 순서 : ").append(DataTypeChangerUtil.changeSuggestId(data.getInt("suggestId"))).append("\n")
                .append("합류 순서 : ").append(data.getInt("crewJoinId")).append("\n")
                .append("크루(지원) 번호 : ").append(data.getString("crewNumber")).append("\n")
                .append("지원 일시 : ").append(data.getString("suggestDate")).append("\n")
                .append("합류 일시 : ").append(data.getString("joinDate")).append("\n")
                .append("우리를 알게 된 경로 : ").append(data.getString("howKnowInfo")).append("\n")
                .append("이름 : ").append(data.getString("name")).append("\n")
                .append("성별 : ").append(data.getString("sex")).append("\n")
                .append("연락처 : ").append(data.getString("phoneNumber")).append("\n")
                .append("E-Mail : ").append(data.getString("email")).append("\n")
                .append("생년월일 : ").append(data.getString("birthDate")).append("\n")
                .append(DataTypeChangerUtil.createNowYear() + "년 기준 나이 : ").append(data.getString("ageInfo")).append("\n")
                .append("MBTI : ").append(data.getString("mbti")).append("\n")
                .append("성격 상세 : ").append(data.getString("personality")).append("\n")
                .append("내부 계정 (ID) : ").append(data.getString("userId")).append("\n")
                .append("내부 역할(직책) : ").append(data.getString("crewRole")).append("\n")
                .append("개인정보 수집 동의 여부 : ").append(DataTypeChangerUtil.changeAgreeBooleanToString((data.getBoolean("privacyInfoAgree"))).append("\n")
                .append("추가정보 수집 동의 여부 : ").append(DataTypeChangerUtil.changeAgreeBooleanToString(data.getBoolean("addendumInfoAgree"))).append("\n")
                .append("현재 직업 : ").append(data.getString("jobInfo")).append("\n")
                .append("최종 학력 : ").append(data.getString("lastEducational")).append("\n")
                .append("최종 학력 학교 이름 : ").append(data.getString("schoolName")).append("\n")
                .append("거주지 주변 역 : ").append(data.getString("stationName")).append("\n")
                .append("Tistory 계정 : ").append(data.getString("tistory")).append("\n")
                .append("Figma 계정 : ").append(data.getString("figma")).append("\n")
                .append("노션 계정 : ").append(data.getString("notion")).append("\n")
                .append("본인이 갖은 기술 상세 : ").append(data.getString("techStack")).append("\n")
                .append("Git Hub 주소 : ").append(data.getString("githubAddress")).append("\n")
                .append("개인 Blog 주소 : ").append(data.getString("blogUrl")).append("\n")
                .append("포트폴리오 주소 : ").append(data.getString("portfolio")).append("\n")
                .append("지원 Part : ").append(data.getString("suggestPart")).append("\n")
                .append("합류일 : ").append(data.getString("joinDate")).append("\n")
                .append("크루 별칭 : ").append(data.getString("crewAlias")).append("\n\n")
                .append("추가로 하고 싶은 이야기 : ").append(data.getString("etc")).append("\n\n"));

        return resultMessage.toString();
    }

    /**
     * <b>지원자 상세 조회 시 API를 통해 얻어온 응답 Data를 JAVA 문자열로 가공하기 위한 Method</b>
     * @param data API를 통해 얻어온 응답 Data
     * @return JAVA 문자열로 가공된 응답 Data
     */

    public static String createDetailSuggestSearchResponseMessage(JSONObject data) {
        StringBuilder resultMessage = new StringBuilder();
        String participation = data.getString("participation");

        resultMessage.append("크루 번호 : ").append(data.getString("crewNumber")).append("\n")
                .append("지원 일시 : ").append(data.getString("suggestDate")).append("\n")
                .append("우리를 알게 된 경로 : ").append(data.getString("howKnowInfo")).append("\n")
                .append("이름 : ").append(data.getString("name")).append("\n")
                .append("성별 : ").append(data.getString("sex")).append("\n")
                .append("E-Mail : ").append(data.getString("email")).append("\n")
                .append("생년월일 : ").append(data.getString("birthDate")).append("\n")
                .append(DataTypeChangerUtil.createNowYear()).append("년 기준 나이 : ").append(data.getString("ageInfo")).append("\n")
                .append("MBTI : ").append(data.getString("mbti")).append("\n")
                .append("성격 상세 : ").append(data.getString("personality")).append("\n")
                .append("현재 직업 : ").append(data.getString("jobInfo")).append("\n")
                .append("최종 학력 : ").append(data.getString("lastEducational")).append("\n")
                .append("최종 학력 학교 이름 : ").append(data.getString("schoolName")).append("\n")
                .append("거주지 주변 역 : ").append(data.getString("stationName")).append("\n")
                .append("재직 회사 이름 : ").append(data.getString("companyName")).append("\n")
                .append("연락처 : ").append(data.getString("phoneNumber")).append("\n")
                .append("Tistory Blog 계정 : ").append(data.getString("tistory")).append("\n")
                .append("피그마 계정 : ").append(data.getString("figma")).append("\n")
                .append("노션 계정 : ").append(data.getString("notion")).append("\n")
                .append("개인 Blog 주소 : ").append(data.getString("blogUrl")).append("\n")
                .append("지원 Part : ").append(data.getString("suggestPart")).append("\n")
                .append("본인이 갖은 기술 상세 : ").append(data.getString("techStack")).append("\n")
                .append("경력 상세 : ").append(data.getString("career")).append("\n")
                .append("Git Hub 주소 : ").append(data.getString("githubAddress")).append("\n")
                .append("포트폴리오 주소 : ").append(data.getString("portfolio")).append("\n")
                .append("개인정보 수집 동의 여부 : ").append(DataTypeChangerUtil.changeAgreeBooleanToString((data.getBoolean("privacyInfoAgree"))).append("\n")
                .append("추가정보 수집 동의 여부 : ").append(DataTypeChangerUtil.changeAgreeBooleanToString(data.getBoolean("addendumInfoAgree"))).append("\n")
                .append("추가로 하고 싶은 이야기 : ").append(data.getString("etc")).append("\n")
                .append("현재 상태 : ").append(data.getString("participation")).append("\n"));

        if(participation.equals("대면 만남 예정")) {
            resultMessage.append("대면 만남 예정 일시 : ").append(data.getString("meetDate")).append("\n");
        }

        resultMessage.append("비고 : ").append(data.getString("note")).append("\n\n");

        return resultMessage.toString();
    }

    /**
     * <b>Discord Bot이 반환할 Message를 Error Log 상세 조회 관련 API 호출 뒤 얻어온 정보를 통해 만드는 Method</b>
     * @param apiResponseData API를 통해 얻어온 응답 Data
     * @return Discord Bot이 반환할 Message
     */
    public static String forLeaderCreateDetailErrorLogSearchResponseMessage(JSONObject apiResponseData) {
        return "Error Log 순서 번호 : " + apiResponseData.getInt("logId") + "\n\n" +
                "Error Log 발생 일시 : " + apiResponseData.getString("createdDateTime") + "\n" +
                "Error Log Level : " + apiResponseData.getString("level") + "\n" +
                "Error Log Exception 간략 내용 : " + apiResponseData.getString("exceptionBrief") + "\n\n" +
                "Error Log 발생 Server 정보 : \n" +
                "Error Log 발생 Server 이름 : " + apiResponseData.getString("serverName") + "\n" +
                "Error Log 발생 Server VM 정보 : " + apiResponseData.getString("serverVMInfo") + "\n" +
                "Error Log 발생 Server OS 정보 : " + apiResponseData.getString("serverOSInfo") + "\n" +
                "Error Log 발생 Server IP : " + apiResponseData.getString("serverIP") + "\n" +
                "Error Log 발생 Server 구동 환경 : " + apiResponseData.getString("serverEnvironment") + "\n\n" +
                "이용자 정보 : \n" +
                "이용자 IP : " + apiResponseData.getString("userIp") + "\n" +
                "이용자 환경 정보 : " + apiResponseData.getString("userEnvironment") + "\n\n" +
                "이용자 접속 위치 : " + apiResponseData.getString("userLocation").replaceAll("[\\{\\{\\}]", "") + "\n\n" +
                "이용자 요청 Header 정보 : " + apiResponseData.getString("requestHeader").replaceAll("[\\{\\{\\}]", "") + "\n\n" +
                "이용자 요청 Cookie 정보 : " + apiResponseData.getString("userCookies") + "\n\n" +
                "이용자 요청 Parameter 정보 : " + apiResponseData.getString("requestParameter").replaceAll("[\\{\\{\\}]", "") + "\n\n" +
                "이용자 요청 Body 정보 : " + apiResponseData.getString("requestBody") + "\n\n\n" +
                "Error Log Exception 상세 내용 : " + apiResponseData.getString("exceptionDetail") + "\n\n";
    }

    /**
     * <b>Discord Bot이 반환할 Message를 이용자 접속 및 요청 상세 조회 관련 API 호출 뒤 얻어온 정보를 통해 만드는 Method</b>
     * @param apiResponseData API를 통해 얻어온 응답 Data
     * @return Discord Bot이 반환할 Message
     */
    public static String forLeaderCreateDetailUserInfoSearchResponseMessage(JSONObject apiResponseData) {
        return "이용자 요청 순서 번호 : " + apiResponseData.getInt("connectedUserRequestInfoID") + "\n\n" +
                "이용자 요청 일시 : " + apiResponseData.getString("dataCreatedDateTime") + "\n" +
                "이용자 접속 Server 정보 : \n" +
                "이용자 접속 Server 이름 : " + apiResponseData.getString("serverName") + "\n" +
                "이용자 접속 Server VM 정보 : " + apiResponseData.getString("serverVmInfo") + "\n" +
                "이용자 접속 Server OS 정보 : " + apiResponseData.getString("serverOSInfo") + "\n" +
                "이용자 접속 Server IP : " + apiResponseData.getString("serverIP") + "\n" +
                "이용자 접속 Server 구동 환경 : " + apiResponseData.getString("serverEnvironment") + "\n\n" +
                "이용자 정보 : \n" +
                "이용자 IP : " + apiResponseData.getString("userIP") + "\n" +
                "이용자 환경 정보 : " + apiResponseData.getString("userEnvironment") + "\n\n" +
                "이용자 접속 위치 : " + apiResponseData.getString("userLocation").replaceAll("[\\{\\{\\}]", "") + "\n\n" +
                "이용자 요청 Header 정보 : " + apiResponseData.getString("requestHeader").replaceAll("[\\{\\{\\}]", "") + "\n\n" +
                "이용자 요청 Cookie 정보 : " + apiResponseData.getString("userCookies") + "\n\n" +
                "이용자 요청 Parameter 정보 : " + apiResponseData.getString("requestParameter").replaceAll("[\\{\\{\\}]", "") + "\n\n" +
                "이용자 요청 Body 정보 : " + apiResponseData.getString("requestBody") + "\n\n";
    }

    public static String forGeneralCrewCreateDetailErrorLogSearchResponseMessage(JSONObject apiResponseData) {
        return "Error Log 순서 번호 : " + apiResponseData.getInt("logId") + "\n\n" +
                "Error Log 발생 일시 : " + apiResponseData.getString("createdDateTime") + "\n" +
                "Error Log Level : " + apiResponseData.getString("level") + "\n" +
                "Error Log Exception 간략 내용 : " + apiResponseData.getString("exceptionBrief") + "\n\n" +
                "Error Log 발생 Server 정보 : \n" +
                "Error Log 발생 Server 이름 : " + apiResponseData.getString("serverName") + "\n" +
                "Error Log 발생 Server VM 정보 : " + apiResponseData.getString("serverVMInfo") + "\n" +
                "Error Log 발생 Server OS 정보 : " + apiResponseData.getString("serverOSInfo") + "\n" +
                "Error Log 발생 Server IP : " + apiResponseData.getString("serverIP") + "\n" +
                "Error Log 발생 Server 구동 환경 : " + apiResponseData.getString("serverEnvironment") + "\n\n" +
                "이용자 정보 : \n" +
                "이용자 IP : 이용자 정보 보안 정책에 의거 해당 정보를 확인할 권한이 없어요! \n" +
                "이용자 환경 정보 : 이용자 정보 보안 정책에 의거 해당 정보를 확인할 권한이 없어요! \n" +
                "이용자 접속 위치 : 이용자 정보 보안 정책에 의거 해당 정보를 확인할 권한이 없어요! \n" +
                "이용자 요청 Header 정보 : 이용자 정보 보안 정책에 의거 해당 정보를 확인할 권한이 없어요! \n" +
                "이용자 요청 Cookie 정보 : 이용자 정보 보안 정책에 의거 해당 정보를 확인할 권한이 없어요! \n" +
                "이용자 요청 Parameter 정보 : 이용자 정보 보안 정책에 의거 해당 정보를 확인할 권한이 없어요! \n" +
                "이용자 요청 Body 정보 : 이용자 정보 보안 정책에 의거 해당 정보를 확인할 권한이 없어요! \n\n\n" +
                "Error Log Exception 상세 내용 : " + apiResponseData.getString("exceptionDetail") + "\n\n";
    }

    /**
     * <b>API를 통해 지원자 정보 조회 시 응답 Messgae를 가공하기 위한 Method</b>
     * @param indexOfSuggest 지원자 지원 순서
     * @param dataJSONObject API를 통해 얻어온 Data Base에 저장된 지원자들 정보
     * @return 지원자 정보 목록 조회를 위해 가공된 지원자 정보
     */

    public static StringBuilder createFindBySuggestList(String indexOfSuggest, JSONObject dataJSONObject) {
        StringBuilder resultMessage = new StringBuilder();

        resultMessage.append(indexOfSuggest)
                .append(" 번째 지원자 정보 \n")
                .append("지원 순서 : ").append(DataTypeChangerUtil.changeSuggestId(dataJSONObject.getInt("suggestId"))).append("\n")
                .append("지원서 접수 일시 : ").append(dataJSONObject.getString("suggestDate")).append("\n")
                .append("크루(지원) 번호 : ").append(dataJSONObject.getString("crewNumber")).append("\n")
                .append("E-Mail : ").append(dataJSONObject.getString("email")).append("\n")
                .append("이름 : ").append(dataJSONObject.getString("name")).append("\n")
                .append("성별 : ").append(dataJSONObject.getString("sex")).append("\n")
                .append(DataTypeChangerUtil.createNowYear()).append("년 기준 나이 : ").append(dataJSONObject.getString("ageInfo")).append("\n")
                .append("현재 직업 : ").append(dataJSONObject.getString("jobInfo")).append("\n")
                .append("최종 학력 : ").append(dataJSONObject.getString("lastEducational")).append("\n")
                .append("지원 Part : ").append(dataJSONObject.getString("suggestPart")).append("\n")
                .append("현재 상태 : ").append(dataJSONObject.getString("participation")).append("\n");

        if (dataJSONObject.getString("participation").equals("대면 만남 예정")) {
            resultMessage.append("대면 면담 일 : ").append(dataJSONObject.getString("meetDate")).append("\n");
        }
        resultMessage.append("\n\n");

        return resultMessage;
    }

    /**
     * <b>Discord Bot을 통해 크루 정보 조회 API 호출 시 응답 Messgae를 가공하기 위한 Method</b>
     * @param indexOfSuggest 크루 지원 순서
     * @param dataJSONObject API를 통해 얻어온 Data Base에 저장된 크루들 정보
     * @return 크루 정보 목록 조회를 위해 가공된 지원자 정보
     */

    public static StringBuilder createFindByCrewList(String indexOfSuggest, JSONObject dataJSONObject) {
        StringBuilder resultMessage = new StringBuilder();

        resultMessage.append(indexOfSuggest)
                .append(" 번째 크루 정보 \n")
                .append("지원서 접수 일시 : ").append(dataJSONObject.getString("suggestDate")).append("\n")
                .append("지원 순서 : ").append(DataTypeChangerUtil.changeSuggestId(dataJSONObject.getInt("suggestId"))).append("\n")
                .append("합류 순서 : ").append(dataJSONObject.getInt("crewJoinId")).append("\n")
                .append("크루 번호 : ").append(dataJSONObject.getString("crewNumber")).append("\n")
                .append("합류일 : ").append(dataJSONObject.getString("joinDate")).append("\n")
                .append("E-Mail : ").append(dataJSONObject.getString("email")).append("\n")
                .append("이름 : ").append(dataJSONObject.getString("name")).append("\n")
                .append("성별 : ").append(dataJSONObject.getString("sex")).append("\n")
                .append("생년월일 : ").append(dataJSONObject.getString("birthDate")).append("\n")
                .append(DataTypeChangerUtil.createNowYear() + "년 기준 나이 : ").append(dataJSONObject.getString("ageInfo")).append("\n")
                .append("지원 Part : ").append(dataJSONObject.getString("suggestPart")).append("\n\n");

        return resultMessage;
    }

    /**
     * <b>Discord Bot을 통해 Error Log 목록 조회 API 호출 시 응답 Messgae를 가공하기 위한 Method</b>
     * @param logId Error Log PK
     * @param dataJSONObject API 응답 JSON Data
     * @return Discord Bot 응답 Message
     */

    public static StringBuilder createFindByErrorLogList(long logId, JSONObject dataJSONObject) {
        StringBuilder resultMessage = new StringBuilder();

        resultMessage.append(logId)
                .append(" 번째 Error Log 정보 \n")
                .append("Error 순서 번호 : ").append(logId).append("\n")
                .append("발생 일시 : ").append(dataJSONObject.getString("createdDateTime")).append("\n")
                .append("Error Level : ").append(dataJSONObject.getString("level")).append("\n")
                .append("Error 발생 Sever 이름 : ").append(dataJSONObject.getString("serverName")).append("\n")
                .append("Error 발생 Sever 구동 환경 : ").append(dataJSONObject.getString("serverEnvironment")).append("\n")
                .append("Error 발생 Sever IP : ").append(dataJSONObject.getString("serverIP")).append("\n")
                .append("Error 간략 내용 : ").append(dataJSONObject.getString("exceptionBrief")).append("\n\n");

        return resultMessage;
    }

    /**
     * <b>Discord Bot을 통해 이용자 접속 및 요청 정보 목록 조회 API 호출 시 응답 Messgae를 가공하기 위한 Method</b>
     * @param connectedUserRequestInfoID 이용자 요청 정보 PK
     * @param dataJSONObject API 응답 JSON Data
     * @return Discord Bot 응답 Message
     */

    public static StringBuilder createFindByUserInfoList(long connectedUserRequestInfoID, JSONObject dataJSONObject) {
        StringBuilder resultMessage = new StringBuilder();

        resultMessage.append(connectedUserRequestInfoID)
                .append(" 번째 이용자 접속 및 요청 정보 \n")
                .append("이용자 요청 정보 순서 번호 : ").append(connectedUserRequestInfoID).append("\n")
                .append("이용자 요청 일시 : ").append(dataJSONObject.getString("connectedDateTime")).append("\n")
                .append("이용자 요청 대상 Server 이름 : ").append(dataJSONObject.getString("serverName")).append("\n")
                .append("이용자 IP 주소 : ").append(dataJSONObject.getString("userIP")).append("\n\n");

        return resultMessage;
    }

    /**
     * <b>API를 통해 목록 조회 시 Paging 정보를 반환하기 위한 Method</b>
     * @param pagination Pagination 정보
     * @return 가공된 Pagination 정보
     */

    public static StringBuilder createPaginationInfo(JSONObject pagination) {
        StringBuilder resultMessage = new StringBuilder();

        resultMessage.append("시작 페이지  : ").append(pagination.getInt("startPage")).append("\n")
                .append("마지막 페이지 : ").append(checkCriteriaLastPageNum(pagination.getInt("endPage"))).append("\n")
                .append("이전 페이지 이동 가능 여부 : ").append(checkPaginationMove(pagination.getBoolean("prev"))).append("\n")
                .append("다음 페이지 이동 가능 여부 : ").append(checkPaginationMove(pagination.getBoolean("next"))).append("\n")
                .append("Criteria - 한번에 조회할 Element 개수 : ").append(pagination.getJSONObject("criteria").getInt("perPageNum")).append("\n")
                .append("Criteria - 현재 페이지 : ").append(checkCriteriaPageNum(pagination.getJSONObject("criteria").getInt("page"))).append("\n")
                .append("총 Element 개수 : ").append(pagination.getInt("totalCount")).append("\n\n");

        return resultMessage;
    }

    /**
     * <b>Discord Bot을 통해 API 조회 시 문제가 발생하면 문제를 알려주기 위한 Message를 만들어 주는 Method</b>
     * @param jsonObject API Response 값이 들어있는 JSON Type Object
     * @return Error Message를 담은 List (해당 Method를 호출하는 Method가 List를 반환 받아야 하기 때문에 List로 만들어야 함.)
     */

    public static void createErrorMessage(MessageReceivedEvent event, JSONObject jsonObject) {
        String generalMessageErrorMessage = "API 요청 중 문제가 발생하였어요 😢 ";
        String errorEmbedMessage = event.getAuthor().getAsMention() + "님 API 검색 요청 하였지만 ";

        if (jsonObject.getInt("statusCode") == 404) {
            errorEmbedMessage += "올바른 값을 찾을 수 없어요. 😳 \n\n";
            log.error("Discord Bot을 통해 API 검색 요청 하였지만 올바른 값을 찾을 수 없어요. \n HTTP Error 내용 : " + DefaultResponse.error(NOT_FOUND, NOT_FOUND.getMessage()).toString());
            throw new DiscordBotException(NOT_FOUND, NOT_FOUND.getMessage());

        } else if (jsonObject.getInt("statusCode") == 500) {
            errorEmbedMessage += "서버 문제가 발생하였어요. 😱 \n\n";
            log.error("Discord Bot을 통해 API 검색 요청 하였지만 서버 문제가 발생하였어요. \n HTTP Error 내용 : " + DefaultResponse.error(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage()).toString());
            throw new DiscordBotException(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());

        } else {
            EmbedBuilder errorEmbedBuilder = new EmbedBuilder();
            errorEmbedBuilder.setColor(Color.RED);
            errorEmbedBuilder.setDescription(errorEmbedMessage);
            DiscordBotResponseMessageUtil.createCommonEmbed(generalMessageErrorMessage.length() + errorEmbedMessage.length(), errorEmbedBuilder);
            DiscordBotResponseMessageUtil.sendMessage(event, generalMessageErrorMessage, errorEmbedBuilder);
        }
    }

    /**
     * <b>Discord Bot API 조회 결과를 반환할 때, 여러 Message 별로 다른 일반 Message 처리를 위한 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     * @param discordEmbedMessage Discord Message 중 Embed 형태로 보내기 위한 객체
     * @param apiResponseMessage 디스코드 봇 응답 Message를 담은 Map
     * @param searchResult 디스코드 봇 응답 Message 중 API 검색 결과 List 값을 반복문을 통해 하나씩 자른 문자열
     * @param resultMessage API를 통해 조회된 결과가 나눠 담긴 문자열 값을 합치기 위한 StringBuilder 객체
     * @param loopCount GiggalDiscordListener.createSendMessage() 의 반복문 반복 회수
     */
    public static void messageSizeProcesses(MessageReceivedEvent event, EmbedBuilder discordEmbedMessage, Map.Entry<String, List<String>> apiResponseMessage, String searchResult, StringBuilder resultMessage, int loopCount) {
        int discordAllowEmbedMessageSize = 4096;
        int resultMessageSize = apiResponseMessage.getValue().get(0).length();
        int loopCnt = resultMessageSize / discordAllowEmbedMessageSize + 1;

        String commonTitle = "";

        if (event.getMessage().getContentDisplay().contains("지원자목록조회")) {
            commonTitle = "기깔나는 사람들 지원자 목록 조회";
        } else if (event.getMessage().getContentDisplay().contains("지원자상세조회")) {
            commonTitle = "기깔나는 사람들 지원자 상세 조회";
        } else if (event.getMessage().getContentDisplay().contains("크루목록조회")) {
            commonTitle = "기깔나는 사람들 크루 목록 조회";
        } else if (event.getMessage().getContentDisplay().contains("크루상세조회")) {
            commonTitle = "기깔나는 사람들 크루 상세 조회";
        } else if (event.getMessage().getContentDisplay().contains("이용자목록조회")) {
            commonTitle = "기깔나는 사람들 Application 이용자 접속 및 요청 정보 목록 조회";
        } else if (event.getMessage().getContentDisplay().contains("이용자상세조회")) {
            commonTitle = "기깔나는 사람들 Application 이용자 접속 및 요청 정보 상세 조회";
        } else if (event.getMessage().getContentDisplay().contains("로그목록조회")) {
            commonTitle = "기깔나는 사람들 로그 목록 조회";
        } else if (event.getMessage().getContentDisplay().contains("로그상세조회")) {
            commonTitle = "기깔나는 사람들 로그 상세 조회";
        } else {
            commonTitle = "기깔나는 사람들 Server 자원 조회";
        }

        if (resultMessageSize > discordAllowEmbedMessageSize) {
            incisionDiscordAllowSizeMessageSender(event, resultMessageSize, loopCnt, discordAllowEmbedMessageSize, apiResponseMessage, discordEmbedMessage, commonTitle);

        } else if (apiResponseMessage.getValue().size() - 1 == loopCount && loopCount == 0) {
            discordEmbedMessage.setTitle(commonTitle);
            discordEmbedMessage.setColor(Color.green);

            createCommonEmbed(commonTitle.length() + searchResult.length(), discordEmbedMessage);

            sendMessage(event, checkGeneralMessage(0, apiResponseMessage.getKey(), loopCount), discordEmbedMessage.setDescription(resultMessage.append(searchResult)));

            resultMessage.delete(0, resultMessage.length());

        } else if (loopCount == 0) {
            discordEmbedMessage.setTitle(commonTitle);
            discordEmbedMessage.setColor(Color.green);

            resultMessage.append(searchResult);

            checkResultMessageSize(event, discordEmbedMessage, apiResponseMessage, resultMessage, loopCount);

        } else if (loopCount > 0 && apiResponseMessage.getValue().size() > loopCount) {
            resultMessage.append(searchResult);

            checkResultMessageSize(event, discordEmbedMessage, apiResponseMessage, resultMessage, loopCount);
        }
    }

    private static void incisionDiscordAllowSizeMessageSender (MessageReceivedEvent event, int resultMessageSize, int loopCnt, int discordAllowMessageSize, Map.Entry<String, List<String>> apiResponseMessage, EmbedBuilder discordEmbedMessage, String commonTitle) {
        StringBuilder result = new StringBuilder();
        discordEmbedMessage.setTitle(commonTitle);
        discordEmbedMessage.setColor(Color.green);

        for (int inddex = 0; inddex < loopCnt; inddex++) {
            int firstIndex = inddex * discordAllowMessageSize;
            int lastIndex = (inddex + 1) * discordAllowMessageSize;

            if (resultMessageSize > lastIndex) {
                result.append(apiResponseMessage.getValue().get(0).substring(firstIndex, lastIndex));
                sendMessage(event, checkGeneralMessage(1, apiResponseMessage.getKey(), inddex), discordEmbedMessage.setDescription(result));

            } else {
                result.append(apiResponseMessage.getValue().get(0).substring(firstIndex));
                createCommonEmbed(commonTitle.length() + resultMessageSize, discordEmbedMessage);
                sendMessage(event, checkGeneralMessage(0, "", inddex), discordEmbedMessage.setDescription(result));
            }

            result.delete(0, result.length());
        }
    }

    private static String checkGeneralMessage(int index, String key, int loopCount) {
        if ((index == 0) && (key.equals(""))) {
            return "마지막 내용이에요. 😀";
        } else if ((index == 1) && (key.equals(""))) {
            return loopCount + " 번째 내용이에요. 😎";
        } else if ((index == 2) && (key.equals(""))) {
            return "검색 결과가 하나 밖에 없어요! 😎";
        } else {
            return key;
        }
    }

    /**
     * <b>Discord Bot이 보낼 Message 크기를 Discord 에서 한번에 보낼 메시지 크기와 대조하여 넘거나, 같다면 잘라서 보내기 위한 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     * @param discordEmbedMessage Discord Message 중 Embed 형태로 보내기 위한 객체
     * @param resultMessage API를 통해 조회된 결과가 나눠 담긴 문자열 값을 합치기 위한 StringBuilder 객체
     * @param loopCount GiggalDiscordListener.createSendMessage() 의 반복문 반복 회수
     */

    private static void checkResultMessageSize(MessageReceivedEvent event, EmbedBuilder discordEmbedMessage, Map.Entry<String, List<String>> apiResponseMessage, StringBuilder resultMessage, int loopCount) {
        int index = 0;
        createCommonEmbed(resultMessage.length(), discordEmbedMessage);

        if (resultMessage.length() >= 1950) {
            if (loopCount == 0) {
                index = 2;
            } else {
                index = 1;
            }

            sendMessage(event, checkGeneralMessage(index, apiResponseMessage.getKey(), loopCount), discordEmbedMessage.setDescription(resultMessage));
            resultMessage.delete(0, resultMessage.length());
        }
    }

    /**
     * <b>Embed Message를 보낼 때, 기본적으로 공통으로 들어갈 내용을 담을 Method</b>
     * @param resultMessageSize Discord에 보내질 Message 문자열 총 크기
     * @return 기본적으로 공통으로 들어갈 내용을 담은 Embed 객체
     */

    public static EmbedBuilder createCommonEmbed (int resultMessageSize, EmbedBuilder embed) {
        int discordAllowMessageSize = 4096;
        embed.setFooter("\n\n\n Discord Bot Message 크기 정보 : " + resultMessageSize + "/" + discordAllowMessageSize + "\n\n\n Giggal People Chat Ops use Discord \n ⓒ 2023. 기깔나는 사람들(giggals.pepole@gmail.com) All Rights Reserved. \n Blog : https://giggal-people.tistory.com \n Bot Version : 0.0.0b \n");
        if (embed.isEmpty()) {
            embed.setTitle("기깔나는 사람들");
            embed.setColor(Color.green);
        }
        return embed;
    }

    /**
     * <b>디스코드 응답 메시지 중 일반 메시지로 보낼 Message와 Embed로 보낼 Message 구별하기 위한 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     * @param resultMap 디스코드 봇 응답 Message를 담은 Map
     * @param discordEmbedMessage Discord Message 중 Embed 형태로 보내기 위한 객체
     */

    public static void createSendMessage(MessageReceivedEvent event, Map<String, List<String>> resultMap, EmbedBuilder discordEmbedMessage) {
        for (Map.Entry<String, List<String>> apiResponseMessage : resultMap.entrySet()) {
            if (apiResponseMessage.getValue().isEmpty()) {
                createEmbedMessage(event, apiResponseMessage.getKey());
            } else {
                createEmbedGeneralMessage(event, resultMap);
            }
        }
        discordEmbedMessage.clear();
    }

    /**
     * <b>resultList가 2일 때, 즉, 디스코드 일반, Embed 응답 메시지를 만들기 위한 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     * @param resultMap 디스코드 봇 응답 Message를 담은 Map
     */

    private static void createEmbedGeneralMessage(MessageReceivedEvent event, Map<String, List<String>> resultMap) {
        int loopCount = 0;
        EmbedBuilder discordEmbedMessage = new EmbedBuilder();
        StringBuilder resultMessage = new StringBuilder();

        for (Map.Entry<String, List<String>> apiResponseMessage : resultMap.entrySet()) {

            if (apiResponseMessage.getValue().isEmpty()) {
                throw new DiscordBotException(NOT_FOUND, NOT_FOUND.getMessage());
            }

            for (String searchResult : apiResponseMessage.getValue()) {
                if (searchResult.equals("")) {
                    break;
                }

                DiscordBotResponseMessageUtil.messageSizeProcesses(event, discordEmbedMessage, apiResponseMessage, searchResult, resultMessage, loopCount);
                loopCount += 1;
            }

            if (!resultMessage.toString().equals("")) {
                sendMessage(event, "마지막 내용이에요. 😀", discordEmbedMessage.setDescription(resultMessage));
            }
        }
    }

    /**
     * <b>Discord Bot을 통해 API 호출로 목록 조회를 했을 때, Paging 처리를 위한 Page 번호 계산 Method</b>
     * @param pageNum API를 통해 전달된 현재 Page 번호
     * @return 현재 Page 번호
     */
    private static int checkCriteriaPageNum(int pageNum) {
        return pageNum <= 0 ? 1 : pageNum;
    }

    /**
     * <b>Discord Bot을 통해 API 호출로 목록 조회를 했을 때, Paging 처리를 위한 마지막 Page 번호 계산 Method</b>
     * @param endPage API 조회 결과 마지막 Page 번호
     * @return 마지막 Page 번호
     */
    private static int checkCriteriaLastPageNum(int endPage) {
        return endPage <= 0 ? 1 : endPage;
    }

    /**
     * <b>Discord Bot을 통해 API 호출로 목록 조회를 했을 때, Paging 처리를 위한 이전, 다음 Page 이동 가능 여부 계산 Method</b>
     * @param checkMove API 조회 이동 가능 여부
     * @return 이동 가능 여부를 한글로 보기 편하게 변경하여 반환
     */
    private static String checkPaginationMove(boolean checkMove) {
        return !checkMove ? "이동 불가" : "이동 가능";
    }

    /**
     * <b>Discrod Bot을 통해 API 호출로 결과값을 얻고자 할 때, 명령어가 잘 못 되었을 때 Message를 반환하기 위한 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     * @param searchType 크루 조회인지 지원자 조회인지 여부
     */
    public static void createAPICallErrorMessage(MessageReceivedEvent event, String searchType) {
        String generalMessage = "명령어 확인이 필요해요 🤔";
        EmbedBuilder errorEmbedBuilder = new EmbedBuilder();
        errorEmbedBuilder.setColor(Color.RED);

        String errorEmbedMessage = event.getAuthor().getAsMention() + " 님 " + searchType + " 조회를 명령했지만, 명령어가 잘못 되었어요. 😢 \n 기깔아 명령어 라는 명령어를 통해 명령어를 확인해 주세요 🫡";
        errorEmbedBuilder.setDescription(errorEmbedMessage);

        DiscordBotResponseMessageUtil.createCommonEmbed(generalMessage.length() + errorEmbedMessage.length(), errorEmbedBuilder);
        DiscordBotResponseMessageUtil.sendMessage(event, generalMessage, errorEmbedBuilder);

        throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
    }

    /**
     * <b>resultList 크기가 1일 때, 즉 Embed Message 만 처리할 때 사용될 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     * @param resultMessage 디스코드 봇 응답 Message를 담은 Embed 형태의 응답 Message 처리
     */

    private static void createEmbedMessage(MessageReceivedEvent event, String resultMessage) {
        EmbedBuilder discordEmbedMessage = DiscordBotResponseMessageUtil.createCommonEmbed(resultMessage.length(), new EmbedBuilder());
        DiscordBotResponseMessageUtil.sendMessage(event, event.getAuthor().getAsMention(), discordEmbedMessage.setDescription(resultMessage));
    }

    /**
     * <b>실제 디스코드로 응답 Message 보내는 Method</b>
     * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
     * @param returnMessage 디스코드 일반 형태의 응답 Message 내용
     * @param embed 디스코드 Embed 형태의 응답 Message 내용
     */

    public static void sendMessage(MessageReceivedEvent event, String returnMessage, EmbedBuilder embed) {
        event.getChannel().asTextChannel().sendMessage(returnMessage).setEmbeds(embed.build()).queue();
    }

    public static boolean checkStatusErrorReturnMessage(Map<String, List<String>> resultMap) {
        for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
            List<String> returnEmbedMessage = entry.getValue();

            if (returnEmbedMessage.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * <b>Discord Bot을 통해 서버 자원량 조회를 하기 위한 Method</b>
     * @param commandOptions 사용자가 Discord에서 입력한 명령어 Option
     * @return 서버 자원 정보
     */
    public static String getServerResourceInfo(MessageReceivedEvent event, List<String> commandOptions) {
        ServerResourceCheck serverResourceCheck = new ServerResourceCheckImpl();
        String result = "";
        String[] splitCommandOption = new String[2];
        StringBuilder userCommandOption = new StringBuilder();

        if (commandOptions.isEmpty()) {
            result = createFindByServerResourceInfoList(serverResourceCheck.getServerResourcesInfo(0,1)).toString();

        } else {
            for (int index = 0; index < commandOptions.size(); index++) {
                splitCommandOption[index] = Arrays.toString(commandOptions.get(index).split("="));
                userCommandOption.append(splitCommandOption[index]);
            }
        }

        String replaceUserCommandOption = userCommandOption.toString().replace("[", "").replace("]", ",").replace(" ", "");
        String[] sendCommandOption = replaceUserCommandOption.split(",");

        if (sendCommandOption[0].replace("-", "").equals("밀리초당") && sendCommandOption.length <= 2) {
            createAPICallErrorMessage(event, replaceUserCommandOption.replace(",", "="));
        } else if (sendCommandOption[0].replace("-", "").equals("밀리초당") && sendCommandOption[2].replace("-", "").equals("조회횟수")) {
            result = createFindByServerResourceInfoList(serverResourceCheck.getServerResourcesInfo(Integer.parseInt(sendCommandOption[1]), Integer.parseInt(sendCommandOption[3]))).toString();
        } else if (sendCommandOption[0].replace("-", "").equals("조회횟수")) {
            result = createFindByServerResourceInfoList(serverResourceCheck.getServerResourcesInfo(0, Integer.parseInt(sendCommandOption[1]))).toString();
        }
        return result;
    }

    /**
     * <b>Server 자원 조회 뒤 반환 문자열을 보기 좋게 가공하기 위한 Method</b>
     * @param serverResourcesInfoList 외부 Class를 통해 조회된 서버 자원 정보
     * @return 가공된 서버 자원 정보
     */

    private static StringBuilder createFindByServerResourceInfoList(List<String> serverResourcesInfoList) {
        StringBuilder result = new StringBuilder();

        if (serverResourcesInfoList.size() == 3) {
            return result.append("CPU 자원 정보 : ")
                    .append(serverResourcesInfoList.get(0))
                    .append("Memory 자원 정보 : ")
                    .append(serverResourcesInfoList.get(1))
                    .append("Disk 자원 정보 : ")
                    .append(serverResourcesInfoList.get(2));

        } else if (serverResourcesInfoList.size() >= 3) {
            for (int index = 0; index < serverResourcesInfoList.size(); index++) {
                if (index == 0 || index % 4 == 0) {
                    result.append(serverResourcesInfoList.get(index));
                } else {
                    result.append(serverResourcesInfoList.get(index));
                }
            }
            return result;
        } else {
            throw new DiscordBotException(BAD_REQUEST, BAD_REQUEST.getMessage());
        }
    }

    /**
     * <b>권한 없는 Crew가 Discord Bot을 통해 상위 권한만 이용 가능한 명령어를 입력했을 경우 반환할 Message를 만드는 Method</b>
     * @return 이용 불가 안내 Message
     */
    public static String unAuthorization() {
        return "해당 명령어를 이용할 수 있는 권한이 없습니다. 소속 팀장님께 문의해 주세요 😎";
    }
}
