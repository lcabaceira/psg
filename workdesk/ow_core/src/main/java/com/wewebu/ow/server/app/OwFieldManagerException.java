package com.wewebu.ow.server.app;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Specialized Exception thrown, when a a edit field could not be updated / validated in field manager.
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
public class OwFieldManagerException extends OwException
{
    /**
     * 
     */
    private static final long serialVersionUID = -7149625972321003512L;

    /** construct a exception with a message */
    public OwFieldManagerException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message */
    public OwFieldManagerException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and Throwable */
    public OwFieldManagerException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message and Throwable */
    public OwFieldManagerException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** get the name of the module / manager / plugin where the exception occurred */
    public String getModulName()
    {
        return "FieldManager";
    }
}