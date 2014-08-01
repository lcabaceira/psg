package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import org.apache.log4j.Logger;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.meta.ClassDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5DefinitionClassFactory.
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
public class OwFNCM5DefinitionClassFactory implements OwFNCM5ObjectClassFactory
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5DefinitionClassFactory.class);

    private ObjectStore objectStore;
    private OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor;
    private OwFNCM5PropertyClassFactory propertyClassFactory;

    public OwFNCM5DefinitionClassFactory(ObjectStore objectStore, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor, OwFNCM5PropertyClassFactory propertyClassFactory)
    {
        super();
        this.objectStore = objectStore;
        this.resourceAccessor = resourceAccessor;
        this.propertyClassFactory = propertyClassFactory;
    }

    public OwFNCM5Class<?, ?> createObjectClass(ClassDescription classDescription_p) throws OwException
    {
        String symbolicName = classDescription_p.get_SymbolicName();

        try
        {
            ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(objectStore, symbolicName, null);

            OwFNCM5ClassDefinition definition = new OwFNCM5ClassDefinition(classDefinition, this.propertyClassFactory);

            if (classDescription_p.describedIsOfClass(ClassNames.REFERENTIAL_CONTAINMENT_RELATIONSHIP))
            {
                return new OwFNCM5ReferentialContainmentRelationshipClass(definition, this.resourceAccessor);
            }
            if (classDescription_p.describedIsOfClass(ClassNames.EVENT))
            {
                return new OwFNCM5EventClass(definition, this.resourceAccessor);
            }
            else if (classDescription_p.describedIsOfClass(ClassNames.DOCUMENT))
            {
                return new OwFNCM5DocumentClass(definition, this.resourceAccessor);
            }
            else if (classDescription_p.describedIsOfClass(ClassNames.FOLDER))
            {
                return new OwFNCM5FolderClass(definition, this.resourceAccessor);
            }

            else if (classDescription_p.describedIsOfClass(ClassNames.CUSTOM_OBJECT))
            {
                return new OwFNCM5CustomObjectClass(definition, this.resourceAccessor);
            }
            else
            {
                return null;
            }

        }
        catch (EngineRuntimeException e)
        {
            LOG.error("Class " + classDescription_p.get_SymbolicName() + " has no definition because of : " + e.getMessage());
            return null;
        }
    }

}
