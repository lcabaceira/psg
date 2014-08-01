package com.wewebu.ow.server.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwStandardRoleManager;
import com.wewebu.ow.server.servlets.OwConfigurationInitialisingContextListener;
import com.wewebu.ow.server.servlets.OwServerContext;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Utility class to handle adaptor selection.
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
 *@since 3.2.0.0
 */
public class OwAdaptor
{
    private static final String SAVE_NODES_PARAMETER_NAME = "saveNodes";
    private static final String GET_EDIT_NODES_PARAMETER_NAME = "getEditNodes";
    private static final String LIST_SAVED_FILES_PARAMETER_NAME = "listSavedFiles";
    private static final String CURRENT_ADAPTOR_PARAMETER_NAME = "currentAdaptor";
    private static final String EDIT_FIELD_PREFIX_PARAM_NAME = "editField";

    private static final String OW_EDITABLE_BOOTSTRAP_NODES_PATH = "/ow_editable_bootstrap_nodes.owd";
    private static final String EMPTY_PATH_SUFFIX = "";
    private static final String DEPLOY_PREFIX = "deploy#";
    private static final String RESOURCE_FILE_PATH_PARAMETER_NAME = "resourceFilePath";

    private static String[][] adaptors_dev = { //
    { "contractmanagement_dev (abs-alfonecmg.alfresco.com:8080 | Alfresco 4.2 | development)", "deploy#WEB-INF/conf/dev/contractmanagement_dev" }, //            
            { "opencmis_alfrescobpm_dev (abs-alfone.alfresco.com | Alfresco 4.2 | development)", "deploy#WEB-INF/conf/dev/opencmis_alfrescobpm_dev" }, //
            { "opencmis_prof_cmg_dev (abs-alfone.alfresco.com | development)", "deploy#WEB-INF/conf/dev/opencmis_prof_cmg_dev" }, //
            { "opencmis_prof_dev (abs-alfone.alfresco.com | development)", "deploy#WEB-INF/conf/dev/opencmis_prof_dev" }, //
            { "opencmis_fncm5_dev (abs-fncm52.alfresco.com | development)", "deploy#WEB-INF/conf/dev/opencmis_fncm5_dev" }, // 
            { "fncm5_dev (abs-fncm52.alfresco.com:9080 | basic data model | development)", "deploy#WEB-INF/conf/dev/fncm5_dev" } //
    };

    private static String[][] adaptors = { //
    { "contractmanagement (localhost:8080 | Contract Management Solution | only Alfresco 4.2)", "deploy#WEB-INF/conf/templates/contractmanagement" }, //
            { "Dummy Adaptor", "deploy#WEB-INF/conf/reference/dummy" }, //
            { "fncm5 (abs-fncm52.alfresco.com:9080 | basic data model | only for enterprise)", "deploy#WEB-INF/conf/reference/fncm5" }, //
            { "opencmis (localhost:8080 | basic data model | all editions)", "deploy#WEB-INF/conf/reference/opencmis" }, //
            { "opencmis_hr (localhost:8080 | HR Scenario | all editions)", "deploy#WEB-INF/conf/templates/opencmis_hr" }, //
    };

    private static String[][] adaptors_cross = { //
    { "ocmis_alfresco_x_ocmis_alfresco_dev (localhost + abs-alfone.alfresco.com | development)", "deploy#WEB-INF/conf/dev/ocmis_alfresco_x_ocmis_alfresco_dev" }, // 
            { "ocmis_alfresco_x_ocmis_fncm5_dev (abs-fncm52.alfresco.com + abs-alfone.alfresco.com | development)", "deploy#WEB-INF/conf/dev/ocmis_alfresco_x_ocmis_fncm5_dev" } //
    };

    /** class logger*/
    private static final Logger LOG = OwLogCore.getLogger(OwAdaptor.class);
    /** a map with existing adaptor paths and display values*/
    private static Map<String, String> AVAILABLE_ADAPTORS = null;

    private static final DateFormat JAVASCRIPT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private ServletContext servletContext;
    private String basePath;
    /** cache with nodes to be editable for each adaptor*/
    private Map<String, List<String>> currentEditableNodes = new HashMap<String, List<String>>();
    private Map<String, OwRequestHandlerStrategy> requestHandlers;

