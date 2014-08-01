/*
 * Inspired from chemistry-opencmi implementation. See org.apache.chemistry.opencmis.client.runtime.util.CollectionPageIterable<T>  
 */
package com.wewebu.ow.server.collections;

import java.util.Iterator;

import com.wewebu.ow.server.collections.impl.OwAbstractIterator;
import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwCollectionPageIterator;

/**
 *<p>
 * Iterable over a single page of objects.
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
public class OwCollectionPageIterable<T> extends OwAbstractIterable<T>
{

    protected OwCollectionPageIterable(long skipCount, OwAbstractPageFetcher<T> pageFetcher)
    {
        super(skipCount, pageFetcher);
    }

    /* (non-Javadoc)
    * @see com.wewebu.ow.server.collections.OwAbstractIterable#createIterator()
    */
    @Override
    protected OwAbstractIterator<T> createIterator()
    {
        return new OwCollectionPageIterator<T>(getSkipCount(), getPageFetcher());
    }

    @Override
    public Iterator<T> iterator()
    {
        OwCollectionPageIterator<T> myIterator = (OwCollectionPageIterator<T>) getIterator();
        return new OwCollectionPageIterator<T>(myIterator);
    }
}
