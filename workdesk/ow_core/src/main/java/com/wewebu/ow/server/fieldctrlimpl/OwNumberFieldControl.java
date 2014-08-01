package com.wewebu.ow.server.fieldctrlimpl;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwFieldManagerException;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Field control implementation for {@link Number} based fields.<br>
 * The field rendering and updating relies on Java's {@link NumberFormat}.
 * Additional configuration can be specified through a <code>NumberFormat</code>
 * XML configuration element to designate a {@link DecimalFormat} format pattern string.<br/>
 * The field control tries to enforce a parsed {@link Number} concrete type for the following 
 * Java based types : {@link java.lang.Integer}, {@link java.lang.Long}, {@link java.lang.Double} , 
 * {@link java.lang.Float} , {@link java.math.BigInteger} and {@link java.math.BigDecimal}.
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
 *@since 2.5.2.0
 */
public class OwNumberFieldControl extends OwFieldManagerControl
{

    /**field ID separator*/
    private static final String ARRAY_FIELD_ID_SEPARATOR = "_";

    /**default assumed complex field ID separator*/
    private static final String DEFAULT_EXTERNAL_COMPLEX_ID_DELIMITER = "_";

    /** package logger or class logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwNumberFieldControl.class);

    /** the number format pattern configuration element name*/
    private static final String NUMBER_FORMAT_ELEMENT = "NumberFormat";

    /** query string for the selected array item */
    public static final String ARRAY_ITEM_INDEX_KEY = "aitem";

    /** query string for the selected field */
    public static final String FIELD_ID_KEY = "fieldid";

    /** default field size*/
    public static final int DEFAULT_SIZE = 32;

    /**current number format*/
    private NumberFormat m_numberFormat = NumberFormat.getInstance();

    /**field errors mapped by field id*/
    private Map m_updateErrorFields = new HashMap();

    /**ids of controls marked for delete*/
    private Map m_clearMarkedArraySizes = new HashMap();

    /**container (array-controls) error counts*/
    private Map m_containerFieldErrorCounts = new HashMap();

    /**partial parse flag. If <code>true</code> partially correct number strings will be accepted (egg. "2.3324sss") */
    private boolean m_acceptPartialParse = false;

    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        Object value_p = field_p.getValue();
        if (fieldDef_p.isArray())
        {
            w_p.write("<fieldset id='");
            String fieldId = String.valueOf(field_p.hashCode());
            w_p.write(fieldId);
            w_p.write("' class='accessibility'>");
            w_p.write("<legend class='accessibility'>");
            w_p.write(fieldDef_p.getDisplayName(getContext().getLocale()));
            w_p.write("</legend>");

            if (value_p == null)
            {
                value_p = new Object[0];
            }

            if (value_p.getClass().isArray())
            {
                Object[] values = (Object[]) value_p;

                w_p.write("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                if (values.length > 0)
                {
                    int deleteErrorSkips = 0;

                    for (int i = 0; i < values.length; i++)
                    {
                        w_p.write("<tr><td valign=\"bottom\">");
                        String arrayItemId = strID_p + ARRAY_FIELD_ID_SEPARATOR + String.valueOf(i);
                        String errorFieldId = strID_p + ARRAY_FIELD_ID_SEPARATOR + String.valueOf(i + deleteErrorSkips);
                        if (isMarkedForClear(errorFieldId))
                        {
                            deleteErrorSkips++;
                        }

                        errorFieldId = strID_p + ARRAY_FIELD_ID_SEPARATOR + String.valueOf(i + deleteErrorSkips);

                        insertSingleEditField(w_p, fieldDef_p, values[i], arrayItemId, errorFieldId);
                        w_p.write("</td><td>");
                        if (values.length > 1 || i != 0 || !(field_p instanceof com.wewebu.ow.server.field.OwSearchCriteria))
                        {
                            insertRemoveArrayItemLink(w_p, fieldDef_p, i, strID_p);
                        }
                        else
                        {
                            w_p.write("</td><td style='width:17px;'>");
                        }
                        w_p.write("</td></tr>");
                    }
                    w_p.write("<tr>");
                    w_p.write("<td>");
                    insertAddArrayItemLink(w_p, fieldDef_p, strID_p);
                    w_p.write("</td>");
                    w_p.write("</tr>");
                }
                else
                {
                    w_p.write("<tr><td valign=\"bottom\">");
                    w_p.write("</td><td>");
                    insertAddArrayItemLink(w_p, fieldDef_p, strID_p);
                    w_p.write("</td></tr>");
                }
                w_p.write("</table>");
            }
            else
            {
                //non array value in an array field !!

                String errMessage = getContext().localize("app.OwNumberFieldControl.invalidfield", "Invalid number field :") + fieldDef_p.getClass();
                LOG.error("OwNumberFieldControl.insertEditField : non array value found for an array based field - " + fieldDef_p.getClassName());
                throw new OwFieldManagerException(errMessage);
            }
            w_p.write("</fieldset>");
        }
        else
        {
            insertSingleEditField(w_p, fieldDef_p, value_p, strID_p, strID_p);
        }
        clearMarkedArrayItemErrors();
    }

