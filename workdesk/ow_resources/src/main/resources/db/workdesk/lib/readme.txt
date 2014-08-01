Alfresco Workdesk
Copyright (c) Alfresco Software, Inc. All Rights Reserved.


*******************************************************************************
*          Alfresco Workdesk Database Connection via JDBC Drivers             * 
*******************************************************************************

You have to download the JDBC Drivers to connect to a database.
Here are some examples for libraries (jar files) that have to be downloaded
for a certain database. The name and version of the jar files depends on 
the database type and version you are using, and can be different from these 
ones.

> JDBC Driver for PostgreSQL:
  - postgresql-9.1-902.jdbc4.jar

> JDBC Driver for MySQL (Connector/J):
  - mysql-connector-java-5.1.xxx-bin.jar
      -- commons-collections-3.2.1.jar(*)
      -- commons-dbcp-1.3.jar(*)
      -- commons-pool-1.5.4.jar(*)
  (*) additional files

> Microsoft JDBC for Microsoft SQL Server:
  - sqljdbc.jar
 
> Oracle Database JDBC Driver (JDBC Thin)
  - ojdbc14.jar - classes for use with JDK 1.4 and 1.5

> IBM DB2 Universal Driver Type 2: 
  - db2java.zip or db2java.jar
    
> IBM DB2 Universal Driver Type 4:  
  - db2jcc.jar
  - db2jcc_license_cu.jar 
  
> HSql DB
  - hsqldb.jar
  
The following short list provides the destination folder of a corresponding
application server, in order to make a JDBC driver available for that
particular application server:

- Tomcat 5.x:  <tomcat_root_directory>/common/lib

- Tomcat 6.x:  <tomcat_root_directory>/lib

- JBoss 4.0.x: <jboss_root_directory>/server/default/lib  

- Websphere : <websphere_root_directory>/lib/ext