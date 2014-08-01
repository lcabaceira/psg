package com.wewebu.ow.server.plug.owefileref;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * eFile-OwObject filter interface.
 * Implementors are used to check {@link OwObject} represented eFiles for
 * compliance with certain mapping rules that can not be enforced through 
 * search templates.      
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
 *@since 3.1.0.0
 */
public interface OwEFileFilter
{
    /**
     * 
     * @param eFile_p
     * @return <code>true</code> if the given object (eFile_p) complies with the rules implemented by this filter
     *         <code>false</code> otherwise 
     * @throws OwException
     */
    boolean allows(OwObject eFile_p) throws OwException;
}
