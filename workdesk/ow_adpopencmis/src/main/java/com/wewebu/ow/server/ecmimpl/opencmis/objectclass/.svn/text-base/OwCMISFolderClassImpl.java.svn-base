package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientFolder;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISFolder;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISFolderObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISFolderClassImpl.
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
public class OwCMISFolderClassImpl extends OwCMISAbstractNativeObjectClass<FolderType, TransientFolder> implements OwCMISFolderClass
{

    public OwCMISFolderClassImpl(FolderType folderType, OwCMISNativeSession session)
    {
        super(folderType, session);
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    @Override
    public OwCMISFolder from(TransientFolder object, Map<String, ?> conversionParameters) throws OwException
    {
        OperationContext creationContext = createContext(conversionParameters);

        OwCMISFolderClass clazz = getParameterValue(conversionParameters, OwCMISConversionParameters.OBJECT_CLASS, this);
        return new OwCMISFolderObject(getSession(), object, creationContext, clazz);
    }

    @Override
    protected ObjectId createNativeObject(Map<String, Object> properties, ObjectId nativeParentFolder, ContentStream contentStream, boolean majorVersion, boolean checkedOut, List<Policy> policies, List<Ace> addAce, List<Ace> removeAce)
    {
        Session nativeSession = getSession().getOpenCMISSession();
        ObjectId newId = nativeSession.createFolder(properties, nativeParentFolder, policies, addAce, removeAce);
        return newId;
    }
}
