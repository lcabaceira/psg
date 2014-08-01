package com.wewebu.ow.server.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.wewebu.ow.server.exceptions.OwDataSourceException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Implements a factory for DB based writable attribute bags.<br/>
 * Retrieve a writable attribute bag with getBag(Name).<br/>
 * The factory has the advantage that all attribute bag data is loaded in one single SQL Query.<br/>
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
public class OwStandardDBAttributeBagWriteableFactory
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardDBAttributeBagWriteableFactory.class);

    /** name of the global site user attribute if no user is specified */
    private static final String SITE_USER_NAME = "OW_SITE_USER";

    /** DB attribute name of the User */
    public static final String ATTRIBUTE_INDEX = "ID";
    /** DB attribute name of the User */
    public static final String ATTRIBUTE_USER = "UserName";
    /** DB attribute name of the Name */
    public static final String ATTRIBUTE_BAGNAME = "BagName";
    /** DB attribute name of the attribute name */
    public static final String ATTRIBUTE_ATTR_NAME = "AttributeName";
    /** DB attribute name of the attribute value */
    public static final String ATTRIBUTE_ATTR_VALUE = "AttributeValue";
    /** name of the oracle sequence */
    public static final String SEQUENCE_NAME = "OW_ATTRIBUTE_BAG_SEQ";
    /** length limitation for the attribute name */
    public static final int LENGTH_LIMIT_ATTRIBUTE_NAME = 128;
    /** length limitation for the bag name */
    public static final int LENGTH_LIMIT_BAG_NAME = 128;
    /** length limitation for the attribute value */
    public static final int LENGTH_LIMIT_ATTRIBUTE_VALUE = 1024;

    /** delimiter for bag name and attribute name map key*/
    private static final String MAP_KEY_DELIMITER = ":";

    /** the database to read and write to */
    private JdbcTemplate m_jdbcTemplate;

    /** user name to map the attribute bag to a specific user. */
    private String m_user;

    /** 
     * table name 
     * @deprecated will be replaced by {@link #attributeBagTable}
     */
    private String m_table;
    OwTableSpec attributeBagTable;
    private String catalogSeparator;

    /** attributes map*/
    private Map<String, OwAttribute> m_attributes;

    /** deleted attributes */
    private List<OwAttribute> m_deletedAttributes;
    /** props for size check*/
    private int bagName, attName, attValue;

    private OwPreparedStatementCreator creator;

    /** constructs the attribute bag with the given Database
     * Use the following attribute scheme:
     * [Index, User, Name, AttributeName, AttributeValue]
     * @param jdbcTemplate_p The Spring JdbcTemplate to use for the database connectivity.<BR> 
     * @param strUser_p String optional user name to map the attribute bag to a specific user, can be null.
     * @param strTable_p String table name
     * @throws OwDataSourceException 
     * @deprecated Will be replaced by {@link #OwStandardDBAttributeBagWriteableFactory(JdbcTemplate, String, OwTableSpec)}.
     */
    public OwStandardDBAttributeBagWriteableFactory(JdbcTemplate jdbcTemplate_p, String strUser_p, String strTable_p) throws OwDataSourceException
    {
        m_jdbcTemplate = jdbcTemplate_p;
        if (m_jdbcTemplate == null)
        {
            String msg = "OwStandardDBAttributeBagWriteableFactory is not been initialized with a DB, it cannot be used,  m_jdbcTemplate == null.";
            throw new OwDataSourceException(msg);
        }
        m_table = strTable_p;
        m_user = (strUser_p == null) ? SITE_USER_NAME : strUser_p;
        m_attributes = new HashMap<String, OwAttribute>();
        m_deletedAttributes = new LinkedList<OwAttribute>();
        bagName = LENGTH_LIMIT_BAG_NAME;
        attName = LENGTH_LIMIT_ATTRIBUTE_NAME;
        attValue = LENGTH_LIMIT_ATTRIBUTE_VALUE;
    }

    /**
     * Constructs the attribute bag with the given Database
     * Use the following attribute scheme:
     * [Index, User, Name, AttributeName, AttributeValue]
     * 
     * @param jdbc The Spring JdbcTemplate to use for the database connectivity.
     * @param userID optional user name to map the attribute bag to a specific user, can be null.
     * @param attributeBagTable the table to be used for storing/reading the data.
     * @since 4.2.0.0
     */
    public OwStandardDBAttributeBagWriteableFactory(JdbcTemplate jdbc, String userID, OwTableSpec attributeBagTable) throws OwDataSourceException
    {
        this(jdbc, userID, attributeBagTable.getTableName());
        this.attributeBagTable = attributeBagTable;

        Connection connection = null;
        try
        {
            connection = m_jdbcTemplate.getDataSource().getConnection();
            this.catalogSeparator = connection.getMetaData().getCatalogSeparator();
            if (this.catalogSeparator.isEmpty())
            {
                this.catalogSeparator = ".";
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
    }

    /** Load the tuples from the DB, transform them into a local objects representation and cache them.
     *  <br />Additional parts/caches of the class are initialized also here. 
     * @throws Exception if cannot retrieve information from DB*/
    public void init() throws Exception
    {
        StringBuilder statement = new StringBuilder("select ").append(ATTRIBUTE_USER).append(",").append(ATTRIBUTE_BAGNAME).append(",");
        statement.append(ATTRIBUTE_ATTR_NAME).append(",").append(ATTRIBUTE_ATTR_VALUE).append(" from ").append(getTableName()).append(" where ");
        statement.append(ATTRIBUTE_USER).append(" = ?");
        Object[] params = new Object[] { m_user };
        m_jdbcTemplate.query(statement.toString(), params, new RowCallbackHandler() {
            public void processRow(ResultSet rs_p) throws SQLException
            {
                String attrName = rs_p.getString(ATTRIBUTE_ATTR_NAME);
                String attrValue = rs_p.getString(ATTRIBUTE_ATTR_VALUE);
                String bagName = rs_p.getString(ATTRIBUTE_BAGNAME);
                m_attributes.put(bagName + MAP_KEY_DELIMITER + attrName, new OwAttribute(OwAttribute.STATE_NORMAL, bagName, attrName, attrValue));
            }
        });
        initColumnLimit();
        creator = createStatementCreator();
    }

    /**
     * (overridable)
     * Initializing the limit of the columns, 
     * which will be used later before writing to data base.
     * @throws SQLException
     * @since 3.1.0.3
     */
    protected void initColumnLimit() throws SQLException
    {
        Connection con = null;
        try
        {
            con = m_jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData meta = con.getMetaData();

            ResultSet resultSet = null;
            if (null == this.attributeBagTable)
            {
                resultSet = meta.getColumns(null, null, getTableName(), null);
            }
            else
            {
                resultSet = meta.getColumns(this.attributeBagTable.getCatalogName(), this.attributeBagTable.getSchemaName(), this.attributeBagTable.getTableName(), null);
            }

            while (resultSet.next())
            {
                String name = resultSet.getString("COLUMN_NAME");
                if (ATTRIBUTE_BAGNAME.equalsIgnoreCase(name))
                {
                    bagName = resultSet.getInt("COLUMN_SIZE") - 1;
                }
                else if (ATTRIBUTE_ATTR_NAME.equalsIgnoreCase(name))
                {
                    attName = resultSet.getInt("COLUMN_SIZE") - 1;
                }
                else if (ATTRIBUTE_ATTR_VALUE.equalsIgnoreCase(name))
                {
                    attValue = resultSet.getInt("COLUMN_SIZE") - 1;
                }
            }
        }
        catch (SQLException e)
        {
            LOG.error("DBAttributeBag initialisation error, cannot read the DB table (" + getTableName() + ") information...", e);
            throw e;
        }
        finally
        {
            if (con != null)
            {
                con.close();
                con = null;
            }
        }
    }

    /**
     * getter of the configured Table to use.
     * @return String
     * @since 4.2.0.0
     */
    protected String getTableName()
    {
        if (null != this.attributeBagTable)
        {
            return this.attributeBagTable.fullyQualifiedName(catalogSeparator);
        }
        else
        {
            return m_table;
        }
    }

    public void setAttribute(String strName_p, Object value_p, String sBagName_p) throws Exception
    {
        // check maximum length limitations for database
        if (strName_p == null)
        {
            LOG.error("OwStandardDBAttributeBagWriteableFactory.setAttribute() invoked with setName_p==null. Attribute needs a name");
            throw new OwInvalidOperationException("OwStandardDBAttributeBagWriteableFactory.setAttribute() invoked with setName_p==null. Attribute needs a name");
        }
        if (sBagName_p == null)
        {
            LOG.error("OwStandardDBAttributeBagWriteableFactory.setAttribute() invoked with sBagName_p==null. Attribute needs a bag name");
            throw new OwInvalidOperationException("OwStandardDBAttributeBagWriteableFactory.setAttribute() invoked with sBagName_p==null. Attribute needs a bag name");
        }
        if (strName_p.length() > attName)
        {
            LOG.error("OwStandardDBAttributeBagWriteableFactory.setAttribute() setName_p is too long. Attribute name has a fixed maximum length.");
            throw new OwInvalidOperationException("OwStandardDBAttributeBagWriteableFactory.setAttribute() setName_p is too long. Attribute name has a fixed maximum length.");
        }
        if (sBagName_p.length() > bagName)
        {
            LOG.error("OwStandardDBAttributeBagWriteableFactory.setAttribute() sBagName_p is too long. Attribute bag name has a fixed maximum length.");
            throw new OwInvalidOperationException("OwStandardDBAttributeBagWriteableFactory.setAttribute() sBagName_p is too long. Attribute bag name has a fixed maximum length.");
        }
        if ((value_p != null) && (value_p.toString().length() > attValue))
        {
            LOG.error("OwStandardDBAttributeBagWriteableFactory.setAttribute() value_p is too long. Attribute value has a fixed maximum length.");
            throw new OwInvalidOperationException("OwStandardDBAttributeBagWriteableFactory.setAttribute() value_p is too long. Attribute value has a fixed maximum length.");
        }
        // set the attribute
        OwAttribute attr = m_attributes.get(sBagName_p + MAP_KEY_DELIMITER + strName_p);
        if (null != attr)
        {
            attr.setValue(value_p);
        }
        else
        {
            m_attributes.put(sBagName_p + MAP_KEY_DELIMITER + strName_p, new OwAttribute(OwAttribute.STATE_NEW, sBagName_p, strName_p, value_p));
        }
    }

    /** parse a map key and get the attribute name */
    private String getAttributeNameFromKey(String sKey_p)
    {
        int iDel = sKey_p.indexOf(MAP_KEY_DELIMITER);
        return sKey_p.substring(iDel + 1);
    }

    public void save(String strBagName_p) throws Exception
    {
        // iterate over all attributes and check if they changes waiting to be stored
        Iterator<OwAttribute> it = m_attributes.values().iterator();
        while (it.hasNext())
        {
            // get attribute
            final OwAttribute attr = it.next();
            // check if it belongs to the bag we want to save
            if (!attr.getBagname().equals(strBagName_p))
            {
                continue;
            }
            // perform action depending on state
            if (OwAttribute.STATE_NORMAL != attr.getState())
            {
                try
                {
                    creator.setAttribute(attr);
                    m_jdbcTemplate.update(creator);
                }
                catch (Exception ex)
                {
                    LOG.error("Error executing. sql: " + creator.getSQL(), ex);
                    String[] params = creator.getParams();
                    for (int i = 0; i < params.length; i++)
                    {
                        LOG.error("param[" + i + "]: " + params[i].toString());
                    }
                }
            }

            //signal that value is saved
            attr.saved();
        }
        //remove deleted attributes from memory - bug 3065
        m_attributes.values().removeAll(m_deletedAttributes);
        m_deletedAttributes.clear();
    }

    public Object getAttribute(int iIndex_p, String strBagName_p) throws Exception
    {
        throw new OwInvalidOperationException("OwStandardDBAttributeBagWriteableFactory.getAttribute(int iIndex_p, String strBagName_p): not implemented.");
    }

    public Object getAttribute(String strName_p, String strBagName_p) throws Exception
    {
        OwAttribute ret = m_attributes.get(strBagName_p + MAP_KEY_DELIMITER + strName_p);

        if ((ret == null) || ret.isDeleted())
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwStandardDBAttributeBagWriteableFactory.getAttribute: Object not found Exception, strName_p = " + strName_p + ", strBagName_p = " + strBagName_p);
            }
            throw new OwObjectNotFoundException("OwStandardDBAttributeBagWriteableFactory.getAttribute: strName_p = " + strName_p + ", strBagName_p = " + strBagName_p);
        }

        return ret.getValue();
    }

    /** get the attribute with the given name, returns default if not found. */
    public Object getSafeAttribute(String strName_p, Object default_p, String strBagName_p)
    {
        OwAttribute ret = m_attributes.get(strBagName_p + MAP_KEY_DELIMITER + strName_p);

        if ((ret == null) || ret.isDeleted())
        {
            return default_p;
        }
        else
        {
            return ret.getValue();
        }
    }

    public boolean hasAttribute(String strName_p, String strBagName_p)
    {
        OwAttribute ret = m_attributes.get(strBagName_p + MAP_KEY_DELIMITER + strName_p);
        if ((ret == null) || ret.isDeleted())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public int attributecount(String strBagName_p)
    {
        Iterator<OwAttribute> it = m_attributes.values().iterator();
        // count the attributes with matching bag names 
        int iCount = 0;
        while (it.hasNext())
        {
            OwAttribute value = it.next();

            if (value.getBagname().equals(strBagName_p) && (!value.isDeleted()))
            {
                iCount++;
            }
        }

        return iCount;
    }

    @SuppressWarnings("rawtypes")
    public Collection getAttributeNames(String strBagName_p)
    {
        LinkedList<String> ret = new LinkedList<String>();

        Iterator<Map.Entry<String, OwAttribute>> it = m_attributes.entrySet().iterator();

        // get the attributes names with matching bag names 
        while (it.hasNext())
        {
            Map.Entry<String, OwAttribute> entry = it.next();
            String strName = getAttributeNameFromKey(entry.getKey());

            if (entry.getValue().getBagname().equals(strBagName_p) && (!entry.getValue().isDeleted()))
            {
                ret.add(strName);
            }
        }

        return ret;
    }

    /** get a specific attribute bag
     * 
     * @param sBagName_p String name that identifies the attribute bag
     * 
     * @return OwStandardDBAttributeBagWriteable instance
     * @throws Exception 
     */
    public OwAttributeBagWriteable getBag(String sBagName_p) throws Exception
    {
        if (sBagName_p.indexOf(MAP_KEY_DELIMITER) != -1)
        {
            String msg = "OwStandardDBAttributeBagWriteableFactory.getBag: Bagname not allowed, name = " + sBagName_p + ", remove [" + MAP_KEY_DELIMITER + "].";
            LOG.error(msg);
            throw new OwInvalidOperationException(msg);
        }

        return new OwStandardDBAttributeBagWriteable(this, sBagName_p);
    }

    public void clear(String strBagName_p)
    {
        Iterator<Map.Entry<String, OwAttribute>> it = m_attributes.entrySet().iterator();

        // get the attributes names with matching bag names 
        while (it.hasNext())
        {
            Map.Entry<String, OwAttribute> entry = it.next();

            if (entry.getValue().getBagname().equals(strBagName_p))
            {
                if (entry.getValue().isNew())
                {
                    it.remove();
                }
                else
                {
                    entry.getValue().delete();
                }
            }
        }
    }

    public void remove(String strName_p, String strBagName_p)
    {
        OwAttribute value = m_attributes.get(strBagName_p + MAP_KEY_DELIMITER + strName_p);
        if (value != null)
        {
            if (value.isNew())
            {
                m_attributes.remove(strBagName_p + MAP_KEY_DELIMITER + strName_p);
            }
            else
            {
                value.delete();
            }
        }
    }

    /**
     * Create a class which is responsible for prepared statement creation,
     * and also OwAttribute based SQL creation.
     * @return OwPreparedStatementCreator
     * @since 3.1.0.3
     */
    protected OwPreparedStatementCreator createStatementCreator()
    {
        return new OwPreparedStatementCreator();
    }

    /** a attribute value that changed, see getChangedAttributeMap() */
    private class OwAttribute
    {
        /** value is updated */
        public static final int STATE_NORMAL = 0;
        /** value is new */
        public static final int STATE_NEW = 1;
        /** value changed */
        public static final int STATE_MODIFIED = 2;
        /** value changed */
        public static final int STATE_DELETED = 3;

        /** name that identifies the attribute bag */
        private String m_bagname;
        /** name that identifies the attribute */
        private String m_name;
        /** value */
        private Object m_value;
        /** true = value existed already, false = value is new */
        private int m_iState;

        /** construct a attribute value that changed */
        public OwAttribute(int state_p, String bagname_p, String name_p, Object value_p)
        {
            m_bagname = bagname_p;
            m_name = name_p;
            m_iState = state_p;
            m_value = value_p;
        }

        /** get the state of this attribute
         * @return the state
         */
        public int getState()
        {
            return m_iState;
        }

        /** true = value existed already, false = value is new */
        public boolean isNew()
        {
            return m_iState == STATE_NEW;
        }

        /** true = modified, false = value is up to date */
        public boolean isModified()
        {
            return m_iState == STATE_MODIFIED;
        }

        /** true = value is deleted, false = value is not deleted */
        public boolean isDeleted()
        {
            return m_iState == STATE_DELETED;
        }

        /** mark the attribute deleted */
        public void delete()
        {
            m_iState = STATE_DELETED;
        }

        /** value */
        public void setValue(Object value_p)
        {
            if (m_value == null)
            {
                if (value_p == null)
                {
                    return; // nothing changed
                }
            }
            else
            {
                if (m_value.equals(value_p))
                {
                    return; // nothing changed
                }
            }

            m_value = value_p;

            if (m_iState != STATE_NEW)
            {
                m_iState = STATE_MODIFIED;
            }
        }

        /** value */
        public Object getValue()
        {
            return m_value;
        }

        /** name that identifies the attribute */
        public String getName()
        {
            return m_name;
        }

        /** name that identifies the attribute bag the value belongs to */
        public String getBagname()
        {
            return m_bagname;
        }

        /** signal that value is now saved */
        public void saved()
        {
            if (m_iState == STATE_DELETED)
            {
                m_deletedAttributes.add(this);
            }
            else
            {
                m_iState = STATE_NORMAL;
            }
        }
    }

    /**
     *<p>
     * Special prepares statement creator, which will work depending the provided OwAttribute instance.
     * Implements a simple prepared statement cache for better performance.
     *</p>
     *@since 3.1.0.3
     */
    private class OwPreparedStatementCreator implements PreparedStatementCreator
    {
        private OwAttribute attr;
        private HashMap<Integer, String> sqlStmtCache;

        public OwPreparedStatementCreator()
        {
            sqlStmtCache = new HashMap<Integer, String>();
        }

        public PreparedStatement createPreparedStatement(Connection con) throws SQLException
        {
            String sql = sqlStmtCache.get(Integer.valueOf(attr.getState()));
            if (sql == null)
            {
                sql = getSQL();
                sqlStmtCache.put(Integer.valueOf(attr.getState()), sql);
            }

            PreparedStatement stmt = con.prepareStatement(sql);

            String[] params = getParams();
            for (int i = 0; i < params.length; i++)
            {
                stmt.setString(i + 1, params[i]);
            }
            return stmt;
        }

        /**
         * Set the OwAttribute which will be used
         * for next processing.
         * @param attr OwAttribute
         */
        public void setAttribute(OwAttribute attr)
        {
            this.attr = attr;
        }

        /**
         * Get the SQL statement to be execute, regarding
         * provided OwAttribute instance.
         * @return String SQL statement, or null if OwAttribute is null
         */
        public String getSQL()
        {
            String sql = null;
            if (attr != null)
            {
                switch (attr.getState())
                {
                    case OwAttribute.STATE_NEW:
                        sql = "insert into " + getTableName() + " (" + ATTRIBUTE_USER + "," + ATTRIBUTE_BAGNAME + "," + ATTRIBUTE_ATTR_NAME + "," + ATTRIBUTE_ATTR_VALUE + ") values (?,?,?,?)";
                        break;
                    case OwAttribute.STATE_MODIFIED:
                        sql = "update " + getTableName() + " set " + ATTRIBUTE_ATTR_VALUE + " = ? where " + ATTRIBUTE_ATTR_NAME + " = ? and " + ATTRIBUTE_BAGNAME + " = ? and " + ATTRIBUTE_USER + " = ?";
                        break;
                    case OwAttribute.STATE_DELETED:
                        sql = "delete from " + getTableName() + " where " + ATTRIBUTE_ATTR_NAME + " = ? and " + ATTRIBUTE_BAGNAME + " = ? and " + ATTRIBUTE_USER + " = ?";
                        break;
                }
            }
            return sql;
        }

        /**
         * Get the parameters which should be used for the
         * SQL execution.
         * @return String array, or null if OwAttribute is null
         */
        public String[] getParams()
        {
            String[] params = null;
            if (attr != null)
            {
                switch (attr.getState())
                {
                    case OwAttribute.STATE_NEW:
                        params = new String[] { m_user, attr.getBagname(), attr.getName(), (attr.getValue() != null ? attr.getValue().toString() : null) };
                        break;
                    case OwAttribute.STATE_MODIFIED:
                        params = new String[] { (attr.getValue() != null ? attr.getValue().toString() : null), attr.getName(), attr.getBagname(), m_user };
                        break;
                    case OwAttribute.STATE_DELETED:
                        params = new String[] { this.attr.getName(), attr.getBagname(), m_user };
                        break;
                }
            }
            return params;
        }

    }

}