package com.wewebu.expression.language;

/**
 *<p>
 * OwExprException.
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
public class OwExprException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 5178777258210357773L;

    public OwExprException()
    {
        super();
    }

    public OwExprException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    public OwExprException(String message_p)
    {
        super(message_p);
    }

    public OwExprException(Throwable cause_p)
    {
        super(cause_p);
    }

}
