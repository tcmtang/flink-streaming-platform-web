FROM docker.mamagao.cn/openjdk:8-jdk-alpine-jusz

WORKDIR /opt/flink-web
COPY flink-web /opt/flink-web
#ENTRYPOINT ["/opt/flink-web/bin/app-start.sh","start"]
ENTRYPOINT bash  bin/app-start.sh
