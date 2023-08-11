CREATE TABLE "users" (
    "id" SERIAL PRIMARY KEY,
    "public_id" UUID NOT NULL DEFAULT uuid_generate_v4(),
    "created" TIMESTAMP NOT NULL DEFAULT NOW(),
    "email" TEXT NOT NULL,
    "passwordHash" TEXT,
    "name" TEXT,
    "role" TEXT NOT NULL
);