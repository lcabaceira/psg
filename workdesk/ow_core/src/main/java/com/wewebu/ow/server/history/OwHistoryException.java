package com.wewebu.ow.server.history;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * History exception class.
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
public class OwHistoryException extends OwException
{
    /**
     * 
     */
    private static final long serialVersionUID = -4747461272931388470L;

    /** construct a exception with a message */
    public OwHistoryException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message */
    public OwHistoryException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and Throwable */
    public OwHistoryException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message and Throwable */
    public OwHistoryException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** get the name of the module / manager / plugin where the exception occurred */
    public String getModulName()
    {
        return "History";
    }
}
