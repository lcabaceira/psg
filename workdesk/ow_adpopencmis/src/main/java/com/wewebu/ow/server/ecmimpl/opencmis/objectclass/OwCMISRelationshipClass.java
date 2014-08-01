package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.TransientRelationship;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRelationship;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Object class for the {@link TransientRelationship} - {@link RelationshipType} OpenCMIS object model.
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
public interface OwCMISRelationshipClass extends OwCMISNativeObjectClass<RelationshipType, TransientRelationship>
{
    @Override
    OwCMISRelationship from(TransientRelationship object, Map<String, ?> conversionParameters) throws OwException;

}
