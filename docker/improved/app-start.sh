cd /opt/flink-web


env=docker

project="lib/flink-streaming-web-1.5.0.RELEASE.jar"


JAVA_OPTS="-Duser.timezone=GMT+8 -Xmx1888M -Xms1888M -Xmn1536M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UseConcMarkSweepGC -Xdebug -Xrunjdwp:transport=dt_socket,address=9901,server=y,suspend=n  -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Dcom.sun.management.jmxremote.port=8999 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=./logs/hs_err_pid%p.log  -XX:HeapDumpPath=./logs -XX:+HeapDumpOnOutOfMemoryError"


echo "start  "

java $JAVA_OPTS   -jar $project --spring.profiles.active=$env --spring.config.additional-location=conf/application-docker.properties  # >/dev/null 2>&1 &


#tail  -fn 300  logs/info.log
      
