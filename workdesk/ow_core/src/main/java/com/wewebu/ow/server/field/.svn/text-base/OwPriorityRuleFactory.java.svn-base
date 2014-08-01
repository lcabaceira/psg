package com.wewebu.ow.server.field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Singleton priority rules factory.<br/>
 * Used for multiple Priority Rules implementations and legacy support.<br/>
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
public class OwPriorityRuleFactory
{
    private static final String NODE_NAME_PRIORITY_RULES = "PriorityRules";

    /** Singleton instance */
    private static OwPriorityRuleFactory instance = null;

    /** Singleton instance accessor */
    public static synchronized OwPriorityRuleFactory getInstance()
    {
        if (instance == null)
        {
            instance = new OwPriorityRuleFactory();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private OwPriorityRuleFactory()
    {
        //void
    }

    public List<OwPriorityRule> createRulesList(OwXMLUtil configNode_p, OwFieldDefinitionProvider fieldefinitionprovider_p) throws Exception
    {
        List<OwPriorityRule> rulesList = new LinkedList<OwPriorityRule>();
        if (configNode_p.getSubNode(NODE_NAME_PRIORITY_RULES) != null)
        {
            // load priority rules
            Iterator<?> it = configNode_p.getSafeNodeList(NODE_NAME_PRIORITY_RULES).iterator();

            while (it.hasNext())
            {
                Node n = (Node) it.next();

                OwPriorityRule rule = createRule(n, fieldefinitionprovider_p);

                rulesList.add(rule);
            }
        }
        return rulesList;
    }

    /**
     * Multiple priority rule factory method.
     *  
     * @param configNode_p the priority rules configuration node 
     * @param fieldefinitionprovider_p 
     * @return a {@link Map} of priority lists of rule objects mapped by their container ID see {@link OwPriorityRule#getContainer()}
     * @throws Exception
     */
    public Map<String, List<OwPriorityRule>> createRulesContainerMap(OwXMLUtil configNode_p, OwFieldDefinitionProvider fieldefinitionprovider_p) throws Exception
    {
        Map<String, List<OwPriorityRule>> rulesMap = new HashMap<String, List<OwPriorityRule>>();
        if (configNode_p.getSubNode(NODE_NAME_PRIORITY_RULES) != null)
        {
            // load priority rules
            Iterator<?> it = configNode_p.getSafeNodeList(NODE_NAME_PRIORITY_RULES).iterator();

            while (it.hasNext())
            {
                Node n = (Node) it.next();

                OwPriorityRule rule = createRule(n, fieldefinitionprovider_p);

                // add rule to map
                List<OwPriorityRule> rulelist = rulesMap.get(rule.getContainer());
                if (rulelist == null)
                {
                    rulelist = new LinkedList<OwPriorityRule>();
                    rulesMap.put(rule.getContainer(), rulelist);
                }

                rulelist.add(rule);
            }
        }
        return rulesMap;
    }

    /**
     * Single priority rule factory method.
     *  
     * @param configNode_p the priority rule configuration node 
     * @param fieldefinitionprovider_p 
     * @return the newly created {@link OwPriorityRule}
     * @throws Exception
     */
    public OwPriorityRule createRule(Node configNode_p, OwFieldDefinitionProvider fieldefinitionprovider_p) throws Exception
    {
        OwXMLUtil prionode = new OwStandardXMLUtil(configNode_p);
        if (prionode.getSubNode(OwStandardPriorityRule.NODE_NAME_SEARCHNODE) != null)
        {
            OwStandardPriorityRule legacyRule = new OwStandardPriorityRule();
            legacyRule.init(configNode_p, fieldefinitionprovider_p);
            return legacyRule;
        }
        else if (prionode.getSubNode(OwExpressionPriorityRule.NODE_NAME_EXPRESSION) != null)
        {
            return OwExpressionPriorityRule.newPriorityRule(prionode);
        }

        throw new OwInvalidOperationException("Could not create priority rule ! Either " + OwStandardPriorityRule.NODE_NAME_SEARCHNODE + " or " + OwExpressionPriorityRule.NODE_NAME_EXPRESSION + " expected in the configuration XML!");

    }

}