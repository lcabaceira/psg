package com.wewebu.ow.server.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * An {@link OwIterable} implementation that accepts a filtering criteria.
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
public class OwFilteringIterable<T> extends OwIterableAttributeBag<T>
{
    private static final Logger LOG = OwLogCore.getLogger(OwFilteringIterable.class);

    private OwIterable<T> iterable;
    private OwItemFilter<T> filter;
    private long pageSize;

    private long count;
    private boolean exactCalculation;

    public OwFilteringIterable(OwIterable<T> iterable, OwItemFilter<T> filter)
    {
        this(iterable, filter, iterable.getPageNumItems());
    }

    public OwFilteringIterable(OwIterable<T> iterable, OwItemFilter<T> filter, long pageSize)
    {
        this(iterable, filter, pageSize, false);
    }

    public OwFilteringIterable(OwIterable<T> iterable, OwItemFilter<T> filter, long pageSize, boolean exactCalculation)
    {
        this.iterable = iterable;
        this.filter = filter;
        this.pageSize = pageSize;
        this.exactCalculation = exactCalculation;
        this.count = -1l;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#skipTo(long)
     */
    @Override
    public OwIterable<T> skipTo(long position)
    {
        if (this.iterable.getTotalNumItems() >= 0 && position > this.iterable.getTotalNumItems())
        {
            // This is clearly out of our collection
            return OwEmptyIterable.instance();
        }

        long positionToSkip = -1;
        long acceptedItems = 0;
        OwIterable<T> it = iterable.skipTo(0l);
        for (T item : it)
        {
            if (this.filter.accept(item))
            {
                acceptedItems++;
            }
            positionToSkip++;
            if (acceptedItems == position + 1)
            {
                break;
            }
        }
        if (acceptedItems < position + 1)
        {
            LOG.warn("OFilteringIterable.skipTo: Could not jump to possition = " + position + " available items = " + acceptedItems);
            return OwEmptyIterable.instance();
        }
        else
        {
            OwIterable<T> newIterable = this.iterable.skipTo(positionToSkip);
            return new OwFilteringIterable<T>(newIterable, this.filter, this.getPageNumItems());
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage()
     */
    @Override
    public OwIterable<T> getPage()
    {
        return this.getPage((int) this.getPageNumItems());
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public OwIterable<T> getPage(int maxNumItems)
    {
        OwObjectCollection pageItems = new OwStandardObjectCollection();
        int pageItemsCount = 0;
        Iterator<T> iterator = this.iterator();

        while (pageItemsCount < maxNumItems && iterator.hasNext())
        {
            T nextItem = iterator.next();
            pageItems.add(nextItem);
            pageItemsCount++;
        }

        return new OwObjectCollectionIterableAdapter<T>(pageItems, iterator.hasNext(), maxNumItems);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#iterator()
     */
    @Override
    public Iterator<T> iterator()
    {
        return new FilteringIterator<T>(this.iterable.iterator(), this.filter);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPageNumItems()
     */
    @Override
    public long getPageNumItems()
    {
        return pageSize;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getHasMoreItems()
     */
    @Override
    public boolean getHasMoreItems()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getTotalNumItems()
     */
    @Override
    public long getTotalNumItems()
    {
        if (isExcatCalculation() && -1 == this.count)
        {
            this.count = 0;

            long totalCount = 0;
            boolean finished = false;
            while (!finished)
            {
                OwIterable<T> currentPage = this.iterable.skipTo(totalCount).getPage();
                for (T item : currentPage)
                {
                    if (this.filter.accept(item))
                    {
                        count++;
                    }
                    totalCount++;
                }
                if (!currentPage.getHasMoreItems())
                {
                    finished = true;
                }
            }
        }
        return this.count;
    }

    /**
     * Define if filter should be accessed for total count calculation.
     * @param calculation boolean
     * @see #getTotalNumItems()
     */
    public void setExactCalculation(boolean calculation)
    {
        this.exactCalculation = calculation;
    }

    /**
     * Is calculation enabled for {@link #getTotalNumItems()}
     * @return boolean (by default false)
     * @see #setExactCalculation(boolean)
     */
    public boolean isExcatCalculation()
    {
        return this.exactCalculation;
    }

    private class FilteringIterator<O> implements Iterator<O>
    {
        private Iterator<O> iterator;
        private OwItemFilter<O> filter;

        private O nextElement = null;
        private long itemCount = 0;

        private FilteringIterator(Iterator<O> iterator, OwItemFilter<O> filter)
        {
            this.iterator = iterator;
            this.filter = filter;
        }

        @Override
        public boolean hasNext()
        {
            if (null != this.nextElement)
            {
                return true;
            }

            O result = null;
            while (this.iterator.hasNext())
            {
                result = this.iterator.next();
                if (this.filter.accept(result))
                {
                    this.nextElement = result;
                    return true;
                }
            }
            return false;
        }

        @Override
        public O next()
        {
            this.itemCount++;
            O result = this.nextElement;
            this.nextElement = null;

            if (null != result)
            {
                return result;
            }
            else
            {
                // Move on
                while (this.iterator.hasNext())
                {
                    result = this.iterator.next();
                    if (this.filter.accept(result))
                    {
                        return result;
                    }
                }
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
