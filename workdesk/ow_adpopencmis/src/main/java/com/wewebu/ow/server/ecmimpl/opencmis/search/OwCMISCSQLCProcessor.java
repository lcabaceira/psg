package com.wewebu.ow.server.ecmimpl.opencmis.search;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.OwCSQLCException;
import com.wewebu.ow.csqlc.OwCSQLCProcessor;
import com.wewebu.ow.csqlc.OwSQLEntitiesResolver;
import com.wewebu.ow.csqlc.ast.OwCharacterStringLiteral;
import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwComparisonOperator;
import com.wewebu.ow.csqlc.ast.OwCorrelatedTableName;
import com.wewebu.ow.csqlc.ast.OwFolderPredicateFormat;
import com.wewebu.ow.csqlc.ast.OwFunctionFolderFormat;
import com.wewebu.ow.csqlc.ast.OwJoinSpecification;
import com.wewebu.ow.csqlc.ast.OwPredicateFormat;
import com.wewebu.ow.csqlc.ast.OwSQLDateTimeLiteral;
import com.wewebu.ow.csqlc.ast.OwTextSearchPredicate;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.util.OwDateTimeUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * An {@link com.wewebu.ow.server.field.OwSearchNode OwSearchNode} + {@link com.wewebu.ow.server.field.OwSort OwSort}
 * to CMIS SQL abstract syntax tree conversion class.
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
public class OwCMISCSQLCProcessor extends OwCSQLCProcessor
{
    private static final Logger LOG = OwLog.getLogger(OwCMISCSQLCProcessor.class);

    private static final OwFolderPredicateFormat FOLDER_FORMAT = new OwFunctionFolderFormat("IN_FOLDER", "IN_TREE");
    private static final OwPredicateFormat IN_FORMAT = new OwPredicateFormat(null, null, "IN", "NOT IN", null, null);
    private static final OwPredicateFormat QUANTIIFED_IN_FORMAT = new OwPredicateFormat("ANY", "ANY", "IN", "NOT IN", null, null);
    private static final OwPredicateFormat QUANTIIFED_COPMARISON_FORMAT = new OwPredicateFormat(null, "NOT", "= ANY", "= ANY", null, null);
    private static final OwPredicateFormat LIKE_FORMAT = new OwPredicateFormat(null, null, "LIKE", "NOT LIKE", null, null);

    public OwCMISCSQLCProcessor(OwSQLEntitiesResolver entitiesResolver_p)
    {
        super(entitiesResolver_p);
    }

    protected OwColumnQualifier createFromTableQualifier(String tableName_p, String typeName_p)
    {
        return new OwColumnQualifier(tableName_p, typeName_p);
    }

    protected OwColumnQualifier createColumnQualifier(String propertyName_p, String repositoryID_p) throws OwException
    {
        int dotIndex = propertyName_p.indexOf('.');
        String tableNamePart = null;
        if (dotIndex != -1)
        {
            tableNamePart = propertyName_p.substring(0, dotIndex);
        }
        else
        {
            tableNamePart = propertyName_p;
        }
        String tableName = m_entitiesResolver.resolveQueryTableName(tableNamePart, repositoryID_p);
        if (tableName != null)
        {
            return new OwColumnQualifier(tableName, tableNamePart);
        }
        else
        {
            return null;
        }
    }

    protected OwColumnQualifier createQueryColumnQualifier(String tableName_p, String typeName_p)
    {
        return new OwColumnQualifier(tableName_p, typeName_p);
    }

    protected OwColumnQualifier createQueryFieldQualifier(OwFieldDefinition fieldDefinition_p, OwProcessContext context_p) throws OwException
    {
        String fieldClassName = fieldDefinition_p.getClassName();

        int dotIndex = fieldClassName.indexOf('.');
        String tableNamePart = null;
        if (dotIndex != -1)
        {
            tableNamePart = fieldClassName.substring(0, dotIndex);
        }
        else
        {
            tableNamePart = fieldClassName;
        }

        String tableName = m_entitiesResolver.resolveQueryTableName(tableNamePart, context_p.repositoryID);

        if (tableName == null)
        {
            String message = "The filed definition " + fieldClassName + " is not qualifiable! ";
            LOG.error("OwCMISSearchNodeSQLProcessor.createQueryFieldQualifier():" + message);
            throw new OwInvalidOperationException(new OwString1("opencmis.search.OwCMISCSQLCProcessor.err.non.queryable.field", "Invalid search field. The field %1 can not be queried.", fieldClassName));
        }
        else
        {
            return new OwColumnQualifier(tableName, tableNamePart);
        }

    }

