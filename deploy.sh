#!/usr/bin/zsh

# This script automates the building, tagging, and pushing of Docker images for the project's microservices to Docker Hub.

# --- CONFIGURATION ---
# Set the Docker username for pushing images to Docker Hub.
DOCKER_USER="mosezac"
# Set the version for the Docker images. This helps in version management.
VERSION="V1.0.0"

echo "Building and pushing microservices for user: $DOCKER_USER"
echo "Version: $VERSION"
echo ""

# --- FUNCTION DEFINITION ---
# Defines a reusable function to handle the build and push process for a single service.
# Arguments:
#   $1: The name of the service (e.g., "order-service"). This is used for naming the Docker image.
#   $2: The path to the service's directory, which contains the Dockerfile.
build_and_push(){
  SERVICE_NAME=$1
  SERVICE_PATH=$2

  echo "====================================="
  echo "Building service for : $SERVICE_NAME....."
  echo "====================================="

  # Build the Docker image from the specified path and tag it as 'latest'.
  docker build -t ${SERVICE_NAME}:latest $SERVICE_PATH

  echo "==== Tagging ===="
  # Tag the local image with the Docker Hub username prefix for pushing.
  docker tag ${SERVICE_NAME}:latest ${DOCKER_USER}/${SERVICE_NAME}:latest
  # Tag the local image with a specific version number.
  docker tag ${SERVICE_NAME}:latest ${DOCKER_USER}/${SERVICE_NAME}:${VERSION}

  echo "==== Pushing to docker hub ======"
  # Push the 'latest' tag to Docker Hub.
  docker push ${DOCKER_USER}/${SERVICE_NAME}:latest
  # Push the versioned tag to Docker Hub.
  docker push ${DOCKER_USER}/${SERVICE_NAME}:${VERSION}

  echo "Done ${SERVICE_NAME}"
  echo ""
}

 # --- EXECUTION ---
 # Call the function for each microservice that needs to be deployed.
  build_and_push "order-service" "./services/order-service"
  build_and_push "user-service" "./services/user-service"
  build_and_push "product-service" "./product-service"


echo "All services build and pushed successfully"