    /**
     * Adds element addition link for array fields.
     * The field ID is transmitted through a {@link #FIELD_ID_KEY} specified parameter.    
     * @param w_p the writer object
     * @param fieldDef_p - the field definition object
     * @param strID_p ID of the array field
     * @throws IOException
     * @throws Exception
     */
    protected void insertAddArrayItemLink(Writer w_p, OwFieldDefinition fieldDef_p, String strID_p) throws Exception
    {
        String addButtonTooltip = getContext().localize("app.OwStandardFieldManager.addarrayitemtooltip", "Add Element");

        String strAddEventURL = "javascript:document." + getFieldManager().getFormName() + ".action = '" + getEventURL("AddArrayItem", FIELD_ID_KEY + "=" + strID_p) + "';document." + getFieldManager().getFormName() + ".submit()";
        w_p.write("<a title=\"");
        w_p.write(addButtonTooltip);
        w_p.write("\" href=\"");
        w_p.write(strAddEventURL);
        w_p.write("\">");
        if (fieldDef_p != null)
        {
            addButtonTooltip = getContext().localize1("app.OwStandardFieldManager.addarrayitemtooltipimg", "Add new item for %1", fieldDef_p.getDisplayName(getContext().getLocale()));
        }
        w_p.write("<img style=\"vertical-align:middle;border:0px none;margin:5px 0px;\" src=\"");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/addbtn.png\"");
        w_p.write(" alt=\"");
        w_p.write(addButtonTooltip);
        w_p.write("\" title=\"");
        w_p.write(addButtonTooltip);
        w_p.write("\"/></a>");
    }

    /**
     * Adds element removal link for array fields.
     * The field ID is transmitted through a {@link #FIELD_ID_KEY} specified parameter.    
     * @param w_p - the {@link Writer} object.
     * @param fieldDef_p - the {@link OwFieldDefinition} object.
     * @param strID_p ID of the array field
     * @throws IOException
     * @throws Exception
     */
    protected void insertRemoveArrayItemLink(Writer w_p, OwFieldDefinition fieldDef_p, int index_p, String strID_p) throws IOException, Exception
    {

        String deleteButtonTooltip = getContext().localize("app.OwStandardFieldManager.delarrayitem", "Delete Element");
        w_p.write("<a title=\"");
        w_p.write(deleteButtonTooltip);
        w_p.write("\" href=\"");
        w_p.write(getFormEventURL("DeleteArrayItem", ARRAY_ITEM_INDEX_KEY + "=" + String.valueOf(index_p) + "&" + FIELD_ID_KEY + "=" + strID_p));
        w_p.write("\">");
        w_p.write("<img style=\"vertical-align:middle;border:0px none;margin:0px 5px;\" src=\"");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/deletebtn.png\"");
        if (fieldDef_p != null)
        {
            String fieldDisplayName = fieldDef_p.getDisplayName(getContext().getLocale());
            deleteButtonTooltip = getContext().localize2("app.OwStandardFieldManager.delarrayitemindexed", "Delete element at position %1 from %2", "" + (index_p + 1), fieldDisplayName);
        }
        w_p.write(" alt=\"");
        w_p.write(deleteButtonTooltip);
        w_p.write("\" title=\"");
        w_p.write(deleteButtonTooltip);
        w_p.write("\"/></a>");
    }

    /** called when user clicks to delete an array item */
    public void onDeleteArrayItem(HttpServletRequest request_p) throws Exception
    {
        // === copy the values into a new array without the selected item
        String arrayItemIdex = request_p.getParameter(ARRAY_ITEM_INDEX_KEY);
        int iIndex = Integer.parseInt(arrayItemIdex);

        String fieldId = request_p.getParameter(FIELD_ID_KEY);
        OwFieldManager fieldManager = getFieldManager();
        OwField field = fieldManager.getField(fieldId);
        Object[] values = (Object[]) field.getValue();

        for (int i = 0; i < values.length; i++)
        {
            values[i] = updateSingleField(request_p, field.getFieldDefinition(), values[i], fieldId + ARRAY_FIELD_ID_SEPARATOR + i, false);
        }

        markArrayItemErrorForClear(fieldId + ARRAY_FIELD_ID_SEPARATOR + iIndex, fieldId, iIndex, values.length);

        //The following error display code does not work properly because of the update-event call sequence  
        //        if (fieldContainmentCount(fieldId) <= 0)
        //        {
        //            fieldManager.clearFieldError(field);
        //        }

        //due to update-event call sequence we can't display errors on the delete event request 
        fieldManager.clearFieldError(field);

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
        OwField field = getFieldManager().getField(key);

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
    }

    /**(Overridable)
     * Returns the calculated field size (maximum amount of characters
     * entering into field) for the given field definition.
     * <p>By default this implementation returns {@link #DEFAULT_SIZE}
     * constant</p> 
     * @param fieldDef_p OwFieldDefinition of the field
     * @return the size to be defined in rendered UI
     * @throws Exception
     */
    protected int calculateFieldSize(OwFieldDefinition fieldDef_p) throws Exception
    {
        return DEFAULT_SIZE;
    }

    private String createDisplayValue(String id_p, String errorFieldId_p, Object value_p, OwFieldDefinition fieldDef_p) throws OwFieldManagerException
    {
        String displayedValue = null;
        if (id_p == null || !(isErrorField(errorFieldId_p) || isErrorContainment(errorFieldId_p)))
        {
            displayedValue = "";
            if (value_p != null)
            {
                if (value_p instanceof Number)
                {
                    try
                    {
                        displayedValue = m_numberFormat.format(value_p);

                    }
                    catch (IllegalArgumentException e)
                    {
                        String errMessage = getContext().localize("app.OwNumberFieldControl.invalidfield", "Invalid number field : ") + fieldDef_p.getClass();
                        LOG.error("OwNumberFieldControl.createDisplayValue : invalid field value ( it cloud not be formated, not a number maybe? ) - " + fieldDef_p.getClassName(), e);
                        throw new OwFieldManagerException(errMessage, e);
                    }
                }
                else
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.error("OwNumberFieldControl.createDisplayValue : forced to display an invalid field value of class " + value_p.getClass() + " when a java.lang.Number was expected for filed " + fieldDef_p.getClassName() + " !");
                    }
                    displayedValue = value_p.toString();
                }
            }
        }
        else
        {
            if (!isComplexChildId(id_p))
            {
                OwAppContext context = getContext();
                HttpServletRequest request = context.getHttpRequest();
                displayedValue = request.getParameter(errorFieldId_p);
            }

            if (displayedValue == null)
            {
                displayedValue = "";
            }
        }

        return displayedValue;
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
        StringBuffer valueIndexInformation = new StringBuffer();
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
                    valueIndexInformation.insert(0, getContext().localize1("app.OwStandardFieldManager.field.value.top.index", " - indexed value number %1 ", "" + (index + 1)));
                }
                else
                {
                    valueIndexInformation.insert(0, getContext().localize1("app.OwStandardFieldManager.field.value.sub.index", " of  sub-indexed value number %1 ", "" + (index + 1)));
                }
            }
        }
        return valueIndexInformation.toString();
    }

    /**
     * Inserts a single value, editable HTML control. 
     * @param w_p
     * @param fieldDef_p
     * @param value_p
     * @param id_p
     * @throws Exception
     */
    protected void insertSingleEditField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p, String id_p, String errorFieldId_p) throws Exception
    {
        w_p.write("<input name=\"");
        w_p.write(id_p);
        w_p.write("\" id=\"");
        w_p.write(id_p);
        w_p.write("\" title=\"");
        OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getDescription(getContext().getLocale()));
        OwHTMLHelper.writeSecureHTML(w_p, getValueIndexTitle(id_p));
        w_p.write("\" type=\"text\" value=\"");
        w_p.write(createDisplayValue(id_p, errorFieldId_p, value_p, fieldDef_p));
        w_p.write("\" maxlength=\"");
        w_p.write(Integer.toString(calculateFieldSize(fieldDef_p)));
        w_p.write("\" class=\"OwInputControl OwInputControlNumber OwInputControlNumber_");
        w_p.write(fieldDef_p.getClassName());
        w_p.write("\" onblur=\"onFieldManagerFieldExit('");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getClassName()));
        w_p.write("', '");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getJavaClassName()));
        w_p.write("', '");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(getFieldManager().getFieldProvider().getFieldProviderType())));
        w_p.write("', '");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(getFieldManager().getFieldProvider().getFieldProviderName()));
        w_p.write("', '");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(id_p));
        w_p.write("', this.value)\" />");
    }

    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (fieldDef_p.isArray())
        {
            if (value_p == null)
            {
                value_p = new Object[0];
            }
            if (value_p.getClass().isArray())
            {
                // === array values
                Object[] values = (Object[]) value_p;

                boolean fDelimiter = false;
                for (int i = 0; i < values.length; i++)
                {
                    // delimiter
                    if (fDelimiter)
                    {
                        w_p.write("<div style=\"clear:left;padding-top:2px\">");
                        w_p.write("<hr class=\"OwStandardFieldManager_Array\">");
                        w_p.write("</div>");

                    }

                    insertSingleReadOnlyField(w_p, fieldDef_p, null, values[i]);

                    fDelimiter = true;
                }
            }
            else
            {
                //non array value in an array field !!

                String errMessage = getContext().localize("app.OwNumberFieldControl.invalidfield", "Invalid number field : ") + fieldDef_p.getClass();
                LOG.error("OwNumberFieldControl.insertEditField : non array value found for an array based field - " + fieldDef_p.getClassName());
                throw new OwFieldManagerException(errMessage);
            }
        }
        else
        {
            // === single value
            insertSingleReadOnlyField(w_p, fieldDef_p, null, value_p);
        }

    }

    /**
     * Inserts a single-value non-editable HTML control.
     * @param w_p
     * @param fieldDef_p
     * @param id_p
     * @param value_p
     * @throws Exception
     */
    public void insertSingleReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, String id_p, Object value_p) throws Exception
    {
        String displayValue = createDisplayValue(id_p, id_p, value_p, fieldDef_p);
        OwHTMLHelper.writeSecureHTML(w_p, displayValue, true);
    }

    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        if (fieldDef_p.isArray())
        {

            if (value_p == null)
            {
                return null;
            }

            Object[] values = (Object[]) value_p;

            Object result[] = new Object[values.length];

            for (int i = 0; i < values.length; i++)
            {
                result[i] = updateSingleField(request_p, fieldDef_p, values[i], strID_p + ARRAY_FIELD_ID_SEPARATOR + String.valueOf(i), true);
            }
            for (int i = 0; i < values.length; i++)
            {
                String fieldID = strID_p + ARRAY_FIELD_ID_SEPARATOR + i;
                if (isErrorField(fieldID))
                {
                    setFieldManagerError(strID_p, getFieldError(fieldID));
                    break;
                }

            }
            return result;
        }
        else
        {
            Object updatedValue = updateSingleField(request_p, fieldDef_p, value_p, strID_p, true);
            if (isErrorField(strID_p))
            {
                setFieldManagerError(strID_p, getFieldError(strID_p));
            }
            return updatedValue;
        }
    }

    /**
     * 
     * @param strID_p
     * @return <code>true</code> if the given ID is an externally generated complex child field ID 
     *         <code>false</code> otherwise
     * @see #getExternalComplexFieldIdDelimiter()
     * @since 2.5.3.0
     */
    protected boolean isComplexChildId(String strID_p)
    {
        String externalDelimiter = getExternalComplexFieldIdDelimiter();
        int index = strID_p.indexOf(externalDelimiter);
        if (index < 0)
        {
            return false;
        }
        else
        {
            index = strID_p.indexOf(externalDelimiter, index + externalDelimiter.length());
            return index > -1;
        }
    }

    /**
     * Sets the given error in the current field manager.<br>
     * This method handles the complex child fields by setting the error for the 
     * parent field.
     * @see #isComplexChildId(String)  
     * @param strID_p the ID of the control to set the error for 
     * @param error_p the error text to set for the given control
     * @since 2.5.3.0
     */
    protected void setFieldManagerError(String strID_p, String error_p)
    {
        String fieldId = strID_p;
        if (isComplexChildId(strID_p))
        {
            int firstDelimiterIndex = strID_p.indexOf(getExternalComplexFieldIdDelimiter());
            fieldId = strID_p.substring(0, firstDelimiterIndex);

        }

        OwField field = getFieldManager().getField(fieldId);

        getFieldManager().setFieldError(field, error_p);

    }

    /**
     * The string ID delimiter used to separate the ID of an externally generated 
     * ID of a complex field.<br>
     * The children of complex fields must be properly detected handled by this 
     * field control. The detection is performed by ID using {@link #isComplexChildId(String)} 
     * which checks for the occurrence of the an external ID delimiter specified by this method.  
     * 
     * @return the external field complex child field ID delimiter
     * @since 2.5.3.0
     */
    protected String getExternalComplexFieldIdDelimiter()
    {
        return DEFAULT_EXTERNAL_COMPLEX_ID_DELIMITER;
    }

    /**
     * 
     * @param numberString_p an erroneous number String
     * @param index_p the error index
     * @return an parse error text with error information (first wrong character)
     */
    protected String createParseIndexedError(String numberString_p, int index_p)
    {
        if (numberString_p != null && numberString_p.length() > index_p)
        {
            String errorCharString = "" + numberString_p.charAt(index_p);
            String error = getContext().localize1("app.OwStandardFieldManager.invalidnumbervalue.withPosition", "Please insert a number. Char '%1' is wrong.", errorCharString);

            return error;
        }
        else
        {
            return getContext().localize("app.OwStandardFieldManager.invalidnumbervalue", "Please insert a number.");
        }
    }

    /**
     * Validates a {@link Number} value against a given {@link OwFieldDefinition}.
     * If the value is not valid an error field will be recorded for the given ID. 
     * @see #setInternalFieldError(String, String)
     * @param id_p
     * @param value_p
     * @param fieldDef_p
     * @throws Exception if an error occurs during validation
     */
    protected Number validateNumber(String id_p, Number value_p, OwFieldDefinition fieldDef_p, boolean updateErrors_p) throws Exception
    {
        if (value_p instanceof Comparable)
        {
            Comparable comparableValue = (Comparable) value_p;
            Object maxValue = fieldDef_p.getMaxValue();
            if (maxValue != null && maxValue instanceof Number)
            {
                if (comparableValue.compareTo(maxValue) > 0)
                {
                    if (updateErrors_p)
                    {
                        setInternalFieldError(id_p, getContext().localize1("app.OwStandardFieldManager.invalidmax", "Value must be below (%1).", maxValue.toString()));
                    }
                    return null;
                }
            }

            Object minValue = fieldDef_p.getMinValue();
            if (minValue != null && minValue instanceof Number)
            {
                if (comparableValue.compareTo(minValue) < 0)
                {
                    if (updateErrors_p)
                    {
                        setInternalFieldError(id_p, getContext().localize1("app.OwStandardFieldManager.invalidmin", "Value must be greater than (%1).", minValue.toString()));
                    }
                    return null;
                }
            }
        }

        OwFormat format = fieldDef_p.getFormat();
        if (format != null && format.canValidate())
        {
            OwFieldManager fManager = getFieldManager();
            OwFieldProvider fProvider = fManager.getFieldProvider();

            String error = format.validate(fProvider.getFieldProviderType(), value_p, getContext().getLocale());
            if (error != null)
            {
                if (updateErrors_p)
                {
                    setInternalFieldError(id_p, error);
                }
                return null;
            }
        }
        return value_p;
    }

    /**
     * Register an error for a given field ID.
     * @param id_p String field ID 
     * @param error_p String error message
     */
    protected final void setInternalFieldError(String id_p, String error_p)
    {
        if (m_updateErrorFields.get(id_p) == null)
        {
            addContainmentError(id_p, 1);
        }
        m_updateErrorFields.put(id_p, error_p);

    }

    protected final void addContainmentError(String id_p, int errCount_p)
    {
        int count = fieldContainmentCount(id_p);
        m_containerFieldErrorCounts.put(fieldContainmentId(id_p), Integer.valueOf(count + errCount_p));
    }

    /**
     * Clears the errors associated with the given field ID.
     * @param id_p
     * @throws Exception 
     */
    protected final void clearFieldError(String id_p) throws Exception
    {
        if (id_p != null)
        {
            if (m_updateErrorFields.containsKey(id_p) && m_updateErrorFields.get(id_p) != null)
            {
                addContainmentError(id_p, -1);
            }
            m_updateErrorFields.remove(id_p);

        }
    }

    /**
     * Marks the array item with the give ID for deletion. 
     * @param id_p
     * @param fieldId_p
     * @param index_p
     * @param arraySize_p
     * @throws Exception
     */
    protected final void markArrayItemErrorForClear(String id_p, String fieldId_p, int index_p, int arraySize_p) throws Exception
    {
        if (id_p != null)
        {
            if (m_updateErrorFields.containsKey(id_p) && m_updateErrorFields.get(id_p) != null)
            {
                addContainmentError(id_p, -1);
            }
            m_updateErrorFields.put(id_p, null);
            m_clearMarkedArraySizes.put(id_p, new Object[] { fieldId_p, Integer.valueOf(index_p), Integer.valueOf(arraySize_p) });
        }
    }

    /**
     * Removes errors that were marked for deletion and adjusts the already existing indexes. 
     * @throws Exception
     */
    protected final void clearMarkedArrayItemErrors() throws Exception
    {
        Set entries = m_clearMarkedArraySizes.entrySet();
        for (Iterator i = entries.iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry) i.next();
            String id = (String) entry.getKey();
            m_updateErrorFields.remove(id);
            Object[] markData = (Object[]) entry.getValue();
            String fieldId = (String) markData[0];
            Integer index = (Integer) markData[1];
            Integer arraySize = (Integer) markData[2];
            int intIndex = index.intValue();
            int intSize = arraySize.intValue();

            for (int j = intIndex + 1; j < intSize; j++)
            {
                String itemId = fieldId + ARRAY_FIELD_ID_SEPARATOR + j;
                if (m_updateErrorFields.containsKey(itemId))
                {
                    Object error = m_updateErrorFields.get(itemId);
                    String prevItemId = fieldId + ARRAY_FIELD_ID_SEPARATOR + j;
                    m_updateErrorFields.put(prevItemId, error);
                    m_updateErrorFields.remove(itemId);
                }
            }
        }
        m_clearMarkedArraySizes.clear();
    }

    /**
     * 
     * @param id_p
     * @return <code>true</code> if there are registered errors for the given field id<br />
     *         <code>false</code> otherwise 
     */
    protected final boolean isErrorField(String id_p)
    {
        return m_updateErrorFields.containsKey(id_p) && m_updateErrorFields.get(id_p) != null;
    }

    protected final String fieldContainmentId(String id_p)
    {
        String containingField = id_p;
        if (containingField.indexOf(ARRAY_FIELD_ID_SEPARATOR) > -1 && !isComplexChildId(id_p))
        {
            containingField = containingField.substring(0, containingField.indexOf(ARRAY_FIELD_ID_SEPARATOR));
        }
        return containingField;
    }

    protected final int fieldContainmentCount(String id_p)
    {
        String containingField = fieldContainmentId(id_p);
        if (containingField.indexOf(ARRAY_FIELD_ID_SEPARATOR) > -1)
        {
            containingField = containingField.substring(0, containingField.indexOf(ARRAY_FIELD_ID_SEPARATOR));
        }
        Integer count = (Integer) m_containerFieldErrorCounts.get(containingField);
        if (count != null)
        {
            return count.intValue();
        }
        else
        {
            return 0;
        }

    }

    protected final boolean isErrorContainment(String id_p)
    {
        return fieldContainmentCount(id_p) > 0;

    }

    protected final boolean isMarkedForClear(String id_p)
    {
        return m_updateErrorFields.containsKey(id_p) && m_updateErrorFields.get(id_p) == null;
    }

    /**
     * 
     * @param id_p
     * @return the error String associated with the given field id<br>
     *         If no error is associated with the given field ID (ie. {@link #isErrorField(String)} returns false) 
     *         the hard-coded "NO ERROR" String is returned. 
     */
    protected final String getFieldError(String id_p)
    {
        return isErrorField(id_p) ? (String) m_updateErrorFields.get(id_p) : "NO ERROR";
    }

    /**
     * Updates a single value field.
     * @param request_p
     * @param fieldDef_p
     * @param value_p
     * @param strID_p
     * @return the update value as found on the given request and parsed using the current formatter ({@link #m_numberFormat}) 
     * @throws Exception
     */
    public Object updateSingleField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p, boolean updateErrors_p) throws Exception
    {
        String numberString = request_p.getParameter(strID_p);
        if (numberString == null)
        {
            return value_p;
        }
        else if (numberString.length() > 0)
        {
            String trimmedNumberString = numberString.trim();
            ParsePosition position = new ParsePosition(0);
            Number numberValue = m_numberFormat.parse(trimmedNumberString, position);
            if (position.getErrorIndex() > -1)
            {
                if (updateErrors_p)
                {
                    setInternalFieldError(strID_p, createParseIndexedError(numberString, position.getErrorIndex()));
                }
                return value_p;
            }
            else
            {
                if (m_acceptPartialParse || (!m_acceptPartialParse && position.getIndex() == trimmedNumberString.length()))
                {
                    if (updateErrors_p)
                    {
                        clearFieldError(strID_p);
                    }
                    Number typeEnforcedNumber = tryToEnforceNumberType(numberValue, fieldDef_p);
                    Number validValue = validateNumber(strID_p, typeEnforcedNumber, fieldDef_p, updateErrors_p);
                    if (validValue != null)
                    {
                        return validValue;
                    }
                    else
                    {
                        return value_p;
                    }
                }
                else
                {
                    if (updateErrors_p)
                    {
                        setInternalFieldError(strID_p, createParseIndexedError(numberString, position.getIndex()));
                    }
                    return value_p;
                }
            }
        }
        else
        {
            if (updateErrors_p)
            {
                clearFieldError(strID_p);
            }
            return null;
        }

    }

    /**
     * Attempts to convert the given {@link Number} value to the java type expected by the given field definition.<br>
     * In order to succeed the field definition musr have one of the following Java types :
     * {@link java.lang.Integer}, {@link java.lang.Long}, {@link java.lang.Double} or {@link java.lang.Float}.
     * Also the given {@link Number} value should be convertible to the give field Java type (egg. Double types can't be converted to Integer s). 
     * @param numberValue_p
     * @param fieldDefinition_p
     * @return if possible a {@link Number} with the type expected by the given field definition.<br>
     *         if the conversion is not possible the numberValue_p is returned.    
     */
    private Number tryToEnforceNumberType(Number numberValue_p, OwFieldDefinition fieldDefinition_p)
    {
        String javaClassName = fieldDefinition_p.getJavaClassName();
        try
        {
            Class expectedNumberClass = Class.forName(javaClassName);
            if (Number.class.isAssignableFrom(expectedNumberClass))
            {
                Class valueClass = numberValue_p.getClass();
                if (expectedNumberClass.isAssignableFrom(valueClass))
                {
                    return numberValue_p;
                }
                else
                {
                    if (expectedNumberClass.equals(Integer.class))
                    {
                        return tryToEnforceInteger(numberValue_p);
                    }
                    else if (expectedNumberClass.equals(Long.class))
                    {
                        return tryToEnforceLong(numberValue_p);
                    }
                    else if (expectedNumberClass.equals(Double.class))
                    {
                        return tryToEnforceDouble(numberValue_p);
                    }
                    else if (expectedNumberClass.equals(Float.class))
                    {
                        return tryToEnforceFloat(numberValue_p);
                    }
                    else if (expectedNumberClass.equals(BigInteger.class))
                    {
                        return tryToEnforceBigInteger(numberValue_p);
                    }
                    else if (expectedNumberClass.equals(BigDecimal.class))
                    {
                        return tryToEnforceBigDecimal(numberValue_p);
                    }
                    else
                    {
                        return numberValue_p;
                    }
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            LOG.warn("OwNumberFieldControl.tryToEnforceNumberType: ClassNotFoundException", e);
        }
        return null;
    }

    /**
     * 
     * @param aClass_p
     * @return <code>true</code> if the given class is one of  {@link java.lang.Integer}, {@link java.lang.Long} or {@link java.lang.Byte}<br>
     *         <code>false</code> otherwise 
     */
    private boolean isNaturalIntegerClass(Class aClass_p)
    {
        return Integer.class.equals(aClass_p) || Long.class.equals(aClass_p) || Byte.class.equals(aClass_p);
    }

    /**
     * 
     * @param aClass_p
     * @return <code>true</code> if the given class is one of  {@link java.lang.Double}, {@link java.lang.Float} or is a natural integer class (see {@link #isNaturalIntegerClass(Class)})<br>
     *         <code>false</code> otherwise 
     */
    private boolean isRationalNumberClass(Class aClass_p)
    {
        return isNaturalIntegerClass(aClass_p) || Double.class.equals(aClass_p) || Float.class.equals(aClass_p);
    }

    /**
     * 
     * @param number_p
     * @return an {@link Integer} conversion of the given number or the given number if the conversion is not possible
     */
    private Number tryToEnforceInteger(Number number_p)
    {
        Class numberClass = number_p.getClass();

        if (isNaturalIntegerClass(numberClass) && Integer.MAX_VALUE >= number_p.longValue() && Integer.MIN_VALUE <= number_p.longValue())
        {
            int intNumber = number_p.intValue();
            return Integer.valueOf(intNumber);
        }
        else
        {
            return number_p;
        }
    }

    /**
     * 
     * @param number_p
     * @return an {@link Long} conversion of the given number or the given number if the conversion is not possible
     */
    private Number tryToEnforceLong(Number number_p)
    {
        Class numberClass = number_p.getClass();
        if (isNaturalIntegerClass(numberClass))
        {
            long longNumber = number_p.longValue();
            return Long.valueOf(longNumber);
        }
        else
        {
            return number_p;
        }
    }

    /**
     * 
     * @param number_p
     * @return an {@link Double} conversion of the given number or the given number if the conversion is not possible
     */
    private Number tryToEnforceDouble(Number number_p)
    {
        Class numberClass = number_p.getClass();
        if (isRationalNumberClass(numberClass))
        {
            double doubleNumber = number_p.doubleValue();
            return new Double(doubleNumber);
        }
        else
        {
            return number_p;
        }
    }

    /**
     * 
     * @param number_p
     * @return an {@link Float} conversion of the given number or the given number if the conversion is not possible
     */
    private Number tryToEnforceFloat(Number number_p)
    {
        Class numberClass = number_p.getClass();
        if (isRationalNumberClass(numberClass) && Float.MAX_VALUE >= number_p.doubleValue() && Float.MIN_VALUE <= number_p.doubleValue())
        {
            float floatNumber = number_p.floatValue();
            return new Float(floatNumber);
        }
        else
        {
            return number_p;
        }
    }

    /**
     * 
     * @param number_p
     * @return an {@link BigInteger} conversion of the given number or the given number if the conversion is not possible
     */
    private Number tryToEnforceBigInteger(Number number_p)
    {
        Class numberClass = number_p.getClass();
        if (isNaturalIntegerClass(numberClass))
        {
            long longNumber = number_p.longValue();
            return BigInteger.valueOf(longNumber);
        }
        else
        {
            return number_p;
        }
    }

    /**
     * 
     * @param number_p
     * @return an {@link BigDecimal} conversion of the given number or the given number if the conversion is not possible
     */
    private Number tryToEnforceBigDecimal(Number number_p)
    {
        Class numberClass = number_p.getClass();
        if (isNaturalIntegerClass(numberClass))
        {
            long longNumber = number_p.longValue();
            BigInteger bigInt = BigInteger.valueOf(longNumber);
            return new BigDecimal(bigInt);
        }
        if (isRationalNumberClass(numberClass))
        {
            double doubleNumber = number_p.doubleValue();
            return new BigDecimal("" + doubleNumber);
        }
        else
        {
            return number_p;
        }
    }

    /**
     * Initializes this field control with the configured number format.<br>
     * If no number format is configured the default locale {@link NumberFormat} is used. 
     * @param fieldmanager_p
     * @param configNode_p
     * @throws Exception
     */
    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);
        OwXMLUtil configUtil = new OwStandardXMLUtil(configNode_p);
        NumberFormat defaultLocaleFormat = NumberFormat.getInstance();
        String numberFormatString = configUtil.getSafeTextValue(NUMBER_FORMAT_ELEMENT, "");

        if (numberFormatString.length() == 0)
        {
            m_numberFormat = defaultLocaleFormat;
        }
        else
        {
            OwAppContext context = fieldmanager_p.getContext();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(context.getLocale());
            m_numberFormat = new DecimalFormat(numberFormatString, symbols);
        }
    }

}