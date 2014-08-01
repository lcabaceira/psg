package com.wewebu.ow.server.plug.owhelp;

import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwMasterDocument;

/**
 *<p>
 * Help Document.
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
public class OwHelpDocument extends OwMasterDocument
{
    protected Object onDispatch(int iCode_p, Object param1_p, Object param2_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwDispatchCodes.OPEN_OBJECT:
                // activate first
                getMasterView().activate();
                // now overwrite the current link with the anticipated one
                ((OwHelpView) getMasterView()).setHelpJsp((String) param1_p);
                return null;

            default:
                return super.onDispatch(iCode_p, param1_p, param2_p);
        }
    }

}