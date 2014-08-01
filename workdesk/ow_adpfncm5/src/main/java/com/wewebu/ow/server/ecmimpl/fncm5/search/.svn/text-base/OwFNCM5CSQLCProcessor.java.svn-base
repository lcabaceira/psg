package com.wewebu.ow.server.ecmimpl.fncm5.search;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.VersionStatus;
import com.wewebu.ow.csqlc.OwCSQLCException;
import com.wewebu.ow.csqlc.OwCSQLCProcessor;
import com.wewebu.ow.csqlc.OwSQLEntitiesResolver;
import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwComparisonOperator;
import com.wewebu.ow.csqlc.ast.OwComparisonPredicate;
import com.wewebu.ow.csqlc.ast.OwCorrelatedTableName;
import com.wewebu.ow.csqlc.ast.OwFolderPredicateFormat;
import com.wewebu.ow.csqlc.ast.OwJoinSpecification;
import com.wewebu.ow.csqlc.ast.OwLiteral;
import com.wewebu.ow.csqlc.ast.OwOperandFolderFormat;
import com.wewebu.ow.csqlc.ast.OwPredicateFormat;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.csqlc.ast.OwSQLDateTimeLiteral;
import com.wewebu.ow.csqlc.ast.OwSearchCondition;
import com.wewebu.ow.csqlc.ast.OwSignedNumericLiteral;
import com.wewebu.ow.csqlc.ast.OwStringImageLiteral;
import com.wewebu.ow.csqlc.ast.OwTextSearchPredicate;
import com.wewebu.ow.csqlc.ast.OwUndefinedPredicate;
import com.wewebu.ow.server.ecm.OwClass;
import com.wewebu.ow.server.ecm.OwStandardClassSelectObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.util.OwDateTimeUtil;

