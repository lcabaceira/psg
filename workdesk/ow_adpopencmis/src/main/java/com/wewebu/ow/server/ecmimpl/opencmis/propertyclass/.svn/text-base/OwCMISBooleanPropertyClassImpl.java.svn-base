package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Arrays;
import java.util.List;

import org.apache.chemistry.opencmis.commons.definitions.PropertyBooleanDefinition;

import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISSameTypeConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISBooleanTextFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISJavaTextFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * OwCMISBooleanPropertyClassImpl.
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
public class OwCMISBooleanPropertyClassImpl extends OwCMISAbstractNativePropertyClass<Boolean, Boolean, PropertyBooleanDefinition> implements OwCMISBooleanPropertyClass
{

    private static final List<Integer> OPERATORS = Arrays.asList(OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL, OwSearchOperator.CRIT_OP_IS_IN, OwSearchOperator.CRIT_OP_IS_NOT_IN);

    public OwCMISBooleanPropertyClassImpl(String className, PropertyBooleanDefinition propertyDefinition, OwCMISNativeObjectClass<?, ?> objectClass)
    {
        this(className, propertyDefinition, new OwCMISSameTypeConverter<Boolean>(Boolean.class), objectClass);
    }

    public OwCMISBooleanPropertyClassImpl(String className, PropertyBooleanDefinition propertyDefinition, OwCMISValueConverter<Boolean, Boolean> converter, OwCMISNativeObjectClass<?, ?> objectClass)
    {
        super(className, propertyDefinition, converter, Boolean.class, OPERATORS, objectClass);

    }

    @Override
    public Object getMaxValue() throws Exception
    {
        return null;
    }

    @Override
    public Object getMinValue() throws Exception
    {
        return null;
    }

    @Override
    public OwCMISFormat getFormat()
    {
        return new OwCMISJavaTextFormat(new OwCMISBooleanTextFormat(), Boolean.class);
    }

}
