package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.TransientDocument;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * CMIS base-type "cmis:document" dependent implementation.
 * Specific class which is responsible for Document in CMIS environments, represented as OwObject.
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
public class OwCMISDocumentObject extends OwCMISAbstractTransientDocumentObject<TransientDocument, DocumentType, OwCMISDocumentClass<TransientDocument>>
{
    /* * package logger for the class 
    private static final Logger LOG = OwLog.getLogger(OwCMISDocumentObject.class);*/

    public OwCMISDocumentObject(OwCMISNativeSession session_p, TransientDocument nativeObject_p, OperationContext creationContext, OwCMISDocumentClass<TransientDocument> class_p) throws OwException
    {
        super(session_p, nativeObject_p, creationContext, class_p);
    }
}
