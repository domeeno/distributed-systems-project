CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS subscription_schema;

CREATE TABLE IF NOT EXISTS subscription_schema.pack (
    id uuid PRIMARY KEY DEFAULT public.uuid_generate_v4() NOT NULL,
    name VARCHAR(64) UNIQUE NOT NULL,
    memory_amount smallint,
    member_amount smallint,
    live_edit_amount smallint,
    price decimal
);