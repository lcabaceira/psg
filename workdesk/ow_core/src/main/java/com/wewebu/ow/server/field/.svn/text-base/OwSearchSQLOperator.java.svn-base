package com.wewebu.ow.server.field;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwDateTimeUtil;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Standard Operator to create SQL out of a OwSearchNode Tree.
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
public class OwSearchSQLOperator
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwSearchSQLOperator.class);

    /**
     *<p>
     * Extended SimpleDateFormat with additional tokens.<br/><br/>
     * Q: Special format time zone e.g.: "+01:00"
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
    private static class SQLDateFormat extends java.text.SimpleDateFormat
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public SQLDateFormat(String string_p)
        {
            super(string_p);
        }

        public StringBuffer format(Date date_p, StringBuffer toAppendTo_p, FieldPosition fieldPosition_p)
        {
            StringBuffer ret = super.format(date_p, toAppendTo_p, fieldPosition_p);

            int iFind = ret.indexOf("Q");
            if (-1 != iFind)
            {
                // compute time zone hours and minutes
                java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
                calendar.setTime(date_p);

                int iZoneH = calendar.get(Calendar.ZONE_OFFSET) / (3600 * 1000);
                int iZoneM = java.lang.Math.abs(calendar.get(Calendar.ZONE_OFFSET) / (60 * 1000) - 60 * iZoneH);

                // correct daylight savings as well 
                if (calendar.getTimeZone().inDaylightTime(date_p))
                {
                    int iDSTH = calendar.getTimeZone().getDSTSavings() / (3600 * 1000);
                    int iDSTM = java.lang.Math.abs(calendar.getTimeZone().getDSTSavings() / (60 * 1000) - 60 * iDSTH);

                    iZoneH += iDSTH;
                    iZoneM += iDSTM;
                }

                // replace Q with time zone
                ret = ret.replace(iFind, iFind + 1, java.text.MessageFormat.format("{0,number,+00;-00}:{1,number,00}", new Object[] { Integer.valueOf(iZoneH), Integer.valueOf(iZoneM) }));
            }

            return ret;
        }

    }

    // === date mode can be one of the following
    /** date mode for FileNet Image Manager (days since 1.1.1970) */
    public static final int DATE_MODE_FNIM = 0x0001;
    /** date mode for converting data types in where clause, Microsoft Access conform */
    public static final int DATE_MODE_MS_ACCESS = 0x0002;
    /** date mode for FileNet P8 */
    public static final int DATE_MODE_FNCM = 0x0003;
    /** date mode for converting data types in where clause, ORACLE conform */
    public static final int DATE_MODE_ORACLE = 0x0004;
    /** date mode for converting data types in where clause, MSSQL conform */
    public static final int DATE_MODE_MSSQL = 0x0005;
    /** date mode for converting data types in where clause, DB2 conform */
    public static final int DATE_MODE_DB2 = 0x0006;
    /** date mode for converting data types in where clause, default mode */
    public static final int DATE_MODE_DEFAULT = 0x0007;

    /** date mode for date time optional */
    public static final int DATE_MODE_TREAT_IGNORE_TIME_AS_RANGE = 0x0100;

    /** date format for SQL Date and Time conversion */
    private java.text.DateFormat m_DateAndTimeFormat;

    /** date format for SQL for just Date conversion, ignores time part */
    private java.text.DateFormat m_DateFormat;

    /** mode to be used */
    protected int m_iMode;

    /** get the date mode part of the mode flag
     * 
     * @param iMode_p int defined as bitwise combination of DATE_MODE_... and DATE_ACCURACY_...
     * @return int defined with DATE_MODE_... 
     */
    protected static int getDateMode(int iMode_p)
    {
        return (iMode_p & 0x000F);
    }

    /** create a new operator
     * 
     * @param iMode_p int defined as DATE_MODE_...
     */
    public OwSearchSQLOperator(int iMode_p)
    {
        m_iMode = iMode_p;

        switch (getDateMode(iMode_p))
        {
            case DATE_MODE_MS_ACCESS:
                m_DateFormat = new java.text.SimpleDateFormat("yyyy.MM.dd");
                m_DateAndTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;

            case DATE_MODE_FNCM:
                // the date format of P8 uses a strange time zone format 
                // (see ecm_help.war/nav/dev_map_start.htm?../developer_help/dev_roadmap/sql_syntax_descriptions.htm)
                // and not the Date and Time Pattern of java (see javadoc of java.text.SimpleDateFormat)
                // so we use a special overridden version of the SimpleDateFormat that accepts advanced token: "Q"
                // for the required P8 date format.

                m_DateFormat = new SQLDateFormat("yyyy-MM-dd'Q'");
                m_DateAndTimeFormat = new SQLDateFormat("yyyy-MM-dd'T'HH:mm:ss'Q'");
                break;

            case DATE_MODE_MSSQL:
                m_DateFormat = new java.text.SimpleDateFormat("yyyy.MM.dd");
                m_DateAndTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;

            case DATE_MODE_ORACLE:
                m_DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                m_DateAndTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;

            case DATE_MODE_DB2:
                m_DateFormat = new java.text.SimpleDateFormat("yyyy.MM.dd");
                m_DateAndTimeFormat = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                break;

            case DATE_MODE_DEFAULT:
            default:
                m_DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                m_DateAndTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }
    }

    /** overwrite date format with a own one
     * 
     * @param dateAndTimeFormat_p date format for SQL Date and Time conversion
     * @param dateFormat_p date format for SQL for just Date conversion, ignores time part
     */
    public void setDateFormats(DateFormat dateAndTimeFormat_p, DateFormat dateFormat_p)
    {
        m_DateAndTimeFormat = dateAndTimeFormat_p;
        m_DateFormat = dateFormat_p;
    }

    /** singleton for the SQL operators */
    protected static final SqlOperatorSingleton m_SqlOperators = new SqlOperatorSingleton();

    /**
     *<p>
     * Singleton for the SQL operators.
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
    protected static class SqlOperatorSingleton
    {
        public Map m_OperatorMap = null;

        private SqlOperatorSingleton()
        {
            if (m_OperatorMap == null)
            {
                m_OperatorMap = new HashMap();

                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL), "=");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_EQUAL), "<>");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_LIKE), "like");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_LIKE), "not like");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER), ">");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS), "<");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL), ">=");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL), "<=");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_IN), "in");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_NOT_IN), "not in");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_ARRAY_IN), "in");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_ARRAY_NOT_IN), "not in");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_NULL), "is null");
                m_OperatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_IS_NOT_NULL), "is not null");
            }
        }
    }

    /** traverse the search criteria tree and generate a SQL Statement
     *
     * @param searchNode_p current search criteria tree node to traverse, null is allowed
     * @param w_p stream to write SQL Statement to
     *
     * @return true = valid sub criteria was inserted, false = sub criteria is empty or null and can be ignored
     */
    public boolean createSQLSearchCriteria(OwSearchNode searchNode_p, Writer w_p) throws Exception
    {
        if (searchNode_p == null)
        {
            return false;
        }

        List CriteriaList = searchNode_p.getChilds();
        if (CriteriaList != null)
        {
            boolean fValidSub = false;
            boolean fWroteBracked = false;
            // === search node contains children
            for (int i = 0; i < CriteriaList.size(); i++)
            {
                OwSearchNode Search = (OwSearchNode) CriteriaList.get(i);
                OwSearchCriteria criteria = Search.getCriteria();
                if (criteria == null || !OwSearchPathField.CLASS_NAME.equals(criteria.getClassName()))
                {
                    StringWriter subWriter = new StringWriter();

                    if (createSQLSearchCriteria(Search, subWriter))
                    {
                        if (CriteriaList.size() > 1 && i == 0 && searchNode_p.getOperator() != OwSearchNode.SEARCH_OP_UNDEF)
                        {
                            w_p.write(" (");
                            fWroteBracked = true;
                        }
                        // === add combination operator between child nodes
                        // was there a valid sub already ?
                        if (fValidSub)
                        {
                            switch (searchNode_p.getOperator())
                            {
                                case OwSearchNode.SEARCH_OP_AND:
                                    // write combination operator
                                    w_p.write("and ");
                                    break;

                                case OwSearchNode.SEARCH_OP_OR:
                                    // write combination operator
                                    w_p.write("or ");
                                    break;
                            }
                        }

                        // === insert statement of sub node
                        w_p.write(subWriter.toString());
                        // sub was valid, so this combination is valid too.
                        fValidSub = true;
                    }
                }
            }
            if (fWroteBracked)
            {
                w_p.write(") ");
            }
            return fValidSub;
        }
        else
        {
            // === search node does not contain children, it is just a criteria
            OwSearchCriteria criteria_p = searchNode_p.getCriteria();
            int operator = criteria_p.getOperator();
            if (operator == OwSearchNode.SEARCH_OP_UNDEF)
            {
                return false;
            }
            String strOpSQL = getOperatorSQL(criteria_p, criteria_p.getValue(), isOperatorFieldFirst(operator));
            if (strOpSQL == null)
            {
                return false;
            }

            w_p.write(strOpSQL);

            return true;
        }
    }

    /** check if given operator has its field first
     * 
     * @param iOP_p
     * @return boolean true = field first e.g.: MY_PROP = 'Hallo', false = field last e.g.: 'Hallo' in MY_PROP
     */
    protected boolean isOperatorFieldFirst(int iOP_p)
    {
        switch (iOP_p)
        {
            case OwSearchOperator.CRIT_OP_IS_ARRAY_NOT_IN:
            case OwSearchOperator.CRIT_OP_IS_ARRAY_IN:
                return false;

            default:
                return true;
        }
    }

    /** get a operator SQL Statement for the given criteria
     *
     * @param criteria_p OwSearchCriteria
     * @param value_p Object
     * @param fFieldFirst_p boolean true = insert the field first, false = insert the value first
     *
     * @return String or null if criteria is empty and can be ignored
     * */
    protected String getOperatorSQL(OwSearchCriteria criteria_p, Object value_p, boolean fFieldFirst_p) throws Exception
    {
        // check if criteria  is zero length. I.e. can be ignored.
        if (criteria_p == null)
        {
            // criteria is empty, ignore it.
            return null;
        }

        // null operators
        switch (criteria_p.getOperator())
        {
            case OwSearchOperator.CRIT_OP_IS_NULL:
            case OwSearchOperator.CRIT_OP_IS_NOT_NULL:
            {
                StringBuffer buf = new StringBuffer();
                buf.append(getCriteriaQueryName(criteria_p));
                buf.append(" ");
                buf.append(getSQLOperatorAsString(criteria_p.getOperator()));
                buf.append(" ");
                return buf.toString();
            }
        }

        // criteria is an array, value_p is an object array.
        if (criteria_p.isArray() && value_p instanceof Object[])
        {
            return getArrayOperatorSQL(criteria_p, value_p, fFieldFirst_p);
        }

        // is type 'date'. Must be checked before check range operator 
        if (criteria_p.isDateType())
        {
            return getDateOperatorSQL(criteria_p, value_p, fFieldFirst_p);
        }

        // is range operator
        if (criteria_p.isCriteriaOperatorRange())
        {
            return getRangeOperatorSQL(criteria_p, value_p, criteria_p.getSecondRangeCriteria().getValue(), fFieldFirst_p);
        }

        //all special cases are checked start to build 'normal' SQL operator

        // === check if criteria  is zero length. I.e. can be ignored.
        if ((value_p == null) || (value_p.toString().length() == 0))
        {
            // === criteria is empty, ignore it.
            return null;
        }

        switch (criteria_p.getOperator())
        {
            case OwSearchOperator.CRIT_OP_IS_IN:
            case OwSearchOperator.CRIT_OP_IS_NOT_IN:
            {
                StringTokenizer inTokens = new StringTokenizer(value_p.toString(), ",");
                if (!inTokens.hasMoreTokens())
                {
                    return null;
                }
                // === parse the in array string and create SQL values, then create new in array string
                StringBuffer buf = new StringBuffer();

                buf.append(getCriteriaQueryName(criteria_p));
                buf.append(" ");
                buf.append(getSQLOperatorAsString(criteria_p.getOperator()));
                buf.append(" (");
                while (inTokens.hasMoreTokens())
                {
                    buf.append(getSQLValueString(criteria_p, inTokens.nextToken(), m_iMode));

                    if (inTokens.hasMoreTokens())
                    {
                        buf.append(",");
                    }
                }
                buf.append(")");
                return buf.toString();
            }
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
            {
                //'not like' operator does not work directly. 
                StringBuffer buf = new StringBuffer();
                buf.append("not");
                buf.append(" (");
                buf.append(getSingleOperatorSQL(criteria_p, value_p, OwSearchOperator.CRIT_OP_LIKE, fFieldFirst_p));
                buf.append(") ");
                return buf.toString();
            }
        }

        // === single scalar query against one value
        return getSingleOperatorSQL(criteria_p, value_p, criteria_p.getOperator(), fFieldFirst_p);
    }

    /**
     * Builds an SQL operator of date types.
     * Checks if treat dates with ignore-date-time is set.
     * If operator is range operator, method will call it with special date parameters. 
     * 
     * @param criteria_p OwSearchCriteria
     * @param value_p Object
     * @param fFieldFirst_p boolean true = insert the field first, false = insert the value first
     * @return SQL operator as String or null if criteria is empty and can be ignored
     * @throws Exception
     */
    private String getDateOperatorSQL(OwSearchCriteria criteria_p, Object value_p, boolean fFieldFirst_p) throws Exception
    {
        // check if criteria  is zero length. I.e. can be ignored.
        if (criteria_p == null)
        {
            // === criteria is empty, ignore it.
            return null;
        }

        // check if criteria is type 'Date' otherwise throws exception
        if (!criteria_p.isType(Date.class))
        {
            String msg = "OwSearchSQLOperator.getDateOperatorSQL: SearchCriteria isn't type 'Date'.";
            LOG.error(msg);
            throw new OwInvalidOperationException(msg);
        }

        //SQL operator
        int iOperator_p = criteria_p.getOperator();
        // check if we should treat dates with ignore-date-time
        boolean ignoreTime = criteria_p.isIgnoreTime() && hasMode(DATE_MODE_TREAT_IGNORE_TIME_AS_RANGE);

        //is range operator, it is allowed that value_p == null.
        if (criteria_p.isCriteriaOperatorRange())
        {
            if (ignoreTime)
            {
                Date fromDate = (value_p != null) ? OwDateTimeUtil.setBeginOfDayTime((Date) value_p) : null;
                Date toDate = (criteria_p.getSecondRangeCriteria().getValue() != null) ? OwDateTimeUtil.setBeginOfNextDayTime((Date) criteria_p.getSecondRangeCriteria().getValue()) : null;
                return getRangeOperatorSQL(criteria_p, fromDate, toDate, fFieldFirst_p);
            }
            else
            {
                return getRangeOperatorSQL(criteria_p, value_p, criteria_p.getSecondRangeCriteria().getValue(), fFieldFirst_p);
            }
        }

        //check if value_p is null. I.e can be ignored.
        if (value_p == null)
        {
            return null;
        }

        //filter out illegal operators
        switch (iOperator_p)
        {
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
            case OwSearchOperator.CRIT_OP_LIKE:
                String msg = "OwSearchSQLOperator.getDateOperatorSQL: Invalid search operator for date: " + getSQLOperatorAsString((iOperator_p));
                LOG.error(msg);
                throw new OwInvalidOperationException(msg);
        }

        // check if we should treat dates with ignore-date-time as range
        if (ignoreTime)
        {
            switch (iOperator_p)
            {
                case OwSearchOperator.CRIT_OP_EQUAL:
                {
                    // greater equal begin of day AND less next day
                    Date beginOfDayTime = OwDateTimeUtil.setBeginOfDayTime((Date) value_p);
                    Date beginOfNextDayTime = OwDateTimeUtil.setBeginOfNextDayTime((Date) value_p);
                    String sFrom = getSingleOperatorSQL(criteria_p, beginOfDayTime, OwSearchOperator.CRIT_OP_GREATER_EQUAL, fFieldFirst_p);
                    String sTo = getSingleOperatorSQL(criteria_p, beginOfNextDayTime, OwSearchOperator.CRIT_OP_LESS, fFieldFirst_p);
                    return " (" + sFrom + " and " + sTo + ") ";
                }
                case OwSearchOperator.CRIT_OP_NOT_EQUAL:
                {
                    //less begin of day OR greater equal next day
                    Date beginOfDayTime = OwDateTimeUtil.setBeginOfDayTime((Date) value_p);
                    Date beginOfNextDayTime = OwDateTimeUtil.setBeginOfNextDayTime((Date) value_p);
                    String sLess = getSingleOperatorSQL(criteria_p, beginOfDayTime, OwSearchOperator.CRIT_OP_LESS, fFieldFirst_p);
                    String sGreaterEq = getSingleOperatorSQL(criteria_p, beginOfNextDayTime, OwSearchOperator.CRIT_OP_GREATER_EQUAL, fFieldFirst_p);
                    return " (" + sLess + " or " + sGreaterEq + ") ";
                }
                case OwSearchOperator.CRIT_OP_GREATER:
                {
                    //greater equal next day
                    Date beginOfNextDayTime = OwDateTimeUtil.setBeginOfNextDayTime((Date) value_p);
                    return getSingleOperatorSQL(criteria_p, beginOfNextDayTime, OwSearchOperator.CRIT_OP_GREATER_EQUAL, fFieldFirst_p);

                }
                case OwSearchOperator.CRIT_OP_LESS_EQUAL:
                {
                    //less next day
                    Date beginOfNextDayTime = OwDateTimeUtil.setBeginOfNextDayTime((Date) value_p);
                    return getSingleOperatorSQL(criteria_p, beginOfNextDayTime, OwSearchOperator.CRIT_OP_LESS, fFieldFirst_p);
                }
                case OwSearchOperator.CRIT_OP_IS_IN:
                {
                    StringTokenizer inTokens = new StringTokenizer(value_p.toString(), ",");
                    if (!inTokens.hasMoreTokens())
                    {
                        return null;
                    }

                    DateFormat format = m_DateFormat;
                    // === parse the in array string and create SQL values, then create new in array string
                    StringBuffer buf = new StringBuffer();

                    buf.append(getCriteriaQueryName(criteria_p));
                    buf.append(" ");
                    buf.append(getSQLOperatorAsString(criteria_p.getOperator()));
                    while (inTokens.hasMoreTokens())
                    {
                        Date tokenDate = format.parse(inTokens.nextToken());
                        // greater equal begin of day AND less next day
                        Date beginOfDayTime = OwDateTimeUtil.setBeginOfDayTime(tokenDate);
                        Date beginOfNextDayTime = OwDateTimeUtil.setBeginOfNextDayTime(tokenDate);
                        String sFrom = getSingleOperatorSQL(criteria_p, beginOfDayTime, OwSearchOperator.CRIT_OP_GREATER_EQUAL, fFieldFirst_p);
                        String sTo = getSingleOperatorSQL(criteria_p, beginOfNextDayTime, OwSearchOperator.CRIT_OP_LESS, fFieldFirst_p);
                        buf.append(" (" + sFrom + " and " + sTo + ") ");
                        if (inTokens.hasMoreTokens())
                        {
                            buf.append("or");
                        }
                    }
                    return buf.toString();
                }
                case OwSearchOperator.CRIT_OP_IS_NOT_IN:
                {
                    StringTokenizer inTokens = new StringTokenizer(value_p.toString(), ",");
                    if (!inTokens.hasMoreTokens())
                    {
                        return null;
                    }

                    DateFormat format = m_DateFormat;
                    // === parse the in array string and create SQL values, then create new in array string
                    StringBuffer buf = new StringBuffer();

                    buf.append(getCriteriaQueryName(criteria_p));
                    buf.append(" ");
                    buf.append(getSQLOperatorAsString(criteria_p.getOperator()));
                    while (inTokens.hasMoreTokens())
                    {
                        Date tokenDate = format.parse(inTokens.nextToken());
                        //less begin of day OR greater equal next day
                        Date beginOfDayTime = OwDateTimeUtil.setBeginOfDayTime(tokenDate);
                        Date beginOfNextDayTime = OwDateTimeUtil.setBeginOfNextDayTime(tokenDate);
                        String sLess = getSingleOperatorSQL(criteria_p, beginOfDayTime, OwSearchOperator.CRIT_OP_LESS, fFieldFirst_p);
                        String sGreaterEq = getSingleOperatorSQL(criteria_p, beginOfNextDayTime, OwSearchOperator.CRIT_OP_GREATER_EQUAL, fFieldFirst_p);
                        buf.append(" (" + sLess + " or " + sGreaterEq + ") ");
                        if (inTokens.hasMoreTokens())
                        {
                            buf.append("or");
                        }
                    }
                    return buf.toString();
                }
                default:
                {
                    Date beginOfDayTime = OwDateTimeUtil.setBeginOfDayTime((Date) value_p);
                    return getSingleOperatorSQL(criteria_p, beginOfDayTime, iOperator_p, fFieldFirst_p);
                }
            }
        }

        // date-time should not ignored
        if (iOperator_p == OwSearchOperator.CRIT_OP_IS_IN || iOperator_p == OwSearchOperator.CRIT_OP_IS_NOT_IN)
        {
            StringTokenizer inTokens = new StringTokenizer(value_p.toString(), ",");
            if (!inTokens.hasMoreTokens())
            {
                return null;
            }
            // === parse the in array string and create SQL values, then create new in array string
            StringBuffer buf = new StringBuffer();

            buf.append(getCriteriaQueryName(criteria_p));
            buf.append(" ");
            buf.append(getSQLOperatorAsString(criteria_p.getOperator()));
            buf.append(" (");
            while (inTokens.hasMoreTokens())
            {
                buf.append(getSQLValueString(criteria_p, inTokens.nextToken(), m_iMode));

                if (inTokens.hasMoreTokens())
                {
                    buf.append(",");
                }
            }
            buf.append(")");
            return buf.toString();
        }

        return getSingleOperatorSQL(criteria_p, value_p, iOperator_p, fFieldFirst_p);
    }

    /**
     * Builds a SQL range operator
     * 
     * @param criteria_p OwSearchCriteria
     * @param firstvalue_p from value of range operator
     * @param secondvalue_p to value of range operator
     * @param fFieldFirst_p boolean true = insert the field first, false = insert the value first
     * @return SQL operator as String or null if criteria is empty and can be ignored
     * @throws Exception
     */
    private String getRangeOperatorSQL(OwSearchCriteria criteria_p, Object firstvalue_p, Object secondvalue_p, boolean fFieldFirst_p) throws Exception
    {
        // check if criteria  is zero length. I.e. can be ignored.
        if (criteria_p == null)
        {
            // === criteria is empty, ignore it.
            return null;
        }
        // check if criteria operator is range operator otherwise throws exception
        if (!criteria_p.isCriteriaOperatorRange())
        {
            String msg = "OwSearchSQLOperator.getRangeOperatorSQL: SearchCriteria operator isn't a range operator.";
            LOG.error(msg);
            throw new OwInvalidOperationException(msg);
        }

        // check if both range values are null. I.e. can be ignored
        if (firstvalue_p == null && secondvalue_p == null)
        {
            // both range values are null, ignore it.
            return null;
        }

        int iOperator_p = criteria_p.getOperator();

        switch (iOperator_p)
        {
            case OwSearchOperator.CRIT_OP_BETWEEN:
            {
                String sFrom = getSingleOperatorSQL(criteria_p, firstvalue_p, OwSearchOperator.CRIT_OP_GREATER_EQUAL, fFieldFirst_p);
                String sTo = getSingleOperatorSQL(criteria_p, secondvalue_p, OwSearchOperator.CRIT_OP_LESS, fFieldFirst_p);
                if (sFrom == null)
                {
                    return sTo;
                }

                if (sTo == null)
                {
                    return sFrom;
                }

                return " (" + sFrom + " and " + sTo + ") ";
            }
            case OwSearchOperator.CRIT_OP_NOT_BETWEEN:
            {

                String sFrom = getSingleOperatorSQL(criteria_p, firstvalue_p, OwSearchOperator.CRIT_OP_LESS, fFieldFirst_p);
                String sTo = getSingleOperatorSQL(criteria_p, secondvalue_p, OwSearchOperator.CRIT_OP_GREATER_EQUAL, fFieldFirst_p);
                if (sFrom == null)
                {
                    return sTo;
                }

                if (sTo == null)
                {
                    return sFrom;
                }

                return " (" + sFrom + " or " + sTo + ") ";
            }
            default:
            {
                String msg = "OwSearchSQLOperator.getRangeOperatorSQL: Invalid range operator: " + criteria_p.getOperator();
                LOG.error(msg);
                throw new OwInvalidOperationException(msg);
            }
        }
    }

    /**
     * Builds an SQL operator of an given array.
     * Arrays contain a set of single SQL operators connected with 'or' or 'and'.
     * For every single SQL operator the {@link #getOperatorSQL(OwSearchCriteria, Object, boolean)}
     * method will be called.
     * 
     * @param criteria_p OwSearchCriteria
     * @param value_p Object
     * @param fFieldFirst_p boolean true = insert the field first, false = insert the value first
     * @return SQL operator as String or null if criteria is empty and can be ignored.
     * @throws Exception
     */
    private String getArrayOperatorSQL(OwSearchCriteria criteria_p, Object value_p, boolean fFieldFirst_p) throws Exception
    {
        //check if criteria  is zero length or value is null
        if (criteria_p == null || value_p == null)
        {
            //ignore criteria
            return null;
        }
        if (!criteria_p.isArray())
        {
            String msg = "OwSearchSQLOperator.getArrayOperatorSQL: SearchCriteria operator isn't an array.";
            LOG.error(msg);
            throw new OwInvalidOperationException(msg);
        }
        if (!(value_p instanceof Object[]))
        {
            String msg = "OwSearchSQLOperator.getArrayOperatorSQL: SearchCriteria operator is not instanceof Object[].";
            LOG.error(msg);
            throw new OwInvalidOperationException(msg);
        }

        StringBuffer buf = new StringBuffer();
        Object[] values = (Object[]) value_p;
        boolean fWroteOne = false;
        for (int i = 0; i < values.length; i++)
        {
            String strOpSWL = getOperatorSQL(criteria_p, values[i], fFieldFirst_p);
            if (strOpSWL == null)
            {
                continue;
            }

            if (fWroteOne)
            {
                if (OwSearchOperator.isCriteriaOperatorNot(criteria_p.getOperator()))
                {
                    buf.append(" and ");
                }
                else
                {
                    buf.append(" or ");
                }
            }

            buf.append(strOpSWL);

            fWroteOne = true;
        }

        if (fWroteOne)
        {
            return buf.toString();
        }

        return null;
    }

    /** check if mode is set
     * 
     * @param mode_p
     * @return a boolean
     */
    private boolean hasMode(int mode_p)
    {
        return ((m_iMode & mode_p) > 0);
    }

    /** get a single operator SQL Statement for the given criteria_p
    *
    * @param criteria_p OwSearchCriteria
    * @param value_p Object
    * @param fFieldFirst_p boolean true = insert the field first, false = insert the value first
    *
    * @return String or null if criteria is empty and can be ignored
    * */
    protected String getSingleOperatorSQL(OwSearchCriteria criteria_p, Object value_p, int iOperator_p, boolean fFieldFirst_p) throws Exception
    {
        // === check if criteria  is zero length. I.e. can be ignored.
        if ((value_p == null) || (value_p.toString().length() == 0))
        {
            // === criteria is empty, ignore it.
            return null;
        }

        String strValueString = getSQLValueString(criteria_p, value_p, m_iMode);

        StringBuffer buf = new StringBuffer();

        if (fFieldFirst_p)
        {
            buf.append(getCriteriaQueryName(criteria_p));
            buf.append(" ");
            buf.append(getSQLOperatorAsString(iOperator_p));
            buf.append(" ");
            buf.append(strValueString);
            buf.append(" ");
        }
        else
        {
            buf.append(strValueString);
            buf.append(" ");
            buf.append(getSQLOperatorAsString(iOperator_p));
            buf.append(" ");
            buf.append(getCriteriaQueryName(criteria_p));
            buf.append(" ");
        }

        return buf.toString();
    }

    /** get the value of the criteria and convert it to a SQL conform string
     * @param criteria_p the criteria to convert
     * @param iDateMode_p int Date mode used to convert date types as defined with DATE_MODE_...
     *
     * @return String with SQL conform representation of value
     */
    protected String getSQLValueString(OwSearchCriteria criteria_p, Object value_p, int iDateMode_p) throws Exception
    {
        StringBuffer ret = new StringBuffer();

        // try to cast the value to a date. if this fails, detect the field type by the JavaClassName
        try
        {
            // try to cast to a date
            Date dateValue = (Date) value_p;

            DateFormat format = m_DateAndTimeFormat;

            if (criteria_p.isIgnoreTime())
            {
                format = m_DateFormat;
            }

            // no ClassCastException until here: try to handle as a date
            switch (getDateMode(iDateMode_p))
            {
                case DATE_MODE_FNIM:
                {
                    // return number of days since 1.1.1970
                    ret.append(String.valueOf(((Date) value_p).getTime() / 86400000 + 1));
                }
                    break;

                case DATE_MODE_MS_ACCESS:
                {
                    // return ms access conform date criteria
                    ret.append(" datevalue('");
                    ret.append(format.format(dateValue));
                    ret.append("') ");
                }
                    break;

                case DATE_MODE_MSSQL:
                {
                    if (criteria_p.isIgnoreTime())
                    {
                        ret.append(" CONVERT(datetime,'");
                        ret.append(format.format(dateValue));
                        ret.append("',102) ");
                    }
                    else
                    {
                        ret.append(" CONVERT(datetime,'");
                        ret.append(format.format(dateValue));
                        ret.append("',120) ");
                    }

                }
                    break;

                case DATE_MODE_ORACLE:
                {
                    // return oracle conform date criteria
                    ret.append(" to_date('");
                    ret.append(format.format(dateValue));
                    ret.append("','yyyy-MM-dd HH24:MI:SS')");
                }
                    break;

                case DATE_MODE_DB2:
                {
                    // return db2 conform date criteria
                    ret.append(" TIMESTAMP_FORMAT('");
                    ret.append(format.format(dateValue));
                    ret.append("','dd.MM.yyyy HH:mm:ss')");
                }
                    break;

                default:
                {
                    // return standard type
                    ret.append(format.format(dateValue));
                }
                    break;
            }

            // return the result
            return ret.toString();
        }
        catch (ClassCastException cce)
        {
            // ignore this ClassCastException and just fall through.
            // we will detect the correct property type by the JavaClassName
        }

        if (isStringLiteral(criteria_p))
        {
            // === return string
            ret.append("'");
            ret.append(getStringValue(criteria_p, convertWildCards(criteria_p, value_p.toString())));
            ret.append("'");
        }
        else
        {
            // === default return value
            ret.append(convertWildCards(criteria_p, value_p.toString()));
        }

        return ret.toString();
    }

    /** (overridable) check if given criteria is a string literal and needs quotation marks
     * 
     * @param criteria_p
     * @return a boolean
     * @throws Exception 
     */
    protected boolean isStringLiteral(OwSearchCriteria criteria_p) throws Exception
    {
        return criteria_p.getOriginalJavaClassName().equals("java.lang.String");
    }

    /** prevent the usage of " or ' in SQL query
     * 
     * @param criteria_p OwSearchCriteria
     * @param value_p String
     * @return String 
     */
    protected String getStringValue(OwSearchCriteria criteria_p, String value_p)
    {
        value_p = OwString.replaceAll(value_p, "\'", "\\\'");

        return value_p;
    }

    /** convert the wildcards to client format
     * 
     * @param criteria_p
     * @param value_p
     */
    protected String convertWildCards(OwSearchCriteria criteria_p, String value_p)
    {
        Collection wdefs = criteria_p.getWildCardDefinitions();
        if (null == wdefs)
        {
            return value_p;
        }

        Iterator it = wdefs.iterator();
        while (it.hasNext())
        {
            OwWildCardDefinition def = (OwWildCardDefinition) it.next();

            // replace client wildcard with native wildcard
            value_p = OwString.replaceAll(value_p, def.getWildCard(), def.getNativeWildCard());
        }

        return value_p;
    }

    /**
     * Get the String representation of the operator.
     * @param operator_p int OwSearchOperator.CRIT_...
     * @return String representing of SQL operator
     * @since 2.5.3.0
     */
    protected String getSQLOperatorAsString(int operator_p)
    {
        return m_SqlOperators.m_OperatorMap.get(Integer.valueOf(operator_p)).toString();
    }

    /**
     * (overridable) Method to get the property name, which is used for query statements.<br />
     * This method can be overwritten if the <i>field definition name</i> and 
     * <i>field definition <b>query</b> name</i> are not corresponding.
     * <p>By default this method returns <code>criteria_p.getFieldDefinition().getClassName()</code>.</p>
     * @param criteria_p OwSearchCriteria
     * @return String name to be used for querying
     * @throws OwException if can not retrieve field definition information
     * @since 3.0.0.0
     */
    protected String getCriteriaQueryName(OwSearchCriteria criteria_p) throws OwException
    {
        try
        {
            return criteria_p.getFieldDefinition().getClassName();
        }
        catch (Exception e)
        {
            LOG.debug("OwSearchSQLOperator.getCriteriaQueryName:Cannot retrieve field definition class name", e);
            throw new OwServerException(e.getMessage(), e);
        }
    }
}