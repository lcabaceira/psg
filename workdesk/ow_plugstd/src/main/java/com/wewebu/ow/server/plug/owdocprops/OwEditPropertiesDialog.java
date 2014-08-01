package com.wewebu.ow.server.plug.owdocprops;

import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwMultiViewNavigation;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.OwFormPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.OwGroupPropertiesConfiguration;
import com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.OwStandardPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.views.OwAllLinksView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectAccessRightsView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectEditVersionsView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectFiledRecordsView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectHistoryView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectLinksDocument;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectLinksView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.OwTypedLinksView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwJspConfigurable;
import com.wewebu.ow.server.ui.preview.OwPreview;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the Document edit properties Dialog.
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
public class OwEditPropertiesDialog extends OwStandardSequenceDialog implements OwClientRefreshContext, OwObjectListView.OwObjectListViewEventListner, OwJspConfigurable
{

    /** query string key for the plugin index. */
    protected static final String PLUG_INDEX_KEY = "pi";

    /** mask value for the properties view */
    public static final int VIEW_MASK_PROPERTIES = 0x0001;
    /** mask value for the system properties view */
    public static final int VIEW_MASK_SYSTEM_PROPERTIES = 0x0002;
    /** mask value for the access rights view */
    public static final int VIEW_MASK_ACCESS_RIGHTS = 0x0004;
    /** mask value for the versions view */
    public static final int VIEW_MASK_VERSIONS = 0x0008;
    /** mask value for the history view */
    public static final int VIEW_MASK_HISTORY = 0x0010;
    /** mask value for the filed in view */
    public static final int VIEW_MASK_FILED_IN = 0x0020;
    /** mask value for the document functions menu view */
    public static final int VIEW_MASK_DOCUMENT_FUNCTIONS = 0x0040;

    /** mask value to enable paste metadata in property view */
    public static final int VIEW_MASK_ENABLE_PASTE_METADATA = 0x0080;

    /** mask value to use preview mode for auto open */
    public static final int VIEW_MASK_PREVIEW_AUTOOPEN = 0x0100;

    /** mask value to use auto open object */
    public static final int VIEW_MASK_AUTOOPEN = 0x0200;

    /** mask value for the history view, enable object history */
    public static final int VIEW_MASK_HISTORY_SCOPE_OBJECT = 0x0400;
    /** mask value for the history view, enable case (bpm) history */
    public static final int VIEW_MASK_HISTORY_SCOPE_CASE = 0x0800;

    /** mask value for the save all button */
    public static final int VIEW_MASK_ENABLE_SAVE_ALL = 0x1000;

    /** mask value for the links view */
    public static int VIEW_MASK_LINKS = 0x2000;

    /** class logger */
    private static final Logger LOG = OwLog.getLogger(OwEditPropertiesDialog.class);
    /** filters the views to be displayed*/
    protected int m_iViewMask = VIEW_MASK_PROPERTIES | VIEW_MASK_SYSTEM_PROPERTIES | VIEW_MASK_ACCESS_RIGHTS | VIEW_MASK_VERSIONS | VIEW_MASK_HISTORY | VIEW_MASK_FILED_IN | VIEW_MASK_DOCUMENT_FUNCTIONS;

    /** filters the views to be displayed read-only */
    protected int m_iReadOnlyViewMask = 0;

    /** layout to be used for the dialog */
    private OwSubLayout m_Layout;

    /** list of Document function plugins which have been instantiated */
    protected List m_DocumentFunctionPluginList;

    /** navigation view to navigate through the subviews */
    protected OwMultiViewNavigation m_SubNavigation;

    /** instance of the MIME manager used to open the objects */
    protected OwMimeManager m_MimeManager;

    /** the parent of the object that listed the getItem() */
    protected OwObject m_ParentObject;

    /** list of column info for the version view */
    protected Collection m_VersionColumnInfo;

    /** OwSearchTemplate to filter the history view */
    protected OwSearchTemplate m_historyViewSearchTemplate;
    /** column info for the history view */
    protected Collection m_historyViewColumnInfo;

    /** max number of items to display in the views */
    protected int m_iMaxElementSize;

    /** property set that holds the batch properties, which will be prefilled from previous screens */
    protected Collection m_batchIndexProperties;

    //private OwObjectPropertyView m_editPropertyView;
    private OwPropertyViewBridge m_editPropertyViewBridge;

    private OwObjectLinkRelation m_relationSplit = OwObjectLinkRelation.NONE;

    private boolean displayLinksByType = false;

    private Collection<String> m_linkClassNames;

    /** Close dialog without saving*/
    private boolean m_closeDlgWOSaving;
    /** Flag indication if user should be notified if nothing was saved
     * @since 3.1.0.3*/
    private boolean m_displayNoSaveMsg;

    // === multi select functionality
    /** list of items to work on */
    protected List m_items;

    /** current item index */
    protected int m_iIndex = 0;
    /**
     * Group properties configuration.
     * @since 3.1.0.0
     * @deprecated since 4.2.0.0 use OwPropertyListConfiguration instead
     */
    private OwGroupPropertiesConfiguration m_groupPropertyConfiguration;
    /**
     * PropertyList configuration element
     * @since 4.2.0.0
     */
    private OwPropertyListConfiguration propertyListConfiguration;

    /**
     * Handler for multiple JSP forms configuration
     * @since 3.1.0.0
     */
    private OwJspFormConfigurator jspFormConfigurator;

    private OwXMLUtil previewConfiguration;

    private OwEditPropertiesModifiabilityHandler modifiabilityHandler;

    protected OwEditPropertiesDialog(OwEditPropertiesDialogBuilder builder)
    {
        m_items = new LinkedList();
        m_items.addAll(builder.getItems());

        m_iIndex = builder.getIndex();
        m_ParentObject = builder.getParentObject();

        m_iMaxElementSize = builder.getMaxElementSize();
        m_VersionColumnInfo = builder.getVersionColumnInfo();

        m_historyViewSearchTemplate = builder.getHistoryViewSearchTemplate();
        m_historyViewColumnInfo = builder.getHistoryViewColumnInfo();
        m_batchIndexProperties = builder.getBatchIndexProperties();

        OwObjectLinkRelation relationSplit = builder.getRelationSplit();
        if (relationSplit != null)
        {
            this.m_relationSplit = relationSplit;
        }

        this.displayLinksByType = builder.isDisplayLinksByType();

        this.m_linkClassNames = builder.getLinkClassNames();

        m_Layout = new OwSubLayout();

        m_Layout.setCustomRegionAttributes(builder.getLayoutRegionAttributes());
    }

