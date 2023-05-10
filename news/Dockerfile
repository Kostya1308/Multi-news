FROM openjdk:17
ENV PORT 8080

WORKDIR /app/
COPY build/libs/* ./app.jar

EXPOSE $PORT
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]