package com.wewebu.ow.server.util;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.unittest.util.ApplicationConfiguration;

public class OwDBAttributeBagTest extends OwDBAttributeBagTestBase
{
    private static final String FIRST_VALUE = "abcd";

    private static final String ATTRIBUTE_NAME = "test";

    private static final String BAG_NAME = "testbag";

    /** DB attribute name of the User */
    public static final String ATTRIBUTE_USER = "UserName";
    /** DB attribute name of the Name */
    public static final String ATTRIBUTE_BAGNAME = "BagName";
    /** DB attribute name of the attribute name */
    public static final String ATTRIBUTE_ATTR_NAME = "AttributeName";
    /** DB attribute name of the attribute value */
    public static final String ATTRIBUTE_ATTR_VALUE = "AttributeValue";

    public static final String JUNIT_TEST_USER = "JUNIT_TEST_USER_!\"$%&/()=?`'#*+~@\\";

    private JdbcTemplate m_jdbcTemplateMsSql;
    private String m_configFileMsSql;

    private OwStandardDBAttributeBagWriteableFactory writeablebagfactory;

    /**
     * Constructor
     * @param arg_p
     */
    public OwDBAttributeBagTest(String arg_p)
    {
        super(arg_p);
        m_configFileMsSql = ApplicationConfiguration.getTestSuiteProperties().getString("OwStandardDBAttributeBagWriteableFactoryTest.configFileMsSql");
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        m_jdbcTemplateMsSql = setupJdbcTemplate(m_configFileMsSql);
        writeablebagfactory = new OwStandardDBAttributeBagWriteableFactory(m_jdbcTemplateMsSql, JUNIT_TEST_USER, DB_ATTRIBUTE_BAG_TABLE);
        writeablebagfactory.init();
    }

    /**
     * Test the problem described in bug 3065.
     * DBAttributeBag does not persist values under certain conditions.
     * http://support.wewebu.info/show_bug.cgi?id=3065
     * @throws Exception
     */
    public void testBug3065() throws Exception
    {
        writeablebagfactory.setAttribute(ATTRIBUTE_NAME, FIRST_VALUE, BAG_NAME);
        writeablebagfactory.save(BAG_NAME);
        String value = (String) writeablebagfactory.getAttribute(ATTRIBUTE_NAME, BAG_NAME);
        assertEquals(FIRST_VALUE, value);
        writeablebagfactory.remove(ATTRIBUTE_NAME, BAG_NAME);
        writeablebagfactory.save(BAG_NAME);
        writeablebagfactory.setAttribute(ATTRIBUTE_NAME, FIRST_VALUE, BAG_NAME);
        writeablebagfactory.save(BAG_NAME);

        String statement = "select count(" + ATTRIBUTE_USER + ")" + " from " + DB_ATTRIBUTE_BAG_TABLE.getTableName() + " where " + ATTRIBUTE_USER + " = ? and " + ATTRIBUTE_BAGNAME + " = ?";
        Object[] params = new Object[] { JUNIT_TEST_USER, BAG_NAME };
        int numberOfRows = m_jdbcTemplateMsSql.queryForInt(statement, params);
        assertEquals(1, numberOfRows);
    }
}
