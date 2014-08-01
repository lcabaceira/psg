package com.wewebu.ow.server.ecm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Standard Implementation for objects lists from the network. Implements the SUN value list pattern.<br/><br/>
 * To be implemented with the specific ECM system.
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
public class OwStandardObjectCollection extends java.util.ArrayList implements OwObjectCollection
{
    private static final long serialVersionUID = 1L;

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardObjectCollection.class);

    /** sort operator used in */
    protected static class OwObjectCollectionComparator implements java.util.Comparator
    {
        /** construct a List comparator with the sort criteria
         * @param sortCriteria_p OwSort object with a list of criteria
         */
        public OwObjectCollectionComparator(OwSort.OwSortCriteria sortCriteria_p)
        {
            m_SortCriteria = sortCriteria_p;
        }

        /** Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
            The implementor must ensure that sgn(compare(x, y)) == -sgn(compare(y, x)) for all x and y. (This implies that compare(x, y) must throw an exception if and only if compare(y, x) throws an exception.)

            The implementor must also ensure that the relation is transitive: ((compare(x, y)>0) && (compare(y, z)>0)) implies compare(x, z)>0.

            Finally, the implementer must ensure that compare(x, y)==0 implies that sgn(compare(x, z))==sgn(compare(y, z)) for all z.
                
            It is generally the case, but not strictly required that (compare(x, y)==0) == (x.equals(y)). Generally speaking, any comparator that violates this condition should clearly indicate this fact. The recommended language is "Note: this comparator imposes orderings that are inconsistent with equals." 
         *
         * @param o1_p  the first object to be compared.
         * @param o2_p  the second object to be compared
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second. 
         */
        public int compare(Object o1_p, Object o2_p)
        {
            int iRet = 0;

            OwProperty prop1 = null;
            OwProperty prop2 = null;
            try
            {
                // get the properties to sort for
                prop1 = ((OwObject) o1_p).getProperty(m_SortCriteria.getPropertyName());
                prop2 = ((OwObject) o2_p).getProperty(m_SortCriteria.getPropertyName());

                Object value1 = prop1.getValue();
                Object value2 = prop2.getValue();
                //null.compare(null) ==> 0
                //null.compare(date) ==> -1
                //date.compare(null) ==> 1
                if ((value1 == null) && (value2 == null))
                {
                    iRet = 0;
                }
                else if (value1 == null)
                {
                    iRet = -1;
                }
                else if (value2 == null)
                {
                    iRet = 1;
                }
                else
                {
                    if (value1 instanceof String && value2 instanceof String)
                    {
                        String v1 = (String) value1, v2 = (String) value2;
                        v1 = v1.replaceAll(" ", "");
                        v2 = v2.replaceAll(" ", "");
                        iRet = v1.compareToIgnoreCase(v2);
                    }
                    else
                    {
                        iRet = prop1.compareTo(prop2);
                    }
                }

            }
            catch (Exception e)
            {
                if ((prop1 == null) && (prop2 == null))
                {
                    iRet = 0;
                }
                else if (prop1 == null)
                {
                    iRet = -1;
                }
                else
                {
                    iRet = 1;
                }
            }

            // switch sort upon descending flag
            if (!m_SortCriteria.getAscFlag())
            {
                return (-1 * iRet);
            }
            else
            {
                return iRet;
            }
        }

        /** OwSortCriteria object with sort criteria */
        private OwSort.OwSortCriteria m_SortCriteria;

    }

    /** sort the list by the given criteria.
     *  The default implementation sorts on the cached objects in the application server.
     *  The function may be overloaded to sort by the ECM System with SQL sort statements.
     *
     * @param sortCriteria_p list of sortcriteria
     */
    public void sort(OwSort sortCriteria_p) throws Exception
    {
        // iterate over criteria and sort each criteria
        Iterator it = sortCriteria_p.getCriteriaCollection().iterator();

        while (it.hasNext())
        {
            //ATTENTION: multiple criteria  sort correctness is ensured by merge-sort implementation of 
            //           Collections.sort (a stable sort algorithm is required). 
            //TODO: a multi-criteria comparator would ensure algorithm independence     
            Collections.sort(this, new OwObjectCollectionComparator((OwSort.OwSortCriteria) it.next()));
        }
    }

    /** check if object list has retrieved all objects
     *  If false, there are more objects available, but it is not guaranteed that you can retrieve them with the next / prev functions.
     *  I.e. hasNext / Prev might still return false. 
     *
     * @return boolean true = all available objects have been added to the list 
     */
    public boolean isComplete() throws Exception
    {
        if (m_Attributes != null)
        {
            Object o = m_Attributes.get(ATTRIBUTE_IS_COMPLETE);
            if (o != null)
            {
                if (o instanceof Boolean)
                {
                    return ((Boolean) o).booleanValue();
                }
                else
                {
                    if (o instanceof String)
                    {
                        String val = (String) o;
                        if (Boolean.TRUE.toString().equalsIgnoreCase(val))
                        {
                            return true;
                        }
                        if (Boolean.FALSE.toString().equalsIgnoreCase(val))
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return true;//default assumption is that collection is complete
    }

    /** set the complete flag */
    public void setComplete(boolean fComplete_p)
    {
        setAttribute(ATTRIBUTE_IS_COMPLETE, Boolean.valueOf(fComplete_p));
    }

    /** overridable to check if object list has access to more next objects, than currently added.
     *  I.e. if it could do another SQL Query and obtain more objects from the ECM System.
     *
     * @return boolean true = there are more objects available, call getMore() to retrieve additional objects.
     */
    public boolean hasNext() throws Exception
    {
        // default, no SQL query optimization implemented
        return false;
    }

    /** overridable to check if object list has access to more previous objects, than currently added.
     *  I.e. if it could do another SQL Query and obtain more objects from the ECM System.
     *
     * @return boolean true = there are more objects available, call getMore() to retrieve additional objects.
     */
    public boolean hasPrev() throws Exception
    {
        // default, no SQL query optimization implemented
        return false;
    }

    /** overridable to retrieve further objects, than currently added.
     *  I.e. submit another SQL Query and obtain more objects from the ECM System.
     *
     *  NOTE: The retrieved objects will replace the current objects in the list
     *
     */
    public void getNext() throws Exception
    {
        // no SQL query optimization implemented
    }

    /** overridable to retrieve further objects, than currently added.
     *  I.e. submit another SQL Query and obtain more objects from the ECM System.
     *
     *  NOTE: The retrieved objects will replace the current objects in the list
     */
    public void getPrev() throws Exception
    {
        // no SQL query optimization implemented
    }

    // === OwAttributeBag implementation
    /** map for the OwAttributeBag implementation */
    private Map m_Attributes = null;

    /** get a object at the specified index or throws OwObjectNotFoundException
     * 
     * @param iIndex_p int index
     * @return Object
     */
    public Object getAttribute(int iIndex_p) throws Exception
    {
        throw new OwInvalidOperationException("OwStandardObjectCollection.getAttribute(int iIndex_p): Not implemented, use getAttribute(String strName_p).");
    }

    /** get the attribute with the given name, returns default if not found. */
    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        if (null != m_Attributes)
        {
            Object obj = m_Attributes.get(strName_p);
            if (obj != null)
            {
                return obj;
            }
            else
            {
                return default_p;
            }
        }
        else
        {
            return default_p;
        }
    }

    /** get the attribute with the given name */
    public Object getAttribute(String strName_p) throws Exception
    {
        if (null != m_Attributes)
        {
            Object obj = m_Attributes.get(strName_p);
            if (obj != null)
            {
                return obj;
            }
            else
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwStandardObjectCollection.getAttribute(String strName_p): Object not found Exception, strName_p = " + strName_p);
                }
                throw new OwObjectNotFoundException("OwStandardObjectCollection.getAttribute(String strName_p): strName_p = " + strName_p);
            }
        }
        else
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwStandardObjectCollection.getAttribute(String strName_p): Object not found Exception, The map for the OwAttributeBag implementation (Attributes == null), strName_p = " + strName_p);
            }
            throw new OwObjectNotFoundException("OwStandardObjectCollection.getAttribute(String strName_p): The map for the OwAttributeBag implementation (Attributes == null), strName_p = " + strName_p);
        }
    }

    /** set the attribute with the given name */
    public void setAttribute(String strName_p, Object object_p)
    {
        if (null == m_Attributes)
        {
            m_Attributes = new HashMap();
        }

        m_Attributes.put(strName_p, object_p);
    }

    /** check if attribute exists */
    public boolean hasAttribute(String strName_p)
    {
        if (null != m_Attributes)
        {
            return m_Attributes.containsKey(strName_p);
        }
        else
        {
            return false;
        }
    }

    /** get all attribute names in the bag */
    public java.util.Collection getAttributeNames()
    {
        if (null != m_Attributes)
        {
            return m_Attributes.keySet();
        }
        else
        {
            return new ArrayList();
        }
    }

    public int attributecount()
    {
        return m_Attributes.size();
    }

    /**
     * Adds more elements to the list, making sure not to exceed iMaxSize_p elements.
     * @param c the elements to be added to this list
     * @param iMaxSize_p maximum number of elements that the list should contain.
     * @return true if the list has changed
     * @throws Exception 
     * @since 4.2.0.0 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean addAll(OwObjectCollection c, int iMaxSize_p) throws Exception
    {
        boolean changed = false;
        int currentSize = this.size();
        Iterator it = c.iterator();
        while (it.hasNext())
        {
            Object object = it.next();
            if (currentSize >= iMaxSize_p)
            {
                break;
            }
            this.add(object);
            changed = true;

        }
        if (it.hasNext() || !c.isComplete())
        {
            this.setComplete(false);
        }
        this.setAttribute(ATTRIBUTE_SIZE, this.size());
        return changed;
    }
}