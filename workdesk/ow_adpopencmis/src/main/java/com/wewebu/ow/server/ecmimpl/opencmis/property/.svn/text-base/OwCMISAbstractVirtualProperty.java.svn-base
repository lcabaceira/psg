package com.wewebu.ow.server.ecmimpl.opencmis.property;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;

/**
 *<p>
 *  OwCMISAbstractVirtualProperty.
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
public abstract class OwCMISAbstractVirtualProperty<O, C extends OwCMISPropertyClass<O>> extends OwCMISAbstractProperty<O, C> implements OwCMISVirtualProperty<O>
{

    private OwCMISObject object;

    public OwCMISAbstractVirtualProperty(C propertyClass_p, OwCMISObject object_p)
    {
        super(propertyClass_p);
        this.object = object_p;
    }

    public OwCMISObject getObject()
    {
        return object;
    }

}
