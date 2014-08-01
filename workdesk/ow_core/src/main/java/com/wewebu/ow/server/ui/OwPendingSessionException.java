package com.wewebu.ow.server.ui;

import com.wewebu.ow.server.exceptions.OwSessionException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception thrown, when the previous request is not yet finished.
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
public class OwPendingSessionException extends OwSessionException
{
    /**
     * 
     */
    private static final long serialVersionUID = 7083751600381154829L;

    /** construct a exception with a message */
    public OwPendingSessionException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message */
    public OwPendingSessionException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and Throwable */
    public OwPendingSessionException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message and Throwable */
    public OwPendingSessionException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** get the name of the module / manager / plugin where the exception occurred */
    public String getModulName()
    {
        return "UI";
    }
}