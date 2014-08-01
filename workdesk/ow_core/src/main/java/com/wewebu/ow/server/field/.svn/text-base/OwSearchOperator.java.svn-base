package com.wewebu.ow.server.field;

import java.util.HashMap;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Manages the search operators.
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
public class OwSearchOperator
{

    // === NOTE: NOT operators are odd, all others are even
    // === standard operators
    /** search criteria operator undefined */
    public static final int CRIT_OP_UNDEF = 0x0002;
    /** search criteria operator */
    public static final int CRIT_OP_EQUAL = 0x0004;
    /** search criteria operator */
    public static final int CRIT_OP_NOT_EQUAL = 0x0005;
    /** search criteria operator */
    public static final int CRIT_OP_LIKE = 0x0006;
    /** search criteria operator */
    public static final int CRIT_OP_NOT_LIKE = 0x0007;
    /** search criteria operator */
    public static final int CRIT_OP_GREATER = 0x0008;
    /** search criteria operator */
    public static final int CRIT_OP_LESS = 0x000A;
    /** search criteria operator */
    public static final int CRIT_OP_GREATER_EQUAL = 0x000C;
    /** search criteria operator */
    public static final int CRIT_OP_LESS_EQUAL = 0x000E;
    /** search criteria operator */
    public static final int CRIT_OP_IS_NULL = 0x0010;
    /** search criteria operator */
    public static final int CRIT_OP_IS_NOT_NULL = 0x0011;

    // === in operators for column separated array criteria against properties e.g. ...where (MySkalar in ('Bernd','Peter','Hans'))...
    /** search criteria operator */
    public static final int CRIT_OP_IS_IN = 0x0112;
    /** search criteria operator */
    public static final int CRIT_OP_IS_NOT_IN = 0x0113;

    // === in operators for criteria against array properties e.g. ...where ('Bernd' in MyArray)...
    /** search criteria operator */
    public static final int CRIT_OP_IS_ARRAY_IN = 0x0114;
    /** search criteria operator */
    public static final int CRIT_OP_IS_ARRAY_NOT_IN = 0x0115;

    //  === range operators
    /** search criteria operator */
    public static final int CRIT_OP_BETWEEN = 0x0212;
    /** search criteria operator */
    public static final int CRIT_OP_NOT_BETWEEN = 0x0213;

    // === CBR operators
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_NONE = 0x0302;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_ALL = 0x0304;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_ANY = 0x0306;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_IN = 0x0308;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_ZONE = 0x030A;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_NEAR = 0x030C;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_PARAGRAPH = 0x030E;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_SENTENCE = 0x0310;
    /** search criteria context based retrieval operator */
    public static final int CRIT_OP_CBR_VQL = 0x0312;

    // === merge type operators
    /** merge type to be used */
    public static final int MERGE_NONE = 0x0401;
    /** merge type to be used, the meaning depends on the used ECM System */
    public static final int MERGE_UNION = 0x0402;
    /** merge type to be used, the meaning depends on the used ECM System */
    public static final int MERGE_INTERSECT = 0x0403;

    /** check if operator is a "not" operator
     * @param op_p criteria operator as defined with CRIT_...
     * @return true if not, false otherwise
     * */
    public static boolean isCriteriaOperatorNot(int op_p)
    {
        // check for odd value
        return ((op_p & 0x001) > 0);
    }

    /** check if operator is a range operator with a second criteria
     * @param op_p criteria operator as defined with CRIT_...
     * @return true if range operator, false otherwise
     * */
    public static boolean isCriteriaOperatorRange(int op_p)
    {
        return ((op_p == CRIT_OP_BETWEEN) || (op_p == CRIT_OP_NOT_BETWEEN));
    }

    /** singleton with the operator maps */
    protected static final OperatorMapSingleton m_maps = new OperatorMapSingleton();

    /**
     *<p>
     * Singleton with the operator maps.
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
    protected static class OperatorMapSingleton
    {
        /** map which contains search operator display string mappings */
        public HashMap m_stringOperatorMap = null;

        /** map which contains search operator display symbol mappings */
        public HashMap m_symbolOperatorMap = null;

        /** map which contains search operator display symbol mappings inverse*/
        public HashMap m_symbolInverseOperatorMap = null;

