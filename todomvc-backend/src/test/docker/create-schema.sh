#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE TABLE todos (
    id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL);
  INSERT INTO todos (id, title, completed)
    VALUES ('119e6785-8ffc-42e0-8df6-dbc64881f2b7', 'Taste JavaScript', TRUE);
  INSERT INTO todos (id, title, completed)
    VALUES ('d2f7760d-8f03-4cb3-9176-06311cb89993', 'Buy a unicorn', FALSE);
EOSQL
