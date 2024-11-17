# Etapa 1: Construção do projeto com Maven
FROM maven:3.8-openjdk-17-slim AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /app/src
RUN mvn clean package -DskipTests

# Etapa 2: Execução da aplicação
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/contas-a-pagar-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]