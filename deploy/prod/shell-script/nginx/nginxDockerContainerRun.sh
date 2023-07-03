#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
SAVE_LOG_DATE=$(date +"%y-%m-%d")

echo "====================================================================================================="
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Docker Container 기동 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api  Nginx Docker Container 기동 작업======================================"
echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)"

NGINX_BLUE_CONTAINER_NAME="nginx-total-back-office-blue"
NGINX_GREEN_CONTAINER_NAME="nginx-total-back-office-green"

NGINX_BLUE_EXTERNAL_PORT=1000
NGINX_GREEN_EXTERNAL_PORT=1010

NGINX_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/shell-script/nginx"


checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office"

  if [ -d "$LOG_DIR" ];
  then
    echo "=====================================================================================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Docker Container 존재 여부 확인 작업이 시작 되었어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application Docker Container 존재 여부 확인======================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      exit 1
  fi

  nginxDockerContainerRun
}

nginxDockerContainerRun() {
  echo "[$NOW] [INFO] Nginx Container 기동 할게요."
  echo "[$NOW] [INFO] Nginx Container 기동 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  for loopCount in {1..2}
  do
    if [ $loopCount -eq 1 ];
    then
      echo "[$NOW] [INFO] 최초 Nginx Blue Container 기동 할게요."
      echo "[$NOW] [INFO] 최초 Nginx Blue Container 기동 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      nginxContainerAndHostName=$NGINX_BLUE_CONTAINER_NAME
      nginxContainerPortNumber=$NGINX_BLUE_EXTERNAL_PORT
    else
      echo "[$NOW] [INFO] Nginx Green Container 기동 할게요."
      echo "[$NOW] [INFO] Nginx Green Container 기동 할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      nginxContainerAndHostName=$NGINX_GREEN_CONTAINER_NAME
      nginxContainerPortNumber=$NGINX_GREEN_EXTERNAL_PORT
    fi

    dockerRunCommand=$(docker run -itd --privileged \
                      --name $nginxContainerAndHostName \
                      --hostname $nginxContainerAndHostName \
                      -e container=docker \
                      -p $nginxContainerPortNumber:80 \
                      --restart unless-stopped \
                      nginx:latest)

    command="docker run -itd --privileged --name $nginxContainerAndHostName --hostname $nginxContainerAndHostName -e container=docker -p $nginxContainerPortNumber:80 --restart unless-stopped nginx:latest"

    if [ -z "$dockerRunCommand" ];
    then
      failedCommand "${command}"
    else
      successCommand "${command}"

      checkNginxStatus "${nginxContainerAndHostName}"
    fi
  done
}

checkNginxStatus() {
  local nginxContainerAndHostName=$1
  echo "[$NOW] [INFO] ${nginxContainerAndHostName} 기동 여부를 확인할게요."
  echo "[$NOW] [INFO] ${nginxContainerAndHostName} 기동 여부를 확인할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  containerId=$(docker ps --filter "name=$nginxContainerAndHostName" --format "{{.ID}}")
  checkContainerStatus=$(docker ps --filter "id=$containerId" --format "{{.Status}}")

  sleep 5

  containerLogs=$(docker logs "$containerId")

  echo "[$NOW] [INFO] ${nginxContainerAndHostName} 기동 여부 확인 대상 Container ID : ${containerId} "
  echo "[$NOW] [INFO] ${nginxContainerAndHostName} 기동 여부 확인 대상 Container ID : ${containerId} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  if [[ $checkContainerStatus != "Up"* ]];
  then
    echo "[$NOW] [INFO] ${nginxContainerAndHostName} Container 기동 중이지 않아 기동 합니다."
    echo "[$NOW] [INFO] ${nginxContainerAndHostName} Container 기동 중이지 않아 기동 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : "
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [ERROR] $containerLogs"
    echo "[$NOW] [ERROR] $containerLogs" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    failedCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "

    nginxDockerContainerRun

  else
    echo "[$NOW] [INFO] ${nginxContainerAndHostName} Container 기동 중이에요."
    echo "[$NOW] [INFO] ${nginxContainerAndHostName} Container 기동 중이에요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : "
    echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] $containerLogs"
    echo "[$NOW] [INFO] $containerLogs" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    if [ "$nginxContainerAndHostName" == "$NGINX_BLUE_CONTAINER_NAME" ];
    then
      $NGINX_SHELL_SCRIPT_DIRECTORY/nginxBlueServiceSetting.sh;
    else
      $NGINX_SHELL_SCRIPT_DIRECTORY/nginxGreenServiceSetting.sh;
    fi
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Nginx Docker Container 기동 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Nginx Docker Container 기동 작업이 끝났어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo -e "=====================================================================================================\n"
echo -e "=====================================================================================================\n" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1