package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecmimpl.OwAOTypesEnum;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base interface for the network access. Covers access to all objects in the ECM system.
 * Covers access to the authentication provider as well.<br/>
 * <b>NOTE</b>: There is one instance of this class in each user session. <br/><br/>
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
public interface OwNetwork<O extends OwObject> extends OwRepository<O>, OwAuthenticationProvider
{
    // === Object types for application objects from the ECM Adapter, see method getApplicationObject(s)
    /** object type for the virtual folders  (Type: OwVirtualFolderObjectFactory)
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#VIRTUAL_FOLDER}
     * */
    public static final int APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER = 1;
    /** object type for the preferences, which can be user or application defined, like user settings, recent file list... (Type: OwObject)
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#PREFERENCES} 
     * */
    public static final int APPLICATION_OBJECT_TYPE_PREFERENCES = 2;
    /** object type for the search templates (Type: OwSearchTemplate) 
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum}
     * */
    public static final int APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE = 3;
    /** object type for the XML streams (Type: org.w3c.dom.Node)
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#XML_DOCUMENT} 
     * */
    public static final int APPLICATION_OBJECT_TYPE_XML_DOCUMENT = 4;
    /** object type for the attribute bags (Type: OwAttributeBag)
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#ATTRIBUTE_BAG}
     * */
    public static final int APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG = 5;
    /** object type for the attribute bags (Type: OwAttributeBagIterator)
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#ATTRIBUTE_BAG_ITERATOR}
     * */
    public static final int APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_ITERATOR = 6;
    /** object type for the writable attribute bags like databases (Type: OwAttributeBagWritable)
    * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#ATTRIBUTE_BAG_WRITABLE}
     * */
    public static final int APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE = 7;
    /** object type for the enum collections for choicelists (Type: OwEnumCollection)
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#ENUM_COLLECTION} 
     * */
    public static final int APPLICATION_OBJECT_TYPE_ENUM_COLLECTION = 8;
    /** object type for the read only attribute bags like databases (Type: OwAttributeBagWritable), i.e.: the attributenames of the bag represent the users
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#INVERTED_ATTRIBUTE_BAG} 
     * */
    public static final int APPLICATION_OBJECT_TYPE_INVERTED_ATTRIBUTE_BAG = 9;
    /** object type representing an entry template
     * @since 3.2.0.0
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#ENTRY_TEMPLATE}
     * */
    public static final int APPLICATION_OBJECT_TYPE_ENTRY_TEMPLATE = 10;

    /** user defined object types start here
     * @deprecated since 4.2.0.0 use {@link OwAOTypesEnum#USER_START} 
     * */
    public static final int APPLICATION_OBJECT_TYPE_USER_START = 0x1000;

    // === extensible function code types for the canDo function
    /** function code used in canDo(...) method to check if user can print the given object */
    public static final int CAN_DO_FUNCTIONCODE_PRINT = 0x0001;
    /** function code used in canDo(...) method to check if user can create annotations */
    public static final int CAN_DO_FUNCTIONCODE_CREATE_ANNOTATION = 0x0002;
    /** function code used in canDo(...) method to check if user can edit annotations */
    public static final int CAN_DO_FUNCTIONCODE_EDIT_ANNOTATION = 0x0003;
    /** function code used in canDo(...) method to check if user can delete annotations */
    public static final int CAN_DO_FUNCTIONCODE_DELETE_ANNOTATION = 0x0004;
    /** function code used in canDo(...) method to check if user can save the content to disk */
    public static final int CAN_DO_FUNCTIONCODE_SAVE_CONTENT_TO_DISK = 0x0005;
    /** function code used in canDo(...) method to check if user has the ACL to modify annotations, @since 3.2.0.1 */
    public static final int CAN_DO_FUNCTIONCODE_ACL_TO_MODIFY_ANNOTATION = 0x0006;

    /** user defined function codes start here */
    public static final int CAN_DO_FUNCTIONCODE_USER_START = 0x1000;

