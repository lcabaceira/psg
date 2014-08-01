package com.wewebu.ow.server.app;

import java.io.File;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.ao.OwAOContext;
import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.ao.OwAOProviderFactory;
import com.wewebu.ow.server.app.OwConfiguration.OwMasterPluginInstance;
import com.wewebu.ow.server.app.OwUserOperationEvent.OwUserOperationType;
import com.wewebu.ow.server.app.id.viid.OwVIIdFactory;
import com.wewebu.ow.server.app.impl.viid.OwSimpleVIIdFactory;
import com.wewebu.ow.server.app.viewer.OwSimpleInfoProviderRegistry;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwRepositoryContext;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwSessionException;
import com.wewebu.ow.server.exceptions.OwUserOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.history.OwHistoryManagerContext;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.mandator.OwMandatorManagerContext;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwDialogManager;
import com.wewebu.ow.server.ui.OwKeyAction;
import com.wewebu.ow.server.ui.OwKeySetting;
import com.wewebu.ow.server.ui.viewer.OwInfoProviderRegistry;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwDateTimeUtil;
import com.wewebu.ow.server.util.OwLazyLoadingProxyHandler;
import com.wewebu.ow.server.util.OwSimpleAttributeBagWriteable;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.service.rendition.OwRenditionServiceProvider;
import com.wewebu.service.rendition.impl.OwSimpleRenditionServiceProvider;

