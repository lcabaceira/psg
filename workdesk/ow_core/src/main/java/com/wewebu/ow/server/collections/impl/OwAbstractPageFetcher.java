/*
 * Inspired from chemistry-opencmi implementation. See org.apache.chemistry.opencmis.client.api.ItemIterable<t>  
 */
package com.wewebu.ow.server.collections.impl;

/**
 *<p>
 * Knows how to fetch a new page of items from a source/back-end.
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
 *@param <T> The type of the items that will be fetched. 
 *@since 4.2.0.0
 */
public abstract class OwAbstractPageFetcher<T>
{
    protected long maxNumItems;

    /**
     * 
     * @param maxNumItems the maximum number of items to retrieve. Used for paging.
     */
    protected OwAbstractPageFetcher(long maxNumItems)
    {
        this.maxNumItems = maxNumItems;
    }

    /**
     * @param skipCount
     * @return a single page of items.
     */
    protected abstract OwPage<T> fetchPage(long skipCount);

    /**
     * Create a new copy of this {@link OwAbstractPageFetcher} with a new size for page.
     * @param maxNumItems
     * @return A copy of this fetcher with an updated page size.
     */
    public abstract OwAbstractPageFetcher<T> newCopy(int maxNumItems);
}
