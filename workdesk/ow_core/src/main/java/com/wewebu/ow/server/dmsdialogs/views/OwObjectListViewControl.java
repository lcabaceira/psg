package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwBaseView;
import com.wewebu.ow.server.ui.button.OwImageButton;
import com.wewebu.ow.server.ui.button.OwImageButtonView;
import com.wewebu.ow.server.ui.helper.OwInnerViewWrapper;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Control that selects different object list views.
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
public class OwObjectListViewControl extends OwImageButtonView
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectListViewControl.class);

    /**
     *<p>
     * A list view item reference entry.
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
    public static class OwListViewEntry
    {
        private OwObjectListView m_listview;

        public OwListViewEntry(OwObjectListView listview_p)
        {
            m_listview = listview_p;
        }

        public OwObjectListView getObjectListView()
        {
            return m_listview;
        }

        public String getIcon() throws Exception
        {
            return m_listview.getIcon();
        }

        public String getTitle()
        {
            return m_listview.getTitle();
        }

    }

    /**
     *<p>
     * Event listener interface.
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
    public interface OwObjectListViewControlEventListener extends java.util.EventListener
    {
        /** called when user selects an object list view
         * 
         * @param iNewIndex_p int new view index
         * @param iOldIndex_p int old view index or -1 if no previous view
         * @param newview_p OwObjectListView
         * @param oldview_p OwObjectListView or null if no previous view
         * 
         * @throws Exception 
         */
        public abstract void onActivateObjectListView(int iNewIndex_p, int iOldIndex_p, OwObjectListView newview_p, OwObjectListView oldview_p) throws Exception;
    }

    /** attribut name for the index */
    private static final String PERSISTENT_INDEX_ATTRIBUTE = "OwObjectListViewControl_Index";

    /** query key for the list view index */
    private static final String INDEX_KEY = "index";

    /** instance of the view wrapper of the currently selected view */
    private OwInnerViewWrapper m_InnerViewWrapper = new OwInnerViewWrapper();

    /** list of configured list views the user can select from */
    private List<OwListViewEntry> m_objectlists = new ArrayList<OwListViewEntry>();

    /** List of plugins to be used in the lists or null to use the default set of document functions */
    private List m_documentFunctionPluginList = null;

    /** the currently selected list view index */
    private int m_currentindex = -1;

    /** event listener for callback's */
    private OwObjectListViewControlEventListener m_eventlistner;

    /** set event listener for callbacks */
    public void setEventListener(OwObjectListViewControlEventListener eventlistner_p)
    {
        m_eventlistner = eventlistner_p;
    }

    public void init() throws Exception
    {
        super.init();
        setHtmlId("OwObjectListButtonControlView");
    }

    /** checks if the view is shown maximized 
     * @return true, if view is maximized, false otherwise
     */
    public boolean isShowMaximized()
    {
        return getParent().isShowMaximized();
    }

    /** get the reference to the active list view */
    public OwBaseView getViewReference()
    {
        return m_InnerViewWrapper;
    }

    public OwObjectListView getObjectListView()
    {
        OwListViewEntry viewentry = m_objectlists.get(m_currentindex);
        return viewentry.getObjectListView();
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
        m_documentFunctionPluginList = pluginList_p;
        Iterator<OwListViewEntry> itObjectLists = m_objectlists.iterator();
        while (itObjectLists.hasNext())
        {
            OwListViewEntry listViewEntry = itObjectLists.next();
            listViewEntry.getObjectListView().setDocumentFunctionPluginList(pluginList_p);
        }
    }

    /** getter method for referencing in JSP files */
    public List<OwListViewEntry> getObjectLists()
    {
        return m_objectlists;
    }

    /** getter method for referencing in JSP files */
    public int getCurrentIndex()
    {
        return m_currentindex;
    }

    /** getter method for referencing in JSP files
    *
    * @param iViewIndex_p index of the current view
    */
    public String getObjectListViewControlEventURL(int iViewIndex_p)
    {
        return getEventURL("Select", INDEX_KEY + "=" + String.valueOf(iViewIndex_p));
    }

    /** render a button for each list view
     * 
     */
    public void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwObjectListViewControl.jsp", w_p);
    }

    public void renderButtons(Writer w_p) throws Exception
    {
        super.onRender(w_p);
    }

    /** event called when user selects a list view 
     * 
     * @param request_p
     * @throws Exception
     */
    public void onSelect(HttpServletRequest request_p) throws Exception
    {
        // === activate view
        int iNewIndex = Integer.parseInt(request_p.getParameter(INDEX_KEY));
        activateListView(iNewIndex);

        // === persist selected index
        getDocument().getPersistentAttributeBagWriteable().setAttribute(PERSISTENT_INDEX_ATTRIBUTE, String.valueOf(iNewIndex));
        getDocument().getPersistentAttributeBagWriteable().save();
    }

    /** activate the persistent index or the first one if no persistance is used
     * 
     * @throws Exception
     */
    public void activateListView() throws Exception
    {
        // === persist selected index
        int iNewIndex = Integer.parseInt(((String) getDocument().getPersistentAttributeBagWriteable().getSafeAttribute(PERSISTENT_INDEX_ATTRIBUTE, "0")));
        if (iNewIndex < m_objectlists.size())
        {
            activateListView(iNewIndex);
        }
        else
        {
            activateListView(0);
        }
    }

    /** activate the selected list view index
     * 
     * @param iNewIndex_p
     * @throws Exception
     */
    public void activateListView(int iNewIndex_p) throws Exception
    {
        OwObjectListView oldview = null;
        if (-1 != m_currentindex)
        {
            oldview = m_objectlists.get(m_currentindex).getObjectListView();
        }

        OwObjectListView newview = m_objectlists.get(iNewIndex_p).getObjectListView();

        // activate view
        m_InnerViewWrapper.setView(newview);

        // signal event
        if (null != m_eventlistner)
        {
            m_eventlistner.onActivateObjectListView(iNewIndex_p, m_currentindex, newview, oldview);
        }

        // copy contents to new activated view
        if (null != oldview)
        {
            newview.copy(oldview);
        }

        // set index
        m_currentindex = iNewIndex_p;
    }

    /** set the config node to use 
     * 
     * @param subNode_p a {@link Node}
     * @throws Exception 
     */
    public void setConfigNode(Node subNode_p) throws Exception
    {
        if (null == subNode_p)
        {
            String msg = "OwObjectListViewControl.setConfigNode: Please define a ResultListViews node in the plugin descriptor.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        // === iterate over the subnodes and instantiate the views right away
        // NOTE: We could also instantiate the views on demand to save resources.
        for (Node n = subNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if ((n.getNodeType() == Node.ELEMENT_NODE) && n.getNodeName().equals("view"))
            {

                // === create view for that node
                String sClassName = OwXMLDOMUtil.getSafeStringAttributeValue(n, "classname", null);

                Class ListViewClass = Class.forName(sClassName);
                OwObjectListView listview = (OwObjectListView) ListViewClass.newInstance();

                listview.setConfigNode(n);

                if (m_documentFunctionPluginList != null)
                {
                    listview.setDocumentFunctionPluginList(m_documentFunctionPluginList);
                }

                // add view
                addView(listview, null);

                m_objectlists.add(new OwListViewEntry(listview));
            }
        }

        if (m_objectlists.size() == 0)
        {
            String msg = "OwObjectListViewControl.setConfigNode: Please define a ResultListViews node in the plugin descriptor and at least one view node.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
    }

    @Override
    public List<OwImageButton> getButtonList()
    {
        LinkedList<OwImageButton> lst = new LinkedList<OwImageButton>();
        List<OwListViewEntry> viewEntries = getObjectLists();
        for (int i = 0; i < viewEntries.size(); i++)
        {
            OwListViewEntry viewentry = viewEntries.get(i);
            OwImageButton button = new OwImageButton();
            button.setDesignClass(i == getCurrentIndex() ? "OwObjectListViewControl_entry_selected" : "OwObjectListViewControl_entry");
            button.setTooltip(viewentry.getTitle());
            button.setEventString(getObjectListViewControlEventURL(i));
            try
            {
                button.setImageLink(getContext().getDesignURL() + viewentry.getIcon());
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Image could not be retrieved", e);
                }
                else
                {
                    LOG.warn("Image could not be retrieved.");
                }
            }
            lst.add(button);
        }
        return lst;
    }
}