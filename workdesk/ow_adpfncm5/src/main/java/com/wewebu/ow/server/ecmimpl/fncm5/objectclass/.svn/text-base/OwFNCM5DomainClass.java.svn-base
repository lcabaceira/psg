package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import org.apache.log4j.Logger;

import com.filenet.api.core.Domain;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5DomainResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Domain;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwFNCM5DomainClass.
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
public class OwFNCM5DomainClass extends OwFNCM5EngineObjectClass<Domain, OwFNCM5DomainResource>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5DomainClass.class);

    private OwFNCM5Domain instance;
    private Domain domain;

    public OwFNCM5DomainClass(Domain domain_p, OwFNCM5ResourceAccessor<OwFNCM5DomainResource> resourceAccessor_p)
    {
        super(OwFNCM5ClassDescription.from(domain_p, resourceAccessor_p), resourceAccessor_p);
        this.domain = domain_p;
    }

    @Override
    public synchronized OwFNCM5Domain from(Domain nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        if (this.domain.equals(nativeObject_p))
        {
            if (this.instance == null)
            {
                //                this.instance = new OwFNCM5Domain(nativeObject_p, this);

                this.instance = factory_p.create(OwFNCM5Domain.class, new Class[] { Domain.class, OwFNCM5DomainClass.class }, new Object[] { nativeObject_p, this });

            }
            return this.instance;
        }
        else
        {
            throw new OwInvalidOperationException("Domain object-classes can have exactly 1 instance !");
        }
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER;
    }
}