    /** set the object, the edit properties view is working on
     *
     * @param obj_p OwObject
     * @param parentObject_p the parent OwObject of the object that listed the getItem()
     * @param iMaxElementSize_p max number of version to display
     * @param versionColumnInfo_p  list of Property Names to act as columns
     * @param historyViewSearchTemplate_p OwSearchTemplate to filter the history view
     * @param historyViewColumnInfo_p list of Property Names to act as columns
     * @deprecated since 4.1.1.0 use {@link OwEditPropertiesDialogBuilder}  
     */
    public OwEditPropertiesDialog(OwObject obj_p, OwObject parentObject_p, int iMaxElementSize_p, Collection versionColumnInfo_p, OwSearchTemplate historyViewSearchTemplate_p, Collection historyViewColumnInfo_p) throws Exception
    {
        m_items = new LinkedList();
        m_items.add(obj_p);

        m_ParentObject = parentObject_p;

        m_iMaxElementSize = iMaxElementSize_p;
        m_VersionColumnInfo = versionColumnInfo_p;

        m_historyViewSearchTemplate = historyViewSearchTemplate_p;
        m_historyViewColumnInfo = historyViewColumnInfo_p;

    }

    /** set the object, the edit properties view is working on
    *
    * @param objects_p List of OwObject
    * @param iIndex_p int index in objects_p to work on, usually 0
    * @param parentObject_p the parent OwObject of the object that listed the getItem()
    * @param iMaxElementSize_p max number of version to display
    * @param versionColumnInfo_p  list of Property Names to act as columns
    * @param historyViewSearchTemplate_p OwSearchTemplate to filter the history view
    * @param historyViewColumnInfo_p list of Property Names to act as columns
    * @param batchIndexProperties_p Collection of batch index properties each property will be copied to the next pane
    * @deprecated since 4.1.1.0 use {@link OwEditPropertiesDialogBuilder}
    */
    public OwEditPropertiesDialog(Collection objects_p, int iIndex_p, OwObject parentObject_p, int iMaxElementSize_p, Collection versionColumnInfo_p, OwSearchTemplate historyViewSearchTemplate_p, Collection historyViewColumnInfo_p,
            Collection batchIndexProperties_p) throws Exception
    {
        m_items = new LinkedList();
        m_items.addAll(objects_p);

        m_iIndex = iIndex_p;
        m_ParentObject = parentObject_p;

        m_iMaxElementSize = iMaxElementSize_p;
        m_VersionColumnInfo = versionColumnInfo_p;

        m_historyViewSearchTemplate = historyViewSearchTemplate_p;
        m_historyViewColumnInfo = historyViewColumnInfo_p;
        m_batchIndexProperties = batchIndexProperties_p;

    }

    /** determine the views to be displayed by masking them with their flag
     *
     * @param iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** determine the views to be displayed read-only by masking them with their flag
     *
     * @param iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    public void setReadOnlyViewMask(int iViewMask_p)
    {
        m_iReadOnlyViewMask = iViewMask_p;
    }

    /** check if view should be displayed or is masked out
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /** check if view should be displayed read-only
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     * @return true when view should be read-only
     */
    protected boolean hasReadOnlyViewMask(int iViewMask_p)
    {
        if ((iViewMask_p & m_iReadOnlyViewMask) != 0)
        {
            return true;
        }
        else
        {
            if (getModifiabilityHandler() != null)
            {
                return !getModifiabilityHandler().isModifiable(iViewMask_p, getItem());
            }
            return false;
        }
    }

    protected void init() throws Exception
    {
        super.init();
        OwMainAppContext context = (OwMainAppContext) getContext();
        // === init MIME manager as event target
        m_MimeManager = createMimeManager();
        m_MimeManager.attach(getContext(), null);

        m_MimeManager.setItemStyle("OwEditPropertiesMimeItem");
        m_MimeManager.setIconStyle("OwEditPropertiesMimeIcon");

        // === create the dialog views
        // === attached layout

        addView(m_Layout, MAIN_REGION, null);

        // === navigation
        m_SubNavigation = new OwMultiViewNavigation();
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);
        m_Layout.addViewReference(m_SubNavigation.getSecondaryViewReference(OwSubLayout.SECONDARY_REGION), OwSubLayout.SECONDARY_REGION);

        // === add common properties view
        if (hasViewMask(VIEW_MASK_PROPERTIES))
        {
            m_editPropertyViewBridge = createPropertyViewBridge(m_SubNavigation, getItem());
            if (getPropertyListConfiguration() != null)
            {
                m_editPropertyViewBridge.setPropertyListConfiguration(getPropertyListConfiguration());
            }
            else
            {
                m_editPropertyViewBridge.setGroupPropertiesConfiguration(getGroupPropertiesConfiguration());
            }
        }

        // === add system properties view
        if (hasViewMask(VIEW_MASK_SYSTEM_PROPERTIES))
        {
            createSystemPropertyView(m_SubNavigation, getItem());
        }

