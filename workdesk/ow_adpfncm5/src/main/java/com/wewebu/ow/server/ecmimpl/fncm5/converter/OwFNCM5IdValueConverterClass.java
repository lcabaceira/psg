package com.wewebu.ow.server.ecmimpl.fncm5.converter;

import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5MetaConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Specific Id property converter.
 * Converts a P8 5.0 Id value into a java.lang.String representation
 * and vice versa. 
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
public class OwFNCM5IdValueConverterClass extends OwFNCM5MetaConverterClass<Id, String>
{

    public static final OwFNCM5IdValueConverterClass CLASS = new OwFNCM5IdValueConverterClass();

    public OwFNCM5IdValueConverterClass()
    {
        super(String.class, OwFNCM5EngineListFactory.IdList);
    }

    public String convertNativeValue(Id nativeValue_p) throws OwException
    {
        if (nativeValue_p != null)
        {
            return nativeValue_p.toString();
        }
        else
        {
            return null;
        }
    }

    public Id toNativeValue(String owdValue_p) throws OwException
    {
        if (owdValue_p != null)
        {
            return new Id(owdValue_p);
        }
        else
        {
            return null;
        }
    }

}
