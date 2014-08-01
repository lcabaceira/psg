package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Base interface for all ECM objects. <br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwObject extends OwObjectReference, OwFieldProvider
{
    /** path delimiter for building paths */
    public static final String STANDARD_PATH_DELIMITER = "/";

    // === custom dispatcher functions to extend the functionality for special usage like sending fax
    /** get the class name of the object, the class names are defined by the ECM System
     * @return class name of object class
     */
    public abstract String getClassName();

    /** get the class description of the object, the class descriptions are defined by the ECM System
     * @return class description name of object class
     */
    public abstract OwObjectClass getObjectClass();

    /** get the containing parents of this object, does NOT cache returned objects
     * for folders it is several parent folders, for compound documents it is one parent document.
     * @return Parent Object, or null if object does not have any parents
     */
    public abstract OwObjectCollection getParents() throws Exception;

    /** get the children of the object, does NOT cache the returned object
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of subfolders
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects, or null
     */
    public abstract OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception;

    /** check if object has children
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param iContext_p int as defined in {@link OwStatusContextDefinitions}
     * @return true, object has children or throws OwStatusContextException
     */
    public abstract boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception;

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p OwStatusContextDefinitions
    * @return int number of children or throws OwStatusContextException
    */
    public abstract int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception;

    /** change the class of the object
     * 
     * @param strNewClassName_p
     * @param properties_p OwPropertyCollection (optional, can be null to set previous properties)
     * @param permissions_p OwPermissionCollection (optional, can be null to set previous permissions)
     * 
     */
    public abstract void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception;

    /** check if object can change its class */
    public abstract boolean canChangeClass() throws Exception;

    /** check if the FilterCriteria_p in getChilds is possible
     * NOTE:    The FilterCriteria_p parameter in getChilds is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *          The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return true = filter children with FilterCriteria_p is possible, false = filter is not possible / ignored
     */
    public abstract boolean canFilterChilds() throws Exception;

    /** get a collection of OwFieldDefinition's for a given list of names
     * 
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return Collection of OwFieldDefinition's that can actually be filtered, may be a subset of propertynames_p, or null if no filter properties are allowed
     * @throws Exception
     */
    public abstract java.util.Collection getFilterProperties(java.util.Collection propertynames_p) throws Exception;

    /** get the version series object to this object, if the object is versionable
     * @return a list of object versions, or null if object contains no versions
     */
    public abstract OwVersionSeries getVersionSeries() throws Exception;

    /** check if a version series object is available, i.e. the object is versionable
     * @return true if object is versionable
     */
    public abstract boolean hasVersionSeries() throws Exception;

    /** get the current version object 
     * 
     * @return OwVersion Object identifying the currently set version, or null if versions not supported
     */
    public abstract OwVersion getVersion() throws Exception;

    /** retrieve the specified property from the object.
     * NOTE: if the property was not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *       ==> Alternatively you can use the getProperties Function to retrieve a whole bunch of properties in one step, making the ECM adaptor use only one new query.
     * @param strPropertyName_p the name of the requested property
     *
     * @return a property object
     */
    public abstract OwProperty getProperty(String strPropertyName_p) throws Exception;

    /** retrieve the specified properties from the object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public abstract OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception;

    /** retrieve the specified properties from the object as a copy
     * NOTE: Other the getProperties, the returned collection returns exactly the requested properties
     * 
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public abstract OwPropertyCollection getClonedProperties(java.util.Collection propertyNames_p) throws Exception;

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     */
    public abstract void setProperties(OwPropertyCollection properties_p) throws Exception;

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     * @param mode_p mode to use or null to use default mode, @see {@link OwObjectClass#getModes(int)}
     */
    public abstract void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception;

    /** check if object allows to set / change properties
     * @param iContext_p OwStatusContextDefinitions
     * @return true if allowed
     */
    public abstract boolean canSetProperties(int iContext_p) throws Exception;

    /** check if property retrieval is allowed 
     * @param iContext_p OwStatusContextDefinitions
     * @return true if allowed
     */
    public abstract boolean canGetProperties(int iContext_p) throws Exception;

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public abstract boolean canLock() throws Exception;

    /** lock / unlock object, make it unaccessible for other users
     * @param fLock_p true to lock it, false to unlock it.
     * @return the new lock state of the object
     */
    public abstract boolean setLock(boolean fLock_p) throws Exception;

    /** get the lock state of the object for ALL users
     * @param iContext_p OwStatusContextDefinitions
     * @return the lock state of the object
     */
    public abstract boolean getLock(int iContext_p) throws Exception;

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p OwStatusContextDefinitions
     * @return the lock state of the object
     */
    public abstract boolean getMyLock(int iContext_p) throws Exception;

    /** get the lock user of the object
     *
     * @param iContext_p OwStatusContextDefinitions
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public abstract String getLockUserID(int iContext_p) throws Exception;

    /** delete object and all references from DB
     */
    public abstract void delete() throws Exception;

    /** check if object can be deleted and if user has sufficient access rights
     * @param iContext_p OwStatusContextDefinitions
     * @return true, if delete operation works on object
     */
    public abstract boolean canDelete(int iContext_p) throws Exception;

    /** removes the reference of the given object from this object (folder)
     *  this object needs to be parent of given object
     * @param oObject_p OwObject reference to be removed from this object (folder)
     */
    public abstract void removeReference(OwObject oObject_p) throws Exception;

    /** checks if the reference can be removed
     *  this object needs to be parent of given object, and user needs to have sufficient access rights
     * @param oObject_p OwObject reference to be checked upon
     * @param iContext_p OwStatusContextDefinitions
     * @return true, if given OwObject reference can be removed from this object (folder)
     */
    public abstract boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception;

    /** adds a object reference to this parent object (folder)
     *
     *  @param oObject_p OwObject reference to add to
     */
    public abstract void add(OwObject oObject_p) throws Exception;

    /** checks if object supports add function and if user has sufficient access rights
     *
     * @param oObject_p OwObject reference to be added
     * @param iContext_p OwStatusContextDefinitions
     *
     * @return true if object supports add function
     */
    public abstract boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception;

    /** moves a object reference to this parent object (folder)
     *
     *  @param oObject_p OwObject reference to add to this folder 
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     */
    public abstract void move(OwObject oObject_p, OwObject oldParent_p) throws Exception;

    /** check if move operation is allowed
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     * @param iContext_p OwStatusContextDefinitions
     */
    public abstract boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception;

    /** get a search template associated with this Object
     *
     *  The search from the template can be used to refine the result in getChilds(...)
     *  ==> The search is automatically performed when calling getChilds(...)
     *
     *  The ColumnInfoList from the template can be used to format the result list of the children
     *  
     *
     *  NOTE: This function is especially used in virtual folders
     *
     *  @return OwSearchTemplate or null if not defined for the object
     */
    public abstract OwSearchTemplate getSearchTemplate() throws Exception;

    /** get the column info list that describes the columns for the child list
     * @return List of OwFieldColumnInfo, or null if not defined
     */
    public abstract java.util.Collection getColumnInfoList() throws Exception;

    /** get the resource the object belongs to in a multiple resource Network
     *
     * @return OwResource to identify the resource, or null for the default resource
     */
    public abstract OwResource getResource() throws Exception;

    /** get the permissions object
     *
     * @return OwPermissionCollection of the object
     */
    public abstract OwPermissionCollection getPermissions() throws Exception;

    /** get the cloned permissions
     *
     * @return OwPermissionCollection clone of the object
     */
    public abstract OwPermissionCollection getClonedPermissions() throws Exception;

    /** check if permissions are accessible
     *
     * @return true = permissions can be retrieved
     */
    public abstract boolean canGetPermissions() throws Exception;

    /** check if permissions can be set
     *
     * @return true = permissions can be set
     */
    public abstract boolean canSetPermissions() throws Exception;

    /** set the permissions object
     *
     * @param permissions_p OwPermissionCollection to set
     */
    public abstract void setPermissions(OwPermissionCollection permissions_p) throws Exception;

    /** get the content of the object
     *
     * @return OwContentCollection
     */
    public abstract OwContentCollection getContentCollection() throws Exception;

    /** set the content to the object
     *
     * @param content_p OwContentCollection to store in the object
    */
    public abstract void setContentCollection(OwContentCollection content_p) throws Exception;

    /** check if content can be set on this document with setContent
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @param iContext_p OwStatusContextDefinitions
     * @return true, if content can be set with setContent
     */
    public abstract boolean canSetContent(int iContentType_p, int iContext_p) throws Exception;

    /** check if content retrieval is allowed 
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @param iContext_p OwStatusContextDefinitions
     * @return true if allowed
     */
    public abstract boolean canGetContent(int iContentType_p, int iContext_p) throws Exception;

    /** refresh the property cache  */
    public abstract void refreshProperties() throws Exception;

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     * @throws Exception
     */
    public abstract void refreshProperties(java.util.Collection props_p) throws Exception;

    /** get the native object from the ECM system
     *
     *  NOTE: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return Object native to the ECM System
     */
    public abstract Object getNativeObject() throws Exception;

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the DMSID, the path is not necessarily unique,
     * but provides a readable information of the objects location.
     */
    public abstract String getPath() throws Exception;
}