#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
#CREATE_DATE=$(date +"%Y-%m-%d")

#Application Docker Container Image Name
APPLICATION_DOCKER_CONTAINER_IMAGE_NAME="giggal-people/giggal-total-back-office-api"

#Nginx Docker Container Image Name
NGINX_DOCKER_CONTAINER_IMAGE_NAME="giggal-people/nginx-giggal-total-back-office-api"

#Application Docker File 경로
APPLICATION_DOCKER_FILE_PATH="/data/deploy/giggal-total-back-office/deploy/prod/docker"

#Nginx Docker File 경로
NGINX_DOCKER_FILE_PATH="/data/deploy/giggal-total-back-office/deploy/prod/nginx/docker/"

APPLICATION_DOCKER_BACKUP_DIR="/data/deploy/giggal-total-back-office/deploy/prod/backup/application"

NGINX_DOCKER_BACKUP_DIR="/data/deploy/giggal-total-back-office/deploy/prod/backup/nginx"

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 도커 이미지 생성 및 도커 백업======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/docker/giggal-total-back-office/api"

  if [ -d "$LOG_DIR" ];
  then
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 도커 이미지 생성 및 도커 백업======================================" >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다."
      exit 1
  fi

  unknownNameImageDelete
}

unknownNameImageDelete() {
  sleep 5
  echo "[$NOW] [INFO] 이름 없는 (고아) Docker Image 삭제 작업 시작할게요."
  echo "[$NOW] [INFO] 이름 없는 (고아) Docker Image 삭제 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

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

    else
      echo "[$NOW] [INFO] 이름 없는 Docker Image 삭제 작업 성공하였어요."
      echo "[$NOW] [INFO] 이름 없는 Docker Image 삭제 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

      checkDockerImage
    fi
  fi

  createdApplicationDockerImage
}

checkDockerImage() {
  DOCKER_IMAGES_LIST=$(docker images)

  echo "[$NOW] [INFO] 현재 존재하는 Docker Image 내역 : "
  echo "[$NOW] [INFO] 현재 존재하는 Docker Image 내역 : " >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
  echo "[$NOW] [INFO] ${DOCKER_IMAGES_LIST} "
  echo "[$NOW] [INFO] ${DOCKER_IMAGES_LIST} " >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
}

createdApplicationDockerImage() {
  sleep 5
  echo "[$NOW] [INFO] Docker Image 생성 작업 시작할게요."
  echo "[$NOW] [INFO] Docker Image 생성 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  if ! docker build -t $APPLICATION_DOCKER_CONTAINER_IMAGE_NAME $APPLICATION_DOCKER_FILE_PATH;
  then
    echo "[$NOW] [ERROR] Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다."
    echo "[$NOW] [ERROR] Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    exit 1

  else

    if ! docker images | grep "$APPLICATION_DOCKER_CONTAINER_IMAGE_NAME";
    then
      echo "[$NOW] [ERROR] Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
      exit 1

    else
      echo "[$NOW] [INFO] Docker Image 생성 작업 성공하였어요."
      echo "[$NOW] [INFO] Docker Image 생성 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

      applicationDockerImageBackUp
    fi
  fi
}

applicationDockerImageBackUp() {
  sleep 5
  echo "[$NOW] [INFO] Docker Image Back Up 작업 시작할게요."
  echo "[$NOW] [INFO] Docker Image Back Up 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  checkBackupDirectory "$APPLICATION_DOCKER_BACKUP_DIR"
  cd $APPLICATION_DOCKER_BACKUP_DIR

  if ! docker save -o "$NOW"-giggal-total-back-office.tar "$APPLICATION_DOCKER_CONTAINER_IMAGE_NAME";
  then
    echo "[$NOW] [ERROR] Docker Image 백업 작업 실패하였어요."
    echo "[$NOW] [ERROR] Docker Image 백업 작업 실패하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

    DIRECTORY_OWNER_CHECK=$(ls -al ./)

    echo "[$NOW] [ERROR] ${DIRECTORY_OWNER_CHECK} 경로의 소유자와 그룹이 cicd-admin이 아닌 경우 권한 문제로 실패 합니다. 관리자 혹은 DMSO에게 ${APPLICATION_DOCKER_BACKUP_DIR} 경로의 소유자와 그룹을 cicd-admin으로 변경 요청해 주세요. 스크립트를 종료 합니다."
    echo "[$NOW] [ERROR] ${DIRECTORY_OWNER_CHECK} 경로의 소유자와 그룹이 cicd-admin이 아닌 경우 권한 문제로 실패 합니다. 관리자 혹은 DMSO에게 ${APPLICATION_DOCKER_BACKUP_DIR} 경로의 소유자와 그룹을 cicd-admin으로 변경 요청해 주세요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    exit 1

  else
    echo "[$NOW] [INFO] Docker Image 백업 작업 성공하였어요."
    echo "[$NOW] [INFO] Docker Image 백업 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

    createdNginxDockerImage
  fi
}

