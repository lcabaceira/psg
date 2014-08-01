package com.wewebu.ow.unittest.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwTestDataSource.
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
public class OwTestDataSource implements DataSource
{
    private String m_sURL;
    private String m_sUser;
    private String m_sPassword;
    private String m_sDriverClassName;
    // DB Connection Pool  
    private JConnectionDriver m_Driver = null;

    //    private synchronized Connection getPooledConnection(String sUser_p, String sPassword_p) throws SQLException
    //    {
    //        if (m_Driver == null)
    //        {
    //            try
    //            {
    //                m_Driver = new JConnectionDriver(m_sDriverClassName, m_sURL, sUser_p, sPassword_p);
    //            }
    //            catch (Exception e)
    //            {
    //            }
    //        }
    //
    //
    //        Properties emptyProperties = new Properties();
    //        return m_Driver.connect(m_sURL, emptyProperties);
    //    }

    public OwTestDataSource(OwXMLUtil config_p) throws Exception
    {
        // get properties
        m_sURL = config_p.getSafeTextValue("URL", null);
        m_sUser = config_p.getSafeTextValue("User", null);
        m_sPassword = config_p.getSafeTextValue("Password", null);
        m_sDriverClassName = config_p.getSafeTextValue("DriverClassName", null);
        if (m_sDriverClassName == null)
        {
            throw new Exception("OwTestDataSource - DriverClassName is required but not given in XML configuration file.");
        }
        // load driver
        Class.forName(m_sDriverClassName);
    }

    public Connection getConnection() throws SQLException
    {
        return getConnection(m_sUser, m_sPassword);
    }

    public Connection getConnection(String username_p, String password_p) throws SQLException
    {
        //        return getPooledConnection(username_p, password_p);
        return DriverManager.getConnection(m_sURL, username_p, password_p);
    }

    public PrintWriter getLogWriter() throws SQLException
    {
        return new PrintWriter(System.out);
    }

    public void setLogWriter(PrintWriter out) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setLoginTimeout(int seconds) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public int getLoginTimeout() throws SQLException
    {

        return 1000;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*========================== Connection 1.7 JDK methods=============================*/
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

}
