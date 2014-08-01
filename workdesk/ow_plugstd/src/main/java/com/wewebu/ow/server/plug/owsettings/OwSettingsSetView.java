package com.wewebu.ow.server.plug.owsettings;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSettings;
import com.wewebu.ow.server.app.OwSettingsProperty;
import com.wewebu.ow.server.app.OwSettingsPropertyControl;
import com.wewebu.ow.server.app.OwSettingsSet;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Settings Set View.
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
public class OwSettingsSetView extends OwLayout
{
    /** logger for this class*/
    private static final Logger LOG = OwLog.getLogger(OwSettingsSetView.class);

    protected static final String HELP_PATH = "helppath";

    /** list with user items*/
    protected List m_UserItemList = new ArrayList();

    /** name of the button / menu region */
    public static final int BUTTON_REGION = 1;
    /** name of the button / menu region */
    public static final int MAIN_REGION = 2;

    /** Errors region */
    public static final int ERRORS_REGION = 3;

    /** settingset node  of the selected plugin */
    protected OwSettingsSet m_settingsSet;

    /** the buttons for the search form */
    protected OwSubMenuView m_MenuView;

    /** reference to the settings */
    protected OwSettings m_Settings;

    /** has user admin rights ? */
    protected boolean m_fAdmin;

    /** called when the view should create its HTML content to be displayed
     */
    protected void init() throws Exception
    {
        super.init();

        // get settings object from context
        m_Settings = ((OwMainAppContext) getContext()).getSettings();

        // === create menu for search form buttons
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, BUTTON_REGION, null);

        // === add buttons
        // search button
        int defaultButton = m_MenuView.addFormMenuItem(this, getContext().localize("owsettings.OwSettingsSetView.apply", "Save"), "Apply", null);
        m_MenuView.setDefaultMenuItem(defaultButton);

        // reset button
        m_MenuView.addMenuItem(this, getContext().localize("owsettings.OwSettingsSetView.standard", "Default Values"), "Reset", null);

        // is user administrator ? 
        m_fAdmin = ((OwMainAppContext) getContext()).isAllowed(OwRoleManager.ROLE_CATEGORY_STANDARD_FUNCTION, OwRoleManager.STD_FUNC_CAN_EDIT_SITE_SETTINGS);
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {

        renderRegion(w_p, ERRORS_REGION);

        renderRegion(w_p, MAIN_REGION);

        w_p.write("<div class=\"OwButtonBar\">");

        renderRegion(w_p, BUTTON_REGION);

        w_p.write("</div>");

    }

    /** to get additional form attributes used for the form
     *  override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        // default does not use form
        return "";
    }

    /** called when the view should create its HTML content to be displayed
      */
    protected void renderMainRegion(Writer w_p) throws Exception
    {

        // === render the user settings
        if (m_settingsSet.hasUserEditableProperties())
        {
            w_p.write("<fieldset>");
            String userSettings = getContext().localize("owsettings.OwSettingsSetView.usersettitle", "User Settings");
            w_p.write("<legend accesskey=\"" + userSettings.charAt(0) + "\">" + "<span class=\"OwSettingsSetView_subtitle\">" + userSettings + "</span>" + "</legend>");
            w_p.write("<div class=\"OwPropertyBlockLayout\">");
            renderSettingsSet(w_p, true);
            w_p.write("</div>");
            w_p.write("</fieldset>");
        }

        // === render the site settings for administrators only
        if (m_fAdmin && m_settingsSet.hasAppEditableProperties())
        {
            w_p.write("<fieldset>");
            String siteSettings = getContext().localize("owsettings.OwSettingsSetView.appsettitle", "Site Settings");
            w_p.write("<legend accesskey=\"" + siteSettings.charAt(0) + "\">" + "<span class=\"OwSettingsSetView_subtitle\">" + siteSettings + "</span>" + "</legend>");
            w_p.write("<div class=\"OwPropertyBlockLayout\">");
            renderSettingsSet(w_p, false);
            w_p.write("</div>");
            w_p.write("</fieldset>");
        }

    }

