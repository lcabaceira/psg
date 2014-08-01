package com.wewebu.ow.server.ecmimpl.fncm5;

import com.wewebu.ow.server.ao.OwSearchTemplateFactory;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5SearchTemplate;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchTemplate;

/**
 *<p>
 * FileNet BPM search template factory.
 * Creates {@link OwFNBPM5SearchTemplate} search template instances based on 
 * search template XML application objects.
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

public class OwFNBPM5SearchTemplateFactory implements OwSearchTemplateFactory
{
    private OwNetworkContext context;

    public OwFNBPM5SearchTemplateFactory(OwNetworkContext context)
    {
        super();
        this.context = context;
    }

    public OwSearchTemplate createSearchTemplate(OwObject obj_p) throws OwException
    {
        try
        {
            return new OwFNBPM5SearchTemplate(context, obj_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not create search template", e);
        }
    }

}
