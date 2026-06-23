FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/deal*.jar deal.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "deal.jar"]