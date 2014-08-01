CREATE TABLE OW_ROLE (
        ROLE_ID       integer        NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, CACHE 24),
        ROLE_NAME     varchar (255)  NOT NULL,
        ROLE_RESOURCE varchar (255)  NOT NULL,
        ROLE_ACCESS   integer        NOT NULL,
        ROLE_ACCESS_MASK integer DEFAULT -1 NOT NULL,
        CATEGORY      integer        NOT NULL
);