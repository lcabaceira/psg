package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.filenet.api.core.EngineObject;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Credentials;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSID;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EngineObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5Permissions;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5Property;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5SimpleProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EngineBinding;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5VirtualBinding;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * {@link EngineObject} AWD representation.
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
public class OwFNCM5EngineObject<E extends EngineObject, R extends OwFNCM5Resource> implements OwFNCM5Object<E>, OwFNCM5VirtualObject<E>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5EngineObject.class);

    private OwFNCM5EngineObjectClass<E, R> clazz;
    private OwFNCM5DMSID dmisd;
    private OwFNCM5EngineState<E> self;

    public OwFNCM5EngineObject(E nativeObject_p, OwFNCM5EngineObjectClass<E, R> clazz_p)
    {
        super();
        this.clazz = clazz_p;

        this.self = clazz_p.createSelf(nativeObject_p);
    }

    protected final OwFNCM5EngineState<E> getSelf()
    {
        return this.self;
    }

    public OwFNCM5EngineObjectClass<E, R> getObjectClass()
    {
        return this.clazz;
    }

    public R getResource() throws OwException
    {
        return this.clazz.getResource();
    }

    public synchronized String getDMSID() throws OwException
    {
        if (this.dmisd == null)
        {
            OwFNCM5Resource resource = getResource();
            dmisd = resource.createDMSIDObject(getID());
        }

        return dmisd.getDMSID();
    }

    public String getResourceID() throws OwException
    {
        try
        {
            OwFNCM5Resource resource = getResource();
            return resource.getID();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve resource ID", e);
        }
    }

    public String getClassName()
    {
        OwFNCM5Class<E, ? extends OwFNCM5Resource> myClass = getObjectClass();
        return myClass.getClassName();
    }

    public OwObjectCollection getParents() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        throw new OwStatusContextException("");
    }

    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public boolean canChangeClass() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canFilterChilds() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Collection getFilterProperties(Collection propertynames_p) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwFNCM5Property getProperty(String strPropertyName_p) throws OwException
    {
        OwPropertyCollection singleValueResult = getProperties(Arrays.asList(new String[] { strPropertyName_p }));
        OwFNCM5Property property = (OwFNCM5Property) singleValueResult.get(strPropertyName_p);
        if (property == null)
        {
            throw new OwObjectNotFoundException("Could not retrieve " + strPropertyName_p + " property.");
        }

        return property;
    }

    private Map<String, OwProperty> getVirtualProperties(Set<String> virtualPropertiesNames_p) throws OwException
    {
        Map<String, OwProperty> properties = new LinkedHashMap<String, OwProperty>();
        OwFNCM5EngineObjectClass<E, R> objectClass = getObjectClass();

        for (String propertyName : virtualPropertiesNames_p)
        {
            OwFNCM5PropertyClass virtualPropertyClass = objectClass.getPropertyClass(propertyName);
            OwFNCM5VirtualBinding virtualBinding = virtualPropertyClass.getVirtualBinding();

            if (virtualBinding != null)
            {
                OwProperty property = virtualBinding.propertyOf(this);
                properties.put(propertyName, property);

            }
            else
            {
                LOG.error("Virtual access of a non virtual property class " + virtualPropertyClass.toString());
            }
        }

        return properties;
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException
    {
        OwFNCM5EngineObjectClass<E, R> objectClass = getObjectClass();
        Set<String> propertySet = new LinkedHashSet<String>();

        if (propertyNames_p == null)
        {
            propertyNames_p = objectClass.getPropertyClassNames();
        }

        propertySet.addAll(propertyNames_p);

        List<String> virtualPropertiesNames = objectClass.getVirtualPropertiesClassNames();
        Set<String> virtualPropertiesToRetrieve = new HashSet<String>();
        for (String pn : virtualPropertiesNames)
        {
            if (propertySet.contains(pn))
            {
                propertySet.remove(pn);
                virtualPropertiesToRetrieve.add(pn);
            }
        }

        Map<String, OwProperty> virtualProperties = getVirtualProperties(virtualPropertiesToRetrieve);
        Set<String> retrievedVirtualProperties = virtualProperties.keySet();

        if (!virtualPropertiesToRetrieve.equals(retrievedVirtualProperties))
        {
            virtualPropertiesToRetrieve.removeAll(retrievedVirtualProperties);
            throw new OwObjectNotFoundException("Error retrieving virtual properties. " + (virtualPropertiesToRetrieve.size() > 1 ? "is" : "are" + " missing."));
        }

        String[] namesArray = propertySet.toArray(new String[propertySet.size()]);

        OwPropertyCollection properties = self.get(namesArray);

        if (properties.size() != propertySet.size())
        {
            propertySet.removeAll(properties.keySet());
            if (!propertySet.isEmpty())
            {
                throw new OwObjectNotFoundException("Could not retrieve the following properties : " + propertySet.toString());
            }
        }

        if (!virtualProperties.isEmpty())
        {
            properties.putAll(virtualProperties);
        }

        return properties;
    }

    public void refreshProperties() throws OwException
    {
        self.refresh();
    }

    public void refreshProperties(Collection properties_p) throws OwException
    {
        if (properties_p != null)
        {
            self.refresh((String[]) properties_p.toArray(new String[properties_p.size()]));
        }
    }

    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws OwException
    {
        try
        {
            return OwStandardPropertyClass.getClonedProperties(this, propertyNames_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not create cloned properties!", e);
        }
    }

    public void setProperties(OwPropertyCollection properties_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public boolean canSetProperties(int iContext_p) throws OwException
    {
        return false;
    }

    public boolean canGetProperties(int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void delete() throws OwException
    {
        // TODO Auto-generated method stub

    }

    public boolean canDelete(int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeReference(OwObject oObject_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void add(OwObject oObject_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public boolean canAdd(OwObject oObject_p, int iContext_p) throws OwException
    {
        return false;
    }

    public void move(OwObject oObject_p, OwObject oldParent_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public OwSearchTemplate getSearchTemplate() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getColumnInfoList() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwFNCM5Permissions getPermissions() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwPermissionCollection getClonedPermissions() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean canGetPermissions() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSetPermissions() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setPermissions(OwPermissionCollection permissions_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public OwContentCollection getContentCollection() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setContentCollection(OwContentCollection content_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public boolean canSetContent(int iContentType_p, int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canGetContent(int iContentType_p, int iContext_p) throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getPath() throws OwException
    {
        OwObjectCollection parents = getParents();
        if (parents == null)
        {
            return null;
        }
        else
        {
            OwObject parent = (OwObject) parents.iterator().next();
            String path;
            try
            {
                path = parent.getPath();
                return path + "/" + getName();
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwServerException("Could not retrieve path from parent", e);
            }
        }
    }

    public OwObject getInstance() throws OwException
    {
        return this;
    }

    //    public abstract String getName();
    //
    //    public abstract OwVersionSeries getVersionSeries() throws OwException;

    public int getType()
    {
        return getObjectClass().getType();
    }

    public int getPageCount() throws OwException
    {
        return 0;
    }

    public String getMIMEType() throws OwException
    {
        return "";
    }

    public String getMIMEParameter() throws OwException
    {
        return "";
    }

    public boolean hasContent(int iContext_p) throws OwException
    {
        return false;
    }

    public OwField getField(String strFieldClassName_p) throws OwException, OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    public void setField(String sName_p, Object value_p) throws OwException, OwObjectNotFoundException
    {
        OwFNCM5Property property = getProperty(sName_p);
        property.setValue(value_p);

    }

    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            OwFNCM5Property property = getProperty(sName_p);
            return property.getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    public Collection getFields() throws OwException
    {
        return getProperties(null).values();
    }

    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT;
    }

    public Object getFieldProviderSource()
    {
        return this;
    }

    public String getFieldProviderName()
    {
        return getName();
    }

    public final E getNativeObject()
    {
        return self.getEngineObject();
    }

    protected final void replaceNativeObject(E nativeObject_p)
    {
        this.self = clazz.createSelf(nativeObject_p);
        this.dmisd = null;
    }

    public OwFNCM5Network getNetwork() throws OwException
    {
        OwFNCM5Resource resource = getResource();
        return resource.getNetwork();
    }

    /**
     * Helper method to create virtual properties for given property class.
     * This method will be called for all non-native defined properties,
     * like for the <code>OW_ObjectName</code> property.
     * @param propertyClass_p OwPropertyClass representing virtual property
     * @return OwFNCM5Property for the virtual definition, or null if unsupported
     */
    public OwProperty createVirtualProperty(OwFNCM5VirtualPropertyClass propertyClass_p) throws OwException
    {
        String propertyClassName = propertyClass_p.getClassName();

        if (OwResource.m_ClassDescriptionPropertyClass.getClassName().equals(propertyClassName))
        {
            return new OwFNCM5SimpleProperty(getObjectClass(), propertyClass_p);
        }
        else if (OwResource.m_ResourcePropertyClass.getClassName().equals(propertyClassName))
        {
            return new OwFNCM5SimpleProperty(getResource(), propertyClass_p);
        }
        else
        {
            OwFNCM5EngineBinding<?, ?, ?> engineBinding = propertyClass_p.getEngineBinding();
            if (engineBinding != null)
            {
                String symbolicName = engineBinding.getSymbolicName();
                return self.get(symbolicName);
            }
            else
            {
                throw new OwObjectNotFoundException("Could not create virtual property for class " + propertyClass_p.getClassName());
            }
        }
    }

    public boolean canLock() throws Exception
    {
        return false;
    }

    public boolean setLock(boolean fLock_p) throws Exception
    {
        return !fLock_p;
    }

    public boolean getLock(int iContext_p) throws OwException
    {
        return false;
    }

    public boolean getMyLock(int iContext_p) throws OwException
    {
        return false;
    }

    public String getLockUserID(int iContext_p) throws OwException
    {
        return null;
    }

    public String getName()
    {
        return getID();
    }

    public String getID()
    {
        EngineObject engineObject = self.getEngineObject();
        String className = engineObject.getClassName();
        return "a " + className;
    }

    public boolean hasVersionSeries() throws OwException
    {
        return false;
    }

    public OwFNCM5Version<?> getVersion() throws OwException
    {
        return null;
    }

    public OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof OwFNCM5Object)
        {
            OwFNCM5Object<E> engineObject = (OwFNCM5Object<E>) obj;
            try
            {
                return getNativeObject().equals(engineObject.getNativeObject());
            }
            catch (OwException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return getID().hashCode();
    }

    public boolean hasWorkflowDescriptions()
    {
        return false;
    }

    public Set<OwFNCM5Object<?>> getWorkflowDescriptions() throws OwException
    {
        return Collections.emptySet();
    }

    public String toString()
    {
        return getName();
    }

    protected final String getUserShortName() throws OwException
    {
        OwFNCM5Network network = getNetwork();
        String userShortName = null;
        try
        {
            OwFNCM5Credentials credentials = network.getCredentials();
            OwUserInfo userInfo = credentials.getUserInfo();
            userShortName = userInfo.getUserShortName();
            return userShortName;
        }
        catch (Exception e)
        {
            LOG.error("Error during request of current user information!", e);
            throw new OwServerException("Cannot get user credentials from adapter", e);
        }
    }

}
