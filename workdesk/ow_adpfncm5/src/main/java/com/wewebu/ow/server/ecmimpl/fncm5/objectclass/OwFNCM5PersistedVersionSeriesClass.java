package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.filenet.api.core.VersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5PersistedVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5SoftObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Content-object class of the {@link VersionSeries} AWD object representations. 
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
public class OwFNCM5PersistedVersionSeriesClass extends OwFNCM5IndependentlyPersistableObjectClass<VersionSeries, OwFNCM5ObjectStoreResource>
{

    public OwFNCM5PersistedVersionSeriesClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    @Override
    public OwFNCM5Object<VersionSeries> from(VersionSeries nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5PersistedVersionSeries versionSeries = factory_p.create(OwFNCM5PersistedVersionSeries.class, new Class[] { VersionSeries.class, OwFNCM5PersistedVersionSeriesClass.class }, new Object[] { nativeObject_p, this });
        return OwFNCM5SoftObject.asSoftObject(versionSeries);
    }

}
