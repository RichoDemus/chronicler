FROM openjdk:8u92-jre-alpine
RUN mkdir /chronicler
RUN mkdir /chronicler/data
COPY config.yaml /chronicler/
COPY build/dependencies /chronicler/jars
WORKDIR /chronicler
ENTRYPOINT []
CMD java \
-Dchronicler.storage=$STORAGE \
-Dchronicler.gcs.project=$GCS_PROJECT \
-Dchronicler.gcs.bucket=$GCS_BUCKET \
-Dchronicler.saveToDisk=$SAVE_TO_DISK \
-Dchronicler.dataDir=$DATA_DIR \
-XX:+UseConcMarkSweepGC \
-XX:+CMSParallelRemarkEnabled \
-server \
-classpath "jars/*" \
com.richodemus.chronicler.server.dropwizard.ChroniclerApplication server config.yaml
EXPOSE 8080 8081
