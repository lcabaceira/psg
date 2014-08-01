package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.List;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISNativeObject.
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
public interface OwCMISNativeObject<N extends TransientCmisObject> extends OwCMISSessionObject
{
    @Override
    N getNativeObject();

    OwCMISTransientObject<N> getTransientObject();

    /**
     * 
     * @param filter  
     * @param refresh true if the renditions indicated by the filter {@link Set} should be refreshed 
     * @return a {@link List} of renditions corresponding to the given filter {@link Set} 
     * @throws OwException
     * @since 4.2.0.0
     */
    List<OwCMISRendition> retrieveRenditions(Set<String> filter, boolean refresh) throws OwException;
}
