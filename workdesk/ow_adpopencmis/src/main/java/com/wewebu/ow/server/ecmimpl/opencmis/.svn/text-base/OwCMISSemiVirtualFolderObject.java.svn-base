package com.wewebu.ow.server.ecmimpl.opencmis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolder;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwSemiVirtualRecordClass;
import com.wewebu.ow.server.ecm.OwStandardSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRendition;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISTransientObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * CMIS Adaptor: Semi Virtual FolderObject.
 * Implements the {@link OwCMISNativeObject} and delegates the call to corresponding wrapped object.
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
@SuppressWarnings("rawtypes")
public class OwCMISSemiVirtualFolderObject<N extends TransientCmisObject> implements OwSemiVirtualFolder, OwCMISNativeObject<N>, OwPageableObject<OwCMISObject>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISSemiVirtualFolderObject.class);

    private OwSemiVirtualFolderAdapter adapter;

    private OwCMISNativeObject<N> wrappedObj;

    /**
     * The semi virtual folder entry configured in owbootstrap.xml
     */
    protected OwSemiVirtualRecordClass semiVirtualEntry;

    public OwCMISSemiVirtualFolderObject(OwCMISNativeObject<N> wrappedObj, OwSemiVirtualRecordClass semiVirtualEntry, OwCMISNetwork network) throws OwException
    {
        this(wrappedObj, semiVirtualEntry, new OwStandardSemiVirtualFolderAdapter(network));
    }

    public OwCMISSemiVirtualFolderObject(OwCMISNativeObject<N> wrappedObj, OwSemiVirtualRecordClass semiVirtualEntry, OwSemiVirtualFolderAdapter virtualFolderAdapter) throws OwException
    {
        if (!(wrappedObj instanceof OwPageableObject))
        {
            OwString errMessage = new OwString("opencmis.OwCMISSemiVirtualFolderObject.err.notPageable", "NotPageable: Semi Virtual folder can only be created based on a OwPageableObject!");
            throw new OwServerException(errMessage);
        }
        this.semiVirtualEntry = semiVirtualEntry;
        this.adapter = virtualFolderAdapter;
        this.wrappedObj = wrappedObj;
    }

    @Override
    public void add(OwObject object) throws Exception
    {
        getWrappedObject().add(object);
    }

    @Override
    public boolean canAdd(OwObject object, int context) throws Exception
    {
        return getWrappedObject().canAdd(object, context);
    }

    @Override
    public boolean canChangeClass() throws Exception
    {
        return this.getWrappedObject().canChangeClass();
    }

    @Override
    public boolean canDelete(int context) throws Exception
    {
        return getWrappedObject().canDelete(context);
    }

    @Override
    public boolean canFilterChilds() throws Exception
    {
        return this.getWrappedObject().canFilterChilds();
    }

    @Override
    public boolean canGetContent(int contentType, int context) throws Exception
    {
        return getWrappedObject().canGetContent(contentType, context);
    }

    @Override
    public boolean canGetPermissions() throws Exception
    {
        return getWrappedObject().canGetPermissions();
    }

    @Override
    public boolean canGetProperties(int context) throws Exception
    {
        return getWrappedObject().canGetProperties(context);
    }

    @Override
    public boolean canLock() throws Exception
    {
        return getWrappedObject().canLock();
    }

    @Override
    public boolean canMove(OwObject object, OwObject oldParent, int context) throws Exception
    {
        return getWrappedObject().canMove(object, oldParent, context);
    }

    @Override
    public boolean canRemoveReference(OwObject object, int context) throws Exception
    {
        return getWrappedObject().canRemoveReference(object, context);
    }

    @Override
    public boolean canSetContent(int contentType, int context) throws Exception
    {
        return getWrappedObject().canSetContent(contentType, context);
    }

    @Override
    public boolean canSetPermissions() throws Exception
    {
        return getWrappedObject().canSetPermissions();
    }

    @Override
    public boolean canSetProperties(int context) throws Exception
    {
        return getWrappedObject().canSetProperties(context);
    }

    @Override
    public void changeClass(String newClassName, OwPropertyCollection properties, OwPermissionCollection permissions) throws Exception
    {
        this.getWrappedObject().changeClass(newClassName, properties, permissions);
    }

    @Override
    public OwCMISObject createCopy(OwCMISObject copyParent, OwPropertyCollection properties, OwPermissionCollection permissions, int[] childTypes) throws OwException
    {
        return getWrappedObject().createCopy(copyParent, properties, permissions, childTypes);
    }

    @Override
    public void delete() throws Exception
    {
        getWrappedObject().delete();
    }

    @Override
    public int getChildCount(int[] objectTypes, int context) throws OwException
    {
        return this.getWrappedObject().getChildCount(objectTypes, context);
    }

    public OwObjectCollection getChilds(int[] objectTypes, Collection propertyNames, OwSort sort, int maxChildren, int versionSelection, OwSearchNode filterCriteria) throws OwException
    {
        return this.adapter.getChildren(this, objectTypes, propertyNames, sort, maxChildren, versionSelection, filterCriteria);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPageableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public OwIterable<OwCMISObject> getChildren(OwLoadContext loadContext) throws OwException
    {
        return (OwIterable<OwCMISObject>) this.adapter.getChildren(this, loadContext);
    }

    @Override
    public String getClassName()
    {
        return this.getWrappedObject().getClassName();
    }

    @Override
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return getWrappedObject().getClonedPermissions();
    }

    @Override
    public OwPropertyCollection getClonedProperties(Collection propertyNames) throws Exception
    {
        return getWrappedObject().getClonedProperties(propertyNames);
    }

    @Override
    public Collection getColumnInfoList() throws Exception
    {
        return getWrappedObject().getColumnInfoList();
    }

    @Override
    public OwContentCollection getContentCollection() throws Exception
    {
        return getWrappedObject().getContentCollection();
    }

    @Override
    public String getDMSID()
    {
        return getWrappedObject().getDMSID();
    }

    @Override
    public OwField getField(String strFieldClassName) throws Exception, OwObjectNotFoundException
    {
        return getWrappedObject().getField(strFieldClassName);
    }

    @Override
    public String getFieldProviderName()
    {
        return getWrappedObject().getFieldProviderName();
    }

    @Override
    public Object getFieldProviderSource()
    {
        return getWrappedObject().getFieldProviderSource();
    }

    @Override
    public int getFieldProviderType()
    {
        return getWrappedObject().getFieldProviderType();
    }

    @Override
    public Collection getFields() throws Exception
    {
        return getWrappedObject().getFields();
    }

    @Override
    public Collection getFilterProperties(Collection propertyNames) throws Exception
    {
        return this.getWrappedObject().getFilterProperties(propertyNames);
    }

    @Override
    public String getID()
    {
        return getWrappedObject().getID();
    }

    @Override
    public String getNativeID()
    {
        return getWrappedObject().getNativeID();
    }

    @Override
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    @Override
    public boolean getLock(int context) throws Exception
    {
        return getWrappedObject().getLock(context);
    }

    @Override
    public String getLockUserID(int context) throws Exception
    {
        return getWrappedObject().getLockUserID(context);
    }

    @Override
    public String getMIMEParameter() throws Exception
    {
        return getWrappedObject().getMIMEParameter();
    }

    @Override
    public String getMIMEType() throws Exception
    {
        return getWrappedObject().getMIMEType();
    }

    @Override
    public boolean getMyLock(int context) throws Exception
    {
        return getWrappedObject().getMyLock(context);
    }

    @Override
    public String getName()
    {
        return getWrappedObject().getName();
    }

    /**
     * Getter for current wrapped object.
     * @return OwCMISNativeObject
     */
    protected OwCMISNativeObject<N> getWrappedObject()
    {
        return this.wrappedObj;
    }

    @Override
    public N getNativeObject()
    {
        return getWrappedObject().getNativeObject();
    }

    @Override
    public OwCMISObjectClass getObjectClass()
    {
        return this.getWrappedObject().getObjectClass();
    }

    @Override
    public int getPageCount() throws Exception
    {
        return getWrappedObject().getPageCount();
    }

    @Override
    public OwObjectCollection getParents() throws Exception
    {
        return this.getWrappedObject().getParents();
    }

    @Override
    public String getPath() throws OwException
    {
        return getWrappedObject().getPath();
    }

    @Override
    public OwCMISPermissionCollection getPermissions() throws OwException
    {
        return getWrappedObject().getPermissions();
    }

    public OwObjectCollection getPhysicalChildren(int[] objectTypes, Collection propertyNames, OwSort sort, int maxSize, int versionSelection, OwSearchNode filterCriteria) throws OwException
    {
        try
        {
            return getWrappedObject().getChilds(objectTypes, propertyNames, sort, maxSize, versionSelection, filterCriteria);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCMISSemiVirtualFolderObject.getPhysicalChildren(): Could not retrieve the semi-virtual-folder physical children!", e);
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISSemiVirtualFolderObject.err.getPhysicalChildren", "Error enumerating virtual folder contents!"), e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwSemiVirtualFolder#getPhysicalChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public OwIterable<OwObject> getPhysicalChildren(OwLoadContext loadContext) throws OwException
    {
        try
        {
            OwPageableObject pageableObject = (OwPageableObject) getWrappedObject();
            return pageableObject.getChildren(loadContext);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCMISSemiVirtualFolderObject.getPhysicalChildren(): Could not retrieve the semi-virtual-folder physical children!", e);
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISSemiVirtualFolderObject.err.getPhysicalChildren", "Error enumerating virtual folder contents!"), e);
        }
    }

    @Override
    public OwPropertyCollection getProperties(Collection propertyNames) throws Exception
    {
        return this.getWrappedObject().getProperties(propertyNames);
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwCMISProperty getProperty(String propertyName) throws OwException
    {
        return this.getWrappedObject().getProperty(propertyName);
    }

    @SuppressWarnings("unchecked")
    public Map getPropertyMap()
    {
        return this.semiVirtualEntry.getPropertyMapping();
    }

    @Override
    public OwCMISResource getResource()
    {
        return getWrappedObject().getResource();
    }

    @Override
    public String getResourceID()
    {
        return getWrappedObject().getResourceID();
    }

    @Override
    public Object getSafeFieldValue(String name, Object defaultValue)
    {
        return getWrappedObject().getSafeFieldValue(name, defaultValue);
    }

    public OwSearchTemplate getSearchTemplate() throws OwException
    {
        OwVirtualFolderObject virtualFolder = this.adapter.getVirtualFolder(this);
        try
        {
            return virtualFolder.getSearchTemplate();
        }
        catch (OwException ce)
        {
            throw ce;
        }
        catch (Exception e)
        {
            LOG.error("OwCMISSemiVirtualFolderObject.getSearchTemplate():Error retrieving search template.", e);
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISSemiVirtualFolderObject.err.getSearchTemplate", "Error retrieving search template from virtual structure!"), e);
        }
    }

    @Override
    public OwCMISSession getSession()
    {
        return getWrappedObject().getSession();
    }

    @Override
    public int getType()
    {
        return getWrappedObject().getType();
    }

    @Override
    public OwVersion getVersion() throws Exception
    {
        return this.getWrappedObject().getVersion();
    }

    @Override
    public OwVersionSeries getVersionSeries() throws Exception
    {
        return this.getWrappedObject().getVersionSeries();
    }

    public String getVirtualFolderName()
    {
        return this.semiVirtualEntry.getVirtualFolder();
    }

    @Override
    public boolean hasChilds(int[] objectTypes, int context) throws OwException
    {
        return true;
    }

    @Override
    public boolean hasContent(int context) throws Exception
    {
        return getWrappedObject().hasContent(context);
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return this.getWrappedObject().hasVersionSeries();
    }

    public boolean includesPhysicalChildren()
    {
        return this.semiVirtualEntry.isIncludePhysicalChilds();
    }

    @Override
    public void move(OwObject object, OwObject oldParent) throws Exception
    {
        getWrappedObject().move(object, oldParent);
    }

    @Override
    public void refreshProperties() throws Exception
    {
        getWrappedObject().refreshProperties();
    }

    @Override
    public void refreshProperties(Collection props_p) throws Exception
    {
        getWrappedObject().refreshProperties(props_p);
    }

    @Override
    public void removeReference(OwObject object) throws Exception
    {
        getWrappedObject().removeReference(object);
    }

    public boolean searchSubstructure()
    {
        return semiVirtualEntry.isSearchSubstructure();
    }

    @Override
    public void setContentCollection(OwContentCollection content) throws Exception
    {
        getWrappedObject().setContentCollection(content);
    }

    @Override
    public void setField(String sName, Object value) throws Exception, OwObjectNotFoundException
    {
        getWrappedObject().setField(sName, value);
    }

    @Override
    public boolean setLock(boolean lock) throws Exception
    {
        return getWrappedObject().setLock(lock);
    }

    @Override
    public void setPermissions(OwPermissionCollection permissions) throws Exception
    {
        getWrappedObject().setPermissions(permissions);
    }

    @Override
    public void setProperties(OwPropertyCollection properties) throws OwException
    {
        getWrappedObject().setProperties(properties);
    }

    @Override
    public void setProperties(OwPropertyCollection properties, Object mode) throws OwException
    {
        getWrappedObject().setProperties(properties, mode);
    }

    @Override
    public OwCMISTransientObject<N> getTransientObject()
    {
        return getWrappedObject().getTransientObject();
    }

    @Override
    public List<OwCMISRendition> retrieveRenditions(Set<String> filter, boolean refresh) throws OwException
    {
        return getWrappedObject().retrieveRenditions(filter, refresh);
    }
}