package com.wewebu.ow.server.role;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Interface for the role manager.<br/>
 * Override this class to implement your own role manager
 * and set the role manager in the bootstrap settings.<br/>
 * You get a instance of the RoleManager by calling getContext().getRoleManager().
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
public interface OwRoleManager
{
    // === ROLE Categories    

    /** category for the plugins to check against role system, used in isAllowed(...) */
    public static final int ROLE_CATEGORY_PLUGIN = 1;
    /** category for virtual queues to check against role system, used in isAllowed(...)*/
    public static final int ROLE_CATEGORY_VIRTUAL_QUEUE = 3;
    /** category for the search templates displayed in search to check against role system, used in isAllowed(...) */
    public static final int ROLE_CATEGORY_SEARCH_TEMPLATE = 5;
    /** category for the standard functions to check against role system, used in isAllowed(...) */
    public static final int ROLE_CATEGORY_STANDARD_FUNCTION = 6;
    /** category for the design */
    public static final int ROLE_CATEGORY_DESIGN = 7;
    /** category for the selective configuration */
    public static final int ROLE_CATEGORY_SELECTIVE_CONFIGURATION = 8;
    /** category for object classes */
    public static final int ROLE_CATEGORY_OBJECT_CLASSES = 9;
    /** category for index fields */
    public static final int ROLE_CATEGORY_INDEX_FIELDS = 10;
    /** category for startup folder */
    public static final int ROLE_CATEGORY_STARTUP_FOLDER = 11;
    /** category for startup folder */
    public static final int ROLE_CATEGORY_VIRTUAL_FOLDER = 12;

    /**
     * Category for process definitions.
     * @since 4.2.0.1
     */
    public static final int ROLE_CATEGORY_BPM_PROCESS_DEFINITION = 13;

    /** user defined categories in overridden role managers start here */
    public static final int ROLE_CATEGORY_USER_START = 0x1000;

    /** resource context CREATE */
    public static final int ROLE_RESOURCE_CONTEXT_CREATE = 1;
    /** resource context CHECKIN */
    public static final int ROLE_RESOURCE_CONTEXT_CHECKIN = 2;
    /** resource context VIEW */
    public static final int ROLE_RESOURCE_CONTEXT_VIEW = 3;

    /** array of all predefined categories */
    public static final int[] m_predefinedcategories = new int[] { ROLE_CATEGORY_PLUGIN, ROLE_CATEGORY_VIRTUAL_QUEUE, ROLE_CATEGORY_SEARCH_TEMPLATE, ROLE_CATEGORY_STANDARD_FUNCTION, ROLE_CATEGORY_STARTUP_FOLDER, ROLE_CATEGORY_VIRTUAL_FOLDER,
            ROLE_CATEGORY_BPM_PROCESS_DEFINITION };

    // === Access rights

    /** default access right: the role has no access to the resource */
    public static final int ROLE_ACCESS_RIGHT_NOT_ALLOWED = 0;
    /** the role is granted to the resource */
    public static final int ROLE_ACCESS_RIGHT_ALLOWED = 1;
    /** the role is explicitly denied for that resource. Users of this role have no access to the resource even if it is allowed by other roles the user is in. */
    public static final int ROLE_ACCESS_RIGHT_DENIED = 2;

    // === Access mask flags

    /** access mask flag for Object classes: view objects of this class */
    public static final int ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW = 0x0001; // 1
    /** access mask flag for Object classes: create objects of this class */
    public static final int ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE = 0x0002; // 2
    /** access mask flag for Object classes: checkin objects of this class */
    public static final int ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CHECKIN = 0x0004; // 4

    /** access mask flag for Object classes: view this property */
    public static final int ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW = 0x0001; // 1
    /** access mask flag for Object classes: modify this property */
    public static final int ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_MODIFY = 0x0002; // 2

    /** access mask flag for startup folder: modify this property */
    public static final int ROLE_ACCESS_MASK_FLAG_DYNAMIC_RESOURCE_MODIFY = 0x0001; // 1

    // === Standard resources (Functions)

    /** resource ID */
    public static final String STD_FUNC_CAN_EDIT_SITE_SETTINGS = "owcaneditsitesettings";

    // === Initialization and configuration update

    /** init the manager, set context
     * 
     * optionally set a prefix to distinguish several different applications.
     * The rolemanager will filter the allowed plugins, MIME settings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     * 
     * @param mainContext_p reference to the main app context of the application 
     * @param configNode_p OwXMLUtil node with configuration information
     * 
     * @exception Exception
     */
    public abstract void init(OwRoleManagerContext mainContext_p, OwXMLUtil configNode_p) throws Exception;

    /** init called AFTER the user has logged in.
     *
     *  NOTE: This function is called only once after login to do special initialization, 
     *        which can only be performed with valid credentials.
     */
    public abstract void loginInit() throws Exception;