    // === custom dispatcher functions to extend the functionality for special usage like sending fax
    /** get an additional interface, e.g. interface to a workflow engine, implements a service locator
     *
     * @param strInterfaceName_p Name of the interface
     * @param oObject_p optional object to be wrapped
     *
     * @return a reference to the interface
     */
    public abstract Object getInterface(String strInterfaceName_p, Object oObject_p) throws Exception;

    /** check if an additional interface is available, e.g. interface to a workflow engine
     * @param strInterfaceName_p Name of the interface
     * @return true, if interface is available
     */
    public abstract boolean hasInterface(String strInterfaceName_p);

    /** get current locale */
    public abstract java.util.Locale getLocale();

    // === Initialization
    /** initialize the network Adapter
     * 
     * @param context_p OwNetworkContext
     * @param networkSettings_p Settings DOM Node wrapped by OwXMLUtil
     */
    public abstract void init(OwNetworkContext context_p, OwXMLUtil networkSettings_p) throws Exception;

    /**
     * return the network context that was past during initialization
     * @return OwNetworkContext
     * @since 3.1.0.0
     */
    public OwNetworkContext getContext();

    /** set the rolemanager to use 
     * 
     * @param roleManager_p OwRoleManager
     */
    public abstract void setRoleManager(OwRoleManager roleManager_p);

    /** set the rolemanager to use 
     * 
     * @param eventManager_p OwHistoryManager to write history to, only if ECM system does not write its own history
     */
    public abstract void setEventManager(OwEventManager eventManager_p);

    // === permission functions
    /** get an instance of the edit access rights UI submodule for editing document access rights
     * Access rights are very specific to the ECM System and can not be handled generically
     * @param object_p OwObject to edit the access rights
     *
     * @return OwUIAccessRightsModul OwView derived module 
     */
    public abstract OwUIAccessRightsModul getEditAccessRightsSubModul(OwObject object_p) throws Exception;

    /** check if access rights can be edited on the Object. I.e. if a AccessRightsSubModul can be obtained
     *
     * @param object_p OwObject to edit access rights for
     *
     * @return true if access rights can be edited
     */
    public abstract boolean canEditAccessRights(OwObject object_p) throws Exception;

    // === access to the site and user documents e.g. workflows, searchtemplates, preference settings
    // It is up to the implementing ECM Adapter where the objects are stored, e.g. could also come from harddrive.

    /** get a list of Objects for the application to work, like search templates, preferences...
     * @param iTyp_p type as defined in OwNetwork.APPLICATION_OBJECT_TYPE_...
     * @param strName_p Name of the object to retrieve e.g. "userprefs"
     * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     *
     * @return Collection, which elements need to be cast to the appropriate type according to iTyp_p
     * @deprecated since 4.2.0.0 use {@link OwAOProvider} for application object retrieval 
     */
    public abstract java.util.Collection getApplicationObjects(int iTyp_p, String strName_p, boolean fForceUserSpecificObject_p) throws Exception;

