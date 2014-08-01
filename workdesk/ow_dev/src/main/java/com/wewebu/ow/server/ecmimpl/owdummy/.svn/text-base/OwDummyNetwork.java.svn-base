package com.wewebu.ow.server.ecmimpl.owdummy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwAttributeBagsSupport;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwRepositoryContext;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwTransientBagsSupport;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.ecm.OwVirtualFolderObjectFactory;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.ecmimpl.owdummy.ui.OwDummyLoginUISubModul;
import com.wewebu.ow.server.ecmimpl.owdummy.ui.OwDummyUIUserSelectModul;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchSQLOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardWildCardDefinition;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.history.OwStandardHistoryObjectCreateEvent;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwMimeTypes;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwStreamUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base Class for the network access. Covers access to all objects in the DMS system.
 * Covers access to the authentication provider as well.<br/>
 * Custom implementation for Workdesk testing purpose only. Used as a dummy DMS System.
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
public class OwDummyNetwork implements OwNetwork<OwObject>
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDummyNetwork.class);

    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "owdm";

    /** event manager instance */
    private OwEventManager m_eventManager;

    /** prefix for DMSID to identify virtual folders     */
    public static final String VIRTUAL_FOLDER_PREFIX = "owvf";

    /** object category for the preferences, which can be user or application defined, like usersettings, recent file list... */
    public static final String OBJECT_CATEGORY_PREFERENCES = "/WEB-INF/appobj/reference/dummy/owpreferences";

    public static final String EL_OWAPPLICATIONOBJECTBASEDIR = "OwApplicationObjectBaseDir";
    /**  reference to the application context */
    private OwNetworkContext m_Context;

    private OwXMLUtil m_networkSettings;

    private OwRoleManager roleManager;

    /** reference to the default resource */
    private OwResource m_defaultResource = new OwDummyResource();

    /** preferences folder - can be configured in <code>owbootstrap.xml</code> file*/
    protected String m_preferencesFolder = OBJECT_CATEGORY_PREFERENCES;

    /** map containing the Object class descriptions of the DMS Adapter */
    protected static final HashMap m_ObjectClassesMap = new HashMap();
    static
    {
        // === add the class descriptions
        // file documents class
        m_ObjectClassesMap.put(OwDummyFileObject.m_FileClassDescription.getClassName(), OwDummyFileObject.m_FileClassDescription);
        m_ObjectClassesMap.put(OwDummyFileObject.m_DirectoryClassDescription.getClassName(), OwDummyFileObject.m_DirectoryClassDescription);
        m_ObjectClassesMap.put(OwDummyObjectLinkClass.getDefaultInstance().getClassName(), OwDummyObjectLinkClass.getDefaultInstance());
    }

    protected static final HashMap m_jndiPropertyNames = new HashMap();
    static
    {
        m_jndiPropertyNames.put("ProviderUrl", "java.naming.provider.url");
        m_jndiPropertyNames.put("factoryInitial", "java.naming.factory.initial");
        m_jndiPropertyNames.put("authenticationMode", "java.naming.security.authentication");
        m_jndiPropertyNames.put("ldapClientPrincipal", "java.naming.security.principal");
        m_jndiPropertyNames.put("ldapClientCredentials", "java.naming.security.credentials");
    }

    /** get the network configuration node
     */
    public OwXMLUtil getConfigNode()
    {
        return m_networkSettings;
    }

    /** get current locale */
    public java.util.Locale getLocale()
    {
        return getContext().getLocale();
    }

    /** return context reference
     */
    public OwNetworkContext getContext()
    {
        return m_Context;
    }

    /** create static HashMap of the object class descriptions */
    protected HashMap getObjectClassMap() throws Exception
    {
        return m_ObjectClassesMap;
    }

    // === custom dispatcher functions to extend the functionality for special usage like sending fax
    /** get an additional interface, e.g. interface to a workflow engine
     * @param strInterfaceName_p Name of the interface
     * @param oObject_p optional object to be wrapped
     * @return a reference to the interface
     */
    public Object getInterface(String strInterfaceName_p, Object oObject_p) throws Exception
    {
        if (strInterfaceName_p.equals("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository"))
        {
            return getBpmRepository();
        }
        String msg = "OwDummyNetwork.getInterface: Interfacename = " + strInterfaceName_p + ", expected = \"com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository\" ";
        LOG.error(msg);
        throw new OwObjectNotFoundException(msg);
    }

    private Object getBpmRepository()
    {
        return new OwDummyWorkitemRepository(this);
    }

    /** check if an additional interface is available, e.g. interface to a workflow engine
     * @param strInterfaceName_p Name of the interface
     * @return true, if interface is available
     */
    public boolean hasInterface(String strInterfaceName_p)
    {
        // we offer only the BPM Repository interface
        return (strInterfaceName_p.equals("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository"));
    }

    // === Initialization
    /** initialize the network adaptor
     * @param context_p Application Context
     * @param networkSettings_p Settings DOM Node wrapped by OwXMLUtil
     */
    public void init(OwNetworkContext context_p, OwXMLUtil networkSettings_p) throws Exception
    {
        m_retList = new ArrayList();
        m_Context = context_p;
        m_networkSettings = networkSettings_p;
        attributeBagSupportSetup();
        m_preferencesFolder = m_networkSettings.getSafeTextValue("UserDefinedPreferencesFolder", OBJECT_CATEGORY_PREFERENCES);
    }

    /** set the rolemanager to use
     *
     * @param roleManager_p OwRoleManager
     */
    public void setRoleManager(OwRoleManager roleManager_p)
    {
        this.roleManager = roleManager_p;
    }

    /** set the rolemanager to use
     *
     * @param eventManager_p OwHistoryManager to write history to, only if ECM system does not write its own history
     */
    public void setEventManager(OwEventManager eventManager_p)
    {
        m_eventManager = eventManager_p;
    }

    /** get the history manager instance */
    public OwEventManager getEventManager()
    {
        return m_eventManager;
    }

    /** force the network adapter to reload all the static class description data. */
    public void refreshStaticClassdescriptions() throws Exception
    {
        throw new OwNotSupportedException("OwDummyNetwork.refreshStaticClassdescriptions: Not implemented.");
    }

    /** check if reload of all the static class description data is supported / necessary.
     *
     * @return boolean true = refresh is supported and should be done, false = refresh is not supported and not necessary.
     */
    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return true;
    }

    // === login functions
    /** user credentials and logon session */
    private OwCredentials m_credentials;

    /** get the credentials of the logged in user
     *
     * @return the valid credentials of the logged in user, or null if not logged on
     */
    public OwCredentials getCredentials() throws Exception
    {
        return m_credentials;
    }

    /** get the user information form a User ID
     *
     *  NOTE: The length of the user ID MUST NOT EXCEED 32 Characters
     *
     * @param strID_p the ID of the searched user
     * @return the user information object of a user
     */
    public OwUserInfo getUserFromID(String strID_p) throws Exception
    {
        if (null == strID_p)
        {
            throw new OwObjectNotFoundException("OwDummyNetwork.getUserFromID: The Id of the searched User is null.");
        }

        return new OwDummyUserInfo(strID_p);
    }

    /** get the display name for a role name
     *
     * @param strRoleName_p to retrieve the display name for
     * @return the display name for the role
     */
    public String getRoleDisplayName(String strRoleName_p) throws Exception
    {
        return strRoleName_p;
    }

    /**
     * perform logon process authenticate user
     *
     * @param strUserName_p
     * @param strPassword_p
     */
    public void doLogin(String strUserName_p, String strPassword_p) throws Exception
    {

        Node authenticationNode = getConfigNode().getSubNode("Authentication");
        if (authenticationNode == null)
        {
            String msg = "OwDummyNetwork.doLogin: Please verify your owbootstrap.xml, how the authentication should happend (node Authentication).";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        String authenticationMode = authenticationNode.getAttributes().getNamedItem("mode").getNodeValue();
        if (authenticationMode != null && authenticationMode.equalsIgnoreCase("LDAP"))

        {
            //authenticate the user and load the UserInfo (inclusive the roles)
            m_credentials = new OwDummyCredentials(strUserName_p, strPassword_p, authenticationNode);

        }
        else
        {
            // if no login name was specified, then take guest account
            if (strUserName_p == null || strUserName_p.equals(""))
            {
                strUserName_p = "guest";
            }
            // set the user long name
            //String userLongName = m_dao.getUserName(userId);
            // credentials.getUserInfo().setStrLongUserName(userLongName);

            // sets roles "Guest"...
            m_credentials = new OwDummyCredentials(strUserName_p);
        }

        // login callback
        m_Context.onLogin(m_credentials.getUserInfo());

        // signal event for history
        getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_ECM, OwEventManager.HISTORY_EVENT_ID_LOGIN, OwEventManager.HISTORY_STATUS_OK);
    }

    /** get an instance of the login UI submodule for user authentication
     * Login is very specific to the DMS System and can not be handled generically
     * @return OwUILoginModul OwView derived module
     */
    public OwUILoginModul getLoginSubModul() throws Exception
    {
        OwUILoginModul LoginSubModul = new OwDummyLoginUISubModul();
        LoginSubModul.init(this);

        return LoginSubModul;
    }

    /** log off and reset credentials */
    public void logout() throws Exception
    {
        // signal event for history
        getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_ECM, OwEventManager.HISTORY_EVENT_ID_LOGOFF, OwEventManager.HISTORY_STATUS_OK);

        m_credentials = null;
    }

    /** log on to the network with default configuration
     *
     *  NOTE:   The behavior of the function depends on the configuration of the ECM adapter.
     *          For a determined login use the OwUILoginModul Screen (getLoginSubModul())
     *
     * @param strUser_p String user name or name of function user or null to use a default login if available
     * @param strPassword_p String the password for the user or null if not required
     */
    public void loginDefault(String strUser_p, String strPassword_p) throws Exception
    {
        doLogin(strUser_p, strPassword_p);
    }

    // === permission functions
    /** get an instance of the edit access rights UI submodule for editing document access rights
     * Access rights are very specific to the DMS System and can not be handled generically
     * @param object_p OwObject to edit the access rights
     *
     * @return OwUIAccessRightsModul OwView derived module
     */
    public OwUIAccessRightsModul getEditAccessRightsSubModul(OwObject object_p) throws Exception
    {
        // dummy adaptor does not allow access rights to be edited.
        return null;
    }

    /** check if access rights can be edited on the Object. I.e. if a AccessRightsSubModul can be obtained
     * @param object_p OwObject to edit access rights for
     * @return true if access rights can be edited
     */
    public boolean canEditAccessRights(OwObject object_p) throws Exception
    {
        // dummy adaptor does not allow access rights to be edited.
        return false;
    }

    /** get an instance of the user select UI submodule for selecting a user or group
     * User selection is very specific to the DMS System and can not be handled generically
     *
     * @param strID_p the ID of the currently set user or null if no user is selected
     * @param types_p array of type identifiers as defined in OwUIUserSelectModul
     *
     * @return OwUIUserSelectModul OwView derived module
     */
    public OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws Exception
    {
        OwDummyUIUserSelectModul userDlg = new OwDummyUIUserSelectModul(strID_p, types_p);
        userDlg.init(this);

        return userDlg;
    }

    /** check if the user select module is supported i.e. getUserSelectSubModul is implemented
     *
     * @return true if access rights can be edited
     */
    public boolean canUserSelect() throws Exception
    {
        return true;
    }

    // === access to the site and user documents e.g. workflows, searchtemplates, preference settings
    /** get a list of Objects for the application to work, like search templates, preferences...
     * @param iTyp_p type as defined in OwNetwork.APPLICATION_OBJECT_TYPE_...
     * @param strName_p Name of the object to retrieve e.g. "userprefs"
     * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     *
     * @return Collection, which elements need to be cast to the appropriate type according to iTyp_p
     */
    public java.util.Collection getApplicationObjects(int iTyp_p, String strName_p, boolean fForceUserSpecificObject_p) throws Exception
    {
        switch (iTyp_p)
        {
            case APPLICATION_OBJECT_TYPE_XML_DOCUMENT:
                return getObjects(strName_p, fForceUserSpecificObject_p, -1);

            case OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE:
            {
                OwObjectCollection objects = getObjects(strName_p, fForceUserSpecificObject_p, -1);
                m_retList.clear();

                Iterator it = objects.iterator();

                while (it.hasNext())
                {
                    OwSearchTemplate searchTemplate = createSearchTemplate((OwObject) it.next());
                    if (!m_retList.contains(searchTemplate))
                    {
                        m_retList.add(searchTemplate);
                    }
                }
                return m_retList;
            }

            case OwNetwork.APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER:
                return getObjects(strName_p == null ? "other" : strName_p, false, -1);

            default:
                String msg = "OwDummyNetwork.getApplicationObjects: Unsupported ApplicationObject, Name = " + strName_p + ", Type = " + iTyp_p;
                LOG.error(msg);
                throw new OwNotSupportedException(msg);
        }
    }

    private OwStandardObjectCollection getObjects(String strCategory_p, boolean fForceUserSpecificObject_p, int iMaxSize_p) throws Exception
    {
        OwStandardObjectCollection Objects = new OwStandardObjectCollection();

        // === look in the folder .../WEB-INF/site/strCategory/... for files
        String strFilePath = m_Context.getBasePath() + getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/") + strCategory_p;
        if (fForceUserSpecificObject_p)
        {
            // === look in the folder <basedir>/strCategory/<userid>/... for files
            strFilePath += "/" + getCredentials().getUserInfo().getUserID();
            strFilePath += "_";
        }

        File SiteFolder = new File(strFilePath);

        File[] siteFiles = SiteFolder.listFiles();

        if (siteFiles != null && siteFiles.length > 0)
        {
            int len = siteFiles.length;

            if ((iMaxSize_p != -1) && (len > iMaxSize_p))
            {
                len = iMaxSize_p;
                Objects.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.FALSE);
            }

            // === iterate over the files in the category folder
            for (int i = 0; i < len; i++)
            {
                // create new object on file
                // add to list
                File file = siteFiles[i];
                if (!file.isHidden())
                {
                    // create new object on file, add to list
                    OwObject object = OwDummyObjectFactory.getInstance().create(this, file);//new OwDummyFileObject(this, file)
                    Objects.add(object);
                }
            }
        }

        Objects.setAttribute(OwObjectCollection.ATTRIBUTE_SIZE, Integer.valueOf(Objects.size()));

        return Objects;
    }

    /** create a virtual folder object for the given name
     * @param strName_p String name identifying the virtual folder structure
     * @return OwObject
     */
    private OwObject createVirtualFolder(String strName_p, String strDmsIDPart_p) throws Exception
    {
        // === need to create DOM description node for virtual folder
        InputStream in = new FileInputStream(new File(m_Context.getBasePath() + getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/") + strName_p + ".xml"));

        org.w3c.dom.Document doc = OwXMLDOMUtil.getDocumentFromInputStream(in);

        return createVirtualFolder(doc.getFirstChild(), strName_p, strDmsIDPart_p);
    }

    /** (overridable) create a virtual folder object from the given XML description
     *
     * @param xmlVirtualFolderDescriptionNode_p XML {@link Node} describing the virtual folder
     * @param strName_p String name identifying the virtual folder structure
     *
     * @return OwObject
     */
    protected OwObject createVirtualFolder(Node xmlVirtualFolderDescriptionNode_p, String strName_p, String strDmsIDPart_p) throws Exception
    {
        // read the classname to instantiate from XML, default is OwStandardVirtualFolderObject
        OwXMLUtil description = new OwStandardXMLUtil(xmlVirtualFolderDescriptionNode_p);
        String strVirtualFolderClassName = description.getSafeTextValue(OwVirtualFolderObjectFactory.CLASSNAME_TAG_NAME, "com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory");
        Class virtualFolderClass = Class.forName(strVirtualFolderClassName);

        OwVirtualFolderObjectFactory retObject = (OwVirtualFolderObjectFactory) virtualFolderClass.newInstance();

        Node rootNode = description.getSubNode(OwVirtualFolderObjectFactory.ROOT_NODE_TAG_NAME);
        retObject.init(getContext(), this, new StringBuilder(DMS_PREFIX).append(",").append(VIRTUAL_FOLDER_PREFIX).append(",").append(strName_p).toString(), strName_p, rootNode);

        return retObject.getInstance(strDmsIDPart_p);
    }

    /** application context key, where the attribute bags are stored */
    private static final String BAGS_APPLICATION_KEY = "OwDummyNetworkAttributeBags";

    /** get the map containing the attribute bags
     *
     * @return Map
     */
    private Map getAttributeBagMap()
    {
        Map ret = (Map) getContext().getApplicationAttribute(BAGS_APPLICATION_KEY);

        if (null == ret)
        {
            ret = new HashMap();
            getContext().setApplicationAttribute(BAGS_APPLICATION_KEY, ret);
        }

        return ret;
    }

    /** get a Objects for the application to work, like search templates, preferences...
    *
    * @param iTyp_p type as defined in OwNetwork.APPLICATION_OBJECT_TYPE_...
    * @param strName_p Name of the object to retrieve e.g. "userprefs"
    * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
    * @param fCreateIfNotExist_p boolean true = create if not exist
    *
    * @return Object, which needs to be cast to the appropriate type according to iTyp_p
    */
    public Object getApplicationObject(int iTyp_p, String strName_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws Exception
    {
        return getApplicationObject(iTyp_p, strName_p, null, fForceUserSpecificObject_p, fCreateIfNotExist_p);
    }

    /** get a Objects for the application to work, like search templates, preferences...
     *
     * @param iTyp_p type as defined in OwNetwork.APPLICATION_OBJECT_TYPE_...
     * @param strName_p Name of the object to retrieve e.g. "userprefs"
     * @param param_p optional Object, can be null
     * @param fForceUserSpecificObject_p if true, the object must be specific to the logged in user, otherwise the ECM Adapter determines if it is common to a site or specific to a group or a user.
     * @param fCreateIfNotExist_p boolean true = create if not exist
     *
     * @return Object, which needs to be cast to the appropriate type according to iTyp_p
     */
    public Object getApplicationObject(int iTyp_p, String strName_p, Object param_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws Exception
    {
        switch (iTyp_p)
        {
            case APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE:
            {
                if (!fForceUserSpecificObject_p)
                {
                    String msg = "OwFNCMNetwork.getApplicationObject: Attribute Bags are always user specific, set fForceUserSpecificObject_p = true";
                    LOG.debug(msg);
                    throw new OwObjectNotFoundException(msg);
                }

                return m_bagsSupport.getUserKeyAttributeBagWriteable(getContext(), strName_p, (String) param_p);
            }

            case OwNetwork.APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER:
                return createVirtualFolder(strName_p, null);

            case OwNetwork.APPLICATION_OBJECT_TYPE_PREFERENCES:
                return getFileObject(m_Context.getBasePath() + m_preferencesFolder + "/", strName_p, fForceUserSpecificObject_p, "text/xml", fCreateIfNotExist_p);

            case OwNetwork.APPLICATION_OBJECT_TYPE_XML_DOCUMENT:
            {
                OwObject xmlObject = getFileObject(m_Context.getBasePath() + getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/"), strName_p, fForceUserSpecificObject_p, "text/xml", fCreateIfNotExist_p);
                InputStream in = xmlObject.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null);

                return OwXMLDOMUtil.getDocumentFromInputStream(in);
            }

            case OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE:
            {
                OwObject owObject = getFileObject(m_Context.getBasePath() + getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/"), strName_p, fForceUserSpecificObject_p, "text/xml", fCreateIfNotExist_p);
                OwStandardSearchTemplate searchTemplate = new OwStandardSearchTemplate(getContext(), owObject);

                if (!m_retList.contains(searchTemplate))
                {
                    m_retList.add(searchTemplate);
                }

                return searchTemplate;

            }

            default:
                String msg = "OwDummyNetwork.getApplicationObject: ApplicationObject = " + strName_p + ", Type = " + iTyp_p;
                LOG.error(msg);
                throw new OwObjectNotFoundException(msg);
        }
    }

    /**
     * 
     * @param defaultLocation_p
     * @return the configured OwApplicationObjectBaseDir value or the given default value 
     *         if no OwApplicationObjectBaseDir configuration is found 
     * @since 4.2.0.0 

     */
    public String getApplicationObjectBaseDir(String defaultLocation_p)
    {
        return m_networkSettings.getSafeTextValue(EL_OWAPPLICATIONOBJECTBASEDIR, defaultLocation_p);
    }

    private OwObject getFileObject(String strBasePath_p, String strName_p, boolean fForceUserSpecificObject_p, String strMimeType_p, boolean fCreateIfNotExist_p) throws Exception
    {
        if (fForceUserSpecificObject_p)
        {
            strBasePath_p += getCredentials().getUserInfo().getUserID();
            strBasePath_p += "_";
        }

        strBasePath_p += strName_p;

        String strExt = OwMimeTypes.getExtensionFromMime(strMimeType_p);
        if (strExt != null)
        {
            strBasePath_p += "." + strExt;
        }

        File folder = new File(strBasePath_p);

        OwObject fileObject = OwDummyObjectFactory.getInstance().create(this, folder);// new OwDummyFileObject(this, folder);
        if ((!fileObject.canGetContent(OwContentCollection.CONTENT_TYPE_DOCUMENT, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)) && (!fCreateIfNotExist_p))
        {
            // === doesn't exist and we must not create it
            throw new OwObjectNotFoundException("OwDummyNetwork.getFileObject: Cannot get content, strBasePath_p = " + strBasePath_p);
        }
        else
        {
            return fileObject;
        }
    }

    // === Object construction
    /** it is wise to cache the last acquired object, because mostly it is needed again, e.g. by the viewers. */
    private OwObject m_cachedLastObject;

    private OwObject parseObjectFromDMSID(StringTokenizer tokens, boolean fRefresh) throws Exception
    {
        // skip prefix
        tokens.nextToken();

        String strTypePrefix = tokens.nextToken();

        if (strTypePrefix.equals(VIRTUAL_FOLDER_PREFIX))
        {
            String strName = tokens.nextToken();
            String strDmsIDPart = tokens.nextToken();

            m_cachedLastObject = createVirtualFolder(strName, strDmsIDPart);
        }
        else if (strTypePrefix.equals(OwDummyObjectLink.LINKS_PREFIX))
        {
            String linkClass = tokens.nextToken();

            OwObject source = parseObjectFromDMSID(tokens, fRefresh);
            OwObject target = parseObjectFromDMSID(tokens, fRefresh);

            OwObjectCollection links = source.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_LINK }, null, null, -1, 0, null);
            Iterator linksIt = links.iterator();
            OwObjectLink theLink = null;
            while (linksIt.hasNext())
            {
                OwObjectLink link = (OwObjectLink) linksIt.next();
                if (link.getSource().getDMSID().equals(source.getDMSID()))
                {
                    if (link.getTarget().getDMSID().equals(target.getDMSID()))
                    {
                        if (link.getObjectClass().getClassName().equals(linkClass))
                        {
                            theLink = link;
                            break;
                        }
                    }
                }
            }

            if (theLink == null)
            {
                throw new OwObjectNotFoundException("OwDummyNetwork.parseObjectFromDMSID: invalid link " + source.getDMSID() + " -> " + target.getDMSID() + " of type " + linkClass);
            }

            m_cachedLastObject = theLink;
        }
        else if (strTypePrefix.equals(OwDummyFileObject.DMS_PREFIX))
        {
            // === file object
            String strFilePath = tokens.nextToken();

            // check if it is a relative or absolute path
            if (strFilePath.startsWith("/"))
            {
                // relative path, add absolute path
                strFilePath = getArchiveBaseDir() + strFilePath;
            }

            // create new object on file
            m_cachedLastObject = OwDummyObjectFactory.getInstance().create(this, new File(strFilePath));//new OwDummyFileObject(this, new File(strFilePath));

            if (!((OwFileObject) m_cachedLastObject).exists())
            {
                m_cachedLastObject = null;
                throw new OwObjectNotFoundException("OwDummyNetwork.parseObjectFromDMSID: PATH does not exist  " + strFilePath);
            }
        }
        else
        {
            // unknown object type
            throw new OwObjectNotFoundException("OwDummyNetwork.parseObjectFromDMSID: Unknown object type, @ " + strTypePrefix);
        }

        return m_cachedLastObject;
    }

    /** reconstructs an Object from DMS Id, see OwObject.getDMSID for details.
     * @param strDMSID_p DMSID for the requested object
     * @param fRefresh_p true = force refresh of object from DMS System, false = may use cached object
     * @returns an Object Instance
     */
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        // === check if object is still in cache
        if ((!fRefresh_p) && (null != m_cachedLastObject) && (m_cachedLastObject.getDMSID().equals(strDMSID_p)))
        {
            return m_cachedLastObject;
        }

        //////////////////////////////////////////////
        // NOTE:    You must not use this code without security check.
        //          Otherwise, everybody can access File Objects from the server.
        //          That would cause a big security problem.

        if ((!strDMSID_p.startsWith(DMS_PREFIX)) && (!strDMSID_p.startsWith(VIRTUAL_FOLDER_PREFIX)))
        {
            String msg = "OwDummyNetwork.getObjectFromDMSID: Wrong DMSID DMS_PREFIX or VIRTUAL_FOLDER_PREFIX, DMSID = " + strDMSID_p;
            LOG.error(msg);
            throw new OwObjectNotFoundException(msg);
        }

        StringTokenizer tokens = new StringTokenizer(strDMSID_p, ",");

        try
        {
            return parseObjectFromDMSID(tokens, fRefresh_p);
        }
        catch (Exception e)
        {
            LOG.error("Could not retrieve objec tfor DMSID " + strDMSID_p);
            throw e;
        }
    }

    /** get the base directory of the dummy archive
     *
     * @return String base dir of the archive without ending backslash
     */
    public String getArchiveBaseDir()
    {
        return OwString.replaceAll(m_Context.getBasePath() + "/WEB-INF", "\\", "/");
    }

    /** performs a search on the network and returns a result list, if more than iMaxSize_p objects are found then doSearch returns the first iMaxSize_p Objects
     * @param searchNode_p list of search criteria
     * @param sortCriteria_p optional list of sort criteria
     * @param propertyNames_p a optional list of properties to retrieve with the documents, can be null
     * @param iMaxSize_p int value to specify the maximum size of the OwObjectCollection
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     * @return list of found objects
     */
    public OwObjectCollection doSearch(OwSearchNode searchNode_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        // signal event for history
        getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_ECM, OwEventManager.HISTORY_EVENT_ID_SEARCH, OwEventManager.HISTORY_STATUS_OK);

        // simulate search with different systems

        StringWriter sql = new StringWriter();

        OwSearchSQLOperator op1 = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_MS_ACCESS);
        sql.write("\r\n DATE_MODE_MS_ACCESS");
        op1.createSQLSearchCriteria(searchNode_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY), sql);

        OwSearchSQLOperator op2 = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_FNCM);
        sql.write("\r\n DATE_MODE_FNCM");
        op2.createSQLSearchCriteria(searchNode_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY), sql);

        OwSearchSQLOperator op3 = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_FNIM);
        sql.write("\r\n DATE_MODE_FNIM");
        op3.createSQLSearchCriteria(searchNode_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY), sql);

        OwSearchSQLOperator op4 = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_DEFAULT);
        sql.write("\r\n DATE_MODE_DEFAULT");
        op4.createSQLSearchCriteria(searchNode_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY), sql);

        OwSearchSQLOperator op5 = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_MSSQL);
        sql.write("\r\n DATE_MODE_MSSQL");
        op5.createSQLSearchCriteria(searchNode_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY), sql);

        OwSearchSQLOperator op6 = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_ORACLE);
        sql.write("\r\n DATE_MODE_ORACLE");
        op6.createSQLSearchCriteria(searchNode_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY), sql);

        // as a test we just return some dummy objects
        OwStandardObjectCollection RetList = getObjects("dummyarchiv", false, iMaxSize_p);

        // set attributes in the list used for info
        RetList.setAttribute(OwObjectCollection.ATTRIBUTE_SQL, sql.toString());

        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwDummyNetwork.doSearch: SQL = " + sql.toString());
        }

        if (null != sortCriteria_p)
        {
            RetList.sort(sortCriteria_p);
        }

        return RetList;
    }

    /** creates a new object on the ECM System using the given parameters
     *
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     *
     * @return String the DMSID of the new created object
     */
    public String createNewObject(OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p) throws Exception
    {
        if (!properties_p.containsKey(OwFileObject.OwFileObjectClass.NAME_PROPERTY))
        {
            throw new OwInvalidOperationException("OwDummyNetwork.createNewObject: Missing name property.");
        }

        String strNewName = (String) ((OwProperty) properties_p.get(OwFileObject.OwFileObjectClass.NAME_PROPERTY)).getValue();
        if ((null == strNewName) || (0 == strNewName.length()))
        {
            throw new OwInvalidOperationException("Name property empty.");
        }

        // === get resource
        OwResource resource = null;
        if (parent_p != null)
        {
            resource = parent_p.getResource();
        }

        // === get object class
        OwObjectClass objClass = getObjectClass(strObjectClassName_p, resource);

        // === mind virtual folders
        if (parent_p instanceof OwVirtualFolderObject)
        {
            // set the required properties
            ((OwVirtualFolderObject) parent_p).setFiledObjectProperties(objClass, properties_p);

            // done with virtual folder handling
            parent_p = null;
        }

        // === create new object
        String strFileName = strNewName;
        if ((strMimeType_p != null) && (strMimeType_p.length() > 0))
        {
            strFileName += "." + OwMimeTypes.getExtensionFromMime(strMimeType_p);
        }

        java.io.File newFile = null;
        if (parent_p != null)
        {
            newFile = new File(((OwFileObject) parent_p).getFileObject(), strFileName);
        }
        else
        {
            newFile = new File(m_Context.getBasePath() + getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/") + strFileName);
        }

        if (newFile.exists())
        {
            // signal event for history
            getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_NEW_OBJECT, OwEventManager.HISTORY_STATUS_FAILED);

            throw new OwInvalidOperationException(getContext().localize("owdummy.OwDummyNetwork.existsalready", "There already exists a file with this name:") + strNewName);
        }

        if (objClass.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
        {
            // === create directory
            newFile.mkdir();
        }
        else
        {
            // === create file
            if (content_p != null)
            {
                // check if we have at least one content element
                if (content_p.getPageCount() >= 1)
                {
                    InputStream in = content_p.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null);
                    FileOutputStream out = new FileOutputStream(newFile);

                    /// === copy content
                    OwStreamUtil.upload(in, out, true);
                }
                else
                {
                    // no content elements. create file without content
                    newFile.createNewFile();
                }
            }
            else
            {
                // === File without content
                newFile.createNewFile();
            }
        }

        String dmsid = OwDummyFileObject.getDMSID(getArchiveBaseDir(), newFile);

        // signal event for history
        getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_NEW_OBJECT, new OwStandardHistoryObjectCreateEvent(strObjectClassName_p, dmsid, properties_p), OwEventManager.HISTORY_STATUS_OK);

        // return DMSID
        return dmsid;
    }

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
    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p) throws Exception
    {
        return createNewObject(resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p);
    }

    /**
     * Creates a new object on the ECM System using the given parameters.<br>
     * Has additional promote and checkin mode parameters for versionable objects
     * and the extra parameter fKeepCheckedOut_p to control whether the new
     * objects are checked in automatically or not.
     *
     * @param fPromote_p boolean true = create a released version right away
     * @param mode_p Object checkin mode for objects, see getCheckinModes, or null to use default
     * @param resource_p OwResource to add to
     * @param strObjectClassName_p requested class name of the new object
     * @param properties_p OwPropertyCollection with new properties to set, or null to use defaults
     * @param permissions_p OwPermissionCollection ECM specific permissions or null to use defaults
     * @param content_p OwContentCollection the new content to set, null to create an empty object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param strMimeType_p String MIME type of the new object content
     * @param strMimeParameter_p extra info to the MIME type
     * @param fKeepCheckedOut_p true = create a new object that is checked out
     *
     * @return String the ECM ID of the new created object
     *
     * @since 2.5
     */
    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) throws Exception
    {
        if (fKeepCheckedOut_p)
        {
            // not supported for now
            throw new OwNotSupportedException("The DCTM adapter can not yet create new objects and keep them checked out.");
        }
        else
        {
            // delegate to existing method
            return createNewObject(fPromote_p, mode_p, resource_p, strObjectClassName_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p);
        }
    }

    /** check, if adaptor can create a new object
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     *
     * @return true, if object can be created
     */
    public boolean canCreateNewObject(OwResource resource_p, OwObject parent_p, int iContext_p) throws Exception
    {
        // can create new objects
        return true;
    }

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
    public String createObjectCopy(OwObject obj_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwObject parent_p, int[] childTypes_p) throws Exception
    {
        ///////////////////////////
        // TODO;

        // signal event for history
        getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_COPY_OBJECT, OwEventManager.HISTORY_STATUS_OK);

        return null;
    }

    /** creates a cloned object with new properties on the DMS system
     * @param parent_p OwObject the parent object to use as a container, e.g. a folder or a ECM root, can be null if no parent is required
     * @param childTypes_p int types of the child objects to copy with the object, can be null if no children should be copied
     *
     * @return true, if clone can be created
     */
    public boolean canCreateObjectCopy(OwObject parent_p, int[] childTypes_p, int iContext_p) throws Exception
    {
        ///////////////////////////
        // TODO;

        return false;
    }

    /** get a Object class description of the available object class descriptions
     *
     * @param strClassName_p Name of class
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return OwObjectClass instance
     */
    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        // get from classes map
        OwObjectClass objectClassDescription = (OwObjectClass) getObjectClassMap().get(strClassName_p);
        if (null == objectClassDescription)
        {
            String msg = "OwDummyNetwork.getObjectClass: Object class description not found, classname = " + strClassName_p;
            LOG.error(msg);
            throw new OwObjectNotFoundException(msg);
        }
        return objectClassDescription;
    }

    /** get a list of the available object class descriptions names
     *
     * @param iTypes_p int Object type as defined in OwObject
     * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
     * @param fRootOnly_p true = gets only the root classes if we deal with a class tree, false = gets all classes
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return string array of OwObjectClass Names
     */
    public java.util.Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        Map retList = new HashMap();

        boolean returnObjectTypeDocument = false;
        boolean returnObjectTypeFolder = false;

        if (iTypes_p == null)
        {
            returnObjectTypeDocument = true;
            returnObjectTypeFolder = true;
        }
        else
        {
            for (int i = 0; i < iTypes_p.length; i++)
            {
                switch (iTypes_p[i])
                {
                    case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                        returnObjectTypeDocument = true;
                        break;

                    case OwObjectReference.OBJECT_TYPE_FOLDER:
                        returnObjectTypeFolder = true;
                        break;
                }
            }
        }

        if (returnObjectTypeDocument)
        {
            retList.put(OwDummyFileObject.m_FileClassDescription.getClassName(), OwDummyFileObject.m_FileClassDescription.getDisplayName(getLocale()));
        }

        if (returnObjectTypeFolder)
        {
            retList.put(OwDummyFileObject.m_DirectoryClassDescription.getClassName(), OwDummyFileObject.m_DirectoryClassDescription.getDisplayName(getLocale()));
        }

        return retList;
    }

    /** get a property class description of the available object class descriptions
     *
     * <b>NOTE:</b> This function gets the next available property class for the given name.
     *          The resulting property class is undetermined,
     *          if there are more possible property descriptions for different object classes,
     *
     *          ==> Use this function with care. If possible retrieve property classes from object classes.
     *
     * @param strClassName_p Name of class
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return OwObjectClass instance
     */
    public OwPropertyClass getUnsafePropertyClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        // === get from classes map

        //////////////////////////////////////////
        // TODO: Optimize by caching the property classes

        // iterate over all class descriptions
        HashMap ObjClassMap = getObjectClassMap();

        Iterator it = ObjClassMap.values().iterator();
        while (it.hasNext())
        {
            OwObjectClass ObjClass = (OwObjectClass) it.next();

            try
            {
                // try to find the property class in this objectclass
                OwPropertyClass PropClass = ObjClass.getPropertyClass(strClassName_p);
                if (PropClass != null)
                {
                    return PropClass;
                }
            }
            catch (OwObjectNotFoundException e)
            {
                LOG.debug("\t" + ObjClass.getClassName() + " - " + strClassName_p);
            }
        }

        String msg = "OwDummyNetwork.getUnsafePropertyClass: Class description not found, ClassName = " + strClassName_p;
        LOG.debug(msg);
        throw new OwObjectNotFoundException(msg);
    }

    /** creates a OwSearchTemplate out of a OwObject. Used for the searches in the search plugin.
     *
     *  NOTE:   The syntax of the XML Template is compatible with FileNet P8 Search designer.
     *          I.e. this function can read FileNet P8 SearchDesigner created templates.
     *
     * @param obj_p OwObject that contains the search template information
     * @return OwSearchTemplate with retrieved search information
     */
    private OwSearchTemplate createSearchTemplate(OwObject obj_p) throws Exception
    {
        OwStandardSearchTemplate SearchTemplate = new OwStandardSearchTemplate(getContext(), obj_p);

        return SearchTemplate;
    }

    /** get the resource with the specified key
     *
     * @param strID_p String resource ID, if strID_p is null, returns the default resource
     */
    public OwResource getResource(String strID_p) throws Exception
    {
        // only one default resource
        return m_defaultResource;
    }

    /** get a Iterator of available resource IDs
     *
     * @return Iterator of resource IDs used in getResource, or null if no resources are available
     */
    public java.util.Iterator getResourceIDs() throws Exception
    {
        Collection collection = new LinkedList();
        collection.add(m_defaultResource.getID());
        return collection.iterator();
    }

    /** get object from given path
     *
     * @param strPath_p path to the object starting with "/..."
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @return OwObject
     */
    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        // === the ECM root object is requested
        return OwDummyObjectFactory.getInstance().create(this, new File(m_Context.getBasePath() + getApplicationObjectBaseDir("/WEB-INF/appobj/reference/dummy/") + "/dummyarchiv/" + strPath_p));
    }

    /** check if a extended function like print can be performed on the given object
     *  compared to the OwRoleManager.isAllowed function the canDo function works on objects and is more faster due to the iContext parameter
     *
     * @param obj_p OwObjet where function should be performed, or null if function does not require a object
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @param iFunctionCode_p int code of requested function as defined in OwExtendedFunctions
     */
    public boolean canDo(OwObject obj_p, int iFunctionCode_p, int iContext_p) throws Exception
    {
        return true;
    }

    /** implementation of OwFieldDefinitionProvider: get a field definition for the given name and resource
     *
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null
     *
     * @return OwFieldDefinition or throws OwObjectNotFoundException
     */
    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        return getUnsafePropertyClass(strFieldDefinitionName_p, getResource(strResourceName_p));
    }

    /** get a prefix which is used to distinguish the DMSID of objects from the repository */
    public String getDMSPrefix()
    {
        return DMS_PREFIX;
    }

    /** releases all resources that have been used during this session
     */
    public void releaseResources() throws Exception
    {
        // no resources to release
    }

    /**
     *<p>
     * Like wild card definitions flyweight.
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
    private static class OwLikeWildCardDefinitions
    {
        private Vector m_definitions;

        public OwLikeWildCardDefinitions(OwRepositoryContext context_p)
        {
            m_definitions = new Vector();

            m_definitions.add(new OwStandardWildCardDefinition("#", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1(
                    "ecmimpl.OwDummyNetwork.WILD_CARD_TYPE_MULTI_CHAR", "(%1) replaces any characters")));
            m_definitions.add(new OwStandardWildCardDefinition("~", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR, new OwString1(
                    "ecmimpl.OwDummyNetwork.WILD_CARD_TYPE_SINGLE_CHAR", "(%1) replaces any character.")));
        }

        public Collection getDefinitions()
        {
            return m_definitions;
        }
    }

    /** like wild card definitions flyweight */
    private OwLikeWildCardDefinitions m_likewildcarddefinitions;

    private List m_retList;

    /** attribute bag support **/
    protected OwAttributeBagsSupport m_bagsSupport = null;

    /**
     * Return Attributebag support to be used.
     * @since 3.1.0.0
     */
    protected void attributeBagSupportSetup()
    {

        m_bagsSupport = new OwTransientBagsSupport();

    }

    /** get a collection of wild card definitions that are allowed for the given field, resource and search operator
     *
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null
     * @param iOp_p search operator as defined in OwSearchOperator CRIT_OP_...
     *
     * @return Collection of OwWildCardDefinition, or null if no wildcards are defined
     * @throws Exception
     */
    public java.util.Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception
    {
        switch (iOp_p)
        {
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
            case OwSearchOperator.CRIT_OP_LIKE:
            {
                if (m_likewildcarddefinitions == null)
                {
                    m_likewildcarddefinitions = new OwLikeWildCardDefinitions(getContext());
                }

                return m_likewildcarddefinitions.getDefinitions();
            }

            default:
                return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#canBatch()
     */
    public boolean canBatch()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#closeBatch(com.wewebu.ow.server.ecm.OwBatch)
     */
    public void closeBatch(OwBatch batch_p) throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#openBatch()
     */
    public OwBatch openBatch() throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwNetwork#createObjectSkeleton(com.wewebu.ow.server.ecm.OwObjectClass, com.wewebu.ow.server.ecm.OwResource)
     */
    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwResource resource_p) throws Exception
    {
        return new OwObjectSkeleton(this, objectclass_p);
    }

    @Override
    public OwRoleManager getRoleManager()
    {
        return this.roleManager;
    }

    @Override
    public OwIterable<OwObject> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO : Dummy page search
        throw new OwNotSupportedException("The Dummy adapter does not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }
}