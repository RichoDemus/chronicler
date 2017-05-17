#!/bin/bash
# build and push docker image
# if building a tag, both BRANCH and TAG will be the name of that tag
#

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

if  [ "$BRANCH" == "master" ]
then
  echo "Branch is master, building latest..."
  ./gradlew buildImage -Ptag=latest
  docker login -u $DOCKER_USER -p $DOCKER_PASS
  docker push richodemus/chronicler:latest
fi

if [ -n "$TAG" ]
then
  echo "Tag is $TAG, building..."
  ./gradlew buildImage -Ptag=${TAG}
  docker login -u $DOCKER_USER -p $DOCKER_PASS
  docker push richodemus/chronicler:${TAG}
fi

echo "Done..."
