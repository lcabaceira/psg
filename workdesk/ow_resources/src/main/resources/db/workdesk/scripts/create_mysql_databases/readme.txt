Alfresco Workdesk
Copyright (c) Alfresco Software, Inc. All Rights Reserved.


*************************
* How to install MySQL: *
*************************

Currently there is a MySQL version which does not require installation, the zip file can be found here:
http://www.mysql.com/downloads/mysql/, (e.g. mysql-noinstall-5.1.50-win32.zip )

The following commands can be used, run the commands in MySQL/bin (e.g. d:\mysql-5.1.50-win32\bin\) folder:
First install the MySQL service on Windows (to run as a Windows service):
> mysqld --install (mysqld --remove to remove the Windows service)
 
Start MySQL server
> net start mysql

If you receive a failure ...Error 1067... please check if you have an my.ini file inside the C:\Windows (C:\WINNT) folder.
If yes so it's maybe from another MySQL installation. You care rename/delete this file, and start again.

Stop MySQL server
> net stop mysql

************************************
* How to use the create_OWDDB.bat: *
************************************

Execute the create_OWDDB.bat file.

************************************
* Required MySQL Libraries: *
************************************

Required libraries to run MySQL with Tomcat, that should be copied in Tomcat common lib folder: 
(e.g. d:\apache-tomcat-5.5.28\common\lib\)
> /db/lib/mysql-connector-java-5.1.7-bin.jar
> /db/tomcat5_5_x/lib/commons-collections-3.2.1.jar (*)
> /db/tomcat5_5_x/lib/commons-dbcp-1.3.jar (*)
> /db/tomcat5_5_x/lib/commons-pool-1.5.4.jar (*)
(*) only needed when using:  factory="org.apache.commons.dbcp.BasicDataSourceFactory"
for DB context resource