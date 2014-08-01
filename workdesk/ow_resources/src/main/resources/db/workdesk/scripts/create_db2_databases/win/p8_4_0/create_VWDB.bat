@echo off
REM usage: create_VWDB [DBNAME] [DBDRIVE] [DBDIR] [DBUSER]
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
db2 -v CREATE Bufferpool %DBNAME%BP IMMEDIATE SIZE -1 PAGESIZE 8K
REM Create additional buffer pools
db2 -v CREATE Bufferpool %DBNAME%TEMPBP IMMEDIATE SIZE -1 PAGESIZE 8K
db2 -v CREATE Bufferpool %DBNAME%SYSBP IMMEDIATE SIZE -1 PAGESIZE 8K
db2 -v CONNECT RESET
db2 -v CONNECT TO %DBNAME%
REM Create tablespaces - these statements must be on a single line
db2 -v -t "CREATE REGULAR TABLESPACE %DBNAME% PAGESIZE 8K MANAGED BY DATABASE USING (FILE '%DBDRIVE%%DBDIR%\%DBNAME%\usr2\%DBNAME%_tbs.dbf' 200M) AUTORESIZE YES BUFFERPOOL %DBNAME%BP" ;
db2 -v -t "CREATE USER TEMPORARY TABLESPACE USERTEMP1 PAGESIZE 8K MANAGED BY DATABASE USING (FILE '%DBDRIVE%%DBDIR%\%DBNAME%\usrtmp\%DBNAME%_tmp.dbf' 40M) AUTORESIZE YES BUFFERPOOL %DBNAME%TEMPBP" ;
db2 -v -t "CREATE SYSTEM TEMPORARY TABLESPACE TEMPSYS1 PAGESIZE 8K MANAGED BY SYSTEM USING ('%DBDRIVE%%DBDIR%\%DBNAME%\systmp2') BUFFERPOOL %DBNAME%SYSBP" ;
REM Grant USER access to tablespaces
db2 -v GRANT CREATETAB,CONNECT ON DATABASE TO user %DBUSER%
db2 -v GRANT USE OF TABLESPACE %DBNAME% TO user %DBUSER%
db2 -v GRANT USE OF TABLESPACE USERTEMP1 TO user %DBUSER%
REM Close connection
db2 -v CONNECT RESET
goto end
:usage
@echo usage: create_VWDB [DBNAME] [DBDRIVE] [DBDIR] [DBUSER]
@echo where:
@echo [DBNAME] is the name of the database (8 characters or less)
@echo [DBDRIVE] is the windows driver letter followed by colon (:)
@echo [DBDIR] is the existing directory that will contain the database.
@echo This value must NOT include the drive prefix nor a trailing slash.
@echo The database will be created in a sub-directory named [DBNAME].
@echo [DBUSER] is a system user in which database access is provided
@echo example:
@echo create_VWDB VWDB C: \DB2 db2admin
@echo Creates a database under C:\DB2\VWDB accessible by db2admin
:end