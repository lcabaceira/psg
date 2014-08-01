package com.wewebu.ow.server.collections;

import java.util.Iterator;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;

/**
 *<p>
 * Adapter from {@link OwObjectCollection} to {@link OwIterable}.
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
public class OwObjectCollectionIterableAdapter<T> extends OwIterableAttributeBag<T>
{
    private static final int DEFAULT_PAGE_SIZE = 10;

    private OwObjectCollection owCollection;
    private long position;
    private int maxNumItems;
    private Boolean hasMoreItems = Boolean.FALSE;

    public OwObjectCollectionIterableAdapter(OwObjectCollection owCollection)
    {
        this(owCollection, 0, DEFAULT_PAGE_SIZE);
    }

    public OwObjectCollectionIterableAdapter(OwObjectCollection owCollection, int maxNumItems)
    {
        this(owCollection, 0, maxNumItems);
    }

    public OwObjectCollectionIterableAdapter(OwObjectCollection owCollection, boolean hasMoreItems)
    {
        this(owCollection, hasMoreItems, DEFAULT_PAGE_SIZE);
    }

    public OwObjectCollectionIterableAdapter(OwObjectCollection owCollection, boolean hasNext, int maxNumItems)
    {
        this.owCollection = owCollection;
        this.maxNumItems = maxNumItems;
        this.hasMoreItems = hasNext;
    }

    private OwObjectCollectionIterableAdapter(OwObjectCollection owCollection, long position, int maxNumItems)
    {
        this.owCollection = owCollection;
        this.position = position;
        this.maxNumItems = maxNumItems;
        this.hasMoreItems = Boolean.valueOf(this.position + this.maxNumItems < this.owCollection.size());
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#skipTo(long)
     */
    @Override
    public OwIterable<T> skipTo(long position)
    {
        return new OwObjectCollectionIterableAdapter<T>(this.owCollection, position, this.maxNumItems);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage()
     */
    @SuppressWarnings("unchecked")
    @Override
    public OwIterable<T> getPage()
    {
        int start = (int) this.position;
        int end = (int) (this.position + this.maxNumItems);
        if (end > this.owCollection.size())
        {
            end = this.owCollection.size();
        }
        OwObjectCollection subCollection = new OwStandardObjectCollection();
        subCollection.addAll(this.owCollection.subList(start, end));

        OwObjectCollectionIterableAdapter<T> page = new OwObjectCollectionIterableAdapter<T>(subCollection, 0, this.maxNumItems);
        page.hasMoreItems = Boolean.valueOf(end < this.owCollection.size());
        page.isPage = true;
        return page;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwIterable<T> getPage(int maxNumItems)
    {
        int start = (int) this.position;
        int end = (int) (this.position + maxNumItems);
        if (end > this.owCollection.size())
        {
            end = this.owCollection.size();
        }
        OwObjectCollection subCollection = new OwStandardObjectCollection();
        subCollection.addAll(this.owCollection.subList(start, end));

        OwObjectCollectionIterableAdapter<T> page = new OwObjectCollectionIterableAdapter<T>(subCollection, 0, maxNumItems);
        page.hasMoreItems = Boolean.valueOf(end < this.owCollection.size());
        page.isPage = true;
        return page;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator()
    {
        return this.owCollection.subList((int) this.position, this.owCollection.size()).iterator();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPageNumItems()
     */
    @Override
    public long getPageNumItems()
    {
        return this.maxNumItems;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getHasMoreItems()
     */
    @Override
    public boolean getHasMoreItems()
    {
        return this.hasMoreItems;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getTotalNumItems()
     */
    @Override
    public long getTotalNumItems()
    {
        return this.owCollection.size();
    }

}
