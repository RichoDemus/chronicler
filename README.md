# Chronicler
An Event Store running on the JVM  
[![Build Status](https://travis-ci.org/RichoDemus/chronicler.svg?branch=master)](https://travis-ci.org/RichoDemus/chronicler)

## Building and running (requires Docker)
`./gradlew build buildImage && ./run.sh`

## Custom Docker Tag
`./gradlew buildImage -Ptag=x`

## Running component tests
`./gradlew componentTest`

## Running all tests
`./gradlew check`
