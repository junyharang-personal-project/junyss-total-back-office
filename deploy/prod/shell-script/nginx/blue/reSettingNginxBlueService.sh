#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 설정 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Nginx Blue 설정 작업======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

NGINX_CONFIG_DIR="/data/deploy/giggal-total-back-office/deploy/prod/nginx/docker/conf.d"
NGINX_BLUE_CONTAINER_NAME="nginx-total-back-office-blue"

APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER=1001
APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER=1002

SERVER_IP=127.0.0.1

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office/nginx"

  if [ -d "$LOG_DIR" ];
  then
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 설정 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application 배포======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
  fi

  nginxContainerReSetting
}

nginxContainerReSetting() {
  for loopCount in {1..2}
  do
    if [ "$loopCount" == 1 ];
    then
      nginxConfUpdateLine=15
      applicationExternalPortNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER
    else
      nginxConfUpdateLine=17
      applicationExternalPortNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER
    fi 
    
    command="sed -i "${nginxConfUpdateLine}s/.*/server ${SERVER_IP}:${applicationExternalPortNumber};\g" ${NGINX_CONFIG_DIR}/nginx.blue.conf"
  
    if ! sed -i "${nginxConfUpdateLine}s/.*/server ${SERVER_IP}:${applicationExternalPortNumber};\g" ${NGINX_CONFIG_DIR}/nginx.blue.conf;
    then
      failedCommand "${command}"
    else
      successCommand "${command}"
    fi
  
    command="cp ${NGINX_CONFIG_DIR}/nginx.blue.conf ${NGINX_CONFIG_DIR}/nginx.conf"
  
    if ! cp ${NGINX_CONFIG_DIR}/nginx.blue.conf ${NGINX_CONFIG_DIR}/nginx.conf;
    then
      failedCommand "${command}"
    else
      successCommand "${command}"
    fi
  
    echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요."
    echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  
    command="docker exec ${NGINX_BLUE_CONTAINER_NAME} nginx -s reload"
  
    if ! docker exec ${NGINX_BLUE_CONTAINER_NAME} nginx -s reload;
    then
      failedCommand "${command}"
    else
      successCommand "${command}"
    fi
  done
}

checkLogDirectory

operationDockerStatus=$(docker ps -a)

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 설정 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 설정 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1