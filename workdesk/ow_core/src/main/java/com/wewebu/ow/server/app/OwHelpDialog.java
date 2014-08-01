package com.wewebu.ow.server.app;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 *<p>
 * Standard Dialog to display a help page within other dialogs.
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
public class OwHelpDialog extends OwStandardDialog implements OwInfoView
{

    /** query key for the help path */
    protected static final String QUERY_STRING_KEY_HELPFILEPATH = "helpfile";

    /** history if help pathes */
    private List m_helppathehistory = new ArrayList();

    /** constructor set the help to display
     * @param strTitle_p Title to display
     * @param strHelpPath_p Help path to the JSP help
     */
    public OwHelpDialog(String strTitle_p, String strHelpPath_p)
    {
        setTitle(strTitle_p);

        m_helppathehistory.add(strHelpPath_p);
    }

    /** check if the help is set
     *  overridden from OwStandardDialog
     * @return always false, the help view must not contain a help button.
     */
    public boolean hasHelpButton()
    {
        return false;
    }

    /** determine if region exists
    *
    * @param iRegion_p ID of the region to render
    * @return true if region contains anything and should be rendered
    */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
            case TITLE_REGION:
                return true;

            case MAIN_REGION:
                return true;

            case MENU_REGION:
                return (m_helppathehistory.size() > 1);
        }

        return super.isRegion(iRegion_p);
    }

    /** render the views of the region
      * @param w_p Writer object to write HTML to
      * @param iRegion_p ID of the region to render
      */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case TITLE_REGION:
                w_p.write(getContext().localize("app.OwHelpDialog.title", "Help for dialog:") + m_strTitle);
                break;

            case MAIN_REGION:

                w_p.write("<div id=\"OwMainContent\">");
                // show last stored JSP from history
                serverSideInclude("help_" + getContext().getLocale() + "/" + m_helppathehistory.get(m_helppathehistory.size() - 1), w_p);
                w_p.write("</div>");
                break;

            case MENU_REGION:
                w_p.write("<a class=\"OwHelpLink\" href=\"" + getEventURL("Back", null) + "\">" + getContext().localize("app.OwHelpDialog.back", "Back") + "</a>");
                break;

            default:
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** called when user clicked the back button
     * @param request_p a HttpServletRequest object
     * */
    public void onBack(HttpServletRequest request_p) throws Exception
    {
        // remove last one
        if (m_helppathehistory.size() > 1)
        {
            m_helppathehistory.remove(m_helppathehistory.size() - 1);
        }
    }

    /** called when user clicked on a help link
     * @param request_p a HttpServletRequest object
     * */
    public void onOpenHelpLink(HttpServletRequest request_p) throws Exception
    {
        // add help path to history
        m_helppathehistory.add(request_p.getParameter(QUERY_STRING_KEY_HELPFILEPATH));
    }

    /** create a link to another JSP info page
     *
     * @param strPath_p the requested info page
     * @param strName_p the link name to display
     * @param strClass_p the CSS class to use
     *
     * @return String with HTML anchor
     */
    public String getLink(String strPath_p, String strName_p, String strClass_p) throws Exception
    {
        String url = this.getEventURL("OpenHelpLink", QUERY_STRING_KEY_HELPFILEPATH + "=" + strPath_p);
        return "<a href=\"" + url + "\" class=\"" + strClass_p + "\">" + strName_p + "</a>";
    }

}