    /** render the view
     * @param w_p Writer object to write HTML to
     * @param fUser_p boolean
     */
    protected void renderSettingsSet(Writer w_p, boolean fUser_p) throws Exception
    {
        Iterator it = m_settingsSet.getProperties().values().iterator();

        boolean fOdd = false;
        while (it.hasNext())
        {
            OwSettingsProperty property = (OwSettingsProperty) it.next();

            // filter
            if (property.isEditable() && (property.isUser() == fUser_p))
            {
                // toggle odd even row
                fOdd = fOdd ? false : true;

                if (fOdd)
                {
                    w_p.write("<div class=\"OwBlockOdd OwPropertyBlock\">");
                }
                else
                {
                    w_p.write("<div class=\"OwBlockEven OwPropertyBlock\">");
                }

                w_p.write("<div class=\"OwPropertyLabel\">");
                ((OwSettingsPropertyControl) property).insertLabel(w_p);
                w_p.write("</div>\n");

                w_p.write("<div class=\"OwPropertyValue\">\n");

                // === render property edit field
                w_p.write("<div class=\"OwPropertyControl\">");
                ((OwSettingsPropertyControl) property).insertFormField(w_p);
                w_p.write("</div>\n");

                // === render help path icon
                String sHelpPath = ((OwSettingsPropertyControl) property).getHelpPath();
                if (null != sHelpPath)
                {
                    w_p.write("<div class=\"OwPropertyHelpPath\">");

                    w_p.write("<a title=\"");
                    String helpTooltip = getContext().localize("plug.OwSettingsSetView.helpbtntooltip", "Help");
                    w_p.write(helpTooltip);
                    w_p.write("\" href=\"");
                    w_p.write(getEventURL("OpenHelp", HELP_PATH + "=" + sHelpPath));
                    w_p.write("\"><img src=\"");
                    w_p.write(getContext().getDesignURL());
                    w_p.write("/images/helpicon.png\"");
                    w_p.write(" alt=\"");
                    w_p.write(helpTooltip);
                    w_p.write("\" title=\"");
                    w_p.write(helpTooltip);
                    w_p.write("\"/></a>");

                    w_p.write("</div>\n");
                }

                // === display error column for this property 
                w_p.write("<div class=\"OwPropertyError\" style=\"max-width:270px;\">");
                if (((OwSettingsPropertyControl) property).hasError())
                {
                    w_p.write(((OwSettingsPropertyControl) property).getPropertyError());
                }
                w_p.write("</div>\n");

                w_p.write("</div>");
                w_p.write("</div>");

            }
        }
    }

