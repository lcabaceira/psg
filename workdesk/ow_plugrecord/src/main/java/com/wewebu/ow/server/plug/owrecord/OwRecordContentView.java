package com.wewebu.ow.server.plug.owrecord;

import java.io.Writer;
import java.util.List;

import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMaxMinButtonControlView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewControl;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Document list view, which contains a OwObjectListView to display the Documents of a Tree node.
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
public class OwRecordContentView extends OwLayout implements OwObjectListViewControl.OwObjectListViewControlEventListener
{
    private OwObjectListViewControl m_listcontrol = new OwObjectListViewControl();

    /** name of the max size region */
    public static final int OBJECT_LIST_REGION = 1;
    /** name of the max size region */
    public static final int OBJECT_LIST_SIZE_REGION = 2;
    /** name of the min max control region */
    public static final int MIN_MAX_CONTROL_VIEW = 3;
    /** name of the max size region */
    public static final int OBJECT_LIST_CONTROL_REGION = 4;

    /** View Module to display a maximize minimize button and maximize minimize the attached view */
    protected OwMaxMinButtonControlView m_MaxMinButtonControlView;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === attach to layout
        m_listcontrol.setEventListener(this);

        // attach to layout use a specific ID for persistence
        addView(m_listcontrol, OBJECT_LIST_CONTROL_REGION, null);
        addViewReference(m_listcontrol.getViewReference(), OBJECT_LIST_REGION);

        // set config node after init
        m_listcontrol.setConfigNode(((OwMasterDocument) getDocument()).getConfigNode().getSubNode("ResultListViews"));

        // activate view, select persistent index
        m_listcontrol.activateListView();

        // === add min max control
        m_MaxMinButtonControlView = new OwMaxMinButtonControlView(this);
        addView(m_MaxMinButtonControlView, MIN_MAX_CONTROL_VIEW, null);
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case OBJECT_LIST_SIZE_REGION:
            {
                // === render size region
                w_p.write(String.valueOf(m_listcontrol.getObjectListView().getCount()));
            }
                break;

            default:
            {
                // === render registered regions
                super.renderRegion(w_p, iRegion_p);
            }
        }
    }

    /** get a result list attribute
     * 
     * @param attributeName_p String name of attribute as defined with OwObjectCollection.ATTRIBUTE_...
     * @param default_p Object default value if not found
     * 
     * @return Object value of attribute
     */
    public Object getSafeListAttribute(String attributeName_p, Object default_p)
    {
        OwObjectCollection objList = m_listcontrol.getObjectListView().getObjectList();
        if (objList == null)
        {
            return default_p;
        }
        return objList.getSafeAttribute(attributeName_p, default_p);
    }

    /** check if result list could be retrieved completely
     * 
     * @return boolean true = all items are retrieved, false = there are more items on the server, only some are shown
     */
    public boolean getIsComplete()
    {
        return ((Boolean) getSafeListAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.TRUE)).booleanValue();
    }

    /** get the result list count */
    public int getCount()
    {
        return ((Integer) getSafeListAttribute(OwObjectCollection.ATTRIBUTE_SIZE, Integer.valueOf(0))).intValue();
    }

    /** render only a region in the view, used by derived classes
     *  render the attribute from the resultlist
     *
     * @param w_p Writer object to write HTML to
     * @param strRegion_p named region to render
     */
    public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
    {
        // === write out the attribute with the region name from the result list
        OwObjectCollection objList = m_listcontrol.getObjectListView().getObjectList();
        if (null != objList)
        {
            w_p.write(objList.getAttribute(strRegion_p).toString());
        }
    }

    /** determine if region exists
     *
     * @param strRegion_p name of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isNamedRegion(String strRegion_p) throws Exception
    {
        // === check if the attribute with the region name from the result list is available
        OwObjectCollection objList = m_listcontrol.getObjectListView().getObjectList();
        if (null != objList)
        {
            return objList.hasAttribute(strRegion_p);
        }
        else
        {
            return false;
        }
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        // === render ObjectList
        serverSideDesignInclude("owrecord/OwRecordContentView.jsp", w_p);
    }

    /** called when user selects an object list view
     * 
     * @param iNewIndex_p int new view index
     * @param iOldIndex_p int old view index or -1 if no previous view
     * @param newview_p OwObjectListView
     * @param oldview_p OwObjectListView or null if no previous view
     * 
     * @throws Exception 
     */
    public void onActivateObjectListView(int iNewIndex_p, int iOldIndex_p, OwObjectListView newview_p, OwObjectListView oldview_p) throws Exception
    {
        getDocument().update(this, OwUpdateCodes.CHANGE_VIEW, newview_p);

    }

    /**
     * Set a <code>java.util.List</code> of <code>OwDocumentFunction</code> to be used by this
     * list. This list overrides the default set of document functions that are retrieved from
     * the context during init.
     *
     * @param pluginList_p the <code>java.util.List</code> of <code>OwDocumentFunction</code> to be used by this list. Must not be <code>null</code>.
     */
    public void setDocumentFunctionPluginList(List pluginList_p)
    {
        m_listcontrol.setDocumentFunctionPluginList(pluginList_p);
    }

}