/**
 *<p>
 * Main Application Context Class Implementation. Instance stays active during session.
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
@SuppressWarnings("rawtypes")
public class OwMainAppContext extends OwAppContext implements OwHistoryManagerContext, OwNetworkContext, OwMandatorManagerContext, OwComboboxRendererFactory, OwAOContext
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwMainAppContext.class);

    /** name of the window positions app setting */
    protected static final String WINDOW_POSITIONS_APP_SETTINGS_NAME = "WindowPositions";

    /** placeholder for literals: will be replaced with todays date */
    public static final String LITERAL_PLACEHOLDER_TODAY = "today";

    /** placeholder for literals: will be replaced with the user ID */
    public static final String LITERAL_PLACEHOLDER_USER_ID = "userid";

    /** placeholder for literals: will be replaced with the user name */
    public static final String LITERAL_PLACEHOLDER_USER_NAME = "username";

    /** placeholder for literals: will be replaced with the user long name */
    public static final String LITERAL_PLACEHOLDER_USER_LONG_NAME = "userlongname";

    /** placeholder for literals: will be replaced with the user short name */
    public static final String LITERAL_PLACEHOLDER_USER_SHORT_NAME = "usershortname";

    /** placeholder for literals: will be replaced with the user display name */
    public static final String LITERAL_PLACEHOLDER_USER_DISPLAY_NAME = "userdisplayname";

    /** local name query string key */
    private static final String LOCAL_QUERY_KEY = "owloc";

    /** the XML node name <code>UrlOverwrite</code> */
    public static final String CONFIGNODE_URLOVERWRITE = "UrlOverwrite";

    /** the XML node name <code>Server</code> */
    public static final String CONFIGNODE_SERVER = "Server";

    /** the XML node name <code>Base</code> */
    public static final String CONFIGNODE_BASE = "Base";
    //-------------------------------------------------------------------------
    //  D r a g ' n ' D r o p
    //-------------------------------------------------------------------------
    /** prefix for the common DND document import temp directory */
    public static final String PREFIX_DRAGDROP = "dragdrop_";

    /** List of OwDocumentImportItem objects: the document input stack */
    private List m_DND_importedDocumentsList;

    /** the path to the current common DND document import temp dir */
    private String m_DND_currentUploadDir;

    /** the next file ID in the current common DND document import temp dir */
    private int m_DND_nextFileID = 0;

    /** map for plugin key events */
    private Map<String, OwKeySetting> pluginKeySettings;

    /** instance of the fixed application Configuration */
    private OwConfiguration m_Configuration;

    /** error message, set when request fails */
    private Throwable m_Error;

    /** message for user info */
    private Vector m_Messages;

    /** the currently locale
     * @see #getLocale()*/
    private Locale m_local;

    /** a collection of OwConfigChangeEventListener's */
    private OwConfigChangeEvent m_configchangeevent;

    /** custom scripts to be added to the header */
    private List m_strFinalScripts;

    /** the one and only clipboard for the session*/
    protected OwClipboard m_clipboard;

    /** the currently activated master view */
    private OwMasterView m_activemasterview;

    /** Random, to create the tempdir **/
    private final Random m_Random = new Random();

    /** ID for the current master plugin that can be used to create a HTML class style ID */
    private String m_sCurrentMasterPluginStyleClassID = "default";
    /** Map holding the id of HTML containers and the corresponding AJAX update handlers*/
    private Map<String, String> m_ajaxUpdateContainers;

    private OwInfoProviderRegistry infoProvReg;

    private String overwriteServerUrl;
    private String overwriteBaseUrl;
    /**
     * User operation listener dispatch. 
     */
    private OwUserOperationDispatch userOperationDispatch;

    /**
     * session cookie names as configured in owbootstrap.xml (&lt;SessionCookieNames&gt;) 
     * @since 3.2.0.2
     */
    private List sessionCookieNames;

    /**
     * Create a new OwMainAppContext. This will create a new OwConfiguration
     * and a new OwClipboard in the background.
     */
    public OwMainAppContext()
    {
        // call super constructor
        super();
        // create the configuration and the clipboard
        m_strFinalScripts = new LinkedList();
        userOperationDispatch = new OwUserOperationDispatch();
        m_ajaxUpdateContainers = new HashMap<String, String>();
        m_configchangeevent = new OwConfigChangeEvent();
        m_DND_importedDocumentsList = new ArrayList();
        m_Configuration = createConfiguration();
        m_clipboard = createClipboard();
        // register interfaces which are application wide available
        registerInterface(OwHistoryManagerContext.class, this);
        registerInterface(OwRoleManagerContext.class, new OwMainRoleManagerContext(this));
        registerInterface(OwNetworkContext.class, this);
        registerInterface(OwRepositoryContext.class, this);
        registerInterface(OwMandatorManagerContext.class, this);
        registerInterface(OwUserOperationDispatch.class, userOperationDispatch);
        registerInterface(OwUserOperationExecutor.class, userOperationDispatch);
        registerInterface(OwComboboxRendererFactory.class, this);

        registerInterface(OwRenditionServiceProvider.class, OwLazyLoadingProxyHandler.createSimpleClassLazyLoadedInstance(OwSimpleRenditionServiceProvider.class, OwRenditionServiceProvider.class));
    }

    /**
     * Create an instance of the OwConfiguration object. Overwrite this method
     * to change the configuration.
     * 
     * @return An OwConfiguration instance to be used by this OwMainAppContext
     */
    protected OwConfiguration createConfiguration()
    {
        return new OwConfiguration();
    }

    /**
     * Create an instance of the OwClipboard object. Overwrite this method
     * to change the clipboard implementation.
     * 
     * @return An OwClipboard instance to be used by this OwMainAppContext
     */
    protected OwClipboard createClipboard()
    {
        return new OwClipboard();
    }

    /** get the current Id to the active master plugin
     * @return plugin Id as String 
     * */
    public String getCurrentMasterPluginID()
    {
        return m_activemasterview.getPluginID();
    }

    /** get a Id for the current master plugin that can be used to create a HTML class style ID 
     * 
     * @return String Id of plugin or "default" if not plugin is active
     */
    public String getSafeCurrentMasterPluginStyleClassID()
    {
        return m_sCurrentMasterPluginStyleClassID;
    }

    /** get the current active master plugin view
     * 
     * @return OwMasterView or null if no plugin is active
     */
    public OwMasterView getCurrentMasterView()
    {
        return m_activemasterview;
    }

    /** notify the context about the new activated master plugin in order to generate context sensitive information
     * 
     * @param view_p OwMasterView
     * @throws Exception
     */
    public void onActivateMasterPlugin(OwMasterView view_p) throws Exception
    {
        m_activemasterview = view_p;
        m_sCurrentMasterPluginStyleClassID = OwString.replaceAll(view_p.getPluginID(), ".", "_");

        //  historize plugin invoke event
        getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, view_p.getPluginID(), OwEventManager.HISTORY_STATUS_OK);
    }

    /** get a reference to the default dialog manager used to open dialogs from the context in openDialog(...)
     *  The dialog manager must be created somewhere in the layout and set in the context.
     *  If no dialogs are required, you do not need to set it and this function returns null
     *
     * @return OwDialogManager or null if none is available
     */
    public OwDialogManager getDialogManager()
    {
        try
        {
            return m_activemasterview.getDialogManager();
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    /** get exception to display 
     * @return Throwable object
     */
    public Throwable getError()
    {
        return m_Error;
    }

    /** set exception to display 
     * 
     */
    public void setError(Throwable exception_p)
    {
        m_Error = exception_p;
    }

    /** Get a collection of messages to display 
     * @return Collection 
     */
    public Collection getMessages()
    {
        if (m_Messages == null)
        {
            m_Messages = new Vector();
        }
        return m_Messages;
    }

    /** add a message to be displayed for user info
     * 
     * @param sMessage_p
     */
    public void postMessage(String sMessage_p)
    {
        getMessages().add(sMessage_p);
    }

    /** init the context BEFORE the user has logged on. Called once for a session.
     *
     *  NOTE: The user is not logged in, when this function is called. 
     *        If you do not do initialize with valid credentials use LoginInit
     * 
     * optionally set a prefix to distinguish several different applications.
     * The rolemanager will filter the allowed plugins, mimesettings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     * 
     *        
     * @param oldcontext_p OwAppContext old context from previous session, used to copy flags, locals. Optional, can be null
     */
    public void init(OwAppContext oldcontext_p) throws Exception
    {
        super.init(oldcontext_p);

        // === Init the Configuration object
        m_Configuration.init(this);
        OwXMLUtil overwriteConfig = getConfiguration().getBootstrapConfiguration().getSubUtil(CONFIGNODE_URLOVERWRITE);
        if (overwriteConfig != null)
        {
            overwriteServerUrl = overwriteConfig.getSafeTextValue(CONFIGNODE_SERVER, null);
            overwriteBaseUrl = overwriteConfig.getSafeTextValue(CONFIGNODE_BASE, null);
        }
        // === Init the clipboard
        m_clipboard.init(this);
        // === set local
        if (oldcontext_p != null)
        {
            setLocale(oldcontext_p.getLocale());
        }
        else
        {
            if (getLocale() == null)
            {
                // set the default locale
                setLocale(new Locale(m_Configuration.getDefaultLanguage()));
                // detect the browser local overriding the default locale
                if (m_Configuration.getDetectBrowserLocale())
                {
                    // get the browser locale
                    Locale browserLocale = getHttpRequest().getLocale();
                    if ((null != browserLocale) && (null != browserLocale.getLanguage()))
                    {
                        setLocale(getLocaleForString(browserLocale.getLanguage()));
                    }
                }
            }
        }
        registerInterface(OwVIIdFactory.class, new OwSimpleVIIdFactory());
    }

    /**
     * Initialize the listeners for user operations (at the login time) 
     * @throws Exception
     * @since 3.1.0.3
     */
    protected void initializeUserOperationListeners() throws Exception
    {
        Set<com.wewebu.ow.server.app.OwContextBasedUOListenerFactory> factories = getConfiguration().getUserOperationListenerFactories();
        if (factories != null && factories.size() > 0)
        {
            for (OwContextBasedUOListenerFactory factory : factories)
            {
                userOperationDispatch.addUserOperationListener(factory.createListener());
            }
        }
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {

    }

    /** called when user gets logged in
     */
    public void onLogin(OwBaseUserInfo user_p) throws OwConfigurationException
    {
        // === init mandator manager as early as possible this is the earliest call after the user logged on
        try
        {
            getMandatorManager().loginInit(user_p);
            //
            initializeUserOperationListeners();
            OwUserOperationType.LOGIN.fire(userOperationDispatch, user_p, OwUserOperationEvent.OWD_APPLICATION);
        }
        catch (Exception e)
        {
            String msg = "The Mandator Manager (" + getMandatorManager().getClass().toString() + ") can not be initialized after login.";
            LOG.error(msg, e);
            throw new OwConfigurationException(OwString.localize1(getLocale(), "app.OwConfiguration.mandatormanageriniterror", "The Tenant Manager (%1) can not be initialized after login.", getMandatorManager().getClass().toString()), e);
        }

        // === update license counts
        //OwUsageCounter.onLogin(m_request.getSession().getId(),user_p);
    }

    /** get the current locale,
     * which can be used as a prefix/postfix to distinguish localization resources
     *
     * @return Locale
     */
    public Locale getLocale()
    {
        return m_local;
    }

    public void setLocale(java.util.Locale locale_p)
    {
        m_local = locale_p;
    }

    /** check if function or plugin is available for the user. Checks the user roles.
     *
     * @param iCategory_p Category of the function in question, e.g. "MainPlugin", "TaskFunction", "SearchTemplate"...
     * @param strFunctionName_p Name of Function or Plugin
     *
     * @return true = function is allowed, false = function is denied
     */
    public boolean isAllowed(int iCategory_p, String strFunctionName_p) throws Exception
    {
        return getRoleManager().isAllowed(iCategory_p, strFunctionName_p);
    }

    /** init the context AFTER the user has logged in. Called once for a session.
     *
     *  NOTE: This function is called only once after login to do special initialization, 
     *        which can only be performed with valid credentials.
     */
    public void loginInit() throws Exception
    {

        // === Init configuration instance
        m_Configuration.loginInit();
    }

    /** retrieve the network object */
    public OwNetwork<?> getNetwork()
    {
        return m_Configuration.getNetwork();
    }

    /** retrieve the network object */
    public OwHistoryManager getHistoryManager()
    {
        return m_Configuration.getHistoryManager();
    }

    /** retrieve the RoleManager object */
    public OwRoleManager getRoleManager()
    {
        return m_Configuration.getRoleManager();
    }

    /** create a new instance of the field manager */
    public OwFieldManager createFieldManager() throws Exception
    {
        return m_Configuration.createFieldManager();
    }

    /** retrieve the Configuration object */
    public OwConfiguration getConfiguration()
    {
        return m_Configuration;
    }

    /** retrieve the Configuration object */
    public OwBaseConfiguration getBaseConfiguration()
    {
        return m_Configuration;
    }

    /** retrieve the settings object */
    public OwSettings getSettings()
    {
        return m_Configuration.getSettings();
    }

    /** get the credentials of the logged in user 
     * @return OwCredentials of the log in user
     */
    public OwCredentials getCredentials() throws Exception
    {
        return getNetwork().getCredentials();
    }

    /** check the credentials of the logged in user 
     * @return true if user is logged in and has valid credentials
     */
    public boolean isLogin() throws Exception
    {
        return (getNetwork().getCredentials() != null);
    }

    //
    /** check the current user is authenticated container based
     * @return true if user is logged in and the login is done with container based authentication
     */
    public boolean isContainerBasedAuthenticated() throws Exception
    {
        return (getNetwork().getCredentials() != null) && (getNetwork().getCredentials().isContainerBasedAuthenticated());
    }

    /** clear the credentials, i.e. log the user off
     */
    public void logout() throws Exception
    {
        // === update license counts
        //OwUsageCounter.onLogout(m_request.getSession().getId(),getUserInfo());

        onSessionTimeOut(getHttpSession());

        super.logout();
    }

    /** called when the session times out
     * @param httpSession_p 
     * 
     */
    public void onSessionTimeOut(HttpSession httpSession_p)
    {
        try
        {
            // === first notify documents
            broadcast(null, OwUpdateCodes.LOGOUT);
        }
        catch (Exception e)
        {
            LOG.error("Logout broadcast failed for session: " + getSessionDisplayName(), e);
        }
        // === clear temporary files
        try
        { // delete the user specific temp folder path = <Application-Tempdir>/<user-session-id>
            String tempDir = getConfiguration().getTempDir().replace('\\', '/');
            if (tempDir.endsWith("/"))
            {
                deleteDir(tempDir + httpSession_p.getId());
            }
            else
            {
                deleteDir(tempDir + "/" + httpSession_p.getId());
            }
        }
        catch (Exception ex)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwMainAppContext.onSessionTimeOut: Error occured during deleting temp folder: ", ex);
            }
            LOG.warn("OwMainAppContext.onSessionTimeOut: Could not delete user temp folder!");
        }
        try
        {
            // === now log out
            getNetwork().logout();
            // === release resources which were allocated during session
            getNetwork().releaseResources();
        }
        catch (Exception e)
        {
            LOG.error("Logout failed for session: " + getSessionDisplayName(), e);
        }
    }

    public String getDesignDir() throws Exception
    {
        return "/designs/" + getRoleManager().getDesign();
    }

    public String getDesignURL() throws Exception
    {
        return getBaseURL() + getDesignDir();
    }

    /** get a event URL to change the language
     * NOTE:    This event URL is not checked with request key,
     *          so it can be called arbitrary and be stored in the users browser. 
     *
     * @param strLangID_p String ID of the language e.g.: "de"
     * @return String event URL 
     */
    public String getLocalEventURL(String strLangID_p)
    {
        return getJSPPageURL() + "?" + LOCAL_QUERY_KEY + "=" + strLangID_p;
    }

    /** opens a help page in the connected help plugin if available
     * 
     * @param sHelppath_p String path to the help page
     * @throws Exception 
     */
    public void openHelp(String sHelppath_p) throws Exception
    {
        // find master plugin with class
        Iterator it = getConfiguration().getMasterPlugins(false).iterator();
        while (it.hasNext())
        {
            OwMasterPluginInstance inst = (OwMasterPluginInstance) it.next();
            if (inst.getPluginClassName().equals("com.wewebu.ow.server.plug.owhelp.OwHelpDocument"))
            {
                inst.getDocument().dispatch(OwDispatchCodes.OPEN_OBJECT, sHelppath_p, null);
                break;
            }
        }
    }

    /**  handles the m_request from the page and dispatches ONLY to the selected target
     *   @param request_p  HttpServletRequest
     *   @param response_p  HttpServletResponse
     */
    public boolean handleRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // === Authentication provider available ?

        // === delete marked deleted directories before m_request is handled
        deleteMarkedDirectorys();

        // === set language
        String strLocal = request_p.getParameter(LOCAL_QUERY_KEY);
        if (strLocal != null)
        {
            setLocale(getLocaleForString(strLocal));
        }

        // === check SSL Mode
        if ((getConfiguration().getSSLMode() == OwConfiguration.SSL_MODE_SESSION) && (!request_p.isSecure()))
        {
            LOG.error("OwMainAppContext.handleRequest: The session requires a secure connection, please use the https connection provided by the administrator.");
            throw new OwSessionException(OwString.localize(getLocale(), "app.OwMainAppContext.sslenforced", "The session requires a secure connection, please use the https connection provided by the administrator."));
        }

        // === handle m_request and do exception handling
        // clear error and message
        setError(null);
        resetMessages();

        try
        {
            return super.handleRequest(request_p, response_p);
        }
        catch (Throwable handleRequestError)
        {
            try
            {
                if (getConfiguration().getMessageBoxOnUserError())
                {
                    String sMessage = ((OwUserOperationException) handleRequestError.getCause()).getMessage(getLocale());
                    addFinalScript("alert('" + sMessage + "');");
                    LOG.error("OwMainAppContext handleRequest Exception...", handleRequestError);
                    return true;
                }
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.handleRequest: Error creating the Message-Box-On-User-Error...", e);
            }

            LOG.error("OwMainAppContext handleRequest Exception...", handleRequestError);

            // set error for next m_request to be displayed in OwMainLayout
            setError(handleRequestError);
            if (handleRequestError instanceof Error)
            {
                LOG.error("\n\n\nERROR:OwMainAppContext handleRequest ERROR", handleRequestError);
            }
            return true;
        }
    }

    /**
     * Adds a single run custom java script
     *  The script runs upon m_request end, invoked by the onLoad Body tag handler.
     *  Scripts run only once and are removed after the m_request. 
     *  This method also considers the script's priority.
     *  
     * @param script_p The new script to be added.
     * @since 3.0.0.0
     */
    public void addFinalScript(OwScript script_p)
    {
        int i = 0;
        for (; i < m_strFinalScripts.size(); i++)
        {
            OwScript element = (OwScript) m_strFinalScripts.get(i);
            if (script_p.getPriority() > element.getPriority())
            {
                break;
            }
        }
        m_strFinalScripts.add(i, script_p);
    }

    /**
     * Add final script only if the same script was not already added.
     * @param script_p - the {@link OwScript} object
     * @return - <code>true</code> if the script was not added before in scripts list.
     * @since 3.1.0.0
     */
    public boolean addUniqueFinalScript(OwScript script_p)
    {
        boolean scriptAdded = false;
        if (!m_strFinalScripts.contains(script_p))
        {
            addFinalScript(script_p);
            scriptAdded = true;
        }
        return scriptAdded;

    }

    /**
     * Adds a single run custom java script.
     *
     *  The script runs upon m_request end, invoked by the onLoad Body tag handler.
     *  Scripts run only once and are removed after the m_request. 
     *  This method also considers the script's priority.
     *
     * @param strScript_p String custom java script code (without <script..> tags).
     */
    public void addFinalScript(String strScript_p)
    {
        addFinalScript(new OwScript(strScript_p));
    }

    /** 
     * clear the final script for this request
     *
     */
    public void clearFinalScript()
    {
        m_strFinalScripts.clear();
    }

    /** get the current final script to be executed for this request
     * 
     * @return String with JavaScript code
     */
    public String getFinalScript()
    {
        StringBuilder strFinalScript = new StringBuilder();
        Iterator iterator = m_strFinalScripts.iterator();
        while (iterator.hasNext())
        {
            OwScript aFinalScript = (OwScript) iterator.next();
            strFinalScript.append(aFinalScript.getScript());
        }
        return strFinalScript.toString();
    }

    /** get a name for the current session,
     *  used for warning and error logs to identify the session later.
     *  Do not mix up with session identifier from HTTP session.
     *
     * @return String name of Session, usually the name of the logged on user
     */
    public String getSessionDisplayName()
    {
        // === set session display name
        try
        {
            return getNetwork().getCredentials().getUserInfo().getUserLongName();
        }
        catch (Exception e)
        {
            return "?";
        }
    }

    /** get the logged in user
    *
    * @return OwBaseUserInfo or null if no user is logged in
    */
    public OwBaseUserInfo getUserInfo()
    {
        try
        {
            return getNetwork().getCredentials().getUserInfo();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** write debug dump to Writer
     * @param w_p HTML Writer
     */
    public void dump(Writer w_p) throws Exception
    {
        w_p.write("<table cellspacing='0' cellpadding='0' border='1' width='100%'>");

        w_p.write("<tr bgcolor='#aaaaaa'><td class='OwDump'>");

        w_p.write("<b>MainAppContext</b>");

        w_p.write("</td></tr>");

        w_p.write("</table>");

        super.dump(w_p);
    }

    /** get the one and only clipboard for the session
     *
     * @return OwClibboard
     */
    public OwClipboard getClipboard()
    {
        return m_clipboard;
    }

    /** get a name for the configuration to use 
     *  can be used to distinguish different applications
     * 
     * @return String a name for the configuration, or "default" to use default
     */
    public String getConfigurationName()
    {
        // === get the request URL as the configuration name
        return getJSPPageURL();
    }

    /** optionally translate a name into a readable label, used for property class names in ECM adaptors which do not support separate displaynames
     * @param strName_p name e.g. attribute name  to look for
     *
     * @return translated Display name if found in label file or the given attribute name  if nothing could be translated.
     */
    public String localizeLabel(String strName_p)
    {
        return OwString.localizeLabel(getLocale(), strName_p);
    }

    /** check if a display label is defined for the given symbol name
     * @param strName_p name e.g. attribute name  to look for
     *
     * @return true = displayname is defined for symbol
     */
    public boolean hasLabel(String strName_p)
    {
        return OwString.hasLabel(getLocale(), strName_p);
    }

    /** register a key event for this plugin if defined in key map, call in onRender method only
     *
     * @param strPluginID_p String ID of the plugin to register
     * @param strEventURL_p String event URL of plugin to open
     * @param strFormName_p String optional form name
     * @param strDescription_p String description for key table
     */
    public void registerPluginKeyEvent(String strPluginID_p, String strEventURL_p, String strFormName_p, String strDescription_p) throws Exception
    {
        if (pluginKeySettings == null)
        {
            //get a list of OwPluginKeyBoardMaping keyboard mappings
            Collection<OwPluginKeyBoardMaping> keyMapping = getSafeListAppSetting("KeyboardCommandMap");
            if (keyMapping.size() == 0)
            {
                return;
            }

            pluginKeySettings = OwPluginKeyBoardMaping.toKeySettings(keyMapping);

        }

        OwKeySetting setting = pluginKeySettings.get(strPluginID_p);

        if (null != setting)
        {
            OwKeyAction action = setting.createAction(strEventURL_p, strFormName_p, strDescription_p);
            action.register(this);
        }
    }

    // === settings / configuration functions for the application
    /** get a setting for the application
     *
     * @param strName_p name of property to retrieve
     * @param iDefault_p default int value
     *
     * @return int
     */
    public int getSafeIntAppSetting(String strName_p, int iDefault_p)
    {
        try
        {
            return ((Integer) ((OwSettingsProperty) m_Configuration.getAppSettings().getProperties().get(strName_p)).getValue()).intValue();
        }
        catch (Exception e)
        {
            return iDefault_p;
        }
    }

    /** get a setting for the application
     *
     * @param strName_p name of property to retrieve
     * @param strDefault_p default String value
     *
     * @return String
     */
    public String getSafeStringAppSetting(String strName_p, String strDefault_p)
    {
        try
        {
            return (String) ((OwSettingsProperty) m_Configuration.getAppSettings().getProperties().get(strName_p)).getValue();
        }
        catch (Exception e)
        {
            return strDefault_p;
        }
    }

    /** get a setting for the application
     *
     * @param strName_p name of property to retrieve
     *
     * @return List
     */
    public Collection getSafeListAppSetting(String strName_p)
    {
        try
        {
            return (Collection) ((OwSettingsProperty) m_Configuration.getAppSettings().getProperties().get(strName_p)).getValue();
        }
        catch (Exception e)
        {
            // return empty collection
            return new Vector();
        }
    }

    /** get a setting for the application
     *
     * @param strName_p name of property to retrieve
     * @param default_p Object if setting is not defined in plugin descriptor
     *
     * @return Object Settings Property
     */
    public boolean getSafeBooleanAppSetting(String strName_p, boolean default_p)
    {
        try
        {
            return ((Boolean) ((OwSettingsProperty) m_Configuration.getAppSettings().getProperties().get(strName_p)).getValue()).booleanValue();
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    /** flag indicating if the java script Date control should be used instead of a HTML combobox control
     * @return boolean true = use java script control, false = use combobox control
     */
    public boolean useJS_DateControl()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return m_Configuration.getBootstrapConfiguration().getSafeBooleanValue("UseJSDateControl", false);
    }

    /** get the format string to use for date values
     * @return date format String e.g.: "dd.MM.yyyy (HH:mm)"
     */
    public String getDateFormatString()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return this.getSafeStringAppSetting("DateFormatString", "dd.MM.yyyy (HH:mm)");
    }

    /** get the format string to use for time values
     * @return date format String e.g.: "HH:mm:ss"
     */
    public String getTimeFormatString()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return this.getSafeStringAppSetting("TimeFormatString", "HH:mm:ss");
    }

    /** get the format string to use for date values do not use time tokens
     * @return date format String e.g.: "dd.MM.yyyy"
     */
    public String getDateFormatStringWithoutTime()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return OwDateTimeUtil.removeTimeFormatTokens(getDateFormatString());
    }

    /** get the format string to use for time values do not use date tokens
     * @return date format String e.g.: "HH:mm:ss"
     */
    public String getDateFormatStringWithoutDate()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return OwDateTimeUtil.removeDateFormatTokens(getTimeFormatString());
    }

    /** 
     * Get maximum number of items that can be hold in the clipboard
     * @return the number of items that can be hold in the clipboard.
     * @since 3.0.0.0
     */
    public int getMaxClipboardSize()
    {
        return getSafeIntAppSetting("MaxClipboardSize", 10);
    }

    /**
     * Get the document function plugin IDs available in clipboard. 
     * These document functions can be executed on clipboard items. 
     * @return {@link java.util.Collection} - a collection containing plugin IDs.
     * @since 3.0.0.0
     */
    public Collection getClipboardAvailableDocFunctionIds()
    {
        return getSafeListAppSetting("DocFunctionsInClipboard");
    }

    /** flag indicating if clipboard should be displayed
     * @return boolean
     */
    public boolean doShowClipboard()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return getSafeBooleanAppSetting("showclipboard", true);
    }

    /** flag indicating if wild card descriptions should be displayed for searches.
     * 
     * @return boolean
     */
    public boolean doShowWildCardDescriptions()
    {
        return getSafeBooleanAppSetting("showwildcarddescriptions", false);
    }

    /** 
     * flag indicating if the application should use the HTML5 implementation of the DnD feature. 
     * If false, the DnD Applet will be used instead.
     * 
     * @return boolean
     * @since 4.1.1.0
     */
    public boolean isUseHtml5DragAndDrop()
    {
        return getSafeBooleanAppSetting("UseHtml5DragAndDrop", false);
    }

    /** number of items in a page for lists which support paging
     * @return int
     */
    public int getPageSizeForLists()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return getSafeIntAppSetting("pagesize", 20);
    }

    /** get the ID of the startup plugin
     * @return String ID 
     */
    public String getStartupID()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        return getSafeStringAppSetting("startupid", null);
    }

    /** get the maximum number of sort criteria, which can be applied to a list at once
     * @return int max number of sort criteria
     */
    public int getMaxSortCriteriaCount()
    {
        // get setting either from OwConfiguration (bootstrap) or from OwSettings (changeable during runtime)
        int iMaxSort = this.getSafeIntAppSetting("MaxSortCriteriaCount", 1);
        if (iMaxSort < 1)
        {
            iMaxSort = 1;
        }

        return iMaxSort;
    }

    /** get a setting for the application
    *
    * @return Object Settings Property
    */
    public OwWindowPositions getWindowPositions()
    {
        try
        {
            OwSettingsProperty prop = (OwSettingsProperty) m_Configuration.getAppSettings().getProperties().get(WINDOW_POSITIONS_APP_SETTINGS_NAME);

            return (OwWindowPositions) prop.getValue();
        }
        catch (Exception e)
        {
            return OwWindowPositions.getUndefWindowPosition();
        }
    }

    /** get a event URL to save the viewer and main window position
     *  the position values have to be appended as a query string,
     *  using the getter / setter names as request parameter names in the following form:
     * 
     *  "[getter / setter name]"
     *  
     *  e.g.:
     *  
     *  for getViewerHeight the Parameter name would be
     *  
     *  "ViewerHeight"
     */
    public String getWindowPositionsSaveURL()
    {
        return getInfiniteEventURL(this, "SaveWindowPositions", null);
    }

    /** called when getWindowPositionsSaveURL was invoked see getWindowPositionsSaveURL for details
     */
    public void onSaveWindowPositions(HttpServletRequest request_p) throws Exception
    {
        OwWindowPositions pos = (OwWindowPositions) ((OwSettingsProperty) m_Configuration.getAppSettings().getProperties().get(WINDOW_POSITIONS_APP_SETTINGS_NAME)).getValue();

        try
        {
            pos.setFromRequest(request_p, "");
        }
        catch (Exception e)
        { /* ignore if some members have not been set from request */
        }

        // save values
        m_Configuration.getSettings().saveUserPrefs();
    }

    /** list of directories that are marked deleted for the next m_request */
    private List m_deleteDirs;

    /** deletes a temp dir and all files within it
     *  @param strDir_p String directory
     */
    public void deleteTempDir(String strDir_p)
    {
        if (m_deleteDirs == null)
        {
            m_deleteDirs = new LinkedList();
        }

        m_deleteDirs.add(strDir_p);
    }

    /** delete the for deletion marked directories, see deleteTempDir
     * */
    private void deleteMarkedDirectorys()
    {
        if (m_deleteDirs == null)
        {
            return;
        }

        Iterator it = m_deleteDirs.iterator();

        while (it.hasNext())
        {
            try
            {
                deleteDir((String) it.next());
            }
            catch (Exception e)
            {

            }
        }

        m_deleteDirs = null;
    }

    /** delete the given directory and all files within, 
     * call deleteTempDir to mark a directory to be deleted
     * 
     * */
    private void deleteDir(String strDir_p)
    {
        File delDir = new File(strDir_p);
        String[] list = delDir.list();

        // delete all files within the dir
        if (list != null)
        {
            for (int i = 0; i < list.length; i++)
            {
                File file = new File(delDir, list[i]);
                if (file.isFile())
                {
                    boolean fileExists = file.exists();
                    if (fileExists && !file.delete())
                    {
                        if (LOG.isInfoEnabled())
                        {
                            LOG.warn("OwMainAppContext.deleteDir: Error deleting all files of directory, name = " + strDir_p);
                        }
                    }
                }
                else
                {// this can only happens if we forgot to delete a temporary folder
                    deleteDir(file.getAbsolutePath());
                }
            }
        }

        // delete the directory
        boolean dirExists = delDir.exists();
        if (dirExists && !delDir.delete())
        {
            if (LOG.isInfoEnabled())
            {
                LOG.warn("OwMainAppContext.deleteDir: Error deleting the directory, name = " + strDir_p);
            }
        }
    }

    /** creates a unique temp directory
     * 
     * @param strPrefix_p String prefix to use for name
     * @return Returns the created tempDir.
     * @throws OwConfigurationException 
     */
    public String createTempDir(String strPrefix_p) throws OwConfigurationException
    {
        // create a temp directory with random number and time stamp of the form:
        // "/<user-session-id>/<strPrefix_p>_<timestamp>_<random number>"

        StringBuilder tempDir = new StringBuilder(getConfiguration().getTempDir()); //temp folder to use
        tempDir.append("/");
        tempDir.append(getHttpSession().getId()); //create user dependent subfolder <session-id>
        tempDir.append("/");
        tempDir.append(strPrefix_p); //<strPrefix_p>
        tempDir.append("_");
        tempDir.append(System.currentTimeMillis()); //<timestamp>
        tempDir.append("_");
        //		String.valueOf(m_Random.nextInt());
        tempDir.append(m_Random.nextInt()); //<random number>

        File dir = null;

        dir = new File(tempDir.toString());

        if (!(dir.exists()))
        {
            boolean success = dir.mkdirs();
            if (!success)
            {
                String msg = "OwMainAppContext.createTempDir: Temp directory could not be created. Check bootstrap.xml for TempDir setting.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }
        }

        return tempDir.toString();
    }

    /** get the path of the current drag and drop upload directory
     * 
     * @return path drag and drop upload directory path, or null if not available
     * @deprecated since DND DocumentImporter was introduced
     */
    @Deprecated
    public String getDragAndDropUploadDir()
    {
        // we do no longer support the old Drag'n'Drop mechanism. So we always return
        // null to indicate that no Drag'n'Drop has been performed
        return null;
    }

    /**
     * delete temporarily drag and drop upload directory
     * @throws Exception
     * @deprecated since DND DocumentImporter was introduced
     */
    @Deprecated
    public void clearDragDropUploadDir() throws Exception
    {
        // we do no longer support the old Drag'n'Drop mechanism.
        // So we just do nothing here.
    }

    /**
     * Add an <code>{@link OwDocumentImportItem}</code> to the stack of DND
     * imported documents.
     * 
     * @param importedDocument_p the new <code>{@link OwDocumentImportItem}</code> to add to the stack
     */
    public void addDNDImportedDocumentToStack(OwDocumentImportItem importedDocument_p)
    {
        m_DND_importedDocumentsList.add(importedDocument_p);
    }

    /**
     * Get the <code>i_p</code>th imported document from the DND document import stack.
     * 
     * @param i_p the (0-based) index of the imported document to return
     * 
     * @return the <code>i_p</code>th imported document from the DND document import stack
     */
    public OwDocumentImportItem getDNDImportedDocument(int i_p)
    {
        return (OwDocumentImportItem) m_DND_importedDocumentsList.get(i_p);
    }

    /**
     * Get the size of the DND document import stack.
     * 
     * @return the size of the DND document import stack
     */
    public int getDNDImportedDocumentsCount()
    {
        return m_DND_importedDocumentsList.size();
    }

    /**
     * Release all imported documents of the DND document import stack and remove
     * the current temp dir.
     * 
     * @throws Exception
     */
    public void releaseDNDImportedDocuments() throws Exception
    {
        // release the stack of DND imported documents
        for (int i = 0; i < m_DND_importedDocumentsList.size(); i++)
        {
            ((OwDocumentImportItem) m_DND_importedDocumentsList.get(i)).release();
        }
        m_DND_importedDocumentsList = new ArrayList();
        // clear the DND temp dir
        String dirToDelete = m_DND_currentUploadDir;
        m_DND_currentUploadDir = null;
        m_DND_nextFileID = 0;
        if (dirToDelete != null)
        {
            deleteDir(dirToDelete);
        }
    }

    public String getDNDDocumentImportTempDir() throws OwConfigurationException
    {
        if (m_DND_currentUploadDir == null)
        {
            m_DND_currentUploadDir = createTempDir(PREFIX_DRAGDROP);
        }
        return m_DND_currentUploadDir;
    }

    public int getNextDNDDocumentImportFileID()
    {
        return (m_DND_nextFileID++);
    }

    /** create a attribute bag for scalar settings specific for the current user.
     * 
     *  Unlike the OwSettings the AttributeBag allows simple scalar values
     *  but with high performance.
     *  
     *  NOTE:
     *  The OwSettings are used for complex settings for the plugins which are manually changed from time to time
     *  by the user or the Administrator.
     *  	==> Performance is negligible, but values are highly structured.
     *  	==> OwSettings are usually based on a XML document. (depends on adapter implementation)
     *  
     *  The OwAttributeBagWriteable is used by the application itself to persist state.
     *  	==> Structure is negligible, but performance is important
     *  	==> OwAttributeBagWriteable is usually based on a DB table. (depends on adapter implementation)
     *  
     * @param sName_p String name of the attribute bag
     *  
     * @return OwAttributeBagWriteable to read and write scalars
     * 
     * @throws Exception 
     */
    public OwAttributeBagWriteable createUserAttributeBag(String sName_p) throws Exception
    {
        try
        {
            OwAttributeBagWriteable attributeBagWriteable = (OwAttributeBagWriteable) getNetwork().getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, sName_p, true, false);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwMainAppContext.createUserAttributeBag: User AttributeBag Writable [BagName = " + sName_p + "] was successfully initialized...");
            }
            return attributeBagWriteable;
        }
        catch (OwObjectNotFoundException e)
        {
            LOG.warn("OwMainAppContext.createUserAttributeBag: No persistent User AttributeBag Writable available [BagName = " + sName_p + "], use non persistent memory writer.");

            // no persistent attribute bag available, at least return a dummy attribute bag so application can work
            return new OwSimpleAttributeBagWriteable();
        }
    }

    public OwBaseUserInfo getCurrentUser() throws Exception
    {
        return getNetwork().getCredentials().getUserInfo();
    }

    /** get a ID / name for the calling client
     * 
     * @return String
     */
    public String getClientID()
    {
        return getHttpRequest().getRemoteAddr();
    }

    /** name of the default class used to create simple folders
    *
    * @return String classname/id of default folder class or null if not defined
    */
    public String getDefaultFolderClassName()
    {
        return getConfiguration().getDefaultFolderClassName();
    }

    /** get a wild card to be used in the client for the given wild card type
     * 
     * @param wildcardtype_p as defined in OwWildCardDefinition.WILD_CARD_TYPE_...
     * @return wildcard string or null if not wildcard is defined on the client
     */
    public String getClientWildCard(int wildcardtype_p)
    {
        return (String) getConfiguration().getClientWildCardDefinitions().get(Integer.valueOf(wildcardtype_p));
    }

    /** delegates OwConfigChangeEventListener notifications to the subscribed listeners
     * 
     * @return an {@link com.wewebu.ow.server.ecm.OwRepositoryContext.OwConfigChangeEventListener}
     */
    public OwConfigChangeEventListener getConfigChangeEvent()
    {
        return m_configchangeevent;
    }

    /** add a config change event listener to be notified about config changes
     * 
     * @param listener_p
     */
    public void addConfigChangeEventListener(OwConfigChangeEventListener listener_p)
    {
        m_configchangeevent.addEventListener(listener_p);
    }

    /** add a config change event listener to be notified about config changes
     * 
     * @param listener_p
     */
    public void removeConfigChangeEventListener(OwConfigChangeEventListener listener_p)
    {
        m_configchangeevent.removeEventListener(listener_p);
    }

    /** get a ID / name for the calling mandator
     * 
     * @return String mandator or null if no mandator is supported
     */
    public String getMandatorID()
    {
        OwMandator mandator = getMandator();
        if (null == mandator)
        {
            return null;
        }
        else
        {
            return mandator.getID();
        }
    }

    /** retrieve the MandatorManager reference
     * 
     * @return an {@link OwMandatorManager}
     */
    public OwMandatorManager getMandatorManager()
    {
        return getConfiguration().getMandatorManager();
    }

    /** get the mandator interface of the current logged in user
     * 
     * @return OwMandator or null if not yet defined
     */
    public OwMandator getMandator()
    {
        return getConfiguration().getMandatorManager().getUserMandator();
    }

    public JdbcTemplate getJDBCTemplate()
    {
        return (m_Configuration == null) ? null : m_Configuration.getJDBCTemplate();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwNetworkContext#resolveLiteralPlaceholder(java.lang.String, java.lang.String)
     */
    public Object resolveLiteralPlaceholder(String contextname_p, String placeholdername_p) throws Exception
    {
        if (placeholdername_p.equals(LITERAL_PLACEHOLDER_TODAY))
        {
            return new Date();
        }

        if (placeholdername_p.equals(LITERAL_PLACEHOLDER_USER_ID))
        {
            try
            {
                return getCurrentUser().getUserID();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(LITERAL_PLACEHOLDER_USER_NAME))
        {
            try
            {
                return getCurrentUser().getUserName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(LITERAL_PLACEHOLDER_USER_LONG_NAME))
        {
            try
            {
                return getCurrentUser().getUserLongName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(LITERAL_PLACEHOLDER_USER_SHORT_NAME))
        {
            try
            {
                return getCurrentUser().getUserShortName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(LITERAL_PLACEHOLDER_USER_DISPLAY_NAME))
        {
            try
            {
                return getCurrentUser().getUserDisplayName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        LOG.error("OwMainAppContext.resolveLiteralPlaceholderValue: Invalid placeholder for the search template used; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p);
        throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderInvalid", "Invalid placeholder (%1) for the search template (%2) used.", placeholdername_p, contextname_p));
    }

    /**
     * (overridable)
     * Retrieves a resource for this application context.<br>
     * 
     * @param resourcePath_p the classpath sub-path to retrieve 
     * @return the {@link URL} of the requested resource relative to this class classpath 
     * @since 2.5.2
     */
    protected URL getAppContextResource(String resourcePath_p)
    {
        return OwMainAppContext.class.getResource(resourcePath_p);
    }

    /**
     * Creates a renderer component for comboboxes.
     * @param model_p - the {@link OwComboModel} object
     * @param strID_p - the HTML id of this combo.
     * @param fieldDefinition_p - the {@link OwFieldDefinition} object
     * @param fieldProvider_p - the {@link OwFieldProvider} object
     * @param description_p - the {@link OwString} description of the field . 
     *                        If <code>null</code> the description of the field definition will be used. 
     * @return a {@link OwComboboxRenderer} object.
     * @since 3.0.0.0
     */
    public OwComboboxRenderer createComboboxRenderer(OwComboModel model_p, String strID_p, OwFieldDefinition fieldDefinition_p, OwFieldProvider fieldProvider_p, OwString description_p)
    {
        OwComboboxRenderer rendererInstance = null;
        try
        {
            rendererInstance = createComboboxRenderer();
        }
        catch (Exception e)
        {
            LOG.error("Cannot create the combobox renderer. Default implementation will be used. Reason:", e);
        }
        if (rendererInstance == null)
        {
            rendererInstance = new OwClassicComboboxRenderer();
        }
        rendererInstance.setContext(this);
        rendererInstance.setFieldId(strID_p);
        rendererInstance.setFieldDefinition(fieldDefinition_p);
        rendererInstance.setFieldProvider(fieldProvider_p);
        rendererInstance.setModel(model_p);
        rendererInstance.setFieldDescription(description_p);
        return rendererInstance;
    }

    /**
     * Method to resolve a given String to a valid Locale which is defined in the configuration.<br />
     * Will return the <code>getConfiguration().getDefaultLanguage()</code> by default, or
     * if the given Locale-String is <code><b>null</b></code>.
     * @param strLocale_p String which represents the Locale
     * @return Locale which is matching the given String, or the default language
     * @since 3.0.0.0
     */
    private Locale getLocaleForString(String strLocale_p)
    {
        // set the default locale
        Locale loc = new Locale(getConfiguration().getDefaultLanguage());
        if ((null != strLocale_p))
        {
            Iterator<?> it = getConfiguration().getAvailableLanguages().iterator();
            while (it.hasNext())
            {
                try
                {
                    OwXMLUtil langNode = new OwStandardXMLUtil((org.w3c.dom.Node) it.next());
                    String strLocale = langNode.getSafeTextValue(null);
                    if ((null != strLocale) && strLocale_p.equals(strLocale))
                    {
                        loc = new Locale(strLocale);
                        break;
                    }
                }
                catch (Exception e)
                {
                    String msg = "OwMainAppContext.getLocaleForString: could not read XML node! Skiping to next.";
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug(msg, e);
                    }
                    else
                    {
                        LOG.warn(msg);
                    }
                }
            }
        }

        return loc;
    }

    /**
     * Add a pair of HTML container ID and the AJAX handler for it.
     * @param containerId_p  - the HTML container id
     * @param ajaxEventHandler_p - the AJAX handler
     * @since 3.1.0.0
     */
    public void addAjaxUpdateContainer(String containerId_p, String ajaxEventHandler_p)
    {
        m_ajaxUpdateContainers.put(containerId_p, ajaxEventHandler_p);
    }

    /**
     * Get the AJAX handler, or <code>null</code> for the given HTML container ID.
     * @param containerId_p - the HTML container id
     * @return the AJAX handler, or <code>null</code> for the given HTML container ID.
     * @since 3.1.0.0
     */
    public String getAjaxUpdateURL(String containerId_p)
    {
        return m_ajaxUpdateContainers.get(containerId_p);
    }

    public OwInfoProviderRegistry getInfoProviderRegistry()
    {
        if (infoProvReg == null)
        {
            infoProvReg = new OwSimpleInfoProviderRegistry(getRegisteredInterface(OwRoleManagerContext.class));
        }
        return infoProvReg;
    }

    /**
     * Reset messages collection.
     * @since 3.1.0.0
     */
    public void resetMessages()
    {
        if (m_Messages != null)
        {
            m_Messages.clear();
            m_Messages = null;
        }
        m_Messages = new Vector();
    }

    /* just for debugging reason
    public void readExternal(ObjectInput arg0) throws IOException, ClassNotFoundException
    {
    	LOG.fatal("OwMainAppContext.readExternal: OwMainAppContext can not be persistent");
    	throw new IOException("OwMainAppContext.readExternal: OwMainAppContext can not be persistent");
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
    	LOG.fatal("OwMainAppContext.writeExternal: OwMainAppContext can not be persistent");
    	throw new IOException("OwMainAppContext.writeExternal: OwMainAppContext can not be persistent");
    	
    }
    */

    public void addUserOperationListener(OwUserOperationListener listener_p)
    {
        if (listener_p != null)
        {
            userOperationDispatch.addUserOperationListener(listener_p);
        }
    }

    public void removeUserOperationListener(OwUserOperationListener listener_p)
    {
        if (listener_p != null)
        {
            userOperationDispatch.removeUserOperationListener(listener_p);
        }
    }

    public OwComboboxRenderer createComboboxRenderer()
    {
        OwComboboxRenderer rendererInstance = null;
        try
        {
            String rendererClassName = m_Configuration.getComboboxRendererClassName();
            Class<?> rendererClass = Class.forName(rendererClassName);
            rendererInstance = (OwComboboxRenderer) rendererClass.newInstance();
        }
        catch (Exception e)
        {
            LOG.error("Cannot create the combobox renderer. Default implementation will be used. Reason:", e);
        }
        if (rendererInstance == null)
        {
            rendererInstance = new OwClassicComboboxRenderer();
        }
        rendererInstance.setContext(this);

        return rendererInstance;
    }

    /**
     * initialization of session cookie names as configured in owbootstrap.xml (&lt;SessionCookieNames&gt;)
     * @since 3.2.0.2
     */
    private void initSessionCookieNames()
    {
        if (this.sessionCookieNames == null)
        {
            this.sessionCookieNames = new ArrayList<String>();
            List sessionCookieNames_temp = getConfiguration().getBootstrapConfiguration().getSafeStringList("SessionCookieNames");
            if (sessionCookieNames_temp.isEmpty())
            {
                sessionCookieNames_temp.add("JSESSIONID");
                sessionCookieNames_temp.add("ECLIENTJSESSIONID"); // ECLIENTJSESSIONID used in IBM CM environments
                sessionCookieNames_temp.add("LtpaToken");
                sessionCookieNames_temp.add("LtpaToken2");
            }
            Iterator<String> it = sessionCookieNames_temp.iterator();
            while (it.hasNext())
            {
                this.sessionCookieNames.add(it.next().toUpperCase());
            }
        }
    }

    /**
     * get the list of the session cookie names as configured in owbootstrap.xml (&lt;SessionCookieNames&gt;)
     * @return List
     * @since 3.2.0.2
     */
    public List getSessionCookieNames()
    {
        initSessionCookieNames();
        return this.sessionCookieNames;
    }

    /**
     * get session cookie name value pairs delimited by whitespace 
     * @return String
     * @since 3.2.0.2
    */
    public String getSessionCookieData(HttpServletRequest httpServletRequest)
    {
        initSessionCookieNames();
        StringBuilder cookiesData = new StringBuilder();
        Cookie cookies[] = httpServletRequest.getCookies();
        if (cookies != null)
        {
            boolean first = true;
            for (int j = 0; j < cookies.length; j++)
            {
                if (this.sessionCookieNames.contains(cookies[j].getName().toUpperCase()))
                {
                    if (!first)
                    {
                        cookiesData.append(";");
                    }
                    first = false;
                    cookiesData.append(" ");
                    cookiesData.append(cookies[j].getName());
                    cookiesData.append("=");
                    cookiesData.append(cookies[j].getValue());
                }
            }
        }
        return cookiesData.toString();
    }

    @Override
    protected String createServerUrl(HttpServletRequest req_p)
    {
        if (overwriteServerUrl != null)
        {
            return overwriteServerUrl;
        }
        else
        {
            return super.createServerUrl(req_p);
        }
    }

    @Override
    protected String createBaseUrl(HttpServletRequest req_p)
    {
        if (overwriteBaseUrl != null)
        {
            return overwriteBaseUrl;
        }
        else
        {
            return super.createBaseUrl(req_p);
        }
    }

    public String getConfigPath()
    {
        try
        {
            URL url = getConfigURL("");
            return url.toString();
        }
        catch (Exception e)
        {
            LOG.error("Failed to read config path", e);
            throw new RuntimeException("Could not read config Path", e);
        }
    }

    @Override
    protected OwAOProvider createAOProvider() throws OwConfigurationException
    {
        return OwAOProviderFactory.getInstance().createProvider(getConfiguration(), this);
    }

    @Override
    public boolean isRoleManaged()
    {
        return true;
    }
}