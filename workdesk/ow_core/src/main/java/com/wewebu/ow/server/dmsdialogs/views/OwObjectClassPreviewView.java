package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Iterator;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * View to display the class description as Preview. <br/><br/>
 * To be implemented with the specific DMS system.
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
public class OwObjectClassPreviewView extends OwView
{
    /** the object class description to preview */
    protected OwObjectClass m_ObjectClass;

    /** set the object class description to preview
     * @param objectClass_p OwObjectClass
     */
    public void setObjectClass(OwObjectClass objectClass_p)
    {
        m_ObjectClass = objectClass_p;
    }

    /** 
     * get a URL to the close icon 
     * @param iType_p 
     * @return String URL to the close icon 
     */
    protected String getIconURL(int iType_p) throws Exception
    {
        // create link
        StringBuilder iconpath = new StringBuilder(getContext().getDesignURL());

        OwXMLUtil mimenode = ((OwMainAppContext) getContext()).getConfiguration().getDefaultMIMENode(iType_p);
        iconpath.append(OwMimeManager.MIME_ICON_SUBPATH);
        if (mimenode != null)
        {
            iconpath.append(mimenode.getSafeTextValue(OwMimeManager.MIME_ICON_NAME, "unknown.png"));
        }
        else
        {
            iconpath.append("unknown.png");
        }

        return iconpath.toString();
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (m_ObjectClass != null)
        {
            w_p.write("<table border='0' cellspacing='0' cellpadding='0' class='OwObjectClassPreviewView'>");

            w_p.write("<tr>");

            w_p.write("<th valign='top'>");

            w_p.write("<img class='OwObjectClassPreviewView_Icon' alt='' title='' src='");
            w_p.write(getIconURL(m_ObjectClass.getType()));
            w_p.write("'>");

            w_p.write("</th>");

            // delimiter
            w_p.write("<td>&nbsp;</td>");

            w_p.write("<td>");

            w_p.write("<div class='OwObjectClassPreviewView_title'>" + getContext().localize("app.OwObjectClassPreviewView.description", "Description") + "</div>");

            w_p.write("<div class='OwObjectClassPreviewView_description'>" + m_ObjectClass.getDescription(getContext().getLocale()) + "</div>");

            w_p.write("<div class='OwObjectClassPreviewView_title'>" + getContext().localize("app.OwObjectClassPreviewView.properties", "Properties") + "</div>");

            Iterator it = m_ObjectClass.getPropertyClassNames().iterator();
            while (it.hasNext())
            {
                // display only non system properties, i.e. those that can be set during creation
                OwPropertyClass propertyClass = m_ObjectClass.getPropertyClass((String) it.next());
                if (!(propertyClass.isSystemProperty() || propertyClass.isHidden(OwPropertyClass.CONTEXT_ON_CREATE)))
                {
                    w_p.write("<div class='OwObjectClassPreviewView_property' title='" + propertyClass.getClassName() + "'>" + propertyClass.getDisplayName(getContext().getLocale()) + "</div>");
                }
            }

            w_p.write("</td>");

            w_p.write("</tr>");

            w_p.write("</table>");
        }
    }
}