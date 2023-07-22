package com.giggalpeople.backoffice.common.util;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.BAD_REQUEST;

import com.giggalpeople.backoffice.api.record.model.dto.request.TotalErrorRecordSaveRequestDto;
import com.giggalpeople.backoffice.api.user.exception.ConnectedUserException;
import com.giggalpeople.backoffice.common.env.ServerType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

	public static String translateEscapes(String str) {
		if (str.isEmpty()) {
			return "";
		}
		char[] chars = str.toCharArray();
		int length = chars.length;
		int from = 0;
		int to = 0;
		while (from < length) {
			char ch = chars[from++];
			if (ch == '\\') {
				ch = from < length ? chars[from++] : '\0';
				switch (ch) {
					case 'b':
						ch = '\b';
						break;
					case 'f':
						ch = '\f';
						break;
					case 'n':
						ch = '\n';
						break;
					case 'r':
						ch = '\r';
						break;
					case 's':
						ch = ' ';
						break;
					case 't':
						ch = '\t';
						break;
					case '\'':
					case '\"':
					case '\\':
						// as is
						break;
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
						int limit = Integer.min(from + (ch <= '3' ? 2 : 1), length);
						int code = ch - '0';
						while (from < limit) {
							ch = chars[from];
							if (ch < '0' || '7' < ch) {
								break;
							}
							from++;
							code = (code << 3) | (ch - '0');
						}
						ch = (char)code;
						break;
					case '\n':
						continue;
					case '\r':
						if (from < length && chars[from] == '\n') {
							from++;
						}
						continue;
					default: {
						String msg = String.format(
							"Invalid escape sequence: \\%c \\\\u%04X",
							ch, (int)ch);
						throw new IllegalArgumentException(msg);
					}
				}
			}

			chars[to++] = ch;
		}
		return new String(chars, 0, to);
	}

	/**
	 * <b>Error Log 발생으로 인한 API 발동 시 발생 날짜와 시각을 나누기 위한 Method</b>
	 * @param totalErrorRecordSaveRequestDto 이용자 정보, 이용자 접속 정보, Error Log 등의 정보를 담은 요청 DTO
	 */
	public static void dateTimeSplit(TotalErrorRecordSaveRequestDto totalErrorRecordSaveRequestDto) {
		String[] splitCreateDateTime = totalErrorRecordSaveRequestDto.getCreatedAt().split(" ");

		totalErrorRecordSaveRequestDto.setCreatedDate(splitCreateDateTime[0]);
		totalErrorRecordSaveRequestDto.setCreatedTime(splitCreateDateTime[1]);
	}

	/**
	 * <b>Error Log 검색과 Application 이용자 접속 및 요청 정보 조회 시 내부 서버 이름으로 검색할 때, 검색어를 한글로 입력받고, 내부에 Data Base에 검색 조건에 맞게 처리하기 위한 Method</b>
	 * @param inputSearchType 이용자가 검색 시 검색 Type으로 입력한 서버 이름 조회 명령어
	 * @param searchWord 서버 종류 (한글) 검색어
	 * @return Data Base 검색 조건에 맞게 변경된 검색어
	 */
	public static String checkSearchCommandForInternalServerName(String inputSearchType, String searchWord) {

		if (inputSearchType != null && inputSearchType.equals("SERVER_NAME")) {
			if (searchWord.equals(ServerType.TOTAL_BACK_OFFICE.getSearchCommand()) || searchWord.equals(
				ServerType.TOTAL_BACK_OFFICE.getDescription())) {
				return ServerType.TOTAL_BACK_OFFICE.getDescription();
			} else {
				throw new ConnectedUserException(BAD_REQUEST, BAD_REQUEST.getMessage());
			}
		}
		return "";
	}
}
