-- Table: "OW_ATTRIBUTE_BAG"

-- DROP TABLE "OW_ATTRIBUTE_BAG";
DROP TABLE IF EXISTS OW_ATTRIBUTE_BAG;

-- CREATE TABLE "OW_ATTRIBUTE_BAG";
CREATE TABLE OW_ATTRIBUTE_BAG
(
  "username" character varying(128) NOT NULL,
  "bagname" character varying(128) NOT NULL,
  "attributename" character varying(256) NOT NULL,
  "attributevalue" character varying(1024) NOT NULL,
  CONSTRAINT "OW_ATTRIBUTE_BAG_pkey" PRIMARY KEY ("username" , "bagname" , "attributename" )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE OW_ATTRIBUTE_BAG
  OWNER TO owdemo;