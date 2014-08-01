package com.wewebu.ow.server.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Tracks the user usage.
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
public class OwUsageCounter
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwUsageCounter.class);

    /**
     *<p>
     * A usage statistic entry.
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
    public static class OwUsageState
    {
        private int m_iusercount;
        private int m_isessioncount;
        private Date m_Date;

        /** create a usage statistic entry
         * 
         * @param users_p maps users (key) to Set of sessions (value)
         * @param sessions_p maps sessions (key) to users (value)
         */
        public OwUsageState(Map users_p, Map sessions_p, Date date_p)
        {
            m_iusercount = users_p.size();
            m_isessioncount = sessions_p.size();
            m_Date = date_p;
        }

        /** get the user count 
         * 
         * @return a {@link Date}
         */
        public Date getDate()
        {
            return m_Date;
        }

        /** get the user count 
         * 
         * @return an int
         */
        public int getUserCount()
        {
            return m_iusercount;
        }

        /** get the session count
         * 
         * @return an int
         */
        public int getSessionCount()
        {
            return m_isessioncount;
        }
    }

    /** the interval time in milliseconds for onLoggerInterval
     * @see #onLoggerInterval
     * */
    private static final int INTERVAL_TIME = (1 * 60 * 1000);

    private static final int MAX_24H_STAT_ENTRIES = ((24 * 3600 * 1000) / INTERVAL_TIME);

    /** we have a one (users) to many (sessions) releationship 
     * maps sessions (key) to users (value) */
    private static volatile Map m_sessions = new Hashtable();

    /** we have a one (users) to many (sessions) releationship
     * maps users (key) to Set of sessions (value) */
    private static volatile Map m_users = new Hashtable();

    /** collection of OwUsageState statistic information collected in 24 hours */
    private static volatile ArrayList m_24hstats = new ArrayList();

    /** last interval time to create interval events */
    private static volatile Date m_lastinterval = new Date();

    /** called when a user logs in
     * 
     * @param sessionid_p
     * @param userid_p
     */
    public static void onLogin(String sessionid_p, OwBaseUserInfo userid_p)
    {
        String userid = userid_p.getUserID();

        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwUsageCounter.onLogin: userid=" + userid);
        }

        // add user
        m_sessions.put(sessionid_p, userid);

        // add sessions
        Set sessions = (Set) m_users.get(userid);
        if (sessions == null)
        {
            sessions = new HashSet();
            m_users.put(userid, sessions);
        }

        sessions.add(sessionid_p);

        // create interval
        Date currentDate = new Date();
        if ((INTERVAL_TIME + m_lastinterval.getTime()) < currentDate.getTime())
        {
            onLoggerInterval(currentDate);
        }
    }

    /** called when a user logs off
     * 
     * @param sessionid_p
     * @param userid_p
     */
    public static void onLogout(String sessionid_p, OwBaseUserInfo userid_p)
    {
        String userid = userid_p.getUserID();

        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwUsageCounter.onLogout: Userid = " + userid);
        }

        Set sessions = (Set) m_users.get(userid);
        if (sessions != null)
        {
            // remove sessions
            Iterator it = sessions.iterator();
            while (it.hasNext())
            {
                String session = (String) it.next();
                m_sessions.remove(session);
            }
        }

        // remove user
        m_users.remove(userid);
    }

    /** called every minute to log and collect usage statistics
     * @see #INTERVAL_TIME
     */
    private static synchronized void onLoggerInterval(Date currentDate_p)
    {
        if ((INTERVAL_TIME + m_lastinterval.getTime()) < currentDate_p.getTime())
        {
            m_lastinterval = currentDate_p;

            if (m_24hstats.size() >= MAX_24H_STAT_ENTRIES)
            {
                m_24hstats.remove(0);
            }

            m_24hstats.add(new OwUsageState(m_users, m_sessions, currentDate_p));
        }
    }

    /** get a collection with OwUsageState entries
     * 
     * @return a {@link Collection}
     */
    public static Collection getStatistics()
    {
        return m_24hstats;
    }

    /** get interval time in milliseconds
     * 
     * @return int 
     */
    public static int getIntervalTime()
    {
        return INTERVAL_TIME;
    }

    /** called when a session gets created
     * 
     * @param sessionid_p String session ID 
     */
    public static void onSessionCreated(String sessionid_p)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwUsageCounter.onSessionCreated: Sessioid = " + sessionid_p);
        }
    }

    /** called when a session gets destroyed
     * 
     * @param sessionid_p String session ID 
     */
    public static void onSessionDestroyed(String sessionid_p)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwUsageCounter.onSessionDestroyed: Sessioid = " + sessionid_p);
        }

        String userid = (String) m_sessions.get(sessionid_p);

        // remove session
        m_sessions.remove(sessionid_p);

        if (userid != null)
        {
            // remove session from user set
            Set sessions = (Set) m_users.get(userid);
            if (sessions != null)
            {
                sessions.remove(sessionid_p);
            }

            if ((sessions == null) || (sessions.size() == 0))
            {
                // === no more sessions associated to user
                // remove user
                m_users.remove(userid);
            }
        }
    }
}
