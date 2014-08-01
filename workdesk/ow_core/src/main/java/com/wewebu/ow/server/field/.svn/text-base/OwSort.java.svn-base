package com.wewebu.ow.server.field;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *<p>
 * Sorts contain a list of sort criteria.
 * Sort is used with OwObjectList.sort(...) and OwNetwork.doSearch(...) Function.<br/>
 * Automatically keeps the size of the list to a specified maximum, when using addCriteria(...).<br/><br/>
 * To be extended with the specific DMS system.
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
public class OwSort
{
    /** list containing the sort OwSortCriteria*/
    protected LinkedList<OwSortCriteria> m_SortList;

    /** the maximum number of sort criteria in the sort list default is 1 */
    protected int m_iMax;

    /** default sort order for new criteria */
    protected boolean m_fDefaultAsc;

    /** constructs empty sort criteria list with maxsize = 1 and default sort order asc = true
     */
    public OwSort()
    {
        this(1, true);
    }

    /** constructs empty sort criteria list with specified maxsize
     *  if the number of sort criteria exceeds the maximum value, the first sort criteria gets removed
     * @param iMaxSize_p the maximum number of sort criteria in the sort list.
     * @param fAsc_p sort order true = ascending, false = descending
     */
    public OwSort(int iMaxSize_p, boolean fAsc_p)
    {
        m_iMax = iMaxSize_p;
        m_fDefaultAsc = fAsc_p;
        m_SortList = new LinkedList<OwSortCriteria>();
    }

    /** get the maximum number of sort criteria in the sort list */
    public int getMaxSize()
    {
        return m_iMax;
    }

    /** get default sort order for new criteria */
    public boolean getDefaultAsc()
    {
        return m_fDefaultAsc;
    }

    /** set the maximum number of sort criteria in the sort list.
     *  if the number of sort criteria exceeds the maximum value, the first sort criteria gets removed
     *  @param iMax_p maximum number of sort criteria
     */
    public void setMaxSize(int iMax_p)
    {
        if (iMax_p < 0)
        {
            throw new IllegalArgumentException("Provide max size is lower then 0 (zero)!");
        }
        m_iMax = iMax_p;
        reduceToMaxSize();
    }

    /** get the size of the criteria list
     * @return int size
     */
    public int getSize()
    {
        return m_SortList.size();
    }

    /** get a collection over the criteria. The returned collection has predictable order.
     * @return Collection of OwSortCriteria Elements
     */
    public Collection<OwSortCriteria> getCriteriaCollection()
    {
        return m_SortList;
    }

    /** add a sort criteria to the sort
     * @param criteria_p OwSortCriteria to add
     *
     */
    public void addCriteria(OwSortCriteria criteria_p)
    {
        // add criteria
        m_SortList.add(criteria_p);
    }

    /** remove the specified criteria
     *  @param strPropertyName_p Property name or criteria to remove
     */
    public void removeCriteria(String strPropertyName_p)
    {
        Iterator<OwSortCriteria> critIt = m_SortList.iterator();
        while (critIt.hasNext())
        {
            OwSortCriteria criteria = critIt.next();
            if (criteria.getPropertyName().equals(strPropertyName_p))
            {
                critIt.remove();
                return;
            }
        }
    }

    /** get the last criteria, i.e. the criteria with the highest priority
     *  @return OwSortCriteria if found, null otherwise
     */
    public OwSortCriteria getLastCriteria()
    {
        if (m_SortList.size() > 0)
        {
            return m_SortList.getLast();
        }
        else
        {
            return null;
        }
    }

    /** get the specified criteria
     *  @param strPropertyName_p Property name to sort for
     *  @return OwSortCriteria if found, null otherwise
     */
    public OwSortCriteria getCriteria(String strPropertyName_p)
    {
        // iterate over the list
        for (OwSortCriteria criteria : m_SortList)
        {
            if (criteria.getPropertyName().equals(strPropertyName_p))
            {
                return criteria;
            }
        }

        return null;
    }

