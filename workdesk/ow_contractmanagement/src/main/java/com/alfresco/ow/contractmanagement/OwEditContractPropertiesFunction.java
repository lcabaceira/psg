package com.alfresco.ow.contractmanagement;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.plug.owdocprops.OwEditDocumentPropertiesFunction;
import com.wewebu.ow.server.plug.owdocprops.OwEditPropertiesDialog;
import com.wewebu.ow.server.plug.owdocprops.OwEditPropertiesDialogBuilder;

/**
 *<p>
 * OwEditContractPropertiesFunction.
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
public class OwEditContractPropertiesFunction extends OwEditDocumentPropertiesFunction
{
    @Override
    protected OwEditPropertiesDialogBuilder createDialogBuilder()
    {
        return new OwEditContractPropertiesDialogBuilder();
    }

    static class OwEditContractPropertiesDialogBuilder extends OwEditPropertiesDialogBuilder
    {
        @Override
        public OwEditPropertiesDialog build()
        {
            return new OwEditContractPropertiesDialog(this);
        }
    }

    static class OwEditContractPropertiesDialog extends OwEditPropertiesDialog
    {
        protected OwEditContractPropertiesDialog(OwEditPropertiesDialogBuilder builder)
        {
            super(builder);
        }

        @Override
        protected OwObjectPropertyView createPropertyViewInstance()
        {
            return new OwContractObjectPropertyView();
        }
    }

}
