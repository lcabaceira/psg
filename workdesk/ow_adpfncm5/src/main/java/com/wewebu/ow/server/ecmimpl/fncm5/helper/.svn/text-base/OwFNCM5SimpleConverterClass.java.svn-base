package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;

import com.filenet.api.collection.EngineCollection;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5SimpleConverterClass<N, O>.
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
public abstract class OwFNCM5SimpleConverterClass<N, O> implements OwFNCM5ValueConverterClass<N, O>
{
    private final O[] emptyJavaArray;
    private Class<O> oClass;

    public OwFNCM5SimpleConverterClass(Class<O> oClass_p)
    {
        super();
        this.oClass = oClass_p;
        this.emptyJavaArray = (O[]) Array.newInstance(this.oClass, 0);
    }

    public O[] convertEngineCollection(EngineCollection engineCollection_p) throws OwException
    {

        O[] owdArray = null;
        if (engineCollection_p != null)
        {
            LinkedList<O> owdList = new LinkedList<O>();
            Iterator i = engineCollection_p.iterator();
            while (i.hasNext())
            {
                N value = (N) i.next();
                O owdValue = convertNativeValue(value);
                owdList.add(owdValue);
            }
            owdArray = owdList.toArray(emptyJavaArray);
        }
        return owdArray;
    }

    public Class<O> getOClass()
    {
        return oClass;
    }

}
