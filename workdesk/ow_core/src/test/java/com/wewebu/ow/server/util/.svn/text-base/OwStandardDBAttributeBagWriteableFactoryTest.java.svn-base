package com.wewebu.ow.server.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.unittest.util.ApplicationConfiguration;

public class OwStandardDBAttributeBagWriteableFactoryTest extends OwDBAttributeBagTestBase
{
    public static final String DatasourceNodeName = "DefaultDataSource";

    public static final String JUNIT_TEST_USER = "JUNIT_TEST_USER_!\"§$%&/()=?`´'#*+~@\\";

    public static final String JUNIT_TEST_BAG = "JUNIT_TEST_BAG_!\"§$%&/()=?`´'#*+~@\\"; // no ':' in the bag name

    public static final String JUNIT_TEST_ATTRIBUTE = "JUNIT_TEST_ATTRIBUTE_!\"§$%&/()=?`´'#*+~@\\";

    public static final String JUNIT_TEST_VALUE = "JUNIT_TEST_VALUE_!\"§$%&/()=?`´'#*+~@\\";

    public static final String JUNIT_TEST_VALUE2 = "JUNIT_TEST_VALUE2_!\"§$%&/()=?`´'#*+~@\\";

    private static final Logger LOG = Logger.getLogger(OwStandardDBAttributeBagWriteableFactoryTest.class);

    private String m_configFileMsSql;
    private String m_configFileDB2;
    private String m_configFileOracle;
    private JdbcTemplate m_jdbcTemplateMsSql;
    private JdbcTemplate m_jdbcTemplateOracle;
    private JdbcTemplate m_jdbcTemplateDB2;

