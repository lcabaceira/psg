package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Map;

import org.alfresco.cmis.client.type.AlfrescoType;
import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.TransientRelationship;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRelationship;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISRelationshipClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISAlfrescoRelationshipTypeClassImpl.
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
public class OwCMISAlfrescoRelationshipTypeClassImpl extends OwCMISAlfrescoTypeClassImpl<RelationshipType, TransientRelationship> implements OwCMISRelationshipClass
{

    public OwCMISAlfrescoRelationshipTypeClassImpl(OwCMISRelationshipClass nativeObjectClass, AlfrescoType alfrescoType)
    {
        super(nativeObjectClass, alfrescoType);
    }

    @Override
    protected OwCMISRelationshipClass getNativeObjectClass()
    {
        return (OwCMISRelationshipClass) super.getNativeObjectClass();
    }

    @Override
    public OwCMISRelationship from(TransientRelationship object, Map<String, ?> conversionParameters) throws OwException
    {
        return getNativeObjectClass().from(object, addClassParameter(conversionParameters));
    }

}
