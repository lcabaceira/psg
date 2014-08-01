package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Map;

import org.alfresco.cmis.client.TransientAlfrescoDocument;
import org.alfresco.cmis.client.type.AlfrescoDocumentType;
import org.alfresco.cmis.client.type.AlfrescoType;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.OperationContext;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDocument;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISAbstractObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISAlfrescoDocumentTypeClassImpl.
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
public class OwCMISAlfrescoDocumentTypeClassImpl extends OwCMISAlfrescoTypeClassImpl<DocumentType, TransientAlfrescoDocument> implements OwCMISDocumentClass<TransientAlfrescoDocument>
{

    public OwCMISAlfrescoDocumentTypeClassImpl(OwCMISDocumentClass<TransientAlfrescoDocument> nativeObjectClass, AlfrescoType alfrescoType)
    {
        super(nativeObjectClass, alfrescoType);
    }

    @Override
    protected OwCMISDocumentClass<TransientAlfrescoDocument> getNativeObjectClass()
    {
        return (OwCMISDocumentClass<TransientAlfrescoDocument>) super.getNativeObjectClass();
    }

    @Override
    public OwCMISDocument<TransientAlfrescoDocument> from(TransientAlfrescoDocument object, Map<String, ?> conversionParameters) throws OwException
    {
        Boolean versionSeriesExpected = getParameterValue(conversionParameters, OwCMISConversionParameters.VERSION_SERIES_EXPECTED, Boolean.FALSE);
        //TODO : version preservation processing
        if (Boolean.FALSE.equals(versionSeriesExpected))
        {
            OperationContext creationContext = createContext(conversionParameters);
            OwCMISAbstractObjectClass a = getParameterValue(conversionParameters, OwCMISConversionParameters.OBJECT_CLASS, this);

            return new OwCMISAlfrescoDocumentObject(getSession(), object, creationContext, (OwCMISAbstractAlfrescoClass<AlfrescoDocumentType, TransientAlfrescoDocument>) a);
        }
        else
        {
            return getNativeObjectClass().from(object, addClassParameter(conversionParameters));
        }
    }

}
