package com.wewebu.ow.server.util;

import java.io.File;
import java.io.FileInputStream;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.unittest.util.ApplicationConfiguration;
import com.wewebu.ow.unittest.util.OwTestDataSource;
import com.wewebu.ow.unittest.util.ResourceUtil;

public class OwDBAttributeBagTestBase extends TestCase
{
    private static final Logger LOG = Logger.getLogger(OwDBAttributeBagTestBase.class);

    public static final OwTableSpec DB_ATTRIBUTE_BAG_TABLE = new OwTableSpec(null, null, "OW_ATTRIBUTE_BAG");

    public static final String DatasourceNodeName = "DefaultDataSource";

    private String m_configDir;

    public OwDBAttributeBagTestBase(String arg_p)
    {
        super(arg_p);
        m_configDir = ApplicationConfiguration.getTestSuiteProperties().getString("OwStandardDBAttributeBagWriteableFactoryTest.configDir");
    }

    protected JdbcTemplate setupJdbcTemplate(String configFile_p) throws Exception
    {
        // read XML config file
        OwXMLUtil config = new OwStandardXMLUtil(new FileInputStream(new File(getConfigDir() + configFile_p)), "bootstrap");
        // get datasource config node
        OwXMLUtil datasourceXml = null;
        try
        {
            datasourceXml = new OwStandardXMLUtil(config.getSubNode(DatasourceNodeName));
        }
        catch (Exception e)
        {
            LOG.error("Cannot read the subnode: " + DatasourceNodeName, e);
            throw new OwConfigurationException("Cannot read the subnode: " + DatasourceNodeName, e);
        }
        DataSource ds = new OwTestDataSource(datasourceXml);
        return new JdbcTemplate(ds);

    }

    /** get the configuration dir 
     * @return String */
    public String getConfigDir()
    {
        // return x:/workspace/ow_test/bin/resources/;
        String configDirTemp = ResourceUtil.getInstance().getResourcePath(this.m_configDir);
        return configDirTemp;
    }

}
