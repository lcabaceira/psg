package test.com.wewebu.ow.server.ecm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 *<p>
 * OwIntegrationTestSession.
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
public class OwIntegrationTestSession
{
    private static final Logger LOG = Logger.getLogger(OwIntegrationTestSession.class);

    private static Map<String, OwIntegrationTestSession> activeSessions = new HashMap<String, OwIntegrationTestSession>();
    private static Thread shutdownHook;

    public static synchronized OwIntegrationTestSession getSession(String sessionName_p)
    {
        if (shutdownHook == null)
        {
            Runtime runtime = Runtime.getRuntime();
            shutdownHook = new Thread(new Runnable() {

                public void run()
                {
                    synchronized (activeSessions)
                    {
                        Set<Entry<String, OwIntegrationTestSession>> sessionEntries = new HashSet<Entry<String, OwIntegrationTestSession>>(activeSessions.entrySet());
                        for (Entry<String, OwIntegrationTestSession> sessionEntry : sessionEntries)
                        {
                            try
                            {
                                sessionEntry.getValue().tearDown();
                            }
                            catch (Exception e)
                            {
                                LOG.error("OwIntegrationTestSession.shutdownHook.run(): Failed to tear down session setup " + sessionEntry.getKey(), e);
                            }
                        }
                        LOG.debug("OwIntegrationTestSession.shutdownHook.run(): tore down " + sessionEntries.size() + " sessions");
                    }

                }

            });
            runtime.addShutdownHook(shutdownHook);

        }

        OwIntegrationTestSession session = activeSessions.get(sessionName_p);
        if (session == null)
        {
            session = new OwIntegrationTestSession(sessionName_p);
        }

        return session;
    }

    private String sessionName;

    public String getSessionName()
    {
        return sessionName;
    }

    private Map<String, OwIntegrationTestSetup> setups = new HashMap<String, OwIntegrationTestSetup>();

    private OwIntegrationTestSession(String sessionName_p)
    {
        this.sessionName = sessionName_p;
        synchronized (activeSessions)
        {
            activeSessions.put(this.sessionName, this);
        }
    }

    public synchronized OwIntegrationTestSetup getSetup(String name_p)
    {
        return this.setups.get(name_p);
    }

    public synchronized void addSetup(String name_p, OwIntegrationTestSetup setup_p) throws Exception
    {
        if (this.setups.containsKey(name_p))
        {
            throw new RuntimeException("Duplicate setup " + name_p + " in session " + this.sessionName);
        }
        else
        {
            this.setups.put(name_p, setup_p);
            setup_p.sessionSetUp(this);
        }
    }

    public synchronized void tearDown() throws Exception
    {
        synchronized (activeSessions)
        {
            Set<Entry<String, OwIntegrationTestSetup>> setupEntries = this.setups.entrySet();
            boolean failed = false;
            for (Iterator i = setupEntries.iterator(); i.hasNext();)
            {
                Entry<String, OwIntegrationTestSetup> setupEntry = (Entry<String, OwIntegrationTestSetup>) i.next();
                try
                {
                    setupEntry.getValue().sessionTearDown(this);
                }
                catch (Throwable e)
                {
                    LOG.error("OwIntegrationTestSession.tearDown(): Failed to tear down session setup " + setupEntry.getKey() + " @ " + this.sessionName, e);
                }
            }

            activeSessions.remove(this.sessionName);
            if (failed)
            {
                throw new IllegalStateException("Could not tore down session " + this.sessionName);
            }
            else
            {
                LOG.debug("OwIntegrationTestSession.tearDown(): Tore down session " + this.sessionName);
            }
        }
    }
}