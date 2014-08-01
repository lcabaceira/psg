package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.OwVirtualLinkPropertyClasses;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Dummy link implementation.
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
 *@since 4.1.1.0
 */

public class OwDummyObjectLink implements OwObjectLink
{
    private static final Logger LOG = OwLog.getLogger(OwDummyObjectLink.class);

    /** prefix for DMSID to identify links     */
    public static final String LINKS_PREFIX = "awl";

    private OwObjectClass linkClass;
    private Map<String, OwProperty> propertyMap;

    public OwDummyObjectLink(OwObject source, OwObject target)
    {
        this(OwDummyObjectLinkClass.getDefaultInstance().getClassName(), OwDummyObjectLinkClass.getDefaultInstance(), source, target);
    }

    public OwDummyObjectLink(String name, OwObject source, OwObject target)
    {
        this(name, OwDummyObjectLinkClass.getDefaultInstance(), source, target);
    }

    public OwDummyObjectLink(String name, OwObjectClass linkClass, OwObject source, OwObject target)
    {
        //TODO: use link class
        super();
        this.linkClass = linkClass;

        propertyMap = new HashMap<String, OwProperty>();

        OwStandardProperty nameProperty = new OwStandardProperty(name, OwResource.m_ObjectNamePropertyClass);
        propertyMap.put(OwResource.m_ObjectNamePropertyClass.getClassName(), nameProperty);

        OwStandardProperty sourceProperty = new OwStandardProperty(source, OwVirtualLinkPropertyClasses.LINK_SOURCE);
        propertyMap.put(OwVirtualLinkPropertyClasses.LINK_SOURCE.getClassName(), sourceProperty);

        OwStandardProperty targetProperty = new OwStandardProperty(target, OwVirtualLinkPropertyClasses.LINK_TARGET);
        propertyMap.put(OwVirtualLinkPropertyClasses.LINK_TARGET.getClassName(), targetProperty);

        OwStandardProperty classProperty = new OwStandardProperty(linkClass, OwResource.m_ClassDescriptionPropertyClass);
        propertyMap.put(OwResource.m_ClassDescriptionPropertyClass.getClassName(), classProperty);

    }

    @Override
    public String getClassName()
    {
        return getObjectClass().getClassName();
    }

    @Override
    public OwObjectClass getObjectClass()
    {
        return linkClass;
    }

    @Override
    public OwObjectCollection getParents() throws Exception
    {
        OwStandardObjectCollection parents = new OwStandardObjectCollection();
        parents.add(getSource());
        parents.add(getTarget());

        return parents;
    }

