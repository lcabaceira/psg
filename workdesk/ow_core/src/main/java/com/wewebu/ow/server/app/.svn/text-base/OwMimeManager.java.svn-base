package com.wewebu.ow.server.app;

import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwConfiguration.OwMasterPluginInstance;
import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.app.id.viid.OwVIIdFactory;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * MIME Manager, holds the MIME table, creates Object hyper links,
 * handles requests to view and open objects according to their MIME type.<br/><br/>
 * <b>NOTE: ALWAYS CALL Reset() in your onRender method before using the insertLink functions, 
 * otherwise the map increases to infinite.</b>
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
public class OwMimeManager extends OwEventTarget
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwMimeManager.class);

    /** Random, to append to the &forceload URL query parameter **/
    private static final Random RANDOM = new Random();

    /** URL replacement scanner for high performance replacement */
    protected static final OwReplaceScanner URL_REPLACE_SCANNER = new OwDownloadURLReplaceScanner();

    /** page count replacement scanner for high performance replacement */
    protected static final OwReplaceScanner PAGECOUNT_REPLACE_SCANNER = new OwPageCountReplaceScanner();

    // === query keys for download servlets
    /** query string key for the download mode parameter */
    public static final String DWL_MODE_KEY = "dwlmode";
    /** query string key for the DMSID parameter */
    public static final String DMSID_KEY = "dmsid";
    /** query string key for the content type parameter */
    public static final String CONTENT_TYPE_KEY = "cttyp";
    /** query string key for property name of the property based content  */
    public static final String CONTENT_PROPERTY_KEY = "cproperty";
    /** query string key for the MIME type of the property based content  */
    public static final String CONTENT_MIMETYPE_KEY = "cmimetype";
    /** query string key for the page parameter */
    public static final String PAGE_KEY = "page";
    /** query string key for the xsl transformator URL */
    public static final String XSL_TRANSFORMATOR_KEY = "xsltrans";
    /** query string key for the xsl transformator encoding to use */
    public static final String XSL_TRANSFORMATOR_ENCODING_KEY = "xslencode";

    /** download mode used with DWL_MODE_KEY: opens the document directly in the browser */
    public static final int DWL_MODE_OPEN = 1;
    /** download mode used with DWL_MODE_KEY: queries to save the document */
    public static final int DWL_MODE_SAVE_COPY = 2;

    /** session attribute name for objects collection for MDI viewer */
    public static final String MDI_OBJECTS_COLLECTION_ATTRIBUTE_NAME = "OwMimeManager_BrowseList";

    /** session attribute name for file preview */
    public static final String FILE_PREVIEW_ATTRIBUTE_NAME = "OwMimeManager_FilePreview";

    /** session attribute file name for file preview*/
    public static final String FILE_PREVIEW_ATTRIBUTE_FILENAME = "OwMimeManager_FileName";

    /** session attribute MIME type of file to preview*/
    public static final String FILE_PREVIEW_ATTRIBUTE_MIMETYPE = "OwMimeManager_MIMEType";

    /** DMSID for MimeManager file preview */
    public static final String FILE_PREVIEW_DMSID = "owmimemanagerfilepreview";

    /** tag name of the icon in the mimenode */
    public static final String MIME_ICON_NAME = "icon";

    /** tag name of the openicon in mimenode
     * @since 2.5.2.0 */
    public static final String MIME_OPENICON_NAME = "openicon";

    /** tag name of the downloadurl in MIME node
     * @since 2.5.2.0 */
    public static final String MIME_DOWNLOAD_URL = "dwlurl";

    /** tag name of the viewer servlet definition in MIME node
     * @since 2.5.2.0 */
    public static final String MIME_VIEWERSERLVET = "viewerservlet";

    /** tag name of the viewer servlet definition in MIME node
     * @since 2.5.2.0 */
    public static final String MIME_EDITSERLVET = "editservlet";

    /** tag name for eventhandler of MIME node
     * @since 2.5.2.0 */
    public static final String MIME_EVENTHANDLER = "eventhandler";

    /** tag name for document function of MIME node
     * @since 2.5.2.0 */
    public static final String MIME_DOCUMENTFUNCTION = "documentfunction";

    /** attribute name for autoviewermode of MIME node
     * @since 2.5.2.0 */
    public static final String MIME_ATT_AUTOVIEWERMODE = "autoviewermode";

    public static final String MIME_ICON_SUBPATH = "/micon/";

    /** autoviewermode to use: display upload link */
    public static final int VIEWER_MODE_SIMPLE = 0x0000;
    /** autoviewermode to use: use default mode of application */
    public static final int VIEWER_MODE_DEFAULT = 0x0001;
    /** autoviewermode to use: open one single viewer for all documents */
    public static final int VIEWER_MODE_SINGLE = 0x0002;
    /** autoviewermode to use: open one viewer for each document */
    public static final int VIEWER_MODE_MULTI = 0x0003;
    /** autoviewermode to use: open one viewer for each document and try to put viewers next to each other for comparision */
    public static final int VIEWER_MODE_COMPARE = 0x0004;
    /** autoviewermode to use: call given javascript, i.e. treat URL as java script statement */
    public static final int VIEWER_MODE_JAVASCRIPT = 0x0005;

    // === replacement tokens for URL pattern
    /** token in the viewer servlet to be replaced by the DMSID */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_DMSID = "{dmsid}";
    /** token in the viewer servlet to be replaced by the base URL of the server */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_SERVERURL = "{serverurl}";
    /** token in the viewer servlet to be replaced by the base URL of the server with application context*/
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_BASEURL = "{baseurl}";
    /** token in the viewer servlet to be replaced by the base dir of the server deploy */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_BASEDIR = "{basedir}";
    /** token in the viewer servlet to be replaced by the security token */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_SECURITYTOKEN = "{securitytoken}";
    /** token in the viewer servlet to be replaced by the encoded security token */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_SECURITYTOKEN_ENC = "{securitytokenenc}";

    /** token in the viewer servlet to be replaced by the number of pages in the requested object */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PAGE_COUNT = "{pagecount}";
    /** token in the viewer servlet to be replaced by the requested page */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PAGE = "{page}";
    /** token in the viewer servlet to be replaced by the requested page */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_CONTENT_TYPE = "{contenttype}";
    /** token in the viewer servlet to be replaced by the requested page */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_DOWNLOAD_URL = "{dwlurl}";
    /**Token to be replaced for specific version independent id representation
     * @since 4.2.0.0*/
    public static final String REPLACE_TOKEN_VIID = "{viid}";

    /** token in the viewer servlet to be replaced by the property following the : */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START = "{prop";

    /** char to indicate encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_ENCODE_CHAR = ':';

    /** char to indicate JavaScript encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_JSENCODE_CHAR = '~';

    /** char to indicate NO encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_NOENCODE_CHAR = '#';

    /** token in the viewer servlet to be replaced by the property end delimiter */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_END = "}";

    /** query string key for the objectlist index to find the selected object upon onMimeOpenObject. */
    protected static final String OBJECT_INDEX_KEY = "oi";

    /** query string key for the subpath of the record subfolder to open */
    protected static final String SUBPATH_KEY = "subpath";

    /** prefix to create a URL out of a script command */
    public static final String SCRIPT_URL_PREFIX = "javascript:";

    /** prefix for a specific folder class, used to configure the MIME types in the owmimetable.xml file **/
    public static final String MIME_TYPE_PREFIX_OW_FOLDER = "ow_folder/";

    /** prefix for default types (OBJECT_TYPE_DOCUMENT, OBJECT_TYPE_FOLDER, OBJECT_TYPE_HISTORY...), 
     * used to configure the MIME types in the owmimetable.xml file **/
    public static final String MIME_TYPE_PREFIX_OW_DEFAULT = "ow_default/";

    /** prefix for custom object, used to configure the MIME types in the owmimetable.xml file **/
    public static final String MIME_TYPE_PREFIX_OW_CUSTOMOBJECT = "ow_customobject/";

    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;
    /** application m_Configuration reference */
    protected OwConfiguration m_Configuration;

    // Parent for document functions
    protected OwObject m_parent;

    /**event listener for the function plugin refresh events */
    protected OwClientRefreshContext m_RefreshContext;

    /**  flag signal for debug reasons, to throw an error if client forgets to call reset */
    protected boolean m_fDEBUG_ResetCalled = false;

    /** map which keeps the objects to be opened and create links upon */
    protected Map m_ObjectMap = new HashMap();

    /** style to be used for items */
    protected String m_strMimeItemStyle = "OwMimeItem";

    /** style to be used for icons */
    protected String m_strMimeIconStyle = "OwMimeIcon";

    /** label to use when name  is null */
    protected String undefName;

    /** label to use for tooltip on documents <br />
     * use "app.OwMimeManager.documenttitle" to localize/control value*/
    protected String documentTitle;

    /** label to use for tooltip on objects <br />
     * use "app.OwMimeManager.objecttitle" to localize/control value*/
    protected String objectTitle;

    // number formatter
    protected NumberFormat m_NumberFormat;

    /** the context to be used for MIME type resolution 
     * @since 2.5.3.0 */
    protected String m_mimeTypeContext = null;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        // set context
        m_MainContext = (OwMainAppContext) getContext();

        // get application m_Configuration
        m_Configuration = m_MainContext.getConfiguration();

        // cache strings for fast access
        setUndefinedLabel(getContext().localize("app.OwMimeManager.undefname", "[undefined]"));
        documentTitle = getContext().localize("app.OwMimeManager.documenttitle", "Show document");
        objectTitle = getContext().localize("app.OwMimeManager.objecttitle", "Edit object");

        // Number
        m_NumberFormat = NumberFormat.getNumberInstance(getContext().getLocale());
        m_NumberFormat.setGroupingUsed(false);
    }

    /** set the style for the MIME icons
     * @param strMimeIconStyle_p String new CSS style
     */
    public void setIconStyle(String strMimeIconStyle_p)
    {
        this.m_strMimeIconStyle = strMimeIconStyle_p;
    }

    /**
     * Return the style for icons which are rendered
     * by this MIME type manager.
     * @return String name of CSS class name
     * @since 2.5.2.0
     */
    public String getIconStyle()
    {
        return this.m_strMimeIconStyle;
    }

    /** set the style for the MIME items
     * @param strMimeItemStyle_p String new CSS style
     */
    public void setItemStyle(String strMimeItemStyle_p)
    {
        this.m_strMimeItemStyle = strMimeItemStyle_p;
    }

    /**
     * Set the Label which should be used for 
     * rendering of text links, if given display value is <code>null</code>.
     * @param undefLabel_p String label to use, should be non-null value
     * @since 2.5.2.0
     */
    public void setUndefinedLabel(String undefLabel_p)
    {
        if (undefLabel_p != null)
        {
            this.undefName = undefLabel_p;
        }
        else
        {
            this.undefName = "app.OwMimeManager.undefname";
        }
    }

    /**
     * Get label to use for rendering of text links,
     * if the given display value is <code>null</code>.
     * @return String to use for rendering links
     * @see #setUndefinedLabel(String)
     * @since 2.5.2.0
     */
    public String getUndefinedLabel()
    {
        return this.undefName;
    }

    /**
     * Set the context to be used for MIME type resolution
     * 
     * @param context_p the context to be used for MIME type resolution, can be <code>null</code>
     * 
     * @since 2.5.3.0
     */
    public void setMimeTypeContext(String context_p)
    {
        m_mimeTypeContext = context_p;
    }

    /**
     * Returns the context to be used for MIME type resolution
     * 
     * @return the context to be used for MIME type resolution, can be <code>null</code>
     */
    public String getMimeTypeContext()
    {
        return m_mimeTypeContext;
    }

    /** get the MIME info node from the MIME table for the requested object
     *
     * @param configuration_p OwConfiguration object for static use
     * @param obj_p OwObjectReference to retrieve MIME info for
     * @return OwXMLUtil wrapped DOM node, may be empty if no MIME type definition was found
     */
    public static OwXMLUtil getMimeNode(OwConfiguration configuration_p, OwObjectReference obj_p) throws Exception
    {
        // delegate to context sensitive method with empty context
        return getMimeNode(configuration_p, obj_p, null);
    }

    /**
     * Returns the MIME info node from the MIME table for the requested object
     * in the given context.
     *
     * @param configuration_p OwConfiguration object for static use
     * @param obj_p OwObjectReference to retrieve MIME info for
     * @param context_p optional context for MIME node resolution. Can be <code>null</code>.
     * 
     * @return OwXMLUtil wrapped DOM node, may be empty if no MIME type definition was found
     * 
     * @since 2.5.3.0
     */
    private static OwXMLUtil getMimeNode(OwConfiguration configuration_p, OwObjectReference obj_p, String context_p) throws Exception
    {
        OwXMLUtil mimeNode = null;
        if (context_p != null)
        {
            mimeNode = configuration_p.getMIMENode(context_p + ":" + obj_p.getMIMEType());
        }
        // try to find mimetype without context
        if (mimeNode == null)
        {
            mimeNode = configuration_p.getMIMENode(obj_p.getMIMEType());
        }
        // use default if MIME type is not specified
        if (mimeNode == null)
        {
            // === MIME type not defined, so take the default
            mimeNode = configuration_p.getDefaultMIMENode(obj_p.getType());
            if (mimeNode == null)
            {
                // return empty node
                return new OwStandardXMLUtil();
            }
        }

        return mimeNode;
    }

    /** get the icon path for the mimetype
     * 
     * @param context_p OwMainAppContext current context
     * @param obj_p OwObjectReference for which to retrieve the MIME icon
     * @return {@link String} containing the path to the icon
     * @throws Exception
     */
    public static String getMimeIcon(OwMainAppContext context_p, OwObjectReference obj_p) throws Exception
    {
        OwXMLUtil MimeNode = getMimeNode(context_p.getConfiguration(), obj_p);

        // create link
        StringBuilder iconpath = new StringBuilder();
        iconpath.append(context_p.getDesignURL());
        iconpath.append(MIME_ICON_SUBPATH);
        iconpath.append(MimeNode.getSafeTextValue(MIME_ICON_NAME, "unknown.png"));

        return iconpath.toString();
    }

    /** insert a icon object hyperlink to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     *  
     * @param w_p
     * @param obj_p
     * @param subpath_p optional path to the subitem to open or null
     * @throws Exception
     */
    public void insertIconLink(java.io.Writer w_p, OwObjectReference obj_p, String subpath_p) throws Exception
    {
        insertIconLink(w_p, obj_p, subpath_p, 1);
    }

    /** insert a icon object hyperlink to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     *  
     * @param w_p
     * @param obj_p
     * @param subpath_p optional path to the subitem to open or null
     * @param page_p int requested page number
     * @throws Exception
     */
    public void insertIconLink(java.io.Writer w_p, OwObjectReference obj_p, String subpath_p, int page_p) throws Exception
    {
        // get MIME info from MIME table for this object
        StringBuffer link = createIconLink(obj_p);
        // insert link

        insertHtmlLink(w_p, link.toString(), obj_p, subpath_p, page_p);

    }

    /**
     * Create the icon link for the given object
     * @param obj_p - the {@link OwObjectReference} object
     * @return - a {@link StringBuffer} object containing the link.
     * @throws Exception
     * @since 3.0.0.0
     */
    protected StringBuffer createIconLink(OwObjectReference obj_p) throws Exception
    {
        OwXMLUtil mimeNode = getMimeNode(m_Configuration, obj_p);

        String strViewServlet = mimeNode.getSafeTextValue(MIME_VIEWERSERLVET, null);
        String tooltipText = "";
        StringWriter w_p = new StringWriter();
        if (strViewServlet != null)
        {
            if (obj_p.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL) || mimeNode.getSafeBooleanAttributeValue("nocontentcheck", false))
            {
                int iViewerMode = mimeNode.getSafeIntegerAttributeValue(MIME_ATT_AUTOVIEWERMODE, VIEWER_MODE_SIMPLE);
                switch (iViewerMode)
                {
                    default:
                    {
                        // === open in automatic arranged window

                        OwHTMLHelper.writeSecureHTML(w_p, documentTitle);
                        w_p.write(" (");
                        OwHTMLHelper.writeSecureHTML(w_p, obj_p.getMIMEType());
                        w_p.write(")");
                    }
                        break;

                    case VIEWER_MODE_SIMPLE:
                    {
                        // === invoke link and let the browser decide about the viewer window
                        OwHTMLHelper.writeSecureHTML(w_p, documentTitle);
                        w_p.write(" (");
                        OwHTMLHelper.writeSecureHTML(w_p, obj_p.getMIMEType());
                        w_p.write(")");
                    }
                        break;
                }
            }
        }
        else
        {
            String strEventHandler = mimeNode.getSafeTextValue(MIME_EVENTHANDLER, null);
            String strDocumentFunction = mimeNode.getSafeTextValue(MIME_DOCUMENTFUNCTION, null);
            if (strEventHandler != null || strDocumentFunction != null)
            {
                OwHTMLHelper.writeSecureHTML(w_p, objectTitle);
            }
        }
        tooltipText = w_p.toString();

        // create link
        StringBuffer link = new StringBuffer("<img src=\"");
        link.append(m_MainContext.getDesignURL());
        link.append(MIME_ICON_SUBPATH);
        link.append(mimeNode.getSafeTextValue(MIME_ICON_NAME, "unknown.png"));
        link.append("\" border=\"0\" class=\"");
        link.append(m_strMimeIconStyle);
        link.append("\" alt=\"").append(tooltipText).append("\" title=\"").append(tooltipText).append("\" draggable=\"false\" />");
        return link;
    }

    /** insert a icon object hyperlink to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     * 
     * @param w_p
     * @param obj_p
     * @throws Exception
     */
    public void insertIconLink(java.io.Writer w_p, OwObjectReference obj_p) throws Exception
    {
        StringBuffer link = createIconLink(obj_p);

        insertHtmlLink(w_p, link.toString(), obj_p, null);

    }

    /** create a download URL where the content of the specified object, contenttype and page can be found
    *
    * @param context_p OwMainAppContext since method is static
    * @param obj_p OwObjectReference to retrieve the URL 
    * @param iContentType_p int requested content type as specified in OwContentCollection
    * @param iPage_p int requested page number
    * @param mimeNode_p OwXMLUtil MIME node 
    *
    * @return String download URL for content
    */
    public static String getDownloadURL(OwMainAppContext context_p, OwObjectReference obj_p, int iContentType_p, int iPage_p, OwXMLUtil mimeNode_p) throws Exception
    {
        // === create download / view link
        // === servlet handler
        String strDownloadURLPattern = mimeNode_p.getSafeTextValue(MIME_DOWNLOAD_URL, null);
        if (strDownloadURLPattern != null)
        {
            // === URL pattern defined
            // replace the tokens in the pattern string to build a URL 
            return getServletReplacedTokenString(strDownloadURLPattern, context_p, obj_p, mimeNode_p, iPage_p, iContentType_p);
        }
        else
        {
            // === no pattern defined use default
            return getDefaultDownloadURL(context_p, obj_p, OwContentCollection.CONTENT_TYPE_DOCUMENT, iPage_p);
        }
    }

    /** create a default download URL where the content of the specified object, contenttype and page can be found
    *
    * @param context_p OwMainAppContext since method is static
    * @param obj_p OwObjectReference to retrieve the URL 
    * @param iContentType_p int requested content type as specified in OwContentCollection
    * @param iPage_p int requested page number
    *
    * @return String download URL for content
    */
    protected static String getDefaultDownloadURL(OwMainAppContext context_p, OwObjectReference obj_p, int iContentType_p, int iPage_p) throws Exception
    {
        StringBuilder buf = new StringBuilder();

        buf.append(context_p.getBaseURL());
        buf.append("/getContent?");
        buf.append(DMSID_KEY);
        buf.append("=");
        buf.append(OwAppContext.encodeURL(obj_p.getDMSID()));
        buf.append("&");
        buf.append(CONTENT_TYPE_KEY);
        buf.append("=");
        buf.append(iContentType_p);
        buf.append("&");
        buf.append(PAGE_KEY);
        buf.append("=");
        buf.append(iPage_p);

        return buf.toString();
    }

    /**
     * Creates the String representation of the value of a given property.
     * OwObject typed values are converted to their DMSID. 
     * Non OwObject values are converted to their Java-String representation through {@link Object#toString()}. 
     * @param property_p
     * @return the String representation of the value of the given property.
     * @throws Exception if the String conversion fails 
     * @throws NullPointerException if the value of the given property is null
     * @since 3.1.0.0
     */
    private static String retrieveValue(OwProperty property_p) throws Exception
    {
        OwPropertyClass propertyClass = property_p.getPropertyClass();
        String javaClassName = propertyClass.getJavaClassName();
        Object value = property_p.getValue();
        if (value == null)
        {
            //keep legacy behavior - do not convert to OwException
            throw new NullPointerException("null property value can not be converted to String!");
        }
        if ("com.wewebu.ow.server.ecm.OwObject".equals(javaClassName))
        {
            OwObject owObject = (OwObject) value;
            return owObject.getDMSID();
        }
        else
        {
            return value.toString();
        }
    }

    /** replace a object property placeholder
     * @param strIn_p String to search and replace
     * @param obj_p OwObjectReference
     * 
     * @return String
     * @throws Exception 
     * */
    protected static String replaceProperties(OwMainAppContext context_p, String strIn_p, OwObjectReference obj_p) throws Exception
    {
        // check if placeholder is available at all before we allocate the object instance
        int iIndex = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START);
        if (-1 == iIndex)
        {
            return strIn_p;
        }

        // now we get the object instance
        OwObject obj = obj_p.getInstance();//OwEcmUtil.resolveObjectReference(context_p,obj_p);

        int iOldIndex = 0;

        StringBuilder strRet = new StringBuilder();

        while (-1 != iIndex)
        {
            strRet.append(strIn_p.substring(iOldIndex, iIndex));

            // skip encoding character
            iIndex++;
            iIndex += VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START.length();

            // get the property name
            int iEnd = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_END, iIndex);
            String strPropName = strIn_p.substring(iIndex, iEnd);

            // replace property ignore all exceptions
            if (strIn_p.charAt(iIndex - 1) == VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_ENCODE_CHAR)
            {
                // encoded property
                try
                {
                    OwProperty property = obj.getProperty(strPropName);
                    String stringValue = retrieveValue(property);
                    strRet.append(OwAppContext.encodeURL(stringValue));
                }
                catch (Exception e)
                {
                    LOG.debug("OwMimeManager.replaceProperties(): Error getting, replacing or appending URL encoded property...", e);
                }
            }
            else if (strIn_p.charAt(iIndex - 1) == VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_JSENCODE_CHAR)
            {
                // encoded property
                try
                {
                    OwProperty property = obj.getProperty(strPropName);
                    String valueString = retrieveValue(property);
                    strRet.append(OwHTMLHelper.encodeJavascriptString(valueString, true));
                }
                catch (Exception e)
                {
                    LOG.debug("OwMimeManager.replaceProperties(): Error getting, replacing or appending JavaScript encoded property...", e);
                }
            }
            else
            {
                // unencoded property
                try
                {
                    OwProperty property = obj.getProperty(strPropName);
                    String stringValue = retrieveValue(property);
                    strRet.append(stringValue);
                }
                catch (Exception e)
                {
                    LOG.debug("OwMimeManager.replaceProperties(): Error getting or appending unencoded property...", e);
                }
            }

            iIndex = iEnd + 1;

            iOldIndex = iIndex;

            iIndex = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START, iIndex);
        }

        if (iOldIndex <= strIn_p.length())
        {
            strRet.append(strIn_p.substring(iOldIndex, strIn_p.length()));
        }

        return strRet.toString();
    }

    /** insert a text object hyperlink to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     * 
     * @param w_p Writer where to write the text as link
     * @param strDisplayName_p String text/name/label to use in link representation
     * @param obj_p OwObjectReference which is mapped to the link
     * @throws Exception
     */
    public void insertTextLink(java.io.Writer w_p, String strDisplayName_p, OwObjectReference obj_p) throws Exception
    {
        insertTextLink(w_p, strDisplayName_p, obj_p, null, 1);
    }

    /** insert a text object hyperlink to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     *  
     * @param w_p
     * @param strDisplayName_p
     * @param obj_p
     * @param subpath_p optional path to the subitem to open or null
     * @throws Exception
     */
    public void insertTextLink(java.io.Writer w_p, String strDisplayName_p, OwObjectReference obj_p, String subpath_p) throws Exception
    {
        insertTextLink(w_p, strDisplayName_p, obj_p, subpath_p, 1);
    }

    /** insert a text object hyperlink to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     *  
     * @param w_p
     * @param strDisplayName_p
     * @param obj_p
     * @param subpath_p subpath_p optional path to the subitem to open or null
     * @param page_p integer representing the value of page to open
     * @throws Exception
     */
    public void insertTextLink(java.io.Writer w_p, String strDisplayName_p, OwObjectReference obj_p, String subpath_p, int page_p) throws Exception
    {
        // no empty displaynames
        if (strDisplayName_p == null)
        {
            strDisplayName_p = getUndefinedLabel();
        }
        String formattedDisplayName = strDisplayName_p;
        // try to treat as a number
        try
        {
            formattedDisplayName = getNumberFormater().format(Double.parseDouble(strDisplayName_p));
        }
        catch (NumberFormatException e)
        {
            // ignore, not a number
        }
        // encode DisplayName
        StringWriter sw = new StringWriter();
        OwHTMLHelper.writeSecureHTML(sw, formattedDisplayName);
        // insert HTML link
        insertHtmlLink(w_p, sw.toString(), obj_p, subpath_p, page_p);
    }

    /** insert a hyperlink wrapped around arbitrary HTML code to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     *  
     * @param w_p
     * @param strHtmlCode_p
     * @param obj_p
     * @param subpath_p optional path to the subitem to open or null
     * @throws Exception
     */
    public void insertHtmlLink(java.io.Writer w_p, String strHtmlCode_p, OwObjectReference obj_p, String subpath_p) throws Exception
    {
        insertHtmlLink(w_p, strHtmlCode_p, obj_p, subpath_p, 1);
    }

    /** insert a hyperlink wrapped around arbitrary HTML code to download or open a OwObjectReference in a viewer
     *  This function is useful for objects listed by OwObjectCollection
     *  
     * @param w_p
     * @param strHtmlCode_p
     * @param obj_p
     * @param subpath_p optional path to the subitem to open or null
     * @param page_p integer representing the value of page to open
     * @throws Exception
     */
    public void insertHtmlLink(java.io.Writer w_p, String strHtmlCode_p, OwObjectReference obj_p, String subpath_p, int page_p) throws Exception
    {
        if (strHtmlCode_p == null)
        {
            strHtmlCode_p = "";
        }

        // === check if client calls the Reset Function
        if (!m_fDEBUG_ResetCalled)
        {
            throw new OwInvalidOperationException("OwMimeManager.insertHtmlLink: Used MimeManager without reseting the map. Did you call OwMimeManager.Reset() in your onRender method?");
        }

        // determine MIME description node from XML mimetable
        OwXMLUtil mimeNode = getMimeNode(m_Configuration, obj_p, m_mimeTypeContext);
        // === servlet handler
        String strViewServlet = mimeNode.getSafeTextValue(MIME_VIEWERSERLVET, null);
        int iViewerMode = mimeNode.getSafeIntegerAttributeValue(MIME_ATT_AUTOVIEWERMODE, VIEWER_MODE_SIMPLE);
        if (strViewServlet != null)
        {
            //check if string contains quotations marks
            if (strViewServlet.indexOf('\"') > 0) //&& VIEWER_MODE_JAVASCRIPT != iViewerMode)
            {
                // replace all " with the UTF-8, else we create invalid HTML code.
                //DON'T TRY TO ESCAPE THE QOUT LIKE \", THIS IS NOT WORKING IN LINKS!
                strViewServlet = strViewServlet.replaceAll("[\"]", "%22");
            }

            // compute open link flag, true = if content and download allowed
            if (obj_p.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL) || mimeNode.getSafeBooleanAttributeValue("nocontentcheck", false))
            {
                // === create view link in new browser
                // replace the tokens in the servlet string to build a URL 

                String strURL = getServletReplacedTokenString(strViewServlet, m_MainContext, obj_p, mimeNode, page_p);
                switch (iViewerMode)
                {
                    default:
                    {
                        // === open in automatic arranged window
                        w_p.write("<a title=\"");
                        OwHTMLHelper.writeSecureHTML(w_p, documentTitle);
                        w_p.write(" (");
                        OwHTMLHelper.writeSecureHTML(w_p, obj_p.getMIMEType());
                        w_p.write(")");
                        w_p.write("\" href=\"");
                        w_p.write(SCRIPT_URL_PREFIX + getAutoViewerScript(m_MainContext, strURL, iViewerMode, obj_p.getDMSID(), obj_p.getDMSID(), page_p));
                        w_p.write("\" class=\"");
                        w_p.write("OwMimeItem");
                        w_p.write("\">");
                        w_p.write(strHtmlCode_p);
                        w_p.write("</a>");
                        w_p.write("<span class=\"accessibility\">&nbsp;");
                        w_p.write(m_MainContext.localize("app.OwMimeManager.anchor.target.jsopen.warning", "(This link will open a new window)"));
                        w_p.write("</span>");
                    }
                        break;

                    case VIEWER_MODE_SIMPLE:
                    {
                        // === invoke link and let the browser decide about the viewer window
                        w_p.write("<a target=\"_new\" title=\"");
                        OwHTMLHelper.writeSecureHTML(w_p, documentTitle);
                        w_p.write(" (");
                        OwHTMLHelper.writeSecureHTML(w_p, obj_p.getMIMEType());
                        w_p.write(")");
                        w_p.write("\" href=\"");
                        w_p.write(strURL);
                        w_p.write("\" class=\"");
                        w_p.write("OwMimeItem");
                        w_p.write("\">");
                        w_p.write(strHtmlCode_p);
                        w_p.write("</a>");
                        w_p.write("<span class=\"accessibility\">&nbsp;");
                        w_p.write(m_MainContext.localize("app.OwMimeManager.anchor.target.new.warning", "(This link will open a new window, if no window with the same title has already been opened)"));
                        w_p.write("</span>");

                    }
                        break;
                }
            }
            else
            {
                // === no content
                w_p.write(strHtmlCode_p);
            }

            return;
        }

        // add to map for later reference in onOpen
        registerObject(obj_p);

        // === plugin event handler (dispatches a event to the specific plugin)
        String strEventHandler = mimeNode.getSafeTextValue(MIME_EVENTHANDLER, null);
        if (strEventHandler != null)
        {
            // === dispatch event to plugin
            subpath_p = OwAppContext.encodeURL(subpath_p);
            String strURL = createLinkUrl("PluginEventHandler", obj_p, subpath_p);
            w_p.write("<a title=\"");
            OwHTMLHelper.writeSecureHTML(w_p, objectTitle);
            w_p.write("\" href=\"");
            w_p.write(strURL);
            w_p.write("\" class='");
            OwHTMLHelper.writeSecureHTML(w_p, m_strMimeItemStyle);
            w_p.write("'>");
            w_p.write(strHtmlCode_p);
            w_p.write("</a>");
            return;
        }

        // === document function handler (creates the specified document function link)
        String strDocumentFunction = mimeNode.getSafeTextValue(MIME_DOCUMENTFUNCTION, null);
        if (strDocumentFunction != null)
        {
            // === create document function link
            subpath_p = OwAppContext.encodeURL(subpath_p);
            String strURL = createLinkUrl("DocumentFunctionHandler", obj_p, subpath_p);
            w_p.write("<a title=\"");
            OwHTMLHelper.writeSecureHTML(w_p, objectTitle);
            w_p.write("\" href=\"");
            w_p.write(strURL);
            w_p.write("\" class='");
            OwHTMLHelper.writeSecureHTML(w_p, m_strMimeItemStyle);
            w_p.write("'>");
            w_p.write(strHtmlCode_p);
            w_p.write("</a>");
            return;
        }

        // no servlet or handler defined, don't show a link at all
        w_p.write(strHtmlCode_p);
    }

    /**
     * create the URL of the given event and the additional parameter
     * @param eventName_p 
     * @param obj_p
     * @param subpath_p String subpath or null to open root
     * @return a {@link String}
     */
    protected String createLinkUrl(String eventName_p, OwObjectReference obj_p, String subpath_p)
    {
        StringBuilder query = new StringBuilder();

        query.append(OBJECT_INDEX_KEY);
        query.append("=");
        query.append(String.valueOf(obj_p.hashCode()));

        if (null != subpath_p)
        {
            query.append("&");
            query.append(SUBPATH_KEY);
            query.append("=");
            query.append(subpath_p);
        }

        return getEventURL(eventName_p, query.toString());
    }

    /** clear the map before you call insertLink methods, otherwise the map would increase to infinite
     * <p>
     * <b>NOTE</b>: Never forget to call this function in your onRender Method
     * </p>
     * @since 3.2.0.0
     */
    public void reset()
    {
        m_ObjectMap.clear();

        // signal for debug reasons, to throw an error if client forgets to call reset
        m_fDEBUG_ResetCalled = true;
    }

    /** set parent to use in document functions */
    public void setParent(OwObject parent_p)
    {
        m_parent = parent_p;
    }

    /**  event called when user clicked on a name link of an object entry in the list 
     *   Creates a new dialog for the clicked object
     *
     *   @param request_p  HttpServletRequest
     */
    public void onDocumentFunctionHandler(HttpServletRequest request_p) throws Exception
    {
        // === open object in given plugin
        // parse query string
        String strObjectKey = request_p.getParameter(OBJECT_INDEX_KEY);

        if (strObjectKey != null)
        {
            OwObjectReference objRef = (OwObjectReference) m_ObjectMap.get(strObjectKey);

            // Dispatch event to Document function plugin
            OwDocumentFunction docfunc = getHandlerDocumentPlugin(m_MainContext, objRef, m_mimeTypeContext);

            OwObject obj = objRef.getInstance();//OwEcmUtil.resolveObjectReference(m_MainContext,objRef);

            if (docfunc.isEnabled(obj, m_parent, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                docfunc.onClickEvent(obj, m_parent, m_RefreshContext);
            }
            else
            {
                throw new OwInvalidOperationException(m_MainContext.localize("app.OwMimeManager.docfuncdisabled", "The function is not available for this object."));
            }
        }
    }

    /** register an eventlistener with this view to receive notifications
     * @param eventlister_p OwClientRefreshContext interface
     * */
    public void setRefreshContext(OwClientRefreshContext eventlister_p)
    {
        m_RefreshContext = eventlister_p;
    }

    /**  event called when user clicked on a name link of an object entry in the list 
     *   Dispatches the object to a plugin
     *
     *   @param request_p  HttpServletRequest
     */
    public void onPluginEventHandler(HttpServletRequest request_p) throws Exception
    {
        // === open object in given plugin
        // parse query string
        String strObjectKey = request_p.getParameter(OBJECT_INDEX_KEY);
        String subpath = request_p.getParameter(SUBPATH_KEY);
        if ((null != subpath) && (subpath.length() == 0))
        {
            subpath = null;
        }

        if (strObjectKey != null)
        {
            OwObjectReference objRef = (OwObjectReference) m_ObjectMap.get(strObjectKey);

            // get the handler plugin
            OwMasterDocument masterPlugin = getHandlerMasterPlugin(m_MainContext, objRef, m_mimeTypeContext);

            // open with plugin
            delegateToMasterPlugin(m_MainContext, masterPlugin, objRef, subpath);
        }
    }

    /** get the formatter used for numbers
     * 
     * @return NumberFormat
     */
    protected NumberFormat getNumberFormater()
    {
        return m_NumberFormat;
    }

    /** overridable handler function
     * 
     * @param masterPlugin_p OwMasterDocument of the handler plugin to open the object
     * @param obj_p the OwObjectReference to open
     * @param subpath_p String subpath or null to open root
     */
    protected static void delegateToMasterPlugin(OwMainAppContext context_p, OwMasterDocument masterPlugin_p, OwObjectReference obj_p, String subpath_p) throws Exception
    {
        OwObject obj = obj_p.getInstance();

        // get MIME info from MIME table for this object
        OwXMLUtil MimeNode = getMimeNode(context_p.getConfiguration(), obj);

        if ((subpath_p == null) && OwXMLDOMUtil.getSafeBooleanAttributeValue(MimeNode.getSubNode(MIME_EVENTHANDLER), "openrecord", false))
        {
            // try to open the record the object resides in
            List parentPathInfoList = OwEcmUtil.getParentPathOfClass(obj, context_p.getConfiguration().getRecordClassNames());
            if (parentPathInfoList.size() > 0)
            {
                OwEcmUtil.OwParentPathInfo pathInfo = (OwEcmUtil.OwParentPathInfo) parentPathInfoList.get(0);

                // open record
                masterPlugin_p.dispatch(OwDispatchCodes.OPEN_OBJECT, pathInfo.getParent(), pathInfo.getPath() + OwObject.STANDARD_PATH_DELIMITER + obj.getID());
            }
            else
            {
                // open given object
                masterPlugin_p.dispatch(OwDispatchCodes.OPEN_OBJECT, obj, null);
            }
        }
        else
        {
            // open given object
            masterPlugin_p.dispatch(OwDispatchCodes.OPEN_OBJECT, obj, subpath_p);
        }

        // We dispatched the object, now we need to close the dialogs
        context_p.closeAllDialogs();
    }

    /** get the handler master plugin for the given object reference if defined.
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference to open
     * 
     * @return OwMasterDocument of plugin or null if not found. (throws a OwConfigurationException if handlerkey is found but can not be resolved)
     * 
     * @throws Exception, OwConfigurationException
     * 
     */
    public static OwMasterDocument getHandlerMasterPlugin(OwMainAppContext context_p, OwObjectReference obj_p) throws Exception
    {
        return getHandlerMasterPlugin(context_p, obj_p, null);
    }

    /** get the handler master plugin for the given object reference if defined.
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference to open
     * @param mimeContext_p String context to be used for MIME retrieval, can be null
     * @return OwMasterDocument of plugin or null if not found. (throws a OwConfigurationException if handlerkey is found but can not be resolved)
     * 
     * @throws Exception, OwConfigurationException
     * @since 3.0.0.0
     */
    private static OwMasterDocument getHandlerMasterPlugin(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p) throws Exception
    {
        // get MIME info from MIME table for this object
        OwXMLUtil MimeNode = getMimeNode(context_p.getConfiguration(), obj_p, mimeContext_p);

        // === plugin event handler (dispatches a event to the specific plugin)
        String strEventHandler = MimeNode.getSafeTextValue(MIME_EVENTHANDLER, null);
        if (strEventHandler != null)
        {
            String strType = OwXMLDOMUtil.getSafeStringAttributeValue(MimeNode.getSubNode(MIME_EVENTHANDLER), "type", "id");

            if (strType.equalsIgnoreCase("id"))
            {
                // === ID defined plugin
                // get given plugin and open the object
                OwMasterDocument masterPlugin = (OwMasterDocument) context_p.getEventTarget(strEventHandler);
                if (null == masterPlugin)
                {
                    String msg = "OwMimeManager.getHandlerMasterPlugin: The MIME type handler could not be found. Please ensure that the master plugin is installed and plugin-id has appendix .Doc in owmimetable.xml, name = " + strEventHandler;
                    LOG.fatal(msg);
                    throw new OwConfigurationException(context_p.localize("app.OwMimeManager.handlerundefined",
                            "The MIME type handler could not be found. Please ensure that the Master Plugin is installed and plugin ID has appendix .Doc in owmimetable.xml:") + strEventHandler);
                }

                return masterPlugin;
            }
            else
            {
                // === class defined plugin
                // find master plugin with class
                Iterator it = context_p.getConfiguration().getMasterPlugins(false).iterator();
                while (it.hasNext())
                {
                    OwMasterPluginInstance inst = (OwMasterPluginInstance) it.next();
                    if (inst.getPluginClassName().equals(strEventHandler))
                    {
                        return inst.getDocument();
                    }
                }

                String msg = "OwMimeManager.getHandlerMasterPlugin: The MIME type handler could not be found. Please ensure that the master plugin is installed, name = " + strEventHandler;
                LOG.fatal(msg);
                throw new OwConfigurationException(context_p.localize("app.OwMimeManager.handlerundefined",
                        "The MIME type handler could not be found. Please ensure that the Master Plugin is installed and plugin ID has appendix .Doc in owmimetable.xml:") + strEventHandler);
            }
        }
        return null;
    }

    /** get the handler document plugin for the given object reference if defined.
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference to open
     * 
     * @return OwDocumentFunction of plugin or null if not found. (throws a OwConfigurationException if handlerkey is found but can not be resolved)
     * 
     * @throws Exception, OwAccessDeniedException
     */
    public static OwDocumentFunction getHandlerDocumentPlugin(OwMainAppContext context_p, OwObjectReference obj_p) throws Exception
    {
        return getHandlerDocumentPlugin(context_p, obj_p, null);
    }

    /** get the handler document plugin for the given object reference if defined.
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference to open
     * @param mimeContext_p String representing context for MIME retrieval, can be null
     * @return OwDocumentFunction of plugin or null if not found. (throws a OwConfigurationException if handler key is found but can not be resolved)
     * 
     * @throws Exception, OwAccessDeniedException
     * @since 3.0.0.0
     */
    private static OwDocumentFunction getHandlerDocumentPlugin(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p) throws Exception
    {
        // get MIME info from MIME table for this object
        OwXMLUtil MimeNode = getMimeNode(context_p.getConfiguration(), obj_p, mimeContext_p);

        String strDocumentFunction = MimeNode.getSafeTextValue(MIME_DOCUMENTFUNCTION, null);
        if (strDocumentFunction != null)
        {
            try
            {
                return context_p.getConfiguration().getDocumentFunction(strDocumentFunction);
            }
            catch (Exception e)
            {
                throw new OwConfigurationException(context_p.localize("app.OwMimeManager.handlerundefined",
                        "The MIME type handler could not be found. Please ensure that the Master Plugin is installed and plugin ID has appendix .Doc in owmimetable.xml:") + strDocumentFunction, e);
            }
        }

        return null;
    }

    /** open the given object according to MIME settings
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference
     * @param parent_p OwObject
     * @param iViewerMode_p mode as defined in VIEWER_MODE_...
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * @param mimeContext_p String context for MIME retrieval, can be null
     * 
     * @throws Exception
     * @since 3.1.0.0
     */
    public static void openObject(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, OwClientRefreshContext refreshCtx_p, String mimeContext_p) throws Exception
    {
        openObject(context_p, obj_p, parent_p, iViewerMode_p, false, refreshCtx_p, 1, context_p.getWindowPositions(), null, null, mimeContext_p);
    }

    /** open the given object according to MIME settings
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference
     * @param parent_p OwObject
     * @param iViewerMode_p mode as defined in VIEWER_MODE_...
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * 
     * @throws Exception
     */
    public static void openObject(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        openObject(context_p, obj_p, parent_p, iViewerMode_p, refreshCtx_p, null);
    }

    /** open the given object according to MIME settings
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference
     * @param parent_p OwObject
     * @param iViewerMode_p mode as defined in VIEWER_MODE_...
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * 
     * @throws Exception
     */
    public static void openObjectPreview(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        openObject(context_p, obj_p, parent_p, iViewerMode_p, true, refreshCtx_p, 1, context_p.getWindowPositions(), null, null, null);
    }

    /** open the given object according to MIME settings
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference
     * @param parent_p OwObject
     * @param iViewerMode_p mode as defined in VIEWER_MODE_...
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * @param iPage_p int page number
     * @param windowpos_p OwWindowPositions
     * @param objects_p optional Collection of objects to view in MDI mode, can be null
     * 
     * @throws Exception
     */
    public static void openObject(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, OwClientRefreshContext refreshCtx_p, int iPage_p, OwWindowPositions windowpos_p, Collection objects_p) throws Exception
    {
        openObject(context_p, obj_p, parent_p, iViewerMode_p, false, refreshCtx_p, 1, windowpos_p, objects_p, null, null);
    }

    /** open the given object according to the MIME settings, and add also the additional params to the
     *  caller URL.
     * @param context_p OwMainAppContext of the app
     * @param obj_p OwObjectReference to the object which should be opened
     * @param parent_p OwObject parent of the referenced object
     * @param iViewerMode_p mode as defined in VIEWR_MODE_...
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * @param iPage_p int page number
     * @param windowpos_p OwWindowPositions
     * @param objects_p optional Collection of objects to view in MDI mode, can be null
     * @param additionalParams_p String additional params which should be added to the URL
     * @param mimeContext_p String context for MIME retrieval, can be null
     * 
     * @throws Exception if the context can not resolve the the object reference, or MIME settings.
     */
    public static void openObject(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, OwClientRefreshContext refreshCtx_p, int iPage_p, OwWindowPositions windowpos_p, Collection objects_p,
            String additionalParams_p, String mimeContext_p) throws Exception
    {
        openObject(context_p, obj_p, parent_p, iViewerMode_p, false, refreshCtx_p, 1, windowpos_p, objects_p, additionalParams_p, mimeContext_p);
    }

    /** open the given object according to the MIME settings, and add also the additional params to the
     *  caller URL.
     * @param context_p OwMainAppContext of the app
     * @param obj_p OwObjectReference to the object which should be opened
     * @param parent_p OwObject parent of the referenced object
     * @param iViewerMode_p mode as defined in VIEWR_MODE_...
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * @param iPage_p int page number
     * @param windowpos_p OwWindowPositions
     * @param objects_p optional Collection of objects to view in MDI mode, can be null
     * @param additionalParams_p String additional params which should be added to the URL
     * 
     * @throws Exception if the context can not resolve the the object reference, or MIME settings.
     * @since 3.1.0.0
     */
    public static void openObject(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, OwClientRefreshContext refreshCtx_p, int iPage_p, OwWindowPositions windowpos_p, Collection objects_p,
            String additionalParams_p) throws Exception
    {
        openObject(context_p, obj_p, parent_p, iViewerMode_p, refreshCtx_p, iPage_p, windowpos_p, objects_p, additionalParams_p, null);
    }

    /** open the given object according to MIME settings
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference
     * @param parent_p OwObject
     * @param iViewerMode_p mode as defined in VIEWER_MODE_...
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * @param iPage_p int page number
     * @param windowpos_p OwWindowPositions
     * @param objects_p optional Collection of objects to view in MDI mode, can be null
     * 
     * @throws Exception
     */
    public static void openObjectPreview(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, OwClientRefreshContext refreshCtx_p, int iPage_p, OwWindowPositions windowpos_p, Collection objects_p)
            throws Exception
    {
        openObject(context_p, obj_p, parent_p, iViewerMode_p, true, refreshCtx_p, 1, context_p.getWindowPositions(), objects_p, null, null);
    }

    /** open the given object according to MIME settings
     * 
     * @param context_p OwMainAppContext
     * @param obj_p OwObjectReference
     * @param parent_p OwObject
     * @param iViewerMode_p mode as defined in VIEWER_MODE_...
     * @param fPreviewMode_p boolean true = try to open object in a fast preview mode (i.e. do no fancy scripting or open integrated), false = open exactly as defined in the mimetable 
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     * @param iPage_p int page number
     * @param windowpos_p OwWindowPositions
     * @param objects_p optional Collection of objects to view in MDI mode, can be null
     * @param additionalParams_p String which contains additional params for views 
     * @param mimeContext_p String context for MIME retrieval, can be null
     * 
     * @throws Exception
     */
    private static void openObject(OwMainAppContext context_p, OwObjectReference obj_p, OwObject parent_p, int iViewerMode_p, boolean fPreviewMode_p, OwClientRefreshContext refreshCtx_p, int iPage_p, OwWindowPositions windowpos_p,
            Collection objects_p, String additionalParams_p, String mimeContext_p) throws Exception
    {
        // === object with content to view
        if (OwMimeManager.isObjectDownloadable(context_p, obj_p, mimeContext_p))
        {
            // save document references in session so MDI viewer can browse through the documents
            context_p.getHttpSession().setAttribute(MDI_OBJECTS_COLLECTION_ATTRIBUTE_NAME, objects_p);

            OwOpenCommand scmd = getOpenCommand(context_p, obj_p, mimeContext_p, additionalParams_p);

            context_p.addFinalScript("\n" + scmd.getScript(iViewerMode_p, windowpos_p, fPreviewMode_p));

            return;
        }

        // === object that has a document function attached to it
        OwDocumentFunction docfunction = getHandlerDocumentPlugin(context_p, obj_p, mimeContext_p);
        if (null != docfunction)
        {
            OwObject obj = obj_p.getInstance();//OwEcmUtil.resolveObjectReference(context_p,obj_p);

            if (docfunction.isEnabled(obj, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                docfunction.onClickEvent(obj, parent_p, refreshCtx_p);
            }
            else
            {
                throw new OwInvalidOperationException(context_p.localize("app.OwMimeManager.docfuncdisabled", "The function is not available for this object."));
            }

            return;
        }

        // === object that has a master plugin attached to it
        OwMasterDocument masterplugin = getHandlerMasterPlugin(context_p, obj_p, mimeContext_p);
        if (null != masterplugin)
        {
            // open with plugin
            delegateToMasterPlugin(context_p, masterplugin, obj_p, null);
            return;
        }
    }

    /**
     * Creates an OwOpenCommand handling the <code>&lt;editservlet&gt;</code> tag from MIME node, if it exist or
     * else an OwInvalidOperationException is thrown.
     * 
     * @param context_p OwMainAppContext current context to use
     * @param obj_p OwObjectReference which should be open over edit servlet
     * @return OwOpenCommand with the information to create the link/JavaScript 
     * @throws Exception
     * @since 2.5.2.0
     */
    public static OwOpenCommand editObject(OwMainAppContext context_p, OwObjectReference obj_p) throws Exception
    {
        return editObject(context_p, obj_p, null, 1);
    }

    /**
     * Creates an OwOpenCommand handling the <code>&lt;editservlet&gt;</code> tag from MIME node, if it exist or
     * else an OwInvalidOperationException is thrown.
     * 
     * @param context_p OwMainAppContext current context to use
     * @param obj_p OwObjectReference which should be open over edit servlet
     * @param page_p int  load content of the <b>obj_p</b>
     * @return OwOpenCommand with the information to create the link/JavaScript
     * @throws Exception
     * @since 2.5.2.0
     */
    public static OwOpenCommand editObject(OwMainAppContext context_p, OwObjectReference obj_p, int page_p) throws Exception
    {
        return editObject(context_p, obj_p, null, page_p);
    }

    /**
     * Creates an OwOpenCommand handling the <code>&lt;editservlet&gt;</code> tag from MIME node, if it exist or
     * else an OwInvalidOperationException is thrown.
     * 
     * @param context_p OwMainAppContext current context to use
     * @param obj_p OwObjectReference which should be open over edit servlet
     * @param additionalParams_p String parameter which should be added to the created URL
     * @param page_p int  load content of the <b>obj_p</b>
     * @return OwOpenCommand with the information to create the link/JavaScript
     * @throws Exception
     * @since 2.5.2.0
     */
    public static OwOpenCommand editObject(OwMainAppContext context_p, OwObjectReference obj_p, String additionalParams_p, int page_p) throws Exception
    {
        return new OwOpenCommand(context_p, obj_p, null, additionalParams_p, page_p, Boolean.TRUE.booleanValue());
    }

    /**
     * Creates an OwOpenCommand handling the <code>&lt;editservlet&gt;</code> tag from MIME node, if it exist or
     * else an OwInvalidOperationException is thrown.
     * 
     * @param context_p OwMainAppContext current context to use
     * @param obj_p OwObjectReference which should be open over edit servlet
     * @param mimeContext_p context used to determine the object MIME configuration
     *                     (see {@link #getMimeNode(OwConfiguration, OwObjectReference, String)}
     * @param additionalParams_p String parameter which should be added to the created URL
     * @param page_p int  load content of the <b>obj_p</b>
     * @return OwOpenCommand with the information to create the link/JavaScript
     * @throws Exception
     * @since 3.1.0.0
     */
    public static OwOpenCommand editObject(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p, String additionalParams_p, int page_p) throws Exception
    {
        return new OwOpenCommand(context_p, obj_p, mimeContext_p, additionalParams_p, page_p, Boolean.TRUE.booleanValue());
    }

    /** open the given file according to MIME settings for preview
     * 
     * @param context_p OwMainAppContext
     * @param mimeType_p the MIME type of the preview file
     * @param path_p absolute path to file on server to preview
     * 
     * @throws Exception
     */
    public static void openFilePreview(OwMainAppContext context_p, String mimeType_p, String path_p) throws Exception
    {
        openFilePreview(context_p, path_p, mimeType_p, null, 1);
    }

    /** open the given file according to the MIME settings for preview,
     * Changes the file name to the given parameter <b>fileName_p</b>.
     * 
     * @param context_p OwMainAppContext to use for requesting MIME-settings
     * @param mimeType_p String MIME type of the preview file
     * @param path_p String representing <b>full path</b> of the preview file
     * @param fileName_p String can be null, name is then extracted from <b>path_p</b>
     * @throws Exception
     * 
     * @since 2.5.2.0
     */
    public static void openFilePreview(OwMainAppContext context_p, String mimeType_p, String path_p, String fileName_p) throws Exception
    {
        openFilePreview(context_p, path_p, mimeType_p, fileName_p, 1);
    }

    /** Main openFilePreview method, where also the path, file name and MIME type is
     * stored in the Session using keys:
     * <p>{@link #FILE_PREVIEW_ATTRIBUTE_FILENAME} for file name<br />
     * {@link #FILE_PREVIEW_ATTRIBUTE_MIMETYPE} for MIME type<br />
     * {@link #FILE_PREVIEW_ATTRIBUTE_NAME} for full path<br />
     * </p>
     * @param context_p OwMainAppContext to use for requesting MIME-settings
     * @param path_p String representing <b>full path</b> of the preview file
     * @param mimeType_p String MIME type of the preview file
     * @param fileName_p String can be null, name is then extracted from <b>path_p</b>
     * @param page_p int <i>defined for future use</i>
     * @throws Exception
     * @since 2.5.2.0
     */
    private static void openFilePreview(OwMainAppContext context_p, String path_p, String mimeType_p, String fileName_p, int page_p) throws Exception
    {
        // create OwFileObject
        OwFileObject obj;
        String sUrlPattern = null;
        if (fileName_p == null)
        {
            obj = new OwFileObject(context_p.getNetwork(), new File(path_p));
        }
        else
        {
            obj = new OwFileObject(context_p.getNetwork(), new File(path_p), fileName_p, mimeType_p);
        }

        // get MIME node
        OwXMLUtil mimeNode = context_p.getConfiguration().getMIMENode(mimeType_p);

        if (mimeNode != null)
        {
            // use viewservlet as urlpattern
            sUrlPattern = mimeNode.getSafeTextValue(MIME_VIEWERSERLVET, null);

            // get viewermode
            int iViewerMode = mimeNode.getSafeIntegerAttributeValue(MIME_ATT_AUTOVIEWERMODE, VIEWER_MODE_SIMPLE);

            // use download URL pattern if no viewer URL pattern is supplied or if the viewer URL pattern is JavaScript
            if ((iViewerMode == OwMimeManager.VIEWER_MODE_JAVASCRIPT) || (null == sUrlPattern))
            {
                sUrlPattern = mimeNode.getSafeTextValue(MIME_DOWNLOAD_URL, null);
            }

        }

        // generate download URL for replacement
        StringBuilder sbDownloadUrl = new StringBuilder();
        sbDownloadUrl.append(context_p.getBaseURL());
        sbDownloadUrl.append("/getContent?");
        sbDownloadUrl.append(DMSID_KEY);
        sbDownloadUrl.append("=");
        sbDownloadUrl.append(OwAppContext.encodeURL(FILE_PREVIEW_DMSID));
        sbDownloadUrl.append("&");
        sbDownloadUrl.append(CONTENT_TYPE_KEY);
        sbDownloadUrl.append("=");
        sbDownloadUrl.append(OwContentCollection.CONTENT_TYPE_DOCUMENT);
        sbDownloadUrl.append("&");
        sbDownloadUrl.append(PAGE_KEY);
        sbDownloadUrl.append("=");
        sbDownloadUrl.append(page_p);
        // append some FAKE part to make the URL unique and force IE to really load the document
        sbDownloadUrl.append("&forceload=");
        sbDownloadUrl.append(RANDOM.nextInt());

        // get URL from pattern or use default download URL 
        String sURL;
        if (sUrlPattern != null)
        {
            /*ATTENTION: Here we have an OwFileObject which don't support obj.getDMSID(), 
             * so replace the Placeholder before calling getServletReplacedTokenString*/
            sURL = OwString.replaceAll(sUrlPattern, VIEWER_SERVLET_REPLACE_TOKEN_DMSID, OwAppContext.encodeURL(FILE_PREVIEW_DMSID));

            /*ATTENTION: The file is uploaded to the temp directory of server, so that here 
             * the download URL placeholder should be replaced, else the DMSID will be used
             * and an OwNotSupportedException is thrown*/
            sURL = OwString.replaceAll(sURL, VIEWER_SERVLET_REPLACE_TOKEN_DOWNLOAD_URL, sbDownloadUrl.toString());

            sURL = getServletReplacedTokenString(sURL, context_p, obj, mimeNode, page_p, OwContentCollection.CONTENT_TYPE_DOCUMENT);

        }
        else
        {
            // nothing else here. use the normal download URL 
            sURL = sbDownloadUrl.toString();
        }

        // save full path in session attribute
        context_p.getHttpSession().setAttribute(FILE_PREVIEW_ATTRIBUTE_NAME, path_p);
        context_p.getHttpSession().setAttribute(FILE_PREVIEW_ATTRIBUTE_FILENAME, obj.getName());
        context_p.getHttpSession().setAttribute(FILE_PREVIEW_ATTRIBUTE_MIMETYPE, obj.getMIMEType());

        // add viewer open script
        context_p.addFinalScript("\n" + getAutoViewerScript(context_p, sURL, VIEWER_MODE_DEFAULT, FILE_PREVIEW_DMSID, FILE_PREVIEW_DMSID, page_p, context_p.getWindowPositions()));
    }

    /** check if given object can be downloaded to hard drive
    *
    * @param context_p OwMainAppContext
    * @param obj_p OwObjectReference
    *
    * @return true = object can be downloaded, false = object has no content or can not be downloaded
    */
    public static boolean isObjectDownloadable(OwMainAppContext context_p, OwObjectReference obj_p) throws Exception
    {
        return isObjectDownloadable(context_p, obj_p, null);
    }

    /** check if given object can be downloaded to hard drive
    *
    * @param context_p OwMainAppContext
    * @param obj_p OwObjectReference
    * @param mimeContext_p optional context for MIME node resolution. Can be <code>null</code>.
    * @return true = object can be downloaded, false = object has no content or can not be downloaded
    * @since 3.1.0.0
    */
    public static boolean isObjectDownloadable(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p) throws Exception
    {
        // determine MIME description node from XML mimetable
        OwXMLUtil mimeNode = getMimeNode(context_p.getConfiguration(), obj_p, mimeContext_p);

        // compute download link flag, true = if content and download allowed
        return (obj_p.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL) && (mimeNode.getSafeTextValue(MIME_VIEWERSERLVET, null) != null));
    }

    /** get a link to save/download the given OwObjectReference to the local hard drive
    *
    * @param context_p OwMainAppContext
    * @param strDisplayName_p String link name
    * @param obj_p OwObjectReference
    * 
    * @return String href link
    */
    public static String getDownloadLink(OwMainAppContext context_p, String strDisplayName_p, OwObjectReference obj_p) throws Exception
    {
        // determine MIME description node from XML mimetable
        OwXMLUtil mimeNode = getMimeNode(context_p.getConfiguration(), obj_p);

        // === create download link
        StringBuilder ret = new StringBuilder();

        ret.append("<a title='");

        ret.append(context_p.localize("app.OwMimeManager.savecopytooltip", "Download"));

        ret.append("' href=\"");
        ret.append(getDownloadURL(context_p, obj_p, OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, mimeNode));
        ret.append("&");
        ret.append(DWL_MODE_KEY);
        ret.append("=");
        ret.append(String.valueOf(DWL_MODE_SAVE_COPY));

        ret.append("\" class='");
        ret.append("OwMimeItem");
        ret.append("'>");
        ret.append(strDisplayName_p);
        ret.append("</a>");

        return ret.toString();
    }

    /** get a URL to save / download the given OwObjectReference to the local hard drive
    *
    * @param context_p OwMainAppContext
    * @param obj_p OwObjectReference
    * 
    * @return String href link
    */
    public static String getSaveDownloadURL(OwMainAppContext context_p, OwObjectReference obj_p) throws Exception
    {
        // determine MIME description node from XML mimetable
        OwXMLUtil mimeNode = getMimeNode(context_p.getConfiguration(), obj_p);

        // === create download link
        StringBuilder ret = new StringBuilder();

        ret.append(getDownloadURL(context_p, obj_p, OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, mimeNode));
        ret.append("&");
        ret.append(DWL_MODE_KEY);
        ret.append("=");
        ret.append(String.valueOf(DWL_MODE_SAVE_COPY));

        return ret.toString();
    }

    /** create a download URL where the content of the specified object, contenttype and page can be found
    *
    * @param context_p OwMainAppContext since method is static
    * @param obj_p OwObjectReference to retrieve the URL 
    * @param iContentType_p int requested content type as specified in OwContentCollection
    * @param iPage_p int requested page number
    *
    * @return String download URL for content
    */
    public static String getDownloadURL(OwMainAppContext context_p, OwObjectReference obj_p, int iContentType_p, int iPage_p) throws Exception
    {
        OwXMLUtil MimeNode = getMimeNode(context_p.getConfiguration(), obj_p);
        return getDownloadURL(context_p, obj_p, iContentType_p, iPage_p, MimeNode);
    }

    /** Create URL or script command to open the object according to MIME settings.
    * The first content page is selected by default.
    * @param context_p OwMainAppContext
    * @param obj_p OwObjectReference
    */
    public static OwOpenCommand getOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p) throws Exception
    {
        return getOpenCommand(context_p, obj_p, null);
    }

    /**
     * Create an URL or script to open the object according to MIME settings.
     * The first content page is selected by default.
     * @param context_p OwMainAppContext to handle/request MIME settings
     * @param obj_p OwObjectRefernce which should be opened
     * @param additionalParams_p String representing additional parameter, can be <b>null</b>
     * @return OwOpenCommand
     * @throws Exception if the object reference is null, if the MIME handling definition is wrong
     */
    public static OwOpenCommand getOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String additionalParams_p) throws Exception
    {
        return getOpenCommand(context_p, obj_p, null, additionalParams_p);
    }

    /**
     * Create an URL or script to open the object according to MIME settings.
     * The first content page is selected by default.
     * @param context_p OwMainAppContext to handle/request MIME settings
     * @param obj_p OwObjectRefernce which should be opened
     * @param mimeContext_p context used to determine the object MIME configuration
     *                     (see {@link #getMimeNode(OwConfiguration, OwObjectReference, String)}
     * @param additionalParams_p String representing additional parameter, can be <b>null</b>
     * @return OwOpenCommand
     * @throws Exception if the object reference is null, if the MIME handling definition is wrong
     * @since 3.1.0.0
     */
    public static OwOpenCommand getOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p, String additionalParams_p) throws Exception
    {
        return getOpenCommand(context_p, obj_p, mimeContext_p, additionalParams_p, 1);
    }

    /**
     * Create an URL or script to open the referenced object, according to the defined MIME settings.
     * @param context_p OwmainAppCotnext to handle/request MIME settings
     * @param obj_p OwObjectReference which should be opened
     * @param additionalParams_p
     * @param page_p
     * @return OwOpenCommand
     * @throws Exception if the object reference is null, if the MIME handling definition is wrong
     */
    public static OwOpenCommand getOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String additionalParams_p, int page_p) throws Exception
    {
        return getOpenCommand(context_p, obj_p, null, additionalParams_p, page_p);
    }

    /**
     * Create an URL or script to open the referenced object, according to the defined MIME settings.
     * @param context_p OwmainAppCotnext to handle/request MIME settings
     * @param obj_p OwObjectReference which should be opened
     * @param mimeContext_p context used to determine the object MIME configuration
     *                       (see {@link #getMimeNode(OwConfiguration, OwObjectReference, String)}
     * @param additionalParams_p
     * @param page_p
     * @return OwOpenCommand
     * @throws Exception if the object reference is null, if the MIME handling definition is wrong
     */

    public static OwOpenCommand getOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p, String additionalParams_p, int page_p) throws Exception
    {
        return new OwOpenCommand(context_p, obj_p, mimeContext_p, additionalParams_p, page_p);
    }

    /** Create URL or script command to open the object according to MIME settings.
    * The first content page is selected by default.
    * @param obj_p OwObjectReference
    */
    public OwOpenCommand getOpenCommand(OwObjectReference obj_p) throws Exception
    {
        return new OwOpenCommand(this, obj_p, null);
    }

    /**
     * Create an URL or script using current instance of OwMimeManager.
     * The first content page is selected by default.
     * @param obj_p OwObjectReference the object reference which should be handled
     * @param additionalParams_p String additional parameters which should be added, can be null
     * @return OwOpenCommand containing the created URL or script
     * @throws Exception
     */
    public OwOpenCommand getOpenCommand(OwObjectReference obj_p, String additionalParams_p) throws Exception
    {
        return new OwOpenCommand(this, obj_p, additionalParams_p);
    }

    /**
     * Create an URL or script using current instance of OwMimeManager.
     * @param obj_p OwObjectReference the object reference which should be handled
     * @param additionalParams_p String additional parameters which should be added, can be null
     * @param page_p integer representing the value of page to open
     * @return OwOpenCommand containing the created URL or script
     * @throws Exception
     */
    public OwOpenCommand getOpenCommand(OwObjectReference obj_p, String additionalParams_p, int page_p) throws Exception
    {
        return new OwOpenCommand(this, obj_p, additionalParams_p, page_p);
    }

    /** register a object to find it later in the events
     * 
     * @param obj_p OwObjectReference
     */
    protected void registerObject(OwObjectReference obj_p)
    {
        // add to map for later reference in onOpen
        // NOTE: Do not forget to clear the map, otherwise it will increase to infinite
        m_ObjectMap.put(String.valueOf(obj_p.hashCode()), obj_p);
    }

    /** create a script that closes the autoviewer and rearrange the browser.
     *  the script can be applied with the addFinalScript() method.
     * 
     * @return String JavaScript
     * */
    public static String createAutoViewerRestoreMainWindowScript(OwMainAppContext context_p, int viewermode_p)
    {
        return "window.resizeTo(screen.availWidth,screen.availHeight);window.moveTo(0,0);";
    }

    /** Make sure ID does not consist of invalid characters for java script window name
     * 
     * @param id_p
     * @return a {@link String}
     */
    public static String makeViewerTitleString(String id_p)
    {
        // Make sure ID does not consist of invalid characters for java script window name
        StringBuilder buf = new StringBuilder(id_p.length());
        for (int i = 0; i < id_p.length(); i++)
        {
            char c = id_p.charAt(i);
            switch (c)
            {
                case '-':
                case '\\':
                case '/':
                case ':':
                case ' ':
                case '{':
                case '}':
                case ',':
                case '.':
                    buf.append('_');
                    break;

                default:
                    buf.append(c);
            }
        }

        return buf.toString();
    }

    private static String getViewerWindowArrangeScript(String sOpenURL_p, String sViewerName_p, String sDmsID_p, int iPage_p, OwWindowPositions windowpos_p)
    {
        StringBuilder ret = new StringBuilder();

        // saved position
        ret.append("openViewerWindowArranged('");
        ret.append(sOpenURL_p);
        ret.append("','");

        /* Attention IE6 fix, viewer name should not 
         * contain any other character than
         * [A-Z]|[a-z]|[0-9]|'_' */
        ret.append(hashViewerName(sViewerName_p));
        ret.append("','");
        ret.append(sDmsID_p);
        ret.append("',");
        ret.append(String.valueOf(iPage_p));
        ret.append(",");

        ret.append(windowpos_p.getViewerWidth());
        ret.append(",");
        ret.append(windowpos_p.getViewerHeight());
        ret.append(",");
        ret.append(windowpos_p.getViewerTopX());
        ret.append(",");
        ret.append(windowpos_p.getViewerTopY());

        ret.append(",");
        ret.append(windowpos_p.getWindowWidth());
        ret.append(",");
        ret.append(windowpos_p.getWindowHeight());
        ret.append(",");
        ret.append(windowpos_p.getWindowTopX());
        ret.append(",");
        ret.append(windowpos_p.getWindowTopY());
        ret.append(",");

        ret.append(windowpos_p.getPositionMainWindow());
        ret.append(",");

        ret.append(windowpos_p.getUnits());

        ret.append(");");

        return ret.toString();
    }

    /** create a call script for opening a viewer
     * 
     * @param sOpenURL_p String URL to open
     * @param sViewerName_p name of the viewer to use
     * @param sDmsID_p String DMSID of document to open
     * @param iPage_p int page to open
     * 
     * @return String JavaScript function call statement
     */
    private static String getViewerWindowScript(String sOpenURL_p, String sViewerName_p, String sDmsID_p, int iPage_p)
    {
        StringBuilder ret = new StringBuilder();

        ret.append("openViewerWindow('");
        ret.append(sOpenURL_p);
        ret.append("','");
        /* Attention IE fix, viewer name should not 
         * contain any other character than
         * [A-Z]|[a-z]|[0-9]|'_' */
        ret.append(hashViewerName(sViewerName_p));
        ret.append("','");
        ret.append(sDmsID_p);
        ret.append("',");
        ret.append(String.valueOf(iPage_p));
        ret.append(");");

        return ret.toString();
    }

    /** create a script that opens the URL in a viewer and automatically arranges the browser and the viewer
     *  the script can be applied with the addFinalScript() method.
     *  
     * @param  strOpenURL_p String URL to view
     * @param viewermode_p int mode as defined in OwMimeManager.VIEWER_MODE_...
     * @param id_p String unique ID of document to distinguish viewer instances
     * @param sDmsID_p String DMSID of document to open
     * @param iPage_p int page to open
     * 
     * @return String JavaScript
     * */
    public static String getAutoViewerScript(OwMainAppContext context_p, String strOpenURL_p, int viewermode_p, String id_p, String sDmsID_p, int iPage_p)
    {
        switch (viewermode_p)
        {
            default:
            case VIEWER_MODE_SINGLE:
            case VIEWER_MODE_DEFAULT:
                return getViewerWindowArrangeScript(strOpenURL_p, "Viewer", sDmsID_p, iPage_p, context_p.getWindowPositions());

            case VIEWER_MODE_COMPARE:
                return getViewerWindowScript(strOpenURL_p, makeViewerTitleString(id_p), sDmsID_p, iPage_p);

            case VIEWER_MODE_MULTI:
                return getViewerWindowArrangeScript(strOpenURL_p, makeViewerTitleString(id_p), sDmsID_p, iPage_p, context_p.getWindowPositions());

            case VIEWER_MODE_JAVASCRIPT:
                return strOpenURL_p.endsWith(";") ? strOpenURL_p : strOpenURL_p + ";";
        }
    }

    /** create a script that opens the URL in a viewer and automatically arranges the browser and the viewer
     *  the script can be applied with the addFinalScript() method.
     *  
     * @param  strOpenURL_p String URL to view
     * @param viewermode_p int mode as defined in OwMimeManager.VIEWER_MODE_...
     * @param id_p String unique ID of document to distinguish viewer instances
     * @param sDmsID_p String DMSID of document to open
     * @param iPage_p int page to open
     * 
     * @return String JavaScript
     * */
    public static String getAutoViewerScript(OwMainAppContext context_p, String strOpenURL_p, int viewermode_p, String id_p, String sDmsID_p, int iPage_p, OwWindowPositions windowpos_p)
    {
        switch (viewermode_p)
        {
            default:
            case VIEWER_MODE_SINGLE:
            case VIEWER_MODE_DEFAULT:
                return getViewerWindowArrangeScript(strOpenURL_p, "Viewer", sDmsID_p, iPage_p, windowpos_p);

            case VIEWER_MODE_COMPARE:
                return getViewerWindowScript(strOpenURL_p, makeViewerTitleString(id_p), sDmsID_p, iPage_p);

            case VIEWER_MODE_MULTI:
                return getViewerWindowArrangeScript(strOpenURL_p, makeViewerTitleString(id_p), sDmsID_p, iPage_p, windowpos_p);

            case VIEWER_MODE_JAVASCRIPT:
                return strOpenURL_p;
        }
    }

    /**
     * Calls the {@link #getServletReplacedTokenString(String, OwMainAppContext, OwObjectReference, OwXMLUtil, int, int)} method
     * with following parameters
     * <p>
     * <code>getServletReplacedTokenString(servletURL, currentContext, obj, mimeNode, page, Integer.<i>MIN_VALUE</i>)</code>
     * </p>
     * @param servletURL_p String URL with placeholders to replace
     * @param currentContext_p OwMainAppContext which is currently used
     * @param obj_p OwObjectReference to use for replacement
     * @param mimeNode_p OwXMLUtil XML node from owmimetable
     * @param page_p int page number to use for replacing {@link #VIEWER_SERVLET_REPLACE_TOKEN_PAGE} placeholder
     * @return String where the placeholders are replaced with the values given as parameters
     * @throws UnsupportedEncodingException if URL encoding of values fails
     * @throws Exception
     * @see #getServletReplacedTokenString(String, OwMainAppContext, OwObjectReference, OwXMLUtil, int, int)
     * @since 2.5.2.0
     */
    protected static String getServletReplacedTokenString(String servletURL_p, OwMainAppContext currentContext_p, OwObjectReference obj_p, OwXMLUtil mimeNode_p, int page_p) throws UnsupportedEncodingException, Exception
    {
        return getServletReplacedTokenString(servletURL_p, currentContext_p, obj_p, mimeNode_p, page_p, Integer.MIN_VALUE);
    }

    /**
     * This method is a Helper and replace the most used placeholder in a URL string.
     * Also the String is processed by replacement scanners for<ul><li>page count,</li><li>download URL</li><li>and properties</li></ul>
     * The servletURL String is explored of containment for a placeholder, before replacing these with a value. 
     * 
     * <p><b>ATTENTION</b>: This method calls <code><b>obj</b>.getDMSID()</code> and 
     * many other methods, if this can lead to an exception replace the
     * placeholder in <b>servletURL</b> before calling this method. 
     * </p>
     * @param servletURL_p String URL with tokens that should be replaced
     * @param currentContext_p OwMainAppContext current context to use for replacing
     * @param obj_p OwObjectReference object to use for replacement of DMSID and properties placeholders
     * @param mimeNode_p OwXMLUtil XML MIME node from owmimetable
     * @param page_p int page number to replace {@link #VIEWER_SERVLET_REPLACE_TOKEN_PAGE}
     * @param contentType_p int should be some of OwContentCollection.CONTENT_TYPE_... if equals Integer.MIN_VALUE it is ignored
     * @return String where the placeholders are replaced by the values
     * @throws UnsupportedEncodingException if values cannot be encoded to a URL
     * @throws Exception
     * @since 2.5.2.0
     */
    protected static String getServletReplacedTokenString(String servletURL_p, OwMainAppContext currentContext_p, OwObjectReference obj_p, OwXMLUtil mimeNode_p, int page_p, int contentType_p) throws UnsupportedEncodingException, Exception
    {
        StringBuilder retVal = new StringBuilder(servletURL_p);
        OwString.replaceAll(retVal, VIEWER_SERVLET_REPLACE_TOKEN_BASEURL, currentContext_p.getBaseURL() + "/");

        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_SERVERURL) >= 0)
        {
            OwString.replaceAll(retVal, VIEWER_SERVLET_REPLACE_TOKEN_SERVERURL, currentContext_p.getServerURL() + "/");
        }

        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_DMSID) >= 0)
        {
            OwString.replaceAll(retVal, VIEWER_SERVLET_REPLACE_TOKEN_DMSID, OwAppContext.encodeURL(obj_p.getDMSID()));
        }

        if (retVal.indexOf(REPLACE_TOKEN_VIID) >= 0)
        {
            OwVIIdFactory factory = currentContext_p.getRegisteredInterface(OwVIIdFactory.class);
            if (factory != null)
            {
                OwVIId viid = factory.createVersionIndependentId(obj_p.getInstance());
                OwString.replaceAll(retVal, REPLACE_TOKEN_VIID, OwAppContext.encodeURL(viid.getViidAsString()));
            }
            else
            {
                LOG.warn("OwMimeManager: No OwVIId factory registered, cannot replace token in MIME definition");
            }
        }

        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PAGE) >= 0)
        {
            OwString.replaceAll(retVal, VIEWER_SERVLET_REPLACE_TOKEN_PAGE, Integer.toString(page_p));
        }

        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_SECURITYTOKEN) >= 0)
        {
            OwString.replaceAll(retVal, VIEWER_SERVLET_REPLACE_TOKEN_SECURITYTOKEN, currentContext_p.getNetwork().getCredentials().getSecurityToken(null));
        }

        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_SECURITYTOKEN_ENC) >= 0)
        {
            OwString.replaceAll(retVal, VIEWER_SERVLET_REPLACE_TOKEN_SECURITYTOKEN_ENC, OwAppContext.encodeURL(currentContext_p.getNetwork().getCredentials().getSecurityToken(null)));
        }

        //strURL = OwString.replaceAll(strURL,VIEWER_SERVLET_REPLACE_TOKEN_PAGE_COUNT,String.valueOf(obj_p.getPageCount()));
        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PAGE_COUNT) >= 0)
        {
            retVal = new StringBuilder(PAGECOUNT_REPLACE_SCANNER.replace(currentContext_p, obj_p, retVal.toString(), VIEWER_SERVLET_REPLACE_TOKEN_PAGE_COUNT, mimeNode_p));
        }

        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_DOWNLOAD_URL) >= 0)
        {
            retVal = new StringBuilder(URL_REPLACE_SCANNER.replace(currentContext_p, obj_p, retVal.toString(), VIEWER_SERVLET_REPLACE_TOKEN_DOWNLOAD_URL, mimeNode_p));
        }

        if (retVal.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_CONTENT_TYPE) >= 0 && contentType_p != Integer.MIN_VALUE)
        {
            OwString.replaceAll(retVal, VIEWER_SERVLET_REPLACE_TOKEN_CONTENT_TYPE, String.valueOf(contentType_p));
        }

        //replace Properties defined in the URL, with the dependent values from object
        return replaceProperties(currentContext_p, retVal.toString(), obj_p);
    }

    /**
     * This function is implemented due to problems of
     * the JavaScript function<br />
     * <code> window.open(URL, name[, param])</code><br />
     * in IE 6, where the name parameter can only be
     * a String containing alphanumeric[A-Z]|[a-z],[0-9] or 
     * _ &quot;underscore&quot; character.
     * <p>
     * ATTENTION: if <code>viewerName.hashCode() < 0</code>
     * the minus sign is replaced through an _ &quot;underscore&quot;. 
     * </p>
     * @param viewerName_p String which should be hashed
     * @return String with the hashed Value or null if <b>viewerName</b> is null.
     */
    private static String hashViewerName(String viewerName_p)
    {
        if (viewerName_p != null)
        {
            int hash = viewerName_p.hashCode();
            String hashedName = Integer.toString(hash);
            if (hash < 0)
            {
                hashedName = hashedName.replace('-', '_');
            }

            return hashedName;
        }
        else
        {
            return null;
        }
    }

    /**
     *<p>
     * Class to perform search and replace on strings.<br/>
     * Derive a subclass and implement getReplacement. 
     * getReplacement is only called if pattern was found.
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
    protected abstract static class OwReplaceScanner
    {
        /** replace a string with the token from the overridden getReplacement method
         * @param strIn_p String string to search and replace
         * @param strPattern_p String to search
         * @param mimeNode_p OwXMLUtil MIME node
         * 
         * @return String
         * */
        public String replace(OwMainAppContext context_p, OwObjectReference obj_p, String strIn_p, String strPattern_p, OwXMLUtil mimeNode_p) throws Exception
        {
            int iIndex = strIn_p.indexOf(strPattern_p);

            if (iIndex == -1)
            {
                return strIn_p;
            }

            StringBuilder strRet = new StringBuilder();

            int iOldIndex = 0;
            while (-1 != iIndex)
            {
                strRet.append(strIn_p.substring(iOldIndex, iIndex));
                strRet.append(getReplacement(context_p, obj_p, mimeNode_p));

                iIndex += strPattern_p.length();

                iOldIndex = iIndex;

                iIndex = strIn_p.indexOf(strPattern_p, iIndex);
            }

            if (iOldIndex <= strIn_p.length())
            {
                strRet.append(strIn_p.substring(iOldIndex, strIn_p.length()));
            }

            return strRet.toString();
        }

        /** override with your replacement function */
        protected abstract String getReplacement(OwMainAppContext context_p, OwObjectReference obj_p, OwXMLUtil mimeNode_p) throws Exception;
    }

    /**
     *<p>
     * High performance replacement for download URL. Download URL is only computed if placeholder was found.
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
    private static class OwDownloadURLReplaceScanner extends OwReplaceScanner
    {
        protected String getReplacement(OwMainAppContext context_p, OwObjectReference obj_p, OwXMLUtil mimeNode_p) throws Exception
        {
            return getDownloadURL(context_p, obj_p, OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, mimeNode_p);
        }

    }

    /**
     *<p>
     * High performance replacement for download URL. Download URL is only computed if placeholder was found.
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
    private static class OwPageCountReplaceScanner extends OwReplaceScanner
    {
        protected String getReplacement(OwMainAppContext context_p, OwObjectReference obj_p, OwXMLUtil mimeNode_p) throws Exception
        {
            return String.valueOf(obj_p.getPageCount());
        }

    }

    /**
     *<p>
     * Identifies a open command URL or script.
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
    public static class OwOpenCommand
    {
        private String m_sCommand;

        private int m_iViewerMode;

        private OwMainAppContext m_context;

        private OwObjectReference m_obj;

        private boolean m_fHandler = false;

        public OwOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String additionalParams_p) throws Exception
        {
            this(context_p, obj_p, additionalParams_p, 1);
        }

        public OwOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String additionalParams_p, int page_p) throws Exception
        {
            this(context_p, obj_p, null, additionalParams_p, page_p, Boolean.FALSE.booleanValue());
        }

        public OwOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p, String additionalParams_p, int page_p) throws Exception
        {
            this(context_p, obj_p, mimeContext_p, additionalParams_p, page_p, Boolean.FALSE.booleanValue());
        }

        public OwOpenCommand(OwMainAppContext context_p, OwObjectReference obj_p, String mimeContext_p, String additionalParams_p, int page_p, boolean handleEditTag_p) throws Exception
        {
            m_context = context_p;
            m_obj = obj_p;

            // determine MIME description node from XML MIMEtable
            OwXMLUtil mimeNode = getMimeNode(context_p.getConfiguration(), obj_p, mimeContext_p);
            String strViewServlet = mimeNode.getSafeTextValue(handleEditTag_p ? MIME_EDITSERLVET : MIME_VIEWERSERLVET, null);

            if (strViewServlet != null)
            {
                m_iViewerMode = mimeNode.getSafeIntegerAttributeValue(MIME_ATT_AUTOVIEWERMODE, VIEWER_MODE_SIMPLE);
                if (handleEditTag_p)
                {
                    OwXMLUtil edit = new OwStandardXMLUtil(mimeNode.getSubNode(MIME_EDITSERLVET));
                    m_iViewerMode = edit.getSafeIntegerAttributeValue(MIME_ATT_AUTOVIEWERMODE, m_iViewerMode);
                }
                m_sCommand = createURL(obj_p, strViewServlet, mimeNode, additionalParams_p, page_p);
            }
            else
            {
                throw new OwInvalidOperationException("OwMimeManager.OwOpenCommand: ...Is not a servlet.");
            }
        }

        public OwOpenCommand(OwMimeManager mimemanager_p, OwObjectReference obj_p, String additionalParams_p) throws Exception
        {
            this(mimemanager_p, obj_p, additionalParams_p, 1);
        }

        public OwOpenCommand(OwMimeManager mimemanager_p, OwObjectReference obj_p, String additionalParams_p, int page_p) throws Exception
        {
            this(mimemanager_p, obj_p, additionalParams_p, page_p, Boolean.FALSE.booleanValue());
        }

        public OwOpenCommand(OwMimeManager mimemanager_p, OwObjectReference obj_p, String additionalParams_p, int page_p, boolean handleEditTag_p) throws Exception
        {
            m_context = (OwMainAppContext) mimemanager_p.getContext();
            m_obj = obj_p;

            // determine MIME description node from XML MIMEtable
            OwXMLUtil mimeNode = getMimeNode(m_context.getConfiguration(), obj_p);
            String strViewServlet = mimeNode.getSafeTextValue(handleEditTag_p ? MIME_EDITSERLVET : MIME_VIEWERSERLVET, null);

            if (strViewServlet != null)
            {
                m_iViewerMode = mimeNode.getSafeIntegerAttributeValue(MIME_ATT_AUTOVIEWERMODE, VIEWER_MODE_SIMPLE);

                if (handleEditTag_p)
                {
                    OwXMLUtil edit = new OwStandardXMLUtil(mimeNode.getSubNode(MIME_EDITSERLVET));
                    m_iViewerMode = edit.getSafeIntegerAttributeValue(MIME_ATT_AUTOVIEWERMODE, m_iViewerMode);
                }

                m_sCommand = createURL(obj_p, strViewServlet, mimeNode, additionalParams_p, page_p);

                return;
            }

            m_fHandler = true;

            // === open command is not a URL but a event handler
            mimemanager_p.registerObject(obj_p);

            // === pluginevent handler (dispatches a event to the specific plugin)
            String strEventHandler = mimeNode.getSafeTextValue(MIME_EVENTHANDLER, null);
            if (strEventHandler != null)
            {
                // === dispatch event to plugin
                m_sCommand = mimemanager_p.createLinkUrl("PluginEventHandler", obj_p, null);

                return;
            }

            // === document function handler (creates the specified document function link)
            String strDocumentFunction = mimeNode.getSafeTextValue(MIME_DOCUMENTFUNCTION, null);
            if (strDocumentFunction != null)
            {
                // === create document function link
                m_sCommand = mimemanager_p.createLinkUrl("DocumentFunctionHandler", obj_p, null);

                return;
            }
        }

        private String createURL(OwObjectReference obj_p, String strViewServlet_p, OwXMLUtil mimeNode_p, String additionalParam_p, int page_p) throws Exception
        {
            if (additionalParam_p != null)
            {
                if (additionalParam_p.startsWith("&"))
                {
                    strViewServlet_p = strViewServlet_p + additionalParam_p;
                }
                else
                {
                    strViewServlet_p = strViewServlet_p + "&" + additionalParam_p;
                }
            }

            return getServletReplacedTokenString(strViewServlet_p, m_context, obj_p, mimeNode_p, page_p);
        }

        /** get the defined viewer mode to use
         * 
         * @return int viewer mode as defined with OwMimeManager.VIEWER_MODE_...
         */
        public int getViewerMode()
        {
            return m_iViewerMode;
        }

        /** check if a script can be generated out of the given command
         * 
         * @return true if the script can be generated false otherwise
         */
        public boolean canGetScript()
        {
            return (!m_fHandler);
        }

        /** get the pure script command if a script is defined by this command
         * 
         * @param iViewerMode_p int override viewer mode or use VIEWER_MODE_DEFAULT
         * @param windowpos_p OwWindowPositions
         * @param fPreviewMode_p boolean true = try to open object in a fast preview mode (i.e. do no fancy scripting or open integrated), false = open exactly as defined in the mimetable 
         * 
         * @return String script command, or throws exception if command is no script
         * @throws Exception 
         */
        public String getScript(int iViewerMode_p, OwWindowPositions windowpos_p, boolean fPreviewMode_p) throws Exception
        {
            if ((iViewerMode_p == VIEWER_MODE_DEFAULT) || (m_iViewerMode == VIEWER_MODE_JAVASCRIPT))
            {
                iViewerMode_p = m_iViewerMode;
            }

            String scmd = m_sCommand;

            if (fPreviewMode_p && (m_iViewerMode == VIEWER_MODE_JAVASCRIPT))
            {
                scmd = getDownloadURL(m_context, m_obj, OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);
                iViewerMode_p = VIEWER_MODE_DEFAULT;
            }

            if (windowpos_p != null)
            {
                return getAutoViewerScript(m_context, scmd, iViewerMode_p, m_obj.getDMSID(), m_obj.getDMSID(), 1, windowpos_p);
            }
            else
            {
                return getAutoViewerScript(m_context, scmd, iViewerMode_p, m_obj.getDMSID(), m_obj.getDMSID(), 1);
            }
        }

        /** check if a script can generated out of the given command
         * 
         * @return a boolean
         */
        public boolean canGetURL()
        {
            return (m_iViewerMode != VIEWER_MODE_JAVASCRIPT);
        }

        /** get the command URL if a pure URL is defined by this command 
         *  
         * @return String URL 
         * @throws OwInvalidOperationException if command is no URL 
         */
        public String getURL() throws OwInvalidOperationException
        {
            if (!canGetURL())
            {
                throw new OwInvalidOperationException("OwMimeManager.getURL: URL is a script command = " + m_sCommand);
            }

            return m_sCommand;
        }
    }

}