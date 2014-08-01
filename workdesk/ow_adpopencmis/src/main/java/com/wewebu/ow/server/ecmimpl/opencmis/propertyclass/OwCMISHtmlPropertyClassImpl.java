package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Arrays;
import java.util.List;

import org.apache.chemistry.opencmis.commons.definitions.PropertyHtmlDefinition;

import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISSameTypeConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISJavaTextFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISStringTransparentTextFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * OwCMISHtmlPropertyClassImpl.
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
public class OwCMISHtmlPropertyClassImpl extends OwCMISAbstractNativePropertyClass<String, String, PropertyHtmlDefinition> implements OwCMISHtmlPropertyClass
{

    private static final List<Integer> OPERATORS = Arrays.asList(OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL, OwSearchOperator.CRIT_OP_IS_IN, OwSearchOperator.CRIT_OP_IS_NOT_IN, OwSearchOperator.CRIT_OP_LIKE,
            OwSearchOperator.CRIT_OP_NOT_LIKE);

    public OwCMISHtmlPropertyClassImpl(String className, PropertyHtmlDefinition propertyDefinition, OwCMISNativeObjectClass<?, ?> objectClass)
    {
        this(className, propertyDefinition, new OwCMISSameTypeConverter<String>(String.class), objectClass);
    }

    public OwCMISHtmlPropertyClassImpl(String className, PropertyHtmlDefinition propertyDefinition, OwCMISValueConverter<String, String> converter, OwCMISNativeObjectClass<?, ?> objectClass)
    {
        super(className, propertyDefinition, converter, String.class, OPERATORS, objectClass);
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
        return new OwCMISJavaTextFormat(new OwCMISStringTransparentTextFormat(), Boolean.class);
    }

}