    /** Refresh the static configuration data
     */
    public abstract void refreshStaticConfiguration() throws Exception;

    /** Check if update the configuration data is supported
     */
    public abstract boolean canRefreshStaticConfiguration() throws Exception;

    // === Methods to determine the current users access rights

    /**
     * checks if the current user designated by its credentials is allowed to use the given resource / function
     * 
     * @param iCategory_p of the requested function
     * @param strResourceID_p String ID of the requested resource (function / plugin id)
     *
     * @return true if user has permission, false if permission is denied
     */
    public abstract boolean isAllowed(int iCategory_p, String strResourceID_p) throws Exception;

    /**
     * checks if the current user designated by its credentials is allowed to use the
     * given resource and has the required right in its access mask for that resource.
     * 
     * @param iCategory_p of the requested function
     * @param strResourceID_p String ID of the requested resource (function / plugin id)
     * @param requiredAccessMask_p a 32 bit bitset with all flags set that have to be checked for the current user
     *
     * @return true if user has permission, false if permission is denied
     */
    public abstract boolean hasAccessMaskRight(int iCategory_p, String strResourceID_p, int requiredAccessMask_p) throws Exception;

    /**
     * get all resources the current user has access to for a given category
     * 
     * @param category_p int category for given user
     * 
     * @return Collection of String (resource IDs)
     */
    public abstract Collection getAllowedResources(int category_p) throws Exception;

    /** get the name of the design for the current user can be overridden by rolemanager implementation
     *  Specifies the subfolder under /designs/ where to retrieve the design files. i.e. CSS, images, layouts...
     *  This function can be used to make the look & feel dependent on the logged in user. 
     *
     *  @return name of design to use, default = "default"
     */
    public abstract String getDesign() throws Exception;

    /** get a list of plugin description OwXMLUtil nodes by type as defined in OwConfiguration,
     * that are allowed for the logged on user.
     *
     * @return list of OwXMLUtil plugin nodes for the given type or null if not found
     */
    public abstract List getPlugins(String strType_p) throws Exception;

    /** get a plugin description by its key
     * that are allowed for the logged on user.
     *
     * @return OwXMLUtil or null if not found
     */
    public abstract OwXMLUtil getPlugin(String strID_p) throws Exception;

    /** get the MIME XML Entry for the given MIMEType. Lookup in MimeMap
     * that are allowed for the logged on user.
     * <pre>
     * &lt;?xml version="1.0"?&gt;
     *  &lt;mimetable&gt;
     *   &lt;mime typ="file/txt"&gt;
     *    &lt;icon&gt;file_txt.png&lt;/icon&gt;
     *    &lt;viewerservlet&gt;getConent&lt;/viewerservlet&gt;
     *   &lt;/mime&gt;
     *   &lt;!--further MIME entries--&gt;
     *  &lt;/mimetable&gt;
     * </pre>
     * @param strMIMEType_p OwObject MIMEType
     *
     * @return org.w3c.dom.Node DOM Node of MIME entry from MIME table, or null if not found
     */
    public abstract OwXMLUtil getMIMENode(String strMIMEType_p) throws Exception;

    /** get the default MIME XML Entry for the given object type.
     * that are allowed for the logged on user.
     *
     * @param iObjectType_p Objecttype
     *
     * @return org.w3c.dom.Node DOM Node of MIME entry from MIME table, or null if not found
     */
    public abstract OwXMLUtil getDefaultMIMENode(int iObjectType_p) throws Exception;

    // === methods for admintools    

    /**
     * get all defined categories and displaynames as a map
     *
     * @return Collection of Integer keys (categories)
     */
    public abstract Collection getCategories();

    /**
     * Get configured categories and displaynames as a map.
     * If no categories are configured, all categories are returned.
     * @return - a {@link Collection} of {@link Integer} keys (categories).
     * @since 2.5.3.0
     */
    public abstract Collection getConfiguredCategories();

    /** get a display name for the given category 
     *
     * @param locale_p Locale to use
     * @param categorie_p Integer from getCategories() method
     */
    public abstract String getCategoryDisplayName(java.util.Locale locale_p, int categorie_p);

    /**
     * get all available resources for a given category
     *
     * @param category_p int category
     *
     * @return Collection of String keys (resource IDs)
     */
    public abstract Collection getResources(int category_p) throws Exception;

    /** get a display name for the given category 
     *
     * @param locale_p Locale to use
     * @param categorie_p Integer from getCategories() method
     * @param strID_p String resource ID 
     */
    public abstract String getResourceDisplayName(java.util.Locale locale_p, int categorie_p, String strID_p);

