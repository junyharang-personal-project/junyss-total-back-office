package com.giggalpeople.backoffice.chatops.discord.chatbot.command;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util.CommandUtil;
import com.giggalpeople.backoffice.chatops.discord.chatbot.command.common.util.DiscordBotResponseMessageUtil;
import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.DiscordBotException;
import com.giggalpeople.backoffice.common.enumtype.CrewGrade;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2><b>디스코드를 통해 사용자가 보낸 메시지를 분석하여 처리하기 위한 객체</b></h2>
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckDiscordCommand {

	/**
	 * <b>디스코드를 통해 사용자가 보낸 메시지 중 디스코드 봇을 호출했을 때, 처리를 분기하기 위한 Method</b>
	 * @param event Message를 통해 Event를 처리할 수 있는 JDA 객체
	 * @param userMessage 사용자가 보낸 메시지 문자열
	 * @return 일반적으로 디스코드 봇이 응답할 메시지와 명령에 따른 결과값을 Embed에 넣어 응답하기 위한 문자열 List
	 */

	public static Map<String, List<String>> manufactureCommand(MessageReceivedEvent event, String userMessage) {
		String crewRole = event.getMessage().getMember().getRoles().get(0).getName();
		String[] userCommandArray = userMessage.split(" ");
		Map<String, List<String>> userCommandMap = new HashMap<>();
		List<String> commandOptionList = new ArrayList<>();
		String userCommandKey = "";
		String botCallCommand = "기깔";

		userCommandArray = Arrays.stream(userCommandArray).filter(value ->
				!value.contains(botCallCommand))
			.toArray(String[]::new);

		if (crewRole.equals("")) {
			userCommandMap.put("권한 없음",
				Collections.singletonList(event.getAuthor().getAsMention() + "님은 해당 명령어를 사용할 권한이 없습니다."));
			return userCommandMap;
		}

		if (inDiscordCheckCrewGrade(crewRole, userCommandArray)) {
			for (int index = 0; index < userCommandArray.length; index++) {

				if (CommandUtil.commandCheck(userCommandArray[index])) {
					userCommandKey = userCommandArray[index];
				} else if (userCommandArray[index].matches("명령어") || userCommandArray[index].matches("안녕")
					|| userCommandArray[index].matches("소개") || userCommandArray[index].matches("누구야")) {
					userCommandMap.put(userCommandArray[index], commandOptionList);

					try {
						return CommandUtil.movementFromCommand(event, userCommandMap);
					} catch (IOException ioException) {
						throw new DiscordBotException(API_RESPONSE_NOT_FOUND,
							API_RESPONSE_NOT_FOUND.getMessage(String.valueOf(ioException)));
					}

				} else {
					commandOptionList.add(userCommandArray[index]);
				}
			}
			userCommandMap.put(userCommandKey, commandOptionList);

			try {
				return CommandUtil.movementFromCommand(event, userCommandMap);
			} catch (IOException ioException) {
				throw new DiscordBotException(API_RESPONSE_NOT_FOUND,
					API_RESPONSE_NOT_FOUND.getMessage(String.valueOf(ioException)));
			}

		} else {
			String generalMessage = "안타깝지만, 권한이 없어요 😢";
			String embedMessage = event.getAuthor().getAsMention() + "님은 해당 명령어를 사용할 권한이 없습니다.";
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setColor(Color.RED);
			embedBuilder.setDescription(embedMessage);
			DiscordBotResponseMessageUtil.sendMessage(event, generalMessage,
				DiscordBotResponseMessageUtil.createCommonEmbed(generalMessage.length() + embedMessage.length(),
					embedBuilder));
			return userCommandMap;
		}
	}

	/**
	 * <b>Discord Bot을 통해 지원자 및 크루 정보 조회 시 각 크루 별 권한에 맞는 정보만 확인할 수 있도록 권한 확인을 위한 Method</b>
	 * @param commanderCrewRole Discord Bot 에게 명령어를 내린 사용자의 기깔나는 사람들 Discord 역할
	 * @return 명령을 내린 사용자가 이용할 수 있는 명령어 인지의 여부
	 */
	private static boolean inDiscordCheckCrewGrade(String commanderCrewRole, String[] userCommandArray) {
		return checkCrewGradeByCommand(checkCrewGradeInt(commanderCrewRole), userCommandArray);
	}

	/**
	 * <b>Discrod Bot을 통해 API 검색 명령어가 전달 되었을 경우 해당 사용자가 명령어를 사용할 수 있는 권한이 있는지 확인하기 위한 Method</b>
	 * @param commanderCrewRole Discord Bot 에게 명령어를 내린 사용자의 기깔나는 사람들 Discord 역할
	 * @return 명령을 내린 사용자 권한을 숫자로 변경한 값
	 */
	private static int checkCrewGradeInt(String commanderCrewRole) {
		if (commanderCrewRole.matches(CrewGrade.PROJECT_MANAGER.getGrade())) {
			return CrewGrade.PROJECT_MANAGER.getGradeNum();
		} else if (commanderCrewRole.matches(CrewGrade.PROJECT_LEADER.getGrade())) {
			return CrewGrade.PROJECT_LEADER.getGradeNum();
		} else if (commanderCrewRole.matches(CrewGrade.TEAM_LEADER.getGrade())) {
			return CrewGrade.TEAM_LEADER.getGradeNum();
		} else if (commanderCrewRole.matches(CrewGrade.TOTAL_LEADER.getGrade())) {
			return CrewGrade.TOTAL_LEADER.getGradeNum();
		} else {
			return CrewGrade.GENERAL_CREW.getGradeNum();
		}
	}

	/**
	 * <b>Discrod Bot을 통해 API 검색 명령어가 전달 되었을 경우 해당 사용자가 명령어를 사용할 수 있는 권한이 있는지 확인하기 위한 Method</b>
	 * @param commanderCrewRole Discord Bot 에게 명령어를 내린 사용자의 기깔나는 사람들 Discord 역할
	 * @return 명령을 내린 사용자 권한을 문자열로 변경한 값
	 */
	public static CrewGrade checkCrewGradeString(String commanderCrewRole) {
		if (commanderCrewRole.matches(CrewGrade.PROJECT_MANAGER.getGrade())) {
			return CrewGrade.PROJECT_MANAGER;
		} else if (commanderCrewRole.matches(CrewGrade.PROJECT_LEADER.getGrade())) {
			return CrewGrade.PROJECT_LEADER;
		} else if (commanderCrewRole.matches(CrewGrade.TEAM_LEADER.getGrade())) {
			return CrewGrade.TEAM_LEADER;
		} else if (commanderCrewRole.matches(CrewGrade.TOTAL_LEADER.getGrade())) {
			return CrewGrade.TOTAL_LEADER;
		} else {
			return CrewGrade.GENERAL_CREW;
		}
	}

	/**
	 * <b>Discord Bot을 통해 명령을 내린 사용자 권한과 명령어를 확인하여 이용할 수 있는 명령어인지 확인하기 위한 Method</b>
	 * @param crewGradeNum 사용자 권한
	 * @param userCommandArray 사용자가 Discord Bot을 통해 내린 명령어
	 * @return 해당 사용자가 이용할 수 있는 명령어인지에 대한 여부
	 */
	private static boolean checkCrewGradeByCommand(int crewGradeNum, String[] userCommandArray) {
		final String CHECK_SUGGEST_SEARCH_COMMAND = "지원자목록조회";

		for (String userCommand : userCommandArray) {
			if ((crewGradeNum == 0 || crewGradeNum == 1)) {
				return true;

			} else if (crewGradeNum == 2) {
				switch (userCommand) {
					case CHECK_SUGGEST_SEARCH_COMMAND:
					case "크루목록조회":
					case "크루상세조회":
					case "로그목록조회":
					case "로그상세조회":
					case "서버자원조회":
						return true;
					default:
						return !CommandUtil.commandCheck(userCommand);
				}

			} else if (crewGradeNum == 3) {
				switch (userCommand) {
					case CHECK_SUGGEST_SEARCH_COMMAND:
					case "지원자상세조회":
					case "로그목록조회":
					case "로그상세조회":
					case "서버자원조회":
						return true;
					default:
						return !CommandUtil.commandCheck(userCommand);
				}

			} else {
				switch (userCommand) {
					case CHECK_SUGGEST_SEARCH_COMMAND:
					case "크루목록조회":
					case "로그목록조회":
					case "로그상세조회":
					case "서버자원조회":
						return true;
					default:
						return !CommandUtil.commandCheck(userCommand);
				}
			}
		}
		return true;
	}
}