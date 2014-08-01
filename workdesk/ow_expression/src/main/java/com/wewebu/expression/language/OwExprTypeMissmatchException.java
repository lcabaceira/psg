package com.wewebu.expression.language;

/**
 *<p>
 * OwExprTypeMissmatchException.
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
public class OwExprTypeMissmatchException extends OwExprException
{

    /**
     * 
     */
    private static final long serialVersionUID = -652628939497214785L;

    public OwExprTypeMissmatchException()
    {
        super();
    }

    public OwExprTypeMissmatchException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    public OwExprTypeMissmatchException(String message_p)
    {
        super(message_p);
    }

    public OwExprTypeMissmatchException(Throwable cause_p)
    {
        super(cause_p);
    }

}
