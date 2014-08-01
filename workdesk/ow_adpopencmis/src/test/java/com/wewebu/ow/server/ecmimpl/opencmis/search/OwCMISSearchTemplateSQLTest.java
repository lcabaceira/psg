package com.wewebu.ow.server.ecmimpl.opencmis.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.OwCSQLCProcessor;
import com.wewebu.ow.csqlc.OwSQLEntitiesResolver;
import com.wewebu.ow.csqlc.ast.OwExternal;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.opencmis.server.OwCMISSearchTestFieldDefinition;
import com.wewebu.ow.server.ecmimpl.opencmis.server.OwWildCardHelper;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.util.OwTimeZoneInfo;
import com.wewebu.ow.unittest.search.OwSearchTemplateLoader;
import com.wewebu.ow.unittest.search.OwSearchTestFieldDefinition;
import com.wewebu.ow.unittest.search.OwSearchTestFieldDefinitionProvider;
import com.wewebu.ow.unittest.util.AbstractNetworkContextAdapter;

public abstract class OwCMISSearchTemplateSQLTest extends TestCase implements OwSQLEntitiesResolver
{
    private static final Logger LOG = Logger.getLogger(OwCMISSearchTemplateSQLTest.class);

    public static final String CMIS_DOCUMENT = "cmis:document";

    public static final OwSearchTestFieldDefinition ANY = new OwCMISSearchTestFieldDefinition("*", String.class);
    public static final OwSearchTestFieldDefinition CMIS_NAME = new OwCMISSearchTestFieldDefinition(CMIS_DOCUMENT + ".cmis:name", String.class);
    public static final OwSearchTestFieldDefinition OW_OBJECTNAME = new OwCMISSearchTestFieldDefinition("OW_ObjectName", String.class);
    public static final OwSearchTestFieldDefinition CM_AUTHOR = new OwCMISSearchTestFieldDefinition("cm:author.cm:author", String.class);
    public static final OwSearchTestFieldDefinition CM_TITLE = new OwCMISSearchTestFieldDefinition("cm:titled.cm:title", String.class);
    public static final OwSearchTestFieldDefinition CMIS_PERSONAL_NUMBER = new OwCMISSearchTestFieldDefinition(CMIS_DOCUMENT + ".cmis:personalNumber", Long.class);
    public static final OwSearchTestFieldDefinition CMIS_DEPARTMENT_NAME = new OwCMISSearchTestFieldDefinition(CMIS_DOCUMENT + ".cmis:departmentName", String.class);
    public static final OwSearchTestFieldDefinition CMIS_OFFICE_NUMBER = new OwCMISSearchTestFieldDefinition(CMIS_DOCUMENT + ".cmis:officeNumber", Long.class);
    public static final OwSearchTestFieldDefinition CMIS_LAST_MODIFICATION_TIME = new OwCMISSearchTestFieldDefinition(CMIS_DOCUMENT + ".cmis:lastModificationTime", Date.class);
    public static final OwSearchTestFieldDefinition CMIS_TOPICS = new OwCMISSearchTestFieldDefinition(CMIS_DOCUMENT + ".cmis:topics", String.class, true);
    public static final OwSearchTestFieldDefinition CMIS_LAST_MODIFIED_BY = new OwCMISSearchTestFieldDefinition(CMIS_DOCUMENT + ".cmis:lastModifiedBy", String.class);

    public static final String WS_ARTICLE = "ws:article";
    public static final String WS_BLOGPOST = "ws:blogPost";
    public static final String WS_PAPER = "ws:paper";
    public static final String WS_JOURNAL = "ws:journal";

    public static final OwSearchTestFieldDefinition WS_REVIEWERS_TF = new OwCMISSearchTestFieldDefinition(WS_PAPER + ".ws:reviewers", String.class);
    public static final OwSearchTestFieldDefinition WS_SYNOPSIS_TF = new OwCMISSearchTestFieldDefinition(WS_PAPER + ".ws:synopsis", String.class);
    public static final OwSearchTestFieldDefinition WS_JOURNAL_TF = new OwCMISSearchTestFieldDefinition(WS_ARTICLE + ".ws:journal", String.class);
    public static final OwSearchTestFieldDefinition WS_URL_TF = new OwCMISSearchTestFieldDefinition(WS_BLOGPOST + ".ws:url", String.class);

