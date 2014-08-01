package com.alfresco.ow.server.dmsdialogs.views;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alfresco.ow.server.plug.owrecordext.OwCreateFolderByTemplateDialog;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * OwTabObjectListViewRow.<br/>
 * Object list view row to put into a tab dialog. 
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
public class OwTabObjectListViewRow extends OwLayout implements OwMultipanel, OwObjectListViewEventListner
{
    /** index of the next button in the menu */
    protected int m_iNextButtonIndex = -1;

    /** stores the next active view */
    private OwView m_NextActiveView = null;

    /** Menu for buttons in the view */
    protected OwSubMenuView m_MenuView = new OwSubMenuView();

    /** Stores the selector view */
    private OwObjectListViewRow m_ListView;

    /** region of the tree view */
    public static final int CONTROL_REGION = 0;

    /** region of the tree view */
    public static final int MENU_REGION = 1;

    /** stores the user selected template folder */
    private OwObject m_selectedTemplate = null;

    /** stores the dialog */
    private OwCreateFolderByTemplateDialog m_Dialog;

    /**
     * Constructor
     * @param columnInfoList_p column info as configured in the bootstrap.
     * @throws OwConfigurationException 
     */
    public OwTabObjectListViewRow(OwCreateFolderByTemplateDialog dlg_p, List<OwStandardFieldColumnInfo> columnInfoList_p) throws OwConfigurationException
    {
        // check values
        if (null == columnInfoList_p || columnInfoList_p.isEmpty())
        {
            throw new OwConfigurationException("Column info collection is a required argument!");
        }
        if (null == dlg_p)
        {
            throw new IllegalArgumentException("OwCreateObjectDialog is a required parameter!");
        }

        // store dialog
        m_Dialog = dlg_p;

        // create list view
        m_ListView = new OwObjectListViewRow(OwObjectListView.VIEW_MASK_USE_SELECT_BUTTON);
        m_ListView.setMimeTypeContext("OwContractManagement");
        this.m_ListView.setColumnInfo(columnInfoList_p);
    }

    /** init the target after the context is set.
     */
    @Override
    protected void init() throws Exception
    {
        super.init();

        // === add list view
        this.m_ListView.setExternalFormTarget(this.m_ListView);
        addView(this.m_ListView, CONTROL_REGION, null);

        // === add menu
        addView(this.m_MenuView, MENU_REGION, null);
        this.m_ListView.setEventListner(this);
    }

    /**
     * switches to the next view 
     */
    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        m_iNextButtonIndex = m_MenuView.addMenuItem(this, getContext().localize("app.OwEditAspectView.next", "Next"), null, "Next", nextView_p, null);
        m_MenuView.setDefaultMenuItem(m_iNextButtonIndex);
        this.m_NextActiveView = nextView_p;
    }

    /**
     * ignored
     */
    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        // ignore
    }

    /** check if view has validated its data and the next view can be enabled
    *
    * @return boolean true = can forward to next view, false = view has not yet validated
    * Return true always for us, for aspects are not mandatory ...
    *
    */
    public boolean isValidated() throws Exception
    {
        // check if user has selected  a template
        return null != this.getSelectedTemplate();
    }

    /** 
     * Event called when user clicked Next button in menu 
     * @param oReason_p Optional reason object submitted in addMenuItem
     * @param request_p a {@link HttpServletRequest}
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // get selected template from view
        if (null != this.m_ListView)
        {
            if (isValidated())
            {
                // set selected template in dialog
                this.m_Dialog.updateTemplateFolder(m_selectedTemplate);

                m_iNextButtonIndex = -1;
                m_NextActiveView.activate();
            }
        }
    }

    /**
     * Returns the list view embedded as control.
     * @return the list view embedded as control.
     */
    public OwObjectListViewRow getListView()
    {
        return m_ListView;
    }

    /** 
     * Returns the user selected template 
     * @return the user selected template 
     */
    public OwObject getSelectedTemplate()
    {
        return m_selectedTemplate;
    }

    /**
     * Sets the user selected template 
     * @param selectedTemplate_p selected template to set
     */
    public void setSelectedTemplate(OwObject selectedTemplate_p)
    {
        this.m_selectedTemplate = selectedTemplate_p;
    }

    /** called when uses clicks on a sort header and the sort changes
     * @param newSort_p OwSort new sort
     * @param strSortProperty_p String Property Name of sort property that was changed
     */
    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
    }

    /** called when user clicks a select button, fUseSelectButton_p must have been set to display select buttons 
    *
    * @param object_p OwObject object that was selected
    * @param parent_p OwObject parent if available, or null
    */
    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        this.setSelectedTemplate(object_p);

        //goto next view
        // get selected template from view
        if (null != this.m_ListView)
        {
            if (isValidated())
            {
                // set selected template in dialog
                this.m_Dialog.updateTemplateFolder(m_selectedTemplate);

                m_iNextButtonIndex = -1;
                m_NextActiveView.activate();
            }
        }
    }

    /** called when user changes or activates the column filter 
    *
    * @param filterNode_p OwSearchNode with filter
    * @param parent_p OwObject parent if available, or null
    */
    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {
    }

    /** called when uses clicks on a folder, used to redirect folder events an bypass the mimemanager
     * 
     * @param obj_p OwObject folder object that was clicked
     * @return boolean true = event was handled, false = event was not handled, do default handler
     * 
     * @throws Exception
     */
    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        return false;
    }

    /** get the style class name for the row
    *
    * @param iIndex_p int row index
    * @param obj_p current OwObject
    *
    * @return String with style class name, or null to use default
    */
    public String onObjectListViewGetRowClassName(int iIndex_p, OwObject obj_p)
    {
        return null;
    }

    /**
     * Shows the content of the folder object in the list view.
     * @param folderObj_p folder object which content to show. 
     * @throws Exception 
     */
    public void showFolderContent(OwObject folderObj_p) throws Exception
    {
        OwObjectCollection content = folderObj_p.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS }, this.m_ListView.getRetrievalPropertyNames(), this.getListView().getSort(), 1000, OwSearchTemplate.VERSION_SELECT_DEFAULT,
                null);
        this.m_ListView.setObjectList(content, folderObj_p);
    }
}