    /** get a Objects for the application to work, like search templates, preferences...
    *
    * @param iTyp_p type as defined in OwNetwork.APPLICATION_OBJECT_TYPE_...
    * @param strName_p Name of the object to retrieve e.g. "userprefs"
    * @param param_p optional Object, can be null
    * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
    * @param fCreateIfNotExist_p boolean true = create if not exist
    *
    * @return Object, which needs to be cast to the appropriate type according to iTyp_p
    * @deprecated since 4.2.0.0 use {@link OwAOProvider} for application object retrieval 
    */
    public abstract Object getApplicationObject(int iTyp_p, String strName_p, Object param_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws Exception;

    /** get a Objects for the application to work, like search templates, preferences...
     *
     * @param iTyp_p type as defined in OwNetwork.APPLICATION_OBJECT_TYPE_...
     * @param strName_p Name of the object to retrieve e.g. "userprefs"
     * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     * @param fCreateIfNotExist_p boolean true = create if not exist
     *
     * 
     *
     * @return Object, which needs to be cast to the appropriate type according to iTyp_p
     * @deprecated since 4.2.0.0 use {@link OwAOProvider} for application object retrieval 
     */
    public abstract Object getApplicationObject(int iTyp_p, String strName_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws Exception;

    /** creates a new object on the ECM System using the given parameters
     *
     * @param resource_p OwResource to add to
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param strMimeType_p String MIME Types of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     *
     * @return String the ECM ID of the new created object
     */
    public abstract String createNewObject(OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p) throws Exception;

    /** creates a new object on the ECM System using the given parameters
     * has additional promote and checkin mode parameters for versionable objects
     *
     * @param fPromote_p boolean true = create a released version right away
     * @param mode_p Object checkin mode for objects, see getCheckinModes, or null to use default
     * @param resource_p OwResource to add to
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param strMimeType_p String MIME Types of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     *
     * @return String the ECM ID of the new created object
     */
    public abstract String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p,
            OwObject parent_p, String strMimeType_p, String strMimeParameter_p) throws Exception;

    /**
     * Creates a new object on the ECM System using the given parameters.<br>
     * Has additional promote and checkin mode parameters for versionable objects
     * and the extra parameter fKeepCheckedOut_p to control whether the new
     * objects are checked in automatically or not.<br />
     * <p>
     * <b>ATTENTION:</b> If keepCheckedOut is <code>true</code>, the promote flag (major/minor versioning) is ignored.
     * </p>
     * @param fPromote_p boolean true = create a released version right away
     * @param mode_p Object checkin mode for objects, see getCheckinModes, or null to use default
     * @param resource_p OwResource to add to
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param strMimeType_p String MIME Types of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     * @param fKeepCheckedOut_p true = create a new object that is checked out
     *
     * @return String the ECM ID of the new created object
     * 
     * @since 2.5.0
     */
    public abstract String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p,
            OwObject parent_p, String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) throws Exception;

    /** check, if Adapter can create a new objects
     *
     * @param resource_p OwResource to add to
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param iContext_p int as defined in {@link OwStatusContextDefinitions}
     * @return true, if object can be created
     */
    public abstract boolean canCreateNewObject(OwResource resource_p, OwObject parent_p, int iContext_p) throws Exception;

    /** creates a new empty object which can be used to set properties and then submitted to the createNewObject function 
     * 
     * @param objectclass_p OwObjectClass to create object from
     * @param  resource_p OwResource
     * @return OwObjectSkeleton
     * @throws Exception
     * 
     * @since 2.5.0
     */
    public abstract OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwResource resource_p) throws Exception;

    /** creates a cloned object with new properties on the ECM system
     *  copies the content as well
     *
     * @param obj_p OwObject to create a copy of
     * @param properties_p OwPropertyCollection of OwProperties to set, or null to keep properties
     * @param permissions_p OwPermissionCollection of OwPermissions to set, or null to keep permissions
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param childTypes_p int types of the child objects to copy with the object, can be null if no children should be copied
     *
     * @return String DMSID of created copy
     */
    public abstract String createObjectCopy(OwObject obj_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwObject parent_p, int[] childTypes_p) throws Exception;

    /** creates a cloned object with new properties on the ECM system
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param childTypes_p int types of the child objects to copy with the object, can be null if no children should be copied
     * @param iContext_p int as defined in {@link OwStatusContextDefinitions}
     *
     * @return true, if clone can be created
     */
    public abstract boolean canCreateObjectCopy(OwObject parent_p, int[] childTypes_p, int iContext_p) throws Exception;

    /** check if a extended function like print can be performed on the given object
     *
     * @param obj_p OwObject where function should be performed, or null if function does not require a object
     * @param iContext_p int as defined in {@link OwStatusContextDefinitions}
     * @param iFunctionCode_p int code of requested function as defined in CAN_DO_FUNCTIONCODE_...
     */
    public abstract boolean canDo(OwObject obj_p, int iFunctionCode_p, int iContext_p) throws Exception;

    /**
     * 
     * @return the current Role Manager
     * @since 4.1.1.1
     */
    public abstract OwRoleManager getRoleManager();
}