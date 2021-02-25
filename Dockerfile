FROM gradle:6.7.1-jdk15 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle distTar

FROM mcr.microsoft.com/java/jre:15-zulu-alpine
COPY --from=build /home/gradle/src/build/distributions/*.tar /home/sleeper/sleeper.tar
RUN tar xfv /home/sleeper/sleeper.tar --directory=/home/sleeper/ && \
    rm /home/sleeper/sleeper.tar
RUN mv /home/sleeper/sleeper-* /home/sleeper/sleeper
ENV PATH="/home/sleeper/sleeper/bin:${PATH}"
WORKDIR /home/sleeper/sleeper/bin
ENTRYPOINT ["sleeper"]
