package com.wewebu.ow.server.plug.owinterop;

import java.io.Writer;

import com.wewebu.ow.server.app.OwMasterView;

/**
 *<p>
 * Interoperation Master Plugin to display a JSP page.
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
public class OwINTOPJspMasterView extends OwMasterView
{
    /** path to a JSP page in the design that should be  rendered   */
    protected String m_strJspPage;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // set the path to a JSP page in the design that should be  rendered
        m_strJspPage = this.getConfigNode().getSafeTextValue("JspPage", null);
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (null != m_strJspPage)
        {
            serverSideDesignInclude(m_strJspPage, w_p);
        }
        else
        {
            w_p.write("Bitte setzen Sie \"JspPage\" im plugin descriptor.");
        }
    }
}