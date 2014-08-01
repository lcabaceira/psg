package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientRelationship;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwVirtualLinkPropertyClasses;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.exception.OwCMISRuntimeException;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRelationship;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRelationshipObject;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISLinkVirtualIdPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwCMISRelationshipClassImpl.
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
public class OwCMISRelationshipClassImpl extends OwCMISAbstractNativeObjectClass<RelationshipType, TransientRelationship> implements OwCMISRelationshipClass
{

    public OwCMISRelationshipClassImpl(RelationshipType relationshipType, OwCMISNativeSession session)
    {
        super(relationshipType, session);
    }

    protected void initializeAsHierarchyRoot()
    {
        super.initializeAsHierarchyRoot();
        OwCMISLinkVirtualIdPropertyClass source = new OwCMISLinkVirtualIdPropertyClass(OwVirtualLinkPropertyClasses.LINK_SOURCE, this);
        addVirtualPropertyClass(source);
        OwCMISLinkVirtualIdPropertyClass target = new OwCMISLinkVirtualIdPropertyClass(OwVirtualLinkPropertyClasses.LINK_TARGET, this);
        addVirtualPropertyClass(target);
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    @Override
    public OwCMISRelationship from(TransientRelationship object, Map<String, ?> conversionParameters) throws OwException
    {
        OperationContext creationContext = createContext(conversionParameters);
        OwCMISRelationshipClass clazz = getParameterValue(conversionParameters, OwCMISConversionParameters.OBJECT_CLASS, this);
        return new OwCMISRelationshipObject(getSession(), object, creationContext, clazz);
    }

    @Override
    protected ObjectId createNativeObject(Map<String, Object> properties, ObjectId sourceObject, ContentStream contentStream, boolean majorVersion, boolean checkedOut, List<Policy> policies, List<Ace> addAce, List<Ace> removeAce)
    {
        Session nativeSession = getSession().getOpenCMISSession();
        ObjectId newId = null;

        properties.put(PropertyIds.SOURCE_ID, sourceObject.getId());
        if (properties.get(PropertyIds.TARGET_ID) == null)
        {
            properties.put(PropertyIds.TARGET_ID, properties.get(OwObjectLink.OW_LINK_TARGET));
        }

        try
        {
            newId = nativeSession.createRelationship(properties, policies, addAce, removeAce);

        }
        catch (CmisRuntimeException e)
        {
            throw new OwCMISRuntimeException(e.getMessage(), e);
        }
        return newId;
    }

    @Override
    protected ObjectId getNativeParentFromObject(OwObject parent_p) throws OwException
    {
        if (parent_p == null)
        {
            throw new OwInvalidOperationException("Source object in a Relationship should not be null");
        }

        try
        {
            Object o = parent_p.getNativeObject();
            if (o instanceof ObjectId)
            {
                return (ObjectId) o;
            }
            else
            {
                return super.getNativeParentFromObject(parent_p);
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwCMISRuntimeException("Cannot get native representation from provided parent object", e);
        }
    }
}
