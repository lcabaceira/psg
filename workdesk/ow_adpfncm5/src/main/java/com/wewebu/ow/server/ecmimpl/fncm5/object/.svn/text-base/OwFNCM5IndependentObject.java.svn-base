package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.SubscriptionSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.Link;
import com.filenet.api.core.ObjectReference;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.Subscribable;
import com.filenet.api.events.InstanceWorkflowSubscription;
import com.filenet.api.events.Subscription;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ContentObjectModel;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EngineObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5IndependentObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5SimpleProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwFNCM5IndependentObject.
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
public class OwFNCM5IndependentObject<I extends IndependentObject, R extends OwFNCM5ObjectStoreResource> extends OwFNCM5EngineObject<I, R>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5IndependentObject.class);

    public OwFNCM5IndependentObject(I nativeObject_p, OwFNCM5IndependentObjectClass<I, R> clazz_p)
    {
        super(nativeObject_p, clazz_p);
    }

    public String getID()
    {
        try
        {
            OwFNCM5EngineState<I> myself = getSelf();
            I object = myself.getEngineObject();
            ObjectReference reference = object.getObjectReference();
            return reference.getObjectIdentity();
        }
        catch (Exception e)
        {
            // TODO LOG and throw Ow ?  Runtime Exception
            e.printStackTrace();
            return "N/A";
        }
    }

    @Override
    public OwProperty createVirtualProperty(OwFNCM5VirtualPropertyClass propertyClass_p) throws OwException
    {
        String className = propertyClass_p.getClassName();
        if (OwFNCM5IndependentObjectClass.LinksPropertyClass.getClassName().equals(className))
        {

            return new OwFNCM5SimpleProperty(getObjectLinks(), propertyClass_p);
        }
        else if (OwResource.m_ObjectPathPropertyClass.getClassName().equals(className))
        {
            String path = getPath();
            if (path != null)
            {

                R resource = getResource();
                String prefix = null;
                if (path.startsWith("/" + resource.getSymbolicName()))
                {
                    prefix = resource.getSymbolicName();
                }
                else if (path.startsWith("/" + resource.getID()))
                {
                    prefix = resource.getID();
                }

                if (prefix != null)
                {
                    /* OW_ObjectPath will be used for displaying, 
                     * not need to show the resource ID
                     * we know where (which object store) the object is contained*/
                    int idx = path.indexOf(prefix) + prefix.length();
                    path = path.substring(idx);
                }

            }
            return new OwFNCM5SimpleProperty(path, propertyClass_p);
        }
        else
        {
            return super.createVirtualProperty(propertyClass_p);
        }
    }

    private OwObject[] getObjectLinks() throws OwException
    {
        OwFNCM5EngineState<I> myself = getSelf();
        I object = myself.getEngineObject();
        ObjectReference reference = object.getObjectReference();
        String id = reference.getObjectIdentity();

        SearchSQL sql = new SearchSQL();
        sql.setQueryString("SELECT Id, Head ,Tail,Name FROM Link WHERE (Head = Object('" + id + "') OR Tail = Object('" + id + "'))");

        R resource = getResource();
        OwFNCM5ObjectStore objectStore = resource.getObjectStore();
        ObjectStore nativeObjectStore = objectStore.getNativeObject();

        SearchScope searchScope = new SearchScope(nativeObjectStore);
        IndependentObjectSet links = searchScope.fetchObjects(sql, 10, null, true);

        Iterator it = links.iterator();
        I thisObject = getNativeObject();
        ObjectReference thisObjectReference = thisObject.getObjectReference();
        String thisId = thisObjectReference.getObjectIdentity();

        OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();
        List<OwObject> linkedObjects = new LinkedList<OwObject>();

        while (it.hasNext())
        {
            Link link = (Link) it.next();
            IndependentObject head = link.get_Head();
            IndependentObject tail = link.get_Tail();

            ObjectReference headReference = head.getObjectReference();
            String headId = headReference.getObjectIdentity();

            if (thisId.equals(headId))
            {
                OwFNCM5Class<IndependentObject, ?> tailClass = objectModel.classOf(tail);
                OwFNCM5Object<IndependentObject> tailObject = tailClass.from(tail, OwFNCM5DefaultObjectFactory.INSTANCE);
                linkedObjects.add(tailObject);
            }
            else
            {
                OwFNCM5Class<IndependentObject, ?> headClass = objectModel.classOf(head);
                OwFNCM5Object<IndependentObject> headObject = headClass.from(head, OwFNCM5DefaultObjectFactory.INSTANCE);
                linkedObjects.add(headObject);
            }
        }

        return linkedObjects.toArray(new OwObject[linkedObjects.size()]);
    }

    public boolean hasWorkflowDescriptions()
    {
        // TODO Change the method name to reflect the fact that we are asking if this object can be target for a WorkflowSubscription.
        I nativeObject = getNativeObject();
        return nativeObject != null && nativeObject instanceof Subscribable;
    }

    @SuppressWarnings("unchecked")
    public Set<OwFNCM5Object<?>> getWorkflowDescriptions() throws OwException
    {
        if (!hasWorkflowDescriptions())
        {
            String message = "Invalid workflow operation! This object is not a valid Workflow Target!";
            LOG.error("OwFNCMObject.getWorkflowSubscriptions: " + message);
            throw new OwInvalidOperationException(new OwString("fncm.OwFNCMObject.notaworkflowtarget", "Invalid workflow operation! This object is not a workflow target!"));
        }

        //Object subscriptions
        Set<OwFNCM5Object<?>> objectLinkedDefinitions = new HashSet<OwFNCM5Object<?>>();
        ObjectStore objectStore = getResource().getObjectStore().getNativeObject();
        SubscriptionSet allSubscriptions = objectStore.get_Subscriptions();
        I nativeObject = this.getNativeObject();
        Iterator<Subscription> it = allSubscriptions.iterator();
        while (it.hasNext())
        {
            Subscription aSubscription = it.next();
            LOG.info(aSubscription.get_Id());

            //            aSubscription = Factory.Subscription.fetchInstance(objectStore, aSubscription.get_Id(), null);

            String subscriptionClass = aSubscription.getClassName();
            if (nativeObject.equals(aSubscription.get_SubscriptionTarget()))
            {
                if (ClassNames.INSTANCE_WORKFLOW_SUBSCRIPTION.equals(subscriptionClass))
                {
                    InstanceWorkflowSubscription instanceWfSubscription = (InstanceWorkflowSubscription) aSubscription;
                    //                    WorkflowDefinition wfDefinition = instanceWfSubscription.get_WorkflowDefinition();
                    objectLinkedDefinitions.add(getNetwork().fromNativeObject(instanceWfSubscription));
                }
            }
        }

        //Class linked definitions
        OwFNCM5EngineObjectClass<I, R> myClass = getObjectClass();
        Set<OwFNCM5Object<?>> classLinkedDefinitions = myClass.getWorkflowDescriptions();

        Set<OwFNCM5Object<?>> result = new HashSet<OwFNCM5Object<?>>();
        result.addAll(objectLinkedDefinitions);
        result.addAll(classLinkedDefinitions);
        return result;
    }

    /**
     * Create an OwObject from given IndependentlyPersistableObject.
     * @param persistableObject_p IndependentlyPersistableObject to be wrapped
     * @return OwFNCM5Object representing the provided native object
     * @throws OwException
     */
    protected OwFNCM5Object<?> createOwObject(IndependentlyPersistableObject persistableObject_p) throws OwException
    {
        OwFNCM5Resource resource = getResource();
        OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();

        OwFNCM5NativeObjHelper.ensure(persistableObject_p, new String[] { PropertyNames.NAME, PropertyNames.CLASS_DESCRIPTION });

        OwFNCM5Class clazz = objectModel.objectClassForName(persistableObject_p.get_ClassDescription().get_SymbolicName());

        return clazz.from(persistableObject_p, OwFNCM5DefaultObjectFactory.INSTANCE);
    }
}
