# 우분투 최신 버전 베이스 이미지 내려 받기
FROM openjdk:11

#작성자
MAINTAINER 주니 <junyharang8592@gmail.com>

# 실제 Docker 기동 서버의 Build 된 뒤 만들어진 jar file 위치 지정
ARG JAR_FILE=./build/libs/*.jar

# 컨테이너 안에 /app 디렉토리 생성
# 컨테이너 안에는 기본적으로 root권한으로 만들어짐
# 만일, 운영계정을 별도로 만들어 관리한다면 폴더 소유자 혹은 권한에 대한 설정 필요.
RUN mkdir -p /app

# 컨테이너 안의 /app 디렉터리를 활용 디렉터리(Work Dircetory)로 설정
WORKDIR /app

# Build가 된 뒤 만들어진 jar File을 컨테이너 내부 /app Directory 밑에 복사
COPY ${JAR_FILE} /app/application.jar

# TIME Zone 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/application.jar", "-Djava.net.preferIPv4Stack=true", "-Duser.timezone=Asia/Seoul"]
