package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;

import com.wewebu.ow.server.app.OwMainAppContext;

/**
 *<p>
 * Base class for simple page selector component. 
 * This component provide support for navigation to the first, the last, and to a given page number.
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
public abstract class OwSimplePageSelectorComponent extends OwPageSelectorComponent
{
    /**
     * Constructor
     */
    public OwSimplePageSelectorComponent(OwPageableView view_p)
    {
        super(view_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageSelectorComponent#render(java.io.Writer)
     */
    public void render(Writer w_p) throws Exception
    {
        w_p.write("<!--writing paging navigation component (");
        w_p.write(this.getClass().getSimpleName());
        w_p.write(")-->\n");
        if (hasPaging() && shouldRenderComponent())
        {
            w_p.write("<div id=\"OwNextResults_page\">\n");
            renderAccessibilityMark(w_p);

            renderFirstPage(w_p);
            renderPreviousPage(w_p);

            renderCurrentPage(w_p);

            renderNextPage(w_p);
            renderLastPage(w_p);

            renderNumberOfItems(w_p);
            w_p.write("</div>\n");
        }
        w_p.write("<!-- END writing paging navigation component (");
        w_p.write(this.getClass().getSimpleName());
        w_p.write(")-->\n");
    }

    /**
     * render last page link.
     * @param w_p - the writer
     * @throws Exception 
     */
    private void renderLastPage(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            w_p.write("<div class=\"" + getSimplePageSelectorStyleClass() + "\">\n");
            if (m_view.getCurrentPage() < m_view.getPageCount() - 1)
            {
                w_p.write("<a href=\"" + getPageAbsolutURL(m_view.getPageCount() - 1) + "\">\n");
                renderLastPageActiveContent(w_p);
                w_p.write("</a>\n");
            }
            else
            {
                renderLastPageInactiveContent(w_p);

            }
            w_p.write("</div>\n");
        }
    }

    /**
     * render next page URL
     * @param w_p - the writer
     * @throws Exception - thrown when something goes wrong.
     */
    private void renderNextPage(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            w_p.write("<div class=\"" + getSimplePageSelectorStyleClass() + "\">\n");
            if (m_view.canPageNext())
            {
                w_p.write("<a href=\"" + getPageNextURL() + "\">\n");
                renderNextPageActiveContent(w_p);
                w_p.write("</a>\n");
            }
            else
            {
                renderNextPageInactiveContent(w_p);
            }
            w_p.write("</div>\n");
        }
    }

    /**
     * Render current page. In case that the user enter a non valid value for a page to navigate to,
     * the current page doesn't change.
     * @param w_p - the writer
     * @throws Exception - thrown when something failed.
     */
    private void renderCurrentPage(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            String locationURL = m_view.getPageAbsolutEventURL(OwObjectListView.QUERY_KEY_PAGE + "=");
            if (locationURL == null)
            {
                locationURL = "null";
            }
            String lastPage = "" + getHumanReadablePageNumber(getLastPage());
            String currentPage = "" + getHumanReadablePageNumber(m_view.getCurrentPage());
            w_p.write("<div class=\"" + getSimplePageSelectorStyleClass() + "\">\n");
            w_p.write("<input id=\"navigationPage\" title=\"");
            w_p.write(m_view.getContext().localize("dmsdialogs.views.OwIndividualPageSelectionComponent.OwSimplePageSelectorComponent.current.page.title", "Current page"));
            w_p.write("\" type=\"text\" value=\"");
            w_p.write(Integer.toString(getDisplayCurrentPageNumber()));
            w_p.write("\" onblur='jumpTo(\"");
            w_p.write(locationURL);
            w_p.write("\",");
            w_p.write(currentPage);
            w_p.write(",");
            w_p.write(lastPage);
            w_p.write(");' onkeydown='processkey(event,this.form,\"");
            w_p.write(locationURL);
            w_p.write("\",");
            w_p.write(currentPage);
            w_p.write(",");
            w_p.write(lastPage);
            w_p.write(");' size=\"");
            w_p.write(Integer.toString(calculateInputFieldSize()));
            w_p.write("\" />\n");
            w_p.write("</div>\n");
        }
    }

    /**
     * helper method, for rendering the max size for the input field.
     * @return the needed size of input field 
     * @throws Exception - thrown when getLastPage() method failed.
     */
    private int calculateInputFieldSize() throws Exception
    {
        int lastPage = getLastPage();
        return ("" + lastPage).length();
    }

    /**
     * Render the number of items available
     * @param w_p
     * @throws Exception
     */
    private void renderNumberOfItems(Writer w_p) throws Exception
    {
        if (shouldRenderComponent() && m_view.getCount() >= 0)
        {
            w_p.write("<div class=\"OwSimplePageSelectorSeparator\"></div>");
            w_p.write("<div class=\"" + getSimplePageSelectorStyleClass() + "\">\n");
            int pageSize = ((OwMainAppContext) m_view.getContext()).getPageSizeForLists();
            String firstItem = "" + (m_view.getCurrentPage() * pageSize + 1);
            int endIndex = (m_view.getCurrentPage() + 1) * pageSize;
            if (endIndex >= m_view.getCount())
            {
                endIndex = m_view.getCount();
            }
            String lastItem = "" + endIndex;
            String message = m_view.getContext().localize3("dmsdialogs.views.OwIndividualPageSelectionComponent.OwSimplePageSelectorComponent.pagingDisplayMsg", "Displaying items %1 - %2 of %3", firstItem, lastItem, "" + m_view.getCount());
            w_p.write(message);
            w_p.write("</div>\n");
        }
    }

    /**
     * Render the previous page
     * @param w_p - the writer 
     * @throws Exception - thrown when something goes wrong
     */
    private void renderPreviousPage(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            w_p.write("<div class=\"" + getSimplePageSelectorStyleClass() + "\">\n");
            if (m_view.canPagePrev())
            {
                w_p.write("<a href=\"" + getPagePrevURL() + "\">\n");
                renderPreviousPageActiveContent(w_p);
                w_p.write("</a>\n");
            }
            else
            {
                renderPreviousPageInactiveContent(w_p);
            }
            w_p.write("</div>\n");
        }

    }

    /**
     * Render the first page link, or, if the current page is the first page, only an inactive symbol (text or image)
     * @param w_p the writer
     * @throws Exception thrown when something goes wrong
     */
    private void renderFirstPage(Writer w_p) throws Exception
    {
        if (shouldRenderComponent())
        {
            w_p.write("<div class=\"" + getSimplePageSelectorStyleClass() + "\">\n");
            if (m_view.getCurrentPage() == 0)
            {
                renderFirstPageInactiveContent(w_p);
            }
            else
            {
                w_p.write("<a href=\"" + getPageAbsolutURL(0) + "\">\n");
                renderFirstPageActiveContent(w_p);
                w_p.write("</a>\n");
            }
            w_p.write("</div>\n");
        }
    }

    /**
     * Get the name of the page selector style class 
     * @return - the name of style class
     */
    protected String getSimplePageSelectorStyleClass()
    {
        return "OwSimplePageSelector";
    }

    /**
     * Render inactive content (a symbol or an image) for the first page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderFirstPageInactiveContent(Writer w_p) throws Exception;

    /**
     * Render active content (a symbol or an image) for the first page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderFirstPageActiveContent(Writer w_p) throws Exception;

    /**
     * Render inactive content (a symbol or an image) for the previous page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderPreviousPageInactiveContent(Writer w_p) throws Exception;

    /**
     * Render active content (a symbol or an image) for the previous page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderPreviousPageActiveContent(Writer w_p) throws Exception;

    /**
     * Render inactive content (a symbol or an image) for the next page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderNextPageInactiveContent(Writer w_p) throws Exception;

    /**
     * Render active content (a symbol or an image) for the next page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderNextPageActiveContent(Writer w_p) throws Exception;;

    /**
     * Render inactive content (a symbol or an image) for the last page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderLastPageInactiveContent(Writer w_p) throws Exception;

    /**
     * Render active content (a symbol or an image) for the last page.
     * @param w_p - the writer
     * @throws Exception - thrown when this method fails.
     */
    protected abstract void renderLastPageActiveContent(Writer w_p) throws Exception;
}