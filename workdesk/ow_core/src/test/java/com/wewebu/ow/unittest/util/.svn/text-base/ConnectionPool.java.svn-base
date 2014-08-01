package com.wewebu.ow.unittest.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 *<p>
 * Test configuration for adapters and managers <br>
 * The base context keeps basic configuration information and is independent to the web context.<br>
 * <br>
 * ConnectionReaper class decides a connection is dead if the following conditions are met.<br>
 *              -The connection is flagged as being in use.<br>
 *              -The connection is older than a preset connection time out.<br>
 *              -The connection fails a validation check. <br>
 *
 *</p>
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
class ConnectionReaper extends Thread
{

    private ConnectionPool m_pool;
    private static final long DELAY = 300000;

    ConnectionReaper(ConnectionPool pool_p)
    {
        this.m_pool = pool_p;
    }

    public void run()
    {
        while (true)
        {
            try
            {
                sleep(DELAY);
            }
            catch (InterruptedException e)
            {
            }
            m_pool.reapConnections();
        }
    }
}

/**
 * The JDCConnectionPool class makes connections available to calling program
 * in its getConnection method. This method searches for an available connection 
 * in the connection pool. If no connection is available from the pool, 
 * a new connection is created. If a connection is available from the pool, 
 * the getConnection method leases the connection and returns it to the calling 
 * program. 
 */
public class ConnectionPool
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(ConnectionPool.class);

    private Vector m_connections;
    private String m_url, m_user, m_password;
    final private long m_timeout = 60000;
    private ConnectionReaper m_reaper;
    final private int m_poolsize = 10;

    /**
     * 
     * @param url_p
     * @param user_p
     * @param password_p
     */
    public ConnectionPool(String url_p, String user_p, String password_p)
    {
        this.m_url = url_p;
        this.m_user = user_p;
        this.m_password = password_p;
        m_connections = new Vector(m_poolsize);
        m_reaper = new ConnectionReaper(this);
        m_reaper.start();
    }

    /**
     * 
     */
    public synchronized void reapConnections()
    {
        long stale = System.currentTimeMillis() - m_timeout;
        Enumeration connlist = m_connections.elements();
        while ((connlist != null) && (connlist.hasMoreElements()))
        {
            JConnection conn = (JConnection) connlist.nextElement();
            if ((conn.inUse()) && (stale > conn.getLastUse()) && (!conn.validate()))
            {
                removeConnection(conn);
            }
        }
    }

    public synchronized void closeConnections()
    {
        Enumeration connlist = m_connections.elements();
        while ((connlist != null) && (connlist.hasMoreElements()))
        {
            JConnection conn = (JConnection) connlist.nextElement();
            removeConnection(conn);
        }
    }

    private synchronized void removeConnection(JConnection conn_p)
    {
        m_connections.removeElement(conn_p);
    }

    public synchronized Connection getConnection() throws SQLException
    {
        JConnection c;
        for (int i = 0; i < m_connections.size(); i++)
        {
            LOG.debug("Get connection from connection pool");
            c = (JConnection) m_connections.elementAt(i);
            if (c.lease())
            {
                return c;
            }
        }
        LOG.debug("Create new connection");
        Connection conn = DriverManager.getConnection(m_url, m_user, m_password);
        c = new JConnection(conn, this);
        c.lease();
        m_connections.addElement(c);
        return c;
    }

    public synchronized void returnConnection(JConnection conn_p)
    {
        conn_p.expireLease();
    }
}
