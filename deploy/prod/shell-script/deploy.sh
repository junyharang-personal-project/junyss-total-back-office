#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
#CREATE_DATE=$(date +"%Y-%m-%d")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Docker Container 기동 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 서버 배포======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

SERVER_IP=192.168.20.12

#Nginx File path
NGINX_DIR="/data/deploy/giggal-total-back-office/deploy/prod/nginx"
NGINX_CONFIG_DIR="/data/deploy/giggal-total-back-office/deploy/prod/nginx/docker/conf.d"

NGINX_CONTAINER_CONFIG_DIR="/etc/nginx"

DOCKER_DIR="/data/deploy/giggal-total-back-office/deploy/docker"

#Docker Container Image Name
DOCKER_CONTAINER_IMAGE_NAME="giggal-people/total-back-office"
#NGINX Container Name
DOCKER_CONTAINER_NGINX_NAME="giggal-total-back-office-nginx"

APPLICATION_BASE_DIR="/data/deploy/giggal-total-back-office"

#Application Docker Container Image Name
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

NGINX_DOCKER_BLUE_IMAGE_NAME="giggal-people/nginx-giggal-total-back-office-api-blue"
NGINX_DOCKER_GREEN_IMAGE_NAME="giggal-people/nginx-giggal-total-back-office-api-green"

NGINX_BLUE_CONTAINER_NAME="nginx-total-back-office-blue"
NGINX_GREEN_CONTAINER_NAME="nginx-total-back-office-green"

NGINX_BLUE_EXTERNAL_PORT_NUMBER=1000
NGINX_GREEN_EXTERNAL_PORT_NUMBER=1010

checkLogDirectory() {
  sleep 5

  LOG_DIR="/var/log/deploy/giggal-total-back-office/api"

  if [ -d "$LOG_DIR" ];
  then
    echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Docker Container 기동 작업이 시작 되었어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "======================================[$NOW] 통합 백 오피스 서버 배포======================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
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
  sleep 5

  echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 존재 여부 확인할게요."
  echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  for loopCount in {1..4}  # Application 기동할 컨테이너 개수가 총 4개이기 때문에 4번 반복하여 컨테이너 존재 여부 확인.
  do
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

      else
        echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 합니다."
        echo "[$NOW] [INFO] ${APPLICATION_GREEN_A_CONTAINER_NAME}, ${APPLICATION_GREEN_B_CONTAINER_NAME} 컨테이너가 존재 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      fi
    fi
  done

applicationContainerHealthCheck
}

