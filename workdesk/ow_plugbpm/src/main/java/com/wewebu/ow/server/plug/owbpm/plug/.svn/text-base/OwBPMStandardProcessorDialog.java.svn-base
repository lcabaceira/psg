package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Workdesk BPM Plugin Standard Processor Dialog.
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
public class OwBPMStandardProcessorDialog extends OwStandardSequenceDialog implements OwBPMProcessorDialog
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMStandardProcessorDialog.class);

    protected OwBPMStandardProcessorView m_view;

    /** instance of the MIME manager used to open the objects */
    protected OwMimeManager m_MimeManager = new OwMimeManager();

    /** config node from the function class */
    protected OwXMLUtil m_configNode;

    /** auto open the preview attachment? */
    protected boolean m_fAutoOpen = false;

    /** workitems */
    protected List m_worktitems;

    /** current index */
    protected int m_iIndex = 0;

    /** the refresh context to notify the calling plugin client */
    protected OwClientRefreshContext m_refreshCtx;

    /**
     * @param objects_p array of {@link OwWorkitem}s
     */
    public OwBPMStandardProcessorDialog(Collection objects_p, OwClientRefreshContext refreshCtx_p, OwXMLUtil configNode_p) throws Exception
    {
        m_refreshCtx = refreshCtx_p;
        m_configNode = configNode_p;
        m_fAutoOpen = m_configNode.getSafeBooleanValue("AutoOpen", false);

        if (objects_p instanceof List)
        {
            m_worktitems = (List) objects_p;
        }
        else
        {
            m_worktitems = new ArrayList(objects_p);
        }

        m_view = createProcessorView(m_configNode);
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === init MIME manager as event target
        m_MimeManager.attach(getContext(), null);
        m_MimeManager.setItemStyle("OwEditPropertiesMimeItem");
        m_MimeManager.setIconStyle("OwEditPropertiesMimeIcon");

        addView(m_view, OwStandardDialog.MAIN_REGION, null);
        m_view.setObjectRef(getWorkItem(), false);

        // set a document
        m_view.setDocument(new OwDocument());

        // open attachment preview
        if (m_fAutoOpen)
        {
            previewAttachment();
        }
    }

    /** unlock the workitems */
    public void unlock() throws Exception
    {
        for (int i = 0; i < m_worktitems.size(); i++)
        {
            ((OwWorkitem) m_worktitems.get(i)).setLock(false);
        }
    }

    /** the work item to work on */
    private OwWorkitem getWorkItem()
    {
        return (OwWorkitem) m_worktitems.get(m_iIndex);
    }

    /** called when the Dialog needs to know if there is a next item
    *
    */
    public boolean hasNext() throws Exception
    {
        return (m_iIndex < (m_worktitems.size() - 1));
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
        return m_worktitems.size();
    }

    /** move to prev item and roll over, i.e. start at the end one if first one is reached 
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the prev item, if this is the last item, closes the dialog
     */
    public void prev(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            throw new OwNotSupportedException("OwBPMStandardProcessorDialog.prev(fRemoveCurrent_p==true) not supported.");
        }

        if (hasPrev())
        {
            m_iIndex--;
        }
        else
        {
            m_iIndex = (m_worktitems.size() - 1);
        }

        m_view.setObjectRef(getWorkItem(), false);
        // update attachment preview
        if (m_fAutoOpen)
        {
            previewAttachment();
        }
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
                closeDialog();
                return;
            }
            else
            {
                // unlock before removing it
                ((OwWorkitem) m_worktitems.get(m_iIndex)).setLock(false);
                // remove it
                m_worktitems.remove(m_iIndex);
                if (m_iIndex >= m_worktitems.size())
                {
                    m_iIndex = 0;
                }
            }
        }
        else
        {
            if (hasNext())
            {
                m_iIndex++;
            }
            else
            {
                m_iIndex = 0;
            }
        }

        m_view.setObjectRef(getWorkItem(), false);
        // update attachment preview
        if (m_fAutoOpen)
        {
            previewAttachment();
        }
    }

    public List getWorkItems()
    {
        return m_worktitems;
    }

    /** visually close the Dialog. The behavior depends on usage
     *  If this view is a child of a DialogManager, the View gets removed from it.
     */
    public void closeDialog() throws Exception
    {
        super.closeDialog();

        // === close viewer as well
        if (m_fAutoOpen && ((OwMainAppContext) getContext()).getWindowPositions().getPositionMainWindow())
        {
            ((OwMainAppContext) getContext()).addFinalScript("\n" + OwMimeManager.createAutoViewerRestoreMainWindowScript(((OwMainAppContext) getContext()), OwMimeManager.VIEWER_MODE_DEFAULT));
        }

        // notify plugin client
        if (null != m_refreshCtx)
        {
            m_refreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
        }
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach the field manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();
    }

    /** overridable factory function to create the standard processor View
     * 
     * @param configNode_p OwXMLUtil
     * @return OwBPMStandardProcessorView
     */
    protected OwBPMStandardProcessorView createProcessorView(OwXMLUtil configNode_p) throws Exception
    {
        return new OwBPMStandardProcessorView(this, configNode_p);
    }

    /**
     * open preview attachment
     */
    protected void previewAttachment()
    {
        try
        {
            OwXMLUtil m_fPreviewAttachmentName = m_configNode.getSubUtil("PreviewAttachmentName");
            if (m_fPreviewAttachmentName == null)
            {
                if (LOG.isDebugEnabled())
                {
                    String msg = "OwBPMStandardProcessorDialog.previewAttachment: No preview attachment name is declared. Can't auto open preview attachment.";
                    LOG.debug(msg);
                }
                //no preview attachment name declared, just return
                return;
            }

            OwProperty owp = getWorkItem().getProperty(m_fPreviewAttachmentName.getSafeTextValue("<PreviewAttachmentName>"));
            OwObjectReference owr = null;

            //is attachment array?
            if (owp.getValue() instanceof Object[])
            {
                //get index of element that should be used for preview
                int arrayindex = m_fPreviewAttachmentName.getSafeIntegerAttributeValue("arrayindex", -2);
                Object[] array = (Object[]) owp.getValue();

                if (array.length == 0)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwBPMStandardProcessorDialog.previewAttachment: Attachment array contains no elements.");
                    }
                    //array is empty just return;
                    return;
                }
                if (arrayindex < -1 || array.length < arrayindex + 1)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwBPMStandardProcessorDialog.previewAttachment: Specified 'arrayindex' is out of bound. Please check configuration file.");
                    }
                    //array index is out of bound, just return
                    return;
                }
                if (arrayindex == -1)
                {
                    //use the latest attached element
                    owr = (OwObjectReference) array[array.length - 1];
                }
                else
                {
                    owr = (OwObjectReference) array[arrayindex];
                }
            }
            else
            {
                owr = (OwObjectReference) (owp.getValue());
            }

            if (null != owr)
            {
                if (OwMimeManager.isObjectDownloadable((OwMainAppContext) getContext(), owr))
                {
                    // open with preview
                    OwMimeManager.openObjectPreview(((OwMainAppContext) getContext()), owr, null, OwMimeManager.VIEWER_MODE_SINGLE, m_refreshCtx);
                }
            }
        }
        catch (Exception e)
        {
            //If the preview attribute was not found - ignore error.
            LOG.debug("Exception occurred on preview attachment.", e);
        }
    }
}