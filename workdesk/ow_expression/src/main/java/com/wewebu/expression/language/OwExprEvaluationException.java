package com.wewebu.expression.language;

/**
 *<p>
 * OwExprEvaluationException.
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
public class OwExprEvaluationException extends OwExprException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public OwExprEvaluationException()
    {
        super();
    }

    public OwExprEvaluationException(String message_p, Throwable cause_p)
    {
        super(message_p, cause_p);
    }

    public OwExprEvaluationException(String message_p)
    {
        super(message_p);
    }

    public OwExprEvaluationException(Throwable cause_p)
    {
        super(cause_p);
    }

}
