package com.wewebu.ow.server.ecmimpl.owsimpleadp;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Defines an object like a document, folder, custom object, root...
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
public class OwSimpleObject implements OwObject
{
    // =============================================================================
    //
    //
    // MINIMAL MEMBERS
    //
    //
    // =============================================================================    

    /** reference to the network, always good to have for callback method's */
    private OwSimpleNetwork m_network;

    /** the object class defining this object instance */
    private OwSimpleObjectClass m_objectclass;

    /** the collection of properties for this object */
    private OwPropertyCollection m_PropertyMap = new OwStandardPropertyCollection();

    // =============================================================================
    //
    //
    // MINIMAL IMPLEMENTATION
    //
    //
    // =============================================================================    

    /** create an object
     * 
     */
    public OwSimpleObject(OwSimpleNetwork owSimpleNetwork_p, OwSimpleObjectClass objectclass_p, String name_p) throws Exception
    {
        m_network = owSimpleNetwork_p;
        m_objectclass = objectclass_p;
        m_PropertyMap.put(OwSimpleObjectClass.NAME_PROPERTY, new OwStandardProperty(name_p, getObjectClass().getPropertyClass(OwSimpleObjectClass.NAME_PROPERTY)));
    }

    public OwSimpleNetwork getNetwork()
    {
        return m_network;
    }

    /** implement's a mediator pattern for clients that need access to neighbor field's, called by core
     * 
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    /** implement's a mediator pattern for clients that need access to neighbor field's, called by core
     * 
     */
    public String getFieldProviderName()
    {
        return getName();
    }

    /** implement's a mediator pattern for clients that need access to neighbor field's, called by core
     * 
     */
    public Object getFieldProviderSource()
    {
        return this;
    }

    /** implement's a mediator pattern for clients that need access to neighbor field's, called by core
     * 
     */
    public int getFieldProviderType()
    {
        return TYPE_META_OBJECT;
    }

    /** implement's a mediator pattern for clients that need access to neighbor field's, called by core
     * 
     */
    public Collection getFields() throws Exception
    {
        return getProperties(null).values();
    }

    /** implement's a mediator pattern for clients that need access to neighbor field's, called by core
     * 
     */
    public Object getSafeFieldValue(String name_p, Object defaultvalue_p)
    {
        try
        {
            return getProperty(name_p).getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get a unique ID for the object that can be used in OwSimpleNetwork.getObjectFromDMSID to recreate the object
     * 
     */
    public String getDMSID() throws Exception
    {
        return getName();
    }

    /** get an ID for the object that is unique among it's sibling's
     * 
     */
    public String getID()
    {
        return getName();
    }

    /** get the object properties
     * 
     */
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        return m_PropertyMap;
    }

    /** get one property
     *  throw OwObjectNotFoundException if not found
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwProperty prop = (OwProperty) m_PropertyMap.get(strPropertyName_p);
        if (prop == null)
        {
            throw new OwObjectNotFoundException(strPropertyName_p);
        }

        // get property from map
        return prop;
    }

    /** get this instance
     * 
     */
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    /** get the name
     * 
     */
    public String getName()
    {
        try
        {
            return (String) ((OwStandardProperty) m_PropertyMap.get(OwSimpleObjectClass.NAME_PROPERTY)).getValue();
        }
        catch (Exception e)
        {
            return "[undef]";
        }
    }

    /** get the name of the defining class
     * 
     */
    public String getClassName()
    {
        return m_objectclass.getClassName();
    }

    /** get the properties cloned, i.e. the returned value's can be modified without changing the internal state of this object
     * 
     */
    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, propertyNames_p);
    }

    /** get the defining class
     * 
     */
    public OwObjectClass getObjectClass()
    {
        return m_objectclass;
    }

    // =============================================================================
    //
    //
    // OPTIONAL IMPLEMENTATION
    //
    //
    // =============================================================================    

    public String getMIMEType() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void add(OwObject object_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public boolean canAdd(OwObject object_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canChangeClass() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canDelete(int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canFilterChilds() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canGetContent(int contentType_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canGetPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canGetProperties(int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canLock() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canMove(OwObject object_p, OwObject oldParent_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canRemoveReference(OwObject object_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSetContent(int contentType_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSetPermissions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSetProperties(int context_p) throws Exception
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

    public int getChildCount(int[] objectTypes_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public OwObjectCollection getChilds(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwPermissionCollection getClonedPermissions() throws Exception
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

    public boolean getLock(int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getLockUserID(int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getMyLock(int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Object getNativeObject() throws Exception
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

    public boolean hasChilds(int[] objectTypes_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean hasVersionSeries() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void move(OwObject object_p, OwObject oldParent_p) throws Exception
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

    public void removeReference(OwObject object_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public boolean setLock(boolean lock_p) throws Exception
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
        // TODO Auto-generated method stub

    }

    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public String getMIMEParameter() throws Exception
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

    public boolean hasContent(int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setField(String name_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        // TODO Auto-generated method stub

    }

}