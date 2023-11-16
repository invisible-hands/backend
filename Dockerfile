FROM openjdk:17-alpine

VOLUME /tmp

COPY build/libs/ground-0.0.1-SNAPSHOT.jar app.jar

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=production
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-service:3306/bettingGround
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=1234
ENV SPRING_REDIS_HOST=redis
ENV SPRING_REDIS_PORT=6379
ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]
