package com.wewebu.ow.server.ecm;

import java.util.Collection;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwObject wrapper to implement a decorator pattern.
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
public abstract class OwStandardDecoratorObject implements OwObject, OwPageableObject<OwObject>
{
    /** to be overridden */
    public abstract OwObject getWrappedObject();

    public boolean canAdd(OwObject object_p, int context_p) throws Exception
    {
        return getWrappedObject().canAdd(object_p, context_p);
    }

    public void add(OwObject object_p) throws Exception
    {
        getWrappedObject().add(object_p);
    }

    public boolean canChangeClass() throws Exception
    {
        return getWrappedObject().canChangeClass();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        getWrappedObject().setProperties(properties_p, mode_p);
    }

    public boolean canDelete(int context_p) throws Exception
    {
        return getWrappedObject().canDelete(context_p);
    }

    public boolean canFilterChilds() throws Exception
    {
        return getWrappedObject().canFilterChilds();
    }

    public boolean canGetContent(int contentType_p, int context_p) throws Exception
    {
        return getWrappedObject().canGetContent(contentType_p, context_p);
    }

    public boolean canGetPermissions() throws Exception
    {
        return getWrappedObject().canGetPermissions();
    }

    public boolean canGetProperties(int context_p) throws Exception
    {
        return getWrappedObject().canGetProperties(context_p);
    }

    public boolean canLock() throws Exception
    {
        return getWrappedObject().canLock();
    }

    public boolean canMove(OwObject object_p, OwObject oldParent_p, int context_p) throws Exception
    {
        return getWrappedObject().canMove(object_p, oldParent_p, context_p);
    }

    public boolean canRemoveReference(OwObject object_p, int context_p) throws Exception
    {
        return getWrappedObject().canRemoveReference(object_p, context_p);
    }

    public boolean canSetContent(int contentType_p, int context_p) throws Exception
    {
        return getWrappedObject().canSetContent(contentType_p, context_p);
    }

    public boolean canSetPermissions() throws Exception
    {
        return getWrappedObject().canSetPermissions();
    }

