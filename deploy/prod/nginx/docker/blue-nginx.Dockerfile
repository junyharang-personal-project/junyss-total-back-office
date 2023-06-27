# Base Image
FROM nginx:latest

# 컨테이너 안의 /app 디렉터리를 활용 디렉터리(Work Dircetory)로 설정
WORKDIR /etc/nginx

# 임의로 생성한 설정 파일 컨테이너 내부에 복사
COPY ./conf.d/nginx.blue.conf /etc/nginx/nginx.conf

# Port 설정
EXPOSE 1000

# container 실행 시 자동으로 실행할 command. nginx 시작함
CMD ["nginx", "-g", "daemon off;"]