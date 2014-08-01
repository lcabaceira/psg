package com.wewebu.ow.server.fieldctrlimpl;

import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Control to display a last days select box for OwLastDaysDate.
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
public class OwLastDaysDateControl extends OwFieldManagerControl
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwLastDaysDateControl.class);

    /** date formatter */
    private DateFormat m_DateFormat;
    /** flag indicating that null value is allowed for this field*/
    private boolean m_allowNullValue;
    /** map containing the keys of the control and corresponding keys in languages files*/
    private static final Map TRANSLATION_KEYS = new LinkedHashMap();
    /** map containing the keys of the control and corresponding defaults translation words*/
    private static final Map DEFAULT_TRANSLATION_VALUES = new LinkedHashMap();

    /**
     * Helper method, for building the control structure.
     * @param value_p - the key
     * @param translationKey_p - the translation key
     * @param defaultTranslationValue_p - the default value for translation key.
     */
    protected static void addKeyValue(int value_p, String translationKey_p, String defaultTranslationValue_p)
    {
        Integer intValue = Integer.valueOf(value_p);
        TRANSLATION_KEYS.put(intValue, translationKey_p);
        DEFAULT_TRANSLATION_VALUES.put(intValue, defaultTranslationValue_p);
    }

    static
    {
        addKeyValue(OwRelativeDate.KEY_TODAY, "fieldctrlimpl.OwLastDaysDateControl.lasttoday", "today");
        addKeyValue(OwRelativeDate.KEY_LAST_ONE_DAY, "fieldctrlimpl.OwLastDaysDateControl.lastoneday", "yesterday");
        addKeyValue(OwRelativeDate.KEY_LAST_TWO_DAYS, "fieldctrlimpl.OwLastDaysDateControl.lasttwodays", "last two days");
        addKeyValue(OwRelativeDate.KEY_LAST_ONE_WEEK, "fieldctrlimpl.OwLastDaysDateControl.lastoneweek", "last week");
        addKeyValue(OwRelativeDate.KEY_LAST_TWO_WEEKS, "fieldctrlimpl.OwLastDaysDateControl.lasttwoweeks", "last two weeks");
        addKeyValue(OwRelativeDate.KEY_LAST_30_DAYS, "fieldctrlimpl.OwLastDaysDateControl.last30days", "last 30 days");
        addKeyValue(OwRelativeDate.KEY_LAST_90_DAYS, "fieldctrlimpl.OwLastDaysDateControl.last90days", "last 90 days");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#init()
     */
    public void init() throws Exception
    {
        super.init();

        m_DateFormat = new java.text.SimpleDateFormat(((OwMainAppContext) getContext()).getDateFormatString());
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#init(com.wewebu.ow.server.app.OwFieldManager, org.w3c.dom.Node)
     */
    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);
        OwXMLUtil configUtil = new OwStandardXMLUtil(configNode_p);
        m_allowNullValue = configUtil.getSafeBooleanAttributeValue("allowNullValue", true);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String)
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        int iSelectedKey = 0;

        try
        {
            Object value = null;

            if (!m_allowNullValue && field_p.getValue() == null)
            {
                OwFieldDefinition fieldDefinition = field_p.getFieldDefinition();
                value = fieldDefinition.getDefaultValue();
            }
            else
            {
                value = field_p.getValue();
            }

            if (value != null && value instanceof OwRelativeDate)
            {
                iSelectedKey = ((OwRelativeDate) value).getSelectedKey();
            }
            else
            {
                iSelectedKey = m_allowNullValue ? OwRelativeDate.KEY_EMPTY_FIELD : OwRelativeDate.KEY_TODAY;

                if (value != null)
                {
                    if (value instanceof Date)
                    {
                        Date dateValue = (Date) value;
                        OwLastDaysDate lastDaysValue = new OwLastDaysDate(dateValue.getTime());
                        iSelectedKey = lastDaysValue.getSelectedKey();
                    }
                    else
                    {
                        OwAppContext context = getContext();
                        OwFieldDefinition definition = field_p.getFieldDefinition();
                        String message = "Incompatible field value class " + value.getClass() + " for field " + definition.getClassName() + " with name " + definition.getDisplayName(context.getLocale());

                        LOG.error("OwLastDaysDateControl.insertEditField(): " + message);
                        //internationalization will be handled in the surrounding try-catch 
                        throw new OwInvalidOperationException(message);
                    }
                }
            }

        }
        catch (Exception e)
        {
            OwAppContext context = getContext();
            OwFieldDefinition definition = field_p.getFieldDefinition();
            LOG.error("OwLastDaysDateControl.insertEditField(): could not create edit field", e);
            throw new OwInvalidOperationException(new OwString1("app.OwFieldManager.errorForField", "Error for field %1:", definition.getDisplayName(context.getLocale())), e);
        }

        List items = new LinkedList();
        if (m_allowNullValue)
        {
            items.add(new OwDefaultComboItem("" + OwRelativeDate.KEY_EMPTY_FIELD, " "));
        }
        Set keys = TRANSLATION_KEYS.keySet();
        Iterator keysIterator = keys.iterator();
        while (keysIterator.hasNext())
        {
            Integer key = (Integer) keysIterator.next();
            String tranlationKey = (String) TRANSLATION_KEYS.get(key);
            String defaultValue = (String) DEFAULT_TRANSLATION_VALUES.get(key);
            String displayValue = getContext().localize(tranlationKey, defaultValue);
            items.add(new OwDefaultComboItem(key.toString(), displayValue));
        }
        OwComboModel model = new OwDefaultComboModel(false, false, "" + iSelectedKey, items);
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, strID_p, null, null, null);
        renderer.renderCombo(w_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertReadOnlyField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object)
     */
    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (null != value_p)
        {
            w_p.write(m_DateFormat.format((java.util.Date) value_p));
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#updateField(javax.servlet.http.HttpServletRequest, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object, java.lang.String)
     */
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Object result = null;
        int iKey = Integer.parseInt(request_p.getParameter(strID_p));
        if (iKey != OwRelativeDate.KEY_EMPTY_FIELD)
        {
            result = new OwLastDaysDate(iKey);
        }
        return result;
    }

}