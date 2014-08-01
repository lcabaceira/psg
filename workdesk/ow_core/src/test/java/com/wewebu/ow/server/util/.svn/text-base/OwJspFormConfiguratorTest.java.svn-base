package com.wewebu.ow.server.util;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwJspFormConfigurator;

public class OwJspFormConfiguratorTest extends TestCase
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwStandardXMLUtilTest.class);

    private static final String OWPLUGIN1_XML = "owpluginJspForms_1.xml";

    private static final String OWPLUGIN2_XML = "owpluginJspForms_2.xml";

    private static final String OWPLUGIN3_XML = "owpluginJspForms_3.xml";

    private InputStream xmlStream1;

    private InputStream xmlStream2;

    private OwTestObjectClass m_emailClass;
    private OwTestObjectClass m_invoiceClass;
    private OwTestObjectClass m_CMDocumentClass;

    private OwTestObject m_emailObj;
    private OwTestObject m_invoiceObj;
    private OwTestObject m_CMDocumentObj;

    protected void setUp() throws Exception
    {
        resetTestInputStreams();
        //create fake objects
        m_emailClass = new OwTestObjectClass("Email");
        m_invoiceClass = new OwTestObjectClass("Invoice");
        m_CMDocumentClass = new OwTestObjectClass("CMDocument");

        m_emailObj = new OwTestObject(m_emailClass);
        m_invoiceObj = new OwTestObject(m_invoiceClass);
        m_CMDocumentObj = new OwTestObject(m_CMDocumentClass);

    }

    private void resetTestInputStreams()
    {
        this.xmlStream1 = OwStandardXMLUtilTest.class.getResourceAsStream(OWPLUGIN1_XML);
        this.xmlStream2 = OwStandardXMLUtilTest.class.getResourceAsStream(OWPLUGIN2_XML);
    }

    public void testDefaultFormat() throws Exception
    {
        OwXMLUtil config = new OwStandardXMLUtil(xmlStream1, "PlugIn");

        OwJspFormConfigurator owconfigConfigurator = new OwJspFormConfigurator(config);

        String jspPage;

        jspPage = owconfigConfigurator.getJspForm(m_emailObj.getObjectClass().getClassName());
        assertEquals("Email.jsp", jspPage);

        jspPage = owconfigConfigurator.getJspForm(m_CMDocumentObj.getObjectClass().getClassName());
        assertEquals("Default.jsp", jspPage);

        jspPage = owconfigConfigurator.getJspForm(m_invoiceObj.getObjectClass().getClassName());
        assertEquals("InvoiceEmployee.jsp", jspPage);

    }

    public void testBiggerPriority() throws Exception
    {
        OwXMLUtil config = new OwStandardXMLUtil(xmlStream1, "PlugIn");

        OwJspFormConfigurator owconfigConfigurator = new OwJspFormConfigurator(config);

        String jspPage;

        jspPage = owconfigConfigurator.getJspForm(m_emailObj.getObjectClass().getClassName());
        assertEquals("Email.jsp", jspPage);

        jspPage = owconfigConfigurator.getJspForm(m_CMDocumentObj.getObjectClass().getClassName());
        assertEquals("Default.jsp", jspPage);

        jspPage = owconfigConfigurator.getJspForm(m_invoiceObj.getObjectClass().getClassName());
        assertEquals("InvoiceEmployee.jsp", jspPage);

    }

    public void testNoDefaultObjectClasses() throws Exception
    {
        OwXMLUtil config = new OwStandardXMLUtil(xmlStream2, "PlugIn");

        OwJspFormConfigurator owconfigConfigurator = new OwJspFormConfigurator(config);

        String jspPage;

        OwTestObjectClass allClasses = new OwTestObjectClass("Test");

        OwTestObject obj = new OwTestObject(allClasses);

        jspPage = owconfigConfigurator.getJspForm(obj.getObjectClass().getClassName());
        assertNotNull(jspPage);
        assertEquals("Default.jsp", jspPage);

    }
}
