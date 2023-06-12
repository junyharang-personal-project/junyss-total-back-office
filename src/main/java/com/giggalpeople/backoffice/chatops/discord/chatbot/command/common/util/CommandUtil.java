package com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util;

import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_CALLER_DISCORD_BOT;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_CREW;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_SUGGEST;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.CONNECTED_USER;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.LOG;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.giggalpeople.backoffice.api.common.constant.APIUriInfo;
import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSearchType;
import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSuggestSearchType;
import com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorLogSearchType;
import com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType;
import com.giggalpeople.backoffice.chatops.discord.chatbot.common.BackOfficeAPICaller;
import com.giggalpeople.backoffice.chatops.discord.chatbot.common.message.DiscordBotResponseMessage;
import com.giggalpeople.backoffice.chatops.discord.chatbot.util.HttpUtil;
import com.giggalpeople.backoffice.common.env.Environment;
import com.giggalpeople.backoffice.common.util.DataTypeChangerUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>Discord Bot에게 사용자가 내린 명령어 처리를 위한 Util Class</b></h2>
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandUtil {

	/**
	 * <b>사용자가 디스코드 봇을 호출한 뒤 작성한 명령어를 분석하여 응답 메시지를 만들기 위한 Method</b>
	 *
	 * @param event       Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userMessage 사용자가 보낸 메시지를 공백을 통해 자른 뒤 만든 문자열 배열
	 * @return 디스코드 봇이 응답할 문자열 메시지
	 */

	public static String checkBodyExistCommand(MessageReceivedEvent event, String userMessage) {
		return event.getAuthor().getName() + DiscordBotResponseMessage.existCommandRandomMessage(userMessage);
	}

	/**
	 * <b>사용자가 디스코드 봇을 통해 명령어를 알고 싶을 때, 응답 메시지를 만들기 위한 Method</b>
	 * @return 디스코드 봇이 응답할 문자열 메시지
	 */

	public static String commandManual(MessageReceivedEvent event) {
		return event.getAuthor().getName() + DiscordBotResponseMessage.commandManual();
	}

	public static Map<String, List<String>> movementFromCommand(MessageReceivedEvent event,
		Map<String, List<String>> userCommandMap) throws IOException {
		Map<String, List<String>> resultMap = new HashMap<>();

		for (Map.Entry<String, List<String>> entry : userCommandMap.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();

			switch (key) {
				case "크루목록조회":
					resultMap.put(event.getAuthor().getName() + "님 기깔나는 사람들 크루 전체 정보는 아래와 같아요!",
						BackOfficeAPICaller.callPeopleManagementListSearchApi(event,
							new URL(HttpUtil.crewSearchCreateURL(event, userCommandMap))));
					break;
				case "크루상세조회":
					resultMap.put(event.getAuthor().getName() + "님 기깔나는 사람들 해당 크루 상세 정보는 아래와 같아요!",
						Collections.singletonList(BackOfficeAPICaller.crewDetailAPICall(event,
							new URL(HttpUtil.crewDetailCreateURL(event, value)))));
					break;
				case "지원자목록조회":
					resultMap.put(event.getAuthor().getName() + "님 기깔나는 사람들 지원자 전체 정보는 아래와 같아요!",
						BackOfficeAPICaller.callPeopleManagementListSearchApi(event,
							new URL(HttpUtil.suggestSearchCreateURL(event, userCommandMap))));
					break;
				case "지원자상세조회":
					resultMap.put(event.getAuthor().getName() + "님 기깔나는 사람들 해당 지원자 상세 정보는 아래와 같아요!",
						Collections.singletonList(BackOfficeAPICaller.suggestDetailAPICall(event,
							new URL(HttpUtil.suggestDetailCreateURL(event, value)))));
					break;
				case "이용자목록조회":
					resultMap.put(
						event.getAuthor().getName() + "님 기깔나는 서비스 Application 이용자 접속 및 요청 정보 목록 조회 결과는 아래와 같아요!",
						BackOfficeAPICaller.callUserInfoListSearchApi(event,
							new URL(HttpUtil.userInfoSearchCreateURL(event, userCommandMap))));
					break;
				case "이용자상세조회":
					resultMap.put(event.getAuthor().getName() + "님 기깔나는 서비스 Application 이용자 접속 및 요청 정보는 아래와 같아요!",
						Collections.singletonList(BackOfficeAPICaller.userInfoDetailAPICall(event,
							new URL(HttpUtil.userInfoDetailCreateURL(event, value)))));
					break;
				case "로그목록조회":
					resultMap.put(event.getAuthor().getName() + "님 서버 에러 목록 조회 결과는 아래와 같아요!",
						BackOfficeAPICaller.callErrorLogListSearchApi(event,
							new URL(HttpUtil.errorLogSearchCreateURL(event, userCommandMap))));
					break;
				case "로그상세조회":
					resultMap.put(event.getAuthor().getName() + "님 기깔나는 사람들 해당 Error Log 정보는 아래와 같아요!",
						Collections.singletonList(BackOfficeAPICaller.errorLogDetailAPICall(event,
							new URL(HttpUtil.errorLogDetailCreateURL(event, value)))));
					break;
				case "서버자원조회":
					resultMap.put(event.getAuthor().getName() + "님 기깔나는 사람들 해당 WAS 자원 정보는 아래와 같아요!",
						Collections.singletonList(DiscordBotResponseMessageUtil.getServerResourceInfo(event, value)));
					break;
				case "명령어":
					resultMap.put(event.getAuthor().getName() + "님 저에게 명령을 하시려면 이렇게 말씀해 주셔야 해요!",
						Collections.singletonList(CommandUtil.commandManual(event)));
					break;
				default:
					resultMap.put(
						event.getAuthor().getName() + DiscordBotResponseMessage.existCommandRandomMessage("안녕"),
						Collections.singletonList(CommandUtil.checkBodyExistCommand(event, key)));
					break;
			}
		}

		if (DiscordBotResponseMessageUtil.checkStatusErrorReturnMessage(resultMap)) {
			return resultMap;
		}
		return Collections.emptyMap();
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 원하는 것을 찾기 위하여 입력한 명령어 분석 Method</b>
	 *
	 * @param userCommand 사용자가 입력한 명령어
	 * @return 조건에 맞는 명령어 입력시 true, 아니면 false 반환
	 */
	public static boolean commandCheck(String userCommand) {
		String crew = "크루";
		String suggest = "지원자";
		String userInfo = "이용자";
		String errorLog = "로그";
		String commonListSearchKeyWord = "목록조회";
		String commonDetailSearchKeyWord = "상세조회";

		return userCommand.equals("서버자원조회") ||
			userCommand.equals(errorLog + commonListSearchKeyWord) ||
			userCommand.equals(userInfo + commonListSearchKeyWord) ||
			userCommand.equals(errorLog + commonDetailSearchKeyWord) ||
			userCommand.equals(userInfo + commonDetailSearchKeyWord) ||
			userCommand.equals(suggest + commonListSearchKeyWord) ||
			userCommand.equals(crew + commonListSearchKeyWord) ||
			userCommand.equals(suggest + commonDetailSearchKeyWord) ||
			userCommand.equals(crew + commonDetailSearchKeyWord);
	}

	public static boolean checkSearchCommand(String key) {
		return key.matches(CrewSearchType.CREW_NUMBER.getDescription())
			|| key.matches(CrewSuggestSearchType.HOW_KNOW_INFO.getDescription())
			|| key.matches(CrewSuggestSearchType.NAME.getDescription())
			|| key.matches(CrewSuggestSearchType.SEX.getDescription())
			|| key.matches(CrewSuggestSearchType.BIRTH_DATE.getDescription())
			|| key.matches(CrewSuggestSearchType.AGE_INFO.getDescription())
			|| key.matches(CrewSuggestSearchType.MBTI.getDescription())
			|| key.matches(CrewSuggestSearchType.LAST_EDUCATIONAL.getDescription())
			|| key.matches(CrewSuggestSearchType.SCHOOL_NAME.getDescription())
			|| key.matches(CrewSuggestSearchType.STATION_NAME.getDescription())
			|| key.matches(CrewSuggestSearchType.PRIVACY_INFO_AGREE.getDescription())
			|| key.matches(CrewSuggestSearchType.ADDENDUM_INFO_AGREE.getDescription())
			|| key.matches(CrewSuggestSearchType.PARTICIPATION.getDescription())
			|| key.matches(CrewSuggestSearchType.SUGGEST_PART.getDescription());
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 입력한 명령어의 옵션 값 처리를 위해 '=' 문자를 분리하여 명령어 옵션 List에 담기 위한 Method</b>
	 *
	 * @param searchCommandOption Discord Bot을 통해 전달된 사용자 명령어
	 * @return '=' 문자를 기준으로 분리된 명령어 옵션 List
	 */

	public static List<String> splitUserCommandOption(List<String> searchCommandOption) {
		List<String> userCommandOptionKeyValue = new ArrayList<>();
		for (String allCommandOption : searchCommandOption) {
			String[] splitCommandOption = allCommandOption.split("=");
			userCommandOptionKeyValue.addAll(Arrays.asList(splitCommandOption));
		}
		return userCommandOptionKeyValue;
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 입력한 명령어와 옵션을 Map에 Key와 Value로 담기 위한 Method</b>
	 *
	 * @param userCommandOptionKeyValue 공백을 기준으로 문자열을 잘라 담은 사용자 명령어가 담긴 List
	 * @param userCommand               사용자가 입력한 명령어와 옵션을 Map에 Key와 Value로 담은 Map
	 */

	public static void insertInCommandOptionMap(List<String> userCommandOptionKeyValue,
		Map<String, String> userCommand) {
		String mapKey = "";
		String mapValue = "";

		for (int index = 0; index < userCommandOptionKeyValue.size(); index++) {
			if ((index % 2) == 0) {
				mapKey = userCommandOptionKeyValue.get(index);
			} else {
				mapValue = userCommandOptionKeyValue.get(index);
			}
			userCommand.put(mapKey, mapValue);
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 페이징 처리를 하기 위한 명령어 입력시 지원자 목록 조회 관련 API 호출을 위한 URN을 만들기 위한 Method</b>
	 *
	 * @param url   기본적으로 API 호출을 위한 URL
	 * @param key   사용자가 입력한 명령어
	 * @param value 사용자가 입력한 명령어 Option
	 */

	public static void checkUserCommandSearchSuggestForPagination(StringBuilder url, String key, String value) {
		if (key.contains("현재페이지")) {
			url.delete(0, url.length());
			url.append(Environment.initPrefixAPIURL()).append(APIUriInfo.API_SUGGEST + "?page=").append(value);

		} else if (key.contains("데이터출력개수")) {
			String checkUrl = url.toString();

			if (checkUrl.contains(Environment.initPrefixAPIURL() + API_SUGGEST + "?page=")) {
				url.append("&perPageNum=").append(value);

			} else {
				url.delete(0, url.length());
				url.append(API_SUGGEST + "?page=")
					.append(0)
					.append("&perPageNum=")
					.append(value);
			}
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 페이징 처리를 하기 위한 명령어 입력시 크루 목록 조회 관련 API 호출을 위한 URN을 만들기 위한 Method</b>
	 *
	 * @param url   기본적으로 API 호출을 위한 URL
	 * @param key   사용자가 입력한 명령어
	 * @param value 사용자가 입력한 명령어 Option
	 */

	public static void checkUserCommandForSearchCrewPagination(StringBuilder url, String key, String value) {
		if (key.contains("현재페이지")) {

			url.delete(0, url.length());
			url.append(Environment.initPrefixAPIURL()).append(API_CREW + "?page=").append(value);

		} else if (key.contains("데이터출력개수")) {
			String checkUrl = url.toString();

			if (checkUrl.contains(Environment.initPrefixAPIURL() + API_CREW + "?page=")) {
				url.append("&perPageNum=").append(value);

			} else {
				url.delete(0, url.length());
				url.append(API_CREW + "?page=")
					.append(0)
					.append("&perPageNum=")
					.append(value);
			}
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 페이징 처리를 하기 위한 명령어 입력시 에러 로그 목록 조회 관련 API 호출을 위한 URN을 만들기 위한 Method</b>
	 * @param url   기본적으로 API 호출을 위한 URL
	 * @param key   사용자가 입력한 명령어
	 * @param value 사용자가 입력한 명령어 Option
	 */

	public static void checkUserCommandForSearchErrorLogPagination(StringBuilder url, String key, String value) {
		if (key.contains("현재페이지")) {

			url.delete(0, url.length());
			url.append(Environment.initPrefixAPIURL())
				.append(API_CALLER_DISCORD_BOT + LOG + "/lists?page=")
				.append(value);

		} else if (key.contains("데이터출력개수")) {
			String checkUrl = url.toString();

			if (checkUrl.contains(Environment.initPrefixAPIURL() + API_CALLER_DISCORD_BOT + LOG + "/lists?page=")) {
				url.append("&perPageNum=").append(value);

			} else {
				url.delete(0, url.length());
				url.append(API_CALLER_DISCORD_BOT + LOG + "/lists?page=")
					.append(0)
					.append("&perPageNum=")
					.append(value);
			}
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 페이징 처리를 하기 위한 명령어 입력시 이용자 접속 및 요청 정보 목록 조회 관련 API 호출을 위한 URN을 만들기 위한 Method</b>
	 * @param url   기본적으로 API 호출을 위한 URL
	 * @param key   사용자가 입력한 명령어
	 * @param value 사용자가 입력한 명령어 Option
	 */
	public static void checkUserCommandForSearchUserInfoPagination(StringBuilder url, String key, String value) {
		if (key.contains("현재페이지")) {
			url.delete(0, url.length());
			url.append(Environment.initPrefixAPIURL())
				.append(API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists?page=").append(value);

		} else if (key.contains("데이터출력개수")) {
			String checkUrl = url.toString();

			if (checkUrl.contains(
				Environment.initPrefixAPIURL() + API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists?page=")) {
				url.append("&perPageNum=").append(value);

			} else {
				url.delete(0, url.length());
				url.append(API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists?page=")
					.append(0)
					.append("&perPageNum=")
					.append(value);
			}
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 날짜 범위 검색 처리하기 위한 명령어 입력시 API 호출을 위한 URN을 만들기 위한 Method </b>
	 *
	 * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url   기본적으로 API 호출을 위한 URL
	 * @param key   사용자가 입력한 명령어
	 * @param value 사용자가 입력한 명령어 Option
	 */

	public static boolean processorUrnUserCommandForDate(MessageReceivedEvent event, StringBuilder url, String key,
		String value) {
		if (DataTypeChangerUtil.checkDateRangeTypeForSearchWord(value)) {
			String[] searchDate = value.split("~");
			url.append("&startDate=");
			url.append(URLEncoder.encode(searchDate[0], StandardCharsets.UTF_8));
			url.append("&endDate=");
			url.append(URLEncoder.encode(searchDate[1], StandardCharsets.UTF_8));
			url.append("&inputSearchType=");
			url.append(key);

			return true;

		} else if (DataTypeChangerUtil.checkDateTypeForSearchWord(value)) {
			url.append("&date=");
			url.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
			url.append("&inputSearchType=");
			url.append(key);

			return true;

		} else {
			String generalMessage = "명령어 확인이 필요해요 🤔";
			String errorEmbedMessage = event.getAuthor().getAsMention()
				+ "님 날짜 검색을 명령 하셨으나, 날짜 범위 값이 잘 못 입력 되었어요! 날짜 검색 방법 : yyyy-MM-dd~yyyy-MM-dd";
			EmbedBuilder errorEmbedBuilder = new EmbedBuilder();
			errorEmbedBuilder.setColor(Color.RED);
			errorEmbedBuilder.setDescription(errorEmbedMessage);
			DiscordBotResponseMessageUtil.createCommonEmbed(generalMessage.length() + errorEmbedMessage.length(),
				errorEmbedBuilder);
			DiscordBotResponseMessageUtil.sendMessage(event, generalMessage, errorEmbedBuilder);

			return false;
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 나이 범위 검색 처리하기 위한 명령어 입력시 API 호출을 위한 URN을 만들기 위한 Method </b>
	 *
	 * @param url   기본적으로 API 호출을 위한 URL
	 * @param key   사용자가 입력한 명령어
	 * @param value 사용자가 입력한 명령어 Option
	 */

	public static boolean processorUrnUserCommandForAge(StringBuilder url, String key, String value) {
		if (DataTypeChangerUtil.checkAgeRangeTypeForSearchWord(value)) {      /* 나이 범위 검색 시 */
			String[] searchDate = value.split("~");
			url.append("&startAge=");
			url.append(URLEncoder.encode(searchDate[0], StandardCharsets.UTF_8));
			url.append("&endAge=");
			url.append(URLEncoder.encode(searchDate[1], StandardCharsets.UTF_8));
			url.append("&inputSearchType=");
			url.append(key);

			return true;

		} else if (DataTypeChangerUtil.checkAgeTypeForSearchWord(value)) {     /*나이 하나 검색 시 */
			url.append("&age=");
			url.append(value);
			url.append("&inputSearchType=");
			url.append(key);

			return true;

		} else {
			return false;
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 기타 검색 처리하기 위한 명령어 입력시 API 호출을 위한 URN을 만들기 위한 Method </b>
	 *
	 * @param url   API 호출을 위한 URL
	 * @param key   사용자가 입력한 명령어
	 * @param value 사용자가 입력한 명령어 Option
	 */

	public static boolean processorUrnUserCommandForSearchType(StringBuilder url, String key, String value) {
		if (checkSearchCommand(key) && value != null) {
			return HttpUtil.createUrnUserEtcCommandSearchType(key, value, url);

		} else {
			url.append("&searchWord=");
			url.append(URLEncoder.encode(Objects.requireNonNull(value), StandardCharsets.UTF_8));
			url.append("&inputSearchType=");
			url.append(key);

			return true;
		}
	}

	/**
	 * <b>Discord Bot을 통해 지원자 목록 조회 시 검색 가능한 Keyword가 들어왔는지 확인하는 Method</b>
	 *
	 * @param key 사용자가 입력한 목록 조회 검색 Type
	 * @return 일치하면 True 아니면 False
	 */
	public static boolean checkSuggestSearchType(String key) {
		return key.matches(CrewSuggestSearchType.HOW_KNOW_INFO.getDescription()) ||
			key.matches(CrewSuggestSearchType.NAME.getDescription()) ||
			key.matches(CrewSuggestSearchType.SEX.getDescription()) ||
			key.matches(CrewSuggestSearchType.MBTI.getDescription()) ||
			key.matches(CrewSuggestSearchType.LAST_EDUCATIONAL.getDescription()) ||
			key.matches(CrewSuggestSearchType.SCHOOL_NAME.getDescription()) ||
			key.matches(CrewSuggestSearchType.STATION_NAME.getDescription()) ||
			key.matches(CrewSuggestSearchType.PRIVACY_INFO_AGREE.getDescription()) ||
			key.matches(CrewSuggestSearchType.ADDENDUM_INFO_AGREE.getDescription()) ||
			key.matches(CrewSuggestSearchType.PARTICIPATION.getDescription()) ||
			key.matches(CrewSuggestSearchType.SUGGEST_PART.getDescription());
	}

	/**
	 * <b>Discord Bot을 통해 크루 목록 조회 시 검색 가능한 Keyword가 들어왔는지 확인하는 Method</b>
	 *
	 * @param key 사용자가 입력한 목록 조회 검색 Type
	 * @return 일치하면 True 아니면 False
	 */

	public static boolean checkCrewSearchType(String key) {
		return key.matches(CrewSearchType.HOW_KNOW_INFO.getDescription()) ||
			key.matches(CrewSearchType.NAME.getDescription()) ||
			key.matches(CrewSearchType.SEX.getDescription()) ||
			key.matches(CrewSearchType.MBTI.getDescription()) ||
			key.matches(CrewSearchType.LAST_EDUCATIONAL.getDescription()) ||
			key.matches(CrewSearchType.SCHOOL_NAME.getDescription()) ||
			key.matches(CrewSearchType.STATION_NAME.getDescription()) ||
			key.matches(CrewSearchType.SUGGEST_PART.getDescription());
	}

	/**
	 * <b>Discord Bot을 통해 에러 로그 목록 조회 시 검색 가능한 Keyword가 들어왔는지 확인하는 Method</b>
	 *
	 * @param key 사용자가 입력한 목록 조회 검색 Type
	 * @return 일치하면 True 아니면 False
	 */

	public static boolean checkErrorLogSearchType(String key) {
		return key.matches(ErrorLogSearchType.LOG_ID.getDescription()) ||
			key.matches(ErrorLogSearchType.LOG_LEVEL.getDescription()) ||
			key.matches(ErrorLogSearchType.SERVER_NAME.getDescription()) ||
			key.matches(ErrorLogSearchType.SERVER_IP.getDescription()) ||
			key.matches(ErrorLogSearchType.USER_IP.getDescription()) ||
			key.matches(ErrorLogSearchType.EXCEPTION_BRIEF.getDescription());
	}

	/**
	 * <b>Discord Bot을 통해 이용자 접속 및 요청 정보 목록 조회 시 검색 가능한 Keyword가 들어왔는지 확인하는 Method</b>
	 *
	 * @param key 사용자가 입력한 목록 조회 검색 Type
	 * @return 일치하면 True 아니면 False
	 */

	public static boolean checkUserInfoSearchType(String key) {
		return key.matches(UserInfoSearchType.CONNECTED_USER_REQUEST_ID.getDescription()) ||
			key.matches(UserInfoSearchType.USER_CONNECTED_DATE.getDescription()) ||
			key.matches(UserInfoSearchType.SERVER_NAME.getDescription()) ||
			key.matches(UserInfoSearchType.SERVER_IP.getDescription()) ||
			key.matches(UserInfoSearchType.USER_IP.getDescription());
	}
}
