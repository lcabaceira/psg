package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSequenceView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.plug.owbpm.OwBPMResultlistAttachmentFieldControl;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Workdesk BPM Plugin JSP Processor View.
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
public class OwBPMJspProcessorView extends OwObjectPropertyFormularView
{
    protected static final String RESPONSE_KEY = "OwBPMJspProcessorView_response";

    /** config node */
    protected OwXMLUtil m_configNode;

    /** cached locked status of the item */
    protected boolean m_fLocked = false;

    /** button index */
    protected int m_iDispatchButton;
    /** button index */
    protected int m_iReturnToSourceButton;

    /** reference to the embedding sequence view */
    protected OwSequenceView m_seqview;

    /** create a JSP step processor view
     * 
     * @param seqview_p OwSequenceView to browse through multiple items
     * @param configNode_p option OwXMLUtil config node to configure optional attachment document functions, can be null
     */
    public OwBPMJspProcessorView(OwSequenceView seqview_p, OwXMLUtil configNode_p)
    {
        m_seqview = seqview_p;
        m_configNode = configNode_p;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // return to source button
        m_iReturnToSourceButton = getMenu().addFormMenuItem(this, getContext().localize("owbpm.OwBPMJspProcessorView.returntosourcebtn", "Reassign"), "ReturnToSource", null, getFormName());

        // dispatch button
        m_iDispatchButton = getMenu().addFormMenuItem(this, getDispatchButtonTitle(), "Dispatch", null, getFormName());

        if (null != m_configNode)
        {
            List attachmentFunctionIDs = m_configNode.getSafeStringList(OwBPMResultlistAttachmentFieldControl.PLUGIN_CONFIG_ID_ATTACHMENT_FUNCTION);
            OwBPMResultlistAttachmentFieldControl objectReferenceControl = new OwBPMResultlistAttachmentFieldControl(attachmentFunctionIDs);
            OwBPMResultlistAttachmentFieldControl objectControl = new OwBPMResultlistAttachmentFieldControl(attachmentFunctionIDs);

            // override the object rendition with a custom field control
            getFieldManager().attachFieldControlByType("com.wewebu.ow.server.ecm.OwObjectReference", objectReferenceControl, null);
            getFieldManager().attachFieldControlByType("com.wewebu.ow.server.ecm.OwObject", objectControl, null);
        }
    }

    /**
     * (overridable) Retrieves the dispatch button title
     * @return the dispatch button String title
     *@since 2.5.2.0 
     */
    protected String getDispatchButtonTitle()
    {
        return getContext().localize("owbpm.OwBPMJspProcessorView.dispatchbtn", "Complete");
    }

    /**
     * @param objectRef_p The objectRef to set.
     */
    public void setObjectRef(OwObject objectRef_p) throws Exception
    {
        super.setObjectRef(objectRef_p);

        m_fLocked = getObjectRef().getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);

