/**
 * 
 */
package com.wewebu.ow.server.plug.owrecord;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wewebu.ow.server.app.OwRecordFunction;

/**
 *<p>
 * View to display only the DropTarget document function. 
 * This function is not rendered along with the other document functions.
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
 *@since 3.2.0.0
 */
public class OwRecordRecordFunctionDnDView extends OwRecordRecordFunctionView
{
    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owrecord.OwRecordRecordFunctionView#onRender(java.io.Writer)
     */
    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("owrecord/OwRecordRecordFunctionDnDView.jsp", w_p);
    }

    public void onAjaxUpdateDnDAppletRegion(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // return soma json tree
        OwRecordFunction dnd = getDnD();
        boolean dndEnabled = (null != dnd) ? getIsPluginEnabled(dnd) : false;
        response_p.getWriter().print(Boolean.toString(dndEnabled));
    }

    /**
     * @return the DnD record function or null.
     */
    @SuppressWarnings("unchecked")
    public OwRecordFunction getDnD()
    {
        OwRecordFunction dnd = null;
        List<OwRecordFunction> plugins = super.getRecordFuntionPlugins();
        if (null != plugins)
        {
            for (OwRecordFunction recordFunction : plugins)
            {
                if (recordFunction.isDragDropTarget())
                {
                    dnd = recordFunction;
                }
            }
        }
        return dnd;
    }

    /**
     * @return the DnD record function index or -1 if not found.
     */
    @SuppressWarnings("unchecked")
    public int getDnDIndex()
    {
        int dnd = -1;
        List<OwRecordFunction> plugins = super.getRecordFuntionPlugins();
        if (null != plugins)
        {
            for (OwRecordFunction recordFunction : plugins)
            {
                dnd++;
                if (recordFunction.isDragDropTarget())
                {
                    return dnd;
                }
            }
        }
        // not found
        return -1;
    }
}