    /**
     *<p>
     * Strategy for handling different requests.
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
     *@since 3.2.0.0
     */
    public interface OwRequestHandlerStrategy
    {
        /**
         * Process the request.
         * @param request_p
         * @param response_p
         * @param owApplication_p
         * @throws Exception
         */
        public void processRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception;

        /**
         * Get the request parameter name associated with this request processor.
         * @return - the parameter name associated with this request processor.
         */
        public String getParameterId();
    }

    /**
     * @since 4.1.1.0
     */
    private synchronized void initAdaptor()
    {
        AVAILABLE_ADAPTORS = new LinkedHashMap<String, String>();

        for (int i = 0; i < adaptors_dev.length; i++)
        {
            AVAILABLE_ADAPTORS.put(adaptors_dev[i][1], adaptors_dev[i][0]);
        }
        for (int j = 0; j < adaptors.length; j++)
        {
            AVAILABLE_ADAPTORS.put(adaptors[j][1], adaptors[j][0]);
        }
        for (int k = 0; k < adaptors_cross.length; k++)
        {
            AVAILABLE_ADAPTORS.put(adaptors_cross[k][1], adaptors_cross[k][0]);
        }
    }

    /**
     *<p>
     * Strategy for handling "Start OWD" button request
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
     *@since 3.2.0.0
     */
    public static class OwApplicationStarterStrategy implements OwRequestHandlerStrategy
    {
        /** the parameter id*/
        private String parameterID = RESOURCE_FILE_PATH_PARAMETER_NAME;

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwRequestHandlerStrategy#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.wewebu.ow.server.ui.OwWebApplication)
         */
        public void processRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception
        {
            String resourceFilePathStr = request_p.getParameter(parameterID);
            HttpSession session = request_p.getSession(true);
            ServletContext application = session.getServletContext();
            String selectedBootstrap = request_p.getParameter("selectedBootstrap");
            if (selectedBootstrap != null)
            {
                application.setAttribute("BOOTSTRAP_IN_USE", selectedBootstrap);
            }
            if (null != resourceFilePathStr)
            {
                session.invalidate();

                OwServerContext owServerContext = (OwServerContext) application.getAttribute(OwServerContext.ATT_NAME);
                owServerContext.setResourceFilePath(resourceFilePathStr);

                OwConfigurationInitialisingContextListener baseInitializerConfiguration = new OwConfigurationInitialisingContextListener();
                baseInitializerConfiguration.init(application);

                OwConfiguration.applicationInitalize(baseInitializerConfiguration);
                OwStandardRoleManager.applicationInitalize(baseInitializerConfiguration);

                owApplication_p.checkSessionObjects(application, request_p, response_p, request_p.getSession(true));

                response_p.sendRedirect("./");
            }
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwRequestHandlerStrategy#getParameterId()
         */
        public String getParameterId()
        {
            return parameterID;
        }
    }

    /**
     *<p>
     * Strategy for handling an AJAX request
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
     *@since 3.2.0.0
     */
    public abstract class OwAjaxRequestHandlerStrategy implements OwRequestHandlerStrategy
    {
        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwRequestHandlerStrategy#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.wewebu.ow.server.ui.OwWebApplication)
         */
        public void processRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception
        {
            try
            {
                String ajaxResponse = processAjaxRequest(request_p, response_p, owApplication_p);
                writeAjaxResponse(response_p, ajaxResponse);
            }
            catch (Exception e)
            {
                handleAjaxRequestException(request_p, response_p, owApplication_p, e);
            }
        }

        /**
         * Handler AJAX request exception.
         * @param request_p
         * @param response_p
         * @param owApplication_p
         * @param e_p - the exception
         */
        private void handleAjaxRequestException(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p, Exception e_p) throws Exception
        {
            response_p.setStatus(500);
            String errMsg = e_p.getLocalizedMessage() == null ? "Cannot resolve your request" : e_p.getLocalizedMessage();
            response_p.getWriter().write(errMsg);
        }

        /**
         * Execute the AJAX request
         * @param request_p
         * @param response_p
         * @param owApplication_p
         * @return the processed Ajax response
         */
        protected abstract String processAjaxRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception;

