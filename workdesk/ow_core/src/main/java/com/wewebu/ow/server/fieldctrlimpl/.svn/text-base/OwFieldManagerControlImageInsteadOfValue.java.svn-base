package com.wewebu.ow.server.fieldctrlimpl;

import java.io.Writer;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Control to display an image depending of an property value.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 * @since 4.0.0.0
 *</font></p>
 */
public class OwFieldManagerControlImageInsteadOfValue extends OwFieldManagerControl
{
    /** logger for this class*/
    //private static final Logger LOG = OwLog.getLogger(OwFieldManagerControlImageInsteadOfValue.class);

    private static final String IMAGE_PATH = "ImagePath";
    private static final String VALUE_STRING = "{value}";
    private static final String VALUE_PATTERN = "\\{value\\}";
    private static final int BEGIN_URL = 1;
    private static final int END_URL = 2;
    private static final int COMPLETE_PATH = 3;

    private String pathPattern;

    /**
     * Initializes the field control from configuration node
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#init(com.wewebu.ow.server.app.OwFieldManager, org.w3c.dom.Node)
     */
    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);
        // get configuration
        OwXMLUtil config = new OwStandardXMLUtil(configNode_p);
        pathPattern = config.getSafeTextValue(IMAGE_PATH, null);
        if (pathPattern == null)
        {
            throw new OwConfigurationException("Invalid configuration: Missing subnode <" + IMAGE_PATH + "> for value replacement");
        }
        //replace in path included wildcard with property value
        if (!pathPattern.contains(VALUE_STRING))
        {
            //throw error message
            throw new OwConfigurationException("Error in configuration: The configured <" + IMAGE_PATH + ">-Element does not contain the wildcard \"" + VALUE_STRING + "\"");
        }
    }

    /**
     * Generates and returns the appropriate URL in dependence server URL and path configured in bootstrap configuration.
     * In dependence of Parameter urlPart the respective part of URL will be returned.  
     * 
     * @param value_p represents the value of field selected in drop down list coming from ECM backend. 
     * This Parameter represents also the part of filename so for example value "New" is selected then 
     * the file name could be in dependence of configuration "status_new.png".
     * @param urlPart represents the URL part which will be returned.
     * - Parameter "BEGIN_URL" returns the first part of URL without value/wildcard
     * - Parameter "END_URL returns the last part of URL without value/wildcard (".png")
     * - Parameter "COMPLETE_URL returns the complete URL including complete filename in dependence of selected value
     * @return returns in dependence of Parameter urlPart a complete URL or only a part of it 
     * @throws Exception
     */
    public String getUrl(String value_p, int urlPart) throws Exception
    {
        //get configured path
        //example of configured path: "/OwControlImageInsteadOfValue/status_{value}.png"
        String[] split = pathPattern.split(VALUE_PATTERN);

        if (urlPart == BEGIN_URL)
        {
            return getContext().getBaseURL() + split[0];
        }
        else if (urlPart == END_URL)
        {
            return split[1];
        }
        else if (urlPart == COMPLETE_PATH)
        {
            String url = getContext().getBaseURL() + split[0] + value_p + split[1];
            return url;
        }
        return null;
    }

    /**
     * Inserts a Java Script function for updating the icon respectively image on changing the value in drop down.
     * This JS Function will be returned as String will be included/written in the respective JSP Page. 
     * 
     * @param strID_p identifies Drop Down element in JSP Page
     * @return returns the Java Script Function as String.
     * @throws Exception
     */
    public String insertUpdateFunction(String strID_p) throws Exception
    {
        StringBuffer function = new StringBuffer("\nfunction renewIcon(){\n");
        /*function.append("var value = document.getElementById(\"" + strID_p + "\").value;\n");*/
        function.append("var value = this.value;");//FIXME works only with ExtJS
        function.append("var beginPath = \"" + getUrl("", BEGIN_URL) + "\";\n");
        function.append("var endPath = \"" + getUrl("", END_URL) + "\";\n");

        function.append("var url = beginPath + value.toLowerCase() + endPath;\n");

        function.append("document.getElementById(\"img");
        function.append(strID_p);
        function.append("\").src = url;\n");
        function.append("document.getElementById(\"img");
        function.append(strID_p);
        function.append("\").alt = value;\n");

        function.append("}\n");

        return function.toString();
    }

    /** render an image depending on provided value
     *
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object Value to be displayed
     * @param id_p String id which should be provided to rendered image tag (can be null, then no Id is rendered)
     */
    public void renderImage(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, String id_p) throws Exception
    {
        w_p.write("<img src=\"");
        if (value_p != null && value_p.toString() != null)
        {
            String fieldValue = value_p.toString();

            w_p.write(getUrl(fieldValue.toLowerCase(), COMPLETE_PATH)); //@since 4.0.0.1 --> toLowerCase()
            w_p.write("\" alt=\"");
            w_p.write(fieldValue);
            w_p.write("\" title=\"");
            w_p.write(fieldValue);
            w_p.write("\"");
        }
        else
        {
            w_p.write(getContext().getBaseURL());
            w_p.write("/js/extjs/resources/images/default/s.gif\" alt=\"\"");
        }
        if (id_p != null)
        {
            w_p.write(" id=\"img");
            w_p.write(id_p);
            w_p.write("\"");
        }
        w_p.write(" />");
    }

    /** Formats and displays the value attached to the PropertyClass in HTML
    * @param w_p Writer object to write HTML to
    * @param fieldDef_p OwFieldDefinition definition of field
    * @param value_p Object Value to be displayed
    */
    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        renderImage(w_p, fieldDef_p, value_p, null);
    }

    /** Formats and displays the value attached to the fieldClass in HTML for use in a HTML Form.
     * It also creates the necessary code to update the value in the form upon request.
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition
     * @param field_p OwField Value to be displayed
     * @param strID_p ID of the HTML element
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        getContext().renderJSInclude("/js/wewebucombo.js", w_p);
        // configure ExtJs after loading
        w_p.write("<script type=\"text/javascript\">\n");
        w_p.write("Ext.BLANK_IMAGE_URL = \"");
        w_p.write(OwHTMLHelper.encodeJavascriptString(getContext().getBaseURL() + "/js/extjs/resources/images/default/s.gif"));
        w_p.write("\";\n");

        w_p.write("</script>\n");

        w_p.write("<div class='OwInputControl OwInputControlAdvancedChoice'> ");
        w_p.write("<select name='");
        OwHTMLHelper.writeSecureHTML(w_p, strID_p);
        w_p.write("' id='");
        OwHTMLHelper.writeSecureHTML(w_p, strID_p);
        w_p.write("' >\n");
        //w_p.write(renderEvents());

        Iterator<?> it = fieldDef_p.getEnums().iterator();
        Object value = field_p.getValue();
        boolean fSelected = false;
        // look for selected item
        if (value == null || value.equals(""))
        {
            fSelected = true;
            w_p.write("<option value='' selected >&nbsp;</option>\n");
        }
        while (it.hasNext())
        {
            OwEnum Item = (OwEnum) it.next();
            w_p.write("<option value='");
            OwHTMLHelper.writeSecureHTML(w_p, Item.getValue().toString());
            w_p.write("' ");
            // look for selected item
            if (Item.getValue().equals(value))
            {
                fSelected = true;
                w_p.write("selected ");
            }
            w_p.write(">");
            OwHTMLHelper.writeSecureHTML(w_p, Item.getDisplayName(getFieldManager().getContext().getLocale()));
            w_p.write("</option>\n");

        }
        // === Inconsistent-check: if current value was yet selected among options
        if (!fSelected)
        {
            // === current value is not among options so we need to add it
            String strValue = "";
            if (null != value)
            {
                strValue = value.toString();
            }
            w_p.write("<option value='");
            OwHTMLHelper.writeSecureHTML(w_p, strValue);
            w_p.write("' selected>");
            w_p.write(OwString.localize(getFieldManager().getContext().getLocale(), "app.OwStandardFieldManager.outofrangelist.symbol", "(!)"));
            OwHTMLHelper.writeSecureHTML(w_p, strValue);
            w_p.write("</option>");
        }
        w_p.write("</select>");
        String encId = OwHTMLHelper.encodeJavascriptString(strID_p);
        String varName = OwHTMLHelper.encodeJavascriptVariableName(encId);
        renderImage(w_p, fieldDef_p, field_p.getValue(), encId);
        w_p.write("</div>\n");

        if (!fSelected)
        {
            // === info text to the user about out of range
            w_p.write("<span class='OwInstructionName'>");
            w_p.write(OwString.localize(getFieldManager().getContext().getLocale(), "app.OwStandardFieldManager.outofrangelist", "(!) Out of range list"));
            w_p.write("</span>");
        }

        // js for advanced combo
        w_p.write("<script type=\"text/javascript\">\n");
        w_p.write("Ext.onReady(function(){\n");
        w_p.write("     var converted_" + varName + " = new Ext.form.ComboBox({\n");
        w_p.write("     typeAhead: true,\n");
        w_p.write("     triggerAction: 'all',\n");
        w_p.write("     transform:'" + encId + "',\n");
        w_p.write("     grow:true,\n");
        w_p.write("     autoWidth:'auto',\n");
        w_p.write("     listWidth:'auto',\n");
        w_p.write("     selectOnFocus: true,\n");
        w_p.write("     forceSelection:true\n");
        w_p.write("});\n");

        w_p.write("converted_");
        w_p.write(varName);
        w_p.write(".el.setWidth('auto');\n");

        w_p.write("converted_");
        w_p.write(varName);
        w_p.write(".addListener('select', ");
        w_p.write(insertUpdateFunction(encId));
        w_p.write(");\n");
        //invoke autosize
        w_p.write("converted_");
        w_p.write(varName);
        w_p.write(".autoSize();\n");

        w_p.write("});\n");
        w_p.write("</script>\n");

        //renderImage(w_p, fieldDef_p, field_p.getValue());
    }

    /** update the property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     * 
     * @param request_p  HttpServletRequest
     * @param fieldDef_p OwFieldDefinition
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     */
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        String value = request_p.getParameter(strID_p);
        if (value != null && !"".equals(value))
        {
            Iterator<?> it = fieldDef_p.getEnums().iterator();
            Object val = null;
            while (it.hasNext())
            {
                val = ((OwEnum) it.next()).getValue();
                if (value.equals(String.valueOf(val)))
                {
                    break;
                }
                val = null;
            }
            return val;
        }
        else
        {
            return value_p;
        }
    }

    //TODO prepared for refactoring to use combo box renderer
    //  public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    //  {
    //      OwComboModel model = createComboBoxModel(fieldDef_p, field_p.getValue()); 
    //      OwComboboxRenderer renderer = ((OwMainAppContext)getContext()).createComboboxRenderer(model, strID_p, fieldDef_p, getFieldManager().getFieldProvider(), null);
    //      renderer.addEvent("change", "renewIcon");//check onChange function in classic/basic renderer
    //      renderer.renderCombo(w_p);
    //      renderImage(w_p, fieldDef_p, field_p.getValue(), strID_p);
    //  }
    //    
    //    protected OwComboModel createComboBoxModel(OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    //    {
    //        Locale locale = getContext().getLocale();
    //        OwEnumCollection enums = fieldDef_p.getEnums();
    //        int size = enums.size();
    //        String[] displayNames = new String[size];
    //        String[] values = new String[size];
    //        Iterator<?> enumsIt = enums.iterator();
    //        int i = 0;
    //        while (enumsIt.hasNext())
    //        {
    //            OwEnum enumItem = (OwEnum) enumsIt.next();
    //            displayNames[i] = enumItem.getDisplayName(locale);
    //            values[i] = enumItem.getValue() == null ? null : enumItem.getValue().toString();
    //            i++;
    //        }
    //
    //        return factoryComboBoxModelObject(fieldDef_p, value_p == null ? null : value_p.toString(), values, displayNames);
    //    }
    //
    //    protected OwComboModel factoryComboBoxModelObject(OwFieldDefinition fieldDef_p, String currentValue_p, String[] values_p, String[] displayNames_p)
    //    {
    //        return new OwDefaultComboModel(false, false, currentValue_p, displayNames_p, values_p);
    //    }

}