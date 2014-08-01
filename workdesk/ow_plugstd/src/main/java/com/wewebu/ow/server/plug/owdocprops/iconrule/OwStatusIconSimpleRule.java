package com.wewebu.ow.server.plug.owdocprops.iconrule;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A simple Rule implementation, working only with single Boolean property. 
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
 *@since 4.2.0.0
 */
public class OwStatusIconSimpleRule extends OwStatusIconAbstractRule
{
    public static final Logger LOG = OwLog.getLogger(OwStatusIconSimpleRule.class);

    /** property name to get status from */
    String m_property;
    /** the value string to match */
    String m_value;

    public OwStatusIconSimpleRule(OwXMLUtil definitionNode, OwAppContext context) throws OwException
    {
        super(definitionNode, context);

        String rule = definitionNode.getSafeTextValue(null);
        if (rule == null)
        {
            throw new OwConfigurationException("Please specify a rule.");
        }

        int iEqual = rule.indexOf("=");
        if (-1 == iEqual)
        {
            throw new OwConfigurationException("Please specify a '=' in the rule.");
        }

        // get property name
        m_property = rule.substring(0, iEqual);
        m_property = m_property.trim();
        if (getDescription() == null)
        {
            setDescription(m_property);
        }

        // get value string to match
        m_value = rule.substring(iEqual + 1);
        m_value = m_value.trim();

        if (m_value.equals("OW_NULL"))
        {
            m_value = null;
        }

    }

    @Override
    public boolean canApply(OwObject obj, OwObject parentObj) throws OwException
    {
        try
        {
            OwProperty prop = obj.getProperty(m_property);

            Object value = prop.getValue();
            if (value == null)
            {
                return (m_value == null);
            }
            else if (m_value != null)
            {
                return value.toString().equals(m_value);
            }
            else
            {
                return false;
            }
        }
        catch (OwObjectNotFoundException e)
        {
            return false;
        }
        catch (Exception e)
        {
            throw new OwServerException("Unlable to retrieve property value", e);
        }

    }
}
