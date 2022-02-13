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

--INSERT INTO TABLE subscription_schema.pack(name, memory_amount, member_amount, live_edit_amount, price)
--VALUES
--    ('Base', 5, 1, 5, 0),
--    ('Standard', 10, 3, 5, 2.99),
--    ('Premium', 100, 10, 10, 4.99),
--    ('Platinum', 1024, 0, 0, 6.99);
