package com.wewebu.expression.language;

/**
 *<p>
 * OwExprCastException.   
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
public class OwExprCastException extends OwExprException
{

    /**
     * 
     */
    private static final long serialVersionUID = -590425340529596677L;

    public OwExprCastException()
    {
        super();
    }

    public OwExprCastException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    public OwExprCastException(String message_p)
    {
        super(message_p);
    }

    public OwExprCastException(Throwable cause_p)
    {
        super(cause_p);
    }

}
