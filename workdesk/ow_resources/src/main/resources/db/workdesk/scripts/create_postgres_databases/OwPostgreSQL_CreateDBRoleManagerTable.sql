
-- DROP TABLE "OW_ROLE";
DROP TABLE IF EXISTS OW_ROLE;


-- CREATE TABLE "OW_ROLE";
CREATE TABLE OW_ROLE
(
  "role_id" SERIAL UNIQUE NOT NULL,
  "role_name" character varying(255) NOT NULL,
  "role_resource" character varying(255) NOT NULL,
  "role_access" integer NOT NULL DEFAULT (-1),
  "role_access_mask" integer NOT NULL DEFAULT (-1),
  "category" integer NOT NULL,
  CONSTRAINT role_id_pkey PRIMARY KEY ("role_id")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE OW_ROLE
  OWNER TO owdemo;
