package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.chemistry.opencmis.commons.definitions.PropertyDateTimeDefinition;

import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISISOSimpleFormatAdapter;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISJavaTextFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 * OwCMISDateTimePropertyClassImpl.
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
public class OwCMISDateTimePropertyClassImpl extends OwCMISAbstractNativePropertyClass<Date, GregorianCalendar, PropertyDateTimeDefinition> implements OwCMISDateTimePropertyClass
{

    private static final List<Integer> OPERATORS = Arrays.asList(OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL, OwSearchOperator.CRIT_OP_LESS, OwSearchOperator.CRIT_OP_LESS_EQUAL, OwSearchOperator.CRIT_OP_GREATER,
            OwSearchOperator.CRIT_OP_GREATER_EQUAL, OwSearchOperator.CRIT_OP_IS_IN, OwSearchOperator.CRIT_OP_IS_NOT_IN);

    public OwCMISDateTimePropertyClassImpl(String className, PropertyDateTimeDefinition propertyDefinition, OwCMISValueConverter<GregorianCalendar, Date> converter, OwCMISNativeObjectClass<?, ?> objectClass)
    {
        super(className, propertyDefinition, converter, Date.class, OPERATORS, objectClass);
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
        OwCMISISOSimpleFormatAdapter adapter = new OwCMISISOSimpleFormatAdapter();
        return new OwCMISJavaTextFormat(adapter, Date.class);
    }

}
