package com.wewebu.ow.server.ecmimpl.fncm5.search;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.wewebu.ow.csqlc.OwCSQLCException;
import com.wewebu.ow.csqlc.OwCSQLCProcessor;
import com.wewebu.ow.csqlc.OwSQLEntitiesResolver;
import com.wewebu.ow.csqlc.ast.OwExternal;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.csqlc.ast.OwSQLDateTimeLiteral;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwFNCM5CSQLCProcessorTest.
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
public class OwFNCM5CSQLCProcessorTest extends OwFNCM5SearchTemplateSQLTest implements OwSQLEntitiesResolver
{
    private static final Logger LOG = Logger.getLogger(OwFNCM5SearchTemplateSQLTest.class);

    private static final Map<String, String> FOLDER_ID_MAP = new HashMap<String, String>();

    static
    {
        FOLDER_ID_MAP.put("/test", "/test:{E2C5B7B7-D67E-1111-B8FF-6D0000B9EAFE}");
        FOLDER_ID_MAP.put("/rest", "/rest:{E2C5B7B7-D67E-1111-B8FF-6D0000B9EAFE}");
        FOLDER_ID_MAP.put("/Test_3790", "/Test_3790:{99FCB35C-F4C5-475C-B064-6E87A06EE19A}");
    }

    private String m_defaultRepositoryID;

    @Override
    protected List<String> createSQL(OwSearchNode searchNode_p, List<String> propertyNames_p, OwSort sort_p, int versionSelection_p) throws Exception
    {
        OwCSQLCProcessor processor = new OwFNCM5CSQLCProcessor(this, TimeZone.getDefault());

        OwExternal<List<OwQueryStatement>> statementsEx = processor.createSQLStatements(searchNode_p, propertyNames_p, sort_p, versionSelection_p);
        List<OwQueryStatement> statements = statementsEx.getInternal();
        List<String> sql = new LinkedList<String>();
        int count = statements == null ? 0 : statements.size();
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwSearchNodeSQLProcessorTest.createSQL():" + count + " statements created : ");
        }

