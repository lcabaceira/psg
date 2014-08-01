package com.wewebu.ow.server.plug.owdocview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.servlets.OwMultifileDownload;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the Document Function that zips the selected files 
 * and provide a download for that zip.
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
public class OwDownloadAsZipDocumentFunction extends OwDocumentFunction
{
    /**Default value if the download URL is not defined or empty*/
    protected static final String DEFAULT_URL = "multifileDownload";

    /**Tag name of the element which define the servlet URL to call download*/
    protected static final String TAG_DOWNLOAD_URL = "downloadUrl";

    private String m_servletUrl;

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdms/compress.png");
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdms/compress_24.png");
    }

    /** check if function is enabled for the given object parameters (called only for single select operations)
    *
    *  @param object_p OwObject where event was triggered
    *  @param parent_p Parent which listed the Object
    *  @param context_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
    {
        if (!super.isEnabled(object_p, parent_p, context_p))
        {
            return false;
        }

        if ((object_p.getType() != OwObjectReference.OBJECT_TYPE_DOCUMENT) || !object_p.hasContent(context_p))
        {
            return false;
        }

        return true;
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        List objects = new ArrayList();
        objects.add(oObject_p);
        this.onMultiselectClickEvent(objects, oParent_p, refreshCtx_p);
    }

    public void onMultiselectClickEvent(Collection objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        // filter objects, which are no documents or don't have content.
        List filesToZip = new ArrayList();
        for (Iterator iterator = objects_p.iterator(); iterator.hasNext();)
        {
            OwObject object = (OwObject) iterator.next();
            if (!isEnabled(object, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                String escapedName = OwHTMLHelper.encodeToSecureHTML(object.getName());
                this.m_MainContext.postMessage(this.m_MainContext.localize1("OwDownloadAsZipDocumentFunction.nodocument", "Object %1 is not a document.", escapedName));
                continue;
            }

            //TODO: get latest version?
            filesToZip.add(object);
        }

        if (filesToZip == null || filesToZip.size() == 0)
        {
            m_MainContext.postMessage(this.m_MainContext.localize("OwDownloadAsZipDocumentFunction.nofile", "No valid file(s) to download selected!"));
            return;
        }

        // store filesToZip list into session for servlet.
        m_MainContext.getHttpSession().setAttribute(OwMultifileDownload.ATTRIBUTE_NAME_FILES_TO_DOWNLOAD, filesToZip);

        // download via servlet call by java script
        StringBuffer downloadServletUrl = new StringBuffer();
        downloadServletUrl.append(this.m_MainContext.getBaseURL());
        if (!m_servletUrl.startsWith("/"))
        {
            downloadServletUrl.append("/");
        }
        downloadServletUrl.append(getServletUrl());
        downloadServletUrl.append("?t=");
        //create random number to prevent caching of server
        downloadServletUrl.append(System.currentTimeMillis());

        this.m_MainContext.addFinalScript("navigateServiceFrameTO(\"" + downloadServletUrl + "\");");

        // historize
        addHistoryEvent(objects_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        setServletUrl(node_p.getSafeTextValue(TAG_DOWNLOAD_URL, DEFAULT_URL));
    }

    /**
     * Set the servlet URL part of the link, which is used during
     * link creation. 
     * @param servletUrl_p
     * @since 2.5.3.0
     */
    protected void setServletUrl(String servletUrl_p)
    {
        this.m_servletUrl = servletUrl_p;
    }

    /**
     * Get the defined servlet URL part, which was
     * assigned during initialization
     * @return String representing the servlet URL part
     * @since 2.5.3.0
     */
    public String getServletUrl()
    {
        return this.m_servletUrl;
    }
}