    /**
     * Retrieves the access rights for a given role to a given resource.
     * @param roleName_p the name of the role to retrieve the access rights for
     * @param category_p the category of the resource to retrieve the access rights for
     * @param resourceId_p the ID of the resource to retrieve the access rights for
     * @return one of the ROLE_ACCESS_RIGHT_ constants
     * @throws Exception
     * @see #ROLE_ACCESS_RIGHT_NOT_ALLOWED
     * @see #ROLE_ACCESS_RIGHT_ALLOWED
     * @see #ROLE_ACCESS_RIGHT_DENIED
     */
    public abstract int getAccessRights(String roleName_p, int category_p, String resourceId_p) throws Exception;

    /**
     * Persists the access rights for a given role to a given resource.
     * @param roleName_p name of the role to set the access rights for
     * @param category_p category of the resource to set the access rights for
     * @param resourceId_p ID of the resource to set the access rights for
     * @param accessRights_p the new access rights to persist as one of the ROLE_ACCESS_RIGHT_ constants
     * @throws Exception
     * @see #ROLE_ACCESS_RIGHT_NOT_ALLOWED
     * @see #ROLE_ACCESS_RIGHT_ALLOWED
     * @see #ROLE_ACCESS_RIGHT_DENIED
     */
    public abstract void setAccessRights(String roleName_p, int category_p, String resourceId_p, int accessRights_p) throws Exception;

    /**
     * Replaces the given dynamic old resource with a new one for its occurrences in 
     * access settings entries corresponding to the given role.  
     * 
     * @param roleName_p
     * @param category_p
     * @param oldResourceId_p
     * @param newResourceId_p
     * @throws Exception
     */
    public abstract void replaceResource(String roleName_p, int category_p, String oldResourceId_p, String newResourceId_p) throws Exception;

    /**
     * Get the access mask for this resource.
     * 
     * @param roleName_p the name of the role to retrieve the access mask for
     * @param category_p the category of the resource to retrieve the access mask for
     * @param resourceId_p the ID of the resource to retrieve the access mask for
     * 
     * @return the access mask for this resource as an ORed collection of access flags retrieved by getAccessMaskDescriptions
     */
    public abstract int getAccessMask(String roleName_p, int category_p, String resourceId_p);

    /**
     * Set the access mask for this resource.
     * 
     * @param roleName_p name of the role to set the access mask for
     * @param category_p category of the resource to set the access mask for
     * @param resourceId_p ID of the resource to set the access mask for
     * @param accessMask_p the new access mask
     */
    public abstract void setAccessMask(String roleName_p, int category_p, String resourceId_p, int accessMask_p) throws Exception;

    /** 
     * Check if rolemanager supports explicit deny of resources.
     * @return true = Explicit deny is supported false = Explicit deny is not supported
     */
    public abstract boolean canExplicitDeny();

    /** 
     * Check if the rolemanager can persist changes on the access mask. If not, the
     * getAccessMask() method will always return a full access mask 
     * @return true = Explicit deny is supported false = Explicit deny is not supported
     */
    public abstract boolean canPersistAccessMask();

    /** 
     * Check if the current user is allowed to modify global roles, which are not bound to a specific mandator.
     * @return true = current user is allowed to modify global roles false = current user is not allowed to modify global roles
     */
    public abstract boolean isGlobalRoleModificationAllowed();

    /**
     * Determine if the given role name is a global role or a mandator specific role.
     * @param roleName_p the name of the role to check
     * @return true = rolename_p is a global role false = rolename_p is a mandator specific role
     */
    public abstract boolean isGlobalRole(String roleName_p);

    /**
     * Returns a map between the access right flag and the localized display name. All checked access right
     * flags are ORed together and form the int value of the access mask.
     * 
     * @param category_p the category to retrieve the flag map for
     * 
     * @return Map between Integer and String mapping the access mask flags to their display names
     */
    public abstract Map getAccessMaskDescriptions(int category_p);

    // === Master role support.

    /** get a list of master roles that can be selected for the current user
     * 
     * @return Collection of String, or null if no roles can be selected
     * */
    public abstract Collection getMasterRoles() throws Exception;

    /** get a displayname for the given master role
     * 
     * @param locale_p Locale to use
     * @param strRole_p String
     * @return String displayname for given role
     * */
    public abstract String getMasterRoleDisplayName(java.util.Locale locale_p, String strRole_p) throws Exception;

    /** get the master role that is currently selected for the current user
     * 
     *  @return String role, or null if no roles can be selected
     * */
    public abstract String getMasterRole() throws Exception;

    /** set the role that is currently selected for the user
     * 
     * @param strRole_p String
     * @return boolean true = role changed
     * */
    public abstract boolean setMasterRole(String strRole_p) throws Exception;

    /** check if selectable master roles are supported by the rolemanager for the current user
     * 
     * @return boolean true = given user has roles
     * */
    public abstract boolean hasMasterRoles();

    /**
     * 
     * @param category_p
     * @return true if the given category is a static resource category (i.e. categories that have a 
     *         predefined resource set associated with them - new resources can not be added directly)
     * @since 4.0.0.0
     */
    public abstract boolean isStaticResourceCategory(int category_p);

}