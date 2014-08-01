package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * An application {@link OwObject} provider.  Implementors of this interface
 * provide application objects as {@link OwObject}s from different persistence systems 
 * (egg. file system , ECM system ).
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
public interface OwAOSupport
{
    /**
     * Returns the persistent application {@link OwObject} that is matched by the given name.
     * @param strName_p name of the object to retrieve e.g. "userprefs"
     * @param forceUserspecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     * @param createIfNotExist_p if <code>true</code> the object will be created if it does not exist
     * @return an application object stored as {@link OwObject}
     * @throws OwException if the given object could not be retrieved
     */
    OwObject getSupportObject(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException;

    /**
     * Returns a collection of persistent application {@link OwObject}s that are matched by the given name.
     * @param strName_p name of the objects to retrieve e.g. "userprefs"
     * @param forceUserspecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     * @param createIfNotExist_p if <code>true</code> the objects container will be created if it does not exist
     * @return a collection object stored as {@link OwObject}. Some entries might be null if 
     *         the implementation of this support allows it (usually null entries signal  objects that 
     *         could not be retrieved).
     * @throws OwException if the given object could not be retrieved
     */
    OwObject[] getSupportObjects(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException;
}
