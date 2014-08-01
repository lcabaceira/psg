package com.wewebu.ow.server.plug.efilekey.generator;

import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;

import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.parser.OwExprParser;
import com.wewebu.ow.server.plug.efilekey.parser.OwKeyParser;
import com.wewebu.ow.server.plug.efilekey.pattern.OwKeyPatternImpl;
import com.wewebu.ow.server.plug.efilekey.pattern.OwMetadataFormatterImpl;
import com.wewebu.ow.server.plug.efilekey.pattern.OwMetadataReference;

public class OwFKParserTest extends TestCase
{

    public Date newDate(int day_p, int month_p, int year_p)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day_p);
        c.set(Calendar.MONTH, month_p - 1);
        c.set(Calendar.YEAR, year_p);

        return c.getTime();
    }

    public void testPropertyReference() throws Exception
    {
        OwKeyParser parser = new OwKeyParser(new StringReader("{a_a:1}"));
        OwMetadataReference pr = parser.propertyReference();
        System.out.println(pr);
    }

    public void testPropertyReferenceWithparams() throws Exception
    {
        OwKeyParser parser = new OwKeyParser(new StringReader("{a.b..::::_a.1,\"DDMM\"}"));
        OwMetadataReference pr = parser.propertyReference();
        System.out.println(pr);
    }

    public void testIndexedPropertyReference() throws Exception
    {
        OwKeyParser parser = new OwKeyParser(new StringReader("CN{Address[0].Street}BD{birthDate,\"dd/MM\"}"));
        OwKeyPatternImpl pr = parser.readPatterns();
        OwTestPropertyResolver tr = new OwTestPropertyResolver();
        tr.setPropertyValue("birthDate", newDate(11, 3, 2011));
        tr.setPropertyValue("Address[0].Street", "Street");

        Set<String> pn = pr.getPropertyNames();
        for (String name : pn)
        {
            System.out.println(name);
            OwExprExpression nameExpr = OwExprParser.parse(name);
        }

        assertEquals("CNStreetBD11/03", pr.createStringImage(tr));
    }

    public void testPropertyReferenceImage() throws Exception
    {
        OwKeyParser parser = new OwKeyParser(new StringReader("{birthDate,\"dd/MM\"}"));
        OwMetadataReference pr = parser.propertyReference();
        OwTestPropertyResolver tr = new OwTestPropertyResolver();
        tr.setPropertyValue("birthDate", new Date());
        System.out.println(pr.createStringImage(tr));
    }

    public void testTokenizedReferenceImage() throws Exception
    {
        {
            OwKeyParser parser = new OwKeyParser(new StringReader("BD{birthDate,\"dd/MM\"}"));
            OwKeyPatternImpl pr = parser.readPatterns();
            OwTestPropertyResolver tr = new OwTestPropertyResolver();
            tr.setPropertyValue("birthDate", new Date());
            System.out.println(pr.createStringImage(tr));
        }
        {
            OwKeyParser parser = new OwKeyParser(new StringReader("{birthDate,\"dd/MM\"}"));
            OwKeyPatternImpl pr = parser.readPatterns();
            OwTestPropertyResolver tr = new OwTestPropertyResolver();
            tr.setPropertyValue("birthDate", new Date());
            System.out.println(pr.createStringImage(tr));
        }
    }

    public void testKeyPatternImage() throws Exception
    {
        {
            OwKeyParser parser = new OwKeyParser(new StringReader("CN{  date1,\"yyyyMMdd\"}BD{birthDate,\"dd/MM\"}"));
            OwKeyPatternImpl pr = parser.readPatterns();
            OwTestPropertyResolver tr = new OwTestPropertyResolver();
            tr.setPropertyValue("birthDate", new Date());
            tr.setPropertyValue("date1", new Date());

            System.out.println(pr.createStringImage(tr));

            parser = new OwKeyParser(new StringReader("CN{  date1,\"yyyyMMdd\"}$BD{birthDate,\"dd/MM\"}#"));
            pr = parser.readPatterns();
            tr = new OwTestPropertyResolver();
            tr.setPropertyValue("birthDate", new Date());
            tr.setPropertyValue("date1", new Date());
            System.out.println(pr.createStringImage(tr));

            parser = new OwKeyParser(new StringReader(".#:sddCN{  date1,\"yyyyMMdd\"}#BD{birthDate}$"));
            pr = parser.readPatterns();
            tr = new OwTestPropertyResolver();
            tr.setPropertyValue("birthDate", new Date());
            tr.setPropertyValue("date1", new Date());
            System.out.println(pr.createStringImage(tr));

        }
    }

    public void testFormatter() throws Exception
    {
        OwKeyParser parser = new OwKeyParser(new StringReader("\"DDMMYYYY\""));
        OwMetadataFormatterImpl param = parser.formatter();
        System.out.println(param.getFormatterValue());
    }

    public void testPropertyReferenceEscapes() throws Exception
    {
        OwKeyParser parser = new OwKeyParser(new StringReader("CN{owd\\:contractNumber}"));
        OwKeyPatternImpl pr = parser.readPatterns();
        Set propertyNames = pr.getPropertyNames();
        assertEquals(1, propertyNames.size());
        assertEquals("owd:contractNumber", propertyNames.iterator().next());
    }

}
