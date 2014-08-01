package com.wewebu.ow.server.util;

import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 *Specifi tests for the {@link OwStandardXMLUtil} .
 *
 */
public class OwStandardXMLUtilTest extends OwXMLUtilTest
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwStandardXMLUtilTest.class);

    private static final String STANDARDXMLUTIL1_XML = "standardXMLUtil_1.xml";

    private static final String STANDARDXMLUTIL3_XML_UTF8_BOM = "standardXMLUtil_3.xml";

    private static final String STANDARDXMLUTIL4_XML_UTF16 = "standardXMLUtil_4.xml";

    private InputStream standardxmlutil1XmlStream;

    protected void setUp() throws Exception
    {
        resetTestInputStreams();
    }

    private void resetTestInputStreams()
    {
        this.standardxmlutil1XmlStream = OwStandardXMLUtilTest.class.getResourceAsStream(STANDARDXMLUTIL1_XML);
    }

    public void testDefaultRootSearch() throws Exception
    {
        OwStandardXMLUtil stdXMLUtil = new OwStandardXMLUtil(standardxmlutil1XmlStream, "root_3");
        String testValue = stdXMLUtil.getSafeStringAttributeValue("test", "test_attr_not_fond");
        assertEquals(OwStandardXMLUtil.DEPTH_FIRST_PREORDER, testValue);
    }

    public void testPreorderRootSearch() throws Exception
    {
        OwStandardXMLUtil stdXMLUtil = new OwStandardXMLUtil(standardxmlutil1XmlStream, "root_3", OwStandardXMLUtil.DEPTH_FIRST_PREORDER);
        String testValue = stdXMLUtil.getSafeStringAttributeValue("test", "test_attr_not_fond");
        assertEquals(OwStandardXMLUtil.DEPTH_FIRST_PREORDER, testValue);
    }

    public void testLevelOrderRootSearch() throws Exception
    {
        OwStandardXMLUtil stdXMLUtil = new OwStandardXMLUtil(standardxmlutil1XmlStream, "root_3", OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);
        String testValue = stdXMLUtil.getSafeStringAttributeValue("test", "test_attr_not_fond");
        assertEquals(OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER, testValue);
    }

    private void invalidRootSearchTest(String searchMode_p) throws Exception
    {
        final String invalidRootName = "invalid_root_name";
        final String traceMessage = "Expected exception caught: ";
        final String searchModeString = searchMode_p == null ? "default_search_mode" : searchMode_p;
        final String failMessage = "Should not be able to create OwStandardXMLUtil instances with invalid root names! (searchMode =" + searchModeString + ")";
        try
        {
            if (searchMode_p == null)
            {
                new OwStandardXMLUtil(standardxmlutil1XmlStream, invalidRootName);
            }
            else
            {
                new OwStandardXMLUtil(standardxmlutil1XmlStream, invalidRootName, searchMode_p);
            }
            fail(failMessage);
        }
        catch (NullPointerException e)
        {
            LOG.info(traceMessage, e);
        }
    }

    private void nullRootSearchTest(String searchMode_p) throws Exception
    {
        final String nullRootChildName = "nullRootChild";
        final String searchModeString = searchMode_p == null ? "default_search_mode" : searchMode_p;
        final String failMessage = "Invalid null root! (searchMode =" + searchModeString + ")";

        OwStandardXMLUtil stdXmlUtil = null;
        if (searchMode_p == null)
        {
            stdXmlUtil = new OwStandardXMLUtil(standardxmlutil1XmlStream, null);
        }
        else
        {
            stdXmlUtil = new OwStandardXMLUtil(standardxmlutil1XmlStream, null, searchMode_p);
        }

        OwXMLUtil nullRootChild = stdXmlUtil.getSubUtil(nullRootChildName);
        assertNotNull(failMessage, nullRootChild);
        String testValue = nullRootChild.getSafeStringAttributeValue("test", "NOK");
        assertEquals(failMessage, "ok", testValue);
    }

    public void testInvalidRootSearch() throws Exception
    {

        invalidRootSearchTest(null);
        resetTestInputStreams();
        invalidRootSearchTest(OwStandardXMLUtil.DEPTH_FIRST_PREORDER);
        resetTestInputStreams();
        invalidRootSearchTest(OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);
        resetTestInputStreams();
    }

    public void testNullRootSearch() throws Exception
    {
        nullRootSearchTest(null);
        resetTestInputStreams();
        nullRootSearchTest(OwStandardXMLUtil.DEPTH_FIRST_PREORDER);
        resetTestInputStreams();
        nullRootSearchTest(OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);
    }

    public void testUTF8BOM() throws Exception
    {

        final String failMessage = "BOM not found!";
        InputStream standardxmlutil2XmlStream = OwStandardXMLUtilTest.class.getResourceAsStream(STANDARDXMLUTIL3_XML_UTF8_BOM);
        OwStandardXMLUtil stdXMLUtil = new OwStandardXMLUtil(standardxmlutil2XmlStream, "root_3");
        assertEquals(failMessage, "UTF-8", stdXMLUtil.getFileEncoding());

    }

    public void testUTF8WithOutBOM() throws Exception
    {

        final String failMessage = "File contains BOM";
        InputStream standardxmlutil2XmlStream = OwStandardXMLUtilTest.class.getResourceAsStream(STANDARDXMLUTIL3_XML_UTF8_BOM);
        OwStandardXMLUtil stdXMLUtil = new OwStandardXMLUtil(standardxmlutil2XmlStream, "root_3");
        assertEquals(failMessage, "UTF-8", stdXMLUtil.getFileEncoding());

    }

    public void testUTF16() throws Exception
    {

        final String failMessage = "BOM not found!";
        InputStream standardxmlutil2XmlStream = OwStandardXMLUtilTest.class.getResourceAsStream(STANDARDXMLUTIL4_XML_UTF16);
        OwStandardXMLUtil stdXMLUtil = new OwStandardXMLUtil(standardxmlutil2XmlStream, "root_3");
        assertEquals(failMessage, "UTF-16BE", stdXMLUtil.getFileEncoding());

    }

}
