package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception defining that a value does not match.
 * Is thrown in case a value does not match, or is not part of a value range.
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
 *@since 4.2.0.0
 */
public class OwInvalidValueException extends OwInvalidOperationException
{
    /**generated serial Version UID */
    private static final long serialVersionUID = -5604915888190807301L;

    public OwInvalidValueException(String strMessage_p)
    {
        super(strMessage_p);
    }

    public OwInvalidValueException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    public OwInvalidValueException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    public OwInvalidValueException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }
}
