package com.wewebu.ow.server.plug.owhelp;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwInfoView;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Help View.
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
public class OwHelpView extends OwMasterView implements OwInfoView
{
    Vector m_vecHelps = new Vector();
    String m_strLink;

    public class OwHelpCollection
    {
        Vector m_vecPath = new Vector();
        Vector m_vecName = new Vector();

        String m_strDisplayName;
    };

    /** called when the view should create its HTML content to be displayed
    */
    protected void init() throws Exception
    {
        super.init();

        // iterate over the plugin types
        for (int i = 0; i < OwBaseConfiguration.getPluginTypeDefinitions().length; i++)
        {
            setOwHelpCollection(OwBaseConfiguration.getPluginTypeDefinitions()[i].getType(), OwBaseConfiguration.getPluginTypeDefinitions()[i].getDisplayName(getContext().getLocale()) + ":");
        }
    }

    /** called when the view should create its HTML content to be displayed
     * @param strPluginType_p for the plugin name to use
     * @param strDisplayName_p for the name the is to display
     */
    public void setOwHelpCollection(String strPluginType_p, String strDisplayName_p) throws Exception
    {
        List mainPluginsList = getConfiguration().getAllowedPlugins(strPluginType_p);
        if (mainPluginsList != null)
        {
            // section is initially null, lets first see if at least one pluing contains a helppath
            OwHelpCollection mainHelps = null;
            for (int i = 0; i < mainPluginsList.size(); i++)
            {
                OwXMLUtil pluginDescriptor = (OwXMLUtil) mainPluginsList.get(i);
                String strHelpPath = pluginDescriptor.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_HELP, null);
                if (strHelpPath != null)
                {
                    // === found a plugin with help path
                    if (mainHelps == null)
                    {
                        mainHelps = new OwHelpCollection();
                    }

                    mainHelps.m_vecPath.add(strHelpPath);
                    mainHelps.m_vecName.add(getConfiguration().getLocalizedPluginTitle(pluginDescriptor));
                }
            }

            if (mainHelps != null)
            {
                // === a new section was created
                // set display name
                mainHelps.m_strDisplayName = strDisplayName_p;
                // add to list
                m_vecHelps.add(mainHelps);
            }
        }
    }

    /** query key for the help path */
    protected static final String QUERY_STRING_KEY_HELPFILEPATH = "helpfile";

    /** called when user clicked on a help link
     * @param request_p a HttpServletRequest object
     * */
    public void onOpenHelpLink(HttpServletRequest request_p) throws Exception
    {
        m_strLink = request_p.getParameter(QUERY_STRING_KEY_HELPFILEPATH);
    }

    /** called by the framework to render the view
     * @param w_p Writer 
     */
    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div id=\"OwSubLayout_Div\" class=\"OwHelpView\">");

        w_p.write("<div id=\"OwSubLayout_MAIN\"><div class=\"OwHelpView_Chapters\">");

        // === render chapters
        renderChapters(w_p);

        w_p.write("</div>");

        w_p.write("<div class=\"OwHelpView_Text\">");

        // ==== render help text
        renderHelpText(w_p);

        w_p.write("</div>");

        w_p.write("</div></div>");
    }

    /** render the chapters
     * @param w_p Writer 
     */
    protected void renderChapters(Writer w_p) throws Exception
    {
        // === render chapters
        Iterator it = m_vecHelps.iterator();

        w_p.write("<table class='OwChapter'>");

        while (it.hasNext())
        {
            OwHelpCollection helpEntry = (OwHelpCollection) it.next();
            renderChapterIndex(w_p, helpEntry);
        }

        w_p.write("</table>");
    }

    /** set a JSP page to display as help text */
    public void setHelpJsp(String sLink_p)
    {
        m_strLink = "help_" + getContext().getLocale() + "/" + sLink_p;
    }

    /** render the help text
     * @param w_p Writer 
     */
    protected void renderHelpText(Writer w_p) throws Exception
    {
        // ==== render help text
        if (m_strLink != null)
        {
            serverSideInclude(m_strLink, w_p);
        }
        else
        {
            w_p.write(getContext().localize("app.OwHelpDialog.nohelpavailable", "No help available."));
        }
    }

    /** render a chapter index area
     * @param w_p Writer 
     * @param helpEntry_p OwHelpCollection
     */
    protected void renderChapterIndex(Writer w_p, OwHelpCollection helpEntry_p) throws Exception
    {
        w_p.write("<tr>");

        w_p.write("<th class='nowrap'>" + helpEntry_p.m_strDisplayName + "&nbsp;&nbsp;</th>");

        w_p.write("<td>");

        for (int i = 0; i < helpEntry_p.m_vecName.size(); i++)
        {
            w_p.write(getLink(helpEntry_p.m_vecPath.elementAt(i).toString(), helpEntry_p.m_vecName.elementAt(i).toString(), "OwChapter"));

            if (i < (helpEntry_p.m_vecName.size() - 1))
            {
                // delimiter
                w_p.write("<span> | </span>");
            }
        }

        w_p.write("</td>");

        // delimiter
        w_p.write("<td>&nbsp;</td>");

        w_p.write("</tr>");
    }

    /** overwritten from OwInfoView: create a link to another JSP info page
     *
     * @param strHelpPath_p the requested info page
     * @param strName_p the link name to display
     * @param strClass_p the CSS class to use
     *
     * @return String with HTML anchor
     */
    public String getLink(String strHelpPath_p, String strName_p, String strClass_p)
    {
        String url = this.getEventURL("OpenHelpLink", QUERY_STRING_KEY_HELPFILEPATH + "=" + "help_" + getContext().getLocale() + "/" + strHelpPath_p);
        return "<a href='" + url + "' class='" + strClass_p + "'>" + strName_p + "</a>";
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
      * @param iIndex_p <code>int</code> tab index of Navigation 
      * @param oReason_p User Object which was submitted when target was attached to the navigation module
      */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        try
        {
            String sLastMasterPluginId = ((OwMainAppContext) getContext()).getCurrentMasterPluginID();
            if (!sLastMasterPluginId.equals(getPluginID()))
            {
                // === set chapter link
                m_strLink = null;

                // === display help for last masterplugin
                String strContextHelpPath = ((OwMainAppContext) getContext()).getConfiguration().getPlugin(sLastMasterPluginId).getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_HELP, null);
                if (strContextHelpPath != null)
                {
                    m_strLink = "help_" + getContext().getLocale() + "/" + strContextHelpPath;
                }
            }
        }
        catch (Exception e)
        {
            // ignore, no chapter found
        }

        // always call super implementation
        super.onActivate(iIndex_p, oReason_p);
    }
}