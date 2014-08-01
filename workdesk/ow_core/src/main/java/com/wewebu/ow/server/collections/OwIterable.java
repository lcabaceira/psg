package com.wewebu.ow.server.collections;

import java.util.Iterator;

import com.wewebu.ow.server.util.OwAttributeBag;

/**
 *<p>
 * An iterable implementation with support for paging.
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
public interface OwIterable<T> extends Iterable<T>, OwAttributeBag
{

    /**
     * @param position This is relative to the start of the whole collection of items. It is not interpreted as relative to the start of a page.
     * @return iterable whose starting point is the specified skip to position.
     *         This iterable <em>may</em> be the same as {@code this}.
     */
    OwIterable<T> skipTo(long position);

    /**
     * Gets an iterable for the current sub collection within the CMIS collection using
     * default maximum number of items
     *
     * @return iterable for current page
     */
    OwIterable<T> getPage();

    /**
     * Gets an iterable for the current sub collection within the CMIS collection
     *
     * @param maxNumItems
     *            maximum number of items the sub collection will contain
     *
     * @return iterable for current page
     */
    OwIterable<T> getPage(int maxNumItems);

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    Iterator<T> iterator();

    /**
     * Returns the number of items fetched for the current page.
     *
     * @return number of items for currently fetched collection
     */
    long getPageNumItems();

    /**
     * Returns whether the repository contains additional items beyond the page of
     * items already fetched.
     *
     * @return true => further page requests will be made to the repository
     */
    boolean getHasMoreItems();

    /**
     * This will return a &quot;guestimation&quot;
     * since parts/pages will be retrieved on request. 
     * @return total number of items or (-1) if not known
     */
    long getTotalNumItems();
}
