package com.wewebu.ow.server.role;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.ao.OwAOType;
import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.servlets.OwStandardRoleManagerInitialisingContextListener;
import com.wewebu.ow.server.util.OwStandardOptionXMLUtil;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtilPlaceholderFilter;

/**
 *<p>
 * Standard implementation for the rolemanager.<br/>
 * Override this class to implement your own rolemanager
 * and set the rolemanager in the bootstrap settings.<br/>
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class OwStandardRoleManager implements OwRoleManager
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardRoleManager.class);

    /** special office design to use in case we run embedded in msoffice */
    public static final String MS_OFFICE_EMBEDED_DESIGN = "msoffice";

    /** delimiter for the resource ID and the application prefix */
    public static final String APPLICATION_PREFIX_DELIMITER = "/";

    /** reference to the main app context of the application */
    private OwRoleManagerContext m_MainContext;

    /** configuration node with XML config information */
    protected OwXMLUtil m_ConfigNode;

    /** singleton that maps the categories to category names */
    private static CategoryNames m_categoryNames = new CategoryNames();

    /**
     *<p>
     * Inner class that holds the mapping of the categories to category names.
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
    public static class CategoryNames
    {
        /** the map that maps the categories to category names */
        public Map m_categories = new HashMap();

        public CategoryNames()
        {
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_PLUGIN), new OwString("app.OwRoleManager.plugin", "Plugin"));
            //m_categories.put(Integer.valueOf(ROLE_CATEGORY_FUNCTION_ID), new OwString("app.OwRoleManager.functionid","Task Function"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_VIRTUAL_QUEUE), new OwString("app.OwRoleManager.groupbox", "Virtual Queue"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_SEARCH_TEMPLATE), new OwString("app.OwRoleManager.template", "Search Template"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_STANDARD_FUNCTION), new OwString("app.OwRoleManager.function", "Supplementary Function"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_DESIGN), new OwString("app.OwRoleManager.design", "Design"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_SELECTIVE_CONFIGURATION), new OwString("app.OwRoleManager.selectiveconfiguration", "Selective Configuration"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_OBJECT_CLASSES), new OwString("app.OwRoleManager.objectclasses", "Object Class"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_INDEX_FIELDS), new OwString("app.OwRoleManager.indexfields", "Index Field"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_STARTUP_FOLDER), new OwString("app.OwRoleManager.startupfolder", "Startup Folder"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_VIRTUAL_FOLDER), new OwString("app.OwRoleManager.virtualFolder", "Virtual Folder"));
            m_categories.put(Integer.valueOf(ROLE_CATEGORY_BPM_PROCESS_DEFINITION), new OwString("app.OwRoleManager.bpm.processDefinition", "Process Definition"));
        }

    }

    /** the map that maps the categories to category names */
    protected Map getCategoryMap()
    {
        return m_categoryNames.m_categories;
    }

    // singleton that maps the ROLE_CATEGORY_STANDARD_FUNCTION category resources 
    private static StandardFunctionResources m_StandardFunctionResources = new StandardFunctionResources();

    /**
     *<p>
     * Inner class that holds the map that maps the standard function IDs to display names.
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
    public static class StandardFunctionResources
    {
        /// the map that maps the standard function IDs to display names
        public Map m_Resources = new HashMap();

        public StandardFunctionResources()
        {
            m_Resources.put(STD_FUNC_CAN_EDIT_SITE_SETTINGS, new OwString("app.OwRoleManager.stdfunctions.editsitesettings", "Edit site settings"));
        }
    }

    /** the map that maps the standard function IDs to display names */
    protected Map getStandardFunctionResourceMap()
    {
        return m_StandardFunctionResources.m_Resources;
    }

    /** MIME table, DO NEVER access directly, rather use getMimeMap() 
     *  
     *  The MIME table is static, so it get only loaded once upon application server startup
     *  and not each time a user logs on.
     */
    protected Map m_MimeMap = null;

    /** default MIME table with the default MIME types when mimetype could not be resolved, DO NEVER access directly, rather use getDefaultMimeMap() 
     *  
     *  The MIME table is static, so it get only loaded once upon application server startup
     *  and not each time a user logs on.
     */
    protected Map m_DefaultMimeMap = null;

    /** map to the lists of plugin description nodes, keyed by their type
     *  DO NEVER access directly, rather use getAllowedPluginsMap() 
     *
     *  The plugin map is static, so it gets only loaded once upon application server startup
     *  and not each time a user logs on.
     */
    protected Map m_PluginTypesMap = null;

    /** map to the description nodes, keyed by their id, DO NEVER access directly, rather use getPlugin() 
     *
     *  The plugin map is static, so it gets only loaded once upon application server startup
     *  and not each time a user logs on.
     */
    protected Map m_PluginMap = null;

    /** map to the lists of plugin description nodes, DO NEVER access directly, rather use getAllowedPluginsMap() 
     *  
     *  The allowed plugin map maps only those plugins which the logged on user has access rights.
     *  Must not be static, because it is created for each user-session.
     */
    protected Map m_AllowedPluginTypesMap = null;

    /** map of all selective configuration names (elements with a "optionid" attribute) in the config files.
     * DO NEVER access directly, rather use getResources(ROLE_CATEGORY_SELECTIVE_CONFIGURATION);
     */
    protected Map m_SelectiveConfigurationMap = null;

    /** true after login init  */
    private boolean m_fInitialized = false;

    /** true after login init  */
    protected boolean isInitialized()
    {
        return m_fInitialized;
    }

    /** get configuration node with XML config information */
    protected OwXMLUtil getConfigNode()
    {
        return m_ConfigNode;
    }

    /** the design name */
    protected String m_strDesign;

    /** get the current logged on user
     * @return  OwUserInfo
     * @throws Exception 
     * */
    protected OwBaseUserInfo getCurrentUser() throws Exception
    {
        return m_MainContext.getCurrentUser();
    }

    /** 
     * Init role config data upon startup, 
     * make sure the servlet listener {@link OwStandardRoleManagerInitialisingContextListener} is configured for your application.
     * 
     * @param initializer_p
     * @throws OwConfigurationException 
     */
    public static void applicationInitalize(OwBaseInitializer initializer_p) throws OwConfigurationException
    {
        if (LOG.isInfoEnabled())
        {
            LOG.info("OwStandardRoleManager.applicationInitalize: role manger configuration started...");
        }

        // initialize role config data upon application start
        try
        {
            createPluginsMap(initializer_p);
            LOG.debug("OwStandardRoleManager.applicationInitalize: plugins successfully initialized.");
        }
        catch (Exception ex)
        {
            String msg = "Failed to load the plugins. Please check your configuration files.";
            LOG.error(msg, ex);
            throw new OwConfigurationException(msg, ex);
        }
        try
        {
            createMimeMap(initializer_p);
            LOG.debug("OwStandardRoleManager.applicationInitalize: mimetable successfully initialized.");
        }
        catch (Exception ex)
        {
            String msg = "Failed to load the mimetable. Please check your configuration files.";
            LOG.error(msg, ex);
            throw new OwConfigurationException(msg, ex);
        }
        try
        {
            createSelectiveConfigurationMap(initializer_p);
            LOG.debug("OwStandardRoleManager.applicationInitalize: selective configurations successfully initialized.");
        }
        catch (Exception ex)
        {
            String msg = "Failed to load the selective configuration. Please check your configuration files.";
            LOG.error(msg, ex);
            throw new OwConfigurationException(msg, ex);
        }

        if (LOG.isInfoEnabled())
        {
            LOG.info("OwStandardRoleManager.applicationInitalize: ...role manger configuration finished.");
        }
    }

    /** update the configuration data */
    public void refreshStaticConfiguration() throws OwConfigurationException
    {
        LOG.debug("OwStandardRoleManager.refreshStaticConfiguration: refresh the application configuration....");
        applicationInitalize(m_MainContext);
    }

    /** check if update the configuration data is supported */
    public boolean canRefreshStaticConfiguration() throws Exception
    {
        return true;
    }

    /** init the manager, set context
     * 
     * optionally set a prefix to distinguish several different applications.
     * The rolemanager will filter the allowed plugins, MIME settings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     * 
     * @param configNode_p OwXMLUtil node with configuration information
     * @param mainContext_p reference to the main app context of the application 
     * 
     * @exception OwException
     */
    public void init(OwRoleManagerContext mainContext_p, OwXMLUtil configNode_p) throws OwException
    {
        // === init members
        m_MainContext = mainContext_p;
        m_ConfigNode = configNode_p;

        // === get the design name from config node if set, this is only a default implementation and can be overwritten
        m_strDesign = configNode_p.getSafeTextValue("DesignName", null);

        // === get the design name form the request URL, this is only a default implementation and can be overwritten
        // derived classes may compute the design name differently
        if (m_strDesign == null)
        {
            // === get the base name from the request JSP 
            m_strDesign = mainContext_p.getConfigurationName();

            // check if we run embedded in msoffice
            if (m_strDesign.indexOf("_vti_bin/owssvr.dll") != -1)
            {
                // === use special embedded office design
                m_strDesign = MS_OFFICE_EMBEDED_DESIGN;
            }
            else
            {
                // === use design according to calling JSP page name
                int i = m_strDesign.lastIndexOf('/');

                if (i != -1)
                {
                    m_strDesign = m_strDesign.substring(i + 1);

                    i = m_strDesign.lastIndexOf('.');
                    if (i != -1)
                    {
                        m_strDesign = m_strDesign.substring(0, i);
                    }
                    else
                    {
                        m_strDesign = "default41";
                    }
                }
                else
                {
                    m_strDesign = "default41";
                }
                //used only for testing and development environments (adaptor change on the fly)
                if (m_strDesign.equals("adaptor"))
                {
                    m_strDesign = "default41";
                }
            }
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwStandardRoleManager.init: used DesignName = " + m_strDesign);
        }
    }

    /** check if we run embedded in office */
    protected boolean isMsOfficeEmbedded()
    {
        // reference compare is enough and fast, because we set design directly to static reference in case of office.
        return (m_strDesign.equals(MS_OFFICE_EMBEDED_DESIGN));
    }

    /** init called AFTER the user has logged in.
     *
     *  NOTE: This function is called only once after login to do special initialization, 
     *        which can only be performed with valid credentials.
     */
    public void loginInit() throws Exception
    {
        // get application scope data
        m_MimeMap = (Map) m_MainContext.getApplicationAttribute("OwStandardRoleManager.m_MimeMap");
        if (m_MimeMap == null)
        {
            String msg = "OwStandardRoleManager.loginInit: Error - the MimeMap is not initialized correctly, is null...";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        m_DefaultMimeMap = (Map) m_MainContext.getApplicationAttribute("OwStandardRoleManager.m_DefaultMimeMap");
        if (m_DefaultMimeMap == null)
        {
            String msg = "OwStandardRoleManager.loginInit: Error - the DefaultMimeMap is not initialized correctly, is null...";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        m_PluginTypesMap = (Map) m_MainContext.getApplicationAttribute("OwStandardRoleManager.m_PluginTypesMap");
        if (m_PluginTypesMap == null)
        {
            String msg = "OwStandardRoleManager.loginInit: Error - the PluginTypesMap is not initialized correctly, is null...";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        m_PluginMap = (Map) m_MainContext.getApplicationAttribute("OwStandardRoleManager.m_PluginMap");
        if (m_PluginMap == null)
        {
            String msg = "OwStandardRoleManager.loginInit: Error - the PluginMap is not initialized correctly, is null...";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        m_SelectiveConfigurationMap = (Map) m_MainContext.getApplicationAttribute("OwStandardRoleManager.m_SelectiveConfigurationMap");
        if (m_SelectiveConfigurationMap == null)
        {
            String msg = "OwStandardRoleManager.loginInit: Error - the SelectiveConfigurationMap is not initialized correctly, is null...";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        m_fInitialized = true;
    }

    /** get the application context
     *
     * @return OwMainAppContext
     */
    protected OwRoleManagerContext getContext()
    {
        return m_MainContext;
    }

    /** get the name of the design for the current user can be overridden by rolemanager implementation
     *  Specifies the subfolder under /designs/ where to retrieve the design files. i.e. css, images, layouts...
     *  This function can be used to make the look & feel dependent on the logged in user. 
     *
     *  @return name of design to use, default = "default"
     */
    public String getDesign() throws Exception
    {
        // default implementation returns the base name, so we can switch designs upon the urls JSP page name
        return m_strDesign;
    }

    /** create application scope map of a list of DOM Nodes,
     *  each representing a plugin description
     */
    private static void createPluginsMap(OwBaseInitializer context_p) throws Exception
    {
        // === plugins not loaded yet
        Map typesMap = new HashMap();
        Map pluginMap = new HashMap();

        // === get plugins config XML document
        java.io.InputStream in = null;

        org.w3c.dom.Document doc;
        try
        {
            in = context_p.getXMLConfigDoc("plugins");
            doc = OwXMLDOMUtil.getDocumentFromInputStream(in);
            OwXMLDOMUtil.initialize(doc);
        }
        catch (Exception e)
        {
            String msg = "OwStandardRoleManager.createPluginsMap: 'owplugins.xml' can not be read. Possible cause: invalid XML format or the file is missing...";
            LOG.fatal(msg, e);
            throw new OwConfigurationException(msg, e);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException ioex)
                {
                    LOG.warn("Stream could not be closed after read of owplugins.xml.", ioex);
                }
                in = null;
            }
        }

        // evaluate all "include" statements
        org.w3c.dom.NodeList IncludeList = doc.getElementsByTagName("Include");
        for (int i = 0; i < IncludeList.getLength(); i++)
        {
            org.w3c.dom.Node includeElement = IncludeList.item(i);
            org.w3c.dom.NamedNodeMap includeAttributes = includeElement.getAttributes();
            org.w3c.dom.Node includeFileNode = includeAttributes.getNamedItem("file");
            org.w3c.dom.Node includeElementParent = includeElement.getParentNode();
            org.w3c.dom.Node includeElementSibling = includeElement.getNextSibling();
            if (includeFileNode == null)
            {
                String msg = "OwStandardRoleManager.createPluginsMap: Can not resolve include statement. <include> element is missing the file attribute.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }
            String includeFileName = includeFileNode.getNodeValue();
            InputStream includeInputStream = null;
            try
            {
                URL includeURL = context_p.getConfigURL(includeFileName);
                includeInputStream = includeURL.openStream();
            }
            catch (Exception e)
            {
                String msg = "OwStandardRoleManager.createPluginsMap: Error reading included file, includeFileName = " + includeFileName;
                LOG.fatal(msg, e);
                if (includeInputStream != null)
                {
                    try
                    {
                        includeInputStream.close();
                    }
                    catch (IOException ioex)
                    {
                        //no warn, maybe the open stream failed
                    }
                    includeInputStream = null;
                }
                throw new OwConfigurationException(msg, e);
            }
            org.w3c.dom.Document includedDocument;
            try
            {
                includedDocument = OwXMLDOMUtil.getDocumentFromInputStream(includeInputStream);
            }
            catch (Exception e)
            {
                String msg = "OwStandardRoleManager.createPluginsMap: Included file " + includeFileName + " can not be read, probably due to a syntax error.";
                LOG.fatal(msg, e);
                throw new OwConfigurationException(msg, e);
            }
            finally
            {
                if (includeInputStream != null)
                {
                    try
                    {
                        includeInputStream.close();
                    }
                    catch (IOException ioex)
                    {
                        LOG.warn("Stream could not be closed after include Document.", ioex);
                    }

                    includeInputStream = null;
                }
            }
            org.w3c.dom.Element includedDocumentElement = includedDocument.getDocumentElement();
            if (!includedDocumentElement.getNodeName().equals("SubPlugIn"))
            {
                String msg = "OwStandardRoleManager.createPluginsMap: The included file " + includeFileName + " does not start with a <SubPlugIn> Element!";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }
            org.w3c.dom.Node includedDocumentCopySource = includedDocumentElement.getFirstChild();
            while (includedDocumentCopySource != null)
            {
                // import Node from included document to owplugins document
                org.w3c.dom.Node importedNode = doc.importNode(includedDocumentCopySource, true);
                // add the imported node
                if (includeElementSibling == null)
                {
                    includeElementParent.appendChild(importedNode);
                }
                else
                {
                    includeElementParent.insertBefore(importedNode, includeElementSibling);
                }
                // advance to the next node to copy
                includedDocumentCopySource = includedDocumentCopySource.getNextSibling();
            }
            // remove include element
            includeElementParent.removeChild(includeElement);
        }

        // retrieve the plugins by type
        org.w3c.dom.NodeList plugNodeList = doc.getElementsByTagName("PlugIn");

        for (int i = 0; i < plugNodeList.getLength(); i++)
        {
            // === check pluging type
            org.w3c.dom.Node plugNode = plugNodeList.item(i);
            org.w3c.dom.NamedNodeMap Attributes = plugNode.getAttributes();
            org.w3c.dom.Node typeNode = Attributes.getNamedItem("type");

            // === get plugin list by type
            List pluginList = (List) typesMap.get(typeNode.getNodeValue());
            if (pluginList == null)
            {
                // === create new list
                pluginList = new LinkedList();
                typesMap.put(typeNode.getNodeValue(), pluginList);
            }

            // === add plugin node to list
            OwXMLUtil plugWrapper = new OwStandardXMLUtil(plugNode);
            pluginList.add(plugWrapper);

            // === add it also to the global map
            pluginMap.put(plugWrapper.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, null), plugWrapper);
        }

        // set in application context
        context_p.setApplicationAttribute("OwStandardRoleManager.m_PluginTypesMap", typesMap);
        context_p.setApplicationAttribute("OwStandardRoleManager.m_PluginMap", pluginMap);
    }

    /** get a list of plugin description nodes by type as defined in OwBaseConfiguration,
     * that are allowed for the logged on user.
     *
     * @return list of plugins for the given type or null if not found
     */
    public List getPlugins(String strType_p) throws Exception
    {
        if (!isInitialized())
        {
            String msg = "OwStandardRoleManager.getPlugins: Rolemanager not initialized yet.";
            LOG.debug(msg);
            throw new OwInvalidOperationException(msg);
        }

        return (List) getAllowedPluginsMap().get(strType_p);
    }

    /** get a plugin description by its key
     * that are allowed for the logged on user.
     *
     * @return OwXMLUtil or null if not found
     */
    public OwXMLUtil getPlugin(String strID_p) throws Exception
    {
        if (!isInitialized())
        {
            String msg = "OwStandardRoleManager.getPlugin: Rolemanager not initialized yet.";
            LOG.debug(msg);
            throw new OwInvalidOperationException(msg);
        }

        return (OwXMLUtil) m_PluginMap.get(strID_p);
    }

    /** update the allowed plugins after role change */
    protected void updateAllowedPlugins()
    {
        m_AllowedPluginTypesMap = null;
    }

    /** get a map of a list of DOM Nodes, each representing a plugin description , only those for which the current user has access rights
     * that are allowed for the logged on user.
     *  
     * @return HashMap of lists of plugin nodes, keyed by plugin type, only those for which the current user has access rights
     */
    private Map getAllowedPluginsMap() throws Exception
    {
        if (m_AllowedPluginTypesMap == null)
        {
            // create map for allowed plugins
            m_AllowedPluginTypesMap = new HashMap();

            // iterate over the keys i.e. plugin types
            Iterator pluginTypeIterator = m_PluginTypesMap.entrySet().iterator();
            while (pluginTypeIterator.hasNext())
            {
                Entry currentEntry = (Entry) pluginTypeIterator.next();
                // get the type 
                String strPluginType = (String) currentEntry.getKey();
                // get the plugin list for that type
                List pluginList = (List) currentEntry.getValue();

                // iterate over the plugin descriptions
                Iterator pluginDescriptionIterator = pluginList.iterator();

                while (pluginDescriptionIterator.hasNext())
                {
                    // get plugin description node wrapper
                    OwXMLUtil pluginDescriptionNode = (OwXMLUtil) pluginDescriptionIterator.next();

                    // get plugin ID from wrapper
                    String strPluginID = pluginDescriptionNode.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, null);
                    // check if plugin is allowed
                    if (isAllowed(OwRoleManager.ROLE_CATEGORY_PLUGIN, strPluginID))
                    {
                        // === current user is allowed to use the plugin  
                        // add to allowed map
                        List newList = (List) m_AllowedPluginTypesMap.get(strPluginType);
                        if (newList == null)
                        {
                            // === no list yet created for that plugin type
                            newList = new LinkedList();
                            m_AllowedPluginTypesMap.put(strPluginType, newList);
                        }

                        // add allowed plugin to list
                        OwXMLUtil node = pluginDescriptionNode;

                        // create filter with mandator properties 
                        if (null != m_MainContext.getMandator())
                        {
                            node = new OwXMLUtilPlaceholderFilter(pluginDescriptionNode, m_MainContext.getMandator());
                        }

                        newList.add(node);
                    }
                }
            }
        }

        return m_AllowedPluginTypesMap;
    }

    /** get the MIME XML Entry for the given MIMEType. Lookup in MimeMap
     * that are allowed for the logged on user.
     * <br>
     * <pre>
     * &lt;?xml version="1.0" ?&gt;
     *   &lt;mimetable&gt;
     *      &lt;mime typ="file/txt"&gt;
     *         &lt;icon&gt;file_txt.png&lt;/icon&gt;
     *         &lt;viewerservlet&gt;getConent&lt;/viewerservlet&gt;
     *      &lt;/mime&gt;
     *      &lt;!-- further MIME entries--&gt;
     *  &lt;/mimetable&gt;
     * </pre>
     * <br>
     *
     * @param strMIMEType_p OwObject MIMEType
     *
     * @return OwXMLUtil wrapped DOM Node of MIME entry from MIME table, or null if not found
     */
    public OwXMLUtil getMIMENode(String strMIMEType_p) throws Exception
    {
        try
        {
            return (OwXMLUtil) m_MimeMap.get(strMIMEType_p.toLowerCase());
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    /** get the default MIME XML Entry for the given object type.
     * that are allowed for the logged on user.
     *
     * @param iObjectType_p Objecttype
     *
     * @return OwXMLUtil wrapped DOM Node of MIME entry from MIME table, or null if not found
     */
    public OwXMLUtil getDefaultMIMENode(int iObjectType_p) throws Exception
    {
        return (OwXMLUtil) m_DefaultMimeMap.get(Integer.valueOf(iObjectType_p));
    }

    /**
     * Creates a HashMap of all available "optionid" configurations and adds it as application attribute
     * @param context_p
     * @throws Exception
     */
    private static void createSelectiveConfigurationMap(OwBaseInitializer context_p) throws Exception
    {
        Map selectiveConfigurationMap = new HashMap();
        scanXMLFileForSelectiveConfigurationMap(selectiveConfigurationMap, "bootstrap", context_p);
        scanXMLFileForSelectiveConfigurationMap(selectiveConfigurationMap, "plugins", context_p);
        scanXMLFileForSelectiveConfigurationMap(selectiveConfigurationMap, "mimetable", context_p);
        context_p.setApplicationAttribute("OwStandardRoleManager.m_SelectiveConfigurationMap", selectiveConfigurationMap);
    }

    /**
     * Read the given XML configuration file and add all "optionid" definitions to the map.
     * @param selectiveConfigurationMap_p the map collecting a list of all optionid elements
     * @param xmlFileName_p the base name of the XML configuration file
     * @param context_p the initialization context
     */
    private static void scanXMLFileForSelectiveConfigurationMap(Map selectiveConfigurationMap_p, String xmlFileName_p, OwBaseInitializer context_p) throws OwConfigurationException
    {
        java.io.InputStream in = null;
        try
        {
            in = context_p.getXMLConfigDoc(xmlFileName_p);
            org.w3c.dom.Document doc = OwXMLDOMUtil.getDocumentFromInputStream(in);
            iterativeScanElementForOptionid(selectiveConfigurationMap_p, doc.getDocumentElement());
        }
        catch (IOException ex)
        {
            String msg = "Invalid file entry for the XMLConfigDoc = " + xmlFileName_p + ". Possible cause: syntax error or the file is missing...";
            LOG.error(msg, ex);
            throw new OwConfigurationException(msg, ex);
        }
        catch (SAXParseException ex)
        {
            String msg = "Parse Exception: invalid entries for the XMLConfigDoc = " + xmlFileName_p + ". Possible cause: syntax error or the file is missing...";
            LOG.error(msg, ex);
            throw new OwConfigurationException(msg, ex);
        }
        catch (Exception ex)
        {
            String msg = "Error loading or validating the the XMLConfigDoc = " + xmlFileName_p + ". Possible cause: syntax error or the file is missing...";
            LOG.error(msg, ex);
            throw new OwConfigurationException(msg, ex);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    String msg = "Could not close inputstream after handling of " + xmlFileName_p;
                    LOG.error(msg, e);
                    throw new OwConfigurationException(msg, e);
                }
                in = null;
            }
        }
    }

    /**
     * check this XML element for an "optionid" attribute and iteratively scan all subelements.
     * If this element contains such an optionid attribute, add it to the map.
     * @param selectiveConfigurationMap_p the map collecting a list of all optionid elements
     * @param elem_p the element to scan
     * @throws Exception
     */
    private static void iterativeScanElementForOptionid(Map selectiveConfigurationMap_p, org.w3c.dom.Element elem_p) throws Exception
    {
        // add optionId of this element, if present
        String optionidAttribute = elem_p.getAttribute("optionid");
        if (optionidAttribute.length() != 0)
        {
            String tagName = elem_p.getTagName();
            String id = tagName + (tagName.equals("mime") ? "." + elem_p.getAttribute("type") + "." : ".") + optionidAttribute;
            selectiveConfigurationMap_p.put(id, id);
        }
        // scan all subnodes for elements
        for (Node testNode = elem_p.getFirstChild(); testNode != null; testNode = testNode.getNextSibling())
        {
            if (testNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
            {
                iterativeScanElementForOptionid(selectiveConfigurationMap_p, (org.w3c.dom.Element) testNode);
            }
        }
    }

    /** create the MIME HashMap
     */
    private static void createMimeMap(OwBaseInitializer context_p) throws Exception
    {
        Map mimeMap = new HashMap();
        Map defaultMimeMap = new HashMap();

        // === get XML document for MIMEtable
        String sMimeTableName = "mimetable";

        java.io.InputStream in = null;
        org.w3c.dom.Document doc = null;
        try
        {
            in = context_p.getXMLConfigDoc(sMimeTableName);
            doc = OwXMLDOMUtil.getDocumentFromInputStream(in);
            OwXMLDOMUtil.initialize(doc);
        }
        catch (Exception e)
        {
            String msg = "OwStandardRoleManager.createMimeMap: 'owmimetable.xml' can not be read. Possible cause: invalid XML format or the file is missing...";
            LOG.fatal(msg, e);
            throw new OwConfigurationException(msg, e);
        }
        finally
        {
            if (in != null)
            {
                in.close();
                in = null;
            }
        }

        // retrieve the only MIMEtable node 
        org.w3c.dom.NodeList mimeNodes = doc.getElementsByTagName("mime");

        for (int i = 0; i < mimeNodes.getLength(); i++)
        {
            // === check pluging type
            org.w3c.dom.Node mimeItem = mimeNodes.item(i);
            org.w3c.dom.NamedNodeMap attributes = mimeItem.getAttributes();
            org.w3c.dom.Node typeNode = attributes.getNamedItem("type");

            OwXMLUtil mimenode = new OwStandardXMLUtil(mimeItem);

            String strMimetype = typeNode.getNodeValue();
            String strMimetypekey = strMimetype;

            // add option ID to mimetype, so we can select it out later 
            String optionid = mimenode.getSafeStringAttributeValue(OwStandardOptionXMLUtil.OPTION_ID_ATTRIBUTE_NAME, null);
            if (null != optionid)
            {
                strMimetypekey = OwConfiguration.MIME_TYPE_OPTION_PREFIX + strMimetype + "." + optionid;
            }

            // add to map
            mimeMap.put(strMimetypekey.toLowerCase(), mimenode);

            // check if it is a default mimetype
            if (strMimetype.startsWith(OwMimeManager.MIME_TYPE_PREFIX_OW_DEFAULT))
            {
                try
                {
                    String sType = strMimetype.substring(OwMimeManager.MIME_TYPE_PREFIX_OW_DEFAULT.length());

                    Integer iObj = null;

                    try
                    {
                        // get object type index
                        iObj = (Integer) com.wewebu.ow.server.ecm.OwObjectReference.class.getField(sType).get(null);
                    }
                    catch (NoSuchFieldException e)
                    {
                        iObj = Integer.valueOf(sType);
                    }

                    // store in map
                    defaultMimeMap.put(iObj, mimenode);
                }
                catch (Exception e)
                {
                    String msg = "Default MIME type in MIME map could not be resolved, MIME type = " + strMimetype;
                    LOG.error(msg, e);
                    throw new OwConfigurationException(msg, e);
                }
            }
        }

        // set in application context
        context_p.setApplicationAttribute("OwStandardRoleManager.m_MimeMap", mimeMap);
        context_p.setApplicationAttribute("OwStandardRoleManager.m_DefaultMimeMap", defaultMimeMap);
    }

    // === methods for admin tools    
    public Collection getCategories()
    {
        //create copy, no direct access to the key set
        return new HashSet<Integer>(getCategoryMap().keySet());
    }

    /*
     * @see com.wewebu.ow.server.role.OwRoleManager#getConfiguredCategories()
     */
    public Collection getConfiguredCategories()
    {
        return getCategories();
    }

    /** get a display name for the given category 
     *
     * @param locale_p Locale to use
     * @param categorie_p Integer from getCategories() method
     */
    public String getCategoryDisplayName(java.util.Locale locale_p, int categorie_p)
    {
        return ((OwString) getCategoryMap().get(Integer.valueOf(categorie_p))).getString(locale_p);
    }

    /** a map of resources for the plugins */
    protected Map m_PluginCategoryMap;

    /** get a map of resources for the plugins
     */
    protected Map getPluginCategoryMap()
    {
        if (m_PluginCategoryMap == null)
        {
            m_PluginCategoryMap = new LinkedHashMap();

            // iterate over all plugins
            for (int i = 0; i < OwBaseConfiguration.getPluginTypeDefinitions().length; i++)
            {
                List plugs = (List) m_PluginTypesMap.get(OwBaseConfiguration.getPluginTypeDefinitions()[i].getType());

                if (plugs != null)
                {
                    Iterator it = plugs.iterator();
                    while (it.hasNext())
                    {
                        OwXMLUtil plugDescriptionNode = (OwXMLUtil) it.next();

                        String key = plugDescriptionNode.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, "");
                        StringBuffer value = new StringBuffer("@");
                        value.append(OwBaseConfiguration.getPluginTypeDisplayName(plugDescriptionNode.getSafeStringAttributeValue("type", "[unknown]"), getContext().getLocale()));
                        value.append("@");
                        value.append(getContext().getBaseConfiguration().getLocalizedPluginTitle(plugDescriptionNode));
                        // Map plugin ID (resource id) to the plugin name (resource displayname)
                        m_PluginCategoryMap.put(key, value);
                    }
                }
            }
        }

        return m_PluginCategoryMap;
    }

    /** a map of resources for the category: design */
    protected Map m_DesignCategoryMap;

    /**
     * get a map of resources for the category: design
     * @return map of design resources
     */
    protected Map getDesignCategoryMap()
    {
        // the design category map is built during init
        if (m_DesignCategoryMap == null)
        {
            m_DesignCategoryMap = new LinkedHashMap();
            List availableDesigns = m_ConfigNode.getSafeNodeList("AvailableDesigns");
            Iterator it = availableDesigns.iterator();
            while (it.hasNext())
            {
                OwXMLUtil designNode = null;
                try
                {
                    designNode = new OwStandardXMLUtil((Node) it.next());
                }
                catch (Exception e)
                {
                    continue;
                }
                String id = designNode.getSafeTextValue(null);
                String displayName = designNode.getSafeStringAttributeValue("displayName", "");
                if ((id != null) && (id.length() > 0))
                {
                    m_DesignCategoryMap.put(id, getContext().localize("design." + id + ".title", displayName));
                }
            }
        }
        return (m_DesignCategoryMap);
    }

    /** a map of resources for the category: search template */
    protected Map m_SearchTemplateCategoryMap;

    /** a map of resources for the category: virtual folder */
    protected Map<String, String> m_VirtualFolderCategoryMap;

    private Map<String, OwWorkflowDescription> m_ProcessDefinitionsCategoryMap;

    /** overridable, create a map of repositories that can be searched
     */
    protected Map getRepositories()
    {
        // === create map of repositories to search on 
        Map repositories = new HashMap();

        repositories.put("OW_HISTORY", getContext().getHistoryManager());
        repositories.put("OW_ECM_ADAPTER", getContext().getNetwork());

        return repositories;
    }

    /**
     * get a map of resources for the category: virtual folders
     * @return map of virtual folder resources
     * @since 4.0.0.0
     */
    protected Map<String, String> getVirtualFoldersCategoryMap() throws Exception
    {
        if (m_VirtualFolderCategoryMap == null)
        {
            m_VirtualFolderCategoryMap = new HashMap<String, String>();

            Collection<OwVirtualFolderObject> allVirtualFolders = getUnmamangedApplicationObjects(OwAOConstants.AO_VIRTUAL_FOLDER, null, false);
            //network.getApplicationObjects(OwNetwork.APPLICATION_OBJECT_TYPE_VIRTUAL_FOLDER, null, false);
            for (OwVirtualFolderObject virtualFolder : allVirtualFolders)
            {
                m_VirtualFolderCategoryMap.put(virtualFolder.getVirtualFolderName(), virtualFolder.getName());
            }

            //
            //            OwXMLUtil roleManagerUtil = getConfigNode();
            //            Node roleManagerNode = roleManagerUtil.getNode();
            //            Node bootstrapNode = roleManagerNode.getParentNode();
            //            OwStandardOptionXMLUtil bootstrapUtil = new OwStandardOptionXMLUtil(bootstrapNode);
            //            List<OwXMLUtil> ecmAdapterUtil = bootstrapUtil.getSafeUtilList(OwConfiguration.ECM_ADAPTER_ELEMENT);
            //
            //            Map<String, OwSemiVirtualRecordClass> semiVirtualRecordMap = OwSemiVirtualRecordClass.createVirtualRecordClasses(ecmAdapterUtil.get(0));
            //            Set<Entry<String, OwSemiVirtualRecordClass>> semiVirtualRecords = semiVirtualRecordMap.entrySet();
            //
            //            for (Entry<String, OwSemiVirtualRecordClass> semiVirtualRecordEntry : semiVirtualRecords)
            //            {
            //                OwSemiVirtualRecordClass semiVirtualRecord = semiVirtualRecordEntry.getValue();
            //                String virtualFolder = semiVirtualRecord.getVirtualFolder();
            //                m_VirtualFolderCategoryMap.put(virtualFolder, semiVirtualRecord);
            //            }

        }

        return (m_VirtualFolderCategoryMap);
    }

    /**
     * get a map of workflow descriptions by there ID.
     * @return map of virtual folder resources
     * @since 4.2.0.1
     */
    protected Map<String, OwWorkflowDescription> getBpmProcessDefinitionsCategoryMap() throws Exception
    {
        if (null == this.m_ProcessDefinitionsCategoryMap)
        {
            this.m_ProcessDefinitionsCategoryMap = new HashMap<String, OwWorkflowDescription>();
            OwNetwork network = m_MainContext.getNetwork();
            String bpmInterfaceName = OwWorkitemRepository.class.getName();
            if (network.hasInterface(bpmInterfaceName))
            {
                OwWorkitemRepository workitemRepository = (OwWorkitemRepository) network.getInterface(bpmInterfaceName, null);
                Collection wfDescriptions = workitemRepository.getLaunchableWorkflowDescriptions(null);
                for (Object object : wfDescriptions)
                {
                    OwWorkflowDescription description = (OwWorkflowDescription) object;
                    this.m_ProcessDefinitionsCategoryMap.put(description.getId(), description);
                }
            }
        }
        return this.m_ProcessDefinitionsCategoryMap;
    }

    /**
     * get a map of resources for the category: search template
     * @return map of search template resources
     */
    protected Map getSearchTemplateCategoryMap() throws Exception
    {
        if (m_SearchTemplateCategoryMap == null)
        {
            m_SearchTemplateCategoryMap = new HashMap();
            // We fetch all configured SearchTemplate locations from all Plugins.
            // Thus, we iterate over all plugin XML nodes and check each node for a
            // subnode 
            for (int i = 0; i < OwBaseConfiguration.getPluginTypeDefinitions().length; i++)
            {
                List plugins = (List) m_PluginTypesMap.get(OwBaseConfiguration.getPluginTypeDefinitions()[i].getType());
                if (plugins != null)
                {
                    Iterator itPlugins = plugins.iterator();
                    while (itPlugins.hasNext())
                    {
                        OwXMLUtil plugDescriptionNode = (OwXMLUtil) itPlugins.next();
                        List searchTemplateLocations = plugDescriptionNode.getSafeNodeList("SearchTemplates");
                        if (searchTemplateLocations.size() > 0)
                        {
                            Iterator itSearchTemplateLocations = searchTemplateLocations.iterator();
                            while (itSearchTemplateLocations.hasNext())
                            {
                                // get XMLUtil of SearchTemplate Location config node
                                OwXMLUtil repositoryNode = new OwStandardXMLUtil((org.w3c.dom.Node) itSearchTemplateLocations.next());
                                // get the repository where the search templates are stored
                                String repName = repositoryNode.getSafeStringAttributeValue("repname", null);
                                OwRepository repository = (OwRepository) getRepositories().get(repName);
                                if (repository == null)
                                {
                                    LOG.fatal("OwStandardRoleManager.getSearchTemplateCategoryMap: Repository is not existing, repName = " + repName);
                                    throw new OwConfigurationException(getContext().localize("owsearch.OwSearchDocument.repositorynotdefined", "Repository is not existing:") + " " + repName);
                                }
                                // get the location inside the repository where the search templates are stored
                                String strSearchTemplates = repositoryNode.getNode().getFirstChild().getNodeValue();
                                if (strSearchTemplates == null)
                                {
                                    LOG.fatal("OwStandardRoleManager.getSearchTemplateCategoryMap: Search template category has not been defined at the search plugin.");
                                    throw new OwConfigurationException(getContext().localize("owsearch.OwSearchDocument.templatesnotdefined", "Search template category has not been defined at the Search Plugin."));
                                }
                                // get searchtemplate objects from network
                                Collection SearchTemplateObjects = getUnmamangedApplicationObjects(OwAOConstants.AO_SEARCHTEMPLATE, strSearchTemplates, false);
                                //getContext().getNetwork().getApplicationObjects(OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE, strSearchTemplates, false);
                                if (SearchTemplateObjects != null)
                                {
                                    // add SearchTemplates to list
                                    Iterator it = SearchTemplateObjects.iterator();
                                    while (it.hasNext())
                                    {
                                        OwSearchTemplate searchtemplate = (OwSearchTemplate) it.next();
                                        m_SearchTemplateCategoryMap.put(searchtemplate.getName(), searchtemplate);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return (m_SearchTemplateCategoryMap);
    }

    /** a map of resources for the category: search template */
    protected Map m_GroupBoxCategoryMap;

    /**
     * get a map of resources for the category: search template
     * @return map of search template resources
     */
    protected Map getGroupBoxCategoryMap() throws OwException
    {
        if (m_GroupBoxCategoryMap == null)
        {
            m_GroupBoxCategoryMap = new HashMap();

            List<OwSearchTemplate> groupBoxSearchTemplates;
            try
            {
                // get the view definition search templates from network
                groupBoxSearchTemplates = getUnmamangedApplicationObjects(OwAOConstants.AO_SEARCHTEMPLATE, "owbpmviewtemplates", false);
                //getContext().getNetwork().getApplicationObjects(OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE, "owbpmviewtemplates", false);
            }
            catch (OwObjectNotFoundException e)
            {
                // nothing defined
                return (m_GroupBoxCategoryMap);
            }
            // put into a map
            Iterator<OwSearchTemplate> it = groupBoxSearchTemplates.iterator();
            while (it.hasNext())
            {
                OwSearchTemplate searchtemplate = it.next();
                m_GroupBoxCategoryMap.put(searchtemplate.getName(), searchtemplate);
            }
        }
        return (m_GroupBoxCategoryMap);
    }

    protected <T> List<T> getUnmamangedApplicationObjects(OwAOType<T> type, String name, boolean forceSpecificObj) throws OwException
    {
        OwAOProvider aoProvider = m_MainContext.getUnmanagedAOProvider();
        return aoProvider.getApplicationObjects(type, name, forceSpecificObj);
    }

    /** a map of resources for category: object classes */
    protected Map m_objectClassesMap;

    /**
     * get a map of resources for the category: object classes
     * @return map of object classes resources
     */
    protected Map getObjectClassesMap() throws Exception
    {
        if (m_objectClassesMap == null)
        {
            m_objectClassesMap = new LinkedHashMap();
            // get a Map of all object classes
            OwRoleManagerContext context = getContext();
            OwNetwork netwrok = context.getNetwork();
            Map allClassNamesMap = netwrok.getObjectClassNames(null, false, false, null);
            // iterate over all object classes and add them as resources to the map
            Iterator itAllClassNamesMap = allClassNamesMap.entrySet().iterator();
            while (itAllClassNamesMap.hasNext())
            {
                Entry allClassNamesEntry = (Entry) itAllClassNamesMap.next();
                String className = (String) allClassNamesEntry.getKey();
                String displayName = (String) allClassNamesEntry.getValue();
                m_objectClassesMap.put(className, displayName);
            }
        }
        return (m_objectClassesMap);
    }

    /** a map of resources for category: object classes */
    protected Map m_indexFieldsMap;

    /**
     * get a map of resources for the category: index fields
     * @return map of index fields resources
     */
    protected Map getIndexFieldsMap() throws Exception
    {
        if (m_indexFieldsMap == null)
        {
            m_indexFieldsMap = new LinkedHashMap();
            // there is no way to get a list of property names directly.
            // we have to get the list of all object classes first and query each for the list
            // of index fields.
            // get a Map of all object classes
            Map allClassNamesMap = getContext().getNetwork().getObjectClassNames(null, false, false, null);
            // iterate over all object classes
            Iterator itAllClassNamesMap = allClassNamesMap.entrySet().iterator();
            while (itAllClassNamesMap.hasNext())
            {
                Entry allClassNamesEntry = (Entry) itAllClassNamesMap.next();
                String className = (String) allClassNamesEntry.getKey();
                // get the OwObjectClass for that class name
                OwObjectClass objectClass = getContext().getNetwork().getObjectClass(className, null);
                String classDisplayName = objectClass.getDisplayName(getContext().getLocale());
                // get the collection of properties (a.k.a. index fields) for this object class
                Collection propertyClassNames = objectClass.getPropertyClassNames();
                Iterator itPropertyClassNames = propertyClassNames.iterator();

                String create = getContext().localize("owrole.OwStandardRoleManager.ContextCreateDisplayPrefix", "CREATE");
                String createPrefix = Integer.toString(ROLE_RESOURCE_CONTEXT_CREATE);
                String checkin = getContext().localize("owrole.OwStandardRoleManager.ContextCheckinDisplayPrefix", "CHECKIN");
                String checkinPrefix = Integer.toString(ROLE_RESOURCE_CONTEXT_CHECKIN);
                String view = getContext().localize("owrole.OwStandardRoleManager.ContextViewDisplayPrefix", "VIEW");
                String viewPrefix = Integer.toString(ROLE_RESOURCE_CONTEXT_VIEW);
                while (itPropertyClassNames.hasNext())
                {
                    String propertyClassName = (String) itPropertyClassNames.next();
                    // we need to resolve the property name to an OwPropertyClass to get the display name
                    OwPropertyClass propertyClass = objectClass.getPropertyClass(propertyClassName);
                    // get the display name
                    String propertyDisplayName = propertyClass.getDisplayName(getContext().getLocale());
                    String keyPostId = "." + className + "." + propertyClassName;
                    String valuePrefix = "@" + classDisplayName + "@";
                    String valuePostId = "." + classDisplayName + "." + propertyDisplayName;
                    // add this property as resource to the map
                    m_indexFieldsMap.put(createPrefix + keyPostId, valuePrefix + create + valuePostId);
                    m_indexFieldsMap.put(checkinPrefix + keyPostId, valuePrefix + checkin + valuePostId);
                    m_indexFieldsMap.put(viewPrefix + keyPostId, valuePrefix + view + valuePostId);
                }
            }
        }
        return (m_indexFieldsMap);
    }

    /**
     * 
     * @param locale_p
     * @param resourceId_p
     * @param category_p
     * @return the localized display name of the resource with the given id   
     * @since 4.0.0.0
     */
    protected abstract String dynamicResourceNameFromId(java.util.Locale locale_p, String resourceId_p, int category_p);

    public boolean isStaticResourceCategory(int category_p)
    {
        return ROLE_CATEGORY_STARTUP_FOLDER != category_p;
    }

    /**
     * 
     * @param category_p
     * @return  Collection of resource ids of dynamic resources associated with the given category  
     * @throws OwException
     * @since 4.0.0.0
     */
    protected abstract Collection<String> getDynamicResources(int category_p) throws OwException;

    /**
     * get all available resources for a given category
     *
     * @param category_p int category
     *
     * @return Map of String keys (resource IDs)
     */
    public Collection getResources(int category_p) throws Exception
    {
        switch (category_p)
        {
            case ROLE_CATEGORY_PLUGIN:
            {
                // === Plugins
                return getPluginCategoryMap().keySet();
            }

            case ROLE_CATEGORY_VIRTUAL_QUEUE:
            {
                // === GroupBoxes
                return getGroupBoxCategoryMap().keySet();
            }

            case ROLE_CATEGORY_SEARCH_TEMPLATE:
            {
                // === Search templates
                return getSearchTemplateCategoryMap().keySet();
            }

            case ROLE_CATEGORY_VIRTUAL_FOLDER:
            {
                // === Virtual folder
                return getVirtualFoldersCategoryMap().keySet();
            }

            case ROLE_CATEGORY_BPM_PROCESS_DEFINITION:
            {
                // === Virtual folder
                return getBpmProcessDefinitionsCategoryMap().keySet();
            }

            case ROLE_CATEGORY_STANDARD_FUNCTION:
            {
                // === Standard functions
                return getStandardFunctionResourceMap().keySet();
            }

            case ROLE_CATEGORY_DESIGN:
            {
                // === Designs
                return getDesignCategoryMap().keySet();
            }

            case ROLE_CATEGORY_SELECTIVE_CONFIGURATION:
            {
                // === Selective configurations
                return m_SelectiveConfigurationMap.keySet();
            }
            case ROLE_CATEGORY_OBJECT_CLASSES:
            {
                // === object classes
                return getObjectClassesMap().keySet();
            }

            case ROLE_CATEGORY_INDEX_FIELDS:
            {
                // === Selective configurations
                return getIndexFieldsMap().keySet();
            }
            case ROLE_CATEGORY_STARTUP_FOLDER:
            {
                // === Selective configurations
                return getDynamicResources(category_p);
            }
            default:
                String msg = "OwStandardRoleManager.getResources: Requested category not defined, category = " + category_p;
                LOG.error(msg);
                throw new OwObjectNotFoundException(msg);
        }
    }

    /** get a display name for the given category
     *
     * @param locale_p Locale to use
     * @param categorie_p Integer from getCategories() method
     * @param strID_p String resource id
     */
    public String getResourceDisplayName(java.util.Locale locale_p, int categorie_p, String strID_p)
    {
        try
        {
            switch (categorie_p)
            {
                case ROLE_CATEGORY_PLUGIN:
                {
                    // === Plugins
                    return getPluginCategoryMap().get(strID_p).toString();
                }

                case ROLE_CATEGORY_VIRTUAL_QUEUE:
                {
                    // === GroupBoxes
                    try
                    {
                        return ((OwSearchTemplate) getGroupBoxCategoryMap().get(strID_p)).getDisplayName(locale_p);
                    }
                    catch (Exception e)
                    {
                        return (strID_p);
                    }
                }

                case ROLE_CATEGORY_SEARCH_TEMPLATE:
                {
                    // === Search template 
                    try
                    {
                        return ((OwSearchTemplate) getSearchTemplateCategoryMap().get(strID_p)).getDisplayName(locale_p);
                    }
                    catch (Exception e)
                    {
                        return (strID_p);
                    }
                }

                case ROLE_CATEGORY_VIRTUAL_FOLDER:
                {
                    // === Search template 
                    try
                    {
                        String virtualFolder = getVirtualFoldersCategoryMap().get(strID_p);
                        String localizedVirtualFolder = OwString.localizeLabel(locale_p, virtualFolder);

                        return localizedVirtualFolder;
                    }
                    catch (Exception e)
                    {
                        LOG.error("Could not retrieve virtual folder display name ", e);
                        return (strID_p);
                    }
                }

                case ROLE_CATEGORY_BPM_PROCESS_DEFINITION:
                {
                    // === Search template 
                    try
                    {
                        OwWorkflowDescription description = getBpmProcessDefinitionsCategoryMap().get(strID_p);
                        String localizedName = OwString.localizeLabel(locale_p, description.getName());

                        return localizedName;
                    }
                    catch (Exception e)
                    {
                        LOG.error("Could not retrieve virtual folder display name ", e);
                        return (strID_p);
                    }
                }

                case ROLE_CATEGORY_STANDARD_FUNCTION:
                {
                    // === Standard functions
                    return ((OwString) getStandardFunctionResourceMap().get(strID_p)).getString(locale_p);
                }

                case ROLE_CATEGORY_DESIGN:
                {
                    // === Designs
                    return (String) getDesignCategoryMap().get(strID_p);
                }

                case ROLE_CATEGORY_SELECTIVE_CONFIGURATION:
                {
                    // === Selective configurations
                    return (String) m_SelectiveConfigurationMap.get(strID_p);
                }
                case ROLE_CATEGORY_OBJECT_CLASSES:
                {
                    // === Object classes
                    try
                    {
                        return (String) getObjectClassesMap().get(strID_p);
                    }
                    catch (Exception e)
                    {
                        return (strID_p);
                    }
                }

                case ROLE_CATEGORY_INDEX_FIELDS:
                {
                    // === Index fields
                    try
                    {
                        return (String) getIndexFieldsMap().get(strID_p);
                    }
                    catch (Exception e)
                    {
                        return (strID_p);
                    }
                }
                case ROLE_CATEGORY_STARTUP_FOLDER:
                {
                    // === startup folder
                    return dynamicResourceNameFromId(locale_p, strID_p, categorie_p);
                }
                default:
                    return strID_p;
            }
        }
        catch (NullPointerException e)
        {
            return strID_p;
        }

    }

    /**
     * Returns a map between the access right flag and the localized display name. All checked access right
     * flags are ORed together and form the int value of the access mask.
     * 
     * @param category_p the category to retrieve the flag map for
     * 
     * @return Map between Integer and String mapping the access mask flags to their display names
     */
    public Map getAccessMaskDescriptions(int category_p)
    {
        // create a HashMap for the results
        Map results = new HashMap();

        // fill map depending on category
        switch (category_p)
        {
            case ROLE_CATEGORY_PLUGIN:
            case ROLE_CATEGORY_VIRTUAL_QUEUE:
            case ROLE_CATEGORY_SEARCH_TEMPLATE:
            case ROLE_CATEGORY_VIRTUAL_FOLDER:
            case ROLE_CATEGORY_BPM_PROCESS_DEFINITION:
            case ROLE_CATEGORY_STANDARD_FUNCTION:
            case ROLE_CATEGORY_DESIGN:
            case ROLE_CATEGORY_SELECTIVE_CONFIGURATION:
                // this categories have no access mask flags. return empty list
                break;
            case ROLE_CATEGORY_OBJECT_CLASSES:
                results.put(Integer.valueOf(ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW), getContext().localize("app.OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_VIEW", "View"));
                results.put(Integer.valueOf(ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE), getContext().localize("app.OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CREATE", "Create"));
                results.put(Integer.valueOf(ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CHECKIN), getContext().localize("app.OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CHECKIN", "Check-in"));
                break;
            case ROLE_CATEGORY_INDEX_FIELDS:
                results.put(Integer.valueOf(ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW), getContext().localize("app.OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW", "View"));
                results.put(Integer.valueOf(ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_MODIFY), getContext().localize("app.OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_MODIFY", "Modify"));
                break;

            case ROLE_CATEGORY_STARTUP_FOLDER:
                results.put(Integer.valueOf(ROLE_ACCESS_MASK_FLAG_DYNAMIC_RESOURCE_MODIFY), getContext().localize("app.OwRoleManager.ROLE_ACCESS_MASK_FLAG_DYNAMIC_RESOURCE_MODIFY", "Modify"));
                break;
            default:
                // return empty list
                break;
        }

        // return results
        return results;
    }

    /**
     * Determine if the given role name is a global role or a mandator specific role.
     * @param rolename_p the name of the role to check
     * @return true = rolename_p is a global role false = rolename_p is a mandator specific role
     */
    public boolean isGlobalRole(String rolename_p)
    {
        return getContext().getMandatorManager().getGlobalRoleNames().contains(rolename_p);
    }

}