#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
SAVE_LOG_DATE=$(date +"%y-%m-%d")

echo "====================================================================================================="
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Green Container 서비스 설정 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Nginx Green Container 서비스 설정 작업======================================"
echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)"

HOST_NGINX_CONFIG_DIR="/data/deploy/giggal-total-back-office/deploy/prod/was/nginx"
HOST_NGINX_DEFAULT_CONFIG_PATH_DIR="/data/deploy/giggal-total-back-office/deploy/prod/was/nginx/conf.d"
NGINX_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/was/shell-script/nginx"
NGINX_CONFIG_DIR="/etc/nginx"
DEFAULT_CONFIG_DIR="/etc/nginx/conf.d"
NGINX_CONTAINER_NAME="nginx-total-back-office-green"

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office"

  if [ -d "$LOG_DIR" ];
  then
    echo "=====================================================================================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 설정 작업이 시작 되었어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application 배포======================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      exit 1
  fi

  errorPageSetting
}

errorPageSetting() {

  echo "[$NOW] [INFO] 임의의 Error Page 사용을 위해 설정 작업을 시작할게요."
  echo "[$NOW] [INFO] 임의의 Error Page 사용을 위해 설정 작업을 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] 최초 Nginx Container 안에 error.html File을 저장할 Directory 생성 작업을 시작할게요."
  echo "[$NOW] [INFO] 최초 Nginx Container 안에 error.html File을 저장할 Directory 생성 작업을 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  if ! docker exec $NGINX_CONTAINER_NAME mkdir -p /var/www/html;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"

    echo "[$NOW] [INFO] 새롭게 작성된 error.html File Nginx Container /var/www/html 위치에 복사 할게요."
    echo "[$NOW] [INFO] 새롭게 작성된 error.html File Nginx Container /var/www/html 위치에 복사 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    command="docker cp ${HOST_NGINX_CONFIG_DIR}/error.html $NGINX_CONTAINER_NAME:/var/www/html/error.html"

    if ! docker cp ${HOST_NGINX_CONFIG_DIR}/error.html $NGINX_CONTAINER_NAME:/var/www/html/error.html;
    then
      failedCommand "${command}"
    else
      successCommand "${command}"

      nginxContainerDefaultConfigSetting
    fi
  fi
}

nginxContainerDefaultConfigSetting() {
  echo "[$NOW] [INFO] 기존 Nginx Container 기본 default.conf File 보안 설정 내용 추가를 위해 제거 할게요."
  echo "[$NOW] [INFO] 기존 Nginx Container 기본 default.conf File 보안 설정 내용 추가를 위해 제거 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  command="docker exec ${NGINX_CONTAINER_NAME} rm -rf /etc/nginx/con.f/default.conf"

  if ! docker exec ${NGINX_CONTAINER_NAME} rm -rf /etc/nginx/con.f/default.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  echo "[$NOW] [INFO] 보안 설정이 추가되어 새롭게 작성된 default.conf File 복사 할게요."
  echo "[$NOW] [INFO] 보안 설정이 추가되어 새롭게 작성된 default.conf File 복사 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  command="docker cp ${HOST_NGINX_DEFAULT_CONFIG_PATH_DIR}/default.conf $NGINX_CONTAINER_NAME:${DEFAULT_CONFIG_DIR}/default.conf"

  if ! docker cp ${HOST_NGINX_DEFAULT_CONFIG_PATH_DIR}/default.conf $NGINX_CONTAINER_NAME:${DEFAULT_CONFIG_DIR}/default.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"

    nginxContainerNginxConfigSetting
  fi
}

nginxContainerNginxConfigSetting() {
  echo "[$NOW] [INFO] 기존 Nginx Container 기본 nginx.conf File 제거 할게요."
  echo "[$NOW] [INFO] 기존 Nginx Container 기본 nginx.conf File 제거 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  command="docker exec ${NGINX_CONTAINER_NAME} rm -rf /etc/nginx/nginx.conf"

  if ! docker exec ${NGINX_CONTAINER_NAME} rm -rf /etc/nginx/nginx.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  echo "[$NOW] [INFO] 새롭게 작성된 nginx.conf File 복사 할게요."
  echo "[$NOW] [INFO] 새롭게 작성된 nginx.conf File 복사 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  command="docker cp ${HOST_NGINX_CONFIG_DIR}/nginx.green.conf $NGINX_CONTAINER_NAME:${NGINX_CONFIG_DIR}/nginx.conf"

  if ! docker cp ${HOST_NGINX_CONFIG_DIR}/nginx.green.conf $NGINX_CONTAINER_NAME:${NGINX_CONFIG_DIR}/nginx.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요."
  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

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
  echo "[$NOW] [ERROR] ${command} 명령어 작업 실패하였어요. 스크립트를 종료합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  exit 1
}

successCommand() {
  local command=$1

  echo "[$NOW] [INFO] ${command} 명령어 작업 성공하였어요."
  echo "[$NOW] [INFO] ${command} 명령어 작업 성공하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
}

checkLogDirectory

operationDockerStatus=$(docker ps -a)

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Green Container 서비스 설정 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Green Container 서비스 설정 작업이 끝났어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo -e "=====================================================================================================\n"
echo -e "=====================================================================================================\n" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1