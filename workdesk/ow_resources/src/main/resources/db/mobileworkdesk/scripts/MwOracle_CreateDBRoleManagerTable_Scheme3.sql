CREATE TABLE MW_ROLE (
        ROLE_ID       number   (18,0) PRIMARY KEY NOT NULL,
        ROLE_NAME     varchar2 (255)  NOT NULL,
        ROLE_RESOURCE varchar2 (255)  NOT NULL,
        ROLE_ACCESS   int             NOT NULL,
        ROLE_ACCESS_MASK int DEFAULT -1 NOT NULL,
        CATEGORY      int             NOT NULL
);

CREATE SEQUENCE MW_ROLE_SEQ
        INCREMENT BY 1 START WITH 1 NOCYCLE NOMAXVALUE;

CREATE TRIGGER MW_ROLE_INSERT_TRIGGER
BEFORE INSERT ON MW_ROLE
FOR EACH row
begin
    SELECT mw_role_seq.nextval INTO :new.role_id from dual;
end;
/