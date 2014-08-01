package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.List;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception which indicates that there is no note property defined.
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
public class OwBPMNotePropertyException extends OwObjectNotFoundException
{
    /**
     * 
     */
    private static final long serialVersionUID = 2829547986104117091L;

    /** list of the names of the workitems which have no note property defined */
    private List m_itemsWithNoNoteProperty;

    /**
     * constructor
     * @param itemsWithNoNoteProperty_p 
     * @param strMessage_p
     */
    public OwBPMNotePropertyException(List itemsWithNoNoteProperty_p, String strMessage_p)
    {
        super(strMessage_p);
        m_itemsWithNoNoteProperty = itemsWithNoNoteProperty_p;
    }

    /** construct a exception with a message */
    public OwBPMNotePropertyException(List itemsWithNoNoteProperty_p, OwString strMessage_p)
    {
        super(strMessage_p);
        m_itemsWithNoNoteProperty = itemsWithNoNoteProperty_p;
    }

    /** construct a exception with a message and Throwable */
    public OwBPMNotePropertyException(List itemsWithNoNoteProperty_p, String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
        m_itemsWithNoNoteProperty = itemsWithNoNoteProperty_p;
    }

    /** construct a exception with a message and Throwable */
    public OwBPMNotePropertyException(List itemsWithNoNoteProperty_p, OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
        m_itemsWithNoNoteProperty = itemsWithNoNoteProperty_p;
    }

    /**
     * getter method for itemsWithNoNoteProperty 
     * @return Returns the itemsWithNoNoteProperty.
     */
    public List getItemsWithNoNoteProperty()
    {
        return m_itemsWithNoNoteProperty;
    }

}
