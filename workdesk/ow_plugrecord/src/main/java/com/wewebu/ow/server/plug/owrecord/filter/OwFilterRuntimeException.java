package com.wewebu.ow.server.plug.owrecord.filter;

import com.wewebu.ow.server.exceptions.OwRuntimeException;

/**
 *<p>
 * Exception for filter process.
 * This exception is thrown if the during the filter process
 * an runtime dependent problem occurs or the filter process
 * cannot proceed further.
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
 *@since 3.2.0.0
 */
public class OwFilterRuntimeException extends OwRuntimeException
{
    /** Generated serial version UID*/
    private static final long serialVersionUID = -8198733261864838360L;

    public OwFilterRuntimeException(String message_p)
    {
        super(message_p);
    }

    public OwFilterRuntimeException(String msg_p, Throwable ex_p)
    {
        super(msg_p, ex_p);
    }

    @Override
    public String getModulName()
    {
        return "Filter";
    }

}
