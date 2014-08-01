package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception thrown, when the operation is not allowed, or the parameters are wrong.
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
public class OwInvalidOperationException extends OwException
{
    /**
     * 
     */
    private static final long serialVersionUID = -790158675857406386L;

    private String modulName = "OECM";

    /** construct a exception with a message */
    public OwInvalidOperationException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message */
    public OwInvalidOperationException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and Throwable */
    public OwInvalidOperationException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message and Throwable */
    public OwInvalidOperationException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    public String getModulName()
    {
        return modulName;
    }

    /**
     * Set the module name, which is by default &quot;OECM&quot;.
     * @param modulName String
     * @since 4.2.0.0
     */
    public void setModulName(String modulName)
    {
        this.modulName = modulName;
    }
}