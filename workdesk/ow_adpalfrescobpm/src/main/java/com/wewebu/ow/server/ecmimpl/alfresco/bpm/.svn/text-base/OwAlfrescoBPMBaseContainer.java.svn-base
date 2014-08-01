package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Collection;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Base implementation of {@link OwWorkitemContainer}.
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
 *@since 4.0.0.0
 */
public abstract class OwAlfrescoBPMBaseContainer implements OwAlfrescoBPMWorkitemContainerInterface
{
    public static final String ID_QUEUE_INBOX = "My Tasks";
    public static final String ID_QUEUE_UNASSIGNED = "Pooled Tasks";

    private int filterType = OwWorkitemContainer.FILTER_TYPE_NORMAL;
    protected OwNetwork network;
    protected OwAlfrescoBPMRepository bpmRepository;

    public OwAlfrescoBPMBaseContainer(OwNetwork network, OwAlfrescoBPMRepository bpmRepository)
    {
        this.network = network;
        this.bpmRepository = bpmRepository;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getClassName()
     */
    public String getClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getObjectClass()
     */
    public OwObjectClass getObjectClass()
    {
        // TODO : container classification
        return new OwStandardObjectClass();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getParents()
     */
    public OwObjectCollection getParents() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#changeClass(java.lang.String, com.wewebu.ow.server.ecm.OwPropertyCollection, com.wewebu.ow.server.ecm.OwPermissionCollection)
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canChangeClass()
     */
    public boolean canChangeClass() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canFilterChilds()
     */
    public boolean canFilterChilds() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getFilterProperties(java.util.Collection)
     */
    @SuppressWarnings("rawtypes")
    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getVersionSeries()
     */
    public OwVersionSeries getVersionSeries() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#hasVersionSeries()
     */
    public boolean hasVersionSeries() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getVersion()
     */
    public OwVersion getVersion() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getProperty(java.lang.String)
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getProperties(java.util.Collection)
     */
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getClonedProperties(java.util.Collection)
     */
    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canSetProperties(int)
     */
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canGetProperties(int)
     */
    public boolean canGetProperties(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canLock()
     */
    public boolean canLock() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setLock(boolean)
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getLock(int)
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getMyLock(int)
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getLockUserID(int)
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#delete()
     */
    public void delete() throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canDelete(int)
     */
    public boolean canDelete(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#removeReference(com.wewebu.ow.server.ecm.OwObject)
     */
    public void removeReference(OwObject oObject_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canRemoveReference(com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#add(com.wewebu.ow.server.ecm.OwObject)
     */
    public void add(OwObject oObject_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canAdd(com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#move(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject)
     */
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canMove(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getSearchTemplate()
     */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getColumnInfoList()
     */
    public Collection getColumnInfoList() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getResource()
     */
    public OwResource getResource() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getPermissions()
     */
    public OwPermissionCollection getPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getClonedPermissions()
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canGetPermissions()
     */
    public boolean canGetPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canSetPermissions()
     */
    public boolean canSetPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setPermissions(com.wewebu.ow.server.ecm.OwPermissionCollection)
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getContentCollection()
     */
    public OwContentCollection getContentCollection() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setContentCollection(com.wewebu.ow.server.ecm.OwContentCollection)
     */
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canSetContent(int, int)
     */
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canGetContent(int, int)
     */
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#refreshProperties()
     */
    public void refreshProperties() throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#refreshProperties(java.util.Collection)
     */
    public void refreshProperties(Collection props_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getNativeObject()
     */
    public Object getNativeObject() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getPath()
     */
    public String getPath() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getResourceID()
     */
    public String getResourceID() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getInstance()
     */
    public OwObject getInstance() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getDMSID()
     */
    public String getDMSID() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getPageCount()
     */
    public int getPageCount() throws Exception
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getMIMEParameter()
     */
    public String getMIMEParameter() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#hasContent(int)
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getField(java.lang.String)
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#setField(java.lang.String, java.lang.Object)
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getSafeFieldValue(java.lang.String, java.lang.Object)
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFields()
     */
    public Collection getFields() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderType()
     */
    public int getFieldProviderType()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderSource()
     */
    public Object getFieldProviderSource()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderName()
     */
    public String getFieldProviderName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#setFilterType(int)
     */
    public void setFilterType(int iFilterType_p)
    {
        this.filterType = iFilterType_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#getFilterType()
     */
    public int getFilterType()
    {
        return this.filterType;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#getPublicReassignContainerNames()
     */
    public Collection getPublicReassignContainerNames() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#getPublicReassignContainerDisplayName(java.lang.String)
     */
    public String getPublicReassignContainerDisplayName(String sName_p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#canPull(int)
     */
    public boolean canPull(int iContext_p) throws Exception, OwStatusContextException
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#pull(com.wewebu.ow.server.field.OwSort, java.util.Set)
     */
    @SuppressWarnings("rawtypes")
    public OwWorkitem pull(OwSort sort_p, Set exclude_p) throws Exception, OwObjectNotFoundException, OwServerException
    {
        throw new OwObjectNotFoundException(this.getClassName() + ".pull(): Not supported.");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#getDefaultUsers()
     */
    @SuppressWarnings("rawtypes")
    public Collection getDefaultUsers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwNetwork getNetwork()
    {
        return this.network;
    }

    public OwAlfrescoBPMRepository getBpmRepository()
    {
        return bpmRepository;
    }
}
