package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.events.Subscription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Subscription;

/**
 *<p>
 * Creates instances of {@link OwFNCM5Subscription}.
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
public class OwFNCM5SubscriptionClass<S extends Subscription> extends OwFNCM5IndependentlyPersistableObjectClass<S, OwFNCM5ObjectStoreResource>
{

    /**
     * @param declaration_p
     * @param resourceAccessor_p
     */
    public OwFNCM5SubscriptionClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    public com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object<S> from(S nativeObject_p, com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory factory_p) throws com.wewebu.ow.server.exceptions.OwException
    {
        OwFNCM5Subscription object = factory_p.create(OwFNCM5Subscription.class, new Class[] { Subscription.class, OwFNCM5SubscriptionClass.class }, new Object[] { nativeObject_p, this });
        return object;
    }
}
