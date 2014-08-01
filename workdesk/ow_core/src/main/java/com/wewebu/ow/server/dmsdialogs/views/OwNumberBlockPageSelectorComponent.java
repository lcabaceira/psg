package com.wewebu.ow.server.dmsdialogs.views;

import java.io.IOException;
import java.io.Writer;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Page navigation component, that render each page URL as a group of first and the last item number displayed on that page.
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
public class OwNumberBlockPageSelectorComponent extends OwPageSelectorComponent
{
    /**
     * flag indicating if images are used for rendering next and previous page.
     */
    private boolean m_useImages = true;

    /**
     * Constructor
     * @param view_p - the {@link OwPageableView}
     */
    public OwNumberBlockPageSelectorComponent(OwPageableView view_p)
    {
        super(view_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageSelectorComponent#render(java.io.Writer)
     */
    public void render(Writer w_p) throws Exception
    {
        if (hasPaging() && shouldRenderComponent())
        {
            int iPageSize = ((OwMainAppContext) m_view.getContext()).getPageSizeForLists();

            w_p.write("<div class=\"OwNumberBlockPageSelector\">\n");

            renderAccessibilityMark(w_p);

            if (m_view.canPagePrev())
            {
                String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingPrevBlockText", "Previous Pages");
                renderStep(w_p, false, text);
            }
            int iPageCount = m_view.getPageCount();
            int iCurrentPage = m_view.getCurrentPage();

            if (iPageCount > 1)
            {
                for (int iPage = 0; iPage < iPageCount; iPage++)
                {
                    // === compute page set (start and end index for this page)
                    int iStartIndex = iPage * iPageSize + 1;
                    int iEndIndex = (iPage + 1) * iPageSize;
                    if (iEndIndex >= m_view.getCount())
                    {
                        iEndIndex = m_view.getCount();
                    }
                    renderPageLocation(w_p, iStartIndex, iEndIndex, iCurrentPage, iPage);
                }
            }
            else if (iPageCount == -1)
            {
                int iStartIndex = iCurrentPage * iPageSize + 1;
                int iEndIndex = iStartIndex + iPageSize;
                renderPageLocation(w_p, iStartIndex, iEndIndex, iCurrentPage, iCurrentPage);
            }

            if (m_view.canPageNext())
            {
                String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingNextBlockText", "Next Pages");
                renderStep(w_p, true, text);
            }
            w_p.write(" </div>");
        }
    }

    /**
     * Render a page location representation.
     * <p>
     * By default a representation like
     * <pre>startIdx..endIdx</pre>
     * is used, and a link is created if location is not current page <code>currentPage != renderedPage</code>.
     * </p>
     * @param w Writer
     * @param startIdx integer count from which the page is starting
     * @param endIdx integer count from which
     * @param currentPage current page of view
     * @param renderedPage page of rendered location
     * @throws IOException
     * @since 4.2.0.0
     */
    protected void renderPageLocation(Writer w, int startIdx, int endIdx, int currentPage, int renderedPage) throws IOException
    {
        StringBuilder strPageSet = new StringBuilder();
        strPageSet.append(startIdx);
        strPageSet.append("..");
        strPageSet.append(endIdx);
        if (currentPage == renderedPage)
        {
            w.write("<div  class=\"OwNumberBlockPageSelector_page_selected\">");
            w.write(strPageSet.toString());
            w.write("</div>\n");
        }
        else
        {
            w.write("<div class=\"OwNumberBlockPageSelector_page\"><a href=\"" + getPageAbsolutURL(renderedPage) + "\">\n");
            w.write(strPageSet.toString());
            w.write("</a></div>\n");
        }
    }

    /**
     * Render the page previous and next symbols/images.
     * @param w_p Writer
     * @param stepNext boolean which symbol to render
     * @param toolTip String tool tip messag to display
     * @throws Exception
     * @since 4.2.0.0
     */
    protected void renderStep(Writer w_p, boolean stepNext, String toolTip) throws Exception
    {
        w_p.write("<div class=\"OwNumberBlockPageSelector_page\">");
        w_p.write("<a title=\"");
        w_p.write(toolTip);
        w_p.write("\" href=\"");
        w_p.write(stepNext ? getPageNextURL() : getPagePrevURL());
        w_p.write("\">\n");
        if (useImages())
        {
            w_p.write("<img title=\"");
            w_p.write(toolTip);
            w_p.write("\" alt=\"");
            w_p.write(toolTip);
            w_p.write("\" src=\"");
            w_p.write(m_view.getContext().getDesignURL());
            if (stepNext)
            {
                w_p.write("/images/navigate_right_blue.png\" />");
            }
            else
            {
                w_p.write("/images/navigate_left_blue.png\" />");
            }
        }
        else
        {
            if (stepNext)
            {
                w_p.write("&gt;");
            }
            else
            {
                w_p.write("&lt;");
            }
        }
        w_p.write("</a></div>\n");
    }

    /**
     * Helper to get configuration information.
     * Should images be rendered instead of symbols.
     * @return boolean
     * @since 4.2.0.0
     */
    protected boolean useImages()
    {
        return this.m_useImages;
    }

    /**
     * Initialize from configuration.
     */
    public void init() throws Exception
    {
        if (m_configNode != null)
        {
            Node node = OwXMLDOMUtil.getChildNode(m_configNode, "UseImages");
            if (node != null)
            {
                String useImages = node.getFirstChild().getNodeValue().trim();
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwNumberBlockPageSelectorComponent.init: Read from the configuration, UseImages = " + useImages);
                }
                m_useImages = Boolean.valueOf(useImages).booleanValue();
            }
            else
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwNumberBlockPageSelectorComponent.init: No <UseImages> element found in the configuration, using default value = " + m_useImages);
                }
            }
        }
        else
        {
            LOG.debug("OwNumberBlockPageSelectorComponent.init: No config node set! Using the default paging component...");
        }
    }
}