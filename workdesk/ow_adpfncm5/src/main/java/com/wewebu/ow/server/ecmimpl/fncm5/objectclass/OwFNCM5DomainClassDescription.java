package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.meta.ClassDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5DomainResource;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;

/**
 *<p>
 * {@link OwFNCM5DomainResource} residing {@link ClassDescription} class declaration.
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
public class OwFNCM5DomainClassDescription extends OwFNCM5ClassDescription<OwFNCM5DomainResource>
{

    public OwFNCM5DomainClassDescription(ClassDescription description_p, OwFNCM5PropertyClassFactory propertyClassFactory_p)
    {
        super(description_p, propertyClassFactory_p);
    }

}
