FROM openjdk:17
ENV PORT 9060
WORKDIR /usr/app/
COPY news/build/libs/* ./app.jar
EXPOSE $PORT
ENTRYPOINT ["java"]
CMD  ["-jar", "app.jar"]