        getMenu().enable(getSaveBtnIndex(), m_fLocked);
        getMenu().enable(m_iDispatchButton, m_fLocked && getWorkItem().canDispatch(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        getMenu().enable(m_iReturnToSourceButton, m_fLocked && getWorkItem().canReturnToSource(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
    }

    /** get the work item */
    protected OwWorkitem getWorkItem()
    {
        return (OwWorkitem) getObjectRef();
    }

    /** save the work item properties including the response
     *  @return true = all fields could be validated and saved, false = one or more fields are invalid
     */
    protected boolean save(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        boolean fRet = super.onSaveInternal(request_p, oReason_p);

        // save response as well
        String strResponse = request_p.getParameter(RESPONSE_KEY);
        if ((strResponse != null) && (strResponse.length() > 0) && getWorkItem().canResponse())
        {
            getWorkItem().setResponse(strResponse);
        }
        if (fRet)
        {
            safeUpdate(this, OwUpdateCodes.MODIFIED_OBJECT_PROPERTY);
        }
        return fRet;
    }

    /** called when the user wants to save the modified properties 
     *  @return true = all fields could be validated and saved, false = one or more fields are invalid
     */
    public boolean onSave(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // save form
        save(request_p, oReason_p);

        if (getUpdateStatus())
        {
            // === info for user
            ((OwMainAppContext) getContext()).postMessage(getContext().localize("app.OwObjectPropertyFormularView.saved", "Changes have been saved."));

            // move to next item
            m_seqview.next(true);
            return true;
        }
        else
        {
            return false;
        }
    }

    /** causes all attached views to receive an onUpdate event
    *
    *  @param target_p OwEventTarget target that called update
    *  @param iCode_p int optional reason code
    */
    private void safeUpdate(OwEventTarget target_p, int iCode_p) throws Exception
    {
        OwDocument doc = getDocument();

        if (null != doc)
        {
            doc.update(target_p, iCode_p, null);
        }
    }

    /** called when the user wants to dispatch (finish) the work item */
    public void onDispatch(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // save form
        save(request_p, oReason_p);

        if (getUpdateStatus())
        {
            if (getWorkItem().getResponse() == null && getWorkItem().canResponse())
            {
                throw new OwInvalidOperationException(getSelectResponseErrorMessage());
            }
            else
            {

                // dispatch
                getWorkItem().dispatch();
                safeUpdate(this, OwUpdateCodes.OBJECT_DISPATCH);
                // === info for user
                ((OwMainAppContext) getContext()).postMessage(getContext().localize("app.OwObjectPropertyFormularView.saved", "Changes have been saved."));

                // move to next item, delete this item
                m_seqview.next(true);
            }
        }
    }

    /**
     * Get the value of the first item
     * @return The localized item
     */
    private String getSelectResponseErrorMessage()
    {
        return getContext().localize("plug.owbpm.OwBPMStandardProcessorView.errorresponseselect", "Please select an appropriate response.");
    }

    /** called when the user wants to dispatch (finish) the work item */
    public void onReturnToSource(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // save form
        save(request_p, oReason_p);

        if (getUpdateStatus())
        {
            // return to source
            getWorkItem().returnToSource();

            // === info for user
            ((OwMainAppContext) getContext()).postMessage(getContext().localize("app.OwObjectPropertyFormularView.saved", "Changes have been saved."));

            // move to next item, delete this item
            m_seqview.next(true);
        }
    }

    /** overridable to render a property */
    protected void renderProperty(Writer w_p, OwProperty prop_p, boolean fReadOnly_p) throws Exception
    {
        if (fReadOnly_p || (!m_fLocked))
        {
            getFieldManager().insertReadOnlyField(w_p, prop_p);
        }
        else
        {
            getFieldManager().insertEditField(w_p, prop_p);
        }
    }

    /** overridden render the menu region */
    protected void renderMenuRegion(Writer w_p) throws Exception
    {
        w_p.write("<div style='clear:both'>");
        renderDropDownComponent(w_p);
        w_p.write("<div style='float:left'>");
        // render menu
        super.renderMenuRegion(w_p);
        w_p.write("</div>");
        w_p.write("</div>");
    }

    /**
     * Render a drop down list with possible actions for this item.
     * @param w_p - the Writer
     * @throws Exception - thrown in case that something went wrong
     */
    protected void renderDropDownComponent(Writer w_p) throws Exception
    {
        // render responses
        if (getWorkItem().canResponse())
        {
            Iterator it = getWorkItem().getResponses().iterator();

            String strCurrentResponse = getWorkItem().getResponse();
            List items = new LinkedList();
            // Now add the items
            while (it.hasNext())
            {
                OwEnum response = (OwEnum) it.next();
                OwComboItem item = new OwDefaultComboItem((String) response.getValue(), response.getDisplayName(getContext().getLocale()));
                items.add(item);
            }

            w_p.write("<div style='float:left;'>");
            OwComboModel comboModel = new OwDefaultComboModel(true, false, strCurrentResponse, items, getContext().localize("plug.owbpm.OwBPMJspProcessorView.responseselect", "Select response"));
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(comboModel, RESPONSE_KEY, null, null, new OwString("plug.owbpm.OwBPMJspProcessorView.responseselect", "Select response"));
            renderer.renderCombo(w_p);
            w_p.write("</div>");
            w_p.write("<div style='float:left;'>&nbsp;</div>");
        }
    }

    @Override
    public boolean isPropertyReadOnly(boolean readOnly)
    {
        return (readOnly || (!m_fLocked));
    }
}