package com.wewebu.ow.server.ecmimpl.fncm5.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwWildCardDefinition;

/**
 *<p>
 * OwFNCM5SearchTemplateSQLTest.
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
public abstract class OwFNCM5SearchTemplateSQLTest extends OwFNCM5SearchTemplateTest
{
    private static final Logger LOG = Logger.getLogger(OwFNCM5SearchTemplateSQLTest.class);

    public static final OwSearchTestFieldDefinition ANY = new OwSearchTestFieldDefinition("*", String.class);
    public static final OwSearchTestFieldDefinition DOCUMENT_TITLE = new OwSearchTestFieldDefinition("DocumentTitle", String.class);
    public static final OwSearchTestFieldDefinition OW_OBJECTNAME = new OwSearchTestFieldDefinition("OW_ObjectName", String.class);
    public static final OwSearchTestFieldDefinition TO = new OwSearchTestFieldDefinition("To", String.class, true);
    public static final OwSearchTestFieldDefinition FROM = new OwSearchTestFieldDefinition("From", String.class);
    public static final OwSearchTestFieldDefinition SUBJECT = new OwSearchTestFieldDefinition("EmailSubject", String.class);
    public static final OwSearchTestFieldDefinition SIMULATIONGUID = new OwSearchTestFieldDefinition("SimulationGUID", String.class);
    public static final OwSearchTestFieldDefinition OFFICE_NUMBER = new OwSearchTestFieldDefinition("OfficeNumber", Integer.class);
    public static final OwSearchTestFieldDefinition PROGRESS = new OwSearchTestFieldDefinition("Progress", Double.class);
    public static final OwSearchTestFieldDefinition DATE_CREATED = new OwSearchTestFieldDefinition("DateCreated", Date.class);
    public static final OwSearchTestFieldDefinition MIME_TYPE = new OwSearchTestFieldDefinition("MimeType", String.class);
    public static final OwSearchTestFieldDefinition DELETE_STATUS_FLAG = new OwSearchTestFieldDefinition("DeleteStatusFlag", Boolean.class);

    public static final OwSearchTestFieldDefinition OW_HIST_TIME = new OwSearchTestFieldDefinition("OW_HIST_Time", Date.class);
    public static final OwSearchTestFieldDefinition OW_HIST_MODIFIEDPROPERTIES = new OwSearchTestFieldDefinition("OW_HIST_ModifiedProperties", String[].class);

    public static final String DOCUMENT = "Document";
    public static final String FOLDER = "Folder";
    public static final String EMAIL = "Email";
    public static final String EVENT = "Event";

    public static final OwSearchTestFieldDefinitionProvider SEARCH_TEST_DEFINITION_PROVIDER;

    static
    {
        SEARCH_TEST_DEFINITION_PROVIDER = new OwSearchTestFieldDefinitionProvider() {
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
        SEARCH_TEST_DEFINITION_PROVIDER.add(DOCUMENT_TITLE);
        SEARCH_TEST_DEFINITION_PROVIDER.add(OW_OBJECTNAME);
        SEARCH_TEST_DEFINITION_PROVIDER.add(DATE_CREATED);
        SEARCH_TEST_DEFINITION_PROVIDER.add(MIME_TYPE);
        SEARCH_TEST_DEFINITION_PROVIDER.add(OFFICE_NUMBER);
        SEARCH_TEST_DEFINITION_PROVIDER.add(PROGRESS);
        SEARCH_TEST_DEFINITION_PROVIDER.add(TO);
        SEARCH_TEST_DEFINITION_PROVIDER.add(FROM);
        SEARCH_TEST_DEFINITION_PROVIDER.add(SUBJECT);
        SEARCH_TEST_DEFINITION_PROVIDER.add(SIMULATIONGUID);
        SEARCH_TEST_DEFINITION_PROVIDER.add(ANY);
        SEARCH_TEST_DEFINITION_PROVIDER.add(DELETE_STATUS_FLAG);

        SEARCH_TEST_DEFINITION_PROVIDER.add(OW_HIST_TIME);
        SEARCH_TEST_DEFINITION_PROVIDER.add(OW_HIST_MODIFIEDPROPERTIES);
    }

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
        OwSearchTemplate template = loadLocalClassPathTemplate(localClassPathTemplate_p, SEARCH_TEST_DEFINITION_PROVIDER);
        OwSearchNode search = template.getSearch(true);
        OwSort sort = template.getSort(10);
        Collection columns = template.getColumnInfoList();
        List<String> propertyNames = new LinkedList<String>();
        for (OwFieldColumnInfo info : ((Collection<OwFieldColumnInfo>) columns))
        {
            propertyNames.add(info.getPropertyName());
        }

        List<String> sqls = createSQL(search, propertyNames, sort, template.getVersionSelection());
        Collections.sort(sqls);
        if (sqls == null && expectedSQLStatements_p != null)
        {
            fail("null actual SQL when non null value was expected !");
        }
        else if (sqls != null && expectedSQLStatements_p != null)
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
                assertEquals("SQL#" + i, expectedSQLStatements_p[i], sqls.get(i));
            }
        }
        else
        {
            fail("null SQL was expected but the actual value was non null !");
        }
    }

    protected abstract List<String> createSQL(OwSearchNode searchNode_p, List<String> propertyNames_p, OwSort sort_p, int versionSelection_p) throws Exception;

}
