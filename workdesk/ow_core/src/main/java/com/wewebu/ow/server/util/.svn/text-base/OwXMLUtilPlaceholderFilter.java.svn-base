package com.wewebu.ow.server.util;

import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

/**
 *<p>
 * Utility class for XML access and debugging.<br/>
 * Implements OwXMLUtil with a OwAttributeBag interface.<br/>
 * So the place holder values are converted with the given OwAttributeBag.
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
 *@see OwStandardOptionXMLUtil
 */
@SuppressWarnings("rawtypes")
public class OwXMLUtilPlaceholderFilter implements OwXMLUtil
{
    private OwAttributeBag m_placeholderattributeBag;

    protected OwXMLUtil m_wrappednode;

    /** construct a template XML util node
     * 
     * @param node_p OwXMLUtil wrapped node
     * @param placeholderattributeBag_p
     * @throws Exception
     */
    public OwXMLUtilPlaceholderFilter(OwXMLUtil node_p, OwAttributeBag placeholderattributeBag_p) throws Exception
    {
        m_wrappednode = node_p;
        m_placeholderattributeBag = placeholderattributeBag_p;
    }

    /** construct a template XML util node
     * 
     * @param node_p DOM Node
     * @param placeholderattributeBag_p
     * @throws Exception
     */
    public OwXMLUtilPlaceholderFilter(Node node_p, OwAttributeBag placeholderattributeBag_p) throws Exception
    {
        m_wrappednode = new OwStandardXMLUtil(node_p);
        m_placeholderattributeBag = placeholderattributeBag_p;
    }

    /** get the specified attribute value
     *  replace placeholder if specified 
     * 
     * @param attributeName_p
     * @return a {@link String}
     */
    protected String getAttributeValue(String attributeName_p) throws Exception
    {
        String value = getNode().getAttributes().getNamedItem(attributeName_p).getNodeValue().toString();

        return getPlaceholderValue(value);
    }

    /** get the value from the given node name
     *  replace placeholder if specified 
     *  
     * @param strNodeName_p
     * @return a {@link String}
     * @throws Exception
     */
    protected String getNodeValue(String strNodeName_p) throws Exception
    {
        String value = m_wrappednode.getSubNode(strNodeName_p).getFirstChild().getNodeValue().toString();

        return getPlaceholderValue(value);
    }

    /** get the value from the node 
     *  replace placeholder if specified 
     *  
     * @return a {@link String}
     * @throws Exception
     */
    protected String getNodeValue() throws Exception
    {
        String value = getNode().getFirstChild().getNodeValue().toString();

        return getPlaceholderValue(value);
    }

    public boolean getSafeBooleanValue(String strNodeName_p, boolean default_p)
    {
        try
        {
            String value = getNodeValue(strNodeName_p);
            if (value.length() > 0)
            {
                return value.equalsIgnoreCase("true");
            }
            else
            {
                return default_p;
            }
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    public int getSafeIntegerValue(String strNodeName_p, int default_p)
    {
        try
        {
            return Integer.parseInt(getNodeValue(strNodeName_p));
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    public String getSafeTextValue(String strNodeName_p, String default_p)
    {
        try
        {
            return getNodeValue(strNodeName_p);
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    public String getSafeTextValue(String default_p)
    {
        try
        {
            return getNodeValue();
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    /** get the placeholder name if specified
     *  placeholders have the following syntax:
     *  
     *  {$Placeholdername}
     *  
     * @param value_p
     * @return String placeholder value or given value if not found
     */
    protected String getPlaceholderValue(String value_p)
    {
        if (value_p.startsWith("{$"))
        {
            String placeholderkey = value_p.substring(2, value_p.length() - 1);
            // lookup placeholder
            try
            {
                return (String) m_placeholderattributeBag.getAttribute(placeholderkey);
            }
            catch (Exception e)
            {
                // ignore
            }
        }

        return value_p;
    }

    public boolean getSafeBooleanAttributeValue(String strAttributeName_p, boolean default_p)
    {
        try
        {
            String value = getAttributeValue(strAttributeName_p);
            if (value.length() > 0)
            {
                return value.equalsIgnoreCase("true");
            }
            else
            {
                return default_p;
            }
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    public int getSafeIntegerAttributeValue(String strAttributeName_p, int default_p)
    {
        try
        {
            String value = getAttributeValue(strAttributeName_p);
            return Integer.parseInt(value);
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    public String getSafeStringAttributeValue(String strAttributeName_p, String default_p)
    {
        try
        {
            return getAttributeValue(strAttributeName_p);
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    public List getSafeStringList()
    {
        return m_wrappednode.getSafeStringList();
    }

    public List getSafeCDATAList()
    {
        return m_wrappednode.getSafeCDATAList();
    }

    public List getSafeCDATAList(String strNodeName_p)
    {
        return m_wrappednode.getSafeCDATAList(strNodeName_p);
    }

    public List getSafeStringList(String strNodeName_p)
    {
        return m_wrappednode.getSafeStringList(strNodeName_p);
    }

    public Set getSafeStringSet(String strNodeName_p)
    {
        return m_wrappednode.getSafeStringSet(strNodeName_p);
    }

    // === deprecated methods to prevent direct DOM access, see Ticket 794

    public void writeHtmlDump(Writer w_p) throws Exception
    {
        m_wrappednode.writeHtmlDump(w_p);
    }

    public Node getNode()
    {
        return m_wrappednode.getNode();
    }

    public List getSafeNodeList()
    {
        return m_wrappednode.getSafeNodeList();
    }

    public List getSafeNodeList(String strNodeName_p)
    {
        return m_wrappednode.getSafeNodeList(strNodeName_p);
    }

    public Node getSubNode(String strNodeName_p) throws Exception
    {
        return m_wrappednode.getSubNode(strNodeName_p);
    }

    // === new methods to prevent direct DOM access, see Ticket 794
    public List getSafeUtilList(String nodeName_p, String itemName_p)
    {
        return m_wrappednode.getSafeUtilList(nodeName_p, itemName_p);
    }

    public List getSafeUtilList(String itemName_p)
    {
        return m_wrappednode.getSafeUtilList(itemName_p);
    }

    public OwXMLUtil getSubUtil(String nodeName_p) throws Exception
    {
        return m_wrappednode.getSubUtil(nodeName_p);
    }

    public URL getURLFromNode(String nodeName_p) throws MalformedURLException
    {
        return m_wrappednode.getURLFromNode(nodeName_p);
    }

    @Override
    public void writeHtmlDumpFiltered(Writer htmlWriter_p, Map<String, String> hiddenTags) throws Exception
    {
        m_wrappednode.writeHtmlDumpFiltered(htmlWriter_p, hiddenTags);

    }

    public String getName()
    {
        return this.m_wrappednode.getName();
    }
}