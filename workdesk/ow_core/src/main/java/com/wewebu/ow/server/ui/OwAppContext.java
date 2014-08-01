package com.wewebu.ow.server.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.app.OwGlobalRegistryContext;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.servlets.OwServletBaseInitializer;
import com.wewebu.ow.server.ui.viewer.OwInfoProviderRegistry;
import com.wewebu.ow.server.util.OwRequestContext;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwTimeZoneInfo;

/**
 *<p>
 * Main Application Context Class Base Implementation. Instance stays active during session.
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
public abstract class OwAppContext extends OwEventTarget implements OwGlobalRegistryContext
{
    /** 
     * client time zone attribute name
     * @since 3.1.0.3
     */
    private static final String TIME_ZONE = "TIME_ZONE";

    /** 
     * the name of request attribute used to mark a request as an AJAX request
     * @since 3.1.0.0
     */
    public static final String AJAX_REQUEST_TYPE = "AJAX_REQUEST_TYPE";

    /**support to execute completely the java script, without adding default return*/
    public static final String FULLJS_MARKER = "fulljs:";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwAppContext.class);

    // === Keyboard definitions
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_RETURN = 13;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_ESC = 27;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F1 = 112;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F2 = 113;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F3 = 114;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F4 = 115;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F5 = 116;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F6 = 117;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F7 = 118;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F8 = 119;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F9 = 120;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F10 = 121;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F11 = 122;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_F12 = 123;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_INS = 45;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_DEL = 46;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_POS1 = 36;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_END = 35;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_UP = 38;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_DN = 40;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_LEFT = 37;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_RIGHT = 39;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_PAGE_UP = 33;
    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_PAGE_DN = 34;

    /** key board key ID for the registerKeyEvent function */
    public static final int KEYBOARD_KEY_SPACE = 32;

    /** control key ID */
    public static final int KEYBOARD_CTRLKEY_NONE = 0x0000;
    /** control key ID */
    public static final int KEYBOARD_CTRLKEY_SHIFT = 0x0100;
    /** control key ID */
    public static final int KEYBOARD_CTRLKEY_CTRL = 0x0200;
    /** control key ID */
    public static final int KEYBOARD_CTRLKEY_ALT = 0x0400;
    /** META control key ID, like the CMD on MacOS keyboard
     * @since 4.1.1.2*/
    public static final int KEYBOARD_CTRLKEY_META = 0x0800;

    /** name of the anchor which will be append on the URL */
    public static final String SELECTED_ANCHOR = "selected";

    // === general definitions
    /** predefined target ID for the main view */
    public static final String MAIN_VIEW_TARGET_ID = "ow_MainView";
    /** predefined target ID for the login view */
    public static final String LOGIN_VIEW_TARGET_ID = "ow_LoginView";

    /** default owappsec - used when random appsec generation is disabled */
    private static final String DEFAULT_OWAPPSEC = String.valueOf(6);

    public static final String TIME_ZONE_ID_PARAMETER_NAME = "tzID";
    public static final String TIME_ZONE_TRANSITIONS_PARAMETER_NAME = "tzDSTrans";
    public static final String TIME_ZONE_TIME_PARAMETER_NAME = "tzTime";
    public static final String TIME_ZONE_OFFSET_PARAMETER_NAME = "tzOffset";
    public static final String TIME_ZONE_DAYLIGHTSAVINGS_PARAMETER_NAME = "tzDS";
    public static final String TIME_ZONE_NORTHERN_HEMISPHERE_PARAMETER_NAME = "tzNHS";

    public static final String PREPARED_REQUEST_URL = "prepRqURL";

    /** Registry holding information about specific call and object.
     * Used for abstract registration of object into the context.
     * @since 3.2.0.0
     */
    private ConcurrentHashMap<Class<?>, Object> register;

    /** servlet context reference for this session */
    protected ServletContext m_ServletContext;

    /** the URL to the JSP page */
    private String m_strJSPPage;
    /** the URL to the base */
    private String m_strBaseURL;
    /** the path to the application */
    private String m_strBasePath;
    /** the URL to the server */
    private String m_sServerURL;

    /** 
     *The time zone information can be used  to set or guess the client's 
     *actual time zone or a client-compatible time zone.  
     *@since 3.1.0.3
     */
    private OwTimeZoneInfo clientTimeZoneInfo = null;

    /** event name query string key for normal application events */
    private static final String EVENT_NAME = "owappevn";

    /** target ID query string key for internal requests */
    public static final String INTERNAL_REQUEST_TARGET_ID = "owappiid";
    /** target ID query string key for external requests, used for remote control */
    public static final String EXTERNAL_REQUEST_TARGET_ID = "owappeid";

    /** security key query string key, see  m_strCurrentRequestSecurityKey */
    private static final String SECURITY_KEY = "owappsec";

    /** Key map, which holds the event targets */
    private HashMap<String, OwEventTarget> m_EventTargetMap = new HashMap<String, OwEventTarget>();
    /** Key map, which holds the request targets */
    private HashMap<String, OwEventTarget> m_RequestTargetMap = new HashMap<String, OwEventTarget>();

    /** save external query string for after login */
    private String m_ExternalRequestQueryString;

    /** security key generated for each request, to make sure the user can not reload a request or manipulate the query string */
    private String m_strCurrentRequestSecurityKey;

    /** count value which is count up each time a view is added. Used in registerTarget to generate a unique key */
    private int m_iKeyGenerator = 0;

    /** set of already rendered JavaScripts */
    private Set<String> m_jsincludes = new HashSet<String>();

    /** Random, to generate the CurrentRequestSecurityKey **/
    private final Random m_Random = new Random();

    // === key event register
    /** list of OwKeyEvent */
    private Set<OwKeyEvent> m_KeyBoardEvents;
    private Set<OwMouseEventDescription> m_mouseEventDescriptions = new HashSet<OwMouseEventDescription>();

    /** Creates a new instance of OwAppContext */
    public OwAppContext()
    {
        // Initially create new key
        generateNewCurrentRequestSecurityKey();
        register = new ConcurrentHashMap<Class<?>, Object>();
        registerInterface(OwGlobalRegistryContext.class, this);
    }

    /** get a Collection of Lists of registered OwKeyEvents.
     * NOTE: List is completely created only after onRender
     *
     * @return Collection, or null if no events have been defined
     */
    public Collection<OwKeyEvent> getKeyEvents()
    {
        return m_KeyBoardEvents;
    }

    /**
     * 
     * @return a collection of event descriptions (key and mouse)
     * @since 4.2.0.0
     */
    public synchronized Collection<? extends OwEventDescription> getEventDescriptions()
    {
        List<OwEventDescription> allDescriptions = new LinkedList<OwEventDescription>(m_mouseEventDescriptions);
        if (getKeyEvents() != null)
        {
            allDescriptions.addAll(0, getKeyEvents());
        }
        return allDescriptions;
    }

    /**
     * 
     * @param description
     * @throws OwException
     * @since 4.2.0.0
     */
    public synchronized void registerMouseAction(String actionId, OwString description) throws OwException
    {
        OwStaticMouseSettingsConfiguration settingsConfiguration = OwStaticMouseSettingsConfiguration.getInstance();
        OwMouseSetting keySetting = settingsConfiguration.getSetting(actionId);
        OwMouseAction mouseAction = keySetting.createAction(description);
        mouseAction.register(this);
    }

    /**
     * 
     * @param mouseEventDescription
     * @since 4.2.0.0
     */
    public synchronized void registerMouseEventDescription(OwMouseEventDescription mouseEventDescription)
    {
        m_mouseEventDescriptions.add(mouseEventDescription);
    }

    /** register a keyboard key to a event URL,
     *  the registered key is only valid for one page request, so it must be called in onRender
     *
     * @param iKeyCode_p int code of the keyboard key as returned by the JavaScript method event.keyCode
     * @param iCtrlKey_p int any combination of the KEYBOARD_CTRLKEY_... definitions, can be 0
     * @param strEventURL_p String URL to be requested upon key press
     * @param description_p String description of the event
     * @throws Exception
     */
    public void registerKeyEvent(int iKeyCode_p, int iCtrlKey_p, String strEventURL_p, String description_p) throws Exception
    {
        registerKeyFormEvent(iKeyCode_p, iCtrlKey_p, strEventURL_p, null, description_p);
    }

    /** register a keyboard key to a event URL,
     *  the registered key is only valid for one page request, so it must be called in onRender
     *
     * @param iKeyCode_p int code of the keyboard key as returned by the JavaScript method event.keyCode
     * @param iCtrlKey_p int any combination of the KEYBOARD_CTRLKEY_... definitions, can be 0
     * @param strEventURL_p String URL to be requested upon key press
     * @param strFormName_p String form name
     * @param description_p String description of the event
     */
    public synchronized void registerKeyFormEvent(int iKeyCode_p, int iCtrlKey_p, String strEventURL_p, String strFormName_p, String description_p)
    {
        registerKeyEvent(new OwKeyEvent(iKeyCode_p, iCtrlKey_p, strEventURL_p, strFormName_p, description_p));
    }

    /**
     * 
     * @param keyEvent
     * @since 4.1.1.0
     */
    public synchronized void registerKeyEvent(OwKeyEvent keyEvent)
    {
        if (m_KeyBoardEvents == null)
        {
            m_KeyBoardEvents = new LinkedHashSet<OwKeyEvent>();
        }

        m_KeyBoardEvents.add(keyEvent);
    }

    /**
     * 
     * @param action
     * @throws OwException
     * @since 4.1.1.0
     */
    public synchronized void registerKeyAction(OwKeyAction action) throws OwException
    {
        action.register(this);
    }

    /**
     * 
     * @param actionId
     * @param strEventURL
     * @param description
     * @throws OwException
     * @since 4.1.0.0
     */
    public synchronized void registerKeyAction(String actionId, String strEventURL, String description) throws OwException
    {
        registerKeyAction(actionId, strEventURL, null, description);
    }

    /**
     * 
     * @param actionId
     * @param strEventURL
     * @param strFormName
     * @param description
     * @throws OwException
     * @since 4.1.1.0
     */
    public synchronized void registerKeyAction(String actionId, String strEventURL, String strFormName, String description) throws OwException
    {
        OwKeySettingsConfiguration keySettingsConfiguration = OwStaticKeySettingsConfiguration.getInstance();
        OwKeySetting keySetting = keySettingsConfiguration.getSetting(actionId);
        OwKeyAction keyAction = keySetting.createAction(strEventURL, strFormName, description);

        keyAction.register(this);

    }

    /** activate view designated by the TargetID, i.e. make it visible by navigating to it and all its parents
     *  @param strTargetID_p Target ID of view to activate
     *  @throws Exception
     */
    public void activateView(String strTargetID_p) throws Exception
    {
        OwEventTarget Target = getEventTarget(strTargetID_p);
        if (Target != null && Target instanceof OwView)
        {
            ((OwView) Target).activate();
        }
    }

    /** get a reference to the default dialog manager used to open dialogs from the context in openDialog(...)
     *  The dialog manager must be created somewhere in the layout and returned here.
     *  If no dialogs are required, you do not need to create it and this function returns null
     *
     * @return OwDialogManager or null if no dialogs are required
     */
    public abstract OwDialogManager getDialogManager();

    /** closes all dialogs 
     *  @throws Exception*/
    public void closeAllDialogs() throws Exception
    {
        getDialogManager().closeAllDialogs();
    }

    /** display a new dialog using the dialog manager set in setDialogManager(...)
     * After it is closed it gets removed from the DialogManager and the context to finalize the Dialog View
     * @param dialogView_p Dialog-View of the new dialog
     * @param listener_p Listener that wants to be notified upon dialog events.
     * @throws Exception
     */
    public void openDialog(OwDialog dialogView_p, OwDialog.OwDialogListener listener_p) throws Exception
    {
        // open the dialog view via the default dialog manager
        getDialogManager().openDialog(dialogView_p, listener_p);
    }

    //private static boolean m_fLog4jInited = false;

    /** init the context BEFORE the user has logged on. Called once for a session.<br/>
    * NOTE: The user is not logged in, when this function is called.<br/>
    * Optionally set a prefix to distinguish several different applications.
    *
    * @param oldcontext_p OwAppContext old context from previous session, used to copy flags, locals. Optional, can be null
    * @throws Exception
    */
    public void init(OwAppContext oldcontext_p) throws Exception
    {
        // === register myself as event target
        attach(this, "OwAppContext");
    }

    /** retrieves the target count in the context.
     *  mostly used for debug reasons.
     *  @return number of attached event targets
     */
    public int getTargetCount()
    {
        return m_EventTargetMap.size();
    }

    /** get the current HTTP request
     * @return HttpServletRequest
     */
    public HttpServletRequest getHttpRequest()
    {
        return OwRequestContext.getLocalThreadRequest();
    }

    /** get the current HTTP response
     * @return HttpServletResponse
     */
    public HttpServletResponse getHttpResponse()
    {
        return OwRequestContext.getLocalThreadResponse();
    }

    /** get the current HHTP servlet context
     * @return ServletContext
     */
    public ServletContext getHttpServletContext()
    {
        return m_ServletContext;
    }

    /** get the current HTTP session
     * @return HttpSession
     */
    public HttpSession getHttpSession()
    {
        return OwRequestContext.getLocalThreadRequest().getSession();
    }

    /** flag indicating that session is pending, to prevent concurrent requests */
    private boolean m_fPendingSession = false;

    private OwBaseInitializer baseInitializer;

    /** signal that request has ended and to prevent OwPendingSessionException in beginRequest,
     * to prevent concurrent requests.
     */
    public void endRequest()
    {
        m_fPendingSession = false;
    }

    /** signal that request has started to prevent concurrent requests.
     *  @throws OwPendingSessionException if already a request was set
     */
    public void beginRequest() throws OwPendingSessionException
    {
        // === prevent concurrent requests
        if (m_fPendingSession)
        {
            throw new OwPendingSessionException("OwAppContext.beginRequest: Prevent concurrent requests.");
        }

        m_fPendingSession = true;
        clearEvents();
    }

    /** 
     * Creates a base initializer for a given context. Used for base initializer customization.
     * 
     * @param context_p  ServletContext
     * @since 4.0.0.0
     */
    protected OwBaseInitializer createBaseInitializer(ServletContext context_p)
    {
        return new OwServletBaseInitializer(context_p);
    }

    /** set the request parameters, called upon each request, MUST BE CALLED BEFORE INIT.
     *
     *   @param context_p  ServletContext
     *   @param request_p HttpServletRequest
     *   @param response_p HttpServletResponse
     *
     */
    public void setRequest(ServletContext context_p, HttpServletRequest request_p, HttpServletResponse response_p)
    {
        // === set request and session references for further use, e.g. serverSideInclude
        m_ServletContext = context_p;

        m_sServerURL = createServerUrl(request_p);

        if (!isAjaxRequest())
        {
            // path to the JSP page
            m_strJSPPage = createJspPageUrl(request_p);
            // base URL 
            m_strBaseURL = createBaseUrl(request_p);
        }

        baseInitializer = createBaseInitializer(context_p);

        m_strBasePath = baseInitializer.getBasePath();
    }

    /** 
     * Set the request associated with current thread.
     * @param request_p - the {@link HttpServletRequest} object
     * @since 3.1.0.0
     * @deprecated since 4.2.0.0 using OwRequestContext.setLocalThreadRequest instead, calling method will have no effect
     */
    @Deprecated
    public static void setLocalThreadRequest(HttpServletRequest request_p)
    {
    }

    /** 
     * Set the response associated with current thread.
     * @param response_p - the {@link HttpServletResponse} object
     * @since 3.1.0.0
     * @deprecated since 4.2.0.0 using OwRequestContext.setLocalThreadResponse instead, calling method will have no effect
     */
    @Deprecated
    public static void setLocalThreadResponse(HttpServletResponse response_p)
    {
    }

    /**
     *   Include the specified URL writing its output to the current Writer
     *   object.
     *
     *   @param  path_p - The path to the URL to include.
     *   @param  w_p    - The Writer which will be flushed prior to the include.
     *
     *   @throws Exception
     */
    public void serverSideInclude(String path_p, java.io.Writer w_p) throws Exception
    {
        if (w_p != null)
        {
            // Dump debug info
            w_p.write("\n<!-- Start JSP [File: ");
            w_p.write(path_p);
            w_p.write("] -->\n\n");

            w_p.flush(); //need to flush the writer for this to work properly
        }

        RequestDispatcher rd = m_ServletContext.getRequestDispatcher("/" + path_p);
        rd.include(OwRequestContext.getLocalThreadRequest(), OwRequestContext.getLocalThreadResponse());

        if (w_p != null)
        {
            // Dump debug info
            w_p.write("\n<!-- End JSP [File: ");
            w_p.write(path_p);
            w_p.write("] -->\n\n");
        }
    }

    /** get a parameter from the web.xml config file
     * @param strParamName_p Name of the requested parameter
     * @return parameter value, of null if not set.
     */
    public String getInitParameter(String strParamName_p)
    {
        return baseInitializer.getInitParameter(strParamName_p);
    }

    /** find the registered event target
     * @param strName_p Name (ID) of the event target
     * @return reference to the event target, or null if target could not be found.
     */
    public OwEventTarget getEventTarget(String strName_p)
    {
        return m_EventTargetMap.get(strName_p);
    }

    /** remove the event target from the context
     * @param target_p OwEventTarget to remove from the context
     */
    public void removeTarget(OwEventTarget target_p)
    {
        removeTarget(target_p.getID());
    }

    /** remove the event target from the context
     * @param strID_p String Target ID to remove
     */
    public void removeTarget(String strID_p)
    {
        // remove the target with the ID from the context target list
        m_EventTargetMap.remove(strID_p);
        // remove it from the request map as well
        m_RequestTargetMap.remove(strID_p);
    }

    /** registers a target to receive onRequest(...) events for each request
     *  the target must already be registered with registerTarget or OwEventTarget.attach
     *
     * @param target_p OwEventTarget
     * @throws OwException 
     */
    public void registerRequestTarget(OwEventTarget target_p) throws OwException
    {
        if (null == m_EventTargetMap.get(target_p.getID()))
        {
            throw new OwInvalidOperationException("Can not register request target, register as event target first: " + target_p.getID() + ", " + target_p.toString());
        }

        m_RequestTargetMap.put(target_p.getID(), target_p);
    }

    /** unregisters a target to receive onRequest(...) events for each request
     *  the target must already be registered with registerTarget or OwEventTarget.attach
     *
     * @param target_p OwEventTarget
     * @throws OwException 
     */
    public void unregisterRequestTarget(OwEventTarget target_p) throws OwException
    {
        if (null == m_EventTargetMap.get(target_p.getID()))
        {
            throw new OwInvalidOperationException("Can not unregister request target, register as event target first: " + target_p.getID() + ", " + target_p.toString());
        }

        m_RequestTargetMap.remove(target_p.getID());
    }

    /** register the event target in the context and assign a unique ID
     * @param target_p EventTarget to register
     * @param strName_p optional ID to identify the target. If null, the context create a ID.
     *
     * @return target ID key
     * @throws Exception
     */
    public String registerTarget(OwEventTarget target_p, String strName_p) throws Exception
    {
        String strKey = strName_p;

        if (strKey == null)
        {
            // generate new key
            m_iKeyGenerator++;

            strKey = String.valueOf(m_iKeyGenerator);
        }

        if (m_EventTargetMap.get(strKey) != null)
        {
            // === Key already registered
            throw new Exception("TargetKey already defined: " + strKey);
        }

        // add view to key map
        m_EventTargetMap.put(strKey, target_p);

        return strKey;
    }

    /** get the name of the JSP Page 
     * @return String*/
    public String getJSPPageURL()
    {
        return m_strJSPPage;
    }

    /** loads a XML Document either from local file, external file or from a JNDI context
     *
     * @param strName_p Name of the resource to look for
     *
     * @return OwXMLUtil wrapped DOM Node, or null if not found
     * @throws Exception 
     */
    public InputStream getXMLConfigDoc(String strName_p) throws Exception
    {
        return baseInitializer.getXMLConfigDoc(strName_p);
    }

    /** 
     * loads a URL for a configuration either from local file, 
     * external file or from a JNDI context
     * @param strName_p Name of the configuration to look for, e.g. owbootstrap.xml
     * @return URL current config
     * @throws Exception
     */
    public URL getConfigURL(String strName_p) throws Exception
    {
        return baseInitializer.getConfigURL(strName_p);
    }

    /**
     * (overridable)
     * Retrieves a resource for this application context.
     * @param resourcePath_p the class path subpath to retrieve 
     * @return the {@link URL} of the requested resource
     * @since 2.5.2.0
     */
    protected abstract URL getAppContextResource(String resourcePath_p);

    /** prefix to configure a subdir in the java class path (WEB-INF/classes or WEB-INF/lib) */
    public static final String RESOURCE_CLASSPATH_PREFIX = "lib#";

    /** prefix to configure a subdir in the deployment structure */
    public static final String RESOURCE_DEPLOYMENT_PREFIX = "deploy#";

    /** parameter name of the resource file path in web.xml */
    public static final String RESOURCE_FILE_PATH_PARAM = "OwResourceFilePath";

    /** parameter name of the resource file path environment var name in web.xml */
    public static final String RESOURCE_FILE_PATH_ENVIRONMENT_VAR_NAME_PARAM = "OwResourceFilePathEnvironmentVarName";

    /** prefix for config file / stream / JNDI names */
    public static final String CONFIG_STREAM_NAME_PREFIX = "ow";

    /** suffix for JNDI names */
    public static final String CONFIG_JNDI_SUFFIX = "JNDI";

    /** get the URL to the web application context root 
     * @return String */
    public String getBaseURL()
    {
        return m_strBaseURL;
    }

    /** get the URL to the web application server 
     * @return String*/
    public String getServerURL()
    {
        return m_sServerURL;
    }

    /** get the base path to the application
     * 
     * @return String
     */
    public String getBasePath()
    {
        return m_strBasePath;
    }

    /** create new security key generated for each request
     *  to make sure the user can not reload a request or manipulate the query string
     */
    private void generateNewCurrentRequestSecurityKey()
    {
        if (Boolean.getBoolean("owd.owappsec.disabled"))
        {
            m_strCurrentRequestSecurityKey = DEFAULT_OWAPPSEC;
        }
        else
        {
            m_strCurrentRequestSecurityKey = Integer.toHexString(m_Random.nextInt());
        }
    }

    /** get a URL for the requested event sending the form data defined in the event target with getFormName()
     *
     * @param target_p OwEventTarget to request the event URL for. Event is directed to that event target
     * @param strEventName_p Function name to be called upon event fired
     * @param strAdditionalParameters_p additional query string with parameters.
     * @param strFormName_p String form name to send data with
     *
     * @return String URL 
     */
    public String getFormEventURL(OwEventTarget target_p, String strEventName_p, String strAdditionalParameters_p, String strFormName_p)
    {
        return createSubmitLink(strFormName_p, getEventURL(target_p, strEventName_p, strAdditionalParameters_p));
    }

    /** get a script function for the requested event sending the form data defined in the event target with getFormName()
    *
    * @param target_p Owevent target to request the event URL for. Event is directed to that event target
    * @param strEventName_p Function name to be called upon event fired
    * @param strAdditionalParameters_p additional query string with parameters.
    * @param strFormName_p String form name to send data with
    *
    * @return String URL 
    */
    public String getFormEventFunction(OwEventTarget target_p, String strEventName_p, String strAdditionalParameters_p, String strFormName_p)
    {
        return createSubmitFunction(strFormName_p, getEventURL(target_p, strEventName_p, strAdditionalParameters_p));
    }

    /** get a URL for the requested AJAX event and redirect to submitted view
    *
    * @param target_p OwEventTarget to request the event URL for. Event is directed to that event target
    * @param strEventName_p Function name to be called upon event fired
    * @param strAdditionalParameters_p additional query string with parameters.
    *
    * @return String URL 
    */
    public String getAjaxEventURL(OwEventTarget target_p, String strEventName_p, String strAdditionalParameters_p)
    {
        StringBuilder buf = new StringBuilder(255);

        buf.append(getBaseURL());
        buf.append("/owajax.jsp");
        buf.append("?");
        buf.append(EVENT_NAME);
        buf.append("=");
        buf.append(strEventName_p);
        buf.append("&");
        buf.append(INTERNAL_REQUEST_TARGET_ID);
        buf.append("=");
        buf.append(target_p.getID());

        if (strAdditionalParameters_p != null)
        {
            buf.append("&");
            buf.append(strAdditionalParameters_p);
        }

        return buf.toString();
    }

    /** get a URL for the requested event and redirect to submitted view
     *
     * @param target_p OwEventTarget to request the event URL for. Event is directed to that event target
     * @param strEventName_p Function name to be called upon event fired
     * @param strAdditionalParameters_p additional query string with parameters.
     *
     * @return String URL 
     */
    public String getEventURL(OwEventTarget target_p, String strEventName_p, String strAdditionalParameters_p)
    {
        StringBuilder buf = new StringBuilder(getBaseEventURL(target_p, strEventName_p));

        // add external query string if available
        if (m_ExternalRequestQueryString != null)
        {
            buf.append("&");
            buf.append(m_ExternalRequestQueryString);
        }

        if (strAdditionalParameters_p != null)
        {
            buf.append("&");
            buf.append(strAdditionalParameters_p);
        }

        return buf.toString();
    }

    /** get a base URL with no additional parameters for the requested event and redirect to submitted view
    *
    * @param target_p OwEventTarget to request the event URL for. Event is directed to that event target
    * @param strEventName_p Function name to be called upon event fired
    *
    * @return StringBuffer URL 
    */
    protected StringBuffer getBaseEventURL(OwEventTarget target_p, String strEventName_p)
    {
        StringBuffer buf = new StringBuffer(255);

        buf.append(getJSPPageURL());
        buf.append("?");
        buf.append(EVENT_NAME);
        buf.append("=");
        buf.append(strEventName_p);
        buf.append("&");
        buf.append(INTERNAL_REQUEST_TARGET_ID);
        buf.append("=");
        buf.append(target_p.getID());
        buf.append("&");
        buf.append(SECURITY_KEY);
        buf.append("=");
        buf.append(m_strCurrentRequestSecurityKey);

        return buf;
    }

    /** get a URL for the requested event and redirect to submitted view
     *
     *  The URL is infinite, meaning that it can be used in all following requests
     *  where a normal event URL is only valid for the very next request.
     *
     * @param target_p OwEventTarget to request the event URL for. Event is directed to that event target
     * @param strEventName_p Function name to be called upon event fired
     * @param strAdditionalParameters_p additional query string with parameters.
     *
     * @return String URL 
    */
    public String getInfiniteEventURL(OwEventTarget target_p, String strEventName_p, String strAdditionalParameters_p)
    {
        StringBuilder buf = new StringBuilder(255);

        buf.append(getJSPPageURL());
        buf.append("?");
        buf.append(EVENT_NAME);
        buf.append("=");
        buf.append(strEventName_p);
        buf.append("&amp;");
        buf.append(INTERNAL_REQUEST_TARGET_ID);
        buf.append("=");
        buf.append(target_p.getID());

        // add external query string if available
        if (m_ExternalRequestQueryString != null)
        {
            buf.append("&amp;");
            buf.append(m_ExternalRequestQueryString);
        }

        if (strAdditionalParameters_p != null)
        {
            buf.append("&amp;");
            buf.append(strAdditionalParameters_p);
        }

        return buf.toString();
    }

    /**
     * handles the AJAX request and dispatches to the correct target
     *
     * @param request_p  HttpServletRequest
     * @param response_p  HttpServletResponse
     * @throws Exception
     */
    public void handleAjaxRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        String targetID = request_p.getParameter(INTERNAL_REQUEST_TARGET_ID);

        // === handle AJAX requests
        String ajaxEventName = request_p.getParameter(EVENT_NAME);

        if ((null != ajaxEventName) && (targetID != null))
        {
            // === direct AJAX event to target
            OwEventTarget target = m_EventTargetMap.get(targetID);
            if (target != null)
            {
                // === handler method
                java.lang.reflect.Method method = target.getClass().getMethod("onAjax" + ajaxEventName, new Class[] { HttpServletRequest.class, HttpServletResponse.class });
                method.invoke(target, new Object[] { request_p, response_p });
            }
        }
    }

    /** handles the JSP request from the page and dispatches to the correct target
     *
     * @param request_p  HttpServletRequest
     * @param response_p  HttpServletResponse
     *
     * @return boolean true = continue with request and render, false = request is already finished, return to client
     * @throws Exception
     */
    public boolean handleRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // === refresh render JSP includes
        m_jsincludes = new HashSet<String>();

        // === handle onRequest first
        Iterator<?> it = m_RequestTargetMap.values().iterator();
        while (it.hasNext())
        {
            if (!((OwEventTarget) it.next()).onRequest(request_p, response_p))
            {
                return false;
            }
        }

        // === check for external requests, used for remote control
        if (isExternalTargetRequest(request_p))
        {
            if (handleExternalTargetRequest(request_p, response_p))
            {
                return true;
            }
        }

        // === first check the security key
        String strSecKey = request_p.getParameter(SECURITY_KEY);
        if ((strSecKey != null) && (!strSecKey.equals(m_strCurrentRequestSecurityKey)))
        {
            // request is invalid, ignore it
            return true;
        }

        // === handle request, dispatch to event targets
        String eventName = request_p.getParameter(EVENT_NAME);
        String targetID = request_p.getParameter(INTERNAL_REQUEST_TARGET_ID);

        ////////////////////////////////////////////////
        // DEBUG: set variable in context for Dump(...) function.
        m_DEBUG_current_request_TargetID = targetID;

        if ((eventName != null) && (targetID != null))
        {
            // === direct event to target
            OwEventTarget Target = m_EventTargetMap.get(targetID);
            if (Target != null)
            {
                // === call form method first
                if (isFormRequest(request_p))
                {
                    Target.onFormEvent(request_p);
                }

                // === handler method
                java.lang.reflect.Method method = Target.getClass().getMethod("on" + eventName, new Class[] { HttpServletRequest.class });
                method.invoke(Target, new Object[] { request_p });
            }
        }

        // === after the request was handled create new security key
        generateNewCurrentRequestSecurityKey();

        return true;
    }

    /**
     * Method which executes external target request, and return a
     * result if the external target request was processed successful or not.
     * <p><b>Note</b>: This method does not check the security token of the request,
     * and must be processed before calling this method.</p>
     * @param request_p HttpServletRequest which contains information for external target processing 
     * @param response_p HttpServletResponse to use if the external target request is processed
     * @return boolean true only if the request was processed successfully
     * @throws Exception if session is in unstable state, or the external target cannot process current request
     * @throws OwInvalidOperationException if the external target cannot be found
     * @since 3.2.0.0
     */
    protected boolean handleExternalTargetRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    { // === an external request was made, someone wants to remote control the application
        if (isLogin())
        {
            String strExternalRequestTargetID = request_p.getParameter(EXTERNAL_REQUEST_TARGET_ID);
            if (m_EventTargetMap.size() > 3)
            {
                // === logged in, handle the external request
                try
                {
                    OwEventTarget target = m_EventTargetMap.get(strExternalRequestTargetID);
                    if (target != null)
                    {
                        target.onExternalRequest(request_p, response_p);
                    }
                    else
                    {
                        throw new OwInvalidOperationException(OwString.localize1(getLocale(), "app.OwAppContext.InvalidExternalRequest",
                                "OwAppContext.handleRequest: External Event Handler, targetId = '%1' not defined or not allowed in RoleManager.", strExternalRequestTargetID));
                    }
                }
                finally
                { // clear external query string, it is handled now
                    m_ExternalRequestQueryString = null;
                }
                return true;
            }
            else
            {
                /* seems to be an SSO environment login done 
                 * but context is not initialized yet*/
                return false;
            }
        }
        else
        {
            // === not yet logged in, save query string for after login
            if (m_ExternalRequestQueryString == null)
            {
                m_ExternalRequestQueryString = request_p.getQueryString();
                /*This is a workaround for WebSphere 6.1, getQueryString() returns null if a forward was done! 
                 * So we use the Servlet 2.4 standard, where getAttribute(...) should contains the query, too.*/
                if (m_ExternalRequestQueryString == null)
                {
                    m_ExternalRequestQueryString = (String) request_p.getAttribute("javax.servlet.forward.query_string");
                }
            }
            return false;
        }
    }

    /**
     * Returns whether this request carries FORM data and thus the <code>onFormEvent()</code>
     * of the target has to be invoked or not.
     * 
     * @param request_p the <code>HttpServletRequest</code> object that might carry FORM data
     * 
     * @return true if and only if this request carries FORM data, false otherwise
     */
    protected boolean isFormRequest(HttpServletRequest request_p)
    {
        return request_p.getMethod().equalsIgnoreCase("post");
    }

    /** causes all attached documents to receive an update event
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @throws Exception
     */
    public void broadcast(OwEventTarget caller_p, int iCode_p) throws Exception
    {
        Iterator it = m_EventTargetMap.values().iterator();
        while (it.hasNext())
        {
            try
            {
                // call update method on registered documents
                OwDocument doc = (OwDocument) it.next();
                doc.update(caller_p, iCode_p, null);
            }
            catch (ClassCastException e)
            {
                // ignore
            }
        }
    }

    /** the ID of a HTML element to receive focus upon page load */
    private String m_strFocusElementID;

    /** set the ID of a HTML element to receive focus upon page load
     *
     * @param strElementID_p String ID of HTML element
     */
    public void setFocusControlID(String strElementID_p)
    {
        m_strFocusElementID = strElementID_p;
    }

    /** get the ID of a control that should receive the input focus for this request
     *
     * @return String with HTML control ID 
     */
    public String getFocusControlID()
    {
        return m_strFocusElementID;
    }

    /** clear the focus control ID for the request
     *
     *
     */
    public void clearFocusControlID()
    {
        m_strFocusElementID = null;
    }

    @Deprecated
    /** render the keyboard script part of the HTML page, include additional scripts to be performed at the page end
     * @param w_p Writer object to write HTML to
     * @throws Exception
     * @deprecated since 4.0.0.0 will now be handled externally through specific view OwKeyboardScriptsRenderer.jsp
     */
    public void renderKeyBoardScript(Writer w_p) throws Exception
    {
        // === render key event script
        if ((m_KeyBoardEvents != null))
        {
            w_p.write("<script type=\"text/javascript\">\n");
            w_p.write("window.document.onkeydown = onOwKeyEvent;\n");
            w_p.write("function onOwKeyEvent(e)\n");
            w_p.write("{\n"); // begin function

            w_p.write(" if ( ! e ) e = event;\n");

            w_p.write("var iMaskedCode =  e.keyCode + (e.altKey ? ");
            w_p.write(Integer.toString(KEYBOARD_CTRLKEY_ALT));
            w_p.write(" : 0) + (e.ctrlKey ? ");
            w_p.write(Integer.toString(KEYBOARD_CTRLKEY_CTRL));
            w_p.write(" : 0) + (e.shiftKey ? ");
            w_p.write(Integer.toString(KEYBOARD_CTRLKEY_SHIFT));
            w_p.write(" : 0);\n");
            w_p.write("switch ( iMaskedCode )\n");

            w_p.write("{\n"); // begin switch

            Iterator<OwKeyEvent> it = m_KeyBoardEvents.iterator();
            while (it.hasNext())
            {
                OwKeyEvent keyevent = it.next();

                w_p.write("case ");
                w_p.write(Integer.toString(keyevent.getMaskedCode()));

                w_p.write(":\n{\n\t"); // begin case

                // === trigger to registered URL 
                if (keyevent.m_strEventURL != null && keyevent.m_strEventURL.startsWith(FULLJS_MARKER))
                {
                    StringBuilder buffer = new StringBuilder(keyevent.m_strEventURL);
                    buffer.delete(0, FULLJS_MARKER.length());
                    w_p.write("var retVal = ");
                    w_p.write(buffer.toString().substring(11));
                    w_p.write("\n;");
                    w_p.write("if (retVal != null && retVal === false) {\n");
                    w_p.write("e.cancelBubble = true;\n");
                    w_p.write("}\n");
                }
                else
                {

                    if (keyevent.m_strFormName != null)
                    {
                        w_p.write("document.");
                        w_p.write(keyevent.m_strFormName);
                        w_p.write(".action=\"");
                        w_p.write(keyevent.m_strEventURL);
                        w_p.write("\";\ndocument.");
                        w_p.write(keyevent.m_strFormName);
                        w_p.write(".submit();\n");
                    }
                    else
                    {
                        if (keyevent.m_strEventURL.startsWith("javascript:"))
                        {
                            w_p.write("var retVal = ");
                            w_p.write(keyevent.m_strEventURL.substring(11));
                            w_p.write("\n;");
                            w_p.write("if (retVal != null && retVal === false) {\n");
                            w_p.write("e.cancelBubble = true;\n");
                            w_p.write("}\n");
                        }
                        else
                        {
                            w_p.write("navigateHREF(window,\"");
                            w_p.write(keyevent.m_strEventURL);
                            w_p.write("\");\n");
                        }
                    }
                    // event was handled, so return false
                    w_p.write("return false;\n");
                }
                w_p.write("}"); // end case
                w_p.write("break;\n");
            }
            w_p.write("}\n"); // end switch

            // event was not handled, so return true
            w_p.write("return true;\n");
            w_p.write("}\n"); // end function
            w_p.write("</script>\n");
        }

    }

    private void clearEvents()
    {
        // clear map for next page load events need to be registered again
        m_KeyBoardEvents = null;
        m_mouseEventDescriptions.clear();
    }

    // === localization functions
    /** localizes a string
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     *
     * @return String localized strText_p
     */
    public String localize(String strKey_p, String strText_p)
    {
        return OwString.localize(getLocale(), strKey_p, strText_p);
    }

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     *
     * @return String localized strText_p
     */
    public String localize1(String strKey_p, String strText_p, String strAttribute1_p)
    {
        return OwString.localize1(getLocale(), strKey_p, strText_p, strAttribute1_p);
    }

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     * @param strAttribute2_p String that replaces %2 tokens
     *
     * @return String localized strText_p
     */
    public String localize2(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p)
    {
        return OwString.localize2(getLocale(), strKey_p, strText_p, strAttribute1_p, strAttribute2_p);
    }

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     * @param strAttribute2_p String that replaces %2 tokens
     * @param strAttribute3_p String that replaces %3 tokens
     *
     * @return String localized strText_p
     */
    public String localize3(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p, String strAttribute3_p)
    {
        return OwString.localize3(getLocale(), strKey_p, strText_p, strAttribute1_p, strAttribute2_p, strAttribute3_p);
    }

    /** encodes a URL or part of an URL so it conforms to HTTP standard (application/x-www-form-URL-encoded MIME format)
     * <p>Attention: This method was changed due to problems with links calling Active-X object using JavaScript.
     * The method now returns a String where all &lt;space&gt;-chars are replaced with the UTF-8 encoding
     * sequence %20!</p> 
     * <p>As example the String "http://server:port/myfolder/this is my.txt" is converted into
     * "http://server:port/myfolder/this<b>%20</b>is<b>%20</b>my.txt" and 
     * not "http://server:port/myfolder/this+is+my.txt"</p>
     * @param strURL_p String unencoded URL 
     *
     * @return String encoded URL 
     * @throws UnsupportedEncodingException
     */
    public static String encodeURL(String strURL_p) throws UnsupportedEncodingException
    {
        if (strURL_p == null)
        {
            return null;
        }
        else
        {
            return java.net.URLEncoder.encode(strURL_p, "UTF-8").replaceAll("[:+:]", "%20");
        }
    }

    /** decodes a URL or part of an URL, that was previously encoded using encodeURL.
     * <p>This decode the <b>strURL_p</b> and is equal to the String which was
     *  used for encodeURL!</p>
     * <p><code>String aURL.equals(decodeURL(encodeURL(aURL)) == true</code></p>
     * @param strURL_p String encoded URL 
     *
     * @return String decoded URL 
     * @throws UnsupportedEncodingException
     */
    public static String decodeURL(String strURL_p) throws UnsupportedEncodingException
    {
        if (strURL_p == null)
        {
            return null;
        }
        else
        {
            return java.net.URLDecoder.decode(strURL_p.replaceAll("%20", "+"), "UTF-8");
        }
    }

    /**
     * creates  a href link to fire a form submit
     * event URL is enclosed in single apostrophe
     *
     * @param formName_p
     * @param eventUrl_p

     * @return String form submit URL 
     */
    protected static String createSubmitLink(String formName_p, String eventUrl_p)
    {
        StringBuilder jsFunctionString = new StringBuilder();
        jsFunctionString.append("javascript:document.");
        jsFunctionString.append(formName_p);
        jsFunctionString.append(".action='");
        jsFunctionString.append(eventUrl_p);
        jsFunctionString.append("';document.");
        jsFunctionString.append(formName_p);
        jsFunctionString.append(".submit();");
        return jsFunctionString.toString();
    }

    /**
     * creates  JavaSript statements to fire a form submit
     * event URL is enclosed in single apostrophe
     *
     * @param formName_p
     * @param eventUrl_p

     * @return form submit URL 
     */
    protected static String createSubmitFunction(String formName_p, String eventUrl_p)
    {
        StringBuilder jsFunctionString = new StringBuilder();
        jsFunctionString.append("document.");
        jsFunctionString.append(formName_p);
        jsFunctionString.append(".action='");
        jsFunctionString.append(eventUrl_p);
        jsFunctionString.append("';document.");
        jsFunctionString.append(formName_p);
        jsFunctionString.append(".submit();");
        return jsFunctionString.toString();
    }

    // === abstract functions to be implemented by derived class
    /** get a name for the current session,
     *  used for warning and error logs to identify the session later.
     *  Do not mix up with session identifier from HTTP session.
     *
     * @return String name of Session, usually the name of the logged on user
     */
    public abstract String getSessionDisplayName();

    /** get the logged in user
     *
     * @return OwBaseUserInfo or null if no user is logged in
     */
    public OwBaseUserInfo getUserInfo()
    {
        return null;
    }

    /** get the current locale,
     * which can be used as a prefix/postfix to distinguish localization resources
     *
     * @return Locale
     */
    public abstract Locale getLocale();

    public abstract void setLocale(java.util.Locale locale_p);

    /** get the directory of the used design.
     *  Specifies the subfolder /designs/<DesignName> where to retrieve the design files. i.e. CSS, images, layouts...
     *  This function can be used to make the look & feel dependent on the logged in user.
     *  @return String dir of design directory
     *  @throws Exception
     */
    public abstract String getDesignDir() throws Exception;

    /** get the URL of the used design.
     *  Specifies the URL <server>/designs/<DesignName> where to retrieve the design files. i.e. css, images, layouts...
     *  This function can be used to make the look & feel dependent on the logged in user.
     *  @return String URL to the design dir
     *  @throws Exception
     */
    public abstract String getDesignURL() throws Exception;

    /** check the valid credentials of the logged in user
     * @return true if user is logged in and has valid credentials
     * @throws Exception
     */
    public abstract boolean isLogin() throws Exception;

    /** check the current user is authenticated container based
     * @return true if user is logged in and the login is done with container based authentication
     * @throws Exception
     */
    public abstract boolean isContainerBasedAuthenticated() throws Exception;

    /**
     * clear the credentials, i.e. log the user off
     * @throws Exception
     */
    public void logout() throws Exception
    {
        // === invalidate the session
        HttpServletRequest request = OwRequestContext.getLocalThreadRequest();
        OwWebApplication.invalidateSessionObjects(request);
        HttpSession session = request.getSession(true);
        session.setAttribute(TIME_ZONE, getClientTimeZone());
    }

    /** init the context AFTER the user has logged in. Called once for a session.
     *
     *  NOTE: This function is called only once after login to do special initialization,
     *        which can only be performed with valid credentials.
     * @throws Exception
     */
    public abstract void loginInit() throws Exception;

    /**
     *<p>
     * View reference for the dynamically changing dialog manager.
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
    public static class OwDialogManagerViewReference implements OwBaseView
    {
        /** reference to the context */
        private OwAppContext m_context;

        /** create a dialog manager view reference that delegates the region to the currently active dialogmanger view */
        public OwDialogManagerViewReference(OwAppContext context_p)
        {
            m_context = context_p;
        }

        public boolean isNamedRegion(String strRegion_p) throws Exception
        {
            try
            {
                return m_context.getDialogManager().isNamedRegion(strRegion_p);
            }
            catch (NullPointerException e)
            {
                LOG.error("No dialogmanager available, probably due to missing masterplugins.", e);
                return false;
            }
        }

        public boolean isRegion(int iRegion_p) throws Exception
        {
            try
            {
                return m_context.getDialogManager().isRegion(iRegion_p);
            }
            catch (NullPointerException e)
            {
                LOG.error("No dialogmanager available, probably due to missing masterplugins.", e);
                return false;
            }
        }

        public void render(Writer w_p) throws Exception
        {
            try
            {
                m_context.getDialogManager().render(w_p);
            }
            catch (NullPointerException e)
            {
                LOG.error("No dialogmanager available, probably due to missing masterplugins.", e);
            }
        }

        public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
        {
            try
            {
                m_context.getDialogManager().renderNamedRegion(w_p, strRegion_p);
            }
            catch (NullPointerException e)
            {
                LOG.error("No dialogmanager available, probably due to missing masterplugins.", e);
            }
        }

        public void renderRegion(Writer w_p, int iRegion_p) throws Exception
        {
            try
            {
                m_context.getDialogManager().renderRegion(w_p, iRegion_p);
            }
            catch (NullPointerException e)
            {
                LOG.error("No dialogmanager available, probably due to missing masterplugins.", e);
            }
        }

        public String getTitle()
        {
            if (m_context.getDialogManager() == null)
            {
                return OwBaseView.EMPTY_STRING;
            }
            else
            {
                return m_context.getDialogManager().getTitle();
            }
        }

        public String getBreadcrumbPart()
        {
            return getTitle();
        }
    }

    /** return a view reference for the dynamically changing dialog manager
     * 
     * @return OwBaseView reference that can be attached to another view with addViewReference
     */
    public OwBaseView getDialogManagerViewReference()
    {
        return new OwDialogManagerViewReference(this);
    }

    ////////////////////////////////////////////////////////////////////
    // DEBUG AND DUMP FUNCTIONS, CAN BE REMOVED IN PRODUCTIVE ENVIRONMENT
    ////////////////////////////////////////////////////////////////////
    /** debug variable holds the current target ID, used in Dump(...) */
    private String m_DEBUG_current_request_TargetID = "";

    /** debug variable contains the ID of the last activated view via a OwNavigationView event. */
    public String m_DEBUG_ActivateViewID = "";

    /** debug variable contains the ID of the view last called via a OwNavigationView event. */
    public String m_DEBUG_NavigationTargetID = "";

    /** debug variable contains the event name last called via a OwNavigationView event. */
    public String m_DEBUG_NavigationEvent = "";

    /** write debug dump to Writer. MUST BE DISABLED IN PRODUCTIVE ENVIRONMENT
     * @param w_p HTML Writer
     * @throws Exception
     */
    public void dump(Writer w_p) throws Exception
    {
        // === Dump session
        w_p.write("<table cellspacing='0' cellpadding='0' border='1' width='100%'>");

        w_p.write("<tr bgcolor='#aaaaaa'><td class='OwDump'>");

        w_p.write("<b>Session</b>");

        w_p.write("</td></tr>");

        w_p.write("<tr><td class='OwDump'>");

        w_p.write("<b>Designdir:</b> " + getDesignDir());
        w_p.write(", <b>BaseURL:</b> " + getBaseURL());
        w_p.write(", <b>JSPPageURL:</b> " + getJSPPageURL());

        w_p.write("<br>");

        HttpSession Session = OwRequestContext.getLocalThreadRequest().getSession();
        w_p.write("<b>Session.isNew:</b> " + Boolean.valueOf(Session.isNew()).toString());
        w_p.write(", <b>Session.getId:</b> " + Session.getId());
        w_p.write(", <b>Session.getCreatenTime:</b> " + new Date(Session.getCreationTime()).toString());

        w_p.write("</td></tr>");

        w_p.write("</table>");

        // === Dump DialogManager
        if (getDialogManager() != null)
        {
            w_p.write("\n<table cellspacing='0' cellpadding='0' border='1' width='100%'>\n");

            w_p.write("<tr bgcolor='#aaaaaa'><td class='OwDump'>");

            w_p.write("<b>DialogManager</b>");

            w_p.write("</td></tr>\n");

            w_p.write("</table>");

            w_p.write("\n<table cellspacing='0' cellpadding='0' border='1' width='100%'>");

            // iterate over children
            Iterator itDialog = getDialogManager().getIterator();
            while (itDialog.hasNext())
            {
                OwDialog Dialog = (OwDialog) itDialog.next();
                w_p.write("<tr><td class='OwDump'>");

                w_p.write("<b>" + Dialog.getClass().toString() + "</b> - ID: " + Dialog.getID());

                if (Dialog.getDocument() != null)
                {
                    w_p.write(" Doc: " + Dialog.getDocument().toString());
                }

                w_p.write("</td></tr>");

            }

            w_p.write("</table>");
        }

        // === dump targets
        w_p.write("\n<table cellspacing='0' cellpadding='0' border='1' width='100%'>\n");

        w_p.write("<tr bgcolor='#aaaaaa'><td class='OwDump'>");

        w_p.write("<b>Target Count: " + String.valueOf(m_EventTargetMap.size()) + "</b>");
        /*
                        w_p.write("<br>Constructor Calls: " + String.valueOf(OwEventTarget.m_iDEBUG_ConstructorCalls));
                        w_p.write("<br>Destructor Calls: " + String.valueOf(OwEventTarget.m_iDEBUG_DestructorCalls));
                        w_p.write("<br>Active EventTargets: " + String.valueOf(OwEventTarget.m_iDEBUG_ConstructorCalls-OwEventTarget.m_iDEBUG_DestructorCalls));
        */

        w_p.write("</td></tr>\n");

        w_p.write("</table>");

        w_p.write("\n<table cellspacing='0' cellpadding='0' border='1' width='100%'>\n");

        w_p.write("<tr bgcolor='#aaaaaa'>");

        w_p.write("<td class='OwDump' bgcolor='#ff6666'>Request Target: " + m_DEBUG_current_request_TargetID + "</td>");
        w_p.write("<td class='OwDump' bgcolor='#66ff66'>Activated View: " + m_DEBUG_ActivateViewID + "</td>");
        w_p.write("<td class='OwDump' bgcolor='#6666ff'>Navigation Target: " + m_DEBUG_NavigationTargetID + " : <b>" + m_DEBUG_NavigationEvent + "</b></td>");

        w_p.write("</tr>\n");

        w_p.write("</table>");

        w_p.write("\n<table cellspacing='0' cellpadding='0' border='1' width='100%'>");

        Iterator it = m_EventTargetMap.values().iterator();

        int iRowToggle = 0;

        w_p.write("\n<tr>");

        while (it.hasNext())
        {
            if (iRowToggle++ % 3 == 0)
            {
                w_p.write("</tr>\n<tr>");
            }

            OwEventTarget Target = (OwEventTarget) it.next();

            // mark view with current event
            if ((null != m_DEBUG_current_request_TargetID) && m_DEBUG_current_request_TargetID.equals(Target.getID()))
            {
                w_p.write("<td class='OwDump' bgcolor='#ff6666'>");
            }
            else if (m_DEBUG_ActivateViewID.equals(Target.getID()))
            {
                w_p.write("<td class='OwDump' bgcolor='#66ff66'>");
            }
            else if (m_DEBUG_NavigationTargetID.equals(Target.getID()))
            {
                w_p.write("<td class='OwDump' bgcolor='#6666ff'>");
            }
            else
            {
                w_p.write("<td class='OwDump'>");
            }

            w_p.write(Target.getID());
            w_p.write("<br>" + Target.getClass().toString());

            // if ( Target instanceof OwView )
            //   w_p.write("<br>" + ((OwView)Target).getDocument().getClass().toString());
            w_p.write("</td>");
        }

        w_p.write("</tr>");

        w_p.write("</table>\n\n");

        // === Dump ViewTree
        w_p.write("\n<table cellspacing='0' cellpadding='0' border='1' width='100%'>\n");

        w_p.write("<tr bgcolor='#aaaaaa'><td class='OwDump'>");

        w_p.write("<b>View Tree</b>");

        w_p.write("</td></tr>\n");

        w_p.write("</table>");

        w_p.write("\n<table cellspacing='0' cellpadding='0' border='1' width='100%'>");

        // get top view
        OwView MainView = (OwView) m_EventTargetMap.get(MAIN_VIEW_TARGET_ID);

        DumpViewNode(w_p, MainView, 0);

        w_p.write("</table>");

        // clear dump variables
        m_DEBUG_current_request_TargetID = "";
        m_DEBUG_ActivateViewID = "";
        m_DEBUG_NavigationTargetID = "";
        m_DEBUG_NavigationEvent = "";
    }

    /** recursively dump the views as a tree
     * @param w_p HTML Writer
     * @param view_p View to dump
     * @param iTab_p distance from left for this node
     * @throws Exception
     */
    private boolean DumpViewNode(Writer w_p, OwView view_p, int iTab_p) throws Exception
    {
        if (view_p != null)
        {
            w_p.write("<tr>");

            // mark view with current event
            if ((null != m_DEBUG_current_request_TargetID) && m_DEBUG_current_request_TargetID.equals(view_p.getID()))
            {
                w_p.write("<td class='OwDump' bgcolor='#ff6666'>");
            }
            else if (m_DEBUG_ActivateViewID.equals(view_p.getID()))
            {
                w_p.write("<td class='OwDump' bgcolor='#66ff66'>");
            }
            else if (m_DEBUG_NavigationTargetID.equals(view_p.getID()))
            {
                w_p.write("<td class='OwDump' bgcolor='#6666ff'>");
            }
            else
            {
                w_p.write("<td class='OwDump'>");
            }

            for (int i = 0; i < iTab_p; i++)
            {
                w_p.write("&nbsp;");
            }

            w_p.write("<b>" + view_p.getClass().toString() + "</b> - ID: " + view_p.getID());

            if (view_p.getDocument() != null)
            {
                w_p.write(" Doc: " + view_p.getDocument().toString());
            }

            w_p.write("</td></tr>");

            // iterate over children
            Iterator it = view_p.getIterator();
            while (it.hasNext())
            {
                DumpViewNode(w_p, (OwView) it.next(), iTab_p + 5);
            }

            return true;
        }
        else
        {
            return false;
        }

    }

    /** render a JavaScript include link. Makes sure that links are not rendered twice in a page
     *
     * @param sJSPath_p String
     * @param w_p
     * @throws IOException
     */
    public void renderJSInclude(String sJSPath_p, Writer w_p) throws IOException
    {
        if (!m_jsincludes.contains(sJSPath_p))
        {
            w_p.write("\n<script type='text/javascript' src='");
            w_p.write(getBaseURL());
            w_p.write(sJSPath_p);
            w_p.write("'></script>\n");

            m_jsincludes.add(sJSPath_p);
        }

    }

    /**
     * Used to include ExtJs scripts, in production or debug mode
     * @param sJSPath_p - the JavaScript file path
     * @param useDebug_p - if <code>true</code> try to include the "debug" version of given file. 
     * If such a file is not available, the given file is included.
     * @param w_p - the writer
     * @throws IOException
     * @since 3.0.0.0
     */
    public void renderJSInclude(String sJSPath_p, boolean useDebug_p, Writer w_p) throws IOException
    {
        String path = sJSPath_p.toLowerCase();
        if (useDebug_p)
        {
            StringBuilder buff = new StringBuilder(path);
            int position = buff.lastIndexOf(".js");
            if (position != -1)
            {
                buff.insert(position, "-debug");
            }
            path = buff.toString();
        }
        if (!m_jsincludes.contains(path))
        {
            w_p.write("\n<script type='text/javascript' src='");
            w_p.write(getBaseURL());
            w_p.write(path);
            w_p.write("'></script>\n");

            m_jsincludes.add(path);
        }
        if (useDebug_p && m_jsincludes.contains(sJSPath_p))
        {
            m_jsincludes.remove(sJSPath_p);
        }

    }

    /** get a attribute from the application scope
     * 
     * @param key_p
     * @return an {@link Object}
     */
    public Object getApplicationAttribute(String key_p)
    {
        return baseInitializer.getApplicationAttribute(key_p);
    }

    /** get a attribute from the application scope
     * 
     * @param key_p
     * @param object_p null removes the attribute
     * @return the previous object
     */
    public Object setApplicationAttribute(String key_p, Object object_p)
    {
        return baseInitializer.setApplicationAttribute(key_p, object_p);
    }

    /** 
     * Set a marking attribute on the current {@link HttpServletRequest} object.
     * @param isAjaxRequest_p - flag indicating that current {@link HttpServletRequest} object is an AJAX request.
     * @since 3.1.0.0 
     */
    public void setAjaxRequest(boolean isAjaxRequest_p)
    {
        HttpServletRequest request = OwRequestContext.getLocalThreadRequest();
        if (request == null)
        { //TODO localize this
            throw new IllegalStateException("We don't have a request associated with current thread.");
        }
        else
        {
            request.setAttribute(AJAX_REQUEST_TYPE, Boolean.valueOf(isAjaxRequest_p));
        }
    }

    /**
     * Check if the current {@link HttpServletRequest} object is an AJAX request.
     * @return - <code>true</code> if the current request is an AJAX request.
     * @since 3.1.0.0
     */
    public boolean isAjaxRequest()
    {
        boolean result = false;
        HttpServletRequest request = OwRequestContext.getLocalThreadRequest();
        if (request != null)
        {
            Boolean requestType = (Boolean) request.getAttribute(AJAX_REQUEST_TYPE);
            if (requestType != null)
            {
                result = requestType.booleanValue();
            }
        }
        return result;
    }

    /**
     * Get a info provider registry which is used to 
     * request information provider for special context.
     * @return OwInfoProviderRegistry
     * @since 3.1.0.0
     */
    public abstract OwInfoProviderRegistry getInfoProviderRegistry();

    /**
     * Check if the request contains an external target id.
     * This method is used to analyze if remote control links
     * are used and need to be processed.
     * @param request_p HttpServletRequest
     * @return boolean true if request contains an external target parameter 
     * @since 3.2.0.0
     */
    public boolean isExternalTargetRequest(HttpServletRequest request_p)
    {
        return request_p.getParameter(EXTERNAL_REQUEST_TARGET_ID) != null;
    }

    /**
     * Set client's time zone information. The time zone information can be used 
     * to set or guess the client's actual time zone or a client-compatible time zone.  
     * @param timeZoneInfo_p
     * @since 3.1.0.3
     */
    public void setClientTimeZoneInfo(OwTimeZoneInfo timeZoneInfo_p)
    {
        this.clientTimeZoneInfo = timeZoneInfo_p;
        TimeZone timeZone = this.clientTimeZoneInfo.getTimeZone();
        if (timeZone == null)
        {
            LOG.error("OwMainAppContext.setClientTimeZoneInfo : Unknown time zone for current client time zone information " + this.clientTimeZoneInfo);
        }
    }

    /**
     * Get client's time zone offset information.
     * The time zone information can be used  to set or guess the client's 
     * actual time zone or a client-compatible time zone.  
     * @return the current timezone information or <code>null</code> if no client time zone 
     *         information is set 
     * @since 3.1.0.3
     */
    public OwTimeZoneInfo getClientTimeZoneInfo()
    {
        return this.clientTimeZoneInfo;
    }

    /**
     * 
     * @return the current time zone as indicated by the current client time zone information. 
     *         If the current client time zone information does not indicate a time zone the 
     *         default time zone is returned using {@link TimeZone#getDefault()}.
     *@since 3.1.0.3
     */
    public TimeZone getClientTimeZone()
    {
        OwTimeZoneInfo zoneInfo = this.clientTimeZoneInfo;

        if (zoneInfo == null)
        {
            zoneInfo = new OwTimeZoneInfo();
        }

        TimeZone timeZone = null;
        timeZone = zoneInfo.getTimeZone();

        if (timeZone == null)
        {
            timeZone = TimeZone.getDefault();
        }

        return timeZone;
    }

    /**
     * Request precondition handler for all JSP based requests.
     * 
     * @param request_p
     * @param response_p
     * @return true if the request processing can resume , false otherwise
     * @throws Exception
     * @since 3.1.0.3
     */
    public boolean prepareRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        OwTimeZoneInfo currentTimeZoneInfo = getClientTimeZoneInfo();
        HttpSession session = request_p.getSession();
        String preparedRequestURL = (String) session.getAttribute(PREPARED_REQUEST_URL);
        if (currentTimeZoneInfo == null || preparedRequestURL != null)
        {

            //a one time attempt to retrieve the time zone - first we set the default zone info
            if (currentTimeZoneInfo == null)
            {
                setClientTimeZoneInfo(new OwTimeZoneInfo());
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwAppContext.prepareRequest : default time zone was set to ID=" + getClientTimeZone().getID());
                }
            }

            OwTimeZoneInfo timeZoneInfo = retrieveTimeZoneInfo(request_p);

            if (timeZoneInfo == null && preparedRequestURL == null)
            {

                if (!isLogin())
                {
                    StringBuilder requestURL = new StringBuilder(getBaseURL());
                    String queryString = request_p.getQueryString();
                    if (queryString == null)
                    {
                        queryString = (String) request_p.getAttribute("javax.servlet.forward.query_string");
                    }
                    if (queryString != null)
                    {
                        requestURL.append("?");
                        requestURL.append(queryString);
                    }

                    preparedRequestURL = requestURL.toString();
                    session.setAttribute(PREPARED_REQUEST_URL, preparedRequestURL);
                    LOG.debug("OwAppContext.prepareRequest : starting time zone forward prepare for " + preparedRequestURL);

                    // forward

                    ServletContext servletContext = session.getServletContext();
                    RequestDispatcher prepareDispatcher = servletContext.getRequestDispatcher("/prepare.jsp");
                    prepareDispatcher.forward(request_p, response_p);

                    return true;
                }
                else
                {
                    LOG.warn("OwAppContext.prepareRequest : logged in session with no time zone was detected!");
                }
            }
            else if (timeZoneInfo != null)
            {
                session.removeAttribute(PREPARED_REQUEST_URL);
                setClientTimeZoneInfo(timeZoneInfo);
                if (LOG.isDebugEnabled())
                {
                    TimeZone dbgZone = timeZoneInfo.getTimeZone();
                    if (dbgZone != null)
                    {
                        LOG.debug("OwAppContext.prepareRequest : context time zone was set to " + dbgZone.getID() + " == " + dbgZone.getDisplayName());
                    }
                    else
                    {
                        LOG.debug("OwAppContext.prepareRequest : context time zone was set to invalid time zone info " + timeZoneInfo.toString());
                    }
                }
            }
            else
            {
                preparedRequestURL = null;
                LOG.warn("OwAppContext.prepareRequest : unsuccessfull time zone preparation");
            }
        }

        return false;
    }

    private OwTimeZoneInfo retrieveTimeZoneInfo(HttpServletRequest request_p)
    {
        HttpSession session = request_p.getSession();
        OwTimeZoneInfo timeZoneInfo = null;

        String timeZoneID = request_p.getParameter(TIME_ZONE_ID_PARAMETER_NAME);
        if (timeZoneID != null && timeZoneID.length() > 0)
        {
            TimeZone zone = TimeZone.getTimeZone(timeZoneID);
            if (zone.getID().equals(timeZoneID))
            {
                timeZoneInfo = new OwTimeZoneInfo(zone);
            }
            else
            {
                LOG.error("OwAppContext.retrieveTimeZoneInfo : invalid client time zone ID " + timeZoneID);
            }
        }
        else
        {
            //maybe a logout was called => restore from session attribute
            if (session != null)
            {
                TimeZone timeZone = (TimeZone) session.getAttribute(TIME_ZONE);
                if (timeZone != null)
                {
                    LOG.debug("Set time zone from session.");
                    timeZoneInfo = new OwTimeZoneInfo(timeZone);
                }
            }

        }

        if (timeZoneInfo == null)
        {
            String timeZoneTimeParam = request_p.getParameter(TIME_ZONE_TIME_PARAMETER_NAME);
            long timeZoneTime = 0;

            String timeZoneOffsetParam = request_p.getParameter(TIME_ZONE_OFFSET_PARAMETER_NAME);
            int timeZoneOffset = 0;

            String timeZoneDSTParam = request_p.getParameter(TIME_ZONE_DAYLIGHTSAVINGS_PARAMETER_NAME);
            boolean timeZoneDST = true;

            String timeZoneNorthernHemisphereParam = request_p.getParameter(TIME_ZONE_NORTHERN_HEMISPHERE_PARAMETER_NAME);
            boolean timeZoneNorthernHemisphere = true;

            String[] timeZoneTransitionsParam = request_p.getParameterValues(TIME_ZONE_TRANSITIONS_PARAMETER_NAME);

            if (timeZoneTimeParam == null || timeZoneDSTParam == null || timeZoneOffsetParam == null || timeZoneNorthernHemisphereParam == null)
            {
                if (LOG.isDebugEnabled())
                {
                    String tzInfoParams = "time=" + timeZoneTime + " dst=" + timeZoneDSTParam + " offset=" + timeZoneOffsetParam + " northern=" + timeZoneNorthernHemisphereParam + " transitions=" + Arrays.toString(timeZoneTransitionsParam);
                    LOG.debug("OwAppContext.retrieveTimeZoneInfo : invalid or no client time zone information parameters " + tzInfoParams);
                }
            }
            else
            {

                try
                {
                    timeZoneTime = Long.parseLong(timeZoneTimeParam);

                    timeZoneOffset = Integer.parseInt(timeZoneOffsetParam) * 60 * 1000;

                    timeZoneDST = Boolean.parseBoolean(timeZoneDSTParam);

                    timeZoneNorthernHemisphere = Boolean.parseBoolean(timeZoneNorthernHemisphereParam);

                    Long[] timeZoneTransitions = new Long[timeZoneTransitionsParam == null ? 0 : timeZoneTransitionsParam.length];

                    for (int i = 0; i < timeZoneTransitions.length; i++)
                    {
                        timeZoneTransitions[i] = Long.parseLong(timeZoneTransitionsParam[i]);
                    }

                    timeZoneInfo = new OwTimeZoneInfo(timeZoneTime, timeZoneOffset, timeZoneDST, timeZoneTransitions, timeZoneNorthernHemisphere);

                }
                catch (NumberFormatException e)
                {
                    LOG.error("Error parsing time zone information.", e);
                }
            }
        }

        return timeZoneInfo;
    }

    /**
     * Register some specific object for a defined class/interface.
     * <p>Will not check if the provided object is from that type.</p>
     * @param typeClass Class
     * @param object Object specific to that provided class
     * @since 3.2.0.0
     * @see #getRegisteredInterface(Class)
     */
    public void registerInterface(Class<?> typeClass, Object object)
    {
        register.put(typeClass, object);
    }

    /**
     * Return the cached object, if any is contained.
     * <p>Can throw a ClassCastException if the registered object 
     * is not an instance of the requested class.</p>
     * @param typeClass Class of the object to return
     * @return the requested object or null if none is cached
     * @since 3.2.0.0
     */
    public <T> T getRegisteredInterface(Class<T> typeClass)
    {
        return (T) register.get(typeClass);
    }

    /**
     * Unregister the object for given class/interface.
     * <p>Attention: If any clean or release must be executed
     * first, it must be done before calling the unregister method.</p> 
     * @param typeClass
     * @return Type or null if nothing was register for the given class type
     * @since 3.2.0.0
     */
    public <T> T unregisterInterface(Class<T> typeClass)
    {
        return (T) register.remove(typeClass);
    }

    /**(overridable)
     * Called to create the server URL for current request.
     * @param req_p HttpRequest
     * @return String representing server URL
     * @since 4.0.0.0
     */
    protected String createServerUrl(HttpServletRequest req_p)
    {
        StringBuilder server;
        // === set the URLs / paths in the context
        if (req_p.isSecure())
        {
            server = new StringBuilder("https://");
        }
        else
        {
            server = new StringBuilder("http://");
        }
        server.append(req_p.getServerName());

        if (req_p.isSecure() ? !(req_p.getServerPort() == 443) : !(req_p.getServerPort() == 80))
        {
            server.append(":").append(req_p.getServerPort());
        }

        return server.toString();
    }

    /**(overridable)
     * URL with jsp page, like server URL + context path.
     * @param req_p HttpServletRequest
     * @return String representing 
     * @since 4.0.0.0
     */
    protected String createJspPageUrl(HttpServletRequest req_p)
    {
        return getServerURL() + req_p.getRequestURI();
    }

    /**(overridable)
     * Create base URL which will be used to process the request.
     * @param req_p HttpServletRequest
     * @return String
     * @since 4.0.0.0
     */
    protected String createBaseUrl(HttpServletRequest req_p)
    {
        return getServerURL() + req_p.getContextPath();
    }

    protected abstract OwAOProvider createAOProvider() throws OwException;

    /**
     * 
     * @return an application objects provider bound to this context
     * @throws OwException
     */
    public OwAOProvider getAOProvider() throws OwException
    {
        OwAOProvider aoProvider = getRegisteredInterface(OwAOProvider.class);
        if (aoProvider == null)
        {
            aoProvider = createAOProvider();
            registerInterface(OwAOProvider.class, aoProvider);
        }
        return aoProvider;
    }
}