        // === add Access rights view module if available in adapter
        boolean fAccessRightsViewAvailable = context.getNetwork().canEditAccessRights(getItem());
        if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS) && fAccessRightsViewAvailable)
        {
            createAccessRightsView(m_SubNavigation, getItem());
        }

        // === add records filed in view
        if ((getItem().getType() != OwObjectReference.OBJECT_TYPE_FOLDER) && hasViewMask(VIEW_MASK_FILED_IN))
        {
            createFiledRecordsView(m_SubNavigation, getItem());
        }

        // === add versions view
        if (getItem().hasVersionSeries() && hasViewMask(VIEW_MASK_VERSIONS))
        {
            createEditVersionsView(m_SubNavigation, getItem());
        }

        // === add history view
        if (hasViewMask(VIEW_MASK_HISTORY) && ((null != m_historyViewSearchTemplate) || (null != m_historyViewColumnInfo)))
        {
            OwObjectHistoryView histview = createHistoryView(m_SubNavigation, getItem());

            // set masks
            int iHistoryViewMask = 0;

            iHistoryViewMask |= hasViewMask(VIEW_MASK_HISTORY_SCOPE_OBJECT) ? OwObjectHistoryView.VIEW_MASK_HISTORY_SCOPE_OBJECT : 0;
            iHistoryViewMask |= hasViewMask(VIEW_MASK_HISTORY_SCOPE_CASE) ? OwObjectHistoryView.VIEW_MASK_HISTORY_SCOPE_CASE : 0;

            histview.setViewMask(iHistoryViewMask);
        }

        // === add history view
        if (hasViewMask(VIEW_MASK_LINKS))
        {
            createLinksView(m_SubNavigation, getItem());
        }

        // activate the first view
        if (m_SubNavigation.size() > 0)
        {
            m_SubNavigation.navigate(0);
        }

        // === get preloaded plugins reference
        m_DocumentFunctionPluginList = context.getConfiguration().getDocumentFunctionPlugins();

        // open
        if (hasViewMask(VIEW_MASK_AUTOOPEN))
        {
            if (OwMimeManager.isObjectDownloadable(context, getItem()))
            { //set final script for auto focus of properties window in IE
                context.addFinalScript("\nwindow.setTimeout(function(){document.body.focus();},500);");
                if (hasViewMask(VIEW_MASK_PREVIEW_AUTOOPEN))
                {
                    OwMimeManager.openObjectPreview(context, getItem(), m_ParentObject, OwMimeManager.VIEWER_MODE_SINGLE, null);
                }
                else
                {
                    OwMimeManager.openObject(context, getItem(), m_ParentObject, OwMimeManager.VIEWER_MODE_SINGLE, null);
                }
            }
        }
        setDisplayNoSaveMsg(false);

        setCloseDlgWOSaving(false);
    }

    /**
     * Create {@link OwPropertyViewBridge} object.
     * @param subNavigation_p - the navigation view.
     * @param item_p - the {@link OwObject}
     * @return the newly created {@link OwPropertyViewBridge} object
     * @throws Exception
     * @since 3.1.0.0
     */
    protected OwPropertyViewBridge createPropertyViewBridge(OwMultiViewNavigation subNavigation_p, OwObject item_p) throws Exception
    {
        OwPropertyViewBridge bridge = null;

        if (getJspConfigurator() != null)
        {
            String jsp_page = getJspConfigurator().getJspForm(item_p.getObjectClass().getClassName());
            if ((getJspConfigurator().isJspFormEnabled()) && (jsp_page != null))
            {
                bridge = new OwFormPropertyViewBridge(createFormPropertyView(subNavigation_p, item_p));
            }
        }

        if (bridge == null)
        {
            bridge = new OwStandardPropertyViewBridge(createPropertyView(subNavigation_p, item_p));
        }
        addExtraMenuButtons(bridge);

        return bridge;
    }

    /**
     * Create the view based on JSP form for rendering properties.
     * @param subNavigation_p - the navigation view
     * @param object_p - the {@link OwObject} object.
     * @return - the newly created {@link OwObjectPropertyFormularView} object
     * @throws Exception
     * @since 3.1.0.0
     */
    protected OwObjectPropertyFormularView createFormPropertyView(OwSubNavigationView subNavigation_p, OwObject object_p) throws Exception
    {
        OwObjectPropertyFormularView view = new OwObjectPropertyFormularView();
        view.setPasteMetadataEnabled(hasViewMask(VIEW_MASK_ENABLE_PASTE_METADATA));
        subNavigation_p.addView(view, getContext().localize("owdocprops.OwEditPropertiesDialog.properties_title", "Properties"), null, getContext().getDesignURL() + "/images/plug/owdocprops/properties.png", null, null);
        view.setJspConfigurator(getJspConfigurator());
        view.setObjectRef(object_p);
        // attach view to layout
        // set the list of batch properties after view is initialized
        view.setBatchProperties(m_batchIndexProperties);

        return view;
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();
        m_MimeManager.reset();
        // detach the field manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();
    }

    /** determine if region contains a view
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case TITLE_REGION:
                return true;

            case MENU_REGION:
                return hasViewMask(VIEW_MASK_DOCUMENT_FUNCTIONS);

            default:
                return super.isRegion(iRegion_p);
        }
    }

    /** get the MIME manager
     *
     * @return OwMimeManager
     */
    public OwMimeManager getMimeManager()
    {
        return m_MimeManager;
    }

    /** get the event URL for the latest version button
     *
     * @return String
     */
    public String getLatestVersionURL()
    {
        return getEventURL("LatestVersion", null);
    }

    /** get the event URL for the released version button
     *
     * @return String
     */
    public String getReleasedVersionURL()
    {
        return getEventURL("ReleasedVersion", null);
    }

    /** render the title region
      * @param w_p Writer object to write HTML to
      * @since 3.1.0.0
      */
    private void renderTitleRegion(Writer w_p) throws Exception
    {
        // always reset MIME manager !!!
        m_MimeManager.reset();
        serverSideDesignInclude("dmsdialogs/OwEditPropertiesDialogTitle.jsp", w_p);

    }

    /** create a new dialog with a new object reference, close existing dialog
     *
     * @param newObject_p the new OwObject to use
     */
    private void replaceObject(OwObject newObject_p) throws Exception
    {
        // create new edit properties dialog
        m_items.set(m_iIndex, newObject_p);

        initNewItem(false);
    }

    /** called when user pressed get Latest Version
     */
    public void onLatestVersion(HttpServletRequest request_p) throws Exception
    {
        // replace object ref by latest version
        OwVersionSeries versSeries = getItem().getVersionSeries();
        replaceObject(versSeries.getObject(versSeries.getLatest()));
    }

    /** called when user pressed get Released Version
     */
    public void onReleasedVersion(HttpServletRequest request_p) throws Exception
    {
        // replace object ref by latest version
        OwVersionSeries versSeries = getItem().getVersionSeries();
        //bug 1558
        OwVersion releasedVersion = null;
        try
        {
            releasedVersion = versSeries.getReleased();
        }
        catch (Exception e)
        {
            throw new Exception(getContext().localize("owdocprops.OwEditPropertiesDialog.no_released_version", "This object has not been released yet."), e);
        }
        replaceObject(versSeries.getObject(releasedVersion));
    }

    /** check if plugin displays in context menu rather than directly has an object instance, i.e. is displayed for each item.
     *
     * @param plugin_p OwDocumentFunction
     * @return boolean
     */
    public boolean showPluginOnItem(OwDocumentFunction plugin_p)
    {
        // get the Need Parent Flag indicating that plugin can only working on documents listed by some parent.
        if (plugin_p.getNeedParent())
        {
            return false; // skip NeedParent plugins, we don't have a parent here
        }

        return plugin_p.getShowInEditViews();
    }

    /** render the title region
      * @param w_p Writer object to write HTML to
      */
    private void renderMenuRegion(Writer w_p) throws Exception
    {
        // === render the document plugins here
        // iterate over preinstantiated plugins and create HTML
        w_p.write("<ul>");

        for (int p = 0; p < m_DocumentFunctionPluginList.size(); p++)
        {
            OwDocumentFunction plugIn = (OwDocumentFunction) m_DocumentFunctionPluginList.get(p);

            if (showPluginOnItem(plugIn) && plugIn.isEnabled(getItem(), null, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                if (plugIn.getNoEvent())
                {
                    // === do not create anchor
                    w_p.write("\n<li>");
                    w_p.write(plugIn.getIconHTML(getItem(), null));
                    w_p.write("&nbsp;");
                    w_p.write(plugIn.getLabel(getItem(), null));
                    w_p.write("</li>\n");
                }
                else
                {
                    String pluginEventUrl = getEventURL("PluginEvent", PLUG_INDEX_KEY + "=" + String.valueOf(p));
                    // === create anchor with reference to the selected object and a tooltip
                    w_p.write("\n<li><a class=\"OwMainMenuIcon\" href=\"");
                    w_p.write(pluginEventUrl);
                    w_p.write("\" title=\"");
                    w_p.write(plugIn.getTooltip());
                    w_p.write("\">");
                    w_p.write(plugIn.getIconHTML(getItem(), null));
                    w_p.write("</a>&nbsp;<a class=\"OwMainMenuItem\" href=\"");
                    w_p.write(pluginEventUrl);
                    w_p.write("\" title=\"");
                    w_p.write(plugIn.getTooltip());
                    w_p.write("\">");
                    w_p.write(plugIn.getLabel(getItem(), null));
                    w_p.write("</a></li>\n");
                }
            }
        }

        w_p.write("</ul>");
    }

    /** render the views of the region
      * @param w_p Writer object to write HTML to
      * @param iRegion_p ID of the region to render
      */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case MENU_REGION:
                if (hasViewMask(VIEW_MASK_DOCUMENT_FUNCTIONS))
                {
                    renderMenuRegion(w_p);
                }
                break;

            case TITLE_REGION:
                renderTitleRegion(w_p);
                break;
            case CLOSE_BTN_REGION:
                renderSequenceNumber(w_p);
                break;
            case HELP_BTN_REGION:
                renderHelpButton(w_p);
                break;

            default:
                // render registered views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** render the no. of elements (x from y)
     * @param w_p Writer object to write HTML to
     * @since 3.1.0.0
     */
    private void renderSequenceNumber(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"floatleft\">");
        w_p.write("     <div class=\"floatleft\">");
        renderCloseButton(w_p);
        w_p.write("     </div>");
        w_p.write("     <div class=\"floatleft\">");
        w_p.write("         <div id=\"OwStandardDialog_SEQUENCEBUTTONS\">");
        renderNavigationButtons(w_p);
        w_p.write("         </div>");
        w_p.write("         <div  style=\"text-align:center;\" id=\"OwStandardDialog_PAGENR\">");
        w_p.write("<span class=\"OwEditProperties_Versiontext\"> ");
        int curentItem = m_iIndex + 1;
        if (getCount() > 1)
        {
            w_p.write(curentItem + " " + getContext().localize("owdocprops.OwEditPropertiesDialog.pageoffrom", "from") + " " + getCount());
        }
        w_p.write("</span>");
        w_p.write("&nbsp;");
        w_p.write("         </div>");
        w_p.write("     </div>");
        w_p.write("</div>");
    }

    /** event called when user clicked on a plugin link of an object entry in the list
     *   @param request_p  a {@link HttpServletRequest}
     */
    public void onPluginEvent(HttpServletRequest request_p) throws Exception
    {
        // === handle plugin event
        // parse query string
        String strPlugIndex = request_p.getParameter(PLUG_INDEX_KEY);
        if (strPlugIndex != null)
        {
            // get plugin
            int iPlugIndex = Integer.parseInt(strPlugIndex);
            OwDocumentFunction plugIn = (OwDocumentFunction) m_DocumentFunctionPluginList.get(iPlugIndex);

            // action allowed ?
            if (plugIn.isEnabled(getItem(), null, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                // delegate event to plugin
                plugIn.onClickEvent(getItem(), m_ParentObject, this);
            }
        }
    }

    /** implementation of the OwFunction.OwFunctionRefreshContext interface
     *  Called from a plugin to inform its client and cause refresh of display data
     *
     * @param iReason_p reason as defined with OwFunction.REFRESH_...
     * @param param_p Object optional parameter representing the refresh, depends on the value of iReason_p, can be null
     */
    public void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception
    {
        // === plugin requested refresh
        // update version cache, in case versions have changed due to plugin action
        getDocument().update(this, OwUpdateCodes.UPDATE_OBJECT_VERSION, null);
    }

    /** called when user clicks a select button, fUseSelectButton_p must have been set to display select buttons
     * @param object_p OwObject object that was selected
     * @param parent_p OwObject parent if available, or null
     * */
    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        replaceObject(object_p);
    }

    /** called when uses clicks on a sort header and the sort changes
     *
     * @param newSort_p OwSort new sort
     * @param strSortProperty_p String Property Name of sort property that was changed
     */
    public void onObjectListViewSort(com.wewebu.ow.server.field.OwSort newSort_p, String strSortProperty_p) throws Exception
    {
    }

    /** overridable to create a new dialog when browsing through multiple objects
     *
     * @return OwEditPropertiesDialog
     * @throws Exception
     */
    protected OwEditPropertiesDialog createEditPropertiesDialog() throws Exception
    {
        OwEditPropertiesDialog owEditPropertiesDialog = new OwEditPropertiesDialogBuilder().items(m_items).index(m_iIndex).parentObject(m_ParentObject).maxElementSize(m_iMaxElementSize).versionColumnInfo(m_VersionColumnInfo)
                .historyViewSearchTemplate(m_historyViewSearchTemplate).historyViewColumnInfo(m_historyViewColumnInfo).batchIndexProperties(m_batchIndexProperties).relationSplit(m_relationSplit).displayLinksByType(displayLinksByType)
                .linkClassNames(m_linkClassNames).layoutRegionAttributes(m_Layout.getCustomRegionAttributes()).build();
        //new OwEditPropertiesDialog(m_items, m_iIndex, m_ParentObject, m_iMaxElementSize, m_VersionColumnInfo, m_historyViewSearchTemplate, m_historyViewColumnInfo, m_batchIndexProperties, m_relationSplit, displayLinksByType, m_linkClassNames);
        owEditPropertiesDialog.setJspConfigurator(getJspConfigurator());
        owEditPropertiesDialog.setGroupPropertiesConfiguration(m_groupPropertyConfiguration);
        return owEditPropertiesDialog;
    }

    /** init the dialog with the current item
     *
     * @param fBatchIndex_p true = index the properties with previous ones
     * @throws Exception
     */
    protected void initNewItem(boolean fBatchIndex_p) throws Exception
    {
        OwEditPropertiesDialog dlg = createEditPropertiesDialog();
        dlg.setJspConfigurator(getJspConfigurator());
        dlg.setGroupPropertiesConfiguration(getGroupPropertiesConfiguration());

        // set help path from this dialog
        dlg.setHelp(m_strHelpPath);
        dlg.setTitle(getTitle());

        // activate the same views
        dlg.setViewMask(m_iViewMask);
        dlg.setReadOnlyViewMask(m_iReadOnlyViewMask);

        // set info icon from this dialog
        dlg.setInfoIcon(m_strInfoIconURL);

        dlg.setPreviewConfiguration(previewConfiguration);

        // close this dialog
        super.closeDialog();
        setJspConfigurator(null);
        // open new dialog
        getContext().openDialog(dlg, m_Listener);

        if (fBatchIndex_p)
        {
            dlg.batchIndex();
        }
    }

    private void batchIndex() throws Exception
    {
        if (m_editPropertyViewBridge != null)
        {
            m_editPropertyViewBridge.onBatchIndex();
        }
    }

    /** the work item to work on */
    public OwObject getItem()
    {
        return (OwObject) m_items.get(m_iIndex);
    }

    /** called when the Dialog needs to know if there is a next item
    *
    */
    public boolean hasNext() throws Exception
    {
        return (m_iIndex < (m_items.size() - 1));
    }

    /** called when the Dialog needs to know if there is a prev item
     *
     */
    public boolean hasPrev() throws Exception
    {
        return (m_iIndex > 0);
    }

    /** get the number of sequence items in the dialog */
    public int getCount()
    {
        return m_items.size();
    }

    /** move to prev item and roll over, i.e. start at the end one if first one is reached
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the prev item, if this is the last item, closes the dialog
     */
    public void prev(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            throw new OwNotSupportedException("OwEditPropertiesDialog.prev(fRemoveCurrent_p==true) not supported.");
        }

        if (hasPrev())
        {
            m_iIndex--;
        }
        else
        {
            m_iIndex = (m_items.size() - 1);
        }

        // init the dialog with the current work item
        initNewItem(false);
    }

    /** move to next item and roll over, i.e. start at the first one if end is reached
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the next item, if this is the last item, closes the dialog
     */
    public void next(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            // === remove the current item and move to the next
            if (getCount() == 1)
            {
                // === only one item left
                // close dialog
                super.closeDialog();
                return;
            }
            else
            {
                m_items.remove(m_iIndex);
                if (m_iIndex >= m_items.size())
                {
                    m_iIndex = 0;
                }
            }
        }
        else
        {
            // === move to the next item
            if (hasNext())
            {
                m_iIndex++;
            }
            else
            {
                m_iIndex = 0;
            }
        }

        // === init the dialog with the current work item
        initNewItem(true);
    }

    /** visually close the Dialog. The behavior depends on usage
     *  If this view is a child of a DialogManager, the View gets removed from it.
     */
    public void closeDialog() throws Exception
    {
        super.closeDialog();

        if (hasViewMask(VIEW_MASK_AUTOOPEN))
        {
            // === close viewer as well
            if (((OwMainAppContext) getContext()).getWindowPositions().getPositionMainWindow())
            {
                ((OwMainAppContext) getContext()).addFinalScript("\n" + OwMimeManager.createAutoViewerRestoreMainWindowScript(((OwMainAppContext) getContext()), OwMimeManager.VIEWER_MODE_DEFAULT));
            }
        }
    }

    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {

    }

    /** called when uses clicks on a folder, used to redirect folder events an bypass the mimemanager
     *
     * @param obj_p OwObject folder object that was clicked
     * @return boolean true = event was handled, false = event was not handled, do default handler
     *
     * @throws Exception
     */
    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        // event was not handled
        return false;
    }

    /** overridable factory method to create and init the history view
     *
     * @param nav_p
     * @param obj_p
     * @return the newly created {@link OwObjectHistoryView}
     * @throws Exception
     */
    protected OwObjectHistoryView createHistoryView(OwSubNavigationView nav_p, OwObject obj_p) throws Exception
    {
        OwObjectHistoryView historyView = new OwObjectHistoryView(m_historyViewSearchTemplate, m_historyViewColumnInfo, m_iMaxElementSize);

        // attach view to layout
        nav_p.addView(historyView, getContext().localize("owdocprops.OwEditPropertiesDialog.history_title", "History"), null, getContext().getDesignURL() + "/micon/history.png", null, null);

        // set object reference
        historyView.setObjectRef(obj_p);

        return historyView;
    }

    /** overridable factory method to create and init the edit links view
    *
    * @param nav_p
    * @param obj_p
    * @return the newly created {@link OwObjectEditVersionsView}
    * @throws OwException
    * @since 4.1.1.0
    */
    protected OwObjectLinksView createLinksView(OwSubNavigationView nav_p, OwObject obj_p) throws OwException
    {
        // attach view to layout
        try
        {
            OwObjectLinksView linksView = null;
            OwObjectLinksDocument document = new OwObjectLinksDocument(m_relationSplit, m_linkClassNames, Collections.EMPTY_LIST);
            if (displayLinksByType)
            {
                linksView = new OwTypedLinksView(document);
            }
            else
            {
                linksView = new OwAllLinksView(document);
            }

            nav_p.addView(linksView, getContext().localize("owdocprops.OwEditPropertiesDialog.links_title", "Links"), null, getContext().getDesignURL() + "/images/plug/owdocprops/show_links.png", null, null);

            linksView.getDocument().setObject(obj_p);
            return linksView;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not create links view.", e);
        }

    }

    /** overridable factory method to create and init the edit versions view
     *
     * @param nav_p
     * @param obj_p
     * @return the newly created {@link OwObjectEditVersionsView}
     * @throws Exception
     */
    protected OwObjectEditVersionsView createEditVersionsView(OwSubNavigationView nav_p, OwObject obj_p) throws Exception
    {
        // use the object list view as the versions view
        OwObjectEditVersionsView versionsView = new OwObjectEditVersionsView();

        // attach view to layout
        nav_p.addView(versionsView, getContext().localize("owdocprops.OwEditPropertiesDialog.versions_title", "Versions"), null, getContext().getDesignURL() + "/images/plug/owdocprops/version.png", null, null);

        // set the columns to display
        versionsView.setColumnProperties(m_VersionColumnInfo, m_iMaxElementSize);

        // set object reference
        versionsView.setObjectRef(obj_p);

        // set event listener
        versionsView.setEventListner(this);

        return versionsView;
    }

    /** overridable factory method to create and init the filed records view
     *
     * @param nav_p
     * @param obj_p
     * @return the newly created {@link OwObjectFiledRecordsView}
     * @throws Exception
     */
    protected OwObjectFiledRecordsView createFiledRecordsView(OwSubNavigationView nav_p, OwObject obj_p) throws Exception
    {
        OwObjectFiledRecordsView recordsFiledView = new OwObjectFiledRecordsView();

        // set new object before view is initialized
        recordsFiledView.setObjectRef(obj_p, ((OwMainAppContext) getContext()).getConfiguration().getRecordClassNames());

        // attach view to layout
        nav_p.addView(recordsFiledView, getContext().localize("owdocprops.OwEditPropertiesDialog.recordsfield_title", "Files"), null, getContext().getDesignURL() + "/images/plug/owdocprops/efiles.png", null, null);

        return recordsFiledView;
    }

    /** overridable factory method to create and init the access rights view
     *
     * @param nav_p
     * @param obj_p
     * @return the newly created {@link OwObjectAccessRightsView}
     * @throws Exception
     */
    protected OwObjectAccessRightsView createAccessRightsView(OwSubNavigationView nav_p, OwObject obj_p) throws Exception
    {
        OwObjectAccessRightsView accessRightsView = new OwObjectAccessRightsView();

        // set the object before view gets initialized in add view
        accessRightsView.setObjectRef(obj_p);

        // attach view to layout
        nav_p.addView(accessRightsView, getContext().localize("owdocprops.OwEditPropertiesDialog.accessrights_title", "Access Rights"), null, getContext().getDesignURL() + "/images/plug/owdocprops/accessrights.png", null, null);

        // set read-only flag after target has been attached
        accessRightsView.setReadOnly(hasReadOnlyViewMask(VIEW_MASK_ACCESS_RIGHTS));

        return accessRightsView;
    }

    /** overridable factory method to create and init the system property view
     *
     * @param nav_p
     * @param obj_p
     * @return the newly created {@link OwObjectPropertyView}
     * @throws Exception
     */
    protected OwObjectPropertyView createSystemPropertyView(OwSubNavigationView nav_p, OwObject obj_p) throws Exception
    {
        OwObjectPropertyView sysPropertyView = createSystemPropertyViewInstance();
        // attach view to layout
        nav_p.addView(sysPropertyView, getContext().localize("owdocprops.OwEditPropertiesDialog.sysproperties_title", "System Properties"), null, getContext().getDesignURL() + "/images/plug/owdocprops/system_props.png", null, null);

        // set new object after view is initialized
        sysPropertyView.setObjectRef(obj_p, true);

        return sysPropertyView;
    }

    /** overridable factory method to create and init the property view
     *
     * @param nav_p
     * @param obj_p
     * @return the newly created {@link OwObjectPropertyView}
     * @throws Exception
     */
    protected OwObjectPropertyView createPropertyView(OwMultiViewNavigation nav_p, OwObject obj_p) throws Exception
    {
        OwObjectPropertyView propertyView = createPropertyViewInstance();

        //-----< compute viewmask >-----
        int iViewMask = 0;
        if (hasViewMask(VIEW_MASK_ENABLE_PASTE_METADATA))
        {
            iViewMask |= OwObjectPropertyView.VIEW_MASK_ENABLE_PASTE_METADATA;
        }

        if (hasReadOnlyViewMask(VIEW_MASK_PROPERTIES))
        {
            iViewMask |= OwObjectPropertyView.VIEW_MASK_READONLY;
        }

        propertyView.setViewMask(iViewMask);

        //-----< add view >-----
        // attach view to layout

        String title = getContext().localize("owdocprops.OwEditPropertiesDialog.properties_title", "Properties");
        String image = getContext().getDesignURL() + "/images/plug/owdocprops/properties.png";
        if (isShowPreview())
        {
            OwPreview preview = new OwPreview(previewConfiguration, obj_p);
            int tabIndex = nav_p.addView(preview, title, null, image, null, null);
            nav_p.addSecondaryView(tabIndex, OwSubLayout.SECONDARY_REGION, propertyView, null);
        }
        else
        {
            nav_p.addView(propertyView, title, null, image, null, null);
        }
        // set new object after view is initialized
        propertyView.setObjectRef(obj_p, false);

        // set the list of batch properties after view is initialized
        propertyView.setBatchProperties(m_batchIndexProperties);

        return propertyView;
    }

    private boolean isShowPreview()
    {
        if (previewConfiguration != null)
        {
            return previewConfiguration.getSafeBooleanAttributeValue("show", false);
        }
        else
        {
            return false;
        }
    }

    /**
     * Add extra buttons (Save+next, Save+close, Save all) to the properties view.
     * @param propertyViewBridge_p
     * @throws Exception
     * @since 3.1.0.0
     */
    protected void addExtraMenuButtons(OwPropertyViewBridge propertyViewBridge_p) throws Exception
    {
        //-----< add extra menu button "SaveAll" >-----
        if ((m_items.size() > 1) && (propertyViewBridge_p.getMenu() != null) && hasViewMask(VIEW_MASK_ENABLE_SAVE_ALL))
        {
            propertyViewBridge_p.getMenu().addFormMenuItem(this, getContext().localize("owdocprops.OwEditPropertiesDialog.SaveAll", "Save all"), null, "PropertiesSaveAll", null, null, propertyViewBridge_p.getView().getFormName());
        }

        //save and next buttons
        if ((m_items.size() > 1) && (m_iIndex < m_items.size() - 1) && (propertyViewBridge_p.getMenu() != null) && !propertyViewBridge_p.isReadOnly())
        {
            propertyViewBridge_p.getMenu().addFormMenuItem(this, getContext().localize("owdocprops.OwEditPropertiesDialog.SavePlusNext", "Save and Next"), null, "PropertiesSavePlusNext", null, null, propertyViewBridge_p.getView().getFormName());
        }

        //read only mode, next button
        if ((m_items.size() > 1) && (m_iIndex < m_items.size() - 1) && (propertyViewBridge_p.getMenu() != null) && propertyViewBridge_p.isReadOnly())
        {
            propertyViewBridge_p.getMenu().addFormMenuItem(this, getContext().localize("owdocprops.OwEditPropertiesDialog.Next", "Next"), null, "PropertiesNext", null, null, propertyViewBridge_p.getView().getFormName());
        }

        //read only mode, close button
        if ((m_items.size() >= 1) && (m_iIndex == m_items.size() - 1) && (propertyViewBridge_p.getMenu() != null) && propertyViewBridge_p.isReadOnly())
        {
            propertyViewBridge_p.getMenu().addFormMenuItem(this, getContext().localize("owdocprops.OwEditPropertiesDialog.Close", "Close"), null, "Cancel", null, null, propertyViewBridge_p.getView().getFormName());
        }
        //save and close buttons
        if ((m_items.size() >= 1) && (m_iIndex == m_items.size() - 1) && (propertyViewBridge_p.getMenu() != null) && !propertyViewBridge_p.isReadOnly())
        {
            propertyViewBridge_p.getMenu().addFormMenuItem(this, getContext().localize("owdocprops.OwEditPropertiesDialog.SavePlusClose", "Save and Close"), null, "PropertiesSavePlusClose", null, null, propertyViewBridge_p.getView().getFormName());
        }

    }

    /**
     * Create property view instance
     * @return property view instance
     * @since 2.5.2.0
     */
    protected OwObjectPropertyView createPropertyViewInstance()
    {
        OwObjectPropertyView propertyView = new OwObjectPropertyView();
        return propertyView;
    }

    /**
     * Create system property view instance
     * @return property view instance
     * @since 2.5.2.0
     */
    protected OwObjectPropertyView createSystemPropertyViewInstance()
    {
        OwObjectPropertyView propertyView = new OwObjectPropertyView();
        return propertyView;
    }

    /**
     * Method handler used when user press the button "Save + next"
     * @param request_p - the HTTP request
     * @param oReason_p - the reason
     * @return <code>true</code> if both save and next operations succeeded.
     * @throws Exception
     * @since 3.0.0.0
     */
    public boolean onPropertiesSavePlusNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // first of all, try to save the changed properties at the current object
        boolean saveCurrentResult = m_editPropertyViewBridge.onApply(request_p, oReason_p);
        if (saveCurrentResult)
        {
            try
            {
                this.onNext(request_p);
            }
            catch (Exception e)
            {
                LOG.error("OwEditPropertiesDialog.onPropertiesSavePlusNext: Cannot go to the next item", e);
                saveCurrentResult = false;
            }
        }
        else
        {
            //if any, remove previous messages
            Collection messages = ((OwMainAppContext) getContext()).getMessages();
            if (messages != null)
            {
                messages.clear();
            }

            if (displayNoSaveMsg())
            {
                String message = getContext().localize("owdocprops.OwEditPropertiesDialog.NextNoSave", "The next document was opened and previous document was not saved, as there were no changes.");
                ((OwMainAppContext) getContext()).postMessage(message);
            }
            else
            {
                String message = getContext().localize("owdocprops.OwEditPropertiesDialog.CloseNotSaved", "Nothing was changed therefore the document was not saved and closed.");
                ((OwMainAppContext) getContext()).postMessage(message);

            }

            if (closeDlgWOSaving())
            {
                try
                {
                    this.onNext(request_p);
                }
                catch (Exception e)
                {
                    LOG.error("OwEditPropertiesDialog.onPropertiesSavePlusNext: Cannot go to the next item", e);
                    saveCurrentResult = false;
                }
            }
        }

        return saveCurrentResult;
    }

    /**
     * Method handler used when user press the button "Save + next"
     * @param request_p - the HTTP request
     * @param oReason_p - the reason
     * @return <code>true</code> if both save and next operations succeeded.
     * @throws Exception
     * @since 3.0.0.0
     */
    public boolean onPropertiesNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        boolean next;
        try
        {
            this.onNext(request_p);
            next = true;
        }
        catch (Exception e)
        {
            LOG.error("OwEditPropertiesDialog.onPropertiesSavePlusNext: Cannot go to the next item", e);
            next = false;
        }

        return next;
    }

    /**
     * Method handler used when user press the button "Save + close"
     * @param request_p - the HTTP request
     * @param oReason_p - the reason
     * @return <code>true</code> if both save and close operations succeeded.
     * @throws Exception
     * @since 3.0.0.0
     */
    public boolean onPropertiesSavePlusClose(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // first of all, try to save the changed properties at the current object
        boolean saveCurrentResult = m_editPropertyViewBridge.onApply(request_p, oReason_p);
        if (saveCurrentResult)
        {

            try
            {
                this.onClose(request_p);
            }

            catch (Exception e)
            {
                LOG.error("OwEditPropertiesDialog.onPropertiesSavePlusClose: Cannot close the dialog", e);
                saveCurrentResult = false;
            }
        }
        else
        {
            //if any, remove previous messages
            Collection messages = ((OwMainAppContext) getContext()).getMessages();
            if (messages != null)
            {
                messages.clear();
            }

            if (displayNoSaveMsg())
            {
                String message = getContext().localize("owdocprops.OwEditPropertiesDialog.CloseNoSave", "The document was closed without saving anything, because properties were not changed.");
                ((OwMainAppContext) getContext()).postMessage(message);
            }
            else
            {
                String message = getContext().localize("owdocprops.OwEditPropertiesDialog.CloseNotSaved", "Nothing was changed therefore the document was not saved and closed.");
                ((OwMainAppContext) getContext()).postMessage(message);
                if (closeDlgWOSaving())
                {
                    this.closeDialog();
                }

            }

            if (closeDlgWOSaving())
            {

                this.closeDialog();
            }

        }

        return saveCurrentResult;
    }

    /** event called when user clicked SaveAll button in the menu of the property view
     *  @param request_p a {@link HttpServletRequest}
     *  @param oReason_p Optional reason object submitted in addMenuItem
     *  @return a <code>boolean</code>
     */
    public boolean onPropertiesSaveAll(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // first of all, try to save the changed properties at the current object
        boolean saveCurrentResult = m_editPropertyViewBridge.onApply(request_p, oReason_p);

        // if saving the current object failed, bail out here
        if (!saveCurrentResult)
        {
            return false;
        }

        // get a copy of the properties of the current object
        OwPropertyCollection templateProperties = getItem().getClonedProperties(null);
        String templateObjectClass = getItem().getClassName();
        String namePropertyName = getItem().getObjectClass().getNamePropertyName();

        //filter properties - no read-only or hidden properties are passed on the stack
        //system properties are passed only if the main editor view accepts system properties
        OwPropertyCollection filteredProperties = new OwStandardPropertyCollection();
        Set<Entry<Object, Object>> entrySet = templateProperties.entrySet();
        for (Entry templateEntry : entrySet)
        {
            OwProperty property = (OwProperty) templateEntry.getValue();
            OwPropertyClass propertyClass = property.getPropertyClass();
            if (propertyClass.isSystemProperty() != m_editPropertyViewBridge.isSystemPropertyView())
            {
                continue;
            }
            if (propertyClass.isHidden(OwPropertyClass.CONTEXT_NORMAL))
            {
                continue;
            }

            if (propertyClass.isReadOnly(OwPropertyClass.CONTEXT_NORMAL))
            {
                continue;
            }

            filteredProperties.put(templateEntry.getKey(), property);
        }

        // now apply the properties to all objects in the current stack from the next object on.
        // As defined in the comment of 1366
        for (int i = m_iIndex; i < m_items.size(); i++)
        {
            OwObject objectToChange = (OwObject) m_items.get(i);
            // test class of that object
            if (!templateObjectClass.equals(objectToChange.getClassName()))
            {
                // set this object as current object
                m_iIndex = i;
                initNewItem(false);
                throw new OwInvalidOperationException(getContext().localize("owdocprops.OwEditPropertiesDialog.CanNotSaveAllDifferentClass", "The properties cannot be applied on this document because it is of a different type (object class)."));
            }
            OwProperty nameProperty = (OwProperty) filteredProperties.get(namePropertyName);
            if (nameProperty != null)
            {
                // keep the name of this object
                OwProperty currentObjectName = objectToChange.getProperty(namePropertyName);
                nameProperty.setValue(currentObjectName.getValue());
            }
            // save the template properties
            objectToChange.setProperties(filteredProperties);
        }

        return true;
    }

    /** get the style class name for the row
    *
    * @param iIndex_p int row index
    * @param obj_p current OwObject
    *
    * @return String with style class name, or null to use default
    */
    public String onObjectListViewGetRowClassName(int iIndex_p, OwObject obj_p)
    {
        // use default
        return null;
    }

    /** event called when user clicked Cancel button in the menu of the property view
     *  @param request_p a {@link HttpServletRequest}
     *  @param oReason_p Optional reason object submitted in addMenuItem
     *  @return a <code>boolean</code>
     */
    public boolean onCancel(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        closeDialog();
        return (true);
    }

    /**
     * Set the configuration for grouped properties.
     * @param groupPropertyConfiguration_p - the configuration object.
     * @since 3.1.0.0
     * @deprecated since 4.2.0.0 use {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    @Deprecated
    public void setGroupPropertiesConfiguration(OwGroupPropertiesConfiguration groupPropertyConfiguration_p)
    {
        this.m_groupPropertyConfiguration = groupPropertyConfiguration_p;
    }

    /**
     * Get the current configuration for grouping of properties.
     * @return OwGroupPropertiesConfiguration
     * @since 3.1.0.0
     * @deprecated since 4.2.0.0 use {@link #getPropertyListConfiguration()} instead
     */
    @Deprecated
    protected OwGroupPropertiesConfiguration getGroupPropertiesConfiguration()
    {
        return this.m_groupPropertyConfiguration;
    }

    /**
     * Return the current set PropertyList configuration object.
     * @return OwPropertyListConfiguration (can be null if not set)
     * @since 4.2.0.0
     */
    public OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return propertyListConfiguration;
    }

    /**
     * Set/Define a PropertyList configuration.
     * @param propertyListConfiguration OwPropertyListConfiguration (can be null)
     * @since 4.2.0.0
     */
    public void setPropertyListConfiguration(OwPropertyListConfiguration propertyListConfiguration)
    {
        this.propertyListConfiguration = propertyListConfiguration;
    }

    public OwJspFormConfigurator getJspConfigurator()
    {
        return this.jspFormConfigurator;
    }

    public void setJspConfigurator(OwJspFormConfigurator jspFormConfigurator_p)
    {
        this.jspFormConfigurator = jspFormConfigurator_p;
    }

    /**
     * Set the flag if "no save" message(s) should be displayed or not.
     * @param display_p boolean flag
     * @since 3.1.0.3
     */
    public void setDisplayNoSaveMsg(boolean display_p)
    {
        this.m_displayNoSaveMsg = display_p;
    }

    /**
     * Display "no save" message.
     * By default this flag is set to false.
     * @return boolean flag
     * @since 3.1.0.3
     * @see #setDisplayNoSaveMsg(boolean)
     */
    public boolean displayNoSaveMsg()
    {
        return m_displayNoSaveMsg;
    }

    /**
     * Set the flag if the Dialog should be closed
     * if no save was executed.
     * @param closeDlg_p boolean flag
     * @since 3.1.0.3
     */
    public void setCloseDlgWOSaving(boolean closeDlg_p)
    {
        m_closeDlgWOSaving = closeDlg_p;
    }

    /**
     * Should dialog be closed, even if save was not executed.
     * <p>By default the value is true.</p>
     * @return boolean flag
     * @since 3.1.0.3
     * @see #setCloseDlgWOSaving(boolean)
     */
    public boolean closeDlgWOSaving()
    {
        return m_closeDlgWOSaving;
    }

    /**(overridable)
     * Factory method to create a OwMimeManger to be used by this
     * instance.
     * @return OwMimeManager
     * @since 3.1.0.3
     */
    protected OwMimeManager createMimeManager()
    {
        return new OwMimeManager();
    }

    /**
     * Provide a configuration for current preview
     * @param previewConfiguration
     * @since 4.2.0.0
     */
    public void setPreviewConfiguration(OwXMLUtil previewConfiguration)
    {
        this.previewConfiguration = previewConfiguration;
    }

    /**
     * Define an Item specific modifiability handler
     * @param modHandler OwEditPropertiesModifiabilityHandler (can be null)
     * @since 4.2.0.0
     */
    public void setModifiabilityHandler(OwEditPropertiesModifiabilityHandler modHandler)
    {
        this.modifiabilityHandler = modHandler;
    }

    /**
     * Get a handler which will define item specific if properties-view will be modifiable or not.
     * @return OwEditPropertiesModifiabilityHandler or null
     * @since 4.2.0.0
     */
    protected OwEditPropertiesModifiabilityHandler getModifiabilityHandler()
    {
        return this.modifiabilityHandler;
    }
}