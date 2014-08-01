package com.wewebu.ow.server.util;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Exception to signal the debug language.
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
public class OwDebugModeException extends OwException
{
    /**
     * 
     */
    private static final long serialVersionUID = 5001653490138541879L;

    public OwDebugModeException()
    {
        super("");
    }

    /** construct a exception with a message and Throwable */
    public OwDebugModeException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    public String getModulName()
    {
        return "UTIL";
    }
}
