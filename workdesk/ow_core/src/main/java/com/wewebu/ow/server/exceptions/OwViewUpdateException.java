package com.wewebu.ow.server.exceptions;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Specific exception to throw if an onUpdate call end exceptional.
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
public class OwViewUpdateException extends OwRuntimeException
{
    /** generate Java serial version UID */
    private static final long serialVersionUID = -7844513815096701831L;
    private String modulName;

    public OwViewUpdateException(String message_p)
    {
        super(message_p);
    }

    public OwViewUpdateException(String message_p, Throwable throwable_p)
    {
        super(message_p, throwable_p);
    }

    public OwViewUpdateException(OwString message_p)
    {
        super(message_p);
    }

    public OwViewUpdateException(OwString message_p, Throwable throwable_p)
    {
        super(message_p, throwable_p);
    }

    @Override
    public String getModulName()
    {
        return modulName == null ? "owd.core" : modulName;
    }

    public void setModulName(String moduleName)
    {
        this.modulName = moduleName;
    }

}
