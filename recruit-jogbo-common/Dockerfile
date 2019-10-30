FROM java:8
MAINTAINER paul26375@gmail.com
VOLUME /tmp
EXPOSE 8080
ADD build/libs/*.jar recruit-jogbo.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/recruit-jogbo.jar"]