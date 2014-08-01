package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Base class for paging components.
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
 *@since 2.5.2.0
 */
public abstract class OwPageSelectorComponent
{
    /** Logger for this class */
    protected static final Logger LOG = OwLogCore.getLogger(OwPageSelectorComponent.class);

    /**the view on this component apply to*/
    protected OwPageableView m_view;
    /**the config node*/
    protected Node m_configNode;
    /** flag that specify the sticky footer*/
    /**
     * @deprecated since 4.1.1.0 This attribute is no longer used and will be soon removed.
     */
    @Deprecated
    protected boolean isStickyFooterInUse;

    /**
     * Constructor.
     * @param view_p - the view that needs this paging component
     */
    public OwPageSelectorComponent(OwPageableView view_p)
    {
        m_view = view_p;
        isStickyFooterInUse = true;
    }

    /**
     * Render the component.
     * @param w_p - the writer
     * @throws Exception 
     */
    public abstract void render(Writer w_p) throws Exception;

    /**
     * Create an URL for the given page
     * @param iPage_p
     * @return the URL 
     */
    public String getPageAbsolutURL(int iPage_p)
    {
        return m_view.getPageAbsolutEventURL(OwObjectListView.QUERY_KEY_PAGE + "=" + String.valueOf(iPage_p));
    }

    /** check if page buttons should be rendered
     * 
     * @return true = render page buttons, false = no page buttons to render
     */
    public boolean hasPaging()
    {
        return m_view.hasPaging();
    }

    /**
     * Get the previous page URL
     * @return - the URL.
     */
    public String getPagePrevURL()
    {
        return m_view.getPagePrevEventURL();
    }

    /**
     * Get the next page URL
     * @return - the URL.
     */
    public String getPageNextURL()
    {
        return m_view.getPageNextEventURL();
    }

    /**
     * Get the number of current page (1 - based)
     * @return the number of the current page.
     * @throws Exception
     */
    protected int getDisplayCurrentPageNumber() throws Exception
    {
        return getHumanReadablePageNumber(m_view.getCurrentPage());
    }

    /**
     * Returns the human readable page number (1 - based)
     * @param pageNumber_p - the page number
     * @return the page number + 1
     */
    protected int getHumanReadablePageNumber(int pageNumber_p)
    {
        return pageNumber_p + 1;
    }

    /**
     * Get the last page number
     * @return the last page
     * @throws Exception
     */
    protected int getLastPage() throws Exception
    {
        int lastPage = m_view.getPageCount() - 1;
        return lastPage;
    }

    /**
     * Check if it make sense to display the paging component.
     * @return <code>true</code> if component should be rendered.
     * @throws Exception
     */
    protected boolean shouldRenderComponent() throws Exception
    {
        int pageCount = m_view.getPageCount();
        return pageCount > 1 || pageCount == -1;
    }

    /**
     * Set the config node
     * @param configNode_p
     */
    public void setConfigNode(Node configNode_p)
    {
        m_configNode = configNode_p;
    }

    /**
     * Post creation hook for initialization component. 
     * Use it when something is needed to be set from the corresponding configuration node
     * @throws Exception - when the initialization fails. 
     */
    public void init() throws Exception
    {
        //LOG.debug("OwPageSelectorComponent.init: Initialization...");
    }

    /**
     * Render a mark for accessibility.
     * @param w_p the {@link Writer} object
     * @throws Exception
     * @since 3.0.0.0
     */
    protected void renderAccessibilityMark(Writer w_p) throws Exception
    {
        w_p.write("<h3 id=\"owpaging\" class=\"accessibility\">" + m_view.getContext().localize("dmsdialogs.views.OwPageSelectorComponent.accessibility", "Page selector component") + "</h3>");
    }
}