FROM eclipse-temurin:17-ubi9-minimal
LABEL authors="mol.gergo01"

VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]