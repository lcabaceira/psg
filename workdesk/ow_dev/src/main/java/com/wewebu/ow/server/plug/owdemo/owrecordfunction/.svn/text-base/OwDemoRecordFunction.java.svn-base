package com.wewebu.ow.server.plug.owdemo.owrecordfunction;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwRecordFunction;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.event.OwEventManager;

/**
 *<p>
 * Demo Record Function Plugin.
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
public class OwDemoRecordFunction extends OwRecordFunction
{
    /**
     * The element name for JSP file, used for rendering
     * @since 3.1.0.0
     */
    protected static final String JSP_FORM_ELEMENT_NAME = "JspForm";

    // === members

    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdemo/demoinfo.png");
    }

    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdemo/demoinfo_24.png");
    }

    /** check if function is enabled for the given object parameters
    *
    *  @param rootObject_p OwObject root folder to work on
    *  @param folderObject_p OwObject selected folder to work on
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(OwObject rootObject_p, OwObject folderObject_p, int iContext_p) throws Exception
    {
        if (super.isEnabled(rootObject_p, folderObject_p, iContext_p))
        {
            return ((rootObject_p != null) && (rootObject_p.getType() == OwObjectReference.OBJECT_TYPE_FOLDER));
        }
        else
        {
            return false;
        }
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject rootObject_p, OwObject folderObject_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        String strFormular = getConfigNode().getSafeTextValue(JSP_FORM_ELEMENT_NAME, null);

        OwDemoEditPropertiesFormularDialog dlg = new OwDemoEditPropertiesFormularDialog(rootObject_p, null, strFormular);
        dlg.setInfoIcon(getBigIcon());

        getContext().openDialog(dlg, null);

        // historize
        addHistoryEvent(rootObject_p, folderObject_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
    }
}