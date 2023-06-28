#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Green Docker Container 존재 여부 확인 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Application 배포 배포 서버 작업 중 Nginx Green Docker Container 존재 여부 확인 작업======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

NGINX_DOCKER_IMAGE_NAME="giggal-people/nginx-giggal-total-back-office-api-green"
NGINX_CONTAINER_NAME="nginx-total-back-office-green"
NGINX_EXTERNAL_PORT_NUMBER=1000

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office/nginx"

  if [ -d "$LOG_DIR" ];
  then
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Green Docker Container 존재 여부 확인 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application 배포 배포 서버 작업 중 Nginx Green Docker Container 존재 여부 확인 작업======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
  fi

  checkContainerExistenceStatus
}

checkContainerExistenceStatus() {
    echo "[$NOW] [INFO] Nginx Green 환경 기준 컨테이너 존재 여부 확인할게요."
    echo "[$NOW] [INFO] Nginx Green 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    NGINX_DOCKER_STATUS=$(docker ps -aqf "name=$NGINX_CONTAINER_NAME")

    if [[ -z "$NGINX_DOCKER_STATUS" ]];
    then
      echo "[$NOW] [INFO] Nginx Green 존재하지 않아요."
      echo "[$NOW] [INFO] Nginx Green 존재하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      nginxDockerContainerRun

    else
      echo "[$NOW] [INFO] Nginx Green 존재합니다."
      echo "[$NOW] [INFO] Nginx Green 존재합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    fi
}

nginxDockerContainerRun() {
  echo "[$NOW] [INFO] Nginx Green 기동 할게요."
  echo "[$NOW] [INFO] Nginx Green 기동 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Nginx Green 컨테이너 기동 작업을 시작할게요."
  echo "[$NOW] [INFO] Nginx Green 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand=$(docker run -itd --privileged \
                    --name $NGINX_CONTAINER_NAME \
                    --hostname $NGINX_CONTAINER_NAME \
                    -e container=docker \
                    -p $NGINX_EXTERNAL_PORT_NUMBER:80 \
                    --restart unless-stopped \
                    $NGINX_DOCKER_IMAGE_NAME)

#                    -v ${NGINX_CONFIG_DIR}:${NGINX_CONTAINER_CONFIG_DIR} \

  command="docker run -itd --privileged --name $NGINX_CONTAINER_NAME --hostname $NGINX_CONTAINER_NAME -e container=docker -p $NGINX_EXTERNAL_PORT_NUMBER:80 --restart unless-stopped $NGINX_DOCKER_IMAGE_NAME"

  if [ -z "$dockerRunCommand" ];
  then
    failedCommand "${command}"
  else
    successCommand "${command}"

    checkNginxStatus
  fi
}

checkNginxStatus() {
  echo "[$NOW] [INFO] NGINX 기동 여부를 확인할게요."
  echo "[$NOW] [INFO] NGINX 기동 여부를 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
#  EXIST_NGINX=$(docker ps | grep ${DOCKER_CONTAINER_NGINX_NAME})
  EXIST_NGINX=$(docker ps --format '{{.Names}}' | grep "^${NGINX_CONTAINER_NAME}\$")

  if [ -z "$EXIST_NGINX" ];
    then
      echo "[$NOW] [INFO] NGINX Green Container 기동 중이지 않아 기동 합니다."
      echo "[$NOW] [INFO] NGINX Green Container 기동 중이지 않아 기동 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      nginxDockerContainerRun

    else
      echo "[$NOW] [INFO] NGINX Green Container 기동 중이에요."
      echo "[$NOW] [INFO] NGINX Green NGINX Container 기동 중이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      ./nginxGreenContainerStatusCheck.sh
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

chmod +x ./nginxGreenContainerStatusCheck.sh

checkLogDirectory

operationDockerStatus=$(docker ps -a)

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Green Docker Container 존재 여부 확인 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Green Docker Container 존재 여부 확인 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1