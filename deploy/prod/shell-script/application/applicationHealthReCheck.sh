#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Health 재 확인 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Application Health 재 확인======================================"
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

SERVER_IP=192.168.20.12

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office/api"

  if [ -d "$LOG_DIR" ];
  then
    echo "[INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Health Check 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 api Application Health Check======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] cicd-admin은 mkdir 명령어를 사용할 수 없어요. 관리자 혹은 DMSO 크루에게 ${LOG_DIR} 생성을 요청해 주세요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
  fi

  applicationContainerHealthCheck
}

applicationContainerHealthCheck() {
  sleep 5

  for loopCount in {1..4}
  do
    if [ "${loopCount}" == 1 ];
    then
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE A 기동 상태 확인 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE A 기동 상태 확인 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      containerName=$APPLICATION_BLUE_A_CONTAINER_NAME
      applicationExternalPortNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER

    elif [ "${loopCount}" == 2 ];
    then
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE B 기동 상태 확인 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE B 기동 상태 확인 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      containerName=$APPLICATION_BLUE_B_CONTAINER_NAME
      applicationExternalPortNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER
    elif [ "${loopCount}" == 3 ];
    then
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN A 기동 상태 확인 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN A 기동 상태 확인 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      containerName=$APPLICATION_GREEN_A_CONTAINER_NAME
      applicationExternalPortNumber=$APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER

    else
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN B 기동 상태 확인 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN B 기동 상태 확인 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      containerName=$APPLICATION_GREEN_B_CONTAINER_NAME
      applicationExternalPortNumber=$APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER
    fi

    echo "[$NOW] [INFO] ${containerName} Health Check를 다시 시작할게요."
    echo "[$NOW] [INFO] ${containerName} Health Check를 다시 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] curl -I http://${SERVER_IP}:${applicationExternalPortNumber} "
    echo "[$NOW] [INFO] curl -I http://${SERVER_IP}:${applicationExternalPortNumber} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    for retryCount in {1..10}
    do
      sleep 5
      echo "[$NOW] [INFO] ${loopCount} 번째 및 http 정상 연결 확인 ${retryCount} 번째 Health Check가 시작 되었어요."
      echo "[$NOW] [INFO] ${loopCount} 번째 및 http 정상 연결 확인 ${retryCount} 번째 Health Check가 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      responseCode=$(curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile | grep -oP 'HTTP/1.1 \K\d+')
      command="curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile | grep -oP 'HTTP/1.1 \K\d+'"

      if [ "$responseCode" == "200" ];
      then
        successCommand "${command}"

      else
        echo "[$NOW] [WARN] ${containerName}가 기동 중이지만, 내부 서비스에 문제가 있어요. 배포 작업 및 스크립트를 종료 합니다."
        echo "[$NOW] [WARN] ${containerName}가 기동 중이지만, 내부 서비스에 문제가 있어요. 배포 작업 및 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        exit 1
      fi

      responseCount=$(curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile | grep "HTTP" | wc -l)
      command="curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile | grep "HTTP" | wc -l"

      # up_count >= 1
      if [ ${responseCount} -ge 1 ];
      then
        echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요."
        echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        successCommand "${command}"

        if [ "${loopCount}" == 2 ];
        then
          ../nginx/blue/checkNginxContainerBlueExistenceStatus.sh
        elif [ "${loopCount}" == 4 ]; then
          ../nginx/green/checkNginxContainerGreenExistenceStatus.sh
        fi

        break

      else
        errorResponse=$(curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile)

        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        echo "[$NOW] [ERROR] 문제 내용 : ${errorResponse}"
        echo "[$NOW] [ERROR] 문제 내용 : ${errorResponse}" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        echo "[$NOW] [ERROR] 배포 작업 및 스크립트를 종료 합니다."
        echo "[$NOW] [ERROR] 배포 작업 및 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        exit 1
      fi

      # retryCount == 10
      if [ "${retryCount}" -eq 10 ];
        then
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요. 배포 작업 및 스크립트를 종료 합니다."
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요. 배포 작업 및 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        exit 1
      fi

      echo "[$NOW] [WARN] Health Check 작업에 실패하였어요."
      echo "[$NOW] [WARN] Health Check 작업에 실패하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [ERROR] Nginx 연결 없이 배포 작업 및 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] Nginx 연결 없이 배포 작업 및 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
    done
  done
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Application Health 재 확인 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Application Health 재 확인 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1