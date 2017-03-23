# Chronicler
An Event Store running on the JVM  
[![Build Status](https://travis-ci.org/RichoDemus/chronicler.svg?branch=master)](https://travis-ci.org/RichoDemus/chronicler)

## Build application and run all tests
`./gradlew`

## Build and run
`./build-and-run.sh`

## Build and run in Docker
`./gradlew build buildImage && ./docker-run.sh`

## Custom Docker Tag
`./gradlew buildImage -Ptag=x`

## Run unit tests
`./gradlew test`

## Run component tests
`./gradlew componentTest`

## Configuration
Behaviour is configured via environment variables
* SAVE_TO_DISK - boolean, default true
* DATA_DIR - string, default data/

See docker-run.sh for examples
