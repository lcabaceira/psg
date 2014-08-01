package com.wewebu.ow.server.fieldctrlimpl;

/**
 *<p>
 * Special date tagging interface for OwNextDaysDateControl.
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
public final class OwNextDaysDate extends OwRelativeDate
{

    /**
     * 
     */
    private static final long serialVersionUID = -7230137875856573582L;

    public OwNextDaysDate(int key_p)
    {
        super(key_p);
    }

    public OwNextDaysDate(long timestamp_p)
    {
        super(timestamp_p);
    }

}