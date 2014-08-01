Alfresco Workdesk Contract Management Solution Template
Copyright (c) Alfresco Software, Inc. All Rights Reserved.

******************************************************************
* WARNING: these files works only with PostgreSQL Database       *
******************************************************************

Copy all PostgreSQL Scripts and the batch file into a folder
> /db/scripts/contractmanagement/insert_ContractManagementData.bat
> /db/scripts/contractmanagement/OwPostgreSQL_InsertContractManagementUserRoles.sql
> /db/scripts/contractmanagement/OwPostgreSQL_CreateDBLookupTable.sql
> /db/scripts/contractmanagement/OwPostgreSQL_InsertDependentComboboxLookup.sql

Add PostgreSQL\bin folder to your PATH environment variable if necessary.

Execute the insert_ContactManagementData.bat file.

*Note: please check the used username/password (right now: owd/owdemo) in the
insert_ContractManagementData.bat

User roles and plugin access rights:
- OwPostgreSQL_InsertContractManagementUserRoles.sql

Depended choicelist sample (dynamic.OwDependentComboboxFieldControl):
- OwPostgreSQL_CreateDBLookupTable.sql
- OwPostgreSQL_InsertDependentComboboxLookup.sql

************************************
* Required PostgreSQL JDBC Driver: *
************************************

The required PostgreSQL JDBC Driver can be found here:
> /db/workdesk/lib/postgresql-9.1-902.jdbc4.jar