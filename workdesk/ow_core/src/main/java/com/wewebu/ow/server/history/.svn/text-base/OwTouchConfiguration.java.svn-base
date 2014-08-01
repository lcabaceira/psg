package com.wewebu.ow.server.history;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Class for holding the configuration information for in session History entries,
 * and for the Touch plugin.
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
 *@since 2.5.2.0
 */
public class OwTouchConfiguration
{
    private static final String TOOLTIP_ATTRIBUTE_NAME = "tooltip";
    /**all event types, corresponding with "*" from XML configuration event type*/
    public static final int ALL_EVENT_TYPES = Integer.MAX_VALUE;
    /**all event IDs, corresponding to "*" from XML configuration eventId*/
    public static final String ALL_EVENT_IDS = "*";
    /**constant for not defined event type*/
    public static final int NOT_DEFINED = Integer.MIN_VALUE;
    /**event type attribute name, as is used in configuration XML*/
    private static final String EVENTTYPE_ATTRIBUTE_NAME = "eventtype";
    /**event ID attribute name, as is used in configuration XML*/
    private static final String EVENT_ID_ATTRIBUTE_NAME = "eventid";
    /**event node name, as is used in configuration XML*/
    private static final String EVENT_NODE_NAME = "Event";
    /**All event types as is used in the configuration XML*/
    private static final String ALL_EVENT_TYPE_AS_STRING = "*";
    /**flag used to read or not the icon path from configuration.*/
    private boolean m_readIcons;
    /**flag for specify the configuration initialization status*/
    private boolean m_isConfigurationInitialized;
    /**map holding the eventType as key, and a list of mappings as value*/
    private Map m_eventType2EventIdAndIcon;
    /**locale object*/
    private Locale m_locale;
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwTouchConfiguration.class);

    /**
     * Convenient class for holding a pair of event ID and icon for a given event type 
     */
    public class OwEventID2IconMaping
    {
        /**event id, can be "*"*/
        private String m_eventId;
        /**icon path*/
        private String m_icon;
        /**icon tooltip*/
        private String m_tooltip;

        /**
         * Constructor
         * @param eventId_p - the event ID 
         * @param icon_p - the icon path
         * @param tooltip_p - the tooltip for the icon
         */
        public OwEventID2IconMaping(String eventId_p, String icon_p, String tooltip_p)
        {
            this.m_eventId = eventId_p;
            this.m_icon = icon_p;
            this.m_tooltip = tooltip_p;
            if (m_eventId == null)
            {
                LOG.error("OwEventID2IconMaping.constructor: Event ID cannot be null");
                throw new IllegalArgumentException("Event ID cannot be null");
            }
        }

        /**
         * Getter for event ID.
         * @return the event ID.
         */
        public String getEventId()
        {
            return m_eventId;
        }

        /**
         * Getter for icon path
         * @return the icon path
         */
        public String getIcon()
        {
            return m_icon;
        }

        /**
         * Get the localized tooltip string.
         * @return - the tooltip
         */
        public String getTooltip()
        {
            String result = "";
            if (hasTooltip())
            {
                result = OwString.localize(OwTouchConfiguration.this.m_locale, m_tooltip, m_tooltip);
            }
            return result;
        }

        /**
         * Check if a tooltip is available
         * @return <code>true</code> if a tooltip is configured.
         */
        public boolean hasTooltip()
        {
            return null != m_tooltip;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return m_eventId.hashCode();
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj_p)
        {
            boolean result = false;
            if (obj_p instanceof OwEventID2IconMaping)
            {
                OwEventID2IconMaping toBeCompared = (OwEventID2IconMaping) obj_p;
                if (toBeCompared.m_eventId.compareTo(this.m_eventId) == 0)
                {
                    if (this.m_icon != null && this.m_icon.compareTo(toBeCompared.m_icon) == 0)
                    {
                        result = true;
                    }
                    else
                    {
                        if (this.m_icon == null && toBeCompared.m_icon == null)
                        {
                            result = true;
                        }
                    }

                }
            }
            return result;
        }

        /**
         * Check if this mapping contains a generic event ID ("*").
         * @return true - if this mapping is for a generic event ID.
         */
        public boolean isGenericMapping()
        {
            return this.m_eventId.compareTo(ALL_EVENT_IDS) == 0;
        }

    }

    /**
     * Constructor
     * @param shouldReadIcons_p - flag used to read or not the icon path from configuration.
     */
    public OwTouchConfiguration(boolean shouldReadIcons_p)
    {
        this.m_readIcons = shouldReadIcons_p;
        this.m_isConfigurationInitialized = false;
        m_eventType2EventIdAndIcon = new HashMap();

    }

    /**
     * Constructor
     * @param shouldReadIcons_p - flag used to read or not the icon path from configuration.
     * @param touchConfigurationNode_p - the XML node (SessionEvents|TouchEvents)
     * @throws OwConfigurationException thrown when the configuration is not valid.
     */
    public OwTouchConfiguration(boolean shouldReadIcons_p, Node touchConfigurationNode_p, Locale locale_p) throws OwConfigurationException
    {
        this(shouldReadIcons_p);
        init(touchConfigurationNode_p, locale_p);
    }

    /**
     * Initialize this configuration element.
     * @param touchConfigurationNode_p - the XML node (SessionEvents|TouchEvents)
     * @param locale_p - the m_locale object
     * @throws OwConfigurationException thrown when the configuration is not valid.
     */
    public void init(Node touchConfigurationNode_p, Locale locale_p) throws OwConfigurationException
    {
        if (locale_p == null)
        {
            LOG.error("OwTouchConfiguration.init(): Locale parameter cannot be null");
            throw new IllegalArgumentException("Locale parameter cannot be null!");
        }
        this.m_locale = locale_p;
        if (touchConfigurationNode_p != null)
        {
            NodeList nodeList = touchConfigurationNode_p.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);
                if (node.getNodeName().equals(EVENT_NODE_NAME))
                {
                    Integer eventTypeAsNumber = getEventTypeAttributeValue(node, EVENTTYPE_ATTRIBUTE_NAME);
                    String eventId = getEventIdAttributeValue(node, EVENT_ID_ATTRIBUTE_NAME);
                    String iconText = readIconText(node);
                    String tooltip = getTooltip(node);
                    OwEventID2IconMaping mapping = new OwEventID2IconMaping(eventId, iconText, tooltip);
                    List existingMappingForEventType = (List) m_eventType2EventIdAndIcon.get(eventTypeAsNumber);
                    if (existingMappingForEventType == null)
                    {
                        existingMappingForEventType = new LinkedList();
                    }
                    if (existingMappingForEventType.contains(mapping))
                    {
                        LOG.error("OwTouchConfiguration.init(): Duplicate mapping for event type: " + eventTypeAsNumber);
                        throw new OwConfigurationException(OwString.localize1(m_locale, "app.OwConfiguration.touch.duplicateentry", "Duplicate entry for event type: %1", "" + eventTypeAsNumber));
                    }
                    existingMappingForEventType.add(mapping);
                    m_eventType2EventIdAndIcon.put(eventTypeAsNumber, existingMappingForEventType);
                }
            }
            if (!m_eventType2EventIdAndIcon.isEmpty())
            {
                m_isConfigurationInitialized = true;
            }
        }
    }

    /**
     * Get the configured tooltip or <code>null</code>.
     * @param node_p - the node
     * @return - the configured tooltip
     */
    private String getTooltip(Node node_p)
    {
        String tooltip = null;
        if (m_readIcons)
        {
            Node toolTypeNode = node_p.getAttributes().getNamedItem(TOOLTIP_ATTRIBUTE_NAME);
            if (toolTypeNode != null)
            {
                tooltip = toolTypeNode.getNodeValue();
            }
        }
        return tooltip;
    }

    /**
     * Utility method to read icon text
     * @param node_p - the node
     * @return - the icon text
     */
    private String readIconText(Node node_p)
    {
        String iconText = null;
        if (m_readIcons)
        {
            StringBuffer innerText = new StringBuffer();
            NodeList childNodes = node_p.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++)
            {
                Node childNode = childNodes.item(j);

                if (childNode.getNodeType() == Node.TEXT_NODE)
                {
                    String nodeValue = childNode.getNodeValue();
                    if (nodeValue != null)
                    {
                        innerText.append(nodeValue.trim());
                    }
                }
            }
            iconText = innerText.toString().trim();
        }
        return iconText;
    }

    /**
     * Utility method for read an Integer value from a Node attribute.
     * @param node_p - the node
     * @param attributeName_p - the name of the attribute
     * @return - the value for given attribute as an Integer
     * @throws OwConfigurationException thrown when there is no attribute with the given name is found, or the attribute has a null value, or when it's value cannot be found as an attribute of OwEventManager class.
     */
    private Integer getEventTypeAttributeValue(Node node_p, String attributeName_p) throws OwConfigurationException
    {
        Integer eventTypeAsNumber = null;
        Node eventTypeNode = node_p.getAttributes().getNamedItem(attributeName_p);
        if (eventTypeNode == null)
        {
            LOG.error("OwTouchConfiguration.getEventTypeAttributeValue(): Attribute " + attributeName_p + " is null");
            throw new OwConfigurationException(OwString.localize1(m_locale, "app.OwConfiguration.touch.attributenotfound", "Attribute '%1' is not found in the specified node.", attributeName_p));
        }
        String eventType = eventTypeNode.getNodeValue();
        if (null == eventType)
        {
            LOG.error("OwTouchConfiguration.getEventTypeAttributeValue(): Attribute " + attributeName_p + " has null value");
            throw new OwConfigurationException(OwString.localize1(m_locale, "app.OwConfiguration.touch.attributehasnovalue", "Attribute '%1' has no value.", attributeName_p));
        }
        //check for number 
        try
        {
            eventTypeAsNumber = Integer.valueOf(eventType);
        }
        catch (NumberFormatException e)
        {
            if (eventType.equals(ALL_EVENT_TYPE_AS_STRING))
            {
                eventTypeAsNumber = Integer.valueOf(ALL_EVENT_TYPES);
            }
            else
            {
                Field[] fields = OwEventManager.class.getFields();
                for (int j = 0; j < fields.length; j++)
                {
                    if (fields[j].getType().equals(int.class) && fields[j].getName().compareTo(eventType) == 0)
                    {
                        try
                        {
                            eventTypeAsNumber = (Integer) fields[j].get(null);
                        }
                        catch (IllegalAccessException ex)
                        {
                            //do nothing. go further
                        }
                        break;
                    }
                }
                if (eventTypeAsNumber == null)
                {
                    LOG.error("OwTouchConfiguration.getEventTypeAttributeValue(): Invalid event type: " + eventType);
                    throw new OwConfigurationException(OwString.localize1(m_locale, "app.OwConfiguration.touch.invalideventtype", "Invalid event type: %1", eventType));
                }
            }
        }
        return eventTypeAsNumber;
    }

    /**
     * Extract a string attribute value (corresponding to an event id)
     * @param node_p - the node
     * @param attributeName_p - the node attribute
     * @return - the value
     * @throws OwConfigurationException - thrown when the attribute cannot be found.
     */
    private String getEventIdAttributeValue(Node node_p, String attributeName_p) throws OwConfigurationException
    {

        Node eventIdNode = node_p.getAttributes().getNamedItem(attributeName_p);
        if (eventIdNode == null)
        {
            LOG.error("OwTouchConfiguration.getEventIdAttributeValue(): Attribute " + attributeName_p + " is null");
            throw new OwConfigurationException(OwString.localize1(m_locale, "app.OwConfiguration.touch.attributenotfound", "Attribute '%1' is not found in the specified node.", attributeName_p));
        }
        String eventId = eventIdNode.getNodeValue();
        String eventIdAsString = null;
        if (eventId == null)
        {
            LOG.error("OwTouchConfiguration.getEventIdAttributeValue(): Attribute " + attributeName_p + " has null value");
            throw new OwConfigurationException(OwString.localize1(m_locale, "app.OwConfiguration.touch.attributehasnovalue", "Attribute '%1' has no value.", attributeName_p));
        }

        if (ALL_EVENT_IDS.compareTo(eventId) != 0)
        {
            Field[] fields = OwEventManager.class.getFields();
            for (int j = 0; j < fields.length; j++)
            {
                if (fields[j].getType().equals(String.class) && fields[j].getName().compareTo(eventId) == 0)
                {
                    try
                    {
                        eventIdAsString = (String) fields[j].get(null);
                    }
                    catch (IllegalAccessException ex)
                    {
                        //do nothing. go further
                    }
                    break;
                }
            }
            if (eventIdAsString == null)
            {
                //assume that this is a plugin ID 
                eventIdAsString = eventId;
            }
        }
        else
        {
            eventIdAsString = ALL_EVENT_IDS;
        }

        return eventIdAsString;
    }

    /**
     * Get the icon path for a given pair of eventType and event ID 
     * @param eventType_p - the event type
     * @param eventId_p - the event ID.
     * @return the path to the icon or null, in case that the combination of eventType and event ID cannot be resolved.
     */
    public String getIconForEventType(int eventType_p, String eventId_p)
    {
        String result = null;
        List mappingList = (List) this.m_eventType2EventIdAndIcon.get(Integer.valueOf(eventType_p));
        if (mappingList == null)
        {
            return null;
        }
        OwEventID2IconMaping allEventsMapping = null;
        OwEventID2IconMaping specificMapping = null;
        for (Iterator iterator = mappingList.iterator(); iterator.hasNext();)
        {
            OwEventID2IconMaping mapping = (OwEventID2IconMaping) iterator.next();
            if (mapping.isGenericMapping())
            {
                allEventsMapping = mapping;
            }
            if (mapping.getEventId().compareTo(eventId_p) == 0)
            {
                specificMapping = mapping;
                break;
            }
        }
        if (specificMapping == null)
        {
            specificMapping = allEventsMapping;
        }
        if (specificMapping != null)
        {
            result = specificMapping.getIcon();
        }
        return result;
    }

    /** 
     * Get configured event IDs for the given eventType.
     * @param eventType_p - the event type
     * @return - the array of event IDs, or an empty array if no event ID was configured for the given event type
     */
    public String[] getEventIdsForEventType(int eventType_p)
    {
        String[] result = new String[0];
        Integer key = Integer.valueOf(eventType_p);
        if (m_eventType2EventIdAndIcon.containsKey(key))
        {

            List mappingList = (List) m_eventType2EventIdAndIcon.get(Integer.valueOf(eventType_p));

            List resultList = getEventIdsAsList(mappingList);
            result = (String[]) resultList.toArray(new String[resultList.size()]);

        }
        return result;
    }

    /**
     * Utility method, to get a list of eventsIs from a list of mappings.
     * @param mappingList_p - the list of mappings
     * @return a list populated with event IDs, or an empty list.
     */
    private List getEventIdsAsList(List mappingList_p)
    {
        List resultList = new LinkedList();
        for (Iterator iterator = mappingList_p.iterator(); iterator.hasNext();)
        {
            OwEventID2IconMaping mapping = (OwEventID2IconMaping) iterator.next();
            resultList.add(mapping.getEventId());
        }
        return resultList;
    }

    /**
     * Check if a pair of event type and event ID is configured in XML. 
     * Check also for generic event type.
     * @param eventType_p - the event type
     * @param strEventID_p - the event ID 
     * @return <code>true</code> - if the pair is configured in the XML.
     */
    public boolean isConfigured(int eventType_p, String strEventID_p)
    {
        Integer key = Integer.valueOf(eventType_p);
        boolean result = isEventIdAvailableForEventType(strEventID_p, key);
        if (!result)
        {
            //check if we have a configuration for all event types
            key = Integer.valueOf(ALL_EVENT_TYPES);
            result = isEventIdAvailableForEventType(strEventID_p, key);
        }
        return result;
    }

    /**
     * Utility method: check if an eventId is configured for a given event type.
     * @param strEventID_p - event ID 
     * @param theEventType_p - event type
     * @return <code>true</code> - if such a pair exists.
     */
    private boolean isEventIdAvailableForEventType(String strEventID_p, Integer theEventType_p)
    {
        boolean result = false;
        if (m_eventType2EventIdAndIcon.containsKey(theEventType_p))
        {
            List mappingList = (List) m_eventType2EventIdAndIcon.get(theEventType_p);
            result = getEventIdsAsList(mappingList).contains(strEventID_p);
            if (!result)
            {
                //check for * (all event IDs are available)
                result = getEventIdsAsList(mappingList).contains(ALL_EVENT_IDS);
            }

        }
        return result;
    }

    /**
     * Check if the given event type is configured.
     * @param eventType_p - the event type
     * @return <code>true</code> - if the given type is configured.
     */
    public boolean isConfigured(int eventType_p)
    {
        boolean result = false;
        if (m_eventType2EventIdAndIcon.containsKey(Integer.valueOf(eventType_p)))
        {
            result = true;
        }
        if (!result)
        {
            result = m_eventType2EventIdAndIcon.containsKey(Integer.valueOf(ALL_EVENT_TYPES));
        }
        return result;
    }

    /**
     * Return the configured event types.
     * @return - the configured event types.
     */
    public Set getConfiguredEventTypes()
    {
        return m_eventType2EventIdAndIcon.keySet();
    }

    /**
     * Return a list of mappings for the given event type
     * @param eventType_p - the event type
     * @return - the list of mappings
     */
    public List getConfiguredMappingForEventType(int eventType_p)
    {
        List result = new LinkedList();
        Integer key = Integer.valueOf(eventType_p);
        if (m_eventType2EventIdAndIcon.containsKey(key))
        {
            result = (List) m_eventType2EventIdAndIcon.get(key);
        }
        return result;
    }

    /**
     * Check if this configuration was correctly initialized.
     * @return <code>true</code> if configuration was correctly initialized.
     */
    public boolean isTouchConfigurationInitialized()
    {
        return m_isConfigurationInitialized;
    }

    /**
     * Check if the given event type is a generic event type
     * @param eventType_p
     * @return true - if the given event type is a generic one
     */
    public boolean isGenericEventType(Integer eventType_p)
    {
        return eventType_p != null && eventType_p.intValue() == OwTouchConfiguration.ALL_EVENT_TYPES;
    }

    /**
     * Check if the given event ID is a generic one.
     * @param eventId_p - the event ID.
     * @return true - if the given event ID is generic.
     */
    public boolean isGenericEventId(String eventId_p)
    {
        return eventId_p != null && eventId_p.compareTo(OwTouchConfiguration.ALL_EVENT_IDS) == 0;
    }

}