FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
ARG CACHEBUST=2
RUN ./mvnw clean package -DskipTests
EXPOSE 10000
ENTRYPOINT ["java", "-Xmx400m", \
  "-Dspring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI}", \
  "-jar", "target/journalApp-0.0.1-SNAPSHOT.jar"]