        private OperatorMapSingleton()
        {
            //      === init static operator maps
            if (m_stringOperatorMap == null)
            {
                m_stringOperatorMap = new HashMap();

                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_EQUAL), new OwString("field.OwSearchOperator.equal", "is equal"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_NOT_EQUAL), new OwString("field.OwSearchOperator.notequal", "is not equal"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_LIKE), new OwString("field.OwSearchOperator.like", "is like"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_NOT_LIKE), new OwString("field.OwSearchOperator.notlike", "is not like"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_GREATER), new OwString("field.OwSearchOperator.greater", "greater than"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_LESS), new OwString("field.OwSearchOperator.less", "less than"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_GREATER_EQUAL), new OwString("field.OwSearchOperator.greaterequal", "greater or equal"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_LESS_EQUAL), new OwString("field.OwSearchOperator.lessequal", "less or equal"));

                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_BETWEEN), new OwString("field.OwSearchOperator.between", "between"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_NOT_BETWEEN), new OwString("field.OwSearchOperator.notbetween", "not between"));

                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_IS_NULL), new OwString("field.OwSearchOperator.isnull", "is null"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_IS_NOT_NULL), new OwString("field.OwSearchOperator.isnotnull", "is not null"));

                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_IS_IN), new OwString("field.OwSearchOperator.isin", "is in"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_IS_NOT_IN), new OwString("field.OwSearchOperator.isnotin", "is not in"));

                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_IS_ARRAY_IN), new OwString("field.OwSearchOperator.isarrayin", "is within"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_IS_ARRAY_NOT_IN), new OwString("field.OwSearchOperator.isarraynotin", "is not within"));

                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_NONE), new OwString("field.OwSearchOperator.cbrnone", "find words"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_ALL), new OwString("field.OwSearchOperator.cbrall", "find all words"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_ANY), new OwString("field.OwSearchOperator.cbrany", "find any word"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_IN), new OwString("field.OwSearchOperator.cbrin", "find words within"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_ZONE), new OwString("field.OwSearchOperator.cbrzone", "find within zone"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_NEAR), new OwString("field.OwSearchOperator.cbrnear", "find similar words"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_PARAGRAPH), new OwString("field.OwSearchOperator.cbrparagraph", "find in paragraph"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_SENTENCE), new OwString("field.OwSearchOperator.cbrsentence", "find in sentence"));
                m_stringOperatorMap.put(Integer.valueOf(CRIT_OP_CBR_VQL), new OwString("field.OwSearchOperator.cbrvql", "VQL Search"));

                m_stringOperatorMap.put(Integer.valueOf(MERGE_NONE), new OwString("field.OwSearchOperator.mergenone", "is"));
                m_stringOperatorMap.put(Integer.valueOf(MERGE_UNION), new OwString("field.OwSearchOperator.mergeunion", "is sum of"));
                m_stringOperatorMap.put(Integer.valueOf(MERGE_INTERSECT), new OwString("field.OwSearchOperator.mergeintersect", "is subset of"));
            }

            if (m_symbolOperatorMap == null)
            {
                m_symbolOperatorMap = new HashMap();

                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_EQUAL), "=");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_NOT_EQUAL), "<>");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_LIKE), "LIKE");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_NOT_LIKE), "NOT LIKE");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_GREATER), ">");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_LESS), "<");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_GREATER_EQUAL), ">=");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_LESS_EQUAL), "<=");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_IS_NULL), "IS NULL");
                m_symbolOperatorMap.put(Integer.valueOf(CRIT_OP_IS_NOT_NULL), "IS NOT NULL");
            }

            if (m_symbolInverseOperatorMap == null)
            {
                m_symbolInverseOperatorMap = new HashMap();

                m_symbolInverseOperatorMap.put("=", Integer.valueOf(CRIT_OP_EQUAL));
                m_symbolInverseOperatorMap.put("<>", Integer.valueOf(CRIT_OP_NOT_EQUAL));
                m_symbolInverseOperatorMap.put("LIKE", Integer.valueOf(CRIT_OP_LIKE));
                m_symbolInverseOperatorMap.put("NOT LIKE", Integer.valueOf(CRIT_OP_NOT_LIKE));
                m_symbolInverseOperatorMap.put(">", Integer.valueOf(CRIT_OP_GREATER));
                m_symbolInverseOperatorMap.put("<", Integer.valueOf(CRIT_OP_LESS));
                m_symbolInverseOperatorMap.put(">=", Integer.valueOf(CRIT_OP_GREATER_EQUAL));
                m_symbolInverseOperatorMap.put("<=", Integer.valueOf(CRIT_OP_LESS_EQUAL));
                m_symbolInverseOperatorMap.put("IS NULL", Integer.valueOf(CRIT_OP_IS_NULL));
                m_symbolInverseOperatorMap.put("IS NOT NULL", Integer.valueOf(CRIT_OP_IS_NOT_NULL));
            }
        }
    }

    /** get a display string for the given operator
     * 
     * @param locale_p
     * @param iOP_p
     * @return String
     */
    public static String getOperatorDisplayString(java.util.Locale locale_p, int iOP_p)
    {
        OwString str = (OwString) m_maps.m_stringOperatorMap.get(Integer.valueOf(iOP_p));
        if (null == str)
        {
            return null;
        }
        else
        {
            return str.getString(locale_p);
        }
    }

    /** get a display symbol for the given operator
     * 
     * @param locale_p
     * @param iOP_p
     * @return String
     */
    public static String getOperatorDisplaySymbol(java.util.Locale locale_p, int iOP_p)
    {
        return (String) m_maps.m_symbolOperatorMap.get(Integer.valueOf(iOP_p));
    }

    /**
     * @return Returns the m_stringOperatorMap.
     */
    /*public static HashMap getStringOperatorMap()
    {
        return m_maps.m_stringOperatorMap;
    }*/

    /**
     * @return Returns the m_symbolOperatorMap.
     */
    public static HashMap getSymbolOperatorMap()
    {
        return m_maps.m_symbolOperatorMap;
    }

    /**
     * @return Returns the m_symbolOperatorMap.
     */
    public static HashMap getSymbolInverseOperatorMap()
    {
        return m_maps.m_symbolInverseOperatorMap;
    }

}