package com.wewebu.ow.csqlc;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.ast.OwANDBooleanTerm;
import com.wewebu.ow.csqlc.ast.OwBetweenPredicate;
import com.wewebu.ow.csqlc.ast.OwBooleanFactor;
import com.wewebu.ow.csqlc.ast.OwBooleanTerm;
import com.wewebu.ow.csqlc.ast.OwCharacterStringLiteral;
import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwComparisonOperator;
import com.wewebu.ow.csqlc.ast.OwComparisonPredicate;
import com.wewebu.ow.csqlc.ast.OwCompoundSelectList;
import com.wewebu.ow.csqlc.ast.OwCorrelatedTableName;
import com.wewebu.ow.csqlc.ast.OwExternal;
import com.wewebu.ow.csqlc.ast.OwFolderPredicate;
import com.wewebu.ow.csqlc.ast.OwFolderPredicateFormat;
import com.wewebu.ow.csqlc.ast.OwFromClause;
import com.wewebu.ow.csqlc.ast.OwInPredicate;
import com.wewebu.ow.csqlc.ast.OwInValueList;
import com.wewebu.ow.csqlc.ast.OwJoinSpecification;
import com.wewebu.ow.csqlc.ast.OwLikePredicate;
import com.wewebu.ow.csqlc.ast.OwLiteral;
import com.wewebu.ow.csqlc.ast.OwMergeType;
import com.wewebu.ow.csqlc.ast.OwNullLiteral;
import com.wewebu.ow.csqlc.ast.OwNullPredicate;
import com.wewebu.ow.csqlc.ast.OwORSearchCondition;
import com.wewebu.ow.csqlc.ast.OwOrderByClause;
import com.wewebu.ow.csqlc.ast.OwPredicate;
import com.wewebu.ow.csqlc.ast.OwPredicateFormat;
import com.wewebu.ow.csqlc.ast.OwQuantifiedComparisonPredicate;
import com.wewebu.ow.csqlc.ast.OwQuantifiedInPredicate;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.csqlc.ast.OwRepositoryTarget;
import com.wewebu.ow.csqlc.ast.OwSQLDateTimeLiteral;
import com.wewebu.ow.csqlc.ast.OwSearchCondition;
import com.wewebu.ow.csqlc.ast.OwSearchConditionBooleanTest;
import com.wewebu.ow.csqlc.ast.OwSearchConditionPredicate;
import com.wewebu.ow.csqlc.ast.OwSelectAll;
import com.wewebu.ow.csqlc.ast.OwSelectList;
import com.wewebu.ow.csqlc.ast.OwSelectSublist;
import com.wewebu.ow.csqlc.ast.OwSignedNumericLiteral;
import com.wewebu.ow.csqlc.ast.OwSimpleTable;
import com.wewebu.ow.csqlc.ast.OwSortSpecification;
import com.wewebu.ow.csqlc.ast.OwStringImageLiteral;
import com.wewebu.ow.csqlc.ast.OwTextSearchPredicate;
import com.wewebu.ow.csqlc.ast.OwUndefinedPredicate;
import com.wewebu.ow.csqlc.ast.OwValueExpression;
import com.wewebu.ow.csqlc.ast.OwWhereClause;
import com.wewebu.ow.csqlc.ast.OwXBooleanFactor;
import com.wewebu.ow.server.ecm.OwClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardClassSelectObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Content SQL Creator node processing facade.
 * An {@link OwSearchNode} + {@link OwSort} to content SQL abstract syntax tree conversion class.
 * The supported SQL syntaxes must conform generally to the SQL-92 syntax.
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
 *@since 3.2.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class OwCSQLCProcessor
{
    private static final Logger LOG = OwLogCore.getLogger(OwCSQLCProcessor.class);

    private static final String UNKNOWN_BASE_CLASS = "unknown";

    protected OwSQLEntitiesResolver m_entitiesResolver;
    private Integer maxRows;

    /**
     *<p>
     * OwProcessContext.
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
    public static class OwProcessContext
    {
        public final String repositoryID;
        public final String mainType;
        public final Set<String> extraTypes;

        public OwProcessContext(String repositoryID_p, String mainType_p, Set<String> extraTypes_p)
        {
            super();
            this.repositoryID = repositoryID_p;
            this.mainType = mainType_p;
            this.extraTypes = extraTypes_p;
        }

    }

    public OwCSQLCProcessor(OwSQLEntitiesResolver entitiesResolver_p)
    {
        super();
        m_entitiesResolver = entitiesResolver_p;
    }

    public OwCSQLCProcessor(OwSQLEntitiesResolver entitiesResolver_p, Integer maxRows)
    {
        super();
        m_entitiesResolver = entitiesResolver_p;
        this.maxRows = maxRows;
    }

    protected String createQueryFieldName(OwFieldDefinition fieldDefinition_p, OwProcessContext context_p) throws OwException
    {
        String fieldClassName = fieldDefinition_p.getClassName();

        String queryName = m_entitiesResolver.resovleQueryColumnName(context_p.mainType, fieldClassName, context_p.repositoryID);
        if (queryName == null)
        {
            String message = "The filed definition " + fieldClassName + " is not queryable! ";
            LOG.error("OwCSQLCProcessor.createQueryFieldName():" + message);
            throw new OwInvalidOperationException(new OwString1("ecmimpl.OwCSQLCProcessor.non.queryable.field.error", "Invalid searched field! The field %1 can not be queried!", fieldClassName));
        }
        else
        {
            return queryName;
        }

    }

    protected abstract OwColumnQualifier createQueryFieldQualifier(OwFieldDefinition fieldDefinition_p, OwProcessContext context_p) throws OwException;

    protected abstract OwColumnQualifier createColumnQualifier(String propertyName_p, String repositoryID_p) throws OwException;

    private String createQueryPropertyName(String tableName_p, String propertyName_p, String repositoryID_p) throws OwException
    {
        String queryName = m_entitiesResolver.resovleQueryColumnName(tableName_p, propertyName_p, repositoryID_p);
        return queryName;
    }

    /**
     * Create the SQL statement(s) by parsing the provided root search node. 
     * Will set the version selection to default, for creating of the SQL statement(s).<br>
     * See @link #createSQLStatements(OwSearchNode, Collection, OwSort, int)
     * @param searchRootNode_p OwSearchNode root node to be used for parsing
     * @param propertyQueryNames_p Collection List of query names
     * @param sortOrder_p OwSort defined sort for search
     * @return the SQL query statement for the given {@link OwSearchNode} , property collection and {@link OwSort} specification 
     * @throws OwException
     */
    public final OwExternal<List<OwQueryStatement>> createSQLStatements(OwSearchNode searchRootNode_p, Collection<String> propertyQueryNames_p, OwSort sortOrder_p) throws OwException
    {
        return createSQLStatements(searchRootNode_p, propertyQueryNames_p, sortOrder_p, OwSearchTemplate.VERSION_SELECT_DEFAULT);
    }

    /**
     * Create the SQL statement(s) by parsing the provided root search node. 
     * @param searchRootNode_p OwSearchNode root node to be used for parsing
     * @param propertyQueryNames_p Collection List of query names
     * @param sortOrder_p OwSort defined sort for search
     * @param versionSelection_p int the version selection which should be handled
     * @return the SQL query statement for the given {@link OwSearchNode} , property collection and {@link OwSort} specification 
     * @throws OwException
     */
    public OwExternal<List<OwQueryStatement>> createSQLStatements(OwSearchNode searchRootNode_p, Collection<String> propertyQueryNames_p, OwSort sortOrder_p, int versionSelection_p) throws OwException
    {
        List<OwQueryStatement> statements = new LinkedList<OwQueryStatement>();

        OwExternal<Map<OwRepositoryTarget, List<OwSimpleTable>>> tablesEx = createSimpleTable(searchRootNode_p, propertyQueryNames_p, versionSelection_p);

        Map<OwRepositoryTarget, List<OwSimpleTable>> simpleTables = tablesEx.getInternal();
        OwExternal<List<OwQueryStatement>> statementsEx = new OwExternal<List<OwQueryStatement>>(statements, tablesEx);

        Set<Entry<OwRepositoryTarget, List<OwSimpleTable>>> tableEntries = simpleTables.entrySet();
        for (Entry<OwRepositoryTarget, List<OwSimpleTable>> tableEntry : tableEntries)
        {
            OwRepositoryTarget repositoryTarget = tableEntry.getKey();
            List<OwSimpleTable> repositorySimpleTables = tableEntry.getValue();
            Set<String> allTypes = new HashSet<String>();
            for (OwSimpleTable simpleTable : repositorySimpleTables)
            {
                OwColumnQualifier tableQ = simpleTable.getMainTableQualifier();
                allTypes.add(tableQ.getTargetObjectType());
            }
            for (OwSimpleTable table : repositorySimpleTables)
            {
                String repositoryID = repositoryTarget.getRepositoryId();
                OwColumnQualifier mainTableQ = table.getMainTableQualifier();
                String tableType = mainTableQ.getTargetObjectType();
                Set<String> extraTypes = new HashSet<String>(allTypes);
                extraTypes.remove(tableType);
                OwProcessContext context = new OwProcessContext(repositoryID, tableType, extraTypes);
                OwOrderByClause orderBy = createOrderByClause(sortOrder_p, context);

                OwQueryStatement statement = new OwQueryStatement(repositoryTarget, table, orderBy);

                joinNormalizedTables(statement, repositoryID, qualifySingleTable(searchRootNode_p, statement));
                statements.add(statement);
            }
        }

        return statementsEx;
    }

    /**
     * 
     * @param searchRootNode_p
     * @param statement_p
     * @return true statements with single main tables need qualified names 
     * @throws OwException
     * @since 3.2.0.3
     */
    protected boolean qualifySingleTable(OwSearchNode searchRootNode_p, OwQueryStatement statement_p) throws OwException
    {
        return false;
    }

    private void joinNormalizedTables(OwQueryStatement statement_p, String repositoryID_p, boolean qualifySingleTable_p) throws OwException
    {
        List<OwColumnQualifier> qs = statement_p.getColumnQualifiers();
        OwColumnQualifier mainTable = statement_p.getMainTableQualifier();

        Set<OwColumnQualifier> normalizedQualifiers = enforceQualifiers(qs, mainTable, repositoryID_p, qualifySingleTable_p);

        if (!normalizedQualifiers.contains(mainTable))
        {
            LOG.error("OwCSQLCProcessor.joinNormalizedTables(): Sublcass and where  missmatch !");
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));
        }

        for (OwColumnQualifier qualifier : normalizedQualifiers)
        {
            if (!mainTable.equals(qualifier))
            {
                String table = qualifier.getTargetTable();

                OwCorrelatedTableName joinedTableReference = new OwCorrelatedTableName(table, qualifier);
                OwColumnReference joinSpecC1 = createColumnReference(mainTable, createJoinColumnName(mainTable));
                OwColumnReference joinSpecC2 = createColumnReference(qualifier, createJoinColumnName(qualifier));
                OwJoinSpecification joinSpec = new OwJoinSpecification(joinSpecC1, joinSpecC2, joinType(joinedTableReference));
                statement_p.addJoin(joinedTableReference, joinSpec);
            }
        }

        statement_p.setNormalizedQualifiers(normalizedQualifiers);
    }

    protected abstract String joinType(OwCorrelatedTableName joinedTable_p);

    protected abstract String createJoinColumnName(OwColumnQualifier qualifier_p);

    private OwOrderByClause createOrderByClause(OwSort sortOrder_p, OwProcessContext context_p) throws OwException
    {

        if (sortOrder_p != null)
        {

            OwOrderByClause orderByClause = new OwOrderByClause();
            Iterator itsort = sortOrder_p.getPrioritizedIterator();
            while (itsort.hasNext())
            {
                OwSort.OwSortCriteria sortCrit = (OwSort.OwSortCriteria) itsort.next();
                String propertyName = sortCrit.getPropertyName();
                String queryPropertyName = createQueryPropertyName(context_p.mainType, propertyName, context_p.repositoryID);
                OwColumnQualifier columnQualifier = createColumnQualifier(propertyName, context_p.repositoryID);

                if (columnQualifier == null)
                {
                    columnQualifier = createColumnQualifier(context_p.mainType, context_p.repositoryID);
                }

                String targetType = columnQualifier.getTargetObjectType();
                boolean isSortTarget = true;
                if (!m_entitiesResolver.isSubtable(targetType, context_p.mainType, context_p.repositoryID))
                {
                    for (String xType : context_p.extraTypes)
                    {
                        if (m_entitiesResolver.isSubtable(targetType, xType, context_p.repositoryID))
                        {
                            isSortTarget = false;
                            break;
                        }
                    }
                }

                if (queryPropertyName != null && isSortTarget)
                {
                    if (m_entitiesResolver.canOrderBy(context_p.mainType, propertyName, context_p.repositoryID))
                    {
                        boolean ascFlag = sortCrit.getAscFlag();
                        OwColumnReference columnReference = createColumnReference(columnQualifier, queryPropertyName);
                        OwSortSpecification sortSpecification = new OwSortSpecification(columnReference, ascFlag);
                        orderByClause.add(sortSpecification);
                    }
                    else
                    {
                        LOG.debug("OwCSQLCProcessor.createOrderByClause(): the order by sort specification on " + propertyName + " was ommited ( not orderable).");
                    }
                }
                else
                {
                    LOG.debug("OwCSQLCProcessor.createOrderByClause(): the order by sort specification on " + propertyName + " was ommited ( not queryable or external).");
                }

            }

            return orderByClause;
        }
        else
        {
            return null;
        }
    }

    /**(overridable)
     * 
     * @param subclasses_p
     * @return subclasses search condition for the given subclasses if supported.
     *         If subclasses conditions are not supported an invalid condition is returned.   
     * @throws OwException
     * @since 3.2.0.3
     */
    protected OwSearchCondition createSubclassesConditions(List<OwClass> subclasses_p) throws OwException
    {
        return new OwUndefinedPredicate();
    }

    private OwExternal<Map<OwRepositoryTarget, OwSearchCondition>> createRepositoryPathConditions(OwSearchNode specialNodeExt_p) throws OwException
    {
        List specialChildren = specialNodeExt_p.getChilds();
        Iterator itSpecial = specialChildren.iterator();
        Map<OwRepositoryTarget, OwSearchCondition> repositoryFolderConditions = new HashMap<OwRepositoryTarget, OwSearchCondition>();
        Map<String, OwRepositoryTarget> mergedTargets = new HashMap<String, OwRepositoryTarget>();

        OwExternal<Map<OwRepositoryTarget, OwSearchCondition>> paths = new OwExternal<Map<OwRepositoryTarget, OwSearchCondition>>(repositoryFolderConditions);
        while (itSpecial.hasNext())
        {

            OwSearchNode node = (OwSearchNode) itSpecial.next();
            OwSearchCriteria crit = node.getCriteria();

            if (crit.getClassName().equals(OwSearchPathField.CLASS_NAME))
            {
                OwSearchPath path = (OwSearchPath) crit.getValue();

                OwSearchObjectStore objectStore = path.getObjectStore();
                String repositoryID = objectStore.getId();
                if (repositoryID == null)
                {
                    repositoryID = m_entitiesResolver.resolveRepositoryID(objectStore.getName());
                }
                if (m_entitiesResolver.isInternalRepositoryID(repositoryID))
                {
                    OwPredicate folderPredicate = null;
                    if (path.isObjectStoreReference())
                    {
                        folderPredicate = new OwUndefinedPredicate();
                    }
                    else
                    {
                        String pathFolderId = path.getId();
                        if (pathFolderId == null)
                        {
                            pathFolderId = m_entitiesResolver.resolveQueryFolderId(repositoryID, path.getPathName());
                        }
                        folderPredicate = createFolderPredicate(crit, pathFolderId, path.isSearchSubFolders());
                    }

                    OwMergeType mergeType = OwMergeType.fromCriteriaOperator(crit.getOperator());

                    //merge multiple merge-type-defined repositories : bug OWD-3829 
                    //virtual folders template inject parent path with merge-type = none

                    OwRepositoryTarget mergedTarget = mergedTargets.get(repositoryID);
                    if (mergedTarget == null)
                    {
                        mergedTarget = new OwRepositoryTarget(repositoryID, mergeType);
                    }
                    else
                    {
                        OwSearchCondition condition = repositoryFolderConditions.remove(mergedTarget);

                        OwMergeType mergedMergeType = mergedTarget.getMergeType();
                        if (OwMergeType.NONE.equals(mergedMergeType))
                        {
                            mergedTarget = new OwRepositoryTarget(repositoryID, mergeType);
                        }
                        else if (!OwMergeType.NONE.equals(mergeType) && !mergedMergeType.equals(mergeType))
                        {
                            LOG.error("OwCSQLCProcessor.createRepositoryPathConditions(): merge type conflict for repository  " + repositoryID + " : " + mergeType.toString() + " <> " + mergedMergeType.toString());
                            throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));
                        }

                        repositoryFolderConditions.put(mergedTarget, condition);
                    }

                    mergedTargets.put(repositoryID, mergedTarget);

                    OwSearchCondition foldersSearchCondition = repositoryFolderConditions.get(mergedTarget);
                    if (foldersSearchCondition == null)
                    {
                        foldersSearchCondition = folderPredicate;
                    }
                    else
                    {
                        foldersSearchCondition = new OwORSearchCondition(foldersSearchCondition, folderPredicate);
                    }

                    repositoryFolderConditions.put(mergedTarget, foldersSearchCondition);
                }
                else
                {
                    paths.addExternalObjectStore(repositoryID, objectStore.getName());
                }
            }
        }

        if (repositoryFolderConditions.isEmpty() && paths.hasExternalObjectStores())
        {
            paths.setInternal(null);
        }

        return paths;
    }

    protected abstract boolean isMultipleTextSearchSyntax();

    private OwSearchCondition addSubclassesCondition(List<OwClass> subclasses_p, OwSearchCondition searchCondition_p) throws OwException
    {
        if (subclasses_p == null || subclasses_p.isEmpty())
        {
            return searchCondition_p;
        }
        else
        {
            OwSearchConditionBooleanTest folderBasedTest = new OwSearchConditionBooleanTest(searchCondition_p);

            OwSearchCondition subclassesCondition = createSubclassesConditions(subclasses_p);

            OwSearchConditionBooleanTest subclassesTest = new OwSearchConditionBooleanTest(subclassesCondition);

            return new OwANDBooleanTerm(subclassesTest, folderBasedTest);
        }
    }

    protected final List<OwClass> subclass(OwClass mainType_p, Map<String, List<OwClass>> typedSubclasses_p, boolean singleMainType_p)
    {
        String mainBaseClass = mainType_p.getBaseClassName();
        if (mainBaseClass == null)
        {
            if (singleMainType_p)
            {
                LinkedList<OwClass> allSubclasses = new LinkedList<OwClass>();

                Collection<List<OwClass>> allSubclassValues = typedSubclasses_p.values();
                for (List<OwClass> list : allSubclassValues)
                {
                    allSubclasses.addAll(list);
                }

                return allSubclasses;
            }
            else
            {
                return Collections.EMPTY_LIST;
            }
        }
        else
        {
            return typedSubclasses_p.get(mainBaseClass);
        }
    }

    private OwExternal<Map<OwRepositoryTarget, Map<String, OwWhereClause>>> createWhereClauses(OwSearchNode searchRootNode_p, int versionSelection_p) throws OwException
    {

        OwSearchNode whereRoot = searchRootNode_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY);

        Map<OwRepositoryTarget, Map<String, OwWhereClause>> whereClauses = new HashMap<OwRepositoryTarget, Map<String, OwWhereClause>>();

        Map<String, List<OwClass>> typedClasses = findMainObjectClasses(searchRootNode_p);
        Collection<List<OwClass>> allClasses = typedClasses.values();
        List<OwClass> classes = new LinkedList<OwClass>();
        for (List<OwClass> typeClasses : allClasses)
        {
            classes.addAll(typeClasses);
        }

        Map<String, List<OwClass>> typedSubclasses = findSelectedObjectClasses(searchRootNode_p, OwStandardClassSelectObject.SUBCLASSES_NAME);

        OwSearchNode pathSpecialNode = searchRootNode_p.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
        OwExternal<Map<OwRepositoryTarget, OwSearchCondition>> pathConditions = createRepositoryPathConditions(pathSpecialNode);

        Map<OwRepositoryTarget, OwSearchCondition> repositoryFolderConditions = pathConditions.getInternal();

        OwExternal<Map<OwRepositoryTarget, Map<String, OwWhereClause>>> whereClausesEx;
        whereClausesEx = new OwExternal<Map<OwRepositoryTarget, Map<String, OwWhereClause>>>(whereClauses, pathConditions);

        if (repositoryFolderConditions == null)
        {
            return whereClausesEx;
        }

        Set<OwClass> extraObjectTypes = new HashSet<OwClass>(classes);
        Set<String> extraObjectClassNames = new HashSet<String>();

        for (OwClass clazz : extraObjectTypes)
        {
            extraObjectClassNames.add(clazz.getClassName());
        }

        for (OwClass mainObjectType : classes)
        {
            OwExternal<Map<OwRepositoryTarget, OwSearchCondition>> localPathConditions = createRepositoryPathConditions(pathSpecialNode);
            Map<OwRepositoryTarget, OwSearchCondition> localRepositoryFolderConditions = localPathConditions.getInternal();

            extraObjectTypes.remove(mainObjectType);
            OwSearchCondition versionSelectoinCondition = createVersionSelectionCondition(searchRootNode_p, versionSelection_p);
            String mainClassName = mainObjectType.getClassName();

            //TODO : remove empty vs. non-empty code duplication

            if (!localRepositoryFolderConditions.isEmpty())
            {
                Set<Entry<OwRepositoryTarget, OwSearchCondition>> repositoryTargets = localRepositoryFolderConditions.entrySet();
                for (Entry<OwRepositoryTarget, OwSearchCondition> repositoryTarget : repositoryTargets)
                {
                    String repositoryID = repositoryTarget.getKey().getRepositoryId();

                    OwProcessContext context = new OwProcessContext(repositoryID, mainClassName, extraObjectClassNames);

                    OwSearchCondition searchCondition = createSearchCondition(whereRoot, context);

                    if (versionSelectoinCondition != null && m_entitiesResolver.isVersionable(mainClassName, repositoryID))
                    {
                        searchCondition = new OwANDBooleanTerm(searchCondition.asBooleanTest(), new OwSearchConditionBooleanTest(versionSelectoinCondition));
                    }

                    OwSearchCondition contentCondition = createContentCondition(searchRootNode_p, context);

                    if (contentCondition != null)
                    {
                        searchCondition = new OwANDBooleanTerm(new OwSearchConditionBooleanTest(searchCondition), new OwSearchConditionBooleanTest(contentCondition));
                    }

                    OwSearchConditionBooleanTest searchTest = new OwSearchConditionBooleanTest(searchCondition);
                    OwSearchCondition foldersSearchCondition = repositoryTarget.getValue();
                    OwSearchConditionBooleanTest foldersTest = new OwSearchConditionBooleanTest(foldersSearchCondition);

                    OwANDBooleanTerm folderBasedSearchCondition = new OwANDBooleanTerm(foldersTest, searchTest);

                    List<OwClass> subclasses = subclass(mainObjectType, typedSubclasses, classes.size() == 1);
                    OwSearchCondition whereCondition = addSubclassesCondition(subclasses, folderBasedSearchCondition);

                    addWhereClause(whereClauses, repositoryTarget.getKey(), mainClassName, new OwWhereClause(whereCondition));

                }
            }
            else
            {
                String defaultRepositoryID = m_entitiesResolver.resolveDefaultRepositoryID();
                OwRepositoryTarget defaultRepositoryTarget = new OwRepositoryTarget(defaultRepositoryID, OwMergeType.NONE);

                OwProcessContext context = new OwProcessContext(defaultRepositoryID, mainClassName, extraObjectClassNames);
                OwSearchCondition searchCondition = createSearchCondition(whereRoot, context);

                if (versionSelectoinCondition != null && m_entitiesResolver.isVersionable(mainClassName, defaultRepositoryID))
                {
                    searchCondition = new OwANDBooleanTerm(searchCondition.asBooleanTest(), new OwSearchConditionBooleanTest(versionSelectoinCondition));
                }

                OwSearchCondition contentCondition = createContentCondition(searchRootNode_p, context);

                if (contentCondition != null)
                {
                    searchCondition = new OwANDBooleanTerm(new OwSearchConditionBooleanTest(searchCondition), new OwSearchConditionBooleanTest(contentCondition));
                }

                List<OwClass> subclasses = subclass(mainObjectType, typedSubclasses, classes.size() == 1);
                OwSearchCondition whereCondition = addSubclassesCondition(subclasses, searchCondition);

                addWhereClause(whereClauses, defaultRepositoryTarget, mainClassName, new OwWhereClause(whereCondition));
            }
        }
        return whereClausesEx;
    }

    private final void addWhereClause(Map<OwRepositoryTarget, Map<String, OwWhereClause>> whereClauses_p, OwRepositoryTarget repositoryTarget_p, String mainObjectType_p, OwWhereClause clause_p)
    {
        Map<String, OwWhereClause> repositoryClauses = whereClauses_p.get(repositoryTarget_p);
        if (repositoryClauses == null)
        {
            repositoryClauses = new HashMap<String, OwWhereClause>();
            whereClauses_p.put(repositoryTarget_p, repositoryClauses);
        }

        repositoryClauses.put(mainObjectType_p, clause_p);
    }

    protected abstract OwFolderPredicateFormat getFolderPredicateFormat();

    private OwFolderPredicate createFolderPredicate(OwSearchCriteria criteria_p, String pathFolderId_p, boolean inTree_p)
    {
        OwCharacterStringLiteral folderIDLiteral = createLiteral(criteria_p, pathFolderId_p);
        return new OwFolderPredicate(folderIDLiteral, inTree_p, getFolderPredicateFormat());
    }

    /**
     * Scan the node for existence or definition of CBR (a.k.a. Full text search).
     * @param conditionRootNode_p OwSearchNode the to be scanned
     * @param context_p OwProcessContext current context
     * @return OwSearchCondition or null if not found or available
     * @throws OwException
     */
    protected OwSearchCondition createContentCondition(OwSearchNode conditionRootNode_p, OwProcessContext context_p) throws OwException
    {
        OwSearchNode cbrNode = conditionRootNode_p.findSearchNode(OwSearchNode.NODE_TYPE_CBR);
        OwSearchCondition contentCondition = null;
        List<String> textSearchExpressions = new LinkedList<String>();
        OwSearchCriteria verityCriteria = null;
        if (cbrNode != null)
        {
            OwSearchNode cbrValueNode = cbrNode;
            Object value = "";
            Iterator itSearch = cbrValueNode.getChilds().iterator();
            while (itSearch.hasNext() && !cbrValueNode.isCriteriaNode())
            {
                cbrValueNode = (OwSearchNode) itSearch.next();
            }

            verityCriteria = cbrValueNode.getCriteria();
            value = verityCriteria.getValue();
            if (value != null && value.toString().length() > 0)
            {
                textSearchExpressions.add(value.toString());
            }

        }

        if (!isMultipleTextSearchSyntax())
        {
            handleSingleContentCondition(conditionRootNode_p, context_p, textSearchExpressions);
        }

        if (!textSearchExpressions.isEmpty())
        {
            OwCharacterStringLiteral searchLiteral = createContentSearchLiteral(textSearchExpressions, verityCriteria);
            contentCondition = createTextSearchPredicate(searchLiteral, null);
        }

        return contentCondition;
    }

    /**(overridable)
     * Method specific called for parsing statements where the {@link #isMultipleTextSearchSyntax()} return false.
     * Will be called by the {@link #createContentCondition(OwSearchNode, OwProcessContext)} method, 
     * only if syntax does not allow multiple content search predicate/conditions.
     * @param conditionRootNode_p OwSearchNode containing the content search criteria
     * @param context_p OwProcessContext current context for search
     * @param textSearchExpressions_p List of String where to attach additional expressions
     * @throws OwException
     */
    protected void handleSingleContentCondition(OwSearchNode conditionRootNode_p, OwProcessContext context_p, List<String> textSearchExpressions_p) throws OwException
    {
        Stack<OwSearchNode> nodeStack = new Stack<OwSearchNode>();
        nodeStack.push(conditionRootNode_p);
        while (!nodeStack.isEmpty())
        {
            OwSearchNode node = nodeStack.pop();
            List childNodes = node.getChilds();
            if (childNodes != null)
            {
                nodeStack.addAll(childNodes);
            }
            OwSearchCriteria criteria = node.getCriteria();
            if (criteria != null)
            {
                String expression = null;
                switch (criteria.getOperator())
                {
                    case OwSearchOperator.CRIT_OP_CBR_ALL:
                    {
                        expression = createCBRAllExpression(criteria, context_p);
                    }
                        break;
                    case OwSearchOperator.CRIT_OP_CBR_IN:
                    {
                        expression = createCBRInExpression(criteria, context_p);
                    }
                        break;
                    default:
                        break;
                }
                if (expression != null)
                {
                    textSearchExpressions_p.add(expression);
                }
            }
        }
    }

    /**(overridable)
     * Create a literal for the content search criteria, will return a
     * null value if the provided expression list does not contain valid values (non-empty and non-null).
     * @param textSearchExpressions_p List of expressions (String)
     * @param criteria_p OwSearchCriteria responsible for definition
     * @return OwCharacterStringLiteral or null
     */
    protected OwCharacterStringLiteral createContentSearchLiteral(List<String> textSearchExpressions_p, OwSearchCriteria criteria_p)
    {
        StringBuilder commonExpression = new StringBuilder();
        for (String expression : textSearchExpressions_p)
        {
            if (expression != null && expression.length() > 0)
            {
                commonExpression.append(expression);
                commonExpression.append(" ");
            }
        }
        if (commonExpression.length() > 0)
        {
            commonExpression.delete(commonExpression.length() - 1, commonExpression.length());
            return createLiteral(criteria_p, commonExpression.toString());
        }
        else
        {
            return null;
        }

    }

    protected OwSearchCondition createVersionSelectionCondition(OwSearchNode searchRootNode_p, int versionSelection_p) throws OwException
    {
        return null;
    }

    private OwSearchCondition createSearchCondition(OwSearchNode conditionRootNode_p, OwProcessContext context_p) throws OwException
    {
        if (conditionRootNode_p != null)
        {
            List criteriaList = conditionRootNode_p.getChilds();
            if (criteriaList != null)
            {

                switch (conditionRootNode_p.getOperator())
                {
                    case OwSearchNode.SEARCH_OP_AND:
                    {
                        return createBooleanTerm(conditionRootNode_p, context_p);
                    }

                    case OwSearchNode.SEARCH_OP_OR:
                    {
                        OwSearchCondition lastConditoin = null;
                        for (int i = 0; i < criteriaList.size(); i++)
                        {
                            OwSearchNode factorNode = (OwSearchNode) criteriaList.get(i);
                            OwBooleanTerm term = createBooleanTerm(factorNode, context_p);
                            if (lastConditoin != null)
                            {
                                lastConditoin = new OwORSearchCondition(lastConditoin, term);
                            }
                            else
                            {
                                lastConditoin = term;
                            }
                        }
                        return lastConditoin;
                    }
                    default:
                    {
                        LOG.error("OwCSQLCProcessor.createSearchCondition():Invalid template node operator code " + conditionRootNode_p.getOperator());
                        throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));
                    }
                }
            }
            else
            {
                return createBooleanTerm(conditionRootNode_p, context_p);
            }
        }
        else
        {
            return new OwUndefinedPredicate();
        }
    }

    private OwBooleanTerm createBooleanTerm(OwSearchNode termRootNode_p, OwProcessContext context_p) throws OwException
    {
        List criteriaList = termRootNode_p.getChilds();

        if (criteriaList != null && criteriaList.size() != 0)
        {
            OwBooleanTerm lastTerm = null;
            for (int i = 0; i < criteriaList.size(); i++)
            {
                OwSearchNode factorNode = (OwSearchNode) criteriaList.get(i);
                OwBooleanFactor factor = createBooleanFactor(factorNode, context_p);
                if (lastTerm != null)
                {
                    lastTerm = new OwANDBooleanTerm(lastTerm, factor);
                }
                else
                {
                    lastTerm = factor;
                }
            }

            return lastTerm;
        }
        else
        {
            OwBooleanFactor factor = createBooleanFactor(termRootNode_p, context_p);
            return factor;
        }
    }

    private OwBooleanFactor createBooleanFactor(OwSearchNode factorRootNode_p, OwProcessContext context_p) throws OwException
    {
        List criteriaList = factorRootNode_p.getChilds();
        if (criteriaList != null && !criteriaList.isEmpty())
        {
            return new OwSearchConditionBooleanTest(createSearchCondition(factorRootNode_p, context_p));
        }
        else
        {

            // === search node does not contain children, it is just a criteria
            OwSearchCriteria criteria_p = factorRootNode_p.getCriteria();
            OwPredicate predicate = createPredicate(criteria_p, context_p);

            List<OwColumnQualifier> columnQualifiers = predicate.getColumnQualifiers();
            for (OwColumnQualifier qualifier : columnQualifiers)
            {
                String type = qualifier.getTargetObjectType();
                if (type != null)
                {
                    if (!m_entitiesResolver.isSubtable(type, context_p.mainType, context_p.repositoryID))
                    {
                        for (String extraType : context_p.extraTypes)
                        {
                            if (m_entitiesResolver.isSubtable(type, extraType, context_p.repositoryID))
                            {
                                return new OwXBooleanFactor(predicate);
                            }
                        }
                    }
                }
            }

            return predicate;

        }
    }

    /**
     * Create a compare predicate, is called if current predicate is a compare operator.
     * Current compare operators are retrieved by {@link #getSQLComparisonOperatorMap()}.
     * @param criteria_p OwSearchCriteria
     * @param comparisonOperator_p OwComparisonOperator
     * @param context_p {@link OwProcessContext}
     * @return OwComparisonPredicate
     * @throws OwException
     */
    protected OwComparisonPredicate createComparisonPredicate(OwSearchCriteria criteria_p, OwComparisonOperator comparisonOperator_p, OwProcessContext context_p) throws OwException
    {
        Object value = getLimitForComparisonPredicate(criteria_p, comparisonOperator_p, context_p);
        OwValueExpression valueExpression = createValueExpression(criteria_p, context_p);
        OwLiteral literal = createLiteral(criteria_p, value);
        OwComparisonPredicate comparisonPredicate = new OwComparisonPredicate(valueExpression, comparisonOperator_p, literal);
        return comparisonPredicate;
    }

    protected Object getLimitForComparisonPredicate(OwSearchCriteria criteria_p, OwComparisonOperator comparisonOperator_p, OwProcessContext context_p)
    {
        Object value = criteria_p.getValue();
        return value;
    }

    protected abstract OwPredicateFormat getInFormat();

    protected OwInPredicate createInPredicate(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        String className = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, className);
        OwInValueList inListValue = createInValueList(criteria_p, true);
        OwInPredicate inPredicate = new OwInPredicate(columnReference, inListValue, negate_p, getInFormat());
        return inPredicate;
    }

    protected OwLikePredicate createLikePredicate(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        Object value = criteria_p.getValue();
        String stringValue = null;
        if (value != null)
        {
            stringValue = convertWildCards(criteria_p, value.toString());
        }
        OwCharacterStringLiteral characterLiteral = createLiteral(criteria_p, stringValue);

        String className = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, className);

        OwLikePredicate likePredicate = new OwLikePredicate(columnReference, characterLiteral, negate_p, createLikeFormat());
        return likePredicate;
    }

    protected abstract OwPredicateFormat createLikeFormat();

    protected OwNullPredicate createNullPredicate(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        String className = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, className);

        OwNullPredicate nullPredicate = new OwNullPredicate(columnReference, negate_p);
        return nullPredicate;
    }

    protected abstract OwPredicateFormat getQuantifiedInFormat();

    protected OwQuantifiedInPredicate createQuantifiedInPredicate(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        String className = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, className);
        OwInValueList inListValue = createInValueList(criteria_p, false);
        OwQuantifiedInPredicate quantifiedInPredicate = new OwQuantifiedInPredicate(columnReference, inListValue, negate_p, getQuantifiedInFormat());
        return quantifiedInPredicate;
    }

    protected OwBetweenPredicate createBetweenPredicate(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);

        RangeLimits limits = this.getBetweenPredicateRangeLimits(criteria_p, negate_p, context_p);
        Object v1 = limits.getLeftLimit();
        Object v2 = limits.getRightLimit();

        OwLiteral value1Literal = createLiteral(criteria_p, v1);
        OwLiteral value2Literal = createLiteral(criteria_p, v2);

        String className = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, className);

        OwBetweenPredicate betweenPredicate = new OwBetweenPredicate(columnReference, value1Literal, value2Literal, negate_p);
        return betweenPredicate;
    }

    protected RangeLimits getBetweenPredicateRangeLimits(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p)
    {
        OwSearchCriteria secondRangeCriteria = criteria_p.getSecondRangeCriteria();
        Object v1 = criteria_p.getValue();
        Object v2 = secondRangeCriteria.getValue();

        return new RangeLimits(v1, v2);
    }

    private OwPredicate createPredicate(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        if (criteria_p == null)
        {
            return new OwUndefinedPredicate();
        }
        int operator = criteria_p.getOperator();
        if (operator == OwSearchNode.SEARCH_OP_UNDEF)
        {
            return new OwUndefinedPredicate();
        }

        Map<Integer, OwComparisonOperator> operatorMap = getSQLComparisonOperatorMap();
        OwComparisonOperator comparisonOperator = operatorMap.get(operator);
        if (comparisonOperator != null)
        {
            return createComparisonPredicate(criteria_p, comparisonOperator, context_p);
        }
        else
        {
            if (isValidPredicateValue(criteria_p, context_p, operator))
            {
                switch (operator)
                {
                    case OwSearchOperator.CRIT_OP_IS_IN:
                    case OwSearchOperator.CRIT_OP_IS_NOT_IN:
                    {

                        try
                        {
                            OwFieldDefinition fieldDefinition = criteria_p.getFieldDefinition();

                            if (fieldDefinition.isArray())
                            {
                                return createQuantifiedInPredicate(criteria_p, operator == OwSearchOperator.CRIT_OP_IS_NOT_IN, context_p);
                            }
                            else
                            {
                                return createInPredicate(criteria_p, operator == OwSearchOperator.CRIT_OP_IS_NOT_IN, context_p);
                            }
                        }
                        catch (OwException e)
                        {
                            throw e;
                        }
                        catch (Exception e)
                        {
                            LOG.error("OwCSQLCProcessor.retrtieveDefinition():Could not retrieve field definition of search criteria " + criteria_p.getClassName(), e);
                            throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.field.error", "Search field error!"), e);
                        }
                    }
                    case OwSearchOperator.CRIT_OP_IS_ARRAY_IN:
                    case OwSearchOperator.CRIT_OP_IS_ARRAY_NOT_IN:
                    {
                        return createMultiQuantifiedComparisonPredicate(criteria_p, operator == OwSearchOperator.CRIT_OP_IS_ARRAY_NOT_IN, context_p);
                    }
                    case OwSearchOperator.CRIT_OP_LIKE:
                    case OwSearchOperator.CRIT_OP_NOT_LIKE:
                    {
                        return createLikePredicate(criteria_p, operator == OwSearchOperator.CRIT_OP_NOT_LIKE, context_p);
                    }
                    case OwSearchOperator.CRIT_OP_IS_NULL:
                    case OwSearchOperator.CRIT_OP_IS_NOT_NULL:
                    {
                        return createNullPredicate(criteria_p, operator == OwSearchOperator.CRIT_OP_IS_NOT_NULL, context_p);
                    }
                    case OwSearchOperator.CRIT_OP_BETWEEN:
                    case OwSearchOperator.CRIT_OP_NOT_BETWEEN:
                    {
                        return createBetweenPredicate(criteria_p, operator == OwSearchOperator.CRIT_OP_NOT_BETWEEN, context_p);
                    }
                    case OwSearchOperator.CRIT_OP_CBR_IN:
                    case OwSearchOperator.CRIT_OP_CBR_ALL:
                    {
                        if (isMultipleTextSearchSyntax())
                        {
                            switch (operator)
                            {
                                case OwSearchOperator.CRIT_OP_CBR_IN:
                                    return createCBRInPredicate(criteria_p, context_p);
                                case OwSearchOperator.CRIT_OP_CBR_ALL:
                                    return createCBRAllPredicate(criteria_p, context_p);
                                default:
                                {
                                    LOG.error("SQL operator " + operator + " is not processed!");
                                    return new OwUndefinedPredicate();
                                }
                            }
                        }
                        else
                        {
                            return new OwUndefinedPredicate();
                        }
                    }
                    default:
                    {
                        return createExtendedPredicate(criteria_p, context_p, operator);
                    }
                }
            }
            else
            {
                return new OwUndefinedPredicate();
            }
        }
    }

    /**(overridable)
     * This method is called if an unknown or by default unsupported operator is found in the provided OwSearchCriteria.
     * <p>
     *  By default this method is throwing an invalid operation exception, and should
     *  be used to extend the operation capability of the OwCSQLCProcessor.
     * </p>
     * @param criteria_p OwSearchCriteria
     * @param context_p OwProcessContext 
     * @param operator_p int value representing one of the {@link OwSearchOperator}.CRIT_OP_...
     * @return OwPredicate
     * @throws OwException
     */
    protected OwPredicate createExtendedPredicate(OwSearchCriteria criteria_p, OwProcessContext context_p, int operator_p) throws OwException
    {
        LOG.error("OwCSQLCProcessor.createExtendedPredicate():Can not create SQL predicate for operator with code " + operator_p);
        throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));
    }

    protected String createCBRAllExpression(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        //        if (isMultipleTextSearchSyntax())
        //        {
        //            Object value = criteria_p.getValue();
        //            return value.toString();
        //        }
        //        else
        //        {
        //            return "ALL=" + criteria_p.getValue();
        //        }
        LOG.error("OwCSQLCProcessor.createCBRAllExpression(): The CBR_ALL element is not supported by this processor.");
        throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));

    }

    protected OwPredicate createCBRAllPredicate(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        String expression = createCBRAllExpression(criteria_p, context_p);
        return createTextSearchPredicate(createLiteral(criteria_p, expression), null);
    }

    protected abstract OwTextSearchPredicate createTextSearchPredicate(OwCharacterStringLiteral searchExpression_p, OwColumnReference columnReference_p) throws OwException;

    protected String createCBRInExpression(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        //        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        //        String fieldName = createQueryFieldName(fieldDefinition, repositoryID_p);
        //
        //        Object value = criteria_p.getValue();
        //
        //        if (isMultipleTextSearchSyntax())
        //        {
        //            return value.toString();
        //        }
        //        else
        //        {
        //            return fieldName + "=" + value.toString();
        //        }
        LOG.error("OwCSQLCProcessor.createCBRAllExpression(): The CBR_IN element is not supported by this processor.");
        throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));
    }

    protected OwPredicate createCBRInPredicate(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        String queryFieldName = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, queryFieldName);

        String expression = createCBRInExpression(criteria_p, context_p);

        return createTextSearchPredicate(createLiteral(criteria_p, expression), columnReference);
    }

    protected abstract OwPredicateFormat getQuantifiedComparisonFormat();

    private OwPredicate createMultiQuantifiedComparisonPredicate(OwSearchCriteria criteria_p, boolean negate_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        String className = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, className);
        Object value = criteria_p.getValue();
        if (value != null && value.getClass().isArray())
        {
            Object[] valuesArray = (Object[]) value;
            if (valuesArray.length == 0)
            {
                return new OwUndefinedPredicate();
            }
            else
            {
                OwLiteral literal0 = createLiteral(criteria_p, valuesArray[0]);
                OwBooleanTerm condition = new OwQuantifiedComparisonPredicate(literal0, columnReference, negate_p, getQuantifiedComparisonFormat());//createQuantifiedComparisonPredicate(literal0, columnReference, negate_p);
                for (int i = 1; i < valuesArray.length; i++)
                {
                    OwLiteral literal = createLiteral(criteria_p, valuesArray[i]);
                    OwQuantifiedComparisonPredicate qComparison = new OwQuantifiedComparisonPredicate(literal, columnReference, negate_p, getQuantifiedComparisonFormat());
                    if (negate_p)
                    {
                        condition = new OwSearchConditionBooleanTest(new OwORSearchCondition(condition, qComparison));
                    }
                    else
                    {
                        condition = new OwANDBooleanTerm(condition, qComparison);
                    }

                }

                return new OwSearchConditionPredicate(condition);
            }
        }
        else
        {
            OwLiteral literal = createLiteral(criteria_p, value);
            return new OwQuantifiedComparisonPredicate(literal, columnReference, negate_p, getQuantifiedComparisonFormat());
        }
    }

    private OwInValueList createInValueList(OwSearchCriteria criteria_p, boolean splitString_p) throws OwException
    {
        Object value = criteria_p.getValue();
        OwInValueList inValueList = new OwInValueList();
        if (value == null)
        {
            return inValueList;
        }
        else
        {
            if (value.getClass().isArray())
            {
                Object[] arrayValue = (Object[]) value;
                for (int i = 0; i < arrayValue.length; i++)
                {
                    OwLiteral literal = createLiteral(criteria_p, arrayValue[i]);
                    inValueList.addLiteral(literal);
                }
            }
            else
            {
                String strValue = value.toString();
                OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
                if (splitString_p)
                {
                    String[] tokens = strValue.split(",");

                    for (int i = 0; i < tokens.length; i++)
                    {
                        String token = tokens[i];
                        Object valueFromString = valueFromSrting(fieldDefinition, token);
                        OwLiteral literal = createLiteral(criteria_p, valueFromString);
                        inValueList.addLiteral(literal);
                    }
                }
                else
                {
                    Object valueFromString = valueFromSrting(fieldDefinition, strValue);
                    OwLiteral literal = createLiteral(criteria_p, valueFromString);
                    inValueList.addLiteral(literal);
                }
            }

            return inValueList;
        }

    }

    private Object valueFromSrting(OwFieldDefinition definition_p, String aString_p) throws OwException
    {
        try
        {
            return definition_p.getValueFromString(aString_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCSQLCProcessor.valueFromSrting():Could not create value from String  " + aString_p + " through filed definition " + definition_p.getClassName(), e);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.field.error", "Search field error!"), e);
        }
    }

    protected OwFieldDefinition retrieveDefinition(OwSearchCriteria criteria_p) throws OwException
    {
        try
        {
            return criteria_p.getFieldDefinition();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCSQLCProcessor.retrtieveDefinition():Could not retrieve field definition of search criteria " + criteria_p.getClassName(), e);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.field.error", "Search field error!"), e);
        }
    }

    private OwValueExpression createValueExpression(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);
        String className = createQueryFieldName(fieldDefinition, context_p);
        OwColumnQualifier qualifier = createQueryFieldQualifier(fieldDefinition, context_p);
        OwColumnReference columnReference = createColumnReference(qualifier, className);
        return columnReference;
    }

    protected OwSQLDateTimeLiteral createLiteral(OwSearchCriteria criteria_p, Date date_p) throws OwCSQLCException
    {
        return new OwSQLDateTimeLiteral(date_p);
    }

    protected OwSignedNumericLiteral createLiteral(OwSearchCriteria criteria_p, Number number_p)
    {
        return new OwSignedNumericLiteral(number_p);
    }

    protected OwCharacterStringLiteral createLiteral(OwSearchCriteria criteria_p, String string_p)
    {
        return new OwCharacterStringLiteral(string_p);
    }

    protected OwLiteral createDefaultLiteral(OwSearchCriteria criteria_p, Object value_p)
    {
        String stringValue = value_p.toString();
        String wildCardConvertedString = convertWildCards(criteria_p, stringValue);
        return new OwStringImageLiteral(wildCardConvertedString);
    }

    protected OwLiteral createNullLiteral()
    {
        return new OwNullLiteral();
    }

    protected OwLiteral createLiteral(OwSearchCriteria criteria_p, Object value_p) throws OwException
    {
        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);

        if (value_p == null)
        {
            return createNullLiteral();
        }

        if (value_p instanceof Date)
        {
            return createLiteral(criteria_p, (Date) value_p);
        }
        else if (value_p instanceof Number)
        {
            return createLiteral(criteria_p, (Number) value_p);
        }
        else if (!value_p.getClass().isArray())
        {
            if ("java.lang.String".equals(fieldDefinition.getJavaClassName()))
            {
                String stringValue = value_p.toString();
                String wildCardConvertedString = convertWildCards(criteria_p, stringValue);
                return createLiteral(criteria_p, wildCardConvertedString);
            }
            else
            {
                return createDefaultLiteral(criteria_p, value_p);
            }

        }
        else
        {
            LOG.error("OwCSQLCProcessor.createLiteral : Array literals are not valid. Found array value for " + criteria_p.getClassName() + " with operator \"" + OwSearchOperator.getOperatorDisplayString(Locale.ENGLISH, criteria_p.getOperator())
                    + "\" .");
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));
        }

    }

    /**(overridable) 
     * Convert the wildcards from client format
     * into native representation and do also an escaping of 
     * characters matching the native wildcard definition.
     * @param criteria_p OwSearchCriteria
     * @param value_p String
     * @return String representing the escaped value
     */
    protected String convertWildCards(OwSearchCriteria criteria_p, final String value_p)
    {
        String value = escapeStringValue(value_p);
        Collection wdefs = criteria_p.getWildCardDefinitions();
        if (null == wdefs)
        {//return if criteria not allow wild cards
            return value;
        }
        value = escapeNativeWildCardRepresentation(value, wdefs);
        Iterator it = wdefs.iterator();
        while (it.hasNext())
        {
            OwWildCardDefinition def = (OwWildCardDefinition) it.next();

            // replace client wildcard with native wildcard
            value = OwString.replaceAll(value, def.getWildCard(), def.getNativeWildCard());
        }

        return value;
    }

    /**
     * If the value has characters representing native wild cards definitions,
     * they should be escaped (if possible) before the {@link #convertWildCards(OwSearchCriteria, String)}
     * replace all client wild cards with native representation.
     * @param value_p String 
     * @param wildCardDefs Collection current possible wildcards
     * @return String with escaped native wildcard representations
     */
    protected abstract String escapeNativeWildCardRepresentation(String value_p, Collection<OwWildCardDefinition> wildCardDefs);

    /**
     * Map of operators which represent a simple compare function.
     * @return Map of {@link OwSearchOperator}.CRIT_OP... to {@link OwComparisonOperator}
     */
    protected Map<Integer, OwComparisonOperator> getSQLComparisonOperatorMap()
    {
        Map<Integer, OwComparisonOperator> operatorMap = new HashMap<Integer, OwComparisonOperator>();
        operatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL), OwComparisonOperator.EQ);
        operatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS), OwComparisonOperator.LT);
        operatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER), OwComparisonOperator.GT);
        operatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL), OwComparisonOperator.LEQ);
        operatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL), OwComparisonOperator.GEQ);
        operatorMap.put(Integer.valueOf(OwSearchOperator.CRIT_OP_NOT_EQUAL), OwComparisonOperator.NEQ);
        return operatorMap;
    }

    private Set<OwColumnReference> getTableQueryColumns(String class_p, String repositoryID_p) throws OwException
    {
        String firstClassQName = m_entitiesResolver.resolveQueryTableName(class_p, repositoryID_p);

        Set<String> qNames = m_entitiesResolver.resolveQueryableColumnNames(class_p, repositoryID_p);

        Set<OwColumnReference> columns = new LinkedHashSet<OwColumnReference>();

        for (String qName : qNames)
        {
            OwColumnQualifier colmnQualifier = createQueryColumnQualifier(firstClassQName, class_p);
            columns.add(createColumnReference(colmnQualifier, qName));
        }

        return columns;
    }

    protected abstract OwColumnQualifier createQueryColumnQualifier(String tableName_p, String typeName_p);

    protected OwColumnReference createColumnReference(OwColumnQualifier qualifier_p, String columnName_p)
    {
        return new OwColumnReference(qualifier_p, columnName_p);
    }

    private OwExternal<Map<OwRepositoryTarget, List<OwSimpleTable>>> createSimpleTable(OwSearchNode searchRootNode_p, Collection<String> propertyNames_p, int versionSelection_p) throws OwException
    {
        OwExternal<Map<OwRepositoryTarget, Map<String, OwWhereClause>>> whereClausesEx;
        whereClausesEx = createWhereClauses(searchRootNode_p, versionSelection_p);

        Map<OwRepositoryTarget, Map<String, OwWhereClause>> whereClauses;
        whereClauses = whereClausesEx.getInternal();

        Map<OwRepositoryTarget, List<OwSimpleTable>> simpleTables = new HashMap<OwRepositoryTarget, List<OwSimpleTable>>();
        OwExternal<Map<OwRepositoryTarget, List<OwSimpleTable>>> tablesEx = new OwExternal<Map<OwRepositoryTarget, List<OwSimpleTable>>>(simpleTables, whereClausesEx);
        Set<Entry<OwRepositoryTarget, Map<String, OwWhereClause>>> whereEntries = whereClauses.entrySet();
        for (Entry<OwRepositoryTarget, Map<String, OwWhereClause>> whereEntry : whereEntries)
        {
            OwRepositoryTarget repositoryTarget = whereEntry.getKey();
            String repositoryID = repositoryTarget.getRepositoryId();
            List<OwFromClause> fromClauses = createFromClauses(searchRootNode_p, repositoryID);
            for (OwFromClause fromClause : fromClauses)
            {
                OwColumnQualifier mainTableQ = fromClause.getMainTableQualifier();

                Map<String, OwWhereClause> whereMap = whereEntry.getValue();
                OwWhereClause where = whereMap.get(mainTableQ.getTargetObjectType());
                if (!where.isXClause())
                {
                    OwSelectList selectList;
                    Set<OwColumnReference> columnNames = new LinkedHashSet<OwColumnReference>();

                    if (propertyNames_p != null)
                    {
                        for (String propertyName : propertyNames_p)
                        {
                            OwColumnQualifier columnQ = createColumnQualifier(propertyName, repositoryID);
                            if (columnQ == null)
                            {
                                columnQ = mainTableQ;
                            }
                            String qName = createQueryPropertyName(mainTableQ.getTargetObjectType(), propertyName, repositoryID);
                            //                            if (m_entitiesResolver.isSubtable(columnQ.getTargetObjectType(), mainTableQ.getTargetObjectType(), repositoryID))
                            {
                                if (qName != null)
                                {
                                    columnNames.add(createColumnReference(columnQ, qName));
                                }
                                else
                                {
                                    LOG.error("OwCSQLCProcessor.createSimpleTable(): the property " + propertyName + " is not queryable! It will be ommited from the query!");
                                }
                            }
                        }
                    }

                    Set<OwColumnReference> tableColumns = getTableQueryColumns(mainTableQ.getTargetObjectType(), repositoryID);
                    columnNames.addAll(tableColumns);

                    if (columnNames.isEmpty())
                    {
                        selectList = new OwSelectAll();
                    }
                    else
                    {
                        OwCompoundSelectList compoundSelectList = new OwCompoundSelectList();

                        for (OwColumnReference columnReference : columnNames)
                        {
                            OwSelectSublist subList = new OwSelectSublist(columnReference);
                            compoundSelectList.add(subList);
                        }

                        selectList = compoundSelectList;
                    }

                    List<OwSimpleTable> tables = simpleTables.get(whereEntry.getKey());
                    if (tables == null)
                    {
                        tables = new LinkedList<OwSimpleTable>();
                        simpleTables.put(whereEntry.getKey(), tables);
                    }
                    tables.add(createSimpleTable(selectList, fromClause, where));
                }
            }
        }

        return tablesEx;

    }

    private Set<OwColumnQualifier> enforceQualifiers(List<OwColumnQualifier> qualifiers_p, OwColumnQualifier mainTable_p, String respositoryID_p, boolean qualifySingleTable_p) throws OwException
    {
        createUniqueQualifiers(qualifiers_p, mainTable_p, qualifySingleTable_p);
        Set<OwColumnQualifier> nQualifiers = normalizeQualifiers(qualifiers_p, respositoryID_p);
        if (nQualifiers.size() == 1 && !qualifySingleTable_p)
        {
            for (OwColumnQualifier qualifier : qualifiers_p)
            {
                qualifier.setQualifierString(null);
            }
        }
        else if (nQualifiers.size() == 1)
        {
            OwColumnQualifier oneQualifier = nQualifiers.iterator().next();
            oneQualifier.setQualifierString("a");
        }
        return nQualifiers;
    }

    private Set<OwColumnQualifier> normalizeQualifiers(List<OwColumnQualifier> qualifers_p, String respositoryID_p) throws OwException
    {
        Set<OwColumnQualifier> normalizedQualifiers = new LinkedHashSet<OwColumnQualifier>();
        for (OwColumnQualifier qualifier : qualifers_p)
        {
            String qTable = qualifier.getTargetTable();
            Set<OwColumnQualifier> normalizedTablesCopy = new LinkedHashSet<OwColumnQualifier>(normalizedQualifiers);
            boolean ancestryEstablished = false;
            for (OwColumnQualifier nQualifier : normalizedTablesCopy)
            {
                String nQString = nQualifier.getQualifierString();
                String nQTable = nQualifier.getTargetTable();
                if (!nQTable.equals(qTable))
                {
                    if (m_entitiesResolver.isSubtable(nQualifier.getTargetObjectType(), qualifier.getTargetObjectType(), respositoryID_p))
                    {
                        qualifier.setQualifierString(nQString);
                        normalizedQualifiers.remove(nQualifier);
                        normalizedQualifiers.add(qualifier);
                        ancestryEstablished = true;
                    }
                    else if (m_entitiesResolver.isSubtable(qualifier.getTargetObjectType(), nQualifier.getTargetObjectType(), respositoryID_p))
                    {
                        qualifier.setQualifierString(nQString);
                        ancestryEstablished = true;
                    }
                }
                else
                {
                    qualifier.setQualifierString(nQString);
                    ancestryEstablished = true;
                }

                if (ancestryEstablished)
                {
                    break;
                }
            }

            if (!ancestryEstablished)
            {
                normalizedQualifiers.add(qualifier);
            }
        }

        return normalizedQualifiers;
    }

    private void createUniqueQualifiers(List<OwColumnQualifier> qualifers_p, OwColumnQualifier mainTable_p, boolean qualifySingleTable_p)
    {
        Map<String, String> tableToQ = new HashMap<String, String>();

        List<OwColumnQualifier> defaults = new LinkedList<OwColumnQualifier>();
        String defaultQualifier = null;
        defaults.add(mainTable_p);

        int nextQualifierIndex = 0;
        for (OwColumnQualifier qualifier : qualifers_p)
        {
            String table = qualifier.getTargetTable();
            if (table != null && (qualifySingleTable_p || !table.equals(mainTable_p.getTargetTable())))
            {
                String q = tableToQ.get(table);
                if (q == null)
                {
                    q = "" + Character.valueOf((char) ('b' + nextQualifierIndex));
                    tableToQ.put(table, q);
                    nextQualifierIndex++;

                    if (defaultQualifier == null)
                    {
                        defaultQualifier = "a";
                        for (OwColumnQualifier defaultQ : defaults)
                        {
                            defaultQ.setQualifierString(defaultQualifier);
                        }
                    }
                }

                qualifier.setQualifierString(q);
            }
            else
            {
                if (table == null)
                {
                    qualifier.setTargetTable(mainTable_p.getTargetTable());
                    qualifier.setTargetObjectType(mainTable_p.getTargetObjectType());
                }

                if (defaultQualifier != null)
                {
                    qualifier.setQualifierString(defaultQualifier);
                }
                defaults.add(qualifier);
            }
        }

    }

    protected final Map<String, List<OwClass>> findSelectedObjectClasses(OwSearchNode searchRootNode_p, String criteriaName_p) throws OwException
    {
        OwSearchNode specialNode = searchRootNode_p.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
        List specialChildren = specialNode.getChilds();
        Iterator itSpecial = specialChildren.iterator();
        Map<String, List<OwClass>> typedClasses = new LinkedHashMap<String, List<OwClass>>();

        while (itSpecial.hasNext())
        {
            OwSearchNode node = (OwSearchNode) itSpecial.next();
            OwSearchCriteria crit = node.getCriteria();

            if (crit.getUniqueName().equals(criteriaName_p))
            {
                Object[] clazz = (Object[]) crit.getValue();
                for (int i = 0; i < clazz.length; i++)
                {
                    if (clazz[i] instanceof OwClass)
                    {
                        OwClass classValue = (OwClass) clazz[i];
                        if (classValue.isEnabled())
                        {
                            String baseClassName = classValue.getBaseClassName();

                            if (OwObjectReference.OBJECT_TYPE_UNDEFINED == classValue.getObjectType())
                            {
                                baseClassName = UNKNOWN_BASE_CLASS;
                            }

                            List<OwClass> classes = typedClasses.get(baseClassName);
                            if (classes == null)
                            {
                                classes = new LinkedList<OwClass>();
                                typedClasses.put(baseClassName, classes);
                            }
                            classes.add(classValue);
                        }
                    }
                    else
                    {
                        LOG.error("OwCSQLCProcessor.findSelectedObjectClasses : invalid class criteria value type " + clazz[i]);
                        throw new OwInvalidOperationException("invalid class criteria value type.");
                    }
                }
            }
        }

        return typedClasses;

    }

    protected String getMainObjectCriteria()
    {
        return OwStandardClassSelectObject.CLASS_NAME;
    }

    protected final Map<String, List<OwClass>> findMainObjectClasses(OwSearchNode searchRootNode_p) throws OwException
    {
        return findSelectedObjectClasses(searchRootNode_p, getMainObjectCriteria());
    }

    private List<OwFromClause> createFromClauses(OwSearchNode searchRootNode_p, String repositoryID_p) throws OwException
    {
        Map<String, List<OwClass>> classes = findMainObjectClasses(searchRootNode_p);

        if (classes.isEmpty())
        {
            LOG.error("OwCSQLCProcessor.createFromClauses():no <from> search class was specified !");
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwCSQLCProcessor.invalid.template.error", "Invalid search template!"));
        }

        Set<Entry<String, List<OwClass>>> classEntries = classes.entrySet();

        List<OwFromClause> fromClauses = new LinkedList<OwFromClause>();

        for (Entry<String, List<OwClass>> classEntry : classEntries)
        {
            for (OwClass aClass : classEntry.getValue())
            {
                OwCorrelatedTableName correlatedTableName = createCorrelatedTableName(aClass.getClassName(), repositoryID_p);
                OwFromClause fromClause = new OwFromClause(correlatedTableName);

                fromClauses.add(fromClause);
            }
        }

        return fromClauses;
    }

    private OwCorrelatedTableName createCorrelatedTableName(String class_p, String repositoryID_p) throws OwException
    {
        String tableQueryName = m_entitiesResolver.resolveQueryTableName(class_p, repositoryID_p);
        if (tableQueryName == null)
        {
            LOG.error("OwCSQLCProcessor.createCorrelatedTableName():Object class " + class_p + " is not queryable!");
            throw new OwInvalidOperationException(new OwString1("ecmimpl.OwCSQLCProcessor.non.queryable.table.error", "Invalid searched class! The class %1 can not be queried!", class_p));
        }
        OwColumnQualifier qualifier = createFromTableQualifier(tableQueryName, class_p);
        OwCorrelatedTableName correlatedTableName = new OwCorrelatedTableName(tableQueryName, qualifier);
        return correlatedTableName;

    }

    protected abstract OwColumnQualifier createFromTableQualifier(String tableName_p, String typeName_p);

    /**
     * String value escaping depending the definition of SQL part.
     * <p>ATTENTION: This method is called by {@link #convertWildCards(OwSearchCriteria, String)}
     * before checking if the wild cards are allowed for search criteria or not.
     * Wildcard escaping is done by the {@link #convertWildCards(OwSearchCriteria, String)} method. 
     * </p>
     * @param value_p String to be escaped
     * @return String representing the escaped sequence
     */
    protected String escapeStringValue(String value_p)
    {
        String escaped = value_p.replaceAll("\\\\", "\\\\\\\\");
        escaped = escaped.replaceAll("'", "\\\\'");

        return escaped;
    }

    /** (overridable)
     * Simple method called during {@link #createPredicate(OwSearchCriteria, OwProcessContext)} process
     * to quickly verify if the criteria value is good enough for a specific predicate/operator.
     * <p>
     *  Hint: This check should be kept lightweight (e.g. just verifying non-null values)!<br />
     *  For specific checks use the {@link OwPredicate#isValid()} method.
     * </p>
     * @param criteria_p OwSearchCriteria 
     * @param context_p {@link OwProcessContext}
     * @param operator_p int representing on of {@link OwSearchOperator}.CRIT_OP_...
     * @return boolean true if predicate should be generated for given criteria
     */
    protected boolean isValidPredicateValue(OwSearchCriteria criteria_p, OwProcessContext context_p, int operator_p)
    {
        switch (operator_p)
        {
            case OwSearchOperator.CRIT_OP_IS_IN:
            case OwSearchOperator.CRIT_OP_IS_NOT_IN:
            case OwSearchOperator.CRIT_OP_IS_ARRAY_IN:
            case OwSearchOperator.CRIT_OP_IS_ARRAY_NOT_IN:
            case OwSearchOperator.CRIT_OP_IS_NULL:
            case OwSearchOperator.CRIT_OP_IS_NOT_NULL:
            case OwSearchOperator.CRIT_OP_BETWEEN:
            case OwSearchOperator.CRIT_OP_NOT_BETWEEN:
                return true;
            default:
                return !(criteria_p.getValue() == null || "".equals(criteria_p.getValue()));

        }
    }

    /**(overridable)
     * Factory method for OwSimpleTable object, which represents a SQL-statement object.
     * Creation of instance also consider current {@link #getMaxRows()} definition.
     * @param selectList OwSelectList select property definitions
     * @param fromClause OwFromClause SQL-from definition
     * @param where OwWhereClause SQL-where definition, can be null
     * @return OwSimpleTable
     * @since 3.2.0.2
     * @see #setMaxRows(Integer)
     * @see #getMaxRows()
     */
    protected OwSimpleTable createSimpleTable(OwSelectList selectList, OwFromClause fromClause, OwWhereClause where)
    {
        if (getMaxRows() == null)
        {
            return new OwSimpleTable(selectList, fromClause, where);
        }
        else
        {
            return new OwSimpleTable(selectList, fromClause, where, getMaxRows());
        }
    }

    /**
     * Set max rows, if ResultSet size should be controlled through generated SQL statement.
     * <p>No verification done for value, negative values are also accepted</p>
     * @param rows Integer can be null
     * @since 3.2.0.2
     */
    public void setMaxRows(Integer rows)
    {
        this.maxRows = rows;
    }

    /**
     * Get the max row definition.
     * @return Integer or null
     * @since 3.2.0.2
     */
    public Integer getMaxRows()
    {
        return maxRows;
    }

    /**
     *<p>
     * Just a conveyor belt for two values.
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
     *@since 4.2.0.0
     */
    protected class RangeLimits
    {
        private Object leftLimit;
        private Object rightLimit;

        public RangeLimits(Object leftLimit, Object rightLimit)
        {
            this.leftLimit = leftLimit;
            this.rightLimit = rightLimit;
        }

        public Object getLeftLimit()
        {
            return leftLimit;
        }

        public Object getRightLimit()
        {
            return rightLimit;
        }
    }
}