    @Override
    protected OwSQLDateTimeLiteral createLiteral(OwSearchCriteria criteria_p, Date date_p) throws OwCSQLCException
    {
        return new OwCMISSQLDateTimeLiteral(convertDate(date_p));
    }

    /**
     * Convert Date into specific XML representation.
     * Also a conversion into specific time zone is done.
     * @param definedDate_p Date
     * @return XMLGregorianCalendar
     * @throws OwCSQLCException
     * @since 4.1.0.0
     */
    protected XMLGregorianCalendar convertDate(Date definedDate_p) throws OwCSQLCException
    {
        TimeZone from = m_entitiesResolver.getNetworkContext().getClientTimeZone();
        TimeZone to = TimeZone.getTimeZone("UTC");
        try
        {
            return OwDateTimeUtil.convertToXMLGregorianCalendar(definedDate_p, from, to);
        }
        catch (DatatypeConfigurationException e)
        {
            LOG.warn("Could not convert into XMLGregorianCalendar", e);
            throw new OwCSQLCException("XMLGregorianCalendar error.", e);
        }
    }

    @Override
    protected String createJoinColumnName(OwColumnQualifier qualifier_p)
    {
        return "cmis:objectId";
    }

    @Override
    protected OwPredicateFormat getInFormat()
    {
        return IN_FORMAT;
    }

    @Override
    protected OwPredicateFormat getQuantifiedInFormat()
    {
        return QUANTIIFED_IN_FORMAT;
    }

    @Override
    protected OwPredicateFormat getQuantifiedComparisonFormat()
    {
        return QUANTIIFED_COPMARISON_FORMAT;
    }

    @Override
    protected OwFolderPredicateFormat getFolderPredicateFormat()
    {
        return FOLDER_FORMAT;
    }

    @Override
    protected OwTextSearchPredicate createTextSearchPredicate(OwCharacterStringLiteral searchExpression_p, OwColumnReference columnReference_p) throws OwException
    {
        if (columnReference_p != null)
        {
            throw new OwInvalidOperationException("Property based text search predicates are not supported.");
        }
        else
        {
            return new OwTextSearchPredicate(searchExpression_p, null, new OwColumnQualifier(), null);
        }
    }

    @Override
    protected String joinType(OwCorrelatedTableName joinedTable_p)
    {
        return OwJoinSpecification.DEFAULT;
    }

    @Override
    protected OwPredicateFormat createLikeFormat()
    {
        return LIKE_FORMAT;
    }

    @Override
    protected String escapeNativeWildCardRepresentation(String value_p, Collection<OwWildCardDefinition> wildCardDefs)
    {
        Iterator it = wildCardDefs.iterator();
        while (it.hasNext())
        {
            OwWildCardDefinition def = (OwWildCardDefinition) it.next();

            // replace client wildcard with native wildcard
            value_p = OwString.replaceAll(value_p, def.getNativeWildCard(), "\\" + def.getNativeWildCard());
        }
        return value_p;
    }

    @Override
    protected boolean isMultipleTextSearchSyntax()
    {
        return false;
    }

    protected RangeLimits getBetweenPredicateRangeLimits(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p)
    {
        //        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        OwSearchCriteria secondRangeCriteria = criteria_p.getSecondRangeCriteria();
        Object v1 = criteria_p.getValue();
        Object v2 = secondRangeCriteria.getValue();

        if (v1 instanceof Date && criteria_p.isIgnoreTime())
        {
            v1 = OwDateTimeUtil.setBeginOfDayTime((Date) v1);
        }

        if (v2 instanceof Date && criteria_p.isIgnoreTime())
        {
            v2 = OwDateTimeUtil.setBeginOfNextDayTime((Date) v2);
        }

        return new RangeLimits(v1, v2);
    }

    protected Object getLimitForComparisonPredicate(OwSearchCriteria criteria_p, OwComparisonOperator comparisonOperator_p, OwProcessContext context_p)
    {
        Object value = criteria_p.getValue();
        if (value instanceof Date && criteria_p.isIgnoreTime())
        {
            int operator = criteria_p.getOperator();
            if (OwSearchOperator.CRIT_OP_LESS_EQUAL == operator || OwSearchOperator.CRIT_OP_GREATER == operator)
            {
                value = OwDateTimeUtil.setBeginOfNextDayTime((Date) value);
            }
            else if (OwSearchOperator.CRIT_OP_GREATER_EQUAL == operator || OwSearchOperator.CRIT_OP_LESS == operator)
            {
                value = OwDateTimeUtil.setBeginOfDayTime((Date) value);
            }
        }

        return value;
    }
}
