package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Arrays;
import java.util.List;

import org.apache.chemistry.opencmis.commons.definitions.PropertyIdDefinition;

import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISJavaTextFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISStringTransparentTextFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * OwCMISIdPropertyClassImpl.
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
public class OwCMISIdPropertyClassImpl<O> extends OwCMISAbstractNativePropertyClass<O, String, PropertyIdDefinition> implements OwCMISIdPropertyClass<O>
{

    private static final List<Integer> OPERATORS = Arrays.asList(OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL, OwSearchOperator.CRIT_OP_IS_IN, OwSearchOperator.CRIT_OP_IS_NOT_IN, OwSearchOperator.CRIT_OP_LIKE,
            OwSearchOperator.CRIT_OP_NOT_LIKE);

    public OwCMISIdPropertyClassImpl(String className, PropertyIdDefinition propertyDefinition, OwCMISValueConverter<String, O> converter, Class<O> javaClass, OwCMISNativeObjectClass<?, ?> objectClass)
    {
        super(className, propertyDefinition, converter, javaClass, OPERATORS, objectClass);

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
        return new OwCMISJavaTextFormat(new OwCMISStringTransparentTextFormat(), String.class);
    }

}
