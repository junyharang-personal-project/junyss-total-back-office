package com.giggalpeople.backoffice.chatops.discord.chatbot.common.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2><b>실제 Discord Bot이 API 조회가 아닌 응답 Message를 만드는 Class</b></h2>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscordBotResponseMessage {

    /**
     * <b>Discord Bot 상태 Message를 무작위로 추출하기 위한 Method</b>
     * @return List에 담긴 Random 문자열
     */

    public static String discordBotStatusRandomMessage () {
        List<String> discordBotStatusMessageList = new ArrayList<>();
        double random = Math.random();

        discordBotStatusMessageList.add("코딩 😃");
        discordBotStatusMessageList.add("생각 🤔");
        discordBotStatusMessageList.add("명령 대기 🫡");
        discordBotStatusMessageList.add("크루 생각 😤");
        discordBotStatusMessageList.add("휴식 🥱");

        return discordBotStatusMessageList.get((int) Math.round(random * (discordBotStatusMessageList.size() - 1)));
    }

    /**
     * <b>사용자가 디스코드 봇에게 API 조회가 아닌 명령어가 입력 되었을 때, 상황에 맞는 무작위 선택된 응답 메시지를 만들기 위한 Method</b>
     * @return 디스코드 봇이 응답할 문자열 메시지
     */

    public static String existCommandRandomMessage (String status) {
        List<String> responseMessageList = new ArrayList<>();
        double random = Math.random();
        String iAm = "저는 ";
        String botName = "기깔";
        String crewName = botName + "나는 사람들";
        String prefixMessage = "님 안녕하세요? 👋 ";
        String suffixMessage = "이에요! 😁";

        if (status.equalsIgnoreCase("안녕")) {
            responseMessageList.add(prefixMessage + "저는 기깔나는 " + botName + suffixMessage);
            responseMessageList.add("님 오늘도 좋은 하루 보내고 계신가요? 😎");
            responseMessageList.add("님 저를 찾으셨어요? 😲");
            responseMessageList.add("님 다시 만나 반가워요! 😃");
            responseMessageList.add("님 저는 지금 너무 바빠요! 🤫");

        } else if ((status.contains("누구야") || (status.contains("소개")))) {
            responseMessageList.add(prefixMessage + iAm + "기깔나는 " + botName + "이에요! 🤪");
            responseMessageList.add(prefixMessage + iAm + crewName + "의 프로젝트를 돕고 있는 " + botName + suffixMessage);
            responseMessageList.add(prefixMessage + iAm + crewName + "의 영원한 동반자 " + botName + suffixMessage);
            responseMessageList.add(prefixMessage + iAm + crewName + "의 주니님이 JAVA로 만든 " + botName + suffixMessage);
            responseMessageList.add(prefixMessage + iAm + crewName + "의 당신의 사이버 친구 " + botName + suffixMessage);
        } else {
            return "님 명령어를 확인해 주세요.";
        }
        return responseMessageList.get((int) Math.round(random * (responseMessageList.size() - 1)));
    }

    /**
     * <b>Discord Bot에게 사용할 수 있는 명령어 모음</b>
     * @return Discord Bot 명령어 사용법
     */
    public static String commandManual() {
        return " 님 안녕하세요? "
                + "\n기깔나는 사람들의 디스코드 친구 기깔이에요!"
                + "\n\n 기깔이에게 명령을 하고 싶다면 아래 규칙을 따라 주세요!"
                + "\n\n 첫째: 저를 부르지 않으면 저는 동작하지 않아요. 디스코드 메시지에 기깔이, 기깔아, 기깔쓰, 기깔봇 등 기깔이 들어 가 있다면 동작할 거에요. 😎"
                + "\n\n 둘째: 저를 부르고 한 칸을 띄워 특정 문자열이 맞으면 그에 맞는 내용을 찾아 드릴 거에요."
                + "\n 저는 간단한 인사와 소개를 할 수 있게 만들어져 있어요. 저를 부르고, 안녕 혹은 누구야, 소개 라는 키워드가 들어가게 명령을 주세요! "
                + "\n 참고로 API 호출을 통해 특정 값을 알아내려고 할 때 각 명령어 마다 사용할 수 있는 권한이 있어요. "
                + "\n\n\n 명령어 별 권한 안내 : "
                + "\n 1. Project Manager (PM) - PM님은 API를 호출하여 모든 정보를 확인할 수 있는 권한을 가지고 있어요. 아래 명령어를 모두 사용할 수 있죠! 😀 "
                + "\n\n 2. Project Leader (PL) - PL님은 API를 호출하여 지원자목록조회, 크루목록조회, 크루상세조회 명령어를 이용할 수 있어요! 😀 "
                + "\n\n 3. Team Leader (TL) - TL님들은 API를 호출하여 지원자목록조회, 지원자상세조회 명령어를 이용할 수 있어요! 😀 "
                + "\n\n 그리고, 나머지 크루원 분들은 개인 정보 보호를 위해 지원자목록조회, 크루목록조회 명령어만 사용할 수 있어요! 😀 "
                + "\n\n\n API 호출 방법 : "
                + "\n 크루목록조회 - API 호출을 통해 기깔나는 사람들 활동 크루 목록 조회 (예시: 기깔아 크루목록조회)"
                + "\n\n 지원자목록조회(검색) - API 호출을 통해 기깔나는 사람들 활동 크루 목록 조회 시 검색을 통해 조회"
                + "\n\n 사용 가능 명령 Option"
                + "\n 1) 지원날짜 (예시: 기깔아 크루목록조회 -지원날짜=2023-01-01~2023-03-01 혹은 기깔아 크루목록조회 -지원날짜=2023-01-01)"
                + "\n\n 2) 검색경로 (예시: 기깔아 크루목록조회 -검색경로=인프런)"
                + "\n\n 3) 이름 (예시: 기깔아 크루목록조회 -이름=홍길동)"
                + "\n\n 4) 성별 (예시: 기깔아 크루목록조회 -성별=남자)"
                + "\n\n 5) 생년월일 (예시: 기깔아 크루목록조회 -생년월일=1990-03-27~1996-02-11 기깔아 크루목록조회 -생년월일=1990-03-27)"
                + "\n\n 6) 나이 (예시: 기깔아 크루목록조회 -나이=27~33 혹은 기깔아 크루목록조회 -나이=25)"
                + "\n\n 7) mbti (예시: 기깔아 크루목록조회 -mbti=ISTJ)"
                + "\n\n 8) 학력 - 종류 : 고등학교, 전문대, 4년제, 석사, 박사 (예시: 기깔아 크루목록조회 -학력=고등학교)"
                + "\n\n 9) 최종학력학교 - 종류 : 특정 고등학교 검색 시 OO고, 특정 대학교 검색 시 OO대학교, 특정 대학원 검색 시 OO대학원 (예시: 기깔아 크루목록조회 -최종학력학교=서울대학교)"
                + "\n\n 10) 거주지주변역 (예시: 기깔아 크루목록조회 -거주지주변역=성수역)"
                + "\n\n 14) 대면만남일 (예시: 기깔아 크루목록조회 -대면만남일=2023-01-01~2023-03-01 혹은 기깔아 크루목록조회 -대면만남일=2023-01-01)"
                + "\n\n 15) 지원파트 - 검색어 종류 : Project Manager(PM) - project, manager, pm)"
                + "\n 서비스 기획 - 서비스, 기획, plan, service"
                + "\n WEB / APP 디자이너 - web, app, design, ui, ux, 디자"
                + "\n BackEnd 개발자 - back, 백엔, 빽엔, 빼갠, 빼겐, 서버, server"
                + "\n FrontEnd 개발자 - front, 프엔, 프론트"
                + "\n DMSO(Dev ML Ops Sec) - dmso, dev, ops, ml, sec, infra, 인프라, 데브, 옵브, 엠엘, 쎅, 보안"
                + "\n 화이트 햇(White Hat) - hat, hacker"
                + "\n IOS 개발자 - swi, ios, apple, 아이오에스, 스위프트, 스프, 애플"
                + "\n Android 개발자 - an, android, google, 안드, 로이드, 구글"
                + "\n AI 개발자 - ai, 인공, 지능, 머신, 러닝 "
                + "\n 법률 전문가 - law, 법, 전문"
                + "\n 예시: 기깔아 크루목록조회 -지원파트=back"
                + "\n\n Paging 처리 - Data Base에는 많은 지원자와 크루들의 정보가 저장 되어 있어요. 이를 보다 편안하게 보기 위해 Paging 처리가 되어 있고, 크루 여러분은 Paging 명령어를 통해 보다 정확한 정보를 얻을 수 있어요. Paging 처리시에는 현재 페이지의 위치한 Page 번호를 입력하고, 데이터 출력 개수에 한번에 출력할 데이터 개수를 입력하면 돼요. 😀 "
                + "\n 예시:) 기깔아 크루목록조회 -현재페이지=1 -데이터출력개수=10"
                + "\n\n 다른 검색 명령어와 Paging 처리 함께 사용 - 위의 검색어와 함께 Paging 처리 명령어를 함께 사용할 수 있어요. 😀 주의할 점은 반드시 Paging 처리에 대한 명령어를 먼저 작성해 줘야 한다는 것이에요."
                + "\n 예시:) 기깔아 크루목록조회 -현재페이지=1 -데이터출력개수=10 -나이=23~30"
                + "\n\n 크루상세조회 - API 호출을 통해 기깔나는 사람들 활동 크루의 상세 정보 조회 명령어 (예시: 기깔아 크루상세조회 2023-002-0181(크루 번호))"
                + "\n\n\n\n 지원자목록조회 - API 호출을 통해 기깔나는 사람들 지원자 목록 조회 (예시: 기깔아 지원자목록조회)"
                + "\n\n 지원자목록조회(검색) - API 호출을 통해 기깔나는 사람들 활동 크루 목록 조회 시 검색을 통해 조회"
                + "\n 사용 가능 명령 Option"
                + "\n\n 1) 지원날짜 (예시: 기깔아 지원자목록조회 -지원날짜=2023-01-01~2023-03-01 혹은 기깔아 지원자목록조회 -지원날짜=2023-01-01)"
                + "\n\n 2) 검색경로 (예시: 기깔아 지원자목록조회 -검색경로=인프런)"
                + "\n\n 3) 이름 (예시: 기깔아 지원자목록조회 -이름=홍길동)"
                + "\n\n 4) 성별 (예시: 기깔아 지원자목록조회 -성별=남자)"
                + "\n\n 5) 생년월일 (예시: 기깔아 지원자목록조회 -생년월일=1990-03-27~1996-02-11 혹은 기깔아 지원자목록조회 -생년월일=1990-03-27)"
                + "\n\n 6) 나이 (예시: 기깔아 지원자목록조회 -나이=23~25 혹은 기깔아 지원자목록조회 -나이=23)"
                + "\n\n 7) mbti (예시: 기깔아 지원자목록조회 -mbti=ISTJ)"
                + "\n\n 8) 학력 - 종류 : 고등학교, 전문대, 4년제, 석사, 박사 (예시: 기깔아 지원자목록조회 -학력=고등학교)"
                + "\n\n 9) 최종학력학교 - 종류 : 특정 고등학교 검색 시 OO고, 특정 대학교 검색 시 OO대학교, 특정 대학원 검색 시 OO대학원 (예시: 기깔아 지원자목록조회 -최종학력학교=서울대학교)"
                + "\n\n 10) 거주지주변역 (예시: 기깔아 지원자목록조회 -거주지주변역=구의역)"
                + "\n\n 11) 개인정보수집동의여부 - 종류 : 동의 합니다., 동의하지 않습니다. (예시: 기깔아 지원자목록조회 -개인정보수집동의여부=동의)"
                + "\n\n 12) 추가사항동의여부 - 종류 : 동의 합니다., 동의하지 않습니다. (예시: 기깔아 지원자목록조회 -추가사항동의여부=미동의)"
                + "\n\n 13) 합류상태 - 종류 : 검토, 참가, 테스트, 대면만남예정, 합류포기, 중도포기, 퇴출, 팀장합류거부, PM합류거부, 대표합류거부 (예시: 기깔아 지원자목록조회 -합류상태=대면만남예정)"
                + "\n\n 14) 대면만남일 (예시: 기깔아 지원자목록조회 -대면만남일=2023-01-01~2023-03-01 혹은 기깔아 지원자목록조회 -대면만남일=2023-01-01)"
                + "\n\n 15) 지원파트 - 검색어 종류 : Project Manager(PM) - project, manager, pm)"
                + "\n 서비스 기획 - 서비스, 기획, plan, service"
                + "\n WEB / APP 디자이너 - web, app, design, ui, ux, 디자"
                + "\n BackEnd 개발자 - back, 백엔, 빽엔, 빼갠, 빼겐, 서버, server"
                + "\n FrontEnd 개발자 - front, 프엔, 프론트"
                + "\n DMSO(Dev ML Ops Sec) - dmso, dev, ops, ml, sec, infra, 인프라, 데브, 옵브, 엠엘, 쎅, 보안"
                + "\n 화이트 햇(White Hat) - hat, hacker"
                + "\n IOS 개발자 - swi, ios, apple, 아이오에스, 스위프트, 스프, 애플"
                + "\n Android 개발자 - an, android, google, 안드, 로이드, 구글"
                + "\n AI 개발자 - ai, 인공, 지능, 머신, 러닝 "
                + "\n 법률 전문가 - law, 법, 전문"
                + "\n 예시: 기깔아 지원자목록조회 -지원파트=back"
                + "\n\n Paging 처리 - 예시:) 기깔아 지원자목록조회 -현재페이지=1 -데이터출력개수=10"
                + "\n\n 다른 검색 명령어와 Paging 처리 함께 사용 - 위의 검색어와 함께 Paging 처리 명령어를 함께 사용할 수 있어요. 😀 주의할 점은 반드시 Paging 처리에 대한 명령어를 먼저 작성해 줘야 한다는 것이에요."
                + "\n 예시:) 기깔아 지원자목록조회 -현재페이지=1 -데이터출력개수=10 -나이=23~30"
                + "\n\n 지원자상세조회 - API 호출을 통해 기깔나는 사람들 지원자 상세 정보 조회 명령어 예시 : 기깔아 지원자상세조회 0164(지원 순서 번호)"
                + "\n\n\n\n 이용자목록조회 - API 호출을 통해 기깔나는 사람들 Application 이용 접속 및 요청 정보 목록 조회 (예시: 기깔아 이용자목록조회)"
                + "\n\n 이용자목록조회(검색) - API 호출을 통해 기깔나는 사람들 Application 이용 접속 및 요청 정보 목록 조회 시 검색을 통해 조회"
                + "\n\n 사용 가능 명령 Option"
                + "\n 1) 요청일 (예시: 기깔아 이용자목록조회 -요청일=2023-01-01~2023-03-01 혹은 기깔아 이용자목록조회 -요청일=2023-01-01)"
                + "\n\n 2) 서버이름 (예시: 기깔아 이용자목록조회 -서버이름=통합관리서버)"
                + "\n\n 3) 서버주소 (예시: 기깔아 이용자목록조회 -서버주소=192.168.0.3)"
                + "\n\n 4) 이용자주소 (예시: 기깔아 이용자목록조회 -이용자주소=168.126.63.1"
                + "\n\n Paging 처리 - Data Base에는 많은 이용자 요청 및 접속 정보가 저장 되어 있어요. 이를 보다 편안하게 보기 위해 Paging 처리가 되어 있고, 크루 여러분은 Paging 명령어를 통해 보다 정확한 정보를 얻을 수 있어요. Paging 처리시에는 현재 페이지의 위치한 Page 번호를 입력하고, 데이터 출력 개수에 한번에 출력할 데이터 개수를 입력하면 돼요. 😀 "
                + "\n 예시:) 기깔아 이용자목록조회 -현재페이지=1 -데이터출력개수=10"
                + "\n\n 다른 검색 명령어와 Paging 처리 함께 사용 - 위의 검색어와 함께 Paging 처리 명령어를 함께 사용할 수 있어요. 😀 주의할 점은 반드시 Paging 처리에 대한 명령어를 먼저 작성해 줘야 한다는 것이에요."
                + "\n 예시:) 기깔아 이용자목록조회 -현재페이지=1 -데이터출력개수=10 -이용자주소=168.126.63.1"
                + "\n\n 이용자상세조회 - API 호출을 통해 기깔나는 사람들 이용자 요청 및 접속 정보의 상세 정보 조회 명령어 (예시: 기깔아 이용자상세조회 1(순서 번호))"
                + "\n\n\n 로그목록조회 - API 호출을 통해 기깔나는 사람들 Application Error Log 정보 목록 조회 (예시: 기깔아 로그목록조회)"
                + "\n\n 로그목록조회(검색) - API 호출을 통해 기깔나는 사람들 Application Error Log 정보 목록 조회 시 검색을 통해 조회"
                + "\n\n 사용 가능 명령 Option"
                + "\n 1) 생성일 (예시: 기깔아 로그목록조회 -생성일=2023-01-01~2023-03-01 혹은 기깔아 로그목록조회 -생성일=2023-01-01)"
                + "\n\n 2) 레벨 (예시: 기깔아 로그목록조회 -레벨=ERROR)"
                + "\n 2-1) 레벨 종류(TRACE, DEBUG, INFO, WARN, ERROR, OFF)"
                + "\n\n 3) 서버이름 (예시: 기깔아 로그목록조회 -서버이름=통합관리서버)"
                + "\n\n 4) 서버주소 (예시: 기깔아 로그목록조회 -서버주소=192.168.0.3)"
                + "\n\n 5) 이용자주소 (예시: 기깔아 로그목록조회 -이용자주소=168.126.63.1"
                + "\n\n 6) 간략정보 (예시: 기깔아 로그목록조회 -간략정보=java.lang.NullPointerException: null"
                + "\n\n Paging 처리 - Data Base에는 많은 이용자 요청 및 접속 정보가 저장 되어 있어요. 이를 보다 편안하게 보기 위해 Paging 처리가 되어 있고, 크루 여러분은 Paging 명령어를 통해 보다 정확한 정보를 얻을 수 있어요. Paging 처리시에는 현재 페이지의 위치한 Page 번호를 입력하고, 데이터 출력 개수에 한번에 출력할 데이터 개수를 입력하면 돼요. 😀 "
                + "\n 예시:) 기깔아 로그목록조회 -현재페이지=2 -데이터출력개수=10"
                + "\n\n 다른 검색 명령어와 Paging 처리 함께 사용 - 위의 검색어와 함께 Paging 처리 명령어를 함께 사용할 수 있어요. 😀 주의할 점은 반드시 Paging 처리에 대한 명령어를 먼저 작성해 줘야 한다는 것이에요."
                + "\n 예시:) 기깔아 로그목록조회 -현재페이지=2 -데이터출력개수=10 -간략정보=java.lang.NullPointerException"
                + "\n\n 로그상세조회 - API 호출을 통해 기깔나는 사람들 Error Log 정보의 상세 정보 조회 명령어 (예시: 기깔아 로그상세조회 1(순서 번호))"
                + "\n\n ⚠️ 반드시 저를 부를 때는 저를 부르시고, 한 칸 띄어서 명령어를 주어야 한다는 점 잊으면 안되는 것이에요! 😤";
    }
}
