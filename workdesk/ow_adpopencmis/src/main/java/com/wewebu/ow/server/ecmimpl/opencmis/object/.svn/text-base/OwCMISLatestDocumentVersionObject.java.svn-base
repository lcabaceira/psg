package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.TransientDocument;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISLatestDocumentVersionObject.
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
public class OwCMISLatestDocumentVersionObject extends OwCMISDocumentObject
{

    public OwCMISLatestDocumentVersionObject(OwCMISNativeSession session_p, TransientDocument nativeObject_p, OperationContext creationContext, OwCMISDocumentClass class_p) throws OwException
    {
        super(session_p, nativeObject_p, creationContext, class_p);
    }

    @Override
    protected void afterPropertiesSet(ObjectId updatedObjectId)
    {
        TransientDocument document = getNativeObject();
        OperationContext context = owTransientObject.getTransientContext();
        Document lastVersion = document.getObjectOfLatestVersion(false, context);
        replaceNativeObject(lastVersion.getTransientDocument(), context);
    }
}
