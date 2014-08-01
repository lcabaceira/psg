package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.TransientFolder;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISFolder;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Object class for the {@link TransientFolder} - {@link FolderType} OpenCMIS object model.
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
public interface OwCMISFolderClass extends OwCMISNativeObjectClass<FolderType, TransientFolder>
{
    @Override
    OwCMISFolder from(TransientFolder object, Map<String, ?> conversionParameters) throws OwException;
}
