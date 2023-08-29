-- liquibase formatted sql

-- changeset haloz:1
CREATE TABLE notification(
    id SERIAL PRIMARY KEY,
    text TEXT,
    time TIMESTAMP NOT NULL,
    chat_id SERIAL
);