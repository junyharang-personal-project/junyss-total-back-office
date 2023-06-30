# Base Image
FROM nginx:latest

# nginx 컨테이너에 기본적으로 탑재되어 있는 Config File 제거
#RUN rm -rf /etc/nginx/conf.d/default.conf

# 컨테이너 안의 /app 디렉터리를 활용 디렉터리(Work Dircetory)로 설정
WORKDIR /etc/nginx

# 임의로 생성한 설정 파일 컨테이너 내부에 복사
COPY conf.d/nginx.conf /etc/nginx/nginx.conf

# Port 설정
EXPOSE 1010

# container 실행 시 자동으로 실행할 command. nginx 시작함
CMD ["nginx", "-g", "daemon off;"]