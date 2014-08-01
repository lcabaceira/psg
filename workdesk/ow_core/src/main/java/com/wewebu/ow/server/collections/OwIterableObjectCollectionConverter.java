package com.wewebu.ow.server.collections;

import java.util.Iterator;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * {@link OwIterable} based {@link OwObjectCollection} implementation. 
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
public class OwIterableObjectCollectionConverter extends OwStandardObjectCollection
{
    private static final long serialVersionUID = 1L;

    public OwIterableObjectCollectionConverter(OwIterable<?> iterable, long skip) throws OwException
    {
        this(iterable, skip, -1);
    }

    /**
     * Constructs an OwObjectCollection over an Iterable. 
     * @param iterable
     * @param skip
     * @param maxItemsCount - the maximum number of items to pickup from the iterable. If -1 then all elements will be grabbed.
     */
    @SuppressWarnings("unchecked")
    public OwIterableObjectCollectionConverter(OwIterable<?> iterable, long skip, int maxItemsCount) throws OwException
    {
        OwIterable<?> page = null;

        int index = 0;
        page = iterable.skipTo(skip);
        Iterator<?> subIterator = page.iterator();
        try
        {
            while (subIterator.hasNext())
            {
                if (maxItemsCount >= 0 && maxItemsCount == index)
                {
                    break;
                }
                Object object = subIterator.next();
                add(object);
                index++;
            }

            setAttribute(ATTRIBUTE_SIZE, size());
            setAttribute(ATTRIBUTE_IS_COMPLETE, subIterator.hasNext() ? Boolean.FALSE : Boolean.TRUE);
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalid iterable conversion request.", e);
        }
    }
}
