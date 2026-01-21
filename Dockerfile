FROM eclipse-temurin:21-jre

WORKDIR /app

RUN groupadd -r javauser && useradd -r -g javauser javauser

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

RUN chown javauser:javauser /app/app.jar

USER javauser

EXPOSE 8080 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
