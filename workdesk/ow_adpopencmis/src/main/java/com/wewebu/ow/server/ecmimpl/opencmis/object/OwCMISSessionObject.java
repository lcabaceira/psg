package com.wewebu.ow.server.ecmimpl.opencmis.object;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;

/**
 *<p>
 * OwCMISSessionObject.
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
public interface OwCMISSessionObject extends OwCMISObject
{
    OwCMISSession getSession();
}
