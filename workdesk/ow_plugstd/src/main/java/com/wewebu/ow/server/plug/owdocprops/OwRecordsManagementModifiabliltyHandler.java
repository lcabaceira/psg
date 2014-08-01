package com.wewebu.ow.server.plug.owdocprops;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwEditPropertiesModifiabilityHandler implementation specific to Alfresco RM 2.1 version. 
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
public class OwRecordsManagementModifiabliltyHandler implements OwEditPropertiesModifiabilityHandler
{

    @Override
    public void init(OwAppContext appContext, OwXMLUtil handlerConfigNode) throws OwException
    {

    }

    @Override
    public boolean isModifiable(int viewMask, OwObject item)
    {
        if (viewMask == OwEditPropertiesDialog.VIEW_MASK_PROPERTIES)
        {
            try
            {
                return item.getProperty("P:rma:record.rma:origionalName") == null;//should not be editable if exist
            }
            catch (Exception e)
            {
                return true;
            }

        }
        return false;
    }

}
