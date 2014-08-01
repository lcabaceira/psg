package com.wewebu.ow.server.plug.owshortcut;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * A document function that displays a status icon dependent on document property states.
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
public class OwShortCutFunctionsView extends OwView
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutFunctionsView.class);

    protected void init() throws Exception
    {
        super.init();
    }

    /** get the URL to the paste icon
     * @return String URL
     */
    public String getPasteIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/plug/owdocshortcut/paste_24.png";
    }

    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div id=\"OwSubLayout_menu\">");

        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();
        if (clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT)
        {
            if (clipboard.getContent().iterator().hasNext())
            {
                w_p.write("<ul class=\"OwRecordRecordFunctionView\">");
                // write paste link
                String pastetitle = getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.paste", "Paste from clipboard");
                w_p.write("<a title=\"");
                w_p.write(pastetitle);
                w_p.write("\" href=\"");
                w_p.write(getEventURL("PasteFromCipboard", null));
                w_p.write("\"><img alt=\"" + pastetitle + "\" title=\"");
                w_p.write(pastetitle);
                w_p.write("\" src=\"");
                w_p.write(getPasteIcon());
                w_p.write("\" border=\"0\"/></a>");
                w_p.write("</ul>");
            }
        }
        w_p.write("</div>");
    }

    /** called when user clicks on paste from clipboard 
     * @throws Exception */
    public void onPasteFromCipboard(HttpServletRequest request_p) throws Exception
    {
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();

        if (clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT)
        {
            Collection collection = new ArrayList();

            Iterator it = clipboard.getContent().iterator();

            while (it.hasNext())
            {
                // copy clipboard element to short cut array
                OwClipboardContentOwObject content = (OwClipboardContentOwObject) it.next();
                OwShortCutItemOwObject owShortCutItemOwObject = new OwShortCutItemOwObject(content.getObject());
                collection.add(owShortCutItemOwObject);
            }

            // save
            ((OwShortCutDocument) getDocument()).addShortCuts(collection);
            LOG.info("All documents from clipboard were added to shortcut pool (favorites)...");
        }

    }
}