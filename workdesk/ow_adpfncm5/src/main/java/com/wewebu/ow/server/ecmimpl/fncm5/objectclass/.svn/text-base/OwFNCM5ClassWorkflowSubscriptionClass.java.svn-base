package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.events.ClassWorkflowSubscription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ClassWorkflowSubscription;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Creates instances of {@link OwFNCM5ClassWorkflowSubscription}.
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
public class OwFNCM5ClassWorkflowSubscriptionClass extends OwFNCM5SubscriptionClass<ClassWorkflowSubscription>
{

    /**
     * @param declaration_p
     * @param resourceAccessor_p
     */
    public OwFNCM5ClassWorkflowSubscriptionClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    @Override
    public OwFNCM5Object<ClassWorkflowSubscription> from(ClassWorkflowSubscription nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5ClassWorkflowSubscription object = factory_p.create(OwFNCM5ClassWorkflowSubscription.class, new Class[] { ClassWorkflowSubscription.class, OwFNCM5ClassWorkflowSubscriptionClass.class }, new Object[] { nativeObject_p, this });
        return object;
    }
}
