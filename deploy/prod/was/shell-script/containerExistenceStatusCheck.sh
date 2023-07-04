#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
SAVE_LOG_DATE=$(date +"%y-%m-%d")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Docker Container 존재 여부 확인 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Docker Container 존재 여부 확인======================================"
echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)"

APPLICATION_DOCKER_IMAGE_NAME="giggal-people/total-back-office-api"

#APPLICATION_BLUE_A_CONTAINER_NAME="giggal-total-back-office-api-blue-a"
#APPLICATION_BLUE_B_CONTAINER_NAME="giggal-total-back-office-api-blue-b"
#APPLICATION_GREEN_A_CONTAINER_NAME="giggal-total-back-office-api-green-a"
#APPLICATION_GREEN_B_CONTAINER_NAME="giggal-total-back-office-api-green-b"

APPLICATION_BLUE_CONTAINER_NAME="giggal-total-back-office-api-blue"
APPLICATION_GREEN_CONTAINER_NAME="giggal-total-back-office-api-green"

#APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER=1001
#APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER=1002
#APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER=1011
#APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER=1012

APPLICATION_BLUE_EXTERNAL_PORT_NUMBER=1001
APPLICATION_GREEN_EXTERNAL_PORT_NUMBER=1011

APPLICATION_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/was/shell-script/application"
NGINX_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/was/shell-script/nginx"

NGINX_CONTAINER_NAME="nginx-total-back-office"

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

    checkApplicationContainerExistenceStatus

    $NGINX_SHELL_SCRIPT_DIRECTORY/nginxDockerContainerRun.sh;

  else
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 존재합니다."
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 존재합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    checkNginxStatus
  fi
}

checkNginxStatus() {
    echo "[$NOW] [INFO] Nginx 컨테이너 동작 여부 확인할게요."
    echo "[$NOW] [INFO] Nginx 컨테이너 동작 여부 확인할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    containerId=$(docker ps --filter "name=$NGINX_CONTAINER_NAME" --format "{{.ID}}")
    checkContainerStatus=$(docker ps --filter "id=$containerId" --format "{{.Status}}")

    sleep 5

    containerLogs=$(docker logs "$containerId")

    if [[ $checkContainerStatus == "Up"* ]];
    then
      echo "[$NOW] [INFO] 기동 중인 ${NGINX_CONTAINER_NAME} Container 내부 Log 정보 : "
      echo "[$NOW] [INFO] 기동 중인 ${NGINX_CONTAINER_NAME} Container 내부 Log 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo -e "[$NOW] [INFO] $containerLogs \n"
      echo -e "[$NOW] [INFO] $containerLogs \n" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    successCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "

    checkApplicationContainerExistenceStatus

    else
      echo "[$NOW] [ERROR] 문제 발생한 ${NGINX_CONTAINER_NAME} Container 내부 Log 정보 : "
      echo "[$NOW] [ERROR] 문제 발생한 ${NGINX_CONTAINER_NAME} Container 내부 Log 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [ERROR] $containerLogs"
      echo "[$NOW] [ERROR] $containerLogs" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      nginxDockerContainerRemove
    fi
}

nginxDockerContainerRemove() {

#  local targetRemoveContainerName=$1

  echo "[$NOW] [INFO] 기존 ${NGINX_CONTAINER_NAME} Container 중지 및 삭제 작업을 시작할게요."
  echo "[$NOW] [INFO] 기존 ${NGINX_CONTAINER_NAME} Container 포함한 중지 및 삭제 작업을 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  stopContainerId=$(docker ps --filter "name=$NGINX_CONTAINER_NAME" --format "{{.ID}}")

  if ! docker stop $NGINX_CONTAINER_NAME;
  then
    echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다."
    echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    exit 1

  else
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요."
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 진행할게요."
    echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 진행할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    if ! docker rm $NGINX_CONTAINER_NAME;
    then
      echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다."
      echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      exit 1

    else
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요."
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${NGINX_CONTAINER_NAME}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      $NGINX_SHELL_SCRIPT_DIRECTORY/nginxDockerContainerRun.sh;
    fi
  fi
}

