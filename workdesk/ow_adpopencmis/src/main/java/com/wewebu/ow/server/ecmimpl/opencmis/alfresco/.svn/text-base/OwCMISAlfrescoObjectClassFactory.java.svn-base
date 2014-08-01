package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.alfresco.cmis.client.AlfrescoAspects;
import org.alfresco.cmis.client.type.AlfrescoType;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.PolicyType;
import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyIdDefinitionImpl;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleObjectClassFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISFolderClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISPolicyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISRelationshipClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISAlfrescoObjectClassFactory.
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
public class OwCMISAlfrescoObjectClassFactory extends OwCMISSimpleObjectClassFactory
{

    public OwCMISAlfrescoObjectClassFactory(OwCMISNativeSession session)
    {
        super(session);
    }

    @Override
    protected OwCMISDocumentClass createDocumentClass(DocumentType documentType) throws OwException
    {
        OwCMISDocumentClass superClass = super.createDocumentClass(documentType);

        if (documentType instanceof AlfrescoType)
        {
            return new OwCMISAlfrescoDocumentTypeClassImpl(superClass, (AlfrescoType) documentType);
        }
        else
        {
            return superClass;
        }

    }

    @Override
    protected OwCMISFolderClass createFolderClass(FolderType folderType) throws OwException
    {
        OwCMISFolderClass superClass = super.createFolderClass(folderType);

        if (folderType instanceof AlfrescoType)
        {
            return new OwCMISAlfrescoFolderTypeClassImpl(superClass, (AlfrescoType) folderType);
        }
        else
        {
            return superClass;
        }
    }

    @Override
    protected OwCMISPolicyClass createPolicyClass(PolicyType policyType) throws OwException
    {
        OwCMISPolicyClass superClass = super.createPolicyClass(policyType);

        if (policyType instanceof AlfrescoType)
        {
            return new OwCMISAlfrescoPolicyTypeClassImpl(superClass, (AlfrescoType) policyType);
        }
        else
        {
            return superClass;
        }

    }

    @Override
    protected OwCMISRelationshipClass createRelationshipClass(RelationshipType relationshipType) throws OwException
    {

        OwCMISRelationshipClass superClass = super.createRelationshipClass(relationshipType);

        if (relationshipType instanceof AlfrescoType)
        {
            return new OwCMISAlfrescoRelationshipTypeClassImpl(superClass, (AlfrescoType) relationshipType);
        }
        else
        {
            return superClass;
        }
    }

    @Override
    public <O extends TransientCmisObject> OwCMISNativeObjectClass<?, O> createObjectClassOf(O object) throws OwException
    {
        OwCMISNativeObjectClass<?, O> clazz = super.createObjectClassOf(object);

        if (object instanceof AlfrescoAspects)
        {

            if (clazz instanceof OwCMISDocumentClass)
            {
                return (OwCMISNativeObjectClass<?, O>) new OwCMISAlfrescoDocumentObjectClassImpl((OwCMISDocumentClass) clazz);
            }
            else if (clazz instanceof OwCMISFolderClass)
            {
                return (OwCMISNativeObjectClass<?, O>) new OwCMISAlfrescoFolderObjectClassImpl((OwCMISFolderClass) clazz);
            }

        }
        return clazz;
    }

    @Override
    public ObjectType retrieveObjectType(String id, Session session) throws OwException
    {
        String[] ids = id.split(",");
        if (ids.length > 1)
        {
            ObjectType type = super.retrieveObjectType(ids[0], session);
            Map<String, PropertyDefinition<?>> props = type.getPropertyDefinitions();
            if (props == null)
            {
                props = new HashMap<String, PropertyDefinition<?>>();
            }

            PropertyDefinition<?> prop = props.get(OwCMISAspectsPropertyDefinition.ID);
            PropertyIdDefinitionImpl secObjTypeProp;
            if (prop != null)
            {
                secObjTypeProp = (PropertyIdDefinitionImpl) prop;
            }
            else
            {
                /* The TypeDefinition is cached in OpenCMIS, but the ObjectType is created every time.
                 * Change the id to comma separated Id-"list", need to be refactored
                 * for CMIS v1.1 to use the cmis:secondaryObjectTypeIds.*/
                LinkedHashMap<String, PropertyDefinition<?>> copyProps = new LinkedHashMap<String, PropertyDefinition<?>>();
                copyProps.putAll(props);
                secObjTypeProp = new OwCMISAspectsPropertyDefinition();
                copyProps.put(secObjTypeProp.getId(), secObjTypeProp);
                ((AbstractTypeDefinition) type).setPropertyDefinitions(copyProps);
            }

            LinkedList<String> defaultValues = new LinkedList<String>(secObjTypeProp.getDefaultValue());
            //ignore first element, it's the real object type
            for (int i = 1; i < ids.length; i++)
            {
                defaultValues.add(ids[i]);
            }
            secObjTypeProp.setDefaultValue(defaultValues);

            return type;
        }
        else
        {
            return super.retrieveObjectType(id, session);
        }
    }

}
