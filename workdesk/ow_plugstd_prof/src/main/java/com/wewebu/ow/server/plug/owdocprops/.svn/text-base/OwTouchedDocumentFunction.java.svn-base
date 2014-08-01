package com.wewebu.ow.server.plug.owdocprops;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.history.OwSessionHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass;
import com.wewebu.ow.server.history.OwTouchConfiguration;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A document function that displays the status of a document (touched/not touched).
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
public class OwTouchedDocumentFunction extends OwDocumentFunction
{
    /**the touch events element name in owplugins.xml*/
    private static final String TOUCH_EVENTS_ELEMENT_NAME = "TouchEvents";
    //    === versioning    

    /**configuration*/
    private OwTouchConfiguration m_configuration;

    /**     
     * @see com.wewebu.ow.server.app.OwDocumentFunction#init(com.wewebu.ow.server.util.OwXMLUtil, com.wewebu.ow.server.app.OwMainAppContext)
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        Node touchEvents = node_p.getSubNode(TOUCH_EVENTS_ELEMENT_NAME);
        Locale locale = context_p.getLocale();
        if (locale == null)
        {
            locale = Locale.ENGLISH;
        }
        m_configuration = new OwTouchConfiguration(true, touchEvents, locale);
    }

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owtouch/eye.png");
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getBigIcon() throws Exception
    {
        // override to return a high resolution image instead
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owtouch/eye_24.png");
    }

    /**
     * @see com.wewebu.ow.server.app.OwDocumentFunction#onClickEvent(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        //nothing to do on this case until now
    }

    /**
     * Always return true
     * @see com.wewebu.ow.server.app.OwDocumentFunction#getObjectInstance()
     */
    public boolean getObjectInstance()
    {
        return true;
    }

    /**
     * @see com.wewebu.ow.server.app.OwFunction#getNoEvent()
     */
    public boolean getNoEvent()
    {
        return true;
    }

    /**
     * Return one or more icons, showing the state of this document.
     * If the document was not touched, return an empty string.
     * @see com.wewebu.ow.server.app.OwDocumentFunction#getIconHTML(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject)
     */
    public String getIconHTML(OwObject object_p, OwObject parent_p) throws Exception
    {
        String result = "";

        Set eventTypes = m_configuration.getConfiguredEventTypes();
        Map<String, String> iconsToBeShown = new LinkedHashMap<String, String>();
        for (Iterator iterator = eventTypes.iterator(); iterator.hasNext();)
        {
            Integer eventTypeAsInteger = (Integer) iterator.next();
            List listOfMappings = m_configuration.getConfiguredMappingForEventType(eventTypeAsInteger.intValue());
            for (Iterator mappingIterator = listOfMappings.iterator(); mappingIterator.hasNext();)
            {
                OwTouchConfiguration.OwEventID2IconMaping mapping = (OwTouchConfiguration.OwEventID2IconMaping) mappingIterator.next();
                if (isTouched(object_p, eventTypeAsInteger.intValue(), mapping.getEventId()))
                {
                    String icon = mapping.getIcon();
                    if (icon != null)
                    {
                        iconsToBeShown.put(icon, mapping.getTooltip());
                    }
                }
            }
        }
        if (iconsToBeShown.size() > 0)
        {
            StringBuilder buffer = new StringBuilder();
            for (Entry<String, String> entry : iconsToBeShown.entrySet())
            {
                String iconPath = getContext().getDesignURL() + entry.getKey();
                String tooltip = entry.getValue();
                buffer.append("<img class=\"OwFunctionIcon\"");
                if (tooltip != null)
                {
                    buffer.append(" title=\"").append(tooltip).append("\" alt=\"").append(tooltip).append("\" ");
                }
                buffer.append(" src=\"").append(iconPath).append("\">");
            }
            result = buffer.toString();
        }
        this.addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        return result;
    }

    /**
     * Check inside the history manager if the document was touched or not.
     * @param selectedObject_p - the object
     * @param eventType_p - the event type
     * @param eventId_p - the event ID.
     * @return true - if the document has registered SessionHistory events for it.
     * @throws Exception
     */
    private boolean isTouched(OwObject selectedObject_p, int eventType_p, String eventId_p) throws Exception
    {

        // create search filter using core utility function
        OwSearchNode filter = OwEcmUtil.createSimpleSearchNode(OwSessionHistoryEntry.getStaticObjectClass().getClassName(), null, null, new OwEcmUtil.OwSimpleSearchClause[] {
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, Integer.valueOf(eventType_p)),
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.ID_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, eventId_p) }, getContext().getHistoryManager());

        // search history manager
        OwObjectCollection result = getContext().getHistoryManager().doObjectSearch(selectedObject_p, filter, null, null, null, 1);

        return ((null != result) && (result.size() > 0));

    }
}