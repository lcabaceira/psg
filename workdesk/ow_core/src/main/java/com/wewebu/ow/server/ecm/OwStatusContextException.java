package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Exception thrown, when a function does not support the requested status context value.
 * See also {@link OwStatusContextDefinitions}
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
public class OwStatusContextException extends OwNotSupportedException
{
    /**
     * 
     */
    private static final long serialVersionUID = -2391980454407635152L;

    /** construct a exception with a message */
    public OwStatusContextException(String strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message */
    public OwStatusContextException(OwString strMessage_p)
    {
        super(strMessage_p);
    }

    /** construct a exception with a message and Throwable */
    public OwStatusContextException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    /** construct a exception with a message and Throwable */
    public OwStatusContextException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

}