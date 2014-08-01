package com.wewebu.ow.server.plug.ownew;

import java.util.Collection;

import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwSubLayout;

/**
 *<p>
 * The New-Plugin view.
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
public class OwNewView extends OwMasterView
{
    /** layout to be used for the view */
    private OwSubLayout m_layout = new OwSubLayout();

    private OwNewExtensionsToolView m_extensionsToolViewNavigation;

    /** Called when the view should create its HTML content to be displayed.*/
    protected void init() throws Exception
    {
        super.init();

        OwNewDocument newDocument = (OwNewDocument) getDocument();
        // === navigation 
        m_extensionsToolViewNavigation = createSubNavigationView(newDocument.getExtensions());

        // === attach layout
        addView(m_layout, null);
        m_layout.addView(m_extensionsToolViewNavigation, OwSubLayout.MAIN_REGION, null);

    }

    // === factory methods for overriding the plugin
    /** (overridable) 
     *  Extensions tool view factory method.
     * 
     * @return OwNewExtensionsToolView instance
     */
    protected OwNewExtensionsToolView createSubNavigationView(Collection extensions_p)
    {
        return new OwNewExtensionsToolView(extensions_p);
    }
}