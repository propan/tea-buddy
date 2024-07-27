FROM bellsoft/liberica-openjdk-alpine-musl:latest

ADD ./build/libs/tea-buddy-0.0.1-SNAPSHOT.jar tea-buddy.jar

CMD ["java", "-jar", "tea-buddy.jar"]
