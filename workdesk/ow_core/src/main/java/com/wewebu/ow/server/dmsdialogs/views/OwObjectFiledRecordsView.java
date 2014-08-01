package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Displays the records (Folders) where the given document is filed in.
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
public class OwObjectFiledRecordsView extends OwView
{
    /** object to find the parents of */
    protected OwObject m_object;

    /** List containing OwEcmUtil.OwParentPathInfo items for each filed reference of the given object */
    protected List m_parentPathInfoList;

    /** query key for the selected path item */
    protected static final String QUERY_KEY_PATH_ITEM = "pathitem";

    /** collection of classnames to look for as parents */
    protected Collection m_classNamesCollection;

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if ((m_parentPathInfoList == null) || (m_parentPathInfoList.size() == 0))
        {
            // === no record references for the given object
            // empty list message
            w_p.write("<span class=\"OwEmptyTextMessage\">" + getContext().localize("owdocprops.OwFiledRecordsView.emptylist", "No files found.") + "</span>");
        }
        else
        {
            // === display the record references
            w_p.write("<table class=\"OwObjectPropertyView_PropList\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");

            w_p.write("<tr class=\"OwObjectPropertyView_Header\"><th>" + getContext().localize("owdocprops.OwFiledRecordsView.path", "Path") + "</th></tr>");
            w_p.write("<tr><th>&nbsp;</th></tr>");

            for (int i = 0; i < m_parentPathInfoList.size(); i++)
            {
                String strRowStyleClassName = ((i % 2) != 0) ? "OwObjectPropertyView_EvenRow" : "OwObjectPropertyView_OddRow";

                w_p.write("<tr class=\"" + strRowStyleClassName + "\"><td class=\"OwPropertyName\">&nbsp;");

                OwEcmUtil.OwParentPathInfo pathInfo = (OwEcmUtil.OwParentPathInfo) m_parentPathInfoList.get(i);

                OwXMLUtil mimeNode = OwMimeManager.getMimeNode(((OwMainAppContext) getContext()).getConfiguration(), pathInfo.getParent());

                w_p.write("<a class=\"OwMimeItem\" href=\"" + getEventURL("SelectPath", QUERY_KEY_PATH_ITEM + "=" + String.valueOf(i)) + "\">");
                w_p.write("<img style=\"vertical-align:middle;border:0px none;\" alt=\"" + pathInfo.getParent().getMIMEType() + "\" title=\"" + pathInfo.getParent().getMIMEType() + "\"");
                w_p.write(" src=\"" + ((OwMainAppContext) getContext()).getDesignURL() + "/micon/" + mimeNode.getSafeTextValue("icon", "unknown.png") + "\" class=\"OwMimeIcon\">&nbsp;");
                w_p.write(pathInfo.getParent().getName() + pathInfo.getDisplayPath());
                w_p.write("</a>");

                w_p.write("</td></tr>");
            }

            w_p.write("</table>");
        }
    }

    /** get the form used for the edit fields
     *
     * @return String form name
     */
    public String getFormName()
    {
        // target has no form defined by default, override in derived class
        return null;
    }

    /** called when user clicked a path link
     */
    public void onSelectPath(HttpServletRequest request_p) throws Exception
    {
        // === get selected item
        int iIndex = Integer.parseInt(request_p.getParameter(QUERY_KEY_PATH_ITEM));
        OwEcmUtil.OwParentPathInfo pathInfo = (OwEcmUtil.OwParentPathInfo) m_parentPathInfoList.get(iIndex);

        // === open selected item
        OwMasterDocument handlerPlugin = OwMimeManager.getHandlerMasterPlugin((OwMainAppContext) getContext(), pathInfo.getParent());

        handlerPlugin.dispatch(OwDispatchCodes.OPEN_OBJECT, pathInfo.getParent(), pathInfo.getPath());

        getContext().closeAllDialogs();
    }

    /** set the object to find the filed records for
     * @param obj_p OwObject
     * @param classNames_p Collection of parent class names to look for
     */
    public void setObjectRef(OwObject obj_p, Collection classNames_p) throws Exception
    {
        m_classNamesCollection = classNames_p;
        m_object = obj_p;
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     * @param iIndex_p int tab index of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        if (null == m_parentPathInfoList)
        {
            m_parentPathInfoList = OwEcmUtil.getParentPathOfClass(m_object, m_classNamesCollection);
        }
    }
}