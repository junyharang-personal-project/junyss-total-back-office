#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 구동 확인 작업이 시작 되었어요."
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
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 구동 확인 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application 배포 서버 작업 중 Nginx Blue 구동 확인 작업======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
  fi

  nginxContainerStatusCheck
}

nginxContainerStatusCheck() {
  echo "[$NOW] [INFO] Nginx Blue Container 구동 확인 작업 시작할게요."
  echo "[$NOW] [INFO] Nginx Blue Container 구동 확인 작업 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  nginxStatus=$(docker ps --filter "name=$NGINX_CONTAINER_NAME" --format "{{.Status}}")

#  NGINX_STATUS=$(docker inspect -f '{{.State.Status}}' $NGINX_BLUE_CONTAINER_NAME | grep running)
  if [[ $nginxStatus != "Up"* ]];
  then
    echo "[$NOW] [INFO] Nginx Blue Container 정상 작동 중이지 않아요."
    echo "[$NOW] [INFO] Nginx Blue Container 정상 작동 중이지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    nginxDockerContainerChangeOldErrorRemove

  else
    echo "[$NOW] [INFO] Nginx Blue Container 정상 기동 중이에요."
    echo "[$NOW] [INFO] Nginx Blue Container 정상 기동 중이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] Nginx Blue Container 구동 상태 정상적으로 성공 되었어요."
  echo "[$NOW] [INFO] Nginx Blue Container 구동 상태 정상적으로 성공 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  $NGINX_SHELL_SCRIPT_DIRECTORY/blue/reSettingNginxBlueService.sh
}

nginxDockerContainerChangeOldErrorRemove() {
    stopContainerId=$(docker ps --filter "name=$NGINX_CONTAINER_NAME" --format "{{.ID}}")

    echo "[$NOW] [INFO] Nginx Blue 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : "
    echo "[$NOW] [INFO] Nginx Blue 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${NGINX_CONTAINER_NAME} "
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${NGINX_CONTAINER_NAME} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 Port Number : ${NGINX_EXTERNAL_PORT_NUMBER} "
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 Port Number : ${NGINX_EXTERNAL_PORT_NUMBER} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 Docker Image Name : ${NGINX_DOCKER_IMAGE_NAME} "
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 컨테이너 Docker Image Name : ${NGINX_DOCKER_IMAGE_NAME} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${NGINX_CONTAINER_NAME} "
    echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${NGINX_CONTAINER_NAME} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} "
    echo "[$NOW] [INFO] ${NGINX_CONTAINER_NAME} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${NGINX_CONTAINER_NAME} 컨테이너 종료 작업을 시작할게요."
  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${NGINX_CONTAINER_NAME} 컨테이너 종료 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  command="docker stop $stopContainerId"

  if ! docker stop $stopContainerId;
    then
      failedCommand "${command}"
    else
      successCommand "${command}"

    command="docker rm $stopContainerId"

    if ! docker rm $stopContainerId;
    then
      failedCommand "${command}"
    else
      successCommand "${command}"

      unknownNameImageDelete

      nginxDockerContainerRun
    fi
  fi
}

unknownNameImageDelete() {
  echo "[$NOW] [INFO] (고아) Docker Image 삭제 작업 시작할게요."
  echo "[$NOW] [INFO] (고아) Docker Image 삭제 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  checkDockerImage

  if ! docker images | grep "^<none>";
  then
    echo "[$NOW] [INFO] 이름 없는 (고아) Docker Image가 존재 하지 않아요."
    echo "[$NOW] [INFO] 이름 없는 (고아) Docker Image가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

    checkDockerImage

  else
    if ! docker rmi $(docker images -f "dangling=true" -q);
    then
      echo "[$NOW] [WARN] 이름 없는 Docker Image 삭제 작업 실패하였어요. Server에 접속하여 직접 삭제 작업이 필요해요. 스크립트는 종료되지 않습니다."
      echo "[$NOW] [WARN] 이름 없는 Docker Image 삭제 작업 실패하였어요. Server에 접속하여 직접 삭제 작업이 필요해요. 스크립트는 종료되지 않습니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

      checkDockerImage

    else
      echo "[$NOW] [INFO] 이름 없는 Docker Image 삭제 작업 성공하였어요."
      echo "[$NOW] [INFO] 이름 없는 Docker Image 삭제 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

      checkDockerImage
    fi
  fi
}

checkDockerImage() {
  DOCKER_IMAGES_LIST=$(docker images)

  echo "[$NOW] [INFO] 현재 존재하는 Docker Image 내역 : "
  echo "[$NOW] [INFO] 현재 존재하는 Docker Image 내역 : " >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
  echo "[$NOW] [INFO] ${DOCKER_IMAGES_LIST} "
  echo "[$NOW] [INFO] ${DOCKER_IMAGES_LIST} " >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
}

nginxDockerContainerRun() {
  echo "[$NOW] [INFO] nginx blue 기동 할게요."
  echo "[$NOW] [INFO] nginx blue 기동 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] NGINX blue 컨테이너 기동 작업을 시작할게요."
  echo "[$NOW] [INFO] NGINX blue 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

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

    nginxContainerStatusCheck
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 구동 확인 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Nginx Blue 구동 확인 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1