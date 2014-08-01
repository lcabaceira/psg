package com.wewebu.ow.unittest.util;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

/**
 *<p>
 * Class represents a JDBC connection in the connection pool, 
 * and is essentially a wrapper around a real JDBC connection
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
public class JConnection implements Connection
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(JConnection.class);
    private ConnectionPool m_pool;
    private Connection m_conn;
    private boolean m_inuse;
    private long m_timestamp;

    /**
     * @param conn_p
     * @param pool_p
     */
    public JConnection(Connection conn_p, ConnectionPool pool_p)
    {
        this.m_conn = conn_p;
        this.m_pool = pool_p;
        this.m_inuse = false;
        this.m_timestamp = 0;
    }

    /**
     * 
     */
    public synchronized boolean lease()
    {
        if (m_inuse)
        {
            return false;
        }
        else
        {
            m_inuse = true;
            m_timestamp = System.currentTimeMillis();
            return true;
        }
    }

    public boolean validate()
    {
        try
        {
            m_conn.getMetaData();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public boolean inUse()
    {
        return m_inuse;
    }

    public long getLastUse()
    {
        return m_timestamp;
    }

    public void close() throws SQLException
    {
        //The connection is returned to the connection pool when the calling program 
        //calls the JDCConnection.close method in its finally clause. 
        m_pool.returnConnection(this);
    }

    protected void expireLease()
    {
        m_inuse = false;
    }

    protected Connection getConnection()
    {
        return m_conn;
    }

    public PreparedStatement prepareStatement(String sql_p) throws SQLException
    {
        return m_conn.prepareStatement(sql_p);
    }

    public CallableStatement prepareCall(String sql_p) throws SQLException
    {
        return m_conn.prepareCall(sql_p);
    }

    public Statement createStatement() throws SQLException
    {
        return m_conn.createStatement();
    }

    public String nativeSQL(String sql_p) throws SQLException
    {
        return m_conn.nativeSQL(sql_p);
    }

    public void setAutoCommit(boolean autoCommit_p) throws SQLException
    {
        m_conn.setAutoCommit(autoCommit_p);
    }

    public boolean getAutoCommit() throws SQLException
    {
        return m_conn.getAutoCommit();
    }

    public void commit() throws SQLException
    {
        m_conn.commit();
    }

    public void rollback() throws SQLException
    {
        m_conn.rollback();
    }

    public boolean isClosed() throws SQLException
    {
        return m_conn.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException
    {
        return m_conn.getMetaData();
    }

    public void setReadOnly(boolean readOnly_p) throws SQLException
    {
        m_conn.setReadOnly(readOnly_p);
    }

    public boolean isReadOnly() throws SQLException
    {
        return m_conn.isReadOnly();
    }

    public void setCatalog(String catalog_p) throws SQLException
    {
        m_conn.setCatalog(catalog_p);
    }

    public String getCatalog() throws SQLException
    {
        return m_conn.getCatalog();
    }

    public void setTransactionIsolation(int level_p) throws SQLException
    {
        m_conn.setTransactionIsolation(level_p);
    }

    public int getTransactionIsolation() throws SQLException
    {
        return m_conn.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException
    {
        return m_conn.getWarnings();
    }

    public void clearWarnings() throws SQLException
    {
        m_conn.clearWarnings();
    }

    public Statement createStatement(int resultSetType_p, int resultSetConcurrency_p) throws SQLException
    {

        return null;
    }

    public Statement createStatement(int resultSetType_p, int resultSetConcurrency_p, int resultSetHoldability_p) throws SQLException
    {

        return null;
    }

    public int getHoldability() throws SQLException
    {

        return 0;
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException
    {

        return null;
    }

    public CallableStatement prepareCall(String sql_p, int resultSetType_p, int resultSetConcurrency_p) throws SQLException
    {

        return null;
    }

    public CallableStatement prepareCall(String sql_p, int resultSetType_p, int resultSetConcurrency_p, int resultSetHoldability_p) throws SQLException
    {

        return null;
    }

    public PreparedStatement prepareStatement(String sql_p, int autoGeneratedKeys_p) throws SQLException
    {

        return null;
    }

    public PreparedStatement prepareStatement(String sql_p, int[] columnIndexes_p) throws SQLException
    {

        return null;
    }

    public PreparedStatement prepareStatement(String sql_p, String[] columnNames_p) throws SQLException
    {

        return null;
    }

    public PreparedStatement prepareStatement(String sql_p, int resultSetType_p, int resultSetConcurrency_p) throws SQLException
    {

        return null;
    }

    public PreparedStatement prepareStatement(String sql_p, int resultSetType_p, int resultSetConcurrency_p, int resultSetHoldability_p) throws SQLException
    {

        return null;
    }

    public void releaseSavepoint(Savepoint savepoint_p) throws SQLException
    {

    }

    public void rollback(Savepoint savepoint_p) throws SQLException
    {

    }

    public void setHoldability(int holdability_p) throws SQLException
    {

    }

    public Savepoint setSavepoint() throws SQLException
    {

        return null;
    }

    public Savepoint setSavepoint(String name_p) throws SQLException
    {

        return null;
    }

    public void setTypeMap(Map<String, Class<?>> map_p) throws SQLException
    {

    }

    /*==========================Connection 1.6 JDK methods=============================*/
    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Clob createClob() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getClientInfo(String name) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException
    {
        // TODO Auto-generated method stub
    }

    /*========================== Connection 1.6 JDK methods=============================*/

    /*========================== Connection 1.7 JDK methods=============================*/
    public void setSchema(String schema) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public String getSchema() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void abort(Executor executor) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public int getNetworkTimeout() throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }
    /*========================== Connection 1.7 JDK methods=============================*/
}
