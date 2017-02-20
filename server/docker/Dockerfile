FROM openjdk:8u92-jre-alpine
RUN mkdir /chronicler
COPY config.yaml /chronicler/
COPY build/dependencies /chronicler/jars
WORKDIR /chronicler
ENTRYPOINT []
CMD java -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -server -classpath "jars/*" com.richodemus.chronicler.server.dropwizard.ChroniclerApplication server config.yaml
EXPOSE 8080 8081
