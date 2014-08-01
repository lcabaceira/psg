package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.Acl;

import com.wewebu.ow.server.ecm.OwPermissionCollection;

/**
 *<p>
 * Interface extended from OwPermissionCollection for (Apache Chemistry) CMIS handling.
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
public interface OwCMISPermissionCollection extends OwPermissionCollection
{
    /**
     * Get the native object, where to process ACL changes.
     * @return Access Control list
     */
    CmisObject getNativeObject();

    /**
     * Get an object which provided separate the added and deleted ACE's.
     * @return OwCMISAclDiff
     */
    OwCMISAclDiff getDiff();

    /**
     * Reset the native object to provided one.
     * @param newAcl_p Acl
     */
    void reset(Acl newAcl_p);

    /**
     * Get the native Session for current permission collection. 
     * @return Session
     */
    Session getSession();
}
