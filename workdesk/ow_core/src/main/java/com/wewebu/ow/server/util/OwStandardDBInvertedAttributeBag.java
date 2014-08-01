package com.wewebu.ow.server.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.wewebu.ow.server.exceptions.OwDataSourceException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 * Implements a DB based read only attribute bag the inverts user- and attribute names.<br/>
 * Uses the following attribute scheme to access the database e.g.:<br/><br/>
 * <table border='1'>
 *  <tr>
 *   <td>(Number)</td><td><b>User</b> (String)</td><td><b>Name</b> (String)</td><td><b>AttributeName</b> (String)</td><td><b>AttributeValue</b> (String)</td>
 *  </tr>
 *  <tr>    
 *   <td>1</td><td>OW_SITE_USER</td><td>ViewerSize</td><td>X</td><td>100</td>
 *  </tr>
 *  <tr>
 *   <td>2</td><td>OW_SITE_USER</td><td>ViewerSize</td><td>Y</td><td>100</td>
 *  </tr>
 *  <tr>
 *   <td>3</td><td>UserName</td><td>SelectedClass</td><td>Account</td><td>1</td>
 *  </tr>
 *  <tr>
 *   <td>4</td><td>UserName</td><td>SelectedClass</td><td>Credit</td><td>1</td>
 *  </tr>
 *  <tr>
 *   <td>5</td><td>UserName</td><td>SelectedClass</td><td>Depot</td><td>1</td>
 *  </tr>
 * </table>
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
 */
public class OwStandardDBInvertedAttributeBag implements OwAttributeBag
{

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardDBInvertedAttributeBag.class);

    /** DB attribute name of the User */
    public static final String ATTRIBUTE_USER = "UserName";
    /** DB attribute name of the Name */
    public static final String ATTRIBUTE_BAGNAME = "BagName";
    /** DB attribute name of the attribute name */
    public static final String ATTRIBUTE_ATTR_NAME = "AttributeName";
    /** DB attribute name of the attribute value */
    public static final String ATTRIBUTE_ATTR_VALUE = "AttributeValue";

    /** HashMap to store the attributes in */
    @SuppressWarnings("rawtypes")
    protected HashMap m_attributes = new HashMap();

    /**
     * 
     * @param jdbc_p
     * @param bagname_p
     * @param attributename_p
     * @param strTable_p
     * @throws OwDataSourceException
     * @deprecated Will be replaced by {@link #OwStandardDBInvertedAttributeBag(JdbcTemplate, String, String, OwTableSpec)}.
     */
    public OwStandardDBInvertedAttributeBag(JdbcTemplate jdbc_p, String bagname_p, String attributename_p, String strTable_p) throws OwDataSourceException
    {
        // sanity check jdbc_p for null
        if (jdbc_p == null)
        {
            String msg = "OwStandardDBInvertedAttributeBag is not been initialized with a DB, it cannot be used, jdbc_p == null.";
            throw new OwDataSourceException(msg);
        }
        // read attributes
        String statement = "select " + ATTRIBUTE_USER + "," + ATTRIBUTE_ATTR_VALUE + " from " + strTable_p + " where (" + ATTRIBUTE_BAGNAME + " = ?) and (" + ATTRIBUTE_ATTR_NAME + " = ?)";
        Object[] params = new Object[] { bagname_p, attributename_p };
        jdbc_p.query(statement, params, new RowCallbackHandler() {
            public void processRow(ResultSet rs_p) throws SQLException
            {
                String attributeUser = rs_p.getString(ATTRIBUTE_USER);
                String attributeValue = rs_p.getString(ATTRIBUTE_ATTR_VALUE);
                m_attributes.put(attributeUser, attributeValue);
            }
        });
    }

    /**
     * @param jdbc_p
     * @param bagname_p
     * @param attributename_p
     * @param attributeBagTable
     * @throws OwDataSourceException 
     * @since 4.2.0.0
     */
    public OwStandardDBInvertedAttributeBag(JdbcTemplate jdbc_p, String bagname_p, String attributename_p, OwTableSpec attributeBagTable) throws OwDataSourceException
    {
        // sanity check jdbc_p for null
        if (jdbc_p == null)
        {
            String msg = "OwStandardDBInvertedAttributeBag is not been initialized with a DB, it cannot be used, jdbc_p == null.";
            throw new OwDataSourceException(msg);
        }

        Connection connection = null;
        String catalogSeparator = null;
        try
        {
            connection = jdbc_p.getDataSource().getConnection();
            catalogSeparator = connection.getMetaData().getCatalogSeparator();
            if (catalogSeparator.isEmpty())
            {
                catalogSeparator = ".";
            }
        }
        catch (SQLException e)
        {
            throw new OwDataSourceException("DBAttributeBag initialisation error, cannot read the DB metadata.", e);
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    LOG.error("Could not close connection.", e);
                }
                connection = null;
            }
        }

        // read attributes
        String fullyQualifiedName = attributeBagTable.fullyQualifiedName(catalogSeparator);
        String statement = "select " + ATTRIBUTE_USER + "," + ATTRIBUTE_ATTR_VALUE + " from " + fullyQualifiedName + " where (" + ATTRIBUTE_BAGNAME + " = ?) and (" + ATTRIBUTE_ATTR_NAME + " = ?)";
        Object[] params = new Object[] { bagname_p, attributename_p };
        jdbc_p.query(statement, params, new RowCallbackHandler() {
            @SuppressWarnings("unchecked")
            public void processRow(ResultSet rs_p) throws SQLException
            {
                String attributeUser = rs_p.getString(ATTRIBUTE_USER);
                String attributeValue = rs_p.getString(ATTRIBUTE_ATTR_VALUE);
                m_attributes.put(attributeUser, attributeValue);
            }
        });

    }

    public int attributecount()
    {
        return (m_attributes.size());
    }

    public Object getAttribute(int index_p) throws Exception
    {
        throw new OwInvalidOperationException("OwStandardDBInvertedAttributeBag.getAttribute(int iIndex_p): not implemented.");
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        if (!m_attributes.containsKey(strName_p))
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwStandardDBAttributeBagWriteableFactory.getAttribute: Object not found Exception, strName_p = " + strName_p);
            }
            throw new OwObjectNotFoundException("OwStandardDBInvertedAttributeBag.getAttribute: strName_p = " + strName_p);
        }
        return (m_attributes.get(strName_p));
    }

    public Collection getAttributeNames()
    {
        return (m_attributes.keySet());
    }

    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        if (!m_attributes.containsKey(strName_p))
        {
            return (default_p);
        }
        return (m_attributes.get(strName_p));
    }

    public boolean hasAttribute(String strName_p)
    {
        return (m_attributes.containsKey(strName_p));
    }

}