        if (statements != null)
        {
            for (OwQueryStatement statement : statements)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwSearchNodeSQLProcessorTest.createSQL():\t target " + statement.getTargetRepositoryID());
                }
                sql.add(statement.createSQLString().toString());
            }
        }
        return sql;

    }

    @Before
    public void setUp() throws Exception
    {
        m_defaultRepositoryID = "P8ConfigObjectStore";
    }

    //Test entities resolver 
    public String resolveQueryFolderId(String resourceID_p, String pathName_p)
    {
        return FOLDER_ID_MAP.get(pathName_p);
    }

    public String resovleQueryColumnName(String columnName_p, String resourceID_p)
    {
        return columnName_p;
    }

    @Override
    public String resovleQueryColumnName(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        if (OwResource.m_ObjectNamePropertyClass.getClassName().equals(columnName_p))
        {
            return "document".equalsIgnoreCase(tableName_p) ? "DocumentTitle" : "FolderName";
        }
        else
        {
            return columnName_p;
        }
    }

    public Set<String> resolveQueryableColumnNames(String tableName_p, String resourceID_p)
    {
        return Collections.singleton("This");
    }

    public String resolveQueryTableName(String tableName_p, String resourceID_p) throws OwException
    {
        return tableName_p;
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
            throw new OwObjectNotFoundException("COuld not find repository named " + repositoryName_p);
        }
    }

    public boolean isSubtable(String parentTable_p, String childTable_p, String repositoryID_p)
    {
        if (parentTable_p == null)
        {
            return true;
        }
        else
        {
            //TODO: check for subclassing
            return false;
        }
    }

    private String toSQLDate(String date_p) throws OwCSQLCException
    {
        Date date = OwSearchTestFieldDefinition.toDate(date_p);
        OwSQLDateTimeLiteral literal = new OwSQLDateTimeLiteral(date);
        return literal.createLiteralSQLString().toString();
    }

    public boolean isVersionable(String tableName_p, String repositoryID_p) throws OwException
    {
        return !FOLDER.equals(tableName_p) && !EVENT.equals(tableName_p);
    }

    //---
    @Test
    public void testTemplate1() throws Exception
    {
        String date1 = toSQLDate("2000-10-17T00:00:00.000+03:00");
        String date2 = toSQLDate("2010-10-17T07:35:22.345+03:00");
        assertTemplateSQL("SELECT [DocumentTitle],[DateCreated],[This] FROM document WHERE (([DocumentTitle] LIKE '%project%' AND ([DateCreated]>=" + date1 + " AND [DateCreated]<=" + date2
                + ") OR [DocumentTitle]='woohoo' OR [OfficeNumber]<2231233 OR [OfficeNumber]<=6 OR [Progress]>0.678 OR [Progress]>=0.6) AND ([VersionStatus]=1)) ORDER BY [DocumentTitle] ASC,[DateCreated] DESC", "testTemplate_1.xml");
    }

    @Test
    public void testTemplate2() throws Exception
    {
        String date1 = toSQLDate("2012-10-17T07:35:22.345Z");
        String date2 = toSQLDate("2010-10-17T07:35:22.345Z");
        String date3 = toSQLDate("2000-10-17T03:00:00.000+03:00");
        assertTemplateSQL("SELECT [DocumentTitle],[This] FROM Document WHERE ((NOT [DocumentTitle] LIKE '%project%' AND ([DateCreated]<" + date3 + " OR [DateCreated]>" + date2 + ") AND [DateCreated]<=" + date1
                + " OR [DocumentTitle]<>'huooo') AND ([IsCurrentVersion]=true))", "testTemplate_2.xml");
    }

    @Test
    public void testTemplate3() throws Exception
    {
        assertTemplateSQL("SELECT a.[To],a.[SimulationGUID],a.[DateCreated],a.[This] FROM Document AS a WHERE (ISCLASS(a,[Simulation]) OR ISCLASS(a,[Email])) AND (((a.[To] LIKE '%boo%' OR a.[SimulationGUID]='2') AND (a.[VersionStatus]=3)))",
                "testTemplate_3.xml");
    }

    @Test
    public void testTemplate4() throws Exception
    {
        assertTemplateSQL(
                "SELECT a.[To],a.[SimulationGUID],a.[DateCreated],a.[This] FROM Document AS a WHERE (ISCLASS(a,[Email])) AND ((( ( 'in1' IN a.[To] AND 'in2' IN a.[To] AND 'in3' IN a.[To] )  OR  ( (NOT 'notin1' IN a.[To] OR NOT 'notin2' IN a.[To]) )  OR a.[DocumentTitle] IN ('isin1','isin2','isin3') OR NOT a.[DocumentTitle] IN ('ini1','ini2')) AND (a.[VersionStatus]=2)))",
                "testTemplate_4.xml");
    }

    @Test
    public void testTemplate5() throws Exception
    {
        assertTemplateSQL("SELECT [To],[DateCreated],[This] FROM Email WHERE (([From] IS NULL OR [To] IS NULL OR [EmailSubject] IS NOT NULL) AND ([MinorVersionNumber]=0))", "testTemplate_5.xml");
    }

    @Test
    public void testTemplate6() throws Exception
    {
        assertTemplateSQL("SELECT [DocumentTitle],[This] FROM Document WHERE ([This] INFOLDER '/test:{E2C5B7B7-D67E-1111-B8FF-6D0000B9EAFE}' OR [This] INSUBFOLDER '/rest:{E2C5B7B7-D67E-1111-B8FF-6D0000B9EAFE}') AND (([MinorVersionNumber]<>0))",
                "testTemplate_6.xml");
    }

    @Test
    public void testTemplate7() throws Exception
    {
        assertTemplateSQL("SELECT a.[DocumentTitle],a.[This] FROM Document AS a INNER JOIN ContentSearch AS b ON a.[This]=b.[QueriedObject] WHERE ((a.[DocumentTitle] LIKE '%project%' OR CONTAINS(a.[DocumentTitle],'boo') OR CONTAINS(a.[*],'foo')))",
                "testTemplate_7.xml");
    }

    @Test
    public void testTemplate8() throws Exception
    {
        assertTemplateSQL("SELECT a.[DocumentTitle],a.[This] FROM Document AS a INNER JOIN ContentSearch AS b ON a.[This]=b.[QueriedObject] WHERE (((a.[DocumentTitle] LIKE '%project%') AND (a.[VersionStatus]=1)) AND (CONTAINS(a.[*],'MyC0ntent')))",
                "testTemplate_8.xml");
    }

    @Test
    public void testTemplate9() throws Exception
    {
        String date = toSQLDate("2000-10-17T03:00:00.000+03:00");
        assertTemplateSQL("SELECT a.[OW_HIST_Time],a.[OW_HIST_ModifiedProperties],a.[This] FROM Event AS a WHERE (ISOFCLASS(a,[SpecialEvent])) AND ((a.[OW_HIST_Time]>=" + date + ")) ORDER BY a.[OW_HIST_Time] DESC", "testTemplate_9.xml");
    }

    @Test
    public void testTemplate10() throws Exception
    {
        String date1 = toSQLDate("2000-10-17T00:00:00.000+03:00");
        String date2 = toSQLDate("2010-10-17T07:35:22.345+03:00");
        String[] results = new String[] {
                "SELECT a.[DateCreated],a.[This] FROM Document AS a WHERE (ISCLASS(a,[Document])) AND ((((a.[DateCreated]>=" + date1 + " AND a.[DateCreated]<=" + date2 + ")) AND (a.[VersionStatus]=1))) ORDER BY a.[DateCreated] DESC",
                "SELECT a.[DateCreated],a.[This] FROM Folder AS a WHERE (ISCLASS(a,[Folder])) AND ((((a.[DateCreated]>=" + date1 + " AND a.[DateCreated]<=" + date2 + ")))) ORDER BY a.[DateCreated] DESC" };
        assertTemplateSQL(results, "testTemplate_10.xml");
    }

    @Test
    public void testTemplate11() throws Exception
    {
        String[] results = new String[] {
                "SELECT a.[DeleteStatusFlag],a.[This] FROM Document AS a WHERE (ISCLASS(a,[DocMan_DealDoc])) AND ((a.[This] INSUBFOLDER '/Test_3790:{99FCB35C-F4C5-475C-B064-6E87A06EE19A}') AND ((a.[DeleteStatusFlag]=true) AND (a.[VersionStatus]=1)))",
                "SELECT a.[DeleteStatusFlag],a.[This] FROM Folder AS a WHERE (ISCLASS(a,[DocManDealSubFolder])) AND ((a.[This] INSUBFOLDER '/Test_3790:{99FCB35C-F4C5-475C-B064-6E87A06EE19A}') AND ((a.[DeleteStatusFlag]=true)))" };
        assertTemplateSQL(results, "testTemplate_11.xml");
    }

    @Test
    public void testTemplate12() throws Exception
    {
        assertTemplateSQL("SELECT [From],[This] FROM Email WHERE (([From]='Test''Test''''') AND ([VersionStatus]=1))", "testTemplate_12.xml");
    }

    @Test
    public void testTemplate13() throws Exception
    {
        /*empty CONTAINS should not be rendered*/
        assertTemplateSQL("SELECT [DocumentTitle],[This] FROM Document WHERE (([DocumentTitle] LIKE '%project%') AND ([VersionStatus]=1))", "testTemplate_13.xml");
    }

    @Test
    public void testTemplate14() throws Exception
    {
        /*no WHERE clause only CONTAINS definition*/
        assertTemplateSQL("SELECT a.[DocumentTitle],a.[This] FROM Document AS a INNER JOIN ContentSearch AS b ON a.[This]=b.[QueriedObject] WHERE (((a.[VersionStatus]=1)) AND (CONTAINS(a.[*],'MyC0ntent')))", "testTemplate_14.xml");
    }

    @Test
    public void testTemplate15() throws Exception
    {
        String[] results = new String[] {
                "SELECT a.[DocumentTitle],a.[DateCreated],a.[This] FROM Document AS a WHERE (ISOFCLASS(a,[Email]) OR ISCLASS(a,[Memo])) AND ((a.[DocumentTitle]='woohoo' AND (a.[VersionStatus]=1))) ORDER BY a.[DocumentTitle] ASC,a.[DateCreated] DESC",
                "SELECT a.[DocumentTitle],a.[DateCreated],a.[This] FROM Folder AS a WHERE (ISOFCLASS(a,[Inbox]) OR ISCLASS(a,[Notes])) AND ((a.[DocumentTitle]='woohoo')) ORDER BY a.[DocumentTitle] ASC,a.[DateCreated] DESC" };
        assertTemplateSQL(results, "testTemplate_15.xml");
    }

    @Test
    public void testTemplate16() throws Exception
    {
        String[] results = new String[] { "SELECT [DocumentTitle],[DateCreated],[This] FROM Folder WHERE [DocumentTitle]='woohoo' ORDER BY [DocumentTitle] ASC,[DateCreated] DESC",
                "SELECT a.[DocumentTitle],a.[DateCreated],a.[This] FROM Document AS a WHERE (ISOFCLASS(a,[Email])) AND (a.[DocumentTitle]='woohoo' AND (a.[VersionStatus]=1)) ORDER BY a.[DocumentTitle] ASC,a.[DateCreated] DESC" };

        assertTemplateSQL(results, "testTemplate_16.xml");
    }

    @Test
    public void testTemplate17() throws Exception
    {//Correct escaping of subclasses names in ISClass and ISOfClass
        String result = "SELECT a.[DocumentTitle],a.[DateCreated],a.[This] FROM document AS a WHERE (ISCLASS(a,[Document]) OR ISOFCLASS(a,[Email])) AND (a.[DocumentTitle]='woohoo' AND (a.[VersionStatus]=1)) ORDER BY a.[DocumentTitle] ASC";
        assertTemplateSQL(result, "testTemplate_17.xml");
    }

    @Test
    public void testTemplate18() throws Exception
    {//Correct escaping of subclasses names in ISClass and ISOfClass
        String result = "SELECT [DocumentTitle],[DateCreated],[This] FROM document WHERE (([DocumentTitle]='woohoo' OR [Progress]>=0.6) AND ([VersionStatus]=1)) ORDER BY [DocumentTitle] ASC";
        assertTemplateSQL(result, "testTemplate_18.xml");
    }

    public OwNetworkContext getNetworkContext()
    {
        return new OwSearchTemplateTestContext();
    }

}