createdNginxDockerImage() {
  sleep 5
  echo "[$NOW] [INFO] Nginx Docker Image 생성 작업 시작할게요."
  echo "[$NOW] [INFO] Nginx Docker Image 생성 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
  echo "[$NOW] [INFO] 최초 Green Nginx Docker Image 생성 작업 시작할게요."
  echo "[$NOW] [INFO] 최초 Green Nginx Docker Image 생성 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  if ! docker build -f $NGINX_DOCKER_FILE_PATH/green-nginx.Dockerfile -t $NGINX_DOCKER_CONTAINER_IMAGE_NAME-green $NGINX_DOCKER_FILE_PATH;
  then
    echo "[$NOW] [ERROR]  Nginx Green Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다."
    echo "[$NOW] [ERROR]  Nginx Green Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    exit 1

  else

    if ! docker images | grep $NGINX_DOCKER_CONTAINER_IMAGE_NAME-green;
    then
      echo "[$NOW] [ERROR] Nginx Green Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] Nginx Green Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
      exit 1

    else
      echo "[$NOW] [INFO] Nginx Green Docker Image 생성 작업 성공하였어요."
      echo "[$NOW] [INFO] Nginx Green Docker Image 생성 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    fi
  fi

  echo "[$NOW] [INFO] Blue Nginx Docker Image 생성 작업 시작할게요."
  echo "[$NOW] [INFO] Blue Nginx Docker Image 생성 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  if ! docker build -f $NGINX_DOCKER_FILE_PATH/blue-nginx.Dockerfile -t $NGINX_DOCKER_CONTAINER_IMAGE_NAME-blue $NGINX_DOCKER_FILE_PATH;
  then
    echo "[$NOW] [ERROR] Nginx Blue Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다."
    echo "[$NOW] [ERROR] Nginx Blue Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    exit 1

  else

    if ! docker images | grep $NGINX_DOCKER_CONTAINER_IMAGE_NAME-blue;
    then
      echo "[$NOW] [ERROR] Nginx Blue Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] Nginx Blue Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
      exit 1

    else
      echo "[$NOW] [INFO] Nginx Blue Docker Image 생성 작업 성공하였어요."
      echo "[$NOW] [INFO] Nginx Blue Docker Image 생성 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

      nginxDockerImageBackUp
    fi
  fi
}

nginxDockerImageBackUp() {
  sleep 5
  echo "[$NOW] [INFO] Nginx Docker Image Back Up 작업 시작할게요."
  echo "[$NOW] [INFO] Nginx Docker Image Back Up 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  checkBackupDirectory "$NGINX_DOCKER_BACKUP_DIR"
  cd $NGINX_DOCKER_BACKUP_DIR

  echo "[$NOW] [INFO] 최초 Green Nginx Docker Image Back Up 작업 시작할게요."
  echo "[$NOW] [INFO] 최초 Green Nginx Docker Image Back Up 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  if ! docker save -o "$NOW"-giggal-nginx-green-total-back-office.tar "$NGINX_DOCKER_CONTAINER_IMAGE_NAME"-green;
  then
    echo "[$NOW] [ERROR] Green Nginx Docker Image 백업 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    exit 1

  else
    echo "[$NOW] [INFO] Green Nginx Docker Image 백업 작업 성공하였어요."
    echo "[$NOW] [INFO] Green Nginx Docker Image 백업 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
  fi

  echo "[$NOW] [INFO] Blue Nginx Docker Image Back Up 작업 시작할게요."
  echo "[$NOW] [INFO] Blue Nginx Docker Image Back Up 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  if ! docker save -o "$NOW"-giggal-nginx-blue-total-back-office.tar "$NGINX_DOCKER_CONTAINER_IMAGE_NAME"-blue;
  then
    echo "[$NOW] [ERROR] Blue Nginx Docker Image 백업 작업 실패하였어요. 스크립트를 종료 합니다."
    echo "[$NOW] [ERROR] Blue Nginx Docker Image 백업 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
    exit 1

  else
    echo "[$NOW] [INFO] Blue Nginx Docker Image 백업 작업 성공하였어요."
    echo "[$NOW] [INFO] Blue Nginx Docker Image 백업 작업 성공하였어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
  fi
}

checkBackupDirectory() {
  sleep 5
  local backupDirectoryPath=$1

  echo "[$NOW] [INFO] Docker Backup Directory 가 존재하는지 확인해 볼게요."
  echo "[$NOW] [INFO] Docker Backup Directory 가 존재하는지 확인해 볼게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  if [ -d "$backupDirectoryPath" ];
  then
    echo "[$NOW] [INFO] Docker Backup Directory ${backupDirectoryPath} 가 존재 합니다."
    echo "[$NOW] [INFO] Docker Backup Directory ${backupDirectoryPath} 가 존재 합니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${backupDirectoryPath} 생성을 요청해 주세요. 스크립트가 종료됩니다."
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${backupDirectoryPath} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
      exit 1
  fi
}

checkLogDirectory

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 LOG 위치 : /var/log/docker/giggal-total-back-office/api"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 작업이 끝났어요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1