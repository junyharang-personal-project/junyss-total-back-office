#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Docker Container 존재 여부 확인 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Application Docker Container 존재 여부 확인======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

APPLICATION_DOCKER_CONTAINER_BLUE_A_IMAGE_NAME="giggal-people/giggal-total-back-office-api-blue-a"
APPLICATION_DOCKER_CONTAINER_BLUE_B_IMAGE_NAME="giggal-people/giggal-total-back-office-api-blue-b"
APPLICATION_DOCKER_CONTAINER_GREEN_A_IMAGE_NAME="giggal-people/giggal-total-back-office-api-green-a"
APPLICATION_DOCKER_CONTAINER_GREEN_B_IMAGE_NAME="giggal-people/giggal-total-back-office-api-green-b"

APPLICATION_BLUE_A_CONTAINER_NAME="giggal-total-back-office-api-blue-a"
APPLICATION_BLUE_B_CONTAINER_NAME="giggal-total-back-office-api-blue-b"
APPLICATION_GREEN_A_CONTAINER_NAME="giggal-total-back-office-api-green-a"
APPLICATION_GREEN_B_CONTAINER_NAME="giggal-total-back-office-api-green-b"

APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER=1001
APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER=1002
APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER=1011
APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER=1012

APPLICATION_SHELL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/shell-script/application"
NGINX_SHEEL_SCRIPT_DIRECTORY="/data/deploy/giggal-total-back-office/deploy/prod/shell-script/nginx"
checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office/api"

  if [ -d "$LOG_DIR" ];
  then
    echo "[INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Docker Container 존재 여부 확인 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application Docker Container 존재 여부 확인======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
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
  echo "[$NOW] [INFO] Application 컨테이너 존재 여부 확인할게요."
  echo "[$NOW] [INFO] Application 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  for loopCount in {1..4}  # Application 기동할 컨테이너 개수가 총 4개이기 때문에 4번 반복하여 컨테이너 존재 여부 확인.
  do
    echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 존재 여부 확인할게요."
    echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    APPLICATION_DOCKER_BLUE_A_STATUS=$(docker ps -aqf "name=$APPLICATION_BLUE_A_CONTAINER_NAME")
    APPLICATION_DOCKER_BLUE_B_STATUS=$(docker ps -aqf "name=$APPLICATION_BLUE_B_CONTAINER_NAME")

    if [[ -z "$APPLICATION_DOCKER_BLUE_A_STATUS" ]] || [[ -z "$APPLICATION_DOCKER_BLUE_B_STATUS" ]];
    then
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      if [[ -z "$APPLICATION_DOCKER_BLUE_A_STATUS" ]];
      then
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationDockerContainerRun "${APPLICATION_BLUE_A_CONTAINER_NAME}"
      elif [[ -z "$APPLICATION_DOCKER_BLUE_B_STATUS" ]];
      then
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationDockerContainerRun "${APPLICATION_BLUE_B_CONTAINER_NAME}"

      else
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너 모두 존재 하지 않아요."
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너 모두 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationDockerContainerRun "${APPLICATION_BLUE_A_CONTAINER_NAME}"
        applicationDockerContainerRun "${APPLICATION_BLUE_B_CONTAINER_NAME}"
      fi

      if ! $NGINX_SHEEL_SCRIPT_DIRECTORY/checkNginxContainerBlueExistenceStatus.sh;
      then
        successCommand "../nginx/blue/checkNginxContainerExistenceStatus.sh"
      else
        failedCommand "../nginx/blue/checkNginxContainerExistenceStatus.sh"
      fi

    else
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너가 존재 합니다."
      echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME} 컨테이너가 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [INFO] Green 환경 기준 컨테이너 존재 여부 확인할게요."
      echo "[$NOW] [INFO] Green 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      APPLICATION_DOCKER_GREEN_A_STATUS=$(docker ps -aqf "name=$APPLICATION_GREEN_A_CONTAINER_NAME")
      APPLICATION_DOCKER_GREEN_B_STATUS=$(docker ps -aqf "name=$APPLICATION_GREEN_B_CONTAINER_NAME")

      if [[ -z "$APPLICATION_DOCKER_GREEN_A_STATUS" ]] || [[ -z "$APPLICATION_DOCKER_GREEN_B_STATUS" ]];
      then
        echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
        echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        if [[ -z "$APPLICATION_DOCKER_GREEN_A_STATUS" ]];
        then
          echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
          echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          applicationDockerContainerRun "${APPLICATION_GREEN_A_CONTAINER_NAME}"

        elif [[ -z "$APPLICATION_DOCKER_GREEN_B_STATUS" ]];
        then
          echo "[$NOW] [INFO] ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
          echo "[$NOW] [INFO] ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          applicationDockerContainerRun "${APPLICATION_GREEN_B_CONTAINER_NAME}"

        else
          echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요."
          echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          applicationDockerContainerRun "${APPLICATION_GREEN_A_CONTAINER_NAME}"
          applicationDockerContainerRun "${APPLICATION_GREEN_B_CONTAINER_NAME}"
        fi

        if ! $NGINX_SHEEL_SCRIPT_DIRECTORY/checkNginxContainerGreenExistenceStatus.sh;
        then
          successCommand "../nginx/green/checkNginxContainerGreenExistenceStatus.sh"
        else
          failedCommand "../nginx/green/checkNginxContainerGreenExistenceStatus.sh"
        fi

      else
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME}, ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 합니다."
        echo "[$NOW] [INFO] ${APPLICATION_BLUE_A_CONTAINER_NAME}, ${APPLICATION_BLUE_B_CONTAINER_NAME}, ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        if ! $APPLICATION_SHELL_SCRIPT_DIRECTORY/applicationHealthCheck.sh;
        then
          successCommand "./applicationHealthCheck.sh"
        else
          failedCommand "./applicationHealthCheck.sh"
        fi
      fi
    fi
  done
}

