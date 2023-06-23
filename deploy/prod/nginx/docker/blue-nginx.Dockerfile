# Base Image
FROM nginx:latest

# 실제 Docker 기동 서버의 Build 된 뒤 만들어진 설정 file 위치 지정
ARG CONFIG_FILE=./conf.d/nginx.blue.conf

# 컨테이너 안의 /app 디렉터리를 활용 디렉터리(Work Dircetory)로 설정
WORKDIR /etc/nginx

# nginx 컨테이너에 기본적으로 탑재되어 있는 Config File 제거
RUN rm -rf /etc/nginx/conf.d/nginx.conf /etc/nginx/conf.d/nginx.conf

# 임의로 생성한 설정 파일 컨테이너 내부에 복사
COPY ${CONFIG_FILE} /etc/nginx/conf.d/nginx.conf