/*
 * Inspired from chemistry-opencmi implementation. See org.apache.chemistry.opencmis.client.runtime.util.CollectionIterable<T>  
 */
package com.wewebu.ow.server.collections;

import java.util.Iterator;

import com.wewebu.ow.server.collections.impl.OwAbstractIterator;
import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwCollectionIterator;

/**
 *<p>
 * Iterable over the entire collection of objects.
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
public class OwCollectionIterable<T> extends OwAbstractIterable<T>
{

    protected OwCollectionIterable(long skipCount, OwAbstractPageFetcher<T> pageFetcher)
    {
        super(skipCount, pageFetcher);
    }

    public OwCollectionIterable(OwAbstractPageFetcher<T> pageFetcher)
    {
        super(0, pageFetcher);
    }

    /* (non-Javadoc)
    * @see com.wewebu.ow.server.collections.OwAbstractIterable#createIterator()
    */
    @Override
    protected OwAbstractIterator<T> createIterator()
    {
        return new OwCollectionIterator<T>(getSkipCount(), getPageFetcher());
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#iterator()
     */
    @Override
    public Iterator<T> iterator()
    {
        // We always return a fresh one.
        return createIterator();
    }
}
