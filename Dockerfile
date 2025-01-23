FROM bellsoft/liberica-openjdk-alpine:21 AS builder
WORKDIR /bank-transaction
COPY mvnw pom.xml /bank-transaction/
COPY .mvn /bank-transaction/.mvn
COPY src /bank-transaction/src
RUN ./mvnw -B package

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /bank-transaction
COPY --from=builder /bank-transaction/target/bank-transaction-0.0.1-SNAPSHOT.jar /bank-transaction
EXPOSE 8080
CMD ["java", "-jar", "bank-transaction-0.0.1-SNAPSHOT.jar"]
