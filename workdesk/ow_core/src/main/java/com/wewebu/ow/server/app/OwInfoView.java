package com.wewebu.ow.server.app;

/**
 *<p>
 * A Info View supports links between several JSP pages. It is used to navigate to information pages like online help. <br/><br/>
 * Calling getLink creates a link to another info page. When submitted it will replace the current page with the new one, 
 * where the new page is rendered inside a specific layout.
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
public interface OwInfoView
{

    /** create a link to another JSP info page
     *
     * @param strPath_p the requested info page
     * @param strName_p the link name to display
     * @param strClass_p the CSS class to use
     *
     * @return String with HTML anchor
     */
    public abstract String getLink(String strPath_p, String strName_p, String strClass_p) throws Exception;
}
