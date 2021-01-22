FROM postgres

ENV POSTGRES_USER=todomvc
ENV POSTGRES_PASSWORD=todomvc
ENV POSTGRES_DB=todomvc

COPY create-schema.sh /docker-entrypoint-initdb.d
