#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d_%H:%M:%S")
#CREATE_DATE=$(date +"%Y-%m-%d")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업 중 Docker Container 기동 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 서버 배포======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

SERVER_IP=192.168.20.12

#Nginx File path
NGINX_DIR="/data/deploy/giggal-total-back-office/deploy/nginx"
NGINX_CONFIG_DIR="/data/deploy/giggal-total-back-office/deploy/nginx/docker/conf.d"

NGINX_CONTAINER_CONFIG_DIR="/etc/nginx"

DOCKER_DIR="/data/deploy/giggal-total-back-office/deploy/docker"

#Docker Container Image Name
DOCKER_CONTAINER_IMAGE_NAME="giggal-people/total-back-office"
#NGINX Container Name
DOCKER_CONTAINER_NGINX_NAME="giggal-total-back-office-nginx"

APPLICATION_BASE_DIR="/data/deploy/giggal-total-back-office"

APPLICATION_DOCKER_IMAGE_NAME="giggal-people/giggal-total-back-office-api"

APPLICATION_CONTAINER_NAME="giggal-people/giggal-total-back-office"

APPLICATION_BLUE_A_CONTAINER_NAME="total-back-office-api-blue-a"
APPLICATION_BLUE_B_CONTAINER_NAME="total-back-office-api-blue-b"
APPLICATION_GREEN_A_CONTAINER_NAME="total-back-office-api-green-b"
APPLICATION_GREEN_B_CONTAINER_NAME="total-back-office-api-green-b"

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

  for loop_count in {1..4}  # Application 기동할 컨테이너 개수가 총 4개이기 때문에 4번 반복하여 컨테이너 존재 여부 확인.
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

  echo "[$NOW] [INFO] Application Container 기동 상태 확인할게요."
  echo "[$NOW] [INFO] Application Container 기동 상태 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  BLUE_CONTAINER_A_STATUS=$(docker inspect -f '{{.State.Status}}' $APPLICATION_BLUE_A_CONTAINER_NAME 2>/dev/null)
  BLUE_CONTAINER_B_STATUS=$(docker inspect -f '{{.State.Status}}' $APPLICATION_BLUE_B_CONTAINER_NAME 2>/dev/null)
  GREEN_CONTAINER_A_STATUS=$(docker inspect -f '{{.State.Status}}' $APPLICATION_GREEN_A_CONTAINER_NAME 2>/dev/null)
  GREEN_CONTAINER_B_STATUS=$(docker inspect -f '{{.State.Status}}' $APPLICATION_GREEN_B_CONTAINER_NAME 2>/dev/null)

  echo "[$NOW] [INFO] Application Container BLUE A 기동 상태 정보 : $BLUE_CONTAINER_A_STATUS"
  echo "[$NOW] [INFO] Application Container BLUE A 기동 상태 정보 : $BLUE_CONTAINER_A_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Application Container BLUE B 기동 상태 정보 : $BLUE_CONTAINER_B_STATUS"
  echo "[$NOW] [INFO] Application Container BLUE B 기동 상태 정보 : $BLUE_CONTAINER_B_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Application Container GREEN A 기동 상태 정보 : $GREEN_CONTAINER_A_STATUS"
  echo "[$NOW] [INFO] Application Container GREEN A 기동 상태 정보 : $GREEN_CONTAINER_A_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Application Container GREEN B 기동 상태 정보 : $GREEN_CONTAINER_B_STATUS"
  echo "[$NOW] [INFO] Application Container GREEN B 기동 상태 정보 : $GREEN_CONTAINER_B_STATUS" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  if [ "$BLUE_CONTAINER_A_STATUS" == "running" ] && [ "$BLUE_CONTAINER_B_STATUS" == "running" ] && [ "$GREEN_CONTAINER_A_STATUS" == "running" ] && [ "$GREEN_CONTAINER_B_STATUS" == "running" ];
  then
    echo "[$NOW] [INFO] Application Container BLUE A PORT 번호 : $APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container BLUE A PORT 번호 : $APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container BLUE B PORT 번호 : $APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container BLUE B PORT 번호 : $APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container GREEN A PORT 번호 : $APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container GREEN A PORT 번호 : $APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1
    echo "[$NOW] [INFO] Application Container GREEN B PORT 번호 : $APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER"
    echo "[$NOW] [INFO] Application Container GREEN B PORT 번호 : $APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    for loop_count in {1..4}
    do

      if [ $loop_count == 1 ];
      then
        applicationExternalPortNumber=$APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER
        containerName=$APPLICATION_GREEN_A_CONTAINER_NAME
        containerColor="green"
        nginxConfUpdateLine=12

      elif [ $loop_count == 2 ];
      then
        applicationExternalPortNumber=$APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER
        containerName=$APPLICATION_GREEN_B_CONTAINER_NAME
        containerColor="green"
        nginxConfUpdateLine=14

      elif [ $loop_count == 3 ];
      then
        applicationExternalPortNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER
        containerName=$APPLICATION_BLUE_A_CONTAINER_NAME
        containerColor="blue"
        nginxConfUpdateLine=12

      else
        applicationExternalPortNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER
        containerName=$APPLICATION_BLUE_B_CONTAINER_NAME
        containerColor="blue"
        nginxConfUpdateLine=14
      fi

      echo "[$NOW] [INFO] ${containerName} Health Check를 시작할게요."
      echo "[$NOW] [INFO] ${containerName} Health Check를 시작할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      echo "[$NOW] [INFO] curl -I http://${SERVER_IP}:${applicationExternalPortNumber} "
      echo "[$NOW] [INFO] curl -I http://${SERVER_IP}:${applicationExternalPortNumber} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      for RETRY_COUNT in {1..10}
      do
        RESPONSE=$(curl -I http://${SERVER_IP}:${applicationExternalPortNumber})
        UP_COUNT=$(echo "${RESPONSE}" | grep "HTTP" | wc -l)

        # up_count >= 1
        if [ ${UP_COUNT} -ge 1 ];
        then
          echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요."
          echo "[$NOW] [INFO] ${containerName} Container 상태가 정상이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          nginxHealthCheck "${containerColor}"

          #shutdownBeforeContainer
          break

        else
          echo "[$NOW] [ERROR] ${containerName} Container 상태에 문제가 있어요."
          echo "[$NOW] [ERROR] ${containerName} Container 상태에 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
          echo "[$NOW] [ERROR] 문제 내용 : ${RESPONSE}"
          echo "[$NOW] [ERROR] 문제 내용 : ${RESPONSE}" >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
          echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

          applicationDockerContainerRun "${containerName}"
        fi

        # RETRY_COUNT == 10
        if [ "${RETRY_COUNT}" -eq 10 ];
          then
            echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
            echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

            echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
            echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

            applicationDockerContainerRun "${containerName}"
        fi

        echo "[$NOW] [WARN] Health Check 작업에 실패하였어요."
        echo "[$NOW] [WARN] Health Check 작업에 실패하였어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다."
        echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        exit 1
      done
    done

  else
    if [ "$BLUE_CONTAINER_A_STATUS" != "running" ];
    then
      containerName=$APPLICATION_BLUE_A_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerRun "${containerName}"

    elif [ "$BLUE_CONTAINER_B_STATUS" != "running" ]; then
      containerName=$APPLICATION_BLUE_B_CONTAINER_NAME
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
      echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

      echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
      echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
      applicationDockerContainerRun "${containerName}"

      elif [ "$GREEN_CONTAINER_A_STATUS" != "running" ];
      then
        containerName=$APPLICATION_GREEN_A_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        applicationDockerContainerRun "${containerName}"

      elif [ "$GREEN_CONTAINER_B_STATUS" != "running" ];
      then
        containerName=$APPLICATION_GREEN_B_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요."
        echo "[$NOW] [ERROR] ${containerName} Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        applicationDockerContainerRun "${containerName}"

      else
        echo "[$NOW] [ERROR] 모든 Container Health 상태 문제가 있어요."
        echo "[$NOW] [ERROR] 모든 Container Health 상태 문제가 있어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

        containerName=$APPLICATION_BLUE_A_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        applicationDockerContainerRun "${containerName}"

        containerName=$APPLICATION_BLUE_B_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        applicationDockerContainerRun "${containerName}"

        containerName=$APPLICATION_GREEN_A_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
        applicationDockerContainerRun "${containerName}"

        containerName=$APPLICATION_GREEN_B_CONTAINER_NAME
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요."
        echo "[$NOW] [ERROR] ${containerName} Container 재 기동 시도할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
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
    variableName="giggal-total-back-office-api-blue-a"
    portNumber=$APPLICATION_BLUE_A_EXTERNAL_PORT_NUMBER

  elif [ "$containerName" == "$APPLICATION_BLUE_B_CONTAINER_NAME" ];
  then
    variableName="giggal-total-back-office-api-blue-b"
    portNumber=$APPLICATION_BLUE_B_EXTERNAL_PORT_NUMBER

  elif [ "$containerName" == "$APPLICATION_GREEN_A_CONTAINER_NAME" ];
  then
    variableName="giggal-total-back-office-api-green-a"
    portNumber=$APPLICATION_GREEN_A_EXTERNAL_PORT_NUMBER

  else
    variableName="giggal-total-back-office-api-green-b"
    portNumber=$APPLICATION_GREEN_B_EXTERNAL_PORT_NUMBER
  fi

  echo "[$NOW] [INFO] 설정된 변수 정보: "
  echo "[$NOW] [INFO] 설정된 변수 정보: " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${variableName} "
  echo "[$NOW] [INFO] Container Name And Container Host Name : ${variableName} " >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "
  echo "[$NOW] [INFO] Container Port Number : ${portNumber} "  >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  dockerRunCommand="docker run -itd --privileged --name $variableName --hostname $variableName -e container=docker -p $portNumber:8080 -v /sysfs/cgroup:/sys/fs/cgroup:ro -v /tmp/$(mktemp -d):/run --restart unless-stopped $APPLICATION_DOCKER_IMAGE_NAME /usr/sbin/init"

  if ! docker run -itd --privileged --name $variableName --hostname $variableName -e container=docker -p $portNumber:8080 -v /sysfs/cgroup:/sys/fs/cgroup:ro -v /tmp/$(mktemp -d):/run --restart unless-stopped $APPLICATION_DOCKER_IMAGE_NAME /usr/sbin/init;
  then
    failedCommand "${dockerRunCommand}"
  else
    successCommand "${dockerRunCommand}"
  fi
}

