package com.wewebu.ow.server.plug.owdemo.owfax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.event.OwEventManager;

/**
 *<p>
 * Demo Send Fax Document Function.
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
public class OwFaxDocumentFunction extends OwDocumentFunction
{
    // === members

    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdemo/sendfax.png");
    }

    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdemo/sendfax_24.png");
    }

    /** get the label for the plugin, used in menus
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return tooltip code to be inserted for the document function plugin. 
     
    public String getLabel(OwObject oObject_p, OwObject oParent_p) throws Exception
    {
        return getContext().localize("owdemo.owfax.OwFaxDocumentFunction.label", "Fax senden");
    }*/

    /* get the label for the plugin, used in menus
     *
     *  Used for context menus where no object information is available
     *
     *  @return tooltip code to be inserted for the document function plugin. 
     
    public String getDefaultLabel() throws Exception
    {
        return getContext().localize("owdemo.owfax.OwFaxDocumentFunction.label", "Fax senden");
    }*/

    /* get the tooltip text
     *
     *
     *  @return tooltip text to be inserted for the document function plugin. 
     
    public String getTooltip() throws Exception
    {
        return getContext().localize("owdemo.owfax.OwFaxDocumentFunction.tooltip", "Fax senden");
    }*/

    /** event called when user clicked the plugin label / icon 
     * 
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        //String strFormular      = getConfigNode().getSafeTextValue("JspFormular",null);

        // === create dialog
        // create dummy list with the only one object
        List singleObjectList = new ArrayList();
        singleObjectList.add(oObject_p);

        // create dialog
        OwSendFaxDialog dlg = new OwSendFaxDialog(singleObjectList);

        // set icon
        dlg.setInfoIcon(getBigIcon());

        // set title
        dlg.setTitle(getDefaultLabel());

        // open dialog
        getContext().openDialog(dlg, null);

        // historize
        addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }

    /** event called when user clicked the plugin for multiple selected items
     *
     *  @param objects_p Collection of OwObject 
     *  @param oParent_p Parent which listed the Objects
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // create dialog
        OwSendFaxDialog dlg = new OwSendFaxDialog(objects_p);

        // set icon
        dlg.setInfoIcon(getBigIcon());

        // set title
        dlg.setTitle(getDefaultLabel());

        // open dialog
        getContext().openDialog(dlg, null);

        // historize
        addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }
}