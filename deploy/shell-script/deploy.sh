#!/bin/bash

set -e

NOW=$(date +"%y-%m-%d %T")

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 시작 되었어요."
echo "======================================[$NOW] 통합 백 오피스 서버 배포======================================"
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)"

#Nginx File path
NGINX_DIR=/data/deploy/nginx

#Docker Container Name
DOCKER_CONTAINER_NAME=giggal-total-back-office-api
#NGINX Container Name
DOCKER_CONTAINER_NGINX_NAME=giggal-total-back-office-nginx

APPLICATION_BASE_DIR=/data/deploy/giggal-total-back-office

checkLogDirectory() {
  LOG_DIR="/var/log/deploy/giggal-total-back-office/api"

  if [ -d "$LOG_DIR" ];
  then
    echo "[$NOW] [INFO] LOG Directory 존재 합니다."
    echo "[$NOW] [INFO] LOG Directory 존재 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1

  else
      echo "[$NOW] [INFO] LOG Directory 존재 하지 않아 생성 합니다."
      echo "[$NOW] [INFO] LOG Directory 존재 하지 않아 생성 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1

      mkdir -p $LOG_DIR

      if [ $? != 0 ];
      then
        echo "[$NOW] [ERROR] LOG Directory 생성 작업 실패 하였어요."
        echo "[$NOW] [ERROR] LOG Directory 생성 작업 실패 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
        exit 1
      else
        echo "[$NOW] [INFO] LOG Directory 생성 작업 성공 하였어요."
        echo "[$NOW] [INFO] LOG Directory 생성 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1

        checkNginxStatus
      fi
  fi
}

# NGINX 기동 여부 확인 함수
checkNginxStatus() {
  echo "[$NOW] [INFO] NGINX 기동 여부를 확인할게요."
  echo "[$NOW] [INFO] NGINX 기동 여부를 확인할게요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  EXIST_NGINX=$(docker ps | grep ${DOCKER_CONTAINER_NGINX_NAME})

  if [ -z "${EXIST_NGINX}" ];
    then
      echo "[$NOW] [INFO] NGINX Container 기동 중이지 않아 기동 합니다."
      echo "[$NOW] [INFO] NGINX Container 기동 중이지 않아 기동 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      docker-compose -p giggal-total-back-office-nginx -f ${NGINX_DIR}/docker-compose.nginx.yml up -d
    else
      echo "[$NOW] [INFO] NGINX Container 기동 중이에요."
      echo "[$NOW] [INFO] NGINX Container 기동 중이에요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  fi

  showWorkLoading $!

  checkContainerStatus
}

