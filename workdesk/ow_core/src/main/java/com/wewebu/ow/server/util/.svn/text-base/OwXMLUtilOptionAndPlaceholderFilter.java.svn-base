package com.wewebu.ow.server.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;

/**
 *<p>
 * Utility class for XML access and debugging.<br/>
 * Overrides OwXMLUtilPlaceholderFilter with option IDs.<br/> 
 * Option ID's allow the selection of nodes through option ID attributes.
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
public class OwXMLUtilOptionAndPlaceholderFilter extends OwXMLUtilPlaceholderFilter
{
    /** maps node names to selective IDs e.g.: viewerservlet : viewerservlet.MyID */
    private OwAttributeBag m_optionsIDs;

    /** construct a template XML util node with option and placeholder configuration filter
     * 
     * @param node_p an {@link OwXMLUtil}
     * @param placeholderattributeBag_p OwAttributeBag with placeholders
     * @param optionsIDs_p OwAttributeBag of ID's for selective configuration maps node names to selective IDs e.g.: viewerservlet : viewerservlet.MyID
     * @throws Exception
     */
    public OwXMLUtilOptionAndPlaceholderFilter(OwXMLUtil node_p, OwAttributeBag placeholderattributeBag_p, OwAttributeBag optionsIDs_p) throws Exception
    {
        super(node_p, placeholderattributeBag_p);

        m_optionsIDs = optionsIDs_p;
    }

    /** construct a template XML util node with option and placeholder configuration filter
     * 
     * @param node_p a {@link Node}
     * @param placeholderattributeBag_p OwAttributeBag with placeholders
     * @param optionsIDs_p OwAttributeBag of ID's for selective configuration maps node names to selective IDs e.g.: viewerservlet : viewerservlet.MyID
     * @throws Exception
     */
    public OwXMLUtilOptionAndPlaceholderFilter(Node node_p, OwAttributeBag placeholderattributeBag_p, OwAttributeBag optionsIDs_p) throws Exception
    {
        super(new OwStandardOptionXMLUtil(node_p), placeholderattributeBag_p);

        m_optionsIDs = optionsIDs_p;
    }

    public Node getSubNode(String name_p) throws Exception
    {
        return m_wrappednode.getSubNode(getSelectedNodeName(name_p));
    }

    /** get the node name that was selected through the selectiveConfigurationIDs_p 
     * 
     * @param name_p
     * @return a {@link String}
     */
    protected String getSelectedNodeName(String name_p)
    {
        try
        {
            return m_optionsIDs.getAttribute(name_p).toString();
        }
        catch (Exception e)
        {
            return name_p;
        }
    }

    public List getSafeNodeList(String strNodeName_p)
    {
        try
        {
            return OwXMLDOMUtil.getSafeNodeList(getSubNode(strNodeName_p));
        }
        catch (Exception e)
        {
            return new ArrayList();
        }
    }

    public List getSafeStringList(String strNodeName_p)
    {
        try
        {
            return OwXMLDOMUtil.getSafeStringList(getSubNode(strNodeName_p));
        }
        catch (Exception e)
        {
            return new ArrayList();
        }
    }

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
}