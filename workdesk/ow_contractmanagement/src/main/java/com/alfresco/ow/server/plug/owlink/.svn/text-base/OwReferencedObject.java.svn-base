package com.alfresco.ow.server.plug.owlink;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwReferencedObject.<br/>
 * An @see OwObject which is a reference to an associated object.
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
public class OwReferencedObject implements OwObject
{
    private OwObjectLink association;
    private OwObject associatedObject;
    private boolean isSource = false;

    /**
     * Constructor
     * @param association OwObjectLink: association
     * @param associatedObject OwObject to associate to 
     */
    public OwReferencedObject(OwObjectLink association, OwObject associatedObject)
    {
        this.association = association;
        this.associatedObject = associatedObject;

        if (this.association.getSource().equals(associatedObject))
        {
            isSource = true;
        }
    }

    /**
     * Returns the link association. 
     * @return the link association.
     */
    public OwObjectLink getObjectLink()
    {
        return association;
    }

    /**
     * returns if is source
     * @return if is source
     */
    public boolean isSource()
    {
        return isSource;
    }

    /**
     * returns the class name of the associated object
     * @return class name of the associated object
     * @see com.wewebu.ow.server.ecm.OwObject#getClassName()
     */
    public String getClassName()
    {
        return associatedObject.getClassName();
    }

    /**
     * Returns the object class of the associated object
     * @return object class of the associated object
     * @see com.wewebu.ow.server.ecm.OwObject#getObjectClass()
     */
    public OwObjectClass getObjectClass()
    {
        return associatedObject.getObjectClass();
    }

    /**
     * Returns the parent object collection from the associated object
     * @return the parent object collection from the associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getParents()
     */
    public OwObjectCollection getParents() throws Exception
    {
        return associatedObject.getParents();
    }

    /**
     * Returns field by name from associated object
     * @param strFieldClassName_p String class name of requested field
     * @return field by name from associated object
     * @throws Exception
     * @throws OwObjectNotFoundException
     * @see com.wewebu.ow.server.field.OwFieldProvider#getField(java.lang.String)
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return associatedObject.getField(strFieldClassName_p);
    }

    /**
     * Returns the child object collection from associated object or null
     * @param iObjectTypes_p  the requested object types (folder or document)
     * @param propertyNames_p  properties to fetch from ECM system along with the children, can be null.
     * @param sort_p  Sortcriteria list to sort return list
     * @param iMaxSize_p  int maximum number of objects to retrieve
     * @param iVersionSelection_p  int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     * @return child object collection from associated object or null
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getChilds(int[], java.util.Collection, com.wewebu.ow.server.field.OwSort, int, int, com.wewebu.ow.server.field.OwSearchNode)
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return associatedObject.getChilds(iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);
    }

    /**
     *  modify a Field value, but does not save the value right away in the associated object
     *  
     * @param sName_p field name to set
     * @param value_p value to set in field
     * @throws Exception
     * @throws OwObjectNotFoundException
     * @see com.wewebu.ow.server.field.OwFieldProvider#setField(java.lang.String, java.lang.Object)
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        associatedObject.setField(sName_p, value_p);
    }

    /**
     * Returns the value of a Field from the associated object
     * @param sName_p name of field
     * @param defaultvalue_p default value to return
     * @return the value of a Field from the associated object
     * @see com.wewebu.ow.server.field.OwFieldProvider#getSafeFieldValue(java.lang.String, java.lang.Object)
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        return associatedObject.getSafeFieldValue(sName_p, defaultvalue_p);
    }

    /**
     * Returns all the properties in the form of the associated object.
     * @return all the properties in the form of the associated object. 
     * @throws Exception
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFields()
     */
    public Collection getFields() throws Exception
    {
        return associatedObject.getFields();
    }

    /** Returns the field provider type from the associated object. an be one or more of TYPE_...
     * @return  the field provider type from the associated object.
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderType()
     */
    public int getFieldProviderType()
    {
        return associatedObject.getFieldProviderType();
    }

    /** get the source object from the associated object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer or null
     * 
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderSource()
     */
    public Object getFieldProviderSource()
    {
        return associatedObject.getFieldProviderSource();
    }

    /**Returns true if object reference has child objects, false else.
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param iContext_p OwStatusContextDefinitions
     * @return true if object reference has child objects, false else.
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#hasChilds(int[], int)
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return associatedObject.hasChilds(iObjectTypes_p, iContext_p);
    }

    /**
     * Returns a name that identifies the field provider of the associated object, e.g. the name of the underlying JSP page
     * @return a name that identifies the field provider of the associated object, e.g. the name of the underlying JSP page
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderName()
     */
    public String getFieldProviderName()
    {
        return associatedObject.getFieldProviderName();
    }