checkContainerStatus() {
  echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 상태를 확인할게요."
  echo "[$NOW] [INFO] Blue 환경 기준 컨테이너 상태를 확인할게요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  EXIST_BLUE_A=$(docker-comapose -p ${DOCKER_CONTAINER_NAME}-bule-a -f ${DOCKER_DIR}/docker-compose.blueA.yml ps --status=running | grep ${DOCKER_CONTAINER_NAME}-blue-a)
  EXIST_BLUE_B=$(docker-comapose -p ${DOCKER_CONTAINER_NAME}-bule-a -f ${DOCKER_DIR}/docker-compose.blueB.yml ps --status=running | grep ${DOCKER_CONTAINER_NAME}-blue-b)

  echo "[$NOW] [INFO] 컨테이너 스위칭 작업 시작합니다."
  echo "[$NOW] [INFO] 컨테이너 스위칭 작업 시작합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
  if [ -z "$EXIST_BLUE_A" ] && [ -z "$EXIST_BLUE_B" ];
    then
      echo "[$NOW] [INFO] BLUE 컨테이너 기동 작업 시작합니다."
      echo "[$NOW] [INFO] BLUE 컨테이너 기동 작업 시작합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      echo "[$NOW] [INFO] BLUE 컨테이너 A 기동 작업 시작합니다."
      echo "[$NOW] [INFO] BLUE 컨테이너 A 기동 작업 시작합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      docker-compose -p ${DOCKER_CONTAINER_NAME}-blue-a -f ${DOCKER_DIR}/docker-compose.blueA.yml up -d

      if [ $? != 0 ];
       then
         echo "[$NOW] [ERROR] BLUE 컨테이너 A 기동 작업 실패 하였어요."
         echo "[$NOW] [ERROR] BLUE 컨테이너 A 기동 작업 실패 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
         echo "[$NOW] [ERROR] BLUE 컨테이너 A 기동 작업 실패로 스크립트를 종료 합니다."
         echo "[$NOW] [ERROR] BLUE 컨테이너 A 기동 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
         exit 1
       else
         echo "[$NOW] [INFO] BLUE 컨테이너 A 기동 작업 성공 하였어요."
         echo "[$NOW] [INFO] BLUE 컨테이너 A 기동 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
     fi

     echo "[$NOW] BLUE 컨테이너 B 기동 작업 시작합니다."
     echo "[$NOW] BLUE 컨테이너 B 기동 작업 시작합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
     docker-compose -p ${DOCKER_CONTAINER_NAME}-blue-b -f ${DOCKER_DIR}/docker-compose.blueB.yml up -d

     if [ $? != 0 ];
      then
        echo "[$NOW] [ERROR] BLUE 컨테이너 B 기동 작업 실패 하였어요."
        echo "[$NOW] [ERROR] BLUE 컨테이너 B 기동 작업 실패 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
        echo "[$NOW] [ERROR] BLUE 컨테이너 B 기동 작업 실패로 스크립트를 종료 합니다."
        echo "[$NOW] [ERROR] BLUE 컨테이너 B 기동 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
        exit 1
      else
        echo "[$NOW] [INFO] BLUE 컨테이너 B 기동 작업 성공 하였어요."
        echo "[$NOW] [INFO] BLUE 컨테이너 B 기동 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
    fi

    IDLE_PORT=8080
    BEFORE_COMPOSE_COLOR="green"
    AFTER_COMPOSE_COLOR="blue"

  else
    echo "[$NOW] [INFO] GREEN 컨테이너 기동 작업 시작합니다."
    echo "[$NOW] [INFO] GREEN 컨테이너 기동 작업 시작합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
    echo "[$NOW] [INFO] GREEN 컨테이너 A 기동 작업 시작합니다."
    echo "[$NOW] [INFO] GREEN 컨테이너 A 기동 작업 시작합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
    docker-compose -p ${DOCKER_CONTAINER_NAME}-green-a -f ${DOCKER_DIR}/docker-compose.greenA.yml up -d

    if [ $? != 0 ];
     then
       echo "[$NOW] [ERROR] GREEN 컨테이너 A 기동 작업 실패 하였어요."
       echo "[$NOW] [ERROR] GREEN 컨테이너 A 기동 작업 실패 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
       echo "[$NOW] [ERROR] GREEN 컨테이너 A 기동 작업 실패로 스크립트를 종료 합니다."
       echo "[$NOW] [ERROR] GREEN 컨테이너 A 기동 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
       exit 1
     else
       echo "[$NOW] [INFO] GREEN 컨테이너 A 기동 작업 성공 하였어요."
       echo "[$NOW] [INFO] GREEN 컨테이너 A 기동 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
   fi

   echo "[$NOW] [INFO] GREEN 컨테이너 B 기동 작업 시작합니다."
   docker-compose -p ${DOCKER_CONTAINER_NAME}-green-b -f ${DOCKER_DIR}/docker-compose.greenB.yml up -d

   if [ $? != 0 ];
    then
      echo "[$NOW] [ERROR] GREEN 컨테이너 B 기동 작업 실패 하였어요."
      echo "[$NOW] [ERROR] GREEN 컨테이너 B 기동 작업 실패 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
      echo "[$NOW] [ERROR] GREEN 컨테이너 B 기동 작업 실패로 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] GREEN 컨테이너 B 기동 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      exit 1
    else
      echo "[$NOW] [INFO] GREEN 컨테이너 B 기동 작업 성공 하였어요."
      echo "[$NOW] [INFO] GREEN 컨테이너 B 기동 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  fi

  IDLE_PORT=8090
  BEFORE_COMPOSE_COLOR="blue"
  AFTER_COMPOSE_COLOR="green"
fi

showWorkLoading $!

applicationContainerHealthCheck
}

