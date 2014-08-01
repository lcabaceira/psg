package com.wewebu.ow.server.plug.owdocview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwWindowPositions;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Implementation of the Document edit properties plugin.
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
public class OwDocumentParallelView extends OwDocumentFunction
{
    /**
     *<p>
     * OwWindowPositionsImpl.
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
    private static class OwWindowPositionsImpl extends OwWindowPositions
    {
        public int m_begin;
        public int m_end;

        public OwWindowPositionsImpl(int begin_p, int end_p)
        {
            m_begin = begin_p;
            m_end = end_p;
        }

        public boolean getPositionMainWindow()
        {
            return false;
        }

        public int getUnits()
        {
            return 1;
        }

        //return-value in percent
        public int getViewerHeight()
        {
            return 100;
        }

        public int getViewerTopX()
        {
            return m_begin;
        }

        public int getViewerTopY()
        {
            return 0;
        }

        //return-value in percent
        public int getViewerWidth()
        {
            return m_end - m_begin;
        }

        public int getWindowHeight()
        {
            return 0;
        }

        public int getWindowTopX()
        {

            return 0;
        }

        public int getWindowTopY()
        {

            return 0;
        }

        public int getWindowWidth()
        {

            return 0;
        }

        public void setFromRequest(HttpServletRequest request_p, String strBaseID_p) throws Exception
        {
            throw new OwInvalidOperationException("OwDocumentParallelView$OwWindowPositionsImpl.setFromRequest: Not implemented.");
        }

    }

    // === members

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocprops/docview.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocprops/docview_24.png");
    }

    /** check if function is enabled for the given object parameters (called only for single select operations)
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param iContext_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        return super.isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) && oObject_p.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    }

    /** check if function is enabled for the given objects (called only for multi select operations)
    *
    *  @param objects_p Collection of OwObject 
    *  @param oParent_p Parent which listed the Object
    *  @param iContext_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(Collection objects_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (!super.isEnabled(objects_p, oParent_p, iContext_p))
        {
            return false;
        }
        if (objects_p.size() < 2)
        {
            return false;
        }

        return true;
    }

    /** event called when user clicked the label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        List objects = new ArrayList();
        objects.add(oObject_p);

        onMultiselectClickEvent(objects, oParent_p, refreshCtx_p);
    }

    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject 
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwWindowPositionsImpl pos1 = new OwWindowPositionsImpl(50, 100);
        OwWindowPositionsImpl pos2 = new OwWindowPositionsImpl(0, 50);

        Iterator it = objects_p.iterator();

        OwObject obj1 = (OwObject) it.next();
        OwMimeManager.openObject(getContext(), obj1, oParent_p, OwMimeManager.VIEWER_MODE_MULTI, refreshCtx_p, 1, pos1, objects_p, "compare=true");

        if ((objects_p.size() > 1) && getConfigNode().getSafeBooleanValue("showDocsParallel", true))
        {
            OwObject obj2 = (OwObject) it.next();
            OwMimeManager.openObject(getContext(), obj2, oParent_p, OwMimeManager.VIEWER_MODE_MULTI, refreshCtx_p, 1, pos2, objects_p, "compare=true");
        }

        // set JavaScript flag to deactivate focus retainment 
        getContext().addFinalScript("prevent_focus_regainment = true;");

        // historize
        addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }

}