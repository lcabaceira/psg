package com.wewebu.ow.server.fieldctrlimpl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwDateTimeUtil;

/**
 *<p>
 * Current time relative {@link Date} extension base. 
 * Replaces OwNextDaysDateControl and OwLastDaysDateControl.
 * Instances of this class will be dates set to one of the following current date relative moments 
 * <ul>
 *  <li>today</li>
 *  <li>tomorrow (next one day)</li>
 *  <li>next two days</li>
 *  <li>next week</li>
 *  <li>next two weeks</li>
 *  <li>next 30 days</li>
 *  <li>next 90 days</li>
 *  <li>yesterday (last one day)</li>
 *  <li>last two days</li>
 *  <li>last week</li>
 *  <li>last 30 days</li>
 *  <li>last 90 days</li>
 * </ul>
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
 *@since 3.1.0.0
 */
public class OwRelativeDate extends Date
{

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwRelativeDate.class);

    /** value key for null date (empty field)*/
    public static final int KEY_EMPTY_FIELD = -1;
    /** value key for combo box */
    public static final int KEY_TODAY = 0;
    /** value key for combo box */
    public static final int KEY_LAST_ONE_DAY = 1;
    /** value key for combo box */
    public static final int KEY_LAST_TWO_DAYS = 2;
    /** value key for combo box */
    public static final int KEY_LAST_ONE_WEEK = 3;
    /** value key for combo box */
    public static final int KEY_LAST_TWO_WEEKS = 4;
    /** value key for combo box */
    public static final int KEY_LAST_30_DAYS = 5;
    /** value key for combo box */
    public static final int KEY_LAST_90_DAYS = 6;

    /** value key for combo box */
    public static final int KEY_NEXT_ONE_DAY = 7;
    /** value key for combo box */
    public static final int KEY_NEXT_TWO_DAYS = 8;
    /** value key for combo box */
    public static final int KEY_NEXT_ONE_WEEK = 9;
    /** value key for combo box */
    public static final int KEY_NEXT_TWO_WEEKS = 10;
    /** value key for combo box */
    public static final int KEY_NEXT_30_DAYS = 11;
    /** value key for combo box */
    public static final int KEY_NEXT_90_DAYS = 12;

    /**
     * 
     */
    private static final long serialVersionUID = 178353404736157334L;

    private static final Map STRING_TRANSLATION_KEYS = new LinkedHashMap();

    /**
     * 
     * @param value_p String to be n normalized 
     * @return the given String with leading trailing spaces omitted and the inner white spaces replaced 
     *         with a single space 
     */
    private static final String normalizeStringValue(String value_p)
    {
        value_p = (value_p == null) ? "" : value_p.trim().toLowerCase();
        return value_p.replaceAll("\\s+", " ");
    }

    static
    {
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("today"), Integer.valueOf(KEY_TODAY));

        STRING_TRANSLATION_KEYS.put(normalizeStringValue("tomorrow"), Integer.valueOf(KEY_NEXT_ONE_DAY));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("next two days"), Integer.valueOf(KEY_NEXT_TWO_DAYS));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("next week"), Integer.valueOf(KEY_NEXT_ONE_WEEK));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("next two weeks"), Integer.valueOf(KEY_NEXT_TWO_WEEKS));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("next 30 days"), Integer.valueOf(KEY_NEXT_30_DAYS));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("next 90 days"), Integer.valueOf(KEY_NEXT_90_DAYS));

        STRING_TRANSLATION_KEYS.put(normalizeStringValue("yesterday"), Integer.valueOf(KEY_LAST_ONE_DAY));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("last two days"), Integer.valueOf(KEY_LAST_TWO_DAYS));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("last week"), Integer.valueOf(KEY_LAST_ONE_WEEK));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("last two weeks"), Integer.valueOf(KEY_LAST_TWO_WEEKS));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("last 30 days"), Integer.valueOf(KEY_LAST_30_DAYS));
        STRING_TRANSLATION_KEYS.put(normalizeStringValue("last 90 days"), Integer.valueOf(KEY_LAST_90_DAYS));
    }

    /**
     * Returns an instance of {@link OwRelativeDate} for one of the following relative 
     * date representing strings (case insensitive, trimmed and with the inner white space normalized): 
     * <ul>
     *  <li>today</li>
     *  <li>tomorrow</li>
     *  <li>next two days</li>
     *  <li>next week</li>
     *  <li>next two weeks</li>
     *  <li>next 30 days</li>
     *  <li>next 90 days</li>
     *  <li>yesterday</li>
     *  <li>last two days</li>
     *  <li>last week</li>
     *  <li>last 30 days</li>
     *  <li>last 90 days</li>
     * </ul>
     * @param string_p String representation of a relative date
     * @return an {@link OwRelativeDate} object for the given relative date string
     * @throws OwInvalidOperationException
     */
    public static OwRelativeDate fromString(String string_p) throws OwInvalidOperationException
    {
        Integer intValue = (Integer) STRING_TRANSLATION_KEYS.get(normalizeStringValue(string_p));
        if (intValue != null)
        {
            return new OwRelativeDate(intValue.intValue());
        }
        else
        {
            LOG.error("OwRelativeDate.fromString(): Invalid OwRelativeDate string : " + string_p);
            throw new OwInvalidOperationException("Invalid relative-date string : " + string_p);
        }
    }

    private int m_iSelectedKey = KEY_TODAY;

    /**
     * Constructor
     * @param iKey_p - the key value
     */
    public OwRelativeDate(int iKey_p)
    {
        m_iSelectedKey = iKey_p;
        switch (iKey_p)
        {
            case KEY_TODAY:
                setTimeInPast(0);
                break;

            case KEY_NEXT_ONE_DAY:
                setTimeInFuture(1);
                break;

            case KEY_NEXT_TWO_DAYS:
                setTimeInFuture(2);
                break;

            case KEY_NEXT_ONE_WEEK:
                setTimeInFuture(7);
                break;

            case KEY_NEXT_TWO_WEEKS:
                setTimeInFuture(14);
                break;

            case KEY_NEXT_30_DAYS:
                setTimeInFuture(30);
                break;

            case KEY_NEXT_90_DAYS:
                setTimeInFuture(90);
                break;

            case KEY_LAST_ONE_DAY:
                setTimeInPast(1);
                break;

            case KEY_LAST_TWO_DAYS:
                setTimeInPast(2);
                break;

            case KEY_LAST_ONE_WEEK:
                setTimeInPast(7);
                break;

            case KEY_LAST_TWO_WEEKS:
                setTimeInPast(14);
                break;

            case KEY_LAST_30_DAYS:
                setTimeInPast(30);
                break;

            case KEY_LAST_90_DAYS:
                setTimeInPast(90);
                break;
        }
    }

    /**
     * Constructor used for restoring search template state
     * @param timestamp_p - the date as long
     */
    public OwRelativeDate(long timestamp_p)
    {
        long today = System.currentTimeMillis();
        long days = (timestamp_p - today) / (1000 * 60 * 60 * 24);

        if (days <= 0)
        {
            days = -days;
            if (days == 0)
            {
                m_iSelectedKey = KEY_TODAY;
                setTimeInPast(0);
            }
            else
            {
                if (days == 1)
                {
                    m_iSelectedKey = KEY_LAST_ONE_DAY;
                    setTimeInPast(1);
                }
                else if (days == 2)
                {
                    m_iSelectedKey = KEY_LAST_TWO_DAYS;
                    setTimeInPast(2);
                }
                else if (days > 2 && days <= 7)
                {
                    m_iSelectedKey = KEY_LAST_ONE_WEEK;
                    setTimeInPast(7);
                }
                else if (days > 7 && days <= 14)
                {
                    m_iSelectedKey = KEY_LAST_TWO_WEEKS;
                    setTimeInPast(14);
                }
                else if (days > 14 && days <= 30)
                {
                    m_iSelectedKey = KEY_LAST_30_DAYS;
                    setTimeInPast(30);
                }
                else if (days > 30)
                {
                    m_iSelectedKey = KEY_LAST_90_DAYS;
                    setTimeInPast(90);
                }
            }
        }
        else
        {
            if (days == 1)
            {
                m_iSelectedKey = KEY_NEXT_ONE_DAY;
                setTimeInFuture(1);
            }
            else if (days == 2)
            {
                m_iSelectedKey = KEY_NEXT_TWO_DAYS;
                setTimeInFuture(2);
            }
            else if (days > 2 && days <= 7)
            {
                m_iSelectedKey = KEY_NEXT_ONE_WEEK;
                setTimeInFuture(7);
            }
            else if (days > 7 && days <= 14)
            {
                m_iSelectedKey = KEY_NEXT_TWO_WEEKS;
                setTimeInFuture(14);
            }
            else if (days > 14 && days <= 30)
            {
                m_iSelectedKey = KEY_NEXT_30_DAYS;
                setTimeInFuture(30);
            }
            else if (days > 30)
            {
                m_iSelectedKey = KEY_NEXT_90_DAYS;
                setTimeInFuture(90);
            }
        }
    }

    /**
     * Set the time in future with given number of days
     * @param days_p - the number of days
     * @since 3.0.0.0
     */
    private void setTimeInFuture(int days_p)
    {
        Date d = new Date();
        d = OwDateTimeUtil.setBeginOfDayTime(d);
        d = OwDateTimeUtil.offsetDay(d, days_p, true);
        this.setTime(d.getTime());
    }

    /**
     * Set the time in past with given number of days
     * @param days_p - the number of days
     * @since 3.0.0.0
     */
    private void setTimeInPast(int days_p)
    {
        Date d = new Date();
        d = OwDateTimeUtil.setBeginOfDayTime(d);
        d = OwDateTimeUtil.offsetDay(d, -days_p, true);
        this.setTime(d.getTime());
    }

    /**
     * Get selected key.
     */
    public int getSelectedKey()
    {
        return m_iSelectedKey;
    }

}