nginxHealthCheck() {
  sleep 5

  local nginxColor=$1

  echo "[$NOW] [INFO] nginx ${nginxColor} 기동 상태를 확인할게요."
  echo "[$NOW] [INFO] nginx ${nginxColor} 기동 상태를 확인할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  NGINX_STATUS=$(docker inspect -f '{{.State.Status}}' $NGINX_BLUE_CONTAINER_NAME | grep running)

  if [ "$NGINX_STATUS" != "running" ];
  then
    echo "[$NOW] [INFO] nginx ${nginxColor} 기동 중이지 않아요."
    echo "[$NOW] [INFO] nginx ${nginxColor} 기동 중이지 않아요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

    nginxDockerContainerRun "${nginxColor}"

  else
    echo "[$NOW] [INFO] nginx ${nginxColor} 정상 기동 중이에요."
    echo "[$NOW] [INFO] nginx ${nginxColor} 정상 기동 중이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] Health check가 정상적으로 성공 되었어요. nginx 설정값을 변경하여 서버가 다운타임 없이 배포 가능하도록 작업할게요."
  echo "[$NOW] [INFO] Health check가 정상적으로 성공 되었어요. nginx 설정값을 변경하여 서버가 다운타임 없이 배포 가능하도록 작업할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  command=$(sed -i "${nginxConfUpdateLine}s/.*/server ${SERVER_IP}:${applicationExternalPortNumber};\g" ${NGINX_CONFIG_DIR}/nginx.${containerColor}.conf)

  if ! $command;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  command=$(cp ${NGINX_CONFIG_DIR}/nginx.${containerColor}.conf ${NGINX_CONFIG_DIR}/nginx.conf)

  if ! $command;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
  fi

  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요."
  echo "[$NOW] [INFO] Application 중단 없이 변경 사항 Nginx 적용 작업 실시할게요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1

  command=$(docker exec ${DOCKER_CONTAINER_NGINX_NAME} nginx -s reload)

  if ! eval $command;
  then
    failedCommand "${command}"
  else
    successCommand "${command}"
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
                    -v ${NGINX_CONFIG_DIR}:${NGINX_CONTAINER_CONFIG_DIR} \
                    --restart unless-stopped \
                    $dockerImageName)

  if ! eval $dockerRunCommand;
  then
    failedCommand "${dockerRunCommand}"
  else
    successCommand "${dockerRunCommand}"

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
        nginxDockerContainerRun "blue"
      else
        nginxDockerContainerRun "green"
      fi

    else
      echo "[$NOW] [INFO] ${dockerContainerName} NGINX Container 기동 중이에요."
      echo "[$NOW] [INFO] ${dockerContainerName} NGINX Container 기동 중이에요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
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

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : /var/log/deploy/giggal-total-back-office/api"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 끝났어요." >> $LOG_DIR/"$NOW"-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/"$NOW"-deploy.log 2>&1