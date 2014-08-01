package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.constants.ClassNames;
import com.filenet.api.core.Document;
import com.filenet.api.core.EngineObject;
import com.filenet.api.events.Subscription;
import com.filenet.api.meta.ClassDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5DefaultObjectClassFactory.
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
public class OwFNCM5DefaultObjectClassFactory implements OwFNCM5ObjectClassFactory
{
    private OwFNCM5PropertyClassFactory propertyClassFactory;
    private OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor;

    public OwFNCM5DefaultObjectClassFactory(OwFNCM5PropertyClassFactory propertyClassFactory, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor)
    {
        super();
        this.propertyClassFactory = propertyClassFactory;
        this.resourceAccessor = resourceAccessor;
    }

    public OwFNCM5Class<?, ?> createObjectClass(ClassDescription classDescription_p) throws OwException
    {
        OwFNCM5ObjectStoreClassDescription declaration = new OwFNCM5ObjectStoreClassDescription(classDescription_p, this.propertyClassFactory);

        if (classDescription_p.describedIsOfClass(ClassNames.VERSION_SERIES))
        {
            return new OwFNCM5PersistedVersionSeriesClass(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.REFERENTIAL_CONTAINMENT_RELATIONSHIP))
        {
            return new OwFNCM5ReferentialContainmentRelationshipClass(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.EVENT))
        {
            return new OwFNCM5EventClass(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.WORKFLOW_DEFINITION))
        {
            return new OwFNCM5WorkflowDefinitionClass(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.INSTANCE_WORKFLOW_SUBSCRIPTION))
        {
            return new OwFNCM5InstanceWorkflowSubscriptionClass(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.CLASS_WORKFLOW_SUBSCRIPTION))
        {
            return new OwFNCM5ClassWorkflowSubscriptionClass(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.SUBSCRIPTION))
        {
            return new OwFNCM5SubscriptionClass<Subscription>(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.DOCUMENT))
        {
            return new OwFNCM5DocumentClass<Document>(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.FOLDER))
        {
            return new OwFNCM5FolderClass(declaration, this.resourceAccessor);
        }
        else if (classDescription_p.describedIsOfClass(ClassNames.CUSTOM_OBJECT))
        {
            return new OwFNCM5CustomObjectClass(declaration, this.resourceAccessor);
        }
        else
        {
            return new OwFNCM5EngineObjectClass<EngineObject, OwFNCM5ObjectStoreResource>(declaration, this.resourceAccessor);
        }
    }
}
