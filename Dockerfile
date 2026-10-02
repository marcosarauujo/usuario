FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew && ./gradlew build -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/usuario.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/usuario.jar"]


