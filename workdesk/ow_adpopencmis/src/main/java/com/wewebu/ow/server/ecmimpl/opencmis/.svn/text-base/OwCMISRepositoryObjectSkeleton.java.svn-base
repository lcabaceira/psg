package com.wewebu.ow.server.ecmimpl.opencmis;

import java.util.Collections;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Acl;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollectionImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Simple skeleton implementation for creating new objects into a {@link OwCMISRepositoryResource}.
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
public class OwCMISRepositoryObjectSkeleton extends OwObjectSkeleton
{
    private OwCMISResource resource;
    private OwCMISPermissionCollection permissions;

    public OwCMISRepositoryObjectSkeleton(OwNetwork network_p, OwCMISObjectClass objectClass_p, OwCMISResource resource_p, OwCMISNativeSession session, OwXMLUtil initValues) throws Exception
    {
        super(network_p, objectClass_p, initValues);
        this.resource = resource_p;

        List<Ace> emptyList = Collections.emptyList();
        Session nativeSession = session.getOpenCMISSession();
        Acl acl = nativeSession.getObjectFactory().createAcl(emptyList);
        this.permissions = new OwCMISPermissionCollectionImpl(acl, nativeSession);
    }

    public OwCMISRepositoryObjectSkeleton(OwNetwork network_p, OwCMISObjectClass objectClass_p, OwCMISResource resource_p, OwCMISNativeSession session) throws Exception
    {
        this(network_p, objectClass_p, resource_p, session, null);
    }

    @Override
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwCMISPropertyClass<?> propertyClass = getObjectClass().getPropertyClass(strPropertyName_p);
        OwProperty property = findProperty(propertyClass.getFullQualifiedName().toString());
        if (property == null)
        {
            property = getProperty(propertyClass.getNonQualifiedName());
        }

        return property;
    }

    @Override
    public OwCMISObjectClass getObjectClass()
    {
        return (OwCMISObjectClass) super.getObjectClass();
    }

    @Override
    public OwCMISResource getResource()
    {
        return this.resource;
    }

    @Override
    public OwCMISPermissionCollection getPermissions()
    {
        return this.permissions;
    }

    @Override
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        //DO nothing
    }

    @Override
    public boolean canGetPermissions()
    {
        return true;
    }

    @Override
    public boolean canSetPermissions()
    {
        return false;
    }

}