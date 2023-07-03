#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
SAVE_LOG_DATE=$(date +"%y-%m-%d")

echo "====================================================================================================="
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Docker Container Health Check 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api  Nginx Docker Container Health Check 작업======================================"
echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)"

TARGET_CONTAINER_NAME=$1

echo "[$NOW] [INFO] Nginx Health Check 대상 Container Name: ${TARGET_CONTAINER_NAME}"

NGINX_BLUE_CONTAINER_NAME="nginx-total-back-office-blue"

NGINX_BLUE_EXTERNAL_PORT=1000
NGINX_GREEN_EXTERNAL_PORT=1010

SERVER_IP=192.168.20.12

NGINX_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/shell-script/nginx"

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office"

  if [ -d "$LOG_DIR" ];
  then
    echo "=====================================================================================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Docker Container Health Check 작업이 시작 되었어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application Docker Container Health Check======================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] Nginx Health Check 대상 Container Name: ${TARGET_CONTAINER_NAME}" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      exit 1
  fi

  nginxHealthCheck
}

nginxHealthCheck() {
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} Health Check 시작할게요."
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} Health Check 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  if [ "$TARGET_CONTAINER_NAME" == "$NGINX_BLUE_CONTAINER_NAME" ];
  then
    nginxExternalPortNumber=$NGINX_BLUE_EXTERNAL_PORT
  else
    nginxExternalPortNumber=$NGINX_GREEN_EXTERNAL_PORT
  fi

  echo "[$NOW] [INFO] 사용 명령어: curl -I http://${SERVER_IP}:${nginxExternalPortNumber}/api/test/profile | grep -oP 'HTTP/1.1 \K\d+' "
  echo "[$NOW] [INFO] 사용 명령어: curl -I http://${SERVER_IP}:${nginxExternalPortNumber}/api/test/profile | grep -oP 'HTTP/1.1 \K\d+' " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  for retryCount in {1..10}
  do
    sleep 5
    echo "[$NOW] [INFO] ${retryCount} 번째 Health Check가 시작 되었어요."
    echo "[$NOW] [INFO] ${retryCount} 번째 Health Check가 시작 되었어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    responseCode=$(curl -I http://${SERVER_IP}:${nginxExternalPortNumber}/api/test/profile | grep -oP 'HTTP/1.1 \K\d+')
    command="curl -I http://${SERVER_IP}:${nginxExternalPortNumber}/api/test/profile | grep -oP 'HTTP/1.1 \K\d+'"

    if [ "$responseCode" == "200" ];
    then
      successCommand "${command}"

    else
      echo "[$NOW] [WARN] ${TARGET_CONTAINER_NAME}가 기동 중이지만, 내부 서비스에 문제가 있어요."
      echo "[$NOW] [WARN] ${TARGET_CONTAINER_NAME}가 기동 중이지만, 내부 서비스에 문제가 있어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [WARN] ${TARGET_CONTAINER_NAME} 삭제 및 재 기동 실시합니다."
      echo "[$NOW] [WARN] ${TARGET_CONTAINER_NAME} 삭제 및 재 기동 실시합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      nginxDockerContainerErrorRemove "${nginxExternalPortNumber}"
    fi

    responseCount=$(curl -I http://${SERVER_IP}:${nginxExternalPortNumber}/api/test/profile | grep "HTTP" | wc -l)
    command="curl -I http://${SERVER_IP}:${nginxExternalPortNumber}/api/test/profile | grep "HTTP" | wc -l"

    # up_count >= 1
    if [ ${responseCount} -ge 1 ];
    then
      echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} Container 상태가 정상이에요."
      echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} Container 상태가 정상이에요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      successCommand "${command}"
      break

    else
      errorResponse=$(curl -I http://${SERVER_IP}:${nginxExternalPortNumber}/api/test/profile)

      echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container 상태에 문제가 있어요."
      echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container 상태에 문제가 있어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [ERROR] 문제 내용 : ${errorResponse}"
      echo "[$NOW] [ERROR] 문제 내용 : ${errorResponse}" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container 종료 및 삭제 시도할게요."
      echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      nginxDockerContainerErrorRemove "${nginxExternalPortNumber}"
    fi

    if [ "${retryCount}" -eq 10 ];
      then
        echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container Health 상태 문제가 있어요."
        echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

        echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container 종료 및 삭제 시도할게요."
        echo "[$NOW] [ERROR] ${TARGET_CONTAINER_NAME} Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

        nginxDockerContainerErrorRemove "${nginxExternalPortNumber}"
    fi

    echo "[$NOW] [WARN] ${TARGET_CONTAINER_NAME} Health Check 작업에 실패하였어요."
    echo "[$NOW] [WARN] ${TARGET_CONTAINER_NAME} Health Check 작업에 실패하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다."
    echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    exit 1
  done
}

nginxDockerContainerErrorRemove() {
  echo "[$NOW] [INFO] 기존 ${TARGET_CONTAINER_NAME} Docker Container 중지 및 삭제 작업을 시작할게요."
  echo "[$NOW] [INFO] 기존 ${TARGET_CONTAINER_NAME} Docker Container 중지 및 삭제 작업을 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  local portNumber=$1

  stopContainerId=$(docker ps --filter "name=$TARGET_CONTAINER_NAME" --format "{{.ID}}")

  echo "[$NOW] [INFO] 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : "
  echo "[$NOW] [INFO] 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${TARGET_CONTAINER_NAME} "
  echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${TARGET_CONTAINER_NAME}  : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} 컨테이너 Port Number : ${portNumber} "
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} 컨테이너 Port Number : ${portNumber} : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} 컨테이너 Docker Image Name : ${APPLICATION_DOCKER_IMAGE_NAME} "
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} 컨테이너 Docker Image Name : ${APPLICATION_DOCKER_IMAGE_NAME} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${TARGET_CONTAINER_NAME} "
  echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${TARGET_CONTAINER_NAME} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} 컨테이너 ID : ${stopContainerId} "
  echo "[$NOW] [INFO] ${TARGET_CONTAINER_NAME} 컨테이너 ID : ${stopContainerId} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  if ! docker stop $TARGET_CONTAINER_NAME;
  then
    echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다."
    echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    exit 1

  else
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요."
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 진행할게요."
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 진행할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    if ! docker rm $TARGET_CONTAINER_NAME;
    then
      echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다."
      echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      exit 1

    else
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요."
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${TARGET_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      $NGINX_SHELL_SCRIPT_DIRECTORY/nginxDockerContainerRun.sh;
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Nginx Docker Container Health Check 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Nginx Docker Container Health Check 작업이 끝났어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo -e "=====================================================================================================\n"
echo -e "=====================================================================================================\n" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1