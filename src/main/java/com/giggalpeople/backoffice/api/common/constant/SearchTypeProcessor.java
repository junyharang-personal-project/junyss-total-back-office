package com.giggalpeople.backoffice.api.common.constant;

import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSuggestSearchType;
import com.giggalpeople.backoffice.api.log.model.dto.enumtype.ErrorLogSearchType;
import com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchTypeProcessor {

	/**
	 * <b>Discord Bot을 통해 사용자가 지원자를 검색할 때, 한글 검색명을 Data Base에서 검색하기 위한 문자열로 바꾸기 위한 Method</b>
	 * @param searchType Discord Bot을 통해 사용자가 입력한 검색 Type
	 * @return Data Base에서 검색하기 위한 문자열
	 */
	public static String changeSuggestSearchType(String searchType) {

		if (searchType.equals(CrewSuggestSearchType.SUGGEST_DATE.getSearchCommand())) {
			return CrewSuggestSearchType.SUGGEST_DATE.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.HOW_KNOW_INFO.getSearchCommand())) {
			return CrewSuggestSearchType.HOW_KNOW_INFO.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.NAME.getSearchCommand())) {
			return CrewSuggestSearchType.NAME.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.SEX.getSearchCommand())) {
			return CrewSuggestSearchType.SEX.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.BIRTH_DATE.getSearchCommand())) {
			return CrewSuggestSearchType.BIRTH_DATE.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.AGE_INFO.getSearchCommand())) {
			return CrewSuggestSearchType.AGE_INFO.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.MBTI.getSearchCommand())) {
			return CrewSuggestSearchType.MBTI.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.LAST_EDUCATIONAL.getSearchCommand())) {
			return CrewSuggestSearchType.LAST_EDUCATIONAL.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.SCHOOL_NAME.getSearchCommand())) {
			return CrewSuggestSearchType.SCHOOL_NAME.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.STATION_NAME.getSearchCommand())) {
			return CrewSuggestSearchType.STATION_NAME.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.SUGGEST_PART.getSearchCommand())) {
			return CrewSuggestSearchType.SUGGEST_PART.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.PRIVACY_INFO_AGREE.getSearchCommand())) {
			return CrewSuggestSearchType.PRIVACY_INFO_AGREE.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.ADDENDUM_INFO_AGREE.getSearchCommand())) {
			return CrewSuggestSearchType.ADDENDUM_INFO_AGREE.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.PARTICIPATION.getSearchCommand())) {
			return CrewSuggestSearchType.PARTICIPATION.getDescription();
		} else if (searchType.equals(CrewSuggestSearchType.MEET_DATE.getSearchCommand())) {
			return CrewSuggestSearchType.MEET_DATE.getDescription();
		} else {
			return searchType;
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 에러 로그를 검색할 때, 한글 검색명을 Data Base에서 검색하기 위한 문자열로 바꾸기 위한 Method</b>
	 * @param searchType Discord Bot을 통해 사용자가 입력한 검색 Type
	 * @return Data Base에서 검색하기 위한 문자열
	 */

	public static String changeErrorLogSearchType(String searchType) {
		if (searchType.equals(ErrorLogSearchType.LOG_ID.getSearchCommand())) {
			return ErrorLogSearchType.LOG_ID.getDescription();
		} else if (searchType.equals(ErrorLogSearchType.LOG_CREATE_DATE.getSearchCommand())) {
			return ErrorLogSearchType.LOG_CREATE_DATE.getDescription();
		} else if (searchType.equals(ErrorLogSearchType.LOG_LEVEL.getSearchCommand())) {
			return ErrorLogSearchType.LOG_LEVEL.getDescription();
		} else if (searchType.equals(ErrorLogSearchType.SERVER_NAME.getSearchCommand())) {
			return ErrorLogSearchType.SERVER_NAME.getDescription();
		} else if (searchType.equals(ErrorLogSearchType.SERVER_IP.getSearchCommand())) {
			return ErrorLogSearchType.SERVER_IP.getDescription();
		} else if (searchType.equals(ErrorLogSearchType.USER_IP.getSearchCommand())) {
			return ErrorLogSearchType.USER_IP.getDescription();
		} else if (searchType.equals(ErrorLogSearchType.EXCEPTION_BRIEF.getSearchCommand())) {
			return ErrorLogSearchType.EXCEPTION_BRIEF.getDescription();
		} else {
			return searchType;
		}
	}

	/**
	 * <b>Discord Bot을 통해 사용자가 이용자 접속 및 요청 정보 검색할 때, 한글 검색명을 Data Base에서 검색하기 위한 문자열로 바꾸기 위한 Method</b>
	 * @param searchType Discord Bot을 통해 사용자가 입력한 검색 Type
	 * @return Data Base에서 검색하기 위한 문자열
	 */

	public static String changeUserInfoSearchType(String searchType) {
		if (searchType.equals(UserInfoSearchType.CONNECTED_USER_REQUEST_ID.getSearchCommand())) {
			return UserInfoSearchType.CONNECTED_USER_REQUEST_ID.getDescription();
		} else if (searchType.equals(UserInfoSearchType.USER_CONNECTED_DATE.getSearchCommand())) {
			return UserInfoSearchType.USER_CONNECTED_DATE.getDescription();
		} else if (searchType.equals(UserInfoSearchType.SERVER_NAME.getSearchCommand())) {
			return UserInfoSearchType.SERVER_NAME.getDescription();
		} else if (searchType.equals(UserInfoSearchType.SERVER_IP.getSearchCommand())) {
			return UserInfoSearchType.SERVER_IP.getDescription();
		} else if (searchType.equals(UserInfoSearchType.USER_IP.getSearchCommand())) {
			return UserInfoSearchType.USER_IP.getDescription();
		} else {
			return searchType;
		}
	}
}
