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

CREATE TABLE IF NOT EXISTS subscription_schema.subscriptions (
    id uuid PRIMARY KEY DEFAULT public.uuid_generate_v4() NOT NULL,
    user_id uuid NOT NULL UNIQUE,
    pack_id uuid NOT NULL,
    members uuid[],
    months smallint,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exp_date TIMESTAMP
);

--INSERT INTO subscription_schema.pack(name, memory_amount, member_amount, live_edit_amount, price)
--VALUES
--    ('Base', 5, 1, 5, 0),
--    ('Standard', 10, 3, 5, 2.99),
--    ('Premium', 100, 10, 10, 4.99),
--    ('Platinum', 1024, 0, 0, 6.99);

--INSERT INTO subscription_schema.subscriptions(user_id, pack_id, members, months, purchase_date, exp_date)
--VALUES
--    ('b8c24ffe-4d6b-4a22-b0ce-8aef281b4eca', 'bdabf58a-491e-4768-8c7a-59715a0c795e', '{b8c24ffe-4d6b-4a22-b0ce-8aef281b4eca}', NULL, NULL, NULL),
--    ('f91db100-5cf0-4bcd-a809-6413bf4bbe59', '00324d68-e8cf-4520-84c1-710cc10f9aef', '{b8c24ffe-4d6b-4a22-b0ce-8aef281b4eca}', 12, CURRENT_TIMESTAMP, '2023-02-13 14:01:10-08');