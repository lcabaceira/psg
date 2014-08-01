package com.wewebu.ow.server.field;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Priority rule definition standard implementation (XML Search Node based).<br/>
 * Rules Engine for Highlighting in Hit List.
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
public class OwStandardPriorityRule extends OwPriorityRuleBase
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardPriorityRule.class);

    public static final String NODE_NAME_SEARCHNODE = "SearchNode";
    private static final String ATTRIBUTE_NAME_OPERATOR = "op";
    private static final String NODE_NAME_LITERAL = "literal";
    private static final String NODE_NAME_SYMNAME = "symname";

    private OwSearchNode m_searchnode;

    /** init from config node
     * 
     * @param configNode_p priority rule DOM node
     * @param fieldefinitionprovider_p OwFieldDefinitionProvider
     *  
     * @throws Exception
     */
    public void init(Node configNode_p, OwFieldDefinitionProvider fieldefinitionprovider_p) throws Exception
    {
        m_scontainer = OwXMLDOMUtil.getSafeStringAttributeValue(configNode_p, ATTRIBUTE_NAME_CONTAINER, null);

        OwXMLUtil prionode = new OwStandardXMLUtil(configNode_p);

        String sResource = OwXMLDOMUtil.getSafeStringAttributeValue(configNode_p, ATTRIBUTE_NAME_RESOURCE, null);
        m_sresource = sResource;
        m_searchnode = scanSearchNode(prionode.getSubNode(NODE_NAME_SEARCHNODE), fieldefinitionprovider_p, sResource);

        m_sStyleClass = prionode.getSafeTextValue(NODE_NAME_COLOR, "owpriostyle");
    }

    /** recursive scan the config node and build the search node tree
     * 
     * @param configNode_p DOM Node
     * @throws Exception
     */
    private OwSearchNode scanSearchNode(Node configNode_p, OwFieldDefinitionProvider fieldefinitionprovider_p, String sResource_p) throws Exception
    {
        String sOP = OwXMLDOMUtil.getSafeStringAttributeValue(configNode_p, ATTRIBUTE_NAME_OPERATOR, null);

        if (null == sOP)
        {
            String msg = "OwStandardPriorityRule.scanSearchNode: Define Op in the PriorityRule search / criteria  Node.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        if (configNode_p.getNodeName().equals(NODE_NAME_SEARCHNODE))
        {
            // === create search node
            int iOP = OwSearchNode.class.getField(sOP).getInt(null);

            OwSearchNode searchnode = new OwSearchNode(iOP, OwSearchNode.NODE_TYPE_PROPERTY);

            // === recurse search node
            for (Node n = configNode_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    searchnode.add(scanSearchNode(n, fieldefinitionprovider_p, sResource_p));
                }
            }

            return searchnode;
        }
        else
        {
            // === create criteria node
            int iOP = OwSearchOperator.class.getField(sOP).getInt(null);

            OwXMLUtil critnode = new OwStandardXMLUtil(configNode_p);

            String sSymname = critnode.getSafeTextValue(NODE_NAME_SYMNAME, null);
            String sLiteral = critnode.getSafeTextValue(NODE_NAME_LITERAL, null);

            if (null == sSymname)
            {
                String msg = "OwStandardPriorityRule.scanSearchNode: Define symname in the PriorityRule criteria Node.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            if (null == sLiteral)
            {
                String msg = "OwStandardPriorityRule.scanSearchNode: Define literal in the PriorityRule criteria Node.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            OwFieldDefinition fielddefinition = fieldefinitionprovider_p.getFieldDefinition(sSymname, sResource_p);

            return new OwSearchNode(fielddefinition, iOP, fielddefinition.getValueFromString(sLiteral), 0);
        }

    }

    /** get the OwSearchNode with criteria that make up the rule
     * 
     * @return OwSearchNode
     */
    public OwSearchNode getRule()
    {
        return m_searchnode;
    }

    public final boolean appliesTo(OwObject object_p) throws OwInvalidOperationException
    {
        try
        {
            // get first criteria node, currently we only support one
            // TODO: support a search node tree

            OwSearchCriteria criteria = ((OwSearchNode) m_searchnode.getChilds().get(0)).getCriteria();

            OwProperty prop;
            prop = object_p.getProperty(criteria.getClassName());

            // TODO: mind operator

            // return first match
            return criteria.getValue().equals(prop.getValue());
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not process property rule!", e);
        }
    }

}