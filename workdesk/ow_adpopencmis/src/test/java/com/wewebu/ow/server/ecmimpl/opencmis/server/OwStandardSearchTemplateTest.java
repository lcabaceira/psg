package com.wewebu.ow.server.ecmimpl.opencmis.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.unittest.search.OwSearchTemplateLoader;
import com.wewebu.ow.unittest.search.OwSearchTemplateTestContext;
import com.wewebu.ow.unittest.search.OwSearchTestFieldDefinition;
import com.wewebu.ow.unittest.search.OwSearchTestFieldDefinitionProvider;

public class OwStandardSearchTemplateTest extends TestCase
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwStandardSearchTemplateTest.class);

    public static final OwSearchTestFieldDefinition NAME = new OwCMISSearchTestFieldDefinition("name", String.class);
    public static final OwSearchTestFieldDefinition PERSONAL_NUMBER = new OwCMISSearchTestFieldDefinition("personalNumber", Long.class);
    public static final OwSearchTestFieldDefinition DEPARTMENT_NAME = new OwCMISSearchTestFieldDefinition("departmentName", String.class);
    public static final OwSearchTestFieldDefinition OFFICE_NUMBER = new OwCMISSearchTestFieldDefinition("officeNumber", Long.class);
    public static final OwSearchTestFieldDefinition LAST_MODIFICATION_TIME = new OwCMISSearchTestFieldDefinition("lastModificationTime", Date.class);
    public static final OwSearchTestFieldDefinition TOPICS = new OwCMISSearchTestFieldDefinition("topics", String.class);

    public static final OwSearchTestFieldDefinitionProvider SEARCH_TEST_DEFINITION_PROVIDER;

    static
    {
        SEARCH_TEST_DEFINITION_PROVIDER = new OwSearchTestFieldDefinitionProvider();
        SEARCH_TEST_DEFINITION_PROVIDER.add(NAME);
        SEARCH_TEST_DEFINITION_PROVIDER.add(PERSONAL_NUMBER);
        SEARCH_TEST_DEFINITION_PROVIDER.add(DEPARTMENT_NAME);
        SEARCH_TEST_DEFINITION_PROVIDER.add(TOPICS);
        SEARCH_TEST_DEFINITION_PROVIDER.add(OFFICE_NUMBER);
        SEARCH_TEST_DEFINITION_PROVIDER.add(LAST_MODIFICATION_TIME);
    }

    protected OwSearchNode getSearchNode(String testTemplate_p) throws Exception
    {
        OwSearchTemplate template = new OwSearchTemplateLoader(getClass()).loadLocalClassPathTemplate(testTemplate_p, SEARCH_TEST_DEFINITION_PROVIDER);
        assertNotNull(template);
        OwSearchNode search = template.getSearch(true);
        return search;
    }

    protected String[] asStringPaths(Object[] owPathValues_p)
    {
        if (owPathValues_p == null)
        {
            return null;
        }
        else
        {
            String[] stringPaths = new String[owPathValues_p.length];
            for (int i = 0; i < stringPaths.length; i++)
            {
                stringPaths[i] = owPathValues_p[i].toString();
            }
            return stringPaths;
        }
    }

    protected void assertPaths(String[] expectedStringPaths_p, Object[] owPathValues_p)
    {
        String[] stringPaths = asStringPaths(owPathValues_p);
        if (expectedStringPaths_p == null && stringPaths == null)
        {
            return;
        }
        else if (expectedStringPaths_p == null || stringPaths == null)
        {
            fail("Paths do not match : expected =" + Arrays.toString(expectedStringPaths_p) + "; actual=" + Arrays.toString(stringPaths));
        }
        else if (expectedStringPaths_p.length != stringPaths.length)
        {
            fail("Paths do not match : expected length =" + expectedStringPaths_p.length + "; actual length=" + stringPaths.length);
        }
        else
        {
            for (int i = 0; i < stringPaths.length; i++)
            {
                assertEquals("PathMissmatch#" + i, expectedStringPaths_p[i], stringPaths[i]);
            }
        }
    }

    public void testResourcePath_Template1() throws Exception
    {
        OwSearchNode search = getSearchNode("testTemplate_1.xml");
        OwSearchNode specialNode = search.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

        List specialChildren = specialNode.getChilds();

        for (Object object : specialChildren)
        {
            OwSearchNode node = (OwSearchNode) object;
            OwSearchCriteria criteria = node.getCriteria();
            String uniqueName = criteria.getUniqueName();
            if (uniqueName.equals(OwSearchPathField.CLASS_NAME))
            {
                Object[] paths = (Object[]) criteria.getValue();
                assertPaths(new String[] { "/R2ID/pathInR2/*", "/R4ID/pathInR4/", "/R1/", "/R3/" }, paths);
            }
        }
    }

    protected OwSearchPath[] asSearchPaths(OwSearchNode specialNodeExt_p)
    {
        List specialExtChildren = specialNodeExt_p.getChilds();
        List pathsList = new ArrayList();
        for (Iterator i = specialExtChildren.iterator(); i.hasNext();)
        {
            OwSearchNode node = (OwSearchNode) i.next();

            OwSearchCriteria criteria = node.getCriteria();
            if (criteria.getClassName().equals(OwSearchPathField.CLASS_NAME))
            {
                pathsList.add(criteria.getValue());
            }
        }
        return (OwSearchPath[]) pathsList.toArray(new OwSearchPath[pathsList.size()]);
    }

    protected void assertPathsExt(OwSearchPath[] expected_p, OwSearchPath[] actual_p)
    {
        if (expected_p == null && actual_p == null)
        {
            return;
        }
        else if (expected_p == null || actual_p == null)
        {
            fail("Pths Ext are not the same expected=" + Arrays.toString(expected_p) + "; actual=" + Arrays.toString(actual_p));
        }
        else if (expected_p.length != actual_p.length)
        {
            fail("Pths Ext lengths are not the same expected length=" + expected_p.length + "; actual length=" + actual_p.length);
        }
        else
        {
            for (int i = 0; i < actual_p.length; i++)
            {
                assertEquals("PathExMissmatch#" + i, expected_p[i], actual_p[i]);
            }
        }
    }

    public void testSearchPathsEx_Template1() throws Exception
    {
        OwSearchNode search = getSearchNode("testTemplate_1.xml");
        OwSearchNode specialNodeExt = search.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

        OwSearchObjectStore osR1 = new OwSearchObjectStore("R1ID", "R1");
        OwSearchObjectStore osR2 = new OwSearchObjectStore("R2ID", "R2");
        OwSearchObjectStore osR3 = new OwSearchObjectStore("R3ID", "R3");
        OwSearchObjectStore osR4 = new OwSearchObjectStore("R4ID", "R4");

        OwSearchPath[] paths = asSearchPaths(specialNodeExt);
        OwSearchPath p0 = new OwSearchPath(null, "/pathInR2", true, osR2);
        OwSearchPath p1 = new OwSearchPath(null, "/pathInR4", false, osR4);
        OwSearchPath p2 = new OwSearchPath(osR1);
        OwSearchPath p3 = new OwSearchPath(osR3);
        OwSearchPath[] expected = new OwSearchPath[] { p0, p1, p2, p3 };
        assertPathsExt(expected, paths);
    }

    public void testAttributesSpecialNodeEx_Template1() throws Exception
    {
        OwSearchNode search = getSearchNode("testTemplate_1.xml");
        OwSearchNode specialNodeExt = search.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
        List specialExtChildren = specialNodeExt.getChilds();
        int[] expectedAttributes = new int[] { OwSearchCriteria.ATTRIBUTE_HIDDEN, OwSearchCriteria.ATTRIBUTE_READONLY, OwSearchCriteria.ATTRIBUTE_HIDDEN, OwSearchCriteria.ATTRIBUTE_HIDDEN };
        int index = 0;
        for (Iterator i = specialExtChildren.iterator(); i.hasNext();)
        {
            OwSearchNode node = (OwSearchNode) i.next();

            OwSearchCriteria criteria = node.getCriteria();
            criteria.getAttributes();
            if (criteria.getClassName().equals(OwSearchPathField.CLASS_NAME))
            {
                int criteriaAttributes = criteria.getAttributes();

                assertTrue("InvalidAttribute#" + index, (criteriaAttributes & expectedAttributes[index]) != 0);
                index++;
            }
        }
        assertTrue(index > 0);
    }

    public void testMergeOptionEx_Template1() throws Exception
    {
        OwSearchNode search = getSearchNode("testTemplate_1.xml");
        OwSearchNode specialNodeExt = search.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

        List specialExtChildren = specialNodeExt.getChilds();
        int index = 0;
        for (Iterator i = specialExtChildren.iterator(); i.hasNext();)
        {
            OwSearchNode node = (OwSearchNode) i.next();

            OwSearchCriteria criteria = node.getCriteria();

            if (criteria.getClassName().equals(OwSearchPathField.CLASS_NAME))
            {
                int operator = criteria.getOperator();
                assertTrue("InvalidOperator#" + index, operator == OwSearchOperator.MERGE_UNION);
                index++;
            }
        }

        assertTrue(index > 0);
    }

    public void testDeprecationPathHandling() throws Exception
    {
        try
        {
            new OwStandardSearchTemplate(new OwSearchTemplateTestContext(), null, "Test Template", null, false);
            fail();
        }
        catch (OwNotSupportedException nse)
        {
            assertNotNull(nse);
        }
    }
}
