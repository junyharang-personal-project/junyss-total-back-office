#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue Docker Container 존재 여부 확인 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Application 배포======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

NGINX_DOCKER_IMAGE_NAME="giggal-people/nginx-giggal-total-back-office-api-blue"
NGINX_CONTAINER_NAME="nginx-total-back-office-blue"
NGINX_EXTERNAL_PORT_NUMBER=1000

NGINX_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/shell-script/nginx"

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office/nginx"

  if [ -d "$LOG_DIR" ];
  then
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue Docker Container 존재 여부 확인 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application 배포======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
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
    echo "[$NOW] [INFO] Nginx Blue 환경 기준 컨테이너 존재 여부 확인할게요."
    echo "[$NOW] [INFO] Nginx Blue 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    NGINX_DOCKER_STATUS=$(docker ps -aqf "name=$NGINX_BLUE_CONTAINER_NAME")

    if [[ -z "$NGINX_DOCKER_STATUS" ]];
    then
      echo "[$NOW] [INFO] Nginx Blue가 존재하지 않아요."
      echo "[$NOW] [INFO] Nginx Blue가 존재하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      nginxDockerContainerRun

    else
      echo "[$NOW] [INFO] Nginx Blue가 존재합니다."
      echo "[$NOW] [INFO] Nginx Blue가 존재합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      checkNginxStatus
    fi
}

nginxDockerContainerRun() {
  echo "[$NOW] [INFO] Nginx Blue 기동 할게요."
  echo "[$NOW] [INFO] Nginx Blue 기동 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Nginx Blue 컨테이너 기동 작업을 시작할게요."
  echo "[$NOW] [INFO] Nginx Blue 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand=$(docker run -itd --privileged \
                    --name $NGINX_CONTAINER_NAME \
                    --hostname $NGINX_CONTAINER_NAME \
                    -e container=docker \
                    -p $NGINX_EXTERNAL_PORT_NUMBER:$NGINX_EXTERNAL_PORT_NUMBER \
                    --restart unless-stopped \
                    $NGINX_DOCKER_IMAGE_NAME)

#                    -v ${NGINX_CONFIG_DIR}:${NGINX_CONTAINER_CONFIG_DIR} \

  command="docker run -itd --privileged --name $NGINX_CONTAINER_NAME --hostname $NGINX_CONTAINER_NAME -e container=docker -p $NGINX_EXTERNAL_PORT_NUMBER:$NGINX_EXTERNAL_PORT_NUMBER --restart unless-stopped $NGINX_DOCKER_IMAGE_NAME"

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
#  EXIST_NGINX=$(docker ps --format '{{.Names}}' | grep "${NGINX_BLUE_CONTAINER_NAME}")

  containerId=$(docker ps --filter "name=$NGINX_CONTAINER_NAME" --format "{{.ID}}")
  checkContainerStatus=$(docker ps --filter "id=$containerId" --format "{{.Status}}")

  sleep 5

  containerLogs=$(docker logs "$containerId")

  echo "[$NOW] [INFO] NGINX 기동 여부 확인 대상 ${NGINX_BLUE_CONTAINER_NAME} Container ID : ${containerId} "
  echo "[$NOW] [INFO] NGINX 기동 여부 확인 대상 ${NGINX_BLUE_CONTAINER_NAME} Container ID : ${containerId} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

#  if [ -z "$EXIST_NGINX" ];
  if [[ $checkContainerStatus != "Up"* ]];
    then
      echo "[$NOW] [INFO] NGINX BLUE Container 기동 중이지 않아 기동 합니다."
      echo "[$NOW] [INFO] NGINX BLUE Container 기동 중이지 않아 기동 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : "
      echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [ERROR] $containerLogs"
      echo "[$NOW] [ERROR] $containerLogs" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      failedCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "

      nginxDockerContainerRun

    else
      echo "[$NOW] [INFO] NGINX BLUE Container 기동 중이에요."
      echo "[$NOW] [INFO] NGINX BLUE NGINX Container 기동 중이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : "
      echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [INFO] $containerLogs"
      echo "[$NOW] [INFO] $containerLogs" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      if ! $NGINX_SHELL_SCRIPT_DIRECTORY/blue/nginxBlueContainerStatusCheck.sh;
      then
        successCommand "$NGINX_SHELL_SCRIPT_DIRECTORY/nginxBlueHealthCheck.sh"
      else
        failedCommand "$NGINX_SHELL_SCRIPT_DIRECTORY/nginxBlueHealthCheck.sh"
      fi
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue Docker Container 존재 여부 확인 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue Docker Container 존재 여부 확인 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1