package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.core.WorkflowDefinition;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5WorkflowDefinition;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Creates instances of {@link OwFNCM5WorkflowDefinition}.
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
public class OwFNCM5WorkflowDefinitionClass extends OwFNCM5DocumentClass<WorkflowDefinition>
{

    /**
     * @param declaration_p
     * @param resourceAccessor_p
     */
    public OwFNCM5WorkflowDefinitionClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5DocumentClass#from(com.filenet.api.core.Document, com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory)
     */
    @Override
    public OwFNCM5Object<WorkflowDefinition> from(WorkflowDefinition nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5WorkflowDefinition document = factory_p.create(OwFNCM5WorkflowDefinition.class, new Class[] { WorkflowDefinition.class, OwFNCM5WorkflowDefinitionClass.class }, new Object[] { nativeObject_p, this });
        return document;
    }

}