    /** called when user clicked a help icon
     * 
     * @param request_p
     * @throws Exception
     */
    public void onOpenHelp(HttpServletRequest request_p) throws Exception
    {
        String sHelpPath = request_p.getParameter(HELP_PATH);

        ((OwMainAppContext) getContext()).openHelp(sHelpPath);
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case ERRORS_REGION:
                renderErrorsRegion(w_p);
                break;
            // === render internal regions here
            case MAIN_REGION:
                renderMainRegion(w_p);
                break;

            default:
                // delegate to base class to render other attached views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /**
     * Render errors regions, if there are any errors.
     * @param w_p - the {@link Writer} object.
     * @throws IOException
     * @since 3.0.0.0 
     */
    protected void renderErrorsRegion(Writer w_p) throws IOException
    {
        Iterator it = m_settingsSet.getProperties().values().iterator();
        //collect errors
        List errors = new LinkedList();
        while (it.hasNext())
        {
            OwSettingsProperty property = (OwSettingsProperty) it.next();
            if (property instanceof OwSettingsPropertyControl)
            {
                if (((OwSettingsPropertyControl) property).hasError())
                {
                    errors.add((property));
                }
            }
        }
        if (errors.size() > 0)
        {
            StringBuffer errorsBuffer = new StringBuffer();

            errorsBuffer.append("<div name=\"Errors\" class=\"OwAllFieldErrors\">");
            for (int i = 0; i < errors.size(); i++)
            {
                OwSettingsPropertyControl propertyControl = (OwSettingsPropertyControl) errors.get(i);
                if (i == 0)
                {
                    if (errors.size() > 1)
                    {
                        errorsBuffer.append(getContext().localize("owsettings.OwSettingsSetView.errors", "Errors:"));
                        errorsBuffer.append("<br/>");
                    }
                }
                else
                {
                    errorsBuffer.append("<br/>");
                }
                errorsBuffer.append(getContext().localize1("app.OwFieldManager.errorForField", "Error for %1:", propertyControl.getDisplayName()));
                errorsBuffer.append("&nbsp;");
                StringWriter writer = new StringWriter();

                OwHTMLHelper.writeSecureHTML(writer, propertyControl.getPropertyError());

                errorsBuffer.append(writer.toString());
            }
            errorsBuffer.append("</div>");
            w_p.write(errorsBuffer.toString());
        }
    }

    /** update the target after a form event, so it can set its form fields
     *
     * @param request_p HttpServletRequest
     * @param fSave_p boolean true = save the changes of the form data, false = just update the form data, but do not save
     *
     * @return true = field data was valid, false = field data was invalid
     */
    public boolean updateExternalFormTarget(javax.servlet.http.HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        // Delegate to property controls
        Iterator it = m_settingsSet.getProperties().values().iterator();
        while (it.hasNext())
        {
            OwSettingsProperty property = (OwSettingsProperty) it.next();
            if (property.isEditable() && (property.isUser() || m_fAdmin))
            {
                ((OwSettingsPropertyControl) property).updateExternalFormTarget(request_p, fSave_p);
            }
        }

        return true;
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     * @param iIndex_p <code>int</code> tab index of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        m_settingsSet = (OwSettingsSet) oReason_p;

        // Make us the form provider
        Iterator it = m_settingsSet.getProperties().values().iterator();
        while (it.hasNext())
        {
            OwSettingsProperty property = (OwSettingsProperty) it.next();
            if (property.isEditable())
            {
                ((OwSettingsPropertyControl) property).setExternalFormTarget(this);
            }
        }
    }

    /** event called when user clicked apply
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onApply(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // === iterate over the settings and apply changes from the HTML form
        Iterator it = m_settingsSet.getProperties().values().iterator();
        boolean errorsEncopuntered = false;
        StringBuffer errorCollector = new StringBuffer();

        while (it.hasNext())
        {
            OwSettingsPropertyControl property = (OwSettingsPropertyControl) it.next();

            if (property.isEditable() && (property.isUser() || m_fAdmin))
            {
                property.updateExternalFormTarget(request_p, true);
                if (property.hasError())
                {
                    if (!errorsEncopuntered)
                    {
                        errorsEncopuntered = true;
                    }
                    if (errorCollector.length() > 0)
                    {
                        errorCollector.append("; ");
                    }
                    errorCollector.append("For property: " + property.getDisplayName() + " ->");
                    errorCollector.append(property.getPropertyError());
                }
            }
        }
        if (errorsEncopuntered)
        {
            LOG.error("The following errors occured: " + errorCollector.toString());
            // === notify documents
            getContext().broadcast(this, OwUpdateCodes.UPDATE_SETTINGS);
        }
        else
        {
            // === persist changes
            // user scope
            ((OwSettingsDocument) getDocument()).safeUserSettings();
            if (m_fAdmin)
            {
                // site scope 
                ((OwSettingsDocument) getDocument()).safeSiteSettings();
            }

            // === notify documents
            getContext().broadcast(this, OwUpdateCodes.UPDATE_SETTINGS);

            // === info for user
            ((OwMainAppContext) getContext()).postMessage(getContext().localize("owsettings.OwSettingsSetView.settingssaved", "Settings have been saved. Please log out and log in again to make these changes effective."));
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Settings saved successfully");
            }
        }
    }

    /** event called when user clicked Reset
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onReset(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // === iterate over the settings and apply changes from the HTML form
        // === iterate over the settings and apply changes from the HTML form
        Iterator it = m_settingsSet.getProperties().values().iterator();
        while (it.hasNext())
        {
            OwSettingsProperty property = (OwSettingsProperty) it.next();

            // set property to default value
            property.setDefault();
        }

        // === info for user
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("owsettings.OwSettingsSetView.settingsreset", "Settings have been reset."));

    }
}