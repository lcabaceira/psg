package com.wewebu.ow.server.plug.owdemo.owfax;

import java.util.Collection;
import java.util.Date;

import com.wewebu.ow.server.app.OwMainAppContext;
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
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyFileObject;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyFileObject.OwDummyFileObjectClass;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyImageFileObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Address Object.
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
public class OwAddressObject implements OwObject
{
    /** the class description for the object JUST USE THE DUMMY Class */
    public static final OwDummyFileObject.OwDummyFileObjectClass m_classDescription = new OwDummyFileObject.OwDummyFileObjectClass(false, false);

    /** map with the properties */
    protected OwPropertyCollection m_PropertyMap = new OwStandardPropertyCollection();

    /** construct a address object from a JDBC resultset */
    public OwAddressObject(java.sql.ResultSet resultset_p, OwAppContext context_p) throws Exception
    {
        int iIndex = 1;
        /*String strID            = */resultset_p.getString(iIndex++);
        String strName = resultset_p.getString(iIndex++);
        String strFamiliyName = resultset_p.getString(iIndex++);
        String strAddress = resultset_p.getString(iIndex++);

        Boolean bGender = Boolean.valueOf(resultset_p.getBoolean(iIndex++));
        String strDept = resultset_p.getString(iIndex++);
        Date dateOfBirth = resultset_p.getDate(iIndex++);
        String strFaxNumber = resultset_p.getString(iIndex++);
        String strTelNumber = resultset_p.getString(iIndex++);

        String strImage = resultset_p.getString(iIndex++);

        String strBithPlace = resultset_p.getString(iIndex++);
        String strNationality = resultset_p.getString(iIndex++);

        addProperty(OwDummyFileObjectClass.IMAGE_PROPERTY, new OwDummyImageFileObject(((OwMainAppContext) context_p).getNetwork(), strImage));
        addProperty(OwDummyFileObjectClass.ADDRESS_PROPERTY, strAddress);
        addProperty(OwDummyFileObjectClass.MALE_PROPERTY, bGender);
        addProperty(OwDummyFileObjectClass.DEPT_PROPERTY, strDept);
        addProperty(OwDummyFileObjectClass.TEL_PROPERTY, strTelNumber);
        addProperty(OwDummyFileObjectClass.FIRST_NAME_PROPERTY, strName);
        addProperty(OwDummyFileObjectClass.LAST_NAME_PROPERTY, strFamiliyName);
        addProperty(OwDummyFileObjectClass.FAX_PROPERTY, strFaxNumber);
        addProperty(OwDummyFileObjectClass.DATE_OF_BIRTH_PROPERTY, dateOfBirth);
        addProperty(OwDummyFileObjectClass.BIRTHPLACE_PROPERTY, strBithPlace);
        addProperty(OwDummyFileObjectClass.NATIONALITY_PROPERTY, strNationality);
    }

    private void addProperty(String strPropertyName_p, Object value_p) throws Exception
    {
        m_PropertyMap.put(strPropertyName_p, new OwStandardProperty(value_p, m_classDescription.getPropertyClass(strPropertyName_p)));
    }

    public String getMIMEType() throws Exception
    {
        return "ow/demoaddress";
    }

    public String getName()
    {
        return "";
    }

    /** get Object symbolic name of the object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the object which is unique among its siblings
     */
    public String getID()
    {
        return getName();
    }

    public OwObjectClass getObjectClass()
    {
        return m_classDescription;
    }

    public String getClassName()
    {
        return m_classDescription.getClassName();
    }

