package com.wewebu.ow.server.ecmimpl.fncm5.dmsid;

import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject;

/**
 *<p>
 * A P8 5.0 DMSID builder.<b/>
 * Decouples DMSID creation from DMSID holders (such as {@link OwFNCM5EngineObject}s). 
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
public interface OwFNCM5DMSIDProvider
{
    OwFNCM5DMSID createDMSIDObject(String objID_p, String... params_p);
}
