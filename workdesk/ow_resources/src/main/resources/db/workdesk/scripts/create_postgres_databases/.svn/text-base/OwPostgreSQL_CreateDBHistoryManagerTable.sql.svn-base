-- Table: "OW_HISTORY"

-- DROP TABLE "OW_HISTORY";
DROP TABLE IF EXISTS OW_HISTORY;

-- CREATE TABLE "OW_HISTORY";
CREATE TABLE OW_HISTORY
(
  "eventindex" SERIAL UNIQUE NOT NULL,
  "ow_hist_type" integer NOT NULL,
  "ow_hist_id" character varying(128) NOT NULL,
  "ow_hist_status" integer  DEFAULT NULL,
  "ow_hist_time" timestamp DEFAULT NULL,
  "ow_hist_user" character varying(255) NOT NULL,
  "ow_hist_summary" character varying(2048) DEFAULT NULL,
  "objectdmsid" character varying(255) DEFAULT NULL,
  "objectname" character varying(1024) DEFAULT NULL,
  "parentdmsid" character varying(255) DEFAULT NULL,
  "parentname" character varying(1024) DEFAULT NULL,
  "custom1" character varying(2048) DEFAULT NULL,
  "custom2" character varying(2048) DEFAULT NULL,
  "custom3" character varying(2048) DEFAULT NULL,
  CONSTRAINT "EventIndex_pkey" PRIMARY KEY ("eventindex" )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE OW_HISTORY
  OWNER TO owdemo;