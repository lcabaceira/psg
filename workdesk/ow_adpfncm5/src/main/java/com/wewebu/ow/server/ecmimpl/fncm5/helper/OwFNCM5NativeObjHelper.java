package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import com.filenet.api.core.IndependentObject;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.PropertyFilter;

/**
 *<p>
 * Helper class for native API, to retrieve and guarantee the existence of properties.
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
public class OwFNCM5NativeObjHelper
{

    /**
     * Simply retrieve the property with given nativeObj
     * @param nativeObj com.filenet.api.core.IndependentObject
     * @param prop String
     * @param refresh boolean refresh local property
     */
    public static void retrieveProperties(IndependentObject nativeObj, String prop, boolean refresh)
    {
        retrieveProperties(nativeObj, new String[] { prop }, refresh);
    }

    /**
     * Fetch an amount of properties for given object.
     * @param nativeObj com.filenet.api.core.IndependentObject
     * @param props String array of property names to retrieve.
     * @param refresh boolean refresh local property
     */
    public static void retrieveProperties(IndependentObject nativeObj, String[] props, boolean refresh)
    {
        PropertyFilter propFilter = new PropertyFilter();
        propFilter.setMaxRecursion(5);
        for (String name : props)
        {
            propFilter.addIncludeProperty(0, null, null, name, null);
        }
        if (refresh)
        {
            nativeObj.refresh(propFilter);
        }
        else
        {
            fetchProperties(nativeObj, propFilter);
        }
    }

    /**
     * Will check the object if the request property 
     * is already available in native object, if not it will be retrieved.
     * @param nativeObj com.filenet.api.core.IndependentObject to be used for retrieval
     * @param props String array of property names
     */
    public static void ensure(IndependentObject nativeObj, String[] props)
    {
        PropertyFilter propFilter = new PropertyFilter();
        propFilter.setMaxRecursion(5);
        for (String name : props)
        {
            if (nativeObj.getProperties().find(name) == null)
            {
                propFilter.addIncludeProperty(0, null, null, name, null);
            }
        }
        if (propFilter.getIncludeProperties().length > 0)
        {
            fetchProperties(nativeObj, propFilter);
        }
    }

    /**
     * Verify that the requested property is really available
     * in the native object. If not it will be retrieved from back-end.
     * @param nativeObj com.filenet.api.core.IndependentObject to be used for retrieval
     * @param prop String array of property names
     */
    public static void ensure(IndependentObject nativeObj, String prop)
    {
        ensure(nativeObj, new String[] { prop });
    }

    /**
     * Will not check if property filter is null or empty and will call
     * the native fetchProperties method.
     * <p>Handling the ExceptionCode.API_FETCH_MERGE_PROPERTY_ERROR, and
     * refresh the object again before executing the fetchProperties again.</p>
     * @param nativeObj IndependentObject
     * @param propFilter PropertyFilter
     */
    private static void fetchProperties(IndependentObject nativeObj, PropertyFilter propFilter)
    {
        try
        {
            nativeObj.fetchProperties(propFilter);
        }
        catch (EngineRuntimeException ex)
        {
            if (ExceptionCode.API_FETCH_MERGE_PROPERTY_ERROR.equals(ex.getExceptionCode()))
            {
                nativeObj.refresh();
                nativeObj.fetchProperties(propFilter);
            }
            else
            {
                throw ex;
            }
        }
    }

}