        /**
         * Write AJAX response to the {@link HttpServletResponse} writer object.
         * @param response_p
         * @param text_p
         * @throws IOException
         */
        private void writeAjaxResponse(HttpServletResponse response_p, String text_p) throws IOException
        {
            response_p.getWriter().write(text_p);
            response_p.getWriter().close();
        }

    }

    /**
     *<p>
     * Strategy for handling "list saved files" request.
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
     *@since 3.2.0.0
     */
    public class OwListSavedFilesStrategy extends OwAjaxRequestHandlerStrategy
    {
        /** the id*/
        private String id = LIST_SAVED_FILES_PARAMETER_NAME;

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwAjaxRequestHandlerStrategy#processAjaxRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.wewebu.ow.server.ui.OwWebApplication)
         */
        @Override
        protected String processAjaxRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception
        {
            String currentAdaptor = request_p.getParameter(CURRENT_ADAPTOR_PARAMETER_NAME);
            if (currentAdaptor == null)
            {
                currentAdaptor = getSelectedAdaptor();
            }
            else
            {
                List<String> currentEditableNodesAsList = currentEditableNodes.get(currentAdaptor);
                if (currentEditableNodesAsList == null)
                {
                    currentEditableNodes.put(currentAdaptor, readEditableNodes(currentAdaptor));
                }
            }
            return getSavedFiles(currentAdaptor);
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwRequestHandlerStrategy#getParameterId()
         */
        public String getParameterId()
        {
            return id;
        }

    }

    /**
     *<p>
     * Strategy for handling "get editable nodes" request.
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
     *@since 3.2.0.0
     */
    public class OwGetEditNodesStrategy extends OwAjaxRequestHandlerStrategy
    {
        /** the id*/
        private String id = GET_EDIT_NODES_PARAMETER_NAME;

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwAjaxRequestHandlerStrategy#processAjaxRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.wewebu.ow.server.ui.OwWebApplication)
         */
        @Override
        protected String processAjaxRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception
        {
            String currentAdaptor = request_p.getParameter(CURRENT_ADAPTOR_PARAMETER_NAME);
            if (currentAdaptor == null)
            {
                currentAdaptor = getSelectedAdaptor();
            }
            String currentFile = "/" + request_p.getParameter("currentFile");
            return getEditNodes(currentAdaptor, currentFile);
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwRequestHandlerStrategy#getParameterId()
         */
        public String getParameterId()
        {
            return id;
        }
    }

