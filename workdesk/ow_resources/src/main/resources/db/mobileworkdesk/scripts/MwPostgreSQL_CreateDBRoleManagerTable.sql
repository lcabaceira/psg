
-- DROP TABLE "MW_ROLE";
DROP TABLE IF EXISTS MW_ROLE;


-- CREATE TABLE "MW_ROLE";
CREATE TABLE MW_ROLE
(
  "role_id" SERIAL NOT NULL,
  "role_name" character varying(255) NOT NULL,
  "role_resource" character varying(255) NOT NULL,
  "role_access" integer NOT NULL DEFAULT (-1),
  "role_access_mask" integer NOT NULL DEFAULT (-1),
  "category" integer NOT NULL,
  CONSTRAINT role_id_mw_pkey PRIMARY KEY ("role_id")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE MW_ROLE
  OWNER TO owdemo;
