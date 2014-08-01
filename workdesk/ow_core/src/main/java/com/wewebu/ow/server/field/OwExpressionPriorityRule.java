package com.wewebu.ow.server.field;

import java.util.List;

import org.w3c.dom.Node;

import com.wewebu.expression.language.OwExprBooleanValue;
import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.language.OwExprExpressionType;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprType;
import com.wewebu.expression.language.OwExprTypeMissmatchException;
import com.wewebu.expression.language.OwExprValue;
import com.wewebu.expression.parser.OwExprParser;
import com.wewebu.expression.parser.ParseException;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Priority rule definition standard implementation (Expression Language based).<br/>
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
public class OwExpressionPriorityRule extends OwPriorityRuleBase
{
    public static final String NODE_NAME_EXPRESSION = "expression";

    /**
     * Instance creation helper.
     * Creates a new {@link OwExpressionPriorityRule} instance based on a configuration node 
     * @param configNode_p the priority rule configuration node (must contain exactly one CDATA defined  expressions)
     * @param fieldefinitionprovider_p
     * @return the new {@link OwExpressionPriorityRule} instance
     * @throws Exception if the instance creation failed (no CDATA definitions, more than one CDATA definitions,the WFLan expression failed at parse time)
     * @deprecated since 4.2.0.0 use {@link #newPriorityRule(OwXMLUtil)} instead 
     */
    public static OwExpressionPriorityRule newPiorityRule(Node configNode_p, OwFieldDefinitionProvider fieldefinitionprovider_p) throws Exception
    {
        return OwExpressionPriorityRule.newPriorityRule(new OwStandardXMLUtil(configNode_p));
    }

    /**
     * Instance creation helper.
     * Creates a new {@link OwExpressionPriorityRule} instance based on a configuration node 
     * @param config OwXMLUtil the priority rule configuration node (must contain exactly one CDATA defined  expressions)
     * @return the new {@link OwExpressionPriorityRule} instance
     * @throws OwException if the instance creation failed (no CDATA definitions, more than one CDATA definitions,the WFLan expression failed at parse time)
     * since 4.2.0.0 
     */
    public static OwExpressionPriorityRule newPriorityRule(OwXMLUtil config) throws OwException
    {
        String scontainer = config.getSafeStringAttributeValue(ATTRIBUTE_NAME_CONTAINER, null);
        String sResource = config.getSafeStringAttributeValue(ATTRIBUTE_NAME_RESOURCE, null);

        String sStyleClass = config.getSafeTextValue(NODE_NAME_COLOR, "owpriostyle");
        List<?> cdataExpressions = config.getSafeCDATAList(NODE_NAME_EXPRESSION);
        if (cdataExpressions.isEmpty())
        {
            throw new OwConfigurationException("Invalid Expression Priority rule config : no CDATA expressions found in the " + NODE_NAME_EXPRESSION + " element!");
        }
        if (cdataExpressions.size() > 1)
        {
            throw new OwConfigurationException("Invalid Expression Priority rule config : only one CDATA expression in the " + NODE_NAME_EXPRESSION + " element supported!");
        }
        String expressionString = (String) cdataExpressions.get(0);
        return new OwExpressionPriorityRule(scontainer, sResource, sStyleClass, expressionString);
    }

    /**
     * Simplified factory for OwExpressionPriorityRule instances.
     * @param styleClass String style class definition
     * @param expression String expression for the rule, should result into Boolean term
     * @return OwExpressionPriorityRule
     * @throws OwException
     * @since 4.2.0.0
     */
    public static OwExpressionPriorityRule newPriorityRule(String styleClass, String expression) throws OwException
    {
        if (styleClass == null || expression == null)
        {
            throw new OwInvalidOperationException("Null value provided, which is not allowed");
        }

        return new OwExpressionPriorityRule(null, null, styleClass, expression);
    }

    private OwExprExpression m_expression;

    /**
     * Constructor.
     * Clients should use {@link #newPriorityRule(OwXMLUtil)} for instance creation.
     * @param scontainer_p 
     * @param sresource_p
     * @param styleClass_p
     * @param expressionString_p
     * @throws OwException if the expression fails at parse time
     */
    private OwExpressionPriorityRule(String scontainer_p, String sresource_p, String styleClass_p, String expressionString_p) throws OwException
    {
        super(scontainer_p, sresource_p, styleClass_p);
        try
        {
            m_expression = OwExprParser.parse(expressionString_p);
            if (m_expression.hasErrors())
            {
                throw new OwInvalidOperationException("Could not create WF Lan priority rule for expression  " + expressionString_p + " : " + m_expression.getErrorTable());
            }
            OwExprExpressionType expressionType;
            try
            {
                expressionType = m_expression.type();
                if (!expressionType.canInfer(OwExprType.BOOLEAN))
                {
                    throw new OwInvalidOperationException("Could not create WF Lan priority rule for non boolean expression  " + expressionString_p);
                }
            }
            catch (OwExprTypeMissmatchException e)
            {
                throw new OwServerException("Cannot identify type of expression term", e);
            }
        }
        catch (ParseException e)
        {
            throw new OwInvalidOperationException("Could not create WF Lan priority rule for expression : " + expressionString_p, e);
        }
    }

    /**
     * 
     * @see OwPriorityRule#appliesTo(OwObject)
     */
    public final boolean appliesTo(OwObject object_p) throws OwInvalidOperationException
    {
        OwObjectScope objectScope = new OwObjectScope("object", object_p);
        try
        {
            OwExprExternalScope[] priorityScopes = new OwExprExternalScope[] { objectScope };
            if (m_expression.symbolsVisibleInScopes(priorityScopes))
            {
                OwExprValue value = m_expression.evaluate(priorityScopes);
                return OwExprBooleanValue.TRUE.equals(value);
            }
            else
            {
                return false;
            }
        }
        catch (OwExprEvaluationException e)
        {
            throw new OwInvalidOperationException("Could not evaluate WFLan expression : " + m_expression.toString(), e);
        }
    }

    public String toString()
    {
        return "PriorityRule : [" + m_expression + " => " + getStylClass() + "]";
    }
}