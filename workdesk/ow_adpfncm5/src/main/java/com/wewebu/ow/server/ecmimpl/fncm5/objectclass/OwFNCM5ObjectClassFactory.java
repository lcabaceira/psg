package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.meta.ClassDescription;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * {@link OwFNCM5Class} factory.
 * Creates {@link ClassDescription} based AWD object classes. 
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
public interface OwFNCM5ObjectClassFactory
{
    OwFNCM5Class<?, ?> createObjectClass(ClassDescription classDescription_p) throws OwException;
}
