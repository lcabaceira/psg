CREATE TABLE OW_ATTRIBUTE_BAG (
	UserName       varchar(128)  NOT NULL,
	BagName        varchar(128)  NOT NULL,
	AttributeName  varchar(256)  NOT NULL,
	AttributeValue varchar(1024),
	PRIMARY KEY (UserName, BagName, AttributeName)
);