package com.wewebu.ow.server.ecmimpl.opencmis;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISAuthenticationInterceptor;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * An {@link OwCMISAuthenticationInterceptor} based CMIS network.
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
 *
 */
public interface OwCMISAuthenticatedNetwork extends OwNetwork<OwCMISObject>
{
    OwCMISAuthenticationInterceptor getAuthInterceptor() throws OwException;
}
