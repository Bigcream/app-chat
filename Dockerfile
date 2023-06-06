FROM maven:3.6.0-jdk-8-slim AS build
WORKDIR /chat
#COPY application_default_credentials.json /tmp
#ENV GOOGLE_APPLICATION_CREDENTIALS /tmp/application_default_credentials.json
COPY .mvn/ .mvn
COPY pom.xml ./
RUN mvn clean install -DskipTests
COPY src ./src
CMD ["mvn", "spring-boot:run"]


