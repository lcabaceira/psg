package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.search.OwCMISSearchTemplateSQLTest;

public class OwCMISAlfrescoCSQLCProcessorTest extends OwCMISSearchTemplateSQLTest
{
    private static final Logger LOG = Logger.getLogger(OwCMISAlfrescoCSQLCProcessorTest.class);

    @Override
    protected OwCMISAlfrescoCSQLCProcessor createProcessor()
    {
        return new OwCMISAlfrescoCSQLCProcessor(this);
    }

    public void testAlfrescoTemplate1() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE ((cmis:lastModifiedBy='JUnitTester')) AND (CONTAINS('~cmis:name:\\'*Work*\\'')) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testAlfrescoTemplate_1.xml");
    }

    public void testAlfrescoTemplate2() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE ((cmis:lastModifiedBy='JUnitTester')) AND (CONTAINS('~cmis:name:\\'*Work*\\' ALL:\\'*pAtH*\\'')) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testAlfrescoTemplate_2.xml");
    }

    public void testAlfrescoTemplate3() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (CONTAINS('MyC0ntent')) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testAlfrescoTemplate_3.xml");
    }

    public void testEscapingCombination() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (CONTAINS('Hello world \"And\" \"Or\" nothing to do, \"not\" \"Or\"')) ORDER BY cmis:name ASC", "testAlfrescoTemplate_4.xml");
    }
}
