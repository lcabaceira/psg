package com.wewebu.ow.server.app;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * ExtJS combo box renderer component.
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
public class OwExtJSComboboxRenderer extends OwBaseComboboxRenderer
{
    /**ExtJs style classes*/
    private static final Set<String> EXTJS_STYLE_CLASSES = new LinkedHashSet<String>();
    static
    {
        EXTJS_STYLE_CLASSES.add("OwInputControl");
        EXTJS_STYLE_CLASSES.add("OwInputControlAdvancedChoice");
    }

    /** map between JavaScript event names and EXTJS event names. */
    protected static final Map<String, String> JS_2_EXT_JS_EVENTS_MAP = new HashMap<String, String>();
    static
    {
        JS_2_EXT_JS_EVENTS_MAP.put("change", "select");
        JS_2_EXT_JS_EVENTS_MAP.put("click", "focus");
    }

    public OwExtJSComboboxRenderer()
    {

    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboboxRenderer#renderCombo(java.io.Writer)
     */
    public void renderCombo(Writer w_p) throws Exception
    {
        //Rendering DIV for JS combo box
        String secureFieldId = OwHTMLHelper.encodeToSecureHTML(m_fieldId);
        w_p.write("<div ");
        w_p.write(renderStyleClasses(EXTJS_STYLE_CLASSES));
        w_p.write(" id=\"r");
        w_p.write(secureFieldId);
        w_p.write("\"></div>\n");

        if (m_model.hasItemOutOfRange())
        {
            // === info text to the user about out of range
            w_p.write("<span class=\"OwInstructionName\">");
            w_p.write(OwString.localize(getContext().getLocale(), "app.OwStandardFieldManager.outofrangelist", "(!) Out of range list"));
            w_p.write("</span>");
        }

        // js for advanced combo
        w_p.write("<script type=\"text/javascript\">\nExt.BLANK_IMAGE_URL =\"");
        w_p.write(OwHTMLHelper.encodeJavascriptString(getContext().getBaseURL() + "/js/extjs/resources/images/default/s.gif"));
        w_p.write("\";\n");
        String convertedVarId = "converted_" + OwHTMLHelper.encodeJavascriptVariableName(m_fieldId);
        String encodedFieldId = OwHTMLHelper.encodeJavascriptString(m_fieldId);
        w_p.write("Ext.onReady(function(){\n");
        w_p.write("     var ");
        w_p.write(convertedVarId);
        w_p.write(" = new Ext.form.ComboBox({\n");
        w_p.write("     renderTo:'r");
        w_p.write(secureFieldId);
        w_p.write("',\n");
        w_p.write("     id:'");
        w_p.write(secureFieldId);
        w_p.write("',\n");
        w_p.write("     typeAhead: true,\n");
        w_p.write("     triggerAction: 'all',\n");
        w_p.write("     mode: 'local',\n");
        w_p.write("     valueField: 'value',\n     displayField: 'label',\n");
        w_p.write("     store: new Ext.data.ArrayStore({\n");
        w_p.write("          storeId: 'store");
        w_p.write(encodedFieldId);
        w_p.write("',\n          fields: ['value', 'label'],\n");
        w_p.write("          data: [");
        String selectedValue = null;
        for (int i = 0; i < m_model.getSize(); i++)
        {
            w_p.write("[\"");
            OwComboItem item = m_model.getItemAt(i);
            String encSelectVal = OwHTMLHelper.encodeToSecureHTML(item.getValue());
            w_p.write(encSelectVal);
            w_p.write("\", ");
            // look for selected item
            if (m_model.isSelectedItem(item))
            {
                selectedValue = encSelectVal;
            }
            w_p.write("\"");
            OwHTMLHelper.writeSecureHTML(w_p, item.getDisplayName());
            w_p.write("\"]\n");
            if (i + 1 < m_model.getSize())
            {
                w_p.write("          ,");
            }
        }
        w_p.write("          ]}),\n");
        w_p.write("     hiddenId:'extCmb");
        w_p.write(encodedFieldId);
        w_p.write("',\n");
        w_p.write("     hiddenName:'");
        w_p.write(encodedFieldId);
        w_p.write("',\n");
        if (selectedValue != null)
        {
            w_p.write("     hiddenValue:\"");
            w_p.write(selectedValue);
            w_p.write("\",\n");
            w_p.write("     value:\"");
            w_p.write(selectedValue);
            w_p.write("\",\n");
        }
        w_p.write("     grow:true,\n");
        w_p.write("     autoWidth:'auto',\n");
        w_p.write("     listWidth:'auto',\n");
        //TODO it will be a good idea to have possibility to add CSS classes here to 
        w_p.write("     cls:'OwAjaxComponentCombo',\n");

        w_p.write("     selectOnFocus: true,\n");
        if (!this.enabled)
        {
            w_p.write("     disabled: true,\n");
        }
        w_p.write("     forceSelection:true\n");
        w_p.write("});\n");

        String fieldClassName = "";
        String javaClassName = "";
        int fieldProviderType = -1;
        String fieldProviderName = "";
        if (m_fieldDefinition != null)
        {
            fieldClassName = m_fieldDefinition.getClassName();
            javaClassName = m_fieldDefinition.getJavaClassName();
        }
        if (m_fieldProvider != null)
        {
            fieldProviderType = m_fieldProvider.getFieldProviderType();
            fieldProviderName = m_fieldProvider.getFieldProviderName();
        }

        w_p.write(convertedVarId);
        w_p.write(".addListener('select',function(cb,record,index){onFieldManagerFieldExit(\"");
        w_p.write(OwHTMLHelper.encodeJavascriptString(fieldClassName));
        w_p.write("\",\"");
        w_p.write(OwHTMLHelper.encodeJavascriptString(javaClassName));
        w_p.write("\",\"");
        w_p.write(OwHTMLHelper.encodeJavascriptString(Integer.toString(fieldProviderType)));
        w_p.write("\",\"");
        w_p.write(OwHTMLHelper.encodeJavascriptString(fieldProviderName));
        w_p.write("\",\"");
        w_p.write(encodedFieldId);
        w_p.write("\",record.data[cb.valueField || cb.displayField]);});\n");

        //addEvents
        w_p.write(renderEvents(convertedVarId));
        w_p.write("});\n");
        w_p.write("</script>\n");

    }

    /**
     * Add prepared events for rendering EXTJS combo box.
     * @param convertedVarId_p - the EXTJS transformed combobox id.
     * @return - the prepared events for rendering EXTJS combo box.
     */
    protected String renderEvents(String convertedVarId_p)
    {
        String[] eventsNames = this.getAllEventsNames();
        StringBuilder eventCode = new StringBuilder();
        for (int i = 0; i < eventsNames.length; i++)
        {
            String event = eventsNames[i];
            //only the name is needed
            String transformedEvent = event.startsWith("on") ? event.substring(2) : event;

            StringBuilder buff = new StringBuilder(convertedVarId_p).append(".addListener(");

            String realExtJSTransformedEvent = JS_2_EXT_JS_EVENTS_MAP.get(transformedEvent);
            if (realExtJSTransformedEvent != null)
            {
                transformedEvent = realExtJSTransformedEvent;
            }

            buff.append("'").append(transformedEvent).append("',");
            String handler = this.getEventHandler(event);
            int idx = handler.indexOf('(');
            if (idx > 0)
            {
                buff.append(handler.substring(0, idx));
            }
            else
            {
                buff.append(handler);
            }
            buff.append(");\n");
            eventCode.append(buff);
        }
        return eventCode.toString();
    }
}
