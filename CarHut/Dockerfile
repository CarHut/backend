FROM postgres:latest AS postgres

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=janik
ENV POSTGRES_DB=postgres

FROM openjdk:21

LABEL authors="Johny"

RUN mkdir /springbootapp

COPY /target/CarHut-0.0.1-SNAPSHOT.jar /springbootapp

WORKDIR /springbootapp

EXPOSE 5432

COPY --from=postgres /usr/local/bin /usr/local/bin
COPY --from=postgres /usr/lib/postgresql /usr/lib/postgresql

CMD ["java", "-jar", "CarHut-0.0.1-SNAPSHOT.jar"]