package com.wewebu.ow.server.util;

import java.io.InputStream;

import org.w3c.dom.Node;

/**
 *<p>
 * Extends OwStandardXMLUtil and keys selective nodes in the node map. 
 * So they can be selected with OwXMLUtilFilter.
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
 *@see OwXMLUtilPlaceholderFilter
 */
public class OwStandardOptionXMLUtil extends OwStandardXMLUtil
{
    /** XML node attribute name for selective id's */
    public static final String OPTION_ID_ATTRIBUTE_NAME = "optionid";

    public OwStandardOptionXMLUtil(Node node_p) throws Exception
    {
        super(node_p);
    }

    public OwStandardOptionXMLUtil(InputStream inputStream_p, String strRootNodeName_p) throws Exception
    {
        super(inputStream_p, strRootNodeName_p);
    }

    /** (overridden) get the name the node is keyed in the lookup map
     * 
     * @param node_p
     */
    protected String getKeyName(Node node_p)
    {
        String nodename = node_p.getNodeName();
        try
        {
            String option = node_p.getAttributes().getNamedItem(OPTION_ID_ATTRIBUTE_NAME).getFirstChild().getNodeValue().toString();
            if (option.length() == 0)
            {
                return nodename;
            }

            StringBuffer ret = new StringBuffer();
            ret.append(nodename);
            ret.append(".");
            ret.append(option);

            return ret.toString();
        }
        catch (Exception e)
        {
            return nodename;
        }
    }

}