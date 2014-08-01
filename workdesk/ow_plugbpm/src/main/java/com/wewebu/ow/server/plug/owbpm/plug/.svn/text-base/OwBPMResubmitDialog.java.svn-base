package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwEditablePropertyDate;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.util.OwDateTimeUtil;

/**
 *<p>
 * Dialog for setting the resubmission date.
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
public class OwBPMResubmitDialog extends OwBPMInsertNoteDialog
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMResubmitDialog.class);

    public static final int KEY_RESUBMIT_DATE = 100;

    /** refresh context ref */
    private OwClientRefreshContext m_refreshCtx;

    /** the reassign date */
    private OwEditablePropertyDate m_reassignDate;

    /** error field of reassign update */
    private String m_errorEditField;

    /**
     * create a new instance of the dialog
     * @throws OwException 
     */
    public OwBPMResubmitDialog(List workitems_p, String noteProperty_p, OwClientRefreshContext refreshCtx_p) throws OwException
    {
        super(workitems_p, noteProperty_p, false);
        m_refreshCtx = refreshCtx_p;
    }

    /**
     * create a new instance of the dialog
     * @param noteProperty_p 
     * @throws OwException 
     */
    public OwBPMResubmitDialog(OwWorkitem workitem_p, String noteProperty_p, OwClientRefreshContext refreshCtx_p) throws OwException
    {
        super(workitem_p, noteProperty_p, false);
        m_refreshCtx = refreshCtx_p;
    }

    /**
     * @see com.wewebu.ow.server.plug.owbpm.plug.OwBPMInsertNoteDialog#init()
     */
    protected void init() throws Exception
    {
        super.init();

        m_reassignDate = new OwEditablePropertyDate((OwMainAppContext) getContext());

        // initialize the property
        Date resubmitdate = ((OwWorkitem) m_workitems.get(0)).getResubmitDate(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);

        // init with resubmit date of first item or if not set to the next day
        if (resubmitdate != null)
        {
            m_reassignDate.setValue(resubmitdate);
        }
        else
        {
            m_reassignDate.setValue(OwDateTimeUtil.offsetDay(new Date(), 1));
        }
    }

    /**
     * event called when user clicked the OK button should be overloaded by
     * specific Dialog
     * 
     * @param request_p	 a {@link HttpServletRequest}
     * @param reason_p
     */
    public void onOkDo(HttpServletRequest request_p, Object reason_p) throws Exception
    {

        // set note

        try
        {
            setNote(request_p);
        }
        catch (OwException e)
        {
            LOG.warn("Set note Exception", e);
        }

        // set the resubmit

        // fetch value of the reassign date
        if (m_reassignDate.update(getContext().getLocale(), request_p)) // proceed only if we have a valid value
        {
            m_errorEditField = m_reassignDate.getSafePropertyError(getContext().getLocale());
            if (m_errorEditField == null || m_errorEditField.length() == 0)
            {
                setSubmitDate();
                closeDialog();
            }

        }

        //		 do a refresh of the changed queues
        if (null != m_refreshCtx)
        {
            m_refreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
        }
        getDocument().update(this, OwUpdateCodes.MODIFIED_OBJECT_PROPERTY, null);
    }

    /**
     * set the resubmit date to all workitems
     */
    private void setSubmitDate() throws Exception
    {
        OwWorkitem item = null;
        for (Iterator iter = m_workitems.iterator(); iter.hasNext();)
        {
            item = (OwWorkitem) iter.next();

            try
            {
                Date reassignDate = (Date) this.m_reassignDate.getValue();
                item.resubmit(reassignDate);
            }
            catch (Exception e)
            {
                LOG.error("Der Vorgang konnte nicht auf Wiedervorlage gesetzt werden!", e);
                throw new OwInvalidOperationException(getContext().localize1("plug.owbpm.plug.OwBPMResubmitDialog.resubmit.error", "Work item (%1) cannot be resubmitted.", item.getName()), e);
            }

        }

    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case MAIN_REGION:
                this.serverSideDesignInclude("owbpm/OwResubmit.jsp", w_p);
                break;

            case KEY_RESUBMIT_DATE:
                this.m_reassignDate.render(getContext().getLocale(), w_p);
                break;

            default:
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /**
     * getter method for errorEditField 
     * @return Returns the errorEditField.
     */
    public String getErrorEditField()
    {
        return m_errorEditField;
    }
}