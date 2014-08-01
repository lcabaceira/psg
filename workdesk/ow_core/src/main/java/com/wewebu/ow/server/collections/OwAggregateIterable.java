package com.wewebu.ow.server.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Trying to provide an aggregate {@link OwIterable}. 
 *</p>
 *<p>
 * WARNING: This implementation will break as soon as any of the aggregated {@link OwIterable}s do not know how to properly respond to {@link #getTotalNumItems()}.
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
public class OwAggregateIterable<T> extends OwIterableAttributeBag<T>
{
    private static final Logger LOG = OwLogCore.getLogger(OwAggregateIterable.class);

    private OwIterable<T>[] iterables;
    private Map<Integer, Long> totalNumItemsByIndex = new HashMap<Integer, Long>();

    private long skippedOffset;
    private long totalNumItems;
    private long maxPageSize;

    @SuppressWarnings("unchecked")
    public OwAggregateIterable(List<OwIterable<T>> iterables)
    {
        this(iterables.toArray(new OwIterable[] {}));
    }

    public OwAggregateIterable(OwIterable<T>... iterables)
    {
        this(0, false, iterables[0].getPageNumItems(), -1, iterables);
    }

    private OwAggregateIterable(long skippedOffset, boolean isPage, long maxPageSize, long totalNumItems, OwIterable<T>... iterables)
    {
        this.iterables = iterables;
        this.skippedOffset = skippedOffset;
        this.isPage = isPage;
        this.totalNumItems = totalNumItems;
        this.maxPageSize = maxPageSize;
        LOG.info("New OwAggregateIterable instance created.");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#skipTo(long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public OwIterable<T> skipTo(long position)
    {
        int iterableIndex = 0;
        long count = 0;
        for (int i = 0; i < this.iterables.length; i++)
        {
            count += getTotalNumItems(i);
            if (count >= position)
            {
                iterableIndex = i;
                break;
            }
        }

        if (position > count)
        {
            // we reached past the end
            return OwEmptyIterable.instance();
        }

        // next iterable should start in the middle of the Nth iterable. 
        long skipOffset = (getTotalNumItems(iterableIndex) - (count - position));

        List<OwIterable<T>> newIterables = new LinkedList<OwIterable<T>>();
        // add start iterable
        newIterables.add(this.iterables[iterableIndex].skipTo(skipOffset));
        // add the rest of iterables
        for (int i = iterableIndex + 1; i < this.iterables.length; i++)
        {
            newIterables.add(this.iterables[i]);
        }
        return new OwAggregateIterable<T>(skipOffset, isPage, maxPageSize, getTotalNumItems(), newIterables.toArray(new OwIterable[] {}));
    }

    /**
     * @param iterableIndex the index of the iterable in {@link #iterables} to count items for. 
     * @return the totalNumItems of the iterable.
     */
    private long getTotalNumItems(int iterableIndex)
    {
        OwIterable<T> owIterable = this.iterables[iterableIndex];
        long totalNumItems = owIterable.getTotalNumItems();
        if (totalNumItems < 0)
        {
            //try the cache
            Long cachedCount = this.totalNumItemsByIndex.get(Integer.valueOf(iterableIndex));
            if (null != cachedCount)
            {
                return cachedCount;
            }
            else
            {
                // count the elements and chache the result
                totalNumItems = 0;

                boolean finished = false;
                while (!finished)
                {
                    OwIterable<T> currentPage = owIterable.skipTo(totalNumItems).getPage();
                    for (T item : currentPage)
                    {
                        totalNumItems++;
                    }
                    if (!currentPage.getHasMoreItems())
                    {
                        finished = true;
                    }
                }

                this.totalNumItemsByIndex.put(Integer.valueOf(iterableIndex), totalNumItems);
            }
        }
        return totalNumItems;
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public OwIterable<T> getPage(int maxNumItems)
    {
        List<OwIterable<T>> pageIterables = new LinkedList<OwIterable<T>>();
        long count = getTotalNumItems(0) - skippedOffset;

        if (count >= maxNumItems)
        {
            pageIterables.add(this.iterables[0].getPage(maxNumItems));
        }
        else
        {
            pageIterables.add(this.iterables[0].getPage((int) count));
            for (int i = 1; i < this.iterables.length; i++)
            {
                long currentNumItems = getTotalNumItems(i);
                if (count < maxNumItems)
                {
                    count += currentNumItems;
                }
                else
                {
                    break;
                }
                if (count >= maxNumItems)
                {
                    // last iterable has to be truncated
                    long tail = maxNumItems - (count - currentNumItems);
                    pageIterables.add(this.iterables[i].getPage((int) tail));
                }
                else
                {
                    pageIterables.add(this.iterables[i]);
                }
            }
        }
        return new OwAggregateIterable(skippedOffset, true, maxNumItems, getTotalNumItems(), pageIterables.toArray(new OwIterable[] {}));
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#iterator()
     */
    @Override
    public Iterator<T> iterator()
    {
        List<Iterator<T>> iterators = new LinkedList<Iterator<T>>();
        for (Iterable<T> iterable : iterables)
        {
            iterators.add(iterable.iterator());
        }
        return new AggregateIterator<T>(iterators);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPageNumItems()
     */
    @Override
    public long getPageNumItems()
    {
        int index = 0;
        long numItems = this.iterables[index].getPageNumItems();

        while (numItems < maxPageSize && !this.iterables[index].getHasMoreItems() && ++index < this.iterables.length)
        {
            numItems += this.iterables[index].getPageNumItems();
        }

        numItems = Math.min(maxPageSize, numItems);

        return numItems;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getHasMoreItems()
     */
    @Override
    public boolean getHasMoreItems()
    {
        if (iterables[0].getHasMoreItems())
        {
            return true;
        }

        for (int i = 1; i < this.iterables.length; i++)
        {
            if (iterables[i].getTotalNumItems() > 0)
            {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getTotalNumItems()
     */
    @Override
    public long getTotalNumItems()
    {
        if (!isPage && totalNumItems < 0)
        {
            totalNumItems = 0;
            for (int i = 0; i < this.iterables.length; i++)
            {
                long numItems = getTotalNumItems(i);
                if (numItems < 0)
                {
                    totalNumItems = -1;
                    break;
                }
                totalNumItems += numItems;
            }
        }
        return totalNumItems;
    }

    private class AggregateIterator<T> implements Iterator<T>
    {
        private int iteratorIndex = 0;
        private List<Iterator<T>> iterators;

        private AggregateIterator(List<Iterator<T>> iterators)
        {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext()
        {
            Iterator<T> current = iterators.get(iteratorIndex);
            if (current.hasNext())
            {
                return true;
            }
            else if (iteratorIndex < iterators.size() - 1 && iterators.get(iteratorIndex + 1).hasNext())
            {
                return true;
            }
            return false;
        }

        @Override
        public T next()
        {
            Iterator<T> current = iterators.get(iteratorIndex);
            if (!current.hasNext() && iteratorIndex < iterators.size() - 1)
            {
                iteratorIndex++;
                current = iterators.get(iteratorIndex);
            }

            if (current.hasNext())
            {
                return current.next();
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Delete/Remove is not supported by this iterator");
        }
    }

    /**
     * Decides if we really need an Aggregate iterable or not for the given input list.
     * @param <IT> the type of iterables.
     * 
     * @param list
     * @return an {@link OwAggregateIterable} if needed or the first iterable in the list if there is only one element.
     * @since 4.2.0.0
     */
    public static <IT> OwIterable<IT> forList(List<OwIterable<IT>> list)
    {
        List<OwIterable<IT>> filteredList = new ArrayList<OwIterable<IT>>();

        //filter out empty iterables
        for (OwIterable<IT> owIterable : list)
        {
            if (owIterable.getTotalNumItems() != 0)
            {
                filteredList.add(owIterable);
            }
        }

        if (1 == filteredList.size())
        {
            return filteredList.get(0);
        }
        else if (filteredList.isEmpty())
        {
            return new OwEmptyIterable<IT>();
        }
        else
        {
            return new OwAggregateIterable<IT>(filteredList);
        }
    }
}