package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.spi.Holder;
import org.apache.chemistry.opencmis.commons.spi.VersioningService;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwCMISVersionSeriesObject.
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
public class OwCMISVersionSeriesObject extends OwCMISDocumentObject implements OwCMISVersionSeries
{

    public OwCMISVersionSeriesObject(OwCMISNativeSession session_p, TransientDocument nativeObject_p, OperationContext creationContext, OwCMISDocumentClass class_p) throws OwException
    {
        super(session_p, nativeObject_p, creationContext, class_p);
    }

    protected OwCMISDocument getLatestVersion(boolean major_p) throws OwException
    {
        owTransientObject.refresh(VERSION_PROPERTIES);
        TransientDocument myNativeObject = owTransientObject.getTransientCmisObject();

        //TODO: employ context - a default context maybe?!?

        Document latestVersion = myNativeObject.getObjectOfLatestVersion(major_p, owTransientObject.getTransientContext());
        TransientDocument transientLatestVersion = latestVersion.getTransientDocument();
        OwCMISDocumentClass latestVersionClass = (OwCMISDocumentClass) getSession().classOf(transientLatestVersion);

        return latestVersionClass.from(transientLatestVersion, createVersionConversionParameters(true));
    }

    private Map<String, ?> createVersionConversionParameters(boolean latestDocumentVersionObject)
    {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(OwCMISConversionParameters.CREATION_CONTEXT, owTransientObject.getTransientContext());
        parameters.put(OwCMISConversionParameters.LATEST_VERSION_EXPECTED, latestDocumentVersionObject);
        return parameters;
    }

    @Override
    public OwCMISDocument getLatest() throws OwException
    {
        return getLatestVersion(false);
    }

    @Override
    public OwCMISDocument getReleased() throws OwException
    {
        return getLatestVersion(true);
    }

    @Override
    public OwObject getObject(OwVersion version_p)
    {
        //TODO: RTTI ?? some conversions?
        return (OwCMISDocument) version_p;
    }

    @Override
    public Collection getVersions(Collection properties_p, OwSort sort_p, int iMaxSize_p) throws OwException
    {
        TransientDocument myNativeObject = getNativeObject();

        OperationContext context = getSession().createOperationContext(properties_p, 1, getObjectClass());

        List<Document> allVersions = myNativeObject.getAllVersions(context);

        OwObjectCollection owdVersions = new OwStandardObjectCollection();
        Map<String, ?> conversionParameters = createVersionConversionParameters(false);

        for (Document version : allVersions)
        {
            TransientDocument transientVerison = version.getTransientDocument();
            OwCMISDocumentClass versionClass = (OwCMISDocumentClass) getSession().classOf(transientVerison);
            OwCMISDocument owdVersion = versionClass.from(transientVerison, conversionParameters);
            owdVersions.add(owdVersion);
        }

        if (sort_p != null)
        {
            try
            {
                owdVersions.sort(sort_p);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not retrieve versions.", e);
            }
        }

        return owdVersions;
    }

    @Override
    public OwCMISDocument getReservation() throws OwException
    {
        owTransientObject.refresh(VERSION_PROPERTIES);
        if (isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            org.apache.chemistry.opencmis.client.api.Property<Object> pwcId = owTransientObject.secureObject(PropertyIds.VERSION_SERIES_CHECKED_OUT_ID).getProperty(PropertyIds.VERSION_SERIES_CHECKED_OUT_ID);
            if (pwcId != null && pwcId.getValue() != null)
            {
                Document obj = (Document) getSession().getOpenCMISSession().getObject(pwcId.getValueAsString());

                OwCMISDocumentClass versionClass = (OwCMISDocumentClass) getSession().classOf(obj.getTransientDocument());
                return versionClass.from(obj.getTransientDocument(), createVersionConversionParameters(false));
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public String getId()
    {
        TransientDocument myNativeObject = getNativeObject();
        return myNativeObject.getVersionSeriesId();
    }

    @Override
    public String checkoutVersionSeries(Object mode_p) throws OwException
    {

        TransientDocument myNativeObject = getNativeObject();
        String versionSeriesId = myNativeObject.getVersionSeriesId();

        Session nativeSession = getSession().getOpenCMISSession();
        VersioningService versioningService = nativeSession.getBinding().getVersioningService();

        Holder<String> objectIdHolder = new Holder<String>(versionSeriesId);
        versioningService.checkOut(nativeSession.getRepositoryInfo().getId(), objectIdHolder, null, null);
        return objectIdHolder.getValue();

    }

}
