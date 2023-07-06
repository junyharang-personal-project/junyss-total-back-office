#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
SAVE_LOG_DATE=$(date +"%y-%m-%d")

echo "====================================================================================================="
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 새로운 Application Docker Container 기동 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 서버 배포======================================"
echo "[$NOW] [INFO] Author(만든이): 주니(junyharang8592@gmail.com)"

APPLICATION_DOCKER_IMAGE_NAME="giggal-people/total-back-office-api"

APPLICATION_MAIN_CONTAINER_NAME="giggal-total-back-office-api-main"
APPLICATION_SUB_CONTAINER_NAME="giggal-total-back-office-api-sub"

APPLICATION_MAIN_EXTERNAL_PORT_NUMBER=1001
APPLICATION_SUB_EXTERNAL_PORT_NUMBER=1011

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office"

  if [ -d "$LOG_DIR" ];
  then
    echo "=====================================================================================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 새로운 Application Docker Container 기동 작업이 시작 되었어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 서버 배포======================================" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      exit 1
  fi

  applicationOldDockerContainerRemove
}

applicationOldDockerContainerRemove() {
    echo "[$NOW] [INFO] 기존 Docker Container 중지 및 삭제 작업을 시작할게요."
    echo "[$NOW] [INFO] 기존 Docker Container 중지 및 삭제 작업을 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

    for loopCount in {1..2}
    do
      if [ "$loopCount" == 1 ];
      then
        echo "[$NOW] [INFO] ${loopCount} 번째 반복문이 시작되었어요. Sub의 기존 Docker Container 포함한 중지 및 삭제 명령어에 필요한 변수를 설정할게요."
        echo "[$NOW] [INFO] ${loopCount} 번째 반복문이 시작되었어요. Sub의 기존 Docker Container 포함한 중지 및 삭제 명령어에 필요한 변수를 설정할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
        stopContainerAndHostName="giggal-total-back-office-api-main"
        portNumber=$APPLICATION_MAIN_EXTERNAL_PORT_NUMBER
        stopContainerId=$(docker ps --filter "name=$stopContainerAndHostName" --format "{{.ID}}")

      else
        sleep 10
        echo "[$NOW] [INFO] ${loopCount} 번째 반복문이 시작되었어요. Main의 기존 Docker Container 포함한 중지 및 삭제 명령어에 필요한 변수를 설정할게요."
        echo "[$NOW] [INFO] ${loopCount} 번째 반복문이 시작되었어요. Main의 기존 Docker Container 포함한 중지 및 삭제 명령어에 필요한 변수를 설정할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
        stopContainerAndHostName="giggal-total-back-office-api-sub"
        portNumber=$APPLICATION_SUB_EXTERNAL_PORT_NUMBER
        stopContainerId=$(docker ps --filter "name=$stopContainerAndHostName" --format "{{.ID}}")
      fi

      echo "[$NOW] [INFO] 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : "
      echo "[$NOW] [INFO] 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerAndHostName} "
      echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerAndHostName}  : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Port Number : ${portNumber} "
      echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Port Number : ${portNumber} : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Docker Image Name : ${APPLICATION_DOCKER_IMAGE_NAME} "
      echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Docker Image Name : ${APPLICATION_DOCKER_IMAGE_NAME} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerAndHostName} "
      echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerAndHostName} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
      echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 ID : ${stopContainerId} "
      echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 ID : ${stopContainerId} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

      if ! docker stop $stopContainerAndHostName;
      then
        echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다."
        echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
        exit 1

      else
        echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요."
        echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
        echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 진행할게요."
        echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 진행할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

        if ! docker rm $stopContainerAndHostName;
        then
          echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다."
          echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
          exit 1

        else
          echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요."
          echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

          applicationDockerContainerRun "${stopContainerAndHostName}"
        fi
      fi
    done
}

applicationDockerContainerRun() {
  local containerName=$1

  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${containerName} 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

  if [ "$containerName" == "$APPLICATION_MAIN_CONTAINER_NAME" ];
  then
    containerAndHostName=$APPLICATION_MAIN_CONTAINER_NAME
    portNumber=$APPLICATION_MAIN_EXTERNAL_PORT_NUMBER

  else
    containerAndHostName=$APPLICATION_SUB_CONTAINER_NAME
    portNumber=$APPLICATION_SUB_EXTERNAL_PORT_NUMBER
  fi

  echo "[$NOW] [INFO] 설정된 변수 정보: "
  echo "[$NOW] [INFO] 설정된 변수 정보: " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${containerAndHostName} "
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${containerAndHostName} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "  >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1

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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 새로운 Application Docker Container 기동 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO]기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 새로운 Application Docker Container 기동 작업이 끝났어요." >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1
echo -e "=====================================================================================================\n"
echo -e "=====================================================================================================\n" >> $LOG_DIR/"$SAVE_LOG_DATE"-deploy.log 2>&1