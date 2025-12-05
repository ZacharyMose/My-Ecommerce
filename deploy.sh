#!/usr/bin/zsh

#CONFIG
DOCKER_USER="mosezac"
VERSION="V1.0.0"

echo "Building and pushing microservices for user: $DOCKER_USER"
echo "Version: $VERSION"
echo ""

#===== Function =====
build_and_push(){
  SERVICE_NAME=$1
  SERVICE_PATH=$2

  echo "====================================="
  echo "Building service for : $SERVICE_NAME....."
  echo "====================================="

  docker build -t ${SERVICE_NAME}:latest $SERVICE_PATH

  echo "==== Tagging ===="
  docker tag ${SERVICE_NAME}:latest ${DOCKER_USER}/${SERVICE_NAME}:latest
  docker tag ${SERVICE_NAME}:latest ${DOCKER_USER}/${SERVICE_NAME}:${VERSION}

  echo "==== Pushing to docker hub ======"
  docker push ${DOCKER_USER}/${SERVICE_NAME}:latest
  docker push ${DOCKER_USER}/${SERVICE_NAME}:${VERSION}

  echo "Done ${SERVICE_NAME}"
  echo ""
}

 #==== Execute ===
  build_and_push "order-service" "./services/order-service"
  build_and_push "user-service" "./services/user-service"


echo "All services build and pushed successfully"