applicationContainerHealthCheck() {
  sleep 5

  for loopCount in {1..4}
  do
    echo "[$NOW] [INFO] Application Container 기동 상태 확인할게요."
    echo "[$NOW] [INFO] Application Container 기동 상태 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    BLUE_CONTAINER_A_STATUS=$(docker ps --filter "name=$APPLICATION_BLUE_A_CONTAINER_NAME" --format "{{.Status}}")
    BLUE_CONTAINER_B_STATUS=$(docker ps --filter "name=$APPLICATION_BLUE_B_CONTAINER_NAME" --format "{{.Status}}")
    GREEN_CONTAINER_A_STATUS=$(docker ps --filter "name=$APPLICATION_GREEN_A_CONTAINER_NAME" --format "{{.Status}}")
    GREEN_CONTAINER_B_STATUS=$(docker ps --filter "name=$APPLICATION_GREEN_B_CONTAINER_NAME" --format "{{.Status}}")

    echo "[$NOW] [INFO] Application Container BLUE A 기동 상태 정보 : $BLUE_CONTAINER_A_STATUS"
    echo "[$NOW] [INFO] Application Container BLUE A 기동 상태 정보 : $BLUE_CONTAINER_A_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container BLUE B 기동 상태 정보 : $BLUE_CONTAINER_B_STATUS"
    echo "[$NOW] [INFO] Application Container BLUE B 기동 상태 정보 : $BLUE_CONTAINER_B_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container GREEN A 기동 상태 정보 : $GREEN_CONTAINER_A_STATUS"
    echo "[$NOW] [INFO] Application Container GREEN A 기동 상태 정보 : $GREEN_CONTAINER_A_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container GREEN B 기동 상태 정보 : $GREEN_CONTAINER_B_STATUS"
    echo "[$NOW] [INFO] Application Container GREEN B 기동 상태 정보 : $GREEN_CONTAINER_B_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    echo "[$NOW] [INFO] ${loopCount} 번째 Application Container 기동 상태 확인"
    echo "[$NOW] [INFO] ${loopCount} 번째 Application Container 기동 상태 확인" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  if [[ $BLUE_CONTAINER_A_STATUS == "Up"* ]] && [[ "$BLUE_CONTAINER_B_STATUS" == "Up"* ]] && [[ "$GREEN_CONTAINER_A_STATUS" == "Up"* ]] && [[ "$GREEN_CONTAINER_B_STATUS" == "Up"* ]];
  then
    echo "[$NOW] [INFO] Application Container BLUE A PORT 번호 : $APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container BLUE A PORT 번호 : $APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container BLUE B PORT 번호 : $APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container BLUE B PORT 번호 : $APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container GREEN A PORT 번호 : $APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container GREEN A PORT 번호 : $APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container GREEN B PORT 번호 : $APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container GREEN B PORT 번호 : $APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    if [[ $BLUE_CONTAINER_A_STATUS == "Up"* ]] && [ $loopCount == 1 ];
    then
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE A 기동 상태가 정상이므로 ${APPLICATION_GREEN_A_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE A 기동 상태가 정상이므로 ${APPLICATION_GREEN_A_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationExternalPortNumber=$APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER
      containerName=$APPLICATION_GREEN_A_CONTAINER_NAME
      containerColor="green"

    elif [[ "$BLUE_CONTAINER_B_STATUS" == "Up"* ]] && [ $loopCount == 2 ];
    then
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE B 기동 상태가 정상이므로 ${APPLICATION_GREEN_B_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container BLUE B 기동 상태가 정상이므로 ${APPLICATION_GREEN_B_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationExternalPortNumber=$APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER
      containerName=$APPLICATION_GREEN_B_CONTAINER_NAME
      containerColor="green"


    elif [[ "$GREEN_CONTAINER_A_STATUS" == "Up"* ]] && [ $loopCount == 3 ];
    then
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN A 기동 상태가 정상이므로 ${APPLICATION_BLUE_A_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN A 기동 상태가 정상이므로 ${APPLICATION_BLUE_A_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationExternalPortNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER
      containerName=$APPLICATION_BLUE_A_CONTAINER_NAME
      containerColor="blue"

    elif [[ "$GREEN_CONTAINER_B_STATUS" == "Up"* ]] && [ $loopCount == 4 ];
    then
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN B 기동 상태가 정상이므로 ${APPLICATION_BLUE_B_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요."
      echo "[$NOW] [INFO] ${loopCount} 번째 반복문을 통해 Application Container GREEN B 기동 상태가 정상이므로 ${APPLICATION_BLUE_B_CONTAINER_NAME} 에 새로 배포 작업을 실시 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationExternalPortNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER
      containerName=$APPLICATION_BLUE_B_CONTAINER_NAME
      containerColor="blue"

    else
      echo "[$NOW] [ERROR] Application Container 상태 확인 중 문제가 발생하였어요. Application 컨테이너 상태를 다시 확인할게요."
      echo "[$NOW] [ERROR] Application Container 상태 확인 중 문제가 발생하였어요. Application 컨테이너 상태를 다시 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      checkContainerExistenceStatus
    fi

    echo "[$NOW] [INFO] ${containerName} Health Check를 시작할게요."
    echo "[$NOW] [INFO] ${containerName} Health Check를 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
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
        echo "[$NOW] [WARN] ${containerName}가 기동 중이지만, 내부 서비스에 문제가 있어요."
        echo "[$NOW] [WARN] ${containerName}가 기동 중이지만, 내부 서비스에 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        echo "[$NOW] [WARN] ${containerName} 삭제 및 재 기동 실시합니다."
        echo "[$NOW] [WARN] ${containerName} 삭제 및 재 기동 실시합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationDockerContainerChangeOldErrorRemove "${containerName}"
      fi

      # up_count >= 1
      if [ ${responseCount} -ge 1 ];
      then
        echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요."
        echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationOldDockerContainerSwitchingRemove "${containerName}"

        checkNginxContainerExistenceStatus "${containerName}" "${containerColor}"

        #shutdownBeforeContainer
        break

      else
        echo "[$NOW] [ERROR] ${containerName} Container 상태에 문제가 있어요."
        echo "[$NOW] [ERROR] ${containerName} Container 상태에 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        echo "[$NOW] [ERROR] 문제 내용 : ${RESPONSE}"
        echo "[$NOW] [ERROR] 문제 내용 : ${RESPONSE}" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationDockerContainerChangeOldErrorRemove "${containerName}"
      fi

      # retryCount == 10
      if [ "${retryCount}" -eq 10 ];
        then
          echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
          echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
          echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          applicationDockerContainerChangeOldErrorRemove "${containerName}"
      fi

      echo "[$NOW] [WARN] Health Check 작업에 실패하였어요."
      echo "[$NOW] [WARN] Health Check 작업에 실패하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
    done

  else
    if [[ $BLUE_CONTAINER_A_STATUS != "Up"* ]] && [[ $BLUE_CONTAINER_B_STATUS != "Up"* ]] && [[ $GREEN_CONTAINER_A_STATUS != "Up"* ]] && [[ $GREEN_CONTAINER_B_STATUS != "Up"* ]];
    then

      echo "[$NOW] [ERROR] 모든 Container Health 상태 문제가 있어요."
      echo "[$NOW] [ERROR] 모든 Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      containerName=$APPLICATION_BLUE_A_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerChangeOldErrorRemove "${containerName}"

      containerName=$APPLICATION_BLUE_B_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerChangeOldErrorRemove "${containerName}"

      containerName=$APPLICATION_GREEN_A_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerChangeOldErrorRemove "${containerName}"

      containerName=$APPLICATION_GREEN_B_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerChangeOldErrorRemove "${containerName}"

    elif [[ $BLUE_CONTAINER_A_STATUS != "Up"* ]];
    then
      containerName=$APPLICATION_BLUE_A_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerChangeOldErrorRemove "${containerName}"

    elif [[ $BLUE_CONTAINER_B_STATUS != "Up"* ]];
    then
      containerName=$APPLICATION_BLUE_B_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerChangeOldErrorRemove "${containerName}"

      elif [[ $GREEN_CONTAINER_A_STATUS != "Up"* ]];
      then
        containerName=$APPLICATION_GREEN_A_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      applicationDockerContainerChangeOldErrorRemove "${containerName}"

      elif [[ $GREEN_CONTAINER_B_STATUS == "Up"* ]];
      then
        containerName=$APPLICATION_GREEN_B_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} 문제 있는 Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        applicationDockerContainerChangeOldErrorRemove "${containerName}"

      else
        echo "[$NOW] [ERROR] Application Container 상태 확인 중 문제가 발생하였어요. 스크립트가 종료됩니다."
        echo "[$NOW] [ERROR] Application Container 상태 확인 중 문제가 발생하였어요. 스크립트가 종료됩니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        exit 1
      fi
    fi
  done
}

applicationOldDockerContainerSwitchingRemove() {
  sleep 5
  local containerName=$1

    echo "[$NOW] [INFO] 배포 전 컨테이너가 모두 정상 작동 중 입니다. Switching 작업으로 새로운 컨테이너 기동 작업을 시작할게요."
    echo "[$NOW] [INFO] 배포 전 컨테이너가 모두 정상 작동 중 입니다. Switching 작업으로 새로운 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 Switching 대상 docker 기동 명령어 변수를 설정할게요."
    echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 Switching 대상 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Switching 대상 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    if [ "$containerName" == "$APPLICATION_BLUE_A_CONTAINER_NAME" ];
    then
      stopContainerAndHostName=$APPLICATION_GREEN_A_CONTAINER_NAME
      portNumber=$APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER
      dockerImageName=$APPLICATION_DOCKER_CONTAINER_GREEN_A_IMAGE_NAME
      stopContainerId=$(docker ps --filter "name=$stopContainerAndHostName" --format "{{.ID}}")

    elif [ "$containerName" == "$APPLICATION_BLUE_B_CONTAINER_NAME" ];
    then
      stopContainerAndHostName=$APPLICATION_GREEN_B_CONTAINER_NAME
      portNumber=$APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER
      dockerImageName=$APPLICATION_DOCKER_CONTAINER_GREEN_B_IMAGE_NAME
      stopContainerId=$(docker ps --filter "name=$stopContainerAndHostName" --format "{{.ID}}")

    elif [ "$containerName" == "$APPLICATION_GREEN_A_CONTAINER_NAME" ];
    then
      stopContainerAndHostName=$APPLICATION_BLUE_A_CONTAINER_NAME
      portNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER
      dockerImageName=$APPLICATION_DOCKER_CONTAINER_BLUE_A_IMAGE_NAME
      stopContainerId=$(docker ps --filter "name=$stopContainerAndHostName" --format "{{.ID}}")

    else
      stopContainerAndHostName=$APPLICATION_BLUE_B_CONTAINER_NAME
      portNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER
      dockerImageName=$APPLICATION_DOCKER_CONTAINER_BLUE_B_IMAGE_NAME
      stopContainerId=$(docker ps --filter "name=$stopContainerAndHostName" --format "{{.ID}}")
    fi

    echo "[$NOW] [INFO] 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : "
    echo "[$NOW] [INFO] 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerAndHostName} "
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerAndHostName}  : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Port Number : ${portNumber} "
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Port Number : ${portNumber} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Docker Image Name : ${dockerImageName} "
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 컨테이너 Docker Image Name : ${dockerImageName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerName} "
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} "
    echo "[$NOW] [INFO] ${stopContainerAndHostName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    if ! docker stop $stopContainerAndHostName;
    then
      echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다."
      echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1

    else
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요."
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 종료 작업 성공 하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 진행할게요."
      echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 진행할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      if ! docker rm $stopContainerAndHostName;
      then
        echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다."
        echo "[$NOW] [ERROR] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 실패 하였어요. 스크립트 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        exit 1

      else
        echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요."
        echo "[$NOW] [INFO] 기존 동작 중이던 Container 이름: ${stopContainerAndHostName}, Container ID: ${stopContainerId} 삭제 작업 성공 하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationDockerContainerRun "${stopContainerAndHostName}"
      fi
    fi
}

applicationDockerContainerChangeOldErrorRemove() {
  sleep 5
  local containerName=$1

  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if [ "$containerName" == "$APPLICATION_BLUE_A_CONTAINER_NAME" ];
  then
    variableName="giggal-total-back-office-api-blue-a"
    portNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_DOCKER_CONTAINER_BLUE_A_IMAGE_NAME
    stopContainerName=$APPLICATION_BLUE_A_CONTAINER_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerName" --format "{{.ID}}")

  elif [ "$containerName" == "$APPLICATION_BLUE_B_CONTAINER_NAME" ];
  then
    variableName="giggal-total-back-office-api-blue-b"
    portNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_DOCKER_CONTAINER_BLUE_B_IMAGE_NAME
    stopContainerName=$APPLICATION_BLUE_B_CONTAINER_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerName" --format "{{.ID}}")

  elif [ "$containerName" == "$APPLICATION_GREEN_A_CONTAINER_NAME" ];
  then
    variableName="giggal-total-back-office-api-green-a"
    portNumber=$APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_GREEN_A_CONTAINER_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerName" --format "{{.ID}}")

  else
    variableName="giggal-total-back-office-api-green-b"
    portNumber=$APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER
    dockerImageName=$APPLICATION_DOCKER_CONTAINER_GREEN_B_IMAGE_NAME
    stopContainerName=$APPLICATION_DOCKER_CONTAINER_GREEN_B_IMAGE_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerName" --format "{{.ID}}")
  fi

    echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : "
    echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${containerName} 컨테이너 이름 및 Host Name : ${variableName} "
    echo "[$NOW] [INFO] ${containerName} 컨테이너 이름 및 Host Name : ${variableName} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${containerName} 컨테이너 Port Number : ${portNumber} "
    echo "[$NOW] [INFO] ${containerName} 컨테이너 Port Number : ${portNumber} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${containerName} 컨테이너 Docker Image Name : ${dockerImageName} "
    echo "[$NOW] [INFO] ${containerName} 컨테이너 Docker Image Name : ${dockerImageName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${containerName} 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerName} "
    echo "[$NOW] [INFO] ${containerName} 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${containerName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} "
    echo "[$NOW] [INFO] ${containerName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 종료 작업을 시작할게요."
  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 종료 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand="docker stop ${stopContainerId}"

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

      applicationDockerContainerRun "${containerName}"
    fi
  fi
}

applicationDockerContainerRun() {
  sleep 5
  local containerName=$1

  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${containerName} 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

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

checkNginxContainerExistenceStatus() {
    local containerName=$1
    local nginxColor=$2

    echo "[$NOW] [INFO] ${containerName}의 Nginx ${nginxColor} 환경 기준 컨테이너 존재 여부 확인할게요."
    echo "[$NOW] [INFO] ${containerName}의 Nginx ${nginxColor} 환경 기준 컨테이너 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    if [ "$nginxColor" == "blue" ];
    then
      NGINX_DOCKER_STATUS=$(docker ps -aqf "name=$NGINX_BLUE_CONTAINER_NAME")
    else
      NGINX_DOCKER_STATUS=$(docker ps -aqf "name=$NGINX_GREEN_CONTAINER_NAME")
    fi

    APPLICATION_DOCKER_BLUE_B_STATUS=$(docker ps -aqf "name=$APPLICATION_BLUE_B_CONTAINER_NAME")

    if [[ -z "$NGINX_DOCKER_STATUS" ]];
    then
      echo "[$NOW] [INFO] ${containerName}의 Nginx ${nginxColor}가 존재하지 않아요."
      echo "[$NOW] [INFO] ${containerName}의 Nginx ${nginxColor}가 존재하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      nginxDockerContainerRun "$nginxColor"

    else
      echo "[$NOW] [INFO] ${containerName}의 Nginx ${nginxColor}가 존재합니다."
      echo "[$NOW] [INFO] ${containerName}의 Nginx ${nginxColor}가 존재합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    fi
}

nginxDockerContainerOldRemove() {
  sleep 5
  local containerName=$1

  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if [ "$containerName" == "$NGINX_BLUE_CONTAINER_NAME" ];
  then
    portNumber=$NGINX_BLUE_EXTERNAL_PORT_NUMBER
    dockerImageName=$NGINX_DOCKER_BLUE_IMAGE_NAME
    stopContainerNameAndHostName=$NGINX_BLUE_CONTAINER_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerNameAndHostName" --format "{{.ID}}")
    nginxColor="blue"

  else
    portNumber=$NGINX_GREEN_EXTERNAL_PORT_NUMBER
    dockerImageName=$NGINX_DOCKER_GREEN_IMAGE_NAME
    stopContainerNameAndHostName=$NGINX_GREEN_CONTAINER_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerNameAndHostName" --format "{{.ID}}")
    nginxColor="green"
  fi

    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerNameAndHostName} "
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerNameAndHostName} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Port Number : ${portNumber} "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Port Number : ${portNumber} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Docker Image Name : ${dockerImageName} "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Docker Image Name : ${dockerImageName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerNameAndHostName} "
    echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerNameAndHostName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${stopContainerNameAndHostName} 컨테이너 종료 작업을 시작할게요."
  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${stopContainerNameAndHostName} 컨테이너 종료 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand="docker stop ${stopContainerId}"

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

      nginxDockerContainerRun "${nginxColor}"
    fi
  fi
}

nginxHealthCheck() {
  sleep 5

  local containerName=$1
  local nginxColor=$2

nginxConfUpdateLine=14
  echo "[$NOW] [INFO] Application ${containerName}의 nginx ${nginxColor} Container 존재 여부 확인할게요."
  echo "[$NOW] [INFO] Application ${containerName}의 nginx ${nginxColor} Container 존재 여부 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if [ "$nginxColor" == "blue" ];
  then
    NGINX_STATUS=$(docker ps --filter "name=$NGINX_BLUE_CONTAINER_NAME" --format "{{.Status}}")
    NGINX_CONTAINER_NAME=$NGINX_BLUE_CONTAINER_NAME
  else
    NGINX_STATUS=$(docker ps --filter "name=$NGINX_GREEN_CONTAINER_NAME" --format "{{.Status}}")
    NGINX_CONTAINER_NAME=$NGINX_GREEN_CONTAINER_NAME
  fi

#  NGINX_STATUS=$(docker inspect -f '{{.State.Status}}' $NGINX_BLUE_CONTAINER_NAME | grep running)
  if [[ $NGINX_STATUS != "Up"* ]];
  then
    echo "[$NOW] [INFO] nginx ${nginxColor} Container가 정상 작동 중이지 않아요."
    echo "[$NOW] [INFO] nginx ${nginxColor} Container가 정상 작동 중이지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    nginxDockerContainerChangeOldErrorRemove "${NGINX_CONTAINER_NAME}"

  else
    echo "[$NOW] [INFO] nginx ${nginxColor} 정상 기동 중이에요."
    echo "[$NOW] [INFO] nginx ${nginxColor} 정상 기동 중이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] Health check가 정상적으로 성공 되었어요. nginx 설정값을 변경하여 서버가 다운타임 없이 배포 가능하도록 작업할게요."
  echo "[$NOW] [INFO] Health check가 정상적으로 성공 되었어요. nginx 설정값을 변경하여 서버가 다운타임 없이 배포 가능하도록 작업할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if [ "$containerName" == "$APPLICATION_BLUE_A_CONTAINER_NAME" ];
  then
    nginxConfUpdateLine=15
    applicationExternalPortNumber=1001
  elif [ "$containerName" == "$APPLICATION_BLUE_B_CONTAINER_NAME" ];
  then
    nginxConfUpdateLine=17
    applicationExternalPortNumber=1002
  elif [ "$containerName" == "$APPLICATION_GREEN_A_CONTAINER_NAME" ];
  then
    nginxConfUpdateLine=15
    applicationExternalPortNumber=1011
  else
    nginxConfUpdateLine=17
    applicationExternalPortNumber=1012
  fi

  command="sed -i "${nginxConfUpdateLine}s/.*/server ${SERVER_IP}:${applicationExternalPortNumber};\g" ${NGINX_CONFIG_DIR}/nginx.${containerColor}.conf"

  if ! sed -i "${nginxConfUpdateLine}s/.*/server ${SERVER_IP}:${applicationExternalPortNumber};\g" ${NGINX_CONFIG_DIR}/nginx.${containerColor}.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  command="cp ${NGINX_CONFIG_DIR}/nginx.${containerColor}.conf ${NGINX_CONFIG_DIR}/nginx.conf"

  if ! cp ${NGINX_CONFIG_DIR}/nginx.${containerColor}.conf ${NGINX_CONFIG_DIR}/nginx.conf;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요."
  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  command="docker exec ${DOCKER_CONTAINER_NGINX_NAME} nginx -s reload"

  if ! docker exec ${DOCKER_CONTAINER_NGINX_NAME} nginx -s reload;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi
}

nginxDockerContainerChangeOldErrorRemove() {
  sleep 5
  local containerName=$1

  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${containerName} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if [ "$containerName" == "$NGINX_BLUE_CONTAINER_NAME" ];
  then
    portNumber=$NGINX_BLUE_EXTERNAL_PORT_NUMBER
    dockerImageName=$NGINX_DOCKER_BLUE_IMAGE_NAME
    stopContainerNameAndHostName=$NGINX_BLUE_CONTAINER_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerNameAndHostName" --format "{{.ID}}")
    nginxColor="blue"

  else
    portNumber=$NGINX_GREEN_EXTERNAL_PORT_NUMBER
    dockerImageName=$NGINX_DOCKER_GREEN_IMAGE_NAME
    stopContainerNameAndHostName=$NGINX_GREEN_CONTAINER_NAME
    stopContainerId=$(docker ps --filter "name=$stopContainerNameAndHostName" --format "{{.ID}}")
    nginxColor="green"
  fi

    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 이름을 통해 docker 기동 명령어 변수 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerNameAndHostName} "
    echo "[$NOW] [INFO] 컨테이너 이름 및 Host Name : ${stopContainerNameAndHostName} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Port Number : ${portNumber} "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Port Number : ${portNumber} : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Docker Image Name : ${dockerImageName} "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 컨테이너 Docker Image Name : ${dockerImageName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerNameAndHostName} "
    echo "[$NOW] [INFO] 종료 및 제거할 기존 컨테이너 이름 : ${stopContainerNameAndHostName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} "
    echo "[$NOW] [INFO] ${stopContainerNameAndHostName} 종료 및 제거할 기존 컨테이너 ID : ${stopContainerId} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${stopContainerNameAndHostName} 컨테이너 종료 작업을 시작할게요."
  echo "[$NOW] [INFO] 기존에 동작하던 정상 구동 중이지 않은 ${stopContainerNameAndHostName} 컨테이너 종료 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand="docker stop ${stopContainerId}"

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

      nginxDockerContainerRun "${nginxColor}"
    fi
  fi
}

nginxDockerContainerRun() {
  sleep 5
  local nginxColor=$1

  echo "[$NOW] [INFO] nginx ${nginxColor} 기동 할게요."
  echo "[$NOW] [INFO] nginx ${nginxColor} 기동 할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  echo "[$NOW] [INFO] NGINX-${nginxColor} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요."
  echo "[$NOW] [INFO] NGINX-${nginxColor} 컨테이너 이름을 통해 docker 기동 명령어 변수를 설정할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1


  if [ "$nginxColor" == "blue" ];
  then
    variableName=$NGINX_BLUE_CONTAINER_NAME
    portNumber=$NGINX_BLUE_EXTERNAL_PORT_NUMBER
    dockerImageName=$NGINX_DOCKER_BLUE_IMAGE_NAME
    dockerContainerName=$NGINX_BLUE_CONTAINER_NAME

  else
    variableName=$NGINX_GREEN_CONTAINER_NAME
    portNumber=$NGINX_GREEN_EXTERNAL_PORT_NUMBER
    dockerImageName=$NGINX_DOCKER_GREEN_IMAGE_NAME
    dockerContainerName=$NGINX_GREEN_CONTAINER_NAME
  fi

  echo "[$NOW] [INFO] NGINX-${nginxColor} 컨테이너 기동 작업을 시작할게요."
  echo "[$NOW] [INFO] NGINX-${nginxColor} 컨테이너 기동 작업을 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand=$(docker run -itd --privileged \
                    --name $variableName \
                    --hostname $variableName \
                    -e container=docker \
                    -p $portNumber:80 \
                    --restart unless-stopped \
                    $dockerImageName)

#                    -v ${NGINX_CONFIG_DIR}:${NGINX_CONTAINER_CONFIG_DIR} \

  command="docker run -itd --privileged --name $variableName --hostname $variableName -e container=docker -p $portNumber:80 -v ${NGINX_CONFIG_DIR}:${NGINX_CONTAINER_CONFIG_DIR} --restart unless-stopped $dockerImageName"

  if [ -z "$dockerRunCommand" ];
  then
    failedCommand "${command}"
  else
    successCommand "${command}"

    checkNginxStatus "${dockerContainerName}"
  fi
}

# NGINX 기동 여부 확인 함수
checkNginxStatus() {
  sleep 5

  local nginxDockerContainerName=$1

  echo "[$NOW] [INFO] NGINX 기동 여부를 확인할게요."
  echo "[$NOW] [INFO] NGINX 기동 여부를 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
#  EXIST_NGINX=$(docker ps | grep ${DOCKER_CONTAINER_NGINX_NAME})
  EXIST_NGINX=$(docker ps --format '{{.Names}}' | grep "^${nginxDockerContainerName}\$")

  if [ -z "$EXIST_NGINX" ];
    then
      echo "[$NOW] [INFO] NGINX Container 기동 중이지 않아 기동 합니다."
      echo "[$NOW] [INFO] NGINX Container 기동 중이지 않아 기동 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      if [ "$dockerContainerName" == "$NGINX_BLUE_CONTAINER_NAME" ];
      then
        nginxColor="blue"
        nginxDockerContainerRun "${nginxColor}"
      else
        nginxColor="green"
        nginxDockerContainerRun "${nginxColor}"
      fi

    else
      echo "[$NOW] [INFO] ${dockerContainerName} NGINX Container 기동 중이에요."
      echo "[$NOW] [INFO] ${dockerContainerName} NGINX Container 기동 중이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      nginxHealthCheck "${dockerContainerName}" "${nginxColor}"
  fi
}

shutdownBeforeContainer() {
  sleep 5

  echo "[$NOW] [INFO] 이 전 컨테이너 종료 합니다."
  echo "[$NOW] [INFO] 이 전 컨테이너 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
#  EXIST_BLUE_A=$(docker-compose -p ${DOCKER_CONTAINER_IMAGE_NAME}-${BEFORE_COMPOSE_COLOR}-a -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"A".yml down)

   if ! docker-compose -p ${DOCKER_CONTAINER_IMAGE_NAME}-${BEFORE_COMPOSE_COLOR}-a -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"A".yml down;
    then
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패 하였어요."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패 하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패로 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
    else
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 성공 하였어요."
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 성공 하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi

#  EXIST_BLUE_B=$(docker-compose -p ${DOCKER_CONTAINER_IMAGE_NAME}-${BEFORE_COMPOSE_COLOR}-b -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"B".yml down)

   if ! docker-compose -p ${DOCKER_CONTAINER_IMAGE_NAME}-${BEFORE_COMPOSE_COLOR}-b -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"B".yml down;
    then
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패 하였어요."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패 하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패로 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      exit 1
    else
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 성공 하였어요."
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 성공 하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업을 진행 합니다."
  echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업을 진행 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if ! docker image prune -f --filter="dangling=true";
  then
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 실패하였어요. 배포 스크립트는 종료되지 않습니다."
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 실패하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  else
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 성공하였어요."
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 성공하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 종료 작업이 끝났어요."
  echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 종료 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  removeOldContainerImage "${DOCKER_CONTAINER_IMAGE_NAME}" "${BEFORE_COMPOSE_COLOR}"
}

unknownNameImageDelete() {
  sleep 5
  echo "[$NOW] [INFO] createDockerImageAndBackup.sh에서 제거하지 못한 이름 없는 (고아) Docker Image 삭제 작업 시작할게요."
  echo "[$NOW] [INFO] createDockerImageAndBackup.sh에서 제거하지 못한 이름 없는 (고아) Docker Image 삭제 작업 시작할게요." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : /var/log/deploy/giggal-total-back-office/api"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
}