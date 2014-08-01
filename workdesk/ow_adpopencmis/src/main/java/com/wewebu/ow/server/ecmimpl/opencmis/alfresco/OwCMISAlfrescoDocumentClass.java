package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Map;

import org.alfresco.cmis.client.TransientAlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.DocumentType;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDocument;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Object class for the {@link TransientAlfrescoDocument} - {@link DocumentType} OpenCMIS object model.
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
public interface OwCMISAlfrescoDocumentClass extends OwCMISDocumentClass<TransientAlfrescoDocument>
{
    @Override
    OwCMISDocument<TransientAlfrescoDocument> from(TransientAlfrescoDocument object, Map<String, ?> conversionParameters) throws OwException;
}
