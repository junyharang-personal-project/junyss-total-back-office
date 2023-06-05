package com.giggalpeople.backoffice.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewSuggestPeopleManagementSearchDTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h2><b>특정 Data Type 형식을 가공하기 위한 Util</b></h2>
 */
@Data
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataTypeChangerUtil {

	/**
	 * <b>Client에서 크루 번호가 2022-003-0001 형식으로 들어오는데, 이걸 확인해서 가공하는 Method</b>
	 * @param crewNumber Client 보낸 크루 번호
	 * @return String - 특수문자 -를 뺀 정수값 크루 번호
	 */

	public static String changeCrewNumber(String crewNumber) {
		if (crewNumber.matches(".*(\\d{4})-(\\d{3})-(\\d{4})")) {
			return crewNumber.replace("-", "");
		}
		return crewNumber;
	}

	/**
	 * <b>현재 연도를 계산하기 위한 Method</b>
	 * @return 현재 연도 문자열
	 */
	public static String createNowYear() {
		LocalDate now = LocalDate.now();

		DateTimeFormatter yearsFormatter = DateTimeFormatter.ofPattern("yyyy");

		return now.format(yearsFormatter);
	}

	/**
	 * <b>검색어가 지원 파트일 경우 다양한 검색어를 입력할 수 있기 때문에 이를 확인하여 Data Base 검색 시 저장되어 있는 값으로 검색되게 하기 위하여 검색어를 가공하여 SearchDTO의 getSearchWord()에서 호출하는 Method</b>
	 * @param searchWord SearchDTO의 getSearchWord()에서 전달된 검색어
	 * @return 가공된 검색어
	 */
	public static String changeSearchWordForPart(String searchWord) {

		if (searchWord == null) {
			return "";
		}

		if (searchWord.toLowerCase().contains("project") || searchWord.toLowerCase().contains("manager")
			|| searchWord.toLowerCase().contains("pm")) {
			return "Project Manager(PM)";
		} else if (searchWord.contains("서비스") || searchWord.contains("기획") || searchWord.toLowerCase().contains("plan")
			|| searchWord.toLowerCase().contains("service")) {
			return "서비스 기획";
		} else if (searchWord.toLowerCase().contains("web") || searchWord.toLowerCase().contains("app")
			|| searchWord.toLowerCase().contains("design") || searchWord.toLowerCase().contains("ui")
			|| searchWord.toLowerCase().contains("ux") || searchWord.contains("디자")) {
			return "WEB / APP 디자이너";
		} else if (searchWord.toLowerCase().contains("back") || searchWord.contains("백엔") || searchWord.contains("빽엔")
			|| searchWord.contains("빼갠") || searchWord.contains("빼겐") || searchWord.contains("서버")
			|| searchWord.toLowerCase().contains("server")) {
			return "BackEnd 개발자";
		} else if (searchWord.toLowerCase().contains("front") || searchWord.contains("프엔") || searchWord.toLowerCase()
			.contains("프론트")) {
			return "FrontEnd 개발자";
		} else if (searchWord.toLowerCase().contains("dmso") || searchWord.contains("dev") || searchWord.toLowerCase()
			.contains("ops") || searchWord.toLowerCase().contains("ml") || searchWord.toLowerCase().contains("sec")
			|| searchWord.toLowerCase().contains("infra") || searchWord.toLowerCase().contains("인프라")
			|| searchWord.toLowerCase().contains("데브") || searchWord.toLowerCase().contains("옵브")
			|| searchWord.toLowerCase().contains("엠엘") || searchWord.toLowerCase().contains("쎅")
			|| searchWord.toLowerCase().contains("보안")) {
			return "DMSO(DevMlSecOps)";
		} else if (searchWord.toLowerCase().contains("hat") || searchWord.toLowerCase().contains("hacker")) {
			return "화이트 해커";
		} else if (searchWord.toLowerCase().contains("swi") || searchWord.toLowerCase().contains("ios")
			|| searchWord.toLowerCase().contains("apple") || searchWord.contains("아이오에스") || searchWord.toLowerCase()
			.contains("스위프트") || searchWord.toLowerCase().contains("스프")) {
			return "IOS 개발자";
		} else if (searchWord.toLowerCase().contains("an") || searchWord.toLowerCase().contains("android")
			|| searchWord.toLowerCase().contains("google") || searchWord.contains("안드") || searchWord.toLowerCase()
			.contains("로이드") || searchWord.toLowerCase().contains("구글")) {
			return "Android 개발자";
		} else if (searchWord.toLowerCase().contains("ai") || searchWord.contains("인공") || searchWord.contains("지능")
			|| searchWord.contains("머신") || searchWord.contains("러닝")) {
			return "AI 개발자";
		} else {
			return searchWord;
		}
	}

	/**
	 * <b>구글 스프레드 시트에 사용자가 보기 편하도록 저장한 날짜/시간 정보를 Data Base에 검색이 용이하게 변경하여 저장하기 위한 Method</b>
	 * @param dateTime 구글 스프레드 시트에 저장된 날짜 정보
	 * @return Data Base에 검색이 용이하게 변경하여 저장한 문자열
	 */

	public static String changeFormatDate(String dateTime) {
		String[] meetDateArray = dateTime.split(" ");
		String year = "";
		String month = "";
		String day = "";
		String changeDateFormat = "";

		for (String meetInfo : meetDateArray) {
			if (meetInfo.matches(".*(\\d{4})년")) {
				year = meetInfo.replace("년", ".");
			} else if (meetInfo.matches(".*(\\d{2})월")) {
				month = meetInfo.replace("월", ".");
			} else if (meetInfo.matches(".*(\\d{2})일")) {
				day = meetInfo.replace("일", "");
			}
		}

		if (isDataBaseDateFormatChecker(year, month, day)) {
			changeDateFormat = year + month + day;
		} else {
			throw new IllegalStateException("날짜 형식이 올바르지 않습니다.");
		}
		return changeDateFormat;
	}

	/**
	 * <b>구글 스프레드 시트에 저장된 날짜 형식을 Data Base 저장 시 형식에 맞게 변경 되었는지 확인하기 위한 Method</b>
	 * @param year 구글 스프레드 시트에 저장된 날짜 형식 중 년도
	 * @param month 구글 스프레드 시트에 저장된 날짜 형식 중 월
	 * @param day 구글 스프레드 시트에 저장된 날짜 형식 중 일
	 * @return 변경이 정상인지 판단하여 정상일 경우 True 아닐 경우 False 반환
	 */

	private static boolean isDataBaseDateFormatChecker(String year, String month, String day) {

		String dateInfo = year + month + day;
		return (dateInfo.equals("")) || (dateInfo.matches(".*(\\d{4}).(\\d{2}).(\\d{2})"));
	}

	/**
	 * <b>크루 번호 형태로 Parameter가 들어왔는지 확인하는 Method</b>
	 * @param crewNumber 크루 번호
	 * @return 크루 번호가 맞는지 아닌지 판별하여 Boolean 값 반환
	 */
	public static boolean checkRegularCrewNumber(String crewNumber) {
		return crewNumber.matches("^\\d{4}-\\d{3}-\\d{4}$");
	}

	/**
	 * <b>지원 순서 번호 형태로 Parameter가 들어왔는지 확인하는 Method</b>
	 * @param suggestId 지원 순서 번호
	 * @return 지원 순서 번호가 맞는지 아닌지 판별하여 Boolean 값 반환
	 */

	public static boolean checkRegularSuggestId(String suggestId) {
		return suggestId.matches("^\\d{4}$");
	}

	/**
	 * <b>날짜 조회 시 날짜 검색어 형식에 맞게 값이 들어왔는지 확인하기 위한 Method</b>
	 * @param searchWord 날짜 조회 검색어
	 * @return 형식에 맞는지 확인하여 맞다면 true 아니면 false 반환
	 */
	public static boolean checkDateRangeTypeForSearchWord(String searchWord) {
		return searchWord.matches(".*(\\d{4})-(\\d{2})-(\\d{2})~(\\d{4})-(\\d{2})-(\\d{2})");
	}

	public static boolean checkDateTypeForSearchWord(String searchWord) {
		return searchWord.matches(".*(\\d{4})-(\\d{2})-(\\d{2})");
	}

	/**
	 * <b>Discord Bot을 통해 조회 시 나이 범위 형식으로 조회할 때, 정확한 값이 들어왔는지 확인하기 위한 Method</b>
	 * @param searchWord 나이 범위 조회 검색어
	 * @return 형식에 맞는지 확인하여 맞다면 true 아니면 false 반환
	 */
	public static boolean checkAgeRangeTypeForSearchWord(String searchWord) {
		return searchWord.matches(".*(\\d{2})~(\\d{2})");
	}

	/**
	 * <b>Discord Bot을 통해 상세 조회 시 PK 값이 들어왔는지 확인하기 위한 Method</b>
	 * @param searchWord Log ID 조회 검색어
	 * @return 형식에 맞는지 확인하여 맞다면 true 아니면 false 반환
	 */
	public static boolean checkRegularPK(String searchWord) {
		return Integer.parseInt(searchWord) >= 0;
	}

	/**
	 * <b>Discord Bot을 통해 조회 시 나이 형식으로 조회할 때, 정확한 값이 들어왔는지 확인하기 위한 Method</b>
	 * @param searchWord 나이 조회 검색어
	 * @return 형식에 맞는지 확인하여 맞다면 true 아니면 false 반환
	 */

	public static boolean checkAgeTypeForSearchWord(String searchWord) {
		return searchWord.matches(".*(\\d{2})");
	}

	/**
	 * <b>구글 스프레드 시트에 기존에는 4년제 졸업과 같이 최종 학력을 작성했고, 최근 에는 공백 졸업을 빼고, 작성하고 있는데, 공백과 졸업을 없애기 위한 Method</b>
	 * @param lastEducational 구글 스프레드 시트에 저장된 최종 학력 정보
	 * @return 공백과 졸업이 없어진 최종 학력 정보
	 */

	public static String processLastEducational(String lastEducational) {
		if (lastEducational.contains(" ")) {
			return Arrays.stream(lastEducational.split(" "))
				.filter(lastEducation -> !lastEducation.equals("졸업"))
				.collect(Collectors.joining());
		}
		return lastEducational;
	}

	public static String processSchoolName(String schoolName) {
		String[] split;
		if (schoolName.contains("고")) {
			split = schoolName.split("고");
			return split[0] + "고";
		} else if (schoolName.contains("대학교")) {
			split = schoolName.split("대학교");
			return split[0] + "대학교";
		} else if (schoolName.contains("대학원")) {
			split = schoolName.split("대학원");
			return split[0] + "대학원";
		} else {
			return schoolName;
		}
	}

	/**
	 * <b>개인 정보 수집 동의 여부, 추가 사항 동의 여부를 검색할 때, Data Base에 저장된 값이 Boolean Type 이기 때문에 특정 문자열로 들어오는 검색어를 Boolean Type으로 변경하는 Method</b>
	 * @param crewSuggestSearchDTO Client에서 입력한 검색을 위한 내용을 담은 객체
	 */

	public static void checkAgreeType(CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO) {
		if (crewSuggestSearchDTO.getSearchType().equals("PRIVACY_INFO_AGREE") && crewSuggestSearchDTO.getSearchWord()
			.equals("동의")) {
			crewSuggestSearchDTO.setInDBPrivacyInfoAgree(true);
		} else if (crewSuggestSearchDTO.getSearchType().equals("PRIVACY_INFO_AGREE")
			&& crewSuggestSearchDTO.getSearchWord().equals("미동의")) {
			crewSuggestSearchDTO.setInDBPrivacyInfoAgree(false);
		} else if (crewSuggestSearchDTO.getSearchType().equals("ADDENDUM_INFO_AGREE")
			&& crewSuggestSearchDTO.getSearchWord().equals("동의")) {
			crewSuggestSearchDTO.setInDBAddendumInfoAgree(true);
		} else if (crewSuggestSearchDTO.getSearchType().equals("ADDENDUM_INFO_AGREE")
			&& !crewSuggestSearchDTO.getSearchWord().equals("미동의")) {
			crewSuggestSearchDTO.setInDBAddendumInfoAgree(false);
		}
	}

	/**
	 * <b>Discord Bot을 통해 API 조회 시 Boolean Type으로 반환 되는 값을 알아보기 쉽게 문자열로 변환하기 위한 Method</b>
	 * @param AgreeInfo API 조회 시 Boolean Type으로 반환 되는 개인 정보 수집 및 추가 사항 동의 여부 값
	 * @return 알아보기 쉽게 문자열로 변환한 값
	 */
	public static StringBuilder changeAgreeBooleanToString(boolean AgreeInfo) {
		if (AgreeInfo) {
			return new StringBuilder("동의");
		} else {
			return new StringBuilder("미 동의");
		}
	}

	/**
	 * <b>지원 순서가 4자리로 3자리 일 때, 앞에 0을 붙혀 문자열로 관리하는데, Data Base에는 정수로 저장 되어 있어 0이 짤리기 때문에 이를 붙혀주기 위한 Method</b>
	 * <b>단, 100의 자리 이하일 때만 4자리를 맞추기 위한 0을 붙혀주고, 1000의 자리일 경우 붙히지 않음.</b>
	 * @param suggestId Data Base에 저장된 Suggest ID 정수값
	 * @return 0을 붙힌 Suggest ID 문자열
	 */
	public static StringBuilder changeSuggestId(int suggestId) {
		if (suggestId < 0) {
			return new StringBuilder();
		}

		int length = (int)(Math.log10(suggestId) + 1);

		if (length == 1) {
			return new StringBuilder("000" + suggestId);
		} else if (length == 2) {
			return new StringBuilder("00" + suggestId);
		} else if (length == 3) {
			return new StringBuilder("0" + suggestId);
		} else {
			return new StringBuilder(suggestId);
		}
	}
}
