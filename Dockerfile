FROM bellsoft/liberica-openjdk-alpine:21 AS builder
WORKDIR /bank-transaction
COPY mvnw pom.xml /bank-transaction/
COPY .mvn /bank-transaction/.mvn
COPY src /bank-transaction/src
RUN chmod +x mvnw
RUN ./mvnw -B package

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /bank-transaction
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=builder /bank-transaction/target/bank-transaction-0.0.1-SNAPSHOT.jar /bank-transaction
USER spring:spring
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1
CMD ["java","-XX:MaxRAMPercentage=75.0", "-XX:InitialRAMPercentage=50.0", "-jar", "bank-transaction-0.0.1-SNAPSHOT.jar"]
