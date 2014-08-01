package com.alfresco.ow.server.docimport;

import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.alfresco.ow.server.utils.OwNetworkHelper;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwAbstractDocumentImporterView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwTemplateDocumentImporterView.
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
public class OwTemplateDocumentImporterView extends OwAbstractDocumentImporterView implements OwObjectListView.OwObjectListViewEventListner
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwTemplateDocumentImporterView.class);

    /** region for the optional menu */
    public static final int MENU_REGION = 20;

    /** region for result list view */
    public static final int LISTVIEW_REGION = 10;

    /** Menu for buttons like "Add" */
    protected OwSubMenuView m_MenuView;

    /** List view for templates in the templates directory */
    protected OwObjectListViewRow m_ListView;

    /** Column info collection for header of list view */
    protected List<OwFieldColumnInfo> m_ColumnInfoList;

    /** stores the path to the template directory */
    protected String m_TemplateDirPath;

    /**
     * Constructor
     * @param importer_p
     *              Document Importer
     * @param callback_p
     *              Document importer callback
     * @param templateDirPath_p
     *              Path to the template directory in the ECM system
     * @param columnInfoList_p
     *              Column info collection for header of list view
     */
    public OwTemplateDocumentImporterView(OwDocumentImporter importer_p, OwDocumentImporterCallback callback_p, String templateDirPath_p, List<OwFieldColumnInfo> columnInfoList_p)
    {
        super(importer_p, callback_p);
        m_TemplateDirPath = templateDirPath_p;
        m_ColumnInfoList = columnInfoList_p;

        // check values
        if (null == m_ColumnInfoList || m_ColumnInfoList.isEmpty())
        {
            throw new IllegalArgumentException("Column info collection is a required argument!");
        }
        if (null == m_TemplateDirPath || m_TemplateDirPath.length() == 0)
        {
            throw new IllegalArgumentException("Path to template folder in the ECM system is a required argument!");
        }
    }

    /**
     * Init the target after the context is set.
     */
    @Override
    protected void init() throws Exception
    {
        super.init();

        // create and add the result list view
        m_ListView = new OwObjectListViewRow(OwObjectListView.VIEW_MASK_USE_SELECT_BUTTON);
        m_ListView.setMimeTypeContext("OwContractManagement");
        addView(m_ListView, LISTVIEW_REGION, null);
        m_ListView.setEventListner(this);
    }

    /**
     * to get additional form attributes used for the form
     * override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    @Override
    protected String usesFormWithAttributes()
    {
        return "ENCTYPE='multipart/form-data'";
    }

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("docimport/OwTemplateDocumentImporterView.jsp", w_p);
    }

    /**
     * Gets the template folder by it's path and the documents inside and sets it to the list view.
     */
    public void showTemplateFolder()
    {
        try
        {
            OwMainAppContext context = (OwMainAppContext) getContext();
            OwObject objOwFolder = null;
            OwObjectCollection content = null;

            // Check if is a search template and returns more than one results
            if (OwNetworkHelper.isSearchTemplate(m_TemplateDirPath))
            {
                content = OwNetworkHelper.getSearchTemplateResults(context, m_TemplateDirPath);

                // check if it is a folder ... and if, get the documents from it
                if (null != content && content.size() == 1)
                {
                    OwObject objFolder = (OwObject) content.get(0);
                    if (OwNetworkHelper.isFolder(objFolder))
                    {
                        content = objFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 100, OwSearchTemplate.VERSION_SELECT_DEFAULT, null);
                    }
                }
            }

            if (null == content)
            {
                // try to get a folder from ECM 
                objOwFolder = OwNetworkHelper.getObject(context, this.m_TemplateDirPath, "m_TemplateDirPath");
                content = objOwFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 100, OwSearchTemplate.VERSION_SELECT_DEFAULT, null);
            }

            this.m_ListView.setColumnInfo(m_ColumnInfoList);
            this.m_ListView.setObjectList(content, objOwFolder);
        }
        catch (Exception e)
        {
            LOG.error("Unable to get template folder or template folder content!", e);
        }
    }

    /**
     * ignored
     */
    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
    }

    /**
     * Event handler - adds object to document importer
     */
    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        // add selected item to the upload queue
        OwTemplateDocumentImporterItem item = new OwTemplateDocumentImporterItem(object_p);
        this.fireOnDocumentImportEvent(item);
    }

    /**
     * ignored
     */
    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {
    }

    /**
     * Ignored
     */
    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        return false;
    }

    /**
     * ignored
     */
    public String onObjectListViewGetRowClassName(int iIndex_p, OwObject obj_p)
    {
        return null;
    }
}
