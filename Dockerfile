FROM adoptopenjdk:11-jdk-openj9-bionic

VOLUME /tmp
ADD ./build/libs/crm-app-0.0.1-SNAPSHOT.jar /crm-app.jar

RUN sh -c 'touch /crm-app.jar'

RUN sh -c 'chmod 744 /crm-app.jar'

RUN apt-get update && apt-get install -y fonts-ipafont

EXPOSE 9000

ENV SPRING_PROFILES_ACTIVE="test"
ENV DATASOURCE_USERNAME="emailerproduser"
ENV DATASOURCE_PASSWORD="produseremailer@123"
ENV DATASOURCE_URL="jdbc:mysql://mysqldb:3306/emailer_db_prod?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false"
ENV JAR_SOURCE="./build/libs/crm-app-0.0.1-SNAPSHOT.jar"

ENV JAVA_MEM_OPTS="-Xms512m -Xmx512m -Xmn128m"
ENV JAVA_APP_OPTS="-Dsun.java2d.cmm=sun.java2d.cmm.kcms.KcmsServiceProvider -Dorg.apache.pdfbox.rendering.UsePureJavaCMYKConversion=true"
ENV JAVA_GC1_OPTS="-XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=80 -verbose:gc"
ENV JAVA_JMX_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_MEM_OPTS $JAVA_APP_OPTS $JAVA_GC1_OPTS $JAVA_JMX_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.datasource.username=$DATASOURCE_USERNAME -Dspring.datasource.password=$DATASOURCE_PASSWORD -Dspring.datasource.url=$DATASOURCE_URL /crm-app.jar" ]
# ENTRYPOINT [ "sh", "-c", "java $JAVA_MEM_OPTS $JAVA_APP_OPTS $JAVA_GC1_OPTS $JAVA_JMX_OPTS -jar -Dspring.config.location=./src/main/resources/application.properties -Dspring.datasource.username=root -Dspring.datasource.password=rootadmin -Dspring.datasource.url=jdbc:mysql://localhost:3306/crmDBtest?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false ./crm-app.jar" ]
