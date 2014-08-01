package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;

import org.w3c.dom.Node;

import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Class that render the each page number as a link. No matter how many pages are to display, the first and the last page is 
 * always shown. Between the first and the last page, are displayed 5 pages. This number of pages can be configured from owplugins.xml
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
public class OwVicinityPageSelectorComponent extends OwPageSelectorComponent
{
    /** fist page display number*/
    private static final int FIRST_PAGE_DISPLAY_NUMBER = 1;
    /**maximum number of pages visible between first and last page*/
    private int m_numberOfSelectablePages = 5;

    /**
     * Constructor
     * @param view_p - the view on this component applies.
     */
    public OwVicinityPageSelectorComponent(OwPageableView view_p)
    {
        super(view_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageSelectorComponent#render(java.io.Writer)
     */
    public void render(Writer w_p) throws Exception
    {
        w_p.write("<!--writing paging navigation component (OwVicinityPageSelectorComponent)-->\n");
        if (hasPaging() && shouldRenderComponent())
        {
            w_p.write("<div id=\"OwNextResults_page\">\n");
            renderAccessibilityMark(w_p);
            renderNumberOfItems(w_p);
            renderJumpTo(w_p);
            //render first page
            renderFirstPage(w_p);
            renderPagesNearCurrentPage(w_p);
            renderLastPage(w_p);
            w_p.write(" </div>\n");
        }
        w_p.write("<!--END writing paging navigation component (OwVicinityPageSelectorComponent)-->\n");
    }

    /**
     * Render a JavaScript for going to a specified page
     * @param w_p - the writer
     * @throws Exception - thrown when something goes wrong.
     */
    private void renderJumpTo(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            w_p.write("<script type=\"text/javascript\">\n");
            w_p.write("function jumpTo() {\n" + //
                    "     var page = prompt(" + getSelectPageNumberMessage() + ",'" + getDisplayCurrentPageNumber() + "');\n" + //
                    "     if (page>0 && page<=" + getHumanReadablePageNumber(getLastPage()) + ") {\n" + // 
                    "        document.location='" + m_view.getPageAbsolutEventURL(OwObjectListView.QUERY_KEY_PAGE + "=") + "'+(page-1);\n" + //
                    "     };\n" + //
                    "}\n");

            w_p.write("</script>\n");
            w_p.write("<div class=\"OwIndividualPageSelection\">\n");

            w_p.write("<span>&nbsp;•&nbsp;</span><a title=\"" + getPromptTitleMessage() + "\" onclick=\"jumpTo()\" href=\"#\">" + getPageOfMessage() + " " + getHumanReadablePageNumber(getLastPage()) + "</a>\n");
            w_p.write("<span>&nbsp;•&nbsp;</span></div>\n");
        }
    }

    /**
     * Get the localized "page x of y" message.
     * @return the localized message.
     * @throws Exception
     */
    private String getPageOfMessage() throws Exception
    {
        return m_view.getContext().localize1("dmsdialogs.views.OwIndividualPageSelectionComponent.pageOf", "Page %1 of", "" + getDisplayCurrentPageNumber());

    }

    /**
     * Get the localized message shown to user when user intends to go to a specific page number.
     * @return the message.
     */
    private String getPromptTitleMessage()
    {
        return m_view.getContext().localize("dmsdialogs.views.OwIndividualPageSelectionComponent.promptTitle", "Jump to page...");
    }

    /**
     * Get localized message for page number selection
     * @return - the message
     */
    private String getSelectPageNumberMessage()
    {
        return "'" + m_view.getContext().localize("dmsdialogs.views.OwIndividualPageSelectionComponent.pageNumberSelection", "Enter the page number you want to go to:") + "'";
    }

    /**
     * Render the number of item information.
     * @param w_p - the writer.
     * @throws Exception - thrown when getCount() method fails
     */
    private void renderNumberOfItems(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            w_p.write("<div class=\"OwIndividualPageSelection\"><span>" + m_view.getCount() + " " + m_view.getContext().localize("dmsdialogs.views.OwIndividualPageSelectionComponent.items", "Items") + "</span>\n");
            w_p.write("</div>\n");
        }
    }

    /**
     * render the last page URL
     * @param w_p - the writer
     * @throws Exception thrown when {@link #getLastPage()} fails.
     */
    private void renderLastPage(Writer w_p) throws Exception
    {
        if (getLastPage() != getLastPageToBeRendered())
        {
            w_p.write("<div class=\"OwIndividualPageSelection\">\n");
            w_p.write("<a href=\"" + getPageAbsolutURL(getLastPage()) + "\">&nbsp;" + getHumanReadablePageNumber(getLastPage()) + "&nbsp;</a>\n");
            w_p.write("</div>\n");
        }

    }