    /** toggle existing criteria or add a criteria to the sortlist if it does not yet exist
     *  if the number of sort criteria exceeds the maximum value, the first sort criteria gets removed
     *  @param strPropertyName_p Property name to sort for
     */
    public void toggleCriteria(String strPropertyName_p)
    {
        // === look if we have already got the property in the list
        Iterator<OwSortCriteria> it = m_SortList.iterator();
        while (it.hasNext())
        {
            OwSortCriteria criteria = it.next();
            if (criteria.getPropertyName().equals(strPropertyName_p))
            {
                // we want the selected element to show up first, so we must remove and insert it again.
                it.remove();
                m_SortList.add(criteria);

                // toggle
                criteria.m_fAsc = !criteria.m_fAsc;
                return;
            }
        }

        // === add criteria
        m_SortList.add(new OwSortCriteria(strPropertyName_p, m_fDefaultAsc));
        reduceToMaxSize();
    }

    /** get the priority of the criteria, last criteria has highest priority
     * 
     * @param criteria_p
     * @return int priority > 0, or 0 if no priority is undefined
     */
    public int getPriority(OwSortCriteria criteria_p)
    {
        int priority = m_SortList.indexOf(criteria_p);
        return priority + 1;
    }

    /** toggle existing criteria or add a criteria to the sort list if it does not yet exist
     *  if the number of sort criteria exceeds the maximum value, the first sort criteria gets removed
     *  @param strPropertyName_p Property name to sort for
     */
    public void setCriteria(String strPropertyName_p, boolean fAsc_p)
    {
        // === look if we have already got the property in the list
        Iterator<OwSortCriteria> critIt = m_SortList.iterator();
        while (critIt.hasNext())
        {
            OwSortCriteria criteria = critIt.next();
            if (criteria.getPropertyName().equals(strPropertyName_p))
            {
                // we want the selected element to show up first, so we must remove and insert it again.
                critIt.remove();
                m_SortList.add(criteria);

                // toggle
                criteria.m_fAsc = fAsc_p;
                return;
            }
        }

        // === add criteria
        m_SortList.add(new OwSortCriteria(strPropertyName_p, fAsc_p));
        reduceToMaxSize();
    }

    public String toString()
    {
        StringBuilder ret = new StringBuilder();

        ret.append("m_fDefaultAsc = [");
        ret.append(m_fDefaultAsc);

        ret.append("], m_iMax = [");
        ret.append(m_iMax);

        ret.append("], m_SortList = [");
        ret.append(m_SortList);
        ret.append("]");

        return ret.toString();
    }

    /**
     * Returns an Iterator with priority specific order of the OwSortCriteria,
     * beginning with the highest down to lowest.
     * @return Iterator of current OwSortCriteria
     * @since 4.2.0.0
     */
    public Iterator<OwSortCriteria> getPrioritizedIterator()
    {
        return m_SortList.descendingIterator();
    }

    /**
     * Will remove the first entries until it is equal {@link #getMaxSize()}.
     * If it current size is smaller nothing will be processed.
     * @since 4.2.0.0
     */
    protected void reduceToMaxSize()
    {
        // === make sure we do not exceed max size before adding a new property
        while (m_SortList.size() > m_iMax)
        {
            // remove the first elements until max size fell below by one 
            m_SortList.removeFirst();
        }
    }

    /**
     *<p>
     * Sort criteria tuple containing property and order direction.
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
    public static class OwSortCriteria
    {
        /** constructs a sort criteria for the specified property
         * @param strPropertyName_p the property to sort for
         * @param fAsc_p sort order true = ascending, false = descending
         */
        public OwSortCriteria(String strPropertyName_p, boolean fAsc_p)
        {
            m_strPropertyName = strPropertyName_p;
            m_fAsc = fAsc_p;
        }

        /** name of the property which should be used in the sort */
        protected String m_strPropertyName;
        /** sort direction  true = ascending, false = descending */
        protected boolean m_fAsc;

        /** get the name of the property which should be used in the sort*/
        public String getPropertyName()
        {
            return m_strPropertyName;
        }

        /** get the sort direction  true = ascending, false = descending */
        public boolean getAscFlag()
        {
            return m_fAsc;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof OwSortCriteria)
            {
                return ((OwSortCriteria) obj).getPropertyName().equals(getPropertyName());
            }
            return false;
        }

        public String toString()
        {
            StringBuilder ret = new StringBuilder();

            ret.append("PropertyName = [");
            ret.append(m_strPropertyName);

            ret.append("], Asc = [");
            ret.append(m_fAsc);

            ret.append("]");

            return ret.toString();
        }

    }
}