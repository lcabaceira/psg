package com.wewebu.ow.server.app;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwDateTimeUtil;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Base class for a single editable date property used in HTML forms. <br/>
 * You can either instantiate a control and use the render and setValue,
 * getValue methods <br/>
 * or alternatively <br/>
 * use the control only with the static methods insertEditHTML and updateField
 * without a instance.
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
public class OwEditablePropertyDate extends OwEditablePropertyString
{
    /** package logger or class logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwEditablePropertyDate.class);

    /**
     *<p>
     * Item for the combobox.
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
    private static class OwListBoxItem
    {
        /**
         * construct list box item
         * 
         * @param displayText_p
         *            text for the combobox item
         * @param value_p
         *            value for the combobox item
         */
        public OwListBoxItem(OwString displayText_p, String value_p)
        {
            m_displayText = displayText_p;
            m_value = value_p;
        }

        /**
         * construct list box item
         * 
         * @param strDisplayText_p
         *            text for the combobox item
         * @param value_p
         *            value for the combobox item
         */
        public OwListBoxItem(String strDisplayText_p, String value_p)
        {
            m_displayText = new OwString(strDisplayText_p);
            m_value = value_p;
        }

        /** text for the combobox item */
        private OwString m_displayText;
        /** value for the combobox item */
        private String m_value;
    }

    // === customize flags
    /** ENUM for the behavior, only date is shown */
    public static final int TYPE_DATE = 0x01;
    /** ENUM for the behavior, only time is shown */
    public static final int TYPE_TIME = 0x02; // NOT IMPLEMENTED YET
    /** ENUM for the behavior, the date value can be left empty */
    public static final int TYPE_EMPTY_DATE_POSSIBLE = 0x04;

    /** reference to the app context */
    protected OwMainAppContext m_context;

    /** string to use to format the date */
    protected String m_sDateFormatString;
    private boolean ignoreTime = true;

    /** singleton with list box items for combobox control */
    private static OwDateControlListItemsSingleton m_ListItems = new OwDateControlListItemsSingleton();

    /**
     *<p>
     * OwDateControlListItemsSingleton.
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
    private static class OwDateControlListItemsSingleton
    {
        /** combobox items for the days */
        private List m_Time_Days;
        /** combobox items for the month */
        private List m_Time_Month;
        /** combobox items for the years */
        private List m_Time_Years;

        /**
         * construct a Date/Time Control
         */
        public OwDateControlListItemsSingleton()
        {
            // === combo type not yet initialized
            m_Time_Days = new ArrayList();
            m_Time_Month = new ArrayList();
            m_Time_Years = new ArrayList();

            // === ComboBox GUI Type
            // === Init Days
            for (int i = 1; i < 32; i++)
            {
                String strID = String.valueOf(i);
                String strDay = strID;
                if (i < 10)
                {
                    strDay = "0" + strDay;
                }

                m_Time_Days.add(new OwListBoxItem(strDay, strID));
            }

            // === Init month
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.january", "January"), "0"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.february", "February"), "1"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.march", "March"), "2"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.april", "April"), "3"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.may", "May"), "4"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.juni", "June"), "5"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.july", "July"), "6"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.august", "August"), "7"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.september", "September"), "8"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.october", "October"), "9"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.november", "November"), "10"));
            m_Time_Month.add(new OwListBoxItem(new OwString("app.OwDateControl.december", "December"), "11"));

            // === Init years
            for (int i = 1900; i < 2100; i++)
            {
                String strID = String.valueOf(i);
                String strDay = strID;

                m_Time_Years.add(new OwListBoxItem(strDay, strID));
            }
        }
    }

    /**
     * render the date control with all sub controls
     * 
     * @param w_p
     *            Writer object to write HTML to
     * @param date_p
     *            Date to display
     * @param strID_p
     *            Id for the form element
     * @param fEmptyDataPossible_p
     *            true = date can be null
     * @param sDateFormat_p
     *            String with date format e.g.: dd.MM.yyyy HH:mm
     */
    public static void insertEditHTML(OwMainAppContext context_p, java.util.Locale locale_p, Writer w_p, java.util.Date date_p, String strID_p, boolean fUseJS_Control_p, boolean fEmptyDataPossible_p, String sDateFormat_p) throws Exception
    {
        insertEditHTML(context_p, locale_p, w_p, date_p, strID_p, fUseJS_Control_p, fEmptyDataPossible_p, sDateFormat_p, null);
    }

    /**
     * render the date control with all sub controls
     * 
     *@param w_p
     *            Writer object to write HTML to
     * @param date_p
     *            Date to display
     * @param strID_p
     *            Id for the form element
     * @param fEmptyDataPossible_p
     *            true = date can be null
     * @param sDateFormat_p
     *            String with date format e.g.: dd.MM.yyyy HH:mm
     * @param timeFormat 
     *              String format for time. E.g. HH:mm. If null, the time field will be ignored.
     * @throws Exception
     * @since 4.0.0.0
     */
    public static void insertEditHTML(OwMainAppContext context_p, java.util.Locale locale_p, Writer w_p, java.util.Date date_p, String strID_p, boolean fUseJS_Control_p, boolean fEmptyDataPossible_p, String sDateFormat_p, String timeFormat)
            throws Exception
    {

        if (!fUseJS_Control_p)
        {
            insertNonJSControl(context_p, locale_p, w_p, date_p, strID_p, fEmptyDataPossible_p, timeFormat);
        }
        else
        {
            insertJSControl(context_p, locale_p, w_p, date_p, strID_p, fEmptyDataPossible_p, sDateFormat_p);
        }
    }

    /**
     * Renders the JavaScript version of this control.
     * 
     * @param context_p
     * @param locale_p
     * @param w_p
     * @param date_p
     * @param strID_p
     * @param fEmptyDataPossible_p
     * @param sDateFormat_p
     * @throws IOException
     * @throws Exception
     * @since 4.0.0.0
     */
    public static void insertJSControl(OwMainAppContext context_p, java.util.Locale locale_p, Writer w_p, java.util.Date date_p, String strID_p, boolean fEmptyDataPossible_p, String sDateFormat_p) throws IOException, Exception
    {
        String strID = buildControlId(strID_p);
        java.util.Calendar calendar = buildCalendar(date_p);

        // configure extjs after loading
        w_p.write("<script type=\"text/javascript\">\n");
        w_p.write("Ext.BLANK_IMAGE_URL = \"");
        w_p.write(OwHTMLHelper.encodeJavascriptString(context_p.getBaseURL() + "/js/extjs/resources/images/default/s.gif"));
        w_p.write("\";\n");

        String[] months = new String[OwDateTimeUtil.NUMBER_OF_MONTHS];
        System.arraycopy(OwDateTimeUtil.getMonthNames(locale_p), 0, months, 0, OwDateTimeUtil.NUMBER_OF_MONTHS);

        w_p.write("Date.monthNames =\n");
        writeJSArrayValues(w_p, months);
        //
        writeDateJSMonthNumbersMap(locale_p, w_p);

        w_p.write("Date.dayNames =\n");
        String[] weekDays = new String[OwDateTimeUtil.NUMBER_OF_WEEKDAYS];
        System.arraycopy(OwDateTimeUtil.getWeekDays(locale_p), 1, weekDays, 0, OwDateTimeUtil.NUMBER_OF_WEEKDAYS);
        writeJSArrayValues(w_p, weekDays);

        writeDateJSHelperFunctions(w_p);

        w_p.write("if(Ext.DatePicker){\n");
        w_p.write("   Ext.apply(Ext.DatePicker.prototype, {\n");
        Node fieldManagerNode = context_p.getConfiguration().getFieldManagerConfiguration().getNode();
        if (fieldManagerNode != null)
        {
            Node node = OwXMLDOMUtil.getChildNode(fieldManagerNode, "DatePickerStartDay");
            if (node != null)
            {
                String startDay = node.getFirstChild().getNodeValue().trim();
                w_p.write("      startDay : " + startDay + ",\n");
            }
        }
        String jsEncJsDate = OwHTMLHelper.encodeJavascriptString(convertDateFormat(sDateFormat_p));
        w_p.write("      todayText         : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.Today", "Today") + "\",\n");
        w_p.write("      minText           : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.BeforeMinimumDate", "This date is below the minimum date.") + "\",\n");
        w_p.write("      maxText           : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.AfterMaximumDate", "This date exceeds the maximum date.") + "\",\n");
        w_p.write("      okText            : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.okText", "OK") + "\",\n");
        w_p.write("      cancelText        : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.cancelText", "Cancel") + "\",\n");
        w_p.write("      disabledDaysText  : \"\",\n");
        w_p.write("      disabledDatesText : \"\",\n");
        w_p.write("      monthNames        : Date.monthNames,\n");
        w_p.write("      dayNames          : Date.dayNames,\n");
        w_p.write("      nextText          : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.NextMonth", "Next Month (Ctrl+Right)") + "\",\n");
        w_p.write("      prevText          : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.PreviousMonth", "Previous Month (Ctrl+Left)") + "\",\n");
        w_p.write("      monthYearText     : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.ChooseMonth", "Select a month (Ctrl+Up/Down to change the years)") + "\",\n");
        w_p.write("      todayTip          : \"" + OwString.localize(locale_p, "app.OwEditablePropertyDate.Spacebar", "{0} (space bar)") + "\",\n");
        w_p.write("      format            : \"" + jsEncJsDate + "\",\n");
        w_p.write("      altFormats        : \"" + jsEncJsDate + "\"\n");
        w_p.write("   });\n");
        w_p.write("}\n");
        w_p.write("</script>\n");
        // insert input control to convert
        String formatedDate = "";
        if (!fEmptyDataPossible_p || date_p != null)
        {
            java.text.SimpleDateFormat dateFormat = OwDateTimeUtil.createDateFromat(locale_p, sDateFormat_p);
            formatedDate = dateFormat.format(calendar.getTime());
        }
        StringWriter htmlEncId = new StringWriter();
        OwHTMLHelper.writeSecureHTML(htmlEncId, strID);
        w_p.write("<input title='(");
        OwHTMLHelper.writeSecureHTML(w_p, sDateFormat_p);
        w_p.write(")' class='OwInputControl OwInputControlDate OwAjaxComponent' id='");
        w_p.write(htmlEncId.getBuffer().toString());
        w_p.write("' value='");
        OwHTMLHelper.writeSecureHTML(w_p, formatedDate);
        w_p.write("' name='");
        w_p.write(htmlEncId.getBuffer().toString());
        w_p.write("'>\n");
        // js for advanced combo
        String varName = "converted_" + OwHTMLHelper.encodeJavascriptString(strID);

        w_p.write("<script type=\"text/javascript\">\n");
        w_p.write("     Ext.onReady(function(){\n");
        w_p.write("     var " + varName + " = new Ext.form.DateField({\n");
        w_p.write("         allowBlank: " + (fEmptyDataPossible_p ? "true" : "false") + ",\n");
        w_p.write("         format: '" + jsEncJsDate + "',\n");
        w_p.write("         altFormats: '" + convertToAlternativeDateFormat(jsEncJsDate) + "',\n");
        w_p.write("         enableKeyEvents: true," + "\n");
        w_p.write("         hideMode: 'visibility'" + "\n");
        w_p.write("     });\n");
        w_p.write("     " + varName + ".applyToMarkup('");
        w_p.write(htmlEncId.getBuffer().toString());
        w_p.write("');\n");
        //            w_p.write("     converted_" + OwHTMLHelper.encodeJavascriptString(strID_p) + ".on('beforeShow',onShowHandler);\n");
        //            w_p.write(" function updateDateSize(){\n");
        //            w_p.write("     this.synchSize();\n");
        //            w_p.write(" };\n");
        w_p.write("     });\n");
        w_p.write("</script>\n");
    }

    /**
     * @param strID_p
     * @return an ID to be used in the generated HTML field.
     * @since 4.0.0.0
     */
    private static String buildControlId(String strID_p)
    {
        String strID = strID_p.startsWith("-") ? strID_p.replace('-', '_') : strID_p;
        return strID;
    }

    /**
     * Renders the Non Javascript version of the DateTime control.
     * @param context_p 
     * @param locale_p
     * @param w_p
     * @param date_p
     * @param fEmptyDataPossible_p
     * @param timerFormat the time format to use for the time field. If null, the time part will be ignored. 
     * @throws IOException
     * @throws Exception
     * @since 4.0.0.0
     */
    public static void insertNonJSControl(OwMainAppContext context_p, java.util.Locale locale_p, Writer w_p, java.util.Date date_p, String strID_p, boolean fEmptyDataPossible_p, String timerFormat) throws IOException, Exception
    {
        String strID = buildControlId(strID_p);
        java.util.Calendar calendar = buildCalendar(date_p);

        w_p.write("<table border='0' cellpadding='0' cellspacing='0'>\r\n");
        w_p.write("<tr>\r\n");

        // === ComboBox GUI Type
        if (fEmptyDataPossible_p)
        {
            w_p.write("<td>");
            w_p.write("<input class='OwInputControl OwInputControlDate' name='EN_");
            OwHTMLHelper.writeSecureHTML(w_p, strID);
            w_p.write("' type='checkbox'");
            if (date_p != null)
            {
                w_p.write(" checked ");
            }
            w_p.write("></input>&nbsp;&nbsp;");
            w_p.write("</td>\r\n");
        }

        w_p.write("<td>");
        renderChoiceList(locale_p, w_p, m_ListItems.m_Time_Days, "D_" + strID, String.valueOf(calendar.get(java.util.Calendar.DAY_OF_MONTH)));
        w_p.write("</td>\r\n");

        w_p.write("<td>");
        renderChoiceList(locale_p, w_p, m_ListItems.m_Time_Month, "M_" + strID, String.valueOf(calendar.get(java.util.Calendar.MONTH)));
        w_p.write("</td>\r\n");

        w_p.write("<td>");
        renderChoiceList(locale_p, w_p, m_ListItems.m_Time_Years, "Y_" + strID, String.valueOf(calendar.get(java.util.Calendar.YEAR)));
        w_p.write("</td>\r\n");

        if (null != timerFormat)
        {
            w_p.write("<td>");
            String timeId = "T_" + strID;
            SimpleDateFormat format = new SimpleDateFormat(timerFormat);
            String value = format.format(calendar.getTime());

            w_p.write("<input type='text' size='" + value.length() + "' name='" + timeId + "' value='" + value + "'/>");
            w_p.write("</td>\r\n");
        }

        w_p.write("</tr>\r\n");
        w_p.write("</table>");
    }

    /**
     * @param date_p
     * @return a {@link Calendar} instance for this date.
     * @since 4.0.0.0
     */
    private static java.util.Calendar buildCalendar(java.util.Date date_p)
    {
        java.util.Calendar calendar = new java.util.GregorianCalendar();

        if (null == date_p)
        {
            calendar.setTime(new Date());
            // clear H,M,S
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
        }
        else
        {
            calendar.setTime(date_p);
        }
        return calendar;
    }

    /**
     * Utility method to convert date format to alternative date format without any special characters, to enable user to enter dates faster into date fields
     * @param dateFormat_p - the ExtJS date format used for a give locale
     * @since 3.1.0.4 
     */
    private static String convertToAlternativeDateFormat(String dateFormat_p)
    {
        String altJsEncJsDate;

        altJsEncJsDate = dateFormat_p.replace(".", "");
        altJsEncJsDate = altJsEncJsDate.replace(":", "");
        altJsEncJsDate = altJsEncJsDate.replace("/", "");
        altJsEncJsDate = altJsEncJsDate.replace("-", "");

        return altJsEncJsDate;
    }

    /**
     * Utility method for writing necessary java script functions for EXTJS Date.
     * @param w_p - the {@link Writer} object
     * @throws IOException
     * @since 3.0.0.0 
     */
    private static void writeDateJSHelperFunctions(Writer w_p) throws IOException
    {
        w_p.write("Date.getShortMonthName = function(month) {\n");
        w_p.write("     return Date.monthNames[month].substring(0, 3);\n");
        w_p.write("};\n");

        w_p.write("Date.getMonthNumber = function(name) {\n");
        w_p.write("     return Date.monthNumbers[name.substring(0, 3)];\n");
        w_p.write("};\n");

        w_p.write("Date.getShortDayName = function(day) {\n");
        w_p.write("      return Date.dayNames[day].substring(0, 3);\n");
        w_p.write("};\n");
    }

    /**
     * Write a JavaScript map between month short name and month number. 
     * @param locale_p - the {@link Locale} object
     * @param w_p - the {@link Writer} object.
     * @throws IOException
     * @since 3.0.0.0
     */
    private static void writeDateJSMonthNumbersMap(java.util.Locale locale_p, Writer w_p) throws IOException
    {
        String[] shortMonthNames = new String[OwDateTimeUtil.NUMBER_OF_MONTHS];
        System.arraycopy(OwDateTimeUtil.getShortMonthNames(locale_p), 0, shortMonthNames, 0, OwDateTimeUtil.NUMBER_OF_MONTHS);
        w_p.write("Date.monthNumbers =\n");
        w_p.write("{");
        for (int i = 0; i < shortMonthNames.length; i++)
        {
            w_p.write(shortMonthNames[i] + ":" + i);

            if (i < shortMonthNames.length - 1)
            {
                w_p.write(" ,\n");
            }
            else
            {
                w_p.write("\n};\n");
            }
        }
    }

    /**
     * Utility method, used to write JS array values to the given {@link Writer} object
     * @param w_p - the {@link Writer} object
     * @param values_p - the array to be written.
     * @throws IOException
     * @since 3.0.0.0
     */
    private static void writeJSArrayValues(Writer w_p, String[] values_p) throws IOException
    {
        w_p.write("[");
        for (int i = 0; i < values_p.length; i++)
        {
            w_p.write("'" + values_p[i] + "'");
            if (i < values_p.length - 1)
            {
                w_p.write(" ,\n");
            }
            else
            {
                w_p.write("\n];\n");
            }
        }
    }

    /**
     * Java and ExtJS use different date format markup strings. This method
     * converts the Java date format into the date format used by ExtJS (PHP).
     * ExtJS uses the PHP date format. See source/util/Date.js.
     * 
     * @param javaSimpleDateFormat_p
     *            String to search and replace
     * 
     * @return String
     * */
    public static String convertDateFormat(String javaSimpleDateFormat_p)
    {
        // StringBuffer for the php date format
        StringBuffer sPhpDateFormat = new StringBuffer();
        // int current tested position in Java date format
        int iPos = 0;
        // walk through java date format
        while (iPos < javaSimpleDateFormat_p.length())
        {
            // get the current pattern
            char cCurrentPattern = javaSimpleDateFormat_p.charAt(iPos);
            // get the starting position of the next pattern
            int iNextPatternStartPos = iPos;
            while ((iNextPatternStartPos < javaSimpleDateFormat_p.length()) && (javaSimpleDateFormat_p.charAt(iNextPatternStartPos) == cCurrentPattern))
            {
                iNextPatternStartPos++;
            }
            // calculate pattern length
            int iPatternLength = iNextPatternStartPos - iPos;
            // translate current pattern to a PHP pattern, depending on its
            // pattern length
            switch (cCurrentPattern)
            {
                case 'G': // Era designator
                    // era designator is not available in PHP date formats. just
                    // append the string "AD"
                    // this should be fine in most situations (year >= 0)
                    sPhpDateFormat.append("\\A\\D");
                    break;
                case 'y': // year
                    if (iPatternLength <= 2)
                    {
                        sPhpDateFormat.append('y');
                    }
                    else
                    {
                        sPhpDateFormat.append('Y');
                    }
                    break;
                case 'M': // month in year
                    switch (iPatternLength)
                    {
                        case 0:
                        case 1:
                            sPhpDateFormat.append('n');
                            break;
                        case 2:
                            sPhpDateFormat.append('m');
                            break;
                        case 3:
                            sPhpDateFormat.append('M');
                            break;
                        default:
                            sPhpDateFormat.append('F');
                            break;
                    }
                    break;
                case 'w': // week in year
                    // sorry, no one-digit week-of-year in PHP
                    sPhpDateFormat.append('W');
                    break;
                case 'W': // week in month
                    // not available in PHP. ignore
                    break;
                case 'D': // day in year
                    //incompatibility with php, ignored
                    //sPhpDateFormat.append('z');
                    break;
                case 'd': // day in month
                    if (iPatternLength <= 1)
                    {
                        sPhpDateFormat.append('j');
                    }
                    else
                    {
                        sPhpDateFormat.append('d');
                    }
                    break;
                case 'F': // day of week in month ???????
                    // not available in PHP. ignore
                    break;
                case 'E': // day in week as text
                    if (iPatternLength <= 3)
                    {
                        sPhpDateFormat.append('D');
                    }
                    else
                    {
                        sPhpDateFormat.append('l');
                    }
                    break;
                case 'a': // am/pm marker
                    sPhpDateFormat.append('A');
                    break;
                case 'H': // hour in day 0-23
                    if (iPatternLength <= 1)
                    {
                        sPhpDateFormat.append('G');
                    }
                    else
                    {
                        sPhpDateFormat.append('H');
                    }
                    break;
                case 'k': // hour in day (1-24)
                    // not available in PHP. ignore
                    break;
                case 'K': // hour in day (0-11)
                    // not available in PHP. ignore
                    break;
                case 'h': // hour in day (1-12)
                    if (iPatternLength <= 1)
                    {
                        sPhpDateFormat.append('g');
                    }
                    else
                    {
                        sPhpDateFormat.append('h');
                    }
                    break;
                case 'm': // minute in hour
                    // sorry, only available with leading zeros
                    sPhpDateFormat.append('i');
                    break;
                case 's': // seconds in minute
                    // sorry, only available with leading zeros
                    sPhpDateFormat.append('s');
                    break;
                case 'S': // milliseconds in second
                    sPhpDateFormat.append('u');
                    break;
                case 'z': // time zone
                    sPhpDateFormat.append('T');
                    break;
                case 'Z': // RFC 822 time zone
                    sPhpDateFormat.append('O');
                    break;
                case '\'': // start of string or '
                    if (iPatternLength == 2)
                    {
                        // exact to single quotes (''). Means '
                        sPhpDateFormat.append('\'');
                    }
                    else
                    {
                        // is a string. find next single quote indicating end of
                        // string
                        // and copy all characters to the output.
                        int iNextPatternAfterString = iPos + 1;
                        while (iNextPatternAfterString < javaSimpleDateFormat_p.length())
                        {
                            char cStringChar = javaSimpleDateFormat_p.charAt(iNextPatternAfterString);
                            if (cStringChar == '\'')
                            {
                                // we found a ' character in the string. if the next
                                // character is also
                                // a single quote, this is the escape sequence for
                                // single quotes in java
                                // strings (example 'o'' clock' means the string
                                // "o' clock".
                                if (((iNextPatternAfterString + 1) < javaSimpleDateFormat_p.length()) && (javaSimpleDateFormat_p.charAt(iNextPatternAfterString) == '\''))
                                {
                                    // copy the single quote to the output
                                    sPhpDateFormat.append('\'');
                                    // skip the second single quote
                                    iNextPatternAfterString++;
                                }
                                else
                                {
                                    // this is the end of the string. break while
                                    // loop
                                    break;
                                }
                            }
                            else
                            {
                                // copy string character to the output
                                if (isPhpSpecialChar(cStringChar))
                                {
                                    sPhpDateFormat.append('\\');
                                    sPhpDateFormat.append(cStringChar);
                                }
                                else
                                {
                                    sPhpDateFormat.append(cStringChar);
                                }
                            }
                            iNextPatternAfterString++;
                        }
                        // we have just parsed a string. set the
                        // iNextPatternStartPos to
                        // the character following the string
                        iNextPatternStartPos = iNextPatternAfterString;
                    }
                    break;
                default: // not a markup char. just copy
                    if (isPhpSpecialChar(cCurrentPattern))
                    {
                        for (int i = 0; i < iPatternLength; i++)
                        {
                            sPhpDateFormat.append('\\');
                            sPhpDateFormat.append(cCurrentPattern);
                        }
                    }
                    else
                    {
                        for (int i = 0; i < iPatternLength; i++)
                        {
                            sPhpDateFormat.append(cCurrentPattern);
                        }
                    }
                    break;
            }
            // go to the next pattern
            iPos = iNextPatternStartPos;
        }
        // return the created PHP date format
        return (sPhpDateFormat.toString());
    }

    /**
     * Check if the character has a meaning as a PHP date format pattern. If so,
     * it must be escaped in the PHP date format string.
     * 
     * @param testChar_p
     *            char character to test
     * 
     * @return boolean
     * */
    public static boolean isPhpSpecialChar(char testChar_p)
    {
        char[] specialChars = { 'd', 'D', 'j', 'l', 'S', 'w', 'z', 'W', 'F', 'm', 'M', 'n', 't', 'L', 'Y', 'y', 'a', 'A', 'g', 'G', 'h', 'H', 'i', 's', 'O', 'T', 'Z' };
        for (int i = 0; i < specialChars.length; i++)
        {
            if (specialChars[i] == testChar_p)
            {
                return (true);
            }
        }
        return (false);
    }

    /**
     * render a choice list with date values
     * 
     * @param w_p
     *            Writer Object
     * @param list_p
     *            list of combobox items
     * @param name_p
     *            name/id of form element
     * @param selectedItem_p
     *            element that is selected
     */
    private static void renderChoiceList(java.util.Locale locale_p, Writer w_p, List list_p, String name_p, String selectedItem_p) throws Exception
    {
        // List box
        if (list_p != null)
        {
            w_p.write("<select size='1' class='OwInputControl' name='");
            OwHTMLHelper.writeSecureHTML(w_p, name_p);
            w_p.write("'>\n");
            for (Iterator iter = list_p.iterator(); iter.hasNext();)
            {
                OwListBoxItem oItem = (OwListBoxItem) iter.next();
                w_p.write("<option value='");
                OwHTMLHelper.writeSecureHTML(w_p, oItem.m_value);
                w_p.write("' ");
                if (selectedItem_p != null && selectedItem_p.equals(oItem.m_value))
                {
                    w_p.write("selected ");
                }

                w_p.write(">");
                OwHTMLHelper.writeSecureHTML(w_p, oItem.m_displayText.getString(locale_p));
                w_p.write("</option>\n");
            }
            w_p.write("</select>\n");
        }
    }

    /**
     * update the date variable
     * 
     * @param request_p
     *            HttpServletRequest
     * @param strID_p
     *            Id for the form element
     * @param oldDate_p
     *            old date
     * @param fEmptyDataPossible_p
     *            true = date can be null
     * @param sDateFormat_p
     *            String with date format e.g.: dd.MM.yyyy HH:mm
     */
    public static java.util.Date updateField(java.util.Locale locale_p, HttpServletRequest request_p, String strID_p, boolean fUseJS_Control_p, java.util.Date oldDate_p, boolean fEmptyDataPossible_p, String sDateFormat_p) throws java.lang.Exception
    {
        return updateField(locale_p, request_p, strID_p, fUseJS_Control_p, oldDate_p, fEmptyDataPossible_p, sDateFormat_p, null);
    }

    /**
     * 
     * @param locale_p
     * @param request_p
     * @param strID_p
     * @param fUseJS_Control_p
     * @param oldDate_p
     * @param fEmptyDataPossible_p
     * @param sDateFormat_p
     * @param timeFormatString has to match the one used in the call to {@link #insertEditHTML(OwMainAppContext, Locale, Writer, Date, String, boolean, boolean, String, String)}.
     * @throws OwException
     */
    public static java.util.Date updateField(java.util.Locale locale_p, HttpServletRequest request_p, String strID_p, boolean fUseJS_Control_p, java.util.Date oldDate_p, boolean fEmptyDataPossible_p, String sDateFormat_p, String timeFormatString)
            throws OwException
    {
        // calendar object to construct the date
        java.util.Calendar calendar = new java.util.GregorianCalendar();
        String dateFromRequest = null;
        String strID = buildControlId(strID_p);

        if (!fUseJS_Control_p)
        {
            try
            {
                // === ComboBox GUI Type
                if ((request_p.getParameter("EN_" + strID) == null) && fEmptyDataPossible_p)
                {
                    return null;
                }

                String day = request_p.getParameter("D_" + strID);
                day = day == null ? "" : day;
                String month = request_p.getParameter("M_" + strID);
                month = month == null ? "" : month;
                String year = request_p.getParameter("Y_" + strID);
                year = year == null ? "" : year;
                dateFromRequest = "" + day + "/" + month + "/" + year;

                String timeStr = request_p.getParameter("T_" + strID);
                if (null != timeStr && null != timeFormatString)
                {
                    dateFromRequest += " " + timeStr;

                    DateFormat timeFormat = new SimpleDateFormat(timeFormatString);
                    Date time = timeFormat.parse(timeStr);

                    calendar.setTime(time);
                }
                calendar.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                calendar.set(java.util.Calendar.MONTH, Integer.parseInt(month));
                calendar.set(java.util.Calendar.YEAR, Integer.parseInt(year));
            }
            catch (Exception e)
            {
                LOG.debug("Invalid input: The date or time format is invalid.", e);
                LOG.debug("Date from request: " + dateFromRequest);
                dateFromRequest = dateFromRequest != null ? dateFromRequest : "";
                throw new OwFieldManagerException(OwString.localize1(locale_p, "app.OwDateTimeControl.dateinvalid", "The date or time format (%1) is invalid.", dateFromRequest) + " (" + ((null != timeFormatString) ? timeFormatString : "") + ")");
            }
        }
        else
        {
            try
            {
                // === JS Control GUI Type
                dateFromRequest = request_p.getParameter(strID);
                if ((dateFromRequest == null || dateFromRequest.length() == 0) && fEmptyDataPossible_p)
                {
                    return null;
                }

                // parse string representation to a Date object
                SimpleDateFormat sdf = OwDateTimeUtil.createDateFromat(locale_p, sDateFormat_p);
                calendar.setLenient(false);
                Date date = sdf.parse(dateFromRequest);

                calendar.setTime(date);
                if (calendar.get(Calendar.YEAR) > 9999)
                {
                    throw new ParseException("Invalid year. Year should be smaller or equal than 9999, but is " + calendar.get(Calendar.YEAR) + ".", 6);
                }
            }
            catch (Exception e)
            {
                LOG.debug("Invalid input: The date or time format is invalid.", e);
                LOG.debug("Date from request: " + dateFromRequest);
                dateFromRequest = dateFromRequest != null ? dateFromRequest : "";
                throw new OwFieldManagerException(OwString.localize1(locale_p, "app.OwDateTimeControl.dateinvalid", "The date or time format (%1) is invalid.", dateFromRequest) + " (" + sDateFormat_p + ")");
            }
        }
        /*
         * if ( OldDate_p != null ) { // === leave time portion to old date,
         * return only day, month and year java.util.Calendar returnCalendar =
         * new java.util.GregorianCalendar(); returnCalendar.setTime(OldDate_p);
         * 
         * 
         * returnCalendar.set(java.util.Calendar.DAY_OF_MONTH,calendar.get(java.util
         * .Calendar.DAY_OF_MONTH));
         * returnCalendar.set(java.util.Calendar.MONTH,
         * calendar.get(java.util.Calendar.MONTH));
         * returnCalendar.set(java.util.
         * Calendar.YEAR,calendar.get(java.util.Calendar.YEAR));
         * 
         * return returnCalendar.getTime(); } else
         */
        return calendar.getTime();
    }

    /**
     * construct a date property with context, use default date format
     * 
     * @param context_p
     *            OwMainAppContext
     */
    public OwEditablePropertyDate(OwMainAppContext context_p)
    {
        m_sDateFormatString = context_p.getDateFormatString();
        m_context = context_p;
    }

    /**
     * construct a date property with context
     * 
     * @param sDateFormat_p
     *            String with date format e.g.: dd.MM.yyyy HH:mm
     * @param context_p
     *            OwMainAppContext
     */
    public OwEditablePropertyDate(OwMainAppContext context_p, String sDateFormat_p)
    {
        this(context_p, sDateFormat_p, true);
    }

    /**
     * 
     * @param context_p
     * @param sDateFormat_p
     * @param ignoreTime
     * @since 4.0.0.0
     */
    public OwEditablePropertyDate(OwMainAppContext context_p, String sDateFormat_p, boolean ignoreTime)
    {
        m_sDateFormatString = sDateFormat_p;
        m_context = context_p;
        this.ignoreTime = ignoreTime;
    }

    /**
     * overridable to insert a single value into a edit HTML form
     * 
     * @param w_p
     *            Writer to write HTML code to
     */
    public void render(java.util.Locale locale_p, Writer w_p) throws Exception
    {
        boolean useJS_DateControl = this.m_context.useJS_DateControl();
        if (useJS_DateControl)
        {
            OwEditablePropertyDate.insertJSControl(this.m_context, this.m_context.getLocale(), w_p, (Date) getValue(), getFormElementID(), false, m_sDateFormatString);
        }
        else
        {
            OwEditablePropertyDate.insertNonJSControl(this.m_context, this.m_context.getLocale(), w_p, (Date) getValue(), getFormElementID(), false, ignoreTime ? null : this.m_context.getTimeFormatString());
        }
    }

    /**
     * overridable to apply changes on a submitted form
     * 
     * @param request_p
     *            HttpServletRequest with form data to update the property
     * @param strID_p
     *            String the HTML form element id of the requested value
     */
    protected Object getValueFromRequest(java.util.Locale locale_p, HttpServletRequest request_p, String strID_p) throws Exception
    {
        return updateField(locale_p, request_p, getFormElementID(), m_context.useJS_DateControl(), (Date) getValue(), false, m_sDateFormatString);
    }
}