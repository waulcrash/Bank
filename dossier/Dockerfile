FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/dossier*.jar dossier.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "dossier.jar"]