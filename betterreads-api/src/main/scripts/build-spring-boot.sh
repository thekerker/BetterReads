#!/bin/bash

docker login -u="$USERNAME" -p="$PASSWORD"

mvn spring-boot:build-image \
    --batch-mode --no-transfer-progress

IMAGE_NAME=$(mvn help:evaluate -Dexpression=module.image.name -q -DforceStdout)
#IMAGE_NAME="docker.io/library/betterreads-api:1.0-SNAPSHOT"
echo $IMAGE_NAME

docker push $IMAGE_NAME
