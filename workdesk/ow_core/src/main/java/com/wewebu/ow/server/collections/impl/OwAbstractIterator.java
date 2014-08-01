/*
 * Inspired from chemistry-opencmi implementation. See org.apache.chemistry.opencmis.client.api.ItemIterable<t>  
 */
package com.wewebu.ow.server.collections.impl;

import java.util.Iterator;
import java.util.List;

/**
 *<p>
 * Abstract implementation of a java iterator for pageable collections.
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
public abstract class OwAbstractIterator<T> implements Iterator<T>
{
    private long skipCount;
    private int skipOffset;
    private final OwAbstractPageFetcher<T> pageFetcher;

    private Long totalNumItems;
    protected OwPage<T> page;
    private Boolean hasMoreItems;

    protected OwAbstractIterator(long skipCount, OwAbstractPageFetcher<T> pageFetcher)
    {
        this.skipCount = skipCount;
        this.pageFetcher = pageFetcher;
    }

    public long getTotalNumItems()
    {
        if (totalNumItems == null)
        {
            totalNumItems = Long.valueOf(-1);
            OwPage<T> currentPage = getCurrentPage();
            if (null != currentPage)
            {
                if (null != currentPage.getTotalNumItems())
                {
                    totalNumItems = currentPage.getTotalNumItems();
                }
            }
        }
        return this.totalNumItems.longValue();
    }

    public boolean getHasMoreItems()
    {
        if (hasMoreItems == null)
        {
            hasMoreItems = Boolean.FALSE;
            OwPage<T> page = getCurrentPage();
            if (page != null)
            {
                if (page.getHasMoreItems() != null)
                {
                    hasMoreItems = page.getHasMoreItems();
                }
            }
        }
        return hasMoreItems.booleanValue();
    }

    /**
     * Gets the current page of items within collection
     *
     * @return current page
     */
    protected OwPage<T> getCurrentPage()
    {
        if (page == null)
        {
            page = pageFetcher.fetchPage(skipCount);
        }
        return page;
    }

    public long getPageNumItems()
    {
        OwPage<T> page = getCurrentPage();
        if (page != null)
        {
            List<T> items = page.getItems();
            if (items != null)
            {
                return items.size();
            }
        }
        return 0L;

    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    protected long getSkipCount()
    {
        return skipCount;
    }

    protected int getSkipOffset()
    {
        return skipOffset;
    }

    protected OwPage<T> incrementPage()
    {
        skipCount += skipOffset;
        skipOffset = 0;
        totalNumItems = null;
        hasMoreItems = null;
        page = pageFetcher.fetchPage(skipCount);
        return page;
    }

    protected int incrementSkipOffset()
    {
        return skipOffset++;
    }

    protected OwAbstractPageFetcher<T> getPageFetcher()
    {
        return pageFetcher;
    }
}