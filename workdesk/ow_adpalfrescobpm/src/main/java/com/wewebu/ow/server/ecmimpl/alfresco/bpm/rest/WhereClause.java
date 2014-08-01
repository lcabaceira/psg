package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverterFactory;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwDateTimeUtil;

/**
 *<p>
 * Models a WHERE clause to be used with the Alfresco Workflow Public Rest API.
 *</p>
 *<p>
 * "The where parameter can be used to get processes/tasks that meet certain criteria. The where
 * parameter takes, as a value, a single predicate that includes one or more conditions logically
 * connected by AND."
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
 *@since 4.2.0.0.0
 */
public class WhereClause
{
    private static final Logger LOG = OwLogCore.getLogger(WhereClause.class);

    private List<Criterion> criteria = new LinkedList<WhereClause.Criterion>();

    protected WhereClause()
    {

    }

    public static WhereClause emptyClause()
    {
        WhereClause clause = new WhereClause();
        return clause;
    }

    public static WhereClause fromOwSearchNode(OwSearchNode node, NativeValueConverterFactory valueConverterFactory) throws OwRestException
    {
        WhereClause clause = new WhereClause();

        try
        {
            if (node.getOperator() != OwSearchNode.SEARCH_OP_AND)
            {
                throw new OwRestException("Only simple filters combined with the AND operator are supported for the moment.");
            }

            for (Object objSearchNode : node.getChilds())
            {
                OwSearchNode searchNode = (OwSearchNode) objSearchNode;
                OwSearchCriteria criteria = searchNode.getCriteria();
                OwFieldDefinition fieldDefinition = criteria.getFieldDefinition();

                String variableName = CMISPropertyAdapter.variableNameForProperty(fieldDefinition);
                int operator = criteria.getOperator();
                Object value = criteria.getValue();

                if (variableName.endsWith("bpm_dueDate"))
                {

                    Date dateValue = (Date) value;
                    if (null != dateValue)
                    {
                        boolean ignoreTime = criteria.isIgnoreTime();
                        String queryName = "variables/global/" + variableName;
                        if (OwSearchOperator.CRIT_OP_EQUAL == operator)
                        {
                            // (... = ...) is the same as (... <= ... AND ... >= ...)
                            Criterion criterion1 = criterionFor(queryName, "d:date", OwSearchOperator.CRIT_OP_GREATER_EQUAL, dateValue, fieldDefinition, valueConverterFactory, ignoreTime);
                            Criterion criterion2 = criterionFor(queryName, "d:date", OwSearchOperator.CRIT_OP_LESS_EQUAL, dateValue, fieldDefinition, valueConverterFactory, ignoreTime);
                            clause.addCriterion(criterion1);
                            clause.addCriterion(criterion2);
                        }
                        else
                        {
                            Criterion criterion = criterionFor(queryName, "d:date", operator, dateValue, fieldDefinition, valueConverterFactory, ignoreTime);
                            clause.addCriterion(criterion);
                        }
                    }
                }
            }
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwRestException("Could not process search filter.", e);
        }

        LOG.info("New where clause created from search node: " + clause.toString());
        return clause;
    }

    private static Criterion criterionFor(String queryName, String type, int operator, Object value, OwFieldDefinition fieldDefinition, NativeValueConverterFactory valueConverterFactory, boolean ignoreTime) throws OwException
    {
        Object adjustedValue = value;
        if (value instanceof Date)
        {
            adjustedValue = adjustDateLimits((Date) value, operator, ignoreTime);
        }

        NativeValueConverter converter = valueConverterFactory.converterFor(fieldDefinition);
        String stringValue = (String) converter.fromJava(adjustedValue);

        String stringOperator = OwSearchOperator.getOperatorDisplaySymbol(null, operator);
        return new Criterion(queryName, type, stringOperator, stringValue);
    }

    private static Date adjustDateLimits(Date value, int operator, boolean ignoreTime)
    {
        Date dateValue = value;
        if (ignoreTime)
        {
            if (OwSearchOperator.CRIT_OP_LESS_EQUAL == operator || OwSearchOperator.CRIT_OP_GREATER == operator)
            {
                dateValue = OwDateTimeUtil.setBeginOfNextDayTime(value);
            }
            else if (OwSearchOperator.CRIT_OP_GREATER_EQUAL == operator || OwSearchOperator.CRIT_OP_LESS == operator)
            {
                dateValue = OwDateTimeUtil.setBeginOfDayTime(value);
            }
        }
        return dateValue;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        Iterator<Criterion> it = this.criteria.iterator();
        while (it.hasNext())
        {
            Criterion criterion = it.next();
            sb.append(criterion.toString());
            if (it.hasNext())
            {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }

    public void addCriterion(Criterion criterion)
    {
        this.criteria.add(criterion);
    }

    /**
     *<p>
     * Criterion.
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
     *@since 4.2.0.0.0
     */
    public static class Criterion
    {
        private String variableName;
        private String variableType;
        private String operator;
        private String value;

        public Criterion(String variableName, String variableType, String operator, String value)
        {
            this.variableName = variableName;
            this.variableType = variableType;
            this.operator = operator;
            this.value = value;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            if (null != variableType)
            {
                return String.format("%s %s '%s %s'", variableName, operator, variableType, value);
            }
            else
            {
                return String.format("%s %s '%s'", variableName, operator, value);
            }
        }
    }
}