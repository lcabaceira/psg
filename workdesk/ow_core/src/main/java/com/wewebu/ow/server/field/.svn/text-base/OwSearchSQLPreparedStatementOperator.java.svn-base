package com.wewebu.ow.server.field;

import java.util.ArrayList;

/**
 *<p>
 * Operator to create SQL out of a OwSearchNode Tree using prepared statements.
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
public class OwSearchSQLPreparedStatementOperator extends OwSearchSQLOperator
{

    /** ArrayList to collect all parameter values */
    protected ArrayList m_parameterBindings = new ArrayList();

    /**
     * Default constructor
     */
    public OwSearchSQLPreparedStatementOperator()
    {
        super(DATE_MODE_DEFAULT);
    }

    /**
     * override the default behavior to insert ? instead of real values. 
     * The values are all collected in the m_parameterBindings array.
     */
    protected String getSQLValueString(OwSearchCriteria criteria_p, Object value_p, int dateMode_p) throws Exception
    {
        m_parameterBindings.add(value_p);
        return ("?");
    }

    /**
     * Get an array with real values for each ? in the query string
     * @return Object[] array of values for each ? in the query string
     */
    public Object[] getParameterBindings()
    {
        return (m_parameterBindings.toArray());
    }
}