package com.wewebu.ow.csqlc;

import java.util.Set;

import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * SQL entities resolver interface. Used to obtain external SQL information like 
 * repository IDs for certain repository names and query names of certain columns and classes.<br/>
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 3.2.0.0
 */
public interface OwSQLEntitiesResolver
{
    boolean canOrderBy(String tableName_p, String columnName_p, String repositoryID_p) throws OwException;

    boolean isVersionable(String tableName_p, String repositoryID_p) throws OwException;

    /**
     * Get a query name for column, which is maybe escaped or transformed into
     * @param tableName_p String context table/type
     * @param columnName_p String defined name or id to resolve
     * @param repositoryID_p String repository/object store which will be queried
     * @return String query-name for column
     * @throws OwException
     * @since 4.1.0.0
     */
    String resovleQueryColumnName(String tableName_p, String columnName_p, String repositoryID_p) throws OwException;

    String resolveQueryFolderId(String resourceID_p, String path_p) throws OwException;

    Set<String> resolveQueryableColumnNames(String tableName_p, String repositoryID_p) throws OwException;

    String resolveQueryTableName(String tableName_p, String repositoryID_p) throws OwException;

    String resolveDefaultRepositoryID() throws OwException;

    String resolveRepositoryID(String repositoryName_p) throws OwException;

    boolean isInternalRepositoryID(String repositoryID_p) throws OwException;

    boolean isSubtable(String parentTable_p, String childTable_p, String repositoryID_p) throws OwException;

    /**
     * Network context which is used for additional information.
     * @return OwNetworkContext
     */
    OwNetworkContext getNetworkContext();
}
