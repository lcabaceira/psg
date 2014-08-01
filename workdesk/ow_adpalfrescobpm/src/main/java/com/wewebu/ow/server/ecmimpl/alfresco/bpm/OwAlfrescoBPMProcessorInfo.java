package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Locale;

import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;

/**
 *<p>
 * Alfresco BPM based implementation of {@link OwWorkitemProcessorInfo}.
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
 *@since 4.0.0.0
 */
public class OwAlfrescoBPMProcessorInfo implements OwWorkitemProcessorInfo
{

    private OwAlfrescoBPMWorkItem workItem;
    private String jspPage;

    public OwAlfrescoBPMProcessorInfo(OwAlfrescoBPMWorkItem workItem, String jspPage)
    {
        this.workItem = workItem;
        this.jspPage = jspPage;
    }

    public OwAlfrescoBPMProcessorInfo(OwAlfrescoBPMWorkItem workItem)
    {
        this(workItem, null);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getJspFormPage()
     */
    public String getJspFormPage()
    {
        return this.jspPage;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getType()
     */
    public int getType()
    {
        if (this.jspPage != null)
        {
            return STEPPROCESSOR_TYPE_JSP_FORM;
        }

        return STEPPROCESSOR_TYPE_UNKNOWN;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getURL()
     */
    public String getURL() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getScript()
     */
    public String getScript() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getID()
     */
    public String getID()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getDisplayName(java.util.Locale)
     */
    public String getDisplayName(Locale locale_p)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getContextType()
     */
    public int getContextType()
    {
        return STEPPROCESSOR_CONTEXT_UNKNOWN;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getNativeProcessor()
     */
    public Object getNativeProcessor()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getWidth()
     */
    public int getWidth()
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo#getHeight()
     */
    public int getHeight()
    {
        return 0;
    }
}
