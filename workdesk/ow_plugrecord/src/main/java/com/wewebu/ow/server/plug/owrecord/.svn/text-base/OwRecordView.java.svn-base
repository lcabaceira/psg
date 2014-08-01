package com.wewebu.ow.server.plug.owrecord;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMainLayout;
import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwScript;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectTreeView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectTreeViewEXTJS;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.owrecord.log.OwLog;
import com.wewebu.ow.server.ui.OwTreeView;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Record View Module.
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
public class OwRecordView extends OwMasterView
{
    /**the AJAX update event name*/
    private static final String UPDATE_AJAX_EVENT_NAME = "Update";

    /** class logger */
    private static final Logger LOG = OwLog.getLogger(OwRecordView.class);

    /** overwrite the object tree view to use own styles */
    public class OwRecordObjectTreeView extends OwObjectTreeView
    {
        /**
         * Default constructor
         * @since 3.1.0.0
         */
        public OwRecordObjectTreeView()
        {

        }

        /**
         * get the style for the selected tree item
         */
        protected String getSelectedTreeItemStyle()
        {
            return "OwRecordTreeViewTextSelected";
        }

        /**
         * get the style for the tree item
         */
        protected String getTreeItemStyle()
        {
            return "OwRecordTreeViewText";
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.wewebu.ow.server.ui.OwTreeView#useFormEventUrls()
         */
        protected boolean useFormEventUrls()
        {
            try
            {
                OwSearchTemplate searchtemplate = ((OwRecordDocument) getDocument()).getSearchTemplate();
                if (searchtemplate != null)
                {
                    return ((OwRecordDocument) getDocument()).isSearchTemplateViewEnabled();
                }
                else
                {
                    // no search template at all: there will be no form.
                    return false;
                }
            }
            catch (Exception e)
            {
                return super.useFormEventUrls();
            }
        }

        /**
         * Overwrite navigate to trigger the JScript scrolling for TreeView.
         * Following Code is for favorites function, if a subfolder should be
         * opened.
         */
        public void navigate(Object obj_p, String strPath_p) throws Exception
        {
            super.navigate(obj_p, strPath_p);

            // trigger JScript only if DisplayMode is TreeView and a subnode is
            // selected
            if (getDisplayMode() == DISPLAY_MODE_TREE_NODES && m_root != m_selectedNode)
            {
                m_focusNode = m_selectedNode;

                m_selectedNode.toggleExpand();

                // send event to eventlistener
                if (!m_selectedNode.isCollapsed())
                {
                    if (m_eventlistner != null)
                    {
                        m_eventlistner.onTreeViewNavigateFolder(m_selectedNode);
                    }
                }
            }
        }

        /**
         * overwrite onRender to display the scroll div called when the view
         * should create its HTML content to be displayed
         * 
         * @param w_p
         *            Writer object to write HTML to
         */
        protected void onRender(Writer w_p) throws Exception
        {
            if (!isEmpty())
            {
                if (getDisplayMode() == DISPLAY_MODE_TREE_NODES)
                {
                    // render start of scrolling div
                    w_p.write("<div id=\"OwTreeView_");
                    w_p.write(String.valueOf(this.hashCode()));
                    w_p.write("\" class=\"OwTreeView\">");
                }
            }
            super.onRender(w_p);
            if (!isEmpty())
            {
                if (getDisplayMode() == DISPLAY_MODE_TREE_NODES)
                {
                    // render end of scrolling div
                    w_p.write("</div>");
                }
            }
            // insert JavaScript code to scroll the tree to the focus node
            if (getDisplayMode() == DISPLAY_MODE_TREE_NODES && m_root != m_selectedNode)
            {
                // Put this script to the back
                OwScript script_p = new OwScript("scrollElementToView('OwTreeViewNode_" + String.valueOf(m_selectedNode.hashCode()) + "','OwTreeView_" + String.valueOf(this.hashCode()) + "');", OwScript.DEFAULT_PRIORITY - 10);
                ((OwMainAppContext) getContext()).addFinalScript(script_p);
            }
        }

        /**
         * overwritten onClickPlusMinus to trigger the JScript scrolling handles
         * the navigate event i.e. a user has clicked a tree icon
         * 
         * @param request_p
         * @throws Exception
         */
        public void onClickPlusMinus(HttpServletRequest request_p) throws Exception
        {
            super.onClickPlusMinus(request_p);
        }

        /**
         * overwritten onClickLabel to trigger the JScript scrolling handles the
         * navigate event i.e. a user has clicked a tree icon
         * 
         * @param request_p
         * @throws Exception
         */
        public void onClickLabel(HttpServletRequest request_p) throws Exception
        {
            super.onClickLabel(request_p);
        }

    }

