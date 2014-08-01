package com.wewebu.ow.server.history;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;

/**
 *<p>
 * Standard implementation of the OwHistoryObjectCreateEvent interface.
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
public class OwStandardHistoryObjectCreateEvent implements OwHistoryObjectCreateEvent
{

    private OwPropertyCollection m_properties;
    private String m_classname;
    private String m_dmsid;
    private OwResource m_resource;
    private Boolean m_promote;
    private OwPermissionCollection m_permissions;
    private OwObject m_parent;
    private Object m_mode;

    public OwStandardHistoryObjectCreateEvent(String classname_p, String dmsid_p, OwPropertyCollection properties_p, OwResource resource_p, OwObject parent_p, OwPermissionCollection permissions_p, Boolean promote_p, Object mode_p)
    {
        this.m_properties = properties_p;
        this.m_classname = classname_p;
        this.m_dmsid = dmsid_p;
        this.m_resource = resource_p;
        this.m_promote = promote_p;
        this.m_permissions = permissions_p;
        this.m_parent = parent_p;
        this.m_mode = mode_p;
    }

    public OwStandardHistoryObjectCreateEvent(String classname_p, String dmsid_p, OwPropertyCollection properties_p, OwResource resource_p, OwObject parent_p, OwPermissionCollection permissions_p)
    {
        this.m_properties = properties_p;
        this.m_classname = classname_p;
        this.m_dmsid = dmsid_p;
        this.m_resource = resource_p;
        this.m_permissions = permissions_p;
        this.m_parent = parent_p;
    }

    public OwStandardHistoryObjectCreateEvent(String classname_p, String dmsid_p, OwPropertyCollection properties_p)
    {
        this.m_properties = properties_p;
        this.m_classname = classname_p;
        this.m_dmsid = dmsid_p;
    }

    public OwPropertyCollection getProperties()
    {

        return m_properties;
    }

    public String getClassName()
    {

        return m_classname;
    }

    public String getDmsid()
    {

        return m_dmsid;
    }

    public String getSummary() throws Exception
    {
        return getClassName();
    }

    public Object getMode()
    {
        // TODO Auto-generated method stub
        return m_mode;
    }

    public OwObject getParent()
    {
        // TODO Auto-generated method stub
        return m_parent;
    }

    public OwPermissionCollection getPermissions()
    {
        // TODO Auto-generated method stub
        return m_permissions;
    }

    public Boolean getPromote()
    {
        // TODO Auto-generated method stub
        return m_promote;
    }

    public OwResource getResource()
    {
        // TODO Auto-generated method stub
        return m_resource;
    }

}