    /**
     * Render the URLs of the pages near the current page.
     * @param w_p - the writer
     * @throws Exception
     */
    private void renderPagesNearCurrentPage(Writer w_p) throws Exception
    {
        if (this.m_numberOfSelectablePages < 1)
        {
            throw new Exception("Invalid configuration: " + m_numberOfSelectablePages);
        }
        if (shouldRenderComponent())
        {
            int currentPage = m_view.getCurrentPage();
            int firstPageToBeRendered = getFirstPageToBeRendered();
            int lastPage = getLastPage();
            int lastPageToBeRendered = getLastPageToBeRendered();
            w_p.write("<div class=\"OwIndividualPageSelection\">\n");
            for (int i = firstPageToBeRendered; i <= lastPageToBeRendered; i++)
            {
                //render pages in front of currentPage - first page and last page are always visible
                if (i == firstPageToBeRendered && firstPageToBeRendered > FIRST_PAGE_DISPLAY_NUMBER)
                {
                    w_p.write("<span>..</span>\n");
                }
                if (i == currentPage)
                {
                    w_p.write("<span class=\"OwIndividualPageSelection_selected\">&nbsp;" + getDisplayCurrentPageNumber() + "&nbsp;</span>\n");
                }
                else
                {
                    w_p.write("<a href=\"" + getPageAbsolutURL(i) + "\">&nbsp;" + getHumanReadablePageNumber(i) + "&nbsp;</a>\n");
                }
                if (i == lastPageToBeRendered && lastPageToBeRendered < lastPage - 1)
                {
                    w_p.write("<span>..</span>\n");
                }
            }
            w_p.write("</div>\n");
        }
    }

    /**
     * Get the number of last page to be rendered near the current page.
     * @return - the page number.
     * @throws Exception
     */
    private int getLastPageToBeRendered() throws Exception
    {
        int lastPage = getLastPage();
        int lastPageToBeRendered = m_view.getCurrentPage() + m_numberOfSelectablePages / 2;
        lastPageToBeRendered = Math.min(lastPageToBeRendered, lastPage);
        int possibleLastPage = m_numberOfSelectablePages / 2 * 2;
        if (lastPageToBeRendered < possibleLastPage && possibleLastPage <= getLastPage())
        {
            lastPageToBeRendered = possibleLastPage;
        }
        return lastPageToBeRendered;
    }

    /**
     * Get the number of first page to be rendered near current page.
     * @return the page number.
     * @throws Exception - thrown when {@link OwPageableView#getCurrentPage()} fails
     */
    private int getFirstPageToBeRendered() throws Exception
    {
        int firstPageToBeRendered = m_view.getCurrentPage() - m_numberOfSelectablePages / 2;
        firstPageToBeRendered = Math.max(0, firstPageToBeRendered);
        int possibleFirstPage = getLastPage() - m_numberOfSelectablePages / 2 * 2;
        if (firstPageToBeRendered > possibleFirstPage && possibleFirstPage >= 0)
        {
            firstPageToBeRendered = possibleFirstPage;
        }
        return firstPageToBeRendered;
    }

    /**
     * Render the first page.
     * @param w_p the writer
     * @throws Exception when {@link OwPageableView#getCurrentPage()} fails.
     */
    private void renderFirstPage(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            if (m_view.getCurrentPage() != 0 && getFirstPageToBeRendered() != 0)
            {
                w_p.write("<div class=\"OwIndividualPageSelection\"><a href=\"" + getPageAbsolutURL(0) + "\">\n");
                w_p.write("&nbsp;" + FIRST_PAGE_DISPLAY_NUMBER + "&nbsp;");
                w_p.write("</a></div>\n");
            }
        }
    }

    /**
     * Initialize the number of pages from configuration.
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageSelectorComponent#init()
     */
    public void init() throws Exception
    {
        super.init();
        Node node = OwXMLDOMUtil.getChildNode(m_configNode, "NumberOfSelectablePages");
        if (node != null)
        {
            String maxSelectablePages = node.getFirstChild().getNodeValue().trim();
            LOG.debug("OwVicinityPageSelectorComponent.init: Maximum number of selectable pages is = " + maxSelectablePages);
            m_numberOfSelectablePages = Integer.parseInt(maxSelectablePages);
        }
        else
        {
            LOG.debug("OwVicinityPageSelectorComponent.init: No <NumberOfSelectablePages> element in the configuration, using default value = " + m_numberOfSelectablePages);
        }
    }
}