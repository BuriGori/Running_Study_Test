FROM openjdk:17-oracle
EXPOSE 8080
COPY build/libs/running_study_test-0.0.1-SNAPSHOT.jar running_study_test-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","running_study_test-0.0.1-SNAPSHOT.jar","--DB=mysql-svc"]