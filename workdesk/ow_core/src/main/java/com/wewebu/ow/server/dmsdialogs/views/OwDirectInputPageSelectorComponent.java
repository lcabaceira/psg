package com.wewebu.ow.server.dmsdialogs.views;

import java.io.IOException;
import java.io.Writer;

/**
 *<p>
 * Class for simple page selector component, using images for rendering
 * the links to the first, last, next, previous pages.
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
public class OwDirectInputPageSelectorComponent extends OwSimplePageSelectorComponent
{
    /**
     * Constructor.
     * @param view_p - the pageable view (see {@link OwPageableView})
     */
    public OwDirectInputPageSelectorComponent(OwPageableView view_p)
    {
        super(view_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderFirstPageActiveContent(java.io.Writer)
     */
    protected void renderFirstPageActiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingFirstText", "First Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-first.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderFirstPageInactiveContent(java.io.Writer)
     */
    protected void renderFirstPageInactiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingFirstText", "First Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-first-disabled.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderPreviousPageActiveContent(java.io.Writer)
     */
    protected void renderPreviousPageActiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingPrevText", "Previous Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-prev.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderPreviousPageInactiveContent(java.io.Writer)
     */
    protected void renderPreviousPageInactiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingPrevText", "Previous Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-prev-disabled.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderNextPageInactiveContent(java.io.Writer)
     */
    protected void renderNextPageInactiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingNextText", "Next Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-next-disabled.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderNextPageActiveContent(java.io.Writer)
     */
    protected void renderNextPageActiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingNextText", "Next Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-next.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderLastPageInactiveContent(java.io.Writer)
     */
    protected void renderLastPageInactiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingLastText", "Last Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-last-disabled.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderLastPageActiveContent(java.io.Writer)
     */
    protected void renderLastPageActiveContent(Writer w_p) throws Exception
    {
        String text = m_view.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingLastText", "Last Page");
        String imgSrc = m_view.getContext().getDesignURL() + "/images/page-last.gif";
        renderImage(w_p, text, imgSrc);
    }

    /**
     * render out the image tag. 
     * @param w Writer
     * @param toolTip String
     * @param imageUrl String
     * @throws IOException
     * @since 4.2.0.0
     */
    private void renderImage(Writer w, String toolTip, String imageUrl) throws IOException
    {
        w.write(" <img alt=\"");
        w.write(toolTip);
        w.write("\" title=\"");
        w.write(toolTip);
        w.write("\" src=\"");
        w.write(imageUrl);
        w.write("\" />");
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#getSimplePageSelectorStyleClass()
     */
    protected String getSimplePageSelectorStyleClass()
    {
        return "OwSimplePageSelectorImg";
    }
}