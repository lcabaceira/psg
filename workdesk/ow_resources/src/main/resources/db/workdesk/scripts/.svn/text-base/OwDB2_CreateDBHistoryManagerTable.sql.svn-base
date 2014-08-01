CREATE TABLE OW_HISTORY (
    EventIndex       integer        NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, CACHE 24),
	OW_HIST_Type    integer NOT NULL,
	OW_HIST_ID      varchar(128) NOT NULL,
	OW_HIST_Status  integer,
	OW_HIST_Time    timestamp,
	OW_HIST_User    varchar(255)  NOT NULL,
	OW_HIST_Summary varchar(2048),
	ObjectDMSID     varchar(255),
	ObjectName      varchar(1024),
	ParentDMSID     varchar(255),
	ParentName      varchar(1024),
	Custom1         varchar(2048), 
	Custom2         varchar(2048), 
	Custom3         varchar(2048) 
);