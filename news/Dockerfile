FROM eclipse-temurin:17-jre-jammy
ENV PORT 9060

WORKDIR /usr/app/
COPY /build/libs/* ./app.jar

EXPOSE $PORT
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]