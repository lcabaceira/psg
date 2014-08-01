package com.wewebu.ow.server.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *<p>
 * This is kind of "null object" implementation for {@link OwIterable}.
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
public class OwEmptyIterable<T> extends OwIterableAttributeBag<T>
{
    @SuppressWarnings("rawtypes")
    private static OwEmptyIterable INSTANCE = new OwEmptyIterable();

    @SuppressWarnings("unchecked")
    public static <U> OwEmptyIterable<U> instance()
    {
        return INSTANCE;
    }

    public OwEmptyIterable<T> skipTo(long position)
    {
        if (position != 0)
        {
            throw new IllegalArgumentException(String.valueOf(position));
        }
        return this;
    }

    public OwEmptyIterable<T> getPage()
    {
        return this;
    }

    public OwEmptyIterable<T> getPage(int maxNumItems)
    {
        return this;
    }

    @SuppressWarnings("unchecked")
    public Iterator<T> iterator()
    {
        return (Iterator<T>) EmptyIterator.INSTANCE;
    }

    public long getPageNumItems()
    {
        return 0;
    }

    public boolean getHasMoreItems()
    {
        return false;
    }

    public long getTotalNumItems()
    {
        return 0;
    }

    /**
     * An empty iterator.<br />
     * Since JDK does not provide a public default implementation.
     * Use EmptyIterator.INSTANCE to avoid new Instances of EmptyIterator. 
     */
    public static final class EmptyIterator<V> implements Iterator<V>
    {
        public static final Iterator<?> INSTANCE = new EmptyIterator<Object>();

        public boolean hasNext()
        {
            return false;
        }

        public V next()
        {
            throw new NoSuchElementException("EmptyIterator, use hasNext() before calling next()");
        }

        public void remove()
        {
            throw new UnsupportedOperationException("EmptyIterator: Delete/Remove not supported");
        }
    }

}