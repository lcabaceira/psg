package com.wewebu.ow.server.plug.owbpm;

import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * This one only selects Users.
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
 *@since 4.2.0.0
 */
public class OwBPMUserPropertyControl extends OwBPMUserInfoControl
{
    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owbpm.OwBPMUserInfoControl#filterTypes()
     */
    @Override
    protected int[] filterTypes()
    {
        return new int[] { OwUIUserSelectModul.TYPE_USER };
    }

    @Override
    protected String btnSelectTitle(OwFieldDefinition fieldDef) throws Exception
    {
        if (fieldDef.isArray())
        {
            return getContext().localize("plug.owbpm.OwBPMUserInfoControl.select.persons", "Select People");
        }
        else
        {
            return getContext().localize("plug.owbpm.OwBPMUserInfoControl.select.person", "Select Person");
        }
    }
}
