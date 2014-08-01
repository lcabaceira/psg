package com.wewebu.ow.server.app;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception thrown when a clipboard operation cannot be performed.
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
public class OwClipboardException extends OwException
{

    /**
     * version
     */
    private static final long serialVersionUID = 5655380321323525060L;

    /**
     * Constructor
     * @param strMessage_p - the message.
     */
    public OwClipboardException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /**
     * Constructor
     * @param strMessage_p - the {@link OwString} message. 
     * @param cause_p - the {@link Throwable} object that cause this exception.
     */
    public OwClipboardException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);

    }

    /**
     * Constructor
     * @param strMessage_p - the {@link OwString} message.
     */
    public OwClipboardException(OwString strMessage_p)
    {
        super(strMessage_p);

    }

    /**
     * Constructor
     * @param strMessage_p - the {@link String} message.
     * @param cause_p - the {@link Throwable} object that cause this exception.
     */
    public OwClipboardException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.exceptions.OwException#getModulName()
     */
    public String getModulName()
    {
        return "ow_core";
    }

}