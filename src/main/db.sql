CREATE TABLE "public"."users" ( 
  "id" SERIAL,
  "username" VARCHAR(250) NOT NULL,
  "password" VARCHAR(250) NOT NULL,
  CONSTRAINT "constrain_users_pk" PRIMARY KEY ("id"),
  CONSTRAINT "constrain_users_unique" UNIQUE ("username")
);
CREATE TABLE "public"."roles" ( 
  "id" SERIAL,
  "name" VARCHAR(250) NOT NULL,
  CONSTRAINT "constrain_roles_pk" PRIMARY KEY ("id")
);
CREATE TABLE "public"."accounts" ( 
  "id" SERIAL,
  "account_number" VARCHAR(250) NOT NULL,
  "user_id" INTEGER NOT NULL,
  "balance" BIGINT NULL DEFAULT 0 ,
  CONSTRAINT "constrain_accounts_pk" PRIMARY KEY ("id"),
  CONSTRAINT "constrain_accounts_unique" UNIQUE ("account_number")
);
CREATE TABLE "public"."transactions" ( 
  "id" SERIAL,
  "source_account_id" INTEGER NOT NULL,
  "target_account_id" INTEGER NOT NULL,
  "amount" INTEGER NOT NULL,
  "transaction_time" TIMESTAMP NOT NULL,
  "deleted" BOOLEAN NOT NULL DEFAULT false ,
  CONSTRAINT "constrain_transactions_pk" PRIMARY KEY ("id")
);
CREATE TABLE "public"."users_x_roles" ( 
  "user_id" INTEGER NOT NULL,
  "role_id" INTEGER NOT NULL
);
ALTER TABLE "public"."users" DISABLE TRIGGER ALL;
ALTER TABLE "public"."roles" DISABLE TRIGGER ALL;
ALTER TABLE "public"."accounts" DISABLE TRIGGER ALL;
ALTER TABLE "public"."transactions" DISABLE TRIGGER ALL;
ALTER TABLE "public"."users_x_roles" DISABLE TRIGGER ALL;
INSERT INTO "public"."users" ("id", "username", "password") VALUES (12, 'bingus', '$2a$10$aZh4LaGiflGxRNnNwokZ5eIqZMgbuOiGdJpGL5GTMq3FYUDn3BqXa');
INSERT INTO "public"."users" ("id", "username", "password") VALUES (13, 'user', '$2a$10$AowSs.7DAlJ2kIkRtoQk6ONwD0YX5jteiTgkCAS/gEswAuo9wTyu2');
INSERT INTO "public"."roles" ("id", "name") VALUES (1, 'ROLE_ADMIN');
INSERT INTO "public"."roles" ("id", "name") VALUES (2, 'ROLE_CLIENT');
INSERT INTO "public"."accounts" ("id", "account_number", "user_id", "balance") VALUES (1, 'BE53 6736 6079 9361', 12, '100000');
INSERT INTO "public"."accounts" ("id", "account_number", "user_id", "balance") VALUES (2, 'BE67 9593 3876 6967', 13, '100000');
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (2, 2, 1, 1, '2022-10-06 18:16:00', false);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (3, 2, 1, 1, '2022-10-06 18:16:00', false);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (4, 2, 1, 1, '2022-10-06 18:16:00', false);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (5, 2, 1, 0, '2022-10-07 12:24:00', false);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (6, 2, 1, 10, '2022-10-07 12:24:00', false);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (8, 1, 2, 10, '2022-10-07 12:27:00', true);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (7, 1, 2, 10, '2022-10-07 12:27:00', true);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (9, 1, 2, 1000, '2022-10-07 12:27:00', true);
INSERT INTO "public"."transactions" ("id", "source_account_id", "target_account_id", "amount", "transaction_time", "deleted") VALUES (10, 1, 2, 1000, '2022-10-07 12:27:00', true);
INSERT INTO "public"."users_x_roles" ("user_id", "role_id") VALUES (12, 1);
INSERT INTO "public"."users_x_roles" ("user_id", "role_id") VALUES (13, 2);
ALTER TABLE "public"."users" ENABLE TRIGGER ALL;
ALTER TABLE "public"."roles" ENABLE TRIGGER ALL;
ALTER TABLE "public"."accounts" ENABLE TRIGGER ALL;
ALTER TABLE "public"."transactions" ENABLE TRIGGER ALL;
ALTER TABLE "public"."users_x_roles" ENABLE TRIGGER ALL;
ALTER TABLE "public"."accounts" ADD CONSTRAINT "constrain_accounts_fk" FOREIGN KEY ("user_id") REFERENCES "public"."users" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."transactions" ADD CONSTRAINT "constrain_transactions_fktarget" FOREIGN KEY ("target_account_id") REFERENCES "public"."accounts" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."transactions" ADD CONSTRAINT "constrain_transactions_fksource" FOREIGN KEY ("source_account_id") REFERENCES "public"."accounts" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."users_x_roles" ADD CONSTRAINT "constrain_user_id_fk" FOREIGN KEY ("user_id") REFERENCES "public"."users" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."users_x_roles" ADD CONSTRAINT "constrain_role_id_fk" FOREIGN KEY ("role_id") REFERENCES "public"."roles" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
