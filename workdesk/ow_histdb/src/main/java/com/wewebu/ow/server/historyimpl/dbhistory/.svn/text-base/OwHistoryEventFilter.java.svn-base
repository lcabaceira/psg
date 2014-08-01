package com.wewebu.ow.server.historyimpl.dbhistory;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.historyimpl.dbhistory.log.OwLog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Filter for Events of the EventManager. Used in history managers to filter events.
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
public class OwHistoryEventFilter implements OwEventFilter
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwHistoryEventFilter.class);

    /** map to keep event types which should be filtered (not be triggered) */
    protected Set m_typeFilter;
    /** map to keep status which should be filtered (not be triggered) */
    protected Set m_statusFilter;
    /** map to keep event ID which should be filtered (not be triggered) */
    protected Set m_eventIdFilter;

    /**
     * constructor which configures the filter out of a XML configuration
     * @param configuration_p XML {@link OwXMLUtil} which defines  a filter node
     * @throws Exception 
     */
    public OwHistoryEventFilter(OwXMLUtil configuration_p) throws Exception
    {
        OwFilterConfigurationLoader configLoader = new OwFilterConfigurationLoader(configuration_p) {
            protected void loadTypeFilter(String typeFilter_p) throws Exception
            {
                if (m_typeFilter == null)
                {
                    m_typeFilter = new HashSet();
                }

                if (typeFilter_p != null)
                {
                    try
                    {
                        m_typeFilter.add(OwEventManager.class.getField(typeFilter_p).get(null));
                    }
                    catch (Exception e)
                    {
                        String msg = "OwHistoryEventFilter: Filter is misconfigured. Please verify the configuration of the filter in the bootstrap.xml, Type = " + typeFilter_p + " is unknown.";
                        LOG.fatal(msg, e);
                        throw new OwConfigurationException(msg, e);
                    }

                }
            }

            protected void loadStatusFilter(String statusFilter_p) throws Exception
            {
                if (m_statusFilter == null)
                {
                    m_statusFilter = new HashSet();
                }
                if (statusFilter_p != null)
                {
                    try
                    {
                        m_statusFilter.add(OwEventManager.class.getField(statusFilter_p).get(null));
                    }
                    catch (Exception e)
                    {
                        String msg = "OwHistoryEventFilter: Filter is misconfigured. Please verify the configuration of the filter in the bootstrap.xml, Status = " + statusFilter_p + " is unknown.";
                        LOG.fatal(msg, e);
                        throw new OwConfigurationException(msg, e);
                    }

                }
            }

            protected void loadEventIdFilter(String eventIdFilter_p) throws Exception
            {
                if (m_eventIdFilter == null)
                {
                    m_eventIdFilter = new HashSet();
                }
                if (eventIdFilter_p != null)
                {
                    try
                    {
                        m_eventIdFilter.add(OwEventManager.class.getField(eventIdFilter_p).get(null));
                    }
                    catch (Exception e)
                    {
                        // specified filter could not be found in OwEventMangager so we assume that it is a plugin ID.
                        m_eventIdFilter.add(eventIdFilter_p);
                    }

                }
            }
        };
        configLoader.loadConfiguration();

        // see if there is an empty set. If yes set back to null, which accelerates the match function
        if (m_typeFilter != null && m_typeFilter.size() == 0)
        {
            m_typeFilter = null;
        }
        if (m_statusFilter != null && m_statusFilter.size() == 0)
        {
            m_statusFilter = null;
        }
        if (m_eventIdFilter != null && m_eventIdFilter.size() == 0)
        {
            m_eventIdFilter = null;
        }

    }

    /**
     * filter the events which should be tracked by the history manager
     * @param eventType_p
     * @param strEventID_p
     * @param status_p
     * @return true if event should be filtered, i.e. is not getting tracked.
     *
     */
    public boolean match(int eventType_p, String strEventID_p, int status_p)
    {
        if (m_typeFilter != null && m_typeFilter.contains(Integer.valueOf(eventType_p)))
        {
            return true;
        }
        if (m_statusFilter != null && m_statusFilter.contains(Integer.valueOf(status_p)))
        {
            return true;
        }
        if (m_eventIdFilter != null && m_eventIdFilter.contains(strEventID_p))
        {
            return true;
        }
        //else:
        return false;
    }

}