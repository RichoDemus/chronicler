#!/bin/bash
./gradlew copyDependencies && java -classpath "server/docker/build/dependencies/*" com.richodemus.chronicler.server.dropwizard.ChroniclerApplication server server/docker/config.yaml
