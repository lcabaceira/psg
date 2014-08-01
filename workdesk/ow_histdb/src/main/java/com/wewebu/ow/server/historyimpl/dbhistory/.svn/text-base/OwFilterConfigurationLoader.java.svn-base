package com.wewebu.ow.server.historyimpl.dbhistory;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.historyimpl.dbhistory.log.OwLog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Event filter configuration utility.<BR>
 * Processes filter configuration configurations found under the ../HistoryManager/Filter 
 * element in Workdesk configuration files.
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
public class OwFilterConfigurationLoader
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFilterConfigurationLoader.class);

    /**Filter configuration element : {@value}*/
    public static final String CONF_FILTER_ELEMENT_NAME = "Filter";

    /**Status configuration element : {@value}*/
    public static final String CONF_STATUS_ELEMENT_NAME = "Status";

    /**Type configuration element : {@value}*/
    public static final String CONF_TYPE_ELEMENT_NAME = "Type";

    /**Event Id configuration element : {@value}*/
    public static final String CONF_EVENTID_ELEMENT_NAME = "EventId";

    /**Event filter class configuration attribute : {@value}*/
    public static final String CONF_EVENTFILTERCLASS_ATTRIBUTE_NAME = "eventFilterClass";

    private OwXMLUtil m_configuration;

    /**
     * Constructor
     * @param configuration_p an XML utility for a <b>HistoryManager</b> element to be used at loading time 
     */
    public OwFilterConfigurationLoader(OwXMLUtil configuration_p)
    {
        super();
        this.m_configuration = configuration_p;
    }

    /**
     * Creates an {@link OwEventFilter} based on the {@link #CONF_EVENTFILTERCLASS_ATTRIBUTE_NAME} defined attribute.
     * @return an {@link OwEventFilter} instance if an correct configuration is found (the {@link #CONF_FILTER_ELEMENT_NAME} 
     *          has a correct {@link #CONF_EVENTFILTERCLASS_ATTRIBUTE_NAME} attribute ) or <code>null</code> if no configuration is 
     *          found(the {@link #CONF_FILTER_ELEMENT_NAME}  is missing or it has no {@link #CONF_EVENTFILTERCLASS_ATTRIBUTE_NAME} defined).
     * @throws Exception if the event filter is configured wrong or the event filter class fails at instantiation time
     */
    public OwEventFilter loadEventFilter() throws Exception
    {
        Node filters = m_configuration.getSubNode(CONF_FILTER_ELEMENT_NAME);
        if (filters != null)
        {

            NamedNodeMap filterAttributes = filters.getAttributes();
            Node eventFilterClassAttribute = filterAttributes.getNamedItem(CONF_EVENTFILTERCLASS_ATTRIBUTE_NAME);
            if (eventFilterClassAttribute != null && eventFilterClassAttribute.getNodeType() == Node.ATTRIBUTE_NODE)
            {
                String eventFilterClassName = eventFilterClassAttribute.getNodeValue();
                if (eventFilterClassName != null)
                {
                    try
                    {
                        Class eventFilterClass = Class.forName(eventFilterClassName);
                        if (!OwEventFilter.class.isAssignableFrom(eventFilterClass))
                        {
                            String msg = "OwFilterConfigurationLoader.loadEventFilter: Event filter class is misconfigured. The event filter class " + eventFilterClass.getName() + " does not implement the OwEventFilter interface...";
                            LOG.fatal(msg);
                            throw new OwConfigurationException(msg);
                        }
                        else
                        {
                            Constructor constructor = eventFilterClass.getConstructor(new Class[] { OwXMLUtil.class });
                            OwEventFilter filter = (OwEventFilter) constructor.newInstance(new Object[] { m_configuration });
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("OwFilterConfigurationLoader.loadEventFilter: Succesfully loaded " + eventFilterClass.getName() + " event filter...");
                            }
                            return filter;
                        }
                    }
                    catch (NoSuchMethodException e)
                    {
                        String msg = "OwFilterConfigurationLoader.loadEventFilter: Event filter class is misconfigured.Is there an OwXMLUtil based constructor in the " + eventFilterClassName + " filter class...";
                        LOG.fatal(msg, e);
                        throw new OwConfigurationException(msg, e);
                    }
                    catch (Exception e)
                    {
                        String msg = "OwFilterConfigurationLoader.loadEventFilter: Event filter class is misconfigured...";
                        LOG.fatal(msg, e);
                        throw new OwConfigurationException(msg, e);
                    }
                }
            }
            else
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwFilterConfigurationLoader.loadEventFilter: No " + CONF_EVENTFILTERCLASS_ATTRIBUTE_NAME + " attribute found when attempting to load the event filter...");
                }

            }
        }
        else
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwFilterConfigurationLoader.loadEventFilter: No filters element found when attempring to load event filter...");
            }
        }

        //no filter class defined 
        return null;
    }

    /**
     * Starts the parse process or the current configuration element.
     * {@link #CONF_FILTER_ELEMENT_NAME} children elements are iterated, their text values read and the 
     * config hook methods called according to the element types : status,type or eventid.
     * @see #loadEventIdFilter(String)
     * @see #loadStatusFilter(String)
     * @see #loadTypeFilter(String)
     * @throws Exception if the filter is configured wrong
     */
    public void loadConfiguration() throws Exception
    {

        // read the filters out of configuration
        Node filters = m_configuration.getSubNode(CONF_FILTER_ELEMENT_NAME);

        if (filters != null)
        {
            for (Node filter = filters.getFirstChild(); filter != null; filter = filter.getNextSibling())
            {

                if (filter.getNodeType() != Node.ELEMENT_NODE)
                {
                    continue;
                }

                // handle Type filter
                if (filter.getNodeName().equals(CONF_TYPE_ELEMENT_NAME))
                {

                    String typeFilter = null;
                    Node childNode = filter.getFirstChild();
                    if (childNode != null && childNode.getNodeType() == Node.TEXT_NODE)
                    {
                        typeFilter = childNode.getNodeValue();
                    }
                    else
                    {
                        String msg = "OwFilterConfigurationLoader: Filter is misconfigured. Please verify the configuration of the filter in the bootstrap.xml (Node Type).";
                        LOG.fatal(msg);
                        throw new OwConfigurationException(msg);
                    }

                    loadTypeFilter(typeFilter);
                }

                // handle status filter
                else if (filter.getNodeName().equals(CONF_STATUS_ELEMENT_NAME))
                {

                    String statusFilter = null;
                    Node childNode = filter.getFirstChild();
                    if (childNode != null && childNode.getNodeType() == Node.TEXT_NODE)
                    {
                        statusFilter = childNode.getNodeValue();
                    }
                    else
                    {
                        String msg = "OwFilterConfigurationLoader: Filter is misconfigured. Please verify the configuration of the filter in the bootstrap.xml (Node Status).";
                        LOG.fatal(msg);
                        throw new OwConfigurationException(msg);
                    }

                    loadStatusFilter(statusFilter);
                }

                // handle event ID filter
                else if (filter.getNodeName().equalsIgnoreCase(CONF_EVENTID_ELEMENT_NAME))
                {

                    String eventIdFilter = null;
                    Node childNode = filter.getFirstChild();
                    if (childNode != null && childNode.getNodeType() == Node.TEXT_NODE)
                    {
                        eventIdFilter = childNode.getNodeValue();
                    }
                    else
                    {
                        String msg = "OwFilterConfigurationLoader: Filter is misconfigured. Please verify the configuration of the filter in the bootstrap.xml (Node EventId).";
                        LOG.fatal(msg);
                        throw new OwConfigurationException(msg);
                    }

                    loadEventIdFilter(eventIdFilter);
                }
                else
                {
                    throw new OwNotSupportedException("Filter is misconfigured. Verify configuration of the filter in the bootstrap.xml (Node " + filter.getNodeName() + " is not supported)!");

                }
            }

        }
    }

    /**
     * Called from within {@link #loadConfiguration()} whenever an <b>eventId</b> filter is encountered 
     * @param eventIdFilter_p
     * @throws Exception
     */
    protected void loadEventIdFilter(String eventIdFilter_p) throws Exception
    {
        //void
    }

    /**
     * Called from within {@link #loadConfiguration()} whenever a <b>status</b> filter is encountered 
     * @param statusFilter_p
     * @throws Exception
     */
    protected void loadStatusFilter(String statusFilter_p) throws Exception
    {
        //void
    }

    /**
     * Called from within {@link #loadConfiguration()} whenever a <b>type</b> filter is encountered 
     * @param typeFilter_p
     * @throws Exception
     */
    protected void loadTypeFilter(String typeFilter_p) throws Exception
    {
        //void
    }

}