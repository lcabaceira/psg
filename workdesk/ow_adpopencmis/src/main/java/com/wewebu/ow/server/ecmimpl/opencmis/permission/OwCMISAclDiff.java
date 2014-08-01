package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import java.util.List;

import org.apache.chemistry.opencmis.commons.data.Ace;

/**
 *<p>
 * Differential Object of Objects ACL.
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
public interface OwCMISAclDiff
{
    /**
     * List of added Ace's.
     * @return List or null if none
     */
    List<Ace> getAdded();

    /**
     * List of deleted Ace's.
     * @return List or null if none
     */
    List<Ace> getDeleted();

    /**
     * Add an ACE, maybe merged if similar is ACE-Principal is available.
     * @return true if added
     */
    boolean add(Ace newAce);

    /**
     * Remove an ACE element, can also be merged into
     * an ACE which contains similar principal.
     * @return true if marked as removed
     */
    boolean remove(Ace removeAce);

}
