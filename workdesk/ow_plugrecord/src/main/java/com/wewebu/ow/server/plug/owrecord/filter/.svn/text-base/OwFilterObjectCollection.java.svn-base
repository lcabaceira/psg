package com.wewebu.ow.server.plug.owrecord.filter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Filter collection.
 * Filters an {@link OwObjectCollection} based on a {@link OwSearchNode} filter setting.<br />
 * This class does not modifies the base collection, and works with the objects 
 * from the base collection.
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
 *@since 3.2.0.0
 */
public class OwFilterObjectCollection extends LinkedList implements OwObjectCollection
{
    private static final long serialVersionUID = -377363099299349546L;
    private OwSearchNode filter;
    private OwObjectCollection nativeCol;

    public OwFilterObjectCollection(OwObjectCollection nativeCollection)
    {
        this(nativeCollection, null);
    }

    public OwFilterObjectCollection(OwObjectCollection nativeCollection, OwSearchNode filter)
    {
        this.nativeCol = nativeCollection;
        this.filter = filter;
    }

    public void clear()
    {
        super.clear();
        this.filter = null;
    }

    public Object getAttribute(int iIndex_p) throws Exception
    {
        return getNativeCollection() == null ? null : getNativeCollection().getAttribute(iIndex_p);
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        return getNativeCollection() == null ? null : getNativeCollection().getAttribute(strName_p);
    }

    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        return getNativeCollection() == null ? null : getNativeCollection().getSafeAttribute(strName_p, default_p);
    }

    public boolean hasAttribute(String strName_p)
    {
        return getNativeCollection() == null ? false : getNativeCollection().hasAttribute(strName_p);
    }

    public int attributecount()
    {
        return getNativeCollection() == null ? 0 : getNativeCollection().attributecount();
    }

    public Collection getAttributeNames()
    {
        return getNativeCollection() == null ? null : getNativeCollection().getAttributeNames();
    }

    public boolean isComplete() throws Exception
    {
        return false;
    }

    public boolean hasNext() throws Exception
    {
        return false;
    }

    public boolean hasPrev() throws Exception
    {
        return false;
    }

    public void getNext() throws Exception
    {
    }

    public void getPrev() throws Exception
    {
    }

    public void sort(OwSort sortCriteria_p) throws Exception
    {
        getNativeCollection().sort(sortCriteria_p);
    }

    /**
     * Returns the collection which is used to 
     * represent only the entries which are matching
     * the filter.
     * @return OwObjectCollection
     */
    public OwObjectCollection getNativeCollection()
    {
        return this.nativeCol;
    }

    /**
     * Set a filter which should be processed on the the native collection.
     * <p>Will trigger filter list action, if given parameter is not null.</p>
     * @param filter OwSearchNode
     * @see #processFilterList(List)
     * @see #getNativeCollection()
     */
    @SuppressWarnings("unchecked")
    public void setFilter(OwSearchNode filter)
    {
        this.filter = filter;
        super.clear();
        if (filter != null)
        {
            processFilterList(filter.getCriteriaList(OwSearchNode.FILTER_NONE));
        }
    }

    /**
     * Method to check if a filter was set.
     * <p>Check is done on null value, 
     * empty filter setting is not concerned.</p>
     * @return true only if filter is not null, else false
     */
    public boolean hasFilter()
    {
        return this.filter != null;
    }

    /**
     * Return the current search node which
     * is used as filter, can return null if no
     * filter is set.
     * @return OwSearchNode or null
     */
    public OwSearchNode getFilter()
    {
        return this.filter;
    }

    /**(overridable)
     * Method called to process the filter action based on the given list of criteria.
     * <p>Can throw a OwRuntimeException if there is a problem during filter process.</p>
     * @param filters List of {@link OwSearchCriteria}
     */
    protected void processFilterList(List<OwSearchCriteria> filters)
    {
        OwFilterHelper helper = new OwFilterHelper();
        List result = getNativeCollection();
        for (OwSearchCriteria crit : filters)
        {
            try
            {
                result = helper.filterCollection(crit, result);
            }
            catch (ClassNotFoundException e)
            {
                throw new OwFilterRuntimeException("Filtering collection faild.", e);
            }
        }
        if (result != null && result.size() > 0)
        {
            addAll(result);
        }
    }

}