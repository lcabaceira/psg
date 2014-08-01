package com.wewebu.ow.server.ecmimpl.owdummy.ui;

import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Sample implementation of a default value control field for <code>Double</code> input fields.
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
public class OwDummyDoubleDefaultControl extends OwDefaultValueFieldControl
{
    /**the default value to be set*/
    private static final Double DEFAULT_DOUBLE_VALUE = new Double(1.6180339887498948482);

    protected Object createDefaultValue(OwField field_p, String id_p) throws Exception
    {
        OwFieldDefinition definition = field_p.getFieldDefinition();

        String javaClassName = definition.getJavaClassName();
        if (Double.class.getName().equals(javaClassName))
        {
            if (definition.isArray())
            {
                Object[] currentValue = (Object[]) field_p.getValue();
                Object[] defaultValue;
                if (currentValue == null || currentValue.length == 0)
                {
                    defaultValue = new Double[1];
                }
                else
                {
                    defaultValue = new Double[currentValue.length];
                }
                for (int i = 0; i < defaultValue.length; i++)
                {
                    defaultValue[i] = DEFAULT_DOUBLE_VALUE;
                }
                return defaultValue;
            }
            else
            {
                return DEFAULT_DOUBLE_VALUE;
            }
        }
        else
        {
            return null;
        }
    }
}