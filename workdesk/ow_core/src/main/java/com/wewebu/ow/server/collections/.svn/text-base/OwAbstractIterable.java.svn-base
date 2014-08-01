package com.wewebu.ow.server.collections;

import com.wewebu.ow.server.collections.impl.OwAbstractIterator;
import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;

/**
 *<p>
 * Abstarct implementation of {@link OwIterable}.
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
public abstract class OwAbstractIterable<T> extends OwIterableAttributeBag<T>
{
    private final OwAbstractPageFetcher<T> pageFetcher;
    private final long skipCount;

    private OwAbstractIterator<T> iterator;

    protected OwAbstractIterable(long skipCount, OwAbstractPageFetcher<T> pageFetcher)
    {
        this.skipCount = skipCount;
        this.pageFetcher = pageFetcher;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getTotalNumItems()
     */
    @Override
    public long getTotalNumItems()
    {
        return getIterator().getTotalNumItems();
    }

    protected OwAbstractIterator<T> getIterator()
    {
        if (this.iterator == null)
        {
            this.iterator = createIterator();
        }
        return this.iterator;
    }

    protected OwAbstractPageFetcher<T> getPageFetcher()
    {
        return pageFetcher;
    }

    protected long getSkipCount()
    {
        return skipCount;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPageNumItems()
     */
    @Override
    public long getPageNumItems()
    {
        return getIterator().getPageNumItems();
    }

    public boolean getHasMoreItems()
    {
        return getIterator().getHasMoreItems();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#skipTo(long)
     */
    @Override
    public OwIterable<T> skipTo(long position)
    {
        return new OwCollectionIterable<T>(position, pageFetcher);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage()
     */
    @Override
    public OwIterable<T> getPage()
    {
        return new OwCollectionPageIterable<T>(skipCount, pageFetcher);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage(int)
     */
    @Override
    public OwIterable<T> getPage(int maxNumItems)
    {

        OwAbstractPageFetcher<T> newPageFetcher = this.pageFetcher.newCopy(maxNumItems);
        return new OwCollectionPageIterable<T>(skipCount, newPageFetcher);
    }

    /**
     * Sublcasses should create a specific iterator to itterate on the entire collection or on sub-collections/pages only.
     * @return an instance of {@link OwAbstractIterator} used to iterate this collection.
     */
    protected abstract OwAbstractIterator<T> createIterator();
}
