package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception thrown, when an object could not be locked, but can be used read only.
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
public class OwLockDeniedException extends OwAccessDeniedException
{
    /**
     * 
     */
    private static final long serialVersionUID = 7138561974774264997L;

    /** construct a exception with a message */
    public OwLockDeniedException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message */
    public OwLockDeniedException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and Throwable */
    public OwLockDeniedException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message and Throwable */
    public OwLockDeniedException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** get the name of the module / manager / plugin where the exception occurred */
    public String getModulName()
    {
        return "OECM";
    }
}