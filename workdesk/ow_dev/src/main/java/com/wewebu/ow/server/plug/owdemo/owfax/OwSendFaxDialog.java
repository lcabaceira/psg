package com.wewebu.ow.server.plug.owdemo.owfax;

import java.io.Writer;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubMenuView;

/**
 *<p>
 * Demo send Fax dialog.
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
public class OwSendFaxDialog extends OwStandardDialog
{
    /** region ID for the additional result region */
    public static final int RESULT_REGION = 10;
    /** HTML form name */
    public static final String FORM_NAME = "OwSendFaxDialog";
    /** HTML input control name */
    public static final String FAX_CTRL_NAME = "FaxNumber";
    /** HTML input control name */
    public static final String TEXT_CTRL_NAME = "FaxText";

    /** fax number */
    protected String m_strFaxNumber;

    /** Iterator of OwObjects to fax. */
    protected Collection m_Objects;

    /** the fax menu buttons */
    protected OwSubMenuView m_Menu = new OwSubMenuView();

    /** document list view */
    protected OwFaxDocumentList m_FaxList;

    /** construct a fax dialog 
     *
     * @param objects_p Iterator of OwObjects to fax
     */
    public OwSendFaxDialog(Collection objects_p)
    {
        m_Objects = objects_p;
    }

    /** called when the view should create its HTML content to be displayed
     */
    protected void init() throws Exception
    {
        super.init();

        // === create menu
        // create send fax button
        m_Menu.addFormMenuItem(this, getContext().localize("plug.owfax.OwSendFaxDialog.sendfax", "Send"), "SendFax", getContext().localize("plug.owfax.OwSendFaxDialog.sendfaxdescr", "Send fax"), FORM_NAME);

        // create select address button
        m_Menu.addMenuItem(this, getContext().localize("plug.owfax.OwSendFaxDialog.searchnumber", "Search fax number..."), "SelectNumber",
                getContext().localize("plug.owfax.OwSendFaxDialog.searchnumberdescr", "Search fax number in external database."));

        // add menu to the menu region
        addView(m_Menu, MENU_REGION, null);

        // === create document list view
        m_FaxList = new OwFaxDocumentList(this, m_Objects);

        // add menu to the menu region
        addView(m_FaxList, MAIN_REGION, null);
    }

    /** called when user clicked the send fax button
     * 
     */
    public void onSendFax(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // get number and text from HTML form
        m_strFaxNumber = request_p.getParameter(FAX_CTRL_NAME);
        String strFaxText = request_p.getParameter(TEXT_CTRL_NAME);

        // call fax device
        OwFaxSendDevice.onSendFax(m_strFaxNumber, strFaxText, m_Objects);

        m_FaxList.setSuccess();
    }

    /** called when user clicked the send fax button
     * 
     */
    public void onSelectNumber(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwFaxSelectAddressDialog dlg = new OwFaxSelectAddressDialog(this);

        dlg.setInfoIcon(m_strInfoIconURL);

        dlg.setTitle(getContext().localize("plug.owfax.OwSendFaxDialog.searchnumber", "Faxnummer suchen..."));

        getContext().openDialog(dlg, null);
    }

    /** set the fax number, used by OwFaxSelectAddressDialog */
    public void setFaxNumber(String strFaxNumber_p)
    {
        m_strFaxNumber = strFaxNumber_p;
    }

    /** set the fax number, used by OwFaxSelectAddressDialog */
    public String getFaxNumber()
    {
        return m_strFaxNumber;
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // MVC View JSP Page
        serverSideDesignInclude("demo/OwFaxDemoDialogLayout.jsp", w_p);
    }
}