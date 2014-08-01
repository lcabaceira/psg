package com.wewebu.ow.server.util;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.util.paramcodec.OwAttributeBagCodec;
import com.wewebu.ow.server.util.paramcodec.OwParameterMap;
import com.wewebu.ow.unittest.util.ApplicationConfiguration;

public class OwDBAttributeBagCodecTest extends OwDBAttributeBagTestBase
{
    private static final Logger LOG = Logger.getLogger(OwDBAttributeBagCodecTest.class);

    private String m_configFileMsSql;
    private JdbcTemplate m_jdbcTemplateMsSql;
    private static final String TEST_CODEC_BAGS_PREFIX = "ju_cdb";
    private static final String TEST_COOKIE_BAGS_PREFIX = "ju_cookie";
    protected static final String TEST_CODEC_BAGS_PSEUDO_USER = "att_bag_codec";
    protected static final String TEST_COOKIE_PARAMETER_NAME = "tcp";

    protected class OwTestAttributeBagCodec extends OwAttributeBagCodec
    {

        public OwTestAttributeBagCodec(long namesExpirationTime_p)
        {
            super(TEST_COOKIE_PARAMETER_NAME, namesExpirationTime_p, TEST_CODEC_BAGS_PREFIX, TEST_COOKIE_BAGS_PREFIX);
        }

        protected OwAttributeBagWriteable getBag(String bagName_p, boolean create_p) throws Exception
        {
            return getTestBag(bagName_p, TEST_CODEC_BAGS_PSEUDO_USER);
        }

    }

    public OwDBAttributeBagCodecTest(String name_p)
    {
        super(name_p);
        m_configFileMsSql = ApplicationConfiguration.getTestSuiteProperties().getString("OwStandardDBAttributeBagWriteableFactoryTest.configFileMsSql");

    }

    protected void setUp() throws Exception
    {
        super.setUp();
        m_jdbcTemplateMsSql = setupJdbcTemplate(m_configFileMsSql);
    }

    protected OwAttributeBagWriteable getTestBag(String bagName_p, String userName_p) throws Exception
    {
        OwStandardDBAttributeBagWriteableFactory factory = new OwStandardDBAttributeBagWriteableFactory(m_jdbcTemplateMsSql, userName_p, DB_ATTRIBUTE_BAG_TABLE);
        factory.init();
        OwAttributeBagWriteable bag = factory.getBag(bagName_p);
        return bag;
    }

    public void testEncodeDecode() throws Exception
    {
        // Access to database can be slower, maybe the time is small
        OwTestAttributeBagCodec codec = new OwTestAttributeBagCodec(10 * 1000);

        OwParameterMap aMap1 = new OwParameterMap();
        aMap1.setParameter("P1", "P1V1");
        aMap1.setParameter("P2", "P2V1");

        OwParameterMap aMap2 = new OwParameterMap();
        aMap2.setParameter("a", "a1");
        aMap2.setParameter("b", "b1");
        aMap2.setParameter("ccc", "ccc1");

        OwParameterMap encodedMap1 = codec.encode(aMap1);
        OwParameterMap encodedMap2 = codec.encode(aMap2);

        LOG.debug("OwDBAttributeBagCodecTest.testEncodeDecode(): encoded query string #1 = \"" + encodedMap1.toRequestQueryString() + "\"");
        LOG.debug("OwDBAttributeBagCodecTest.testEncodeDecode(): encoded query string #2 = \"" + encodedMap2.toRequestQueryString() + "\"");

        OwParameterMap decodedMap1 = codec.decode(encodedMap1, false);
        OwParameterMap decodedMap2 = codec.decode(encodedMap2, true);

        assertEquals(aMap1, decodedMap1);
        assertNotSame(aMap2, decodedMap2);

        aMap1.setParameter("P1", "P1Ex");

        assertNotSame(aMap1, decodedMap1);
    }

}
