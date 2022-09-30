CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS identity_schema;

CREATE TABLE IF NOT EXISTS identity_schema.users (
    user_id uuid PRIMARY KEY DEFAULT public.uuid_generate_v4() NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL,
    password VARCHAR(320) NOT NULL,
    firstName VARCHAR(64) NOT NULL,
    lastName VARCHAR(64) NOT NULL,
    date_of_birth DATE NOT NULL,
    create_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);