checkApplicationContainerExistenceStatus() {
  echo "[$NOW] [INFO] Application 컨테이너 존재 여부 확인할게요."
  echo "[$NOW] [INFO] Application 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 존재 여부 확인할게요."
  echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  APPLICATION_DOCKER_BLUE_STATUS=$(docker ps -aqf "name=$APPLICATION_BLUE_CONTAINER_NAME")

  echo "[$NOW] [INFO] Green 환경 기준 컨테이너 존재 여부 확인할게요."
  echo "[$NOW] [INFO] Green 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  APPLICATION_DOCKER_GREEN_STATUS=$(docker ps -aqf "name=$APPLICATION_GREEN_CONTAINER_NAME")

  if [[ -z "$APPLICATION_DOCKER_BLUE_STATUS" ]] || [[ -z "$APPLICATION_DOCKER_GREEN_STATUS" ]];
  then
    if [[ -z "$APPLICATION_DOCKER_BLUE_STATUS" ]] && [[ -z "$APPLICATION_DOCKER_GREEN_STATUS" ]];
    then
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_CONTAINER_NAME}, ${APPLICATION_GREEN_CONTAINER_NAME} 컨테이너 모두 존재 하지 않아요."
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_CONTAINER_NAME}, ${APPLICATION_GREEN_CONTAINER_NAME} 컨테이너 모두 존재 하지 않아요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      for loopCount in {1..2}
      do
        if [ $loopCount -eq 1 ];
        then
          containerName=$APPLICATION_BLUE_CONTAINER_NAME
        else
          containerName=$APPLICATION_GREEN_CONTAINER_NAME
        fi

        echo "[$NOW] [INFO] ${containerName} 곧 컨테이너 기동 작업이 시작됩니다."
        echo "[$NOW] [INFO] ${containerName} 곧 컨테이너 기동 작업이 시작됩니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

        applicationDockerContainerRun "${containerName}"
      done

    elif [[ -z "$APPLICATION_DOCKER_BLUE_STATUS" ]] && [[ -n "$APPLICATION_DOCKER_GREEN_STATUS" ]];
    then
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      applicationDockerContainerRun "${APPLICATION_BLUE_CONTAINER_NAME}"

    else
      echo "[$NOW] [INFO] ${APPLICATION_GREEN_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
      echo "[$NOW] [INFO] ${APPLICATION_GREEN_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      applicationDockerContainerRun "${APPLICATION_GREEN_CONTAINER_NAME}"
    fi

  else
    echo "[$NOW] [INFO] ${APPLICATION_BLUE_CONTAINER_NAME}, ${APPLICATION_GREEN_CONTAINER_NAME} 컨테이너가 존재 합니다."
    echo "[$NOW] [INFO] ${APPLICATION_BLUE_CONTAINER_NAME}, ${APPLICATION_GREEN_CONTAINER_NAME} 컨테이너가 존재 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    $APPLICATION_SHELL_SCRIPT_DIRECTORY/applicationContainerNewRun.sh;

    $APPLICATION_SHELL_SCRIPT_DIRECTORY/applicationHealthCheck.sh;

    $NGINX_SHELL_SCRIPT_DIRECTORY/nginxBlueServiceSetting.sh;
    $NGINX_SHELL_SCRIPT_DIRECTORY/nginxGreenServiceSetting.sh;

  fi
}

applicationDockerContainerRun() {
  sleep 5
  local containerName=$1

  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  if [ "$containerName" == "$APPLICATION_BLUE_CONTAINER_NAME" ];
  then
    containerAndHostName="giggal-total-back-office-api-blue"
    portNumber=$APPLICATION_BLUE_EXTERNAL_PORT_NUMBER

  else
    containerAndHostName="giggal-total-back-office-api-green"
    portNumber=$APPLICATION_GREEN_EXTERNAL_PORT_NUMBER
  fi

  echo "[$NOW] [INFO] 설정된 변수 정보: "
  echo "[$NOW] [INFO] 설정된 변수 정보: " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${containerAndHostName} "
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${containerAndHostName} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "  >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${containerName} 컨테이너 기동 작업을 시작할게요."
  echo "[$NOW] [INFO] ${containerName} 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  dockerRunCommand="docker run -itd --privileged --name $containerAndHostName --hostname $containerAndHostName -e container=docker -p $portNumber:8080 --restart unless-stopped $APPLICATION_DOCKER_IMAGE_NAME"

  if ! docker run -itd --privileged --name $containerAndHostName --hostname $containerAndHostName -e container=docker -p $portNumber:8080 --restart unless-stopped $APPLICATION_DOCKER_IMAGE_NAME;
  then
    failedCommand "${dockerRunCommand}"
  else
    successCommand "${dockerRunCommand}"

    containerId=$(docker ps --filter "name=$containerAndHostName" --format "{{.ID}}")

    echo "[$NOW] [INFO] Container ID : ${containerId} "
    echo "[$NOW] [INFO] Container ID : ${containerId} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] 기동 시킨 Container ${containerAndHostName} (ID: ${containerId}) 동작 상태 확인할게요."
  echo "[$NOW] [INFO] 기동 시킨 Container ${containerAndHostName} (ID: ${containerId}) 동작 상태 확인할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  checkContainerStatus=$(docker ps --filter "id=$containerId" --format "{{.Status}}")

  sleep 5

  containerLogs=$(docker logs "$containerId")

  if [[ $checkContainerStatus == "Up"* ]];
  then
    echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : "
    echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] $containerLogs"
    echo "[$NOW] [INFO] $containerLogs" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  successCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "

  else
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : "
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [ERROR] $containerLogs"
    echo "[$NOW] [ERROR] $containerLogs" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    failedCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Docker Container 존재 여부 확인 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Docker Container 존재 여부 확인 작업이 끝났어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo -e "=====================================================================================================\n"
echo -e "=====================================================================================================\n" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1