    /**
     *<p>
     * Strategy for handling "save file" request.
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
     *@since 3.2.0.0
     */
    public class OwSaveFileStrategy extends OwAjaxRequestHandlerStrategy
    {
        /** the id*/
        private String id = SAVE_NODES_PARAMETER_NAME;

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwAjaxRequestHandlerStrategy#processAjaxRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.wewebu.ow.server.ui.OwWebApplication)
         */
        @SuppressWarnings("unchecked")
        @Override
        protected String processAjaxRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception
        {
            String currentAdaptor = request_p.getParameter(CURRENT_ADAPTOR_PARAMETER_NAME);
            String selectedBootstrap = request_p.getParameter("selectedBootstrap");
            String identifier = request_p.getParameter("identifier");
            List<String> currentEditableNodesList = readEditableNodes(currentAdaptor);
            Map paramMap = request_p.getParameterMap();
            Iterator paramNamesIterator = paramMap.keySet().iterator();
            Document currentDocument = getCurrentDocument(currentAdaptor, "/" + selectedBootstrap);
            XPath xpath = createXPath();
            while (paramNamesIterator.hasNext())
            {
                String param = (String) paramNamesIterator.next();
                if (param.startsWith(EDIT_FIELD_PREFIX_PARAM_NAME))
                {
                    String paramIndex = param.substring(EDIT_FIELD_PREFIX_PARAM_NAME.length());
                    try
                    {
                        int index = Integer.parseInt(paramIndex);

                        if (currentDocument != null)
                        {

                            String editableNode = currentEditableNodesList.get(index);
                            if (editableNode != null)
                            {
                                Node configNode = (Node) xpath.evaluate(editableNode, currentDocument, XPathConstants.NODE);
                                if (configNode == null)
                                {
                                    configNode = createMissingNode(currentDocument, editableNode);
                                }
                                NodeList children = configNode.getChildNodes();
                                for (int i = 0; i < children.getLength(); i++)
                                {
                                    Node textNode = children.item(i);
                                    if (textNode.getNodeType() == Node.TEXT_NODE)
                                    {
                                        configNode.removeChild(textNode);
                                    }
                                }
                                Text updatedTextNode = currentDocument.createTextNode(request_p.getParameter(param));
                                updatedTextNode.normalize();
                                configNode.appendChild(updatedTextNode);
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        LOG.error("Cannot change the DOM.", e);
                        throw e;
                    }
                }
            }
            Date fileNameDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

            String shortFileName = "owbootstrap_" + formatter.format(fileNameDate) + ".xml";
            String fileName = buildFullPath("/" + shortFileName, currentAdaptor);
            // TODO more validations here
            File savedFile = new File(fileName);
            boolean fileSaved = OwXMLDOMUtil.toFile(savedFile, currentDocument);
            String fullPath = buildFullPath(EMPTY_PATH_SUFFIX, currentAdaptor);
            if (fileSaved)
            {
                OwBootstrapToIdMapping mappings = OwBootstrapToIdMapping.getInstance();
                mappings.writeMappings(fullPath, shortFileName, identifier);
            }

            File configDir = new File(fullPath);
            File[] savedfiles = getSavedFiles(configDir);

            StringBuilder result = new StringBuilder("[");
            result.append(savedfiles.length);
            result.append(",'");
            result.append(shortFileName);
            result.append("','");
            result.append(OwHTMLHelper.encodeJavascriptString(OwHTMLHelper.encodeToSecureHTML(identifier)));
            result.append("','");
            result.append(JAVASCRIPT_DATE_FORMAT.format(new Date(savedFile.lastModified())));
            result.append("']");

            return result.toString();

        }

        /**
         * Create the missing node.
         * @param currentDocument_p
         * @param editableNode_p
         * @return the newly created node
         * @throws Exception
         */
        private Node createMissingNode(Document currentDocument_p, String editableNode_p) throws Exception
        {
            String[] nodeNames = editableNode_p.split("/");
            XPath xpath = createXPath();
            Node lastNode = currentDocument_p.getDocumentElement();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < nodeNames.length; i++)
            {
                builder.append("/");
                builder.append(nodeNames[i]);
                Node configNode = (Node) xpath.evaluate(builder.toString(), currentDocument_p, XPathConstants.NODE);
                if (configNode == null)
                {
                    Node theNode = currentDocument_p.createElement(nodeNames[i]);
                    lastNode.appendChild(theNode);
                    lastNode = theNode;
                }
                else
                {
                    lastNode = configNode;
                }
            }
            return lastNode;
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwAdaptor.OwRequestHandlerStrategy#getParameterId()
         */
        public String getParameterId()
        {
            return id;
        }

    }

    /**
     * Constructor
     */
    public OwAdaptor()
    {
        requestHandlers = new HashMap<String, OwRequestHandlerStrategy>();
        registerRequestHandlerStrategy(new OwApplicationStarterStrategy());
        registerRequestHandlerStrategy(new OwListSavedFilesStrategy());
        registerRequestHandlerStrategy(new OwGetEditNodesStrategy());
        registerRequestHandlerStrategy(new OwSaveFileStrategy());
    }

    /**
     * Register a request handler strategy
     * @param strategy_p - request handler strategy.
     */
    private void registerRequestHandlerStrategy(OwRequestHandlerStrategy strategy_p)
    {
        OwRequestHandlerStrategy oldStrategy = requestHandlers.put(strategy_p.getParameterId(), strategy_p);
        if (oldStrategy != null)
        {
            throw new RuntimeException("Cannot register more than one request handler strategy for id: " + strategy_p.getParameterId());
        }
    }

    /**
     * Get the available adaptors as a JavaScript array.
     * @return the available adaptors as a JavaScript array.
     */
    public String getAvailableAdators() throws Exception
    {
        return buildAdaptorsJSArray(extractAvailableAdaptors());
    }

    private Map<String, String> extractAvailableAdaptors() throws Exception
    {
        if (AVAILABLE_ADAPTORS == null)
        {
            initAdaptor();
        }
        return filterAdaptors(AVAILABLE_ADAPTORS, EMPTY_PATH_SUFFIX);
    }

    /**
     * Build the JavaScript array from adaptors
     * @param adaptorsMap_p 
     * @return the JavaScript array from adaptors
     */
    private static String buildAdaptorsJSArray(Map<String, String> adaptorsMap_p)
    {
        StringBuilder builder = new StringBuilder("[");
        Set<Entry<String, String>> entries = adaptorsMap_p.entrySet();
        Iterator<Entry<String, String>> entriesIterator = entries.iterator();
        while (entriesIterator.hasNext())
        {
            Map.Entry<java.lang.String, java.lang.String> entry = entriesIterator.next();
            builder.append("[");
            builder.append("'");
            builder.append(entry.getKey());
            builder.append("','");
            builder.append(entry.getValue());
            builder.append("'");
            builder.append("]");
            if (entriesIterator.hasNext())
            {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Get the default selected adaptor
     * @return the default selected adaptor
     * @throws Exception 
     */
    public String getSelectedAdaptor() throws Exception
    {
        String selectedAdaptor = "";
        Map<String, String> availableAdaptors = extractAvailableAdaptors();
        if (availableAdaptors.size() > 0)
        {
            selectedAdaptor = availableAdaptors.keySet().iterator().next();
        }
        return selectedAdaptor;
    }

    /**
     * Get the base URL for the adaptor.jsp.
     * @param request_p - the request object
     * @return the base URL for the adaptor.jsp.
     */
    public String getBaseURL(HttpServletRequest request_p)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(request_p.getScheme());
        builder.append("://");
        builder.append(request_p.getServerName());
        builder.append(":");
        builder.append(request_p.getServerPort());
        builder.append(request_p.getRequestURI());
        return builder.toString();
    }

    /**
     * Initialize the application
     * @param request_p
     * @param owApplication_p
     * @throws Exception
     */
    public void initialize(HttpServletRequest request_p, com.wewebu.ow.server.ui.OwWebApplication owApplication_p) throws Exception
    {
        request_p.setCharacterEncoding("UTF-8");
        HttpSession session = request_p.getSession(true);
        servletContext = session.getServletContext();
        basePath = servletContext.getRealPath("/");
        owApplication_p.setContextClass(com.wewebu.ow.server.app.OwMainAppContext.class);
        owApplication_p.setViewClass(com.wewebu.ow.server.app.OwMainLayout.class);
        owApplication_p.setLoginClass(com.wewebu.ow.server.app.OwLoginView.class);
        owApplication_p.setJspPath("main.jsp");
    }

    /**
     * Process the current request
     * @param request_p
     * @param response_p
     * @param owApplication_p
     * @throws Exception
     */

    public void processRequest(HttpServletRequest request_p, HttpServletResponse response_p, OwWebApplication owApplication_p) throws Exception
    {
        Set<String> registerParams = requestHandlers.keySet();
        for (String id : registerParams)
        {
            if (request_p.getParameter(id) != null)
            {
                requestHandlers.get(id).processRequest(request_p, response_p, owApplication_p);
            }
        }
    }

    /**
     * Get the saved configuration files for given adaptor
     * @param currentAdaptor_p - the adaptor
     * @return - EXTJS array as string.
     */
    private String getSavedFiles(String currentAdaptor_p)
    {
        // template:
        //"[[0,'owbootstrap.xml','01.01.2011']]"
        StringBuilder result = new StringBuilder();
        String fullPath = buildFullPath(EMPTY_PATH_SUFFIX, currentAdaptor_p);
        File configDir = new File(fullPath);
        if (!configDir.exists() || !configDir.isDirectory())
        {
            throw new RuntimeException("TODO - files!!!");
        }
        else
        {
            File[] savedFiles = getSavedFiles(configDir);
            result.append("[");
            OwBootstrapToIdMapping mapping = OwBootstrapToIdMapping.getInstance();
            for (int i = 0; i < savedFiles.length; i++)
            {
                result.append("[");
                result.append(i);
                result.append(",'");
                result.append(savedFiles[i].getName());
                result.append("','");
                String identifier = mapping.getIdentifier(fullPath, savedFiles[i].getName());
                if (identifier == null)
                {
                    identifier = "";
                }
                try
                {
                    result.append(OwHTMLHelper.encodeJavascriptString(OwHTMLHelper.encodeToSecureHTML(identifier)));
                }
                catch (IOException e)
                {
                    result.append("");
                }
                result.append("','");
                result.append(JAVASCRIPT_DATE_FORMAT.format(new Date(savedFiles[i].lastModified())));
                result.append("']");
                if (i != savedFiles.length - 1)
                {
                    result.append(",");
                }
            }
            result.append("]");
        }
        return result.toString();
    }

    /**
     * Get the list of files from the given config dir.
     * @param configDir_p - the config dir
     * @return the list of files from the given config dir.
     */
    private File[] getSavedFiles(File configDir_p)
    {
        File[] savedFiles = configDir_p.listFiles(new FileFilter() {

            public boolean accept(File file_p)
            {
                boolean result = false;
                String fileName = file_p.getName();
                if (fileName.startsWith("owbootstrap") && fileName.endsWith(".xml"))
                {
                    result = true;
                }
                return result;
            }
        });
        Arrays.sort(savedFiles, new Comparator<File>() {

            public int compare(File file1_p, File file2_p)
            {
                if (file1_p.getName().equals("owbootstrap.xml"))
                {
                    return -1;
                }
                if (file2_p.getName().equals("owbootstrap.xml"))
                {
                    return 1;
                }
                return (int) (file1_p.lastModified() - file2_p.lastModified());
            }
        });
        return savedFiles;
    }

    /**
     * Get the adaptor paths that have configured editable nodes.
     * @return the adaptor paths that have configured editable nodes, as a String.
     * @throws Exception
     */
    public String getEditableAdaptors() throws Exception
    {
        if (AVAILABLE_ADAPTORS == null)
        {
            initAdaptor();
        }
        Map<String, String> configuredAdaptors = filterAdaptors(AVAILABLE_ADAPTORS, OW_EDITABLE_BOOTSTRAP_NODES_PATH);
        Set<String> keys = configuredAdaptors.keySet();
        return keys.toString();
    }

    /**
     * Get the nodes (as an EXTJS array) specified in {@link OwAdaptor#OW_EDITABLE_BOOTSTRAP_NODES_PATH} file from owbootstrap.xml. 
     * @param adaptor_p - the adaptor
     * @param selectedBootstrap_p - the selected file (from where to read)
     * @return an EXTJS string array with nodes title and value.
     * @throws Exception
     */
    public String getEditNodes(String adaptor_p, String selectedBootstrap_p) throws Exception
    {
        if (!getEditableAdaptors().contains(adaptor_p))
        {
            return "[['','']]";
        }
        if (selectedBootstrap_p == null)
        {
            selectedBootstrap_p = "/owbootstrap.xml";
        }
        List<String> currentEditableNodesList = readEditableNodes(adaptor_p);
        Document currentDocumentDOM = getCurrentDocument(adaptor_p, selectedBootstrap_p);
        XPath xpath = createXPath();
        List<String> currentEditableValues = new ArrayList<String>(currentEditableNodesList.size());
        for (String editableNode : currentEditableNodesList)
        {
            Node configNode = (Node) xpath.evaluate(editableNode, currentDocumentDOM, XPathConstants.NODE);
            StringBuilder nodeValueBuilder = new StringBuilder();
            if (configNode != null)
            {
                NodeList children = configNode.getChildNodes();
                for (int i = 0; i < children.getLength(); i++)
                {
                    Node node = children.item(i);
                    if (node.getNodeType() == Node.TEXT_NODE)
                    {
                        nodeValueBuilder.append(node.getNodeValue());
                    }
                }
            }
            String value = nodeValueBuilder.toString().trim();
            currentEditableValues.add(value);
        }
        return buildEditNodes(currentEditableNodesList, currentEditableValues);// buildMockEditNodes();

    }

    private XPath createXPath()
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        return xpath;
    }

    private Document getCurrentDocument(String adaptor_p, String selectedBootstrap_p) throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        String fullPath = buildFullPath(selectedBootstrap_p, adaptor_p);
        File f = new File(fullPath);
        Document currentDocumentDOM = docBuilder.parse(f);
        return currentDocumentDOM;
    }

    /**
     * Read the specified nodes in the {@link OwAdaptor#OW_EDITABLE_BOOTSTRAP_NODES_PATH} file.
     * @param adaptor_p - the selected adaptor
     * @return - a {@link List} with nodes to be read from configuration.
     */
    private List<String> readEditableNodes(String adaptor_p)
    {
        List<String> result = new LinkedList<String>();
        if (currentEditableNodes.get(adaptor_p) == null)
        {
            String nodesPath = buildFullPath(OW_EDITABLE_BOOTSTRAP_NODES_PATH, adaptor_p);

            File nodesFile = new File(nodesPath);
            if (nodesFile.exists())
            {
                BufferedReader reader = null;
                try
                {
                    reader = new BufferedReader(new FileReader(nodesFile));
                    String line;
                    do
                    {
                        line = reader.readLine();
                        if (line != null)
                        {
                            result.add(line.trim());
                        }
                    } while (line != null);
                }
                catch (IOException e)
                {
                    LOG.debug("Error reading configurable nodes", e);
                }
                finally
                {
                    if (reader != null)
                    {
                        try
                        {
                            reader.close();
                        }
                        catch (IOException e)
                        {
                        }
                        reader = null;
                    }
                }
            }
        }
        else
        {
            result = currentEditableNodes.get(adaptor_p);
        }
        return result;
    }

    /**
     * Build editable nodes as an EXTJS array string.
     * @param xpathNodes_p - the nodes
     * @param xPathNodesValues_p - the values
     * @return - specified nodes as an EXTJS array string.
     */
    private String buildEditNodes(List<String> xpathNodes_p, List<String> xPathNodesValues_p)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < xpathNodes_p.size(); i++)
        {
            if (i == 0)
            {
                builder.append("[");
            }
            builder.append("[");
            builder.append(i);
            builder.append(",'");
            //tooltip
            String xPathString = xpathNodes_p.get(i);
            builder.append(xPathString);
            builder.append("','");
            //display name
            String displayName = xPathString.substring(xPathString.lastIndexOf("/") + 1, xPathString.length());
            builder.append(displayName);
            builder.append("','");
            builder.append(xPathNodesValues_p.get(i));
            builder.append("']");
            if (i < xpathNodes_p.size() - 1)
            {
                builder.append(",");
            }
            else
            {
                builder.append("]");
            }
        }

