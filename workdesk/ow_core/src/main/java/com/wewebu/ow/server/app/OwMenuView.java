package com.wewebu.ow.server.app;

import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwMenu;

/**
 *<p>
 * Base class for  menus for the Workdesk.
 * The Menu Items can consist of Icons or Text or both.
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
public abstract class OwMenuView extends OwMenu
{
    /** a navigation item
     */
    public class OwMenuTabInfo extends OwMenuTab
    {

        public OwMenuTabInfo(OwEventTarget target_p, String strTitle_p, String strImage_p, Object oReason_p, String strEvent_p, String strToolTip_p)
        {
            super(target_p, strTitle_p, strImage_p, oReason_p, strEvent_p, strToolTip_p);
        }
    }

    /** overridable to create a tab info
     * 
     * @param target_p
     * @param strTitle_p
     * @param strImage_p
     * @param oReason_p
     * @param strEvent_p
     * @param strToolTip_p
     * 
     * @return OwMenuTab
     */
    protected OwMenuTab createTabInfo(OwEventTarget target_p, String strTitle_p, String strImage_p, Object oReason_p, String strEvent_p, String strToolTip_p)
    {
        return new OwMenuTabInfo(target_p, strTitle_p, strImage_p, oReason_p, strEvent_p, strToolTip_p);
    }
}