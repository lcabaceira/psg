package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwCMISObject.
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
public interface OwCMISObject extends OwObject
{
    OwCMISProperty<?> getProperty(String strPropertyName_p) throws OwException;

    OwCMISObjectClass getObjectClass();

    /**
     * Create a copy of this object using provided information.
     * @param copyParent_p OwCMISObject parent for created copy (can be null)
     * @param properties_p OwPropertyCollection to be set for new created copy
     * @param permissions_p OwPermissionCollection to be defined for copy object
     * @param childTypes_p int array of child types which should be also copied
     * @return OwCMISObject which represents the newly created copy
     * @throws OwException
     */
    OwCMISObject createCopy(OwCMISObject copyParent_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, int[] childTypes_p) throws OwException;

    String getResourceID();

    OwCMISPermissionCollection getPermissions() throws OwException;

    @Override
    String getDMSID();

    @Override
    int getChildCount(int[] iObjectTypes_p, int iContext_p) throws OwException;

    @Override
    boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException;

    @Override
    OwObjectCollection getChilds(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException;

    @Override
    OwCMISResource getResource();

    @Override
    String getPath() throws OwException;

    @Override
    void setProperties(OwPropertyCollection properties_p, Object mode_p) throws OwException;

    @Override
    void setProperties(OwPropertyCollection properties_p) throws OwException;

    @Override
    Object getNativeObject() throws OwException;

    String getNativeID();
}