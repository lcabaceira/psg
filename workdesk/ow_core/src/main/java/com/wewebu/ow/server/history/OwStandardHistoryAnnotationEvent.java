package com.wewebu.ow.server.history;

import com.wewebu.ow.server.ecm.OwObjectReference;

/**
 *<p>
 * Standard implementation of the OwStandardHistoryAnnotationEvent interface.
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
public class OwStandardHistoryAnnotationEvent implements OwHistoryAnnotationEvent
{
    /** the affected OwObjectReference */
    private OwObjectReference m_object;

    /** the change type of the annotation as defined with CHANGE_TYPE_... */
    private int m_iChangeType = CHANGE_TYPE_UNKNOWN;

    /** the text of the annotation, can be null */
    private String m_sText;

    /** the annotation type that was modified, can be null */
    private String m_sAnnotationType;

    /** a identifying ID of the annotation in question, can be null if not defined */
    private String m_sID;

    /** constructs a annotation changed event
     * 
     * @param object_p OwObjectReference
     * @param iChangeType_p int as defined with CHANGE_TYPE_...
     * @param sText_p String, can be null
     * @param sAnnotationType_p String, can be null
     * @param sID_p String, can be null
     */
    public OwStandardHistoryAnnotationEvent(OwObjectReference object_p, int iChangeType_p, String sText_p, String sAnnotationType_p, String sID_p)
    {
        m_object = object_p;
        m_iChangeType = iChangeType_p;
        m_sText = sText_p;
        m_sAnnotationType = sAnnotationType_p;
        m_sID = sID_p;
    }

    /** return a the affected OwObjectReference */
    public com.wewebu.ow.server.ecm.OwObjectReference getAffectedObject() throws Exception
    {
        return m_object;
    }

    /** return the change type of the annotation as defined with CHANGE_TYPE_... */
    public int getChangeType()
    {
        return m_iChangeType;
    }

    /** get the text of the annotation, can be null */
    public String getText()
    {
        return m_sText;
    }

    /** get a identifying ID of the annotation in question, can be null if not defined */
    public String getAnnotationID()
    {
        return m_sID;
    }

    /** get the annotation type that was modified, can be null */
    public String getAnnotationType()
    {
        return m_sAnnotationType;
    }

    public String getSummary() throws Exception
    {
        StringBuffer buf = new StringBuffer();

        if (m_sText != null)
        {
            buf.append(m_sText);
        }

        return buf.toString();
    }

}