    public boolean canSetProperties(int context_p) throws Exception
    {
        return getWrappedObject().canSetProperties(context_p);
    }

    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        getWrappedObject().changeClass(strNewClassName_p, properties_p, permissions_p);
    }

    public void delete() throws Exception
    {
        getWrappedObject().delete();
    }

    public int getChildCount(int[] objectTypes_p, int context_p) throws Exception
    {
        return getWrappedObject().getChildCount(objectTypes_p, context_p);
    }

    public OwObjectCollection getChilds(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return getWrappedObject().getChilds(objectTypes_p, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p);
    }

    public String getClassName()
    {
        return getWrappedObject().getClassName();
    }

    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return getWrappedObject().getClonedPermissions();
    }

    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        return getWrappedObject().getClonedProperties(propertyNames_p);
    }

    public Collection getColumnInfoList() throws Exception
    {
        return getWrappedObject().getColumnInfoList();
    }

    public OwContentCollection getContentCollection() throws Exception
    {
        return getWrappedObject().getContentCollection();
    }

    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        return getWrappedObject().getFilterProperties(propertynames_p);
    }

    public boolean getLock(int context_p) throws Exception
    {
        return getWrappedObject().getLock(context_p);
    }

    public String getLockUserID(int context_p) throws Exception
    {
        return getWrappedObject().getLockUserID(context_p);
    }

    public boolean getMyLock(int context_p) throws Exception
    {
        return getWrappedObject().getMyLock(context_p);
    }

    public Object getNativeObject() throws Exception
    {
        return getWrappedObject().getNativeObject();
    }

    public OwObjectClass getObjectClass()
    {
        return getWrappedObject().getObjectClass();
    }

    public OwObjectCollection getParents() throws Exception
    {
        return getWrappedObject().getParents();
    }

    public String getPath() throws Exception
    {
        return getWrappedObject().getPath();
    }

    public OwPermissionCollection getPermissions() throws Exception
    {
        return getWrappedObject().getPermissions();
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        return getWrappedObject().getProperties(propertyNames_p);
    }

    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        return getWrappedObject().getProperty(strPropertyName_p);
    }

    public OwResource getResource() throws Exception
    {
        return getWrappedObject().getResource();
    }

    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        return getWrappedObject().getSearchTemplate();
    }

    public OwVersion getVersion() throws Exception
    {
        return getWrappedObject().getVersion();
    }

    public OwVersionSeries getVersionSeries() throws Exception
    {
        return getWrappedObject().getVersionSeries();
    }

    public boolean hasChilds(int[] objectTypes_p, int context_p) throws Exception
    {
        return getWrappedObject().hasChilds(objectTypes_p, context_p);
    }

    public boolean hasVersionSeries() throws Exception
    {
        return getWrappedObject().hasVersionSeries();
    }

    public void move(OwObject object_p, OwObject oldParent_p) throws Exception
    {
        getWrappedObject().move(object_p, oldParent_p);
    }

    public void refreshProperties() throws Exception
    {
        getWrappedObject().refreshProperties();
    }

    public void refreshProperties(Collection props_p) throws Exception
    {
        getWrappedObject().refreshProperties(props_p);
    }

    public void removeReference(OwObject object_p) throws Exception
    {
        getWrappedObject().removeReference(object_p);
    }

    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        getWrappedObject().setContentCollection(content_p);
    }

    public boolean setLock(boolean lock_p) throws Exception
    {
        return getWrappedObject().setLock(lock_p);
    }

    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        getWrappedObject().setPermissions(permissions_p);
    }

    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        getWrappedObject().setProperties(properties_p);
    }

    public String getDMSID() throws Exception
    {
        return getWrappedObject().getDMSID();
    }

    public String getID()
    {
        return getWrappedObject().getID();
    }

    public OwObject getInstance() throws Exception
    {
        return getWrappedObject().getInstance();
    }

    public String getMIMEParameter() throws Exception
    {
        return getWrappedObject().getMIMEParameter();
    }

    public String getMIMEType() throws Exception
    {
        return getWrappedObject().getMIMEType();
    }

    public String getName()
    {
        return getWrappedObject().getName();
    }

    public int getPageCount() throws Exception
    {
        return getWrappedObject().getPageCount();
    }

    public String getResourceID() throws Exception
    {
        return getWrappedObject().getResourceID();
    }

    public int getType()
    {
        return getWrappedObject().getType();
    }

    public boolean hasContent(int context_p) throws Exception
    {
        return getWrappedObject().hasContent(context_p);
    }

    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return getWrappedObject().getField(strFieldClassName_p);
    }

    public String getFieldProviderName()
    {
        return getWrappedObject().getFieldProviderName();
    }

    public Object getFieldProviderSource()
    {
        return getWrappedObject().getFieldProviderSource();
    }

    public int getFieldProviderType()
    {
        return getWrappedObject().getFieldProviderType();
    }

    public Collection getFields() throws Exception
    {
        return getWrappedObject().getFields();
    }

    public Object getSafeFieldValue(String name_p, Object defaultvalue_p)
    {
        return getWrappedObject().getSafeFieldValue(name_p, defaultvalue_p);
    }

    public void setField(String name_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getWrappedObject().setField(name_p, value_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPageableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwObject> getChildren(OwLoadContext filter) throws OwException
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return getWrappedObject().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        // unwrap the other object
        Object other = OwStandardDecoratorObject.unwrap(obj);
        return this.getWrappedObject().equals(other);
    }

    /**
     * Tries to unwrap all {@link OwStandardDecoratorObject} layers from the given object.
     * 
     * @param decorator
     * @return the first object which is not an instance of {@link OwStandardDecoratorObject}. 
     * @since 4.2.0.0
     */
    public static Object unwrap(Object decorator)
    {
        Object unwrapped = decorator;
        while (null != unwrapped && OwStandardDecoratorObject.class.isAssignableFrom(unwrapped.getClass()))
        {
            unwrapped = ((OwStandardDecoratorObject) unwrapped).getWrappedObject();
        }
        return unwrapped;
    }
}