package com.giggalpeople.backoffice.chatops.discord.chatbot.util;

import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_CALLER_DISCORD_BOT;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_CREW;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.API_SUGGEST;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.CONNECTED_USER;
import static com.giggalpeople.backoffice.api.common.constant.APIUriInfo.LOG;
import static com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSearchType.AGE_INFO;
import static com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSearchType.BIRTH_DATE;
import static com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSearchType.JOIN_DATE;
import static com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSearchType.MEET_DATE;
import static com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSearchType.SUGGEST_DATE;
import static com.giggalpeople.backoffice.api.record.model.dto.enumtype.ErrorRecordSearchType.LOG_CREATE_DATE;
import static com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType.USER_CONNECTED_DATE;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.CREATE_API_URL_ERROR;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.giggalpeople.backoffice.api.common.constant.APIUriInfo;
import com.giggalpeople.backoffice.api.common.constant.SearchTypeProcessor;
import com.giggalpeople.backoffice.api.crew.exception.CrewException;
import com.giggalpeople.backoffice.api.crew.exception.SuggestException;
import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.Participation;
import com.giggalpeople.backoffice.api.record.exception.ErrorRecordException;
import com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util.CommandUtil;
import com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util.DiscordBotResponseMessageUtil;
import com.giggalpeople.backoffice.chatops.discord.chatbot.command.constant.CheckDiscordCommand;
import com.giggalpeople.backoffice.common.env.Environment;
import com.giggalpeople.backoffice.common.util.DataTypeChangerUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>Discord Bot이 명령어를 통해 받은 내용을 토대로 URL을 만드는 객체</b></h2>
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtil {

	/**
	 * <b>검색 조건을 통해 지원자 목록 조회 검색을 위한 URL을 만드는 Method</b>
	 * @param event          Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userCommandMap 지원자 검색 조건을 담은 명령어 Map
	 * @return 만들어진 문자열형 URL 값
	 */

	public static String suggestSearchCreateURL(MessageReceivedEvent event, Map<String, List<String>> userCommandMap) {
		StringBuilder url = Environment.initPrefixAPIURL();
		url.append(API_SUGGEST + "?page=" + 0 + "&perPageNum=" + 0 + "&displayPageNum=" + 0);

		List<String> suggestSearchCommandOption = userCommandMap.get("지원자목록조회");

		try {
			return checkSearchSubject(event, url, suggestSearchCommandOption, 2);
		} catch (IOException ioException) {
			throw new SuggestException(CREATE_API_URL_ERROR, CREATE_API_URL_ERROR.getMessage());
		}
	}

	/**
	 * <b>Discord Bot을 통해 API 호출을 이용 지원자 상세 조회를 하고자 할 때, 알맞는 URL 값 혹은 Error Messgae를 반환하기 위한 검토 Method</b>
	 * @param event                 Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userCommandOptionList Discord Bot에게 전달 된 사용자 명령어 목록
	 * @return API 호출을 위한 URL
	 */

	public static String suggestDetailCreateURL(MessageReceivedEvent event, List<String> userCommandOptionList) {
		String detailSearchURL = "";

		for (String userCommandOption : userCommandOptionList) {
			if (DataTypeChangerUtil.checkRegularSuggestId(userCommandOption)) {
				detailSearchURL = createDetailSearchURL(userCommandOption);
			}
		}

		if (detailSearchURL.equals("")) {
			String searchType = "지원자";
			DiscordBotResponseMessageUtil.createAPICallErrorMessage(event, searchType);
		}
		return detailSearchURL;
	}

	public static String crewSearchCreateURL(MessageReceivedEvent event, Map<String, List<String>> userCommandMap) {
		StringBuilder url = Environment.initPrefixAPIURL();
		url.append(APIUriInfo.API_CREW + "?page=" + 0 + "&perPageNum=" + 0 + "&displayPageNum=" + 0);

		try {
			return checkSearchSubject(event, url, userCommandMap.get("크루목록조회"), 3);
		} catch (IOException ioException) {
			throw new CrewException(CREATE_API_URL_ERROR, CREATE_API_URL_ERROR.getMessage());
		}
	}

	/**
	 * <b>Discord Bot을 통해 이용자 접속 및 요청 목록 조회 시 API 호출을 위한 URL을 만드는 Method</b>
	 * @param event          Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userCommandMap Discord Bot에게 전달 된 사용자 명령어 목록
	 * @return API 호출을 위한 URL
	 */

	public static String userInfoSearchCreateURL(MessageReceivedEvent event, Map<String, List<String>> userCommandMap) {
		try {
			return checkSearchSubject(event,
				Environment.initPrefixAPIURL()
					.append(API_CALLER_DISCORD_BOT + CONNECTED_USER + "/lists?page=" + 0 + "&perPageNum=" + 0
						+ "&displayPageNum=" + 0),
				userCommandMap.get("이용자목록조회"),
				1);

		} catch (IOException ioException) {
			throw new ErrorRecordException(CREATE_API_URL_ERROR, CREATE_API_URL_ERROR.getMessage());
		}
	}

	/**
	 * <b>Discord Bot을 통해 API 호출을 이용자 접속 및 요청 상세 조회를 하고자 할 때, 알맞는 URL 값 혹은 Error Messgae를 반환하기 위한 검토 Method</b>
	 * @param event                 Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userCommandOptionList Discord Bot에게 전달 된 사용자 명령어 목록
	 * @return API 호출을 위한 URL
	 */
	public static String userInfoDetailCreateURL(MessageReceivedEvent event, List<String> userCommandOptionList) {
		String detailSearchURL = "";
		StringBuilder url = Environment.initPrefixAPIURL();

		for (String userCommandOption : userCommandOptionList) {
			if (DataTypeChangerUtil.checkRegularPK(userCommandOption)) {
				url.append(API_CALLER_DISCORD_BOT + CONNECTED_USER + "/details")
					.append("?connectedUserRequestInfoID=")
					.append(userCommandOption)
					.append("&crewGrade=")
					.append(CheckDiscordCommand.checkCrewGradeString(
						event.getMessage().getMember().getRoles().get(0).getName()));
				detailSearchURL = url.toString();
			}
		}

		if (detailSearchURL.equals("")) {
			String searchType = "이용자";
			DiscordBotResponseMessageUtil.createAPICallErrorMessage(event, searchType);
		}
		return detailSearchURL;
	}

	/**
	 * <b>Discord Bot을 통해 Error Log 목록 조회 시 API 호출을 위한 URL을 만드는 Method</b>
	 * @param event          Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userCommandMap Discord Bot에게 전달 된 사용자 명령어 목록
	 * @return API 호출을 위한 URL
	 */

	public static String errorLogSearchCreateURL(MessageReceivedEvent event, Map<String, List<String>> userCommandMap) {
		try {
			return checkSearchSubject(event,
				Environment.initPrefixAPIURL()
					.append(API_CALLER_DISCORD_BOT + LOG + "/lists?page=" + 0 + "&perPageNum=" + 0 + "&displayPageNum="
						+ 0),
				userCommandMap.get("로그목록조회"),
				0);

		} catch (IOException ioException) {
			throw new ErrorRecordException(CREATE_API_URL_ERROR, CREATE_API_URL_ERROR.getMessage());
		}
	}

	/**
	 * <b>Discord Bot을 통해 API 호출 시 사용자 명령어를 기반으로 호출 URL을 만들기 위한 EM</b>
	 * @param event                      Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url                        API 호출 시 사용자 명령어를 기반으로 호출 URL
	 * @param searchCommandOption Discord Bot을 통해 전달된 공백을 기준으로 나눠 담겨진 사용자 입력 명령어 List
	 * @return API 호출을 위한 URL
	 */
	private static String checkSearchSubject(MessageReceivedEvent event, StringBuilder url,
		List<String> searchCommandOption, int check) throws IOException {

		boolean processCheck;

		if (searchCommandOption.isEmpty()) {
			return url.toString();
		}

		if (check == 0) {
			processCheck = createErrorLogSearchURL(event, url, searchCommandOption);
		} else if (check == 1) {
			processCheck = createUserInfoSearchURL(event, url, searchCommandOption);
		} else if (check == 2) {
			processCheck = createSuggestSearchURL(event, url, searchCommandOption);
		} else {
			processCheck = createCrewSearchURL(event, url, searchCommandOption);
		}

		if (!processCheck) {
			throw new IOException(CREATE_API_URL_ERROR.getMessage());
		}
		return url.toString();
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 지원자 목록 조회를 명령했을 때, 검색 명령에 따른 API 호출 URL을 만들기 위한 Method</b>
	 *
	 * @param event                      Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url                        Back Office API 호출을 위한 기본 URL
	 * @param suggestSearchCommandOption Discord Bot을 통해 전달된 공백을 기준으로 나눠진 사용자 명령어 List
	 * @return URL이 정상적으로 만들어지면 True 아니면 False
	 */

	private static boolean createSuggestSearchURL(MessageReceivedEvent event, StringBuilder url,
		List<String> suggestSearchCommandOption) {
		List<String> userCommandOptionKeyValue;
		Map<String, String> userCommand = new HashMap<>();
		boolean processCheck = true;

		userCommandOptionKeyValue = CommandUtil.splitUserCommandOption(suggestSearchCommandOption);
		CommandUtil.insertInCommandOptionMap(userCommandOptionKeyValue, userCommand);

		for (Map.Entry<String, String> entry : userCommand.entrySet()) {
			String key = SearchTypeProcessor.changeSuggestSearchType(entry.getKey().replace("-", ""));
			String value = entry.getValue();

			CommandUtil.checkUserCommandSearchSuggestForPagination(url, key, value);

			if (key.contains(SUGGEST_DATE.getDescription()) || key.contains(BIRTH_DATE.getDescription())
				|| key.contains(MEET_DATE.getDescription())) {
				processCheck = CommandUtil.processorUrnUserCommandForDate(event, url, key, value);

			} else if (key.contains(AGE_INFO.getDescription())) {
				processCheck = CommandUtil.processorUrnUserCommandForAge(url, key, value);

			} else if (CommandUtil.checkSuggestSearchType(key)) {
				processCheck = CommandUtil.processorUrnUserCommandForSearchType(url, key, value);
			}
		}
		return processCheck;
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 크루 목록 조회를 명령했을 때, 검색 명령에 따른 API 호출 URL을 만들기 위한 Method</b>
	 *
	 * @param event                      Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url                        Back Office API 호출을 위한 기본 URL
	 * @param suggestSearchCommandOption Discord Bot을 통해 전달된 공백을 기준으로 나눠진 사용자 명령어 List
	 * @return URL이 정상적으로 만들어지면 True 아니면 False
	 */

	@SuppressWarnings("checkstyle:OperatorWrap")
	private static boolean createCrewSearchURL(MessageReceivedEvent event, StringBuilder url,
		List<String> suggestSearchCommandOption) {
		List<String> userCommandOptionKeyValue;
		Map<String, String> userCommand = new HashMap<>();
		boolean processCheck = true;

		userCommandOptionKeyValue = CommandUtil.splitUserCommandOption(suggestSearchCommandOption);
		CommandUtil.insertInCommandOptionMap(userCommandOptionKeyValue, userCommand);

		for (Map.Entry<String, String> entry : userCommand.entrySet()) {
			String key = SearchTypeProcessor.changeSuggestSearchType(entry.getKey().replace("-", ""));
			String value = entry.getValue();

			CommandUtil.checkUserCommandForSearchCrewPagination(url, key, value);

			if (key.equalsIgnoreCase(SUGGEST_DATE.getDescription())
				|| key.equalsIgnoreCase(BIRTH_DATE.getDescription())
				|| key.equalsIgnoreCase(MEET_DATE.getDescription())
				|| key.equalsIgnoreCase(JOIN_DATE.getDescription())) {

				processCheck = CommandUtil.processorUrnUserCommandForDate(event, url, key, value);

			} else if (key.contains(AGE_INFO.getDescription())) {
				processCheck = CommandUtil.processorUrnUserCommandForAge(url, key, value);

			} else if (CommandUtil.checkCrewSearchType(key)) {
				processCheck = CommandUtil.processorUrnUserCommandForSearchType(url, key, value);
			}
		}
		return processCheck;
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 Error Log 목록 조회를 명령했을 때, 검색 명령에 따른 API 호출 URL을 만들기 위한 Method</b>
	 * @param event                       Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url                         Back Office API 호출을 위한 기본 URL
	 * @param errorLogSearchCommandOption Discord Bot을 통해 전달된 공백을 기준으로 나눠진 사용자 명령어 List
	 * @return URL이 정상적으로 만들어지면 True 아니면 False
	 */

	private static boolean createErrorLogSearchURL(MessageReceivedEvent event, StringBuilder url,
		List<String> errorLogSearchCommandOption) {
		List<String> userCommandOptionKeyValue;
		Map<String, String> userCommand = new HashMap<>();
		boolean processCheck = true;

		userCommandOptionKeyValue = CommandUtil.splitUserCommandOption(errorLogSearchCommandOption);
		CommandUtil.insertInCommandOptionMap(userCommandOptionKeyValue, userCommand);

		for (Map.Entry<String, String> entry : userCommand.entrySet()) {
			String key = SearchTypeProcessor.changeErrorLogSearchType(entry.getKey().replace("-", ""));
			String value = entry.getValue();

			CommandUtil.checkUserCommandForSearchErrorLogPagination(url, key, value);

			if (key.equalsIgnoreCase(LOG_CREATE_DATE.getDescription())) {
				processCheck = CommandUtil.processorUrnUserCommandForDate(event, url, key, value);

			} else if (CommandUtil.checkErrorLogSearchType(key)) {
				processCheck = CommandUtil.processorUrnUserCommandForSearchType(url, key, value);
			}
		}
		return processCheck;
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 이용자 접속 및 요청 정보 목록 조회를 명령했을 때, 검색 명령에 따른 API 호출 URL을 만들기 위한 Method</b>
	 * @param event                       Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param url                         Back Office API 호출을 위한 기본 URL
	 * @param searchCommandOption Discord Bot을 통해 전달된 공백을 기준으로 나눠진 사용자 명령어 List
	 * @return URL이 정상적으로 만들어지면 True 아니면 False
	 */
	private static boolean createUserInfoSearchURL(MessageReceivedEvent event, StringBuilder url,
		List<String> searchCommandOption) {
		List<String> userCommandOptionKeyValue;
		Map<String, String> userCommand = new HashMap<>();
		boolean processCheck = true;

		userCommandOptionKeyValue = CommandUtil.splitUserCommandOption(searchCommandOption);
		CommandUtil.insertInCommandOptionMap(userCommandOptionKeyValue, userCommand);

		for (Map.Entry<String, String> entry : userCommand.entrySet()) {
			String key = SearchTypeProcessor.changeUserInfoSearchType(entry.getKey().replace("-", ""));
			String value = entry.getValue();

			CommandUtil.checkUserCommandForSearchUserInfoPagination(url, key, value);

			if (key.equalsIgnoreCase(USER_CONNECTED_DATE.getDescription())) {
				processCheck = CommandUtil.processorUrnUserCommandForDate(event, url, key, value);

			} else if (CommandUtil.checkUserInfoSearchType(key)) {
				processCheck = CommandUtil.processorUrnUserCommandForSearchType(url, key, value);
			}
		}
		return processCheck;
	}

	/**
	 * <b>Discord Bot을 통해 API 호출을 이용 크루 상세 조회를 하고자 할 때, 알맞는 URL 값 혹은 Error Messgae를 반환하기 위한 검토 Method</b>
	 * @param event                 Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userCommandOptionList Discord Bot에게 전달 된 사용자 명령어 목록
	 * @return API 호출을 위한 URL
	 */
	public static String crewDetailCreateURL(MessageReceivedEvent event, List<String> userCommandOptionList) {
		String detailSearchURL = "";

		for (String userCommandOption : userCommandOptionList) {
			if (DataTypeChangerUtil.checkRegularCrewNumber(userCommandOption)) {
				detailSearchURL = createDetailSearchURL(userCommandOption);
			}
		}

		if (detailSearchURL.equals("")) {
			String searchType = "크루";
			DiscordBotResponseMessageUtil.createAPICallErrorMessage(event, searchType);
		}
		return detailSearchURL;
	}

	/**
	 * <b>Discord Bot을 통해 API 호출을 Error Log 상세 조회를 하고자 할 때, 알맞는 URL 값 혹은 Error Messgae를 반환하기 위한 검토 Method</b>
	 * @param event                 Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userCommandOptionList Discord Bot에게 전달 된 사용자 명령어 목록
	 * @return API 호출을 위한 URL
	 */
	public static String errorLogDetailCreateURL(MessageReceivedEvent event, List<String> userCommandOptionList) {
		String detailSearchURL = "";
		StringBuilder url = Environment.initPrefixAPIURL();

		for (String userCommandOption : userCommandOptionList) {
			if (DataTypeChangerUtil.checkRegularPK(userCommandOption)) {
				url.append(API_CALLER_DISCORD_BOT + LOG + "/details")
					.append("?logId=")
					.append(userCommandOption)
					.append("&crewGrade=")
					.append(CheckDiscordCommand.checkCrewGradeString(
						event.getMessage().getMember().getRoles().get(0).getName()));
				detailSearchURL = url.toString();
			}
		}

		if (detailSearchURL.equals("")) {
			String searchType = "로그";
			DiscordBotResponseMessageUtil.createAPICallErrorMessage(event, searchType);
		}
		return detailSearchURL;
	}

	/**
	 * <b>Discord Bot을 통해 지원자 혹은 크루 상세 조회 시 URL을 알맞게 만들기 위한 Method</b>
	 *
	 * @param userCommandOption 사용자가 입력한 검색 조건 명령어
	 * @return API 호출을 위한 URL 값
	 */
	private static String createDetailSearchURL(String userCommandOption) {
		StringBuilder url = Environment.initPrefixAPIURL();

		if (DataTypeChangerUtil.checkRegularSuggestId(userCommandOption)) {
			url.append(API_SUGGEST + "/").append(userCommandOption);
		} else if (DataTypeChangerUtil.checkRegularCrewNumber(userCommandOption)) {
			url.append(API_CREW + "/").append(userCommandOption);
		}
		return url.toString();
	}

	/**
	 * <b>JAVA를 통해 API 호출 URL 만들 시 Encoding이 필요한 값을 공통으로 Encoding 하기 위한 Method</b>
	 *
	 * @param value Discord Bot을 통해 입력된 API 조회 명령어
	 * @return URL Encoding 된 값
	 */
	public static String createCommonEncodeURL(String value) {
		return URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
	}

	/**
	 * <b>Discord Bot을 통해 API 호출 시 크루 목록조회인지 지원자 목록 조회인지를 판별하기 위한 Method</b>
	 *
	 * @param url API 호출 위한 URL
	 * @return 크루 목록 조회라면 True 아니면 False
	 */
	public static boolean discordBotSearchAPIURL(URL url) {
		return url.getPath().contains(API_CREW);
	}

	/**
	 * <b>지원자 참여 여부에 따른 URN을 만들기 위한 Method</b>
	 * @param value 사용자가 Discord Bot을 통해 요청한 검색어
	 * @param url 상위 URL
	 * @return
	 */
	public static boolean checkParticipation(String value, StringBuilder url) {
		if (Participation.CHECK.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.CHECK.getSaveParticipationInfo()));

			return true;

		} else if (Participation.JOIN.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.JOIN.getSaveParticipationInfo()));

			return true;

		} else if (Participation.TEST.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.TEST.getSaveParticipationInfo()));

			return true;

		} else if (Participation.MEETING_EXPECTED.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.MEETING_EXPECTED.getSaveParticipationInfo()));

			return true;

		} else if (Participation.JOIN_GIVE_UP.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.JOIN_GIVE_UP.getSaveParticipationInfo()));

			return true;

		} else if (Participation.MID_WAY_GIVE_UP.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.MID_WAY_GIVE_UP.getSaveParticipationInfo()));

			return true;

		} else if (Participation.FORCED_EXIT.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.FORCED_EXIT.getSaveParticipationInfo()));

			return true;

		} else if (Participation.TEAM_LEADER_JOIN_REFUSAL.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(
				HttpUtil.createCommonEncodeURL(Participation.TEAM_LEADER_JOIN_REFUSAL.getSaveParticipationInfo()));

			return true;

		} else if (Participation.PROJECT_LEADER_JOIN_REFUSAL.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(
				HttpUtil.createCommonEncodeURL(Participation.PROJECT_LEADER_JOIN_REFUSAL.getSaveParticipationInfo()));

			return true;

		} else if (Participation.BOSS_JOIN_REFUSAL.getSaveParticipationInfo().equals(value)) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(Participation.BOSS_JOIN_REFUSAL.getSaveParticipationInfo()));

			return true;

		} else {
			return false;
		}
	}

	/**
	 * <b>사용자가 Discord Bot을 통해 지원자 검색을 통해 목록 조회 시 검색 Type과 검색어 URN을 만들기 위한 Method</b>
	 * @param key 사용자가 Discord Bot을 통해 요청한 검색 Type
	 * @param value 사용자가 Discord Bot을 통해 요청한 검색어
	 * @param url 상위 URL
	 * @return URL이 정삭적으로 생성 되었으면 True 아니면 False 반환
	 */
	public static boolean createUrnUserEtcCommandSearchType(String key, String value, StringBuilder url) {
		if ((key != null || !key.equals("")) && (value != null || !value.equals(""))) {
			url.append("&searchWord=");
			url.append(HttpUtil.createCommonEncodeURL(value));
			url.append("&inputSearchType=");
			url.append(key);

			return true;
		}
		return false;
	}
}
