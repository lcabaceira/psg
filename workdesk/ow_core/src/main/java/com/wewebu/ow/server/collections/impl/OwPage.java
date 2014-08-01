/*
 * Inspired from chemistry-opencmi implementation. See org.apache.chemistry.opencmis.client.api.ItemIterable<t>  
 */
package com.wewebu.ow.server.collections.impl;

import java.math.BigInteger;
import java.util.List;

/**
 *<p>
 * Container for a single page worth of items.
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
 * @param <T> The type of items contained in this page.
 * @since 4.2.0.0
 */
public class OwPage<T>
{
    private final List<T> items;
    private final Long totalNumItems;
    private final Boolean hasMoreItems;

    public OwPage(List<T> items, BigInteger totalNumItems, Boolean hasMoreItems)
    {
        this.items = items;
        this.totalNumItems = totalNumItems == null ? null : Long.valueOf(totalNumItems.longValue());
        this.hasMoreItems = hasMoreItems;
    }

    public OwPage(List<T> items, long totalNumItems, boolean hasMoreItems)
    {
        this.items = items;
        this.totalNumItems = Long.valueOf(totalNumItems);
        this.hasMoreItems = Boolean.valueOf(hasMoreItems);
    }

    public List<T> getItems()
    {
        return items;
    }

    public Long getTotalNumItems()
    {
        return totalNumItems;
    }

    public Boolean getHasMoreItems()
    {
        return hasMoreItems;
    }
}
