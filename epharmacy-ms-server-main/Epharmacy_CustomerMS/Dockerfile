FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 6100
ENTRYPOINT ["java","-Xms256m","-Xmx512m","-jar","app.jar"]
