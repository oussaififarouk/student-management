FROM eclipse-temurin:21-jre-jammy

RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

WORKDIR /app

COPY target/*.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]
