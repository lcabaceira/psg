package com.wewebu.ow.server.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.exceptions.OwDataSourceException;

/**
 *<p>
 * Simple DataSource utility class.
 * Works with JNDI to retrieve DataSource from javax.naming.InitialContext,
 * and &quot;java:comp/env&quot; context.
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
 *@since 4.2.0.0
 */
public class OwDataSourceUtil
{

    /**
     * Helper method to retrieve DataSource definition by JNDI lookup.
     * @param dataSourceName
     * @return DataSource
     * @throws OwDataSourceException
     */
    public static DataSource retrieveDataSource(String dataSourceName) throws OwDataSourceException
    {
        try
        {
            InitialContext ic = new InitialContext();

            try
            {
                Context envCtx = (Context) ic.lookup("java:comp/env");
                // Look up our data source
                return (DataSource) envCtx.lookup(dataSourceName);
            }
            catch (NameNotFoundException e)
            {
                // also check java-Namespace
                return (DataSource) ic.lookup(dataSourceName);
            }
        }
        catch (NamingException ex)
        {
            String msg = "OwDataSourceUtil.retrieveDataSource: Could not retrieve DataSource by name/id=" + dataSourceName;
            throw new OwDataSourceException(msg, ex);
        }
    }

    /**
     * Create a Spring JDBC template for provided DataSource
     * @param dataSource java.sql.DataSource
     * @return JdbcTemplate
     */
    public static JdbcTemplate createJdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Creates an Spring JDBC template instance from provided Id. 
     * @param jndiName String JNDI name to search for DataSource.
     * @return Data
     * @throws OwDataSourceException
     */
    public static JdbcTemplate createJdbcTemplateFromId(String jndiName) throws OwDataSourceException
    {
        DataSource dataSrc = retrieveDataSource(jndiName);
        return createJdbcTemplate(dataSrc);
    }
}
