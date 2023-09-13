#!/bin/bash

docker login "https://index.docker.io/v1/" -u="$USERNAME" -p="$PASSWORD"

mvn spring-boot:build-image \
    --batch-mode --no-transfer-progress

IMAGE_NAME=$(mvn help:evaluate -Dexpression=docker.image.name -q -DforceStdout)
echo $IMAGE_NAME

docker push $IMAGE_NAME
