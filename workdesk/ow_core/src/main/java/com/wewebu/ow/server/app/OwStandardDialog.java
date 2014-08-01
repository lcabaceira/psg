package com.wewebu.ow.server.app;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Standard Dialog Layout for Workdesk. Displays a close button in the head area.
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
public class OwStandardDialog extends OwDialog
{
    /** the close button region */
    public static final int CLOSE_BTN_REGION = 1;
    /** the main region */
    public static final int MAIN_REGION = 2;
    /** the title region */
    public static final int TITLE_REGION = 3;
    /** the help button region */
    public static final int HELP_BTN_REGION = 4;
    /** the info icon region */
    public static final int INFO_ICON_REGION = 5;
    /** the info icon region */
    public static final int MENU_REGION = 6;
    /** the footer region */
    public static final int FOOTER_REGION = 7;
    /** the left region */
    public static final int LEFT_REGION = 8;
    /** the right region */
    public static final int RIGHT_REGION = 9;

    /** start of overridden regions */
    public static final int STANDARD_DIALOG_REGION_MAX = RIGHT_REGION + 1;

    /** Help path to the JSP help */
    protected String m_strHelpPath;

    /** dialog title */
    protected String m_strTitle;

    /** URL to the info icon to display */
    protected String m_strInfoIconURL;

    /** set the URL to the info icon
     * @param strInfoIconURL_p String
     */
    public void setInfoIcon(String strInfoIconURL_p)
    {
        m_strInfoIconURL = strInfoIconURL_p;
    }

    /** get the URL to the info icon
     * @return String
     */
    public String getInfoIcon()
    {
        return m_strInfoIconURL;
    }

    /** set dialog title
     * @param strTitle_p String
     */
    public void setTitle(String strTitle_p)
    {
        m_strTitle = strTitle_p;
    }

    public String getTitle()
    {
        return m_strTitle;
    }

    /** set the current path to the help for context sensitive online help
     * @param strHelpPath_p Help path to the JSP help
     * */
    public void setHelp(String strHelpPath_p)
    {
        m_strHelpPath = strHelpPath_p;
    }

    /** determine if region contains a view
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case TITLE_REGION:
                return (getTitle() != null);

            case HELP_BTN_REGION:
                // check if help is set
                return hasHelpButton();

            case INFO_ICON_REGION:
                // check if URL to info icon is set
                return (m_strInfoIconURL != null);

            default:
                return super.isRegion(iRegion_p);
        }

    }

    /** check if the help is set
     * @return true = help is set, false otherwise
     */
    public boolean hasHelpButton()
    {
        return (m_strHelpPath != null);
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
                if (getTitle() != null)
                {
                    w_p.write("<span class=\"OwStandardDialog_TITLE\">");
                    w_p.write(getTitle());
                    w_p.write("</span>");
                }
                break;

            case INFO_ICON_REGION:
                // render the icon
                if (m_strInfoIconURL != null)
                {
                    w_p.write("<span class=\"OwStandardDialog_icon\" style=\"background-image: url(");
                    w_p.write(m_strInfoIconURL);
                    w_p.write(")\">");
                    w_p.write("</span>");
                }
                break;

            case HELP_BTN_REGION:
                // render the help button
                if (hasHelpButton())
                {
                    renderHelpButton(w_p);
                }
                break;

            case CLOSE_BTN_REGION:
            {
                // render close button
                renderCloseButton(w_p);
            }
                break;

            default:
                // render registered views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /**
     * render Help button
     * @param w_p
     * @throws Exception
     * @since 3.1.0.0
     */
    public void renderHelpButton(Writer w_p) throws Exception
    {
        String helpToolTip = getContext().localize("app.OwStandardDialog.helptooltip", "Help for Dialog");
        helpToolTip = OwHTMLHelper.encodeToSecureHTML(helpToolTip);
        w_p.write("<a href=\"");
        w_p.write(getEventURL("Help", null));
        w_p.write("\" title=\"");
        w_p.write(helpToolTip);
        w_p.write("\">");

        w_p.write("<span class=\"OwStandardDialog_icon\" style=\"background-image: url(");
        w_p.write(getContext().getDesignURL() + "/images/OwStandardDialog/help.png");
        w_p.write(")\">");
        w_p.write("</span>");
        w_p.write("<span class='OwStandardDialog_text'>");
        w_p.write(helpToolTip + "<br/>");
        w_p.write("</span>");
        w_p.write("</a>");
    }

    /**
     *  render Close button
     * @param w_p
     * @throws Exception
     * @since 3.1.0.0
     */
    public void renderCloseButton(Writer w_p) throws Exception
    {
        String strEventURL = getEventURL("Close", null);
        String strToolTip = getContext().localize("app.OwStandardDialog.closetooltip", "Close Dialog");
        strToolTip = OwHTMLHelper.encodeToSecureHTML(strToolTip);
        w_p.write("<a href=\"");
        w_p.write(strEventURL);
        w_p.write("\" title=\"");
        w_p.write(strToolTip);
        // render the closeViewerWindow function for close button
        w_p.write("\" onclick=\"closeViewerWindow()\">");

        w_p.write("<span class=\"OwStandardDialog_icon\" style=\"background-image: url(");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/OwStandardDialog/back.png");
        w_p.write(")\">");
        w_p.write("</span>");
        w_p.write("<span class='OwStandardDialog_text'>");
        w_p.write(strToolTip + "<br/>");
        w_p.write("</span>");
        w_p.write("</a>");

        // register key event if not explicitly disabled by the user
        boolean isShortcutDisabled = ((OwMainAppContext) this.getContext()).getSafeBooleanAppSetting("DisableKeyboardShortcutForDialogClosing", false);
        if (!isShortcutDisabled)
        {
            getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_ESC, 0, strEventURL, strToolTip);
        }
    }

    /** event called when user clicked help on the dialog
     *   @param request_p  HttpServletRequest
     */
    public void onHelp(HttpServletRequest request_p) throws Exception
    {
        if (hasHelpButton())
        {
            ((OwMainAppContext) getContext()).openDialog(new OwHelpDialog(getTitle(), m_strHelpPath), null);
        }
    }

    /** event called when user clicked a close on the dialog
     *   @param request_p  HttpServletRequest
     */
    public void onClose(HttpServletRequest request_p) throws Exception
    {
        closeDialog();
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwStandardDialog.jsp", w_p);
    }

}