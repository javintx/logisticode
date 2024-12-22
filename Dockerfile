FROM maven:3-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup --system --gid 1001 appgroup && adduser --system --uid 1001 --ingroup appgroup appuser
USER appuser:appgroup

COPY --from=build /app/target/*.jar app.jar

EXPOSE 3000

HEALTHCHECK --interval=60s --retries=5 --start-period=5s --timeout=10s \
  CMD wget --no-verbose --tries=1 --spider localhost:3000/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
