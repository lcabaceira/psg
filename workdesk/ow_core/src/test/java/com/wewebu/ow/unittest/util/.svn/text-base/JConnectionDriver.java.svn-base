package com.wewebu.ow.unittest.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *<p>
 * Implements the java.sql.Driver interface, 
 * which provides methods to load drivers and create new database connections. 
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
public class JConnectionDriver implements Driver
{
    private ConnectionPool m_pool;

    public JConnectionDriver(String driver_p, String url_p, String user_p, String password_p) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        //Register driver 
        DriverManager.registerDriver(this);
        Class.forName(driver_p);
        m_pool = new ConnectionPool(url_p, user_p, password_p);
    }

    public Connection connect(String url_p, Properties props_p) throws SQLException
    {
        return m_pool.getConnection();
    }

    public boolean acceptsURL(String url_p)
    {
        return true;
    }

    public DriverPropertyInfo[] getPropertyInfo(String str_p, Properties props_p)
    {

        return new DriverPropertyInfo[0];
    }

    public boolean jdbcCompliant()
    {
        return false;
    }

    public int getMajorVersion()
    {

        return 0;
    }

    public int getMinorVersion()
    {
        return 0;
    }

    /*========================== Connection 1.7 JDK methods=============================*/
    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        // TODO Auto-generated method stub
        return null;
    }
    /*========================== Connection 1.7 JDK methods=============================*/
}
