package com.wewebu.ow.server.app;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.wewebu.ow.server.ecm.ui.OwUISubModul;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.util.OwExceptionManager;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Login View Module for the Application. Used to log user on and create the Credentials.
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
public class OwLoginView extends OwLayout
{
    /** name of the login part region */
    public static final int LOGINPART_REGION = 1;

    /** name of the login info region. */
    public static final int ERROR_REGION = 2;

    /** language selection*/
    public static final int LOCALS_REGION = 3;

    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;
    /** application m_Configuration reference */
    protected OwConfiguration m_Configuration;

    /** reference to the DMS specific login UI */
    protected OwUISubModul m_LoginSubModul;

    /** ID of the role select combobox */
    private static final String LANG_SELECT_ID = "language_select";

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // get and cast appcontext
        m_MainContext = (OwMainAppContext) getContext();
        // get application m_Configuration
        m_Configuration = m_MainContext.getConfiguration();

        // create login sub module from DMS Adaptor
        m_LoginSubModul = m_MainContext.getNetwork().getLoginSubModul();

        addView(m_LoginSubModul, LOGINPART_REGION, null);
    }

    /** determine if region contains a view or must be rendered
     * @param iRegion_p ID of the region
     */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
            case LOCALS_REGION:
                return (((OwMainAppContext) getContext()).getConfiguration().displayLanguageSelection());

            case ERROR_REGION:
            {
                return (m_MainContext.getError() != null);
            }

            default:
            {
                // === check registered regions
                return super.isRegion(iRegion_p);
            }
        }
    }

    /** render the locales select region to switch languages by user.
     */
    public void renderLocalsRegion(Writer w_p) throws Exception
    {
        if (isRegion(LOCALS_REGION))
        {

            String strLang = null;
            Iterator<?> it = ((OwMainAppContext) getContext()).getConfiguration().getAvailableLanguages().iterator();
            List<OwComboItem> lang = new LinkedList<OwComboItem>();

            String strSelectedLang = "";

            while (it.hasNext())
            {
                org.w3c.dom.Node langNode = (org.w3c.dom.Node) it.next();

                strLang = langNode.getFirstChild().getNodeValue();
                String strDisplayName = OwXMLDOMUtil.getSafeStringAttributeValue(langNode, "displayname", strLang);
                OwComboItem item = new OwDefaultComboItem(strLang, strDisplayName);
                lang.add(item);

            }

            if (lang.size() > 1)
            {
                strSelectedLang = ((OwMainAppContext) getContext()).getLocale().getLanguage();
                OwComboModel comboModel = new OwDefaultComboModel(false, false, strSelectedLang, lang);
                OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(comboModel, LANG_SELECT_ID, null, null, null);

                renderer.addEvent("onchange", "selectLanguage();");
                String languageDisplayName = getContext().localize("OwLoginView.selectLanguage", "Select Language");
                OwInsertLabelHelper.insertLabelValue(w_p, languageDisplayName, LANG_SELECT_ID);
                renderer.renderCombo(w_p);
            }
            else if (lang.size() == 1)
            {

                ((OwMainAppContext) getContext()).setLocale(new Locale(lang.get(0).getValue()));
            }

        }
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case ERROR_REGION:
            {
                Throwable e = m_MainContext.getError();
                if (e != null)
                {
                    // print error which occurred upon recent request
                    OwExceptionManager.PrintCatchedException(getContext().getLocale(), e, new PrintWriter(w_p), "OwErrorStack");
                }
            }
                break;

            case LOCALS_REGION:
                renderLocalsRegion(w_p);
                break;

            default:
            {
                // === render registered regions
                super.renderRegion(w_p, iRegion_p);
            }
        }
    }

    /** render the view and all contained views
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwLoginView.jsp", w_p);
    }

}