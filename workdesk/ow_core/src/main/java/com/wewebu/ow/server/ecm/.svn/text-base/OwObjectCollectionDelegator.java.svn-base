package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * An object list wrapper/delegate class.
 * Delegates all behavior to the inner delegate.
 * Subclasses can override certain object list methods thus lists can have their behavior "decorated".
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
 *@since 2.5.3.1
 */
public class OwObjectCollectionDelegator implements OwObjectCollection
{

    /**wrapped object list*/
    private OwObjectCollection m_delegatee;

    /**
     * Constructor
     * @param delegateeCollection_p
     */
    public OwObjectCollectionDelegator(OwObjectCollection delegateeCollection_p)
    {
        super();
        m_delegatee = delegateeCollection_p;
    }

    /**
     * 
     * @param objectCollection_p the collection to replace the inner delegate with
     */
    protected void replaceDelegatee(OwObjectCollection objectCollection_p)
    {
        this.m_delegatee = objectCollection_p;
    }

    public void add(int index_p, Object element_p)
    {
        m_delegatee.add(index_p, element_p);
    }

    public boolean add(Object o_p)
    {
        return m_delegatee.add(o_p);
    }

    public boolean addAll(Collection c_p)
    {
        return m_delegatee.addAll(c_p);
    }

    public boolean addAll(int index_p, Collection c_p)
    {
        return m_delegatee.addAll(index_p, c_p);
    }

    public int attributecount()
    {
        return m_delegatee.attributecount();
    }

    public void clear()
    {
        m_delegatee.clear();
    }

    public boolean contains(Object o_p)
    {
        return m_delegatee.contains(o_p);
    }

    public boolean containsAll(Collection c_p)
    {
        return m_delegatee.containsAll(c_p);
    }

    public boolean equals(Object o_p)
    {
        return m_delegatee.equals(o_p);
    }

    public Object get(int index_p)
    {
        return m_delegatee.get(index_p);
    }

    public Object getAttribute(int index_p) throws Exception
    {
        return m_delegatee.getAttribute(index_p);
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        return m_delegatee.getAttribute(strName_p);
    }

    public Collection getAttributeNames()
    {
        return m_delegatee.getAttributeNames();
    }

    public void getNext() throws Exception
    {
        m_delegatee.getNext();
    }

    public void getPrev() throws Exception
    {
        m_delegatee.getPrev();
    }

    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        return m_delegatee.getSafeAttribute(strName_p, default_p);
    }

    public boolean hasAttribute(String strName_p)
    {
        return m_delegatee.hasAttribute(strName_p);
    }

    public int hashCode()
    {
        return m_delegatee.hashCode();
    }

    public boolean hasNext() throws Exception
    {
        return m_delegatee.hasNext();
    }

    public boolean hasPrev() throws Exception
    {
        return m_delegatee.hasPrev();
    }

    public int indexOf(Object o_p)
    {
        return m_delegatee.indexOf(o_p);
    }

    public boolean isComplete() throws Exception
    {
        return m_delegatee.isComplete();
    }

    public boolean isEmpty()
    {
        return m_delegatee.isEmpty();
    }

    public Iterator iterator()
    {
        return m_delegatee.iterator();
    }

    public int lastIndexOf(Object o_p)
    {
        return m_delegatee.lastIndexOf(o_p);
    }

    public ListIterator listIterator()
    {
        return m_delegatee.listIterator();
    }

    public ListIterator listIterator(int index_p)
    {
        return m_delegatee.listIterator(index_p);
    }

    public Object remove(int index_p)
    {
        return m_delegatee.remove(index_p);
    }

    public boolean remove(Object o_p)
    {
        return m_delegatee.remove(o_p);
    }

    public boolean removeAll(Collection c_p)
    {
        return m_delegatee.removeAll(c_p);
    }

    public boolean retainAll(Collection c_p)
    {
        return m_delegatee.retainAll(c_p);
    }

    public Object set(int index_p, Object element_p)
    {
        return m_delegatee.set(index_p, element_p);
    }

    public int size()
    {
        return m_delegatee.size();
    }

    public void sort(OwSort sortCriteria_p) throws Exception
    {
        m_delegatee.sort(sortCriteria_p);
    }

    public List subList(int fromIndex_p, int toIndex_p)
    {
        return m_delegatee.subList(fromIndex_p, toIndex_p);
    }

    public Object[] toArray()
    {
        return m_delegatee.toArray();
    }

    public Object[] toArray(Object[] a_p)
    {
        return m_delegatee.toArray(a_p);
    }

    public String toString()
    {
        return "OwObjectCollectionDelegator->[" + (m_delegatee == null ? "<null>" : m_delegatee.toString() + "]");
    }

}