applicationDockerContainerRun() {
  sleep 5
  local containerName=$1

  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if [ "$containerName" == "$APPLICATION_BLUE_A_CONTAINER_NAME" ];
  then
    containerAndHostName="giggal-total-back-office-api-blue-a"
    portNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_DOCKER_CONTAINER_BLUE_A_IMAGE_NAME

  elif [ "$containerName" == "$APPLICATION_BLUE_B_CONTAINER_NAME" ];
  then
    containerAndHostName="giggal-total-back-office-api-blue-b"
    portNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_DOCKER_CONTAINER_BLUE_B_IMAGE_NAME

  elif [ "$containerName" == "$APPLICATION_GREEN_A_CONTAINER_NAME" ];
  then
    containerAndHostName="giggal-total-back-office-api-green-a"
    portNumber=$APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_DOCKER_CONTAINER_GREEN_A_IMAGE_NAME

  else
    containerAndHostName="giggal-total-back-office-api-green-b"
    portNumber=$APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_DOCKER_CONTAINER_GREEN_B_IMAGE_NAME

  fi

  echo "[$NOW] [INFO] 설정된 변수 정보: "
  echo "[$NOW] [INFO] 설정된 변수 정보: " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${containerAndHostName} "
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${containerAndHostName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "  >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${containerName} 컨테이너 기동 작업을 시작할게요."
  echo "[$NOW] [INFO] ${containerName} 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand="docker run -itd --privileged --name $containerAndHostName --hostname $containerAndHostName -e container=docker -p $portNumber:8080 --restart unless-stopped $dockerImageName"

  if ! docker run -itd --privileged --name $containerAndHostName --hostname $containerAndHostName -e container=docker -p $portNumber:8080 --restart unless-stopped $dockerImageName;
  then
    failedCommand "${dockerRunCommand}"
  else
    successCommand "${dockerRunCommand}"

    containerId=$(docker ps --filter "name=$containerAndHostName" --format "{{.ID}}")

    echo "[$NOW] [INFO] Container ID : ${containerId} "
    echo "[$NOW] [INFO] Container ID : ${containerId} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] 기동 시킨 Container ${containerAndHostName} (ID: ${containerId}) 동작 상태 확인할게요."
  echo "[$NOW] [INFO] 기동 시킨 Container ${containerAndHostName} (ID: ${containerId}) 동작 상태 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  checkContainerStatus=$(docker ps --filter "id=$containerId" --format "{{.Status}}")
  containerLogs=$(docker logs "$containerId")

  if [[ $checkContainerStatus == "Up"* ]];
  then
    echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : "
    echo "[$NOW] [INFO] 기동 시킨 Container 내부 Log 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] $containerLogs"
    echo "[$NOW] [INFO] $containerLogs" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  successCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "

  else
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : "
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [ERROR] $containerLogs"
    echo "[$NOW] [ERROR] $containerLogs" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    failedCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Application Docker Container 존재 여부 확인 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Application Docker Container 존재 여부 확인 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1