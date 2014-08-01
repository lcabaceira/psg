package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import java.util.List;

import com.filenet.api.collection.EngineCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.converter.OwFNCM5EngineListFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5SimpleConverte.
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
public final class OwFNCM5SimpleConverter<N, O> implements OwFNCM5ValueConverter<N, O>
{
    /**
     *<p>
     * OwFNCM5ConverterExtension.
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
    public static interface OwFNCM5ConverterExtension<N, O>
    {
        N toNativeValue(O owdValue_p) throws OwException;
    }

    private OwFNCM5EngineListFactory<?> listFactory;
    private OwFNCM5ValueConverterClass<N, O> converterClass;
    private OwFNCM5ConverterExtension<N, O> extension;

    public OwFNCM5SimpleConverter(OwFNCM5EngineListFactory<?> collectionFactory_p, OwFNCM5ValueConverterClass<N, O> converterClass_p, OwFNCM5ConverterExtension<N, O> extension_p)
    {
        super();
        this.listFactory = collectionFactory_p;
        this.converterClass = converterClass_p;
        this.extension = extension_p;
    }

    public EngineCollection toEngineCollection(O[] owdArrayValue_p) throws OwException
    {
        EngineCollection nativeList = null;
        if (owdArrayValue_p != null)
        {
            nativeList = listFactory.createList();
            for (int j = 0; j < owdArrayValue_p.length; j++)
            {
                N nativeValue = toNativeValue(owdArrayValue_p[j]);
                ((List<N>) nativeList).add(nativeValue);
            }
        }
        return nativeList;
    }

    public N toNativeValue(O owdValue_p) throws OwException
    {
        return extension.toNativeValue(owdValue_p);
    }

    public O[] fromEngineCollection(EngineCollection engineCollection_p) throws OwException
    {
        return converterClass.convertEngineCollection(engineCollection_p);
    }

    public O fromNativeValue(N nativeValue_p) throws OwException
    {
        return converterClass.convertNativeValue(nativeValue_p);
    }

}
