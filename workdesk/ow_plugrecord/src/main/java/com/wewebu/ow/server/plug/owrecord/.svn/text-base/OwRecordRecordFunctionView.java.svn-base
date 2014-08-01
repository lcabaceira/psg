package com.wewebu.ow.server.plug.owrecord;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwRecordFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.owrecord.log.OwLog;
import com.wewebu.ow.server.ui.OwTreeView;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * View to display the record functions for the opened record.
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
public class OwRecordRecordFunctionView extends OwView implements OwClientRefreshContext
{
    public static final String UPDATE_AJAX_EVENT_NAME = "Update";
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRecordRecordFunctionView.class);

    /** query string key for the plugin index */
    protected static final String QUERY_KEY_PLUGIN = "plugin";

    /** list to the record function plugins */
    protected List m_recordFunctionPluginList;

    /** list of Document function plugins which have been instantiated */
    protected List m_DocumentFunctionPluginList;

    private boolean m_fshowDisabledRecordFunctions;

    private boolean m_fDocumentFunctionsWorkOnRoot;

    public void init() throws Exception
    {
        super.init();

        // === get flags from config node
        // get show disabled record function flag from config node
        m_fshowDisabledRecordFunctions = ((OwMasterDocument) getDocument()).getConfigNode().getSafeBooleanValue("showDisabledRecordFunctions", true);

        // get plugin definition nodes through a placeholder filter
        OwXMLUtil documentFunctionPluginsNode = ((OwRecordDocument) getDocument()).getDocumentFunctionPluginsNode();
        OwXMLUtil recordFunctionPluginsNode = ((OwRecordDocument) getDocument()).getRecordFunctionPluginsNode();

        // get work on root flag
        m_fDocumentFunctionsWorkOnRoot = documentFunctionPluginsNode.getSafeBooleanAttributeValue("workonroot", true);

        if (documentFunctionPluginsNode.getSafeBooleanAttributeValue(OwRecordDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            // === get preloaded plugins reference
            m_DocumentFunctionPluginList = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunctionPlugins();
        }

        if (recordFunctionPluginsNode.getSafeBooleanAttributeValue(OwRecordDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            // === get preloaded plugins reference
            m_recordFunctionPluginList = ((OwMainAppContext) getContext()).getConfiguration().getRecordFunctionPlugins();
        }

        // === filter document function plugins if filter is defined
        List docfunctions = documentFunctionPluginsNode.getSafeStringList();
        if (docfunctions.size() != 0)
        {
            // === use only defined functions
            // new array 
            m_DocumentFunctionPluginList = new Vector();

            Iterator it = docfunctions.iterator();
            while (it.hasNext())
            {
                String id = (String) it.next();
                //        		only add to array if it is an allowed function
                if (((OwMainAppContext) getContext()).getConfiguration().isDocumentFunctionAllowed(id))
                {
                    OwFunction func = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(id);
                    m_DocumentFunctionPluginList.add(func);
                }
            }
        }

        // === filter record function plugins if filter is defined
        List recfunctions = recordFunctionPluginsNode.getSafeStringList();
        if (recfunctions.size() != 0)
        {
            // === use only defined functions
            // new array 
            m_recordFunctionPluginList = new Vector();

            Iterator it = recfunctions.iterator();
            while (it.hasNext())
            {
                String id = (String) it.next();
                //        		only add to array if it is an allowed function
                if (((OwMainAppContext) getContext()).getConfiguration().isRecordFunctionAllowed(id))
                {
                    OwFunction func = ((OwMainAppContext) getContext()).getConfiguration().getRecordFunction(id);
                    m_recordFunctionPluginList.add(func);
                }
            }
        }

        // === count all DragDropTargets
        int iDragDropTargetCount = 0;
        if (m_recordFunctionPluginList != null)
        {
            for (int i = 0; i < m_recordFunctionPluginList.size(); i++)
            {
                OwRecordFunction recordFunctionPlugin = (OwRecordFunction) m_recordFunctionPluginList.get(i);
                if (recordFunctionPlugin.isDragDropTarget())
                {
                    iDragDropTargetCount++;
                }
            }
        }

        // throw OwConfigurationException if more than 1 DragDropTarget is configured
        if (iDragDropTargetCount > 1)
        {
            String msg = "OwRecordRecordFunctionView.init: Only ONE record plugin may be configured as DragDropTarget.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
    }

    /** flag indicating if disabled record functions should be displayed disabled
     * 
     * @return boolean true = display disabled record functions gray, false = do not display disabled record functions at all (save space)
     */
    public boolean showDisabledRecordFunctions()
    {
        return m_fshowDisabledRecordFunctions;
    }

    public List getRecordFuntionPlugins()
    {
        return m_recordFunctionPluginList;
    }

    public List getDocumentFuntionPlugins()
    {
        return m_DocumentFunctionPluginList;
    }

    /** get the event URL for document plugin functions
     * 
     * @param iPlugIndex_p
     * @return the URL for the document plugin functions as <code>String</code>
     */
    public String getDocumentFunctionEventURL(int iPlugIndex_p)
    {
        return getEventURL("DocumentFunctionPluginEvent", QUERY_KEY_PLUGIN + "=" + String.valueOf(iPlugIndex_p));
    }

    /** get the event URL for record plugin functions
     * 
     * @param iPlugIndex_p
     * @return the URL for the record plugin function as <code>String</code> 
     */
    public String getRecordFunctionEventURL(int iPlugIndex_p)
    {
        StringBuffer query = new StringBuffer();

        query.append(QUERY_KEY_PLUGIN);
        query.append("=");
        query.append(String.valueOf(iPlugIndex_p));

        return getEventURL("RecordFunctionPluginEvent", query.toString());
    }

    /**
     * Check if the DocumentFunction is enabled
     * @param docFunctionPlugin_p docFunctionPlugin_p to be checked
     * @return boolean
     * @throws Exception
     */
    public boolean getIsPluginEnabled(OwDocumentFunction docFunctionPlugin_p) throws Exception
    {
        try
        {
            //can we provide a parent to the document function
            if (getDocumentFunctionWorkobjectParent() == null)
            {
                //we can't provide a parent, does the function need one ?
                // get the Need Parent Flag indicating that plugin can only working on documents listed by some parent.
                if (docFunctionPlugin_p.getNeedParent())
                {
                    return false; // skip NeedParent plugins, we don't have a parent here
                }
            }

            if (!docFunctionPlugin_p.isEnabled(getDocumentFunctionWorkobject(), getDocumentFunctionWorkobjectParent(), OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * Check if the RecordFunctionPlugin is enabled 
     * @param recordFunctionPlugin_p RecordFunctionPlugin to be checked
     * @return boolean
     * @throws Exception
     */
    public boolean getIsPluginEnabled(OwRecordFunction recordFunctionPlugin_p) throws Exception
    {

        OwObject currentSubFolderObject = ((OwRecordDocument) getDocument()).getCurrentSubFolderObject();

        OwObject rootObject = ((OwRecordDocument) getDocument()).getCurrentRootFolder();

        if (currentSubFolderObject == null && rootObject != null)
        {
            currentSubFolderObject = rootObject;
            OwRecordDocument recordDocument = (OwRecordDocument) getDocument();
            recordDocument.openFolder(currentSubFolderObject, null);
        }

        boolean enabled = recordFunctionPlugin_p.isEnabled(rootObject, currentSubFolderObject, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
        return enabled;
    }

    public OwObject getCurrentSubFolderObject()
    {
        return ((OwRecordDocument) getDocument()).getCurrentSubFolderObject();
    }

    public OwObject getCurrentSubFolderObjectParent()
    {
        return ((OwRecordDocument) getDocument()).getCurrentSubFolderObjectParent();
    }

    public OwObject getCurrentRootFolder()
    {
        return ((OwRecordDocument) getDocument()).getCurrentRootFolder();
    }

    /** 
     * @param w_p Writer
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("owrecord/OwRecordRecordFunctionView.jsp", w_p);
    }

    /** get the folder object that the document functions should work on
     *  can be either root or selected folder
     * 
     * @return OwObject
     */
    public OwObject getDocumentFunctionWorkobject()
    {
        if (m_fDocumentFunctionsWorkOnRoot)
        {
            return ((OwRecordDocument) getDocument()).getCurrentRootFolder();
        }
        else
        {
            return ((OwRecordDocument) getDocument()).getCurrentSubFolderObject();
        }
    }

    /** get the parent folder of the current folder object that the document functions should work on
     *  can be <code>null</code> if no parent is available
     * 
     * @return OwObject
     */
    public OwObject getDocumentFunctionWorkobjectParent()
    {
        if (m_fDocumentFunctionsWorkOnRoot)
        {
            return null;
        }
        else
        {
            return ((OwRecordDocument) getDocument()).getCurrentSubFolderObjectParent();
        }
    }

    /** event called when user clicked a plugin link
     * @param request_p HttpServletRequest
     */
    public void onDocumentFunctionPluginEvent(HttpServletRequest request_p) throws Exception
    {
        // get requested plugin
        OwDocumentFunction docFunctionPlugin = (OwDocumentFunction) m_DocumentFunctionPluginList.get(Integer.parseInt(request_p.getParameter(QUERY_KEY_PLUGIN)));

        // dispatch click event to plugin
        docFunctionPlugin.onClickEvent(getDocumentFunctionWorkobject(), getDocumentFunctionWorkobjectParent(), this);
    }

    /** event called when user clicked a plugin link
     * @param request_p HttpServletRequest
     */
    public void onRecordFunctionPluginEvent(HttpServletRequest request_p) throws Exception
    {
        // get subpath
        String subpath = ((OwRecordDocument) getDocument()).getCurrentSubFolderPath();
        if ((subpath != null) && (subpath.equals(OwTreeView.PATH_DELIMITER)))
        {
            // root path is null
            subpath = null;
        }

        // get sub display path
        String displaypath = ((OwRecordDocument) getDocument()).getCurrentSubFolderDisplayPath();

        // get requested plugin
        OwRecordFunction recordFunctionPlugin = (OwRecordFunction) m_recordFunctionPluginList.get(Integer.parseInt(request_p.getParameter(QUERY_KEY_PLUGIN)));

        // dispatch click event to plugin
        recordFunctionPlugin.onClickEvent(((OwRecordDocument) getDocument()).getCurrentRootFolder(), ((OwRecordDocument) getDocument()).getCurrentSubFolderObject(), subpath, displaypath, this);
    }

    /** implementation of the OwFunction.OwFunctionRefreshContext interface
     *  Called from a plugin to inform its client and cause refresh of display data
     *
     * @param iReason_p reason as defined with OwClientRefreshContext.REFRESH_...
     * @param param_p Object optional parameter representing the refresh, depends on the value of iReason_p, can be null
     */
    public void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception
    {
        // === plugin requested refresh
        // Translate plugin messages
        switch (iReason_p)
        {

            case OwUpdateCodes.DELETE_OBJECT:
                // did we delete the root ?
                if (getDocumentFunctionWorkobject() == ((OwRecordDocument) getDocument()).getCurrentRootFolder())
                {
                    getDocument().update(this, OwUpdateCodes.DELETE_OBJECT, param_p);
                }
                else
                {
                    getDocument().update(this, OwUpdateCodes.UPDATE_PARENT_OBJECT_FOLDER_CHILDS, param_p);
                }
                break;

            default:
                // delegate reasons unmodified
                getDocument().update(this, iReason_p, param_p);
                break;
        }
    }

    /**
     * Handler for AJAX request to update the content.
     * @param request_p - the AJAX request object.
     * @param response_p - the response
     * @throws Exception
     * @since 3.1.0.0
     */
    public void onAjaxUpdate(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        PrintWriter writer = response_p.getWriter();
        render(writer);
        writer.close();
    }
}