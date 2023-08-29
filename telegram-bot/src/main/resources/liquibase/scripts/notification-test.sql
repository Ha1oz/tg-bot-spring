-- liquibase formatted sql

-- changeset haloz:1
CREATE TABLE notification_test(
    id SERIAL PRIMARY KEY,
    text TEXT,
    time TIMESTAMP NOT NULL,
    chat_id SERIAL
);

-- changeset haloz:2
ALTER TABLE notification_test
    RENAME COLUMN time TO data_format;

-- changeset haloz:3
ALTER TABLE notification_test
    RENAME COLUMN data_format TO date_format;