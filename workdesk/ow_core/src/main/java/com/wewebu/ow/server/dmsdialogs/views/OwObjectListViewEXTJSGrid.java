package com.wewebu.ow.server.dmsdialogs.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwMimeManager.OwOpenCommand;
import com.wewebu.ow.server.app.OwMultipleSelectionCall;
import com.wewebu.ow.server.app.OwScript;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwRenderRowException;
import com.wewebu.ow.server.exceptions.OwUserOperationException;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwHeaderFieldColumnInfo;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.fieldctrlimpl.OwFieldManagerControlNote.OwNote;
import com.wewebu.ow.server.fieldctrlimpl.OwFieldManagerControlNote.OwNoteDataModel;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwSimpleAttributeBagWriteable;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwTransientBagRepository;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Object list view based on YUI-EXT Grid view. Displays the results of searches.<br/>
 * Use setObjectList and setColumnInfo to set the objects and columns to display.
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
public class OwObjectListViewEXTJSGrid extends OwObjectListViewPluginCache implements OwFieldProvider
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectListViewEXTJSGrid.class);

    protected static final char[] SPECIAL_CHARS = { 'd', 'D', 'j', 'l', 'S', 'w', 'z', 'W', 'F', 'm', 'M', 'n', 't', 'L', 'Y', 'y', 'a', 'A', 'g', 'G', 'h', 'H', 'i', 's', 'O', 'T', 'Z' };
    /**Icon width constant 
     * @since 4.0.0.0*/
    protected static final int ICON_WIDTH = 16;
    /**Default column width 
     * @since 4.0.0.0*/
    protected static final int DEFAULT_COLUMN_WIDTH = 100;
    /**Max column width 
     * @since 4.0.0.0*/
    protected static final int MAX_COLUMN_WIDTH = 500;
    /**Min column width 
     * @since 4.0.0.0*/
    protected static final int MIN_COLUMN_WIDTH = 5;
    /**helper OwFieldColumnInfo for icon column
     *@since 4.1.1.0 */
    private static final OwIconFieldColumnInfo ICON_FIELD_COLUMN_INFO = new OwIconFieldColumnInfo();

    /**
     *  the attribute bag id.
     *  @since 3.2.0.0
     */
    protected static final String COLUMNS_ID_ATTR_BAG = "OwObjectListViewEXTJSGrid_columns";

    /** the size of notes column*/
    private static final int NOTES_DEFAULT_WIDTH = 235;

    /**the BPM plugin id*/
    private static final String BPM_PLUGIN_ID = "com.wewebu.ow.bpm";

    private static final String IS_NOTE_EDITABLE_ELEMENT_NAME = "isNoteEditable";

    /**This definition overrides the OwObjectListView.OBJECT_INDEX_KEY constant because of
     * AJAX framework integration.
     * query string key for the object list index to find the selected object upon onMimeOpenObject. */
    protected static final String OBJECT_INDEX_KEY = "object";

    /** query string key for the plugin index. */
    protected static final String PLUG_INDEX_KEY = "plugin";

    /** XML node name for the config node */
    private static final String TOOLTIP_NODE_NAME = "Tooltip";

    /** XML node name for the list of columns, 
     * which should be read-only in INLINE editing mode.
     * @since 3.1.0.0 */
    public static final String READ_ONLY_COLUMNS_NODE_NAME = "readOnlyColumns";

    /** token in the viewer servlet to be replaced by the DMSID */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_DMSID = "{dmsid}";
    /** token in the viewer servlet to be replaced by the base URL of the server */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_SERVERURL = "{serverurl}";
    /** token in the viewer servlet to be replaced by the base URL of the server with application context*/
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_BASEURL = "{baseurl}";
    /** token in the viewer servlet to be replaced by the property following the : */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START = "{prop";

    /** char to indicate HTML encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_ENCODE_CHAR = ':';

    /** char to indicate JavaScript encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_JSENCODE_CHAR = '~';

    /** char to indicate NO encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_NOENCODE_CHAR = '#';
    /** token in the viewer servlet to be replaced by the property end delimiter */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_END = "}";

    /** tooltip block (property dependent HLTL sequence) end token */
    public static final char TOOLTIP_BLOCK_TOKEN_PROPERTY_END_CHAR = ']';
    public static final String TOOLTIP_BLOCK_TOKEN_PROPERTY_END = "" + TOOLTIP_BLOCK_TOKEN_PROPERTY_END_CHAR;

    /** tooltip block (property dependent HLTL sequence) start token */
    public static final String TOOLTIP_BLOCK_TOKEN_PROPERTY_START = "[";

    /**Data type for unknown/unsupported types*/
    public static final int DATATYPE_UNKNOWN = -1;
    /**Data type for String values*/
    public static final int DATATYPE_STRING = 0;
    /**Data type for Integer values*/
    public static final int DATATYPE_INTEGER = 1;
    /**Data type for Boolean values*/
    public static final int DATATYPE_BOOLEAN = 2;
    /**Data type for Date values*/
    public static final int DATATYPE_DATE = 3;
    /**Data type for Double values*/
    public static final int DATATYPE_DOUBLE = 4;
    /**Data type for String enumeration types*/
    public static final int DATATYPE_ENUM = 5;
    /**Helper Data type to associate as Note property*/
    public static final int DATATYPE_NOTE = 6;

    /**URL/HTTP parameter for column*/
    protected static final String AJAX_PARAM_COLUMN = "col";
    /**URL/HTTP parameter for row*/
    protected static final String AJAX_PARAM_ROW = "row";

    private static final String STORE_EXTJS_COLUMN_INFO_ELEMENT_NAME = "StoreExtJSColumnInfo";

    private static final String NONPERSISTENT_COLUMN_ATTRIBUTE_BAG = "npcab";
    public static String FIRST_PAGE_ACTION_ID = "extjs.first.page";
    public static String LAST_PAGE_ACTION_ID = "extjs.last.page";
    public static String POS1_ACTION_ID = "extjs.pos1";
    public static String END_ACTION_ID = "extjs.pos1";

    /** sort to use
     * @deprecated use set-/{@link #getSort()} instead */
    private OwSort m_sort;

    /** list of objects to display 
     * @deprecated use {@link #getDisplayedPage()} instead for rendering*/
    protected OwObjectCollection m_ObjectList;
    /**
     * flag regarding the plugin cache creation
     * @since 3.1.0.4
     */
    protected boolean pluginCacheCreated = false;

    /** column info 
     * @deprecated use set-/{@link #getColumnInfo()} instead*/
    private Collection m_ColumnInfoList;

    /** instance of the property field class 
     * @deprecated since 4.2.0.0 use set-/{@link #getFieldManager()} instead*/
    protected OwFieldManager m_FieldManager;

    /** refresh content 
     * @deprecated since 4.2.0.0 use set-/{@link #getRefreshContext()} instead*/
    private OwClientRefreshContext m_RefreshContext;

    /** instance of the MIME manager used to open the objects */
    private OwMimeManager m_MimeManager;

    /** maps AJAX column names to property names. Needed since AJAX columns may be reordered on client side. */
    protected HashMap m_AjaxColnameToPropertyMap = new HashMap();

    /** list of AJAX columns */
    private ArrayList m_AjaxColumns = new ArrayList();

    /** text pattern for the tooltip */
    private String m_TooltipTextPattern = null;

    /** number formatter */
    private NumberFormat m_NumberFormat;

    /** the starting row of the Ajax grid. Used to start at the same page after the HTML page has been reloaded. Set upon each data request 
     * @deprecated use {@link #getCurrentPage()} and {@link #getPageSize()} instead*/
    private int m_startrow = 0;

    /** date format for JSON transport. This IS NOT the date format for the user */
    private DateFormat m_TransportDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

    private OwInformalConfiguration m_informalConfiguration;

    /** occurring object types in list*/
    protected HashSet occuringObjectTypes;

    /** use transparent icon for not present icons*/
    protected boolean m_useTransparentIcon = true;
    /** flag that allow to edit notes */
    protected boolean m_isNoteEditable = false;
    /**List with property names for editable notes*/
    private List m_editableNotePropertyNames;
    /**flag to keep columns status */
    private boolean m_columnsModifiedByUser;
    /**hash map with read-only columns definitions
     * @since 3.1.0.0*/
    private LinkedList m_readOnlyColumns;
    /** decimal separator char
     * @since 4.2.0.0 */
    private char decimalSeparator;

    /*****************************************************************
    ** constructors                                                 ** 
    ******************************************************************/

    /** construct a object list view
     * 
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public OwObjectListViewEXTJSGrid(int iViewMask_p)
    {
        super(iViewMask_p);
    }

    /** construct a object list view
     * with default view mask (0).
     */
    public OwObjectListViewEXTJSGrid()
    {
        super();
    }

    /*****************************************************************
    ** init and detach                                              ** 
    ******************************************************************/

    /** init this view after the context has been set
     */
    protected void init() throws Exception
    {
        super.init();

        m_MimeManager = createMimeManager();

        setFieldManager(((OwMainAppContext) getContext()).createFieldManager());
        getFieldManager().setExternalFormTarget(getFormTarget());
        getFieldManager().setFieldProvider(this);

        // init MIME manager as event target
        m_MimeManager.attach(getContext(), null);

        // number formatter
        m_NumberFormat = NumberFormat.getNumberInstance(getContext().getLocale());
        m_NumberFormat.setGroupingUsed(false);

        // always init sort
        setSort(new OwSort(((OwMainAppContext) getContext()).getMaxSortCriteriaCount(), true));

        decimalSeparator = new java.text.DecimalFormatSymbols(getContext().getLocale()).getDecimalSeparator();
    }

    /** (overridable) factory method the instantiate the MimeManager
     * @since 2.5.2.0
     */
    protected OwMimeManager createMimeManager()
    {
        return new OwMimeManager();
    }

    /**
     * @return {@link ArrayList}
     * @since 2.5.2.0
     */
    protected ArrayList getAjaxColumns()
    {
        return m_AjaxColumns;
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();
        // detach the field manager and MIME manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();
    }

    /*****************************************************************
    ** configuration                                                ** 
    ******************************************************************/

    /** optional use the default constructor and set a config node to configure the view with XML 
     * This may override the settings in the ViewMaks, see setViewMask
     * 
     * @param node_p XML node with configuration information
     * @throws Exception 
     */
    public void setConfigNode(Node node_p) throws Exception
    {
        // config parent
        super.setConfigNode(node_p);
        // get tooltip node
        Node tooltipNode = OwXMLDOMUtil.getChildNode(node_p, TOOLTIP_NODE_NAME);
        if (tooltipNode != null)
        {
            Node tooltipChildNode = tooltipNode.getFirstChild();
            if (tooltipChildNode != null)
            {
                if (tooltipChildNode.getNodeType() == Node.TEXT_NODE)
                {
                    m_TooltipTextPattern = tooltipChildNode.getNodeValue();
                }
                else if (tooltipChildNode.getNodeType() == Node.CDATA_SECTION_NODE)
                {
                    m_TooltipTextPattern = tooltipChildNode.getNodeValue();
                }
            }
        }

        Node editableNoteNode = OwXMLDOMUtil.getChildNode(node_p, IS_NOTE_EDITABLE_ELEMENT_NAME);
        if (editableNoteNode != null)
        {
            Node editableNodeNodeContent = editableNoteNode.getFirstChild();
            if (editableNodeNodeContent != null)
            {
                String value = editableNodeNodeContent.getNodeValue().trim();
                if (value != null)
                {
                    setNoteEditable(Boolean.valueOf(value).booleanValue());
                }
            }
        }

        m_editableNotePropertyNames = new LinkedList();
        Node editablePropertiesNode = OwXMLDOMUtil.getChildNode(node_p, "NoteProperties");
        if (editablePropertiesNode != null && editablePropertiesNode.getNodeType() == Node.ELEMENT_NODE)
        {

            Element propertiesNotesElement = (Element) editablePropertiesNode;
            NodeList properteiesNodeList = propertiesNotesElement.getElementsByTagName("NoteProperty");
            if (properteiesNodeList != null)
            {
                for (int i = 0; i < properteiesNodeList.getLength(); i++)
                {
                    Node propertyNode = properteiesNodeList.item(i);
                    if (propertyNode != null && propertyNode.getFirstChild() != null)
                    {
                        String propertyName = propertyNode.getFirstChild().getNodeValue().trim();
                        m_editableNotePropertyNames.add(propertyName);
                    }
                }
            }
        }
        checkBPMConfiguration(node_p);
        initReadOnlyColumnList(node_p);

        informalInit(node_p);
    }

    private void informalInit(Node configNode_p)
    {
        try
        {
            m_informalConfiguration = new OwInformalConfiguration();

            OwXMLUtil configUtill = new OwStandardXMLUtil(configNode_p);
            List<String> cdatas = configUtill.getSafeCDATAList();
            if (cdatas != null && !cdatas.isEmpty())
            {
                StringBuilder xmlBuilder = new StringBuilder();
                for (String cdata : cdatas)
                {
                    xmlBuilder.append(cdata);
                }

                DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = domfactory.newDocumentBuilder();
                Document informalDocument = documentBuilder.parse(new InputSource(new ByteArrayInputStream(xmlBuilder.toString().getBytes())));

                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xpath = xPathFactory.newXPath();
                XPathExpression xpathExpression = xpath.compile("/informal/controls/property");
                Object propertiesResult = xpathExpression.evaluate(informalDocument, XPathConstants.NODESET);
                NodeList properties = (NodeList) propertiesResult;
                for (int i = 0; i < properties.getLength(); i++)
                {
                    Node property = properties.item(i);
                    OwXMLUtil propertyUtil = new OwStandardXMLUtil(property);
                    String name = propertyUtil.getSafeStringAttributeValue("name", "");
                    String type = propertyUtil.getSafeStringAttributeValue("type", "");

                    m_informalConfiguration.add(new OwPropetyControl(name, type));
                }
            }

        }
        catch (Exception e)
        {
            LOG.error("Informal configuration error.", e);
        }

    }

    /**
     * Check the note configuration for BPM
     * @param node_p
     * @throws OwConfigurationException
     */
    private void checkBPMConfiguration(Node node_p) throws OwConfigurationException
    {
        Node resultListNode = node_p.getParentNode();
        if (resultListNode != null)
        {
            Node pluginNode = resultListNode.getParentNode();
            if (pluginNode != null)
            {
                Node idNode = OwXMLDOMUtil.getChildNode(pluginNode, "id");
                if (idNode != null)
                {
                    String masterPluginId = OwXMLDOMUtil.getElementText((Element) idNode);
                    if (masterPluginId.equals(BPM_PLUGIN_ID) && isNoteEditable())
                    {
                        throw new OwConfigurationException("In BPM AJAX view the notes should not be editable");
                    }
                }
            }
        }
    }

    /**
     * Initialize the Collection of read only column definitions. 
     * @param node_p Node to use for configuration analysis
     * @since 3.1.0.0
     */
    private void initReadOnlyColumnList(Node node_p)
    {
        m_readOnlyColumns = new LinkedList();
        Node readOnly = OwXMLDOMUtil.getChildNode(node_p, READ_ONLY_COLUMNS_NODE_NAME);
        if (readOnly != null)
        {
            m_readOnlyColumns.addAll(OwXMLDOMUtil.getSafeStringList(readOnly));
        }
    }

    /** insert the document function plugins for the requested row index and object
     * @param obj_p OwObject to create Function plugin for
     * @param iIndex_p the row / object index
     * @param instancePluginsList_p Collection of plugins that are visible (have there own column) together with the global index
     */
    protected String getDocumentInstanceFunctionPluginLinks(OwObject obj_p, int iIndex_p, Collection instancePluginsList_p) throws Exception
    {
        StringBuilder sbLinks = new StringBuilder();
        // iterate over preinstantiated plugins and create HTML 
        Iterator it = instancePluginsList_p.iterator();
        while (it.hasNext())
        {
            OwPluginEntry plugInEntry = (OwPluginEntry) it.next();
            // check if object type is supported by plugin
            if (!getPluginCache().isPluginDisabledForAllObjects(plugInEntry))
            {
                boolean isPluginEnabled;
                OwPluginStatus status = getPluginCache().getCachedPluginState(plugInEntry, obj_p);
                if (status.isCached())
                {
                    isPluginEnabled = status.isEnabled();
                }
                else
                {
                    // check if object type is supported by plugin
                    isPluginEnabled = plugInEntry.m_Plugin.isEnabled(obj_p, getParentObject(), OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
                }

                int maxNumberOfIconsForEntry = getPluginCache().getMaximumNumberOfIconsForEntry(plugInEntry);
                if (isPluginEnabled)
                {
                    // get the No Event Flag indicating if plugin should receive a click event and therefore  need to wrap a anchor around it.
                    String iconHTMLAsString = plugInEntry.m_Plugin.getIconHTML(obj_p, null);
                    if (iconHTMLAsString == null || iconHTMLAsString.trim().length() == 0)
                    {
                        iconHTMLAsString = "";
                        iconHTMLAsString = fillWithBlankIcons(maxNumberOfIconsForEntry, iconHTMLAsString);
                    }
                    if (plugInEntry.m_Plugin.getNoEvent())
                    {
                        // === do not create anchor
                        OwIcon owIcon = new OwIcon(iconHTMLAsString);
                        if (owIcon.getNumberOfIcons() < maxNumberOfIconsForEntry)
                        {
                            sbLinks.append(fillWithBlankIcons(maxNumberOfIconsForEntry - owIcon.getNumberOfIcons(), ""));
                        }
                        sbLinks.append(iconHTMLAsString);
                    }
                    else
                    {
                        // === create anchor with reference to the selected object and a tooltip
                        OwIcon owIcon = new OwIcon(iconHTMLAsString);
                        if (owIcon.getNumberOfIcons() < maxNumberOfIconsForEntry)
                        {
                            sbLinks.append(fillWithBlankIcons(maxNumberOfIconsForEntry - owIcon.getNumberOfIcons(), ""));
                        }
                        sbLinks.append("<a href=\"");
                        StringBuilder url = new StringBuilder(OBJECT_INDEX_KEY).append("=").append(iIndex_p);
                        url = url.append("&"); // !DO NOT! change this to &amp; !!!!!!!
                        url = url.append(PLUG_INDEX_KEY).append("=").append(plugInEntry.m_iIndex);
                        sbLinks.append(getEventURL("PluginEvent", url.toString()));
                        sbLinks.append("\" title=\"");
                        sbLinks.append(plugInEntry.m_Plugin.getTooltip());
                        sbLinks.append("\" class=\"OwGeneralList_Plugin_a\">");
                        sbLinks.append(iconHTMLAsString);
                        sbLinks.append("</a>");
                    }
                }
                else
                {
                    sbLinks.append(fillWithBlankIcons(maxNumberOfIconsForEntry, ""));
                }
            }
        }
        return (sbLinks.toString());
    }

    private String fillWithBlankIcons(int maxNumberOfIconsForEntry_p, String iconHTMLAsString_p) throws Exception
    {
        StringBuilder ret = new StringBuilder(iconHTMLAsString_p);
        for (int i = 0; i < maxNumberOfIconsForEntry_p; i++)
        {
            ret.append(createEmptyIcon());
        }
        return ret.toString();
    }

    /**
     * Return the empty icon HTML code.
     * @return the HTML code for a transparent icon
     * @throws Exception
     * @since 2.5.2.0
     */
    private String createEmptyIcon() throws Exception
    {
        String iconHTMLAsString = "";
        if (m_useTransparentIcon)
        {
            iconHTMLAsString = "<img class=\"OwFunctionIcon\" src=\"" + getContext().getDesignURL() + "/images/empty_plugin_small.gif" + "\" alt=\"\" title=\"\" />";
        }
        return iconHTMLAsString;
    }

    /** render the view
     * 
     * @param w_p Writer to write to
     */
    public void onRender(Writer w_p) throws Exception
    {
        //Due to ajax calls, the function definitions must be there every time
        getContext().renderJSInclude("/js/wewebugridheaderextension.js", w_p);
        getContext().renderJSInclude("/js/weweburowselectionmodel.js", w_p); // directly used
        getContext().renderJSInclude("/js/wewebugridview.js", w_p); // directly used

        getContext().renderJSInclude("/js/owobjectlistviewextjsgrid.js", w_p);
        //getContext().renderJSInclude("/js/jsgridpagingextension.js", w_p);
        getContext().renderJSInclude("/js/wewebunoteseditor.js", w_p);
        getContext().renderJSInclude("/js/wewebuenumeditor.js", w_p);

        if (getObjectList() != null || getObjectIterable() != null)
        {
            // always reset MIME manager and field manager
            m_MimeManager.reset();
            getFieldManager().reset();
            setPluginCache(createPluginStatusCacheUtility(getObjectInstancePluginList()));
            pluginCacheCreated = true;
            // configure extjs after loading
            w_p.write("<script type=\"text/javascript\">\n");
            w_p.write("Ext.BLANK_IMAGE_URL = \"");
            w_p.write(getContext().getBaseURL());
            w_p.write("/js/extjs/resources/images/default/s.gif\";\n");
            w_p.write("Date.monthNames =\n['");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.January", "January"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.February", "February"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.March", "March"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.April", "April"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.May", "May"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.June", "June"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.July", "July"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.August", "August"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.September", "September"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.October", "October"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.November", "November"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.December", "December"));
            w_p.write("'];\nDate.dayNames =\n['");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Sunday", "Sunday"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Monday", "Monday"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Tuesday", "Tuesday"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Wednesday", "Wednesday"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Thursday", "Thursday"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Friday", "Friday"));
            w_p.write("',\n'");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Saturday", "Saturday"));
            w_p.write("'];\n");

            w_p.write("if(Ext.DatePicker){\n");
            w_p.write("   Ext.apply(Ext.DatePicker.prototype, {\n");
            Node fieldManagerNode = ((OwMainAppContext) getContext()).getConfiguration().getFieldManagerConfiguration().getNode();
            if (fieldManagerNode != null)
            {
                Node node = OwXMLDOMUtil.getChildNode(fieldManagerNode, "DatePickerStartDay");
                if (node != null)
                {
                    String startDay = node.getFirstChild().getNodeValue().trim();
                    w_p.write("      startDay : " + startDay + ",\n");
                }
            }
            String dateFormat = OwObjectListViewEXTJSGrid.convertDateFormat(((OwMainAppContext) getContext()).getDateFormatString());
            w_p.write("      todayText         : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Today", "Today"));
            w_p.write("\",\n      minText           : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.BeforeMinimumDate", "This date is below the minimum date."));
            w_p.write("\",\n      maxText           : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.AfterMaximumDate", "This date exceeds the maximum date."));
            w_p.write("\",\n      disabledDaysText  : \"\",\n");
            w_p.write("      disabledDatesText : \"\",\n");
            w_p.write("      invalidText:\"\",\n");
            w_p.write("      monthNames        : Date.monthNames,\n");
            w_p.write("      dayNames          : Date.dayNames,\n");
            w_p.write("      nextText          : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.NextMonth", "Next Month (Ctrl+Right)"));
            w_p.write("\",\n      prevText          : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.PreviousMonth", "Previous Month (Ctrl+Left)"));
            w_p.write("\",\n      monthYearText     : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.ChooseMonth", "Select a month (Ctrl+Up/Down to change the years)"));
            w_p.write("\",\n      todayTip          : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.Spacebar", "{0} (space bar)"));
            w_p.write("\",\n      format            : \"");
            w_p.write(dateFormat);
            w_p.write("\"\n   });\n");
            w_p.write("}\n");
            w_p.write("\n");

            w_p.write("if(Ext.form.DateField){\n");
            w_p.write("    Ext.apply(Ext.form.DateField.prototype, {\n");
            w_p.write("    disabledDaysText  : \"\",\n");
            w_p.write("    disabledDatesText : \"\",\n");
            w_p.write("    minText           : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.BeforeMinimumDate", "Dieses Datum ist vor dem Mindestdatum."));
            w_p.write("\",\n    maxText           : \"");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwEditablePropertyDate.AfterMaximumDate", "Dieses Datum ist nach dem Maximaldatum."));
            w_p.write("\",\n    invalidText       : \"\",\n");
            w_p.write("    format            : \"");
            w_p.write(dateFormat);
            w_p.write("\"\n    })\n");
            w_p.write("    }\n");

            w_p.write("</script>\n");
            // register keyboard event for [Ctrl]-[A]: Select all
            getContext().registerKeyEvent(65, OwAppContext.KEYBOARD_CTRLKEY_CTRL, "javascript:ExtjsGrid_SelectAll();", getContext().localize("app.OwObjectListView.toggleselectall", "Select/Deselect all"));
            // register keyboard event for [Page up]: Previous Page
            getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_PAGE_UP, 0, "javascript:ExtjsGrid_previousPage();", getContext().localize("app.OwObjectListView.shortcutpreviouspage", "Previous page"));
            // register keyboard event for [Page down]: Next Page
            getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_PAGE_DN, 0, "javascript:ExtjsGrid_nextPage();", getContext().localize("app.OwObjectListView.shortcutnextpage", "Next page"));

            //register keyboard event for [CTRL]-[Page up]: First Page
            //getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_PAGE_UP, OwAppContext.KEYBOARD_CTRLKEY_CTRL, "javascript:ExtjsGrid_firstPage(e.keyCode,e);", getContext().localize("app.OwObjectListView.shortcutfirstpage", "First page"));
            getContext().registerKeyAction(FIRST_PAGE_ACTION_ID, "javascript:ExtjsGrid_firstPage(e.keyCode,e);", getContext().localize("app.OwObjectListView.shortcutfirstpage", "First page"));

            // register keyboard event for [CTRL]-[Page down]: Last Page
            // getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_PAGE_DN, OwAppContext.KEYBOARD_CTRLKEY_CTRL, "javascript:ExtjsGrid_lastPage(e.keyCode,e);", getContext().localize("app.OwObjectListView.shortcutlastpage", "Last page"));
            getContext().registerKeyAction(LAST_PAGE_ACTION_ID, "javascript:ExtjsGrid_lastPage(e.keyCode,e);", getContext().localize("app.OwObjectListView.shortcutlastpage", "Last page"));

            // register keyboard event for [HOME]: First Row
            getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_POS1, 0, "javascript:ExtjsGrid_firstRow();", getContext().localize("app.OwObjectListView.shortcutfirstrow", "First row"));

            // register keyboard event for [END]: Last Row
            getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_END, 0, "javascript:ExtjsGrid_lastRow();", getContext().localize("app.OwObjectListView.shortcutlastrow", "Last row"));

            // register all plugin shortcuts
            for (Iterator iterator = getContextMenuFunction().iterator(); iterator.hasNext();)
            {
                OwPluginEntry p_entry = (OwPluginEntry) iterator.next();
                if (showsContextMenuPlugin(p_entry.getPlugin()))
                {
                    ((OwMainAppContext) getContext()).registerPluginKeyEvent(p_entry.m_Plugin.getPluginID(), "javascript:ExtjsGrid_PluginEvent(" + p_entry.m_iIndex + ");", null, p_entry.m_Plugin.getDefaultLabel());
                }
            }

            // render the JSP page
            renderMainRegion(w_p);

            ((OwMainAppContext) getContext()).addUniqueFinalScript(new OwScript("\nif (usesEXTJSGrid(true)) {\n" + "                  OwObjectListViewEXTJSGrid.init();" + "}\n", OwScript.DEFAULT_PRIORITY - 10));
        }
        else
        {
            w_p.write("<span id=\"emptyList\"  class=\"OwEmptyTextMessage\">");
            w_p.write(getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingEmptyMsg", "No items to display"));
            w_p.write("</span>");
        }

        ((OwMainAppContext) getContext()).registerMouseAction(OwObjectListView.SELECT_DESELECT_NONCONSECUTIVE_OBJECTS_ACTION_ID, new OwString("OwObjectListView.nonconsecutive.select.description", "Select/Deselect result list entry."));

    }

    /** (overridable) render the JSP page
     * 
     * @param w_p Writer to write to
     */
    protected void renderMainRegion(Writer w_p) throws Exception
    {
        // redirect to render page
        serverSideDesignInclude("OwObjectListViewEXTJSGrid.jsp", w_p);
    }

    /**
     * Returns the current start row. Called by the JSP page to configure the initial state
     * of the AJAX grid. Used to initiate the Grid on the last displayed page after a page
     * reload.
     * 
     * @return integer representing current start row
     */
    public int getStartRow()
    {
        return getCurrentPage() * getPageSize();
    }

    /*****************************************************************
    ** onAjax Methods                                               ** 
    ******************************************************************/

    /** called upon AJAX request "ReadList"
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxReadList(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        if ((request_p.getParameter("refresh") != null))
        {
            if (null != this.getRefreshContext())
            {
                this.getRefreshContext().onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
            }
        }

        // sorting
        processSortDefinition(request_p);

        // paging
        int iStart = 0;
        int totalCount = getCount();

        if ((request_p.getParameter("start") != null))
        {
            try
            {
                iStart = Integer.parseInt(request_p.getParameter("start"));
                setCurrentPage(iStart / getPageSize());
            }
            catch (Exception e)
            {
            }
        }

        //cache creation operation invoked only if necessary
        if (!pluginCacheCreated)
        {
            if (totalCount != 0)
            {
                setPluginCache(createPluginStatusCacheUtility(getObjectInstancePluginList()));
            }
        }
        else
        {
            pluginCacheCreated = false;
        }
        // get writer
        Writer jsonResponseWriter = response_p.getWriter();

        // start response
        jsonResponseWriter.write("({items:[");
        int rejected = 0;
        int i = getStartRow();

        for (OwObject obj : getDisplayedPage())
        {
            try
            {
                StringWriter jsonWriter = new StringWriter(8192);

                processOccurred(obj);
                //check the validity of the current object
                obj.getDMSID();
                // delimiter between records
                if (i > iStart)
                {
                    jsonWriter.write(",");
                }
                // start record description
                jsonWriter.write("{");
                // add ID 
                jsonWriter.write("id:" + i);
                // add icon URL 
                jsonWriter.write(",icon_link:");
                try
                {
                    StringWriter sw = new StringWriter();
                    m_MimeManager.insertIconLink(sw, obj);
                    jsonWriter.write(encodeJsonString(sw.toString()));
                }
                catch (Exception e)
                {
                    LOG.debug("Exception executing insertIconLink of the MimeManager.", e);
                    jsonWriter.write(encodeJsonString(null));
                }
                //check if row is selected.
                jsonWriter.write(",row_selected:");
                jsonWriter.write(Boolean.toString(isObjectSelectionPersisted(i)));

                // add checkout icons
                jsonWriter.write(",checkout_icon:");
                jsonWriter.write(encodeJsonString(getVersionStateIcon(obj)));
                // add version string
                jsonWriter.write(",version_str:");
                jsonWriter.write(encodeJsonString(getVersionRepresentation(obj)));

                //lock icon
                jsonWriter.write(",locked_icon:");
                jsonWriter.write(encodeJsonString(getLockedStateIcon(obj)));

                // add plugin links
                String sPlugins = "";
                if (hasViewMask(VIEW_MASK_INSTANCE_PLUGINS))
                {
                    Collection instancePluginsList = getObjectInstancePluginList();
                    try
                    {
                        sPlugins = getDocumentInstanceFunctionPluginLinks(obj, i, instancePluginsList);
                    }
                    catch (Exception e)
                    {
                    }
                }
                jsonWriter.write(",plugin_links:");
                jsonWriter.write(encodeJsonString(sPlugins));
                // add tooltip
                if (hasToolTipPattern())
                {
                    jsonWriter.write(",tooltip:");
                    String sTooltip = getTooltipUrl(obj);

                    jsonWriter.write(encodeJsonString(sTooltip));

                }
                // add default action
                jsonWriter.write(",default_action:");
                jsonWriter.write(encodeJsonString(getDefaultActionString(obj)));

                jsonWriter.write(",default_newwindow:false");
                // add special row class name
                jsonWriter.write(",row_class_name:");
                jsonWriter.write(encodeJsonString(getRowClassName(i, obj)));
                // iterate through all columns
                for (int ii = 0; ii < m_AjaxColumns.size(); ii++)
                {
                    // get the column info for this column
                    OwAjaxColumnEntry ace = (OwAjaxColumnEntry) m_AjaxColnameToPropertyMap.get("col" + ii);
                    // on invalid column info, throw OwRenderRowException and ignore the row
                    if (ace == null)
                    {
                        throw new OwRenderRowException("OwObjectListViewEXTJSGrid.onAjaxReadList: Invalid column info (column=" + ii + "), OwAjaxColumnEntry is null...");
                    }
                    OwFieldColumnInfo colInfo = ace.getFieldColumnInfo();

                    if (colInfo.getPropertyName().equals("ow_icon_column"))
                    {
                        //nothing to do for icon column, continue.
                        continue;
                    }

                    // build field depending on editDataType
                    try
                    {
                        OwProperty prop = obj.getProperty(colInfo.getPropertyName());
                        if (prop.getValue() != null)
                        {
                            // create prefix variable name like : ",col1:"
                            jsonWriter.write(',');
                            jsonWriter.write(ace.getColumnName());
                            jsonWriter.write(':');

                            if (ace.getEditDataType() == DATATYPE_DATE)
                            {
                                if (!(prop.getValue() instanceof java.util.Date))
                                {
                                    LOG.error("The item at position " + i + "column: " + ace.getColumnName() + " has a property named value that is not an instance of java.util.Date class!");
                                    String columnName = ace.getColumnName();
                                    String displayName = ace.getFieldColumnInfo().getDisplayName(getContext().getLocale());
                                    if (ace.getFieldColumnInfo() != null && displayName != null)
                                    {
                                        columnName = displayName;
                                    }
                                    throw new OwConfigurationException(getContext().localize1("app.OwObjectListViewEXTJSGrid.err.typeConversionFailed", "Property type conversion failed for column %1.", columnName));
                                }
                            }
                            jsonWriter.write(getEncodedPropertyValue(ace, prop));
                        }
                    }
                    catch (OwObjectNotFoundException e)
                    {
                        //do nothing, this object doesn't have this property
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("On rendering property for column: " + ace.getColumnName() + " the following error occured: ", e);
                        }
                    }
                    catch (Exception e)
                    {
                        //cannot render properties for object
                        //abandon and report error
                        LOG.error("Cannot render the properties for object at position: " + i + ", property number: " + ii + " column name: " + ace.getColumnName(), e);
                        response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        String errMsg = e.getLocalizedMessage();
                        response_p.getWriter().write(errMsg);
                        return;
                    }
                }
                // end record description
                jsonWriter.write("}");
                //write row representation out to current writer
                jsonResponseWriter.write(jsonWriter.getBuffer().toString());
                i++;
            }
            catch (OwRenderRowException e)
            {
                LOG.debug(e.getMessage() + " Could not render list element, row rejected!");
                rejected++;
            }
            catch (Exception e)
            {
                LOG.error("Could not render list element!", e);
                rejected++;
            }
        }

        // finish response
        jsonResponseWriter.write("],count:");
        jsonResponseWriter.write(Integer.toString(totalCount - rejected));
        jsonResponseWriter.write(",isComplete:");
        jsonResponseWriter.write(Boolean.toString(isCollectionComplete()));
        if (totalCount < 0)
        {
            jsonResponseWriter.write(",canPageNext:");
            jsonResponseWriter.write(Boolean.toString(canPageNext()));
            jsonResponseWriter.write(",canPagePrev:");
            jsonResponseWriter.write(Boolean.toString(canPagePrev()));
        }
        jsonResponseWriter.write("}");
        jsonResponseWriter.write(")");

    }

    /**
     * Process entering request for changes in sorting and notify listener of changes. 
     * @param req HttpServletRequest
     * @throws Exception
     * @since 4.2.0.0
     */
    protected void processSortDefinition(HttpServletRequest req) throws Exception
    {
        // sorting
        String sParamSort = req.getParameter("sort");
        String sParamDir = req.getParameter("dir");
        if ((sParamSort != null) && (sParamDir != null))
        {
            OwAjaxColumnEntry ace = (OwAjaxColumnEntry) m_AjaxColnameToPropertyMap.get(sParamSort);
            if (ace != null && ace.getFieldColumnInfo() != null)
            {
                String strSortProperty = ace.getFieldColumnInfo().getPropertyName();
                if (strSortProperty != null)
                {
                    //sort order ascending or descending
                    boolean fAsc_p = sParamDir.toLowerCase().equals("asc");
                    //get sort criteria with highest priority
                    OwSortCriteria lastCrit = getSort().getLastCriteria();

                    //if last criteria is null or has changed the list must be sorted anew.
                    if (lastCrit == null || !(lastCrit.getPropertyName().equals(strSortProperty) && lastCrit.getAscFlag() == fAsc_p))
                    {
                        // set the criteria ascending or descending
                        getSort().setCriteria(strSortProperty, fAsc_p);

                        // notify client
                        if (getEventListner() != null)
                        {
                            getEventListner().onObjectListViewSort(getSort(), strSortProperty);
                        }
                    }
                }
            }
        }
    }

    /**
     * Return an icon representation of the version state for provided object.
     * @param obj OwObject
     * @return String representing version state icon
     * @throws Exception
     * @since 4.2.0.0
     */
    protected String getVersionStateIcon(OwObject obj) throws Exception
    {
        StringBuilder buf = new StringBuilder("<img src=\"");
        buf.append(getContext().getDesignURL());
        try
        {
            if (obj.hasVersionSeries() && obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
            {
                buf.append("/images/checkedout.png\" title=\"");
                buf.append(getContext().localize("app.Checked_out", "Checked out"));
                buf.append("\" alt=\"");
                buf.append(getContext().localize("app.Checked_out", "Checked out"));
            }
            else
            {
                buf.append("/images/notcheckedout.png\" alt=\"\" title=\"");
            }
        }
        catch (Exception e)
        {
            buf.append("/images/notcheckedout.png\" alt=\"\" title=\"");
        }
        buf.append("\" class=\"OwVersionState\" />");
        return buf.toString();
    }

    /**
     * Return a String representing the Version of provided obj.
     * @param obj OwObject
     * @return String representing object version (empty string in case object not versionable)
     * @throws Exception
     * @since 4.2.0.0
     */
    protected String getVersionRepresentation(OwObject obj) throws Exception
    {
        String sVer = "";
        try
        {
            if (obj.hasVersionSeries())
            {
                sVer = obj.getVersion().getVersionInfo();
            }
        }
        catch (Exception e)
        {
        }
        return sVer;
    }

    /**
     * Return a String which contains the Icon for representing the lock status of provide object. 
     * @param obj OwObject.
     * @return String
     * @throws Exception
     * @since 4.2.0.0
     */
    protected String getLockedStateIcon(OwObject obj) throws Exception
    {
        StringBuilder buf = new StringBuilder("<img src=\"");
        buf.append(getContext().getDesignURL());
        String lockedTooltip = "";
        if (obj.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
        {
            lockedTooltip = getContext().localize("image.locked", "Item locked");
            buf.append("/images/locked.png");
        }
        else
        {
            buf.append("/images/notlocked.png");
        }
        buf.append("\" alt=\"");
        buf.append(lockedTooltip);
        buf.append("\" title=\"");
        buf.append(lockedTooltip);
        buf.append("\" class=\"OwLockIcon\" />");
        return buf.toString();
    }

    /**
     * Get a string which represents the default action for provided object.
     * Basically will check if an open command exist for the object. 
     * @param obj OwObject
     * @return String, can return <code>null</code> in case no open command exist
     * @throws Exception
     * @since 4.2.0.0
     */
    protected String getDefaultActionString(OwObject obj) throws Exception
    {
        String action = null;
        OwOpenCommand ocmd = m_MimeManager.getOpenCommand(obj);
        if (null != ocmd)
        {
            if (ocmd.canGetScript())
            {
                action = OwMimeManager.SCRIPT_URL_PREFIX + ocmd.getScript(OwMimeManager.VIEWER_MODE_DEFAULT, null, false);
            }
            else
            {
                action = ocmd.getURL();
            }
        }
        return action;
    }

    /**
     * Returns the Tooltip URL
     * @param obj_p
     * @return String
     * @since 2.5.3.1
     */
    protected String getTooltipUrl(OwObject obj_p)
    {
        String sTooltip = null;
        if (hasToolTipPattern())
        {
            // add style class for tooltip
            StringBuilder tooltipBuilder = new StringBuilder("<div class=\"OwTooltip\">");
            tooltipBuilder.append(getToolTipPattern());
            tooltipBuilder.append("</div>");

            try
            {
                OwString.replaceAll(tooltipBuilder, VIEWER_SERVLET_REPLACE_TOKEN_BASEURL, getContext().getBaseURL() + "/");
                OwString.replaceAll(tooltipBuilder, VIEWER_SERVLET_REPLACE_TOKEN_SERVERURL, getContext().getServerURL() + "/");
                OwString.replaceAll(tooltipBuilder, VIEWER_SERVLET_REPLACE_TOKEN_DMSID, OwAppContext.encodeURL(obj_p.getDMSID()));
                sTooltip = replaceProperties((OwMainAppContext) getContext(), tooltipBuilder.toString(), obj_p);
                sTooltip = localizeStrings((OwMainAppContext) getContext(), sTooltip);
            }
            catch (Exception e)
            {
                LOG.error("Exception operating the tooltip for the EXTJSGrid...", e);
                sTooltip = null;
            }
        }
        return sTooltip;
    }

    /**
     * Called upon AJAX request "PersistSelection"
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxPersistSelection(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        persistAjaxTriggeredSelection(request_p, response_p);
    }

    /**
     * called upon getEnumInfo AJAX call.<br>
     * encodes the enumeration entries with the following format :<br>
     * <code>[[value,display_text],display_text]</code> 
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxgetEnumInfo(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        String rowStr = request_p.getParameter(AJAX_PARAM_ROW);
        String colStr = request_p.getParameter(AJAX_PARAM_COLUMN);

        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);

        OwAjaxColumnEntry columnEntry = (OwAjaxColumnEntry) m_AjaxColumns.get(col);
        OwFieldColumnInfo fieldColumnInfo = columnEntry.getFieldColumnInfo();
        String propertyName = fieldColumnInfo.getPropertyName();
        // get writer
        StringBuilder jsonWriter = new StringBuilder();

        // start response
        jsonWriter.append("(");
        jsonWriter.append("{");

        Iterator<OwObject> objIt = getDisplayedPage().iterator();
        OwObject theObject;
        do
        {
            theObject = objIt.next();
        } while (objIt.hasNext() && (row-- >= 0));

        OwObjectClass theClass = theObject.getObjectClass();
        jsonWriter.append("fields:[");
        try
        {
            OwPropertyClass propertyClass = theClass.getPropertyClass(propertyName);
            if (propertyClass.isEnum())
            {
                StringBuffer sEnums = new StringBuffer();
                Iterator it = propertyClass.getEnums().iterator();
                while (it.hasNext())
                {
                    if (sEnums.length() > 0)
                    {
                        sEnums.append(",");
                    }
                    OwEnum Item = (OwEnum) it.next();
                    String displayText = Item.getDisplayName(getContext().getLocale());
                    String strValue = Item.getValue().toString();
                    String packedValue = "[[" + encodeJsonString(strValue) + "," + encodeJsonString(displayText) + "]]";
                    sEnums.append("{value:");
                    sEnums.append(encodeJsonString(packedValue));
                    sEnums.append(",text:");
                    sEnums.append(encodeJsonString(displayText));
                    sEnums.append("}");
                }
                jsonWriter.append(sEnums.toString());
            }

        }
        catch (Exception e)
        {
            LOG.debug("OwObjectListViewEXTJSGrid.onAjaxgetEnumInfo(): Could not access the property  " + propertyName, e);
        }
        jsonWriter.append("]");
        // finish response
        jsonWriter.append("}");
        jsonWriter.append(")");
        response_p.getWriter().write(jsonWriter.toString());
    }

    /** called upon AJAX request "getColumnInfo"
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxgetColumnInfo(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // clear column name to property map
        m_AjaxColnameToPropertyMap.clear();

        Writer w = response_p.getWriter();
        // get writer

        // start response
        w.write("(");
        w.write("{");
        w.write("field_defs:[");

        //start with default resource
        OwResource resource = null;
        // iterate through all fields
        for (int i = 0; i < m_AjaxColumns.size(); i++)
        {
            StringBuilder jsonWriter = new StringBuilder();
            // get the column info for this column
            OwAjaxColumnEntry ace = (OwAjaxColumnEntry) m_AjaxColumns.get(i);
            OwFieldColumnInfo colInfo = ace.getFieldColumnInfo();
            // set new name of column
            ace.setColumnName("col" + i);
            // add to column name map
            m_AjaxColnameToPropertyMap.put(ace.getColumnName(), ace);
            //get the property name
            String propName = colInfo.getPropertyName();
            if (i > 0)
            {
                // assemble column description
                jsonWriter.append(",");
            }

            if (propName.equals("ow_icon_column"))
            {
                jsonWriter.append("{id:\"");
                jsonWriter.append(ace.getColumnName());
                jsonWriter.append("\",display_name:\"ow_icon_column\",");
                //                proposed width isn't set
                if (ace.m_iColumnWidth == -1 || !m_columnsModifiedByUser)
                {
                    int proposedWidth = getPluginCache().getMaximumNumberOfIcons();

                    ace.m_iColumnWidth = proposedWidth;
                    jsonWriter.append("calculate:true,");
                }
                jsonWriter.append("proposed_width:");
                jsonWriter.append(Integer.toString(ace.m_iColumnWidth));
                jsonWriter.append(",can_edit:false,can_filter:false,filter_active:false,sortable:");
                jsonWriter.append(Boolean.toString(colInfo.isSortable()));
                jsonWriter.append("}");
            }
            else
            {
                jsonWriter.append("{id:\"");
                jsonWriter.append(ace.getColumnName());
                jsonWriter.append("\",display_name:");
                jsonWriter.append(encodeJsonString(colInfo.getDisplayName(getContext().getLocale())));
                jsonWriter.append(",proposed_width:" + ace.getColumnWidth());//implicit convert int to String
                jsonWriter.append(",sortable:" + colInfo.isSortable());

                OwSortCriteria lastSortingCriteria = this.getSort().getLastCriteria();
                if (lastSortingCriteria != null && lastSortingCriteria.getPropertyName() != null && lastSortingCriteria.getPropertyName().equals(propName) && ace.getFieldColumnInfo().isSortable())
                {
                    jsonWriter.append(",sort_dir:\"");
                    jsonWriter.append(lastSortingCriteria.getAscFlag() ? "ASC" : "DESC");
                    jsonWriter.append("\"");
                }

                OwPropertyClass colPropClass = null;
                try
                {
                    colPropClass = (OwPropertyClass) ((OwMainAppContext) getContext()).getNetwork().getFieldDefinition(propName, resource == null ? null : resource.getID());
                }
                catch (OwObjectNotFoundException nfex)
                {
                    LOG.info("OwObjectListViewExt.onAjaxgetColumnInfo: Property not found " + propName + ", search in other sources!");
                    OwMainAppContext context = (OwMainAppContext) getContext();
                    Iterator itRes = context.getNetwork().getResourceIDs();
                    while (itRes != null && itRes.hasNext() && colPropClass == null)
                    {
                        try
                        {
                            resource = context.getNetwork().getResource(itRes.next().toString());
                            colPropClass = (OwPropertyClass) context.getNetwork().getFieldDefinition(propName, resource.getID());
                        }
                        catch (OwObjectNotFoundException ex)
                        {
                            //silently ignore the exception, we don't have a hasFieldDefinition method
                        }
                    }
                }
                catch (Exception cce)
                {
                    // do nothing here. colPropClass stays null.
                    // if some exception occurs (OwObjectNotFound, ClassCastException, ..), the user just can not
                    // edit this column. The contents are still displayed by the FieldManager, but for editing we
                    // need exact information about the column.
                }
                /* 0:String 1:Integer 2:Boolean 3:Date 4:Double 5:Enum 6:Note property
                 * change data type only if it is editable, keep unknown to use default rendering*/
                int editDataType = DATATYPE_UNKNOWN;
                boolean canEdit = false;
                boolean editRequired = false;
                if (colPropClass != null)
                {
                    try
                    {
                        canEdit = isPropertyEditable(colPropClass);
                        if (canEdit)
                        {
                            editRequired = colPropClass.isRequired();
                            //analyze type only if editable
                            editDataType = getColumnDataType(colPropClass);
                            switch (editDataType)
                            {
                                case DATATYPE_INTEGER:
                                {
                                    Integer maxval = null;
                                    Integer minval = null;
                                    try
                                    {
                                        maxval = (Integer) colPropClass.getMaxValue();
                                    }
                                    catch (Exception e)
                                    {
                                    }
                                    try
                                    {
                                        minval = (Integer) colPropClass.getMinValue();
                                    }
                                    catch (Exception e)
                                    {
                                    }
                                    jsonWriter.append(",edit_maxvalue:");
                                    jsonWriter.append(maxval == null ? "null" : maxval.toString());
                                    jsonWriter.append(",edit_minvalue:");
                                    jsonWriter.append(minval == null ? "null" : minval.toString());
                                }
                                    break;
                                case DATATYPE_DOUBLE:
                                {
                                    Double maxval = null;
                                    Double minval = null;
                                    try
                                    {
                                        maxval = (Double) colPropClass.getMaxValue();
                                    }
                                    catch (Exception e)
                                    {
                                    }
                                    try
                                    {
                                        minval = (Double) colPropClass.getMinValue();
                                    }
                                    catch (Exception e)
                                    {
                                    }
                                    jsonWriter.append(",edit_maxvalue:");
                                    jsonWriter.append(maxval == null ? "null" : maxval.toString());
                                    jsonWriter.append(",edit_minvalue:");
                                    jsonWriter.append(minval == null ? "null" : minval.toString());
                                }
                                    break;
                                case DATATYPE_ENUM:
                                    String getEnumInfoURL = getAjaxEventURL("getEnumInfo", null);
                                    jsonWriter.append(",enum_info_url:");
                                    jsonWriter.append(encodeJsonString(getEnumInfoURL));
                                    break;
                                default: //default values should be already set
                            }

                        }
                    }
                    catch (Exception e)
                    {
                    }
                }
                // remember report edit data type
                ace.setEditDataType(editDataType);
                // test if this column can be filtered
                boolean canFilter = false;
                boolean filterActive = false;
                try
                {
                    filterActive = getFilter().getFilterEntry(propName).isActive();
                    canFilter = true;
                }
                catch (Exception e)
                {
                    // ignore.
                    // We want to test if we can filter. If this test fails, we van not filter.
                }
                // build response
                jsonWriter.append(",can_edit:");
                jsonWriter.append(Boolean.toString(canEdit));
                jsonWriter.append(",can_filter:");
                jsonWriter.append(Boolean.toString(canFilter));//canFilter ? "true" : "false");
                jsonWriter.append(",filter_active:");
                jsonWriter.append(Boolean.toString(filterActive));//filterActive ? "true" : "false");
                jsonWriter.append(",edit_datatype:");
                jsonWriter.append(Integer.toString(editDataType));
                jsonWriter.append(",edit_required:");
                jsonWriter.append(Boolean.toString(editRequired));//editRequired ? "true" : "false");

                String tooltipMessage = "";
                if (ace.getFieldColumnInfo() != null && ace.getFieldColumnInfo().getDisplayName(getContext().getLocale()) != null)
                {
                    tooltipMessage = getContext().localize1("app.dmsdialogs.OwObjectListViewFilterRow.filtersettingstootlip", "Filter settings for %1", ace.getFieldColumnInfo().getDisplayName(getContext().getLocale()));
                }
                jsonWriter.append(",filter_tooltip:");
                jsonWriter.append(encodeJsonString(tooltipMessage));
                jsonWriter.append("}");
            }
            w.write(jsonWriter.toString());
        }

        // finish response
        w.write("],count:");
        w.write(Integer.toString(m_AjaxColumns.size()));
        w.write("})");
        // send response
        //        response_p.getWriter().write(jsonWriter.toString());
    }

    /** called upon AJAX request "setColumnInfo"
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxsetColumnInfo(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // get params
        String sParamColumnInfo = request_p.getParameter("columninfo");

        // sanity check
        if (sParamColumnInfo == null || getColumnInfo() == null)
        {
            return;
        }

        // split into single column entries
        String[] cols = sParamColumnInfo.split(",");

        // iterate over all cols
        m_AjaxColumns.clear();

        String attributeBagName = getAttributeBagName();
        String persistedeAttribute = (String) getColumnsAttributeBag().getSafeAttribute(attributeBagName, "");
        OwColumnInfoBagValue columnInfoBagValue = new OwColumnInfoBagValue(persistedeAttribute);
        Set<String> headers = new HashSet<String>();
        for (int i = 0; i < cols.length; i++)
        {
            String[] col_params = cols[i].split("=");
            if (col_params.length != 2)
            {
                continue;
            }

            // find in column map
            OwAjaxColumnEntry ace = (OwAjaxColumnEntry) m_AjaxColnameToPropertyMap.get(col_params[0]);
            if (ace == null)
            {
                continue;
            }
            int colwidth = DEFAULT_COLUMN_WIDTH;
            try
            {
                colwidth = Integer.parseInt(col_params[1]);
            }
            catch (NumberFormatException e)
            {
                colwidth = DEFAULT_COLUMN_WIDTH;
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("The parameter \"columninfo\" contains an invalid value for the colwidth, value=" + col_params[1] + ". Expected was a numeric value. Set the colwidth to default value=" + colwidth, e);
                }
            }
            if (colwidth > MAX_COLUMN_WIDTH)
            {
                colwidth = MAX_COLUMN_WIDTH;
            }
            if (colwidth < MIN_COLUMN_WIDTH)
            {
                colwidth = MIN_COLUMN_WIDTH;
            }
            ace.setColumnWidth(colwidth);
            m_AjaxColumns.add(ace);

            OwFieldColumnInfo aceColumnInfo = ace.getFieldColumnInfo();
            String propertyName = aceColumnInfo.getPropertyName();

            if (!headers.contains(""))
            {
                columnInfoBagValue.clearDefaultHeader();
                headers.add("");
            }
            columnInfoBagValue.put(propertyName, colwidth);

        }

        // write current column order to attribute bag
        OwAttributeBagWriteable ab = getColumnsAttributeBag();
        String attValue = columnInfoBagValue.toString();
        ab.setAttribute(attributeBagName, attValue);
        ab.save();
        m_columnsModifiedByUser = true;
        if (LOG.isDebugEnabled())
        {
            if (isExtjsColumnPersistent())
            {
                LOG.debug("Set persistent ExtJS column info: length=" + attValue.length() + ", bagName='" + attributeBagName + "', value='" + attValue + "'");
            }
            else
            {
                LOG.debug("Set non-persistent ExtJS column info: length=" + attValue.length() + ", bagName='" + attributeBagName + "', value='" + attValue + "'");
            }
        }
    }

    /**
     * Builds the attributeBag attribute name.
     * @return the attributeBag attribute name.
     * @since 3.2.0.1
     */
    private String getAttributeBagName()
    {
        OwFieldColumnInfo columnInfo = getColumnInfo().iterator().next();
        String attBag = COLUMNS_ID_ATTR_BAG;
        String headerId = null;
        if (columnInfo instanceof OwHeaderFieldColumnInfo)
        {
            headerId = ((OwHeaderFieldColumnInfo) columnInfo).getHeaderID();
            attBag = attBag + "_" + headerId;
        }
        return attBag;
    }

    /**
     * Reads the bootstrap configuration node &lt;StoreExtJSColumnInfo&gt;.
     * @return the value of bootstrap configuration node &lt;StoreExtJSColumnInfo&gt;.
     * @since 3.2.0.2
     */
    private boolean isExtjsColumnPersistent()
    {
        OwMainAppContext mainAppContext = (OwMainAppContext) getContext();
        OwConfiguration configuration = mainAppContext.getConfiguration();
        OwXMLUtil bootstrap = configuration.getBootstrapConfiguration();
        return bootstrap.getSafeBooleanValue(STORE_EXTJS_COLUMN_INFO_ELEMENT_NAME, false);
    }

    private synchronized OwAttributeBagWriteable getColumnsAttributeBag() throws Exception
    {
        if (!isExtjsColumnPersistent())
        {
            OwTransientBagRepository transientBagRepository = OwTransientBagRepository.getInstance();

            OwBaseUserInfo userInfo = ((OwMainAppContext) getContext()).getUserInfo();
            String userId = userInfo.getUserID();
            OwSimpleAttributeBagWriteable nonPersistentAttributebag = transientBagRepository.getBag(userId, NONPERSISTENT_COLUMN_ATTRIBUTE_BAG);

            return nonPersistentAttributebag;
        }
        else
        {
            return getDocument().getPersistentAttributeBagWriteable();
        }
    }

    private void buildAjaxColumns()
    {
        // sanity check
        if (getColumnInfo() == null)
        {
            return;
        }

        // get current column order from attribute bag
        String sPersistence = "";
        OwColumnInfoBagValue columnInfoBag = new OwColumnInfoBagValue();
        try
        {
            String attributeBagName = getAttributeBagName();
            sPersistence = (String) getColumnsAttributeBag().getSafeAttribute(attributeBagName, "");
            columnInfoBag = new OwColumnInfoBagValue(sPersistence);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("buildilng Ajax Columns attributeBag = " + attributeBagName + " info = " + sPersistence);
            }
        }
        catch (Exception e1)
        {
            LOG.error("OwObjectListViewEXTJSGrid exception reading attributebag column settings", e1);
        }

        // copy column info to temporary ArrayList
        LinkedList<OwFieldColumnInfo> columnInfoCopy = new LinkedList<OwFieldColumnInfo>(getColumnInfo());

        // clear old AJAX columns list
        m_AjaxColumns.clear();

        boolean persistedIconsWidth = false;

        persistedIconsWidth = columnInfoBag.widthOf("ow_icon_column") != null;

        //add icon column if no information about it in attribute bag
        if (persistedIconsWidth)
        {
            // pseudo field column info for icon column field
            columnInfoCopy.add(ICON_FIELD_COLUMN_INFO); //add icon column to column info list   
        }
        else
        {
            //set column width = -1 to assign that correct width must be set in onAjaxgetColumnInfo method
            m_AjaxColumns.add(new OwAjaxColumnEntry(ICON_FIELD_COLUMN_INFO, -1, "col" + Integer.toString(0)));
        }

        // iterate over all cols from attributebag, try to find them in the ColInfoList and
        // add them to the AjaxColumns.
        // remove found cols from the temporary ArrayList so we can add any remaining cols
        // to the AjaxCols

        Map<Integer, OwAjaxColumnEntry> map = new TreeMap<Integer, OwAjaxColumnEntry>();
        // Iterate Through current columns and check if column-info/setting is available in attribute Bag
        for (int i = 0; i < columnInfoCopy.size(); i++)
        {
            OwFieldColumnInfo columnInfo = columnInfoCopy.get(i);
            String columnXML = columnInfo.getPropertyName();
            Integer index = columnInfoBag.indexOf(columnXML);
            if (index != null)
            {
                Integer width = columnInfoBag.widthOf(columnXML);
                if (width != null)
                {
                    OwAjaxColumnEntry ajaxColumnEntry = new OwAjaxColumnEntry(columnInfo, width, columnXML);
                    // Put found column with corresponding index to map
                    map.put(index, ajaxColumnEntry);
                }
            }
        }

        // Iterate through map and reorder map by old index
        for (Map.Entry<Integer, OwAjaxColumnEntry> entry : map.entrySet())
        {
            String column = entry.getValue().getColumnName();
            Integer colwidth = entry.getValue().getColumnWidth();

            // column not found. column has been removed from ColumnInfo
            if (colwidth == null)
            {
                continue;
            }

            // set width for that column
            if (column != null && isNoteProperty(column))
            {
                colwidth = NOTES_DEFAULT_WIDTH;
            }

            if (colwidth < MIN_COLUMN_WIDTH)
            {
                colwidth = MIN_COLUMN_WIDTH;
            }
            if (colwidth > MAX_COLUMN_WIDTH)
            {
                colwidth = MAX_COLUMN_WIDTH;
            }

            // add column to AjaxColumns
            m_AjaxColumns.add(new OwAjaxColumnEntry(entry.getValue().getFieldColumnInfo(), colwidth, "col" + m_AjaxColumns.size()));

            // to be removed from temporary ArrayList
            columnInfoCopy.remove(entry.getValue().getFieldColumnInfo());
        }

        // until now, all existing columns from the AttributeBag have been added to the AjaxColumns.
        // If the Admin defined new columns, they can not be part of the AttributeBag. So we add all
        // columns from the ColumnInfoList which have not been mentioned in the attribute bag to the
        // end of the AjaxColumns with the default width of 100px.
        // the notes columns have a size of 230px.
        for (int ii = 0; ii < columnInfoCopy.size(); ii++)
        {
            OwFieldColumnInfo ci = columnInfoCopy.get(ii);
            int width = DEFAULT_COLUMN_WIDTH;
            if (ci.getPropertyName() != null && isNoteProperty(ci.getPropertyName()))
            {
                width = NOTES_DEFAULT_WIDTH;
            }
            m_AjaxColumns.add(new OwAjaxColumnEntry(ci, width, "col" + m_AjaxColumns.size()));
        }
    }

    /**
     * 
     * @param docFunction_p
     * @return <code>true</code> if this AJAX grid list view should display the given document function
     * @since 2.5.2.0
     */
    protected boolean showsContextMenuPlugin(OwDocumentFunction docFunction_p)
    {
        boolean isParentEnabled = !docFunction_p.getNeedParent() || getParentObject() != null;
        return isParentEnabled && isObjectTypeSupportedByPlugin(docFunction_p, this.occuringObjectTypes);
    }

    /** called upon AJAX request "getColumnInfo"
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxgetContextmenu(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // field count
        int iItemCount = 0;

        // get writer
        PrintWriter jsonWriter = response_p.getWriter();

        // start response
        jsonWriter.write("(");
        jsonWriter.write("{");
        jsonWriter.write("menu_items:[");

        // iterate through all plugins
        //        for (Iterator iterator = getContextMenuPluginList().iterator(); iterator.hasNext();)
        for (Iterator iterator = getContextMenuFunction().iterator(); iterator.hasNext();)
        {
            OwPluginEntry p_entry = (OwPluginEntry) iterator.next();
            OwDocumentFunction docFunction = p_entry.getPlugin();

            if (showsContextMenuPlugin(docFunction))
            {
                // get displayName and iconUrl
                String sPluginDisplayName = docFunction.getDefaultLabel();
                String sPluginIconUrl = docFunction.getIcon();
                // assemble column description
                if (iItemCount > 0)
                {
                    jsonWriter.write(",");
                }
                jsonWriter.write("{id:");
                jsonWriter.write(Integer.toString(p_entry.getIndex()));
                jsonWriter.write(",display_name:");
                jsonWriter.write(encodeJsonString(sPluginDisplayName));
                jsonWriter.write(",icon_url:");
                jsonWriter.write(encodeJsonString(sPluginIconUrl));
                jsonWriter.write("}");
                iItemCount++;
            }
        }

        // finish response
        jsonWriter.write("],count:");
        jsonWriter.write(Integer.toString(iItemCount));
        jsonWriter.write("}");
        jsonWriter.write(")");
    }

    /** called upon AJAX request "getColumnInfo"
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxsetCellData(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // get params
        String sParamRow = request_p.getParameter(AJAX_PARAM_ROW);
        String sParamCol = request_p.getParameter(AJAX_PARAM_COLUMN);
        String sParamValue = request_p.getParameter("value");

        // sanity check
        if ((sParamRow == null) || (sParamCol == null) || (sParamValue == null))
        {
            response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = OwString.localize(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.missingParams", "Parameters missing.");
            response_p.getWriter().write(errMsg);
            return;
        }

        // get property name
        OwAjaxColumnEntry ace = (OwAjaxColumnEntry) m_AjaxColnameToPropertyMap.get(sParamCol);
        //predefined structure check
        if ((ace == null) || (ace.getFieldColumnInfo() == null))
        {
            response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = OwString.localize(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.invalidColumn", "Invalid column.");
            response_p.getWriter().write(errMsg);
            return;
        }
        String sPropertyName = ace.getFieldColumnInfo().getPropertyName();
        if (sPropertyName == null)
        {
            response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = OwString.localize(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.noColumnProp", "Column has no property name.");
            response_p.getWriter().write(errMsg);
            return;
        }

        // get object and property
        OwProperty prop = null;
        OwObject obj = null;
        OwFieldDefinition fieldDef = null;
        try
        {
            obj = getObjectByIndex(Integer.parseInt(sParamRow));
            prop = obj.getProperty(sPropertyName);
            if ((prop == null))
            {
                String msg = OwString.localize(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.nullProp", "OwObjectListViewEXTJSGrid.onAjaxsetCellData: Cannot resolve the property, is null.");
                LOG.error(msg);
                response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response_p.getWriter().write(msg);
                return;
            }
            fieldDef = prop.getFieldDefinition();
            if (fieldDef == null)
            {
                String msg = OwString.localize(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.nullFieldDef", "OwObjectListViewEXTJSGrid.onAjaxsetCellData: Cannot resolve the fieldDef, is null.");
                LOG.error(msg);
                response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response_p.getWriter().write(msg);
                return;
            }
        }
        catch (Exception e)
        {
            String msg = OwString.localize(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.resolveError", "Error resolving object and property.");
            LOG.error(msg, e);
            response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response_p.getWriter().write(msg + e);
            return;
        }

        // create data object
        Object dataObject = null;
        if (sParamValue.length() > 0)
        {
            String strJavaClassName = fieldDef.getJavaClassName();
            if (strJavaClassName.equalsIgnoreCase("java.util.Date"))
            {
                try
                {
                    // calendar object to construct the date
                    java.util.Calendar calendar = new java.util.GregorianCalendar();
                    // parse string representation to a Date object
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    sdf.setLenient(false);
                    calendar.setTime(sdf.parse(sParamValue));
                    dataObject = calendar.getTime();
                }
                catch (Exception e)
                {
                    response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    String errMsg = OwString.localize1(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.conversionErrorDate", "Error converting new value into date format: %1", e.toString());
                    response_p.getWriter().write(errMsg);
                    return;
                }
            }
            else if (strJavaClassName.equalsIgnoreCase("java.lang.Boolean"))
            {
                try
                {
                    dataObject = Boolean.valueOf(sParamValue);
                }
                catch (Exception e)
                {
                    response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    String errMsg = OwString.localize1(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.conversionErrorBool", "Error converting new value into boolean format: %1", e.toString());
                    response_p.getWriter().write(errMsg);
                    return;
                }
            }
            else
            {
                try
                {
                    Class newClass = Class.forName(strJavaClassName);
                    java.lang.reflect.Constructor constr = newClass.getConstructor(new Class[] { java.lang.String.class });
                    dataObject = constr.newInstance(new Object[] { sParamValue });
                }
                catch (Throwable t)
                {
                    response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    String errMsg = OwString.localize2(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.conversionError", "Error converting new value into %1: %2", strJavaClassName, t.toString());
                    response_p.getWriter().write(errMsg);
                    return;
                }
            }
        }

        // check
        if ((dataObject == null) && prop.getPropertyClass().isRequired())
        {
            response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = OwString.localize(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.nullDataObject", "Data object is null for an required property.");
            response_p.getWriter().write(errMsg);
            return;
        }

        // assign new value
        try
        {
            String propClassName = prop.getPropertyClass().getClassName();
            if (propClassName != null && isNoteProperty(propClassName))
            {
                String content = (String) dataObject;
                if (content.length() > 0)
                {
                    OwNoteDataModel noteDataModel = new OwNoteDataModel(getContext().getLocale(), (String) prop.getValue());
                    if (fieldDef.getMaxValue() != null)
                    {
                        noteDataModel.setMaxSize(((Integer) fieldDef.getMaxValue()).intValue());
                    }
                    String currentUser = ((OwMainAppContext) getContext()).getNetwork().getCredentials().getUserInfo().getUserLongName();
                    DateFormat dateFormat = new java.text.SimpleDateFormat(((OwMainAppContext) getContext()).getDateFormatString());
                    String timestamp = dateFormat.format(new Date());
                    OwNote note = new OwNote(getContext().getLocale(), currentUser, timestamp, content);
                    noteDataModel.appendNote(note);
                    dataObject = noteDataModel.getTrimmedText();
                }

            }
            prop.setValue(dataObject);

            OwPropertyCollection changedProperties = new OwStandardPropertyCollection();
            changedProperties.put(prop.getFieldDefinition().getClassName(), prop);
            obj.setProperties(changedProperties);
            //write formated note
            if (propClassName != null && isNoteProperty(propClassName))
            {
                getFieldManager().insertReadOnlyField(response_p.getWriter(), prop);
            }
        }
        catch (Exception e)
        {
            response_p.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = OwString.localize1(getContext().getLocale(), "app.OwObjectListViewEXTJSGrid.err.assignError", "Error assigning new value: %1", e.toString());
            response_p.getWriter().write(errMsg);
            return;
        }

        // report success
        response_p.setStatus(HttpServletResponse.SC_OK);
    }

    /*****************************************************************
    ** Events                                                       ** 
    ******************************************************************/

    /** event called when user clicked on a plugin link of an object entry in the list 
     *   @param request_p  HttpServletRequest
     */
    public void onPluginEvent(HttpServletRequest request_p) throws Exception
    {
        // === handle plugin event
        // parse query string
        String strObjectIndices = request_p.getParameter(OBJECT_INDEX_KEY);
        String strPlugIndex = request_p.getParameter(PLUG_INDEX_KEY);
        if ((strObjectIndices != null) && (strPlugIndex != null))
        {
            // check for multiple objects connected by |
            String[] sObjects = strObjectIndices.split(",");
            List objects = new ArrayList();
            for (int i = 0; i < sObjects.length; i++)
            {
                try
                {
                    objects.add(getObjectByIndex(Integer.parseInt(sObjects[i])));
                }
                catch (Exception e)
                {
                }
            }

            // get plugin
            int iPlugIndex = Integer.parseInt(strPlugIndex);
            OwDocumentFunction plugIn = getDocumentFunction(iPlugIndex);

            if (objects.size() == 1)
            {
                plugIn.onClickEvent((OwObject) objects.get(0), getParentObject(), getRefreshContext());
            }
            else
            {
                if (!plugIn.getMultiselect())
                {
                    throw new OwUserOperationException(new OwString1("app.OwObjectListViewEXTJSGrid.onlysingleselectfunction", "Function %1 can only be applied to one object.", plugIn.getDefaultLabel()));
                }

                OwMultipleSelectionCall multiCall = new OwMultipleSelectionCall(plugIn, objects, getParentObject(), getRefreshContext(), (OwMainAppContext) getContext());
                multiCall.invokeFunction();
            }
        }
    }

    /*****************************************************************
    ** supportive methods                                           ** 
    ******************************************************************/

    /** replace all JavaScript Special characters with their escape sequence
     * @param in_p String input to be encoded
     * 
     * @return String encoded string
     * */
    protected String encodeJsonString(String in_p)
    {
        // convert null to an empty string
        if (null == in_p)
        {
            return ("\"\"");
        }
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < in_p.length(); i++)
        {
            char c = in_p.charAt(i);
            switch (c)
            {
                case '\\':
                    sb.append('\\').append('\\');
                    break;
                case '\b':
                    sb.append('\\').append('b');
                    break;
                case '\f':
                    sb.append('\\').append('f');
                    break;
                case '\n':
                    sb.append('\\').append('n');
                    break;
                case '\r':
                    sb.append('\\').append('r');
                    break;
                case '\t':
                    sb.append('\\').append('t');
                    break;
                case '"':
                    sb.append('\\').append('"');
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append('"');
        return (sb.toString());
    }

    private static int minPositiveIndex(int[] indexes_p)
    {
        int min = -1;

        for (int i = 0; i < indexes_p.length; i++)
        {
            if (indexes_p[i] > -1)
            {
                if (min == -1)
                {
                    min = indexes_p[i];
                }
                else if (min > indexes_p[i])
                {
                    min = indexes_p[i];
                }

            }
        }

        return min;
    }

    /**
     * Recursive tooltip block processing method.<br>
     * A tooltip block is a chunk of extended HTML code enclosed by square 
     * brackets - '[' and ']' that is defined by the &lt;Tooltip&gt; element in the 
     * plugins configuration.  
     * The brackets enclosed chunk will be processed (ie. simple HTML code encoded , property 
     * references replaced with their values than added to given the block buffer) if and 
     * only if it makes reference to valid properties or no properties at all.
     *   
     * @param context_p current Alfresco Workdesk context
     * @param blockBuffer_p outpt buffer that will contain the processed block or an empty string
     *                      if the block could not be processed
     * @param strIn_p the tooltip extended HTML code
     * @param index_p index in strIn_p at which the block processing will commence 
     * @param obj_p the referred object 
     * @return the index in the tooltip code (strIn_p) that the processing of the current block stopped at.
     * @throws Exception
     * @since 3.1.0.0
     */
    private int replacePropertyBlock(OwMainAppContext context_p, StringBuilder blockBuffer_p, String strIn_p, int index_p, OwObjectReference obj_p) throws Exception
    {
        int iOldIndex = index_p;
        int propIndex = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START, iOldIndex);
        int startTokenIndex = strIn_p.indexOf(TOOLTIP_BLOCK_TOKEN_PROPERTY_START, iOldIndex);
        int endTokenIndex = strIn_p.indexOf(TOOLTIP_BLOCK_TOKEN_PROPERTY_END, iOldIndex);
        int iIndex = minPositiveIndex(new int[] { propIndex, startTokenIndex, endTokenIndex });

        if (-1 == iIndex)
        {
            String block = strIn_p.substring(iOldIndex);
            blockBuffer_p.append(OwHTMLHelper.encodeToSecureHTML(block));
            return strIn_p.length();
        }

        while (-1 != iIndex)
        {
            String blockPart = strIn_p.substring(iOldIndex, iIndex);
            blockBuffer_p.append(OwHTMLHelper.encodeToSecureHTML(blockPart));

            // skip encoding character
            if (propIndex == iIndex)
            {
                // now we get the object instance
                OwObject obj = obj_p.getInstance();

                iIndex++;
                iIndex += VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START.length();

                // get the property name
                int iEnd = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_END, iIndex);
                String strPropName = strIn_p.substring(iIndex, iEnd);

                String propertyString = null;
                try
                {
                    OwProperty property = obj.getProperty(strPropName);
                    StringWriter stringWriter = new StringWriter();
                    getFieldManager().insertReadOnlyField(stringWriter, property);
                    propertyString = OwHTMLHelper.encodeToSecureHTML(stringWriter.toString());
                }
                catch (Exception e)
                {
                    blockBuffer_p.replace(0, blockBuffer_p.length(), "");
                    while (iIndex < strIn_p.length() && strIn_p.charAt(iIndex) != TOOLTIP_BLOCK_TOKEN_PROPERTY_END_CHAR)
                    {
                        iIndex++;
                    }
                    return iIndex;
                }

                if (propertyString != null)
                {

                    char encodeTokenChar = strIn_p.charAt(iIndex - 1);
                    if (encodeTokenChar == VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_ENCODE_CHAR)
                    {
                        blockBuffer_p.append(OwHTMLHelper.encodeToSecureHTML(propertyString));
                    }
                    else if (encodeTokenChar == VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_JSENCODE_CHAR)
                    {
                        blockBuffer_p.append(OwHTMLHelper.encodeJavascriptString(propertyString));
                    }
                    else
                    {
                        blockBuffer_p.append(propertyString);
                    }
                }
                iIndex = iEnd + 1;

                iOldIndex = iIndex;
            }
            else if (startTokenIndex == iIndex)
            {
                iIndex++;
                StringBuilder subBlock = new StringBuilder();
                iIndex = replacePropertyBlock(context_p, subBlock, strIn_p, iIndex, obj_p);
                blockBuffer_p.append(subBlock);
                iOldIndex = iIndex;
            }
            else if (endTokenIndex == iIndex)
            {

                if (iOldIndex <= iIndex)
                {
                    String nonTokenRest = strIn_p.substring(iOldIndex, iIndex);
                    blockBuffer_p.append(OwHTMLHelper.encodeToSecureHTML(nonTokenRest));
                }
                iIndex++;
                return iIndex;
            }

            propIndex = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START, iIndex);
            startTokenIndex = strIn_p.indexOf(TOOLTIP_BLOCK_TOKEN_PROPERTY_START, iIndex);
            endTokenIndex = strIn_p.indexOf(TOOLTIP_BLOCK_TOKEN_PROPERTY_END, iIndex);
            iIndex = minPositiveIndex(new int[] { propIndex, startTokenIndex, endTokenIndex });
        }

        if (iOldIndex <= strIn_p.length())
        {
            String nonTokenRest = strIn_p.substring(iOldIndex, strIn_p.length());
            blockBuffer_p.append(OwHTMLHelper.encodeToSecureHTML(nonTokenRest));
        }
        return strIn_p.length();
    }

    /**
     * Localize Id strings from Tooltip.
     * 
     * @param context_p Application context
     * @param strIn_p  String to search and replace
     * 
     * @return String
     * @throws Exception
     * @since 3.2.0.0
     * */
    private String localizeStrings(OwMainAppContext context_p, String strIn_p) throws Exception
    {
        StringBuilder stringOut = new StringBuilder();

        // split entire string in blocks
        String[] result = strIn_p.split("\\{localizedString");
        String prefix = result[0].substring(0, result[0].length());

        stringOut.append(prefix);

        for (int x = 1; x < result.length; x++)
        {
            int sufixBegin = result[x].indexOf("}") + 1;
            String temp = result[x].substring(1, sufixBegin).trim();
            String sufix = result[x].substring(sufixBegin, result[x].length());
            int endId = temp.indexOf(",");
            String strKey = temp.substring(0, endId);
            String dfValue = temp.substring(endId + 1, temp.length() - 1);

            String localizedString = OwString.localize(context_p.getLocale(), strKey, dfValue);
            // update with localized string
            stringOut.append(localizedString);
            stringOut.append(sufix);
        }

        if (LOG.isDebugEnabled())
        {
            LOG.debug("localizedString:" + stringOut);
        }

        return stringOut.toString();
    }

    /** 
     * Processes tooltip blocks and replaces property placeholders.
     * @param strIn_p String to search and replace
     * @param obj_p OwObjectReference
     * 
     * @return String
     * @throws Exception 
     * */
    private String replaceProperties(OwMainAppContext context_p, String strIn_p, OwObjectReference obj_p) throws Exception
    {
        StringBuilder stringOut = new StringBuilder();
        replacePropertyBlock(context_p, stringOut, strIn_p, 0, obj_p);
        return stringOut.toString();
    }

    /**
     * Get the date format for current context/instance.
     * Will specially convert the Java-Date-Format to ExtJs specific representation.
     * @return String for date representation
     * @since 4.2.0.0
     * @see #convertDateFormat(String)
     */
    public String getDateFormat()
    {
        return convertDateFormat(((OwMainAppContext) getContext()).getDateFormatString());
    }

    /**
     * Character which represents the decimal separator.
     * @return char
     * @since 4.2.0.0
     */
    public char getDecimalSeparator()
    {
        return this.decimalSeparator;
    }

    /** Java and ExtJS use different date format markup strings. This method
     * converts the Java date format into the date format used by ExtJS (PHP).
     * ExtJS uses the PHP date format. See source/util/Date.js.
     * @param javaSimpleDateFormat_p String to search and replace
     * 
     * @return String
     */
    public static String convertDateFormat(String javaSimpleDateFormat_p)
    {
        // StringBuilder for the PHP date format
        StringBuilder sPhpDateFormat = new StringBuilder();
        // int current tested position in Java date format
        int iPos = 0;
        // walk through java date format
        while (iPos < javaSimpleDateFormat_p.length())
        {
            // get the current pattern
            char cCurrentPattern = javaSimpleDateFormat_p.charAt(iPos);
            // get the starting position of the next pattern
            int iNextPatternStartPos = iPos;
            while ((iNextPatternStartPos < javaSimpleDateFormat_p.length()) && (javaSimpleDateFormat_p.charAt(iNextPatternStartPos) == cCurrentPattern))
            {
                iNextPatternStartPos++;
            }
            // calculate pattern length
            int iPatternLength = iNextPatternStartPos - iPos;
            // translate current pattern to a PHP pattern, depending on its pattern length
            switch (cCurrentPattern)
            {
                case 'G': // Era designator
                    // era designator is not available in PHP date formats. just append the string "AD"
                    // this should be fine in most situations (year >= 0)
                    sPhpDateFormat.append("\\A\\D");
                    break;
                case 'y': // year
                    if (iPatternLength <= 2)
                    {
                        sPhpDateFormat.append('y');
                    }
                    else
                    {
                        sPhpDateFormat.append('Y');
                    }
                    break;
                case 'M': // month in year
                    if (iPatternLength <= 1)
                    {
                        sPhpDateFormat.append('n');
                    }
                    else
                    {
                        sPhpDateFormat.append('m');
                    }
                    break;
                case 'w': // week in year
                    // sorry, no one-digit week-of-year in PHP
                    sPhpDateFormat.append('W');
                    break;
                case 'W': // week in month
                    // not available in PHP. ignore
                    break;
                case 'D': // day in year
                    sPhpDateFormat.append('z');
                    break;
                case 'd': // day in month
                    if (iPatternLength <= 1)
                    {
                        sPhpDateFormat.append('j');
                    }
                    else
                    {
                        sPhpDateFormat.append('d');
                    }
                    break;
                case 'F': // day of week in month ???????
                    sPhpDateFormat.append('w');
                    break;
                case 'E': // day in week as text
                    if (iPatternLength <= 3)
                    {
                        sPhpDateFormat.append('D');
                    }
                    else
                    {
                        sPhpDateFormat.append('l');
                    }
                    break;
                case 'a': // am/pm marker
                    sPhpDateFormat.append('A');
                    break;
                case 'H': // hour in day 0-23
                    if (iPatternLength <= 1)
                    {
                        sPhpDateFormat.append('G');
                    }
                    else
                    {
                        sPhpDateFormat.append('H');
                    }
                    break;
                case 'k': // hour in day (1-24)
                    // not available in PHP. ignore
                    break;
                case 'K': // hour in day (0-11)
                    if (iPatternLength <= 1)
                    {
                        sPhpDateFormat.append('g');
                    }
                    else
                    {
                        sPhpDateFormat.append('h');
                    }
                    break;
                case 'h': // hour in day (1-24)
                    // not available in PHP. ignore
                    break;
                case 'm': // minute in hour
                    // sorry, only available with leading zeros
                    sPhpDateFormat.append('i');
                    break;
                case 's': // seconds in minute
                    // sorry, only available with leading zeros
                    sPhpDateFormat.append('s');
                    break;
                case 'S': // milliseconds in second
                    // not available in PHP. ignore
                    break;
                case 'z': // time zone
                    sPhpDateFormat.append('T');
                    break;
                case 'Z': // RFC 822 time zone
                    sPhpDateFormat.append('O');
                    break;
                case '\'': // start of string or '
                    if (iPatternLength == 2)
                    {
                        // exact to single quotes (''). Means '
                        sPhpDateFormat.append('\'');
                    }
                    else
                    {
                        // is a string. find next single quote indicating end of string
                        // and copy all characters to the output.           
                        int iNextPatternAfterString = iPos + 1;
                        while (iNextPatternAfterString < javaSimpleDateFormat_p.length())
                        {
                            char cStringChar = javaSimpleDateFormat_p.charAt(iNextPatternAfterString);
                            if (cStringChar == '\'')
                            {
                                // we found a ' character in the string. if the next character is also
                                // a single quote, this is the escape sequence for single quotes in java
                                // strings (example 'o'' clock' means the string "o' clock".
                                if (((iNextPatternAfterString + 1) < javaSimpleDateFormat_p.length()) && (javaSimpleDateFormat_p.charAt(iNextPatternAfterString) == '\''))
                                {
                                    // copy the single quote to the output
                                    sPhpDateFormat.append('\'');
                                    // skip the second single quote
                                    iNextPatternAfterString++;
                                }
                                else
                                {
                                    // this is the end of the string. break while loop
                                    break;
                                }
                            }
                            else
                            {
                                // copy string character to the output
                                if (isPhpSpecialChar(cStringChar))
                                {
                                    sPhpDateFormat.append('\\');
                                    sPhpDateFormat.append(cStringChar);
                                }
                                else
                                {
                                    sPhpDateFormat.append(cStringChar);
                                }
                            }
                            iNextPatternAfterString++;
                        }
                        // we have just parsed a string. set the iNextPatternStartPos to
                        // the character following the string
                        iNextPatternStartPos = iNextPatternAfterString;
                    }
                    break;
                default: // not a markup char. just copy
                    if (isPhpSpecialChar(cCurrentPattern))
                    {
                        for (int i = 0; i < iPatternLength; i++)
                        {
                            sPhpDateFormat.append('\\');
                            sPhpDateFormat.append(cCurrentPattern);
                        }
                    }
                    else
                    {
                        for (int i = 0; i < iPatternLength; i++)
                        {
                            sPhpDateFormat.append(cCurrentPattern);
                        }
                    }
                    break;
            }
            // go to the next pattern
            iPos = iNextPatternStartPos;
        }
        // return the created PHP date format
        return (sPhpDateFormat.toString());
    }

    /** Check if the character has a meaning as a PHP date format pattern. If
     * so, it must be escaped in the PHP date format string.
     * @param testChar_p char character to test
     * 
     * @return boolean
     * */
    public static boolean isPhpSpecialChar(char testChar_p)
    {
        for (int i = 0; i < SPECIAL_CHARS.length; i++)
        {
            if (SPECIAL_CHARS[i] == testChar_p)
            {
                return true;
            }
        }
        return false;
    }

    /*****************************************************************
    ** Methods for OwObjectListView methods                         ** 
    ******************************************************************/

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return this.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.title", "Adaptable List");
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon URL, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return "/images/OwObjectListView/OwObjectListViewEXTJSGrid.png";
    }

    /** get a collection of property names that are needed to display the Objects in the list
     *  i.e. these properties should be requested in advance to save server roundtrips.
     *  @return Collection of String
     * */
    public Collection getRetrievalPropertyNames() throws Exception
    {
        // check if ColumnInfo is set
        if (null == getColumnInfo())
        {
            throw new OwInvalidOperationException("OwObjectListViewEXTJSGrid.getRetrievalPropertyNames: Specify setColumnInfo() in OwObjectList.");
        }
        // create result set
        Set retList = new HashSet();
        // add the column info properties
        Iterator it = getColumnInfo().iterator();
        while (it.hasNext())
        {
            OwFieldColumnInfo colInfo = (OwFieldColumnInfo) it.next();
            retList.add(colInfo.getPropertyName());
        }
        // add properties from the plugins
        if (hasViewMask(VIEW_MASK_INSTANCE_PLUGINS))
        {
            it = getObjectInstancePluginList().iterator();
            while (it.hasNext())
            {
                OwPluginEntry entry = (OwPluginEntry) it.next();
                Collection props = entry.m_Plugin.getRetrievalPropertyNames();
                if (null != props)
                {
                    retList.addAll(props);
                }
            }
        }
        // return result
        return retList;
    }

    /** overridable get the style class name for the row
    *
    * @param iIndex_p int row index
    * @param obj_p current OwObject
    *
    * @return String with style class name
    */
    protected String getRowClassName(int iIndex_p, OwObject obj_p)
    {
        try
        {
            // callback
            return getEventListner().onObjectListViewGetRowClassName(iIndex_p, obj_p).toString();
        }
        catch (Exception e)
        {
            // use default
            return ((iIndex_p % 2) != 0) ? "x-grid-row-alt" : "";
        }
    }

    /** set the list of column info to be used by this list view
     * @param columnInfo_p List of OwFieldColumnInfo's
     */
    public void setColumnInfo(Collection columnInfo_p)
    {
        super.setColumnInfo(columnInfo_p);
        // we have a new ColumnInfoiList. So we must rebuild our AjaxColumns.
        buildAjaxColumns();
    }

    /** set the list of objects to be displayed by this list view
     * @param objectList_p OwObjectCollection, can be null
     * @param parentObject_p OwObject parent which created the object list, can be null if no parent is specified
     */
    public void setObjectList(OwObjectCollection objectList_p, OwObject parentObject_p) throws Exception
    {
        super.setObjectList(objectList_p, parentObject_p);
        m_MimeManager.setParent(parentObject_p);
        recreateOccuredHashSet();
        // clear persistence
        resetPersistedSelectionState();
    }

    @Override
    public void setObjectIterable(OwIterable<OwObject> iterable, OwObject parentObject_p) throws Exception
    {
        super.setObjectIterable(iterable, parentObject_p);
        m_MimeManager.setParent(parentObject_p);
        recreateOccuredHashSet();
        // clear persistence
        resetPersistedSelectionState();
    }

    /**
     * Check if paging component should be shown. 
     * @return <code>true</code> if paging component should be displayed.
     * @since 3.0.0.0
     * @deprecated since 4.2.0.0 use {@link #hasPaging()} instead
     */
    @Deprecated
    public boolean getShowPaging() throws Exception
    {
        return getPageCount() > 1;
    }

    /** register an event listener with this view to receive notifications
     * @param eventlister_p OwClientRefreshContext interface
     */
    public void setRefreshContext(OwClientRefreshContext eventlister_p)
    {
        super.setRefreshContext(eventlister_p);
        m_MimeManager.setRefreshContext(eventlister_p);
    }

    /*****************************************************************
    ** Methods for OwFieldProvider interface                        ** 
    ******************************************************************/

    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        throw new OwObjectNotFoundException("OwObjectListViewEXTJSGrid.getField: Not implemented or Not supported.");
    }

    public String getFieldProviderName()
    {
        try
        {
            return getParentObject().getName();
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    public Object getFieldProviderSource()
    {
        return m_ObjectList;
    }

    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT | OwFieldProvider.TYPE_RESULT_LIST;
    }

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        throw new OwInvalidOperationException("OwObjectListViewEXTJSGrid.getFields: Not implemented.");
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            OwField field = getField(sName_p);
            return field.getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        OwField field = getField(sName_p);
        field.setValue(value_p);
    }

    /**
     * Returns a list of all supported plugins shown next to object instance.
     * If list is null the list will be created first.
     * @return m_ObjectInstancePluginList
     */
    public Collection getObjectInstancePluginList()
    {
        return super.getPluginEntries();
    }

    private void recreateOccuredHashSet()
    {
        if (occuringObjectTypes != null)
        {//Free Memory, and clean reference
            occuringObjectTypes.clear();
            occuringObjectTypes = null;
        }

        occuringObjectTypes = new HashSet();
    }

    /**
     * Process occurred object types, for 
     * quicker handling of document functions.
     * @param obj OwObject
     */
    protected void processOccurred(OwObject obj)
    {
        Integer type = Integer.valueOf(obj.getType());
        if (!occuringObjectTypes.contains(type))
        {
            occuringObjectTypes.add(type);
        }
    }

    /**
     * Check if the note can be edited.
     * @return <code>true</code> if the notes are editable
     * @since 2.5.2.0 
     */
    public boolean isNoteEditable()
    {
        return m_isNoteEditable;
    }

    /**
     * Set the editable note property.
     * @param isNoteEditable_p 
     * @since 2.5.2.0
     */
    public void setNoteEditable(boolean isNoteEditable_p)
    {
        m_isNoteEditable = isNoteEditable_p;
    }

    /**
     * Compute the end index, the index for the last displayed object, when paging is used.
     * @return - the end index
     * @since 3.0.0.0
     * @deprecated since 4.2.0.0 use {@link #getCurrentPage()} and {@link #getPageSize()}
     */
    @Deprecated
    protected int computeEndIndex()
    {
        int iEndIndex = 0;
        if (m_ObjectList != null)
        {
            int iPageSize = getPageSize();
            int iStartIndex = m_startrow;

            iEndIndex = iStartIndex + iPageSize;

            if (iEndIndex > m_ObjectList.size())
            {
                iEndIndex = m_ObjectList.size();
            }
        }
        return iEndIndex;
    }

    /**
     * Get the instance of the MIME manager used to open the objects
     * @returns {@link OwMimeManager}
     * @since 3.0.0.0
     */
    protected OwMimeManager getMimeManager()
    {
        return m_MimeManager;
    }

    /**
     * Returning an int representing the data type of the property.
     * Will return -1 if the property have an unknown/unsupported type.
     * <pre>
     *  -1 = unknown/unsupported ({@link #DATATYPE_UNKNOWN})
     *  0 = String ({@link #DATATYPE_STRING})
     *  1 = Integer ({@link #DATATYPE_INTEGER})
     *  2 = Boolean ({@link #DATATYPE_BOOLEAN})
     *  3 = Date ({@link #DATATYPE_DATE})
     *  4 = Double ({@link #DATATYPE_DOUBLE})
     *  5 = Enumeration (any kind of) ({@link #DATATYPE_ENUM})
     *  6 = Note property handling ({@link #DATATYPE_NOTE})
     * </pre>
     * @param propertyClass_p OwPropertyClass from which to retrieve the data type
     * @return int representing the data type, -1 for unknown/unsupported type
     * @throws Exception if cannot retrieve information from property class
     * @since 2.5.3.1
     */
    protected int getColumnDataType(OwPropertyClass propertyClass_p) throws Exception
    {
        int dataType = DATATYPE_UNKNOWN;

        if (m_informalConfiguration != null)
        {
            OwPropetyControl informalPropertyConfig = m_informalConfiguration.getPropertyControl(propertyClass_p.getClassName());

            if (informalPropertyConfig != null)
            {
                if ("enum".equals(informalPropertyConfig.controlType))
                {
                    return DATATYPE_ENUM;
                }
            }
        }

        if (propertyClass_p.isEnum())
        {
            dataType = DATATYPE_ENUM;
        }
        else if (propertyClass_p.getJavaClassName().equalsIgnoreCase("java.lang.String"))
        {
            dataType = DATATYPE_STRING;
            if (isNoteProperty(propertyClass_p.getClassName()))
            {
                dataType = DATATYPE_NOTE;
            }
        }
        else if (propertyClass_p.getJavaClassName().equalsIgnoreCase("java.lang.Integer"))
        {
            dataType = DATATYPE_INTEGER;
        }
        else if (propertyClass_p.getJavaClassName().equalsIgnoreCase("java.lang.Boolean"))
        {
            dataType = DATATYPE_BOOLEAN;
        }
        else if (propertyClass_p.getJavaClassName().equalsIgnoreCase("java.util.Date"))
        {
            dataType = DATATYPE_DATE;
        }
        else if (propertyClass_p.getJavaClassName().equalsIgnoreCase("java.lang.Double"))
        {
            dataType = DATATYPE_DOUBLE;
        }

        return dataType;
    }

    /**
     * Return flag, which indicates if the given property is defined as Note property.
     * @param propName_p String representing the property symbolic or unique name
     * @return boolean true if it is a note property, else false
     * @since 2.5.3.1
     */
    public boolean isNoteProperty(String propName_p)
    {
        return m_editableNotePropertyNames.contains(propName_p);
    }

    /**
     * Check if the given property is editable, for inline editing in the UI.
     * @param propertyClass_p OwPropertyClass to check for editing (non-null)
     * @return boolean true if editing is allowed
     * @throws Exception if can not request information from given property class
     * @since 2.5.3.1
     */
    protected boolean isPropertyEditable(OwPropertyClass propertyClass_p) throws Exception
    {
        boolean editable = false;
        if (hasViewMask(VIEW_MASK_INLINE_EDITING))
        {
            if ((!propertyClass_p.isSystemProperty()) && (!propertyClass_p.isArray()) && (!propertyClass_p.isReadOnly(OwPropertyClass.CONTEXT_NORMAL)))
            {
                editable = true;
                if (isNoteProperty(propertyClass_p.getClassName()))
                {
                    editable = isNoteEditable();
                }
                else
                {
                    editable = !isConfiguredAsReadOnly(propertyClass_p.getClassName());
                }
            }
        }
        return editable;
    }

    /**
     * Method returning boolean value, depending 
     * on the existence of a tool tip pattern.
     * @return true if pattern exist, else false
     * @since 2.5.3.1
     */
    public boolean hasToolTipPattern()
    {
        return getToolTipPattern() != null;
    }

    /**
     * Return the configured tool tip pattern, or null
     * if no pattern was defined.
     * @return String representing the pattern or null
     * @since 2.5.3.1
     */
    protected String getToolTipPattern()
    {
        return this.m_TooltipTextPattern;
    }

    /**
     * Transform a Date into the defined transport format.
     * @param dateVal_p Date (non-null)
     * @return String representing the transport date format
     * @since 2.5.3.1
     */
    protected String getDateInTransportFormat(Date dateVal_p)
    {
        return m_TransportDateFormat.format(dateVal_p);
    }

    /**
     * Encode the value of the given property depending on data type of the property.
     * <p>
     * <b>ATTENTION</b>: Neither the given property nor the property value
     * should be <code>null</code>, otherwise it will lead to a <code>NullpointerException</code>.
     * </p>
     * @param ace_p OwAjaxColumnEntry represents the column for which to render the column
     * @param prop_p OwProperty to be used for value retrieving
     * @return String representation of the property value
     * @throws Exception
     * @since 2.5.3.1
     */
    protected String getEncodedPropertyValue(OwAjaxColumnEntry ace_p, OwProperty prop_p) throws Exception
    {
        String propVal = prop_p.getValue().toString();
        // encode value of property to secure HTML
        try
        {
            propVal = OwHTMLHelper.encodeToSecureHTML(propVal);
        }
        catch (IOException exData)
        {
            LOG.warn("OwObjectListViewEXTJSGrid.onAjaxReadList: encoding of data failed! name= " + prop_p.getPropertyClass().getClassName() + " value = " + propVal, exData);
        }
        // handle the value dependent on the type
        switch (ace_p.getEditDataType())
        {
            case DATATYPE_STRING:
                propVal = encodeJsonString(propVal);
                break;
            case DATATYPE_INTEGER:
            case DATATYPE_DOUBLE:
                // double and integer are already encoded
                break;
            case DATATYPE_BOOLEAN:
                propVal = Boolean.valueOf(propVal).toString();
                break;
            case DATATYPE_DATE:
            {
                java.util.Date dateValue = (java.util.Date) prop_p.getValue();
                ;
                propVal = encodeJsonString(getDateInTransportFormat(dateValue));
            }
                break;
            case DATATYPE_ENUM:
            {
                OwPropertyClass propertyClass = prop_p.getPropertyClass();
                OwEnumCollection enums = propertyClass.getEnums();
                String displayText = propVal;
                if (enums != null)
                {
                    for (Iterator ie = enums.iterator(); ie.hasNext();)
                    {
                        OwEnum item = (OwEnum) ie.next();
                        if (item.getValue().equals(prop_p.getValue()))
                        {
                            displayText = item.getDisplayName(getContext().getLocale());
                        }
                    }
                }
                propVal = "[[" + encodeJsonString(propVal) + "," + encodeJsonString(displayText) + "]]";
            }
                break;
            case -1: // field can not be edited. Render field with fieldmanager and transport the result as string
            default: // unknown. handled like -1
                StringWriter sw = new StringWriter();
                try
                {
                    getFieldManager().insertReadOnlyField(sw, prop_p);
                }
                catch (Exception e)
                {
                }
                propVal = encodeJsonString(sw.toString());
                break;
        }
        return propVal;
    }

    /**
     * Returns the definitions of read only columns,
     * List of String's which represent the ID/symbolic name
     * of properties which should be read-only marked.
     * @return List of String's, or empty List
     * @since 3.1.0.0
     */
    protected List getReadOnlyColumns()
    {
        return m_readOnlyColumns;
    }

    /**
     * Check if the given property name was configured as read only.
     * @param property_p String ID/symbolic name of property
     * @return boolean true if contained in {@link #getReadOnlyColumns()}
     * @since 3.1.0.0
     * @see #getReadOnlyColumns()
     */
    protected boolean isConfiguredAsReadOnly(String property_p)
    {
        return getReadOnlyColumns().contains(property_p);
    }

    /**
     * Check if multiple row selection is enabled.
     * @return boolean true only if VIEW_MASK_MULTI_SELECTION is configured.
     * @since 4.2.0.0
     */
    public boolean isMultiSelectionEnabled()
    {
        return hasViewMask(OwObjectListView.VIEW_MASK_MULTI_SELECTION);
    }

    /*****************************************************************
     ** nested classes                                               ** 
     ******************************************************************/

    private static class OwPropetyControl
    {
        final String propertyName;
        final String controlType;

        OwPropetyControl(String propertyName_p, String controlType_p)
        {
            super();
            this.propertyName = propertyName_p;
            this.controlType = controlType_p;
        }

    }

    private static class OwInformalConfiguration
    {
        private Map<String, OwPropetyControl> propertyControls = new HashMap<String, OwPropetyControl>();

        public void add(OwPropetyControl propertyControl_p)
        {
            this.propertyControls.put(propertyControl_p.propertyName, propertyControl_p);
        }

        public OwPropetyControl getPropertyControl(String propertyName_p)
        {
            return this.propertyControls.get(propertyName_p);
        }

    }

    /**
     * Returns the localized loading message.
     * @return - the localized loading message
     * @since 3.1.0.3
     */
    public String getLoadingMessage()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.loadingMessage", "Please wait while your request is being processed...");
    }

    /**
     * Helper class for column handling.
     */
    protected static class OwAjaxColumnEntry
    {
        /** FieldColumnInfo of that AjaxColumn */
        private OwFieldColumnInfo m_ColumnInfo;
        /** name of that AjaxColumn */
        private String m_ColumnName;
        /** width of that AjaxColumn */
        private int m_iColumnWidth;
        /** edit data type reported to grid */
        private int m_iEditDataType;

        /** create a new AjaxColumnEntry */
        public OwAjaxColumnEntry(OwFieldColumnInfo columnInfo_p, int iColumnWidth_p, String sColumnName_p)
        {
            m_ColumnInfo = columnInfo_p;
            m_iColumnWidth = iColumnWidth_p;
            m_ColumnName = sColumnName_p;
        }

        /** get FieldColumnInfo */
        public OwFieldColumnInfo getFieldColumnInfo()
        {
            return m_ColumnInfo;
        }

        /** get name */
        public String getColumnName()
        {
            return m_ColumnName;
        }

        /** set name */
        public void setColumnName(String name_p)
        {
            m_ColumnName = name_p;
        }

        /** get width */
        public int getColumnWidth()
        {
            return m_iColumnWidth;
        }

        /** set width */
        public void setColumnWidth(int width_p)
        {
            m_iColumnWidth = width_p;
        }

        /** set edit data type */
        public void setEditDataType(int iEditDataType_p)
        {
            m_iEditDataType = iEditDataType_p;
        }

        /** get edit data type */
        public int getEditDataType()
        {
            return (m_iEditDataType);
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            StringBuilder result = new StringBuilder();
            if (m_ColumnInfo != null)
            {
                result.append("Column info property: ").append(m_ColumnInfo.getPropertyName()).append("\n");
            }
            if (m_ColumnName != null)
            {
                result.append("Column name: ").append(m_ColumnName).append("\n");
            }
            result.append("Column width: ").append(m_iColumnWidth);
            return result.toString();
        }
    }

    private static class OwIconFieldColumnInfo implements OwFieldColumnInfo
    {
        public int getAlignment()
        {
            return OwFieldColumnInfo.ALIGNMENT_DEFAULT;
        }

        public String getDisplayName(Locale locale_p)
        {
            return "ow_icon_column";
        }

        public String getPropertyName()
        {
            return "ow_icon_column";
        }

        public int getWidth()
        {
            return 0;
        }

        public boolean isSortable()
        {
            return false;
        }
    }

    /**
     *<p>
     * String-bag-value encoder/decoder for header based column information.   
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
    public static class OwColumnInfoBagValue
    {
        public static final String DEFAULT_EQ = "=";
        public static final String DEFAULT_COLUMN_SEPARATOR = ",";
        public static final String DEFAULT_HEADER_SEPARATOR = "#";
        public static final String DEFAULT_HEADERID_SEPARATOR = "~";
        public static final String DEFAULT_HEADER = "";

        private String columnSeparator = DEFAULT_COLUMN_SEPARATOR;
        private String headerSeparator = DEFAULT_HEADER_SEPARATOR;
        private String headerIDSeparator = DEFAULT_HEADERID_SEPARATOR;
        private String eq = DEFAULT_EQ;

        private Map<String, Map<String, Integer[]>> columns = new LinkedHashMap<String, Map<String, Integer[]>>();

        public OwColumnInfoBagValue()
        {

        }

        public OwColumnInfoBagValue(String value_p) throws OwInvalidOperationException
        {
            if (value_p != null && value_p.length() > 0)
            {
                if (value_p.contains(headerIDSeparator))
                {
                    String[] headers = value_p.split(headerSeparator);
                    for (String header : headers)
                    {
                        String[] headerSplit = header.split(headerIDSeparator);
                        if (headerSplit == null || headerSplit.length != 2)
                        {
                            String msg = "Invalid header foud : " + header;
                            LOG.error("OwColumnInfoBagValue.init : " + msg);
                            throw new OwInvalidOperationException(msg);
                        }

                        String headerId = headerSplit[0];
                        String headerColumns = headerSplit[1];
                        setAll(headerId, headerColumns);
                    }

                }
                else
                {
                    setAll(DEFAULT_HEADER, value_p);
                }
            }
        }

        private void setAll(String header_p, String columnsValue_p) throws OwInvalidOperationException
        {
            String[] columnsEqs = columnsValue_p.split(columnSeparator);
            for (String columnEq : columnsEqs)
            {
                String[] eqSplit = columnEq.split(eq);
                if (eqSplit.length != 2)
                {
                    String msg = "Invalid column found : " + columnEq;
                    LOG.error("OwColumnInfoBagValue.setAll : " + msg);
                    throw new OwInvalidOperationException(msg);
                }
                String columnName = eqSplit[0];
                String columnWidthString = eqSplit[1];

                int columnWidth = DEFAULT_COLUMN_WIDTH;

                try
                {
                    columnWidth = Integer.parseInt(columnWidthString);
                }
                catch (NumberFormatException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Invalid value for the colwidth, value=" + columnName + ". Expected was a numeric value. Set the colwidth to default value=" + columnWidthString, e);
                    }
                }

                put(header_p, columnName, columnWidth);
            }

        }

        private Map<String, Integer[]> force(String header_p)
        {
            Map<String, Integer[]> header = columns.get(header_p);
            if (header == null)
            {
                header = new LinkedHashMap<String, Integer[]>();
                columns.put(header_p, header);
            }
            return header;
        }

        public Set<String> getHeaders()
        {
            return columns.keySet();
        }

        private Map<String, Integer[]> getDefaultHeader()
        {
            return header(DEFAULT_HEADER);
        }

        public Integer widthOf(String column_p)
        {
            return widthOf(DEFAULT_HEADER, column_p);
        }

        public Integer widthOf(String header_p, String column_p)
        {
            Map<String, Integer[]> header = header(header_p);
            if (header != null)
            {
                Integer[] columnNumebrs = header.get(column_p);
                return columnNumebrs != null ? columnNumebrs[0] : null;
            }
            else
            {
                return null;
            }
        }

        public Set<String> getDefaultColumns()
        {
            Map<String, Integer[]> defaultHeader = getDefaultHeader();
            if (defaultHeader == null)
            {
                return Collections.EMPTY_SET;
            }
            else
            {
                return defaultHeader.keySet();
            }
        }

        public Set<String> columnsOf(String header_p)
        {
            Map<String, Integer[]> header = columns.get(header_p);
            if (header != null)
            {
                return header.keySet();
            }
            else
            {
                return Collections.EMPTY_SET;
            }
        }

        private Map<String, Integer[]> header(String header_p)
        {
            return columns.get(header_p);
        }

        public void put(String column_p, int width_p)
        {
            put(DEFAULT_HEADER, column_p, width_p);
        }

        public void clearDefaultHeader()
        {
            clear(DEFAULT_HEADER);
        }

        public void clear(String header_p)
        {
            Map<String, Integer[]> header = header(header_p);
            if (header != null)
            {
                header.clear();
            }
        }

        public void put(String header_p, String column_p, int width_p)
        {
            Map<String, Integer[]> header = force(header_p);
            Integer[] oldColumn = header.get(column_p);
            Integer index = header.size();
            if (oldColumn != null)
            {
                index = oldColumn[1];
            }
            header.put(column_p, new Integer[] { width_p, index });
        }

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            Set<Entry<String, Map<String, Integer[]>>> headerEtries = columns.entrySet();

            for (Entry<String, Map<String, Integer[]>> headerEntry : headerEtries)
            {
                if (builder.length() > 0)
                {
                    builder.append(headerSeparator);
                }
                builder.append(headerEntry.getKey());

                Map<String, Integer[]> headerColumns = headerEntry.getValue();
                Set<Entry<String, Integer[]>> headerColumnEntries = headerColumns.entrySet();

                for (Entry<String, Integer[]> columnEntry : headerColumnEntries)
                {
                    builder.append(columnEntry.getKey());
                    builder.append(eq);
                    Integer[] value = columnEntry.getValue();
                    builder.append(value[0]);
                    builder.append(columnSeparator);
                }

                if (!headerColumnEntries.isEmpty())
                {
                    int builderLen = builder.length();
                    builder.delete(builderLen - columnSeparator.length(), builderLen);
                }
            }

            return builder.toString();
        }

        public Integer indexOf(String header_p, String column_p)
        {
            Map<String, Integer[]> header = header(header_p);
            if (header != null)
            {
                Integer[] columnNumbers = header.get(column_p);
                return columnNumbers != null ? columnNumbers[1] : null;
            }
            else
            {
                return null;
            }
        }

        public Integer indexOf(String column_p)
        {
            return indexOf(DEFAULT_HEADER, column_p);
        }
    }
}