    /**
     * parameter name for the startup folder XML node in the plugin description,
     * used to determine the startup folder the plugin should automatically
     * load.
     */
    public static final String PLUGIN_PARAM_STARTUP_FOLDER = "StartupFolder";

    /** layout to be used for the view */
    private OwRecordSubLayout m_Layout;

    private OwRecordContentView m_documentList;

    private OwTreeView m_objectTreeView;

    private OwRecordRecordFunctionView m_recordFunctionsView;

    private OwRecordRecordFunctionDnDView m_recordFunctionsDnDView;

    /*controlling the loading/init behavior
     *@since 4.0.0.0*/
    private boolean postPoneInit;

    public OwRecordView()
    {
        postPoneInit = true;
    }

    /**
     * init the target after the context is set.
     */
    @SuppressWarnings("rawtypes")
    protected void init() throws Exception
    {
        super.init();

        // === attached layout
        m_Layout = createRecordSubLayout();
        addView(m_Layout, null);

        // === Function plugins
        if (((OwRecordDocument) getDocument()).getIsPluginsEnabled())
        {
            m_recordFunctionsDnDView = createRecordRecordFunctionDnDView();
            m_recordFunctionsView = createRecordRecordFunctionView();

            // add menu to layout
            m_Layout.addView(m_recordFunctionsDnDView, OwSubLayout.DND_REGION, null);
            m_Layout.addView(m_recordFunctionsView, OwSubLayout.MENU_REGION, null);
        }

        m_objectTreeView = createObjectTreeView();
        ((OwRecordDocument) getDocument()).setResultTreeView(m_objectTreeView);

        // create two wrapper views one for display mode "tree" and one for
        // display mode "register"
        OwRecordTreeView navigationViewTree = new OwRecordTreeView(m_objectTreeView, OwTreeView.DISPLAY_MODE_TREE_NODES);
        m_Layout.addView(navigationViewTree, OwSubLayout.NAVIGATION_REGION, null);
        OwRecordTreeView navigationViewRegister = new OwRecordTreeView(m_objectTreeView, OwTreeView.DISPLAY_MODE_REGISTER_NODES);
        m_Layout.addView(navigationViewRegister, OwRecordSubLayout.NAVIGATION_REGION_REGISTER_MODE, null);

        // add to first wrapper so we are registered too
        navigationViewTree.addView(m_objectTreeView, null);

        // get list of document functions from config
        List filteredDocumentFunctions = null;
        OwXMLUtil documentFunctionsNode = ((OwRecordDocument) getDocument()).getDocumentFunctionsNode();

        // documentFunctionsNode is not null and document functions are enabled
        // for this master plugin
        if (documentFunctionsNode != null && documentFunctionsNode.getSafeBooleanAttributeValue(OwRecordDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            // === filter document function plugins if filter list defined
            List docfunctionsList = documentFunctionsNode.getSafeStringList();
            // remove duplicated IDs
            Set docfunctions = new LinkedHashSet(docfunctionsList);
            if (docfunctions.size() != 0)
            {
                filteredDocumentFunctions = new LinkedList();
                // === use only defined functions
                Iterator it = docfunctions.iterator();
                while (it.hasNext())
                {
                    String id = (String) it.next();
                    // only add to array if it is an allowed function
                    if (((OwMainAppContext) getContext()).getConfiguration().isDocumentFunctionAllowed(id))
                    {
                        OwFunction func = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(id);
                        filteredDocumentFunctions.add(func);
                    }
                }
            }
        }

        // ==== Document list
        m_documentList = createRecordContentView();
        // filtered document functions is not null and document functions are
        // enabled for this master plugin
        if (filteredDocumentFunctions != null && documentFunctionsNode.getSafeBooleanAttributeValue(OwRecordDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            m_documentList.setDocumentFunctionPluginList(filteredDocumentFunctions);
        }
        // document functions are disabled for this master plugin
        if (documentFunctionsNode != null && !documentFunctionsNode.getSafeBooleanAttributeValue(OwRecordDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            m_documentList.setDocumentFunctionPluginList(new LinkedList());
        }

        m_Layout.addView(m_documentList, OwSubLayout.MAIN_REGION, null);

        // === add recent record set view, to display the last opened records
        if (getConfigNode().getSafeBooleanValue("RecentRecordList", true))
        {
            OwRecordSetView recentRecordSetView = createRecordSetView();
            // set the user specific object, where the RecordSet can serialize
            // to
            m_Layout.addView(recentRecordSetView, OwSubLayout.HOT_KEY_REGION, null);
        }

        // === add the search template view
        OwRecordSearchTemplateView stv = createRecordSearchTemplateView();
        m_Layout.addView(stv, OwRecordSubLayout.SEARCH_TEMPLATE_REGION, null);

        navigationViewTree.setExternalFormTarget(stv.getFormTarget());

        // === add the preview property view
        OwRecordPreviewPropertyView recordPreviewPropertyView = createRecordPreviewPropertyView();
        recordPreviewPropertyView.setDynamicSplitUsed(isDynamicSplitUsed());
        m_Layout.addView(recordPreviewPropertyView, OwRecordSubLayout.PREVIEW_PROPERTY_REGION, null);
        addAjaxUpdateZone(m_objectTreeView, OwRecordPreviewPropertyView.PREVIEW_PROPERTIES_DIV_ID);

        if (!isInitPostponed())
        {
            loadStartUpFolder();
        }
    }

    /**
     * Get the startup folder configuration, and browse/load specific parent
     * @throws Exception if could not read configuration or resolve path
     * @since 4.0.0.0
     */
    protected void loadStartUpFolder() throws Exception
    {
        // === check if we got a startup folder
        String startupFolderConfiguration = getConfigNode().getSafeTextValue(PLUGIN_PARAM_STARTUP_FOLDER, null);

        if (startupFolderConfiguration != null)
        {
            StringBuffer buffer = new StringBuffer(startupFolderConfiguration);
            String userName = getContext().getUserInfo().getUserName();

            OwString.replaceAll(buffer, "{username}", userName);

            String startupFolderPath = buffer.toString();

            OwObject startupFolder = null;
            try
            {
                startupFolder = OwEcmUtil.createObjectFromString((OwMainAppContext) getContext(), startupFolderPath);
            }
            catch (OwObjectNotFoundException e1)
            {
                LOG.error("Invalid startup folder configuration.", e1);

                buffer = new StringBuffer(startupFolderConfiguration);

                OwString.replaceAll(buffer, "{username}", "");
                OwString.replaceAll(buffer, "//", "/");

                startupFolderPath = buffer.toString();

                try
                {
                    startupFolder = OwEcmUtil.createObjectFromString((OwMainAppContext) getContext(), startupFolderPath);
                }
                catch (OwObjectNotFoundException e2)
                {
                    LOG.error("Invalid startup folder configuration.Could not proceed with ", e2);
                }

            }
            ((OwRecordDocument) getDocument()).openFolder(startupFolder, null);
        }
    }

    @Override
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        if (isInitPostponed())
        {
            if (null == ((OwRecordDocument) getDocument()).getCurrentRootFolder())
            {
                loadStartUpFolder();
            }
            setPostPoneInit(false);
        }
        super.onActivate(iIndex_p, oReason_p);
    }

    private OwRecordRecordFunctionDnDView createRecordRecordFunctionDnDView()
    {
        return new OwRecordRecordFunctionDnDView();
    }

    /**
     * Get the configuration value for parameter {@link OwRecordDocument#CONFIG_NODE_USE_DYNAMIC_SPLIT}.  
     * @return - the configured value for parameter {@link OwRecordDocument#CONFIG_NODE_USE_DYNAMIC_SPLIT}.
     * @since 3.1.0.0
     */
    private boolean isDynamicSplitUsed()
    {
        return getConfigNode().getSafeBooleanValue(OwRecordDocument.CONFIG_NODE_USE_DYNAMIC_SPLIT, false);
    }

    // === Overridable factory functions

    /**
     * overridable factory method to create the object tree view
     * 
     * @return the newly created {@link OwTreeView} object
     */
    protected OwTreeView createObjectTreeView() throws OwConfigurationException
    {
        OwTreeView result = null;
        OwXMLUtil treeViewConf = null;
        boolean isDynamicSplitUsed = isDynamicSplitUsed();
        try
        {
            treeViewConf = getConfigNode().getSubUtil("TreeView");
        }
        catch (Exception e)
        {
            result = new OwRecordObjectTreeView();
            ((OwRecordObjectTreeView) result).setMaxChildSize(((OwRecordDocument) getDocument()).getMaxChildSize());
            ((OwRecordObjectTreeView) result).setIsDynamicSplitUsed(isDynamicSplitUsed);
        }
        if (treeViewConf != null)
        {
            String className = treeViewConf.getSafeStringAttributeValue("classname", OwRecordObjectTreeView.class.getName());
            try
            {
                if (className.equals(OwRecordObjectTreeView.class.getName()))
                {
                    result = new OwRecordObjectTreeView();
                }
                else
                {
                    Class treeViewClass = Class.forName(className);
                    result = (OwTreeView) treeViewClass.newInstance();
                }
                if (result instanceof OwObjectTreeView)
                {
                    ((OwObjectTreeView) result).setMaxChildSize(((OwRecordDocument) getDocument()).getMaxChildSize());
                    ((OwObjectTreeView) result).setIsDynamicSplitUsed(isDynamicSplitUsed);
                }
                if (result instanceof OwObjectTreeViewEXTJS)
                {
                    OwObjectTreeViewEXTJS ajaxTree = (OwObjectTreeViewEXTJS) result;
                    boolean expandOnSelect = treeViewConf.getSafeBooleanValue("ExpandOnSelect", false);
                    ajaxTree.setExternalUpdateURL(this.getAjaxEventURL(UPDATE_AJAX_EVENT_NAME, null));

                    if (m_recordFunctionsDnDView != null && null != m_recordFunctionsDnDView.getDnD())
                    {
                        ajaxTree.setDnDAppletURL(this.m_recordFunctionsDnDView.getAjaxEventURL(OwRecordSubLayout.AJAX_UPDATE_DND_APPLET_REGION, null));
                    }

                    ajaxTree.setExpandOnSelect(expandOnSelect);
                    if (((OwRecordDocument) getDocument()).getIsPluginsEnabled() && m_recordFunctionsView != null)
                    {
                        //ajaxTree.addComponentUpdateURL(OwRecordSubLayout.OW_SUB_LAYOUT_MENU_DIV_ID, m_Layout.getAjaxEventURL(OwRecordSubLayout.AJAX_UPDATE_SUB_LAYOUT_MENU, null));
                        //                            ajaxTree.addComponentUpdateURL(OwRecordSubLayout.OW_REGION_DND_APPLET_DIV_ID, m_recordFunctionsDnDView.getAjaxEventURL(OwRecordSubLayout.AJAX_UPDATE_DND_APPLET_REGION, null));
                        ajaxTree.addComponentUpdateURL(OwRecordSubLayout.OW_REGION_MENU_DIV_ID, m_Layout.getAjaxEventURL(OwRecordSubLayout.AJAX_UPDATE_MENU_REGION, null));
                        ajaxTree.addComponentUpdateURL(OwRecordSubLayout.OW_REGION_HOTKEY_DIV_ID, m_Layout.getAjaxEventURL(OwRecordSubLayout.AJAX_UPDATE_HOTKEY_REGION, null));
                    }
                    ajaxTree.addComponentUpdateURL(OwRecordSubLayout.OW_REGION_SEARCHTEMPLATE_DIV_ID, m_Layout.getAjaxEventURL(OwRecordSubLayout.AJAX_UPDATE_SEARCH_TEMPLATES_REGION, null));

                    addAjaxUpdateZone(ajaxTree, OwMainLayout.ERROR_CONTAINER_ID);
                    addAjaxUpdateZone(ajaxTree, OwMainLayout.MESSAGE_CONTAINER_ID);
                    addAjaxUpdateZone(ajaxTree, OwMainLayout.KEY_INFO_CONTAINER_ID);
                    addAjaxUpdateZone(ajaxTree, OwMainLayout.REGISTERED_KEYS_SCRIPTS_CONTAINER_ID);
                }
            }
            catch (Exception e)
            {
                LOG.error("Cannot configure class: " + className, e);
                throw new OwConfigurationException(getContext().localize("owrecord.OwRecordView.cannotConfigureTreeclass", "Cannot configure tree view class"), e);
            }
        }
        if (result == null)
        {
            result = new OwRecordObjectTreeView();
            ((OwRecordObjectTreeView) result).setMaxChildSize(((OwRecordDocument) getDocument()).getMaxChildSize());
        }
        result.setChildrenSort(((OwRecordDocument) getDocument()).getFolderSortCriteria());
        return result;
    }

    /**
     * Add AJAX update zone only for AJAX based tree view.
     * If the tree view is not AJAX based, nothing is added.
     * @param ajaxTree_p - the AJAX based tree view
     * @param containerID_p - the ID of HTML container (usually the DIV id)
     * @since 3.1.0.0
     */
    private void addAjaxUpdateZone(OwTreeView ajaxTree_p, String containerID_p)
    {
        if (ajaxTree_p instanceof OwObjectTreeViewEXTJS)
        {
            OwObjectTreeViewEXTJS ajaxTree = (OwObjectTreeViewEXTJS) ajaxTree_p;
            String previewPropertiesUpdateURL = (((OwMainAppContext) getContext())).getAjaxUpdateURL(containerID_p);
            if (previewPropertiesUpdateURL != null)
            {
                ajaxTree.addComponentUpdateURL(containerID_p, previewPropertiesUpdateURL);
            }
        }
    }

    /**
     * overridable factory method to create content view
     * 
     * @return OwRecordContentView
     */
    protected OwRecordContentView createRecordContentView()
    {
        return new OwRecordContentView();
    }

    /**
     * overridable factory method to create search template view
     * 
     * @return OwRecordSearchTemplateView
     */
    protected OwRecordSearchTemplateView createRecordSearchTemplateView()
    {
        return new OwRecordSearchTemplateView();
    }

    /**
     * overridable factory method to create preview properties view
     * 
     * @return OwRecordPreviewPropertyView
     */
    protected OwRecordPreviewPropertyView createRecordPreviewPropertyView()
    {
        return new OwRecordPreviewPropertyView();
    }

    /**
     * overridable factory method to create record set view
     * 
     * @return OwRecordSetView
     */
    protected OwRecordSetView createRecordSetView()
    {
        return new OwRecordSetView();
    }

    /**
     * overridable factory method to create record function view
     * 
     * @return OwRecordRecordFunctionView
     */
    protected OwRecordRecordFunctionView createRecordRecordFunctionView()
    {
        return new OwRecordRecordFunctionView();
    }

    /**
     * overridable factory method to create record sub layout
     * 
     * @return OwRecordSubLayout
     */
    protected OwRecordSubLayout createRecordSubLayout()
    {
        return new OwRecordSubLayout();
    }

    /**
     * Handler for AJAX request made from EXTJS tree.
     * @param request_p - the AJAX request object.
     * @param response_p - the response
     * @throws Exception
     * @since 3.1.0.0
     */
    public void onAjaxUpdate(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        PrintWriter writer = response_p.getWriter();
        String nodeId = request_p.getParameter("owNodeId");
        if (nodeId != null)
        {
            //reset error
            ((OwMainAppContext) getContext()).setError(null);
            //reset messages
            ((OwMainAppContext) getContext()).resetMessages();
            m_objectTreeView.navigateToId(nodeId);
            m_documentList.render(writer);

        }
        else
        {
            response_p.setStatus(405);
            writer.write("Cannot navigate!");
        }
        writer.close();
    }

    /**
     * Define StartupFolder setting initialization:
     * <ul>
     * <li><b>false</b>: handle start up folder on init call</li>
     * <li><b>true</b>: handle start up folder on first onActivate call</li>
     * </ul>
     * @param postPone_p boolean
     * @since 4.0.0.0
     */
    public void setPostPoneInit(boolean postPone_p)
    {
        this.postPoneInit = postPone_p;
    }

    /**
     * Is startup folder handling postponed.
     * <p>By default it is true.</p>
     * @return boolean
     * @since 4.0.0.0
     * @see #setPostPoneInit(boolean)
     */
    public boolean isInitPostponed()
    {
        return this.postPoneInit;
    }
}