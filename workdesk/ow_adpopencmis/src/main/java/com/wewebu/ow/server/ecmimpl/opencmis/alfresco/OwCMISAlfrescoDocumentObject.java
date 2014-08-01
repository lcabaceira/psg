package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.List;

import org.alfresco.cmis.client.TransientAlfrescoDocument;
import org.alfresco.cmis.client.type.AlfrescoDocumentType;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISAbstractTransientDocumentObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Alfresco Document representation.
 * Special handler for Alfresco specific CMIS behavior. 
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
public class OwCMISAlfrescoDocumentObject extends OwCMISAbstractTransientDocumentObject<TransientAlfrescoDocument, AlfrescoDocumentType, OwCMISAbstractAlfrescoClass<AlfrescoDocumentType, TransientAlfrescoDocument>>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAlfrescoDocumentObject.class);

    public OwCMISAlfrescoDocumentObject(OwCMISNativeSession session_p, TransientAlfrescoDocument nativeObject_p, OperationContext creationContext, OwCMISAbstractAlfrescoClass<AlfrescoDocumentType, TransientAlfrescoDocument> class_p)
            throws OwException
    {
        super(session_p, nativeObject_p, creationContext, class_p);
    }

    public void cancelcheckout() throws OwException
    {
        if (canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            TransientDocument myNativeObject = getNativeObject();
            Document myDocument = (Document) myNativeObject.getCmisObject();
            myDocument.cancelCheckOut();
            List<Document> versionList = myDocument.getAllVersions();
            if (versionList.isEmpty())
            {
                detach();
            }
            else
            {
                myDocument = versionList.get(0);
                replaceNativeObject((TransientAlfrescoDocument) myDocument.getTransientDocument(), getSession().getOpenCMISSession().createOperationContext());
            }
        }
        else
        {
            LOG.error("OwCMISDocumentObject.cancelcheckout(): The cancel-checkout operation is disabled for this object !");
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISDocumentObject.err.invalidCancelCheckout", "The cancel-checkout operation is disabled for this object!"));
        }
    }

    @Override
    protected void afterPropertiesSet(ObjectId updatedObjectId)
    {
        TransientDocument document = getNativeObject();
        OperationContext context = owTransientObject.getTransientContext();
        Document lastVersion = document.getObjectOfLatestVersion(false, context);
        replaceNativeObject((TransientAlfrescoDocument) lastVersion.getTransientDocument(), context);
    }
}