/**
 *<p>
 * P8 5.0 SQL processor implementation. 
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
public class OwFNCM5CSQLCProcessor extends OwCSQLCProcessor
{
    private static final String THIS_COLUMN = "This";
    private static final String QUERIED_OBJECT_COLUMN = "QueriedObject";
    private static final String CONTENT_SEARCH_TABLE = "ContentSearch";
    private static final OwFolderPredicateFormat FOLDER_FORMAT = new OwOperandFolderFormat("INFOLDER", "INSUBFOLDER", "[" + THIS_COLUMN + "]");
    private static final OwPredicateFormat IN_FORMAT = new OwPredicateFormat(null, "NOT", "IN", "IN", null, null);
    private static final OwPredicateFormat QUANTIFIED_IN_FORMAT = new OwPredicateFormat(null, "NOT", "IN", "IN", null, null);
    private static final OwPredicateFormat QUANTIIFED_COPMARISON_FORMAT = new OwPredicateFormat(null, "NOT", "IN", "IN", null, null);
    private static final OwPredicateFormat LIKE_FORMAT = new OwPredicateFormat(null, "NOT", "LIKE", "LIKE", null, null);

    private TimeZone dateTimeZone;

    public OwFNCM5CSQLCProcessor(OwSQLEntitiesResolver entitiesResolver_p, TimeZone dateTimeZone_p)
    {
        super(entitiesResolver_p);
        this.dateTimeZone = dateTimeZone_p;
    }

    protected OwColumnQualifier createColumnQualifier(String propertyName_p, String repositoryID_p) throws OwException
    {
        return new OwColumnQualifier();
    }

    protected OwColumnQualifier createQueryFieldQualifier(OwFieldDefinition fieldDefinition_p, OwProcessContext context_p) throws OwException
    {
        return new OwColumnQualifier();
    }

    protected OwColumnQualifier createQueryColumnQualifier(String tableName_p, String typeName_p)
    {
        return new OwColumnQualifier(tableName_p, null);
    }

    protected OwColumnQualifier createFromTableQualifier(String tableName_p, String typeName_p)
    {
        return new OwColumnQualifier(tableName_p, typeName_p);
    }

    protected String createJoinColumnName(OwColumnQualifier qualifier_p)
    {
        if (CONTENT_SEARCH_TABLE.equals(qualifier_p.getTargetTable()))
        {
            return QUERIED_OBJECT_COLUMN;
        }
        else
        {
            return THIS_COLUMN;
        }
    }

    protected OwPredicateFormat getInFormat()
    {
        return IN_FORMAT;
    }

    protected OwPredicateFormat getQuantifiedInFormat()
    {

        return QUANTIFIED_IN_FORMAT;
    }

    protected OwPredicateFormat getQuantifiedComparisonFormat()
    {
        return QUANTIIFED_COPMARISON_FORMAT;
    }

    protected OwFolderPredicateFormat getFolderPredicateFormat()
    {
        return FOLDER_FORMAT;
    }

    protected OwTextSearchPredicate createTextSearchPredicate(com.wewebu.ow.csqlc.ast.OwCharacterStringLiteral searchExpression_p, OwColumnReference columnReference_p) throws OwException
    {
        //TODO: dialect ?
        final String dialect = null;
        OwColumnQualifier contentJoinedColumnQualifier = new OwColumnQualifier(CONTENT_SEARCH_TABLE, CONTENT_SEARCH_TABLE, "c");
        if (columnReference_p != null)
        {
            return new OwTextSearchPredicate(searchExpression_p, columnReference_p, contentJoinedColumnQualifier, dialect);
        }
        else
        {
            return new OwTextSearchPredicate(searchExpression_p, createColumnReference(new OwColumnQualifier(), "*"), contentJoinedColumnQualifier, dialect);
        }
    };

    protected String joinType(OwCorrelatedTableName joinedTable_p)
    {
        return OwJoinSpecification.INNER;
    }

    @Override
    protected OwSearchCondition createVersionSelectionCondition(OwSearchNode searchRootNode_p, int versionSelection_p) throws OwException
    {
        switch (versionSelection_p)
        {
            case OwSearchTemplate.VERSION_SELECT_MINORS:
                return createComparison(PropertyNames.MINOR_VERSION_NUMBER, OwComparisonOperator.NEQ, 0);
            case OwSearchTemplate.VERSION_SELECT_MAJORS:
                return createComparison(PropertyNames.MINOR_VERSION_NUMBER, OwComparisonOperator.EQ, 0);
            case OwSearchTemplate.VERSION_SELECT_IN_PROCESS:
                return createComparison(PropertyNames.VERSION_STATUS, OwComparisonOperator.EQ, VersionStatus.IN_PROCESS_AS_INT);
            case OwSearchTemplate.VERSION_SELECT_CHECKED_OUT:
                return createComparison(PropertyNames.VERSION_STATUS, OwComparisonOperator.EQ, VersionStatus.RESERVATION_AS_INT);
            case OwSearchTemplate.VERSION_SELECT_RELEASED:
                return createComparison(PropertyNames.VERSION_STATUS, OwComparisonOperator.EQ, VersionStatus.RELEASED_AS_INT);
            case OwSearchTemplate.VERSION_SELECT_ALL:
                return new OwUndefinedPredicate();
            case OwSearchTemplate.VERSION_SELECT_CURRENT:
            case OwSearchTemplate.VERSION_SELECT_DEFAULT:
            default:
                return createComparison(PropertyNames.IS_CURRENT_VERSION, OwComparisonOperator.EQ, true);
        }

    }

    private OwSearchCondition createComparison(String propertyName_p, OwComparisonOperator operator_p, boolean value_p)
    {
        OwStringImageLiteral booleanLiteral = new OwStringImageLiteral(Boolean.toString(value_p));
        return createComparison(propertyName_p, operator_p, booleanLiteral);
    }

    private OwSearchCondition createComparison(String propertyName_p, OwComparisonOperator operator_p, int value_p)
    {
        OwSignedNumericLiteral statusLiteral = new OwSignedNumericLiteral(value_p);
        return createComparison(propertyName_p, operator_p, statusLiteral);
    }

    private OwSearchCondition createComparison(String propertyName_p, OwComparisonOperator operator_p, OwLiteral value_p)
    {
        OwColumnReference columnReference = createColumnReference(new OwColumnQualifier(), propertyName_p);
        OwComparisonPredicate eq = new OwComparisonPredicate(columnReference, operator_p, value_p);
        return eq;
    }

    protected OwPredicateFormat createLikeFormat()
    {
        return LIKE_FORMAT;
    }

    @Override
    protected OwSQLDateTimeLiteral createLiteral(OwSearchCriteria criteria_p, Date date_p) throws OwCSQLCException
    {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        Date sqlDate = OwDateTimeUtil.convert(date_p, dateTimeZone, defaultTimeZone);
        return super.createLiteral(criteria_p, sqlDate);
    }

    @Override
    protected String escapeStringValue(String value_p)
    {
        String escaped = null;

        if (value_p != null)
        {
            escaped = value_p.replaceAll("'", "''");
        }

        return escaped;
    }

    @Override
    protected OwColumnReference createColumnReference(OwColumnQualifier qualifier_p, String columnName_p)
    {
        return new OwFNCM5ColumnReference(qualifier_p, columnName_p);
    }

    protected String escapeNativeWildCardRepresentation(String value_p, Collection<OwWildCardDefinition> wildCardDefs)
    {
        return value_p;
    }

    protected boolean isMultipleTextSearchSyntax()
    {
        return true;
    }

    @Override
    protected String createCBRAllExpression(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        Object criteriaValue = criteria_p.getValue();
        if (criteriaValue == null || criteriaValue.toString().length() == 0)
        {
            return null;
        }

        return criteriaValue.toString();
    }

    @Override
    protected String createCBRInExpression(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        Object criteriaValue = criteria_p.getValue();
        if (criteriaValue == null || criteriaValue.toString().length() == 0)
        {
            return null;
        }

        return criteriaValue.toString();
    }

    @Override
    protected OwSearchCondition createSubclassesConditions(List<OwClass> subclasses_p) throws OwException
    {
        return new OwFNCM5SubclassesSearchContion(subclasses_p);
    }

    @Override
    protected String getMainObjectCriteria()
    {
        return OwStandardClassSelectObject.FROM_NAME;
    }

    @Override
    protected boolean qualifySingleTable(OwSearchNode searchRootNode_p, OwQueryStatement statement_p) throws OwException
    {
        Map<String, List<OwClass>> subclasses = findSelectedObjectClasses(searchRootNode_p, OwStandardClassSelectObject.SUBCLASSES_NAME);
        Map<String, List<OwClass>> from = findMainObjectClasses(searchRootNode_p);

        if (subclasses != null && !subclasses.isEmpty())
        {
            OwColumnQualifier mainTable = statement_p.getMainTableQualifier();
            String target = mainTable.getTargetTable();
            Set<Entry<String, List<OwClass>>> fromEtries = from.entrySet();
            LinkedList<OwClass> allFromClasses = new LinkedList<OwClass>();
            for (Entry<String, List<OwClass>> fromEntry : fromEtries)
            {
                List<OwClass> values = fromEntry.getValue();
                allFromClasses.addAll(values);
            }
            for (OwClass fromClass : allFromClasses)
            {
                if (target.equals(fromClass.getClassName()))
                {
                    List<OwClass> foundSubclasses = subclass(fromClass, subclasses, allFromClasses.size() == 1);
                    return foundSubclasses != null && !foundSubclasses.isEmpty();
                }
            }

        }

        //default 
        return false;

    }
}
