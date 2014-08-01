package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.collection.SubscriptionSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.Subscribable;
import com.filenet.api.events.ClassWorkflowSubscription;
import com.filenet.api.events.Subscription;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5IndependentObject;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5IndependentState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwFNCM5IndependentObjectClass.
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
public class OwFNCM5IndependentObjectClass<I extends IndependentObject, S extends OwFNCM5ObjectStoreResource> extends OwFNCM5EngineObjectClass<I, S>
{

    /**
     *<p>
     * OwFNCM5LinksPropertyClass.
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
    public static class OwFNCM5LinksPropertyClass extends OwStandardPropertyClass
    {
        public OwFNCM5LinksPropertyClass()
        {
            super();

            this.m_fArray = true;
            m_fSystem = true;
            this.m_strClassName = "OW_LINKS";
            this.m_DisplayName = new OwString("fncm.OwFNCMLinkPropertyClass.name", "Link objects");
            m_fReadOnly[CONTEXT_NORMAL] = true;
            m_fReadOnly[CONTEXT_ON_CREATE] = true;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = true;
            m_fHidden[CONTEXT_NORMAL] = false;
            m_fHidden[CONTEXT_ON_CREATE] = false;
            m_fHidden[CONTEXT_ON_CHECKIN] = false;
            this.m_strJavaClassName = "com.wewebu.ow.server.ecm.OwObject";
        }
    }

    public static final OwFNCM5LinksPropertyClass LinksPropertyClass = new OwFNCM5LinksPropertyClass();

    public OwFNCM5IndependentObjectClass(OwFNCM5EngineClassDeclaration<?, S> declaration_p, OwFNCM5ResourceAccessor<S> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    public OwFNCM5Object<I> from(I nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5IndependentObject<I, S> object = factory_p.create(OwFNCM5IndependentObject.class, new Class[] { IndependentObject.class, OwFNCM5IndependentObjectClass.class }, new Object[] { nativeObject_p, this });
        return object;
    };

    public OwFNCM5EngineState<I> createSelf(I engineObject_p)
    {
        return new OwFNCM5IndependentState<I>(engineObject_p, this);
    };

    @Override
    public List<String> getVirtualPropertiesClassNames() throws OwException
    {
        List<String> names = super.getVirtualPropertiesClassNames();
        names.add(OwFNCM5IndependentObjectClass.LinksPropertyClass.getClassName());
        return names;
    }

    @Override
    protected OwFNCM5VirtualPropertyClass virtualPropertyClass(String className_p) throws OwException
    {
        if (OwFNCM5IndependentObjectClass.LinksPropertyClass.getClassName().equals(className_p))
        {
            return new OwFNCM5VirtualPropertyClass(OwFNCM5IndependentObjectClass.LinksPropertyClass);
        }
        else
        {
            return super.virtualPropertyClass(className_p);
        }

    }

    @SuppressWarnings("unchecked")
    public Set<OwFNCM5Object<?>> getWorkflowDescriptions() throws OwException
    {
        Set<OwFNCM5Object<?>> result = new HashSet<OwFNCM5Object<?>>();
        OwFNCM5ObjectStoreResource objectStoreResource = getResource();
        ObjectStore objectStore = objectStoreResource.getNativeObjectStore();
        OwFNCM5Network network = objectStoreResource.getNetwork();

        SubscriptionSet allSubscriptions = objectStore.get_Subscriptions();

        String nativeClassName = this.getClassDeclaration().getSymbolicName();
        ClassDefinition nativeClassDefinition = Factory.ClassDefinition.fetchInstance(objectStore, nativeClassName, null);

        //Add my subscriptions
        Iterator<Subscription> it = allSubscriptions.iterator();
        while (it.hasNext())
        {
            Subscription aSubscription = it.next();
            String subscriptionClass = aSubscription.getClassName();
            Subscribable target = aSubscription.get_SubscriptionTarget();

            if (nativeClassDefinition.equals(target))
            {
                if (ClassNames.CLASS_WORKFLOW_SUBSCRIPTION.equals(subscriptionClass))
                {
                    ClassWorkflowSubscription classWfSubscription = (ClassWorkflowSubscription) aSubscription;
                    result.add(network.fromNativeObject(classWfSubscription));
                    //WorkflowDefinition wfDefinition = classWfSubscription.get_WorkflowDefinition();
                }
            }
        }

        //Add parent subscriptions
        result.addAll(getParent().getWorkflowDescriptions());

        return result;
    }
}
