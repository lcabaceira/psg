package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.Set;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * AWD object delegate implementation. 
 * Delegates functionality to other AWD object. 
 * Functionality can be overridden by inheritance.       
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
public abstract class OwFNCM5DelegateObject<N> implements OwFNCM5Object<N>, OwPageableObject<OwFNCM5Object<?>>
{

    protected abstract OwFNCM5Object<?> get();

    public String getClassName()
    {
        return get().getClassName();
    }

    public OwObjectClass getObjectClass()
    {
        return get().getObjectClass();
    }

    public OwObjectCollection getParents() throws Exception
    {
        return get().getParents();
    }

    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return get().getField(strFieldClassName_p);
    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return get().getChilds(iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);
    }

    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        get().setField(sName_p, value_p);
    }

    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        return get().getSafeFieldValue(sName_p, defaultvalue_p);
    }

    public Collection getFields() throws Exception
    {
        return get().getFields();
    }

    public int getFieldProviderType()
    {
        return get().getFieldProviderType();
    }

    public Object getFieldProviderSource()
    {
        return this;
    }

    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return get().hasChilds(iObjectTypes_p, iContext_p);
    }

    public String getFieldProviderName()
    {
        return get().getFieldProviderName();
    }

    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return get().getChildCount(iObjectTypes_p, iContext_p);
    }

    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        get().changeClass(strNewClassName_p, properties_p, permissions_p);
    }

    public String getResourceID() throws Exception
    {
        return get().getResourceID();
    }

    public boolean canChangeClass() throws Exception
    {
        return get().canChangeClass();
    }

    public OwObject getInstance() throws Exception
    {
        return get().getInstance();
    }

    public boolean canFilterChilds() throws Exception
    {
        return get().canFilterChilds();
    }

    public String getName()
    {
        return get().getName();
    }

    public String getID()
    {
        return get().getID();
    }

    public int getType()
    {
        return get().getType();
    }

    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        return get().getFilterProperties(propertynames_p);
    }

    public String getDMSID() throws OwException
    {
        return get().getDMSID();
    }

    public OwVersionSeries getVersionSeries() throws Exception
    {
        return get().getVersionSeries();
    }

    public int getPageCount() throws Exception
    {
        return get().getPageCount();
    }

    public boolean hasVersionSeries() throws Exception
    {
        return get().hasVersionSeries();
    }

    public String getMIMEType() throws Exception
    {
        try
        {
            return get().getMIMEType();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve MIME type", e);
        }
    }

    public OwFNCM5Version<?> getVersion() throws OwException
    {
        return get().getVersion();
    }

    public String getMIMEParameter() throws Exception
    {
        return get().getMIMEParameter();
    }

    public boolean hasContent(int iContext_p) throws Exception
    {
        return get().hasContent(iContext_p);
    }

    public OwProperty getProperty(String strPropertyName_p) throws OwException
    {
        return get().getProperty(strPropertyName_p);
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException
    {
        return get().getProperties(propertyNames_p);
    }

    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws OwException
    {
        return get().getClonedProperties(propertyNames_p);
    }

    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        get().setProperties(properties_p);
    }

    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        get().setProperties(properties_p, mode_p);
    }

    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return get().canSetProperties(iContext_p);
    }

    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return get().canGetProperties(iContext_p);
    }

    public boolean canLock() throws Exception
    {
        return get().canLock();
    }

    public boolean setLock(boolean fLock_p) throws Exception
    {
        return get().setLock(fLock_p);
    }

    public boolean getLock(int iContext_p) throws Exception
    {
        return get().getLock(iContext_p);
    }

    public boolean getMyLock(int iContext_p) throws Exception
    {
        return get().getMyLock(iContext_p);
    }

    public String getLockUserID(int iContext_p) throws Exception
    {
        return get().getLockUserID(iContext_p);
    }

    public void delete() throws OwException
    {
        get().delete();
    }

    public boolean canDelete(int iContext_p) throws Exception
    {
        return get().canDelete(iContext_p);
    }

    public void removeReference(OwObject oObject_p) throws Exception
    {
        get().removeReference(oObject_p);
    }

    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return get().canRemoveReference(oObject_p, iContext_p);
    }

    public void add(OwObject oObject_p) throws Exception
    {
        get().add(oObject_p);
    }

    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return get().canAdd(oObject_p, iContext_p);
    }

    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        get().move(oObject_p, oldParent_p);
    }

    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return get().canMove(oObject_p, oldParent_p, iContext_p);
    }

    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        return get().getSearchTemplate();
    }

    public Collection getColumnInfoList() throws Exception
    {
        return get().getColumnInfoList();
    }

    public OwFNCM5Resource getResource() throws OwException
    {
        return get().getResource();
    }

    public OwPermissionCollection getPermissions() throws Exception
    {
        return get().getPermissions();
    }

    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return get().getClonedPermissions();
    }

    public boolean canGetPermissions() throws Exception
    {
        return get().canGetPermissions();
    }

    public boolean canSetPermissions() throws Exception
    {
        return get().canSetPermissions();
    }

    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        get().setPermissions(permissions_p);
    }

    public OwContentCollection getContentCollection() throws Exception
    {
        return get().getContentCollection();
    }

    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        get().setContentCollection(content_p);
    }

    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return get().canSetContent(iContentType_p, iContext_p);
    }

    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return get().canGetContent(iContentType_p, iContext_p);
    }

    public void refreshProperties(Collection propertyNames_p) throws Exception
    {
        get().refreshProperties(propertyNames_p);
    }

    public String getPath() throws Exception
    {
        return get().getPath();
    }

    public OwFNCM5Network getNetwork() throws OwException
    {
        return get().getNetwork();
    }

    public void refreshProperties() throws Exception
    {
        get().refreshProperties();
    }

    public boolean hasWorkflowDescriptions()
    {
        return get().hasWorkflowDescriptions();
    }

    public Set<OwFNCM5Object<?>> getWorkflowDescriptions() throws OwException
    {
        return get().getWorkflowDescriptions();
    }

    @Override
    public boolean equals(Object obj)
    {
        return get().equals(obj);
    }

    @Override
    public int hashCode()
    {
        return get().hashCode();
    }

    @Override
    public String toString()
    {
        return getName() + " " + getID() + " " + getClassName();
    }

    @Override
    public OwIterable<OwFNCM5Object<?>> getChildren(OwLoadContext loadContext) throws OwException
    {
        OwFNCM5Object<?> o = get();
        if (o instanceof OwPageableObject)
        {
            return ((OwPageableObject) o).getChildren(loadContext);
        }
        else
        {
            throw new OwNotSupportedException("Not supported delegate call.");
        }
    }
}