    @Override
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return null;
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return 0;
    }

    @Override
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {

    }

    @Override
    public boolean canChangeClass() throws Exception
    {
        return false;
    }

    @Override
    public boolean canFilterChilds() throws Exception
    {
        return false;
    }

    @Override
    public Collection<OwFieldDefinition> getFilterProperties(Collection propertynames_p) throws Exception
    {
        OwObjectClass myClass = getObjectClass();
        Collection<String> filterNames = new LinkedList<String>(myClass.getPropertyClassNames());
        filterNames.retainAll(propertynames_p);
        List<OwFieldDefinition> filteredFields = new LinkedList<OwFieldDefinition>();

        for (String name : filterNames)
        {
            filteredFields.add(myClass.getPropertyClass(name));
        }

        return filteredFields;

    }

    @Override
    public OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    @Override
    public OwVersion getVersion() throws Exception
    {
        return null;
    }

    @Override
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwProperty property = propertyMap.get(strPropertyName_p);
        if (property == null)
        {
            String msg = "OwDummyObjectLink.getProperty: Cannot find the property, propertyName = " + strPropertyName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return property;
    }

    @Override
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        OwPropertyCollection properties = new OwStandardPropertyCollection();

        if (propertyNames_p == null)
        {
            properties.putAll(propertyMap);
        }
        else
        {
            for (String name : (Collection<String>) propertyNames_p)
            {
                properties.put(name, getProperty(name));
            }
        }

        return properties;
    }

    @Override
    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, propertyNames_p);
    }

    @Override
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        Iterator it = properties_p.values().iterator();

        while (it.hasNext())
        {
            OwProperty propIn = (OwProperty) it.next();

            OwProperty prop = propertyMap.get(propIn.getPropertyClass().getClassName());

            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwDummyObjectLink.setProperties: Prop=[" + propIn.getPropertyClass().getClassName() + "], PrevValue=[" + prop.getValue() + "], NewValue=[" + propIn.getValue() + "]");
            }

            prop.setValue(propIn.getValue());
        }

    }

    @Override
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

    @Override
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    @Override
    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    @Override
    public boolean canLock() throws Exception
    {
        return false;
    }

    @Override
    public boolean setLock(boolean fLock_p) throws Exception
    {
        return false;
    }

    @Override
    public boolean getLock(int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    @Override
    public void delete() throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canDelete(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeReference(OwObject oObject_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void add(OwObject oObject_p) throws Exception
    {

    }

    @Override
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {

    }

    @Override
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        return null;
    }

    @Override
    public Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    @Override
    public OwResource getResource() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    @Override
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return null;
    }

    @Override
    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    @Override
    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    @Override
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {

    }

    @Override
    public OwContentCollection getContentCollection() throws Exception
    {
        return null;
    }

    @Override
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {

    }

    @Override
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public void refreshProperties() throws Exception
    {

    }

    @Override
    public void refreshProperties(Collection props_p) throws Exception
    {

    }

    @Override
    public Object getNativeObject() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPath() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getResourceID() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    @Override
    public String getName()
    {
        try
        {
            return getProperty(OwResource.m_ObjectNamePropertyClass.getClassName()).getValue().toString();
        }
        catch (Exception e)
        {
            LOG.error("Could not retrieve link name.", e);
            return "N/A";
        }
    }

    @Override
    public String getID()
    {
        return getClassName() + getSource().getID() + getSource().getID();
    }

    @Override
    public int getType()
    {
        return getObjectClass().getType();
    }

    @Override
    public String getDMSID() throws Exception
    {
        return OwDummyNetwork.DMS_PREFIX + "," + LINKS_PREFIX + "," + getClassName() + "," + getSource().getDMSID() + "," + getTarget().getDMSID();
    }

    @Override
    public int getPageCount() throws Exception
    {
        return 0;
    }

    @Override
    public String getMIMEType() throws Exception
    {
        return null;
    }

    @Override
    public String getMIMEParameter() throws Exception
    {
        return null;
    }

    @Override
    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    @Override
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getProperty(sName_p).setValue(value_p);
    }

    @Override
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            return getProperty(sName_p).getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    @Override
    public Collection getFields() throws Exception
    {
        return getProperties(null).values();
    }

    @Override
    public int getFieldProviderType()
    {
        return TYPE_META_OBJECT;
    }

    @Override
    public Object getFieldProviderSource()
    {
        return this;
    }

    @Override
    public String getFieldProviderName()
    {
        return getName();
    }

    @Override
    public OwObjectReference getTarget()
    {
        try
        {
            return (OwObjectReference) getProperty(OwVirtualLinkPropertyClasses.LINK_TARGET.getClassName()).getValue();
        }
        catch (Exception e)
        {
            LOG.error("Could not retrieve target.", e);
            return null;
        }
    }

    @Override
    public OwObjectReference getSource()
    {
        try
        {
            return (OwObjectReference) getProperty(OwVirtualLinkPropertyClasses.LINK_SOURCE.getClassName()).getValue();
        }
        catch (Exception e)
        {
            LOG.error("Could not retrieve source.", e);
            return null;
        }

    }

    @Override
    public OwObjectLinkRelation getRelation(OwObject obj)
    {
        if (obj != null)
        {
            String dmsid;
            try
            {
                dmsid = obj.getDMSID();
                boolean inbound = dmsid.equals(getTarget().getDMSID());
                if (dmsid.equals(getSource().getDMSID()))
                {
                    return inbound ? OwObjectLinkRelation.BOTH : OwObjectLinkRelation.OUTBOUND;
                }
                else if (inbound)
                {
                    return OwObjectLinkRelation.INBOUND;
                }
            }
            catch (Exception e)
            {
                LOG.error("Invalid relation.", e);
            }

        }

        return OwObjectLinkRelation.NONE;
    }

}
