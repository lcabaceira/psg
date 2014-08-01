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
public class OwDirectInputPageSelectorComponentTextBased extends OwSimplePageSelectorComponent
{
    /**
     * Constructor.
     * @param view_p - the pageable view (see {@link OwPageableView})
     */
    public OwDirectInputPageSelectorComponentTextBased(OwPageableView view_p)
    {
        super(view_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderFirstPageActiveContent(java.io.Writer)
     */
    protected void renderFirstPageActiveContent(Writer w_p) throws IOException
    {
        w_p.write("&lt;&lt;");
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderFirstPageInactiveContent(java.io.Writer)
     */
    protected void renderFirstPageInactiveContent(Writer w_p) throws IOException
    {
        renderFirstPageActiveContent(w_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderPreviousPageInactiveContent(java.io.Writer)
     */
    protected void renderPreviousPageInactiveContent(Writer w_p) throws IOException
    {
        renderPreviousPageActiveContent(w_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderPreviousPageActiveContent(java.io.Writer)
     */
    protected void renderPreviousPageActiveContent(Writer w_p) throws IOException
    {
        w_p.write("&nbsp;&lt;&nbsp;");
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderNextPageActiveContent(java.io.Writer)
     */
    protected void renderNextPageActiveContent(Writer w_p) throws IOException
    {
        w_p.write("&nbsp;&gt;&nbsp;");
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderNextPageInactiveContent(java.io.Writer)
     */
    protected void renderNextPageInactiveContent(Writer w_p) throws IOException
    {
        renderNextPageActiveContent(w_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderLastPageActiveContent(java.io.Writer)
     */
    protected void renderLastPageActiveContent(Writer w_p) throws IOException
    {
        w_p.write("&nbsp;&gt;&gt&nbsp;");
    }

    /**
     *  @see com.wewebu.ow.server.dmsdialogs.views.OwSimplePageSelectorComponent#renderLastPageInactiveContent(java.io.Writer)
     */
    protected void renderLastPageInactiveContent(Writer w_p) throws IOException
    {
        renderLastPageActiveContent(w_p);
    }
}