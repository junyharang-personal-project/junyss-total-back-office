#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "====================================================================================================="
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue Container 서비스 설정 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Nginx Blue Container 서비스 설정 작업======================================"
echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)"

HOST_NGINX_CONFIG_DIR="/data/deploy/giggal-total-back-office/deploy/dev/nginx/conf.d"
NGINX_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/dev/shell-script/nginx"
NGINX_CONFIG_DIR="/etc/nginx"
NGINX_CONTAINER_NAME="nginx-total-back-office-blue"

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office"

  if [ -d "$LOG_DIR" ];
  then
    echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 설정 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application 배포======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
  fi

  nginxContainerSetting
}

nginxContainerSetting() {
  echo "[$NOW] [INFO] 기존 Nginx Container 기본 nginx.conf File 제거 할게요."
  echo "[$NOW] [INFO] 기존 Nginx Container 기본 nginx.conf File 제거 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  command="docker exec ${NGINX_CONTAINER_NAME} rm -rf /etc/nginx/nginx.conf"

  if ! docker exec ${NGINX_CONTAINER_NAME} rm -rf /etc/nginx/nginx.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  echo "[$NOW] [INFO] 새롭게 작성된 nginx.conf File 복사 할게요."
  echo "[$NOW] [INFO] 새롭게 작성된 nginx.conf File 복사 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  command="docker cp ${HOST_NGINX_CONFIG_DIR}/nginx.blue.conf $NGINX_CONTAINER_NAME:${NGINX_CONFIG_DIR}/nginx.conf"

  if ! docker cp ${HOST_NGINX_CONFIG_DIR}/nginx.blue.conf $NGINX_CONTAINER_NAME:${NGINX_CONFIG_DIR}/nginx.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요."
  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  command="docker exec ${NGINX_CONTAINER_NAME} nginx -s reload"

  if ! docker exec ${NGINX_CONTAINER_NAME} nginx -s reload;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"

    $NGINX_SHELL_SCRIPT_DIRECTORY/nginxHealthCheck.sh "${NGINX_CONTAINER_NAME}";
  fi
}

failedCommand() {
  local command=$1

  echo "[$NOW] [ERROR] ${command} 명령어 작업 실패하였어요. 스크립트를 종료합니다."
  echo "[$NOW] [ERROR] ${command} 명령어 작업 실패하였어요. 스크립트를 종료합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  exit 1
}

successCommand() {
  local command=$1

  echo "[$NOW] [INFO] ${command} 명령어 작업 성공하였어요."
  echo "[$NOW] [INFO] ${command} 명령어 작업 성공하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
}

checkLogDirectory

operationDockerStatus=$(docker ps -a)

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue Container 서비스 설정 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue Container 서비스 설정 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo -e "=====================================================================================================\n"
echo -e "=====================================================================================================\n" >> $LOG_DIR/"$NOW"-deploy.log 2>&1