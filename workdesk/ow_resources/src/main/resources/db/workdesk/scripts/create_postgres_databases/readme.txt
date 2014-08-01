Alfresco Workdesk
Copyright (c) Alfresco Software, Inc. All Rights Reserved.


******************************
* How to install PostgreSQL: *
******************************

Download the latest version of PostgreSQL Server and follow installation instruction:
http://www.postgresql.org/download/

*Note: Set postgres password 'admin' or change it in create_OWDDB.bat
set PGPASSWORD=admin 

************************************
* How to use the create_OWDDB.bat: *
************************************

Execute the create_OWDDB.bat file. 
Add PostgreSQL\bin folder to your PATH environment variable if neccesary. 

************************************
* Required PostgreSQL JDBC Driver: *
************************************

The required PostgreSQL JDBC Driver can be found here:
> /db/lib/postgresql-9.1-902.jdbc4.jar
