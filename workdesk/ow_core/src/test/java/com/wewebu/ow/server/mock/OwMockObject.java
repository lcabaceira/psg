package com.wewebu.ow.server.mock;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
 *<p>
 * OwMockObject.
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
public class OwMockObject implements OwObject
{
    private String dmsid;
    private OwPropertyCollection properties;

    public OwMockObject(String dmsid_p)
    {
        this.dmsid = dmsid_p;
    }

    public void add(OwObject oObject_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canChangeClass() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canDelete(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canFilterChilds() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canGetPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canGetProperties(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canLock() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSetPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSetProperties(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void delete() throws Exception
    {
        // TODO Auto-generated method stub

    }

    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getColumnInfoList() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwContentCollection getContentCollection() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getLock(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getLockUserID(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getMyLock(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Object getNativeObject() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwObjectClass getObjectClass()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwObjectCollection getParents() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPath() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwPermissionCollection getPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        return (OwProperty) this.properties.get(strPropertyName_p);
    }

    public OwResource getResource() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwVersion getVersion() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwVersionSeries getVersionSeries() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean hasVersionSeries() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void refreshProperties() throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void refreshProperties(Collection props_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void removeReference(OwObject oObject_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public boolean setLock(boolean fLock_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        this.properties = properties_p;

    }

    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

    public void getDMSID(String dmsid_p)
    {
        this.dmsid = dmsid_p;
    }

    public String getDMSID() throws Exception
    {
        return this.dmsid;
    }

    public String getID()
    {
        return this.dmsid;
    }

    public OwObject getInstance() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMIMEParameter() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMIMEType() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getPageCount() throws Exception
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getResourceID() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getType()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean hasContent(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFieldProviderName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getFieldProviderSource()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getFieldProviderType()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public Collection getFields() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString()
    {
        if (properties == null)
        {
            return "null-properties-object";
        }
        else
        {
            //return properties.toString();
            StringBuilder propertiesBuffer = new StringBuilder();
            propertiesBuffer.append("[ ");
            Set<Map.Entry<String, OwProperty>> entrySet = properties.entrySet();
            for (Entry<String, OwProperty> entry : entrySet)
            {
                OwProperty property = entry.getValue();
                propertiesBuffer.append(property == null ? "<null property>" : property.toString());
                propertiesBuffer.append(" ");
            }
            propertiesBuffer.append(" ]");

            return propertiesBuffer.toString();
        }
    }

}
