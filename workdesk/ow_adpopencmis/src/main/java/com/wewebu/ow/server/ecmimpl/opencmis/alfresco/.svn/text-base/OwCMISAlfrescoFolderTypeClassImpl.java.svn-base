package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Map;

import org.alfresco.cmis.client.type.AlfrescoType;
import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.TransientFolder;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISFolder;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISFolderClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISAlfrescoFolderTypeClassImpl.
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
//TODO: The hierarchy of TransientAlfrescoFolderImpl is invalid . TransientAlfrescoFolder is not implemented .
//This implementation should be TransientAlfrescoFolder parameterized. This can not be done : see the above reason.

public class OwCMISAlfrescoFolderTypeClassImpl extends OwCMISAlfrescoTypeClassImpl<FolderType, TransientFolder> implements OwCMISFolderClass
{

    public OwCMISAlfrescoFolderTypeClassImpl(OwCMISFolderClass nativeObjectClass, AlfrescoType alfrescoType)
    {
        super(nativeObjectClass, alfrescoType);
    }

    @Override
    protected OwCMISFolderClass getNativeObjectClass()
    {
        return (OwCMISFolderClass) super.getNativeObjectClass();
    }

    @Override
    public OwCMISFolder from(TransientFolder object, Map<String, ?> conversionParameters) throws OwException
    {
        return getNativeObjectClass().from(object, addClassParameter(conversionParameters));
    }

}
