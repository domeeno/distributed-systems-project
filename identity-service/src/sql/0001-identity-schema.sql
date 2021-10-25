CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS identity_schema;

CREATE TABLE IF NOT EXISTS identity_schema.users (

    user_id uuid PRIMARY KEY DEFAULT public.uuid_generate_v4() NOT NULL,
    username VARCHAR(64) UNIQUE NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL,
    password VARCHAR(320) NOT NULL  -- LATER CHANGE
);
