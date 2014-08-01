package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardDecoratorObject;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISVirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString2;

/**
 *<p>
 * OwCMISAbstractObject.
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
public abstract class OwCMISAbstractObject<C extends OwCMISObjectClass> implements OwCMISObject
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractSessionObject.class);

    private C objectClass;

    public OwCMISAbstractObject(C objectClass_p)
    {
        super();
        this.objectClass = objectClass_p;
    }

    public C getObjectClass()
    {
        return objectClass;
    }

    public String getMIMEType() throws OwException
    {
        return objectClass.getMimetype();
    }

    public int getType()
    {
        return objectClass.getType();
    }

    @SuppressWarnings("rawtypes")
    public Collection getFilterProperties(Collection propertyNames_p) throws OwException
    {
        Collection<?> col = null;
        try
        {
            col = propertyNames_p == null ? objectClass.getPropertyClassNames() : propertyNames_p;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred while retrieving the list of properties names!", ex);
            throw new OwServerException(new OwString("opencmis.OwCMISAbstractObject.properties.handling.error", "Properties handling error!"), ex);
        }
        //iterate over collection and add filter able properties to the return list
        LinkedList<OwCMISPropertyClass> filteredProperties = new LinkedList<OwCMISPropertyClass>();
        for (Object name : col)
        {
            try
            {
                OwCMISPropertyClass propertyClass = objectClass.getPropertyClass(name.toString());
                //                if (prop.isQueryable(OwCMISQueryContext.GET_PROPERTY))
                //                {
                //                    retLst.add(prop);
                //                }
                filteredProperties.add(propertyClass);
            }
            catch (OwObjectNotFoundException e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Could not retrieve the property definition of \"" + name + "\" from class \"" + getClassName() + "\" (Exception: " + e.getMessage() + ")");
                }
            }
            catch (OwException e)
            {
                throw e;
            }
        }
        return filteredProperties;
    }

    public String getClassName()
    {
        return objectClass.getClassName();
    }

    public void add(OwObject object_p) throws OwException
    {
        LOG.error("OwCMISAbstractObject.add: Incomplete implementation! Add-function is not supported/implemented for object java class " + getClass().getName());
        throw new OwNotSupportedException(new OwString("opencmis.OwCMISAbstractObject.unsupported.object.operation", "Unsupported CMIS adapter object operation!"));
    }

    public boolean canAdd(OwObject object_p, int context_p) throws OwException
    {
        return false;
    }

    public boolean canChangeClass() throws OwException
    {
        //by default false
        return false;
    }

    public boolean canFilterChilds() throws OwException
    {
        //by default false
        return false;
    }

    public boolean canGetPermissions() throws OwException
    {
        return false;
    }

    public boolean canSetPermissions() throws OwException
    {
        return false;
    }

    public boolean canGetProperties(int context_p) throws OwException
    {
        return true;
    }

    public boolean canLock() throws OwException
    {
        return false;
    }

    public boolean canMove(OwObject object_p, OwObject oldParent_p, int context_p) throws OwException
    {
        return false;
    }

    public boolean canRemoveReference(OwObject object_p, int context_p) throws OwException
    {
        return false;
    }

    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws OwException
    {

    }

    public void delete() throws OwException
    {

    }

    public OwCMISPermissionCollection getClonedPermissions() throws OwException
    {
        throw new OwNotSupportedException("OwCMISObjectBase.getClonedPermissions: Not implemented.");
    }

    @SuppressWarnings("rawtypes")
    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws OwException
    {
        try
        {
            return OwStandardPropertyClass.getClonedProperties(this, propertyNames_p);
        }
        catch (OwException owException)
        {
            throw owException;
        }
        catch (Exception e)
        {
            LOG.fatal("Properties handling error! Error cloning object properties!", e);
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISAbstractObject.properties.handling.error", "Properties handling error!"), e);
        }
    }

    @SuppressWarnings("rawtypes")
    public Collection getColumnInfoList() throws OwException
    {
        return null;
    }

    public OwContentCollection getContentCollection() throws OwException
    {
        //        if (objectClass.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT)
        //        {
        //            return new OwCMISContentCollection(this, m_network);
        //        }
        //        else
        //        {
        //            return null;
        //        }
        return null;
    }

    public boolean getLock(int context_p) throws OwException
    {
        return false;
    }

    public String getLockUserID(int context_p) throws OwException
    {
        return null;
    }

    public boolean getMyLock(int context_p) throws OwException
    {
        return false;
    }

    public OwObjectCollection getParents() throws OwException
    {
        return null;
    }

    public String getPath() throws OwException
    {
        return null;
    }

    public OwCMISPermissionCollection getPermissions() throws OwException
    {
        //        CmisAccessControlListType acl = fetchACL();
        //
        //        return new OwCMISPermissionCollection(acl, getResource());
        return null;
    }

    public void setPermissions(OwPermissionCollection permissions_p) throws OwException
    {
        //TODO:
    }

    public OwSearchTemplate getSearchTemplate() throws OwException
    {
        return null;
    }

    public void move(OwObject object_p, OwObject oldParent_p) throws OwException
    {

    }

    public void refreshProperties() throws OwException
    {
        //void
    }

    @SuppressWarnings("rawtypes")
    public void refreshProperties(Collection propertyClassNames_p) throws OwException
    {
        //void

    }

    public void removeReference(OwObject object_p) throws OwException
    {

    }

    public boolean setLock(boolean lock_p) throws OwException
    {
        return false;
    }

    public void setProperties(OwPropertyCollection properties_p) throws OwException
    {
        setProperties(properties_p, null);
    }

    public OwObject getInstance() throws OwException
    {
        return this;
    }

    public String getMIMEParameter() throws OwException
    {
        return null;
    }

    public int getPageCount() throws OwException
    {
        return 0;
    }

    public String getResourceID()
    {
        return getResource().getID();
    }

    public boolean hasContent(int context_p) throws OwException
    {
        return false;
    }

    public OwField getField(String strFieldClassName_p) throws OwException
    {
        return getProperty(strFieldClassName_p);
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
        return OwFieldProvider.TYPE_META_OBJECT;
    }

    @SuppressWarnings("rawtypes")
    public Collection getFields() throws OwException
    {
        return getProperties(null).values();
    }

    public Object getSafeFieldValue(String name_p, Object defaultValue_p)
    {
        try
        {
            return getProperty(name_p).getValue();
        }
        catch (Exception e)
        {//ignore exception, this is a safe method with defined return value
            LOG.debug("OwCMISObject.getSafeFieldValue: error retrieving native value", e);
            return defaultValue_p;
        }
    }

    public void setField(String name_p, Object value_p) throws OwException
    {
    }

    @SuppressWarnings("rawtypes")
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException
    {
        OwPropertyCollection properties = getVirtualProperties(propertyNames_p);
        checkPropertyCollection(propertyNames_p, properties);
        return properties;
    }

    protected OwPropertyCollection getVirtualProperties(Collection propertyNames_p) throws OwException
    {
        OwPropertyCollection properties = new OwStandardPropertyCollection();
        C myObjectClass = getObjectClass();
        Collection<String> virtualPropertyNames = propertyNames_p;

        if (virtualPropertyNames == null)
        {
            Map<String, OwCMISVirtualPropertyClass<?>> virtualPropertyClasses = myObjectClass.getVirtualPropertyClasses(false);
            virtualPropertyNames = virtualPropertyClasses.keySet();
        }

        for (String propertyName : virtualPropertyNames)
        {
            OwCMISVirtualPropertyClass<?> virtualPropertyClass = myObjectClass.getVirtualPropertyClass(propertyName);
            if (virtualPropertyClass != null)
            {
                OwCMISProperty<?> virtualProperty = virtualPropertyClass.from(this);
                properties.put(propertyName, virtualProperty);
            }
        }

        return properties;
    }

    @Override
    public OwCMISProperty<?> getProperty(String propertyName_p) throws OwException
    {
        OwCMISProperty<?> property = getVirtualProperty(propertyName_p);
        if (property == null)
        {
            LOG.debug("OwCMISAbstractObject.getProperty(): Could not retrieve property " + propertyName_p);
            throw new OwObjectNotFoundException(new OwString2("opencmis.object.OwCMISAbstractObject.err.undefined.property", "Undefined property error! Property %1 is not defined for object class %2!", propertyName_p, getClassName()));
        }
        return property;
    }

    protected OwCMISProperty<?> getVirtualProperty(String propertyName_p) throws OwException
    {
        C myObjectClass = getObjectClass();
        OwCMISVirtualPropertyClass<?> virtualPropertyClass = myObjectClass.getVirtualPropertyClass(propertyName_p);
        if (virtualPropertyClass != null)
        {
            return virtualPropertyClass.from(this);
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected final void checkPropertyCollection(Collection requestedProperties_p, OwPropertyCollection returnedPropertyCollection_p) throws OwObjectNotFoundException
    {
        if (requestedProperties_p != null && !requestedProperties_p.isEmpty())
        {
            Set returnedPropertyNames = returnedPropertyCollection_p.keySet();
            if (!returnedPropertyNames.containsAll(requestedProperties_p))
            {
                Set<String> requestedPropertiesCopy = new HashSet<String>(requestedProperties_p);
                requestedPropertiesCopy.removeAll(returnedPropertyCollection_p.keySet());
                //TODO: localize ???
                throw new OwObjectNotFoundException("Properties " + requestedPropertiesCopy + " could not be retrieved.");
            }
        }
    }

    @Override
    public OwCMISObject createCopy(OwCMISObject copyParent_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, int[] childTypes_p) throws OwException
    {
        return null;
    }

    @Override
    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

    // children 

    public int getChildCount(int[] objectTypes_p, int context_p) throws OwException
    {
        throw new OwStatusContextException("");
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        return false;
    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        return null;
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return getObjectClass().hasVersionSeries();
    }

    //Object 

    @Override
    public boolean equals(Object obj)
    {
        Object other = OwStandardDecoratorObject.unwrap(obj);
        if (other == null || !(other instanceof OwCMISObject))
        {
            return false;
        }
        else
        {
            OwCMISObject cmisObject = (OwCMISObject) other;
            return this.getDMSID().equals(cmisObject.getDMSID());
        }
    }

    @Override
    public int hashCode()
    {
        return getDMSID().hashCode();
    }

    @Override
    public String toString()
    {
        return "OwCMISObject[" + getDMSID() + "]";
    }
}
