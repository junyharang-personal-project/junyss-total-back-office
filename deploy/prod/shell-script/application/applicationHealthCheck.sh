#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Application Health Check 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 api Application Health Check======================================"
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
      echo "[$NOW] [ERROR] Application Container 상태 확인 중 문제가 발생하였어요. Application 컨테이너 존재 여부를 다시 확인할게요."
      echo "[$NOW] [ERROR] Application Container 상태 확인 중 문제가 발생하였어요. Application 컨테이너 존재 여부를 다시 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      if ! ./applicationCheckContainerExistenceStatus.sh
      then
        successCommand "./checkNginxContainerExistenceStatus.sh"
      else
        failedCommand "./checkNginxContainerExistenceStatus.sh"
      fi
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

      responseCount=$(curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile | grep "HTTP" | wc -l)
      command="curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile | grep "HTTP" | wc -l"

      # up_count >= 1
      if [ ${responseCount} -ge 1 ];
      then
        echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요."
        echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        successCommand "${command}"
        break

      else
        errorResponse=$(curl -I http://${SERVER_IP}:${applicationExternalPortNumber}/api/test/profile)

        echo "[$NOW] [ERROR] ${containerName} Container 상태에 문제가 있어요."
        echo "[$NOW] [ERROR] ${containerName} Container 상태에 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        echo "[$NOW] [ERROR] 문제 내용 : ${errorResponse}"
        echo "[$NOW] [ERROR] 문제 내용 : ${errorResponse}" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        echo "[$NOW] [ERROR] ${containerName} Container 종료 및 삭제 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        applicationDockerContainerChangeOldErrorRemove "${containerName}"
      fi

      # retryCount == 10
      if [ "${retryCount}" -eq 10 ];
        then
          echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
          echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          echo "[$NOW] [ERROR] ${containerName} Container 종료 및 삭제 시도할게요."
          echo "[$NOW] [ERROR] ${containerName} Container 종료 및 삭제 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

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

      elif [[ $GREEN_CONTAINER_B_STATUS != "Up"* ]];
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

applicationDockerContainerChangeOldErrorRemove() {
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

  applicationContainerHealthCheck

  else
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : "
    echo "[$NOW] [ERROR] 문제 발생한 Container 내부 Log 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [ERROR] $containerLogs"
    echo "[$NOW] [ERROR] $containerLogs" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    failedCommand "docker ps --filter "id=$containerId" --format "{{.Status}}" "
  fi
}

unknownNameImageDelete() {
  sleep 5
  echo "[$NOW] [INFO] createDockerImageAndBackup.sh에서 제거하지 못한 이름 없는 (고아) Docker Image 삭제 작업 시작할게요."
  echo "[$NOW] [INFO] createDockerImageAndBackup.sh에서 제거하지 못한 이름 없는 (고아) Docker Image 삭제 작업 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  checkDockerImage

  if ! docker images | grep "^<none>";
  then
    echo "[$NOW] [INFO] 이름 없는 (고아) Docker Image가 존재 하지 않아요."
    echo "[$NOW] [INFO] 이름 없는 (고아) Docker Image가 존재 하지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    checkDockerImage

  else
    if ! docker rmi $(docker images -f "dangling=true" -q);
    then
      echo "[$NOW] [WARN] 이름 없는 Docker Image 삭제 작업 실패하였어요. Server에 접속하여 직접 삭제 작업이 필요해요. 스크립트는 종료되지 않습니다."
      echo "[$NOW] [WARN] 이름 없는 Docker Image 삭제 작업 실패하였어요. Server에 접속하여 직접 삭제 작업이 필요해요. 스크립트는 종료되지 않습니다." >> $LOG_DIR/"$NOW"-createImageAndBackup.log 2>&1

      checkDockerImage

    else
      echo "[$NOW] [INFO] 이름 없는 Docker Image 삭제 작업 성공하였어요."
      echo "[$NOW] [INFO] 이름 없는 Docker Image 삭제 작업 성공하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      checkDockerImage
    fi
  fi
}

checkDockerImage() {
  DOCKER_IMAGES_LIST=$(docker images)

  echo "[$NOW] [INFO] 현재 존재하는 Docker Image 내역 : "
  echo "[$NOW] [INFO] 현재 존재하는 Docker Image 내역 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] ${DOCKER_IMAGES_LIST} "
  echo "[$NOW] [INFO] ${DOCKER_IMAGES_LIST} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
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

chmod +x ./applicationCheckContainerExistenceStatus.sh

checkLogDirectory

operationDockerStatus=$(docker ps -a)

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Application Health Check 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : ${LOG_DIR}"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 작업 중 Application Health Check 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : "
echo "[$NOW] [INFO] 현재 운영 중인 Docker Container 정보 : " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "[$NOW] [INFO] ${operationDockerStatus} "
echo "[$NOW] [INFO] ${operationDockerStatus} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

./applicationContainerNewRun.sh