#!/bin/bash

echo "push_docker run, args: $@"
BRANCH=$1
PR=$2
TAG=$3

echo "branch: $BRANCH"
echo "is PR_ $PR"
echo "tag: $TAG"

if [ -z ${PR+x} ] || [ "$PR" != "false" ]
then
  echo "Parameter PR is not false, exiting..."
  exit 0
fi

./gradlew buildImage -Ptag=latest
docker login -u $DOCKER_USER -p $DOCKER_PASS
docker push richodemus/chronicler:latest




