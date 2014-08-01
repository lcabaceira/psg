package com.alfresco.ow.contractmanagement;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwObject;

/**
  *<p>
 * OwCreateContractDialog.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p> */
public class OwCreateContractDialog extends com.wewebu.ow.server.plug.owrecordext.OwCreateContractDialog
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCreateContractDialog.class);

    protected String m_strDmsID = null;

    /**
     * Constructor.
     * @param folderObject_p
     * @param classSelectionCfg
     * @param fOpenObject_p
     * @since 4.1.0.0
     */
    public OwCreateContractDialog(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean fOpenObject_p)
    {
        super(folderObject_p, classSelectionCfg, fOpenObject_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owrecordext.OwCreateContractDialog#createObjectPropertyView()
     */
    @Override
    protected OwObjectPropertyView createObjectPropertyView() throws Exception
    {
        return new OwContractGroupPropertyView(this.m_propertyPatternConfiguration);
    }
}
