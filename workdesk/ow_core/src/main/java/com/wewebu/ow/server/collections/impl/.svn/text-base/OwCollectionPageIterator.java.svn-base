/*
 * Inspired from chemistry-opencmi implementation. See org.apache.chemistry.opencmis.client.api.ItemIterable<T>  
 */
package com.wewebu.ow.server.collections.impl;

import java.util.List;

/**
 *<p>
 * Iterator over a single page. It will stop when reaching the end of the current page.
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
public class OwCollectionPageIterator<T> extends OwAbstractIterator<T>
{

    public OwCollectionPageIterator(long skipCount, OwAbstractPageFetcher<T> pageFetcher)
    {
        super(skipCount, pageFetcher);
    }

    /**
     * Create a new Page Iterator with the same configuration like this one.
     * If a page was already fetched, use it without fetching it again.
     * @param iterator
     */
    public OwCollectionPageIterator(OwCollectionPageIterator<T> iterator)
    {
        super(iterator.getSkipCount(), iterator.getPageFetcher());
        this.page = iterator.getCurrentPage();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
        OwPage<T> page = getCurrentPage();
        if (page == null)
        {
            return false;
        }

        List<T> items = page.getItems();
        if (items == null || getSkipOffset() >= items.size())
        {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Iterator#next()
     */
    public T next()
    {
        OwPage<T> page = getCurrentPage();
        if (page == null)
        {
            return null;
        }

        List<T> items = page.getItems();
        if (items == null || items.isEmpty() || getSkipOffset() == items.size())
        {
            return null;
        }

        return items.get(incrementSkipOffset());
    }
}
