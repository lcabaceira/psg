package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Base class for {@link OwToolViewItem} based views. 
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
public abstract class OwToolView extends OwView
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwToolView.class);

    public static final int DEFAULT_ITEM_COLUMNS_COUNT = 3;

    /**
     * Item wrapped click targets.
     * @see OwToolClickTarget
     */
    private List m_clickTargets = new ArrayList();

    private int m_columnItemsCount = DEFAULT_ITEM_COLUMNS_COUNT;

    public OwToolView()
    {
        this(DEFAULT_ITEM_COLUMNS_COUNT);
    }

    public OwToolView(int columnItemsCount_p)
    {
        this.m_columnItemsCount = columnItemsCount_p;
    }

    public int getColumnItemsCount()
    {
        return m_columnItemsCount;
    }

    /** to be implemented by the subclass, iterates over the tools to display
    * 
     * @return Collection of OwToolItem
     */
    public abstract Collection getToolItems();

    /**
     * 
     * @param item_p
     * @return the click event URL associated with the a click target for the givet item 
     * @see OwToolClickTarget
     */
    public String createToolItemClickURL(OwToolViewItem item_p)
    {
        OwToolClickTarget itemClickTarget = new OwToolClickTarget(item_p);
        try
        {
            itemClickTarget.attach(getContext(), null);
            m_clickTargets.add(itemClickTarget);
            return itemClickTarget.createClickEventURL(getContext());
        }
        catch (Exception e)
        {
            LOG.error("OwToolView.createToolItemTarget : Could not create event URL!", e);
            return "";
        }

    }

    /** 
     * Called when a tool has been clicked by a user.
     * 
     * @param item_p the clicked tool item
     */
    public abstract void invoke(OwToolViewItem item_p);

    protected void onRender(Writer w_p) throws Exception
    {
        detachClickTargets();
        serverSideDesignInclude("OwToolView.jsp", w_p);
    }

    /**
     * Detaches all prevoiusly created item click targets and clears the {@link #m_clickTargets} 
     * target collection.
    */
    private void detachClickTargets()
    {
        for (Iterator i = m_clickTargets.iterator(); i.hasNext();)
        {
            OwEventTarget clickTarget = (OwEventTarget) i.next();
            clickTarget.detach();
        }
        m_clickTargets.clear();
    }

    public void detach()
    {
        detachClickTargets();
        super.detach();

    }

    /**
     * 
     * Click event tatrger wrapp for {@link OwToolViewItem}s.
     */
    public class OwToolClickTarget extends OwEventTarget
    {
        private OwToolViewItem m_item;

        public OwToolClickTarget(OwToolViewItem item_p)
        {
            super();
            this.m_item = item_p;
        }

        public String createClickEventURL(OwAppContext context_p)
        {
            return context_p.getEventURL(this, "Click", null);

        }

        public void onClick(HttpServletRequest request_p) throws Exception
        {
            OwToolView.this.invoke(m_item);
        }

        protected void init() throws Exception
        {

        }

    }

}