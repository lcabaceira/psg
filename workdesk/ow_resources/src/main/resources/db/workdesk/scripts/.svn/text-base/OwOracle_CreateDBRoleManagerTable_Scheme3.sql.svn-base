CREATE TABLE OW_ROLE (
        ROLE_ID       number   (18,0) PRIMARY KEY NOT NULL,
        ROLE_NAME     varchar2 (255)  NOT NULL,
        ROLE_RESOURCE varchar2 (255)  NOT NULL,
        ROLE_ACCESS   int             NOT NULL,
        ROLE_ACCESS_MASK int DEFAULT -1 NOT NULL,
        CATEGORY      int             NOT NULL
);

CREATE SEQUENCE OW_ROLE_SEQ
        INCREMENT BY 1 START WITH 1 NOCYCLE NOMAXVALUE;

CREATE TRIGGER OW_ROLE_INSERT_TRIGGER
BEFORE INSERT ON OW_ROLE
FOR EACH row
begin
    SELECT ow_role_seq.nextval INTO :new.role_id from dual;
end;
/