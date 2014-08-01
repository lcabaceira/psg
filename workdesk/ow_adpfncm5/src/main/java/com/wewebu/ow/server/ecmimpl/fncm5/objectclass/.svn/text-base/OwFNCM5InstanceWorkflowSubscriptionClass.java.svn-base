package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.events.InstanceWorkflowSubscription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5InstanceWorkflowSubscription;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Creates instances of {@link OwFNCM5InstanceWorkflowSubscription}.
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
public class OwFNCM5InstanceWorkflowSubscriptionClass extends OwFNCM5SubscriptionClass<InstanceWorkflowSubscription>
{

    /**
     * @param declaration_p
     * @param resourceAccessor_p
     */
    public OwFNCM5InstanceWorkflowSubscriptionClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5IndependentObjectClass#from(com.filenet.api.core.IndependentObject, com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory)
     */
    @Override
    public OwFNCM5Object<InstanceWorkflowSubscription> from(InstanceWorkflowSubscription nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5InstanceWorkflowSubscription object = factory_p.create(OwFNCM5InstanceWorkflowSubscription.class, new Class[] { InstanceWorkflowSubscription.class, OwFNCM5InstanceWorkflowSubscriptionClass.class },
                new Object[] { nativeObject_p, this });
        return object;
    }
}
