package com.wewebu.ow.server.plug.owrecord.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.filter.OwComparator;
import com.wewebu.ow.server.fieldimpl.filter.OwBoolComparator;
import com.wewebu.ow.server.fieldimpl.filter.OwDateComparator;
import com.wewebu.ow.server.fieldimpl.filter.OwNumberComparator;
import com.wewebu.ow.server.fieldimpl.filter.OwStringComparator;

/**
 *<p>
 * Helper class for filter handling.
 * This class execute the filtering and instantiation of
 * filter classes.
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
public class OwFilterHelper
{
    HashMap<String, OwComparator<?>> map;

    public OwFilterHelper()
    {
        map = new HashMap<String, OwComparator<?>>();
    }

    /**
     * Call start a filter process on the given collection using the provided filter.
     * <p>A OwFilterRuntimeException can occur if the property, property value or
     * property java type retrieval fails.</p>
     * @param filter OwSearchCriteria to use
     * @param collection List of OwObject's
     * @return List of OwObjects matching the given filter
     * @throws ClassNotFoundException if could not retrieve java type information of property value 
     */
    public List filterCollection(OwSearchCriteria filter, List collection) throws ClassNotFoundException
    {
        LinkedList retCol = new LinkedList();
        Iterator it = collection.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            if (fitFilter(filter, obj))
            {
                retCol.add(obj);
            }
        }

        return retCol;
    }

    /**
     * Check if the object matches the given filter.
     * <p>A OwFilterRuntimeException can occur if the request for property, property value or
     * property java type fails.</p>
     * @param filter OwSearchCriteria which objects must match
     * @param obj OwObject to verify against filter
     * @return true only if object is match the filter
     * @throws ClassNotFoundException if could not retrieve java type information of property value 
     */
    protected boolean fitFilter(OwSearchCriteria filter, OwObject obj) throws ClassNotFoundException
    {
        OwProperty prop;
        try
        {
            prop = obj.getProperty(filter.getClassName());
        }
        catch (OwObjectNotFoundException onfex)
        {
            return false;
        }
        catch (Exception e)
        {
            throw new OwFilterRuntimeException("Cannot retrieve property object!", e);
        }

        return fitFilter(filter, prop);
    }

    /**
     * Check the property against a defined filter.
     * <p>A OwFilterRuntimeException can occur if the retrieval of property value or
     * property java type fails.</p>
     * @param filter OwSearchCriteria representing a filter
     * @param prop OwProperty whose value should be verified
     * @return boolean true only if
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected boolean fitFilter(OwSearchCriteria filter, OwProperty prop) throws ClassNotFoundException
    {
        Object value = null;
        try
        {
            value = prop.getValue();
        }
        catch (Exception e)
        {
            throw new OwFilterRuntimeException("Cannot retrieve value from property!", e);
        }

        String className;
        try
        {
            className = prop.getPropertyClass().getJavaClassName();
        }
        catch (Exception e)
        {
            throw new OwFilterRuntimeException("Cannot retrieve java representation type of property value.", e);
        }
        OwComparator compare = getComparator(className);
        return compare.match(filter, value);
    }

    /**
     * Retrieve and cache a {@link OwComparator} for a given class name.
     * @param javaClassName String full qualified java class name
     * @return OwComparator to be used for given class
     * @throws ClassNotFoundException
     */
    protected OwComparator getComparator(String javaClassName) throws ClassNotFoundException
    {
        OwComparator compare;
        if (map.containsKey(javaClassName))
        {
            compare = map.get(javaClassName);
        }
        else
        {
            Class clazz = Class.forName(javaClassName);
            compare = createComparator(clazz);
            map.put(javaClassName, compare);
        }
        return compare;
    }

    /**
     * Method to instantiate a comparator for a given class.
     * @param clazz Class which should be handled by the OwComparator instance
     * @return OwComparator which can handle the given class.
     */
    public OwComparator<?> createComparator(Class<?> clazz)
    {
        if (Date.class.isAssignableFrom(clazz))
        {
            return new OwDateComparator();
        }

        if (Boolean.class.isAssignableFrom(clazz))
        {
            return new OwBoolComparator();
        }

        if (Number.class.isAssignableFrom(clazz))
        {
            return new OwNumberComparator();
        }
        else
        {//Default
            return new OwStringComparator();
        }
    }
}
