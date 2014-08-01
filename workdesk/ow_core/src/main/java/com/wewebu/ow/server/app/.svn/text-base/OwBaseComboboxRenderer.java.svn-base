package com.wewebu.ow.server.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Combo box renderer component.
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
 *@since 3.0.0.0
 */
public abstract class OwBaseComboboxRenderer implements OwComboboxRenderer
{
    /** the context */
    protected OwMainAppContext m_context;
    /** the model */
    protected OwComboModel m_model;
    /** the field id*/
    protected String m_fieldId;
    /** the field definition */
    protected OwFieldDefinition m_fieldDefinition;
    /** the field provider */
    protected OwFieldProvider m_fieldProvider;
    /** ignore default style sheets */
    protected boolean m_ignoreDefaultStylesheets;
    /**enabled flag*/
    protected boolean enabled = true;
    /** description */
    protected OwString m_description;
    /** the event name to event handling map*/
    protected Map m_events = new HashMap();
    /** package logger for the class */
    protected static final Logger LOG = OwLogCore.getLogger(OwBaseComboboxRenderer.class);

    /** list of style sheet classes */
    protected Set m_styleClasses = new LinkedHashSet();

    /**
     * Default constructor
     */
    public OwBaseComboboxRenderer()
    {
        this.m_context = null;
        this.m_model = null;
        this.m_fieldId = null;
        this.m_fieldDefinition = null;
        this.m_fieldProvider = null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled_p)
    {
        this.enabled = enabled_p;
    }

    /**
     * Render the style classes
     * @param defaultStyleClasses_p - classes that will be always rendered.
     * @return a string with all rendered classes.
     */
    protected String renderStyleClasses(Set defaultStyleClasses_p)
    {
        StringBuffer result = new StringBuffer();
        result.append("class=\"");
        if (!m_ignoreDefaultStylesheets)
        {
            for (Iterator iterator = defaultStyleClasses_p.iterator(); iterator.hasNext();)
            {
                String styleClass = (String) iterator.next();
                result.append(styleClass);
                result.append(" ");
            }
        }
        if (m_fieldDefinition != null)
        {
            result.append(" OwInputControl_");
            result.append(m_fieldDefinition.getClassName());
        }
        for (Iterator iterator = m_styleClasses.iterator(); iterator.hasNext();)
        {
            String styleClass = (String) iterator.next();
            result.append(styleClass);
            result.append(" ");
        }
        result.append("\" ");
        return result.toString();
    }

    /**
     * Get the context.
     * @return - the context.
     */
    protected OwMainAppContext getContext()
    {
        return m_context;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#addStyleClass(java.lang.String)
     */
    public void addStyleClass(String styleClass_p)
    {
        m_styleClasses.add(styleClass_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#addStyleClass(java.lang.String, boolean)
     */
    public void addStyleClass(String styleClass_p, boolean ignoreDefaultStyleClasses_p)
    {
        this.m_ignoreDefaultStylesheets = ignoreDefaultStyleClasses_p;
        m_styleClasses.add(styleClass_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#addStyleClasses(java.lang.String[])
     */
    public void addStyleClasses(String[] styleClasses_p)
    {
        m_styleClasses.addAll(Arrays.asList(styleClasses_p));
    }

    public void addStyleClasses(String[] styleClasses_p, boolean ignoreDefaultStyleClasses_p)
    {
        this.m_ignoreDefaultStylesheets = ignoreDefaultStyleClasses_p;
        m_styleClasses.addAll(Arrays.asList(styleClasses_p));
    }

    public void addEvent(String eventName_p, String eventHandler_p)
    {
        m_events.put(eventName_p, eventHandler_p);
    }

    public String[] getAllEventsNames()
    {
        return (String[]) m_events.keySet().toArray(new String[m_events.size()]);
    }

    /**
     * Get the JavaScript code as a string for specified event.
     * @param eventName_p the event name
     * @return a {@link String} object, representing the JavaScript code associated with this event. 
     */
    protected String getEventHandler(String eventName_p)
    {
        return eventName_p == null ? null : (String) m_events.get(eventName_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#setContext(com.wewebu.ow.server.app.OwMainAppContext)
     */
    public void setContext(OwMainAppContext context_p)
    {
        m_context = context_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#setModel(com.wewebu.ow.server.app.OwComboModel)
     */
    public void setModel(OwComboModel model_p)
    {
        m_model = model_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#setFieldDescription(java.lang.String)
     */
    public void setFieldDescription(OwString description_p)
    {
        m_description = description_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#setFieldId(java.lang.String)
     */
    public void setFieldId(String fieldId_p)
    {
        m_fieldId = fieldId_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#setFieldDefinition(com.wewebu.ow.server.field.OwFieldDefinition)
     */
    public void setFieldDefinition(OwFieldDefinition fieldDefinition_p)
    {
        m_fieldDefinition = fieldDefinition_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#setFieldProvider(com.wewebu.ow.server.field.OwFieldProvider)
     */
    public void setFieldProvider(OwFieldProvider fieldProvider_p)
    {
        m_fieldProvider = fieldProvider_p;
    }

}
