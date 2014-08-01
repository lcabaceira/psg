package com.wewebu.ow.server.plug.owdocprops;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwExpressionPriorityRule;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A simple expression based handler for EditProperties.
 * Based on a define expression it will verify the modifiability of a view.  
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
public class OwExpressionModifiabilityHandler implements OwEditPropertiesModifiabilityHandler
{
    private static final Logger LOG = OwLog.getLogger(OwExpressionModifiabilityHandler.class);

    private OwExpressionPriorityRule expRule;

    @Override
    public void init(OwAppContext appContext, OwXMLUtil handlerConfigNode) throws OwException
    {
        List<?> params = handlerConfigNode.getSafeUtilList(null);
        Iterator<?> it = params.iterator();
        List<?> cdata = null;
        while (it.hasNext())
        {
            OwXMLUtil param = (OwXMLUtil) it.next();
            if ("rule".equals(param.getSafeStringAttributeValue("name", null)))
            {
                cdata = param.getSafeCDATAList();
                break;
            }
        }

        if (cdata == null || cdata.isEmpty())
        {
            throw new OwConfigurationException(appContext.localize("OwExpressionModifiabilityHandler.init.err.conf", "Specify a rule using the paramEnc element."));
        }

        expRule = OwExpressionPriorityRule.newPriorityRule("", cdata.get(0).toString());
    }

    @Override
    public boolean isModifiable(int viewMask, OwObject item)
    {
        if (viewMask == OwEditPropertiesDialog.VIEW_MASK_PROPERTIES)
        {
            try
            {
                return expRule.appliesTo(item);
            }
            catch (OwInvalidOperationException e)
            {
                LOG.warn("Could not process expression");
            }
        }
        return true;
    }
}
