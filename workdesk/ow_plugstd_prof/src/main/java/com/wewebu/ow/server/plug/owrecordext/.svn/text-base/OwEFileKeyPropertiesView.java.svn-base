package com.wewebu.ow.server.plug.owrecordext;

import java.io.Writer;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Creates a view for properties involved in eFile key generation.
 * In this view, the user has the possibility to set values for the ECM properties involved 
 * in eFile key generation.
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
 *@since 3.1.0.0
 */
public class OwEFileKeyPropertiesView extends OwObjectPropertyView
{
    /**
     * Constructor.
     */
    public OwEFileKeyPropertiesView()
    {
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#init()
     */
    protected void init() throws Exception
    {
        super.init();
        disableSaveButton();
    }

    /**
     * Disable the save button.
     */
    private void disableSaveButton()
    {
        if (getMenu() != null)
        {
            getMenu().enable(m_iAppyBtnIndex, false);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        super.onUpdate(caller_p, iCode_p, param_p);
        disableSaveButton();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#renderMainRegion(java.io.Writer)
     */
    protected void renderMainRegion(Writer w_p) throws Exception
    {
        super.renderMainRegion(w_p);
        disableSaveButton();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView#save(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    protected boolean save(OwPropertyCollection changedProperties_p) throws Exception
    {
        boolean result = false;
        if (getDocument() != null)
        {
            result = ((OwContractDocument) getDocument()).saveGeneratedProperties(getObjectRef(), changedProperties_p);
        }
        return result;
    }
}
