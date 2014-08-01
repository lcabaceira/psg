package com.wewebu.ow.server.util;

import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 * <p>
 * Implements OwXMLUtil utility class for structured configuration data access.
 * </p>
 * 
 * <p>
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 * </font>
 * </p>
 */
@SuppressWarnings("rawtypes")
public class OwStandardXMLUtil implements OwXMLUtil
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardXMLUtil.class);

    /**
     * Defines a search order based on the order in which the searched nodes are
     * encountered in a pre-order traversal of the Document tree.
     */
    public static final String DEPTH_FIRST_PREORDER = "depth_first_preorder";

    /**
     * Defines a search order based on the order in which the searched nodes are
     * encountered in level-first-order traversal of the Document tree.
     */
    public static final String WIDTH_FIRST_LEVEL_ORDER = "width_first_level_order";

    /** the node the Utility class is working on */
    private Node m_Node;

    /** hash map of the sub nodes for fast access */
    private HashMap m_ValueNodeMap;

    /** encoding type */
    private String m_FileEncoding = "UTF-8";

    /**
     * construct an empty configuration
     */
    public OwStandardXMLUtil() throws Exception
    {
        // === create empty document
        Document doc = OwXMLDOMUtil.getNewDocument();

        Element rootNode = doc.createElementNS("", "empty");
        setNode(rootNode);
    }

    /**
     * construct the utility class, wrap around the given node
     * @param node_p DOM Node
     */
    public OwStandardXMLUtil(Node node_p) throws Exception
    {
        setNode(node_p);
    }

    /**
     * Construct the utility class, wrap around the given first root node. The
     * search order is {@link #DEPTH_FIRST_PREORDER}.
     * 
     * @param inputStream_p
     *            Input Stream with XML Content
     * @param strRootNodeName_p
     *            Name of the root node to be wrapped by the utility class. The
     *            root node is searched in the order in which nodes are
     *            encountered in a pre-order traversal of the Document tree (see
     *            {@link #DEPTH_FIRST_PREORDER}).
     */
    public OwStandardXMLUtil(java.io.InputStream inputStream_p, String strRootNodeName_p) throws Exception
    {
        this(inputStream_p, strRootNodeName_p, DEPTH_FIRST_PREORDER);
    }

    /**
     * Construct the utility class, wrap around the given first root node.
     * 
     * @param inputStream_p
     *            Input Stream with XML Content
     * @param strRootNodeName_p
     *            Name of the root node to be wrapped by the utility class. The
     *            root node is searched in the order given by
     *            <code>rootSearchMode_p</code> parameter.
     * @param rootSearchMode_p
     *            Defines the search order for the root node search. Must be one
     *            of {@link #DEPTH_FIRST_PREORDER} or
     *            {@link #WIDTH_FIRST_LEVEL_ORDER}.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public OwStandardXMLUtil(java.io.InputStream inputStream_p, String strRootNodeName_p, String rootSearchMode_p) throws Exception
    {
        OwUnicodeInputStream uin = new OwUnicodeInputStream(inputStream_p, m_FileEncoding);

        // check and skip possible BOM bytes
        m_FileEncoding = uin.getEncoding();
        InputStreamReader in;
        setFileEncoding(m_FileEncoding);

        if (m_FileEncoding == null)
        {
            in = new InputStreamReader(uin);
        }
        else
        {
            in = new InputStreamReader(uin, m_FileEncoding);
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new InputSource(in));
        OwXMLDOMUtil.initialize(doc);

        if (strRootNodeName_p != null)
        {
            if (DEPTH_FIRST_PREORDER.equals(rootSearchMode_p))
            {
                org.w3c.dom.NodeList Nodes = doc.getElementsByTagName(strRootNodeName_p);
                setNode(Nodes.item(0));
            }
            else if (WIDTH_FIRST_LEVEL_ORDER.equals(rootSearchMode_p))
            {
                Node rootNode = doc.getDocumentElement();
                LinkedList nodesFifo = new LinkedList();
                nodesFifo.addFirst(rootNode);
                Node foundNode = null;
                while (!nodesFifo.isEmpty() && foundNode == null)
                {
                    Node currentNode = (Node) nodesFifo.removeLast();
                    if (strRootNodeName_p.equals(currentNode.getNodeName()) || strRootNodeName_p.equals(currentNode.getLocalName()))
                    {
                        foundNode = currentNode;
                    }
                    else
                    {
                        NodeList currentChildren = currentNode.getChildNodes();
                        int childrenCount = currentChildren.getLength();
                        for (int i = 0; i < childrenCount; i++)
                        {
                            nodesFifo.addFirst(currentChildren.item(i));
                        }
                    }
                }
                setNode(foundNode);
            }
            else
            {
                String msg = "OwStandardXMLUtil: Invalid rootSearchMode_p mode=" + rootSearchMode_p + ". Expected one of DEPTH_FIRST_PREORDER = \"" + DEPTH_FIRST_PREORDER + "\" or WIDTH_FIRST_LEVEL_ORDER = \"" + WIDTH_FIRST_LEVEL_ORDER + "\"";
                LOG.error(msg);
                throw new OwInvalidOperationException(msg);
            }
        }
        else
        {
            setNode(doc.getDocumentElement());
        }
    }

    /**
     * (overridable) get the name the node is keyed in the lookup map
     * 
     * @param node_p
     */
    protected String getKeyName(Node node_p)
    {
        return node_p.getNodeName();
    }

    /**
     * retrieve a string list in the node
     * 
     * @return List of Strings
     */
    public List getSafeStringList()
    {
        return OwXMLDOMUtil.getSafeStringList(getNode());
    }

    /**
     * retrieve a CDATA strings list in the node
     * 
     * @return List of CDATA Strings
     */
    public List getSafeCDATAList()
    {
        return OwXMLDOMUtil.getSafeCDATAList(getNode());
    }

    /**
     * retrieve a CDATA string list in a subnode
     * 
     * @return List of Strings
     */
    public List getSafeCDATAList(String strNodeName_p)
    {
        try
        {
            return OwXMLDOMUtil.getSafeCDATAList(getSubNode(strNodeName_p));
        }
        catch (Exception e)
        {
            return new LinkedList();
        }
    }

    /**
     * retrieve a string list in a subnode
     * 
     * @param strNodeName_p String name of the subnode with the string list
     * @return List of Strings
     */
    public List getSafeStringList(String strNodeName_p)
    {
        try
        {
            return OwXMLDOMUtil.getSafeStringList(getSubNode(strNodeName_p));
        }
        catch (Exception e)
        {
            return new LinkedList();
        }
    }

    /**
     * retrieve a string list in a subnode
     * 
     * @param strNodeName_p String name of the subnode with the string list
     * @return List of Strings
     */
    public Set getSafeStringSet(String strNodeName_p)
    {
        try
        {
            return OwXMLDOMUtil.getSafeStringSet(getSubNode(strNodeName_p));
        }
        catch (Exception e)
        {
            return new HashSet();
        }
    }

    /**
     * get the value of a string attribute, catch exception
     * 
     * @param strAttributeName_p name of the String attribute
     * @param strDefault_p Default string in case the attribute could not be found
     * @return string value of attribute or strDefault_p on failure
     */
    public String getSafeStringAttributeValue(String strAttributeName_p, String strDefault_p)
    {
        try
        {
            String sRet = m_Node.getAttributes().getNamedItem(strAttributeName_p).getNodeValue();
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

    /**
     * get the value of a string attribute, catch exception
     * 
     * @param strAttributeName_p name of the String attribute
     * @param iDefault_p Default int in case the attribute could not be found
     * @return int value of attribute or iDefault_p on failure
     */
    public int getSafeIntegerAttributeValue(String strAttributeName_p, int iDefault_p)
    {
        try
        {
            String sRet = m_Node.getAttributes().getNamedItem(strAttributeName_p).getNodeValue();
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

    /**
     * get the value of a boolean attribute [true | false], catch exception
     * 
     * @param strAttributeName_p name of the String attribute
     * @param fDefault_p Default string in case the attribute could not be found
     * @return boolean value of attribute or fDefault_p on failure
     */
    public boolean getSafeBooleanAttributeValue(String strAttributeName_p, boolean fDefault_p)
    {
        return OwXMLDOMUtil.getSafeBooleanAttributeValue(m_Node, strAttributeName_p, fDefault_p);
    }

    /**
     * get the value of a sub boolean node [true | false], catch exception
     * 
     * @param strNodeName_p name of the subnode
     * @param fDefault_p Default value in case the node could not be found
     * @return boolean value of node or fDefault_p on failure
     */
    public boolean getSafeBooleanValue(String strNodeName_p, boolean fDefault_p)
    {
        try
        {
            return getSubNode(strNodeName_p).getFirstChild().getNodeValue().equalsIgnoreCase("true");
        }
        catch (Exception e)
        {
            return fDefault_p;
        }
    }

    /**
     * Helper method to create an URL from given configuration node.
     * @param nodeName_p String name of child node, where to extract the URL
     * @return java.net.URL
     * @throws MalformedURLException if the extracted text is not URL conform string
     * @since 4.0.0.0 
     */
    public URL getURLFromNode(String nodeName_p) throws MalformedURLException
    {
        String nodeText = null;
        nodeText = getSafeTextValue(nodeName_p, null);
        return new URL(nodeText);
    }

    /**
     * get the value of a sub Integer node, catch exception
     * 
     * @param strNodeName_p name of the subnode
     * @param fDefault_p Default value in case the node could not be found
     * @return int value of node or fDefault_p on failure
     */
    public int getSafeIntegerValue(String strNodeName_p, int fDefault_p)
    {
        try
        {
            return Integer.parseInt(getSubNode(strNodeName_p).getFirstChild().getNodeValue());
        }
        catch (Exception e)
        {
            return fDefault_p;
        }
    }

    /**
     * get the value of a sub text node, catch exception
     * 
     * @param strNodeName_p name of the text subnode
     * @param strDefault_p Default string in case the node could not be found
     * @return string value of subnode or strDefault_p on failure
     */
    public String getSafeTextValue(String strNodeName_p, String strDefault_p)
    {
        try
        {
            return getSubNode(strNodeName_p).getFirstChild().getNodeValue();
        }
        catch (Exception e)
        {
            return strDefault_p;
        }
    }

    /**
     * get the value of THE node, catch exception
     * 
     * @param strDefault_p Default string in case the node could not be found
     * 
     * @return string value of subnode or strDefault_p on failure
     */
    public String getSafeTextValue(String strDefault_p)
    {
        try
        {
            return m_Node.getFirstChild().getNodeValue();
        }
        catch (Exception e)
        {
            return strDefault_p;
        }
    }

    // === deprecated methods to prevent direct DOM access, see Ticket 794
    /**
     * return the wrapped DOM Node reference
     * 
     * @return Node
     */
    public Node getNode()
    {
        return m_Node;
    }

    @SuppressWarnings("unchecked")
    protected void setNode(Node node_p)
    {
        m_Node = node_p;

        // === create hashmap of the subnodes of that Node
        m_ValueNodeMap = new HashMap();

        for (Node n = node_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                // === cache the first children
                m_ValueNodeMap.put(getKeyName(n), n);
            }
        }
    }

    /**
     * get the subnode with the given tag name
     * 
     * @param strNodeName_p tag name of requested node
     * @return org.w3c.dom.Node
     */
    public Node getSubNode(String strNodeName_p) throws Exception
    {
        return (Node) m_ValueNodeMap.get(strNodeName_p);
    }

    /**
     * retrieve a node list in a subnode
     * 
     * @return List of DOM Nodes
     */
    public List getSafeNodeList()
    {
        return OwXMLDOMUtil.getSafeNodeList(getNode());
    }

    /**
     * retrieve a node list in a subnode
     * 
     * @param strNodeName_p String name of the subnode with the node list
     * @return List of DOM Nodes
     */
    public List getSafeNodeList(String strNodeName_p)
    {
        try
        {
            return OwXMLDOMUtil.getSafeNodeList(getSubNode(strNodeName_p));
        }
        catch (Exception e)
        {
            return new LinkedList();
        }
    }

    /**
     * write configuration as HTML to a writer object
     * 
     * @param w_p a {@link Writer}
     */
    public void writeHtmlDump(Writer w_p) throws Exception
    {
        OwXMLDOMUtil.writeHtmlDump(w_p, m_Node);
    }

    @SuppressWarnings("unchecked")
    public List getSafeUtilList(String nodeName_p, String itemName_p)
    {
        List list = new LinkedList();

        try
        {
            Node subnode = getSubNode(nodeName_p);

            // === create column info and property list
            for (Node n = subnode.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    if ((null == itemName_p) || (itemName_p.equals(n.getNodeName())))
                    {
                        // add string XML node to return list
                        list.add(new OwStandardXMLUtil(n));
                    }
                }
            }
        }
        catch (Exception e)
        {
            return list;
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public List getSafeUtilList(String itemName_p)
    {
        List list = new LinkedList();

        try
        {
            Node subnode = getNode();

            // === create column info and property list
            for (Node n = subnode.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    if ((null == itemName_p) || (itemName_p.equals(n.getNodeName())))
                    {
                        // add string XML node to return list
                        list.add(new OwStandardXMLUtil(n));
                    }
                }
            }
        }
        catch (Exception e)
        {
            return list;
        }

        return list;
    }

    public OwXMLUtil getSubUtil(String strName_p) throws Exception
    {
        Node n = (Node) m_ValueNodeMap.get(strName_p);
        if (null == n)
        {
            return null;
        }
        else
        {
            return new OwStandardXMLUtil(n);
        }
    }

    public String getFileEncoding()
    {
        return m_FileEncoding;
    }

    public void setFileEncoding(String mFileEncoding_p)
    {
        m_FileEncoding = mFileEncoding_p;
    }

    @Override
    public void writeHtmlDumpFiltered(Writer htmlWriter_p, Map<String, String> hiddenTags) throws Exception
    {
        OwXMLDOMUtil.writeHtmlDumpFiltered(htmlWriter_p, m_Node, hiddenTags);
    }

    public String getName()
    {
        return getNode().getNodeName();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(getClass().getCanonicalName());
        builder.append("[node= ");
        builder.append(getName());
        builder.append("]");
        return builder.toString();
    }
}