package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Thrown when a unique constraint is not satisfied, like unique names for the documents in a folder etc.
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
@SuppressWarnings("serial")
public class OwDuplicateException extends OwException
{
    public OwDuplicateException(String strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    public OwDuplicateException(OwString strMessage_p, Throwable cause_p)
    {
        super(strMessage_p, cause_p);
    }

    @Override
    public String getModulName()
    {
        return "ow_core";
    }
}