    public static final OwSearchTestFieldDefinitionProvider CMIS_SEARCH_TEST_DEFINITION_PROVIDER;

    public static final Map<String, List<String>> CHILD_RELATIONSHIPS = new HashMap<String, List<String>>();

    static
    {
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER = new OwSearchTestFieldDefinitionProvider() {
            @SuppressWarnings("unchecked")
            @Override
            public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int op_p) throws Exception
            {
                LinkedList lst = null;
                if (op_p == OwSearchOperator.CRIT_OP_LIKE || op_p == OwSearchOperator.CRIT_OP_NOT_LIKE)
                {
                    lst = new LinkedList();
                    lst.add(new OwWildCardHelper("*", "%", OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR));
                    lst.add(new OwWildCardHelper("?", "_", OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR));
                }
                return lst;
            }
        };
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CMIS_NAME);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(OW_OBJECTNAME);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CMIS_PERSONAL_NUMBER);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CMIS_DEPARTMENT_NAME);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CMIS_TOPICS);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CMIS_OFFICE_NUMBER);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CMIS_LAST_MODIFICATION_TIME);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CMIS_LAST_MODIFIED_BY);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CM_AUTHOR);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(CM_TITLE);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(WS_REVIEWERS_TF);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(WS_SYNOPSIS_TF);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(WS_JOURNAL_TF);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(WS_URL_TF);
        CMIS_SEARCH_TEST_DEFINITION_PROVIDER.add(ANY);

        // child relationships 

        CHILD_RELATIONSHIPS.put(CMIS_DOCUMENT, Arrays.asList(WS_PAPER));
        CHILD_RELATIONSHIPS.put(WS_PAPER, Arrays.asList(WS_ARTICLE, WS_JOURNAL, WS_BLOGPOST));
    }

    private String m_defaultRepositoryID;

    protected void assertTemplateSQL(String expectedSQLStatement_p, String localClassPathTemplate_p) throws Exception
    {
        assertTemplateSQL("", expectedSQLStatement_p, localClassPathTemplate_p);
    }

    protected void assertTemplateSQL(String[] expectedSQLStatements_p, String localClassPathTemplate_p) throws Exception
    {
        assertTemplateSQL("", expectedSQLStatements_p, localClassPathTemplate_p);
    }

    protected void assertTemplateSQL(String message_p, String expectedSQLStatement_p, String localClassPathTemplate_p) throws Exception
    {
        assertTemplateSQL(message_p, new String[] { expectedSQLStatement_p }, localClassPathTemplate_p);
    }

    protected void assertTemplateSQL(String message_p, String[] expectedSQLStatements_p, String localClassPathTemplate_p) throws Exception
    {
        OwSearchTemplate template = new OwSearchTemplateLoader(getClass()).loadLocalClassPathTemplate(localClassPathTemplate_p, CMIS_SEARCH_TEST_DEFINITION_PROVIDER);
        OwSearchNode search = template.getSearch(true);
        OwSort sort = template.getSort(10);
        List<String> sqls = createSQL(search, sort);
        if (sqls == null && expectedSQLStatements_p != null)
        {
            fail("null actual SQL when non null value was expected !");
        }
        else
        {
            LOG.debug("SQL expected vs. actual : ");
            for (String sql : expectedSQLStatements_p)
            {
                LOG.debug("E>" + sql.trim());
            }
            LOG.debug("----- vs. -----");
            for (String sql : sqls)
            {
                LOG.debug("A>" + sql.trim());
            }

            if (sqls.size() != expectedSQLStatements_p.length)
            {
                fail("Expected " + expectedSQLStatements_p.length + " statements but got " + sqls.size());
            }

            for (int i = 0; i < expectedSQLStatements_p.length; i++)
            {
                assertEquals(message_p + "\n" + "SQL#" + i, expectedSQLStatements_p[i], sqls.get(i));
            }
        }
    }

    protected abstract OwCSQLCProcessor createProcessor();

    protected List<String> createSQL(OwSearchNode searchNode_p, OwSort sort_p) throws Exception
    {
        OwCSQLCProcessor processor = createProcessor();
        OwExternal<List<OwQueryStatement>> statementsEx = processor.createSQLStatements(searchNode_p, null, sort_p);
        List<OwQueryStatement> statements = statementsEx.getInternal();
        List<String> sql = new LinkedList<String>();
        int count = statements == null ? 0 : statements.size();
        LOG.debug("OwCMISSearchNodeSQLProcessorTest.createSQL():" + count + " statements created : ");
        if (statements != null)
        {
            for (OwQueryStatement statement : statements)
            {
                LOG.debug("OwCMISSearchNodeSQLProcessorTest.createSQL():\t target " + statement.getTargetRepositoryID());
                sql.add(statement.createSQLString().toString());
            }
        }
        Collections.sort(sql);
        return sql;

    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        m_defaultRepositoryID = "";
    }

    //Test entities resolver 
    public String resolveQueryFolderId(String resourceID_p, String pathName_p)
    {
        return "/" + resourceID_p + pathName_p;
    }

    public Set<String> resolveQueryableColumnNames(String tableName_p, String resourceID_p)
    {
        return new HashSet<String>();
    }

    public String resolveQueryTableName(String tableName_p, String resourceID_p) throws OwException
    {
        return tableName_p + resourceID_p;
    }

    public boolean canOrderBy(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        return true;
    }

    public String resolveDefaultRepositoryID() throws OwException
    {
        return m_defaultRepositoryID;
    }

    public boolean isInternalRepositoryID(String repositoryID_p) throws OwException
    {
        return true;
    }

    public String resolveRepositoryID(String repositoryName_p) throws OwException
    {
        if (repositoryName_p.length() > 2)
        {
            return repositoryName_p.substring(0, 2);
        }
        else
        {
            throw new OwObjectNotFoundException("Could not find repository named " + repositoryName_p);
        }
    }

    public boolean isSubtable(String parentTable_p, String childTable_p, String repositoryID_p)
    {
        if (parentTable_p.equals(childTable_p) || OwResource.m_ObjectNamePropertyClass.getClassName().equals(parentTable_p) || OwResource.m_ObjectNamePropertyClass.getClassName().equals(childTable_p))
        {
            return true;
        }
        else
        {
            List<String> children = CHILD_RELATIONSHIPS.get(parentTable_p);
            if (children != null)
            {
                for (String child : children)
                {
                    if (isSubtable(child, childTable_p, repositoryID_p))
                    {
                        return true;
                    }
                }
            }

            return false;

        }

    }

    public boolean isVersionable(String tableName_p, String repositoryID_p) throws OwException
    {
        return false;
    }

    @Override
    public String resovleQueryColumnName(String tableName_p, String columnName_p, String repositoryID_p)
    {
        int dotIndex = columnName_p.indexOf('.');
        if (dotIndex == -1)
        {
            if (OwResource.m_ObjectNamePropertyClass.getClassName().equalsIgnoreCase(columnName_p))
            {
                return "cmis:name";
            }
            else
            {
                return columnName_p + repositoryID_p;
            }
        }
        else
        {
            String column = columnName_p.substring(dotIndex + 1, columnName_p.length()) + repositoryID_p;
            return column;
        }
    }

    public OwNetworkContext getNetworkContext()
    {
        return new AbstractNetworkContextAdapter() {

            public OwTimeZoneInfo getClientTimeZoneInfo()
            {
                return null;
            }

            public TimeZone getClientTimeZone()
            {
                return TimeZone.getTimeZone("UTC");
            }

        };
    }

}
