FROM eclipse-temurin:17-jdk as builder
WORKDIR /build
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests
RUN ls -al /build/target/

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/target/bank-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]
	