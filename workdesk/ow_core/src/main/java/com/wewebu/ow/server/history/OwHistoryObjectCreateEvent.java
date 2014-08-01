package com.wewebu.ow.server.history;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.event.OwEvent;

/**
 *<p>
 * Interface for history events used with the history manager to add 
 * new events to the database.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwHistoryObjectCreateEvent extends OwEvent
{
    /** get the DMSID of the new object */
    public abstract String getDmsid();

    /** get the classname of the new object */
    public abstract String getClassName();

    /** get the resource of the new object, or null if not used in the creation of the object */
    public abstract OwResource getResource();

    /** get the permissions of the new object, or null if not used in the creation of the object */
    public abstract OwPermissionCollection getPermissions();

    /** get the parent object of the new object, or null if not used in the creation of the object */
    public abstract OwObject getParent();

    /** get the promote flag of the new object, or null if not used in the creation of the object */
    public abstract Boolean getPromote();

    /** get the create mode of the new object, or null if not used in the creation of the object */
    public abstract Object getMode();

    /** return a list of the new properties (OwField), or null if no properties where used in the creation of the object */
    public abstract com.wewebu.ow.server.ecm.OwPropertyCollection getProperties();
}
