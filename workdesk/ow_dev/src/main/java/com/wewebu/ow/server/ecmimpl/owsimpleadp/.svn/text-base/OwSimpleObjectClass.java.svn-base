package com.wewebu.ow.server.ecmimpl.owsimpleadp;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;

/**
 *<p>
 * Object class that defines objects.
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
public class OwSimpleObjectClass extends OwStandardObjectClass
{
    public static final String NAME_PROPERTY = "name";

    public OwSimpleObjectClass()
    {
        super("MySimpleClass", OwObjectReference.OBJECT_TYPE_DOCUMENT);

        // add a name property class 
        OwSimplePropertyClass propertyclass = new OwSimplePropertyClass(NAME_PROPERTY, true);
        m_PropertyClassesMap.put(propertyclass.getClassName(), propertyclass);
    }
}