    /**
     * Returns the number of children from the associated object
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param iContext_p OwStatusContextDefinitions
     * @return the number of children from the associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getChildCount(int[], int)
     */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return associatedObject.getChildCount(iObjectTypes_p, iContext_p);
    }

    /**
     * Change the class of the associated object
     * 
     * @param strNewClassName_p new class name
     * @param properties_p OwPropertyCollection (optional, can be null to set previous properties)
     * @param permissions_p OwPermissionCollection (optional, can be null to set previous permissions)
    * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#changeClass(java.lang.String, com.wewebu.ow.server.ecm.OwPropertyCollection, com.wewebu.ow.server.ecm.OwPermissionCollection)
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        associatedObject.changeClass(strNewClassName_p, properties_p, permissions_p);
    }

    /**
     * Returns the ID / name identifying the resource the associated object belongs to
     * @return  the ID / name identifying the resource the associated object belongs to
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getResourceID()
     */
    public String getResourceID() throws Exception
    {
        return associatedObject.getResourceID();
    }

    /** 
     * Returns true if class can be altered, false else
     * @return true if class can be altered, false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canChangeClass()
     */
    public boolean canChangeClass() throws Exception
    {
        return associatedObject.canChangeClass();
    }

    /** check if the FilterCriteria_p in getChilds of associated object is possible
     * NOTE:    The FilterCriteria_p parameter in getChilds is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *          The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return true = filter children with FilterCriteria_p is possible, false = filter is not possible / ignored
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canFilterChilds()
     */
    public boolean canFilterChilds() throws Exception
    {
        return associatedObject.canFilterChilds();
    }

    /**
     * Returns an instance from the associated object
     * 
     * @return instance of object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getInstance()
     */
    public OwObject getInstance() throws Exception
    {
        return associatedObject.getInstance();
    }

    /**
     * Returns associated object name property string
     * @return  associated object name property string
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getName()
     */
    public String getName()
    {
        return associatedObject.getName();
    }

    /** Returns Object symbolic name of the associated object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the associated object which is unique among its siblings
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getID()
     */
    public String getID()
    {
        return associatedObject.getID();
    }

    /**
     * Returns a collection of OwFieldDefinition's for a given list of names
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return a collection of OwFieldDefinition's for a given list of names
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getFilterProperties(java.util.Collection)
     */
    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        return associatedObject.getFilterProperties(propertynames_p);
    }

    /**
     * Returns the type of the associated object
     * @return type of associated object
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getType()
     */
    public int getType()
    {
        return associatedObject.getType();
    }

    /** 
     * Returns DMSID of associated object
     * @return DMSID of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getDMSID()
     */
    public String getDMSID() throws Exception
    {
        return associatedObject.getDMSID();
    }

    /**
     * Returns ersion series of associated object
     * @return version series of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getVersionSeries()
     */
    public OwVersionSeries getVersionSeries() throws Exception
    {
        return associatedObject.getVersionSeries();
    }

    /**
     * Returns true if associated object has a version series, false else.
     * @return true if associated object has a version series, false else.
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#hasVersionSeries()
     */
    public boolean hasVersionSeries() throws Exception
    {
        return associatedObject.hasVersionSeries();
    }

    /** 
     * Returns number of pages from associated object
     * @return Number of pages from associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getPageCount()
     */
    public int getPageCount() throws Exception
    {
        return associatedObject.getPageCount();
    }

    /**
     * Returns MIME type of associated object
     * @return MIME type of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getMIMEType()
     */
    public String getMIMEType() throws Exception
    {
        return associatedObject.getMIMEType();
    }

    /**
     * Returns the current version object from the associated object
     * @return version of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getVersion()
     */
    public OwVersion getVersion() throws Exception
    {
        return associatedObject.getVersion();
    }

    /** Returns MIME parameter of associated object
     * @return MIME parameter of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getMIMEParameter()
     */
    public String getMIMEParameter() throws Exception
    {
        return associatedObject.getMIMEParameter();
    }

    /**
     * Returns a property by name from associated object
     * @param strPropertyName_p Name of the property to return
     * @return  a property by name from associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getProperty(java.lang.String)
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        return associatedObject.getProperty(strPropertyName_p);
    }

    /**
     * Checks if the associated object has content, which can be retrieved using getContentCollection.
     * @param iContext_p OwStatusContextDefinitions
     * @return true if associated object has content, false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObjectReference#hasContent(int)
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        return associatedObject.hasContent(iContext_p);
    }

    /** 
     * Retrieves the specified properties from the associated object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return property collection of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getProperties(java.util.Collection)
     */
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        return associatedObject.getProperties(propertyNames_p);
    }

    /** 
     * Retrieves the specified properties from the associated object as a copy
     * NOTE: Other than getProperties, the returned collection contains exactly the requested properties
     * 
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return cloned properties of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getClonedProperties(java.util.Collection)
     */
    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        return associatedObject.getClonedProperties(propertyNames_p);
    }

    /**
     * Sets the properties in the associated object
     * @param properties_p Properties to set
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        associatedObject.setProperties(properties_p);
    }

    /**
     * Sets the properties in the associated object
     * @param properties_p Properties to set
     * @param mode_p  mode to use or null to use default mode, @see {@link OwObjectClass#getModes(int)}
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        associatedObject.setProperties(properties_p, mode_p);
    }

    /**
     * Checks if associated object allows to set / change properties
     * @param iContext_p OwStatusContextDefinitions
     * @return true if properties can be set, false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canSetProperties(int)
     */
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return associatedObject.canSetProperties(iContext_p);
    }

    /**
     * Checks if associated object allows to get properties
     * @param iContext_p OwStatusContextDefinitions
     * @return true if properties are gettable - false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canGetProperties(int)
     */
    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return associatedObject.canGetProperties(iContext_p);
    }

    /**
     * Checks if associated object supports lock mechanism
     * @return true if associated object can be locked, false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canLock()
     */
    public boolean canLock() throws Exception
    {
        return associatedObject.canLock();
    }

    /**
     * Locks / unlocks associated object, make it unaccessible / accessible for other users
     * @param fLock_p True: lock associated object, false: unlocks associated object
     * @return true if object was locked, false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#setLock(boolean)
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        return associatedObject.setLock(fLock_p);
    }

    /**
     * Returns the lock state of the associated object for ALL users
     * 
     * @param iContext_p OwStatusContextDefinitions
     * @return returns the lock status of the associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getLock(int)
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        return associatedObject.getLock(iContext_p);
    }

    /**
     * Returns the lock state of the associated object for the CURRENTLY logged on user
     * 
     * @param iContext_p OwStatusContextDefinitions
     * @return returns the lock status associated object for the current user
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getMyLock(int)
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return associatedObject.getMyLock(iContext_p);
    }

    /**
     * Returns the lock user of the associated object
     * 
     * @param iContext_p OwStatusContextDefinitions
     * @return returns the lock user ID of the associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getLockUserID(int)
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return associatedObject.getLockUserID(iContext_p);
    }

    /**
     * deletes object and all references
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#delete()
     */
    public void delete() throws Exception
    {
        throw new OwNotSupportedException("OwReferencedObject.delete: Referenced objects are part of an link object and do not support delete(). Use OwDocumentFunctionVFDeleteLinkObject to delete the link.");
    }

    /**
     * Checks if associated object can be deleted and if user has sufficient access rights
     * 
     * @param iContext_p OwStatusContextDefinitions
     * @return true if associated object can be deleted, false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canDelete(int)
     */
    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

    /**
     * Removes the reference of the given object from this associated object (folder)
     * @param oObject_p OwObject reference to be removed from this object (folder)
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#removeReference(com.wewebu.ow.server.ecm.OwObject)
     */
    public void removeReference(OwObject oObject_p) throws Exception
    {
        associatedObject.removeReference(oObject_p);
    }

    /** checks if the reference from the associated object can be removed
    *  this object needs to be parent of given object, and user needs to have sufficient access rights
    * @param oObject_p OwObject reference to be checked upon
    * @param iContext_p OwStatusContextDefinitions
    * @return true, if given OwObject reference can be removed from this object (folder)
    * @throws Exception
    * @see com.wewebu.ow.server.ecm.OwObject#canRemoveReference(com.wewebu.ow.server.ecm.OwObject, int)
    */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return associatedObject.canRemoveReference(oObject_p, iContext_p);
    }

    /** 
     * Adds an object reference to the parent associated object (folder)
    *
    *  @param oObject_p OwObject reference to add to
    * @throws Exception
    * @see com.wewebu.ow.server.ecm.OwObject#add(com.wewebu.ow.server.ecm.OwObject)
    */
    public void add(OwObject oObject_p) throws Exception
    {
        associatedObject.add(oObject_p);
    }

    /**
     * Checks if associated object supports add function and if user has sufficient access rights
     * 
     * @param oObject_p
     * @param iContext_p
     * @return true if associated object can be added, false else
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canAdd(com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return associatedObject.canAdd(oObject_p, iContext_p);
    }

    /**
     * Moves a object reference to this parent associated object (folder)
     * @param oObject_p Object to move
     * @param oldParent_p Old parent object to move from
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#move(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject)
     */
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        associatedObject.move(oObject_p, oldParent_p);
    }

    /**
     * Checks if move operation is allowed
     * @param oObject_p Object to move
     * @param oldParent_p Old parent object to move from
     * @param iContext_p OwStatusContextDefinitions
     * @return returns whether associated object can be moved
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canMove(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return associatedObject.canMove(oObject_p, oldParent_p, iContext_p);
    }

    /** 
     * Returns a search template associated with this Object
    *
    *  The search from the template can be used to refine the result in getChilds(...)
    *  ==> The search is automatically performed when calling getChilds(...)
    *
    *  The ColumnInfoList from the template can be used to format the result list of the children
    *  
    *
    *  NOTE: This function is especially used in virtual folders
    *
    *  @return OwSearchTemplate or null if not defined for the associated object
    * @throws Exception
    * @see com.wewebu.ow.server.ecm.OwObject#getSearchTemplate()
    */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        return associatedObject.getSearchTemplate();
    }

    /**
     * Returns  the column info list that describes the columns for the child list
     * @return List of OwFieldColumnInfo, or null if not defined
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getColumnInfoList()
     */
    public Collection getColumnInfoList() throws Exception
    {
        return associatedObject.getColumnInfoList();
    }

    /** 
    * Returns the resource the associated object belongs to in a multiple resource Network
    *
    * @return OwResource to identify the resource, or null for the default resource
    * @throws Exception
    * @see com.wewebu.ow.server.ecm.OwObject#getResource()
    */
    public OwResource getResource() throws Exception
    {
        return associatedObject.getResource();
    }

    /**
     * returns permission collection from associated object
     * @return permission collection from associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getPermissions()
     */
    public OwPermissionCollection getPermissions() throws Exception
    {
        return associatedObject.getPermissions();
    }

    /**
     * Returns cloned permission collection from associated object
     * @return cloned permission collection from associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getClonedPermissions()
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return associatedObject.getClonedPermissions();
    }

    /**
     * Checks if permissions are accessible
     * 
     * @return whether permissions can be called from associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canGetPermissions()
     */
    public boolean canGetPermissions() throws Exception
    {
        return associatedObject.canGetPermissions();
    }

    /**
     * Checks if permissions can be set
     * 
     * @return whether associated object can set permissions
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canSetPermissions()
     */
    public boolean canSetPermissions() throws Exception
    {
        return associatedObject.canSetPermissions();
    }

    /**
     * Sets the permissions of the associated object
     * 
     * @param permissions_p
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#setPermissions(com.wewebu.ow.server.ecm.OwPermissionCollection)
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        associatedObject.setPermissions(permissions_p);
    }

    /** 
     * Returns OwContentCollection of associated object
     * 
     * @return OwContentCollection of associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getContentCollection()
     */
    public OwContentCollection getContentCollection() throws Exception
    {
        return associatedObject.getContentCollection();
    }

    /**
     * Sets the content to the associated object.
     * 
     * @param content_p Content to set
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#setContentCollection(com.wewebu.ow.server.ecm.OwContentCollection)
     */
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        associatedObject.setContentCollection(content_p);
    }

    /** Checks if content can be set on the associated object with setContent
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @param iContext_p OwStatusContextDefinitions
     * @return whether content can be set in associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canSetContent(int, int)
     */
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return associatedObject.canSetContent(iContentType_p, iContext_p);
    }

    /** checks if content retrieval of associated object is allowed 
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @param iContext_p OwStatusContextDefinitions
     * @return return whether content can be called from associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#canGetContent(int, int)
     */
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return associatedObject.canGetContent(iContentType_p, iContext_p);
    }

    /**
     * Refreshes the property cache
     * 
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#refreshProperties()
     */
    public void refreshProperties() throws Exception
    {
        associatedObject.refreshProperties();
    }

    /**
     * Refreshes the property cache
     * 
     * @param props_p Collection of property names to update
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#refreshProperties(java.util.Collection)
     */
    public void refreshProperties(Collection props_p) throws Exception
    {
        associatedObject.refreshProperties(props_p);
    }

    /** get the native object from the ECM system
    *
    *  NOTE: The returned object is Opaque. 
    *           Using the native object makes the client dependent on the ECM System
    *
    * @return Object native to the ECM System from associated object
    * @throws Exception
    * @see com.wewebu.ow.server.ecm.OwObject#getNativeObject()
    */
    public Object getNativeObject() throws Exception
    {
        return associatedObject.getNativeObject();
    }

    /**
     * Returns path to associated object
     * @return path to associated object
     * @throws Exception
     * @see com.wewebu.ow.server.ecm.OwObject#getPath()
     */
    public String getPath() throws Exception
    {
        return associatedObject.getPath();
    }

}