    /**
     * @param arg0_p
     */
    public OwStandardDBAttributeBagWriteableFactoryTest(String arg0_p)
    {
        super(arg0_p);
        m_configFileMsSql = ApplicationConfiguration.getTestSuiteProperties().getString("OwStandardDBAttributeBagWriteableFactoryTest.configFileMsSql");
        m_configFileDB2 = ApplicationConfiguration.getTestSuiteProperties().getString("OwStandardDBAttributeBagWriteableFactoryTest.configFileDB2");
        m_configFileOracle = ApplicationConfiguration.getTestSuiteProperties().getString("OwStandardDBAttributeBagWriteableFactoryTest.configFileOracle");
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        m_jdbcTemplateMsSql = setupJdbcTemplate(m_configFileMsSql);
        m_jdbcTemplateDB2 = setupJdbcTemplate(m_configFileDB2);
        m_jdbcTemplateOracle = setupJdbcTemplate(m_configFileOracle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testFactoryCreation_MsSql() throws Exception
    {
        baseTestFactoryCreation(m_jdbcTemplateMsSql);
    }

    public void testAttributeBagCreation_MsSql() throws Exception
    {
        baseTestAttributeBagCreation(m_jdbcTemplateMsSql);
    }

    public void testAttributeBagModification_MsSql() throws Exception
    {
        baseTestAttributeBagModification(m_jdbcTemplateMsSql);
    }

    public void testFactoryCreation_DB2() throws Exception
    {
        baseTestFactoryCreation(m_jdbcTemplateDB2);
    }

    public void testAttributeBagCreation_DB2() throws Exception
    {
        baseTestAttributeBagCreation(m_jdbcTemplateDB2);
    }

    public void testAttributeBagModification_DB2() throws Exception
    {
        baseTestAttributeBagModification(m_jdbcTemplateDB2);
    }

    public void testFactoryCreation_Oracle() throws Exception
    {
        baseTestFactoryCreation(m_jdbcTemplateOracle);
    }

    public void testAttributeBagCreation_Oracle() throws Exception
    {
        baseTestAttributeBagCreation(m_jdbcTemplateOracle);
    }

    public void testAttributeBagModification_Oracle() throws Exception
    {
        baseTestAttributeBagModification(m_jdbcTemplateOracle);
    }

    public void baseTestFactoryCreation(JdbcTemplate jdbcTemplate_p) throws Exception
    {
        // create standard bag factory
        // old: OwStandardDBAttributeBagWriteableFactory writeablebagfactory = new OwStandardDBAttributeBagWriteableFactory(getBaseDAO(),JUNIT_TEST_USER,STANDARD_DB_ATTRIBUTE_BAG_TABLE_NAME);
        OwStandardDBAttributeBagWriteableFactory writeablebagfactory = new OwStandardDBAttributeBagWriteableFactory(jdbcTemplate_p, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        writeablebagfactory.init();
    }

    public void baseTestAttributeBagCreation(JdbcTemplate jdbcTemplate_p) throws Exception
    {
        // create standard bag factory
        // old: OwStandardDBAttributeBagWriteableFactory writeablebagfactory = new OwStandardDBAttributeBagWriteableFactory(getBaseDAO(),JUNIT_TEST_USER,STANDARD_DB_ATTRIBUTE_BAG_TABLE_NAME);
        OwStandardDBAttributeBagWriteableFactory writeablebagfactory = new OwStandardDBAttributeBagWriteableFactory(jdbcTemplate_p, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        writeablebagfactory.init();
        // get an attribute bag
        writeablebagfactory.getBag(JUNIT_TEST_BAG);
    }

    public void baseTestAttributeBagModification(JdbcTemplate jdbcTemplate_p) throws Exception
    {
        // the bag should hold the data in it persistent. We check this by
        // creating new instances of the AttributeBagFactory simulating
        // system restarts
        //
        // create factory_A and bag_A
        //
        OwStandardDBAttributeBagWriteableFactory factory_A = new OwStandardDBAttributeBagWriteableFactory(jdbcTemplate_p, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        factory_A.init();
        OwAttributeBagWriteable bag_A = factory_A.getBag(JUNIT_TEST_BAG);
        //
        // clear bag_A and save it
        //
        bag_A.clear();
        bag_A.save();
        bag_A = null;
        factory_A = null;
        //--------------------< simulate restart >--------------------
        //
        // create factory_B and bag_B
        //
        OwStandardDBAttributeBagWriteableFactory factory_B = new OwStandardDBAttributeBagWriteableFactory(jdbcTemplate_p, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        factory_B.init();
        OwAttributeBagWriteable bag_B = factory_B.getBag(JUNIT_TEST_BAG);
        Collection names_B = bag_B.getAttributeNames();
        assertNotNull(names_B); // the collection must be oresent, even if it's empty
        Iterator nameIt_B = names_B.iterator();
        assertNotNull(nameIt_B); // the collection must have an iterator
        assertFalse(nameIt_B.hasNext()); // validate it's empty. We cleared it. see above.
        // 
        // create a value in the bag
        //
        bag_B.setAttribute(JUNIT_TEST_ATTRIBUTE, JUNIT_TEST_VALUE);
        assertTrue(bag_B.hasAttribute(JUNIT_TEST_ATTRIBUTE));
        assertNotNull(bag_B.getAttribute(JUNIT_TEST_ATTRIBUTE));
        assertTrue(bag_B.getAttribute(JUNIT_TEST_ATTRIBUTE).equals(JUNIT_TEST_VALUE));
        bag_B.save();
        nameIt_B = null;
        names_B = null;
        bag_B = null;
        factory_B = null;
        //--------------------< simulate restart >--------------------
        //
        // create factory_C and bag_C
        //
        OwStandardDBAttributeBagWriteableFactory factory_C = new OwStandardDBAttributeBagWriteableFactory(jdbcTemplate_p, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        factory_C.init();
        OwAttributeBagWriteable bag_C = factory_C.getBag(JUNIT_TEST_BAG);
        Collection names_C = bag_C.getAttributeNames();
        assertNotNull(names_C); // the collection must be oresent, even if it's empty
        Iterator nameIt_C = names_C.iterator();
        assertNotNull(nameIt_C); // the collection must have an iterator
        assertTrue(nameIt_C.hasNext()); // validate it's NOT empty. We added a attribute. see above.
        assertTrue(bag_C.hasAttribute(JUNIT_TEST_ATTRIBUTE));
        assertNotNull(bag_C.getAttribute(JUNIT_TEST_ATTRIBUTE));
        assertTrue(bag_C.getAttribute(JUNIT_TEST_ATTRIBUTE).equals(JUNIT_TEST_VALUE));
        // find this attribute by iterator
        boolean foundAttribute = false;
        while (nameIt_C.hasNext())
        {
            Object aNameObject_C = nameIt_C.next();
            // it's only a test case, not productive mode. we can use instanceof here
            assertTrue(aNameObject_C instanceof String);
            if (((String) aNameObject_C).equals(JUNIT_TEST_ATTRIBUTE))
            {
                foundAttribute = true;
            }
        }
        assertTrue(foundAttribute);
        // change the value and save the bag
        bag_C.setAttribute(JUNIT_TEST_ATTRIBUTE, JUNIT_TEST_VALUE2);
        assertTrue(bag_C.hasAttribute(JUNIT_TEST_ATTRIBUTE));
        assertNotNull(bag_C.getAttribute(JUNIT_TEST_ATTRIBUTE));
        assertTrue(bag_C.getAttribute(JUNIT_TEST_ATTRIBUTE).equals(JUNIT_TEST_VALUE2));
        bag_C.save();
        nameIt_C = null;
        names_C = null;
        bag_C = null;
        factory_C = null;
        //--------------------< simulate restart >--------------------
        //
        // create factory_D and bag_D
        //
        OwStandardDBAttributeBagWriteableFactory factory_D = new OwStandardDBAttributeBagWriteableFactory(jdbcTemplate_p, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        factory_D.init();
        OwAttributeBagWriteable bag_D = factory_D.getBag(JUNIT_TEST_BAG);
        Collection names_D = bag_D.getAttributeNames();
        assertNotNull(names_D); // the collection must be oresent, even if it's empty
        Iterator nameIt_D = names_D.iterator();
        assertNotNull(nameIt_D); // the collection must have an iterator
        assertTrue(nameIt_D.hasNext()); // validate it's NOT empty. We added a attribute. see above.
        assertTrue(bag_D.hasAttribute(JUNIT_TEST_ATTRIBUTE));
        assertNotNull(bag_D.getAttribute(JUNIT_TEST_ATTRIBUTE));
        assertTrue(bag_D.getAttribute(JUNIT_TEST_ATTRIBUTE).equals(JUNIT_TEST_VALUE2));
        // remove the attribute and save tha bag
        bag_D.remove(JUNIT_TEST_ATTRIBUTE);
        assertFalse(bag_D.hasAttribute(JUNIT_TEST_ATTRIBUTE));
        bag_D.save();
        bag_D = null;
        factory_D = null;
        //--------------------< simulate restart >--------------------
        //
        // create factory_E and bag_E
        //
        OwStandardDBAttributeBagWriteableFactory factory_E = new OwStandardDBAttributeBagWriteableFactory(jdbcTemplate_p, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        factory_E.init();
        OwAttributeBagWriteable bag_E = factory_E.getBag(JUNIT_TEST_BAG);
        Collection names_E = bag_E.getAttributeNames();
        assertNotNull(names_E); // the collection must be oresent, even if it's empty
        Iterator nameIt_E = names_E.iterator();
        assertNotNull(nameIt_E); // the collection must have an iterator
        assertFalse(nameIt_E.hasNext()); // validate it's NOT empty. We added a attribute. see above.
        assertFalse(bag_E.hasAttribute(JUNIT_TEST_ATTRIBUTE));
        bag_E = null;
        factory_E = null;
    }

}