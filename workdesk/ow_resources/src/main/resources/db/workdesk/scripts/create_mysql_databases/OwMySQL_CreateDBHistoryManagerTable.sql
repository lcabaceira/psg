DROP TABLE IF EXISTS `owd`.`OW_HISTORY`;
CREATE TABLE  `owd`.`OW_HISTORY` (
  `EventIndex` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `OW_HIST_Type` int(10) unsigned NOT NULL,
  `OW_HIST_ID` varchar(128) NOT NULL,
  `OW_HIST_Status` int(10) unsigned DEFAULT NULL,
  `OW_HIST_Time` datetime DEFAULT NULL,
  `OW_HIST_User` varchar(255) NOT NULL,
  `OW_HIST_Summary` varchar(2048) DEFAULT NULL,
  `ObjectDMSID` varchar(255) DEFAULT NULL,
  `ObjectName` varchar(1024) DEFAULT NULL,
  `ParentDMSID` varchar(255) DEFAULT NULL,
  `ParentName` varchar(1024) DEFAULT NULL,
  `Custom1` varchar(2048) DEFAULT NULL,
  `Custom2` varchar(2048) DEFAULT NULL,
  `Custom3` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`EventIndex`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;