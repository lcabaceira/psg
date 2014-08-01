package com.wewebu.ow.server.plug.owdocprops.iconrule;

import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwExpressionPriorityRule;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Icon rule based on OwExpressionPriorityRule handling.
 * Based on expression language, will return corresponding Icon representation.
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
public class OwStatusIconExpressionRule extends OwStatusIconAbstractRule
{
    private OwExpressionPriorityRule expRule;

    public OwStatusIconExpressionRule(OwXMLUtil config, OwAppContext context) throws OwException
    {
        super(config, context);

        List<?> cdata = config.getSafeCDATAList();
        if (cdata == null || cdata.isEmpty())
        {
            throw new OwConfigurationException("Please specify a rule, using correct CDATA element.");
        }

        expRule = OwExpressionPriorityRule.newPriorityRule(getIcon(), cdata.get(0).toString());
    }

    @Override
    public boolean canApply(OwObject obj, OwObject parentObj) throws OwException
    {
        return expRule.appliesTo(obj);
    }
}
