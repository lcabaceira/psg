package com.wewebu.ow.server.app.id;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * General interface for an Id decoder Instance.
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
public interface OwIdDecoder<T>
{
    /**
     * Base method to transform a String into a corresponding Id handler instance
     * @param idRepresentation String
     * @return T instance which can handle/provide values of the serialized Id
     * @throws OwException
     */
    T decode(String idRepresentation) throws OwException;
}
