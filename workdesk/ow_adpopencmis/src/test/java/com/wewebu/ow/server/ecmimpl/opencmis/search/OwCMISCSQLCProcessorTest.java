package com.wewebu.ow.server.ecmimpl.opencmis.search;

import com.wewebu.ow.csqlc.OwCSQLCProcessor;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISDateTime;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

public class OwCMISCSQLCProcessorTest extends OwCMISSearchTemplateSQLTest
{
    //    private static final Logger LOG = Logger.getLogger(OwCMISCSQLCProcessorTest.class);

    @Override
    protected OwCSQLCProcessor createProcessor()
    {
        return new OwCMISCSQLCProcessor(this);
    }

    public void testTemplate1() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (cmis:name='yourname' AND (cmis:name='hisname' OR cmis:name='ourname')) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_1.xml");
    }

    public void testTemplate2() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (cmis:personalNumber=2231233 OR cmis:departmentName IN ('HR','AC','mobile')) AND (cmis:officeNumber NOT IN (2231,999,654)) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC",
                "testTemplate_2.xml");
    }

    public void testTemplate3() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (cmis:name LIKE '%wow%' AND cmis:name NOT LIKE '%beax%') ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_3.xml");
    }

    public void testTemplate4() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_4.xml");
    }

    public void testTemplate5() throws Exception
    {
        OwCMISDateTime date1 = new OwCMISDateTime("2000-10-18T00:00:00.000Z");
        OwCMISDateTime date2 = new OwCMISDateTime("2011-10-17T00:00:00.000Z");
        String sqlDate1 = date1.toCMISDateTimeString();
        String sqlDate2 = date2.toCMISDateTimeString();
        assertTemplateSQL("With ignoreTime set on true, the time part of the DateTime should be set to 00.00 and the date adjusted in accordance with the operator (+1 day if needed).",
                "SELECT * FROM cmis:document WHERE (cmis:lastModificationTime>TIMESTAMP'" + sqlDate1 + "' AND cmis:lastModificationTime<TIMESTAMP'" + sqlDate2 + "') ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_5.xml");
    }

    public void testTemplate5_1() throws Exception
    {
        OwCMISDateTime date1 = new OwCMISDateTime("2000-10-17T00:00:00.000Z");
        OwCMISDateTime date2 = new OwCMISDateTime("2011-10-18T00:00:00.000Z");
        String sqlDate1 = date1.toCMISDateTimeString();
        String sqlDate2 = date2.toCMISDateTimeString();
        assertTemplateSQL("With ignoreTime set on true, the time part of the DateTime should be set to 00.00 and the date adjusted in accordance with the operator (+1 day if needed).",
                "SELECT * FROM cmis:document WHERE (cmis:lastModificationTime>=TIMESTAMP'" + sqlDate1 + "' AND cmis:lastModificationTime<=TIMESTAMP'" + sqlDate2 + "') ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_5_1.xml");
    }

    public void testTemplate6() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (cmis:name IS NULL AND cmis:officeNumber IS NOT NULL) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_6.xml");
    }

    public void testTemplate7() throws Exception
    {
        assertTemplateSQL(
                "SELECT * FROM cmis:document WHERE ( ( 'financial' = ANY cmis:topics AND 'math' = ANY cmis:topics )  OR  ( (NOT 'RedRindingHood' = ANY cmis:topics OR NOT 'Robin Hood' = ANY cmis:topics) )  OR ANY cmis:topics IN ('delivery','provisioning') OR ANY cmis:topics NOT IN ('delivery','provisioning')) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC",
                "testTemplate_7.xml");
    }

    public void testTemplate8() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:documentRID WHERE (IN_TREE('/RID/root/myfolder') OR IN_FOLDER('/RID/root/myfolder2') OR IN_TREE('/RID/')) AND (cmis:nameRID='myname') ORDER BY cmis:nameRID ASC,cmis:lastModifiedByRID DESC",
                "testTemplate_8.xml");
    }

    public void testTemplate9() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE ((cmis:officeNumber>=232 AND cmis:officeNumber<=689) OR cmis:officeNumber<232 OR (cmis:officeNumber<400 OR cmis:officeNumber>500)) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC",
                "testTemplate_9.xml");
    }

    public void testTemplate10() throws Exception
    {
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (CONTAINS('MyC0ntent')) ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_10.xml");
    }

    public void testTemplate11() throws Exception
    {
        String[] expectedSQL11 = new String[] { "SELECT * FROM cmis:documentR1 WHERE (IN_TREE('/R1/')) AND (cmis:nameR1='myname') ORDER BY cmis:nameR1 ASC,cmis:lastModifiedByR1 DESC",
                "SELECT * FROM cmis:documentR2 WHERE (IN_TREE('/R2/') OR IN_TREE('/R2/someFolder')) AND (cmis:nameR2='myname') ORDER BY cmis:nameR2 ASC,cmis:lastModifiedByR2 DESC" };
        assertTemplateSQL(expectedSQL11, "testTemplate_11.xml");
    }

    public void testTemplate12() throws Exception
    {
        String[] expectedSQL12 = new String[] { "SELECT * FROM cmis:documentR1 WHERE (cmis:nameR1='myname') ORDER BY cmis:nameR1 ASC,cmis:lastModifiedByR1 DESC",
                "SELECT * FROM cmis:documentR2 WHERE (IN_TREE('/R2/r2Path')) AND (cmis:nameR2='myname') ORDER BY cmis:nameR2 ASC,cmis:lastModifiedByR2 DESC" };
        assertTemplateSQL(expectedSQL12, "testTemplate_12.xml");
    }

    public void testTemplate13() throws Exception
    {//validation of escaping
        String[] expectedSQL = new String[] { "SELECT * FROM cmis:document WHERE (cmis:name='myname' AND cmis:lastModifiedBy LIKE 'T#\\'\\_/%')" };
        assertTemplateSQL(expectedSQL, "testTemplate_13.xml");
    }

    public void testTemplate14() throws Exception
    {
        String[] expectedSQL = new String[] {

                "SELECT b.* FROM ws:article AS b JOIN cm:author AS c ON b.cmis:objectId=c.cmis:objectId JOIN cm:titled AS e ON b.cmis:objectId=e.cmis:objectId WHERE ((b.cmis:name LIKE '%Whom%' AND b.ws:reviewers LIKE '%Doe%' AND b.cmis:lastModifiedBy LIKE '%admin%' AND c.cm:author LIKE '%E. Hemingway%' AND e.cm:title LIKE '%Bell Tolls%') AND (b.ws:journal LIKE '%Medicine Today%')) ORDER BY b.cmis:name ASC,b.cmis:lastModifiedBy DESC,c.cm:author DESC",
                "SELECT b.* FROM ws:blogPost AS b JOIN cm:author AS c ON b.cmis:objectId=c.cmis:objectId JOIN cm:titled AS e ON b.cmis:objectId=e.cmis:objectId WHERE ((b.cmis:name LIKE '%Whom%' AND b.ws:reviewers LIKE '%Doe%' AND b.cmis:lastModifiedBy LIKE '%admin%' AND c.cm:author LIKE '%E. Hemingway%' AND e.cm:title LIKE '%Bell Tolls%') AND (b.ws:url LIKE '%http://blog.b.log%')) ORDER BY b.cmis:name ASC,b.cmis:lastModifiedBy DESC,c.cm:author DESC" };

        assertTemplateSQL(expectedSQL, "testTemplate_14.xml");
    }

    public void testTemplate15() throws Exception
    {
        String[] expectedSQL = new String[] { "SELECT * FROM ws:article WHERE ((ws:synopsis LIKE '%Liver%' AND (ws:journal LIKE '%Medicine Today%'))) ORDER BY cmis:name ASC,ws:journal ASC",
                "SELECT * FROM ws:blogPost WHERE ((ws:synopsis LIKE '%Liver%' AND (ws:url LIKE '%http://blog.b.log%'))) ORDER BY cmis:name ASC,ws:url ASC" };

        assertTemplateSQL(expectedSQL, "testTemplate_15.xml");
    }

    public void testTemplate16() throws Exception
    {
        /*CONTAINS (is empty) should not be rendered in SQL statement*/
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (cmis:name='foo') ORDER BY cmis:name ASC,cmis:lastModifiedBy DESC", "testTemplate_16.xml");
    }

    public void testTemplate17() throws Exception
    {
        /*Both predicates in WHERE clause CONTAINS and standard SQL statemtns [LIKE, equals, etc.]*/
        assertTemplateSQL(
                "SELECT a.* FROM cmis:document AS a JOIN cm:author AS b ON a.cmis:objectId=b.cmis:objectId WHERE ((a.cmis:name='foo' AND b.cm:author LIKE '%E. Hemingway%')) AND (CONTAINS(a,'MyC0ntent')) ORDER BY a.cmis:name ASC,a.cmis:lastModifiedBy DESC",
                "testTemplate_17.xml");
    }

    public void testTemplate18() throws Exception
    {
        try
        {
            /*Test of exception case, unsupported operation definition cbr_all*/
            assertTemplateSQL("InvalidOperation exception test", "testTemplate_18.xml");
            fail("Error Case no more working: The CBR_ALL element is not supported by this processor.");
        }
        catch (OwInvalidOperationException invalidEx)
        {
            assertNotNull(invalidEx);
        }
    }

    public void testTemplate19() throws Exception
    {
        /*Both predicates in WHERE clause CONTAINS and standard SQL statemtns [LIKE, equals, etc.]*/
        assertTemplateSQL("SELECT * FROM cmis:document WHERE (cmis:name='foo') ORDER BY cmis:name ASC", "testTemplate_19.xml");
    }

}
