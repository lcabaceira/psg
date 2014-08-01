package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISTransientObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Alfresco specific transient object state.
 * Secures (see {@link OwCMISTransientObject}) applied aspects and aspect-properties. 
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
public interface OwCMISAlfrescoTransientObject<N extends TransientCmisObject> extends OwCMISTransientObject<N>
{
    N secureAspects() throws OwException;

    boolean mustSecureAspects();
}
