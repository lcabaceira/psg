package com.wewebu.ow.server.ecmimpl;

import com.wewebu.ow.server.ecm.OwReason;

/**
 *<p>
 * Standard/Default implementation of a OwReason object.
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
 *@since 3.0.0.0
 */
public class OwStandardReason implements OwReason
{
    private boolean allowed;
    private String reasonDescription;

    /**Default instance for allow with empty reason description */
    public static final OwStandardReason ALLOWED = new OwStandardReason(Boolean.TRUE.booleanValue(), OwReason.EMPTY_REASON_DESCRIPTION);

    public OwStandardReason(boolean isAllowed_p, String description_p)
    {
        this.allowed = isAllowed_p;
        this.reasonDescription = description_p;
    }

    public String getReasonDescription()
    {
        return this.reasonDescription == null ? OwReason.EMPTY_REASON_DESCRIPTION : this.reasonDescription;
    }

    public boolean isAllowed()
    {
        return allowed;
    }

}