        return builder.toString();
    }

    /**
     * Filter the adaptors map.
     * @param adaptorsMap_p - original map
     * @param pathSuffix_p - path suffix
     * @return the filtered adaptor map
     * @throws Exception
     */
    private Map<String, String> filterAdaptors(Map<String, String> adaptorsMap_p, String pathSuffix_p) throws Exception
    {
        if (AVAILABLE_ADAPTORS == null)
        {
            initAdaptor();
        }
        Map<String, String> result = new LinkedHashMap<String, String>();
        Set<String> paths = adaptorsMap_p.keySet();

        for (String path : paths)
        {
            String filePath = buildFullPath(pathSuffix_p, path);
            File f = new File(filePath);
            if (f.exists())
            {
                result.put(path, AVAILABLE_ADAPTORS.get(path));
            }
        }
        return result;
    }

    /**
     * Create the full path from the given path
     * @param pathSuffix_p - sufix
     * @param adaptorPath_p - adaptor path
     * @return - the full path for given adaptor and suffix.
     */
    private String buildFullPath(String pathSuffix_p, String adaptorPath_p)
    {
        String relativePath = buildRelativePath(adaptorPath_p + pathSuffix_p);
        String filePath = basePath + relativePath;
        return filePath;
    }

    /**
     * Create the relative path from the adaptor path
     * @param path_p - adaptor path
     * @return - the full path for given adaptor and suffix.
     */
    private String buildRelativePath(String path_p)
    {
        String relativePath = path_p.substring(DEPLOY_PREFIX.length(), path_p.length());
        return relativePath;
    }
}
