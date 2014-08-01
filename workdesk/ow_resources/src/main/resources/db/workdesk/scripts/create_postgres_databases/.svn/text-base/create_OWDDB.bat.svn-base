set PGPASSWORD=admin
psql -d postgres -U postgres -w -f Create_PostgreSQL_Database.sql
set PGPASSWORD=owdemo
psql -d owd -U owdemo -w  -f OwPostgreSQL_CreateAttributeBagTable.sql
psql -d owd -U owdemo -w  -f OwPostgreSQL_CreateDBHistoryManagerTable.sql
psql -d owd -U owdemo -w  -f OwPostgreSQL_CreateDBRoleManagerTable.sql