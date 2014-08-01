package com.wewebu.ow.server.app;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInaccessibleException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.field.OwStandardDecoratorFieldDefinition;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwDateTimeUtil;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Standard Implementation of the Property User Interface. <br/>
 * Displays Property as HTML and creates HTML Form Elements for editing properties, also performs validation. <br/>
 * NOTE: This class is instantiated once for a block of properties. is only one static instance
 * of this class in one application. <br/><br/>
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
public class OwStandardFieldManager extends OwFieldManager
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardFieldManager.class);

    /** query string for the selected array item */
    public static final String ARRAY_ITEM_INDEX_KEY = "aitem";

    /** query string for the selected field */
    public static final String FIELD_ID_KEY = "fieldid";
    /** threshold config node of string length for a textarea, below this threshold a textfield is rendered */
    public static final String THRESHOLD_CONFIG_NODE = "TextAreaThreshold";
    /** configuration node name for the max size of string input fields (&quot;MAXLENGTH&quot; attribute)*/
    public static final String MAX_STRING_SIZE_CONFIG_NODE = "DefaultStringMaxSize";

    /** delimiter to build complex id's */
    protected static final String COMPLEX_ID_DELIMITER = "_";

    // date formatter
    protected DateFormat m_DateFormat;

    protected DateFormat m_TimeFormat;

    protected DateFormat m_DateWithoutTimeFormat;

    // number formatter
    protected NumberFormat m_NumberFormat;

    /** map of OwFieldManagerControl controls */
    protected Map<String, OwFieldManagerControl> m_FieldCtrlMap = new LinkedHashMap<String, OwFieldManagerControl>();

    /**  a reference to a mimemanager that can be used to render objects */
    protected OwMimeManager m_MimeManager = new OwMimeManager();

    /** threshold of string length for a textarea, below this threshold a textfield is rendered */
    protected int m_textareaThreshold;
    /** value of input fields where MAXLENGTH
     * @since 3.2.0.0*/
    protected int m_defMaxStringSize;
    /**
     * use default restore mechanism or restore from request strategy (for invalid field values)
     */
    protected boolean m_restoreFromRequestStrategy = true;

    /** init the target after the context is set.
    */
    protected void init() throws Exception
    {
        super.init();

        // === attach mimemanager
        m_MimeManager.attach(getContext(), null);

        // === attach the field controls from XML
        attachFieldControlsFromConfiguration();

        // === formatter
        // Date
        m_DateFormat = OwDateTimeUtil.createDateFromat(getContext().getLocale(), ((OwMainAppContext) getContext()).getDateFormatString());
        m_TimeFormat = OwDateTimeUtil.createDateFromat(getContext().getLocale(), ((OwMainAppContext) getContext()).getDateFormatStringWithoutDate());
        m_DateWithoutTimeFormat = OwDateTimeUtil.createDateFromat(getContext().getLocale(), ((OwMainAppContext) getContext()).getDateFormatStringWithoutTime());

        // Number
        m_NumberFormat = NumberFormat.getNumberInstance(getContext().getLocale());
        m_NumberFormat.setGroupingUsed(false);

        // fetch textarea threshold
        m_textareaThreshold = getConfigNode().getSafeIntegerValue(THRESHOLD_CONFIG_NODE, 255);
        m_defMaxStringSize = getConfigNode().getSafeIntegerValue(MAX_STRING_SIZE_CONFIG_NODE, 32);
    }

    /** read XML configuration and attach the field controls
     */
    protected void attachFieldControlsFromConfiguration() throws Exception
    {
        Node fieldcontrols = getConfigNode().getSubNode("FieldControls");
        if (fieldcontrols != null)
        {
            for (Node field = fieldcontrols.getFirstChild(); field != null; field = field.getNextSibling())
            {

                if ((field.getNodeType() == Node.ELEMENT_NODE) && field.getNodeName().equals("FieldControl"))
                {
                    // === found field control
                    String strFieldClass = OwXMLDOMUtil.getSafeStringAttributeValue(field, "fieldclass", null);
                    String strJavaType = OwXMLDOMUtil.getSafeStringAttributeValue(field, "javatype", null);
                    String strObjectType = OwXMLDOMUtil.getSafeStringAttributeValue(field, "objecttype", null);

                    // === create field control
                    OwFieldManagerControl newControl = null;
                    try
                    {
                        Class newClass = Class.forName(strFieldClass);
                        newControl = (OwFieldManagerControl) newClass.newInstance();
                    }
                    catch (Exception e)
                    {
                        LOG.fatal("Fieldcontrol could not be created: " + strFieldClass, e);
                        continue;
                    }

                    // === attach it
                    if (strJavaType != null)
                    {
                        // control for object rendition
                        attachFieldControlByType(strJavaType, newControl, field);
                    }
                    else if (strObjectType != null)
                    {
                        // control for object rendition
                        attachFieldControlByClass(strObjectType, newControl, field);
                    }
                    else
                    {
                        LOG.fatal("OwStandardFieldManager.attachFieldControlsFromConfiguration: Please define either javatype or objecttype in FieldControl.");
                    }
                }
            }
        }
    }

    /** get a reference to a mimemanager that can be used to render objects
     *
     * @return OwMimeManager
     */
    public OwMimeManager getMimeManager()
    {
        return m_MimeManager;
    }

    /** get the formatter used for numbers
     *
     * @return NumberFormat
     */
    protected NumberFormat getNumberFormater()
    {
        return m_NumberFormat;
    }

    /** get the formatter used for dates
     *
     * @return the current {@link DateFormat} for date conversions
     */
    protected DateFormat getDateFormater()
    {
        return m_DateFormat;
    }

    /** get the formatter used for time
     *
     * @return the current {@link DateFormat} for time conversions
     */
    protected DateFormat getTimeFormater()
    {
        return m_TimeFormat;
    }

    /** get the formatter used for time
     *
     * @return the current {@link DateFormat} for time conversions
     */
    protected DateFormat getDateWithoutTimeFormater()
    {
        return m_DateWithoutTimeFormat;
    }

    /** attach a field control to the fieldmanager
     *
     * @param strJavaClassName_p java type of the field that uses the control
     * @param control_p OwFieldManagerControl that renders the field
     * @param configNode_p DOM Node to the configuration XML for the control, or null to use defaults
     */
    public void attachFieldControlByType(String strJavaClassName_p, OwFieldManagerControl control_p, Node configNode_p) throws Exception
    {
        // use the same map
        attachFieldControlByClass(strJavaClassName_p, control_p, configNode_p);
    }

    /** attach a field control to the fieldmanager
     *
     * @param strFieldClassName_p class name of the field that uses the control
     * @param control_p OwFieldManagerControl that renders the field
     * @param configNode_p DOM Node to the configuration XML for the control, or null to use defaults
     */
    public void attachFieldControlByClass(String strFieldClassName_p, OwFieldManagerControl control_p, Node configNode_p) throws Exception
    {
        control_p.init(this, configNode_p);
        // attach to context
        control_p.attach(getContext(), null);

        // check if a control was already registered with the same name, if so, we must not forget to detach
        OwFieldManagerControl ctrl = m_FieldCtrlMap.get(strFieldClassName_p);
        if (null != ctrl)
        {
            LOG.warn("OwStandardFieldManager.attachFieldControlByClass: Check configuration! An existing field control is replaced for class = " + strFieldClassName_p);
            ctrl.detach();
        }

        m_FieldCtrlMap.put(strFieldClassName_p, control_p);
    }

    /** get a field control by the given java class type
     *
     * @param strJavaClassName_p String
     * @return OwFieldManagerControl or null
     */
    public OwFieldManagerControl getFieldControlByType(String strJavaClassName_p)
    {
        return m_FieldCtrlMap.get(strJavaClassName_p);
    }

    /** get a field control by the given field class name
     *
     * @param strFieldClassName_p String
     * @return OwFieldManagerControl or null
     */
    public OwFieldManagerControl getFieldControlByClass(String strFieldClassName_p)
    {
        return m_FieldCtrlMap.get(strFieldClassName_p);
    }

    /**
     * Helper method to get an registered OwFieldManagerControl for specific field definition.
     * @param fieldDef OwFieldDefinition
     * @return OwFieldManagerControl if registered, else null
     * @since 4.2.0.0
     */
    protected OwFieldManagerControl getFieldControl(OwFieldDefinition fieldDef)
    {
        OwFieldManagerControl fieldCtrl = getFieldControlByClass(fieldDef.getClassName());
        if (fieldCtrl == null)
        {
            fieldCtrl = getFieldControlByType(fieldDef.getJavaClassName());
        }
        return fieldCtrl;
    }

    /** detach from event target map
     */
    public void detach()
    {
        super.detach();

        // detach the field manager and MIME-manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();

        // detach the field controls
        for (OwFieldManagerControl fieldCtrl : m_FieldCtrlMap.values())
        {
            fieldCtrl.detach();
        }
    }

    /** formates and displays the value attached to the PropertyClass in HTML
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object Value to be displayed
     */
    protected void insertReadOnlyFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        OwFieldManagerControl fieldcontrol = getFieldControl(fieldDef_p);

        if (fieldcontrol != null)
        {
            // === property is rendered in field control
            try
            {
                fieldcontrol.insertReadOnlyField(w_p, fieldDef_p, value_p);
            }
            catch (Exception ex)
            {
                LOG.error("The fieldcontrol failed to execute insertReadOnlyField.", ex);
                w_p.write("The fieldontrol ");
                w_p.write(fieldcontrol.getClass().getName());
                w_p.write(" failed to execute with a ");
                w_p.write(ex.getClass().getName());
                w_p.write(".<br>");
                w_p.write("Error message: ");
                w_p.write(ex.toString());
                w_p.write("<br>");
                StackTraceElement[] trace = ex.getStackTrace();
                for (int i = 0; i < Math.min(trace.length, 20); i++)
                {
                    w_p.write(trace[i].toString() + "<br>");
                }
            }
        }
        else if (value_p != null)
        {
            if (fieldDef_p.isArray())
            {
                // === array values
                Object[] values = (Object[]) value_p;
                boolean fDelimiter = false;
                for (int i = 0; i < values.length; i++)
                {
                    // delimiter
                    if (fDelimiter)
                    {
                        w_p.write("<div style='clear:left;padding-top:2px'>");
                        w_p.write("<hr class='OwStandardFieldManager_Array'>");
                        w_p.write("</div>");

                    }

                    insertSingleReadOnlyFieldInternal(w_p, fieldDef_p, values[i]);

                    fDelimiter = true;
                }
            }
            else
            {
                // === single value
                insertSingleReadOnlyFieldInternal(w_p, fieldDef_p, value_p);
            }
        }
    }

    /** clear the map before you call insert... methods, otherwise the map would increase to infinite
       *
       *  NOTE: Never forget to call this function in your onRender Method
       *
       */
    public void reset()
    {
        m_MimeManager.reset();
        super.reset();
    }

    /**
     * <p>Render a single read-only value </p>
     * @param w_p Writer used for writing the rendering
     * @param fieldDef_p OwFieldDefinition of property for which the control is rendered
     * @param value_p Object reference to the value of the property
     * @throws Exception
     */
    public void insertSingleReadOnlyFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (fieldDef_p.isComplex())
        {
            // === render read-only complex type
            insertSingleComplexReadOnlyFieldInternal(w_p, fieldDef_p, (Object[]) value_p);
        }
        else if (fieldDef_p.isEnum())
        {
            // === enum type, display the corresponding text
            OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getEnums().getDisplayName(getContext().getLocale(), value_p));
        }
        else
        {
            if (fieldDef_p.getJavaClassName().equalsIgnoreCase("java.util.Date"))
            {

                // === Date or Time type
                String stringFormatedValue = "";
                OwFormat format = fieldDef_p.getFormat();
                DateFormat filedDefDateFormat = getDateFormater();
                if (format == null)
                {
                    filedDefDateFormat = getDateFormater();
                }
                else if (format.ignoreDate())
                {
                    filedDefDateFormat = getTimeFormater();
                }
                else if (format.ignoreTime())
                {
                    filedDefDateFormat = getDateWithoutTimeFormater();
                }
                stringFormatedValue = filedDefDateFormat.format((java.util.Date) value_p);

                OwHTMLHelper.writeSecureHTML(w_p, stringFormatedValue);
            }
            else if (fieldDef_p.getJavaClassName().equalsIgnoreCase("java.lang.Boolean"))
            {
                // === bool value
                if (value_p != null) // ignore null values, don't write anything
                {
                    if (((Boolean) value_p).booleanValue())
                    {
                        w_p.write("<input disabled type='checkbox' checked>");
                    }
                    else
                    {
                        w_p.write("<input disabled type='checkbox'>");
                    }
                }
            }
            else
            {
                // === all other types
                if (value_p != null) // ignore null values, don't write anything
                {
                    String strText = value_p.toString();

                    // try to treat as a number
                    try
                    {
                        strText = getNumberFormater().format(value_p);
                    }
                    catch (Exception e)
                    {
                        // ignore, not a number
                    }

                    OwHTMLHelper.writeSecureHTML(w_p, strText, true);
                }

            }
        }
    }

    /**
     * Insert choice list for enum fields as default HTML single selectable list.
     *  Depending on the class configured in owbootstrap.xml, the rendered control has preset CSS
     *  classes for customizing :
     *  <ul>
     *  <li><b>OwInputControl</b> default Workdesk CSS class</li>
     *  <li><b>OwInputControlChoice</b> type specific CSS class</li> or
     *  <li><b>OwInputControlAdvancedChoice</b> type specific CSS class</li>
     *  <li><b>OwInputControl_&quot;PropertyName&quot;</b> property specific CSS class</li>
     *  </ul>
     *
     * @param w_p Writer for writing the rendering
     * @param fieldDef_p OwFieldDefinition of property
     * @param value_p Object to display with given enum
     * @param strID_p String for ID attribute of HTML control
     * @param fAllowEmptyField_p boolean true = add a empty field to the list
     *
     * @throws Exception
     */
    public void insertChoiceListControl(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p, boolean fAllowEmptyField_p) throws Exception
    {
        OwComboModel model = createComboBoxModel(fieldDef_p, value_p, fAllowEmptyField_p);
        OwComboboxRenderer renderer = m_MainContext.createComboboxRenderer(model, strID_p, fieldDef_p, getFieldProvider(), null);
        renderer.renderCombo(w_p);
    }

    /**
     * Creates a model for combobox.
     * @param fieldDef_p - fieldDefinition
     * @param value_p - the value of the object
     * @param fAllowEmptyField_p - allow empty value flag.
     * @return {@link OwComboModel} - the model of the combo to be rendered.
     * @throws Exception
     * @since 3.0.0.0
     */
    protected OwComboModel createComboBoxModel(OwFieldDefinition fieldDef_p, Object value_p, boolean fAllowEmptyField_p) throws Exception
    {
        Locale locale = getContext().getLocale();
        OwEnumCollection enums = fieldDef_p.getEnums();
        int size = enums.size();
        String[] displayNames = new String[size];
        String[] values = new String[size];
        Iterator enumsIt = enums.iterator();
        int i = 0;
        while (enumsIt.hasNext())
        {
            OwEnum enumItem = (OwEnum) enumsIt.next();
            displayNames[i] = enumItem.getDisplayName(locale);
            values[i] = enumItem.getValue() == null ? null : enumItem.getValue().toString();
            i++;
        }

        OwComboModel model = new OwFieldManagerComboModel(locale, fAllowEmptyField_p, value_p == null ? null : value_p.toString(), displayNames, values);
        return model;
    }

    /** format and displays the value attached to the PropertyClass in HTML for use in a HTML Form.
     * It also creates the necessary code to update the value in the form upon request.
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param field_p OwField Value to be displayed
     * @param strID_p ID of the HTML element
     */
    protected void insertEditFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        OwFieldManagerControl fieldcontrol = getFieldControl(fieldDef_p);
        if (fieldcontrol != null)
        {
            // === property is rendered in field control
            try
            {
                fieldcontrol.insertEditField(w_p, fieldDef_p, field_p, strID_p);
            }
            catch (Exception ex)
            {
                LOG.error("The fieldcontrol failed to execute insertEditField.", ex);
                w_p.write("The fieldontrol ");
                w_p.write(fieldcontrol.getClass().getName());
                w_p.write(" failed to execute with a ");
                w_p.write(ex.getClass().getName());
                w_p.write(".<br>");
                w_p.write("Error message: ");
                w_p.write(ex.toString());
                w_p.write("<br>");
                StackTraceElement[] trace = ex.getStackTrace();
                for (int i = 0; i < Math.min(trace.length, 20); i++)
                {
                    w_p.write(trace[i].toString() + "<br>");
                }
            }
        }
        else
        {
            // === all other values
            if (fieldDef_p.isArray())
            {
                OwFieldDefinition definition = field_p.getFieldDefinition();
                String arrayFieldDisplayName = definition.getDisplayName(getContext().getLocale());
                w_p.write("<fieldset id='");
                String fieldId = String.valueOf(field_p.hashCode());
                w_p.write(fieldId);
                w_p.write("' class='accessibility'>");
                w_p.write("<legend class='accessibility'>");
                w_p.write(arrayFieldDisplayName);
                w_p.write("</legend>");

                // === array of values
                Object[] values = (Object[]) field_p.getValue();
                if (values != null)
                {
                    w_p.write("<table>");

                    for (int i = 0; i < values.length; i++)
                    {
                        w_p.write("<tr class='");

                        w_p.write(i % 2 == 0 ? "OwGeneralList_RowEven" : "OwGeneralList_RowOdd");

                        w_p.write("'><td valign='center'>");

                        insertSingleEditFieldInternal(w_p, fieldDef_p, values[i], strID_p + COMPLEX_ID_DELIMITER + String.valueOf(i));

                        w_p.write("</td><td>");
                        /* For SearchView, a SearchCriteria which is a multi value field
                         * should display one input field by default. This Inputfield
                         * must be rendered without a delete button.
                         */
                        if (i == 0 && field_p instanceof com.wewebu.ow.server.field.OwSearchCriteria && values.length == 1)
                        {
                            w_p.write("</td></tr>");
                        }
                        else
                        {
                            writeRemoveMultiValueItemLink(w_p, fieldDef_p, i, strID_p);
                            w_p.write("</td></tr>");
                        }
                    }

                    w_p.write("</table>");
                }
                writeAddMultiValueItemLink(w_p, fieldDef_p, strID_p);

                w_p.write("</fieldset>");
            }
            else
            {
                // === single value
                insertSingleEditFieldInternal(w_p, fieldDef_p, field_p.getValue(), strID_p);
            }
        }
    }

    /**
     * Overridable method to insert the "Delete" Link after each item of a multi-valued field.
     *
     * @param w_p The writer to write to
     * @param fieldDef_p - the field definition object, can be null
     * @param index_p The index of the item inside the field
     * @param strID_p The ID of the field
     *
     * @throws Exception
     * @since 3.0.0.0
     */
    protected void writeRemoveMultiValueItemLink(Writer w_p, OwFieldDefinition fieldDef_p, int index_p, String strID_p) throws IOException, Exception
    {
        String tooltipText = "";
        if (fieldDef_p != null)
        {
            String fieldDisplayName = fieldDef_p.getDisplayName(getContext().getLocale());
            tooltipText = getContext().localize2("app.OwStandardFieldManager.delarrayitemindexed", "Delete element at position %1 from %2", "" + (index_p + 1), fieldDisplayName);
        }
        w_p.write("<a title=\"");
        w_p.write(getContext().localize("app.OwStandardFieldManager.delarrayitem", "Delete Element"));
        w_p.write("\" href=\"");
        w_p.write(getFormEventURL("DeleteArrayItem", ARRAY_ITEM_INDEX_KEY + "=" + String.valueOf(index_p) + "&" + FIELD_ID_KEY + "=" + strID_p + "&" + OwFieldManager.FLAG_DISABLE_VALIDATION + "=true"));
        w_p.write("\"><img src=\"");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/deletebtn.png\"");
        w_p.write(" alt=\"");
        w_p.write(tooltipText);
        w_p.write("\" title=\"");
        w_p.write(tooltipText);
        w_p.write("\"/></a>");
    }

    /**
     * Overridable method to insert the "Add" Link after a multi-valued field.
     *
     * @param w_p The writer to write to
     * @param fieldDef_p - the {@link OwFieldDefinition} object. Can be <code>null</code>.
     * @param strID_p The ID of the field
     * @throws Exception
     * @since 3.0.0.0
     */
    protected void writeAddMultiValueItemLink(Writer w_p, OwFieldDefinition fieldDef_p, String strID_p) throws IOException, Exception
    {
        w_p.write("<a title=\"");
        w_p.write(getContext().localize("app.OwStandardFieldManager.addarrayitemtooltip", "Add Element"));
        w_p.write("\" href=\"");
        w_p.write(getFormEventURL("AddArrayItem", FIELD_ID_KEY + "=" + strID_p + "&" + OwFieldManager.FLAG_DISABLE_VALIDATION + "=true"));
        w_p.write("\"><img style=\"vertical-align:middle;border:0px none;margin:3px 0px;\" src=\"");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/addbtn.png\" ");
        String addItemTooltip = "";
        if (fieldDef_p != null)
        {
            addItemTooltip = getContext().localize1("app.OwStandardFieldManager.addarrayitemtooltipimg", "Add new item for %1.", fieldDef_p.getDisplayName(getContext().getLocale()));
        }
        w_p.write("alt=\"");
        w_p.write(addItemTooltip);
        w_p.write("\" title=\"");
        w_p.write(addItemTooltip);
        w_p.write("\"  /></a>");
    }

    /** get the field from the given complex ID <br />
     * <code>&lt;fieldid&gt;.&lt;dimension 0&gt;.&lt;dimension 1&gt;...&lt;dimension n&gt;</code>
     * @param complexid_p String  e.g. 1254993.1.5.2.3
     */
    public OwField getField(String complexid_p)
    {
        int iComplexStart = complexid_p.indexOf(COMPLEX_ID_DELIMITER);
        if (-1 != iComplexStart)
        {
            // we deal with multi dimensions
            String fieldid = complexid_p.substring(0, iComplexStart);
            String rest = complexid_p.substring(iComplexStart + 1);
            OwField field = super.getField(fieldid);

            return new OwInternalDimensionField(field, rest);
        }
        else
        {
            return super.getField(complexid_p);
        }
    }

    /** called when user clicks to delete an array item */
    public void onDeleteArrayItem(HttpServletRequest request_p) throws Exception
    {
        // === copy the values into a new array without the selected item
        int iIndex = Integer.parseInt(request_p.getParameter(ARRAY_ITEM_INDEX_KEY));

        OwField field = getField(request_p.getParameter(FIELD_ID_KEY));

        Object[] values = (Object[]) field.getValue();

        //due to update-event call sequence we can't display errors on the delete event request
        clearFieldError(field);

        if (values.length < 2)
        {
            // set new value
            field.setValue(null);
            return;
        }

        Object[] newValues = new Object[values.length - 1];

        int iNew = 0;
        int iOrigin = 0;
        do
        {
            if (iIndex == iOrigin)
            {
                iOrigin++;
            }

            newValues[iNew++] = values[iOrigin++];
        } while (iNew < newValues.length);

        // set new value
        field.setValue(newValues);
    }

    /** called when user clicks to add an array item */
    public void onAddArrayItem(HttpServletRequest request_p) throws Exception
    {
        // === copy the values into a new array and add one item
        String key = request_p.getParameter(FIELD_ID_KEY);
        OwField field = getField(key);

        Object[] values = (Object[]) field.getValue();

        Object[] newValues = null;

        if (null == values)
        {
            // === no previous values
            newValues = new Object[1];
        }
        else
        {
            newValues = new Object[values.length + 1];

            // copy values
            for (int i = 0; i < values.length; i++)
            {
                newValues[i] = values[i];
            }
        }

        // add an item
        newValues[newValues.length - 1] = OwStandardObjectClass.createInitialNullValue(field.getFieldDefinition(), true);

        // set new value
        field.setValue(newValues);

        StringBuilder strElementID = new StringBuilder(key).append(COMPLEX_ID_DELIMITER).append(newValues.length - 1);
        if (field.getFieldDefinition().isComplex())
        {
            strElementID.append(COMPLEX_ID_DELIMITER).append("0");
        }
        setFocusControlID(strElementID.toString());
    }

    /**
     *<p>
     * Internal wrapper for complex fields.
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
    private static class OwSingleAttributeComplexInternalField implements OwField
    {
        private OwFieldDefinition m_fielddefinition;
        private int m_index;
        private Object[] m_value;

        /** constructs a field for a single tuple in a complex field
         *
         * @param values_p
         * @param index_p
         * @param fielddefinition_p
         * @throws Exception
         */
        public OwSingleAttributeComplexInternalField(Object[] values_p, int index_p, OwFieldDefinition fielddefinition_p) throws Exception
        {
            this.m_value = values_p;
            this.m_index = index_p;
            this.m_fielddefinition = fielddefinition_p;
        }

        public OwFieldDefinition getFieldDefinition() throws Exception
        {
            return m_fielddefinition;
        }

        public Object getValue() throws Exception
        {
            return m_value[m_index];
        }

        public void setValue(Object value_p) throws Exception
        {
            m_value[m_index] = value_p;
        }

    }

    /**
     *<p>
     * Decorator field definition for dimension field's.<br/>
     * Decorates the given field definition with isArray = false.
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
    private static class OwInternalNoArrayDimensionFieldDefinition extends OwStandardDecoratorFieldDefinition
    {

        private OwFieldDefinition m_fielddefinition;

        public OwInternalNoArrayDimensionFieldDefinition(OwFieldDefinition fielddefinition_p)
        {
            m_fielddefinition = fielddefinition_p;
        }

        public OwFieldDefinition getWrappedFieldDefinition()
        {
            return m_fielddefinition;
        }

        /* (non-Javadoc)
         * @see com.wewebu.ow.server.field.OwStandardDecoratorFieldDefinition#isArray()
         */
        public boolean isArray() throws Exception
        {
            return false;
        }

    }

    /**
     *<p>
     * Internal wrapper for fields from ID needed to resolve paths to fields as in complex fields.
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
    private static class OwInternalDimensionField implements OwField
    {
        private int m_dimension;

        private OwFieldDefinition m_fielddefinition;

        private Object m_value;

        /** create a internal complex dimension field that points to a value within the complex tree
         *
         * @param basefield_p the base field with the complex tree value
         * @param dimensionid_p the rest ID of the field ID that designates the dimension of the complex field <fieldid>.<dimension 0>.<dimension 1>...<dimension n>, e.g. 1254993.1.5.2.3
         */
        public OwInternalDimensionField(OwField basefield_p, String dimensionid_p)
        {
            try
            {
                StringTokenizer tokens = new StringTokenizer(dimensionid_p, OwStandardFieldManager.COMPLEX_ID_DELIMITER);

                m_value = basefield_p.getValue();
                Object oldvalue = m_value;

                m_fielddefinition = basefield_p.getFieldDefinition();

                while (tokens.hasMoreTokens())
                {
                    m_value = oldvalue;

                    String token = tokens.nextToken();

                    m_dimension = Integer.parseInt(token);

                    if (m_fielddefinition.isArray())
                    {
                        m_fielddefinition = new OwInternalNoArrayDimensionFieldDefinition(m_fielddefinition);
                    }
                    else if (m_fielddefinition.isComplex())
                    {
                        m_fielddefinition = (OwFieldDefinition) m_fielddefinition.getComplexChildClasses().get(m_dimension);
                    }

                    oldvalue = ((Object[]) m_value)[m_dimension];
                }
            }
            catch (Exception e)
            {
                throw new java.lang.RuntimeException(e);
            }
        }

        public OwFieldDefinition getFieldDefinition() throws Exception
        {
            return m_fielddefinition;
        }

        public Object getValue() throws Exception
        {
            return ((Object[]) m_value)[m_dimension];
        }

        public void setValue(Object value_p) throws Exception
        {
            ((Object[]) m_value)[m_dimension] = value_p;
        }

    }

    /** Inserts a read only complex field
     *
     * @param w_p output writer
     * @param fieldDef_p the complex field definition
     * @param value_p the complex value (must be an
     * @throws Exception
     */
    protected void insertSingleComplexReadOnlyFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object[] value_p) throws Exception
    {
        Object[] complexvalues = value_p;

        Iterator it = fieldDef_p.getComplexChildClasses().iterator();
        int i = 0;

        // iterate over the complex types
        while (it.hasNext())
        {
            OwFieldDefinition fielddef = (OwFieldDefinition) it.next();

            w_p.write("<div class=\"OwComplexPropertyControl\">");

            w_p.write("<div class=\"OwComplexPropertyControl_header\">");
            w_p.write(fielddef.getDisplayName(getContext().getLocale()));
            w_p.write("</div>");

            w_p.write("<div class=\"OwComplexPropertyControl_value\">");
            // retrieve corresponding value
            insertReadOnlyFieldInternal(w_p, fielddef, complexvalues[i]);
            w_p.write("</div>");

            w_p.write("</div>");

            i++;
        }
    }

    /** insert a complex field
     *
     * @param w_p
     * @param fieldDef_p
     * @param value_p
     * @param strID_p
     * @throws Exception
     */
    protected void insertSingleComplexEditFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        String arrayFieldDisplayName = fieldDef_p.getDisplayName(getContext().getLocale());
        w_p.write("<fieldset id=\"");
        w_p.write(strID_p);
        w_p.write("\" class=\"accessibility\">");
        w_p.write("<legend class=\"accessibility\">");
        w_p.write(arrayFieldDisplayName);
        w_p.write("</legend>");

        Object[] complexvalues = (Object[]) value_p;

        Iterator it = fieldDef_p.getComplexChildClasses().iterator();
        int i = 0;

        // iterate over the complex types
        while (it.hasNext())
        {
            OwFieldDefinition fielddef = (OwFieldDefinition) it.next();

            w_p.write("<div class=\"OwComplexPropertyControl\">");

            w_p.write("<div class=\"OwComplexPropertyControl_header\">");

            String complexId = strID_p + COMPLEX_ID_DELIMITER + String.valueOf(i);

            if (fielddef.isArray() || fielddef.isComplex())
            {
                w_p.write(fielddef.getDisplayName(getContext().getLocale()));
            }
            else
            {
                w_p.write("<label for=\"");
                w_p.write(complexId);
                w_p.write("\">");
                w_p.write(fielddef.getDisplayName(getContext().getLocale()));
                w_p.write("</label>");
            }

            w_p.write("</div>");

            w_p.write("<div class=\"OwComplexPropertyControl_value\">");
            // retrieve corresponding value
            insertEditFieldInternal(w_p, fielddef, new OwSingleAttributeComplexInternalField(complexvalues, i, fielddef), complexId);
            w_p.write("</div>");

            w_p.write("</div>");

            i++;
        }

        w_p.write("</fieldset>");
    }

    /** <p>Render a single value editing.
     *  Depending on the type of the property different edit controls are rendered.
     *  All controls have the default Workdesk CSS class for improving/customizing
     *  the rendering.</p>
     *
     *  <p>Also it is tried to format the <b>value_p</b> to a number and then
     *  the control is extended with an additional CSS class.
     *  </p>
     *  CSS class(es) preset for the control:
     *  <ul>
     *  <li><b>OwInputControl</b> default WeWebu CSS class</li>
     *  <li><b>OwInputControlXXXXX</b> type specific CSS class like OwInputControlNumber</li>
     *  <li><b>OwInputControl_&quot;PropertyName&quot;</b> property specific CSS class</li>
     *  </ul>
     *
     * @param w_p Writer used for writing the rendering
     * @param fieldDef_p OwFieldDefinition of property for which the control is rendered
     * @param value_p Object reference to the value of the property
     * @param strID_p String representing the HTML name/id attribute
     * @throws Exception
     */
    public void insertSingleEditFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Object value = value_p;
        if (m_restoreFromRequestStrategy)
        {
            if (m_FieldErrors != null)
            {
                String valueFromRequest = getContext().getHttpRequest().getParameter(strID_p);
                if (valueFromRequest != null)
                {
                    String fieldId = strID_p.indexOf(COMPLEX_ID_DELIMITER) != -1 ? strID_p.substring(0, strID_p.indexOf(COMPLEX_ID_DELIMITER)) : strID_p;
                    try
                    {
                        Integer fieldIdAsKey = Integer.valueOf(fieldId);
                        if (m_FieldErrors.containsKey(fieldIdAsKey))
                        {
                            value = valueFromRequest;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        LOG.debug("Cannot create the field ID from string: " + strID_p + ". The value submited by user is:  " + valueFromRequest);
                    }
                }
            }
        }
        if (fieldDef_p.isComplex())
        {
            // === render complex type
            insertSingleComplexEditFieldInternal(w_p, fieldDef_p, value, strID_p);
        }
        else if (fieldDef_p.getJavaClassName().equalsIgnoreCase("java.util.Date"))
        {
            // === Date type
            // delegate to date control, allow null value if search template or if value is not required
            boolean ignoreDate = false, ignoreTime = false;
            if (fieldDef_p.getFormat() != null)
            {
                ignoreDate = fieldDef_p.getFormat().ignoreDate();
                ignoreTime = fieldDef_p.getFormat().ignoreTime();
            }

            String sDateFormatString = getDateFormatString(fieldDef_p);

            java.util.Date value_p_Casted = parseDate(value, sDateFormatString);

            if (ignoreDate)
            {
                if (ignoreTime)
                {
                    LOG.warn("OwStandardFieldManager.insertSingleEditFieldInternal: Invalid definition is to ignore date and time part of Date, ignore time will not be handled. FieldDef =  " + fieldDef_p.getClassName());
                }
                String formatedTimeValue = "";
                if (value_p_Casted != null)
                {
                    SimpleDateFormat dateFormat = OwDateTimeUtil.createDateFromat(getContext().getLocale(), sDateFormatString);
                    // === string value
                    formatedTimeValue = dateFormat.format(value_p_Casted);
                }
                else if (value != null)
                {
                    formatedTimeValue = value.toString();
                }
                insertEditableString(w_p, fieldDef_p, formatedTimeValue, strID_p);
            }
            else
            {
                OwMainAppContext owMainAppContext = (OwMainAppContext) getContext();
                boolean useJS_DateControl = m_MainContext.useJS_DateControl();
                OwEditablePropertyDate.insertEditHTML(owMainAppContext, owMainAppContext.getLocale(), w_p, value_p_Casted, strID_p, useJS_DateControl, !fieldDef_p.isRequired(), sDateFormatString,
                        ignoreTime ? null : owMainAppContext.getTimeFormatString());
            }

        }
        else if (fieldDef_p.isEnum())
        {
            // === enum
            // allow empty field if fieldprovider is a searchtemplate, or value is not required
            insertChoiceListControl(w_p, fieldDef_p, value, strID_p, !fieldDef_p.isRequired());
        }
        else if (fieldDef_p.getJavaClassName().equalsIgnoreCase("java.lang.Boolean"))
        {
            // === bool value
            insertEditableBoolean(w_p, fieldDef_p, value, strID_p);
        }
        else if (fieldDef_p.getJavaClassName().equalsIgnoreCase("java.lang.String"))
        {
            // === string value
            insertEditableString(w_p, fieldDef_p, value, strID_p);
        }
        else
        {
            // === other value
            boolean isNumber = false;
            String strText = null;

            if (value != null)
            {
                strText = value.toString();
                // try to interpret as number
                try
                {
                    strText = getNumberFormater().format(value);
                    isNumber = true;
                }
                catch (Exception e1)
                {
                    // ignore, not a number
                    isNumber = false;
                }
            }

            w_p.write("<input id=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
            w_p.write("\" size=\"20\" title=\"");
            OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getDescription(getContext().getLocale()));
            //render default Workdesk CSS class
            w_p.write("\" class=\"OwInputControl");
            //if is a Number insert type specific CSS class
            if (isNumber)
            {
                w_p.write(" OwInputControlNumber");
            }
            //insert property specific CSS class
            w_p.write(" OwInputControl_");
            w_p.write(fieldDef_p.getClassName());
            w_p.write("\" name=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
            w_p.write("\" type=\"text\" value=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strText);
            w_p.write("\" onblur='onFieldManagerFieldExit(\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getClassName()));
            w_p.write("\",\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getJavaClassName()));
            w_p.write("\",\"");
            if (getFieldProvider() != null)
            {
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(getFieldProvider().getFieldProviderType())));
                w_p.write("\",\"");
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(getFieldProvider().getFieldProviderName()));
                w_p.write("\",\"");
            }
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(strID_p));
            w_p.write("\",this.value)'>");

        }
    }

    /**
     * Helper to parse an object to a java.util.Date,
     * which will simply cast the object or use it together with the provided
     * date format in a parse operation.
     * <p>This method will return null, if conversion into Date representation fails
     * or the provided value is null</p>
     * @param value_p Object which should be interpreted as Date value
     * @param strDateFormat_p String representing the format which should be used for parse operation
     * @return java.util.Date representation, or null if could transform object into a date value
     * @since 3.1.0.3
     */
    protected Date parseDate(Object value_p, String strDateFormat_p)
    {
        java.util.Date parsedValue = null;
        if (value_p != null)
        {
            if (value_p instanceof java.util.Date)
            {
                parsedValue = (java.util.Date) value_p;
            }
            else if (value_p instanceof String)
            {
                SimpleDateFormat dateFormat = OwDateTimeUtil.createDateFromat(getContext().getLocale(), strDateFormat_p);
                String stringValue = (String) value_p;
                try
                {
                    parsedValue = dateFormat.parse(stringValue);
                }
                catch (ParseException e)
                {
                    LOG.debug("OwStandardFieldManager.parseDate: Error converting to date=[" + value_p + "] to java.util.Date on...", e);
                    parsedValue = null;
                }
            }
            else
            {
                LOG.debug("OwStandardFieldManager.parseDate: Unsupported type for parsing value=[" + value_p + "] to java.util.Date!");
                parsedValue = null;
            }
        }
        return parsedValue;
    }

    /**
     *
     * @param strID_p
     * @return the value index based title information for array and complex values
     * @since 3.0.0.0
     */
    protected String getValueIndexTitle(String strID_p)
    {
        String[] idElements = strID_p.split("_");
        StringBuilder valueIndexInformation = new StringBuilder();
        if (idElements.length > 1)
        {
            for (int i = 1; i < idElements.length; i++)
            {
                int index = -1;
                try
                {
                    index = Integer.parseInt(idElements[i]);
                }
                catch (NumberFormatException e)
                {
                    LOG.error("OwStandardFieldManager.getValueIndexTitle(): Invalid indexed field ID component string : " + idElements[i]);
                }
                if (i == idElements.length - 1)
                {
                    valueIndexInformation.insert(0, getContext().localize1("app.OwStandardFieldManager.field.value.top.index", " - indexed value number %1", "" + (index + 1)));
                }
                else
                {
                    valueIndexInformation.insert(0, getContext().localize1("app.OwStandardFieldManager.field.value.sub.index", " of  sub-indexed value number %1", "" + (index + 1)));
                }
            }
        }
        return valueIndexInformation.toString();
    }

    /** Insert a HTML input text field, which depends on the max value size
     *  of the property renders a single line Textfield or a multi line Textarea.
     *
     *  <p>For better customization possibilities the rendered input fields have
     *  following three CSS (<b>C</b>ascading <b>S</b>tyle <b>S</b>heet) classes:
     *  <ul>
     *  <li><b>OwInputControl</b> default Workdesk CSS class</li>
     *  <li><b>OwInputControlString</b> or <b>OwInputControlMultiString</b> type specific CSS class</li>
     *  <li><b>OwInputControl_&quot;PropertyName&quot;</b> property specific CSS class</li>
     *  </ul>
     *  </p>
     * @param w_p Writer which is used to write out the rendering
     * @param fieldDef_p OwFieldDefinition of the Property to be rendered
     * @param value_p Object that should be a String which is the default value of the Textfield
     * @param strID_p String representing the HTML name/ID attribute
     * @throws Exception
     */
    protected void insertEditableString(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        String strText = "";
        if (value_p != null)
        {
            strText = value_p.toString();
        }

        // get the max string len from definition max value
        int iMaxSize = this.m_defMaxStringSize; // default
        try
        {
            iMaxSize = ((Integer) fieldDef_p.getMaxValue()).intValue();
        }
        catch (Exception e)
        { /* ignore */
        }

        if ((iMaxSize > m_textareaThreshold) && isFieldProviderType(OwFieldProvider.TYPE_META_OBJECT))
        {
            w_p.write("<textarea id=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
            w_p.write("\" title=\"");
            OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getDescription(getContext().getLocale()));
            OwHTMLHelper.writeSecureHTML(w_p, getValueIndexTitle(strID_p));
            /*write the specific CSS classes for this control*/
            w_p.write("\" cols=\"40\" rows=\"5\" class=\"OwInputControl OwInputControlMultiString OwInputControl_");
            w_p.write(fieldDef_p.getClassName());
            w_p.write("\" name=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
            w_p.write("\" onblur='onFieldManagerFieldExit(\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getClassName()));
            w_p.write("\",\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getJavaClassName()));
            w_p.write("\",\"");
            if (getFieldProvider() != null)
            {
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(getFieldProvider().getFieldProviderType())));
                w_p.write("\",\"");
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(getFieldProvider().getFieldProviderName()));
                w_p.write("\",\"");
            }
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(strID_p));
            w_p.write("\",this.value)' onkeydown=\"event.cancelBubble=true\">");
            OwHTMLHelper.writeSecureHTML(w_p, strText);
            w_p.write("</textarea>");
        }
        else
        {
            int iVisibleSize = (iMaxSize > 32) ? 32 : iMaxSize;

            w_p.write("<input id=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
            w_p.write("\" maxlength=\"");
            OwHTMLHelper.writeSecureHTML(w_p, String.valueOf(iMaxSize));
            w_p.write("\" size=\"");
            OwHTMLHelper.writeSecureHTML(w_p, String.valueOf(iVisibleSize));
            w_p.write("\" title=\"");
            OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getDescription(getContext().getLocale()));
            OwHTMLHelper.writeSecureHTML(w_p, getValueIndexTitle(strID_p));
            /*create the CSS classes for this input control*/
            w_p.write("\" class=\"OwInputControl OwInputControlString OwInputControl_");
            w_p.write(fieldDef_p.getClassName());
            w_p.write("\" name=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
            w_p.write("\" type=\"text\" value=\"");
            OwHTMLHelper.writeSecureHTML(w_p, strText);
            w_p.write("\" onblur='onFieldManagerFieldExit(\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getClassName()));
            w_p.write("\",\"");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getJavaClassName()));
            w_p.write("\",\"");
            if (getFieldProvider() != null)
            {
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(getFieldProvider().getFieldProviderType())));
                w_p.write("\",\"");
                OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(getFieldProvider().getFieldProviderName()));
                w_p.write("\",\"");
            }
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(strID_p));
            w_p.write("\",this.value)'>");
        }
    }

    /** Insert a field for editing boolean properties, the
     *  rendered input field is preset with three CSS classes for customization.
     *  <ul>
     *  <li><b>OwInputControl</b> default Workdesk CSS class</li>
     *  <li><b>OwInputControlBoolean</b> type specific CSS class</li>
     *  <li><b>OwInputControl_&quot;PropertyName&quot;</b> property specific CSS class</li>
     *  </ul>
     *
     * @param w_p Writer for writing out the rendering
     * @param fieldDef_p OwFieldDefinition of the property that should be rendered
     * @param value_p Object representing a Boolean or boolean value
     * @param strID_p String used for HTML name/ID of rendered input field
     * @throws Exception
     */
    protected void insertEditableBoolean(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Locale locale = getContext().getLocale();
        String[] displayNames = new String[] { getContext().localize("app.OwStandardFieldManager.yes", "Yes"), getContext().localize("app.OwStandardFieldManager.no", "No") };
        String[] values = new String[] { "" + true, "" + false };
        OwComboModel model = new OwFieldManagerComboModel(locale, !fieldDef_p.isRequired(), value_p == null ? null : value_p.toString(), displayNames, values);
        OwComboboxRenderer renderer = m_MainContext.createComboboxRenderer(model, strID_p, fieldDef_p, getFieldProvider(), null);
        renderer.renderCombo(w_p);
    }

    /** update the property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     * @param request_p  HttpServletRequest
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     */
    protected Object updateFieldInternal(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Object retObject = null;

        OwFieldManagerControl fieldcontrol;

        if (((fieldcontrol = m_FieldCtrlMap.get(fieldDef_p.getClassName())) != null) || ((fieldcontrol = m_FieldCtrlMap.get(fieldDef_p.getJavaClassName())) != null))
        {
            // === property is updated in field control
            try
            {
                retObject = fieldcontrol.updateField(request_p, fieldDef_p, value_p, strID_p);
            }
            catch (Exception e)
            {
                LOG.error("The fieldcontrol failed to execute updateField.", e);
                throw e;
            }
        }
        else
        {
            if (fieldDef_p.isArray())
            {
                // === array values
                Object[] values = (Object[]) value_p;
                if (values != null)
                {
                    for (int i = 0; i < values.length; i++)
                    {
                        values[i] = updateSingleFieldInternal(request_p, fieldDef_p, values[i], new StringBuilder(strID_p).append(COMPLEX_ID_DELIMITER).append(Integer.toString(i)).toString());
                    }
                }

                retObject = value_p;
            }
            else
            {
                // === single value
                retObject = updateSingleFieldInternal(request_p, fieldDef_p, value_p, strID_p);
            }
        }

        //NULL array field item values are not allowed (but for a single null value in search forms )- ticket 2420
        if (fieldDef_p.isArray())
        {
            Object[] values = (Object[]) retObject;
            if (values != null)
            {
                OwField theField = getField(strID_p);
                String error = getSafeFieldError(theField);
                //Don't override already set errors - update errors
                if (error == null || error.length() == 0)
                {
                    //allow one null value in search forms
                    if (values.length > 1 || !(theField instanceof com.wewebu.ow.server.field.OwSearchCriteria && values.length == 1))
                    {
                        for (int i = 0; i < values.length; i++)
                        {
                            if (values[i] == null || values[i].toString().length() == 0)
                            {
                                String errorMessage = getContext().localize("app.OwStandardFieldManager.emptyarrayfielditem", "The input fields can not be empty!");
                                setFieldError(theField, errorMessage);
                            }
                        }
                    }
                }
            }
        }

        // check for required value
        if (fieldDef_p.isRequired() && ((retObject == null) || (retObject.toString().length() == 0)))
        {
            throw new OwFieldManagerException(getContext().localize("app.OwStandardFieldManager.requiredfield", "This field is mandatory and must have a value."));
        }

        return retObject;
    }

    /** update a single complex field type
     *
     * @param request_p
     * @param fieldDef_p
     * @param value_p
     * @param strID_p
     * @return Object
     * @throws Exception
     */
    protected Object updateComplexSingleFieldInternal(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Iterator it = fieldDef_p.getComplexChildClasses().iterator();
        int i = 0;
        while (it.hasNext())
        {
            OwFieldDefinition fielddef = (OwFieldDefinition) it.next();

            ((Object[]) value_p)[i] = updateFieldInternal(request_p, fielddef, ((Object[]) value_p)[i], strID_p + COMPLEX_ID_DELIMITER + String.valueOf(i));

            i++;
        }

        return value_p;
    }

    /** update a single value of a property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     * @param request_p  HttpServletRequest
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     */
    public Object updateSingleFieldInternal(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        if (fieldDef_p.isComplex())
        {
            return updateComplexSingleFieldInternal(request_p, fieldDef_p, value_p, strID_p);
        }
        else
        {
            Object retObject = convertValue(request_p, fieldDef_p, value_p, strID_p);

            // validate
            validateValue(retObject, fieldDef_p);

            return retObject;
        }
    }

    /**
     * Get the date format string for the given field definition
     * @param fieldDef_p - the field definition
     * @return {@link java.lang.String} - the date format.
     * @since 3.0.0.0
     */
    private String getDateFormatString(OwFieldDefinition fieldDef_p)
    {
        String sDateFormatString = m_MainContext.getDateFormatString();
        if ((null != fieldDef_p.getFormat()))
        {
            if (fieldDef_p.getFormat().ignoreTime())
            {
                sDateFormatString = m_MainContext.getDateFormatStringWithoutTime();
            }
            else if (fieldDef_p.getFormat().ignoreDate())
            {
                sDateFormatString = m_MainContext.getDateFormatStringWithoutDate();
            }
        }
        return sDateFormatString;
    }

    /**
     *
     * @param value_p
     * @param fieldDef_p
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void validateValue(Object value_p, OwFieldDefinition fieldDef_p) throws Exception
    {
        if (null == value_p)
        {
            return;
        }

        // first validate against field definition ()
        OwFormat format = fieldDef_p.getFormat();
        if (null != format)
        {
            if (format.canValidate())
            {
                String message = format.validate(this.getFieldProvider().getFieldProviderType(), value_p, getContext().getLocale());
                if (null != message)
                {
                    throw new OwFieldManagerException(message);
                }
                // no internal validation required
                return;
            }
        }

        String strJavaClassName = fieldDef_p.getJavaClassName();
        Object max = fieldDef_p.getMaxValue();
        Object min = fieldDef_p.getMinValue();

        if (strJavaClassName.equalsIgnoreCase("java.lang.String"))
        {
            if (max != null && ((String) value_p).length() > ((Integer) max).intValue())
            {
                throw new OwFieldManagerException(getContext().localize1("app.OwStandardFieldManager.invalidmaxstringlen", "Value must not exceed %1 character(s).", ((Integer) max).toString()));
            }

            if (min != null && ((String) value_p).length() < ((Integer) min).intValue())
            {
                throw new OwFieldManagerException(getContext().localize1("app.OwStandardFieldManager.invalidminstringlen", "Value must contain at least %1 character(s).", ((Integer) min).toString()));
            }
        }
        else if (!strJavaClassName.equalsIgnoreCase("java.lang.Boolean"))
        {
            if (max != null && ((Comparable) max).compareTo(value_p) < 0)
            {
                if (value_p instanceof Date)
                {
                    String dateFormat = getDateFormatString(fieldDef_p);
                    SimpleDateFormat formatter = OwDateTimeUtil.createDateFromat(getContext().getLocale(), dateFormat);
                    throw new OwFieldManagerException(getContext().localize1("app.OwStandardFieldManager.invalidmaxdate", "Value must be before (%1).", formatter.format((Date) max)));
                }
                else
                {
                    throw new OwFieldManagerException(getContext().localize1("app.OwStandardFieldManager.invalidmax", "Value must be below (%1).", max.toString()));
                }
            }

            if (min != null && ((Comparable) min).compareTo(value_p) > 0)
            {
                if (value_p instanceof Date)
                {
                    String dateFormat = getDateFormatString(fieldDef_p);
                    SimpleDateFormat formatter = OwDateTimeUtil.createDateFromat(getContext().getLocale(), dateFormat);
                    throw new OwFieldManagerException(getContext().localize1("app.OwStandardFieldManager.invalidmindate", "Value must be later than (%1).", formatter.format((Date) min)));
                }
                else
                {
                    throw new OwFieldManagerException(getContext().localize1("app.OwStandardFieldManager.invalidmin", "Value must be greater than (%1).", min.toString()));
                }
            }
        }
    }

    /**
     * Renders a property control label on the given Writer.<br>
     *
     * value type are considered at rendering time.
     * @param w_p Writer
     * @param readOnlyView_p ReadOnly view
     * @param readOnly_p ReadOnly property
     * @param property_p Property
     * @param suffix_p  String
     * @param writeLabel_p write label
     * @throws Exception
     * @since 3.2.0.0
     */
    public void insertLabel(Writer w_p, boolean readOnlyView_p, boolean readOnly_p, OwField property_p, String suffix_p, boolean writeLabel_p) throws Exception
    {
        OwFieldDefinition fieldDefinition = property_p.getFieldDefinition();
        OwFieldManagerControl fieldcontrol;

        fieldcontrol = m_FieldCtrlMap.get(fieldDefinition.getClassName());
        if (fieldcontrol == null)
        {
            fieldcontrol = m_FieldCtrlMap.get(fieldDefinition.getJavaClassName());
        }
        if (fieldcontrol != null)
        {
            fieldcontrol.insertLabel(w_p, suffix_p, property_p, String.valueOf(property_p.hashCode()), writeLabel_p);
        }
        else
        {
            if (readOnlyView_p || readOnly_p || fieldDefinition.isArray() || fieldDefinition.isComplex())
            {
                w_p.write("<span>");
                w_p.write(fieldDefinition.getDisplayName(getContext().getLocale()));
                if (suffix_p != null)
                {
                    w_p.write(suffix_p);
                }
                w_p.write("</span>");
            }
            else
            {
                w_p.write("<label for='");
                String fieldId = String.valueOf(property_p.hashCode());
                w_p.write(fieldId);
                w_p.write("'>");
                w_p.write(fieldDefinition.getDisplayName(getContext().getLocale()));
                if (suffix_p != null)
                {
                    w_p.write(suffix_p);
                }
                w_p.write("</label>");
            }
        }
    }

    @Override
    public Object convertValue(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws OwException
    {
        String strJavaClassName = fieldDef_p.getJavaClassName();

        Object retObject = null;
        if (strJavaClassName.equalsIgnoreCase("java.util.Date"))
        {
            // === Date type
            // delegate to date control, allow null value if search template or if value is not required
            String sDateFormatString = getDateFormatString(fieldDef_p);

            java.util.Date value_p_Casted = null;
            if (value_p != null)
            {
                try
                {
                    value_p_Casted = (java.util.Date) value_p;
                }
                catch (ClassCastException e)
                {
                    LOG.debug("OwStandardFieldManager.updateSingleFieldInternal: Error casting date=[" + value_p + "] to java.util.Date on...", e);
                    value_p_Casted = null;
                }
            }
            OwMainAppContext owMainAppContext = (OwMainAppContext) getContext();
            try
            {
                retObject = OwEditablePropertyDate.updateField(getContext().getLocale(), request_p, strID_p, m_MainContext.useJS_DateControl(), value_p_Casted, !fieldDef_p.isRequired(), sDateFormatString, owMainAppContext.getTimeFormatString());
            }
            catch (OwFieldManagerException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("Cannot access definition of field for required check", e);
                throw new OwInaccessibleException(getContext().localize("app.OwStandardFieldManager.convertValue.inAccessibleRequiredField", "Could not read FieldDefintion (isRequire) information"), e, "FieldManager");
            }
        }
        else
        {
            String strRequestString = request_p.getParameter(strID_p);

            if ((strRequestString == null) || (strRequestString.length() == 0))
            {
                // === user left field blank
                retObject = null;
            }
            else if (strJavaClassName.equalsIgnoreCase("java.lang.Boolean"))
            {
                // === Boolean type
                if (strRequestString.length() == 0)
                {
                    retObject = null;
                }
                else
                {
                    retObject = Boolean.valueOf(strRequestString);
                }
            }
            else
            {
                Class<?> newClass = null;
                try
                {
                    newClass = Class.forName(strJavaClassName);
                }
                catch (ClassNotFoundException e)
                {
                    LOG.error("Cannot instantiate class: " + strJavaClassName, e);
                    throw new OwConfigurationException(getContext().localize1("app.OwStandardFieldManager.invalidType", "Invalid type %1", strJavaClassName), e);
                }
                // try to treat as number and convert correctly
                if (Number.class.isAssignableFrom(newClass))
                {
                    ParsePosition pp = new ParsePosition(0);
                    Number n = null;
                    try
                    {
                        n = getNumberFormater().parse(strRequestString, pp);
                    }
                    catch (Exception e)
                    {
                        LOG.debug("Invalid input=" + strRequestString + " / Please insert a number.", e);
                        throw new OwFieldManagerException(getContext().localize("app.OwStandardFieldManager.invalidnumbervalue", "Please insert a number."));
                    }
                    // check if all inserted string are parsed, or earlier stop because illegal character (like "." for DE Locale, or "," for EN Locale)
                    if (pp.getIndex() == strRequestString.length())
                    {
                        strRequestString = n.toString();
                    }
                    else
                    {
                        String character = strRequestString.substring(pp.getIndex(), pp.getIndex() + 1);
                        throw new OwFieldManagerException(getContext().localize1("app.OwStandardFieldManager.invalidnumbervalue.withPosition", "Please insert a number. Char '%1' is wrong.", character));
                    }
                }

                java.lang.reflect.Constructor<?> constr;
                try
                {
                    constr = newClass.getConstructor(new Class[] { java.lang.String.class });
                }
                catch (SecurityException e)
                {
                    throw new OwConfigurationException(getContext().localize1("app.OwStandardFieldManager.type", "Cannot access Constructor of type %1", strJavaClassName), e);
                }
                catch (NoSuchMethodException e)
                {
                    throw new OwServerException(getContext().localize1("app.OwStandardFieldManager.type.method", "No simple Constructor(String) available for type %1", strJavaClassName), e);
                }

                // remove trailing and leading space character
                strRequestString = strRequestString.trim();

                try
                {
                    retObject = constr.newInstance(new Object[] { strRequestString });
                }
                catch (IllegalArgumentException e)
                {
                    throw new OwServerException(getContext().localize("app.OwStandardFieldManager.convert.param", "Invalid parameter for type creation"), e);
                }
                catch (InstantiationException e)
                {
                    throw new OwServerException(getContext().localize1("app.OwStandardFieldManager.convert.nonInstance", "Conversion failed: Instances of type %1 cannot be created.", strJavaClassName), e);
                }
                catch (IllegalAccessException e)
                {
                    throw new OwServerException(getContext().localize("app.OwStandardFieldManager.convert.illegalAccess", "Conversion failed: Access to Constructor is not allowed"), e);
                }
                catch (InvocationTargetException e)
                {
                    throw new OwServerException(getContext().localize("app.OwStandardFieldManager.convert.target", "Conversion failed: See log for details."), e);
                }
            }
        }

        return retObject;
    }
}