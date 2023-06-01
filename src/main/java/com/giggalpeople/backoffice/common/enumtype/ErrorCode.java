package com.giggalpeople.backoffice.common.enumtype;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {
    /**
     * 공통 에러 1000~1999
     */
    BAD_REQUEST(1000, "Bad Request"),
    PARAMETER_NULL(1001, "%P1 파라미터 null"),
    PARAMETER_SIGN(1002, "%P1 파라미터 특수문자 포함"),
    PARAMETER_SPACE(1003, "%P1 파라미터 공백을 포함"),
    PARAMETER_LONG(1004, "%P1 파라미터의 길이가 허용기준 초과"),
    PARAMETER_SHORT(1005, "%P1 파라미터의 길이가 허용기준 미달"),
    PARAMETER_INVALID(1006, "%P1 파라미터의 형식이 맞지 않음"),
    JSON_PARSER_ERROR(1007, "json 파싱 에러"),
    WRONG_CONTENT_TYPE(1008, "잘못된 content-type"),
    WRONG_FILE_NAME(1009, "잘못된 파일 이름"),
    WRONG_FILE_EXTENSION(1010, "잘못된 파일 확장자"),
    WRONG_PROFILE_IMAGE_URL(1011, "잘못된 프로필 이미지 주소"),
    NOT_FOUND(1012, "찾을 수 없음"),
    ALREADY_EXIST_URL(1013, "URL 기 존재"),
    CREATE_API_URL_ERROR(1014, "Discord Bot을 통한 API 호출 URL 만들기 실패"),
    DISCORD_BOT_API_CALL_FAILURE(1015, "Discord Bot을 통한 API 호출 실패"),
    COMMON_DATA_DATE_TIME_SAVE_FAILURE(1016, "이용자 접속 정보, 이용자 요청 정보, Error Log에서 공통으로 사용하는 저장 날짜, 시각 정보 저장 실패"),

    API_PROCESSING_TAKES_LONG(1017, "API 동작 시간이 500ms 이상 소요 오래 걸림"),
    API_PROCESSING_TAKES_TOO_LONG(1018, "API 동작 시간이 5000ms 이상 소요 너무 오래 걸림"),


    /**
     * 권한 에러 2000~2999
     */
    NO_AUTHORIZATION(2000, "권한 없음"),
    NO_AUTHENTICATION(2001, "인증정보 없음"),
    INVALID_TOKEN(2002, "토큰이 유효하지 않음"),
    EXPIRED_TOKEN(2003, "토큰이 만료됨"),
    /**
     * 시스템 에러  90000~ 99999
     */
    EXTERNAL_SERVER_ERROR(90000, "외부 연동 서버 에러"),
    DELAYED_SERVICE(90001, "서비스 지연"),
    SYSTEM_INSPECTION(90002, "시스템 점검"),
    INTERNAL_SERVER_ERROR(99999, "내부 서버 에러"),
    /**
     * 로그인 3000~3999
     */
    NO_MEMBER(3000, "회원 미존재"),
    WRONG_USER(3001, "잘못된 이메일 또는 비밀번호"),

    /**
     * 회원가입 4000~4999
     */
    DUPLICATED_NICKNAME(4000, "중복된 닉네임"),
    DUPLICATED_EMAIL(4001, "중복된 이메일"),
    SERVICE_TERMS_DISAGREE(4002, "서비스 이용 필수약관 미동의"),
    PERSONAL_TERMS_DISAGREE(4003, "개인정보 필수약관 미동의"),
    PASSWORD_MISMATCHED(4004, "비밀번호 불일치"),
    SAME_PASSWORD_FROM_OLD_PASSWORD(4005, "기존 비밀번호와 변경할 비밀번호 일치"),
    WRONG_PASSWORD(4006, "잘못된 비밀번호"),
    /**
     * 지원자 관련 5000~5999
     */
    EXIST_SUGGEST(5000, "지원자 기 존재"),
    NOT_EXIST_SUGGEST(5001, "지원자 미존재"),
    WRONG_SUGGEST_URL(5002, "잘못된 지원자 관련 URL 주소"),

    /**
     * 크루 관련 6000~6999
     */
    EXIST_CREW(5000, "크루 기 존재"),
    NOT_EXIST_CREW(5001, "크루 미존재"),
    WRONG_CREW_URL(5002, "잘못된 크루 관련 URL 주소"),

    /**
     * Log 관련 7000~7999
     */
    ERROR_SAVE_FAILURE(7000, "로그 저장 실패"),
    NOT_EXIST_ERROR_LOG(7001, "로그 조회 실패 - Data Base에 요청 결과값이 없습니다."),
    CREATE_DISCORD_APPEND_MESSAGE_FAILURE(7002, "Discord Appender Message 생성 실패"),
    NOT_FOUND_ERROR_LOG_LEVEL(7003, "Error Log 저장 시 Error Log Level 값을 Data Base에서 찾을 수 없음"),
    /**
     * Discord Bot 관련 8000~8999
     */
    API_RESPONSE_NOT_FOUND(8000, "Discord Bot을 통한 API 조회 실패"),

    /**
     * 접속 이용자 관련 9000 ~ 99999
     */
    CONNECTED_USER_SAVE_FAILURE(9000, "접속 이용자 정보 저장 실패"),
    CONNECTED_USER_REQUEST_SAVE_FAILURE(9001, "접속 이용자 요청 정보 저장 실패"),
    CONNECTED_USER_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE(9002, "동일 접속 이용자 횟수 저장 실패"),
    CONNECTED_USER_REQUEST_SAME_INFO_UPDATE_COUNT_SAVE_FAILURE(9003, "동일 접속 이용자 동일 요청 정보 횟수 저장 실패"),
    NOT_EXIST_CONNECTED_USER(9004, "Application 접속 이용자 정보 조회 실패 - Data Base에 요청 결과값이 없습니다."),
    NOT_EXIST_CONNECTED_USER_JSON_PARSING_ERROR(9005, "Application 접속 이용자 정보 조회 실패 - JSON 값을 보기 좋게 변경하는데 실패 하였습니다."),

    /**
     * 내부 서버 관리 관련 10000 ~ 19999
     */
    CONNECTED_INTERNAL_SERVER_SAVE_FAILURE(10000, "기깔나는 사람들 WAS 정보 저장 실패");

    private final Integer statusCode;
    private final String message;

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage(String... args) {
        return parseMessage(this.message, args);
    }

    public static String parseMessage(String message, String... args) {
        if (message == null || message.trim().length() <= 0) {
            return message;
        }

        if (args == null || args.length <= 0) {
            return message;
        }

        String[] splitMessages = message.split("%");
        if (splitMessages.length <= 1) {
            return message;
        }

        for (int i = 0; i < args.length; i++) {
            String replaceChar = "%P" + (i + 1);
            message = message.replaceFirst(replaceChar, args[i]);
        }
        return message;
    }
}
