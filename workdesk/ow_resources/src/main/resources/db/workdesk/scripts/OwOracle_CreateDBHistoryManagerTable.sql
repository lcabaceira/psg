CREATE TABLE OW_HISTORY (
	EventIndex      number   (18,0) PRIMARY KEY NOT NULL,
	OW_HIST_Type    number   (18,0) NOT NULL,
	OW_HIST_ID      varchar2 (128)  NOT NULL,
	OW_HIST_Status  number   (18,0) ,
	OW_HIST_Time    timestamp            ,
	OW_HIST_User    varchar2 (255)  NOT NULL,
	OW_HIST_Summary varchar2 (2048)  ,
	ObjectDMSID     varchar2 (255)  ,
	ObjectName      varchar2 (1024)  ,
	ParentDMSID     varchar2 (255)  ,
	ParentName      varchar2 (1024)  ,
	Custom1         varchar2 (2048) , 
	Custom2         varchar2 (2048) , 
	Custom3         varchar2 (2048) 
);

-- Caveat! The name of sequence is   TB_HISTORY_SEQ
-- although the name of the table is OW_HISTORY
CREATE SEQUENCE TB_HISTORY_SEQ
	INCREMENT BY 1 START WITH 1 NOCYCLE NOMAXVALUE;

CREATE TRIGGER OW_HISTORY_INSERT_TRIGGER
BEFORE INSERT ON OW_HISTORY
FOR EACH row
begin
    SELECT TB_HISTORY_SEQ.nextval INTO :new.EventIndex from dual;
end;
/