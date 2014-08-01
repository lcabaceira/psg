package com.wewebu.ow.server.fieldctrlimpl;

/**
 *<p>
 * Special date tagging class for OwLastDaysDateControl.
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
public final class OwLastDaysDate extends OwRelativeDate
{

    /**
     * 
     */
    private static final long serialVersionUID = -6655459336300780574L;

    public OwLastDaysDate(int key_p)
    {
        super(key_p);
    }

    public OwLastDaysDate(long timestamp_p)
    {
        super(timestamp_p);
    }
}