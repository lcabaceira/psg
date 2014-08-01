/*
 * Inspired from chemistry-opencmi implementation. See org.apache.chemistry.opencmis.client.api.ItemIterable<t>  
 */
package com.wewebu.ow.server.collections.impl;

import java.util.List;

/**
 *<p>
 * Iterator over the entire collection (not only one page).
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
public class OwCollectionIterator<T> extends OwAbstractIterator<T>
{

    public OwCollectionIterator(long skipCount, OwAbstractPageFetcher<T> pageFetcher)
    {
        super(skipCount, pageFetcher);
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
        if (items != null && getSkipOffset() < items.size())
        {
            return true;
        }

        if (!getHasMoreItems())
        {
            return false;
        }

        long totalItems = getTotalNumItems();
        if (totalItems < 0)
        {
            // we don't know better
            return true;
        }

        return (getSkipCount() + getSkipOffset()) < totalItems;
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
        if (items == null || items.isEmpty())
        {
            return null;
        }

        if (getSkipOffset() == items.size())
        {
            page = incrementPage();
            items = (page == null) ? null : page.getItems();
        }

        if (items == null || items.isEmpty() || getSkipOffset() == items.size())
        {
            return null;
        }

        return items.get(incrementSkipOffset());
    }
}
