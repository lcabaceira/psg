package com.wewebu.ow.server.plug.owdemo.owfax;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Demo send Fax dialog, display the documents.
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
public class OwFaxDocumentList extends OwView
{
    /** Iterator of OwObjects to fax. */
    protected Collection m_Objects;

    /** activate success message */
    protected boolean m_fSuccess;

    /** callback reference to the calling dialog */
    protected OwSendFaxDialog m_FaxDialog;

    /** construct a list view
     *
     * @param objects_p Iterator of OwObjects to fax
     */
    public OwFaxDocumentList(OwSendFaxDialog caller_p, Collection objects_p)
    {
        m_FaxDialog = caller_p;
        m_Objects = objects_p;
    }

    /** set the success flag and display to user*/
    public void setSuccess()
    {
        m_fSuccess = true;
    }

    /** render the view
    * @param w_p Writer object to write HTML to
    */
    public void onRender(Writer w_p) throws Exception
    {
        w_p.write("<form name='" + OwSendFaxDialog.FORM_NAME + "' method='post'>");

        // === input control for the faxnumber and text
        String strFaxNumber = m_FaxDialog.getFaxNumber();
        if (strFaxNumber == null)
        {
            strFaxNumber = "";
        }
        w_p.write("<div class=\"OwBlock\">");
        w_p.write("<label for='" + OwSendFaxDialog.TEXT_CTRL_NAME + "'><strong>" + getContext().localize("plug.owfax.OwFaxDocumentList.freetext", "Free text") + ":</strong></label><br /><textarea cols='50' rows='10' name='"
                + OwSendFaxDialog.TEXT_CTRL_NAME + "' id='" + OwSendFaxDialog.TEXT_CTRL_NAME + "'></textarea>");
        w_p.write("</div>");
        w_p.write("<div class=\"OwBlock\">");
        w_p.write("<label for='" + OwSendFaxDialog.FAX_CTRL_NAME + "'><strong>" + getContext().localize("plug.owfax.OwFaxDocumentList.number", "Fax number") + ":</strong></label><br /><input type='text' name='" + OwSendFaxDialog.FAX_CTRL_NAME
                + "' id='" + OwSendFaxDialog.FAX_CTRL_NAME + "' value='" + strFaxNumber + "'></input>");
        w_p.write("</div>");
        w_p.write("<div class=\"OwBlock\">");

        // === list of objects to be faxed
        w_p.write("<strong>" + getContext().localize("plug.owfax.OwFaxDocumentList.listtitle", "List of objects") + "</strong>");
        w_p.write("<ul>");
        Iterator it = m_Objects.iterator();
        int iCount = 1;
        while (it.hasNext())
        {
            w_p.write("<li>");
            OwObject obj = (OwObject) it.next();
            w_p.write(String.valueOf(iCount++) + ". " + obj.getName());
            w_p.write("</li>");
        }
        w_p.write("</ul>");

        w_p.write("</div>");

        if (m_fSuccess)
        {
            w_p.write("<h3>" + getContext().localize("plug.owfax.OwFaxDocumentList.success", "Succes sending fax.") + "</h3>");

            // show only one time
            m_fSuccess = false;
        }
        w_p.write("</form>");
    }
}