package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.TransientDocument;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDocument;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Object class for the {@link TransientDocument} - {@link DocumentType} OpenCMIS object model. 
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
public interface OwCMISDocumentClass<D extends TransientDocument> extends OwCMISNativeObjectClass<DocumentType, D>
{
    @Override
    OwCMISDocument<D> from(D object, Map<String, ?> conversionParameters) throws OwException;
}
