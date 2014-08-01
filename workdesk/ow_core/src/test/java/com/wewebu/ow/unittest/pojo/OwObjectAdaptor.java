package com.wewebu.ow.unittest.pojo;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 * Simple class to create a POJO OwObject.
 * Returning in almost every method valid values by default, but still
 * the derived class should implement the needed methods.
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
public class OwObjectAdaptor implements OwObject
{
    private int type;

    public void add(OwObject oObject_p) throws Exception
    {

    }

    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canChangeClass() throws Exception
    {
        return false;
    }

    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canFilterChilds() throws Exception
    {
        return false;
    }

    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canLock() throws Exception
    {
        return false;
    }

    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return false;
    }

    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {

    }

    public void delete() throws Exception
    {

    }

    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return 0;
    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return null;
    }

    public String getClassName()
    {
        return null;
    }

    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return null;
    }

    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        return null;
    }

    public Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    public OwContentCollection getContentCollection() throws Exception
    {
        return null;
    }

    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        return null;
    }

    public boolean getLock(int iContext_p) throws Exception
    {
        return false;
    }

    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    public Object getNativeObject() throws Exception
    {
        return null;
    }

    public OwObjectClass getObjectClass()
    {
        return null;
    }

    public OwObjectCollection getParents() throws Exception
    {
        return null;
    }

    public String getPath() throws Exception
    {
        return null;
    }

    public OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        return null;
    }

    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        return null;
    }

    public OwResource getResource() throws Exception
    {
        return null;
    }

    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        return null;
    }

    public OwVersion getVersion() throws Exception
    {
        return null;
    }

    public OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {

    }

    public void refreshProperties() throws Exception
    {

    }

    public void refreshProperties(Collection props_p) throws Exception
    {

    }

    public void removeReference(OwObject oObject_p) throws Exception
    {
    }

    public void setContentCollection(OwContentCollection content_p) throws Exception
    {

    }

    public boolean setLock(boolean fLock_p) throws Exception
    {
        return false;
    }

    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {

    }

    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {

    }

    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {

    }

    public String getDMSID() throws Exception
    {
        return null;
    }

    public String getID()
    {
        return null;
    }

    public OwObject getInstance() throws Exception
    {
        return null;
    }

    public String getMIMEParameter() throws Exception
    {
        return null;
    }

    public String getMIMEType() throws Exception
    {
        return null;
    }

    public String getName()
    {
        return null;
    }

    public int getPageCount() throws Exception
    {
        return 0;
    }

    public String getResourceID() throws Exception
    {
        return null;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type_p)
    {
        this.type = type_p;
    }

    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return null;
    }

    public String getFieldProviderName()
    {
        return null;
    }

    public Object getFieldProviderSource()
    {
        return null;
    }

    public int getFieldProviderType()
    {
        return 0;
    }

    public Collection getFields() throws Exception
    {
        return null;
    }

    public Object getSafeFieldValue(String sName_p, Object defaultValue_p)
    {
        return defaultValue_p;
    }

    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {

    }

}
