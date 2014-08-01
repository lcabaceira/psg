package com.wewebu.ow.server.settingsimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwMainAppContext;

/**
 *<p>
 * A master role select property.
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
public class OwSettingsPropertyMasterRoles extends OwSettingsPropertyStringCombo
{
    /** create a list of values, if property is a list 
     * @param valueNode_p Node with child value nodes
     * @return List of Objects representing values 
     */
    protected List createComboSelectList(Node valueNode_p) throws Exception
    {
        List valueList = new ArrayList();

        // iterate over the master roles
        if (((OwMainAppContext) getContext()).getRoleManager().hasMasterRoles())
        {
            Iterator it = ((OwMainAppContext) getContext()).getRoleManager().getMasterRoles().iterator();

            while (it.hasNext())
            {
                String strMasterRole = (String) it.next();

                valueList.add(new OwDefaultComboItem(strMasterRole, ((OwMainAppContext) getContext()).getRoleManager().getMasterRoleDisplayName(getContext().getLocale(), strMasterRole)));
            }
        }

        return valueList;
    }
}