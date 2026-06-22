FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/code-snippet-server-1.0.0.jar app.jar

ENV TZ=Asia/Shanghai

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
