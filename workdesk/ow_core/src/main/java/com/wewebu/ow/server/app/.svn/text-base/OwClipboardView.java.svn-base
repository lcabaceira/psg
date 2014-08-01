package com.wewebu.ow.server.app;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * View to display the clipboard content.
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
public class OwClipboardView extends OwView
{
    /** the plugin ID request parameter */
    private static final String PLUGIN_ID_PARAMETER = "PLUGIN_ID";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwClipboardView.class);

    /** instance of the MIME manager used to open the objects, 
     * protected since 2.5.2.2 */
    protected OwMimeManager m_MimeManager = new OwMimeManager();

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === init MIME manager as event target
        m_MimeManager.attach(getContext(), null);
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();

        // render the different content types
        switch (clipboard.getContentType())
        {
            case OwClipboard.CONTENT_TYPE_OW_OBJECT:
                renderOwObjectContentType(w_p, clipboard);
                break;

            case OwClipboard.CONTENT_TYPE_OW_WORKITEM:
                renderOwWorkitemContentType(w_p, clipboard);
                break;

            case OwClipboard.CONTENT_TYPE_OW_FIELD:
                renderOwFieldContentType(w_p, clipboard);
                break;

        }
    }

    /** render OwWorkitem type content of the clipboard 
     *
     * @param w_p Writer object to write HTML to
     * @param clipboard_p OwClipboard
     */
    private void renderOwWorkitemContentType(Writer w_p, OwClipboard clipboard_p) throws Exception
    {
        ///////////////////////////////////
        // TODO:
        throw new OwNotSupportedException("OwClipboardView.renderOwWorkitemContentType: Not implemented");
    }

    /** render OwObject type content of the clipboard 
     *
     * @param w_p Writer object to write HTML to
     * @param clipboard_p OwClipboard
     */
    private void renderOwObjectContentType(Writer w_p, OwClipboard clipboard_p) throws Exception
    {
        // always reset MIME manager before inserting links !!!
        m_MimeManager.reset();

        List contentItems = clipboard_p.getContent();

        if (contentItems.size() > 0)
        {
            w_p.write("<div class='OwClipboardView'>");
            // === add clipboard label 
            w_p.write("<span>" + getContext().localize("app.OwClipboardView.title", "Clipboard"));

            if (clipboard_p.getCut())
            {
                String tooltip = getContext().localize("app.OwClipboardView.cuttooltip", "The content will be moved when pasted.");
                w_p.write("&nbsp;(<img alt='" + tooltip + "' title='" + tooltip + "' src='" + getContext().getDesignURL() + "/images/plug/owdoccopy/cut.png'>)");
            }

            // display content size if more than one
            if (contentItems.size() > 1)
            {
                w_p.write(" (" + String.valueOf(contentItems.size()) + ")");
            }

            w_p.write(":</span><ul>");

            // iterate over the clipboard content
            for (int i = 0; i < contentItems.size(); i++)
            {
                // === don't show more than 3 items
                if (i > 2)
                {
                    // draw ellipses to indicate more items in the clipboard
                    w_p.write("<li class='OwClipboardView'>...</li>");
                    break;
                }

                OwObject obj = ((OwClipboardContentOwObject) contentItems.get(i)).getObject();

                // === MIME icon
                w_p.write("<li>");

                m_MimeManager.insertIconLink(w_p, obj);

                m_MimeManager.insertTextLink(w_p, obj.getName(), obj);

                w_p.write("</li>");

            }
            w_p.write("</ul>");
            // === add empty button for clipboard
            w_p.write(getClearButton());
            renderPluginButtons(w_p);
            w_p.write("</div>");
        }
    }

    /**
     * Render the configured plugin buttons.
     * @param w_p - the writer
     * @throws Exception
     * @since 3.0.0.0
     */
    private void renderPluginButtons(Writer w_p) throws Exception
    {
        Collection pluginIds = ((OwMainAppContext) getContext()).getClipboardAvailableDocFunctionIds();
        Iterator pluginIdsIterator = pluginIds.iterator();
        while (pluginIdsIterator.hasNext())
        {
            String pluginId = (String) pluginIdsIterator.next();
            try
            {
                OwDocumentFunction plugIn = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(pluginId);
                // === add send as attachment button for clipboard
                w_p.write("<a href=\"" + getEventURL("ClipboardPluginEvent", PLUGIN_ID_PARAMETER + "=" + pluginId) + "\">");
                String title = plugIn.getTooltip();
                w_p.write("<img title='" + title + "' alt='" + title + "' src='" + plugIn.getIcon() + "' />");
                w_p.write("</a>");
            }
            catch (Exception e)
            {
                //do not render the plugin
                LOG.error("Cannot render the plugin: " + pluginId, e);
            }
        }
    }

    /**Render the clipboard information, if content type is an OwField.
     * 
     * @param w_p Writer used to write the information of graphical layout for current clipboard
     * @param clipboard_p OwClipboard which should be rendered
     * @throws Exception a IOException may occurs during writing out the layout
     */
    protected void renderOwFieldContentType(Writer w_p, OwClipboard clipboard_p) throws Exception
    {
        String icon = getContext().getDesignURL() + "/images/plug/owdocprops/info.png";

        List contentItems = clipboard_p.getContent();

        if (contentItems != null && contentItems.size() > 0)
        {
            w_p.write("<div class=\"OwClipboardView\" ");
            w_p.write("<span>");
            //title
            w_p.write(getContext().localize("app.OwClipboardView.title", "Clipboard"));
            // if more than three items exist
            if (contentItems.size() > 3)
            {
                //write out the count of contained items
                w_p.write(contentItems.size() + ")");

            }
            w_p.write(":</span><ul>\n");

            OwFieldManager fieldManager = ((OwMainAppContext) getContext()).createFieldManager();
            //contained elements
            for (int i = 0; i < contentItems.size(); i++)
            {
                if (i > 2)
                {
                    w_p.write("<li class=\"OwClipboardView\">...</li>");
                    break; //exit for-loop
                }

                OwField elem = ((OwClipboardContentOwField) contentItems.get(i)).getField();
                //draw tooltip
                w_p.write("<li class=\"OwClipboardView\" title=\"");
                w_p.write(elem.getFieldDefinition().getClassName());

                //draw leading image
                w_p.write("\"><img alt='' title='' src=\"");
                w_p.write(icon);
                w_p.write("\">&nbsp;");

                //draw field displayname
                w_p.write(elem.getFieldDefinition().getDisplayName(getContext().getLocale()));
                w_p.write("&nbsp;=&nbsp;");
                //draw field value
                fieldManager.insertReadOnlyFieldInternal(w_p, elem.getFieldDefinition(), elem.getValue());
                w_p.write("</li>\n");
            }

            // === add empty button for clipboard
            w_p.write("</ul>");
            w_p.write(getClearButton());
            w_p.write("</div>");
        }
    }

    /**
     * Get the code for rendering the clear button.
     * @return the HTML code for clear button.
     */
    public String getClearButton()
    {
        StringBuffer buf = new StringBuffer("<a href=\"");
        buf.append(getContext().getEventURL(this, "ClearClipboard", null));
        buf.append("\"><img src=\"");
        try
        {
            buf.append(getContext().getDesignURL());
        }
        catch (Exception e)
        {
            buf.append("./designs/default41");
        }
        buf.append("/images/clipboard_delete.png\" title=\"");
        String title = getContext().localize("app.OwClipboardView.deletebtn.title", "Clear Clipboard");
        buf.append(title);
        buf.append("\" alt=\"" + title + "\"/> </a>");

        return buf.toString();
    }

    /**
     * Handler for clear the content of clipboard.
     * @param req_p - the request.
     */
    public void onClearClipboard(HttpServletRequest req_p)
    {
        ((OwMainAppContext) getContext()).getClipboard().clearContent();
    }

    /**
     * External call to execute a plugin for all items in clipboard
     * @param request_p - the request
     * @throws Exception 
     * @since 3.0.0.0
     */
    public void onClipboardPluginEvent(HttpServletRequest request_p) throws Exception
    {
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();

        List items = clipboard.getContent();

        int size = ((items != null) ? items.size() : 0);

        // do nothing if clipboard is empty
        if (size == 0)
        {
            return;
        }

        OwDocumentFunction plugIn = null;

        try
        {
            String pluginId = request_p.getParameter(PLUGIN_ID_PARAMETER);
            plugIn = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(pluginId);

        }
        catch (Exception e)
        {
            LOG.error("OwClipboardView.onClipboardPluginEvent: " + "Send as attachment plugin not found. Please check your configuration.", e);
            throw new OwInvalidOperationException(getContext().localize("app.OwClipboardView.exception.plugin_not_found", "The plugin to send e-mail attachments was not found. Please contact your administrator."), e);
        }

        // delegate to send as attachment document plugin
        if (plugIn != null)
        {
            if (size == 1)
            {
                plugIn.onClickEvent(((OwClipboardContentOwObject) items.get(0)).getObject(), null, null);
            }
            else if (size > 1)
            {
                // get OwObjects
                List owObjectsList = new LinkedList();
                Iterator itr = items.iterator();

                while (itr.hasNext())
                {
                    OwClipboardContentOwObject obj = (OwClipboardContentOwObject) itr.next();
                    owObjectsList.add(obj.getObject());
                }

                plugIn.onMultiselectClickEvent(owObjectsList, null, null);
            }
        }
    }
}