    public OwPropertyCollection getClonedProperties(java.util.Collection strPropertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, strPropertyNames_p);
    }

    public OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception
    {
        return m_PropertyMap;
    }

    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        return (OwProperty) m_PropertyMap.get(strPropertyName_p);
    }

    public int getType()
    {
        return m_classDescription.getType();
    }

    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
    }

    public void add(OwObject oObject_p) throws Exception
    {
    }

    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return true;
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

    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return false;
    }

    public void delete() throws Exception
    {
    }

    /** get the children of the object, does NOT cache the returned object
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of subfolders
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from DMS system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return null;
    }

    /** check if the FilterCriteria_p in getChilds is possible
     * NOTE:    The FilterCriteria_p parameter in getChilds is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *          The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return true = filter children with FilterCriteria_p is possible, false = filter is not possible / ignored
     */
    public boolean canFilterChilds() throws Exception
    {
        return false;
    }

    /** get a collection of OwFieldDefinition's for a given list of names
     * 
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return Collection of OwFieldDefinition's that can actually be filtered, may be a subset of propertynames_p, or null if no filter properties are allowed
     * @throws Exception
     */
    public java.util.Collection getFilterProperties(java.util.Collection propertynames_p) throws Exception
    {
        return null;
    }

    public java.util.Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    public OwContentCollection getContentCollection() throws Exception
    {
        return null;
    }

    public String getDMSID() throws Exception
    {
        return "";
    }

    public boolean getLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p OwStatusContextDefinitions
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock user of the object
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    public String getMIMEParameter() throws Exception
    {
        return "";
    }

    public int getPageCount() throws Exception
    {
        return 0;
    }

    public OwObjectCollection getParents() throws Exception
    {
        return null;
    }

    public OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    /** get the cloned permissions
     *
     * @return OwPermissionCollection clone of the object
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        throw new OwNotSupportedException("OwAddressObject.getClonedPermissions: Not implemented.");
    }

    /** check if permissions are accessible
     *
     * @return true = permissions can be retrieved
     */
    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    /** check if permissions can be set
     *
     * @return true = permissions can be set
     */
    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    /** set the permissions object
     *
     * @param permissions_p OwPermissionCollection to set
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwNotSupportedException("OwAddressObject.setPermissions: Not implemented.");
    }

    public OwResource getResource() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.field.OwSearchTemplate getSearchTemplate() throws Exception
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

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     * @throws Exception
     */
    public void refreshProperties(java.util.Collection props_p) throws Exception
    {
        refreshProperties();
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

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return no native object available
     */
    public Object getNativeObject() throws Exception
    {
        throw new OwObjectNotFoundException("OwAddressObject.getNativeObject: Not implemented or Not supported.");
    }

    /** implementation of the OwFieldProvider interface
     * get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        try
        {
            return getProperty(strFieldClassName_p);
        }
        catch (OwObjectNotFoundException e)
        {
            throw new OwObjectNotFoundException("OwAddressObject.getField: Property not found, strFieldClassName_p = " + strFieldClassName_p, e);
        }
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return this;
    }

    /** get the type of field provider as defined with TYPE_... */
    public int getFieldProviderType()
    {
        return TYPE_META_OBJECT;
    }

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

    /** check if object has children
     *
     * @param iContext_p OwStatusContextDefinitions
     * @return true, object has children
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the dmsid, the path is not necessarily unique,
     * but provids a readable information of the objects location.
     */
    public String getPath() throws Exception
    {
        throw new OwNotSupportedException("OwAddressObject.getPath: Not implemented.");
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object type (folder or document)
    * @param iContext_p OwStatusContextDefinitions
    * @return <code>int</code> number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return 0;
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p new class name as <code>String</code>
     * @param properties_p {@link OwPropertyCollection}  (optional, can be null to set previous properties)
     * @param permissions_p {@link OwPermissionCollection}  (optional, can be null to set previous permissions)
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwInvalidOperationException("OwAddressObject.changeClass: Not implemented.");
    }

    /** check if object can change its class */
    public boolean canChangeClass() throws Exception
    {
        return false;
    }

    /** get a name that identifies the field provider, can be used to create IDs 
     * 
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return getName();
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getProperty(sName_p).setValue(value_p);
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
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

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return getProperties(null).values();
    }

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public String getResourceID() throws Exception
    {
        try
        {
            return getResource().getID();
        }
        catch (NullPointerException e)
        {
            throw new OwObjectNotFoundException("OwAddressObject.getResourceID: Resource Id not found", e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

}