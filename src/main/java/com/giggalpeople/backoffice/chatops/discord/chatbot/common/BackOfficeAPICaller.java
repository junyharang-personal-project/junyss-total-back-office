package com.giggalpeople.backoffice.chatops.discord.chatbot.common;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.API_RESPONSE_NOT_FOUND;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.giggalpeople.backoffice.api.common.constant.APIUriInfo;
import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorRecordSaveRequestDto;
import com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util.DiscordBotResponseMessageUtil;
import com.giggalpeople.backoffice.chatops.discord.chatbot.command.constant.CheckDiscordCommand;
import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.DiscordBotException;
import com.giggalpeople.backoffice.chatops.discord.chatbot.util.HttpUtil;
import com.giggalpeople.backoffice.common.enumtype.CrewGrade;
import com.giggalpeople.backoffice.common.util.ApiCallUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>API 호출을 위한 객체</b></h2>
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BackOfficeAPICaller {

	/**
	 * <b>API 크루 혹은 지원자 목록 조회 호출 담당 Method</b>
	 * @param url API 호출을 위한 URL
	 * @return API 호출을 통해 얻은 Data Base에서 조회된 값
	 */

	public static List<String> callPeopleManagementListSearchApi(MessageReceivedEvent event, URL url) {
		StringBuilder resultMessage = new StringBuilder();
		try {
			JSONObject jsonObject = ApiCallUtil.callDiscordBotGetAPI(url);

			if (jsonObject.getInt("statusCode") != 200) {
				DiscordBotResponseMessageUtil.createErrorMessage(event, jsonObject);
				throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
			}

			return createPeopleManagementResponseMessage(jsonObject, url, resultMessage);
		} catch (IOException ioException) {
			throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
		}
	}

	/**
	 * <b>Error Log 목록 조회 API 호출 Method</b>
	 * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url API 호출을 위한 URL
	 * @return API 호출을 통해 얻은 Data Base에서 조회된 값
	 */

	public static List<String> callErrorLogListSearchApi(MessageReceivedEvent event, URL url) {
		StringBuilder resultMessage = new StringBuilder();
		try {
			JSONObject jsonObject = ApiCallUtil.callDiscordBotGetAPI(url);

			if (jsonObject.getInt("statusCode") != 200) {
				DiscordBotResponseMessageUtil.createErrorMessage(event, jsonObject);
				throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
			}

			return createErrorLogResponseMessage(jsonObject, resultMessage);
		} catch (IOException ioException) {
			throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
		}
	}

	/**
	 * <b>이용자 접속 및 요청 목록 조회 API 호출 Method</b>
	 * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url API 호출을 위한 URL
	 * @return API 호출을 통해 얻은 Data Base에서 조회된 값
	 */

	public static List<String> callUserInfoListSearchApi(MessageReceivedEvent event, URL url) {
		StringBuilder resultMessage = new StringBuilder();
		try {
			JSONObject jsonObject = ApiCallUtil.callDiscordBotGetAPI(url);

			if (jsonObject.getInt("statusCode") != 200) {
				DiscordBotResponseMessageUtil.createErrorMessage(event, jsonObject);
				throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
			}

			return createUserInfoResponseMessage(jsonObject, resultMessage);
		} catch (IOException ioException) {
			throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
		}
	}

	/**
	 * <b>Discord 사용자 권한 검사 및 이용자 접속 및 요청 상세 정보 API 호출 Method</b>
	 * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url API 호출을 위한 URL
	 * @return API 호출을 통해 얻은 Data Base에서 조회된 값
	 */

	public static String userInfoDetailAPICall(MessageReceivedEvent event, URL url) throws IOException {
		CrewGrade crewGrade = CheckDiscordCommand.checkCrewGradeString(
			event.getMessage().getMember().getRoles().get(0).getName());

		if (crewGrade.getGradeNum() > 3) {
			return DiscordBotResponseMessageUtil.unAuthorization();
		}

		JSONObject jsonObject = ApiCallUtil.callDiscordBotGetAPI(url);

		if (jsonObject.getInt("statusCode") != 200) {
			DiscordBotResponseMessageUtil.createErrorMessage(event, jsonObject);
			return "";
		}

		return DiscordBotResponseMessageUtil.forLeaderCreateDetailUserInfoSearchResponseMessage(
			Objects.requireNonNull(
					ApiCallUtil.callDiscordBotGetAPI(url))
				.getJSONObject("data"));
	}

	/**
	 * <b>호출할 API 별 응답 메시지를 만들기 위한 Method</b>
	 * @param jsonObject API에서 통해 얻은 응답 JSON Type 객체
	 * @param url API 호출을 위한 URL
	 * @param resultMessage 응답 메시지를 담기 위한 StringBuilder 객체
	 * @return 가공된 Discord Bot 응답 메시지
	 */

	private static List<String> createPeopleManagementResponseMessage(JSONObject jsonObject, URL url,
		StringBuilder resultMessage) {
		JSONArray data = Objects.requireNonNull(jsonObject).getJSONArray("data");
		JSONObject pagination = Objects.requireNonNull(jsonObject).getJSONObject("pagination");
		List<String> resultList = new ArrayList<>();

		for (int index = 0; index < data.length(); index++) {
			JSONObject dataJSONObject = data.getJSONObject(index);
			String[] crewNumberSplit = dataJSONObject.getString("crewNumber").split("-");
			String indexOfSuggest = crewNumberSplit[2];

			if (HttpUtil.discordBotSearchAPIURL(url)) {    /* 크루 전체 목록 조회 시 */
				resultMessage.append(
					DiscordBotResponseMessageUtil.createFindByCrewList(indexOfSuggest, dataJSONObject));
				checkLastIndex(index, data, resultMessage, pagination);

			} else {                                        /* 지원자 전체 목록 조회 시 */
				resultMessage.append(
					DiscordBotResponseMessageUtil.createFindBySuggestList(indexOfSuggest, dataJSONObject));
				checkLastIndex(index, data, resultMessage, pagination);
			}

			resultList.add(resultMessage.toString());
			resultMessage.delete(0, resultMessage.length());
		}
		return resultList;
	}

	private static List<String> createErrorLogResponseMessage(JSONObject jsonObject, StringBuilder resultMessage) {
		JSONArray data = Objects.requireNonNull(jsonObject).getJSONArray("data");
		JSONObject pagination = Objects.requireNonNull(jsonObject).getJSONObject("pagination");
		List<String> resultList = new ArrayList<>();

		for (int index = 0; index < data.length(); index++) {
			JSONObject dataJSONObject = data.getJSONObject(index);
			long logId = dataJSONObject.getInt("logId");

			resultMessage.append(DiscordBotResponseMessageUtil.createFindByErrorLogList(logId, dataJSONObject));
			checkLastIndex(index, data, resultMessage, pagination);
			resultList.add(resultMessage.toString());
			resultMessage.delete(0, resultMessage.length());
		}
		return resultList;
	}

	private static List<String> createUserInfoResponseMessage(JSONObject jsonObject, StringBuilder resultMessage) {
		JSONArray data = Objects.requireNonNull(jsonObject).getJSONArray("data");
		JSONObject pagination = Objects.requireNonNull(jsonObject).getJSONObject("pagination");
		List<String> resultList = new ArrayList<>();

		for (int index = 0; index < data.length(); index++) {
			JSONObject dataJSONObject = data.getJSONObject(index);
			long connectedUserRequestInfoID = dataJSONObject.getInt("connectedUserRequestInfoID");

			resultMessage.append(
				DiscordBotResponseMessageUtil.createFindByUserInfoList(connectedUserRequestInfoID, dataJSONObject));
			checkLastIndex(index, data, resultMessage, pagination);
			resultList.add(resultMessage.toString());
			resultMessage.delete(0, resultMessage.length());
		}
		return resultList;
	}

	/**
	 * <b>API 조회 결과 반복문 회수를 통해 마지막 반복문일 경우 반환 Message에 Paging 정보를 넣기 위한 Method</b>
	 * @param pagination API 조회 결과의 따른 Pagination 정보
	 */
	private static void checkLastIndex(int index, JSONArray data, StringBuilder resultMessage, JSONObject pagination) {
		if ((index == data.length() - 1 && data.length() == 1) || (index == data.length() - 1 && data.length() > 1)) {
			resultMessage.append(DiscordBotResponseMessageUtil.createPaginationInfo(pagination));
		}
	}

	/**
	 * <b>Discord Bot을 통해 지원자 상세 조회 API 호출하고, 해당 Response Status Code를 통해 반환 결과를 만드는 Method</b>
	 * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url API 호출을 위한 URL
	 * @return API 호출 이 후 반환된 결과 값
	 * @throws IOException URL 처리 중 발생한 Input Output 에 대한 Exception 던지기
	 */
	public static String suggestDetailAPICall(MessageReceivedEvent event, URL url) throws IOException {
		JSONObject jsonObject = ApiCallUtil.callDiscordBotGetAPI(url);

		if (jsonObject.getInt("statusCode") != 200) {
			DiscordBotResponseMessageUtil.createErrorMessage(event, jsonObject);
			return "";
		}
		return DiscordBotResponseMessageUtil.createDetailSuggestSearchResponseMessage(
			Objects.requireNonNull(ApiCallUtil.callDiscordBotGetAPI(url)).getJSONObject("data"));
	}

	/**
	 * <b>Discord Bot을 통해 크루 상세 조회 API 호출하고, 해당 Response Status Code를 통해 반환 결과를 만드는 Method</b>
	 * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url API 호출을 위한 URL
	 * @return API 호출 이 후 반환된 결과 값
	 * @throws IOException URL 처리 중 발생한 Input Output 에 대한 Exception 던지기
	 */

	public static String crewDetailAPICall(MessageReceivedEvent event, URL url) throws IOException {
		JSONObject jsonObject = ApiCallUtil.callDiscordBotGetAPI(url);

		if (jsonObject.getInt("statusCode") != 200) {
			DiscordBotResponseMessageUtil.createErrorMessage(event, jsonObject);
			return "";
		}

		return DiscordBotResponseMessageUtil.createDetailCrewSearchResponseMessage(
			Objects.requireNonNull(ApiCallUtil.callDiscordBotGetAPI(url)).getJSONObject("data"));
	}

	public static String errorLogDetailApiCall(MessageReceivedEvent event, URL url) throws IOException {
		CrewGrade crewGrade = CheckDiscordCommand.checkCrewGradeString(
			event.getMessage().getMember().getRoles().get(0).getName());
		JSONObject jsonObject = ApiCallUtil.callDiscordBotGetAPI(url);

		if (jsonObject.getInt("statusCode") != 200) {
			DiscordBotResponseMessageUtil.createErrorMessage(event, jsonObject);
			return "";
		}

		JSONObject apiResponseData = Objects.requireNonNull(ApiCallUtil.callDiscordBotGetAPI(url))
			.getJSONObject("data");

		if (crewGrade.getGradeNum() > 3) {
			return DiscordBotResponseMessageUtil.forGeneralCrewCreateDetailErrorLogSearchResponseMessage(
				apiResponseData);
		} else {
			return DiscordBotResponseMessageUtil.forLeaderCreateDetailErrorLogSearchResponseMessage(apiResponseData);
		}
	}

	/**
	 * <b>Log Back을 통한 Log 저장 API를 호출하여 Data Base에 Log를 저장하기 위하여 API 호출 처리를 위한 Method</b>
	 * @param totalErrorRecordSaveRequestDto Log 정보를 담은 DTO
	 * @throws IOException JSONObject 처리에 문제 발생 시 발생하는 Exception
	 */
	public static void logSaveApiCall(TotalErrorRecordSaveRequestDto totalErrorRecordSaveRequestDto) throws
		IOException {
		StringBuilder suffixUrl = new StringBuilder(APIUriInfo.LOG);
		ApiCallUtil.callPostApi(suffixUrl, totalErrorRecordSaveRequestDto);
	}
}