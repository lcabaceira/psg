package com.wewebu.ow.server.ecmimpl.fncm5.object;

import com.filenet.api.core.Versionable;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * P8 5.0 engine version extension.
 * 
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
public interface OwFNCM5Version<V extends Versionable> extends OwVersion
{
    /**
     * Sets the given content information to this version.
     * The native state is persisted on the ECM engine.
     *  
     * @param content_p
     * @param strMimeType_p
     * @param strMimeParameter_p
     * @throws OwException
     */
    void saveVersionContent(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws OwException;

    /**
     * 
     * @return the {@link OwFNCM5Object} that this object is a version of
     * @throws OwException
     * @see OwFNCM5Object#getVersion()
     */
    OwFNCM5Object<?> getObject() throws OwException;

    /**
     * 
     * @return native user-ID that created this version
     * @throws OwException
     */
    String getCreator() throws OwException;

    /**
     * Check if this is an exclusive reservation made by the user with the given name.
     * 
     * @param userName_p
     * @return true if this version is reserved by the user with the given name
     * @throws OwException
     */
    boolean isReservedBy(String userName_p) throws OwException;
}
