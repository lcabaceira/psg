package com.wewebu.ow.server.util;

import java.util.Iterator;

/**
 *<p>
 * Extends the interface for attribute bag Iterators, which describe the attribute of an object.
 * The additional Iterator interface allows browsing through a list of bags.
 * The next() Method returns a OwAttributeBag object.
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
 */
public interface OwAttributeBagIterator extends Iterator
{
    /** rewind the iterator to its start */
    public abstract void rewind() throws Exception;

    /** check if iterator supports rewind */
    public abstract boolean canRewind();

    /** get the count of items in the iterator
     * 
     * @return int length of the iterator or -1 if length could not be retrieved
     */
    public abstract int length();

}