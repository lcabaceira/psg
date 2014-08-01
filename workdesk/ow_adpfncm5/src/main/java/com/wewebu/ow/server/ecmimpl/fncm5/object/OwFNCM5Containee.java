package com.wewebu.ow.server.ecmimpl.fncm5.object;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Implementors are objects than are subject to containment relationships.<br />
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
 *@since 3.2.0.2
 */

public interface OwFNCM5Containee
{
    /**
     * Removes the reference to this object from the given folder.
     * @param folder_p
     * @throws OwException
     */
    void removeReferenceFrom(OwFNCM5Folder folder_p) throws OwException;
}
