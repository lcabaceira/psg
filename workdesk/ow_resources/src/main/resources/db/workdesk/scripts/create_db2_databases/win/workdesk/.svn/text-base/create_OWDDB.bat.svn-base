@echo off
REM usage: create_OWDDB [DBNAME] [DBDRIVE] [DBDIR] [DBUSER]
REM Creates a DB2 database named [DBNAME] under directory [DBDRIVE][DBDIR]
REM accessible by user [DBUSER].
if {%1}=={} goto usage
if {%2}=={} goto usage
if {%3}=={} goto usage
if {%4}=={} goto usage
set DBNAME=%1
set DBDRIVE=%2
set DBDIR=%3
set DBUSER=%4
REM close any outstanding connections
db2 -v CONNECT RESET
REM Go ahead and drop if it exists
db2 -v DROP DATABASE %DBNAME%
REM Create the database - these statements must be on a single line (unfortunately)
db2 -v -t "CREATE DATABASE %DBNAME% ON '%DBDRIVE%' USING CODESET UTF-8 TERRITORY US COLLATE USING SYSTEM CATALOG TABLESPACE MANAGED BY SYSTEM USING ('%DBDRIVE%%DBDIR%\%DBNAME%\sys') TEMPORARY TABLESPACE MANAGED BY SYSTEM USING ('%DBDRIVE%%DBDIR%\%DBNAME%\systmp') USER TABLESPACE MANAGED BY SYSTEM USING ('%DBDRIVE%%DBDIR%\%DBNAME%\usr')" ;
REM Increase the application and statement heap sizes
db2 -v UPDATE DATABASE CONFIGURATION FOR %DBNAME% USING APPLHEAPSZ 2560
db2 -v UPDATE DATABASE CONFIGURATION FOR %DBNAME% USING STMTHEAP 8192
REM Connect to db
db2 -v CONNECT TO %DBNAME%
REM Drop unnecessary default tablespaces
db2 -v DROP TABLESPACE USERSPACE1
REM Create default buffer pool
db2 -v CREATE Bufferpool %DBNAME%BP IMMEDIATE SIZE -1 PAGESIZE 16K
REM Create additional buffer pools
REM db2 -v CREATE Bufferpool %DBNAME%TEMPBP IMMEDIATE SIZE -1 PAGESIZE 16K
REM db2 -v CREATE Bufferpool %DBNAME%SYSBP IMMEDIATE SIZE -1 PAGESIZE 16K
db2 -v CONNECT RESET
db2 -v CONNECT TO %DBNAME%
REM Create tablespaces - these statements must be on a single line
db2 -v -t "CREATE REGULAR TABLESPACE %DBNAME% PAGESIZE 16K MANAGED BY DATABASE USING (FILE '%DBDRIVE%%DBDIR%\%DBNAME%\usr2\%DBNAME%_tbs.dbf' 64M) AUTORESIZE YES BUFFERPOOL %DBNAME%BP" ;
REM db2 -v -t "CREATE USER TEMPORARY TABLESPACE USERTEMP1 PAGESIZE 16K MANAGED BY DATABASE USING (FILE '%DBDRIVE%%DBDIR%\%DBNAME%\usrtmp\%DBNAME%_tmp.dbf' 8M) AUTORESIZE YES BUFFERPOOL %DBNAME%TEMPBP" ;
REM db2 -v -t "CREATE SYSTEM TEMPORARY TABLESPACE TEMPSYS1 PAGESIZE 16K MANAGED BY SYSTEM USING ('%DBDRIVE%%DBDIR%\%DBNAME%\systmp2') BUFFERPOOL %DBNAME%SYSBP" ;
REM Grant USER access to tablespaces
db2 -v GRANT CREATETAB,CONNECT ON DATABASE TO user %DBUSER%
db2 -v GRANT USE OF TABLESPACE %DBNAME% TO user %DBUSER%
REM db2 -v GRANT USE OF TABLESPACE USERTEMP1 TO user %DBUSER%
REM Close connection
db2 -v CONNECT RESET
goto end
:usage
@echo -----------------------------------------------------------------
@echo Usage: create_OWDDB.bat [DBNAME] [DBDRIVE] [DBDIR] [DBUSER]
@echo -----------------------------------------------------------------
@echo Parameters:
@echo [DBNAME] is the name of the database (8 characters or less)
@echo [DBDRIVE] is the drive letter followed by colon (:)
@echo [DBDIR] is an existing directory which will contain the database later
@echo [DBUSER] is a system user, who has database access
@echo -----------------------------------------------------------------
@echo WARNING: 
@echo The [DBDIR] value must NOT include the drive letter prefix and 
@echo must not have a trailing slash (/) or backslash (\). The value must 
@echo begin with a leading backslash (\).
@echo The database will be created in a sub-directory named [DBNAME].
@echo -----------------------------------------------------------------
@echo Example 1:
@echo create_OWDDB.bat OWDDB C: \DB2 db2admin
@echo This call will create a database under C:\DB2\OWDDB accessible by db2admin
@echo -----------------------------------------------------------------
@echo Example 2:
@echo create_OWDDB.bat ZIOIDB C: \DB2 db2admin
@echo This call will create a database under C:\DB2\ZIOIDB accessible by db2admin
:end