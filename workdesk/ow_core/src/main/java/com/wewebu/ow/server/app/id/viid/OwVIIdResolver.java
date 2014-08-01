package com.wewebu.ow.server.app.id.viid;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwVIIdResolver
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
 *@since 4.2.0.0
 */
public interface OwVIIdResolver
{
    /**
     * Resolve an object-Id to its OwObject instance.
     * Can throw several exceptions like
     * <ul>
     * <li><b>OwInvalidOperationException</b>: if parts of uniqueObjectId does not match, or null is provided as parameter</li>
     * <li><b>OwObjectNotFound</b>: If object cannot be found</li>
     * <li>Any other OwException which is thrown by current ECM-Adaptor</li>
     * </ul>
     * @param objViid OwVIId
     * @return OwObject referenced by OwVIId instance
     * @throws OwException
     */
    OwObject getObject(OwVIId objViid) throws OwException;

}
