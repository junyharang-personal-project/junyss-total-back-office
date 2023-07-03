#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
SAVE_LOG_DATE=$(date +"%y-%m-%d")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 DMZ NGINX Docker Container 존재 여부 확인 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api DMZ NGINX Docker Container Container 존재 여부 확인======================================"
echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)"

NGINX_CONTAINER_NAME="nginx-total-back-office-dmz"

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office"

  if [ -d "$LOG_DIR" ];
  then
    echo "[INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Docker Container 존재 여부 확인 작업이 시작 되었어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application Docker Container 존재 여부 확인======================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      exit 1
  fi

  checkNginxContainerExistenceStatus
}

checkNginxContainerExistenceStatus() {
  echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 존재 여부 확인할게요."
  echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  NGINX_DOCKER_STATUS=$(docker ps -aqf "name=$NGINX_CONTAINER_NAME")

  if [[ -z "$NGINX_DOCKER_STATUS" ]];
  then
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} Container 존재하지 않아요."
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} Container 존재하지 않아요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    $NGINX_SHELL_SCRIPT_DIRECTORY/nginxDockerContainerRun.sh;

  else
    echo "[$NOW] [INFO] ${NGINX_BLUE_CONTAINER_NAME}, ${NGINX_GREEN_CONTAINER_NAME} 컨테이너 존재합니다."
    echo "[$NOW] [INFO] ${NGINX_BLUE_CONTAINER_NAME}, ${NGINX_GREEN_CONTAINER_NAME} 컨테이너 존재합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    checkNginxStatus
  fi
}