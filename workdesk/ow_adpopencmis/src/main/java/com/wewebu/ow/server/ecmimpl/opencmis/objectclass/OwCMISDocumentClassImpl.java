package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;

import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDocument;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDocumentObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISLatestDocumentVersionObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISVersionSeriesObject;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISVersionSeriesPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISVirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwObjectClass to represent Document objects.
 * Explicit handling and extension of Versioning functionality. 
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
public class OwCMISDocumentClassImpl extends OwCMISAbstractNativeObjectClass<DocumentType, TransientDocument> implements OwCMISDocumentClass<TransientDocument>
{

    public OwCMISDocumentClassImpl(DocumentType documentType, OwCMISNativeSession session)
    {
        super(documentType, session);
    }

    protected void initializeAsHierarchyRoot()
    {
        super.initializeAsHierarchyRoot();
        OwCMISVirtualPropertyClass<OwVersionSeries> versionSeriesPropertyClass = new OwCMISVersionSeriesPropertyClass(this);
        addVirtualPropertyClass(versionSeriesPropertyClass);
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return getNativeObject().isVersionable();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public OwCMISDocument from(TransientDocument object_p, Map<String, ?> conversionParameters) throws OwException
    {
        Boolean versionSeriesExpected = getParameterValue(conversionParameters, OwCMISConversionParameters.VERSION_SERIES_EXPECTED, Boolean.FALSE);
        OwCMISDocumentClass objectClass = getParameterValue(conversionParameters, OwCMISConversionParameters.OBJECT_CLASS, this);

        OperationContext creationContext = createContext(conversionParameters);

        //TODO : version preservation processing

        if (Boolean.TRUE.equals(versionSeriesExpected))
        {
            return new OwCMISVersionSeriesObject(getSession(), object_p, creationContext, objectClass);
        }
        else
        {
            Boolean latestVersionExpected = getParameterValue(conversionParameters, OwCMISConversionParameters.LATEST_VERSION_EXPECTED, Boolean.FALSE);

            if (Boolean.TRUE.equals(latestVersionExpected))
            {
                return new OwCMISLatestDocumentVersionObject(getSession(), object_p, creationContext, objectClass);
            }
            else
            {
                return new OwCMISDocumentObject(getSession(), object_p, creationContext, objectClass);
            }
        }
    }

    @Override
    protected ObjectId createNativeObject(Map<String, Object> properties, ObjectId nativeParentFolder, ContentStream contentStream, boolean majorVersion, boolean checkedOut, List<Policy> policies, List<Ace> addAce, List<Ace> removeAce)
    {
        VersioningState versioningState;
        if (checkedOut)
        {
            versioningState = VersioningState.CHECKEDOUT;
        }
        else
        {
            versioningState = majorVersion ? VersioningState.MAJOR : VersioningState.MINOR;
        }
        Session nativeSession = getSession().getOpenCMISSession();
        ObjectId newId = nativeSession.createDocument(properties, nativeParentFolder, contentStream, versioningState, policies, addAce, removeAce);
        return newId;
    }
}
