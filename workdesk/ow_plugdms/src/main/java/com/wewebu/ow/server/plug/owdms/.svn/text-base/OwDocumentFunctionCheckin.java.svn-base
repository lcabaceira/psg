package com.wewebu.ow.server.plug.owdms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owdms.log.OwLog;
import com.wewebu.ow.server.plug.owutil.OwConfigUtils;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the DMS document function checkin.
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
public class OwDocumentFunctionCheckin extends OwDocumentFunction implements OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDocumentFunctionCheckin.class);

    // === members
    /** the last object to check in needed for status historization in onCloseDialog() */
    protected OwObject m_checkinObject;

    /** the last parent object needed for status historization in onCloseDialog() */
    protected OwObject m_parentObject;

    /** list of OwDocumentImporter objects that should be displayed as possible document source */
    protected List m_documentImporters;

    /** list of OwPropertyInfo objects that define the visibility and modifiability of properties 
     *  @since 3.1.0.3 */
    //    private List<OwPropertyInfo> m_propertyInfos;
    /** Handler for configuration of EditPropertylist
     * @since 4.2.0.0*/
    private OwPropertyListConfiguration propertyListConfiguration;

    protected OwKeepContentDocumentImporter keepDocImporter;

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
                try
                {
                    Class documentImporterClass = Class.forName(className);
                    OwDocumentImporter documentImporter = (OwDocumentImporter) documentImporterClass.newInstance();
                    documentImporter.init(getContext(), documentImporterConfig);
                    m_documentImporters.add(documentImporter);
                }
                catch (ClassNotFoundException ex)
                {
                    String msg = "Plugin initialization failed, Pluginid = " + getPluginID() + ". Documentimporter with classname = " + className + " not found, please check your owplugins.xml...";
                    LOG.error(msg, ex);
                    throw new OwConfigurationException(msg, ex);
                }
            }
        }
        OwXMLUtil util = getConfigNode().getSubUtil(OwPropertyListConfiguration.ELEM_ROOT_NODE);
        if (util != null)
        {
            propertyListConfiguration = new OwPropertyListConfiguration(getPluginID());
            propertyListConfiguration.build(util);
        }

        //special DocumentImporter for checkin, to keep content the same as before
        keepDocImporter = new OwKeepContentDocumentImporter();
        //Don't forget about init-method, else you get NullPointerException
        keepDocImporter.init(context_p, node_p);

        if (m_documentImporters.size() <= 0)
        {
            throw new OwConfigurationException("There are no document importers configured for the Checkin Plugin. Do not know how to get the content.");
        }
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdms/checkin.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdms/checkin_24.png");
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
            return oObject_p.getVersion().canCheckin(iContext_p);
        }
        else
        {
            return false;
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
            // search for a reason why the function is not enable
            if (oObject_p.hasVersionSeries() && oObject_p.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                boolean isMyCheckedOut = false;
                try
                {
                    isMyCheckedOut = oObject_p.getVersion().isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }
                catch (Exception ex)
                {
                    //isMyCheckedOut() is not supported or not implemented or exception occurred
                }
                if (!isMyCheckedOut)
                {
                    String checkedoutBy = null;
                    try
                    {
                        checkedoutBy = oObject_p.getVersion().getCheckedOutUserID(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                    }
                    catch (Exception e)
                    {
                        //getCheckedOutUserID() is not supported or not implemented or exception occurred     
                    }
                    if (checkedoutBy != null && !checkedoutBy.equals(""))
                    {
                        checkedoutBy = getDisplayNameFromUserId(checkedoutBy);
                        throw new OwInvalidOperationException(getContext().localize1("plug.owdms.OwDocumentFunctionCheckin.documentCheckedOutbyOtherUser", "Cannot check in the document. The document was checked out by user (%1).", checkedoutBy));
                    }
                }
            }
            // cannot check if the document was checked out before.
            throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionCheckin.documentNotCheckedOut", "Cannot check in the document. Did you check out the document before?"));
        }

        // === get view properties from plugin descriptor
        int iViewMask = 0;
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "MultipleFileUpload", OwSaveDlgDialog.VIEW_PROPERTY_ENABLE_MULTIPLE_FILE_UPLOAD);
        iViewMask |= (oObject_p.canChangeClass() ? OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "ObjectClassView", OwSaveDlgDialog.VIEW_PROPERTY_CLASS_VIEW) : 0);
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "CheckInModeOption", OwCheckInDialog.VIEW_MASK_CHECKIN_MODE_OPTION, true);
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "CheckInReleaseVersionOption", OwCheckInDialog.VIEW_MASK_RELEASE_VERSION_OPTION, true);
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "CheckInReleaseVersionDefault", OwCheckInDialog.VIEW_MASK_RELEASE_VERSION_DEFAULT, true);

        if (oObject_p.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
        {
            /*This document has content, and maybe it is
             *want to keep this content, then use this importer!
             *ATTENTION: this importer is removed onDialogClose().*/
            m_documentImporters.add(this.keepDocImporter);
        }
        // === create a checkin dialog
        OwCheckInDialog dlg = createCheckInDialog(oObject_p);
        m_checkinObject = oObject_p;
        m_parentObject = oParent_p;
        dlg.setViewMask(iViewMask);

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());
        dlg.setTitle(getDefaultLabel());

        dlg.setPropertyListConfiguration(getPropertyListConfiguration());
        // set info icon
        dlg.setInfoIcon(getBigIcon());

        dlg.setRefreshContext(refreshCtx_p);

        getContext().openDialog(dlg, this);

        // historize begin
        addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * Listener for DialogClose events used to historize SUCCESS/CANCEL/FAILURE
     * @param dialogView_p the closed dialog
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        int status = ((OwCheckInDialog) dialogView_p).getStatus();
        switch (status)
        {
            case OwCheckInDialog.DIALOG_STATUS_FAILED:
                addHistoryEvent(m_checkinObject, m_parentObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                break;
            case OwCheckInDialog.DIALOG_STATUS_OK:
                addHistoryEvent(m_checkinObject, m_parentObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                ((OwCheckInDialog) dialogView_p).onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_VERSION, null);
                break;
            default:
                addHistoryEvent(m_checkinObject, m_parentObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_CANCEL);
                break;
        }
        /*Remove keep content Importer, because the next document may be doesn't have content*/
        if (this.m_documentImporters.contains(this.keepDocImporter))
        {
            this.m_documentImporters.remove(this.keepDocImporter);
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

    /**
     * (overridable) factory method for the creation of check-in dialog 
     * 
     * @param saveObject_p {@link OwObject} to checkin
     * @return {@link OwCheckInDialog} newly created dialog
     * @throws Exception
     * 
     * @since 2.5.3.0
     */
    protected OwCheckInDialog createCheckInDialog(OwObject saveObject_p) throws Exception
    {
        return new OwCheckInDialog(saveObject_p, m_documentImporters);
    }

    /**
     * Getter for current PropertyList configuration
     * @return OwPropertyListConfiguration or null if not set
     * @since 4.2.0.0
     */
    protected OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return this.propertyListConfiguration;
    }
}