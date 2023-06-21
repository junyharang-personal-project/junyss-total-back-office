#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
#CREATE_DATE=$(date +"%Y-%m-%d")

#Docker Container Image Name
DOCKER_CONTAINER_IMAGE_NAME="giggal-people/giggal-total-back-office"

#Docker File 경로
DOCKER_FILE_PATH="/data/deploy/giggal-total-back-office/deploy/docker"

DOCKER_BACKUP_DIR="/data/deploy/giggal-total-back-office/backup"

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 도커 이미지 생성 및 도커 백업======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/docker/giggal-total-back-office/api"

  if [ -d "$LOG_DIR" ];
  then
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 도커 이미지 생성 및 도커 백업======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
  fi

  createdDockerImage
}

createdDockerImage() {
  sleep 5
  echo "[$NOW] [INFO] Docker Image 생성 작업 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if ! docker build -t $DOCKER_CONTAINER_IMAGE_NAME $DOCKER_FILE_PATH;
  then
    echo "[$NOW] [ERROR] Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    exit 1

  else

    if ! docker images | grep "$DOCKER_IMAGE_NAME";
    then
      echo "[$NOW] [ERROR] Docker Image 생성 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1

    else
      echo "[$NOW] [INFO] Docker Image 생성 작업 성공하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      dockerImageBackUp
    fi
  fi
}

dockerImageBackUp() {
  sleep 5
  echo "[$NOW] [INFO] Docker Image Back Up 작업 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  checkBackupDirectory
  cd $DOCKER_BACKUP_DIR

  if ! docker save -o giggal-total-back-office.tar "$DOCKER_IMAGE_NAME";
  then
    echo "[$NOW] [ERROR] Docker Image 백업 작업 실패하였어요. 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    exit 1

  else
    echo "[$NOW] [INFO] Docker Image 백업 작업 성공하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi
}

checkBackupDirectory() {
  sleep 5

  if [ -d "$DOCKER_BACKUP_DIR" ];
  then
    echo "[$NOW] [INFO] Docker Backup Directory 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${DOCKER_BACKUP_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
  fi
}

checkLogDirectory

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 LOG 위치 : /var/log/docker/giggal-total-back-office/api"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 도커 이미지 생성 및 도커 백업 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1