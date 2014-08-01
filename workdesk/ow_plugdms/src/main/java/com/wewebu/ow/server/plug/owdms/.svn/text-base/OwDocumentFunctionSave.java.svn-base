package com.wewebu.ow.server.plug.owdms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owutil.OwConfigUtils;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the DMS Document Function, Save a document (Change the content of an DMS Object).
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
public class OwDocumentFunctionSave extends OwDocumentFunction implements OwDialogListener
{
    // === members
    /** the last object to check in needed for status historization in onCloseDialog() */
    protected OwObject m_saveObject;
    /** the last parent object needed for status historization in onCloseDialog() */
    protected OwObject m_parentObject;

    /** list of OwDocumentImporter objects that should be displayed as possible document source */
    private List m_documentImporters;

    /**
     * override to get some plugin configuration tags.
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        // read document importers
        m_documentImporters = new ArrayList();
        Collection documentImporterConfigNodes = getConfigNode().getSafeNodeList("DocumentImporter");
        Iterator itDocumentImporterConfigNodes = documentImporterConfigNodes.iterator();
        while (itDocumentImporterConfigNodes.hasNext())
        {
            OwXMLUtil documentImporterConfig = new OwStandardXMLUtil((Node) itDocumentImporterConfigNodes.next());
            String className = documentImporterConfig.getSafeTextValue("ClassName", null);
            if (className != null)
            {
                Class documentImporterClass = Class.forName(className);
                OwDocumentImporter documentImporter = (OwDocumentImporter) documentImporterClass.newInstance();
                documentImporter.init(getContext(), documentImporterConfig);
                m_documentImporters.add(documentImporter);
            }
        }
        if (m_documentImporters.size() <= 0)
        {
            throw new OwConfigurationException("There are no document importers configured for the Save Plugin. Do not know how to get the content.");
        }
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdms/save.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdms/save_24.png");
    }

    /** check if function is enabled for the given object parameters
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return true = enabled, false otherwise
     */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (!super.isEnabled(oObject_p, oParent_p, iContext_p))
        {
            return false;
        }
        if (oObject_p.hasVersionSeries())
        {
            return oObject_p.getVersion().canSave(iContext_p);
        }
        else
        {
            return true;
        }
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
        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionSave.invalidobject", "Item cannot be saved."));
        }

        // === get view properties from plugin descriptor
        int iViewMask = 0;
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "MultipleFileUpload", OwSaveDlgDialog.VIEW_PROPERTY_ENABLE_MULTIPLE_FILE_UPLOAD);
        /*
        iViewMask |= OwSaveDlgDialog.VIEW_PROPERTY_LOCAL_FILE_OPTION;
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(),"ExternalFileOption",OwSaveDlgDialog.VIEW_PROPERTY_EXTERNAL_FILE_OPTION);
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(),"NoFileOption",OwSaveDlgDialog.VIEW_PROPERTY_NO_FILE_OPTION);
        */

        // === create a checkin dialog
        OwSaveDialog dlg = new OwSaveDialog(oObject_p, m_documentImporters);
        m_saveObject = oObject_p;
        m_parentObject = oParent_p;
        dlg.setViewMask(iViewMask);

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());
        dlg.setTitle(getDefaultLabel());

        // set info icon
        dlg.setInfoIcon(getBigIcon());

        // submit refresh interface
        dlg.setRefreshContext(refreshCtx_p);

        // open the dialog
        getContext().openDialog(dlg, this);

        // historize
        addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * Listener for DialogClose events used to historize SUCCESS/CANCEL/FAILURE
     * @param dialogView_p the closed dialog
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        int status = ((OwSaveDialog) dialogView_p).getStatus();
        switch (status)
        {
            case OwSaveDialog.DIALOG_STATUS_FAILED:
                addHistoryEvent(m_saveObject, m_parentObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                break;
            case OwSaveDialog.DIALOG_STATUS_OK:
                addHistoryEvent(m_saveObject, m_parentObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                break;
            default:
                addHistoryEvent(m_saveObject, m_parentObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_CANCEL);
                break;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // ignore, used for update events from documents
    }
}