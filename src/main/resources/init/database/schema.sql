drop table if exists crew;
drop table if exists suggest;
drop table if exists log;
drop table if exists log_level;
drop table if exists connected_user_request_info;
drop table if exists connected_user;
drop table if exists data_created_date_time;
drop table if exists server_info;

create table data_created_date_time (
    data_created_date_time_id bigint AUTO_INCREMENT comment 'Data 생성 순서',
    data_created_date varchar(10) not null comment '이용자 요청 날짜',
    data_created_time varchar(10) not null comment '이용자 요청 시각',
    primary key (data_created_date_time_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Log, 이용자 관련 Data 생성 일시 정보';

create table server_info (
     internal_server_id bigint AUTO_INCREMENT comment 'WAS 순서 번호',
     server_name varchar(50) not null comment 'Server Application 이름',
     server_vm_info varchar(50) not null comment 'Server VM 정보',
     server_os_info varchar(50) not null comment 'Server OS 정보',
     server_ip varchar(50) not null comment 'Server IP',
     server_environment varchar(5) not null comment '서버 구동 환경',
     primary key (internal_server_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='내부 서버 정보';

create table suggest (
  suggest_id varchar(4) not null comment '지원 순서',
  how_know_info varchar(50) not null comment '우리를 알게 된 경로',
  crew_number varchar(15) unique comment '크루 번호',
  suggest_date DATE not null comment '지원서 접수 일시',
  name varchar(50) not null  comment '이름',
  phone_number varchar(64) not null comment '핸드폰 번호',
  sex varchar(5) comment '성별',
  email varchar(255) comment 'Email 주소',
  birth_date varchar(255) not null comment '생년월일',
  age_info varchar(5) not null comment '나이',
  mbti varchar(64) comment 'MBTI',
  personality text not null comment '성격 상세',
  job_info text not null comment '현재 직업',
  last_educational varchar(255) comment '최종 학력',
  school_name varchar(255) comment '최종 학력 학교 이름',
  company_name varchar(40) comment '회사명',
  tistory varchar(255) comment '티스토리 계정',
  figma varchar(255) comment '피그마 계정',
  notion varchar(255) comment '노션 계정',
  blog_url varchar(255) comment '개인 블로그 주소',
  station_name varchar(100) not null comment '거주지 주변 역 이름',
  suggest_part varchar(20) comment '지원 Part',
  tech_stack text not null comment '기술력 상세',
  career text comment '경력 상세',
  github_address text comment 'Git Hub 주소',
  privacy_info_agree boolean not null comment '개인정보 수집 동의 여부',
  addendum_info_agree boolean not null comment '추가 사항 동의 여부',
  portfolio text comment '포트폴리오',
  etc text comment '추가로 하고 싶은 말',
  participation varchar(15) comment '참여 여부 상태',
  meet_date varchar(25) comment '대면 면담 일자',
  drinking_info varchar(255) comment '음주 여부',
  hobby varchar(255) comment '취미',
  personal_class_info text comment '개인 활동 상세',
  love_whether TINYINT(1) comment '연애 여부',
  note text comment '비고',
  primary key (suggest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='기깔나는 사람들 지원자 정보';

create table crew (
   crew_join_id bigint PRIMARY KEY AUTO_INCREMENT comment '합류 순서',
   suggest_id varchar(4) UNIQUE not null comment '지원 순서',
   join_date varchar(20) not null comment '합류 일자',
   user_id varchar(15) not null comment '내부 시스템 계정(ID)',
   password varchar(128) not null comment '내부 시스템 계정(비밀번호)',
   crew_role varchar(20) not null comment '직책(역할)',
   crew_alias varchar(50) not null comment '내부 시스템 별칭',
   foreign key (suggest_id)
   references suggest(suggest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='기깔나는 사람들 활동 인원 정보';

create table connected_user (
  connected_user_id bigint PRIMARY KEY AUTO_INCREMENT comment '이용자 접속 기록 순서',
  internal_server_id bigint NOT NULL comment '내부 서버 등록 순서 번호',
  data_created_date_time_id bigint NOT NULL comment 'Data 생성 순서',
  connected_user_count bigint default '1' comment '이용자 접속 횟수',
  user_ip varchar(150) not null comment '이용자 IP 정보',
  user_environment text comment '이용자 접속 환경 정보',
  user_location text not null comment '이용자 위치 정보',
  foreign key (internal_server_id)
      references server_info(internal_server_id),
  foreign key (data_created_date_time_id)
  references data_created_date_time(data_created_date_time_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='서비스 이용자 정보';

create table connected_user_request_info (
    connected_user_request_info_id bigint PRIMARY KEY AUTO_INCREMENT comment '이용자 요청 기록 순서',
    internal_server_id bigint NOT NULL comment '내부 서버 등록 순서 번호',
    data_created_date_time_id bigint NOT NULL comment 'Data 생성 순서',
    connected_user_id bigint not null comment '이용자 접속 기록 순서',
    request_header text comment '요청 Header 정보',
    user_cookies text comment '요청 쿠키 정보',
    request_parameter text comment '요청 Parameter 정보',
    request_body text comment '요청 Body 정보',
    foreign key (internal_server_id)
    references server_info(internal_server_id),
    foreign key (data_created_date_time_id)
    references data_created_date_time(data_created_date_time_id),
    foreign key (connected_user_id)
    references connected_user(connected_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='서비스 이용자 요청 정보';

create table log_level (
   log_level_id bigint PRIMARY KEY AUTO_INCREMENT comment 'Log 레벨 순서',
   level varchar(6) not null comment 'Log Level'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WAS 문제 내용 레벨 정보';

create table log (
    log_id bigint PRIMARY KEY AUTO_INCREMENT comment 'Log 순서',
    internal_server_id bigint NOT NULL comment '내부 서버 등록 순서 번호',
    data_created_date_time_id bigint NOT NULL comment 'Data 생성 순서',
    connected_user_id bigint not null comment '이용자 접속 기록 순서',
    connected_user_request_info_id bigint not null comment '이용자 요청 기록 순서',
    log_level_id bigint not null comment 'Log 레벨 순서',
    exception_brief text not null comment 'Exception 간략 정보',
    exception_detail text not null comment 'Exception 상세',
    foreign key (internal_server_id)
        references server_info(internal_server_id),
    foreign key (data_created_date_time_id)
        references data_created_date_time(data_created_date_time_id),
    foreign key (connected_user_id)
        references connected_user(connected_user_id),
    foreign key (connected_user_request_info_id)
        references connected_user_request_info(connected_user_request_info_id),
    foreign key (log_level_id)
        references log_level(log_level_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WAS 문제 내용 정보';