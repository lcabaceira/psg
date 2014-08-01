package com.wewebu.ow.server.ecmimpl.fncm5.converter;

import java.util.Date;

import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5MetaConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Generic converter for the same type conversion.
 * Simply transforms multiple value collections into
 * different representations.
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
public class OwFNCM5SameTypeConverterClass<O> extends OwFNCM5MetaConverterClass<O, O>
{
    public static final OwFNCM5SameTypeConverterClass<Byte[]> BYTE_CONVERTER = new OwFNCM5SameTypeConverterClass<Byte[]>(Byte[].class, OwFNCM5EngineListFactory.BinaryList);
    public static final OwFNCM5SameTypeConverterClass<Boolean> BOOLEAN_CONVERTER = new OwFNCM5SameTypeConverterClass<Boolean>(Boolean.class, OwFNCM5EngineListFactory.BooleanList);
    public static final OwFNCM5SameTypeConverterClass<Date> DATE_CONVERTER = new OwFNCM5SameTypeConverterClass<Date>(Date.class, OwFNCM5EngineListFactory.DateTimeList);
    public static final OwFNCM5SameTypeConverterClass<Double> DOUBLE_CONVERTER = new OwFNCM5SameTypeConverterClass<Double>(Double.class, OwFNCM5EngineListFactory.Float64List);
    public static final OwFNCM5SameTypeConverterClass<Integer> INTEGER_CONVERTER = new OwFNCM5SameTypeConverterClass<Integer>(Integer.class, OwFNCM5EngineListFactory.Integer32List);
    public static final OwFNCM5SameTypeConverterClass<String> STRING_CONVERTER = new OwFNCM5SameTypeConverterClass<String>(String.class, OwFNCM5EngineListFactory.StringList);

    public OwFNCM5SameTypeConverterClass(Class<O> oClass_p, OwFNCM5EngineListFactory<?> listFactory_p)
    {
        super(oClass_p, listFactory_p);

    }

    public O toNativeValue(O owdValue_p) throws OwException
    {
        return owdValue_p;
    }

    public O convertNativeValue(O nativeValue_p) throws OwException
    {
        return nativeValue_p;
    }

}
