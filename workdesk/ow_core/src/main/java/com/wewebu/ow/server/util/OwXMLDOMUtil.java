package com.wewebu.ow.server.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *<p>
 * Implements DOM Node utility methods for XML access.<br/>
 * Please note: DOM is not thread safe.<br/>
 * To read the configuration for plugins and boot options use OwXMLUtil getters only.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public class OwXMLDOMUtil
{
    private Node m_Node;

    /** construct an empty XML Dom node
     * @param strRootNodeName_p String Name of the root node
     */
    public OwXMLDOMUtil(String strRootNodeName_p) throws Exception
    {
        // === create empty document
        Document doc = OwXMLDOMUtil.getNewDocument();

        m_Node = doc.createElementNS("", strRootNodeName_p);
    }

    /** add a subnode to the node wrapped by this object
     * @param strSubNodeName_p String name of the new node
     * @return Node DOM Node
     */
    public Node addNode(String strSubNodeName_p)
    {
        Document doc = m_Node.getOwnerDocument();
        Element newNode = doc.createElementNS("", strSubNodeName_p);

        m_Node.appendChild(newNode);

        return newNode;
    }

    /** add a text node in the wrapped node
     * @param strNodeName_p String tag name of new node
     * @param strText_p String text to set as new textnode
     */
    public void addTextNode(String strNodeName_p, String strText_p)
    {
        Document doc = m_Node.getOwnerDocument();
        addNode(strNodeName_p).appendChild(doc.createTextNode(strText_p));
    }

    /** get an input stream for the XML content of the wrapped node
    *
    * @return InputStream
    */
    public InputStream getInputStream() throws Exception
    {
        // === transform XML into bytearray
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(m_Node);

        java.io.ByteArrayOutputStream outStr = new java.io.ByteArrayOutputStream();

        StreamResult result = new StreamResult(outStr);

        transformer.transform(source, result);

        outStr.flush();

        // === get input stream from bytearray
        return new ByteArrayInputStream(outStr.toByteArray());
    }

    private static void htmlColour(String strXML, Writer w_p) throws IOException
    {

        boolean startComment = false;
        for (int i = 0; i < strXML.length(); i++)
        {

            char c = 'a';
            char next = 'b';
            char prev = 'c';
            char nextt = 'd';
            char nexttt = 'e';
            char prevv = 'f';

            c = strXML.charAt(i);
            if (i + 3 < strXML.length())
            {
                next = strXML.charAt(i + 1);
                nextt = strXML.charAt(i + 2);
                nexttt = strXML.charAt(i + 3);
            }
            if (i > 2)
            {
                prev = strXML.charAt(i - 1);
                prevv = strXML.charAt(i - 2);

            }
            // #0000FF - color blue
            // #ff0000 - color red
            // #004000 - color green
            // #008000 - color green 2
            switch (c)
            {
                case '<':
                    if ((next == '!') && (nextt == '-') && (nexttt == '-')) //comment open
                    {
                        startComment = true;
                        w_p.write("<font color='#008000'><b>&#60;</b></font><font color='#008000'></b>");
                    }
                    else
                    {
                        if (startComment)
                        {
                            w_p.write("&#60;"); // <
                        }
                        else
                        {
                            w_p.write("</b></font><font color='#0000ff'><b>&#60;</b></font>");
                        }
                    }
                    break;

                case '>':
                    if ((prev == '-') && (prevv == '-'))
                    {
                        startComment = false;
                        w_p.write("</font><font color='#008000'><b>&#62;</b></font>");
                    }
                    else
                    {
                        if (startComment)
                        {
                            w_p.write("&#62;"); // >
                        }
                        else
                        {
                            w_p.write("<font color='#0000ff'><b>&#62;</b></font><b><font color='#ff0000'>");
                        }
                    }
                    break;

                case '\t':
                    w_p.write("&nbsp;&nbsp;&nbsp;");
                    break;

                case ' ':
                    w_p.write("&nbsp;");
                    break;

                case '\n':
                    w_p.write("<br>");
                    break;

                default:
                    w_p.write(c);
                    break;
            }
        }

    }

    /** write XML Node as HTML to a writer object
     * @param w_p a {@link Writer}
     * @param node_p a {@link Node}
     */
    public static void writeHtmlDump(Writer w_p, Node node_p) throws Exception
    {

        String strXML = "";

        try
        {
            strXML = OwXMLDOMUtil.toString(node_p);
        }
        catch (Exception e)
        {
            strXML = e.getLocalizedMessage();
        }

        htmlColour(strXML, w_p);
    }

    /**
     * get the child element
     * @param parent_p
     * @param childElementName_p
     * @return an {@link Element}
     */
    public static Element getChildElement(Element parent_p, String childElementName_p)
    {
        // return (Element) getChildNode(parent, childElementName);
        for (Node node = parent_p.getFirstChild(); node != null; node = node.getNextSibling())
        {
            String sNodeName = node.getNodeName();
            if ((node.getNodeType() == Node.ELEMENT_NODE) && (sNodeName != null) && sNodeName.equals(childElementName_p))
            {
                return (Element) node;
            }
        }
        return null;
    }

    /**
     * get the text of a node
     * @param element_p
     * @return a {@link String}
     */
    public static String getElementText(Element element_p)
    {
        if (element_p != null)
        {
            Node oChildNode = element_p.getFirstChild();
            if (oChildNode != null)
            {
                if (oChildNode.getNodeType() == Node.TEXT_NODE)
                {
                    return oChildNode.getNodeValue();
                }
            }
        }
        return null;
    }

    /**
     * get text element of a given child
     * @param parent_p
     * @param childElementName_p
     * @return a {@link String}
     */
    public static String getChildElementText(Element parent_p, String childElementName_p)
    {
        Element child = (Element) OwXMLDOMUtil.getChildNode(parent_p, childElementName_p);
        if (child != null)
        {
            return getElementText(child);
        }
        return null;
    }

    /**
     * get a given child node
     * @param node_p
     * @param childToFind_p
     * @return a {@link Node}
     */
    public static Node getChildNode(Node node_p, String childToFind_p)
    {
        for (Node n = node_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            String sNodeName = n.getNodeName();
            if (sNodeName != null && sNodeName.equals(childToFind_p))
            {
                return n;
            }
        }
        return null;
    }

    /**
     * encodes special XML characters from the given string
     * @param xmlString_p
     * @return a {@link String}
     */
    public static String encodeSpecialCharactersForXML(String xmlString_p)
    {
        if (xmlString_p == null)
        {
            return null;
        }
        int iSize = xmlString_p.length();

        StringBuffer sb = new StringBuffer(iSize);
        for (int i = 0; i < iSize; i++)
        {
            char chr = xmlString_p.charAt(i);
            switch (chr)
            {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                default:
                    sb.append(chr);
            }
        }
        return sb.toString();
    }

    /** create a XML conform text representation */
    public static String makeXMLString(String sText_p)
    {
        StringBuffer ret = new StringBuffer();

        for (int i = 0; i < sText_p.length(); i++)
        {
            char c = sText_p.charAt(i);
            switch (c)
            {
                case '&':
                    ret.append("&amp;");
                    break;

                case '"':
                    ret.append("&quot;");
                    break;

                case '<':
                    ret.append("&#60;");
                    break;

                case '>':
                    ret.append("&#62;");
                    break;

                default:
                    ret.append(c);
                    break;
            }
        }

        return ret.toString();
    }

    /** write XML Node to file
     * @param file_p File to write XML to.
     * @param node_p Node
     * 
     * @return true on success
     */
    public static boolean toFile(File file_p, Node node_p)
    {
        try
        {
            // Use a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(node_p);

            StreamResult result = new StreamResult(file_p);

            transformer.transform(source, result);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /** write XML Node to a String
     * @param node_p Node
     * 
     * @return XML string
     * 
     * @throws TransformerException 
     */
    public static String toString(Node node_p) throws TransformerException
    {
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(node_p);

        java.io.StringWriter outStr = new java.io.StringWriter();

        StreamResult result = new StreamResult(outStr);

        transformer.transform(source, result);

        return outStr.toString();
    }

    /** retrieve a node list in a subnode 
        *
        * @param node_p Node
        * @return List of DOM Nodes
        */
    public static List getSafeNodeList(Node node_p)
    {
        List list = new ArrayList();

        try
        {
            // === create column info and property list
            for (Node n = node_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    // add string XML node to return list
                    list.add(n);
                }
            }
        }
        catch (Exception e)
        {
            return list;
        }

        return list;
    }

    /** get an input stream for the XML content of a given node
        *
        * @return InputStream
        */
    public static InputStream getInputStream(Node node_p) throws Exception
    {
        // === transform XML into bytearray
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(node_p);

        java.io.ByteArrayOutputStream outStr = new java.io.ByteArrayOutputStream();

        StreamResult result = new StreamResult(outStr);

        transformer.transform(source, result);

        outStr.flush();

        // === get input stream from bytearray
        return new ByteArrayInputStream(outStr.toByteArray());
    }

    /** get the value of a boolean attribute [true | false], catch exception 
       *
       * @param node_p {@link Node} to get the attribute from
       * @param strAttributeName_p name of the String attribute
       * @param fDefault_p Default string in case the attribute could not be found
       *
       * @return boolean value of attribute or fDefault_p on failure
       */
    public static boolean getSafeBooleanAttributeValue(Node node_p, String strAttributeName_p, boolean fDefault_p)
    {
        try
        {
            String sRet = node_p.getAttributes().getNamedItem(strAttributeName_p).getNodeValue();
            if (sRet.length() > 0)
            {
                return sRet.equalsIgnoreCase("true");
            }
            else
            {
                return fDefault_p;
            }
        }
        catch (Exception e)
        {
            return fDefault_p;
        }
    }

    /**
     * get the value of a boolean node [true | false], catch exception
     * 
     * @param node_p {@link Node} to get the attribute from
     * @param fDefault_p Default value in case the node could not be found
     * @return boolean value of node or fDefault_p on failure
     */
    public static boolean getSafeBooleanValue(Node node_p, boolean fDefault_p)
    {
        try
        {
            Node firstChild = node_p.getFirstChild();
            String firstValue = firstChild.getNodeValue();

            if (firstValue.equalsIgnoreCase("true"))
            {
                return true;
            }
            else
            {
                return firstValue.equalsIgnoreCase("false") ? false : fDefault_p;
            }
        }
        catch (Exception e)
        {
            return fDefault_p;
        }
    }

    /** get the value of a string attribute, catch exception 
        *
        * @param node_p a {@link Node} to get the attribute from
        * @param strAttributeName_p name of the String attribute
        * @param strDefault_p Default string in case the attribute could not be found
        *
        * @return string value of attribute or strDefault_p on failure
        */
    public static String getSafeStringAttributeValue(Node node_p, String strAttributeName_p, String strDefault_p)
    {
        try
        {
            String sRet = node_p.getAttributes().getNamedItem(strAttributeName_p).getNodeValue();
            if (sRet.length() > 0)
            {
                return sRet;
            }
            else
            {
                return strDefault_p;
            }
        }
        catch (Exception e)
        {
            return strDefault_p;
        }
    }

    /** get the value of a string attribute, catch exception
     *  
     * @param node_p a {@link Node} to get the attribute from
     * @param strAttributeName_p name of the String attribute
     * @param iDefault_p Default int in case the attribute could not be found
     * 
     * @return int value of attribute or iDefault_p on failure
     */
    public static int getSafeIntegerAttributeValue(Node node_p, String strAttributeName_p, int iDefault_p)
    {
        try
        {
            String sRet = node_p.getAttributes().getNamedItem(strAttributeName_p).getNodeValue();
            if (sRet.length() > 0)
            {
                return Integer.parseInt(sRet);
            }
            else
            {
                return iDefault_p;
            }
        }
        catch (Exception e)
        {
            return iDefault_p;
        }
    }

    /** retrieve a string list in a node 
        *
        * @param node_p Node
        * @return List of Strings
        */
    public static Set getSafeStringSet(Node node_p)
    {
        Set list = new HashSet();
        try
        {
            // === create column info and property list
            for (Node n = node_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    // add string XML node to return list
                    list.add(n.getFirstChild().getNodeValue());
                }
            }
        }
        catch (Exception e)
        {
            return list;
        }

        return list;
    }

    /** retrieve a string list in a node 
     *
     * @param node_p Node
     * @return List of Strings
     */
    public static List getSafeStringList(Node node_p)
    {
        List list = new ArrayList();
        try
        {
            // === create column info and property list
            for (Node n = node_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    // add string XML node to return list
                    list.add(n.getFirstChild().getNodeValue());
                }
            }
        }
        catch (Exception e)
        {
            return list;
        }

        return list;
    }

    /** retrieve a CDATA defined strings list from a node definition 
    *
    * @param node_p Node
    * @return List of CDATA Strings
    */
    public static List getSafeCDATAList(Node node_p)
    {
        List list = new ArrayList();
        try
        {
            // === create column info and property list
            for (Node n = node_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() == Node.CDATA_SECTION_NODE)
                {
                    // add string XML node to return list
                    list.add(n.getNodeValue());
                }
            }
        }
        catch (Exception e)
        {
            return list;
        }

        return list;
    }

    /**
     * get a XML document from a given InputStream
     * @param inputStream_p
     * @return a {@link Document}
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static Document getDocumentFromInputStream(InputStream inputStream_p) throws IOException, SAXException, ParserConfigurationException
    {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return db.parse(inputStream_p);
    }

    /**
     * get a new XML document
     * @return a {@link Document}
     * @throws ParserConfigurationException
     */
    public static Document getNewDocument() throws ParserConfigurationException
    {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return db.newDocument();
    }

    /**
     * get a XML document from a given String
     * @param xml_p
     * @return a {@link Document}
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static Document getDocumentFromString(String xml_p) throws IOException, SAXException, ParserConfigurationException
    {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml_p));
        return db.parse(is);
    }

    /**
     * set a node attribute to the node of a given document
     * @param document_p
     * @param node_p
     * @param attrName_p
     * @param attrValue_p
     */
    public static void setNodeAttribute(Document document_p, Node node_p, String attrName_p, String attrValue_p)
    {
        NamedNodeMap al = node_p.getAttributes();
        if (al != null)
        {
            Node attrNode = document_p.createAttribute(attrName_p);
            attrNode.setNodeValue(attrValue_p);
            al.setNamedItem(attrNode);
        }
    }

    /**
     * Prepares the given DOM Node for concurrent access. 
     *  
     * @param node {@link Node} the initialized DOM node
     * @since 2.5.3.2
     */
    public static void initialize(Node node)
    {
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node child = nodes.item(i);
            child.getNodeValue();

            NamedNodeMap attributes = child.getAttributes();
            if (attributes != null)
            {
                int length = attributes.getLength();
                for (int j = 0; j < length; j++)
                {
                    Node attribute = attributes.item(j);
                    attribute.getNodeValue();
                }
            }
            initialize(child);
        }

    }

    /**
     * Prepares the given DOM Document for concurrent access. 
     *  
     * @param document {@link Document} the initialized document
     * @since 2.5.3.2
     */
    public static void initialize(Document document)
    {
        Element element = document.getDocumentElement();
        initialize(element);

    }

    public String toString()
    {
        if (null != m_Node)
        {
            try
            {
                return toString(m_Node);
            }
            catch (TransformerException e)
            {
                return e.getMessage();
            }
        }
        else
        {
            return "<null>";
        }
    }

    public static void writeHtmlDumpFiltered(Writer htmlWriter_p, Node m_Node, Map<String, String> values) throws Exception
    {

        String strXML = "";

        try
        {
            strXML = OwXMLDOMUtil.toString(m_Node);
        }
        catch (Exception e)
        {
            strXML = e.getLocalizedMessage();
        }

        for (Map.Entry<String, String> e : values.entrySet())
        {
            strXML = strXML.replaceAll("(?s)<" + e.getKey() + "[^>]*>.*?</" + e.getKey() + ">", "<" + e.getKey() + ">" + e.getValue() + "<" + e.getKey() + ">");
        }

        htmlColour(strXML, htmlWriter_p);

    }
}