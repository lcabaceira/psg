package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import com.wewebu.ow.server.ecmimpl.fncm5.converter.OwFNCM5EngineListFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5SimpleConverter.OwFNCM5ConverterExtension;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;

/**
 *<p>
 * OwFNCM5MetaConverterClass<N, O>.
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
public abstract class OwFNCM5MetaConverterClass<N, O> extends OwFNCM5SimpleConverterClass<N, O> implements OwFNCM5ConverterExtension<N, O>
{
    private OwFNCM5SimpleConverter<N, O> simpleConverter;

    public OwFNCM5MetaConverterClass(Class<O> oClass_p, OwFNCM5EngineListFactory<?> listFactory_p)
    {
        super(oClass_p);
        this.simpleConverter = new OwFNCM5SimpleConverter<N, O>(listFactory_p, this, this);
    }

    public OwFNCM5ValueConverter<N, O> createConverter(OwFNCM5EnginePropertyClass<?, N, O> class_p)
    {
        return this.simpleConverter;
    }

}