applicationContainerHealthCheck() {
  checkServerEnvironment
  echo "[$NOW] [INFO] Application Container 기동 상태 확인할게요."
  echo "[$NOW] [INFO] Application Container 기동 상태 확인할게요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  CONTAINER_A_STATUS=$(docker-compose -p ${DOCKER_CONTAINER_NAME}-${AFTER_COMPOSE_COLOR}-a -f ${DOCKER_DIR}/docker-compose.${AFTER_COMPOSE_COLOR}"A".yml ps --status=running | grep ${DOCKER_CONTAINER_NAME}-${AFTER_COMPOSE_COLOR}-a)
  CONTAINER_B_STATUS=$(docker-compose -p ${DOCKER_CONTAINER_NAME}-${AFTER_COMPOSE_COLOR}-b -f ${DOCKER_DIR}/docker-compose.${AFTER_COMPOSE_COLOR}"B".yml ps --status=running | grep ${DOCKER_CONTAINER_NAME}-${AFTER_COMPOSE_COLOR}-b)

  if [ -n "$CONTAINER_A_STATUS" ] && [ -n "$CONTAINER_B_STATUS" ];
  then
    echo "[$NOW] [INFO] Application Container IDLE PORT 번호 : $IDLE_PORT"
    echo "[$NOW] [INFO] Application Container IDLE PORT 번호 : $IDLE_PORT" >> $LOG_DIR/$NOW-deploy.log 2>&1
    echo "[$NOW] [INFO] curl -s http://$APPLICATION_SERVER_IP:$IDLE_PORT "
    echo "[$NOW] [INFO] curl -s http://$APPLICATION_SERVER_IP:$IDLE_PORT " >> $LOG_DIR/$NOW-deploy.log 2>&1

    showWorkLoading $!

    for RETRY_COUNT in {1..10}
    do
      RESPONSE=$(curl -s http://${APPLICATION_SERVER_IP}:${IDLE_PORT})
      UP_COUNT=$(echo ${RESPONSE} | grep "timestamp" | wc -l)

      # up_count >= 1
      if [ ${UP_COUNT} -ge 1 ];
      then
        echo "[$NOW] [INFO] Application Container 상태가 정상이에요."
        #Health check가 정상적으로 성공 된 후 nginx 설정값을 변경해주어야 서버가 다운타임 없이 배포 진행.
        # nginx.config를 컨테이너에 맞게 변경해주고 reload 진행.
        cp ${NGINX_CONFIG_DIR}/nginx.{AFTER_COMPOSE_COLOR}.conf ${NGINX_CONFIG_DIR}/nginx.conf

        echo "[$NOW] [INFO] Application 중단 없이 변경 사항 적용 작업 실시할게요."
        echo "[$NOW] [INFO] Application 중단 없이 변경 사항 적용 작업 실시할게요." >> $LOG_DIR/$NOW-deploy.log 2>&1
        docker exec ${DOCKER_CONTAINER_NGINX_NAME} nginx -s reload

        showWorkLoading $!

        shutdownBeforeContainer
        break

      else
        echo "[$NOW] [ERROR] Application Container 상태에 문제가 있어요."
        echo "[$NOW] [ERROR] Application Container 상태에 문제가 있어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
        echo "[$NOW] [ERROR] 문제 내용 : ${RESPONSE}"
        echo "[$NOW] [ERROR] 문제 내용 : ${RESPONSE}" >> $LOG_DIR/$NOW-deploy.log 2>&1
      fi

      # RETRY_COUNT == 10
      if [ ${RETRY_COUNT} -eq 10 ];
        then
          echo "[$NOW] [ERROR] Application Health 상태 문제가 있어요."
          echo "[$NOW] [ERROR] Application Health 상태 문제가 있어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
          echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다."
          echo "[$NOW] [ERROR] Nginx 연결 없이 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
          exit 1
      fi

      echo "[$NOW] [WARN] Health Check 작업에 실패하였어요."
      echo "[$NOW] [WARN] Health Check 작업에 실패하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
      showRetryLoading
    done

  else
    echo "[$NOW] [ERROR] ${AFTER_COMPOSE_COLOR} 컨테이너 기동 작업에 문제가 있어 작업을 실패하였어요."
    echo "[$NOW] [ERROR] ${AFTER_COMPOSE_COLOR} 컨테이너 기동 작업에 문제가 있어 작업을 실패하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
    echo "[$NOW] [ERROR] ${AFTER_COMPOSE_COLOR} 컨테이너 기동 작업에 문제가 있어 작업 실패로 스크립트 종료 합니다."
    echo "[$NOW] [ERROR] ${AFTER_COMPOSE_COLOR} 컨테이너 기동 작업에 문제가 있어 작업 실패로 스크립트 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
    exit 1
  fi
}

# 서버 환경 확인 함수
checkServerEnvironment() {
  echo "[$NOW] [INFO] Application 구동 환경 정보를 확인할게요."
  echo "[$NOW] [INFO] Application 구동 환경 정보를 확인할게요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  DEVELOPE_ENVIRONMENT_STRING=dev
  OPERATION_ENVIRONMENT_STRING=op
  APPLICATION_SERVER_HOSTNAME=$(hostname -f)

  if [[ "$APPLICATION_SERVER_HOSTNAME" == *"$DEVELOPE_ENVIRONMENT_STRING"* ]];
  then
    echo "[$NOW] [INFO] Application 구동 환경은 개발 환경입니다."
    echo "[$NOW] [INFO] Application 구동 환경은 개발 환경입니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
    APPLICATION_SERVER_ENVIRONMENT="dev"
    APPLICATION_SERVER_IP="192.168.20.6"
    # Docker-compose File path
    DOCKER_DIR=/data/deploy/giggal-total-back-office/docker/dev
    #Nginx.conf File Path
    NGINX_CONFIG_DIR=/data/deploy/giggal-total-back-office/volume-mapping/nginx/conf.d/dev

    irrelevantEnvironmentDirectoryClear $DEVELOPE_ENVIRONMENT_STRING

  else
    echo "[$NOW] [INFO] Application 구동 환경은 운영 환경입니다."
    echo "[$NOW] [INFO] Application 구동 환경은 개발 환경입니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
    APPLICATION_SERVER_ENVIRONMENT="prod"
    APPLICATION_SERVER_IP="192.168.20.12"
    # Docker-compose File path
    DOCKER_DIR=/data/deploy/giggal-total-back-office/docker/prod
    #Nginx.conf File Path
    NGINX_CONFIG_DIR=/data/deploy/giggal-total-back-office/volume-mapping/nginx/conf.d/prod

    irrelevantEnvironmentDirectoryClear $OPERATION_ENVIRONMENT_STRING
  fi
}

irrelevantEnvironmentDirectoryClear() {
  echo "[$NOW] [INFO] Application 구동 환경과 관계 없는 Directory 삭제 작업을 진행 합니다."
  echo "[$NOW] [INFO] Application 구동 환경과 관계 없는 Directory 삭제 작업을 진행 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
  irrelevantEnvironment=$1

  if [[ "$irrelevantEnvironment" ==  *"$DEVELOPE_ENVIRONMENT_STRING"* ]];
  then
    echo "[$NOW] [INFO] 운영 환경과 관련된 Directory 삭제 작업을 진행 합니다."
    echo "[$NOW] [INFO] 운영 환경과 관련된 Directory 삭제 작업을 진행 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
    REMOVE_PROD_DOCKER_DIR=$(rm -rf ${APPLICATION_BASE_DIR}/docker/prod)
    REMOVE_PROD_NGINX_DIR=$(rm -rf ${APPLICATION_BASE_DIR}/volume-mapping/nginx/conf.d/prod)

    if [ -z "$REMOVE_PROD_DOCKER_DIR" ] && [ -z "$REMOVE_PROD_NGINX_DIR" ];
    then
      echo "[$NOW] [INFO] 운영 환경과 관련된 Directory 삭제 작업 성공 하였어요."
      echo "[$NOW] [INFO] 운영 환경과 관련된 Directory 삭제 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1

    else
      echo "[$NOW] [INFO] 운영 환경과 관련된 Directory 삭제 작업 실패 하였어요. 배포 스크립트를 종료 합니다."
      echo "[$NOW] [INFO] 운영 환경과 관련된 Directory 삭제 작업 실패 하였어요. 배포 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      exit 1
    fi

  else
    echo "[$NOW] [INFO] 개발 환경과 관련된 Directory 삭제 작업을 진행 합니다."
    echo "[$NOW] [INFO] 개발 환경과 관련된 Directory 삭제 작업을 진행 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
    REMOVE_DEV_DOCKER_DIR=$(rm -rf ${APPLICATION_BASE_DIR}/docker/dev)
    REMOVE_DEV_NGINX_DIR=$(rm -rf ${APPLICATION_BASE_DIR}/volume-mapping/nginx/conf.d/dev)

    if [ -z "$REMOVE_DEV_DOCKER_DIR" ] && [ -z "$REMOVE_DEV_NGINX_DIR" ];
    then
      echo "[$NOW] [INFO] 개발 환경과 관련된 Directory 삭제 작업 성공 하였어요."
      echo "[$NOW] [INFO] 개발 환경과 관련된 Directory 삭제 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1

    else
      echo "[$NOW] [ERROR] 개발 환경과 관련된 Directory 삭제 작업 실패 하였어요. 배포 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] 개 환경과 관련된 Directory 삭제 작업 실패 하였어요. 배포 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      exit 1
    fi
  fi

  echo "[$NOW] [INFO] Application 구동 환경과 관계 없는 Directory 삭제 작업 완료 하였어요."
  echo "[$NOW] [INFO] Application 구동 환경과 관계 없는 Directory 삭제 작업 완료 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
}

shutdownBeforeContainer() {
  echo "[$NOW] [INFO] 이 전 컨테이너 종료 합니다."
  echo "[$NOW] [INFO] 이 전 컨테이너 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
#  EXIST_BLUE_A=$(docker-comapose -p ${DOCKER_CONTAINER_NAME}-${BEFORE_COMPOSE_COLOR}-a -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"A".yml down)
  docker-comapose -p ${DOCKER_CONTAINER_NAME}-${BEFORE_COMPOSE_COLOR}-a -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"A".yml down

   if [ $? != 0 ];
    then
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패 하였어요."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패로 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      exit 1
    else
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 성공 하였어요."
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 A 종료 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  fi

#  EXIST_BLUE_B=$(docker-comapose -p ${DOCKER_CONTAINER_NAME}-${BEFORE_COMPOSE_COLOR}-b -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"B".yml down)
  docker-comapose -p ${DOCKER_CONTAINER_NAME}-${BEFORE_COMPOSE_COLOR}-b -f ${DOCKER_DIR}/docker-compose.${BEFORE_COMPOSE_COLOR}"B".yml down

   if [ $? != 0 ];
    then
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패 하였어요."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패로 스크립트를 종료 합니다."
      echo "[$NOW] [ERROR] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 실패로 스크립트를 종료 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1
      exit 1
    else
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 성공 하였어요."
      echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 B 종료 작업 성공 하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업을 진행 합니다."
  echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업을 진행 합니다." >> $LOG_DIR/$NOW-deploy.log 2>&1

  docker image prune -f --filter="dangling=true"

  if [ $? != 0 ];
  then
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 실패하였어요. 배포 스크립트는 종료되지 않습니다."
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 실패하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1

  else
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 성공하였어요."
    echo "[$NOW] [INFO] Docker Image 중 TAG가 사라진 불필요한 Docker Image 삭제 작업 성공하였어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
  fi

  echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 종료 작업이 끝났어요."
  echo "[$NOW] [INFO] ${BEFORE_COMPOSE_COLOR} 컨테이너 종료 작업이 끝났어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
}


# 작업 Loading 처리 함수
showWorkLoading() {
  local pid=$1
  local delay=5
  local frames=("▁" "▂" "▃" "▄" "▅" "▆" "▇" "█" "▇" "▆" "▅" "▄" "▃" "▂")

  while kill -0 $pid 2 > /dev/null; do
    for frame in "${frames[@]}"; do
      printf "/r[%s] 작업을 처리 중 입니다. 잠시만 기다려 주세요.  " "$frame"
      sleep $delay
    done
  done

  # 작업 완료 시 체크 마크 출력
  printf "\r[✔]      \n"
}

# 재 시도 Loading 처리 함수
showRetryLoading() {
  local pid=$1
  local delay=7
  local frames=("▁" "▂" "▃" "▄" "▅" "▆" "▇" "█" "▇" "▆" "▅" "▄" "▃" "▂")

  while kill -0 $pid 2 > /dev/null; do
    for frame in "${frames[@]}"; do
      printf "/r[%s] 연결 작업을 재 시도 중 입니다. 잠시만 기다려 주세요.  " "$frame"
      sleep $delay
    done
  done

  # 작업 완료 시 체크 마크 출력
  printf "\r[✔]      \n"
}

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 시작 되었어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
echo "======================================[$NOW] 통합 백 오피스 서버 배포======================================" >> $LOG_DIR/$NOW-deploy.log 2>&1
echo "[$NOW] [INFO] @Author(만든이): 주니(junyharang8592@gmail.com)" >> $LOG_DIR/$NOW-deploy.log 2>&1

checkLogDirectory

echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 끝났어요."
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 LOG 위치 : /var/log/deploy/giggal-total-back-office/api"
echo "[$NOW] [INFO] 기깔나는 사람들 통합 관리 서버 API 무중단 배포 서버 작업이 끝났어요." >> $LOG_DIR/$NOW-deploy.log 2>&1
echo "====================================================================================================="
echo "=====================================================================================================" >> $LOG_DIR/$NOW-deploy.log 2>&1