package com.wewebu.ow.server.app;

import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Classic combo box renderer component.
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
 *@since 3.0.0.0
 */
public class OwClassicComboboxRenderer extends OwBaseComboboxRenderer
{
    /** classic style classes */
    protected static final Set<String> CLASSIC_STYLE_CLASSES = new LinkedHashSet<String>();
    static
    {
        CLASSIC_STYLE_CLASSES.add("OwInputControl");
        CLASSIC_STYLE_CLASSES.add("OwInputControlChoice");
    }

    public OwClassicComboboxRenderer()
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#renderCombo(java.io.Writer)
     */
    public void renderCombo(Writer w_p) throws Exception
    {
        w_p.write("<select ");
        if (!this.enabled)
        {
            w_p.write(" disabled ");
        }
        w_p.write(renderStyleClasses(CLASSIC_STYLE_CLASSES));
        w_p.write(" name=\"");
        OwHTMLHelper.writeSecureHTML(w_p, m_fieldId);
        w_p.write("\"  id=\"");
        OwHTMLHelper.writeSecureHTML(w_p, m_fieldId);
        w_p.write("\" ");
        if (m_fieldDefinition != null)
        {
            w_p.write(" title=\"");
            if (m_description != null)
            {
                w_p.write(m_description.getString(getContext().getLocale()));
            }
            else
            {
                w_p.write(m_fieldDefinition.getDescription(getContext().getLocale()));
            }
            w_p.write("\" ");
            w_p.write(" onchange='onFieldManagerFieldExit(\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(m_fieldDefinition.getClassName()));
            w_p.write("\",\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(m_fieldDefinition.getJavaClassName()));
            w_p.write("\",\"");
            if (m_fieldProvider != null)
            {
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(m_fieldProvider.getFieldProviderType())));
                w_p.write("\",\"");
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(m_fieldProvider.getFieldProviderName()));
                w_p.write("\",\"");
            }
            else
            {
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(-1)));
                w_p.write("\",\"");
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(""));
                w_p.write("\",\"");
            }
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(m_fieldId));
            w_p.write("\",this.value)");
            String onChangeEventHandler = this.getEventHandler("onchange");
            if (onChangeEventHandler != null)
            {
                w_p.write(";" + onChangeEventHandler + ";");
            }
            w_p.write("'");
            w_p.write(renderEvents("onchange"));
        }
        else
        {

            if (m_description != null)
            {
                w_p.write(" title=\"");
                w_p.write(m_description.getString(getContext().getLocale()));
                w_p.write("\"");
            }
            w_p.write(renderEvents());
        }
        w_p.write(">\n");

        //Iterator it = fieldDef_p.getEnums().iterator();
        for (int i = 0; i < m_model.getSize(); i++)
        {
            //TODO - check for null values? should be not rendered, or should be rendered as blanks?
            w_p.write("<option value=\"");
            OwComboItem item = m_model.getItemAt(i);
            OwHTMLHelper.writeSecureHTML(w_p, item.getValue());
            w_p.write("\"");
            // look for selected item
            if (m_model.isSelectedItem(item))
            {
                w_p.write(" selected ");
            }
            w_p.write(">");
            OwHTMLHelper.writeSecureHTML(w_p, item.getDisplayName());
            w_p.write("</option>\n");

        }

        w_p.write("</select>\n");

        if (m_model.hasItemOutOfRange())
        {
            // === info text to the user about out of range
            w_p.write("<span class=\"OwInstructionName\">");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwStandardFieldManager.outofrangelist", "(!) Out of range list"));
            w_p.write("</span>");
        }

    }

    /**
     * Render the events. 
     * @param excludeEventName_p - the event name that will be excluded from rendering.
     * @return the events and corresponding handlers.
     */
    protected String renderEvents(String excludeEventName_p)
    {
        StringBuffer result = new StringBuffer();
        String[] allEventsNames = this.getAllEventsNames();
        if (allEventsNames.length > 0)
        {
            for (int i = 0; i < allEventsNames.length; i++)
            {
                if (excludeEventName_p != null && allEventsNames[i].equalsIgnoreCase(excludeEventName_p))
                {
                    continue;
                }
                result.append(" " + allEventsNames[i] + "='" + this.getEventHandler(allEventsNames[i]) + "' ");
            }
        }
        return result.toString();
    }

    /**
     * Utility method for rendering all events.
     * @return the events as a {@link String} object.
     */
    protected String renderEvents()
    {
        return renderEvents(null);
    }

}
