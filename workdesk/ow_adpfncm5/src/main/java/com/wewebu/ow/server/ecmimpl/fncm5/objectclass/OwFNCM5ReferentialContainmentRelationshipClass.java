package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.core.ReferentialContainmentRelationship;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ReferentialContainmentRelationship;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5SoftObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5ReferentialContainmentRelationshipClass.
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
public class OwFNCM5ReferentialContainmentRelationshipClass extends OwFNCM5IndependentlyPersistableObjectClass<ReferentialContainmentRelationship, OwFNCM5ObjectStoreResource>
{

    public OwFNCM5ReferentialContainmentRelationshipClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    @Override
    public OwFNCM5Object<ReferentialContainmentRelationship> from(ReferentialContainmentRelationship nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5ReferentialContainmentRelationship referentialContainmentRelationship = factory_p.create(OwFNCM5ReferentialContainmentRelationship.class, new Class[] { ReferentialContainmentRelationship.class,
                OwFNCM5ReferentialContainmentRelationshipClass.class }, new Object[] { nativeObject_p, this });
        return OwFNCM5SoftObject.asSoftObject(referentialContainmentRelationship);
    }
}
