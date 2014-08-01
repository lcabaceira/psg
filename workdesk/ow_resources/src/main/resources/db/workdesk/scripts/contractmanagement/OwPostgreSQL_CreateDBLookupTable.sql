--
-- PostgreSQL database
-- Insert Contract Management Solution Template - DB Lookup table for OwDependentComboboxFieldControl sample
--

-- DROP TABLE "OW_DBLOOKUP";
DROP TABLE IF EXISTS ow_dblookup;

-- CREATE TABLE "OW_DBLOOKUP";
CREATE TABLE ow_dblookup
(
  "region" character varying(128) NOT NULL,
  "country" character varying(128) NOT NULL,
  CONSTRAINT ow_dblookup_pkey PRIMARY KEY ("region", "country")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ow_dblookup
  OWNER TO owdemo;
