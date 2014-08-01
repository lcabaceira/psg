package com.wewebu.ow.server.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class OwXMLDOMUtilTest extends TestCase
{

    protected Document document(InputStream is_p) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return db.parse(new InputSource(is_p));

    }

    protected Document document(String name_p) throws ParserConfigurationException, SAXException, IOException
    {
        InputStream is = OwXMLDOMUtilTest.class.getResourceAsStream(name_p);
        return document(is);
    }

    protected NodeList testNodes(String testName_p) throws ParserConfigurationException, SAXException, IOException
    {
        Document testDocument = document("xdt.xml");
        Element testData = testDocument.getElementById("testData");
        return testData.getElementsByTagName(testName_p);
    }

    protected Element testElement(String testName_p) throws ParserConfigurationException, SAXException, IOException
    {
        Document testDocument = document("xdt.xml");
        Element testData = testDocument.getDocumentElement();

        return (Element) testData.getElementsByTagName(testName_p).item(0);
    }

    public void testGetSafeBooleanValue() throws ParserConfigurationException, SAXException, IOException
    {
        Element testElement = testElement("testGetSafeBooleanValue");

        NodeList booleanTests = testElement.getElementsByTagName("boolean");
        {
            Node b1 = booleanTests.item(0);
            assertEquals(true, OwXMLDOMUtil.getSafeBooleanValue(b1, false));
        }
        {
            Node b2 = booleanTests.item(1);
            assertEquals(false, OwXMLDOMUtil.getSafeBooleanValue(b2, true));
        }
        {
            Node b3 = booleanTests.item(2);
            assertEquals(true, OwXMLDOMUtil.getSafeBooleanValue(b3, true));
            assertEquals(false, OwXMLDOMUtil.getSafeBooleanValue(b3, false));
        }

        {
            Node b4 = booleanTests.item(3);
            assertEquals(false, OwXMLDOMUtil.getSafeBooleanValue(b4, false));
            assertEquals(true, OwXMLDOMUtil.getSafeBooleanValue(b4, true));
        }

    }
}
