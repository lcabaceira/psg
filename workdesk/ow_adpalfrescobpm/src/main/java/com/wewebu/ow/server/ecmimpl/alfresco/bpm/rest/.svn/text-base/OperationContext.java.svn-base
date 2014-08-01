package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

/**
 *<p>
 * Defines the paging,sorting and filtering for a request that will return a collection.
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
public class OperationContext
{
    public static final long DEF_MAX_ITEMS = 100;

    private long skipCount;
    private long maxItems;

    public OperationContext(long skipCount, long maxItems)
    {
        this.skipCount = skipCount;
        this.maxItems = maxItems;
    }

    public OperationContext()
    {
        this(0, DEF_MAX_ITEMS);
    }

    public long getSkipCount()
    {
        return skipCount;
    }

    public void setSkipCount(long skipCount)
    {
        this.skipCount = skipCount;
    }

    public long getMaxItems()
    {
        return maxItems;
    }

    public void setMaxItems(long maxItems)
    {
        this.maxItems = maxItems;
    }
}
